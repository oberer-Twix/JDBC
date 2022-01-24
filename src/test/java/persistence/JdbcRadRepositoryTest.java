package persistence;

import domain.Lager;
import domain.Rad;
import domain.RadGroesen;
import org.junit.jupiter.api.*;
import persistence.rad.JdbcRadRepository;
import persistence.rad.RadRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

public class JdbcRadRepositoryTest {
    private static Connection connection;
    private static RadRepository radRepository;
    private static Savepoint savepoint;

    @BeforeAll
    public static void start() throws SQLException, IOException {
        connection = ConnectionManager.getInstance();
        var sb = new StringBuilder();
        try (var lines = Files.lines(Path.of("src/main/resources/TestData.sql"))) {
            lines.forEach(sb::append);
        }
        try (var statement = connection.prepareStatement(sb.toString())) {
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Hat nicht funktioniert");
            }
        }
        connection.setAutoCommit(false);
        radRepository = new JdbcRadRepository(connection);
    }

    @BeforeEach
    public void prepare() throws SQLException {
        savepoint = connection.setSavepoint();
    }

    @AfterEach
    public void undo() throws SQLException {
        connection.rollback(savepoint);
    }

    @AfterAll
    public static void end() throws SQLException {
        connection.setAutoCommit(true);
        connection.close();
    }

    @Nested
    class findByID {
        @Test
        public void id_is_zero() throws SQLException {
            assertThat(radRepository.findByID(0)).isEqualTo(Optional.empty());

        }

        @Test
        public void id_is_a_very_big_number() throws SQLException {
            assertThat(radRepository.findByID(99_999_999)).isEqualTo(Optional.empty());
        }

        @Test
        public void works() throws SQLException {
            Lager lager = new Lager(4, "Lager");
            var rad = new Rad(1, "rider", 'M', "KTM", 1300, lager);
            Optional<Rad> optionalRad = radRepository.findByID(1);
            optionalRad.ifPresent(value -> assertThat(value).isEqualTo(rad));
        }
    }

    @Nested
    class findAll {
        @Test
        public void works() throws SQLException {
            assertThat(radRepository.findAll())
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        }
    }

    @Nested
    class findAllInLagerWithID {
        @Test
        public void id_is_zero() throws SQLException {
            assertThat(radRepository.findAllInLagerWithID(0)).isEqualTo(List.of());
        }

        @Test
        public void id_is_very_big() throws SQLException {
            assertThat(radRepository.findAllInLagerWithID(99_999_999)).isEqualTo(List.of());
        }

        @Test
        public void works() throws SQLException {
            var lager = new Lager(1, "name");
            var rad = List.of(new Rad(1, "name", 'M', "marke", 1234, lager));
            assertThat(radRepository.findAllInLagerWithID(4)).isEqualTo(rad);
        }
    }

    @Nested
    class findAllInLAger {
        @Test
        public void works() throws SQLException {
            assertThat(radRepository.findAllInLager())
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        }
    }

    @Nested
    class findAllFromBrand {
        @Test
        public void marke_is_empty() throws SQLException {
            assertThat(radRepository.findAllFromBrand("")).isEqualTo(List.of());
        }

        @Test
        public void works() throws SQLException {
            assertThat(radRepository.findAllFromBrand("KTM"))
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(1);
        }
    }

    @Nested
    class findAllWithGroese {
        @Test
        public void works() throws SQLException {
            assertThat(radRepository.findAllWithGroesen(RadGroesen.SMALL))
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(2);
        }
    }

    @Nested
    class save {
        @Test
        public void rad_with_id() {
            var lager = new Lager(1, "name");
            var rad = new Rad(1, "name", 'M', "marke", 1234, lager);
            assertThatThrownBy(() -> radRepository.save(rad));
        }

        @Test
        public void works() throws SQLException {
            var lager = new Lager(1, "name");
            var rad = new Rad("name", 'M', "marke", 1234, lager);
            var rad1 = radRepository.save(rad);
            assertThat(rad1.getRadID()).isNotNull();
            assertThat(radRepository.findByID(rad1.getRadID()).orElseThrow()).isEqualTo(rad1);
        }
    }

    @Nested
    class delete {
        @Test
        public void delete_not_valid_id() {
            assertThatThrownBy(() -> radRepository.delete(0));
        }

        @Test
        public void works() throws SQLException {
            radRepository.delete(1);
            assertThat(radRepository.findAll())
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(2, 3, 4, 5);
        }
    }

    @Nested
    class storage {
        @Test
        public void take_Rad_out_of_Lager() throws SQLException {
            var rad = new Rad(1, "name", 'M', "marke", 1234, null);
            radRepository.withdraw(rad.getRadID());
            assertThat(radRepository.findAllInLager())
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(2, 3, 4, 5);
        }

        @Test
        public void put_Rad_in_Lager() throws SQLException {
            take_Rad_out_of_Lager();
            var lager = new Lager(1, "name");
            var rad = new Rad(1, "name", 'M', "marke", 1234, lager);
            radRepository.store(lager, rad.getRadID());
            assertThat(radRepository.findAllInLager())
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        }

        @Test
        public void get_all_Raeder_in_Lager() throws SQLException {
            assertThat(radRepository.findAllInLager())
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        }

        @Test
        public void get_Raeder_in_Lager_With_ID() throws SQLException {
            assertThat(radRepository.findAllInLagerWithID(4))
                    .extracting(Rad::getRadID)
                    .containsExactlyInAnyOrder(1);
        }

        @Test
        public void get_anzahl_Raeder_in_Lager() throws SQLException {
            assertThat(radRepository.getAnzahlRaederInLager(4))
                    .isEqualTo(1);
        }

        @Test
        public void get_anzahl_Raeder_in_Lager_with_wrong_id() throws SQLException {
            assertThat(radRepository.getAnzahlRaederInLager(0)).isEqualTo(0);
        }

        @Test
        void throws_Exception_on_withdraw_with_not_known_ID(){
            assertThatThrownBy(() -> radRepository.withdraw(10));
        }

        @Test
        void throws_Exception_on_store_with_not_known_ID() {
            assertThatThrownBy(() -> radRepository.store(new Lager(1, "lager"), 100));
        }
    }
}

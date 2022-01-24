package domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class RadTest {

    Lager lager = new Lager(1, "name");

    @Nested
    class Constructor{

        @Test
        public void empty_name(){
            assertThatThrownBy(() -> new Rad(" ", 'M', "marke", 1234, lager));
        }

        @Test
        public void empty_groese(){
            assertThatThrownBy(() -> new Rad("name", ' ', "marke", 1234, lager));
        }

        @Test
        public void empty_Marke(){
            assertThatThrownBy(() -> new Rad("name", 'M', "", 1234, lager));
        }

        @Test
        public void negativ_Price(){
            assertThatThrownBy(() -> new Rad("name", 'M', "Marke", -12, lager));
        }

        @Test
        public void unknown_Groese(){
            assertThatThrownBy(() -> new Rad("name", 'E', "Marke", 1234, lager));
        }

        @Test
        public void to_long_String_as_Groese(){
            assertThatThrownBy(() -> new Rad(1,"name", "Lange", "Marke", 1234, lager));
        }

        @Test
        public void is_working_correct_with_String(){
            var rad1 = new Rad(1, "name", 'M', "Marke", 1234, lager);
            var rad2 = new Rad(1, "name", "M", "Marke", 1234, lager);
            assertThat(rad2).hasSameHashCodeAs(rad1);
            assertThat(rad2).isEqualTo(rad1);
        }
    }
    @Test
    void toString_works(){
        var rad = new Rad(1, "name", "M", "KTM", 1234, lager);
        assertThat(rad.toString()).isEqualTo("Rad(radID=1, radName=name, radGroese=MEDIUM, radMarke=KTM, radKaufpreis=1234, radLager=name)");
    }

    @Nested
    class Setter{

        Rad rad = new Rad(1, "name", "M", "KTM", 1234, lager);

        @Test
        void name_Setter() {
            String newName = "newName";
            rad.setRadName(newName);
            assertThat(rad.getRadName()).isEqualTo(newName);
        }

        @Test
        void Groese_Setter() {
            var newGroese = RadGroesen.SMALL;
            rad.setRadGroese(newGroese);
            assertThat(rad.getRadGroese()).isEqualTo(newGroese);
        }

        @Test
        void marke_Setter() {
            String newMarke = "Trek";
            rad.setRadMarke(newMarke);
            assertThat(rad.getRadMarke()).isEqualTo(newMarke);
        }

        @Test
        void price_Setter() {
            int newPrice = 1111;
            rad.setRadKaufpreis(newPrice);
            assertThat(rad.getRadKaufpreis()).isEqualTo(newPrice);
        }

        @Test
        void lager_Setter() {
            var newLager = new Lager(1, "lager");
            rad.setRadLager(newLager);
            assertThat(rad.getRadLager()).isEqualTo(newLager);
        }
    }
}

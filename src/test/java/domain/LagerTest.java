package domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LagerTest {


    @Test
    public void empty_name_exeption(){
        assertThatThrownBy(() -> new Lager(null, ""));
    }

    @Test
    public void name_only(){
        var lager1 = new Lager(null, "name");
        var lager2 = new Lager( "name");
        assertThat(lager1).hasSameHashCodeAs(lager2);
        assertThat(lager1).isEqualTo(lager2);
    }

    @Test
    void name_Setter() {
        var lager = new Lager( "name");
        String newName = "newName";
        lager.setLagerName(newName);
        assertThat(lager.getLagerName()).isEqualTo(newName);
    }
}

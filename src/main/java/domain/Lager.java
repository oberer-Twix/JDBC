package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter

@EqualsAndHashCode
public class Lager {
    private final Integer lagerID;
    @Setter
    private String lagerName;

    public Lager(Integer lagerID, String lagerName) {
        if(lagerName.isBlank())
            throw new IllegalArgumentException("Lager hat keinen Namen");

        this.lagerID = lagerID;
        this.lagerName = lagerName;
    }

    public Lager(String lagerName) {
        this(null, lagerName);
    }

    @Override
    public String toString() {
        return this.lagerName;
    }
}

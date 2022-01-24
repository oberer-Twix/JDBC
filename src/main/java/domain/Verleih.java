package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
public class Verleih {
    private final Integer verleihID;
    @Setter
    private int verleihDauer;
    @Setter
    private int verleihPreis;
    @Setter
    private LocalDate verleihAnfangsdatum;
    @Setter
    private String verleihZusatztools;
    @Setter
    private Kunde verleihKunde;
    @Setter
    private Mitarbeiter verleihMitarbeiter;

    public Verleih(Integer verleihID, int verleihDauer, int verleihPreis, LocalDate verleihAnfangsdatum, String verleihZusatztools, Kunde verleihKunde, Mitarbeiter verleihMitarbeiter) {
        if(verleihDauer < 0){
            throw new IllegalArgumentException("Ausleihdauer ist nicht möglich");
        }
        if(verleihPreis < 0){
            throw new IllegalArgumentException("Preis ist nicht möglich");
        }
        if(verleihAnfangsdatum == null){
            throw new IllegalArgumentException("Datum ist undefiniert");
        }
        if(verleihKunde == null){
            throw new IllegalArgumentException("Kein Kunde");
        }
        if(verleihMitarbeiter == null){
            throw new IllegalArgumentException("Kein Mitarbeiter");
        }

        this.verleihID = verleihID;
        this.verleihDauer = verleihDauer;
        this.verleihPreis = verleihPreis;
        this.verleihAnfangsdatum = verleihAnfangsdatum;
        this.verleihZusatztools = verleihZusatztools;
        this.verleihKunde = verleihKunde;
        this.verleihMitarbeiter = verleihMitarbeiter;
    }

    public Verleih(int verleihDauer, int verleihPreis, LocalDate verleihAnfangsdatum, String verleihZusatztools, Kunde verleihKunde, Mitarbeiter verleihMitarbeiter){
        this(null, verleihDauer, verleihPreis, verleihAnfangsdatum, verleihZusatztools, verleihKunde, verleihMitarbeiter);
    }
}

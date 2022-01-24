package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Rad {
    private final Integer radID;
    @Setter
    private String radName;
    @Setter
    private RadGroesen radGroese;
    @Setter
    private String radMarke;
    @Setter
    private int radKaufpreis;
    @Setter
    private Lager radLager;


    public Rad(Integer radID, String radName, char radGroese, String radMarke, int radKaufpreis, Lager radLager) {
        if(radName.isBlank()){
            throw new IllegalArgumentException("Name des Rades ist unbekannt");
        }
        if(radMarke.isBlank()) {
            throw new IllegalArgumentException("Marke des Rades ist Unbekannt");
        }
        if(radKaufpreis < 0) {
            throw new IllegalArgumentException("Rad hat keinen gültigen Kaufpreis");
        }

        this.radGroese = switch (radGroese) {
            case 'S' -> RadGroesen.SMALL;
            case 'M' -> RadGroesen.MEDIUM;
            case 'L' -> RadGroesen.LARGE;
            default -> throw new IllegalArgumentException("Keine korrekte Größe gefunden");
        };
        this.radID = radID;
        this.radName = radName;
        this.radMarke = radMarke;
        this.radKaufpreis = radKaufpreis;
        this.radLager = radLager;
    }

    public Rad(String radName, char radGroese, String radMarke, int radKaufpreis, Lager radLager){
        this(null, radName, radGroese, radMarke, radKaufpreis, radLager);
    }

    public Rad(Integer radID, String radName, String radGroese, String radMarke, int radKaufpreis, Lager radLager) {
        this(radID, radName, getCorrectCharForGroese(radGroese), radMarke, radKaufpreis, radLager);
    }

    private static char getCorrectCharForGroese(String radGroese) {
        if (radGroese.length() != 1){
            throw new IllegalArgumentException("String für Groese zu lang");
        }
        return radGroese.charAt(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rad rad = (Rad) o;

        return radID != null ? radID.equals(rad.radID) : rad.radID == null;
    }

    @Override
    public int hashCode() {
        return radID != null ? radID.hashCode() : 0;
    }
}

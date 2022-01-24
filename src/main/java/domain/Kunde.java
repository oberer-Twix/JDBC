package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class Kunde {
    private final Integer kundenID;
    @Setter
    private String kundenName;
    @Setter
    private String kundenAdresse;
    @Setter
    private String kundenTelephonnummer;
    @Setter
    private int schaeden;

    public Kunde(Integer kundenID, String kundenName, String kundenAdresse, String kundenTelephonnummer, int schaeden) {
        if(kundenName.isBlank())
            throw new IllegalArgumentException("Name des Kunden ist leer");

        if(kundenAdresse.isBlank())
            throw new IllegalArgumentException("Adresse des Kunden ist leer");

        if(kundenTelephonnummer.isBlank())
            throw new IllegalArgumentException("Telephonnummer des Kunden ist leer");

        if (schaeden > 5 || schaeden < 1)
            throw new IllegalArgumentException("Schäden des Kunden sind nicht korrekt");

        this.kundenID = kundenID;
        this.kundenName = kundenName;
        this.kundenAdresse = kundenAdresse;
        this.kundenTelephonnummer = kundenTelephonnummer;
        this.schaeden = schaeden;
    }

    public Kunde(String kundenName, String kundenAdresse, String kundenTelephonnummer, int schaeden) {
        this(null, kundenName, kundenAdresse, kundenTelephonnummer, schaeden);
    }

    @Override
    public String toString() {
        return this.kundenName;
    }
}

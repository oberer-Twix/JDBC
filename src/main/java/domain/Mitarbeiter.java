package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class Mitarbeiter {
    private final String mitarbeiterName;
    @Setter
    private String handynummer;

    public Mitarbeiter(String mitarbeiterName, String handynummer) {
        if(handynummer.isBlank())
            throw new IllegalArgumentException("Mitarbeiter hat keine Handynummer");

        this.mitarbeiterName = mitarbeiterName;
        this.handynummer = handynummer;
    }

    public Mitarbeiter(String handynummer){
        this(null, handynummer);
    }
}

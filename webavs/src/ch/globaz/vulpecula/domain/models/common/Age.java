package ch.globaz.vulpecula.domain.models.common;

/***
 * ValueObject représentant un nombre à virgule
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 déc. 2013
 */
public class Age implements ValueObject {
    private static final long serialVersionUID = -4118150332952622247L;

    private int currency;

    public Age(int value) {
        currency = value;
    }

    public Age(String value) {
        currency = Double.valueOf(value).intValue();
    }

    public int getValue() {
        return currency;
    }
}

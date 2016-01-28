package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 22 janv. 2014
 * 
 */
public class OccupationGSON implements Serializable {
    public double taux;
    public String dateValidite;

    public Occupation convertToDomain() {
        Occupation occupation = new Occupation();
        occupation.setTaux(new Taux((int) taux));
        occupation.setDateValidite(new Date(dateValidite));
        return occupation;
    }
}

package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;

/**
 * @author Arnaud Geiser (AGE) | Créé le 21 janv. 2014
 * 
 */
public class TravailleurGSON implements Serializable {
    public Long id;

    public Travailleur convertToDomain() {
        Travailleur travailleur = new Travailleur();
        travailleur.setId(String.valueOf(id));
        return travailleur;
    }
}

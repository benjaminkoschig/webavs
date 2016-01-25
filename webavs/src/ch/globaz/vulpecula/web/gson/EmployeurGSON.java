package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

/**
 * @author Arnaud Geiser (AGE) | Créé le 21 janv. 2014
 * 
 */
public class EmployeurGSON implements Serializable {
    public long id;

    public Employeur convertToDomain() {
        Employeur employeur = new Employeur();
        employeur.setId(String.valueOf(id));
        return employeur;
    }
}

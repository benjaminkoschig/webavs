package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class AdhesionCotisationPosteTravailGSON implements Serializable {
    private static final long serialVersionUID = 2351703750500871852L;

    public String id;
    public String idCotisation;
    public String dateEntree;
    public String dateSortie;
    public String spy;
    public String idAssurance;

    public AdhesionCotisationPosteTravail convertToDomain() {
        AdhesionCotisationPosteTravail adhesionCaissePosteTravail = new AdhesionCotisationPosteTravail();
        adhesionCaissePosteTravail.setId(id);
        Cotisation cotisation = new Cotisation();
        cotisation.setId(idCotisation);
        Assurance assurance = new Assurance();
        assurance.setId(idAssurance);
        cotisation.setAssurance(assurance);
        adhesionCaissePosteTravail.setCotisation(cotisation);
        adhesionCaissePosteTravail.setPeriode(new Periode(dateEntree, dateSortie));
        adhesionCaissePosteTravail.setSpy(spy);
        return adhesionCaissePosteTravail;
    }
}

package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;

public class AdhesionCotisationPosteTravailGSON implements Serializable {
    private static final long serialVersionUID = 2351703750500871852L;

    public String id;
    public String idCotisation;
    public String dateEntree;
    public String dateSortie;
    public String spy;

    public AdhesionCotisationPosteTravail convertToDomain() {
        AdhesionCotisationPosteTravail adhesionCaissePosteTravail = new AdhesionCotisationPosteTravail();
        adhesionCaissePosteTravail.setId(id);
        Cotisation cotisation = new Cotisation();
        cotisation.setId(idCotisation);
        adhesionCaissePosteTravail.setCotisation(cotisation);
        adhesionCaissePosteTravail.setPeriode(new Periode(dateEntree, dateSortie));
        adhesionCaissePosteTravail.setSpy(spy);
        return adhesionCaissePosteTravail;
    }
}

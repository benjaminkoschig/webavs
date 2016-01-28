package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailAdhesionChevauchantesSpecification extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(PosteTravail posteTravail) {
        Map<String, List<Periode>> periodesParCotisation = new HashMap<String, List<Periode>>();
        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : posteTravail.getAdhesionsCotisations()) {
            String idCotisation = adhesionCotisationPosteTravail.getIdCotisation();
            Periode periode = adhesionCotisationPosteTravail.getPeriode();
            if (!periodesParCotisation.containsKey(idCotisation)) {
                periodesParCotisation.put(idCotisation, new ArrayList<Periode>());
            }
            periodesParCotisation.get(idCotisation).add(periode);
        }

        for (Map.Entry<String, List<Periode>> entry : periodesParCotisation.entrySet()) {
            if (!Periode.seChevauchent(entry.getValue()).isEmpty()) {
                // A désactiver pour la reprise
                addMessage(SpecificationMessage.POSTE_TRAVAIL_COTISATIONS_CHEVAUCHANTES);
            }
        }
        return true;
    }
}

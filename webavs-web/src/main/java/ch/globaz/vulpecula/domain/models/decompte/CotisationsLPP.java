package ch.globaz.vulpecula.domain.models.decompte;

import java.util.Collection;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Container permettant de gérer les cotisations LPP
 */
public class CotisationsLPP {
    private final Collection<Cotisation> cotisations;
    private final Collection<String> priorites;

    public CotisationsLPP(Collection<Cotisation> cotisations, Collection<String> priorites) {
        this.cotisations = filterLPP(cotisations);
        this.priorites = priorites;
    }

    /**
     * Retourne la cotisation pour un décompte CPP.
     * Il s'agit par défaut de la cotisation LPP standard, que la majorité des employeurs possèdent.
     * 
     * @return Cotisation LPP
     */
    public Optional<Cotisation> getCotisationPourDecompteCPP() {
        for (String priorite : priorites) {
            Collection<Cotisation> prioritaires = filterParPriorite(priorite, cotisations);
            if (!prioritaires.isEmpty()) {
                return Optional.of(prioritaires.iterator().next());
            }
        }
        return Optional.absent();
    }

    private Collection<Cotisation> filterParPriorite(final String priorite, Collection<Cotisation> cotisations) {
        return Collections2.filter(cotisations, new Predicate<Cotisation>() {
            @Override
            public boolean apply(Cotisation cotisation) {
                return priorite.equals(cotisation.getAssuranceId());
            }
        });
    }

    private Collection<Cotisation> filterLPP(Collection<Cotisation> cotisations) {
        return Collections2.filter(cotisations, new Predicate<Cotisation>() {
            @Override
            public boolean apply(Cotisation cotisation) {
                return cotisation.isAssuranceLPP();
            }
        });
    }
}

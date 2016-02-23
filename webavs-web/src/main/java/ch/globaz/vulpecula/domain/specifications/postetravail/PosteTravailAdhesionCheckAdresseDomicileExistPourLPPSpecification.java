package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;

public class PosteTravailAdhesionCheckAdresseDomicileExistPourLPPSpecification extends
        AbstractSpecification<PosteTravail> {
    private Adresse adresseDomicile;

    public static final List<String> ASSURANCES_LPP;

    static {
        ASSURANCES_LPP = new ArrayList<String>();
        ASSURANCES_LPP.add("231");
        ASSURANCES_LPP.add("232");
        ASSURANCES_LPP.add("233");
        ASSURANCES_LPP.add("234");
        ASSURANCES_LPP.add("235");
        ASSURANCES_LPP.add("236");
    }

    public PosteTravailAdhesionCheckAdresseDomicileExistPourLPPSpecification(Adresse adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }

    @Override
    public boolean isValid(PosteTravail posteTravail) {
        for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : posteTravail.getAdhesionsCotisations()) {
            if (adresseDomicile == null
                    && ASSURANCES_LPP.contains(adhesionCotisationPosteTravail.getAssurance().getId())) {
                addMessage(SpecificationMessage.LPP_SANS_ADRESSE_DOMICILE);
            }

        }

        return true;
    }
}

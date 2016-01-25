package globaz.vulpecula.vb.association;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;

public class PTAssociationAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = 2903043860223182595L;

    private String idEmployeur;
    private Map<AssociationGenre, List<AssociationCotisation>> cotisationsParAssociation;

    @Override
    public void retrieve() throws Exception {
        cotisationsParAssociation = VulpeculaServiceLocator.getAssociationCotisationService()
                .getCotisationByAssociation(idEmployeur);
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public Map<AssociationGenre, List<AssociationCotisation>> getCotisationsParAssociation() {
        return cotisationsParAssociation;
    }

    public void setCotisationsParAssociation(
            Map<AssociationGenre, List<AssociationCotisation>> cotisationsParAssociation) {
        this.cotisationsParAssociation = cotisationsParAssociation;
    }
}

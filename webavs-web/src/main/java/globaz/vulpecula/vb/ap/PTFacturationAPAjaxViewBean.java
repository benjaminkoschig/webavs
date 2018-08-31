package globaz.vulpecula.vb.ap;

import globaz.globall.db.BSessionUtil;
import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.association.EnteteFactureAssociation;
import ch.globaz.vulpecula.domain.models.association.FacturesAssociations;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTFacturationAPAjaxViewBean extends JadeAbstractAjaxFindForDomain<EnteteFactureAssociation> {
    private static final long serialVersionUID = 6945360872073577082L;

    private String idEmployeur;

    private FacturesAssociations factures;

    public void setIdEmployeur(final String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    @Override
    public EnteteFactureAssociation getEntity() {
        return new EnteteFactureAssociation();
    }

    @Override
    public Repository<EnteteFactureAssociation> getRepository() {
        return VulpeculaRepositoryLocator.getEnteteFactureRepository();
    }

    @Override
    public List<EnteteFactureAssociation> findByRepository() {
        return VulpeculaRepositoryLocator.getEnteteFactureRepository().findByIdEmployeurWithDependencies(idEmployeur);
    }

    @Override
    public void retrieve() throws Exception {
        factures = VulpeculaRepositoryLocator.getFactureAssociationRepository().findByIdEmployeur(idEmployeur);
    }

    public String getLabelReductionAssociation() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("FACTURATION_AP_REDUCTION_ASSOCITAION");
    }

    public FacturesAssociations getFactures() {
        return factures;
    }
}

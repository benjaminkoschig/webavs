package globaz.vulpecula.vb.decompte;

import java.util.List;
import ch.globaz.common.vb.JadeAbstractAjaxFindForDomain;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.Repository;

public class PTImprimerTOAjaxViewBean extends JadeAbstractAjaxFindForDomain<TaxationOffice> {
    private static final long serialVersionUID = -2745463043855968910L;

    private String idPassage;
    private EtatTaxation etatTaxation;
    private String noAffilie;

    @Override
    public TaxationOffice getEntity() {
        return new TaxationOffice();
    }

    @Override
    public Repository<TaxationOffice> getRepository() {
        return VulpeculaRepositoryLocator.getTaxationOfficeRepository();
    }

    @Override
    public List<TaxationOffice> findByRepository() {
        return VulpeculaRepositoryLocator.getTaxationOfficeRepository().findBy(idPassage, noAffilie, etatTaxation);

    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setEtatTaxation(String etatTaxation) {
        if (etatTaxation.isEmpty()) {
            return;
        }
        this.etatTaxation = EtatTaxation.fromValue(etatTaxation);
    }

    public String getNoAffilie() {
        return noAffilie;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }
}

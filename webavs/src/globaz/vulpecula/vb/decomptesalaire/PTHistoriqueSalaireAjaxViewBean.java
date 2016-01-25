package globaz.vulpecula.vb.decomptesalaire;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

public class PTHistoriqueSalaireAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = -8172279262263672649L;

    private String idPosteTravail;
    private List<DecompteSalaire> historiqueSalaire;

    @Override
    public void retrieve() throws Exception {
        historiqueSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdPosteTravailOneYearAgo(
                idPosteTravail);
    }

    /**
     * @return the historiqueSalaire
     */
    public List<DecompteSalaire> getHistoriqueSalaire() {
        return historiqueSalaire;
    }

    /**
     * @param idPosteTravail the idPosteTravail to set
     */
    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

}

package globaz.vulpecula.vb.congepaye;

import globaz.vulpecula.vb.PTAjaxDisplayViewBean;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.congepaye.LigneCompteur;

public class PTCompteursAjaxViewBean extends PTAjaxDisplayViewBean {
    private static final long serialVersionUID = 1L;

    private String idCompteur;

    private List<LigneCompteur> lignes;

    @Override
    public void retrieve() throws Exception {
        lignes = VulpeculaServiceLocator.getCompteurService().findByIdCompteur(idCompteur);
    }

    public List<LigneCompteur> getLignes() {
        return lignes;
    }

    public String getIdCompteur() {
        return idCompteur;
    }

    public void setIdCompteur(String idCompteur) {
        this.idCompteur = idCompteur;
    }
}

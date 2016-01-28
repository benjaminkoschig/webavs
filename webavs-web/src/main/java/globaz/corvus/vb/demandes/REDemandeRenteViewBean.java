/*
 * Créé le 29 dec. 06
 */
package globaz.corvus.vb.demandes;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author bsc
 */
public class REDemandeRenteViewBean extends REDemandeRente implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Renseignée lors de la copie d'une demande de rente API ou AI.
    private String idDemandeRenteCopiee = null;

    public String getIdDemandeRenteCopiee() {
        return idDemandeRenteCopiee;
    }

    public void setIdDemandeRenteCopiee(String idDemandeRenteCopiee) {
        this.idDemandeRenteCopiee = idDemandeRenteCopiee;
    }

}

package globaz.corvus.vb.annonces;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.framework.bean.FWViewBeanInterface;

public class REAnnoncesDiminution10EmeViewBean extends REAnnoncesDiminution10Eme implements FWViewBeanInterface {

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * getter pour l'attribut modifier permis.
     * 
     * @return la valeur courante de l'attribut modifier permis
     */
    public boolean isModifierPermis() {
        return IREAnnonces.CS_ETAT_OUVERT.equals(getEtat());
    }

    /**
     * getter pour l'attribut supprimer permis.
     * 
     * @return la valeur courante de l'attribut supprimer permis
     */
    public boolean isSupprimerPermis() {
        return IREAnnonces.CS_ETAT_OUVERT.equals(getEtat());
    }

}

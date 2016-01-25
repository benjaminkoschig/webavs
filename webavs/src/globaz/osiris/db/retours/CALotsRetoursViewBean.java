package globaz.osiris.db.retours;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.comptes.CARubrique;

public class CALotsRetoursViewBean extends CALotsRetours implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CARubrique rubrique;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Chargement de la Rubrique ou Compte Insérez la description de la méthode ici. Date de création : (30.01.2002
     * 07:44:40)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    public CARubrique getCompte() {
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdCompteFinancier());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }

        return rubrique;
    }

    /**
     * Donne le libelle de l'etat du lot
     * 
     * @return
     */
    public String getCsEtatLotLibelle() {
        return getSession().getCodeLibelle(super.getCsEtatLot());
    }

    /**
     * Vrais si le lot peut etre modifie
     * 
     * @return
     */
    public boolean isModifiable() {
        return !CALotsRetours.CS_ETAT_LOT_LIQUIDE.equals(getCsEtatLot());
    }

    /**
     * Vrais si le lot peut etre supprime
     * 
     * @return
     */
    public boolean isSupprimable() {
        return CALotsRetours.CS_ETAT_LOT_OUVERT.equals(getCsEtatLot());
    }

}

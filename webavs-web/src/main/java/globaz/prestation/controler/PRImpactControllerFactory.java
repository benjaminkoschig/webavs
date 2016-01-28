package globaz.prestation.controler;

import globaz.prestation.controler.rentes.PRRenteController;

/**
 * 
 * @author SCR
 * 
 */
public class PRImpactControllerFactory {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Retourne le controler du domaine pass� en param�tre.
     * 
     * Peut retourner null, si aucun domaineControler correspondant trouv�.
     */
    public static IPRImpactController getControler(int domaineControler) throws Exception {

        if (IPRImpactController.DOMAINE_RENTE_CONTROLER == domaineControler) {
            PRRenteController controler = new PRRenteController();
            return controler;
        }

        return null;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private PRImpactControllerFactory() {
        super();
    }
}

/*
 * Cr�� le 30 novembre 2009
 */
package globaz.cygnus.utils;

import globaz.cygnus.application.RFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.util.Vector;

/**
 * @author jje
 */
public class RFGestionnaireHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private static Vector<String[]> responsables = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le vecteur de tableaux a 2 entr�es {userId, userFullName} des gestionnaires pour le type de prestation
     * d�fini pour ce view bean.
     * 
     * <p>
     * Le vecteur n'est cr�� qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut responsable data
     */
    public static Vector getResponsableData(BSession session) {
        // cr�er les responsables s'ils n'ont pas d�j� �t� r�cup�r�s
        responsables = null;
        if (responsables == null) {
            try {
                responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(((RFApplication) GlobazSystem
                        .getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS))
                        .getProperty(RFApplication.PROPERTY_GROUPE_CYGNUS_GESTIONNAIRE));
            } catch (Exception e) {
                session.addError(session.getLabel(ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (responsables == null) {
                responsables = new Vector<String[]>();
            }

            responsables.insertElementAt(new String[] { "", "" }, 0);
        }

        return responsables;
    }
}

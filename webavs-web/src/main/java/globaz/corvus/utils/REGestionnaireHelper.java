/*
 * Cr�� le 9 janvier 2007
 */
package globaz.corvus.utils;

import globaz.corvus.application.REApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author hpe
 */
public class REGestionnaireHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private static Vector responsables = null;

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
        if (responsables == null) {
            try {
                responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(((REApplication) GlobazSystem
                        .getApplication(REApplication.DEFAULT_APPLICATION_CORVUS))
                        .getProperty(REApplication.PROPERTY_GROUPE_CORVUS_GESTIONNAIRE));
            } catch (Exception e) {
                session.addError(session.getLabel(ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (responsables == null) {
                responsables = new Vector();
            }

            responsables.insertElementAt(new String[] { "", "" }, 0);
        }

        return responsables;
    }
}

/*
 * Créé le 12 sept. 05
 */
package globaz.ij.utils;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.ij.application.IJApplication;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author dvh
 */
public class IJGestionnaireHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private static Vector responsables = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Retourne le vecteur de tableaux a 2 entrées {userId, userFullName} des gestionnaires pour le type de prestation
     * défini pour ce view bean.
     * 
     * <p>
     * Le vecteur n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut responsable data
     */
    public static Vector getResponsableData(BSession session) {
        // créer les responsables s'ils n'ont pas déjà été récupérés
        if (responsables == null) {
            try {
                responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(((IJApplication) GlobazSystem
                        .getApplication(IJApplication.DEFAULT_APPLICATION_IJ))
                        .getProperty(IJApplication.PROPERTY_GROUPE_IJ_GESTIONNAIRE));
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

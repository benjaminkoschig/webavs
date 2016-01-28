/*
 * Créé le 9 ovembre 2009
 */
package globaz.pegasus.utils;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.util.Vector;
import ch.globaz.pegasus.web.application.PCApplication;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author bsc
 */
public class PCGestionnaireHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private static Vector responsables = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public static String getDetailLoginGestionnaire(BSession session, String idGestionnaire)
            throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(idGestionnaire)) {
            return "";
        } else {
            JadeUser userName = session.getApplication()._getSecurityManager().getUserForVisa(session, idGestionnaire);
            return userName.getIdUser();
        }
    }

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
        if (PCGestionnaireHelper.responsables == null) {
            try {
                PCGestionnaireHelper.responsables = PRGestionnaireHelper
                        .getIdsEtNomsGestionnaires(((PCApplication) GlobazSystem
                                .getApplication(PCApplication.DEFAULT_APPLICATION_PEGASUS))
                                .getProperty(PCApplication.PROPERTY_GROUPE_PEGASUS_GESTIONNAIRE));
            } catch (Exception e) {
                session.addError(session.getLabel(PCGestionnaireHelper.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (PCGestionnaireHelper.responsables == null) {
                PCGestionnaireHelper.responsables = new Vector();
            }

            PCGestionnaireHelper.responsables.insertElementAt(new String[] { "", "" }, 0);
        }

        return PCGestionnaireHelper.responsables;
    }

}

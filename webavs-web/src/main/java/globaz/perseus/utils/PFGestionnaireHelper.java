package globaz.perseus.utils;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import java.util.Vector;
import ch.globaz.perseus.web.application.PFApplication;

public class PFGestionnaireHelper {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";
    private static Vector responsables = null;
    private static Vector gestionnairePlusAgence;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public static String getDetailGestionnaire(BSession session, String idGestionnaire)
            throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(idGestionnaire)) {
            return "";
        } else {
            JadeUser userName = session.getApplication()._getSecurityManager().getUserForVisa(session, idGestionnaire);
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
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
        if (PFGestionnaireHelper.responsables == null) {
            try {
                PFGestionnaireHelper.responsables = PRGestionnaireHelper
                        .getIdsEtNomsGestionnaires(((PFApplication) GlobazSystem
                                .getApplication(PFApplication.DEFAULT_APPLICATION_PERSEUS))
                                .getProperty(PFApplication.PROPERTY_GROUPE_PERSEUS_GESTIONNAIRE));
            } catch (Exception e) {
                session.addError(session.getLabel(PFGestionnaireHelper.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (PFGestionnaireHelper.responsables == null) {
                PFGestionnaireHelper.responsables = new Vector();
            }

            PFGestionnaireHelper.responsables.insertElementAt(new String[] { "", "" }, 0);
        }

        return PFGestionnaireHelper.responsables;
    }

    public static Vector findGestionaierEtAgence(BSession session) {
        // créer les responsables s'ils n'ont pas déjà été récupérés
        if (PFGestionnaireHelper.gestionnairePlusAgence == null) {
            try {
                PFGestionnaireHelper.gestionnairePlusAgence = new Vector();
                Vector gestionnaires = PRGestionnaireHelper.getIdsEtNomsGestionnaires(((PFApplication) GlobazSystem
                        .getApplication(PFApplication.DEFAULT_APPLICATION_PERSEUS))
                        .getProperty(PFApplication.PROPERTY_GROUPE_PERSEUS_GESTIONNAIRE));

                Vector agence = PRGestionnaireHelper.getIdsEtNomsGestionnaires(((PFApplication) GlobazSystem
                        .getApplication(PFApplication.DEFAULT_APPLICATION_PERSEUS))
                        .getProperty(PFApplication.PROPERTY_GROUPE_PERSEUS_AGENCE));

                gestionnairePlusAgence.addAll(agence);
                gestionnairePlusAgence.addAll(gestionnaires);

            } catch (Exception e) {
                session.addError(session.getLabel(PFGestionnaireHelper.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (PFGestionnaireHelper.gestionnairePlusAgence == null) {
                PFGestionnaireHelper.gestionnairePlusAgence = new Vector();
            }

            PFGestionnaireHelper.gestionnairePlusAgence.insertElementAt(new String[] { "", "" }, 0);
        }

        return PFGestionnaireHelper.gestionnairePlusAgence;
    }

}

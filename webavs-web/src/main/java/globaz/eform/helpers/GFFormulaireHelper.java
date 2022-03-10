package globaz.eform.helpers;

import ch.globaz.eform.constant.GFTypeEForm;
import ch.globaz.eform.web.application.GFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;

import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public abstract class GFFormulaireHelper {
    private static final String ERREUR_GESTIONNAIRES_INTROUVABLE = "GESTIONNAIRES_INTROUVABLE";

    public static final String PROPERTY_GROUPE_GESTIONNAIRE = "groupe.gestionnaire";

    private static Vector gestionnaires = null;
    private static Vector<String[]> type = null;

    public static  Vector<String[]> getTypeData(BSession session) {
        if (Objects.isNull(type)) {
            type = new Vector<>();
            Arrays.stream(GFTypeEForm.values()).forEach(gfTypeEForm -> {
                type.add(new String[] { gfTypeEForm.getCodeEForm(), gfTypeEForm.getDesignation(session) });
            });

            type.insertElementAt(new String[] { "", "" }, 0);
        }

        return type;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {userId, userFullName} des gestionnaires
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
    public static Vector getGestionnairesData(BSession session) {
        if (Objects.isNull(gestionnaires)) {
            try {
                gestionnaires = PRGestionnaireHelper
                        .getIdsEtNomsGestionnaires(GlobazSystem
                                .getApplication(GFApplication.APPLICATION_ID)
                                .getProperty(PROPERTY_GROUPE_GESTIONNAIRE));
            } catch (Exception e) {
                session.addError(session.getLabel(ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (Objects.isNull(gestionnaires)) {
                gestionnaires = new Vector<>();
            }

            gestionnaires.insertElementAt(new String[] { "", "" }, 0);
        }

        return gestionnaires;
    }
}

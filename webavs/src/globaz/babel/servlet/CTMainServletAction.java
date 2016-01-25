/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.servlet;

import globaz.babel.application.CTApplication;
import globaz.framework.controller.FWAction;
import java.util.HashMap;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * This class contains the mappings between the action strings and their handler class.
 * </p>
 * 
 * @author vre
 */
public class CTMainServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String ACTION_ANNEXES_COPIES = createAction("editableDocument.choixAnnexesCopies");
    public static final String ACTION_DEFAULT_ANNEXE = createAction("annexes.documentJointDefaultAnnexes");
    public static final String ACTION_DEFAULT_COPIE = createAction("copies.documentJointDefaultCopies");
    public static final String ACTION_DOCUMENTS = createAction("cat.document");
    public static final String ACTION_EDIT_PARAGRAPHES = createAction("editableDocument.editParagraphes");
    public static final String ACTION_PARAGRAPHES = createAction("editableDocument.choixParagraphes");
    public static final String ACTION_TEXTES = createAction("cat.texte");
    public static final String ACTION_TEXTES_SAISIE = createAction("cat.texteSaisie");

    private static final HashMap ACTIONS = new HashMap();

    static {
        ACTIONS.put(ACTION_DOCUMENTS, CTDocumentAction.class);
        ACTIONS.put(ACTION_TEXTES, CTTexteAction.class);
        ACTIONS.put(ACTION_TEXTES_SAISIE, CTTexteSaisieAction.class);
        ACTIONS.put(ACTION_DEFAULT_ANNEXE, CTDocumentJointDefaultAnnexesAction.class);
        ACTIONS.put(ACTION_DEFAULT_COPIE, CTDocumentJointDefaultCopiesAction.class);
        ACTIONS.put(ACTION_ANNEXES_COPIES, CTChoixAnnexesCopiesAction.class);
        ACTIONS.put(ACTION_PARAGRAPHES, CTChoixParagraphesAction.class);
        ACTIONS.put(ACTION_EDIT_PARAGRAPHES, CTEditParagraphesAction.class);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private static String createAction(String actionString) {
        return CTApplication.DEFAULT_APPLICATION_BABEL.toLowerCase() + "." + actionString;
    }

    /**
     * returns the class of the handler for this action string.
     * 
     * @param action
     *            the action string
     * 
     * @return la valeur courante de l'attribut action class
     */
    public static Class getActionClass(FWAction action) {
        String key = action.getApplicationPart() + "." + action.getPackagePart() + "." + action.getClassPart();

        return (Class) ACTIONS.get(key);
    }
}

package globaz.naos.stack.rules;

import globaz.framework.controller.FWAction;
import globaz.framework.utils.params.FWParam;
import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.framework.utils.urls.rules.FWUrlsStackRule;
import globaz.naos.application.AFApplication;
import java.util.HashMap;

public class AFUrlStackRule implements FWUrlsStackRule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final static String PARAM_USER_ACTION = "userAction";

    /**
     * Contient une map des actions sur lesquelles intervenir et remplacer (ou pas) par l'action � garder en Value
     * </br>
     * <b>put(<i>action � catcher, action � garder</i>)</b>
     * </br>
     * L'<i>action � garder</i> peut diff�rer de l'<i>action � catcher</i> comme par example dans le cas d'une action
     * <u>selectionner</u> qui au <b><u>BACK</u></b> est ignor�e, on voudrait par example la remplacer par une action
     * <u>reAfficher</u>
     * </br>
     * <i>Info</i> : Toutes actions � garder en <b>Value</b> sera ignor�e par le cleaner d'action a l'ajout dans la
     * stack, m�me si elle ne d�coule pas de l'action a catcher en <b>Key</b>
     * 
     */
    private HashMap<String, String> actionToKeep = new HashMap<String, String>();

    public AFUrlStackRule() {
        actionToKeep.put("naos.affiliation.affiliation.selectionner", "naos.affiliation.affiliation.reAfficher");
    }

    @Override
    public void applyOn(FWUrlsStack aStack) {
        if (aStack.size() >= 1) {
            FWUrl lastUrl = aStack.peek();
            FWParam lastUrlUserAction = lastUrl.getParam(PARAM_USER_ACTION);
            String usrAction = (String) lastUrlUserAction.getValue();
            FWAction _monAction = FWAction.newInstance(usrAction);
            // contr�le que l'on reste dans le scope du module
            if (AFApplication.DEFAULT_APPLICATION_NAOS.equalsIgnoreCase(_monAction.getApplicationPart())
                    && usrAction.startsWith("naos.")) {
                // gestion des trigger
                if (!actionToKeep.isEmpty() && actionToKeep.containsKey(usrAction)) {
                    // modification de la usrAction vers la cible et remplacement dans la stack
                    FWParamString newParam = new FWParamString();
                    newParam.setKey(PARAM_USER_ACTION);
                    newParam.setValue(actionToKeep.get(usrAction));
                    lastUrl.removeParam(PARAM_USER_ACTION);
                    lastUrl.addParam(newParam);
                    aStack.pop();
                    aStack.push(lastUrl);
                    // gestion globale sur le module [chercher-afficher] (+ exceptions dues aux trigger)
                } else if (usrAction.endsWith("naos.releve.apercuReleve.supprimer")) { // Correction lorsque la stack essaie de r�afficher un relev� supprim�
                    aStack.pop(); // Pop le "supprimer"
                    // Tant que la derni�re URL de la stack se termine par "naos.releve.apercuReleve.afficher" --> pop
                    // (cliquer plusieurs fois sur le bouton d'affichage des d�tails provoque plusieurs push)
                    while (aStack.peek().getParam(PARAM_USER_ACTION).toString().endsWith("naos.releve.apercuReleve.afficher")) {
                        aStack.pop(); // Pop les "afficher"
                    }
                    // Si la prochaine action est le "chercher" --> pop
                    usrAction = (String) aStack.peek().getParam(PARAM_USER_ACTION).getValue();
                    if (usrAction.endsWith("naos.releve.apercuReleve.chercher")) {
                        aStack.pop(); // Pop le "chercher"
                    }
                } else if (!usrAction.endsWith(FWAction.ACTION_CHERCHER)
                        && !usrAction.endsWith(FWAction.ACTION_AFFICHER) && !actionToKeep.containsValue(usrAction)
                        && !usrAction.endsWith(".imprimer")) {
                    lastUrl = aStack.pop();
                }
            }
        }
    }

    @Override
    public byte getPriority() {
        return 100;
    }

}

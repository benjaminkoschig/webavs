package globaz.lynx.db.codetva;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;

public class LXCodeTvaViewBean extends LXCodeTva implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWParametersUserCode ucCodeTVA = null;

    /**
     * Retourne le code utilisateur d'un code system pour le code TVA
     * 
     * @return
     */
    public String getCodeUtilisateur() {
        return getUcCodeTVA().getCodeUtilisateur();
    }

    /**
     * Retourne le libelle d'un code system pour le code TVA
     * 
     * @return
     */
    public String getLibelle() {
        return getUcCodeTVA().getLibelle();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcCodeTVA() {

        if (ucCodeTVA == null) {
            // liste pas encore chargee, on la charge
            ucCodeTVA = new FWParametersUserCode();
            ucCodeTVA.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsCodeTVA())) {
                ucCodeTVA.setIdCodeSysteme(getCsCodeTVA());
                ucCodeTVA.setIdLangue(getSession().getIdLangue());
                try {
                    ucCodeTVA.retrieve();
                    if (ucCodeTVA.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                }
            }
        }

        return ucCodeTVA;
    }
}

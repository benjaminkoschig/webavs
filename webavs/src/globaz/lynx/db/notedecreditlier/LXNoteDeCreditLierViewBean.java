package globaz.lynx.db.notedecreditlier;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXNoteDeCreditUtil;

public class LXNoteDeCreditLierViewBean extends LXNoteDeCreditLier implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWParametersUserCode ucEtat = null;

    /**
     * Constructeur
     */
    public LXNoteDeCreditLierViewBean() {
        super();
    }

    /**
     * Permet de retourner l'ID Externe de la section de la NDC liée (coté facture)
     * 
     * @return
     * @throws Exception
     */
    public String getIdExterneSectionLiee() throws Exception {
        return LXNoteDeCreditUtil.getIdExterneSection(getSession(), getIdSectionLiee());
    }

    /**
     * Get libellé de l'état de la facture
     * 
     * @param code
     * @return
     */
    public FWParametersUserCode getUcEtat(String code) {

        if (ucEtat == null || (ucEtat != null && !JadeStringUtil.isIntegerEmpty(code))) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
            ucEtat.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(code)) {
                ucEtat.setIdCodeSysteme(code);
                ucEtat.setIdLangue(getSession().getIdLangue());
                try {
                    ucEtat.retrieve();
                    if (ucEtat.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                }
            }
        }

        return ucEtat;
    }
}

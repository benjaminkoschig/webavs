package globaz.lynx.db.recherche;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXUtils;

public class LXRechercheDetailViewBean extends LXRechercheDetail implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWParametersUserCode ucType = null;

    /**
     * Constructeur
     */
    public LXRechercheDetailViewBean() {
        super();
    }

    public String getMontantFormattedPositif() {
        String montant = LXUtils.getMontantPositif(getMontant());
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(montant), true, true, false, 2);
    }

    public FWParametersUserCode getUcType(String code) {

        if (ucType == null || (ucType != null && !JadeStringUtil.isIntegerEmpty(code))) {
            // liste pas encore chargee, on la charge
            ucType = new FWParametersUserCode();
            ucType.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsTypeOperation()) || !JadeStringUtil.isIntegerEmpty(code)) {
                ucType.setIdCodeSysteme(JadeStringUtil.isIntegerEmpty(code) ? getCsTypeOperation() : code);
                ucType.setIdLangue(getSession().getIdLangue());
                try {
                    ucType.retrieve();
                    if (ucType.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_UTILISATEUR"));
                }
            }
        }

        return ucType;
    }

}

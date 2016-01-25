package globaz.lynx.db.ordregroupe;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

public class LXOrdreGroupeViewBean extends LXOrdreGroupe implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String TYPE_DATE_PMT_ECHEANCE = "echeance";
    public static final String TYPE_DATE_PMT_FIXE = "fixe";

    private String typeDatePmt = null;
    private FWParametersUserCode ucEtat = null;

    /**
     * Constructeur de LXOrdreGroupeViewBean.
     */
    public LXOrdreGroupeViewBean() {
        super();
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        super._validate(statement);

        if (LXOrdreGroupeViewBean.TYPE_DATE_PMT_ECHEANCE.equals(getTypeDatePmt())) {
            setDatePaiement("0");
        }

        // Vérification présence de la date de paiement si "à date fixe" a été selectionné.
        if (LXOrdreGroupeViewBean.TYPE_DATE_PMT_FIXE.equals(getTypeDatePmt())
                && !JadeDateUtil.isGlobazDate(getDatePaiement())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_DATE_PAIEMENT_FORCEE"));
        }
    }

    /**
     * @return the typeDatePmt
     */
    public String getTypeDatePmt() {
        return typeDatePmt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return FWParametersUserCode
     */
    public FWParametersUserCode getUcEtat() {
        return this.getUcEtat(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @param code
     *            Un code systeme
     * @return
     */
    public FWParametersUserCode getUcEtat(String code) {

        if ((ucEtat == null) || ((ucEtat != null) && !JadeStringUtil.isIntegerEmpty(code))) {
            // liste pas encore chargee, on la charge
            ucEtat = new FWParametersUserCode();
            ucEtat.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsEtat()) || !JadeStringUtil.isIntegerEmpty(code)) {
                ucEtat.setIdCodeSysteme(JadeStringUtil.isIntegerEmpty(code) ? getCsEtat() : code);
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

    /**
     * @param typeDatePmt
     *            the typeDatePmt to set
     */
    public void setTypeDatePmt(String typeDatePmt) {
        this.typeDatePmt = typeDatePmt;
    }
}

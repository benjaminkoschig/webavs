package globaz.lynx.db.paiement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.organeexecution.LXOrganeExecution;

public class LXPaiementViewBean extends LXPaiement implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    FWParametersUserCode ucMotifBlocage = null;

    /**
     * Constructeur
     */
    public LXPaiementViewBean() {
        super();
    }

    @Override
    public String getMontantCredit(int i) {
        String result = "";
        if (i == 1 || i == 2) {
            result = "0.00";
        }

        if (getVentilations().size() > i) {

            if ((CodeSystem.CS_CREDIT.equals(getVentilations().get(i).getCodeDebitCredit()) || (CodeSystem.CS_EXTOURNE_CREDIT
                    .equals(getVentilations().get(i).getCodeDebitCredit())))
                    && !JadeStringUtil.isDecimalEmpty(getVentilations().get(i).getMontant())) {
                result = getVentilations().get(i).getMontant();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    @Override
    public String getMontantDebit(int i) {
        String result = "";
        if (i == 0) {
            result = "0.00";
        }

        if (getVentilations().size() > i) {
            if ((CodeSystem.CS_DEBIT.equals(getVentilations().get(i).getCodeDebitCredit()) || (CodeSystem.CS_EXTOURNE_DEBIT
                    .equals(getVentilations().get(i).getCodeDebitCredit())))
                    && !JadeStringUtil.isDecimalEmpty(getVentilations().get(i).getMontant())) {
                result = getVentilations().get(i).getMontant();
            }
        }

        return JANumberFormatter.fmt(result, true, true, false, 2);
    }

    /**
     * Permet la récupération du libellé de l'organe d'execution
     * 
     * @return
     */
    public String getOrganeExecutionLibelle() {

        if (!JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            LXOrganeExecution organeExecution = new LXOrganeExecution();
            organeExecution.setSession(getSession());
            organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            try {
                organeExecution.retrieve();
                if (organeExecution.isNew()) {
                    _addError(null, getSession().getLabel("CHARGEMENT_LIBELLE_ORGANEEXECUTION"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("CHARGEMENT_LIBELLE_ORGANEEXECUTION"));
            }
            return organeExecution.getNom();

        }
        return "";

    }

    public FWParametersUserCode getUcMotifBlocage() {
        return getUcMotifBlocage(null);
    }

    public FWParametersUserCode getUcMotifBlocage(String code) {

        if (ucMotifBlocage == null || (ucMotifBlocage != null && !JadeStringUtil.isIntegerEmpty(code))) {
            ucMotifBlocage = new FWParametersUserCode();
            ucMotifBlocage.setSession(getSession());
            // Récupérer le code système dans la langue de l'utilisateur
            if (!JadeStringUtil.isIntegerEmpty(getCsMotifBlocage()) || !JadeStringUtil.isIntegerEmpty(code)) {
                ucMotifBlocage.setIdCodeSysteme(JadeStringUtil.isIntegerEmpty(code) ? getCsMotifBlocage() : code);
                ucMotifBlocage.setIdLangue(getSession().getIdLangue());
                try {
                    ucMotifBlocage.retrieve();
                    if (ucMotifBlocage.isNew()) {
                        _addError(null, getSession().getLabel("CHARGEMENT_CODE_MOTIF_BLOCAGE"));
                    }
                } catch (Exception e) {
                    _addError(null, getSession().getLabel("CHARGEMENT_CODE_MOTIF_BLOCAGE"));
                }
            }
        }

        return ucMotifBlocage;
    }
}

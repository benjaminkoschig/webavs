package globaz.lynx.db.notedecredit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.utils.LXNoteDeCreditUtil;

public class LXNoteDeCreditViewBean extends LXNoteDeCredit implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
     * Recherche si il existe une note de cr�dit encaiss�e pour la section courante - <b>true</b> si il existe une note
     * de credit encaiss�e - <b>false</b> so il n'existe pas de cnote de cr�dit encaiss�e
     * 
     * @return
     */
    public boolean isExisteNoteDeCreditEncaissee() {
        return LXNoteDeCreditUtil.isExisteNoteDeCreditEncaissee(getSession(), getIdSection(), getIdOperation());
    }

    /**
     * Recherche si il existe une note de cr�dit li�e pour la section courante - <b>true</b> si il existe une note de
     * credit li�e - <b>false</b> so il n'existe pas de cnote de cr�dit li�e
     * 
     * @return
     */
    public boolean isExisteNoteDeCreditLiee() {
        return LXNoteDeCreditUtil.isExisteNoteDeCreditLiee(getSession(), getIdSection(), getIdOperation());
    }

    /**
     * Retourne vrai si une note de cr�dit peut �tre Encaissable ou Liable
     * 
     * @return
     */
    public boolean isPossibleEncaissable() {
        return LXNoteDeCreditUtil.isPossibleEncaissable(getSession(), getIdSection());
    }
}

package globaz.aquila.db.journaux;

import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.client.util.JadeStringUtil;

public class COJournalBatchViewBean extends COJournalBatch implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pour écran.
     * 
     * @return Le libellé de l'état de l'élément.
     */
    public String getEtatLibelle() {
        FWParametersUserCode ucEtat = new FWParametersUserCode();
        ucEtat.setSession(getSession());

        if (!JadeStringUtil.isIntegerEmpty(getEtat())) {
            ucEtat.setIdCodeSysteme(getEtat());
            ucEtat.setIdLangue(getSession().getIdLangue());

            try {
                ucEtat.retrieve();
                if (ucEtat.isNew() || ucEtat.hasErrors()) {
                    return "";
                }
            } catch (Exception e) {
                return "";
            }
        }

        return ucEtat.getLibelle();
    }

}

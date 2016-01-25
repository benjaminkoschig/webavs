package globaz.lynx.helpers.notedecredit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.notedecredit.LXNoteDeCreditViewBean;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.helpers.utils.LXHelperUtils;
import globaz.lynx.helpers.utils.LXNoteDeCreditUtils;

/**
 * Permet d'ajouter des note De Credit à un journal.
 * 
 * @author DDA
 */
public class LXNoteDeCreditAdd {

    /**
     * Ajout des noteDeCredit depuis l'écran de LYNX.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void addNoteDeCredit(BISession session, FWViewBeanInterface viewBean) throws Exception {
        LXNoteDeCreditViewBean noteDeCredit = (LXNoteDeCreditViewBean) viewBean;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            transaction.openTransaction();

            LXNoteDeCreditUtils.validate(session, transaction, noteDeCredit);

            LXSection section = LXHelperUtils.getSection(session, noteDeCredit, transaction,
                    LXSection.CS_TYPE_NOTEDECREDIT);

            LXOperation operation = LXHelperUtils.createOperation(session, noteDeCredit, transaction, section);

            LXHelperUtils.addVentilations(session, transaction, noteDeCredit, operation);

            noteDeCredit.setIdSection(section.getIdSection());
            noteDeCredit.setIdOperation(operation.getIdOperation());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
            }

            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }

                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }
}

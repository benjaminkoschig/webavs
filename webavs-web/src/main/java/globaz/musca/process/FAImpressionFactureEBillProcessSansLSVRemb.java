package globaz.musca.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

/**
 * Ins?rez la description du type ici. Date de cr?ation : (26.02.2002 14:53:33)
 * 
 * @author: Administrator
 */
public class FAImpressionFactureEBillProcessSansLSVRemb extends FAImpressionFactureEBillProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public FAImpressionFactureEBillProcessSansLSVRemb() throws Exception {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     *
     * @exception Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureEBillProcessSansLSVRemb(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     *
     * @exception Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureEBillProcessSansLSVRemb(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void executeRemboursementLSV() {
        // pas de remboursement dans cette impl?memtation
    }

}

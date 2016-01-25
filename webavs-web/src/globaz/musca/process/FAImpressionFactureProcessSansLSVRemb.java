package globaz.musca.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

/**
 * Insérez la description du type ici. Date de création : (26.02.2002 14:53:33)
 * 
 * @author: Administrator
 */
public class FAImpressionFactureProcessSansLSVRemb extends FAImpressionFactureProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FAImpressionFactureProcessSansLSVRemb() throws Exception {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureProcessSansLSVRemb(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAImpressionFactureProcessSansLSVRemb(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void executeRemboursementLSV() {
        // pas de remboursement dans cette implémemtation
    }

}

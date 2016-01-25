package globaz.osiris.db.print;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListTypeOperation;

public class CAListTypeOperationViewBean extends CAAbstractListProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idTypeOperation = new String();

    public CAListTypeOperationViewBean() throws Exception {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.print.CAListProcess#getDocument()
     */
    @Override
    FWIDocumentInterface getDocument() {
        try {
            CAIListTypeOperation _doc = new CAIListTypeOperation(this);
            _doc.setIdTypeOperation(idTypeOperation);
            _doc.setEMailAddress(getEMailAddress());
            return _doc;
        } catch (FWIException e) {
        }
        return null;

    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }
}

package globaz.osiris.db.print;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListTypeSection;

public class CAListTypeSectionViewBean extends CAAbstractListProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAListTypeSectionViewBean() throws Exception {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.print.CAAbstractListProcess#getDocument()
     */
    @Override
    FWIDocumentInterface getDocument() {
        try {
            CAIListTypeSection _doc = new CAIListTypeSection(this);
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

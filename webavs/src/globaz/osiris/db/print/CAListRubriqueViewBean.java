package globaz.osiris.db.print;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListRubrique;

public class CAListRubriqueViewBean extends CAAbstractListProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idRubrique = new String();

    public CAListRubriqueViewBean() throws Exception {
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
     * @see globaz.osiris.db.print.CAAbstractListProcess#getDocument()
     */
    @Override
    FWIDocumentInterface getDocument() {
        try {
            CAIListRubrique _doc = new CAIListRubrique(this);
            _doc.setIdRubrique(getIdRubrique());
            _doc.setEMailAddress(getEMailAddress());
            return _doc;
        } catch (FWIException e) {
        }
        return null;
    }

    /**
     * Returns the idRubrique.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRubrique() {
        return idRubrique;
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

    /**
     * Sets the idRubrique.
     * 
     * @param idRubrique
     *            The idRubrique to set
     */
    public void setIdRubrique(java.lang.String idRubrique) {
        this.idRubrique = idRubrique;
    }

}

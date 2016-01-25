package globaz.osiris.db.print;

import globaz.framework.printing.itext.api.FWIDocumentInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.print.itext.list.CAIListRole;

public class CAListRoleViewBean extends CAAbstractListProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idRole = new String();

    public CAListRoleViewBean() throws Exception {
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
            CAIListRole _doc = new CAIListRole(this);
            _doc.setIdRole(getIdRole());
            _doc.setEMailAddress(getEMailAddress());
            return _doc;
        } catch (FWIException e) {
        }
        return null;
    }

    /**
     * Cette méthode retourne l'idRole.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRole() {
        return idRole;
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
     * Cette méthode permet d'attribuer l'idRole.
     * 
     * @param newIdRubrique
     *            java.lang.String
     */
    public void setIdRole(java.lang.String newIdRole) {
        idRole = newIdRole;
    }

}

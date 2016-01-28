package globaz.pavo.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationMilitaireIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationRecord;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator;
import java.util.TreeMap;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class ICIDeclarationFactory implements ICIDeclarationIterator {
    /**
     * Constructor for ICIDeclarationFactory.
     */
    public ICIDeclarationFactory() {
        super();
    }

    public ICIDeclarationFactory(String type) {
        if (type.equals("militaire")) {
            new CIDeclarationMilitaireIterator();
        }
    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#close()
     */
    @Override
    public void close() {
    }

    @Override
    public String getDateReception() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#getFilename()
     */
    @Override
    public String getFilename() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#getNbSalaires ()
     */
    @Override
    public TreeMap getNbSalaires() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    @Override
    public TreeMap getNoAffiliePourReception() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BSession getSession() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator# getTotauxJournaux()
     */
    @Override
    public TreeMap getTotauxJournaux() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#getTransaction()
     */
    @Override
    public BTransaction getTransaction() {
        return null;
    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#hasNext()
     */
    @Override
    public boolean hasNext() throws Exception {
        return false;
    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#next()
     */
    @Override
    public CIDeclarationRecord next() throws Exception {
        return null;
    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#setFilename(String)
     */
    @Override
    public void setFilename(String filename) {
    }

    @Override
    public void setProvenance(String provenance) {

    }

    @Override
    public void setSession(BSession session) {

    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#setTransaction(BTransaction)
     */
    @Override
    public void setTransaction(BTransaction transaction) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#setTypeImport (java.lang.String)
     */
    @Override
    public void setTypeImport(String type) {
        // TODO Raccord de méthode auto-généré

    }

    /**
     * @see globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator#size()
     */
    @Override
    public int size() throws Exception {
        return 0;
    }

}

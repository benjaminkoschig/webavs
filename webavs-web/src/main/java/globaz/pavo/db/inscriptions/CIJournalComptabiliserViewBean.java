package globaz.pavo.db.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.pavo.vb.CIAbstractPersistentViewBean;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: David Girardin
 */
public class CIJournalComptabiliserViewBean extends CIAbstractPersistentViewBean implements FWViewBeanInterface {
    String idJournal = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return la description du journal
     */
    public String getDescriptionJournal() {
        CIJournal jour = new CIJournal();
        jour.setSession(getSession());
        jour.setIdJournal(getIdJournal());
        try {
            jour.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jour.getDescription();
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String newId) {
        super.setId(newId);
        setIdJournal(newId);
    }

    /**
     * @param idJournal
     *            the idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}

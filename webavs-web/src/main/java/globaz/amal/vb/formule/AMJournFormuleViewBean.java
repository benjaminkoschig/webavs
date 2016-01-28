package globaz.amal.vb.formule;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.journalisation.HistoriqueImportation;
import ch.globaz.amal.business.models.journalisation.HistoriqueImportationSearch;

public class AMJournFormuleViewBean extends BJadePersistentObjectViewBean {
    private HistoriqueImportation historiqueImportation = null;

    /**
	 * 
	 */
    public AMJournFormuleViewBean() {
        super();
        historiqueImportation = new HistoriqueImportation();
    }

    public AMJournFormuleViewBean(HistoriqueImportation historiqueImportation) {
        super();
        this.historiqueImportation = historiqueImportation;
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public HistoriqueImportation getHistoriqueImportation() {
        return historiqueImportation;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return historiqueImportation.getId();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        HistoriqueImportationSearch search = new HistoriqueImportationSearch();

    }

    public void setHistoriqueImportation(HistoriqueImportation historiqueImportation) {
        this.historiqueImportation = historiqueImportation;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}

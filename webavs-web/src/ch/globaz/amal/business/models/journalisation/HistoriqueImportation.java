/**
 * 
 */
package ch.globaz.amal.business.models.journalisation;

import globaz.globall.db.BSpy;
import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author LFO
 * 
 */
public class HistoriqueImportation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleJournalisation simpleJournalisation = null;
    private SimpleReferenceProvenanceJournalisation simpleReferenceProvenanceJournalisation = null;

    public HistoriqueImportation() {
        simpleJournalisation = new SimpleJournalisation();
        simpleReferenceProvenanceJournalisation = new SimpleReferenceProvenanceJournalisation();

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return simpleJournalisation.getId();
    }

    public SimpleJournalisation getSimpleJournalisation() {
        return simpleJournalisation;
    }

    public SimpleReferenceProvenanceJournalisation getSimpleReferenceProvenanceJournalisation() {
        return simpleReferenceProvenanceJournalisation;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return simpleJournalisation.getSpy();
    }

    public String getSpyFormate() {
        // TODO Auto-generated method stub
        return new BSpy(simpleJournalisation.getSpy()).getDate();
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setSimpleJournalisation(SimpleJournalisation simpleJournalisation) {
        this.simpleJournalisation = simpleJournalisation;
    }

    public void setSimpleReferenceProvenanceJournalisation(
            SimpleReferenceProvenanceJournalisation simpleReferenceProvenanceJournalisation) {
        this.simpleReferenceProvenanceJournalisation = simpleReferenceProvenanceJournalisation;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }
}

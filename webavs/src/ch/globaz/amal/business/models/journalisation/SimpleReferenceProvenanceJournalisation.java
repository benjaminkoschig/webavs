package ch.globaz.amal.business.models.journalisation;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleReferenceProvenanceJournalisation extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalisation = null;
    private String idReferenceJournalisation = null;
    private String idReferenceProvenance = null;
    private String type = null;

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idReferenceJournalisation;
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    public String getIdReferenceJournalisation() {
        return idReferenceJournalisation;
    }

    public String getIdReferenceProvenance() {
        return idReferenceProvenance;
    }

    public String getType() {
        return type;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idReferenceJournalisation = id;

    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

    public void setIdReferenceJournalisation(String idReferenceJournalisation) {
        this.idReferenceJournalisation = idReferenceJournalisation;
    }

    public void setIdReferenceProvenance(String idReferenceProvenance) {
        this.idReferenceProvenance = idReferenceProvenance;
    }

    public void setType(String type) {
        this.type = type;
    }

}

package ch.globaz.amal.business.models.journalisation;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleJournalisation extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalisation = null;
    private String libelle = null;
    private String user = null;

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idJournalisation;
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getUser() {
        return user;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idJournalisation = id;
    }

    public void setIdJournalisation(String idJournalisation) {
        this.idJournalisation = idJournalisation;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

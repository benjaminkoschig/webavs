package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCO 19 mai 2010
 */
public class JournalSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String date;
    private String dateValeurCG;
    private String etat;
    private String idJournal;
    private String libelle;
    private String noJournalCG;
    private String proprietaire;
    private String typeJournal;

    public String getDate() {
        return date;
    }

    public String getDateValeurCG() {
        return dateValeurCG;
    }

    public String getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return getIdJournal();
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getNoJournalCG() {
        return noJournalCG;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public String getTypeJournal() {
        return typeJournal;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateValeurCG(String dateValeurCG) {
        this.dateValeurCG = dateValeurCG;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String id) {
        setIdJournal(id);
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setNoJournalCG(String noJournalCG) {
        this.noJournalCG = noJournalCG;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public void setTypeJournal(String typeJournal) {
        this.typeJournal = typeJournal;
    }
}

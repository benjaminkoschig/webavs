package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAutresDettesProuvees extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAutresDettesProuvees = null;
    private String idDonneeFinanciereHeader = null;
    private String montantDette = null;
    private String nomPrenomCreancier = null;

    @Override
    public String getId() {
        return idAutresDettesProuvees;
    }

    public String getIdAutresDettesProuvees() {
        return idAutresDettesProuvees;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getMontantDette() {
        return montantDette;
    }

    public String getNomPrenomCreancier() {
        return nomPrenomCreancier;
    }

    @Override
    public void setId(String id) {
        idAutresDettesProuvees = id;
    }

    public void setIdAutresDettesProuvees(String idAutresDettesProuvees) {
        this.idAutresDettesProuvees = idAutresDettesProuvees;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

    public void setNomPrenomCreancier(String nomPrenomCreancier) {
        this.nomPrenomCreancier = nomPrenomCreancier;
    }

}

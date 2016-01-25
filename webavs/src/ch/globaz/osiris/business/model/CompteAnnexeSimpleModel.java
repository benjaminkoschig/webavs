package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCO 19 mai 2010
 */
public class CompteAnnexeSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description;
    private String idCompteAnnexe;
    private String idExterneRole;
    private String idJournal;
    private String idRole;
    private String idTiers;
    /** solde du compte annexe */
    private String solde;

    public String getDescription() {
        return description;
    }

    @Override
    public String getId() {
        return getIdCompteAnnexe();
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterneRole() {
        return idExterneRole;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdRole() {
        return idRole;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getSolde() {
        return solde;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setId(String id) {
        setIdCompteAnnexe(id);
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setSolde(String solde) {
        this.solde = solde;
    }
}

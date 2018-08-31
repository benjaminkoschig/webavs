package ch.globaz.vulpecula.external.models.osiris;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;

public class CompteAnnexe implements DomainEntity {
    private String idCompteAnnexe;
    private String idTiers;
    private String categorie;
    private String numAffilie;
    private Montant solde;
    private String spy;

    @Override
    public String getId() {
        return idCompteAnnexe;
    }

    @Override
    public void setId(String id) {
        idCompteAnnexe = id;
    }

    /**
     * @return the idCompteAnnexe
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the categorie
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return the solde
     */
    public Montant getSolde() {
        return solde;
    }

    /**
     * @param idCompteAnnexe the idCompteAnnexe to set
     */
    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param idTiers the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param categorie the categorie to set
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * @param numAffilie the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param solde the solde to set
     */
    public void setSolde(Montant solde) {
        this.solde = solde;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

}

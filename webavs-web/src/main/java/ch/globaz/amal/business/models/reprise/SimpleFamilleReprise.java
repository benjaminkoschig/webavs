/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleFamilleReprise extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeTraitementDossier = null;

    private String dateAvisRIP = null;

    private String dateNaissance = null;

    private String finDefinitive = null;

    private String idContribuable = null;

    private String idFamille = null;

    private String idTiers = null;

    private Boolean isActive = null;

    private Boolean isContribuable = null;

    private String nnssContribuable = null;

    private String noAVS = null;

    private String nomPrenom = null;

    private String noPersonne = null;

    private String pereMereEnfant = null;

    private String sexe = null;

    private String typeAvisRIP = null;

    private String typeNoPersonne = null;

    /**
	 * 
	 */
    public SimpleFamilleReprise() {
        super();
    }

    /**
     * @return the codeTraitementDossier
     */
    public String getCodeTraitementDossier() {
        return codeTraitementDossier;
    }

    /**
     * @return the dateAvisRIP
     */
    public String getDateAvisRIP() {
        return dateAvisRIP;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the finDefinitive
     */
    public String getFinDefinitive() {
        return finDefinitive;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idFamille;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idFamille
     */
    public String getIdFamille() {
        return idFamille;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @return the isContribuable
     */
    public Boolean getIsContribuable() {
        return isContribuable;
    }

    /**
     * @return the nnssContribuable
     */
    public String getNnssContribuable() {
        return nnssContribuable;
    }

    /**
     * @return the noAVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * @return the nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return the noPersonne
     */
    public String getNoPersonne() {
        return noPersonne;
    }

    /**
     * @return the pereMereEnfant
     */
    public String getPereMereEnfant() {
        return pereMereEnfant;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @return the typeAvisRIP
     */
    public String getTypeAvisRIP() {
        return typeAvisRIP;
    }

    /**
     * @return the typeNoPersonne
     */
    public String getTypeNoPersonne() {
        return typeNoPersonne;
    }

    /**
     * @param codeTraitementDossier
     *            the codeTraitementDossier to set
     */
    public void setCodeTraitementDossier(String codeTraitementDossier) {
        this.codeTraitementDossier = codeTraitementDossier;
    }

    /**
     * @param dateAvisRIP
     *            the dateAvisRIP to set
     */
    public void setDateAvisRIP(String dateAvisRIP) {
        this.dateAvisRIP = dateAvisRIP;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param finDefinitive
     *            the finDefinitive to set
     */
    public void setFinDefinitive(String finDefinitive) {
        this.finDefinitive = finDefinitive;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idFamille = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idFamille
     *            the idFamille to set
     */
    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param isActive
     *            the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @param isContribuable
     *            the isContribuable to set
     */
    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    /**
     * @param nnssContribuable
     *            the nnssContribuable to set
     */
    public void setNnssContribuable(String nnssContribuable) {
        this.nnssContribuable = nnssContribuable;
    }

    /**
     * @param noAVS
     *            the noAVS to set
     */
    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }

    /**
     * @param nomPrenom
     *            the nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * @param noPersonne
     *            the noPersonne to set
     */
    public void setNoPersonne(String noPersonne) {
        this.noPersonne = noPersonne;
    }

    /**
     * @param pereMereEnfant
     *            the pereMereEnfant to set
     */
    public void setPereMereEnfant(String pereMereEnfant) {
        this.pereMereEnfant = pereMereEnfant;
    }

    /**
     * @param sexe
     *            the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * @param typeAvisRIP
     *            the typeAvisRIP to set
     */
    public void setTypeAvisRIP(String typeAvisRIP) {
        this.typeAvisRIP = typeAvisRIP;
    }

    /**
     * @param typeNoPersonne
     *            the typeNoPersonne to set
     */
    public void setTypeNoPersonne(String typeNoPersonne) {
        this.typeNoPersonne = typeNoPersonne;
    }

}

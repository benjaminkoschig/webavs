package ch.globaz.vulpecula.external.models.osiris;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;

/**
 * @since WebBMS 2.3
 */
public class OrganeExecution implements DomainEntity {
    private String idOrganeExecution = "";
    private String nom = "";
    private String idRubrique = "";
    private String numeroRubrique;
    private String genre = "";
    private String idAdressePaiement = "";
    private String identifiantDTA = "";
    private String idAdresseDebitTaxes = "";

    private String idTypeTraitementBV = "";
    private String idTypeTraitementLS = "";
    private String idTypeTraitementOG = "";
    private String nomClasseParserBvr = "";
    private String noAdherentBVR = "";
    private String nomClasseParserLSV = "";
    private String noAdherent = "";
    private String numInterneLsv = "";
    private String modeTransfert = "";

    private String spy;

    @Override
    public String getId() {
        return getIdOrganeExecution();
    }

    @Override
    public void setId(String id) {
        setIdOrganeExecution(id);
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    /**
     * @return the idOrganeExecution
     */
    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return the numeroRubrique
     */
    public String getNumeroRubrique() {
        return numeroRubrique;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @return the idAdressePaiement
     */
    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    /**
     * @return the identifiantDTA
     */
    public String getIdentifiantDTA() {
        return identifiantDTA;
    }

    /**
     * @return the idAdresseDebitTaxes
     */
    public String getIdAdresseDebitTaxes() {
        return idAdresseDebitTaxes;
    }

    /**
     * @return the idTypeTraitementBV
     */
    public String getIdTypeTraitementBV() {
        return idTypeTraitementBV;
    }

    /**
     * @return the idTypeTraitementLS
     */
    public String getIdTypeTraitementLS() {
        return idTypeTraitementLS;
    }

    /**
     * @return the idTypeTraitementOG
     */
    public String getIdTypeTraitementOG() {
        return idTypeTraitementOG;
    }

    /**
     * @return the nomClasseParserBvr
     */
    public String getNomClasseParserBvr() {
        return nomClasseParserBvr;
    }

    /**
     * @return the noAdherentBVR
     */
    public String getNoAdherentBVR() {
        return noAdherentBVR;
    }

    /**
     * @return the nomClasseParserLSV
     */
    public String getNomClasseParserLSV() {
        return nomClasseParserLSV;
    }

    /**
     * @return the noAdherent
     */
    public String getNoAdherent() {
        return noAdherent;
    }

    /**
     * @return the numInterneLsv
     */
    public String getNumInterneLsv() {
        return numInterneLsv;
    }

    /**
     * @return the modeTransfert
     */
    public String getModeTransfert() {
        return modeTransfert;
    }

    /**
     * @param idOrganeExecution the idOrganeExecution to set
     */
    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param idRubrique the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @param numeroRubrique the numeroRubrique to set
     */
    public void setNumeroRubrique(String numeroRubrique) {
        this.numeroRubrique = numeroRubrique;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @param idAdressePaiement the idAdressePaiement to set
     */
    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    /**
     * @param identifiantDTA the identifiantDTA to set
     */
    public void setIdentifiantDTA(String identifiantDTA) {
        this.identifiantDTA = identifiantDTA;
    }

    /**
     * @param idAdresseDebitTaxes the idAdresseDebitTaxes to set
     */
    public void setIdAdresseDebitTaxes(String idAdresseDebitTaxes) {
        this.idAdresseDebitTaxes = idAdresseDebitTaxes;
    }

    /**
     * @param idTypeTraitementBV the idTypeTraitementBV to set
     */
    public void setIdTypeTraitementBV(String idTypeTraitementBV) {
        this.idTypeTraitementBV = idTypeTraitementBV;
    }

    /**
     * @param idTypeTraitementLS the idTypeTraitementLS to set
     */
    public void setIdTypeTraitementLS(String idTypeTraitementLS) {
        this.idTypeTraitementLS = idTypeTraitementLS;
    }

    /**
     * @param idTypeTraitementOG the idTypeTraitementOG to set
     */
    public void setIdTypeTraitementOG(String idTypeTraitementOG) {
        this.idTypeTraitementOG = idTypeTraitementOG;
    }

    /**
     * @param nomClasseParserBvr the nomClasseParserBvr to set
     */
    public void setNomClasseParserBvr(String nomClasseParserBvr) {
        this.nomClasseParserBvr = nomClasseParserBvr;
    }

    /**
     * @param noAdherentBVR the noAdherentBVR to set
     */
    public void setNoAdherentBVR(String noAdherentBVR) {
        this.noAdherentBVR = noAdherentBVR;
    }

    /**
     * @param nomClasseParserLSV the nomClasseParserLSV to set
     */
    public void setNomClasseParserLSV(String nomClasseParserLSV) {
        this.nomClasseParserLSV = nomClasseParserLSV;
    }

    /**
     * @param noAdherent the noAdherent to set
     */
    public void setNoAdherent(String noAdherent) {
        this.noAdherent = noAdherent;
    }

    /**
     * @param numInterneLsv the numInterneLsv to set
     */
    public void setNumInterneLsv(String numInterneLsv) {
        this.numInterneLsv = numInterneLsv;
    }

    /**
     * @param modeTransfert the modeTransfert to set
     */
    public void setModeTransfert(String modeTransfert) {
        this.modeTransfert = modeTransfert;
    }

}

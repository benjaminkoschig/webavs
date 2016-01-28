package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Décompte des Allocations Différentielles Internationales (ADI)
 * 
 * @author PTA
 * 
 */
public class DecompteAdiModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Année du décompte ADI
     */
    private String anneeDecompte = null;

    /**
     * code de la monnaie
     */
    private String codeMonnaie = null;

    /**
     * date de saisie
     */
    private String dateEtat = null;

    /**
     * date de la réception de la demande
     */
    private String dateReception = null;

    /**
     * Etat du décompte
     */
    private String etatDecompte = null;

    /**
     * identifiant du décompte de l'ADI
     */
    private String idDecompteAdi = null;

    /**
     * identifiant du décompte remplacé par celui-là
     */
    private String idDecompteRemplace = null;

    /**
     * identifiant du dossier
     */
    private String idDossier = null;

    /**
     * idnetifiant de l'entête de la prestation découlant du décompte
     */
    private String idPrestationAdi = null;

    /**
     * identifiant de l'organisme étranger (tiers)
     */
    private String idTiersOrganismeEtranger = null;

    /**
     * Début de la période du décompte
     */
    private String periodeDebut = null;

    /**
     * Fin de la période du décompte
     */
    private String periodeFin = null;

    /**
     * Texte accompagnant
     */
    private String texteLibre = null;

    /**
     * @return the anneeDecompte
     */
    public String getAnneeDecompte() {
        return anneeDecompte;
    }

    /**
     * @return the codeMonnaie
     */
    public String getCodeMonnaie() {
        return codeMonnaie;
    }

    /**
     * @return the dateEtat
     */
    public String getDateEtat() {
        return dateEtat;
    }

    /**
     * @return the dateReception
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * @return the etatDecompte
     */
    public String getEtatDecompte() {
        return etatDecompte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return idDecompteAdi;
    }

    /**
     * @return the idDecompteAdi
     */
    public String getIdDecompteAdi() {
        return idDecompteAdi;
    }

    /**
     * @return the idDecompteRemplace
     */
    public String getIdDecompteRemplace() {
        return idDecompteRemplace;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idPrestationAdi
     */
    public String getIdPrestationAdi() {
        return idPrestationAdi;
    }

    /**
     * @return the idTiersOrganismeEtranger
     */
    public String getIdTiersOrganismeEtranger() {
        return idTiersOrganismeEtranger;
    }

    /**
     * @return the periodeDebut
     */
    public String getPeriodeDebut() {
        return periodeDebut;
    }

    /**
     * @return the periodeFin
     */
    public String getPeriodeFin() {
        return periodeFin;
    }

    /**
     * @return the texteLibre
     */
    public String getTexteLibre() {
        return texteLibre;
    }

    /**
     * @param anneeDecompte
     *            the anneeDecompte to set
     */
    public void setAnneeDecompte(String anneeDecompte) {
        this.anneeDecompte = anneeDecompte;
    }

    /**
     * @param codeMonnaie
     *            the codeMonnaie to set
     */
    public void setCodeMonnaie(String codeMonnaie) {
        this.codeMonnaie = codeMonnaie;
    }

    /**
     * @param dateEtat
     *            the dateEtat to set
     */
    public void setDateEtat(String dateEtat) {
        this.dateEtat = dateEtat;
    }

    /**
     * @param dateReception
     *            the dateReception to set
     */
    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    /**
     * @param etatDecompte
     *            the etatDecompte to set
     */
    public void setEtatDecompte(String etatDecompte) {
        this.etatDecompte = etatDecompte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDecompteAdi = id;

    }

    /**
     * @param idDecompteAdi
     *            the idDecompteAdi to set
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    /**
     * @param idDecompteRemplace
     *            the idDecompteRemplace to set
     */
    public void setIdDecompteRemplace(String idDecompteRemplace) {
        this.idDecompteRemplace = idDecompteRemplace;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idPrestationAdi
     *            the idPrestationAdi to set
     */
    public void setIdPrestationAdi(String idPrestationAdi) {
        this.idPrestationAdi = idPrestationAdi;
    }

    /**
     * @param idTiersOrganismeEtranger
     *            the idTiersOrganismeEtranger to set
     */
    public void setIdTiersOrganismeEtranger(String idTiersOrganismeEtranger) {
        this.idTiersOrganismeEtranger = idTiersOrganismeEtranger;
    }

    /**
     * @param periodeDebut
     *            the periodeDebut to set
     */
    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    /**
     * @param periodeFin
     *            the periodeFin to set
     */
    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    /**
     * @param texteLibre
     *            the texteLibre to set
     */
    public void setTexteLibre(String texteLibre) {
        this.texteLibre = texteLibre;
    }
}

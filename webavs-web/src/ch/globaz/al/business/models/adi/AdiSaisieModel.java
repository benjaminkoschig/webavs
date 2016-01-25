package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle représentant une ligne saisie pour les ADI selon les attestations de l'organisme étranger
 * 
 * @author GMO
 * 
 */
public class AdiSaisieModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Clé étrangère du décompte auquel appartient la saisie
     */
    private String idDecompteAdi = null;
    /**
     * Clé étrangère de l'enfant pour lequel la saisie est faite
     */
    private String idEnfant = null;
    /**
     * id de la saisie (clé primaire)
     */
    private String idSaisieAdi = null;
    /**
     * montant saisi
     */
    private String montantSaisi = null;
    /**
     * début de la période sur laquelle porte la saisie
     */
    private String periodeA = null;
    /**
     * fin de la période sur laquelle porte la saisie
     */
    private String periodeDe = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idSaisieAdi;
    }

    /**
     * 
     * @return idDecompteAdi
     */
    public String getIdDecompteAdi() {
        return idDecompteAdi;
    }

    /**
     * @return idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * @return idSaisie
     */
    public String getIdSaisieAdi() {
        return idSaisieAdi;
    }

    /**
     * @return montantSaisi
     */
    public String getMontantSaisi() {
        return montantSaisi;
    }

    /**
     * @return periodeA
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * @return periodeDe
     */
    public String getPeriodeDe() {
        return periodeDe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idSaisieAdi = id;

    }

    /**
     * @param idDecompteAdi
     *            id du décompte adi lié à la saisie
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    /**
     * @param idEnfant
     *            id de l'enfant pour lequel la saisie a été faite
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * @param idSaisie
     *            id de la saisie
     */
    public void setIdSaisieAdi(String idSaisie) {
        idSaisieAdi = idSaisie;
    }

    /**
     * @param montantSaisi
     *            montant saisi
     */
    public void setMontantSaisi(String montantSaisi) {
        this.montantSaisi = montantSaisi;
    }

    /**
     * @param periodeA
     *            fin de la période
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * @param periodeDe
     *            début de la période
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

}

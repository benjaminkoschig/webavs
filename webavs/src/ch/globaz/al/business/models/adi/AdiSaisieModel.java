package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le repr�sentant une ligne saisie pour les ADI selon les attestations de l'organisme �tranger
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
     * Cl� �trang�re du d�compte auquel appartient la saisie
     */
    private String idDecompteAdi = null;
    /**
     * Cl� �trang�re de l'enfant pour lequel la saisie est faite
     */
    private String idEnfant = null;
    /**
     * id de la saisie (cl� primaire)
     */
    private String idSaisieAdi = null;
    /**
     * montant saisi
     */
    private String montantSaisi = null;
    /**
     * d�but de la p�riode sur laquelle porte la saisie
     */
    private String periodeA = null;
    /**
     * fin de la p�riode sur laquelle porte la saisie
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
     *            id du d�compte adi li� � la saisie
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    /**
     * @param idEnfant
     *            id de l'enfant pour lequel la saisie a �t� faite
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
     *            fin de la p�riode
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * @param periodeDe
     *            d�but de la p�riode
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

}

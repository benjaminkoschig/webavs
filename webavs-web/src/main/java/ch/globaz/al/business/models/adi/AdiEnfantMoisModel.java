/**
 * 
 */
package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * ADI - Prestations par enfant / mois
 * 
 * @author PTA
 * 
 */
public class AdiEnfantMoisModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * cours du change de la monnaie
     */
    private String coursChangeMonnaie = null;
    /**
     * identifiant du décompte ADI
     */
    private String idDecompteAdi = null;
    /**
     * identifiant du droit
     */
    private String idDroit = null;
    /**
     * identifiant de la prestation
     */
    private String idEnfantMoisAdi = null;

    /**
     * période (le mois )de l'ADI
     */
    private String moisPeriode = null;

    /**
     * montant de l'ADI
     */
    private String montantAdi = null;

    /**
     * montant versé par la caisse ETR pour chaque enfant pour les alloc. supplément
     */

    /**
     * montant versé par la caisse Suisse pour l'allocation de l'enfant
     */
    private String montantAllocCH = null;

    /**
     * montant en monnaie étrangère versée pour la caisse pour l'allocation de l'enfant
     */
    private String montantAllocEtr = null;

    /**
     * montant total versé par la caisse (allocation + supplément réparti)
     */
    private String montantCHTotal = null;

    /**
     * montant total versé par la caisse étrangère en monnaie ETR (allocation + supplément réparti)
     */
    private String montantEtrTotal = null;

    /**
     * montant total versé par la caisse étrangère converti en francs Suisse
     */
    private String montantEtrTotalEnCh = null;

    /**
     * montant versé par la caisse par enfant pour les allocations supplément
     */
    private String montantRepartiCH = null;

    /**
     * montant versé par la caisse pour tous les enfants pour les allocations supplément
     */
    private String montantRepartiCHTotal = null;

    /**
     * montant versé par la caisse ETR pour chaque enfant pour les alloc. supplément
     */
    private String montantRepartiEtr = null;

    /**
     * montant versé par la caisse ETR pour tous les enfants pour les alloc. supplément
     */
    private String montantRepartiEtrTotal = null;

    /**
     * nombre d'enfants dans la famille
     */
    private String nbrEnfantFamille = null;

    /**
     * @return the coursChangeMonnaie
     */
    public String getCoursChangeMonnaie() {
        return coursChangeMonnaie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idEnfantMoisAdi;
    }

    /**
     * @return the idDecompteAdi
     */
    public String getIdDecompteAdi() {
        return idDecompteAdi;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idPrestationAdi
     */
    public String getIdEnfantMoisAdi() {
        return idEnfantMoisAdi;
    }

    /**
     * @return the moisPeriode
     */
    public String getMoisPeriode() {
        return moisPeriode;
    }

    /**
     * @return the montantAdi
     */
    public String getMontantAdi() {
        return montantAdi;
    }

    /**
     * @return the montantAllocCH
     */
    public String getMontantAllocCH() {
        return montantAllocCH;
    }

    /**
     * @return the montantAllocEtr
     */
    public String getMontantAllocEtr() {
        return montantAllocEtr;
    }

    /**
     * @return the montantCHTotal
     */
    public String getMontantCHTotal() {
        return montantCHTotal;
    }

    /**
     * 
     * @return montantEtrTotal
     */
    public String getMontantEtrTotal() {
        return montantEtrTotal;
    }

    /**
     * @return the montantEtrTotalEnCh
     */

    public String getMontantEtrTotalEnCh() {
        return montantEtrTotalEnCh;
    }

    /**
     * 
     * @return montantRepartiCH
     */
    public String getMontantRepartiCH() {
        return montantRepartiCH;
    }

    /**
     * 
     * @return montantRepartiCHTotal
     */
    public String getMontantRepartiCHTotal() {
        return montantRepartiCHTotal;
    }

    /**
     * 
     * @return montantRepartiEtr
     */
    public String getMontantRepartiEtr() {
        return montantRepartiEtr;
    }

    /**
     * 
     * @return montantRepartiEtrTotal
     */
    public String getMontantRepartiEtrTotal() {
        return montantRepartiEtrTotal;
    }

    /**
     * @return the nbrEnfantFamille
     */
    public String getNbrEnfantFamille() {
        return nbrEnfantFamille;
    }

    /**
     * @param coursChangeMonnaie
     *            the coursChangeMonnaie to set
     */
    public void setCoursChangeMonnaie(String coursChangeMonnaie) {
        this.coursChangeMonnaie = coursChangeMonnaie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idEnfantMoisAdi = id;

    }

    /**
     * @param idDecompteAdi
     *            the idDecompteAdi to set
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idEnfantMoisAdi
     *            the idEnfantMoisAdi to set
     */
    public void setIdEnfantMoisAdi(String idEnfantMoisAdi) {
        this.idEnfantMoisAdi = idEnfantMoisAdi;
    }

    /**
     * @param moisPeriode
     *            the moisPeriode to set
     */
    public void setMoisPeriode(String moisPeriode) {
        this.moisPeriode = moisPeriode;
    }

    /**
     * @param montantAdi
     *            the montantAdi to set
     */
    public void setMontantAdi(String montantAdi) {
        this.montantAdi = montantAdi;
    }

    /**
     * @param montantAllocCH
     *            the montantAllocCH to set
     */
    public void setMontantAllocCH(String montantAllocCH) {
        this.montantAllocCH = montantAllocCH;
    }

    /**
     * @param montantAllocEtr
     *            the montantAllocEtr to set
     */
    public void setMontantAllocEtr(String montantAllocEtr) {
        this.montantAllocEtr = montantAllocEtr;
    }

    /**
     * @param montantCHTotal
     *            the montantCHTotal to set
     */
    public void setMontantCHTotal(String montantCHTotal) {
        this.montantCHTotal = montantCHTotal;
    }

    /**
     * 
     * @param montantEtrangerTotal
     *            le montant étranger total
     */
    public void setMontantEtrTotal(String montantEtrangerTotal) {
        montantEtrTotal = montantEtrangerTotal;
    }

    /**
     * @param montantEtrangerEnCh
     *            the montantEtrangerEnCh to set
     */
    public void setMontantEtrTotalEnCh(String montantEtrangerEnCh) {
        montantEtrTotalEnCh = montantEtrangerEnCh;
    }

    /**
     * 
     * @param montantRepartiCH
     *            le montant réparti CH
     */
    public void setMontantRepartiCH(String montantRepartiCH) {
        this.montantRepartiCH = montantRepartiCH;
    }

    /**
     * 
     * @param montantRepartiCHTotal
     *            le montant réparti CH Total
     */
    public void setMontantRepartiCHTotal(String montantRepartiCHTotal) {
        this.montantRepartiCHTotal = montantRepartiCHTotal;
    }

    /**
     * 
     * @param montantRepartiEtr
     *            le montant réparti ETR
     */
    public void setMontantRepartiEtr(String montantRepartiEtr) {
        this.montantRepartiEtr = montantRepartiEtr;
    }

    /**
     * 
     * @param montantRepartiEtrTotal
     *            le montant réparti ETR total
     */
    public void setMontantRepartiEtrTotal(String montantRepartiEtrTotal) {
        this.montantRepartiEtrTotal = montantRepartiEtrTotal;
    }

    /**
     * @param nbrEnfantFamille
     *            the nbrEnfantFamille to set
     */
    public void setNbrEnfantFamille(String nbrEnfantFamille) {
        this.nbrEnfantFamille = nbrEnfantFamille;
    }

}

/**
 * 
 */
package ch.globaz.pegasus.business.models.dessaisissement;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;

/**
 * @author ECO
 * 
 */
public class DessaisissementFortuneAuto extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String compagnieRenteViagere = null;
    private String csGenreTitre = null;
    private String csTypeFortuneAutreFortuneMobiliere = null;
    private String debiteurPretEnversTiers = null;
    private String designationBetail = null;
    private String designationTitre = null;
    private String designationVehicule = null;
    private String excedentRenteViagere = null;
    private String ibanCompteBancaireCCP = null;
    private String IPCapitalLPP = null;
    private MembreFamilleEtendu membreFamilleEtendu = null;
    private String montantAutreFortuneMobiliere = null;
    private String montantBetail = null;
    private String montantBienImmobilierNonHabitable = null;
    private String montantBienImmobilierNonPrincipal = null;
    private String montantBienImmobilierPrincipal = null;
    private String montantCapitalLPP = null;
    private String montantCompteBancaireCCP = null;
    private String montantMarchandisesStock = null;
    private String montantNumeraire = null;
    private String montantPretEnversTiers = null;
    private String montantTitre = null;
    private String montantValeurRachatAssuranceVie = null;
    private String montantValeurRachatRenteViagere = null;
    private String montantVehicule = null;
    private String nomCompagnieAssuranceVie = null;
    private SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader = null;
    private String typeBienImmobilierNonHabitable = null;
    private String typeBienImmobilierNonPrincipal = null;
    private String typeBienImmobilierPrincipal = null;

    /**
	 * 
	 */
    public DessaisissementFortuneAuto() {
        super();
        simpleDonneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        membreFamilleEtendu = new MembreFamilleEtendu();
    }

    /**
     * @return the compagnieRenteViagere
     */
    public String getCompagnieRenteViagere() {
        return compagnieRenteViagere;
    }

    public String getCsGenreTitre() {
        return csGenreTitre;
    }

    /**
     * @return the csTypeFortuneAutreFortuneMobiliere
     */
    public String getCsTypeFortuneAutreFortuneMobiliere() {
        return csTypeFortuneAutreFortuneMobiliere;
    }

    /**
     * @return the debiteurPretEnversTiers
     */
    public String getDebiteurPretEnversTiers() {
        return debiteurPretEnversTiers;
    }

    /**
     * @return the designationBetail
     */
    public String getDesignationBetail() {
        return designationBetail;
    }

    /**
     * @return the designationTitre
     */
    public String getDesignationTitre() {
        return designationTitre;
    }

    /**
     * @return the designationVehicule
     */
    public String getDesignationVehicule() {
        return designationVehicule;
    }

    /**
     * @return the excedentRenteViagere
     */
    public String getExcedentRenteViagere() {
        return excedentRenteViagere;
    }

    /**
     * @return the ibanCompteBancaireCCP
     */
    public String getIbanCompteBancaireCCP() {
        return ibanCompteBancaireCCP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDonneeFinanciereHeader.getId();
    }

    /**
     * @return the iPCapitalLPP
     */
    public String getIPCapitalLPP() {
        return IPCapitalLPP;
    }

    /**
     * @return the membreFamilleEtendu
     */
    public MembreFamilleEtendu getMembreFamilleEtendu() {
        return membreFamilleEtendu;
    }

    /**
     * @return the montantAutreFortuneMobiliere
     */
    public String getMontantAutreFortuneMobiliere() {
        return montantAutreFortuneMobiliere;
    }

    /**
     * @return the montantBetail
     */
    public String getMontantBetail() {
        return montantBetail;
    }

    /**
     * @return the montantBienImmobilierNonHabitable
     */
    public String getMontantBienImmobilierNonHabitable() {
        return montantBienImmobilierNonHabitable;
    }

    /**
     * @return the montantBienImmobilierNonPrincipal
     */
    public String getMontantBienImmobilierNonPrincipal() {
        return montantBienImmobilierNonPrincipal;
    }

    /**
     * @return the montantBienImmobilierPrincipal
     */
    public String getMontantBienImmobilierPrincipal() {
        return montantBienImmobilierPrincipal;
    }

    /**
     * @return the montantCapitalLPP
     */
    public String getMontantCapitalLPP() {
        return montantCapitalLPP;
    }

    /**
     * @return the montantCompteBancaireCCP
     */
    public String getMontantCompteBancaireCCP() {
        return montantCompteBancaireCCP;
    }

    /**
     * @return the montantMarchandisesStock
     */
    public String getMontantMarchandisesStock() {
        return montantMarchandisesStock;
    }

    /**
     * @return the montantNumeraire
     */
    public String getMontantNumeraire() {
        return montantNumeraire;
    }

    /**
     * @return the montantPretEnversTiers
     */
    public String getMontantPretEnversTiers() {
        return montantPretEnversTiers;
    }

    /**
     * @return the montantTitre
     */
    public String getMontantTitre() {
        return montantTitre;
    }

    /**
     * @return the montantValeurRachatAssuranceVie
     */
    public String getMontantValeurRachatAssuranceVie() {
        return montantValeurRachatAssuranceVie;
    }

    /**
     * @return the montantValeurRachatRenteViagere
     */
    public String getMontantValeurRachatRenteViagere() {
        return montantValeurRachatRenteViagere;
    }

    /**
     * @return the montantVehicule
     */
    public String getMontantVehicule() {
        return montantVehicule;
    }

    /**
     * @return the nomCompagnieAssuranceVie
     */
    public String getNomCompagnieAssuranceVie() {
        return nomCompagnieAssuranceVie;
    }

    /**
     * @return the simpleDessaisissementFortune
     */
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return simpleDonneeFinanciereHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDonneeFinanciereHeader.getSpy();
    }

    /**
     * @return the typeBienImmobilierNonHabitable
     */
    public String getTypeBienImmobilierNonHabitable() {
        return typeBienImmobilierNonHabitable;
    }

    /**
     * @return the typeBienImmobilierNonPrincipal
     */
    public String getTypeBienImmobilierNonPrincipal() {
        return typeBienImmobilierNonPrincipal;
    }

    /**
     * @return the typeBienImmobilierPrincipal
     */
    public String getTypeBienImmobilierPrincipal() {
        return typeBienImmobilierPrincipal;
    }

    /**
     * @param compagnieRenteViagere
     *            the compagnieRenteViagere to set
     */
    public void setCompagnieRenteViagere(String compagnieRenteViagere) {
        this.compagnieRenteViagere = compagnieRenteViagere;
    }

    public void setCsGenreTitre(String csGenreTitre) {
        this.csGenreTitre = csGenreTitre;
    }

    /**
     * @param csTypeFortuneAutreFortuneMobiliere
     *            the csTypeFortuneAutreFortuneMobiliere to set
     */
    public void setCsTypeFortuneAutreFortuneMobiliere(String csTypeFortuneAutreFortuneMobiliere) {
        this.csTypeFortuneAutreFortuneMobiliere = csTypeFortuneAutreFortuneMobiliere;
    }

    /**
     * @param debiteurPretEnversTiers
     *            the debiteurPretEnversTiers to set
     */
    public void setDebiteurPretEnversTiers(String debiteurPretEnversTiers) {
        this.debiteurPretEnversTiers = debiteurPretEnversTiers;
    }

    /**
     * @param designationBetail
     *            the designationBetail to set
     */
    public void setDesignationBetail(String designationBetail) {
        this.designationBetail = designationBetail;
    }

    /**
     * @param designationTitre
     *            the designationTitre to set
     */
    public void setDesignationTitre(String designationTitre) {
        this.designationTitre = designationTitre;
    }

    /**
     * @param designationVehicule
     *            the designationVehicule to set
     */
    public void setDesignationVehicule(String designationVehicule) {
        this.designationVehicule = designationVehicule;
    }

    /**
     * @param excedentRenteViagere
     *            the excedentRenteViagere to set
     */
    public void setExcedentRenteViagere(String excedentRenteViagere) {
        this.excedentRenteViagere = excedentRenteViagere;
    }

    /**
     * @param ibanCompteBancaireCCP
     *            the ibanCompteBancaireCCP to set
     */
    public void setIbanCompteBancaireCCP(String ibanCompteBancaireCCP) {
        this.ibanCompteBancaireCCP = ibanCompteBancaireCCP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDonneeFinanciereHeader.setId(id);
    }

    /**
     * @param iPCapitalLPP
     *            the iPCapitalLPP to set
     */
    public void setIPCapitalLPP(String iPCapitalLPP) {
        IPCapitalLPP = iPCapitalLPP;
    }

    /**
     * @param membreFamilleEtendu
     *            the membreFamilleEtendu to set
     */
    public void setMembreFamilleEtendu(MembreFamilleEtendu membreFamilleEtendu) {
        this.membreFamilleEtendu = membreFamilleEtendu;
    }

    /**
     * @param montantAutreFortuneMobiliere
     *            the montantAutreFortuneMobiliere to set
     */
    public void setMontantAutreFortuneMobiliere(String montantAutreFortuneMobiliere) {
        this.montantAutreFortuneMobiliere = montantAutreFortuneMobiliere;
    }

    /**
     * @param montantBetail
     *            the montantBetail to set
     */
    public void setMontantBetail(String montantBetail) {
        this.montantBetail = montantBetail;
    }

    /**
     * @param montantBienImmobilierNonHabitable
     *            the montantBienImmobilierNonHabitable to set
     */
    public void setMontantBienImmobilierNonHabitable(String montantBienImmobilierNonHabitable) {
        this.montantBienImmobilierNonHabitable = montantBienImmobilierNonHabitable;
    }

    /**
     * @param montantBienImmobilierNonPrincipal
     *            the montantBienImmobilierNonPrincipal to set
     */
    public void setMontantBienImmobilierNonPrincipal(String montantBienImmobilierNonPrincipal) {
        this.montantBienImmobilierNonPrincipal = montantBienImmobilierNonPrincipal;
    }

    /**
     * @param montantBienImmobilierPrincipal
     *            the montantBienImmobilierPrincipal to set
     */
    public void setMontantBienImmobilierPrincipal(String montantBienImmobilierPrincipal) {
        this.montantBienImmobilierPrincipal = montantBienImmobilierPrincipal;
    }

    /**
     * @param montantCapitalLPP
     *            the montantCapitalLPP to set
     */
    public void setMontantCapitalLPP(String montantCapitalLPP) {
        this.montantCapitalLPP = montantCapitalLPP;
    }

    /**
     * @param montantCompteBancaireCCP
     *            the montantCompteBancaireCCP to set
     */
    public void setMontantCompteBancaireCCP(String montantCompteBancaireCCP) {
        this.montantCompteBancaireCCP = montantCompteBancaireCCP;
    }

    /**
     * @param montantMarchandisesStock
     *            the montantMarchandisesStock to set
     */
    public void setMontantMarchandisesStock(String montantMarchandisesStock) {
        this.montantMarchandisesStock = montantMarchandisesStock;
    }

    /**
     * @param montantNumeraire
     *            the montantNumeraire to set
     */
    public void setMontantNumeraire(String montantNumeraire) {
        this.montantNumeraire = montantNumeraire;
    }

    /**
     * @param montantPretEnversTiers
     *            the montantPretEnversTiers to set
     */
    public void setMontantPretEnversTiers(String montantPretEnversTiers) {
        this.montantPretEnversTiers = montantPretEnversTiers;
    }

    /**
     * @param montantTitre
     *            the montantTitre to set
     */
    public void setMontantTitre(String montantTitre) {
        this.montantTitre = montantTitre;
    }

    /**
     * @param montantValeurRachatAssuranceVie
     *            the montantValeurRachatAssuranceVie to set
     */
    public void setMontantValeurRachatAssuranceVie(String montantValeurRachatAssuranceVie) {
        this.montantValeurRachatAssuranceVie = montantValeurRachatAssuranceVie;
    }

    /**
     * @param montantValeurRachatRenteViagere
     *            the montantValeurRachatRenteViagere to set
     */
    public void setMontantValeurRachatRenteViagere(String montantValeurRachatRenteViagere) {
        this.montantValeurRachatRenteViagere = montantValeurRachatRenteViagere;
    }

    /**
     * @param montantVehicule
     *            the montantVehicule to set
     */
    public void setMontantVehicule(String montantVehicule) {
        this.montantVehicule = montantVehicule;
    }

    /**
     * @param nomCompagnieAssuranceVie
     *            the nomCompagnieAssuranceVie to set
     */
    public void setNomCompagnieAssuranceVie(String nomCompagnieAssuranceVie) {
        this.nomCompagnieAssuranceVie = nomCompagnieAssuranceVie;
    }

    /**
     * @param simpleDessaisissementFortune
     *            the simpleDessaisissementFortune to set
     */
    public void setSimpleDonneeFinanciereHeader(SimpleDonneeFinanciereHeader simpleDessaisissementFortune) {
        simpleDonneeFinanciereHeader = simpleDessaisissementFortune;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDonneeFinanciereHeader.setSpy(spy);
    }

    /**
     * @param typeBienImmobilierNonHabitable
     *            the typeBienImmobilierNonHabitable to set
     */
    public void setTypeBienImmobilierNonHabitable(String typeBienImmobilierNonHabitable) {
        this.typeBienImmobilierNonHabitable = typeBienImmobilierNonHabitable;
    }

    /**
     * @param typeBienImmobilierNonPrincipal
     *            the typeBienImmobilierNonPrincipal to set
     */
    public void setTypeBienImmobilierNonPrincipal(String typeBienImmobilierNonPrincipal) {
        this.typeBienImmobilierNonPrincipal = typeBienImmobilierNonPrincipal;
    }

    /**
     * @param typeBienImmobilierPrincipal
     *            the typeBienImmobilierPrincipal to set
     */
    public void setTypeBienImmobilierPrincipal(String typeBienImmobilierPrincipal) {
        this.typeBienImmobilierPrincipal = typeBienImmobilierPrincipal;
    }

}

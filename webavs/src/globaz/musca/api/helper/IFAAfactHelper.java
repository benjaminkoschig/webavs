package globaz.musca.api.helper;

import globaz.framework.util.FWCurrency;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.musca.api.IFAAfact;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IFAAfactHelper extends GlobazHelper implements IFAAfact {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IFAAfactHelper
     */
    public IFAAfactHelper() {
        super("globaz.musca.db.facturation.FAAfact");
    }

    /**
     * Constructeur du type IFAAfactHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IFAAfactHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IFAAfactHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IFAAfactHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public java.lang.String getAnneeCotisation() {
        return (java.lang.String) _getValueObject().getProperty("anneeCotisation");
    }

    @Override
    public String getCanton() {
        return (String) _getValueObject().getProperty("canton");
    }

    @Override
    public java.lang.String getDebutPeriode() {
        return (java.lang.String) _getValueObject().getProperty("debutPeriode");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:50:06)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescriptionDecompte() {
        return (String) _getValueObject().getProperty("descriptionDecompte");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:24:17)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescriptionTiers() {
        return (String) _getValueObject().getProperty("descriptionTiers");
    }

    @Override
    public java.lang.String getFinPeriode() {
        return (java.lang.String) _getValueObject().getProperty("finPeriode");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdAfact() {
        return (java.lang.String) _getValueObject().getProperty("idAfact");
    }

    @Override
    public String getIdAssurance() {
        return (String) _getValueObject().getProperty("idAssurance");
    }

    @Override
    public java.lang.String getIdEnteteFacture() {
        return (java.lang.String) _getValueObject().getProperty("idEnteteFacture");
    }

    @Override
    public java.lang.String getIdExterneDebiteurCompensation() {
        return (java.lang.String) _getValueObject().getProperty("idExterneDebiteurCompensation");
    }

    @Override
    public java.lang.String getIdExterneFactureCompensation() {
        return (java.lang.String) _getValueObject().getProperty("idExterneFactureCompensation");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 17:48:07)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdExterneRubrique() {
        return (java.lang.String) _getValueObject().getProperty("idExterneRubrique");
    }

    @Override
    public java.lang.String getIdModuleFacturation() {
        return (java.lang.String) _getValueObject().getProperty("idModuleFacturation");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdPassage() {
        return (java.lang.String) _getValueObject().getProperty("idPassage");
    }

    @Override
    public java.lang.String getIdRemarque() {
        return (java.lang.String) _getValueObject().getProperty("idRemarque");
    }

    @Override
    public java.lang.String getIdRoleDebiteurCompensation() {
        return (java.lang.String) _getValueObject().getProperty("idRoleDebiteurCompensation");
    }

    @Override
    public java.lang.String getIdRubrique() {
        return (java.lang.String) _getValueObject().getProperty("idRubrique");
    }

    @Override
    public String getIdSection() {
        return (String) _getValueObject().getProperty("idSection");
    }

    @Override
    public java.lang.String getIdTiersDebiteurCompensation() {
        return (java.lang.String) _getValueObject().getProperty("idTiersDebiteurCompensation");
    }

    @Override
    public java.lang.String getIdTypeAfact() {
        return (java.lang.String) _getValueObject().getProperty("idTypeAfact");
    }

    @Override
    public java.lang.String getIdTypeFactureCompensation() {
        return (java.lang.String) _getValueObject().getProperty("idTypeFactureCompensation");
    }

    @Override
    public java.lang.String getLibelle() {
        return (java.lang.String) _getValueObject().getProperty("libelle");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:31:20)
     * 
     * @return java.lang.String
     */
    @Override
    public String getLibelleRubrique() {
        return (String) _getValueObject().getProperty("libelleRubrique");
    }

    /**
     * Récupère le libellé sur facture (libellé forcé ou description du compte Date de création : (12.12.2002 14:32:37)
     * 
     * @return java.lang.String Le libellé sur facture
     */
    @Override
    public String getLibelleSurFacture() {
        return (String) _getValueObject().getProperty("libelleSurFacture");
    }

    @Override
    public java.lang.String getMasseDejaFacturee() {
        return (java.lang.String) _getValueObject().getProperty("masseDejaFacturee");
    }

    @Override
    public java.lang.String getMasseFacture() {
        return (java.lang.String) _getValueObject().getProperty("masseFacture");
    }

    @Override
    public java.lang.String getMasseInitiale() {
        return (java.lang.String) _getValueObject().getProperty("masseInitiale");
    }

    @Override
    public java.lang.String getMontantDejaFacture() {
        return (java.lang.String) _getValueObject().getProperty("montantDejaFacture");
    }

    @Override
    public java.lang.String getMontantFacture() {
        return (java.lang.String) _getValueObject().getProperty("montantFacture");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 16:24:56)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    @Override
    public FWCurrency getMontantFactureToCurrency() {
        return (FWCurrency) _getValueObject().getProperty("montantFactureToCurrency");
    }

    @Override
    public java.lang.String getMontantInitial() {
        return (java.lang.String) _getValueObject().getProperty("montantInitial");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNomTiers() {
        return (String) _getValueObject().getProperty("nomTiers");
    }

    @Override
    public java.lang.String getReferenceExterne() {
        return (java.lang.String) _getValueObject().getProperty("referenceExterne");
    }

    @Override
    public java.lang.String getRemarque() {
        return (java.lang.String) _getValueObject().getProperty("remarque");
    }

    @Override
    public java.lang.String getTauxDejaFacture() {
        return (java.lang.String) _getValueObject().getProperty("tauxDejaFacture");
    }

    @Override
    public java.lang.String getTauxFacture() {
        return (java.lang.String) _getValueObject().getProperty("tauxFacture");
    }

    @Override
    public java.lang.String getTauxInitial() {
        return (java.lang.String) _getValueObject().getProperty("tauxInitial");
    }

    @Override
    public java.lang.String getUser() {
        return (java.lang.String) _getValueObject().getProperty("user");
    }

    @Override
    public void setAnneeCotisation(java.lang.String newAnneeCotisation) {
        _getValueObject().setProperty("anneeCotisation", newAnneeCotisation);
    }

    @Override
    public void setAQuittancer(java.lang.Boolean newAQuittancer) {
        _getValueObject().setProperty("aQuittancer", newAQuittancer);
    }

    @Override
    public void setCanton(String canton) {
        _getValueObject().setProperty("canton", canton);
    }

    @Override
    public void setDebutPeriode(java.lang.String newDebutPeriode) {
        _getValueObject().setProperty("debutPeriode", newDebutPeriode);
    }

    @Override
    public void setFinPeriode(java.lang.String newFinPeriode) {
        _getValueObject().setProperty("finPeriode", newFinPeriode);
    }

    /**
     * Setter
     */
    @Override
    public void setIdAfact(java.lang.String newIdAfact) {
        _getValueObject().setProperty("idAfact", newIdAfact);
    }

    @Override
    public void setIdAssurance(String idAssurance) {
        _getValueObject().setProperty("idAssurance", idAssurance);
    }

    @Override
    public void setIdEnteteFacture(java.lang.String newIdEnteteFacture) {
        _getValueObject().setProperty("idEnteteFacture", newIdEnteteFacture);
    }

    @Override
    public void setIdExterneDebiteurCompensation(java.lang.String newIdExterneDebiteurCompensation) {
        _getValueObject().setProperty("idExterneDebiteurCompensation", newIdExterneDebiteurCompensation);
    }

    @Override
    public void setIdExterneFactureCompensation(java.lang.String newIdExterneFactureCompensation) {
        _getValueObject().setProperty("idExterneFactureCompensation", newIdExterneFactureCompensation);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.12.2002 17:48:07)
     * 
     * @param newIdExterneRubrique
     *            java.lang.String
     */
    @Override
    public void setIdExterneRubrique(java.lang.String newIdExterneRubrique) {
        _getValueObject().setProperty("idExterneRubrique", newIdExterneRubrique);
    }

    @Override
    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        _getValueObject().setProperty("idModuleFacturation", newIdModuleFacturation);
    }

    /**
     * Setter
     */
    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        _getValueObject().setProperty("idPassage", newIdPassage);
    }

    @Override
    public void setIdRemarque(java.lang.String newIdRemarque) {
        _getValueObject().setProperty("idRemarque", newIdRemarque);
    }

    @Override
    public void setIdRoleDebiteurCompensation(java.lang.String newIdRoleDebiteurCompensation) {
        _getValueObject().setProperty("idRoleDebiteurCompensation", newIdRoleDebiteurCompensation);
    }

    @Override
    public void setIdRubrique(java.lang.String newIdRubrique) {
        _getValueObject().setProperty("idRubrique", newIdRubrique);
    }

    @Override
    public void setIdSection(String idSection) {
        _getValueObject().setProperty("idSection", idSection);
    }

    @Override
    public void setIdTiersDebiteurCompensation(java.lang.String newIdTiersDebiteurCompensation) {
        _getValueObject().setProperty("idTiersDebiteurCompensation", newIdTiersDebiteurCompensation);
    }

    @Override
    public void setIdTypeAfact(java.lang.String newIdTypeAfact) {
        _getValueObject().setProperty("idTypeAfact", newIdTypeAfact);
    }

    @Override
    public void setIdTypeFactureCompensation(java.lang.String newIdTypeFactureCompensation) {
        _getValueObject().setProperty("idTypeFactureCompensation", newIdTypeFactureCompensation);
    }

    @Override
    public void setLibelle(java.lang.String newLibelle) {
        _getValueObject().setProperty("libelle", newLibelle);
    }

    @Override
    public void setMasseDejaFacturee(java.lang.String newMasseDejaFacturee) {
        _getValueObject().setProperty("masseDejaFacturee", newMasseDejaFacturee);
    }

    @Override
    public void setMasseFacture(java.lang.String newMasseFacture) {
        _getValueObject().setProperty("masseFacture", newMasseFacture);
    }

    @Override
    public void setMasseInitiale(java.lang.String newMasseInitiale) {
        _getValueObject().setProperty("masseInitiale", newMasseInitiale);
    }

    @Override
    public void setMontantDejaFacture(java.lang.String newMontantDejaFacture) {
        _getValueObject().setProperty("montantDejaFacture", newMontantDejaFacture);
    }

    @Override
    public void setMontantFacture(java.lang.String newMontantFacture) {
        _getValueObject().setProperty("montantFacture", newMontantFacture);
    }

    @Override
    public void setMontantInitial(java.lang.String newMontantInitial) {
        _getValueObject().setProperty("montantInitial", newMontantInitial);
    }

    @Override
    public void setNonComptabilisable(java.lang.Boolean newNonComptabilisable) {
        _getValueObject().setProperty("nonComptabilisable", newNonComptabilisable);
    }

    @Override
    public void setNonImprimable(java.lang.Boolean newNonImprimable) {
        _getValueObject().setProperty("nonImprimable", newNonImprimable);
    }

    @Override
    public void setReferenceExterne(java.lang.String newReferenceExterne) {
        _getValueObject().setProperty("referenceExterne", newReferenceExterne);
    }

    @Override
    public void setRemarque(java.lang.String newRemarque) {
        _getValueObject().setProperty("remarque", newRemarque);
    }

    @Override
    public void setTauxDejaFacture(java.lang.String newTauxDejaFacture) {
        _getValueObject().setProperty("tauxDejaFacture", newTauxDejaFacture);
    }

    @Override
    public void setTauxFacture(java.lang.String newTauxFacture) {
        _getValueObject().setProperty("tauxFacture", newTauxFacture);
    }

    @Override
    public void setTauxInitial(java.lang.String newTauxInitial) {
        _getValueObject().setProperty("tauxInitial", newTauxInitial);
    }

    @Override
    public void setUser(java.lang.String newUser) {
        _getValueObject().setProperty("user", newUser);
    }
}

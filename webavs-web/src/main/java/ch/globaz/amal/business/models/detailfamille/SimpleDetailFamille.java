package ch.globaz.amal.business.models.detailfamille;

import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 *
 */
public class SimpleDetailFamille extends JadeSimpleModel implements Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private AdministrationComplexModel administration = null;
    private String anneeHistorique = null;
    private String anneeRecalcul = null;
    private Boolean annonceCaisseMaladie = null;
    private Boolean codeActif = null;
    private Boolean codeForcer = null;
    private String codeTraitement = null;
    private String codeTraitementDossier = null;
    private String dateAnnonceCaisseMaladie = null;
    private String dateAvisRIP = null;
    private String dateEnvoi = null;
    private String dateModification = null;
    private String dateRecepDemande = null;
    private String dateRecepDemandeRecalcul = null;
    private String debutDroit = null;
    private String finDroit = null;
    private String idContribuable = null;
    private String idDetailFamille = null;
    private String idFamille = null;
    private String montantContribAnnuelle = null;
    private String montantContribSansSuppl = null;
    private String montantContribution = null;
    private String montantContributionAssiste = null;
    private String montantContributionPC = null;
    private String montantExact = null;
    private String montantPrimeAssurance = null;
    private String noAssure = null;
    private String noCaisseMaladie = null;
    private String noLot = null;
    private String noModeles = null;
    private String oldMontantContribAnnuelle = null;
    private Boolean refus = null;
    private String montantSupplement = null;
    private String supplExtra = null;

    private String tauxEnfantCharge = null;
    private String typeAvisRIP = null;
    private String typeDemande = null;
    private String user = null;

    @Override
    public SimpleDetailFamille clone() throws CloneNotSupportedException {
        return (SimpleDetailFamille) super.clone();
    }

    /**
     * Gets the current administration linked to the current detail famille
     *
     * @return
     */
    public AdministrationComplexModel getAdministration() {
        if (!JadeStringUtil.isEmpty(getNoCaisseMaladie()) && (administration == null)) {
            try {
                administration = TIBusinessServiceLocator.getAdministrationService().read(getNoCaisseMaladie());
            } catch (Exception ex) {
                administration = new AdministrationComplexModel();
            }
        } else if (administration == null) {
            administration = new AdministrationComplexModel();
        }
        return administration;
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the anneeRecalcul
     */
    public String getAnneeRecalcul() {
        return anneeRecalcul;
    }

    /**
     * @return the annonceCaisseMaladie
     */
    public Boolean getAnnonceCaisseMaladie() {
        return annonceCaisseMaladie;
    }

    public Boolean getCodeActif() {
        return codeActif;
    }

    /**
     * @return the codeForcer
     */
    public Boolean getCodeForcer() {
        return codeForcer;
    }

    /**
     * @return the codeTraitement
     */
    public String getCodeTraitement() {
        return codeTraitement;
    }

    /**
     * @return the codeTraitementDossier
     */
    public String getCodeTraitementDossier() {
        return codeTraitementDossier;
    }

    /**
     * @return the dateAnnonceCaisseMaladie
     */
    public String getDateAnnonceCaisseMaladie() {
        return dateAnnonceCaisseMaladie;
    }

    /**
     * @return the dateAvisRIP
     */
    public String getDateAvisRIP() {
        return dateAvisRIP;
    }

    /**
     * @return the dateEnvoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * @return the dateModification
     */
    public String getDateModification() {
        return dateModification;
    }

    /**
     * @return the dateRecepDemande
     */
    public String getDateRecepDemande() {
        return dateRecepDemande;
    }

    public String getDateRecepDemandeRecalcul() {
        return dateRecepDemandeRecalcul;
    }

    /**
     * @return the debutDroit
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * @return the finDroit
     */
    public String getFinDroit() {
        return finDroit;
    }

    @Override
    public String getId() {
        return idDetailFamille;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idDetailFamille
     */
    public String getIdDetailFamille() {
        return idDetailFamille;
    }

    /**
     * @return the idFamille
     */
    public String getIdFamille() {
        return idFamille;
    }

    /**
     * @return the montantContribAnnuelle
     */
    public String getMontantContribAnnuelle() {
        return montantContribAnnuelle;
    }

    /**
     * This field doesn't exist in DB. Only for simulation
     *
     * @return
     */
    public String getMontantContribSansSuppl() {
        return montantContribSansSuppl;
    }

    /**
     * @return the montantContribution
     */
    public String getMontantContribution() {
        return montantContribution;
    }

    /**
     * Champs pour accueilir le montant de subside pour un assisté (utilisé uniquement en simulation, n'est pas inséré
     * en DB)
     *
     * @return montantContributionAssiste
     */
    public String getMontantContributionAssiste() {
        return montantContributionAssiste;
    }

    /**
     * Retourne le montant de la contribution avec le suppl extra donc le montant total attribué. Pas en DB.
     *
     * @return
     */
    public String getMontantContributionAvecSupplExtra() {
        String total = "0.0";
        String montantContribution = getMontantContribution();
        double dMontantContribution = 0.0;
        String montantSupplExtra = getSupplExtra();
        double dMontantSupplExtra = 0.0;
        // montantcontribution
        if (montantContribution.length() <= 0) {
            montantContribution = "0.0";
        }
        if (!(montantContribution.indexOf(".") >= 0)) {
            montantContribution += ".0";
        }
        try {
            dMontantContribution = Double.parseDouble(montantContribution);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // montantSupplExtra
        if (montantSupplExtra.length() <= 0) {
            montantSupplExtra = "0.0";
        }
        if (!(montantSupplExtra.indexOf(".") >= 0)) {
            montantSupplExtra += ".0";
        }
        try {
            dMontantSupplExtra = Double.parseDouble(montantSupplExtra);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        total = "" + (dMontantContribution + dMontantSupplExtra);
        return total;
    }

    /**
     * @return the montantExact
     */
    public String getMontantExact() {
        return montantExact;
    }

    /**
     * @return the montantPrimeAssurance
     */
    public String getMontantPrimeAssurance() {
        return montantPrimeAssurance;
    }

    /**
     * This field doesn't exist in DB. Only for simulation
     *
     * @return
     */
    public String getMontantSupplement() {
        return montantSupplement;
    }

    /**
     * @return the noAssure
     */
    public String getNoAssure() {
        return noAssure;
    }

    /**
     * @return the noCaisseMaladie
     */
    public String getNoCaisseMaladie() {
        return noCaisseMaladie;
    }

    /**
     * @return the noLot
     */
    public String getNoLot() {
        return noLot;
    }

    /**
     * Gets the name of the caissemaladie from the linked administration
     *
     * @return
     */
    public String getNomCaisseMaladieFromAdministration() {
        String csReturn = "";
        try {
            csReturn = getAdministration().getTiers().getDesignation1();
            if (csReturn == null) {
                csReturn = "";
            }
        } catch (Exception ex) {
            csReturn = "";
        }
        return csReturn;
    }

    /**
     * @return the noModeles
     */
    public String getNoModeles() {
        return noModeles;
    }

    /**
     * @return the oldMontantContribAnnuelle
     */
    public String getOldMontantContribAnnuelle() {
        return oldMontantContribAnnuelle;
    }

    /**
     * @return the refus
     */
    public Boolean getRefus() {
        return refus;
    }

    /**
     * @return the supplExtra
     */
    public String getSupplExtra() {
        return supplExtra;
    }

    /**
     * @return tauxEnfantCharge
     */
    public String getTauxEnfantCharge() {
        return tauxEnfantCharge;
    }

    /**
     * @return the typeAvisRIP
     */
    public String getTypeAvisRIP() {
        return typeAvisRIP;
    }

    /**
     * @return the typeDemande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param anneeRecalcul
     *            the anneeRecalcul to set
     */
    public void setAnneeRecalcul(String anneeRecalcul) {
        this.anneeRecalcul = anneeRecalcul;
    }

    /**
     * @param annonceCaisseMaladie
     *            the annonceCaisseMaladie to set
     */
    public void setAnnonceCaisseMaladie(Boolean annonceCaisseMaladie) {
        this.annonceCaisseMaladie = annonceCaisseMaladie;
    }

    public void setCodeActif(Boolean codeActif) {
        this.codeActif = codeActif;
    }

    /**
     * @param codeForcer
     *            the codeForcer to set
     */
    public void setCodeForcer(Boolean codeForcer) {
        this.codeForcer = codeForcer;
    }

    /**
     * @param codeTraitement
     *            the codeTraitement to set
     */
    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    /**
     * @param codeTraitementDossier
     *            the codeTraitementDossier to set
     */
    public void setCodeTraitementDossier(String codeTraitementDossier) {
        this.codeTraitementDossier = codeTraitementDossier;
    }

    /**
     * @param dateAnnonceCaisseMaladie
     *            the dateAnnonceCaisseMaladie to set
     */
    public void setDateAnnonceCaisseMaladie(String dateAnnonceCaisseMaladie) {
        this.dateAnnonceCaisseMaladie = dateAnnonceCaisseMaladie;
    }

    /**
     * @param dateAvisRIP
     *            the dateAvisRIP to set
     */
    public void setDateAvisRIP(String dateAvisRIP) {
        this.dateAvisRIP = dateAvisRIP;
    }

    /**
     * @param dateEnvoi
     *            the dateEnvoi to set
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    /**
     * @param dateModification
     *            the dateModification to set
     */
    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    /**
     * @param dateRecepDemande
     *            the dateRecepDemande to set
     */
    public void setDateRecepDemande(String dateRecepDemande) {
        this.dateRecepDemande = dateRecepDemande;
    }

    public void setDateRecepDemandeRecalcul(String dateRecepDemandeRecalcul) {
        this.dateRecepDemandeRecalcul = dateRecepDemandeRecalcul;
    }

    /**
     * @param debutDroit
     *            the debutDroit to set
     */
    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    /**
     * @param finDroit
     *            the finDroit to set
     */
    public void setFinDroit(String finDroit) {
        this.finDroit = finDroit;
    }

    @Override
    public void setId(String id) {
        idDetailFamille = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idDetailFamille
     *            the idDetailFamille to set
     */
    public void setIdDetailFamille(String idDetailFamille) {
        this.idDetailFamille = idDetailFamille;
    }

    /**
     * @param idFamille
     *            the idFamille to set
     */
    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    /**
     * @param montantContribAnnuelle
     *            the montantContribAnnuelle to set
     */
    public void setMontantContribAnnuelle(String montantContribAnnuelle) {
        this.montantContribAnnuelle = montantContribAnnuelle;
    }

    /**
     * This field doesn't exist in DB. Only for simulation
     *
     * @param montantContribSansSuppl
     */
    public void setMontantContribSansSuppl(String montantContribSansSuppl) {
        this.montantContribSansSuppl = montantContribSansSuppl;
    }

    /**
     * @param montantContribution
     *            the montantContribution to set
     */
    public void setMontantContribution(String montantContribution) {
        this.montantContribution = montantContribution;
    }

    /**
     * Champs pour accueilir le montant de subside pour un assisté (utilisé uniquement en simulation, n'est pas inséré
     * en DB)
     *
     * @param montantContributionAssiste
     */
    public void setMontantContributionAssiste(String montantContributionAssiste) {
        this.montantContributionAssiste = montantContributionAssiste;
    }

    /**
     * @param montantExact
     *            the montantExact to set
     */
    public void setMontantExact(String montantExact) {
        this.montantExact = montantExact;
    }

    /**
     * @param montantPrimeAssurance
     *            the montantPrimeAssurance to set
     */
    public void setMontantPrimeAssurance(String montantPrimeAssurance) {
        this.montantPrimeAssurance = montantPrimeAssurance;
    }

    /**
     * This field doesn't exist in DB. Only for simulation
     *
     * @param montantSupplement
     */
    public void setMontantSupplement(String montantSupplement) {
        this.montantSupplement = montantSupplement;
    }

    /**
     * @param noAssure
     *            the noAssure to set
     */
    public void setNoAssure(String noAssure) {
        this.noAssure = noAssure;
    }

    /**
     * @param noCaisseMaladie
     *            the noCaisseMaladie to set
     */
    public void setNoCaisseMaladie(String noCaisseMaladie) {
        this.noCaisseMaladie = noCaisseMaladie;
    }

    /**
     * @param noLot
     *            the noLot to set
     */
    public void setNoLot(String noLot) {
        this.noLot = noLot;
    }

    /**
     * @param noModeles
     *            the noModeles to set
     */
    public void setNoModeles(String noModeles) {
        this.noModeles = noModeles;
    }

    /**
     * @param oldMontantContribAnnuelle
     *            the oldMontantContribAnnuelle to set
     */
    public void setOldMontantContribAnnuelle(String oldMontantContribAnnuelle) {
        this.oldMontantContribAnnuelle = oldMontantContribAnnuelle;
    }

    /**
     * @param refus
     *            the refus to set
     */
    public void setRefus(Boolean refus) {
        this.refus = refus;
    }

    /**
     * @param supplExtra
     *            the supplExtra to set
     */
    public void setSupplExtra(String supplExtra) {
        this.supplExtra = supplExtra;
    }

    /**
     * @param tauxEnfantCharge
     */
    public void setTauxEnfantCharge(String tauxEnfantCharge) {
        this.tauxEnfantCharge = tauxEnfantCharge;
    }

    /**
     * @param typeAvisRIP
     *            the typeAvisRIP to set
     */
    public void setTypeAvisRIP(String typeAvisRIP) {
        this.typeAvisRIP = typeAvisRIP;
    }

    /**
     * @param typeDemande
     *            the typeDemande to set
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    public String getMontantContributionPC() {
        return montantContributionPC;
    }

    public void setMontantContributionPC(String montantContributionPC) {
        this.montantContributionPC = montantContributionPC;
    }
}

/*
 * Créé le 26 juin. 07
 */
package globaz.corvus.vb.ci;

import globaz.corvus.db.annonces.REAnnonceInscriptionCI;
import globaz.corvus.db.ci.REInscriptionCI;
import globaz.corvus.db.ci.RERassemblementCI;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRImagesConstants;

/**
 * @author bsc
 */
public class REInscriptionCIViewBean extends REInscriptionCI implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private REAnnonceInscriptionCI annonce = null;

    private PRTiersWrapper ayantDroit = null;
    private RERassemblementCI rassemblement = null;

    private PRTiersWrapper tiers = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public String getAnneeCotisations() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getAnneeCotisations();
        } else {
            return "";
        }
    }

    /**
     * Charge et retourne l'annonce correspondant a l'inscription CI
     * 
     * @return
     */
    public REAnnonceInscriptionCI getAnnonceInscriptionCI() {
        loadAnnonceInscriptionCI();
        return annonce;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getAttenteCIAdditionnelCS() throws Exception {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getAttenteCIAdditionnelCS();
        } else {
            throw new Exception("Unable to load REAnnonceInscriptionCI with id : " + getIdArc());
        }
    }

    /**
     * @return
     */
    public String getBrancheEconomique() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getBrancheEconomique();
        } else {
            return "";
        }
    }

    public String getBrancheEconomiqueCode() {
        return getSession().getCode(getBrancheEconomique());
    }

    public String getBrancheEconomiqueLibelle() {
        return getSession().getCodeLibelle(getBrancheEconomique());
    }

    /**
     * @return
     */
    public Boolean getCiAdditionnel() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCiAdditionnel();
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * @return
     */
    public String getCodeADS() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCodeADS();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCodeApplication() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCodeApplication();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCodeEnregistrement01() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCodeEnregistrement01();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCodeExtourne() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCodeExtourne();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCodeParticulier() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCodeParticulier();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCodeSpecial() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getCodeSpecial();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getDateCloture() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getDateCloture();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getDateOrdre() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getDateOrdre();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getEtat() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getEtat();
        } else {
            return "";
        }
    }

    /**
     * Le genre est la concatenation de Extourne + cotisation + splitting si extourne est vide on met 1 espace
     * 
     * @return
     */
    public String getGenre() {

        String codeExtourne = getCodeExtourne();
        if (JadeStringUtil.isEmpty(codeExtourne)) {
            codeExtourne = "_";
        }

        String genreCotisation = getGenreCotisation();
        if (JadeStringUtil.isEmpty(genreCotisation)) {
            genreCotisation = "_";
        }

        String codeParticulier = getCodeParticulier();
        if (JadeStringUtil.isEmpty(codeParticulier)) {
            codeParticulier = "_";
        }

        return codeExtourne + genreCotisation + codeParticulier;
    }

    /**
     * @return
     */
    public String getGenreCotisation() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getGenreCotisation();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getIdTiers() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getIdTiers();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getIdTiersAyantDroit() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getIdTiersAyantDroit();
        } else {
            return "";
        }
    }

    /**
     * getter pour l'image isCiAdditionnel
     * 
     * @return l'image correspondant a isCiAdditionnel
     */
    public String getIsCiAdditionnelImage() {
        if (isCiAdditionnel()) {
            return PRImagesConstants.IMAGE_OK;
        } else {
            return PRImagesConstants.IMAGE_ERREUR;
        }
    }

    /**
     * @return
     */
    public String getMoisDebutCotisations() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getMoisDebutCotisations();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getMoisFinCotisations() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getMoisFinCotisations();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getMotif() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getMotif();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNoAffilie() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getNoAffilie();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNoAgenceTenantCI() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getNoAgenceTenantCI();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNoAvsTiers() {
        if (loadTiers()) {
            return tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            return "";
        }

    }

    /**
     * @return
     */
    public String getNoAvsTiersAyantDroit() {
        if (loadAyantDroit()) {
            return ayantDroit.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNoCaisseTenantCI() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getNoCaisseTenantCI();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNoPostalEmployeur() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getNoPostalEmployeur();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNumeroAgence() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getNumeroAgence();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getNumeroCaisse() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getNumeroCaisse();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getPartBonifAssist() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getPartBonifAssist();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getPartieInformation() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getPartieInformation();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getProvenance() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getProvenance();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getRefInterneCaisse() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getRefInterneCaisse();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getRevenu() {
        if (loadAnnonceInscriptionCI()) {
            return annonce.getRevenu();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public boolean isCiAdditionnel() {
        if (loadRassemblementCI()) {
            return !JadeStringUtil.isIntegerEmpty(rassemblement.getIdParent());
        } else {
            return false;
        }
    }

    /**
     * Charge l'annonce correspondant à l'inscripton CI
     */
    private boolean loadAnnonceInscriptionCI() {
        if (annonce == null) {
            annonce = new REAnnonceInscriptionCI();
            annonce.setSession(getSession());
            annonce.setIdAnnonce(getIdArc());
            try {
                annonce.retrieve();
            } catch (Exception e) {
                getSession().addError("L'annonce CI " + getIdArc() + "ne peut pas être chargée.");
            }
        }

        return annonce != null;
    }

    /**
     * Charge le tiers
     */
    private boolean loadAyantDroit() {
        if ((ayantDroit == null) && !JadeStringUtil.isIntegerEmpty(getIdTiersAyantDroit())) {
            try {
                ayantDroit = PRTiersHelper.getPersonneAVS(getSession(), getIdTiersAyantDroit());
            } catch (Exception e) {
                getSession().addError("Le Tiers Ayant Droit " + getIdTiers() + "ne peut pas être chargée.");
            }
        }

        return ayantDroit != null;
    }

    /**
     * Charge le rassemblement correspondant à l'inscripton CI
     */
    private boolean loadRassemblementCI() {
        if (rassemblement == null) {
            rassemblement = new RERassemblementCI();
            rassemblement.setSession(getSession());
            rassemblement.setIdRCI(getIdRCI());
            try {
                rassemblement.retrieve();
            } catch (Exception e) {
                getSession().addError("Le rassemblement de l'annonce CI " + getIdArc() + "ne peut pas être chargée.");
            }
        }

        return !rassemblement.isNew();
    }

    /**
     * Charge le tiers
     */
    private boolean loadTiers() {
        if ((tiers == null) && !JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            try {
                tiers = PRTiersHelper.getPersonneAVS(getSession(), getIdTiers());

                if (tiers == null) {
                    tiers = PRTiersHelper.getAdministrationParId(getSession(), getIdTiers());
                }
            } catch (Exception e) {
                getSession().addError("Le Tiers " + getIdTiers() + "ne peut pas être chargée.");
            }
        }

        return tiers != null;
    }

    /**
     * @param string
     */
    public void setAnneeCotisations(String string) {
        annonce.setAnneeCotisations(string);
    }

    /**
     * @param string
     */
    public void setBrancheEconomique(String string) {
        annonce.setBrancheEconomique(string);
    }

    /**
     * @param ciAdditionnel
     */
    public void setCiAdditionnel(Boolean ciAdditionnel) {
        annonce.setCiAdditionnel(ciAdditionnel);
    }

    /**
     * @param codeADS
     */
    public void setCodeADS(String codeADS) {
        annonce.setCodeADS(codeADS);
    }

    /**
     * @param codeApplication
     */
    public void setCodeApplication(String codeApplication) {
        annonce.setCodeApplication(codeApplication);
    }

    /**
     * @param codeEnregistrement01
     */
    public void setCodeEnregistrement01(String codeEnregistrement01) {
        annonce.setCodeEnregistrement01(codeEnregistrement01);
    }

    /**
     * @param codeExtourne
     */
    public void setCodeExtourne(String codeExtourne) {
        annonce.setCodeExtourne(codeExtourne);
    }

    /**
     * @param codeParticulier
     */
    public void setCodeParticulier(String codeParticulier) {
        annonce.setCodeParticulier(codeParticulier);
    }

    /**
     * @param codeSpeciale
     */
    public void setCodeSpeciale(String codeSpeciale) {
        annonce.setCodeSpeciale(codeSpeciale);
    }

    /**
     * @param dateCloture
     */
    public void setDateCloture(String dateCloture) {
        annonce.setDateCloture(dateCloture);
    }

    /**
     * @param dateOrdre
     */
    public void setDateOrdre(String dateOrdre) {
        annonce.setDateOrdre(dateOrdre);
    }

    /**
     * @param etat
     */
    public void setEtat(String etat) {
        annonce.setEtat(etat);
    }

    /**
     * @param genreCotisation
     */
    public void setGenreCotisation(String genreCotisation) {
        annonce.setGenreCotisation(genreCotisation);
    }

    /**
     * @param idAnnonce
     */
    public void setIdAnnonce(String idAnnonce) {
        annonce.setIdAnnonce(idAnnonce);
    }

    /**
     * @param idTiers
     */
    public void setIdTiers(String idTiers) {
        annonce.setIdTiers(idTiers);
    }

    /**
     * @param idTiersAyantDroit
     */
    public void setIdTiersAyantDroit(String idTiersAyantDroit) {
        annonce.setIdTiersAyantDroit(idTiersAyantDroit);
    }

    /**
     * @param string
     */
    public void setMoisDebutCotisations(String string) {
        annonce.setMoisDebutCotisations(string);
    }

    /**
     * @param string
     */
    public void setMoisFinCotisations(String string) {
        annonce.setMoisFinCotisations(string);
    }

    /**
     * @param motif
     */
    public void setMotif(String motif) {
        annonce.setMotif(motif);
    }

    /**
     * @param noAffilie
     */
    public void setNoAffilie(String noAffilie) {
        annonce.setNoAffilie(noAffilie);
    }

    /**
     * @param noAgenceTenantCI
     */
    public void setNoAgenceTenantCI(String noAgenceTenantCI) {
        annonce.setNoAgenceTenantCI(noAgenceTenantCI);
    }

    /**
     * @param noCaisseTenantCI
     */
    public void setNoCaisseTenantCI(String noCaisseTenantCI) {
        annonce.setNoCaisseTenantCI(noCaisseTenantCI);
    }

    /**
     * @param string
     */
    public void setNoPostalEmployeur(String string) {
        annonce.setNoPostalEmployeur(string);
    }

    /**
     * @param numeroAgence
     */
    public void setNumeroAgence(String numeroAgence) {
        annonce.setNumeroAgence(numeroAgence);
    }

    /**
     * @param numeroCaisse
     */
    public void setNumeroCaisse(String numeroCaisse) {
        annonce.setNumeroCaisse(numeroCaisse);
    }

    /**
     * @param partBonifAssist
     */
    public void setPartBonifAssist(String partBonifAssist) {
        annonce.setPartBonifAssist(partBonifAssist);
    }

    /**
     * @param string
     */
    public void setPartieInformation(String string) {
        annonce.setPartieInformation(string);
    }

    /**
     * @param provenance
     */
    public void setProvenance(String provenance) {
        annonce.setProvenance(provenance);
    }

    /**
     * @param refInterneCaisse
     */
    public void setRefInterneCaisse(String refInterneCaisse) {
        annonce.setRefInterneCaisse(refInterneCaisse);
    }

    // public Boolean getActif() {
    // return annonce.getActif();
    // }
    //
    // public Boolean getAttenteCiAdd() {
    // return annonce.getAttenteCiAdd();
    // }
    //
    // public void setActif(Boolean actif) {
    // annonce.setActif(actif);
    // }
    //
    // public void setAttenteCiAdd(Boolean attenteCiAdd) {
    // annonce.setAttenteCiAdd(attenteCiAdd);
    // }

    /**
     * @param revenu
     */
    public void setRevenu(String revenu) {
        annonce.setRevenu(revenu);
    }

    public final void setAttenteCIAdditionnelCS(String attenteCIAdditionnelCS) {
        annonce.setAttenteCIAdditionnelCS(attenteCIAdditionnelCS);
    }

}

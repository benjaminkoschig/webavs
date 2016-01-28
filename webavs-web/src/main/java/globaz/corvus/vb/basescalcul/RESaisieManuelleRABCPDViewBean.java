/*
 * Créé le 2 juil. 07
 */
package globaz.corvus.vb.basescalcul;

import globaz.commons.nss.NSUtil;
import globaz.corvus.utils.RENumberFormatter;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.Vector;

/**
 * @author HPE
 */
public class RESaisieManuelleRABCPDViewBean extends PRAbstractViewBeanSupport {

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] { new String[] {
            "idTiersAdressePmtICDepuisPyxis", "getIdTiers" } };

    private String adresseFormattee = "";
    private String anneeNiveauBasesCalcul = "";
    private String anneesAnticipationDemandeRente = "";
    private String anneesAssistanceBasesCalcul = "";
    private String anneesEducatifBasesCalcul = "";
    private String anneesTransitionBasesCalcul = "";
    private String anneeTraitementBasesCalcul = "";
    private String bonusEducatifBasesCalcul = "";
    private String ccpOuBanqueFormatte = "";
    private String cleInfirmiteDemandeRente = "";
    private String codeOfficeAIDemandeRente = "";
    private String codeRevenu9emeBasesCalcul = "";
    private Boolean codeRevenusSplittesBasesCalcul = Boolean.FALSE;
    private String codesCasSpecialRenteAccordee1 = "";
    private String codesCasSpecialRenteAccordee2 = "";
    private String codesCasSpecialRenteAccordee3 = "";
    private String codesCasSpecialRenteAccordee4 = "";
    private String codesCasSpecialRenteAccordee5 = "";
    private String codeSurvivantInvalideRenteAccordee = "";
    private String dateDebutDroitRenteAccordee = "";
    private String dateRevocationDemandeRente = "";
    private String debAnticipationRenteAccordee = "";
    private String degreInvaliditeDemandeRente = "";
    private String droitBasesCalcul = "";
    private String dureeAjournementRenteAccordee = "";
    private String dureeCotAp73BasesCalcul = "";
    private String dureeCotAv73BasesCalcul = "";
    private String dureeCotClasseBasesCalcul = "";
    private String dureeCotEtrAp73BasesCalcul = "";
    private String dureeCotEtrAv73BasesCalcul = "";
    private String dureeCotRAMBasesCalcul = "";
    private String echelleBasesCalcul = "";
    private String finDroitRenteAccordee = "";
    private String fractionRenteRenteAccordee = "";
    private String genrePrestationRenteAccordee = "";
    private String idBasesCalcul = "";
    private String idCompteAnnexeIC = "";
    private String idDemandeRente = "";
    private String idInfoComptaIC = "";
    private String idPrestationDue = "";
    private String idRenteAccordee = "";
    private String idRenteCalculee = "";
    private String idTierRequerant = "";
    private String idTiersAdressePmtIC = "";
    private String idTiersAdressePmtICDepuisPyxis = "";
    private String idTiersBeneficiaire = "";
    private String idTiersDemandeRente = "";
    private Boolean invaliditePrecoceBasesCalcul = Boolean.FALSE;
    private String isAdresseModifiee = "false";
    private Boolean isAjourneDemandeRente = Boolean.FALSE;
    private Boolean isBasesCalculModifiable = Boolean.FALSE;
    private Boolean isTransfere = Boolean.FALSE;
    private String moisAppAp73BasesCalcul = "";
    private String moisAppAv73BasesCalcul = "";
    private String montantRenteAccordee = "";
    private String noDemandeRente = "";
    private String RAMBasesCalcul = "";
    private String reductionAnticipationRenteAccordee = "";
    private String referencePmt = "";
    private Boolean retourDepuisPyxis = Boolean.FALSE;
    private String suppAIRAMBasesCalcul = "";
    private String supplementAjournementRenteAccordee = "";
    private String survEvAssDemandeRente = "";
    private String tauxReductionAnticipation = "";
    private PRTiersWrapper tiersBeneficiaire = null;
    private Boolean tiersBeneficiaireChange = Boolean.FALSE;

    public RESaisieManuelleRABCPDViewBean() {
        super();
    }

    public String getAdresseFormattee() {
        return adresseFormattee;
    }

    public String getAnneeNiveauBasesCalcul() {
        return anneeNiveauBasesCalcul;
    }

    public String getAnneesAnticipationDemandeRente() {

        if (anneesAnticipationDemandeRente.length() != 8) {
            return anneesAnticipationDemandeRente;
        } else {
            return getSession().getCodeLibelle(anneesAnticipationDemandeRente);
        }
    }

    public String getAnneesAssistanceBasesCalcul() {
        if (anneesAssistanceBasesCalcul.length() == 5) {
            return anneesAssistanceBasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(anneesAssistanceBasesCalcul), false,
                    false, true, 2, 2);
        }
    }

    public String getAnneesEducatifBasesCalcul() {
        if (anneesEducatifBasesCalcul.length() == 5) {
            return anneesEducatifBasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(anneesEducatifBasesCalcul), false,
                    false, true, 2, 2);
        }
    }

    public String getAnneesTransitionBasesCalcul() {
        if (anneesTransitionBasesCalcul.length() == 3) {
            return anneesTransitionBasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AM_to_AxM(anneesTransitionBasesCalcul), false,
                    false, true, 1, 1);
        }

    }

    public String getAnneeTraitementBasesCalcul() {
        return anneeTraitementBasesCalcul;
    }

    public String getBonusEducatifBasesCalcul() {
        return bonusEducatifBasesCalcul;
    }

    public String getCcpOuBanqueFormatte() {
        return ccpOuBanqueFormatte;
    }

    public String getCleInfirmiteDemandeRente() {
        return cleInfirmiteDemandeRente;
    }

    public String getCodeOfficeAIDemandeRente() {
        return codeOfficeAIDemandeRente;
    }

    public String getCodeRevenu9emeBasesCalcul() {
        return codeRevenu9emeBasesCalcul;
    }

    public Boolean getCodeRevenusSplittesBasesCalcul() {
        return codeRevenusSplittesBasesCalcul;
    }

    public String getCodesCasSpecialRenteAccordee1() {
        return codesCasSpecialRenteAccordee1;
    }

    public String getCodesCasSpecialRenteAccordee2() {
        return codesCasSpecialRenteAccordee2;
    }

    public String getCodesCasSpecialRenteAccordee3() {
        return codesCasSpecialRenteAccordee3;
    }

    public String getCodesCasSpecialRenteAccordee4() {
        return codesCasSpecialRenteAccordee4;
    }

    public String getCodesCasSpecialRenteAccordee5() {
        return codesCasSpecialRenteAccordee5;
    }

    public String getCodeSurvivantInvalideRenteAccordee() {
        return codeSurvivantInvalideRenteAccordee;
    }

    public String getDateDebutDroitRenteAccordee() {
        if (dateDebutDroitRenteAccordee.length() != 7) {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_MMAAAA_to_MMxAAAA(dateDebutDroitRenteAccordee),
                    false, false, true, 4, 2);
        } else {
            return dateDebutDroitRenteAccordee;
        }
    }

    public String getDateNaissanceTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
        } else {
            return "";
        }
    }

    public String getDateRevocationDemandeRente() {
        return dateRevocationDemandeRente;
    }

    public String getDebAnticipationRenteAccordee() {
        return debAnticipationRenteAccordee;
    }

    public String getDegreInvaliditeDemandeRente() {
        return degreInvaliditeDemandeRente;
    }

    public String getDroitBasesCalcul() {
        return droitBasesCalcul;
    }

    public String getDureeAjournementRenteAccordee() {
        return dureeAjournementRenteAccordee;
    }

    public String getDureeCotAp73BasesCalcul() {
        if (dureeCotAp73BasesCalcul.length() == 5) {
            return dureeCotAp73BasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(dureeCotAp73BasesCalcul), false,
                    false, true, 2, 2);
        }

    }

    public String getDureeCotAv73BasesCalcul() {
        if (dureeCotAv73BasesCalcul.length() == 5) {
            return dureeCotAv73BasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(dureeCotAv73BasesCalcul), false,
                    false, true, 2, 2);
        }
    }

    public String getDureeCotClasseBasesCalcul() {
        return dureeCotClasseBasesCalcul;
    }

    public String getDureeCotEtrAp73BasesCalcul() {
        if (dureeCotEtrAp73BasesCalcul.length() == 5) {
            return dureeCotEtrAp73BasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(dureeCotEtrAp73BasesCalcul), false,
                    false, true, 2, 2);
        }
    }

    public String getDureeCotEtrAv73BasesCalcul() {
        if (dureeCotEtrAv73BasesCalcul.length() == 5) {
            return dureeCotEtrAv73BasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(dureeCotEtrAv73BasesCalcul), false,
                    false, true, 2, 2);
        }
    }

    public String getDureeCotRAMBasesCalcul() {
        if (dureeCotRAMBasesCalcul.length() == 5) {
            return dureeCotRAMBasesCalcul;
        } else {
            return RENumberFormatter.fmt(PRDateFormater.convertDate_AAMM_to_AAxMM(dureeCotRAMBasesCalcul), false,
                    false, true, 2, 2);
        }
    }

    public String getEchelleBasesCalcul() {
        return echelleBasesCalcul;
    }

    public String getFinDroitRenteAccordee() {
        return finDroitRenteAccordee;
    }

    public String getFractionRenteRenteAccordee() {
        return fractionRenteRenteAccordee;
    }

    public String getGenrePrestationRenteAccordee() {
        return genrePrestationRenteAccordee;
    }

    public String getIdBasesCalcul() {
        return idBasesCalcul;
    }

    public String getIdCompteAnnexeIC() {
        return idCompteAnnexeIC;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdInfoComptaIC() {
        return idInfoComptaIC;
    }

    public String getIdPrestationDue() {
        return idPrestationDue;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getIdTiersAdressePmtIC() {
        return idTiersAdressePmtIC;
    }

    public String getIdTiersAdressePmtICDepuisPyxis() {
        return idTiersAdressePmtICDepuisPyxis;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdTiersDemandeRente() {
        return idTiersDemandeRente;
    }

    public Boolean getInvaliditePrecoceBasesCalcul() {
        return invaliditePrecoceBasesCalcul;
    }

    public String getIsAdresseModifiee() {
        return isAdresseModifiee;
    }

    public Boolean getIsAjourneDemandeRente() {
        return isAjourneDemandeRente;
    }

    public Boolean getIsBasesCalculModifiable() {
        return isBasesCalculModifiable;
    }

    public Boolean getIsTransfere() {
        return isTransfere;
    }

    /**
     * retourne un tableau de correspondance entre méthodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'une adresse de paiement.
     * 
     * @return la valeur courante de l'attribut méthodes selection adresse
     */
    public Object[] getMethodesSelectionAdressePaiement() {
        return RESaisieManuelleRABCPDViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    public String getMoisAppAp73BasesCalcul() {
        if (moisAppAp73BasesCalcul.length() == 2) {
            return moisAppAp73BasesCalcul;
        } else if (moisAppAp73BasesCalcul.length() == 0) {
            return "";
        } else {
            return "0" + moisAppAp73BasesCalcul;
        }
    }

    public String getMoisAppAv73BasesCalcul() {
        if (moisAppAv73BasesCalcul.length() == 2) {
            return moisAppAv73BasesCalcul;
        } else if (moisAppAv73BasesCalcul.length() == 0) {
            return "";
        } else {
            return "0" + moisAppAv73BasesCalcul;
        }
    }

    public String getMontantRenteAccordee() {
        return montantRenteAccordee;
    }

    public String getNationaliteTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE);
        } else {
            return "";
        }
    }

    public String getNoDemandeRente() {
        return noDemandeRente;
    }

    public String getNomPrenomTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            return "";
        }
    }

    public String getNssTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            return "";
        }
    }

    public String getNssTiersBeneficiaireWithoutPrefix() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return NSUtil.formatWithoutPrefixe(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? true : false);
        } else {
            return "";
        }
    }

    public String getRAMBasesCalcul() {
        return RAMBasesCalcul;
    }

    public String getReductionAnticipationRenteAccordee() {
        return reductionAnticipationRenteAccordee;
    }

    public String getReferencePmt() {
        return referencePmt;
    }

    public String getSexeTiersBeneficiaire() {

        loadTiersBeneficiaire();

        if (tiersBeneficiaire != null) {
            return getSession().getCodeLibelle(tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_SEXE));
        } else {
            return "";
        }
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getSession());
    }

    public String getSuppAIRAMBasesCalcul() {
        return suppAIRAMBasesCalcul;
    }

    public String getSupplementAjournementRenteAccordee() {
        return supplementAjournementRenteAccordee;
    }

    public String getSurvEvAssDemandeRente() {
        return survEvAssDemandeRente;
    }

    public String getTauxReductionAnticipation() {
        return tauxReductionAnticipation;
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    /**
     * Vérifie si le niveau d'année est comprise entre 00 & 99
     * 
     * @param level
     * @return
     */
    public boolean isAnneeNiveauBasesCalculCorrect(int level) {
        if ((level < 0) || (level > 99)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Vérifie le genre de prestation pour savoir si il exige un niveau d'année
     * 
     * @param genrePrestation
     * @return
     */
    public boolean isGenrePrestationWithNiveauAnneeMandatory(int genrePrestation) {
        if (((genrePrestation > 9) && (genrePrestation < 17)) || ((genrePrestation > 32) && (genrePrestation < 37))
                || ((genrePrestation > 49) && (genrePrestation < 57))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSSTiersBeneficiaire() {

        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).length() > 14 ? "true"
                    : "false";
        } else {
            return "";
        }

    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public boolean isTiersBeneficiaireChange() {
        return tiersBeneficiaireChange;
    }

    /**
     * retrouve les propriétés du bénéficiaire
     */
    private void loadTiersBeneficiaire() {
        if (tiersBeneficiaire == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiersBeneficiaire())) {
                try {
                    tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiaire());
                } catch (Exception e) {
                    // on net fait rien
                }
            }
        }
    }

    public void setAdresseFormattee(String string) {
        adresseFormattee = string;
    }

    public void setAdressePaiement(TIAdressePaiementData adressePaiement) throws Exception {
        if (adressePaiement != null) {
            idTiersAdressePmtIC = adressePaiement.getIdTiers();
        } else {
            idTiersAdressePmtIC = "";
        }
    }

    public void setAnneeNiveauBasesCalcul(String string) {
        anneeNiveauBasesCalcul = string;
    }

    public void setAnneesAnticipationDemandeRente(String string) {
        anneesAnticipationDemandeRente = string;
    }

    public void setAnneesAssistanceBasesCalcul(String string) {
        anneesAssistanceBasesCalcul = string;
    }

    public void setAnneesEducatifBasesCalcul(String string) {
        anneesEducatifBasesCalcul = string;
    }

    public void setAnneesTransitionBasesCalcul(String string) {
        anneesTransitionBasesCalcul = string;
    }

    public void setAnneeTraitementBasesCalcul(String string) {
        anneeTraitementBasesCalcul = string;
    }

    public void setBonusEducatifBasesCalcul(String string) {
        bonusEducatifBasesCalcul = string;
    }

    public void setCcpOuBanqueFormatte(String string) {
        ccpOuBanqueFormatte = string;
    }

    public void setCleInfirmiteDemandeRente(String string) {
        cleInfirmiteDemandeRente = string;
    }

    public void setCodeOfficeAIDemandeRente(String string) {
        codeOfficeAIDemandeRente = string;
    }

    public void setCodeRevenu9emeBasesCalcul(String string) {
        codeRevenu9emeBasesCalcul = string;
    }

    public void setCodeRevenusSplittesBasesCalcul(Boolean string) {
        codeRevenusSplittesBasesCalcul = string;
    }

    public void setCodesCasSpecialRenteAccordee1(String codesCasSpecialRenteAccordee1) {
        this.codesCasSpecialRenteAccordee1 = codesCasSpecialRenteAccordee1;
    }

    public void setCodesCasSpecialRenteAccordee2(String codesCasSpecialRenteAccordee2) {
        this.codesCasSpecialRenteAccordee2 = codesCasSpecialRenteAccordee2;
    }

    public void setCodesCasSpecialRenteAccordee3(String codesCasSpecialRenteAccordee3) {
        this.codesCasSpecialRenteAccordee3 = codesCasSpecialRenteAccordee3;
    }

    public void setCodesCasSpecialRenteAccordee4(String codesCasSpecialRenteAccordee4) {
        this.codesCasSpecialRenteAccordee4 = codesCasSpecialRenteAccordee4;
    }

    public void setCodesCasSpecialRenteAccordee5(String codesCasSpecialRenteAccordee5) {
        this.codesCasSpecialRenteAccordee5 = codesCasSpecialRenteAccordee5;
    }

    public void setCodeSurvivantInvalideRenteAccordee(String string) {
        codeSurvivantInvalideRenteAccordee = string;
    }

    public void setDateDebutDroitRenteAccordee(String string) {
        dateDebutDroitRenteAccordee = string;
    }

    public void setDateRevocationDemandeRente(String string) {
        dateRevocationDemandeRente = string;
    }

    public void setDebAnticipationRenteAccordee(String string) {
        debAnticipationRenteAccordee = string;
    }

    public void setDegreInvaliditeDemandeRente(String string) {
        degreInvaliditeDemandeRente = string;
    }

    public void setDroitBasesCalcul(String string) {
        droitBasesCalcul = string;
    }

    public void setDureeAjournementRenteAccordee(String string) {
        dureeAjournementRenteAccordee = string;
    }

    public void setDureeCotAp73BasesCalcul(String string) {
        dureeCotAp73BasesCalcul = string;
    }

    public void setDureeCotAv73BasesCalcul(String string) {
        dureeCotAv73BasesCalcul = string;
    }

    public void setDureeCotClasseBasesCalcul(String string) {
        dureeCotClasseBasesCalcul = string;
    }

    public void setDureeCotEtrAp73BasesCalcul(String string) {
        dureeCotEtrAp73BasesCalcul = string;
    }

    public void setDureeCotEtrAv73BasesCalcul(String string) {
        dureeCotEtrAv73BasesCalcul = string;
    }

    public void setDureeCotRAMBasesCalcul(String string) {
        dureeCotRAMBasesCalcul = string;
    }

    public void setEchelleBasesCalcul(String string) {
        echelleBasesCalcul = string;
    }

    public void setFinDroitRenteAccordee(String string) {
        finDroitRenteAccordee = string;
    }

    public void setFractionRenteRenteAccordee(String string) {
        fractionRenteRenteAccordee = string;
    }

    public void setGenrePrestationRenteAccordee(String string) {
        genrePrestationRenteAccordee = string;
    }

    public void setIdBasesCalcul(String string) {
        idBasesCalcul = string;
    }

    public void setIdCompteAnnexeIC(String string) {
        idCompteAnnexeIC = string;
    }

    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    public void setIdInfoComptaIC(String string) {
        idInfoComptaIC = string;
    }

    public void setIdPrestationDue(String string) {
        idPrestationDue = string;
    }

    public void setIdRenteAccordee(String string) {
        idRenteAccordee = string;
    }

    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }

    public void setIdTierRequerant(String string) {
        idTierRequerant = string;
    }

    public void setIdTiersAdressePmtIC(String string) {
        idTiersAdressePmtIC = string;
    }

    public void setIdTiersAdressePmtICDepuisPyxis(String string) {
        setIdTiersAdressePmtIC(string);
        idTiersAdressePmtICDepuisPyxis = string;
        retourDepuisPyxis = true;
        tiersBeneficiaireChange = true;

    }

    public void setIdTiersBeneficiaire(String string) {
        idTiersBeneficiaire = string;
    }

    public void setIdTiersDemandeRente(String string) {
        idTiersDemandeRente = string;
    }

    public void setInvaliditePrecoceBasesCalcul(Boolean string) {
        invaliditePrecoceBasesCalcul = string;
    }

    public void setIsAdresseModifiee(String string) {
        isAdresseModifiee = string;
    }

    public void setIsAjourneDemandeRente(Boolean boolean1) {
        isAjourneDemandeRente = boolean1;
    }

    public void setIsBasesCalculModifiable(Boolean string) {
        isBasesCalculModifiable = string;
    }

    public void setIsTransfere(Boolean boolean1) {
        isTransfere = boolean1;
    }

    public void setMoisAppAp73BasesCalcul(String string) {
        moisAppAp73BasesCalcul = string;
    }

    public void setMoisAppAv73BasesCalcul(String string) {
        moisAppAv73BasesCalcul = string;
    }

    public void setMontantRenteAccordee(String string) {
        montantRenteAccordee = string;
    }

    public void setNoDemandeRente(String string) {
        noDemandeRente = string;
    }

    public void setRAMBasesCalcul(String string) {
        RAMBasesCalcul = string;
    }

    public void setReductionAnticipationRenteAccordee(String string) {
        reductionAnticipationRenteAccordee = string;
    }

    public void setReferencePmt(String string) {
        referencePmt = string;
    }

    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

    public void setSuppAIRAMBasesCalcul(String string) {
        suppAIRAMBasesCalcul = string;
    }

    public void setSupplementAjournementRenteAccordee(String string) {
        supplementAjournementRenteAccordee = string;
    }

    public void setSurvEvAssDemandeRente(String string) {
        survEvAssDemandeRente = string;
    }

    public void setTauxReductionAnticipation(String tauxReductionAnticipation) {
        this.tauxReductionAnticipation = tauxReductionAnticipation;
    }

    public void setTiersBeneficiaireChange(boolean b) {
        tiersBeneficiaireChange = b;
    }

    @Override
    public boolean validate() {
        return false;
    }
}

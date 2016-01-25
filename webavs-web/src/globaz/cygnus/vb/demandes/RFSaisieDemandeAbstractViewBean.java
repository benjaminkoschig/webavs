package globaz.cygnus.vb.demandes;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.utils.RFMotifsRefusListBuillder;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 
 * @author jje
 */
public abstract class RFSaisieDemandeAbstractViewBean implements FWViewBeanInterface, BIPersistentObject {

    public static final String CHECKED = "checked";

    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "idTiers" },
            new String[] { "descAdressePaiement", "nom" } };

    private static final Object[] METHODES_SEL_FOURNISSEUR = new Object[] {
            new String[] { "idTiersFournisseurDepuisPyxis", "idTiers" }, new String[] { "descFournisseur", "nom" } };

    public static final String TYPE_DEMANDE_DEFAUT = "defaut";
    public static final String TYPE_DEMANDE_DEV19 = "devis";
    public static final String TYPE_DEMANDE_FQP17_FRE18 = "franchise_fraisRefuse";
    public static final String TYPE_DEMANDE_FRA16 = "typeVehicule";
    public static final String TYPE_DEMANDE_FTD15 = "fraisDentaire";
    public static final String TYPE_DEMANDE_MAI13 = "maintien";
    public static final String TYPE_DEMANDE_MOY5_6_7 = "moyensAuxiliaires";
    public static final String TYPE_VALIDATION_FIN_DE_SAISIE = "fin";
    public static final String TYPE_VALIDATION_MEME_ASSURE = "assure";
    public static final String TYPE_VALIDATION_MEME_FACTURE = "facture";
    public static final String TYPE_VALIDATION_MEME_TYPE = "type";
    public static final String TYPE_VALIDATION_NOUVELLE_SAISIE = "nouveau";

    private String codeSousTypeDeSoin = "";
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";
    private BSpy creationSpy = null;
    private String csCanton = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String descFournisseur = "";
    private boolean erreurSaisieDemande = false;
    private Vector<String[]> etatsDemande = null;
    private boolean focusAdressePaiement = false;
    private boolean focusMotifsDeRefus = false;
    private Vector<String[]> genresDeSoins = null;
    private boolean hasMotifRefusDemande = false;
    private String idAdressePaiementDemande = "";
    private String idAffilieAdressePaiment = "";
    private String idDemandeParent = "";
    private String idDomaineApplicatif = "";
    private String idFournisseurDemande = "";
    private String idGestionnaire = "";
    private String idTiers = "";
    private String idTiersAdressePaiement = "";
    private boolean isAfficherCaseForcerPaiement = false;
    private boolean isAfficherDetail = false;
    private boolean isAfficherRemFournisseur = false;
    private boolean isConventionne = false;
    private boolean isPreparerDecisonDemarre = false;
    private String listMotifsRefusInput = "";
    private String message = "";
    private boolean modifie = false;
    private Map<String, String[]> montantsMotifsRefus = null;
    private String msgType = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    private String provenance = "";
    private boolean retourDepuisPyxis = false;
    private BISession session = null;
    private Vector<String[]> sourceDemande = null;
    private transient String sousTypeDeSoinParTypeInnerJavascript = "";
    private BSpy spy = null;
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;
    private String typeDemande = "";
    private transient Vector<String[]> typeDeSoinsDemande = null;
    private String typeValidation = "";
    private Vector<String[]> typeVhc = null;
    private boolean warningMode = false;
    private Boolean isRetro = false;

    public Boolean getIsRetro() {
        return isRetro;
    }

    public void setIsRetro(Boolean isRetro) {
        this.isRetro = isRetro;
    }

    protected RFSaisieDemandeAbstractViewBean() {
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeSousTypeDeSoinList() {
        return codeSousTypeDeSoinList;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getCodeTypeDeSoinList() {
        return codeTypeDeSoinList;
    }

    public BSpy getCreationSpy() {
        return creationSpy;
    }

    public String getCsCanton() {
        return csCanton;
    }

    public Vector<String[]> getCsEtatData() {
        if (etatsDemande == null) {
            etatsDemande = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_ETAT_DEMANDE, getSession());
        }

        return etatsDemande;
    }

    public Vector<String[]> getCsGenreDeSoinData() {
        if (genresDeSoins == null) {
            genresDeSoins = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_GENRE_DE_SOIN, getSession());
        }

        return genresDeSoins;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public Vector<String[]> getCsSourceData() {
        if (sourceDemande == null) {
            sourceDemande = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_SOURCE_DEMANDE, getSession());
        }

        return sourceDemande;
    }

    public Vector<String[]> getCsTypeVhcData() {
        if (typeVhc == null) {
            typeVhc = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_TYPE_VHC, getSession());
        }

        return typeVhc;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDescFournisseur() {

        try {

            if (!JadeStringUtil.isBlankOrZero(getIdFournisseurDemande()) || isRetourDepuisPyxis()) {

                PRTiersWrapper prTiersWrapper = PRTiersHelper.getTiersParId(session, getIdFournisseurDemande());

                if (null == prTiersWrapper) {
                    prTiersWrapper = PRTiersHelper.getAdministrationParId(session, getIdFournisseurDemande());
                }

                descFournisseur = prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

                return descFournisseur;
            } else {
                return descFournisseur;
            }

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(this, e.getMessage());
            return "";
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getLibelleCourtSexe(), getLibellePays());
        }
    }

    public String getDetailGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = ((BSession) getISession()).getApplication()._getSecurityManager()
                    .getUserForVisa(((BSession) getISession()), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    /**
     * Méthode qui retourne le détail du requérant formaté
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getLibelleCourtSexe(), getLibellePays());
        }

    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = ((BSession) getISession()).getApplication()._getSecurityManager()
                    .getUserForVisa(((BSession) getISession()), getIdGestionnaire());
            return userName.getIdUser();
        }
    }

    public String getIdAdressePaiementDemande() {
        return idAdressePaiementDemande;
    }

    public final String getIdAffilieAdressePaiment() {
        return idAffilieAdressePaiment;
    }

    public String getIdDemandeParent() {
        return idDemandeParent;
    }

    public final String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    public String getIdFournisseurDemande() {
        return idFournisseurDemande;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    /**
     * renvoi le flag indiquant si cette demande est conventionné
     */
    public String getImageIsConventionne() {
        if (isConventionne) {
            return getImageSuccess();
        } else {
            return getImageError();
        }
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    public boolean getIsAfficherCaseForcerPaiement() {
        return isAfficherCaseForcerPaiement;
    }

    public boolean getIsAfficherDetail() {
        return isAfficherDetail;
    }

    public boolean getIsAfficherRemFournisseur() {
        return isAfficherRemFournisseur;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return ((BSession) getISession()).getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return ((BSession) getISession()).getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(((BSession) getISession()).getCode(((BSession) getISession()).getSystemCode("CIPAYORI",
                getCsNationalite())))) {
            return "";
        } else {
            return ((BSession) getISession()).getCodeLibelle(((BSession) getISession()).getSystemCode("CIPAYORI",
                    getCsNationalite()));
        }

    }

    public String getListMotifsRefusInput() {
        return listMotifsRefusInput;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * getter pour l'attribut methodes selecteur agent execution.
     * 
     * @return la valeur courante de l'attribut methodes selecteur agent execution
     */
    public Object[] getMethodesSelecteurAdressePaiement() {
        return RFSaisieDemandeAbstractViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    /**
     * getter pour l'attribut methodes selecteur agent execution.
     * 
     * @return la valeur courante de l'attribut methodes selecteur agent execution
     */
    public Object[] getMethodesSelecteurFournisseur() {
        return RFSaisieDemandeAbstractViewBean.METHODES_SEL_FOURNISSEUR;
    }

    public Map<String, String[]> getMontantsMotifsRefus() {
        if (null == montantsMotifsRefus) {
            montantsMotifsRefus = new HashMap<String, String[]>();
        }
        return montantsMotifsRefus;
    }

    public String getMotifsRefusAssociationInnerJavascript() {

        StringBuffer motifsRefusAssociationInnerJavascript = new StringBuffer();
        Vector<String[]> motifsRefus = getMotifsRefusParSousType();

        if (null != motifsRefus) {
            for (int i = 0; i < motifsRefus.size(); i++) {
                motifsRefusAssociationInnerJavascript.append("currentMotifsRefus[\"");
                motifsRefusAssociationInnerJavascript.append((motifsRefus.get(i))[0] + "\"]={libelle :\"");
                motifsRefusAssociationInnerJavascript.append((motifsRefus.get(i))[1] + "\", hasMontant :\"");
                motifsRefusAssociationInnerJavascript.append((motifsRefus.get(i))[2] + "\", montant :\"0.00\"};");
                // currentMotifsRefus["12"]={libelle :
                // "Décompte pour ancien canton", hasMontant : "false", montant
                // : "0.00"};
            }
        }

        return motifsRefusAssociationInnerJavascript.toString();
    }

    public String getMotifsRefusInnerJavascript() {

        StringBuffer motifsRefusInnerJavascript = new StringBuffer();
        motifsRefusInnerJavascript.append("var currentMotifsRefus=[");
        Vector<String[]> motifsRefus = getMotifsRefusParSousType();

        if (null != motifsRefus) {
            for (int i = 0; i < motifsRefus.size(); i++) {
                motifsRefusInnerJavascript.append("\"" + (motifsRefus.get(i))[0] + "\"");

                if (i != motifsRefus.size() - 1) {
                    motifsRefusInnerJavascript.append(",");
                }
            }
        }

        motifsRefusInnerJavascript.append("];");

        return motifsRefusInnerJavascript.toString(); // var
        // currentMotifsRefus=["66000601","66000602","66000603","66000604"];
    }

    public Vector<String[]> getMotifsRefusParSousType() {

        Vector<String[]> motifsRefus = null;

        try {
            motifsRefus = ((RFMotifsRefusListBuillder.getInstance(((BSession) getISession()))
                    .getMotifsRefusParSoinMap().get(codeTypeDeSoinList))).get(codeSousTypeDeSoinList);
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return motifsRefus;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals(Boolean.TRUE.toString()) ? true : false);
    }

    public String getPrenom() {
        return prenom;
    }

    public String getProvenance() {
        return provenance;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        try {
            RFSoinsListsBuilder rfSoinsListBuilder = RFSoinsListsBuilder.getInstance(((BSession) getISession()));
            sousTypeDeSoinParTypeInnerJavascript = rfSoinsListBuilder.getSousTypeDeSoinParTypeInnerJavascript();
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return sousTypeDeSoinParTypeInnerJavascript;

    }

    public BSpy getSpy() {
        return spy;
    }

    public Vector<String[]> getTiPays() throws Exception {
        return PRTiersHelper.getPays(getISession());
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * Méthode qui retourne une liste de type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return Vector
     */
    public Vector<String[]> getTypeDeSoinData() {
        try {
            typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(((BSession) getISession())).getTypeDeSoinsDemande();
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDeSoinsDemande;
    }

    public String getTypeValidation() {
        return typeValidation;
    }

    public boolean getWarningMode() {
        return warningMode;
    }

    public boolean hasCreationSpy() {
        return true;
    }

    public boolean hasSpy() {
        return true;
    }

    public boolean isChecked(String csMotifRefus) {

        if (!JadeStringUtil.isEmpty(getListMotifsRefusInput())) {
            String[] listMotifsRefusCheckedTab = getListMotifsRefusInput().split(",");

            int i = 0;
            while (i < listMotifsRefusCheckedTab.length) {

                if (listMotifsRefusCheckedTab[i].equals(csMotifRefus)) {
                    return true;
                }
                i++;
            }
        }

        return false;
    }

    public boolean isConventionne() {
        return isConventionne;
    }

    public boolean isErreurSaisieDemande() {
        return erreurSaisieDemande;
    }

    public boolean isFocusAdressePaiement() {
        return focusAdressePaiement;
    }

    public boolean isFocusMotifsDeRefus() {
        return focusMotifsDeRefus;
    }

    public boolean isHasMotifRefusDemande() {
        return hasMotifRefusDemande;
    }

    public boolean isModifie() {
        return modifie;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }

        if (getNss().length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    public boolean isPreparerDecisonDemarre() {
        return isPreparerDecisonDemarre;
    }

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    public void setAfficherCaseForcerPaiement(boolean isAfficherCaseForcerPaiement) {
        this.isAfficherCaseForcerPaiement = isAfficherCaseForcerPaiement;
    }

    public void setAfficherRemFournisseur(boolean isAfficherRemFournisseur) {
        this.isAfficherRemFournisseur = isAfficherRemFournisseur;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeSousTypeDeSoinList(String codeSousTypeDeSoinList) {
        this.codeSousTypeDeSoinList = codeSousTypeDeSoinList;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setCodeTypeDeSoinList(String codeTypeDeSoinList) {
        this.codeTypeDeSoinList = codeTypeDeSoinList;
    }

    public void setCreationSpy(BSpy creationSpy) {
        this.creationSpy = creationSpy;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDescFournisseur(String descFournisseur) {
        this.descFournisseur = descFournisseur;
    }

    public void setErreurSaisieDemande(boolean erreurSaisieDemande) {
        this.erreurSaisieDemande = erreurSaisieDemande;
    }

    public void setFocusAdressePaiement(boolean focusAdressePaiement) {
        this.focusAdressePaiement = focusAdressePaiement;
    }

    public void setFocusMotifsDeRefus(boolean focusMotifsDeRefus) {
        this.focusMotifsDeRefus = focusMotifsDeRefus;
    }

    public void setHasMotifRefusDemande(boolean hasMotifRefusDemande) {
        this.hasMotifRefusDemande = hasMotifRefusDemande;
    }

    public void setIdAdressePaiementDemande(String idAdressePaiement) {
        idAdressePaiementDemande = idAdressePaiement;
    }

    public final void setIdAffilieAdressePaiment(String idAffilieAdressePaiment) {
        this.idAffilieAdressePaiment = idAffilieAdressePaiment;
    }

    public void setIdDemandeParent(String idDemandeParent) {
        this.idDemandeParent = idDemandeParent;
    }

    public final void setIdDomaineApplicatif(String idDomaineApplicatif) {
        this.idDomaineApplicatif = idDomaineApplicatif;
    }

    public void setIdFournisseurDemande(String idFournisseur) {
        idFournisseurDemande = idFournisseur;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiementDepuisPyxis(String idTiers) {
        setIdAdressePaiementDemande(idTiers);
        setFocusAdressePaiement(true);
        setRetourPyxisTrue();
    }

    /**
     * setter pour l'attribut id tiers agent execution depuis pyxis.
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersFournisseurDepuisPyxis(String idTiers) {
        setIdFournisseurDemande(idTiers);
        setFocusMotifsDeRefus(true);
        setRetourPyxisTrue();
    }

    public void setIsAfficherDetail(boolean isAfficherDetail) {
        this.isAfficherDetail = isAfficherDetail;
    }

    public void setIsConventionne(boolean isConventionne) {
        this.isConventionne = isConventionne;
    }

    @Override
    public void setISession(BISession session) {
        this.session = session;
    }

    public void setListMotifsRefusInput(String listMotifsRefusInput) {
        this.listMotifsRefusInput = listMotifsRefusInput;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    public void setMontantsMotifsRefus(Map<String, String[]> montantsMotifsRefus) {
        this.montantsMotifsRefus = montantsMotifsRefus;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPreparerDecisonDemarre(boolean isPreparerDecisonDemarre) {
        this.isPreparerDecisonDemarre = isPreparerDecisonDemarre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.interfaces.util.nss.INSSViewBean#setProvenance(java .lang.String)
     */
    public void setProvenance(String string) {
        if (PRUtil.PROVENANCE_TIERS.equals(string)) {
            trouveDansCI = false;
            trouveDansTiers = true;
        } else if (PRUtil.PROVENANCE_CI.equals(string)) {
            trouveDansCI = true;
            trouveDansTiers = false;
        } else {
            trouveDansCI = false;
            trouveDansTiers = false;
        }
    }

    public void setRetourPyxisFalse() {
        retourDepuisPyxis = false;
    }

    public void setRetourPyxisTrue() {
        retourDepuisPyxis = true;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSpy(BSpy spy) {
        this.spy = spy;
    }

    public void setTrouveDansCI(boolean trouveDansCI) {
        this.trouveDansCI = trouveDansCI;
    }

    public void setTrouveDansTiers(boolean trouveDansTiers) {
        this.trouveDansTiers = trouveDansTiers;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    public void setTypeValidation(String typeValidation) {
        this.typeValidation = typeValidation;
    }

    public void setWarningMode(boolean warningMode) {
        this.warningMode = warningMode;
    }
}
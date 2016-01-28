/*
 * Créé le 26 mai 2010
 */
package globaz.cygnus.vb.qds;

import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.dossiers.RFDossierJointDecisionJointTiersManager;
import globaz.cygnus.utils.RFSoinsListsBuilder;
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
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * 
 * @author jje
 */
public abstract class RFQdAbstractViewBean implements FWViewBeanInterface, BIPersistentObject {

    private static final String LABEL_DROITS_TOUS = "DROITS_TOUS";

    private String augmentationQd = "";

    private String codeSousTypeDeSoin = "";

    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";
    private BSpy creationSpy = null;
    private String csCanton = "";
    private Vector<String[]> csDegresApiData = null;
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private Vector<String[]> etatsQdData = null;
    private Vector<String[]> genresPCAccordeeData = null;

    private Vector<String[]> genresQdData = null;
    private String idDossier = "";

    private String idGestionnaire = "";
    private String idTiers = "";

    private Boolean isAfficherCaseRi = false;
    // private Boolean isCcju = Boolean.FALSE;
    private boolean isAfficherDetail = false;
    private Boolean isAfficherTypeRemboursement = Boolean.FALSE;

    private boolean modifie = false;

    private String nom = "";

    private String nss = "";

    private String prenom = "";

    private String provenance = "";
    private BISession session = null;

    private String soldeCharge = "";
    private Vector<String[]> sourcesQdData = null;
    private transient String sousTypeDeSoinParTypeInnerJavascript = "";

    private BSpy spy = null;

    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;
    private transient Vector<String[]> typeDeSoinsDemande = null;

    private Vector<String[]> typesBeneficiaireData = null;

    private Vector<String[]> typesPcData = null;
    private Vector<String[]> typesRemboursementConjointData = null;
    private Vector<String[]> typesRemboursementRequerantData = null;

    public RFQdAbstractViewBean() {
        super();
    }

    public String getAugmentationQd() {
        return augmentationQd;
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

    public Vector<String[]> getCsDegresApiData() {

        if (csDegresApiData == null) {
            csDegresApiData = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_DEGRE_API, getSession());
            csDegresApiData.add(0, new String[] { "", "" });
        }

        return csDegresApiData;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), this.getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getLibelleCourtSexe(), this.getLibellePays());
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

    public Vector<String[]> getEtatsQdData(boolean isdroitsTous) {

        if (etatsQdData == null) {
            etatsQdData = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_ETAT_QD, getSession());

            // ajout des options custom
            if (isdroitsTous) {
                etatsQdData.add(0, new String[] { RFDossierJointDecisionJointTiersManager.CLE_DROITS_TOUS,
                        getSession().getLabel(RFQdAbstractViewBean.LABEL_DROITS_TOUS) });
            }
        }

        return etatsQdData;
    }

    public Vector<String[]> getGenresPCAccordeeData() {

        if (genresPCAccordeeData == null) {
            genresPCAccordeeData = PRCodeSystem.getLibellesPourGroupe(IPCPCAccordee.GROUPE_GENRE_PC, getSession());
            genresPCAccordeeData.add(0, new String[] { "", "" });
        }

        return genresPCAccordeeData;
    }

    public Vector<String[]> getGenresQdData() {

        if (genresQdData == null) {
            genresQdData = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_GENRE_QD, getSession());
            genresQdData.add(0, new String[] { "", "" });
        }

        return genresQdData;
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

    public Boolean getHasConjoint() {
        // TODO
        return Boolean.FALSE;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    public Boolean getIsAfficherCaseRi() {
        return isAfficherCaseRi;
    }

    public Boolean getIsAfficherTypeRemboursement() {
        return isAfficherTypeRemboursement;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    // public Boolean getIsCcju() {
    // return this.isCcju;
    // }

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

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(((BSession) getISession()).getCode(((BSession) getISession()).getSystemCode("CIPAYORI",
                csNationalite)))) {
            return "";
        } else {
            return ((BSession) getISession()).getCodeLibelle(((BSession) getISession()).getSystemCode("CIPAYORI",
                    csNationalite));
        }

    }

    public String getNom() {
        return nom;
    }

    public String getNomConjoint() {
        // TODO
        return "";
    }

    public String getNss() {
        return nss;
    }

    public String getNssConjoint() {
        // TODO
        return "";
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

    public String getPrenomConjoint() {
        // TODO
        return "";
    }

    public String getProvenance() {
        return provenance;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    public String getSoldeCharge() {
        return soldeCharge;
    }

    public Vector<String[]> getSourcesQdData() {

        if (sourcesQdData == null) {
            sourcesQdData = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_SOURCE_QD, getSession());
            sourcesQdData.add(0, new String[] { "", "" });
        }

        return sourcesQdData;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        // if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)){
        try {
            RFSoinsListsBuilder rfSoinsListBuilder = RFSoinsListsBuilder.getInstance(((BSession) getISession()));
            sousTypeDeSoinParTypeInnerJavascript = rfSoinsListBuilder.getSousTypeDeSoinParTypeInnerJavascript();
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }
        // }

        return sousTypeDeSoinParTypeInnerJavascript;

    }

    public BSpy getSpy() {
        return spy;
    }

    public Vector<String[]> getTiPays() throws Exception {
        return PRTiersHelper.getPays(getISession());
    }

    /**
     * Méthode qui retourne une liste de type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return Vector
     */
    public Vector<String[]> getTypeDeSoinData() {
        try {
            // if (typeDeSoinsDemande == null) {
            typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(((BSession) getISession())).getTypeDeSoinsDemande();
            // }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDeSoinsDemande;
    }

    public Vector<String[]> getTypesBeneficiaireData() {

        if (typesBeneficiaireData == null) {
            typesBeneficiaireData = PRCodeSystem.getLibellesPourGroupe(
                    IRFTypesBeneficiairePc.CS_GROUPE_TYPE_BENEFICIAIRE_PC, getSession());
            typesBeneficiaireData.add(0, new String[] { "", "" });
        }

        return typesBeneficiaireData;
    }

    public Vector<String[]> getTypesPcData() {

        if (typesPcData == null) {
            typesPcData = PRCodeSystem.getLibellesPourGroupe(IPCPCAccordee.GROUPE_TYPE_PC, getSession());
            typesPcData.add(0, new String[] { "", "" });
        }

        return typesPcData;
    }

    public Vector<String[]> getTypesRemboursementConjointData() {

        if (typesRemboursementConjointData == null) {
            typesRemboursementConjointData = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_TYPE_REMBOURSEMENT,
                    getSession());
            // this.typesRemboursementConjointData.add(0, new String[] { "", "" });
        }

        return typesRemboursementConjointData;
    }

    public Vector<String[]> getTypesRemboursementRequerantData() {

        if (typesRemboursementRequerantData == null) {
            typesRemboursementRequerantData = PRCodeSystem.getLibellesPourGroupe(IRFQd.CS_GROUPE_TYPE_REMBOURSEMENT,
                    getSession());
            // this.typesRemboursementRequerantData.add(0, new String[] { "", "" });
        }

        return typesRemboursementRequerantData;
    }

    public boolean hasCreationSpy() {
        return true;
    }

    public boolean hasSpy() {
        return true;
    }

    public boolean isAfficherDetail() {
        return isAfficherDetail;
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

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    public void setAfficherDetail(boolean isAfficherDetail) {
        this.isAfficherDetail = isAfficherDetail;
    }

    public void setAugmentationQd(String augmentationQd) {
        this.augmentationQd = augmentationQd;
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

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsAfficherCaseRi(Boolean isAfficherCaseRi) {
        this.isAfficherCaseRi = isAfficherCaseRi;
    }

    public void setIsAfficherTypeRemboursement(Boolean isAfficherTypeRemboursement) {
        this.isAfficherTypeRemboursement = isAfficherTypeRemboursement;
    }

    // public void setIsCcju(Boolean isCcju) {
    // this.isCcju = isCcju;
    // }

    @Override
    public void setISession(BISession session) {
        this.session = session;
    }

    public void setModifie(boolean modifie) {
        this.modifie = modifie;
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

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSoldeCharge(String soldeCharge) {
        this.soldeCharge = soldeCharge;
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

    public void setTypesRemboursementConjointData(Vector<String[]> typesRemboursementConjointData) {
        this.typesRemboursementConjointData = typesRemboursementConjointData;
    }

    public void setTypesRemboursementRequerantData(Vector<String[]> typesRemboursementRequerantData) {
        this.typesRemboursementRequerantData = typesRemboursementRequerantData;
    }

}

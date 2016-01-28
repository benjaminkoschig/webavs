package globaz.cygnus.vb.attestations;

import globaz.cygnus.api.attestations.IRFAttestations;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.db.attestations.RFAssAttestationDossier;
import globaz.cygnus.db.attestations.RFAttestation;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BStatement;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * author fha
 */
public class RFAttestationViewBean extends RFAssAttestationDossier implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String TYPE_ATTESTATION_AVS = "attestationAVS";
    public static final String TYPE_ATTESTATION_DEFAUT = "defaut";

    public static final String TYPE_ATTESTATION_FRAIS_LIVRAISON9 = "fraisLivraison";
    public static final String TYPE_ATTESTATION_MAINTIEN_DOMICILE13 = "maintienDomicile";

    public static final String TYPE_ATTESTATION_MOYENS_AUX_BON11 = "moyensAuxiliaireBons";
    public static final String TYPE_ATTESTATION_REGIME_ALI2 = "regimeAlimentaire";

    private String codeSousTypeDeSoin = "";
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";
    private String csNationalite = "";
    private String csNiveauAvertissement = "";
    private Vector<String[]> csNiveauAvertissementData = null;
    private String csSexe = "";
    private String csTypeAttestation = "";
    private Vector<String[]> csTypeAttestationData = null;
    private String dateCreation = "";
    private String dateDeces = "";

    private String dateNaissance = "";

    private String detailAssure = "";
    // 1 si on vient de attestation - 0 sinon
    private String fromAttestation = "";
    private String idDossier = "";
    private String idGestionnaire = "";
    private String idSousTypeDeSoin = "";
    private String idTypeDeSoin = "";
    // concatène tous les champs
    private String infosDossier = "";
    private Boolean isUpdate = Boolean.FALSE;
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String sousTypeDeSoinParTypeInnerJavascript = "";
    private String typeAttestation = "";
    private Vector<String[]> typeDeSoinsDemandeData = null;
    private String typeDocument = "";

    // ~ Constructor
    // ------------------------------------------------------------------------------------------------
    public RFAttestationViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        dateCreation = statement.dbReadDateAMJ(RFAttestation.FIELDNAME_DATE_CREATION);
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

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsNiveauAvertissement() {
        return csNiveauAvertissement;
    }

    public Vector getCsNiveauAvertissementData() {
        if (csNiveauAvertissementData == null) {
            csNiveauAvertissementData = PRCodeSystem.getLibellesPourGroupe(IRFDemande.CS_GROUPE_STATUT_DEMANDE,
                    getSession());
        }

        return csNiveauAvertissementData;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeAttestation() {
        return csTypeAttestation;
    }

    public Vector<String[]> getCsTypeAttestationData() {
        if (csTypeAttestationData == null) {
            csTypeAttestationData = PRCodeSystem.getLibellesPourGroupe(IRFAttestations.CS_TYPE_ATTESTATION,
                    getSession());

            csTypeAttestationData.add(0, new String[] { "", "" });
        }

        return csTypeAttestationData;
    }

    public String getDateCreation() {
        return dateCreation;
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
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    public String getFromAttestation() {
        return fromAttestation;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getIdGestionnaire());
            return userName.getIdUser();
        }
    }

    @Override
    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public String getIdTypeDeSoin() {
        return idTypeDeSoin;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
    }

    public String getInfosDossier() {
        return infosDossier;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
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

        String code = getSession().getSystemCode("CIPAYORI", getCsNationalite());

        if ("999".equals(getSession().getCode(code))) {
            return "";
        } else {
            return getSession().getCodeLibelle(code);
        }

    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)) {
            try {
                sousTypeDeSoinParTypeInnerJavascript = RFSoinsListsBuilder.getInstance(getSession())
                        .getSousTypeDeSoinParTypeInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;
    }

    public String getTypeAttestation() {
        return typeAttestation;
    }

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemandeData == null) {
                typeDeSoinsDemandeData = RFSoinsListsBuilder.getInstance(getSession()).getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDeSoinsDemandeData;
    }

    public Vector<String[]> getTypeDeSoinsDemandeData() {
        return typeDeSoinsDemandeData;
    }

    public String getTypeDocument() {
        return typeDocument;
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

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsNiveauAvertissement(String csNiveauAvertissement) {
        this.csNiveauAvertissement = csNiveauAvertissement;
    }

    public void setCsNiveauAvertissementData(Vector<String[]> csNiveauAvertissementData) {
        this.csNiveauAvertissementData = csNiveauAvertissementData;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypeAttestation(String csTypeAttestation) {
        this.csTypeAttestation = csTypeAttestation;
    }

    public void setCsTypeAttestationData(Vector<String[]> csTypeAttestationData) {
        this.csTypeAttestationData = csTypeAttestationData;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDetailAssure(String detailAssure) {
        this.detailAssure = detailAssure;
    }

    public void setFromAttestation(String fromAttestation) {
        this.fromAttestation = fromAttestation;
    }

    @Override
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    public void setInfosDossier(String infosDossier) {
        this.infosDossier = infosDossier;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
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

    public void setSousTypeDeSoinParTypeInnerJavascript(String sousTypeDeSoinParTypeInnerJavascript) {
        this.sousTypeDeSoinParTypeInnerJavascript = sousTypeDeSoinParTypeInnerJavascript;
    }

    public void setTypeAttestation(String typeAttestation) {
        this.typeAttestation = typeAttestation;
    }

    public void setTypeDeSoinsDemandeData(Vector<String[]> typeDeSoinsDemandeData) {
        this.typeDeSoinsDemandeData = typeDeSoinsDemandeData;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    // ceci n'est plus possible !!!
    // retrouver le typeAttestation à partir de l'idSousTypeSoin
    // public String typeAttestation(int idSousTypeSoin) {
    // if (idSousTypeSoin == new Integer(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE)) {
    // return IRFAttestations.REGIME_ALIMENTAIRE;
    // } else if (((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_3_LOMBOSTAT_ORTHOPEDIQUE)) && (idSousTypeSoin <=
    // new Integer(
    // IRFTypesDeSoins.st_3_MINERVE)))
    // || (idSousTypeSoin == new Integer(IRFTypesDeSoins.st_2_REGIME_ALIMENTAIRE))
    // || ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE)) && (idSousTypeSoin <= new Integer(
    // IRFTypesDeSoins.st_9_BARRIERES)))
    // || (idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE))
    // || (idSousTypeSoin <= new Integer(
    // IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC))
    // || ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_16_AU_LIEU_DU_TRAITEMENT_MEDICAL)) && (idSousTypeSoin <=
    // new Integer(
    // IRFTypesDeSoins.st_16_VISITE_CHEZ_LES_PARENTS_ENFANT_EN_EMS)))) {
    // return IRFAttestations.CERTIFICAT_MOYENS_AUXILIAIRES;
    // } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_5_APPAREIL_ACOUSTIQUE))
    // && (idSousTypeSoin <= new Integer(IRFTypesDeSoins.st_5_PROTHESE_FACIALE_EPITHESES))) {
    // return IRFAttestations.DECISION_MOYENS_AUXILIAIRES;
    // } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_9_LIT_ELECTRIQUE))
    // && (idSousTypeSoin <= new Integer(IRFTypesDeSoins.st_10_REPRISE_DE_LIT_ELECTRIQUE))) {
    // return IRFAttestations.FRAIS_LIVRAISON;
    // } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_11_MACHINE_A_ECRIRE_ELECTRIQUE))
    // && (idSousTypeSoin <= new Integer(IRFTypesDeSoins.st_11_MAGNETOPHONE_POUR_AVEUGLE))) {
    // return IRFAttestations.BON_MOYENS_AUXILIAIRES;
    // } else if ((idSousTypeSoin >= new Integer(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS))
    // && (idSousTypeSoin <= new Integer(
    // IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC))) {
    // return IRFAttestations.MAINTIEN_DOMICILE;
    // } else {
    // return "";
    // }
    // }

}

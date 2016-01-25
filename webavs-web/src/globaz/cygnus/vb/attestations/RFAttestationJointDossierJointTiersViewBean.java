package globaz.cygnus.vb.attestations;

import globaz.cygnus.db.attestations.RFAttestationJointDossierJointTiers;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * author fha
 */
public class RFAttestationJointDossierJointTiersViewBean extends RFAttestationJointDossierJointTiers implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeSousTypeDeSoin = "";
    private String codeSousTypeDeSoinList = "";
    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";
    private String forIdDossier = "";
    private String idSousTypeDeSoin = "";
    private String idTypeDeSoin = "";
    private String infosDossier = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likePrenom = "";

    private transient String sousTypeDeSoinParTypeInnerJavascript = "";
    private transient Vector<String[]> typeDeSoinsDemande = null;

    // ~ Constructor
    // ------------------------------------------------------------------------------------------------
    public RFAttestationJointDossierJointTiersViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    // methods

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

    public String getForIdDossier() {
        return forIdDossier;
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

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikePrenom() {
        return likePrenom;
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

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(getSession()).getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(FWViewBeanInterface.ERROR);
        }

        return typeDeSoinsDemande;
    }

    public Vector<String[]> getTypeDeSoinsDemande() {
        return typeDeSoinsDemande;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
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

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
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

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public void setTypeDeSoinsDemande(Vector<String[]> typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
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
    // || (idSousTypeSoin == new Integer(IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE))
    // || (idSousTypeSoin == new Integer(
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

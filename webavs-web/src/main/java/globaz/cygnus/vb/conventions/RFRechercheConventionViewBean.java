// créé le 24 mars 2010
package globaz.cygnus.vb.conventions;

import globaz.cygnus.db.conventions.RFConvention;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointConventionAssure;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSpy;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;

/**
 * author fha
 */
public class RFRechercheConventionViewBean extends RFConventionJointAssConFouTsJointFournisseurJointConventionAssure
        implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";
    private transient Boolean forActif = Boolean.FALSE;
    private transient String forCodeSousTypeSoin = "";
    private transient String forCodeTypeSoin = "";
    private transient String forCsSexe = "";
    private transient String forDateNaissance = "";
    private transient String forFournisseur = "";
    private transient String forLibelle = "";
    private transient String forListeSousTypeSoin = "";
    private transient String forListeTypeSoin = "";
    private transient String forOrderBy = "";
    private transient Boolean forParConvention = Boolean.FALSE;
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private transient String idGestionnaire = "";
    private transient String likeNom = "";

    private transient String likeNumeroAVS = "";
    private transient String likePrenom = "";

    private transient Vector orderBy = null;

    private String sousTypeDeSoinParTypeInnerJavascript = "";
    private transient Vector typeDeSoinsDemande = null;

    // ~ Constructor
    // ------------------------------------------------------------------------------------------------
    public RFRechercheConventionViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    // ~ Accessors and mutators
    // ------------------------------------------------------------------------------------------------

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    @Override
    public BSpy getCreationSpy() {

        RFConvention sts = new RFConvention();

        try {
            sts = RFConvention.loadConvention(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdConvention());
        } catch (Exception e) {
        }
        return sts.getCreationSpy();
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes On passe les valeurs de
     * RFConventionJointAssConFouTsJointFournisseurJointConventionAssure
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

        if (JadeStringUtil.isEmpty(getVisaGestionnaire())) {
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getVisaGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    public Boolean getForActif() {
        return forActif;
    }

    public String getForCodeSousTypeSoin() {
        return forCodeSousTypeSoin;
    }

    public String getForCodeTypeSoin() {
        return forCodeTypeSoin;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForFournisseur() {
        return forFournisseur;
    }

    public String getForLibelle() {
        return forLibelle;
    }

    public String getForListeSousTypeSoin() {
        return forListeSousTypeSoin;
    }

    public String getForListeTypeSoin() {
        return forListeTypeSoin;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public Boolean getForParConvention() {
        return forParConvention;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        if (JadeStringUtil.isEmpty(getVisaGestionnaire())) {
            return "";
        } else {
            JadeUser userName = getSession().getApplication()._getSecurityManager()
                    .getUserForVisa(getSession(), getVisaGestionnaire());
            return userName.getIdUser();
        }
    }

    @Override
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getImageError() {
        return "/images/erreur.gif";
    }

    public String getImageSuccess() {
        return "/images/ok.gif";
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

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
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

    public Vector getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector(3);
            orderBy.add(new String[] { "", "" });
            orderBy.add(new String[] { RFConvention.FIELDNAME_TEXT_LIBELLE,
                    getSession().getLabel("JSP_RF_SAISIE_CONV_LIBELLE") });
        }
        return orderBy;
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
                setMsgType(ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;

    }

    @Override
    public BSpy getSpy() {

        RFConvention sts = new RFConvention();

        try {
            sts = RFConvention.loadConvention(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdConvention());
        } catch (Exception e) {
        }
        return sts.getSpy();
    }

    public Vector getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(getSession()).getTypeDeSoinsDemande();
            }
        } catch (Exception e) {
            setMessage(e.getMessage());
            setMsgType(ERROR);
        }

        return typeDeSoinsDemande;
    }

    public Vector getTypeDeSoinsDemande() {
        return typeDeSoinsDemande;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setForActif(Boolean forActif) {
        this.forActif = forActif;
    }

    public void setForCodeSousTypeSoin(String forCodeSousTypeSoin) {
        this.forCodeSousTypeSoin = forCodeSousTypeSoin;
    }

    public void setForCodeTypeSoin(String forCodeTypeSoin) {
        this.forCodeTypeSoin = forCodeTypeSoin;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForFournisseur(String forFournisseur) {
        this.forFournisseur = forFournisseur;
    }

    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

    public void setForListeSousTypeSoin(String forListeSousTypeSoin) {
        this.forListeSousTypeSoin = forListeSousTypeSoin;
    }

    public void setForListeTypeSoin(String forListeTypeSoin) {
        this.forListeTypeSoin = forListeTypeSoin;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForParConvention(Boolean forParConvention) {
        this.forParConvention = forParConvention;
    }

    @Override
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
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

    public void setOrderBy(Vector orderBy) {
        this.orderBy = orderBy;
    }

    public void setSousTypeDeSoinParTypeInnerJavascript(String sousTypeDeSoinParTypeInnerJavascript) {
        this.sousTypeDeSoinParTypeInnerJavascript = sousTypeDeSoinParTypeInnerJavascript;
    }

    public void setTypeDeSoinsDemande(Vector typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
    }

}

/*
 * Créé le 30 mars 2010
 */

package globaz.cygnus.vb.decisions;

import globaz.babel.api.ICTDocument;
import globaz.commons.nss.NSUtil;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFDecisions;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.decisions.RFDecisionJointTiers;
import globaz.cygnus.services.RFMembreFamilleService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;

/**
 * 
 * @author fha
 */
public class RFDecisionJointTiersViewBean extends RFDecisionJointTiers implements FWViewBeanInterface {
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Object[] METHODES_SEL_ADRESSE_PAIEMENT = new Object[] {
            new String[] { "idTiersAdressePaiementDepuisPyxis", "idTiers" },
            new String[] { "descAdressePaiement", "nom" } };
    private static final Object[] METHODES_SEL_ASSURE = new Object[] {
            new String[] { "idTiersAssureDepuisPyxis", "idTiers" }, new String[] { "descAssure", "nom" } };
    private static final Object[] METHODES_SEL_FOURNISSEUR = new Object[] {
            new String[] { "idTiersFournisseurDepuisPyxis", "idTiers" }, new String[] { "descFournisseur", "nom" } };

    public static Object[] getMethodesSelFournisseur() {
        return RFDecisionJointTiersViewBean.METHODES_SEL_FOURNISSEUR;
    }

    private String adresse = "";
    private String adressePaiementNom = "";
    // Liste des assurés
    private ArrayList<String> AssureArray = new ArrayList<String>();

    private boolean autreRetour = false;

    protected String codeIsoLangue = "";
    private Boolean concerneAssure = Boolean.FALSE;

    // Liste des objets fournisseurs-type de soin
    private ArrayList<RFCopieDecisionsValidationData> CopieDecisionArray = new ArrayList<RFCopieDecisionsValidationData>();
    private transient String csEtatsDecision = "";
    private transient Vector<String[]> csEtatsDecisionData = null;

    private String descAdressePaiement = "";

    private String descAssure = "";
    private String descFournisseur = "";
    private String detailAssureGroupBy = "";
    private String detailConjoint = "";
    protected ICTDocument documentDecision;
    protected ICTDocument documentHelper;
    private transient String forAnneeQD = "";
    private transient String forCsEtatDecision = "";
    private transient String forCsSexe = "";
    private String forDateDebut = "";
    private String forDateFin = "";
    private transient String forDateNaissance = "";
    private transient String forDecideFrom = "";
    private transient String forIdDecision = "";
    private transient String forNumeroDecision = "";
    private transient String forOrderBy = "";
    private transient String forPrepareFrom = "";
    private transient String forPreparePar = "";
    private transient String forValideFrom = "";
    private transient String forValidePar = "";
    // Liste des objets fournisseurs-type de soin
    private ArrayList<String> FournisseurTypeArray = new ArrayList<String>();
    private String idAdressePaiement = "";
    private String idAssure = "";
    private String idCopie = "";
    private String idDestinataire = "";
    private String idFournisseur = "";
    // Liste des ìd assurés à supprimer
    private ArrayList<String> idSuppressionAssureArray = new ArrayList<String>();
    // Liste des ìd fournisseurs-type de soin à supprimer
    private ArrayList<String> idSuppressionFournisseurArray = new ArrayList<String>();
    private String jsonCopie = "";
    private String libelle = "";
    private transient String likeNom = "";
    private transient String likeNumeroAVS = "";
    private transient String likePrenom = "";
    private boolean modifie = false;

    private String montantAPayerPlusExcedentDeRecettePlusForcerPaiement = "";

    private String montantTotalARembourser = "";
    private String nomTiers = "";
    private String nssConjoint = null;
    // concerne l'assuré
    private String nssTiers = "";
    private transient Vector<String[]> orderBy = null;
    private String prenomTiers = "";
    private boolean retourDepuisPyxis = false;
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;

    // ~ Constructor
    // ------------------------------------------------------------------------------------------------
    public RFDecisionJointTiersViewBean() {
        super();

        // TODO Auto-generated constructor stub
    }

    // ~ Accessors and mutators
    // ------------------------------------------------------------------------------------------------
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_PONCTUELLE);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            documentDecision = documents[0];
        }

    }

    public String getAdresse() {
        return adresse;
    }

    public String getAdresseCourrier(String idTiers, BISession session) throws Exception {

        String adresse = PRTiersHelper.getAdresseDomicileFormatee(session, idTiers);
        // remplacer les retours à la ligne par **
        adresse = adresse.replaceAll("\n", "**");
        adresse = adresse.replaceAll("\\s*", "");

        return adresse;
    }

    // renvoi l'adresse formatée
    public String getAdresseEnvoiCourrier() throws Exception {
        String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(), getIdTiers(), "",
                RFApplication.DEFAULT_APPLICATION_CYGNUS);
        return adresse.replaceAll("\n", "<br>");
    }

    public String getAdressePaiementNom() {
        return adressePaiementNom;
    }

    public ArrayList<String> getAssureArray() {
        return AssureArray;
    }

    public Boolean getConcerneAssure() {
        return concerneAssure;
    }

    public Vector<String[]> getConvOrderBy() {
        return orderBy;
    }

    public ArrayList<RFCopieDecisionsValidationData> getCopieDecisionArray() {
        return CopieDecisionArray;
    }

    @Override
    public BSpy getCreationSpy() {

        RFDecision decision = new RFDecision();

        try {
            decision = RFDecision.loadDecision(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdDecision());
        } catch (Exception e) {
        }
        return decision.getCreationSpy();
    }

    public Vector<String[]> getCsEtatDecisionData(boolean isdroitsTous) {
        if (csEtatsDecisionData == null) {
            csEtatsDecisionData = PRCodeSystem
                    .getLibellesPourGroupe(IRFDecisions.CS_GROUPE_ETAT_DECISION, getSession());

            csEtatsDecisionData.add(0, new String[] { "", "" });
        }

        return csEtatsDecisionData;
    }

    public String getCsEtatsDecision() {
        return csEtatsDecision;
    }

    public Vector getCsEtatsDecisionData() {
        return csEtatsDecisionData;
    }

    public String getDescAdressePaiement() {
        return descAdressePaiement;
    }

    // private String texteAnnexe = "";

    public String getDescAssure() {
        return descAssure;
    }

    public String getDescFournisseur() {
        try {

            if (!JadeStringUtil.isBlankOrZero(getIdFournisseur())
                    && (JadeStringUtil.isBlank(descFournisseur) || isRetourDepuisPyxis())) {

                PRTiersWrapper prTiersWrapper = PRTiersHelper.getTiersParId(getISession(), getIdFournisseur());

                if (null == prTiersWrapper) {
                    prTiersWrapper = PRTiersHelper.getAdministrationParId(getISession(), getIdFournisseur());
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

    public String getDetailAssureGroupBy() {
        return detailAssureGroupBy;
    }

    public String getDetailConjoint() throws Exception {
        rechercheDetailConjointInit();
        return detailConjoint;
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

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
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

    public String getForAnneeQD() {
        return forAnneeQD;
    }

    public String getForCsEtatDecision() {
        return forCsEtatDecision;
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDecideFrom() {
        return forDecideFrom;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForNumeroDecision() {
        return forNumeroDecision;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getForPrepareFrom() {
        return forPrepareFrom;
    }

    public String getForPreparePar() {
        return forPreparePar;
    }

    public String getForValideFrom() {
        return forValideFrom;
    }

    public String getForValidePar() {
        return forValidePar;
    }

    public ArrayList<String> getFournisseurTypeArray() {
        return FournisseurTypeArray;
    }

    public String getGestionnaire() throws FWSecurityLoginException, Exception {

        return getIdGestionnaire();

    }

    @Override
    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdAssure() {
        return idAssure;
    }

    public String getIdCopie() {
        return idCopie;
    }

    public String getIdDestinataire() {
        return idDestinataire;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public ArrayList<String> getIdSuppressionAssureArray() {
        return idSuppressionAssureArray;
    }

    public ArrayList<String> getIdSuppressionFournisseurArray() {
        return idSuppressionFournisseurArray;
    }

    public String getJsonCopie() {
        return jsonCopie;
    }

    public String getLibelle() {
        return libelle;
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

    /**
     * Méthode qui retourne le libellé du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return "Homme";// getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return "Femme";// getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
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
     * getter pour l'attribut methodes selecteur agent execution.
     * 
     * @return la valeur courante de l'attribut methodes selecteur agent execution
     */
    public Object[] getMethodesSelecteurFournisseur() {
        return RFDecisionJointTiersViewBean.METHODES_SEL_FOURNISSEUR;
    }

    public String getMontantAPayerPlusExcedentDeRecettePlusForcerPaiement() {
        return montantAPayerPlusExcedentDeRecettePlusForcerPaiement;
    }

    public String getMontantDecisionList() {
        return new FWCurrency(new BigDecimal(getMontantRembourserRFM()).add(
                new BigDecimal(getMontantARembourserParLeDsas())).toString()).toStringFormat();
    }

    public String getMontantRembourserRFM() {
        return new BigDecimal(getMontantTotalRFM()).toString();
    }

    // selection fournisseur, assuré et adresse payement

    public String getMontantTotalARembourser() {
        return montantTotalARembourser;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNssConjoint() {
        return nssConjoint;
    }

    public String getNssTiers() {
        return nssTiers;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), this.isNNSS().equals(Boolean.TRUE.toString()) ? true : false);
    }

    public Vector<String[]> getOrderBy() {
        return orderBy;
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        if (orderBy == null) {

            String orderByDateSurDocumentNullLast = " CASE WHEN " + RFDecision.FIELDNAME_DATE_SUR_DOCUMENT
                    + " IS NULL THEN 0 ELSE " + RFDecision.FIELDNAME_DATE_SUR_DOCUMENT + " END DESC";

            orderBy = new Vector<String[]>(2);
            orderBy.add(new String[] {
                    orderByDateSurDocumentNullLast + "," + RFDecisionJointTiers.FIELDNAME_NOM + ","
                            + RFDecisionJointTiers.FIELDNAME_PRENOM + "," + RFDecision.FIELDNAME_DATE_PREPARATION
                            + " DESC", getSession().getLabel("JSP_RF_DECISION_DATE_DECISION") });
            orderBy.add(new String[] {
                    RFDecisionJointTiers.FIELDNAME_NOM + "," + RFDecisionJointTiers.FIELDNAME_PRENOM,
                    getSession().getLabel("JSP_RF_DECISION_NOM") });
        }

        return orderBy;
    }

    public String getPhraseIncitationDepot() {
        return documentDecision.getTextes(4).getTexte(9).getDescription();
    }

    public String getPhraseRemarque() {
        return documentDecision.getTextes(4).getTexte(5).getDescription();
    }

    public String getPhraseRetourBV() {
        return documentDecision.getTextes(4).getTexte(10).getDescription();
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    @Override
    public BSpy getSpy() {

        RFDecision decision = new RFDecision();

        try {
            decision = RFDecision.loadDecision(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdDecision());
        } catch (Exception e) {
        }
        return decision.getSpy();
    }

    public Vector<String[]> getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public boolean isAutreRetour() {
        return autreRetour;
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

    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    // @Override
    // public String getTexteAnnexe() {
    // return this.texteAnnexe;
    // }

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    // ~ methods
    // ------------------------------------------------------------------------------------------------------

    /**
     * Méthode remontant le détail du conjoint
     * 
     * @param FWViewBeanInterface
     *            , BSession
     * @throws Exception
     */
    // public void rechercheDetailConjointInit() throws Exception {
    //
    // this.detailConjoint = "";
    // String urlGed = "";
    // // Recherche du conjoint
    // BITransaction transaction = null;
    // try {
    // transaction = (this.getSession()).newTransaction();
    // transaction.openTransaction();
    //
    // RFMembreFamilleService rfMembreFamilleService = new RFMembreFamilleService((BTransaction) transaction);
    //
    // MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes = rfMembreFamilleService.getMembreFamille(
    // this.getIdTiers(), "", false);
    //
    // for (MembreFamilleVO membreFamille : searchMembresFamilleRequerantDomaineRentes) {
    // if ((membreFamille != null)
    // && membreFamille.getRelationAuRequerant().equals(
    // ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT)) {
    //
    // // Pas d'idTiers, pas de RFM
    // if (!JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())) {
    //
    // urlGed = this.servletContextGed + this.userActionGed + this.numAvsTiersGed
    // + membreFamille.getNss() + null + this.serviceNameGed;
    //
    // this.nssConjoint = membreFamille.getNss();
    //
    // this.detailConjoint = PRNSSUtil.formatDetailRequerantDetail("<b>" + membreFamille.getNss()
    // + "</b>", membreFamille.getNom() + " " + membreFamille.getPrenom(), membreFamille
    // .getDateNaissance(), this.getSession().getCodeLibelle(membreFamille.getCsSexe()), this
    // .getSession().getCodeLibelle(membreFamille.getCsNationalite())
    // + "<a href=\"#\" id=\"lienGed\" onclick=\"window.open('"
    // + urlGed
    // + "','GED_CONSULT')\">" + "GED" + "</a>");
    //
    // // setIdSfConjoint(membreFamille.getIdTiers());
    // }
    // }
    // }
    // } catch (NullPointerException e) {
    // this.detailConjoint = "";
    // } catch (Exception e) {
    // RFUtils.setMsgExceptionErreurViewBean(this, e.getMessage());
    // } finally {
    // if (transaction != null) {
    // transaction.closeTransaction();
    // }
    // }
    // }

    public void rechercheDetailConjointInit() throws Exception {

        detailConjoint = "";
        // String urlGed = "";
        // Recherche du conjoint
        BITransaction transaction = null;
        try {
            transaction = (getSession()).newTransaction();
            transaction.openTransaction();

            RFMembreFamilleService rfMembreFamilleService = new RFMembreFamilleService((BTransaction) transaction);

            MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes = rfMembreFamilleService.getMembreFamille(
                    getIdTiers(), "", false);

            for (MembreFamilleVO membreFamille : searchMembresFamilleRequerantDomaineRentes) {
                if ((membreFamille != null)
                        && membreFamille.getRelationAuRequerant().equals(
                                ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT)) {

                    // Pas d'idTiers, pas de RFM
                    if (!JadeStringUtil.isIntegerEmpty(membreFamille.getIdTiers())) {

                        nssConjoint = membreFamille.getNss();

                        detailConjoint = PRNSSUtil.formatDetailRequerantDetail("<b>" + membreFamille.getNss() + "</b>",
                                membreFamille.getNom() + " " + membreFamille.getPrenom(), membreFamille
                                        .getDateNaissance(), getSession().getCodeLibelle(membreFamille.getCsSexe()),
                                getSession().getCodeLibelle(membreFamille.getCsNationalite()));
                    }
                }
            }
        } catch (NullPointerException e) {
            detailConjoint = "";
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(this, e.getMessage());
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAdressePaiementNom(String adressePaiementNom) {
        this.adressePaiementNom = adressePaiementNom;
    }

    public void setAssureArray(ArrayList<String> assureArray) {
        AssureArray = assureArray;
    }

    public void setAutreRetour(boolean autreRetour) {
        this.autreRetour = autreRetour;
    }

    public void setConcerneAssure(Boolean concerneAssure) {
        this.concerneAssure = concerneAssure;
    }

    public void setConvOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setCopieDecisionArray(ArrayList<RFCopieDecisionsValidationData> copieDecisionArray) {
        CopieDecisionArray = copieDecisionArray;
    }

    public void setCsEtatsDecision(String csEtatsDecision) {
        this.csEtatsDecision = csEtatsDecision;
    }

    public void setCsEtatsDecisionData(Vector<String[]> csEtatsDecisionData) {
        this.csEtatsDecisionData = csEtatsDecisionData;
    }

    public void setDescAdressePaiement(String descAdressePaiement) {
        this.descAdressePaiement = descAdressePaiement;
    }

    public void setDescAssure(String descAssure) {
        this.descAssure = descAssure;
    }

    public void setDescFournisseur(String descFournisseur) {
        this.descFournisseur = descFournisseur;
    }

    public void setDetailAssureGroupBy(String detailAssureGroupBy) {
        this.detailAssureGroupBy = detailAssureGroupBy;
    }

    public void setDetailConjoint(String detailConjoint) {
        this.detailConjoint = detailConjoint;
    }

    public void setForAnneeQD(String forAnneeQD) {
        this.forAnneeQD = forAnneeQD;
    }

    public void setForCsEtatDecision(String forCsEtatDecision) {
        this.forCsEtatDecision = forCsEtatDecision;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDecideFrom(String forDecideFrom) {
        this.forDecideFrom = forDecideFrom;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForNumeroDecision(String forNumeroDecision) {
        this.forNumeroDecision = forNumeroDecision;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setForPrepareFrom(String forPrepareFrom) {
        this.forPrepareFrom = forPrepareFrom;
    }

    public void setForPreparePar(String forPreparePar) {
        this.forPreparePar = forPreparePar;
    }

    public void setForValideFrom(String forValideFrom) {
        this.forValideFrom = forValideFrom;
    }

    public void setForValidePar(String forValidePar) {
        this.forValidePar = forValidePar;
    }

    public void setFournisseurTypeArray(ArrayList<String> fournisseurTypeArray) {
        FournisseurTypeArray = fournisseurTypeArray;
    }

    @Override
    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    // ~ Accessors and mutators
    // ------------------------------------------------------------------------------------------------

    public void setIdCopie(String idCopie) {
        this.idCopie = idCopie;
    }

    public void setIdDestinataire(String idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdSuppressionAssureArray(ArrayList<String> idSuppressionAssureArray) {
        this.idSuppressionAssureArray = idSuppressionAssureArray;
    }

    public void setIdSuppressionFournisseurArray(ArrayList<String> idSuppressionFournisseurArray) {
        this.idSuppressionFournisseurArray = idSuppressionFournisseurArray;
    }

    /**
     * setter pour l'attribut id tiers agent execution depuis pyxis.
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersFournisseurDepuisPyxis(String idTiers) {
        setIdDestinataire(idTiers);
        retourDepuisPyxis = true;
    }

    public void setJsonCopie(String jsonCopie) {
        this.jsonCopie = jsonCopie;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    public void setMontantAPayerPlusExcedentDeRecettePlusForcerPaiement(
            String montantAPayerPlusExcedentDeRecettePlusForcerPaiement) {
        this.montantAPayerPlusExcedentDeRecettePlusForcerPaiement = montantAPayerPlusExcedentDeRecettePlusForcerPaiement;
    }

    public void setMontantTotalARembourser(String montantTotalARembourser) {
        this.montantTotalARembourser = montantTotalARembourser;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    public void setRetourPyxisFalse() {
        retourDepuisPyxis = false;
    }

    // @Override
    // public void setTexteAnnexe(String texteAnnexe) {
    // this.texteAnnexe = texteAnnexe;
    // }

    public void setRetourPyxisTrue() {
        retourDepuisPyxis = true;
    }

    public void setTrouveDansCI(boolean trouveDansCI) {
        this.trouveDansCI = trouveDansCI;
    }

    public void setTrouveDansTiers(boolean trouveDansTiers) {
        this.trouveDansTiers = trouveDansTiers;
    }

}

/*
 * créé le 01 octobre 2010
 */
package globaz.cygnus.vb.conventions;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.conventions.IRFConventions;
import globaz.cygnus.db.conventions.RFConvention;
import globaz.cygnus.db.conventions.RFMontantsConvention;
import globaz.cygnus.utils.RFSoinsListsBuilder;
import globaz.cygnus.utils.RFUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * author fha
 */
public class RFConventionViewBean extends BEntity implements FWViewBeanInterface {

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
        return RFConventionViewBean.METHODES_SEL_FOURNISSEUR;
    }

    private String adresse = "";
    private String adressePaiementNom = "";
    // ~ Liste des assurés
    private ArrayList AssureArray = new ArrayList();
    private String codeSousTypeDeSoin = "";
    private String codeSousTypeDeSoinList = "";

    private String codeTypeDeSoin = "";
    private String codeTypeDeSoinList = "";

    private Boolean concerneAssure = Boolean.FALSE;
    private transient Vector<String[]> convOrderBy = null;
    private String csCanton = "";
    private String csGenrePCAccordee = "";
    private String csNationalite = "";
    private String csPeriodicite = "";
    private transient Vector<String[]> csPeriodiciteData = null;
    private String csSexe = "";

    private String csTypeBeneficiaire = "";
    private transient Vector<String[]> csTypeBeneficiairePcData = null;

    private String csTypePCAccordee = "";

    private String dateCreation = "";
    private String dateDebut = "";
    private String dateDeces = "";
    private String dateFin = "";

    private String dateNaissance = "";
    private String descAdressePaiement = "";

    private String descAssure = "";
    private String descFournisseur = "";
    private transient Boolean enCours = Boolean.FALSE;
    private transient Boolean forActif = Boolean.FALSE;
    private Boolean forConcerneAssure = Boolean.FALSE;
    private transient String forDateCreation = "";
    private String forDateDebut = "";
    private String forDateFin = "";
    private transient String forFournisseur = "";

    private transient String forLibelle = "";
    private String forMontantAssure = "";

    private transient Boolean forParConvention = Boolean.TRUE;
    // ~ Liste des objets fournisseurs-type de soin
    private ArrayList FournisseurTypeArray = new ArrayList();

    private boolean fromAssure = false;
    private transient String fromDate = "";

    private Vector<String[]> genresPCAccordeeData = null;
    private String idAdressePaiement = "";
    private String idAssure = "";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    private String idConvention = "";

    private String idConvFouSts = "";
    private String idFournisseur = "";

    // gestion d'un client (NSS)
    private String idGestionnaire = "";

    private String idMontant = "";

    private String idSousTypeDeSoin = "";
    // ~ Liste des ìd assurés à supprimer
    private ArrayList idSuppressionAssureArray = new ArrayList();
    // ~ Liste des ìd fournisseurs-type de soin à supprimer
    private ArrayList idSuppressionFournisseurArray = new ArrayList();
    private String idTypeDeSoin = "";
    private Boolean isActif = Boolean.FALSE;
    private Boolean isAjout = Boolean.FALSE;
    private Boolean isUpdate = Boolean.FALSE;
    private String libelle = "";
    private String mntMaxAvecApiFaible = "";
    private String mntMaxAvecApiGrave = "";
    private String mntMaxAvecApiMoyen = "";
    private String mntMaxDefaut = "";
    private String mntMaxSansApi = "";
    private boolean modifie = false;
    private String nom = "";
    private String nomEcranCourant = IRFConventions.ECRAN_CONVENTION;
    private String nomTiers = "";
    private String Nss = "";
    private String nssTiers = "";
    private transient Vector<String[]> orderBy = null;
    private Boolean plafonne = Boolean.TRUE;
    private String prenom = "";
    private String prenomTiers = "";
    private boolean retourDepuisPyxis = false;
    private transient String sousTypeDeSoinParTypeInnerJavascript = "";

    private transient String triePar = "";
    private boolean trouveDansCI = false;

    private boolean trouveDansTiers = false;
    private transient Vector<String[]> typeDeSoinsDemande = null;

    private Vector<String[]> typesPcData = null;

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAdresse() throws Exception {
        if (!JadeStringUtil.isBlank(getIdAssure())) {
            PRTiersWrapper prTiersWrapper = PRTiersHelper.getPersonneAVS((getISession()), getIdAssure());// on
            // a
            // enlevé
            // le
            // getDemande

            return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA);
        } else {
            return adresse;
        }
    }

    public String getAdressePaiementNom() {
        return adressePaiementNom;
    }

    public ArrayList getAssureArray() {
        return AssureArray;
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

    public Boolean getConcerneAssure() {
        return concerneAssure;
    }

    public Vector<String[]> getConvOrderBy() {
        return orderBy;
    }

    // pour la recherche de convention
    public Vector<String[]> getConvOrderByData() {
        if (convOrderBy == null) {
            convOrderBy = new Vector<String[]>(3);
            convOrderBy.add(new String[] { "", "" });
            convOrderBy.add(new String[] { RFConvention.FIELDNAME_TEXT_LIBELLE,
                    getSession().getLabel("JSP_RF_SAISIE_CONV_LIBELLE") });
        }
        return convOrderBy;
    }

    // ~ methods
    // ------------------------------------------------------------------------------------------------------
    @Override
    public BSpy getCreationSpy() {

        if (IRFConventions.ECRAN_CONVENTION.equals(nomEcranCourant)) {
            RFConvention convention = new RFConvention();

            try {
                convention = RFConvention.loadConvention(getSession(), getSession().getCurrentThreadTransaction(),
                        getIdConvention());
            } catch (Exception e) {
            }
            return convention.getCreationSpy();
        } else {
            if (IRFConventions.ECRAN_MONTANT_CONVENTION.equals(nomEcranCourant)) {
                RFMontantsConvention montantConvention = new RFMontantsConvention();

                try {
                    montantConvention = RFMontantsConvention.loadMontantConvention(getSession(), getSession()
                            .getCurrentThreadTransaction(), getIdMontant());
                } catch (Exception e) {
                }
                return montantConvention.getCreationSpy();
            } else {
                // nouveauMontantConvention
                return null;
            }
        }
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsGenrePCAccordee() {
        return csGenrePCAccordee;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsPeriodicite() {
        return csPeriodicite;
    }

    public Vector<String[]> getCsPeriodiciteData() {
        if (csPeriodiciteData == null) {
            csPeriodiciteData = PRCodeSystem.getLibellesPourGroupe(IRFConventions.CS_GROUPE_PERIODICITE, getSession());
            csPeriodiciteData.add(0, new String[] { "", "" });
        }
        return csPeriodiciteData;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeBeneficiaire() {
        return csTypeBeneficiaire;
    }

    public Vector<String[]> getCsTypeBeneficiairePCData() {

        csTypeBeneficiairePcData = PRCodeSystem.getLibellesPourGroupe(
                IRFTypesBeneficiairePc.CS_GROUPE_TYPE_BENEFICIAIRE_PC, getSession());

        // ajout des options custom
        csTypeBeneficiairePcData.add(0, new String[] { "", "" });
        csTypeBeneficiairePcData.add(
                1,
                new String[] { IRFTypesBeneficiairePc.POUR_TOUS,
                        getSession().getLabel("JSP_RF_SAISIE_CONVENTION_MONTANT_POUR_TOUS_LISTE") });

        return csTypeBeneficiairePcData;
    }

    public String getCsTypePCAccordee() {
        return csTypePCAccordee;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDescAdressePaiement() {

        String idTiers = "";
        if (!JadeStringUtil.isBlankOrZero(getIdAdressePaiement())) {
            idTiers = getIdAdressePaiement();
        } else if (!JadeStringUtil.isBlankOrZero(getIdFournisseur())) {
            idTiers = getIdFournisseur();
        }

        try {
            if (!JadeStringUtil.isBlankOrZero(idTiers)
                    && (JadeStringUtil.isBlank(descAdressePaiement) || isRetourDepuisPyxis())) {

                String adrPaiementNom = "";
                descAdressePaiement = "";

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(getISession(), idTiers);
                if (tw == null) {
                    tw = PRTiersHelper.getAdministrationParId(getISession(), idTiers);
                }

                if (null != tw) {

                    adrPaiementNom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

                    // recherche de l'adresse de paiement
                    TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData((BSession) getISession(),
                            ((BSession) getISession()).getCurrentThreadTransaction(), idTiers,
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());// TODO:
                                                                                                                        // c'est
                                                                                                                        // pas
                                                                                                                        // la

                    if ((adresse != null) && !adresse.isNew()) {
                        TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                        source.load(adresse);

                        ITIAdresseFormater tiAdrPaiBanFor;

                        // formatter le no de ccp ou le no bancaire
                        if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                            tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                        } else {
                            tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                        }

                        if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                            descAdressePaiement = adrPaiementNom + "<br/>"
                                    + JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                        } else {
                            descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                        }

                        return descAdressePaiement;
                    } else {
                        return "";
                    }
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(this, "getDescAdressePaiement()", "RFConventionViewBean");
                    return "";
                }
            } else {
                return descAdressePaiement;
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(this, e.getMessage());
            return "";
        }
    }

    // adresse de paiement depuis un tiers
    public String getDescAdressePaiement(String idTiers) {

        try {
            String adrPaiementNom = "";
            descAdressePaiement = "";

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getISession(), idTiers);
            if (tw == null) {
                tw = PRTiersHelper.getAdministrationParId(getISession(), idTiers);
            }

            if (null != tw) {

                adrPaiementNom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

                // recherche de l'adresse de paiement
                TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData((BSession) getISession(),
                        ((BSession) getISession()).getCurrentThreadTransaction(), idTiers,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

                if ((adresse != null) && !adresse.isNew()) {
                    TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                    source.load(adresse);

                    ITIAdresseFormater tiAdrPaiBanFor;

                    // formatter le no de ccp ou le no bancaire
                    if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                        tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                    } else {
                        tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                    }

                    if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                        descAdressePaiement = adrPaiementNom + "<br/>"
                                + JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                    } else {
                        descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                    }

                    return descAdressePaiement;
                } else {
                    return "";
                }
            } else {
                RFUtils.setMsgErreurInattendueViewBean(this, "getDescAdressePaiement()", "RFConventionViewBean");
                return "";
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(this, e.getMessage());
            return "";
        }
    }

    public String getDescAdressePaiementFromFournisseur() {
        try {
            if (!JadeStringUtil.isBlankOrZero(getIdFournisseur())
                    && (JadeStringUtil.isBlank(descAdressePaiement) || isRetourDepuisPyxis())) {

                String adrPaiementNom = "";
                descAdressePaiement = "";

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(getISession(), getIdFournisseur());

                if (null != tw) {

                    adrPaiementNom = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

                    // recherche de l'adresse de paiement
                    TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData((BSession) getISession(),
                            ((BSession) getISession()).getCurrentThreadTransaction(), getIdFournisseur(),
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

                    if ((adresse != null) && !adresse.isNew()) {
                        TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
                        source.load(adresse);

                        ITIAdresseFormater tiAdrPaiBanFor;

                        // formatter le no de ccp ou le no bancaire
                        if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                            tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
                        } else {
                            tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
                        }

                        if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                            descAdressePaiement = adrPaiementNom + "<br/>"
                                    + JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                        } else {
                            descAdressePaiement = JadeStringUtil.change(tiAdrPaiBanFor.format(source), "\n", "<br/>");
                        }

                        return descAdressePaiement;
                    } else {
                        return "";
                    }
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(this, "getDescAdressePaiement()", "RFConventionViewBean");
                    return "";
                }
            } else {
                return descAdressePaiement;
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(this, e.getMessage());
            return "";
        }
    }

    public String getDescAssure() {

        if (!JadeStringUtil.isBlank(getIdAssure()) && JadeStringUtil.isBlank(descAssure)) {
            // Recherche du nom du fournisseur
            try {

                PRTiersWrapper prTiersWrapper = PRTiersHelper.getTiersParId((getISession()), getIdAssure());

                if (null == prTiersWrapper) {
                    prTiersWrapper = PRTiersHelper.getAdministrationParId((getISession()), getIdAssure());
                }
                /*
                 * on set aussi le nom, le prénom et l'adresse
                 */
                return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            return descAssure;
        }

        return "";

    }

    public String getDescFournisseur() {
        // si l'adresse de paiement est vide il ne faut pas la réinitialiser
        this.getDescAdressePaiement();
        if (!JadeStringUtil.isBlank(getIdFournisseur()) && JadeStringUtil.isBlank(descFournisseur)) {
            // Recherche du nom du fournisseur
            try {

                PRTiersWrapper prTiersWrapper = PRTiersHelper.getTiersParId((getISession()), getIdFournisseur());// on a
                                                                                                                 // enlevé
                                                                                                                 // le
                                                                                                                 // getDemande

                if (null == prTiersWrapper) {
                    prTiersWrapper = PRTiersHelper.getAdministrationParId((getISession()), getIdFournisseur());
                }

                return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        } else {
            return descFournisseur;
        }

        return "";

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

        if (JadeStringUtil.isEmpty(getIdGestionnaire())) {
            return "";
        } else {
            JadeUser userName = ((BSession) getISession()).getApplication()._getSecurityManager()
                    .getUserForVisa(((BSession) getISession()), getIdGestionnaire());
            return userName.getIdUser() + " - " + userName.getFirstname() + " " + userName.getLastname();
        }
    }

    public Boolean getEnCours() {
        return enCours;
    }

    public Boolean getForActif() {
        return forActif;
    }

    public Boolean getForConcerneAssure() {
        return forConcerneAssure;
    }

    public String getForDateCreation() {
        return forDateCreation;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForFournisseur() {
        return forFournisseur;
    }

    public String getForLibelle() {
        return forLibelle;
    }

    public String getForMontantAssure() {
        return forMontantAssure;
    }

    public Boolean getForParConvention() {
        return forParConvention;
    }

    public ArrayList getFournisseurTypeArray() {
        return FournisseurTypeArray;
    }

    public String getFromDate() {
        return fromDate;
    }

    public Vector<String[]> getGenresPCAccordeeData() {

        if (genresPCAccordeeData == null) {
            genresPCAccordeeData = PRCodeSystem.getLibellesPourGroupe(IPCPCAccordee.GROUPE_GENRE_PC, getSession());
            genresPCAccordeeData.add(0, new String[] { "", "" });
            genresPCAccordeeData.add(1,
                    new String[] { IRFConventions.CS_GENRE_PC_TOUS, getSession().getLabel("JSP_RF_CONV_TOUS") });
        }

        return genresPCAccordeeData;
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

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdAssure() {
        return idAssure;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getIdConvFouSts() {
        return idConvFouSts;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdMontant() {
        return idMontant;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public ArrayList getIdSuppressionAssureArray() {
        return idSuppressionAssureArray;
    }

    public ArrayList getIdSuppressionFournisseurArray() {
        return idSuppressionFournisseurArray;
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

    public Boolean getIsActif() {
        return isActif;
    }

    public Boolean getIsAjout() {
        return isAjout;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
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
     * getter pour l'attribut methodes selecteur agent execution.
     * 
     * @return la valeur courante de l'attribut methodes selecteur agent execution
     */
    public Object[] getMethodesSelecteurAdressePaiement() {
        return RFConventionViewBean.METHODES_SEL_ADRESSE_PAIEMENT;
    }

    /**
     * getter pour l'attribut methodes selection d'un assuré
     * 
     * @return la valeur courante de l'attribut methodes selection assuré
     */
    public Object[] getMethodesSelecteurAssure() {
        return RFConventionViewBean.METHODES_SEL_ASSURE;
    }

    /**
     * getter pour l'attribut methodes selecteur agent execution.
     * 
     * @return la valeur courante de l'attribut methodes selecteur agent execution
     */
    public Object[] getMethodesSelecteurFournisseur() {
        return RFConventionViewBean.METHODES_SEL_FOURNISSEUR;
    }

    public String getMntMaxAvecApiFaible() {
        return mntMaxAvecApiFaible;
    }

    // ~ Accessors and mutators
    // ------------------------------------------------------------------------------------------------

    public String getMntMaxAvecApiGrave() {
        return mntMaxAvecApiGrave;
    }

    public String getMntMaxAvecApiMoyen() {
        return mntMaxAvecApiMoyen;
    }

    public String getMntMaxDefaut() {
        return mntMaxDefaut;
    }

    public String getMntMaxSansApi() {
        return mntMaxSansApi;
    }

    public String getNom() {
        return nom;
    }

    public String getNomEcranCourant() {
        return nomEcranCourant;
    }

    public String getNomTiers() throws Exception {
        if (!JadeStringUtil.isBlank(getIdAssure())) {
            PRTiersWrapper prTiersWrapper = PRTiersHelper.getPersonneAVS((getISession()), getIdAssure());

            return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
        } else {
            return nomTiers;
        }
    }

    public String getNss() {
        return Nss;
    }

    public String getNssTiers() throws Exception {
        if (!JadeStringUtil.isBlank(getIdAssure())) {
            PRTiersWrapper prTiersWrapper = PRTiersHelper.getPersonneAVS((getISession()), getIdAssure());

            return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        } else {
            return idAssure;
        }
    }

    public Vector<String[]> getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector<String[]>(3);
            orderBy.add(new String[] { "", "" });
            orderBy.add(new String[] { RFMontantsConvention.FIELDNAME_TYPE_BENEFICIAIRE,
                    getSession().getLabel("JSP_RF_SAISIE_MNT_CONV_BENEFICIAIRE") });
            orderBy.add(new String[] { RFMontantsConvention.FIELDNAME_DATE_DEBUT,
                    getSession().getLabel("JSP_RF_SAISIE_MNT_CONV_PERIODE") });
        }

        return orderBy;
    }

    public Boolean getPlafonne() {
        return plafonne;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPrenomTiers() throws Exception {
        if (!JadeStringUtil.isBlank(getIdAssure())) {
            PRTiersWrapper prTiersWrapper = PRTiersHelper.getPersonneAVS((getISession()), getIdAssure());

            return prTiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
        } else {
            return prenomTiers;
        }
    }

    /**
     * Méthode qui retourne un tableau javascript de sous type de soins à 2 dimension (code,CSlibelle)
     * 
     * @return String
     */
    public String getSousTypeDeSoinParTypeInnerJavascript() {

        if (JadeStringUtil.isBlank(sousTypeDeSoinParTypeInnerJavascript)) {
            try {
                sousTypeDeSoinParTypeInnerJavascript = RFSoinsListsBuilder.getInstance(((BSession) getISession()))
                        .getSousTypeDeSoinParTypeInnerJavascript();
            } catch (Exception e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return sousTypeDeSoinParTypeInnerJavascript;
    }

    @Override
    public BSpy getSpy() {

        if (IRFConventions.ECRAN_CONVENTION.equals(nomEcranCourant)) {
            RFConvention convention = new RFConvention();

            try {
                convention = RFConvention.loadConvention(getSession(), getSession().getCurrentThreadTransaction(),
                        getIdConvention());
            } catch (Exception e) {
            }
            return convention.getSpy();
        } else {
            if (IRFConventions.ECRAN_MONTANT_CONVENTION.equals(nomEcranCourant)) {
                RFMontantsConvention montantConvention = new RFMontantsConvention();

                try {
                    montantConvention = RFMontantsConvention.loadMontantConvention(getSession(), getSession()
                            .getCurrentThreadTransaction(), getIdMontant());
                } catch (Exception e) {
                }
                return montantConvention.getSpy();
            } else {
                // nouveauMontantConvention
                return null;
            }
        }
    }

    public String getTriePar() {
        return triePar;
    }

    public Vector<String[]> getTypeDeSoinData() {
        try {
            if (typeDeSoinsDemande == null) {
                typeDeSoinsDemande = RFSoinsListsBuilder.getInstance(((BSession) getISession()))
                        .getTypeDeSoinsDemande();
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

    public Vector<String[]> getTypesPcData() {

        if (typesPcData == null) {
            typesPcData = PRCodeSystem.getLibellesPourGroupe(IPCPCAccordee.GROUPE_TYPE_PC, getSession());
            typesPcData.add(0, new String[] { "", "" });
            typesPcData.add(1,
                    new String[] { IRFConventions.CS_TYPE_PC_TOUS, getSession().getLabel("JSP_RF_CONV_TOUS") });
        }

        return typesPcData;
    }

    public boolean isFromAssure() {
        return fromAssure;
    }

    public boolean isModifie() {
        return modifie;
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

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setAdressePaiementNom(String adressePaiementNom) {
        this.adressePaiementNom = adressePaiementNom;
    }

    public void setAssureArray(ArrayList assureArray) {
        AssureArray = assureArray;
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

    public void setConcerneAssure(Boolean concerneAssure) {
        this.concerneAssure = concerneAssure;
    }

    public void setConvOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsGenrePCAccordee(String csGenrePCAccordee) {
        this.csGenrePCAccordee = csGenrePCAccordee;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsPeriodicite(String csPeriodicite) {
        this.csPeriodicite = csPeriodicite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypeBeneficiaire(String csTypeBeneficiaire) {
        this.csTypeBeneficiaire = csTypeBeneficiaire;
    }

    public void setCsTypeBeneficiairePcData(Vector csTypeBeneficiairePcData) {
        this.csTypeBeneficiairePcData = csTypeBeneficiairePcData;
    }

    public void setCsTypePCAccordee(String csTypePCAccordee) {
        this.csTypePCAccordee = csTypePCAccordee;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
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

    public void setEnCours(Boolean enCours) {
        this.enCours = enCours;
    }

    public void setForActif(Boolean forActif) {
        this.forActif = forActif;
    }

    public void setForConcerneAssure(Boolean forConcerneAssure) {
        this.forConcerneAssure = forConcerneAssure;
    }

    public void setForDateCreation(String forDateCreation) {
        this.forDateCreation = forDateCreation;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForFournisseur(String forFournisseur) {
        this.forFournisseur = forFournisseur;
    }

    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

    public void setForMontantAssure(String forMontantAssure) {
        this.forMontantAssure = forMontantAssure;
    }

    public void setForParConvention(Boolean forParConvention) {
        this.forParConvention = forParConvention;
    }

    public void setFournisseurTypeArray(ArrayList fournisseurTypeArray) {
        FournisseurTypeArray = fournisseurTypeArray;
    }

    public void setFromAssure(boolean fromAssure) {
        this.fromAssure = fromAssure;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setIdConvFouSts(String idConvFouSts) {
        this.idConvFouSts = idConvFouSts;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdMontant(String idMontant) {
        this.idMontant = idMontant;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIdSuppressionAssureArray(ArrayList idSuppressionAssureArray) {
        this.idSuppressionAssureArray = idSuppressionAssureArray;
    }

    public void setIdSuppressionFournisseurArray(ArrayList idSuppressionFournisseurArray) {
        this.idSuppressionFournisseurArray = idSuppressionFournisseurArray;
    }

    public void setIdTiersAdressePaiementDepuisPyxis(String idTiers) {
        setFromAssure(false);
        setIdAdressePaiement(idTiers);
        retourDepuisPyxis = true;
    }

    public void setIdTiersAssureDepuisPyxis(String idTiers) {
        setFromAssure(true);
        setIdAssure(idTiers);
        setConcerneAssure(concerneAssure);
        retourDepuisPyxis = true;
    }

    /**
     * setter pour l'attribut id tiers agent execution depuis pyxis.
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersFournisseurDepuisPyxis(String idTiers) {
        setFromAssure(false);
        setIdFournisseur(idTiers);
        setIdAdressePaiement(idTiers);
        retourDepuisPyxis = true;
    }

    public void setIdTypeDeSoin(String idTypeDeSoin) {
        this.idTypeDeSoin = idTypeDeSoin;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    public void setIsAjout(Boolean isAjout) {
        this.isAjout = isAjout;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMntMaxAvecApiFaible(String mntMaxAvecApiFaible) {
        this.mntMaxAvecApiFaible = mntMaxAvecApiFaible;
    }

    public void setMntMaxAvecApiGrave(String mntMaxAvecApiGrave) {
        this.mntMaxAvecApiGrave = mntMaxAvecApiGrave;
    }

    public void setMntMaxAvecApiMoyen(String mntMaxAvecApiMoyen) {
        this.mntMaxAvecApiMoyen = mntMaxAvecApiMoyen;
    }

    public void setMntMaxDefaut(String mntMaxDefaut) {
        this.mntMaxDefaut = mntMaxDefaut;
    }

    public void setMntMaxSansApi(String mntMaxSansApi) {
        this.mntMaxSansApi = mntMaxSansApi;
    }

    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomEcranCourant(String nomEcranCourant) {
        this.nomEcranCourant = nomEcranCourant;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNss(String nss) {
        Nss = nss;
    }

    public void setNssTiers(String nssTiers) {
        this.nssTiers = nssTiers;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

    public void setPlafonne(Boolean plafonne) {
        this.plafonne = plafonne;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public void setSousTypeDeSoinParTypeInnerJavascript(String sousTypeDeSoinParTypeInnerJavascript) {
        this.sousTypeDeSoinParTypeInnerJavascript = sousTypeDeSoinParTypeInnerJavascript;
    }

    public void setTriePar(String triePar) {
        this.triePar = triePar;
    }

    public void setTrouveDansCI(boolean trouveDansCI) {
        this.trouveDansCI = trouveDansCI;
    }

    public void setTrouveDansTiers(boolean trouveDansTiers) {
        this.trouveDansTiers = trouveDansTiers;
    }

    public void setTypeDeSoinsDemande(Vector<String[]> typeDeSoinsDemande) {
        this.typeDeSoinsDemande = typeDeSoinsDemande;
    }

    public String videEtAfficheAdressePaiement() {
        setIdAdressePaiement("");
        return "";
    }

    public String videEtAfficheAssure() {
        setIdAssure("");
        return "";
    }

    public String videEtAfficheFournisseur() {
        setIdFournisseur("");
        setDescFournisseur("");
        return "";
    }

}

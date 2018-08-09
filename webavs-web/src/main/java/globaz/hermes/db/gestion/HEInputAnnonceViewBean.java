package globaz.hermes.db.gestion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.application.HEProperties;
import globaz.hermes.db.access.HECheckRights;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean;
import globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationListViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationViewBean;
import globaz.hermes.db.parametrage.HELienChampAnnonceListViewBean;
import globaz.hermes.db.parametrage.HELienChampAnnonceViewBean;
import globaz.hermes.db.parametrage.HELienannonceListViewBean;
import globaz.hermes.db.parametrage.HELienannonceViewBean;
import globaz.hermes.db.parametrage.HEMotifcodeapplication;
import globaz.hermes.db.parametrage.HEMotifsListViewBean;
import globaz.hermes.db.parametrage.HEMotifsViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonce;
import globaz.hermes.db.parametrage.HEParametrageannonceManager;
import globaz.hermes.handler.HELotHandler;
import globaz.hermes.utils.AVSUtils;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HEConfigurationServiceUtils;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CIAnnonceCIAdditionnel;
import globaz.pavo.db.compte.CIAnnonceSuspens;
import globaz.pavo.util.CIAffilieManager;

/**
 * @author: ado
 */
public class HEInputAnnonceViewBean extends HEAnnoncesViewBean {
    private static final long serialVersionUID = 4856816416117929599L;
    public static String MSG_DELETE_OK = "DELETEOK";
    public static String MSG_INSERT_OK = "INSERTOK";
    public static String MSG_UPDATE_OK = "UPDATEOK";

    private String actionMessage;

    private String adresseAssure = "";
    private String adresseRentier = "";
    private String categorie = "";
    private String idDernierAjout;
    // le critere dans la page
    private String inputCritere = "";
    // le motif choisit dans la page
    private String inputMotif = "";
    private boolean isCheckRightActif = true;
    /***/
    private String langueCorrespondance = "";
    // /////// Connectique MICOM
    private String paramNumeroAvs = "";

    private String paramReferenceExterne = "";
    private String titreAssure = "";
    private String titreRentier = "";
    private String formulePolitesse = "";
    // tout les paramétrages annonce différents
    private Vector toutParams = new Vector();
    protected String wantCheckNumAffilie = IHEAnnoncesViewBean.WANT_CHECK_NUM_AFF_TRUE;

    private boolean wantCheckUnique = true;
    private String warningEmployeurSansPersoOrAccountZero = "";

    private Boolean chkCreerArc61 = false;

    public HEInputAnnonceViewBean() {
        super();
    }

    /**
     * Constructor HEInputAnnonceViewBean.
     *
     * @param bSession
     */
    public HEInputAnnonceViewBean(BSession bSession) {
        this();
        setSession(bSession);
    }

    /**
     * Effectue des traitements après un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après l'ajout de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterAdd(BTransaction transaction)</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws java.lang.Exception {
        //
        /* s'il faut des retours : */
        // ajout dans attenteRetourAnnonce
        // - de l'idAnnonce
        // - de l'idAttendu du parametrage
        // - de la référence unique
        //
        HEAttenteRetourViewBean attenteRetour = new HEAttenteRetourViewBean();
        attenteRetour.setNnss(getNnss());

        attenteRetour = ajoutRetours(getSession(), transaction);

        if ((lienAnnonceListe.size() >= 1) && !getTypeLot().equals(HELotViewBean.CS_TYPE_ENVOI)
                && StringUtils.unPad(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT)).equals("1")) {
            lierRetours(getSession(), transaction);
        }
        // //////////// CAS PARTICULIER POUR LES 25, on attend des 38-39
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("25")
                && (!this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01"))) {
            ajoutRetours25(getSession(), transaction, attenteRetour);
        }
        // je regarde si on déclenche la création automatique de la suite...
        // setToutParams(champsTable.getParametrageAnnonce());
        if (toutParams.size() != 0) {
            ajoutEnregistrements(getSession(), transaction);
        }

        // BZ 5522: exclure le traitement des ordres si uniquement PRESTATIONS (CCB)
        if (!"true".equals(
                HEProperties.getParameter(HEProperties.PROP_CODE_EXCL_TRAITEMENT_ORDRE, getSession(), transaction))) {
            // PUSH PAVO si enregistrement 01 annonce 21, 22, 23, 29
            if ((this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("21")
                    || this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("22")
                    || this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("23")
                    || this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("29"))
                    && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {

                pushAnnonceCI(getSession(), transaction);
            }
        }
        // si c'est une 39
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("39")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("001")
                && JAUtil.isIntegerEmpty(this.getField(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI))) {
            HEAttenteRetourListViewBean retours38 = new HEAttenteRetourListViewBean(getSession());
            retours38.setForIdAnnonceRetourAttendue("8");
            retours38.setForNumeroAVS(getNumeroAVS());
            retours38.setForNumeroCaisse(getNumeroCaisse());
            retours38.setForMotif(getMotif());
            retours38.find(transaction);
            for (int i = 0; i < retours38.size(); i++) {
                ((HEAttenteRetourViewBean) retours38.getEntity(i)).delete(transaction);
            }
        }

        // rajoute les infos complémentaires
        if ("11".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {

            if ((HEAnnoncesViewBean.isMotifCA(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                    || (HEAnnoncesViewBean.isMotifForDeclSalaire(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)))
                    || globaz.hermes.utils.HEUtil.isMotifCert(getSession(),
                            this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)))
                    && "true".equals(getSession().getApplication().getProperty("affilie.input"))) {
                // Inforom 444
                if (!JadeStringUtil.isEmpty(getLangueCorrespondance())) {
                    HEInfos infoslan = new HEInfos();
                    infoslan = new HEInfos();
                    infoslan.setIdArc(getIdAnnonce());
                    infoslan.setTypeInfo(HEInfos.CS_LANGUE_CORRESPONDANCE);
                    infoslan.setLibInfo(getLangueCorrespondance());
                    infoslan.add(transaction);
                }
                if (HEUtil.isNNSSActif(getSession())) {
                    // ajout de la categorie
                    HEInfos infoscat = new HEInfos();
                    infoscat.setSession(getSession());
                    infoscat.setIdArc(getIdAnnonce());
                    infoscat.setTypeInfo(HEInfos.CS_CATEGORIE);
                    infoscat.setLibInfo(getCategorie());
                    infoscat.add(transaction);
                    if (!IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER.equals(getCategorie())) {
                        HEInfos infos = new HEInfos();
                        infos.setSession(getSession());
                        infos.setIdArc(getIdAnnonce());
                        infos.setTypeInfo(HEInfos.CS_NUMERO_AFFILIE);
                        infos.setLibInfo(getNumeroAffilie());
                        infos.add(transaction);
                        infos.setSession(getSession());
                        infos.setIdArc(getIdAnnonce());
                        infos.setTypeInfo(HEInfos.CS_NUMERO_SUCCURSALE);
                        infos.setLibInfo(getNumeroSuccursale());
                        infos.add(transaction);
                        infos.setSession(getSession());
                        infos.setIdArc(getIdAnnonce());
                        infos.setTypeInfo(HEInfos.CS_NUMERO_EMPLOYE);
                        infos.setLibInfo(getNumeroEmploye());
                        infos.add(transaction);
                    } else {
                        HEInfos infos = new HEInfos();
                        infos.setSession(getSession());
                        infos.setIdArc(getIdAnnonce());
                        infos.setTypeInfo(HEInfos.CS_ADRESSE_ASSURE);
                        infos.setLibInfo(getAdresseRentier());
                        infos.add(transaction);
                        infos = new HEInfos();
                        infos.setSession(getSession());
                        infos.setIdArc(getIdAnnonce());
                        infos.setTypeInfo(HEInfos.CS_TITRE_ASSURE);
                        infos.setLibInfo(getTitreRentier());
                        infos.add(transaction);

                    }
                } else {
                    // si le numéro n'est pas actif, on continue de saisir le
                    // numéro affilié normal
                    HEInfos infos = new HEInfos();
                    infos.setSession(getSession());
                    infos.setIdArc(getIdAnnonce());
                    infos.setTypeInfo(HEInfos.CS_NUMERO_AFFILIE);
                    infos.setLibInfo(getNumeroAffilie());
                    infos.add(transaction);
                    infos.setSession(getSession());
                    infos.setIdArc(getIdAnnonce());
                    infos.setTypeInfo(HEInfos.CS_NUMERO_SUCCURSALE);
                    infos.setLibInfo(getNumeroSuccursale());
                    infos.add(transaction);
                    infos.setSession(getSession());
                    infos.setIdArc(getIdAnnonce());
                    infos.setTypeInfo(HEInfos.CS_NUMERO_EMPLOYE);
                    infos.setLibInfo(getNumeroEmploye());
                    infos.add(transaction);
                }
            }

            if ((HEAnnoncesViewBean.isMotifForDeclSalaire(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                    || HEUtil.isMotifCert(getSession(), this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)))
                    && "true".equals(getSession().getApplication().getProperty("affilie.input"))) {
                if (!JadeStringUtil.isEmpty(getNumeroAffilie())) {
                    HEInfos infos = new HEInfos();
                    infos.setSession(getSession());
                    infos.setIdArc(getIdAnnonce());
                    infos.setTypeInfo(HEInfos.CS_DATE_ENGAGEMENT);
                    infos.setLibInfo(getDateEngagement());
                    infos.add(transaction);
                }
            }
            // ////////////
            if ("97".equals(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                    && "true".equals(getSession().getApplication().getProperty("adresse.input"))) {
                HEInfos infos = new HEInfos();
                // facultatif car session dans transaction
                // infos.setSession(getSession());
                infos.setIdArc(getIdAnnonce());
                infos.setTypeInfo(HEInfos.CS_ADRESSE_ASSURE);
                infos.setLibInfo(getAdresseAssure());
                infos.add(transaction);
                infos = new HEInfos();
                infos.setIdArc(getIdAnnonce());
                infos.setTypeInfo(HEInfos.CS_LANGUE_CORRESPONDANCE);
                infos.setLibInfo(getLangueCorrespondance());
                infos.add(transaction);
                infos = new HEInfos();
                infos.setIdArc(getIdAnnonce());
                infos.setTypeInfo(HEInfos.CS_TITRE_ASSURE);
                infos.setLibInfo(getTitreAssure());
                infos.add(transaction);
            }
        }

        // Gestion de la formule de politesse
        if (!JadeStringUtil.isBlank(getFormulePolitesse())) {
            HEInfos infos = new HEInfos();
            infos.setSession(getSession());
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_FORMULE_POLITESSE);
            infos.setLibInfo(getFormulePolitesse());
            infos.add(transaction);
        }

        // inforom 573 contrôler si c'est un employeur sans personnelle ou avec acompte AVS à zéro
        if (!HEUtil.checkAffSansPersoOrAccountAVSZero(getNumeroAffilie(), categorie, getSession())) {

            String AVSOrDesignation = HEUtil.getNumAVSOrDesignation(getNumeroAVS(),
                    this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF), getSession());

            setWarningEmployeurSansPersoOrAccountZero(
                    AVSOrDesignation + " : " + getSession().getLabel("HERMES_EMPLOYEUR_SANS_PERSONNEL"));
        }
        addARC61(transaction);
    }

    private void addARC61(BTransaction transaction) {
        try {
            if ("11".equals(getMotif()) && getChkCreerArc61() && !JadeStringUtil.isBlankOrZero(getNumeroAVS())) {
                setMotif("61");
                setChkCreerArc61(false);
                getInputTable().put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "61");
                this.add(transaction);
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    @Override
    protected void _alwaysAfterAdd(BTransaction transaction) throws java.lang.Exception {
        if (transaction.hasErrors()) {
            setIdLot("");
        }
    } // ald : méthode créé pour la pagination dans les pages web

    /**
     * Appellée avant l'ajout, permet de générer la PK
     *
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si l'incrémentation échoue
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception, HEInputAnnonceException {
        if (utilisateur.equals("")) {
            setUtilisateur(getSession().getUserName());
        }
        String codeApp = this.getField(IHEAnnoncesViewBean.CODE_APPLICATION);
        String codeEnr = this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT);
        String inputedRef = this.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);
        if (codeApp.equals("11") && codeEnr.equals("01")) {
            if ((inputedRef != null) && (inputedRef.trim().length() != 0)) {

                put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE, StringUtils
                        .transformDiphtAndAccent(
                                JadeStringUtil.stripBlanks(this.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE)))
                        .toUpperCase());
                if ("true".equals(getSession().getApplication().getProperty("service.input"))) {
                    // verification que le service dans la référence est pas
                    // vide
                    if (inputedRef.lastIndexOf("/") != -1) {
                        String service = inputedRef.substring(0, inputedRef.indexOf("/"));

                        HEConfigurationServiceUtils utils = new HEConfigurationServiceUtils();

                        if (!utils.isCorrespondanceServiceExistant(getSession(), service)) {
                            _addError(transaction, getSession().getLabel("HERMES_10060"));
                        }

                        String ref = inputedRef.substring(inputedRef.indexOf("/") + 1, inputedRef.length());
                        if ((service.trim().length() == 0) || (ref.trim().length() == 0)) {
                            _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                                    getSession().getCodeLibelle(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE)));
                        }
                    }
                }
            }
        }
        if (!codeApp.equals("39")) {
            // faire le contrôle seulement si ce n'est pas un code application
            // 39
            String name = this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF);
            if ((name != null) && (name.trim().length() != 0)) {
                if (!HEUtil.checkName(name)) {
                    put(IHEAnnoncesViewBean.ETAT_NOMINATIF,
                            StringUtils.fixName(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF)));
                }
            }
        }
        traiterChampNumAssure();
        // NNSS
        if ("true".equalsIgnoreCase(getNumeroAvsNNSS())) {
            setNnss(new Boolean("true"));
        }

        if (IHEAnnoncesViewBean.WANT_CHECK_NUM_AFF_TRUE.equals(getWantCheckNumAffilie())
                && HEUtil.isNNSSActif(getSession()) && !JadeStringUtil.isEmpty(getNumeroAffilie())) {

            CIAffilieManager mgr = new CIAffilieManager();
            mgr.setSession(getSession());
            mgr.setLikeAffilieNumero(getNumeroAffilie());
            // po 1618
            // mgr.setForActif(true);
            mgr.setForParitaire(false);
            mgr.changeManagerSize(1);
            if (IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR.equals(getCategorie())) {
                mgr.setForCategorie(CIAffilieManager.PARITAIRE);
            }
            if (IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT.equals(getCategorie())) {
                mgr.setForCategorie(CIAffilieManager.PERSONNEL);
            }
            mgr.find();
            if (mgr.size() == 0) {
                _addError(transaction, getSession().getLabel("HERMES_AFFILIE_CATEGORIE_FAUX"));
            }
        }

        //
        if (this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE, true).equals("")) {
            try {
                put(IHEAnnoncesViewBean.NUMERO_CAISSE, getSession().getApplication().getProperty("noCaisse"));
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
            }
        }
        if (this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE, true).equals("")) {
            try {
                put(IHEAnnoncesViewBean.NUMERO_AGENCE, getSession().getApplication().getProperty("noAgence"));
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
            }
        }
        // traitement des diphtongues et mise en maj de l'etat nominatif
        if (!codeApp.equals("39")) {
            try {
                if (!JadeStringUtil.isBlank(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF))) {
                    put(IHEAnnoncesViewBean.ETAT_NOMINATIF, StringUtils
                            .transformDiphtongues(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF)).toUpperCase());
                }
            } catch (Exception e) {
                _addError(transaction, e.getMessage());
            }
        }
        // La date du jour
        if (getDateAnnonce().trim().equals("")) {
            setDateAnnonce(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYYYY));
        }
        if (JadeStringUtil.isBlank(getIdProgramme())) {
            setIdProgramme(getSession().getApplicationId());
        }
        // je set l'id du lot
        setIdLot(HELotHandler.getLotId(getIdLot(), getTypeLot(), getPrioriteLot(), getSession(), getTypeLot(),
                getDateAnnonce(), JadeStringUtil.isBlank(getIdProgramme()) ? getUtilisateur() : getIdProgramme(),
                transaction));
        // je set l'id de l'annonce
        setIdAnnonce(this._incCounter(transaction, "0"));
        // //////////////////////////////////////////////////////////////////////////////////////////////
        // je mets la référence unique
        if (JadeStringUtil.isBlank(getRefUnique()) || (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01"))) {
            setRefUnique(getIdAnnonce());
        }
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {
            if (getIdAnnonce().length() > 6) {
                put(IHEAnnoncesViewBean.NUMERO_ANNONCE,
                        getIdAnnonce().substring(getIdAnnonce().length() - 6, getIdAnnonce().length()));
            } else {
                put(IHEAnnoncesViewBean.NUMERO_ANNONCE, getIdAnnonce());
            }
        } // numéro de la caisse
          // - Si le numéro de caisse est vide, je set le numéro de caisse et le
          // numéro d'agence
          // par rapport au fichier properties
        if ((this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("22")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01"))) {
            setNumeroCaisse(StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE)) + "."
                    + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE)));
        } else if ((this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("38")
                || this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("39"))
                && HELotViewBean.CS_TYPE_RECEPTION.equals(getTypeLot())) {
            // réception, donc on prend la caisse CI
            setNumeroCaisse(StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI)) + "."
                    + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI)));
        } else if ((this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("38")
                || this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("39"))
                && HELotViewBean.CS_TYPE_ENVOI.equals(getTypeLot())) {
            // envoi
            setNumeroCaisse(StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE)) + "."
                    + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE)));
        } else if (!this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("22")) {
            if (this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE, true).equals("")
                    || this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE, true).equals("")) {
                put(IHEAnnoncesViewBean.NUMERO_CAISSE, getSession().getApplication().getProperty("noCaisse"));
                put(IHEAnnoncesViewBean.NUMERO_AGENCE, getSession().getApplication().getProperty("noAgence"));
                setNumeroCaisse(StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE)));
                if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("23")) {
                    setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE));
                }
            } else { // if (!getField(CODE_APPLICATION).equals("22")) {
                setNumeroCaisse(StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE)));
            }
        }
        // //////////////////////////////////////////////////////////////////////////////////////////////
        // je set la date
        // setDateAnnonce(currentDate);
        // L'utilisateur
        //
        // le référence unique pour la table annonce
        //
        // le statut
        if (getStatut().trim().length() == 0) {
            if (IHELotViewBean.TYPE_ENVOI.equals(getTypeLot())) {
                setStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);
            } else {
                setStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
            }
        } // le message
        setIdMessage("0");

        // cas particulier 63
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")
                && isMotifToOverrideChampObligatoire()) {
            String avs = (String) getInputTable().get(IHEAnnoncesViewBean.NUMERO_ASSURE);
            if (avs == null) {
                avs = "";
            }
            avs = avs.trim();
            if (avs.length() != 0) {
                put(IHEAnnoncesViewBean.ETAT_ORIGINE, "");
                put(IHEAnnoncesViewBean.SEXE, "");
                put(IHEAnnoncesViewBean.ETAT_NOMINATIF, "");
                put(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA, "");
            }
        } // cas particulier RCI
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("04")
                && (HEAnnoncesViewBean.isMotifRCI(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                        || HEAnnoncesViewBean.isMotifRevocation(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)))) { // vérification
            // de
            // la
            // date
            // de
            // cloture
            // TODO : Vérification du message d'erreur
            if (!JadeStringUtil.isBlank(this.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA))) {
                // si la date de clôture n'est pas vide, on contrôle aussi
                // qu'elle soit bien formatée
                String dateCloture = StringUtils.removeBeginChars(
                        StringUtils.removeDots(this.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA)), '0');
                if (JadeStringUtil.isBlank(dateCloture)
                        || ((Integer.parseInt(dateCloture) <= IHEAnnoncesViewBean.DATE_CLOTURE_AA_MAX)
                                && (Integer.parseInt(dateCloture) < IHEAnnoncesViewBean.DATE_CLOTURE_MMAA_MAX))) {
                    // date sous format AA et entrée sous forme 92
                    dateCloture = StringUtils.padBeforeString(dateCloture, "0", 2);
                    put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, StringUtils.padBeforeString(dateCloture, " ", 4));
                }
                if ((Integer.parseInt(dateCloture) > IHEAnnoncesViewBean.DATE_CLOTURE_AA_MAX)
                        && (Integer.parseInt(dateCloture) < IHEAnnoncesViewBean.DATE_CLOTURE_MMAA_MAX)) {
                    // date sous format MM.AA
                    put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, StringUtils.padBeforeString(dateCloture, "0", 4));
                }
            }
        }
        // BZ 7666 -> méthode permettant de setter la clé primaire en fonction de l'annonce
        if (codeApp.equals("39") || codeApp.equals("38")) {
            String pkForAnnonce = this.getField(CIAnnonceCIAdditionnel.ID_FIELD_INCREMENT_PROVIDER, true);
            String referenceUnique = this.getField(CIAnnonceCIAdditionnel.ID_FIELD_REFERENCE_UNIQUE_PROVIDER, true);

            if (!JadeStringUtil.isBlankOrZero(pkForAnnonce)) {
                setIdAnnonce(this._incCounter(transaction, pkForAnnonce));
            }

            if (!JadeStringUtil.isBlankOrZero(referenceUnique)) {
                setRefUnique(referenceUnique);
            }

        }
        validate(transaction);
        // on vérifie que ça existe pas déjà, dans ce lot si le code application
        // est 11
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")) {
            try {
                checkUniqueARC(transaction);
            } catch (Exception e) {
                if (isWantCheckUnique()) {
                    _addError(transaction, e.getMessage());
                }
            }
        }
        //
        if ("22".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            // statut pour les 22 sans ARC de départ
            // dépend de la caisse, au cas où y'a des filtres
            setStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
        }
        // traitement des annonces avis de décès
        if ("52".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            setStatut(IHEAnnoncesViewBean.CS_TERMINE);
        }
        // traitement des annonces rentes
        if ("50".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            setStatut(IHEAnnoncesViewBean.CS_TERMINE);
        }
        if ("51".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            setStatut(IHEAnnoncesViewBean.CS_TERMINE);
        }
        if ("53".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            setStatut(IHEAnnoncesViewBean.CS_TERMINE);
        }
        if ("61".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            setStatut(IHEAnnoncesViewBean.CS_TERMINE);
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
        } else if (!this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE).trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE));
        } else {
            // une fois que le NNSS est introduit, terminé de pré-calculé le
            // NNSS
            if (!HEUtil.isNNSSActif(getSession())) {
                try {
                    AVSUtils avs = new AVSUtils(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF),
                            this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA),
                            Integer.parseInt(this.getField(IHEAnnoncesViewBean.SEXE)));
                    avs.setFormat(DateUtils.JJMMAAAA);
                    setNumeroAVS(avs.getNumeroAvs());
                } catch (Exception e) {

                    setNumeroAVS(getNumeroAVS());
                }
            }
        }
        if ("11".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))) {
            String inputedRef = this.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);
            if ((inputedRef != null) && (inputedRef.trim().length() != 0)) {
                put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        StringUtils.transformDiphtAndAccent(inputedRef).toUpperCase());
            }
        }
        validate(transaction);
        super._beforeUpdate(transaction);
    }

    @Override
    protected void _validate(BStatement statement) {
        super._validate(statement);
        // si c'est une ouverture de CA, obliger l'ajout du numéro d'affilié
        try {
            if ("11".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                    && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))
                    && "true".equals(getSession().getApplication().getProperty("affilie.input"))) {
                // si un numéro d'affilié est présent, on le contrôle
                if (!JadeStringUtil.isEmpty(getNumeroAffilie())) {
                    try {
                        ((HEApplication) getSession().getApplication()).checkAffilie(getNumeroAffilie());
                    } catch (Exception e1) {
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_NUM_AFF_FAUX"));
                    }
                    try {
                        if (!getCategorie().equals(IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT)) {
                            ((HEApplication) getSession().getApplication()).checkDateEngagement(getDateEngagement(),
                                    getNumeroAffilie(), statement.getTransaction());
                        }
                    } catch (Exception e) {
                        _addError(statement.getTransaction(), e.getMessage());
                    }
                }
            }
            if ("97".equals(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                    && "11".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                    && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))
                    && "true".equals(getSession().getApplication().getProperty("adresse.input"))) {
                _propertyMandatory(statement.getTransaction(), getAdresseAssure(),
                        getSession().getLabel("HERMES_ADRESSE_OBLIGATOIRE"));
                _propertyMandatory(statement.getTransaction(), getLangueCorrespondance(),
                        getSession().getLabel("HERMES_LANGUE_OBLIGATOIRE"));
            }

            /* Vérification des motifs en fonction des rôles */
            if ("11".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                    && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT)) && isCheckRightActif()) {
                HECheckRights checkRights;
                try {
                    checkRights = new HECheckRights(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE), getSession());
                    try {
                        checkRights.checkRole();
                    } catch (Exception e2) {
                        _addError(statement.getTransaction(), e2.getMessage());
                    }
                } catch (HEOutputAnnonceException e1) {
                    _addError(statement.getTransaction(), e1.getMessage());
                }
            }

        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }

        /* fin vérification */
    }

    private void ajoutAttente22(BTransaction transaction, HEAttenteRetourViewBean attenteRetour22, String numeroCaisse,
            String numeroAgence) {
        try {
            List listeCaisses = ((HEApplication) getSession().getApplication()).getListCaisseAttendreCloture();
            if (listeCaisses.contains(numeroCaisse + "." + numeroAgence)) {
                attenteRetour22.setIdAnnonceRetourAttendue("15");
                attenteRetour22.add(transaction);
            }
        } catch (Exception e) {
            System.err.println("ajoutAttente22 " + e.toString());
        }
    }

    private void ajoutEnregistrements(BSession session, BTransaction transaction) throws Exception { // je
        // retire
        // celui
        // que
        // je
        // viens
        // de
        // faire
        toutParams.removeElement(paramAnnonce.getIdParametrageAnnonce());
        if (toutParams.size() != 0) {
            paramAnnonce.setIdParametrageAnnonce((String) toutParams.firstElement());
            paramAnnonce.retrieve(transaction);
            put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, paramAnnonce.getCodeEnregistrementDebut());
            this.add(transaction);
        }
    }

    private HEAttenteRetourViewBean ajoutRetours(BSession session, BTransaction transaction) throws Exception {
        HEAttenteRetourViewBean attenteRetour = new HEAttenteRetourViewBean();
        attenteRetour.setSession(getSession());
        lienAnnonceListe.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
        lienAnnonceListe.find(transaction);
        for (int i = 0; i < lienAnnonceListe.size(); i++) {
            HELienannonceViewBean lien = (HELienannonceViewBean) lienAnnonceListe.getEntity(i);
            String idParamRetour = lien.getPar_idParametrageAnnonce();
            if (!idParamRetour.equals("0")) { // on attend un truc en retour !
                // //System.out.println("Retour : " +
                // lien.getPar_idParametrageAnnonce());
                attenteRetour.setReferenceUnique(this.getField(getRefUnique()));
                attenteRetour.setIdAnnonce(getIdAnnonce());
                attenteRetour.setIdAnnonceRetourAttendue(idParamRetour);
                attenteRetour.setReferenceUnique(getRefUnique());
                if (!getNumeroAVS().equals("")) {
                    attenteRetour.setNumeroAvs(getNumeroAVS());
                } else { // on génére le numero AVS
                    if (!HEUtil.isNNSSActif(getSession())) {
                        try {
                            AVSUtils avs = new AVSUtils(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF),
                                    this.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA),
                                    Integer.parseInt(this.getField(IHEAnnoncesViewBean.SEXE)));
                            attenteRetour.setNumeroAvs(avs.getNumeroAvs());
                        } catch (Exception e) {
                            JadeLogger.error(this, e);
                            attenteRetour.setNumeroAvs(getNumeroAVS());
                        }
                    }
                }
                attenteRetour.setMotif(getMotif());
                attenteRetour.setNumeroCaisse(getNumeroCaisse());
                attenteRetour.setNnss(getNnss());
                attenteRetour.add(transaction);
            }
        }
        lienAnnonceListe.setForPar_idParametrageAnnonce(lienAnnonceListe.getForIdParametrageAnnonce());
        lienAnnonceListe.setForIdParametrageAnnonce("");
        lienAnnonceListe.find(transaction);
        return attenteRetour;
    }

    private void ajoutRetours25(BSession session, BTransaction transaction, HEAttenteRetourViewBean attenteRetour)
            throws Exception {
        HEAttenteRetourViewBean attenteCI = new HEAttenteRetourViewBean();
        attenteCI.setSession(getSession());
        attenteRetour.wantCallMethodAfter(false);
        /* Je cherche le 11 01 pour cette refunique */
        HEAttenteEnvoiListViewBean lvBean = new HEAttenteEnvoiListViewBean();
        lvBean.setSession(getSession());
        lvBean.setForCodeApplication("11");
        lvBean.setForReferenceUnique(getRefUnique());
        lvBean.find(transaction);
        if (lvBean.size() == 0) { // Orpheline
            attenteRetour.setIdAnnonce(getIdAnnonce());
            attenteRetour.setReferenceUnique(getRefUnique());
        } else {
            HEAttenteEnvoiViewBean arcOrigine = (HEAttenteEnvoiViewBean) lvBean.getFirstEntity();
            String refArcOrigine = arcOrigine.getRefUnique();
            String idArcOrigine = arcOrigine.getIdAnnonce();
            attenteRetour.setIdAnnonce(idArcOrigine);
            attenteRetour.setReferenceUnique(refArcOrigine);
        }
        String numCaisse = "";
        attenteRetour.setMotif(getMotif());
        attenteRetour.setNumeroAvs(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE).trim().equals("")) { // motif

            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_2).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_2).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_2))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_2)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_2));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_2),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_2));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_3).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_3).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_3))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_3)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_3));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_3),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_3));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_4).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_4).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_4))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_4)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_4));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_4),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_4));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_5).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_5).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_5))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_5)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_5));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_5),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_5));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_6).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_6).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_6))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_6)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_6));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_6),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_6));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_7).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_7).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_7))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_7)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_7));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_7),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_7));
            }
        }
        if (!this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_8).trim().equals("")
                && !this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_8).trim().equals("")) {
            if (generatesReturn(this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT_8))) {
                numCaisse = StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_8)) + "."
                        + StringUtils.unPad(this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_8));
                attenteRetour.setNumeroCaisse(numCaisse);
                attenteRetour.setIdAnnonceRetourAttendue("9");
                attenteRetour.add(transaction);
                ajoutAttente22(transaction, attenteRetour, this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_8),
                        this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_8));
            }
        }
    }

    private void callMethod(String classMethodName, BTransaction transaction) throws HEInputAnnonceException {
        String methodName = "";
        try {
            methodName = classMethodName.substring(classMethodName.lastIndexOf(".") + 1, classMethodName.length());
            Class c = this.getClass();
            Class[] methodParams = new Class[1];
            methodParams[0] = Class.forName("globaz.globall.db.BTransaction");
            Method hello = c.getMethod(methodName, methodParams);
            Object params[] = new Object[1];
            params[0] = transaction;
            hello.invoke(this, params);
        } catch (NoSuchMethodException nsme) {
            throw new HEInputAnnonceException("La méthode n'existe pas " + nsme.toString());
        } catch (InvocationTargetException ite) {
            throw (HEInputAnnonceException) ite.getTargetException();
        } catch (IllegalAccessException iae) {
            throw new HEInputAnnonceException(iae.toString());
        } catch (ClassNotFoundException cnfe) {
            throw new HEInputAnnonceException(cnfe.toString());
        }
    }

    public boolean canDoNext() {
        return false;
    }

    public boolean canDoPrev() {
        return false;
    }

    public int critereSize() {
        return motifCodeAppListe.size();
    }

    /**
     * Method generatesReturn. les CODE_TRAITEMENT des annonces 25 qui valent 01,02,94,73,09,71,75 et 94 ne génèrent pas
     * de retours
     *
     * @param CODE_DE_TRAITEMENT
     * @return boolean
     */
    private boolean generatesReturn(String codeTraitement) {
        // les 01,02,94,73,09,71,75,79,91 et 94 ne génèrent pas de retours
        // 67,81 si et slt si motif != 93,98
        return !"01".equals(codeTraitement) && !"02".equals(codeTraitement) && !"05".equals(codeTraitement)
                && !"94".equals(codeTraitement) && !"73".equals(codeTraitement) && !"09".equals(codeTraitement)
                && !"71".equals(codeTraitement) && !"77".equals(codeTraitement) && !"75".equals(codeTraitement)
                && !"79".equals(codeTraitement) && !"91".equals(codeTraitement)
                && !("67".equals(codeTraitement) && !("93".equals(getMotif()) || "98".equals(getMotif())
                        || "92".equals(getMotif()) || "97".equals(getMotif())))
                && !("81".equals(codeTraitement) && !("93".equals(getMotif()) || "98".equals(getMotif())
                        || "92".equals(getMotif()) || "97".equals(getMotif())));
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public String getAdresseAssure() {
        return adresseAssure;

    }

    public String getAdresseRentier() {
        return adresseRentier;
    }

    public String getCategorie() {
        return categorie;
    }

    @Override
    public Vector getChampsAsCodeSystem(String keyChamp) {
        Vector vList = new Vector(); // ajoute un blanc
        String[] list = new String[2];
        if (keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE) || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_1)) {
            try {
                HEApplication app = (HEApplication) getSession().getApplication();
                FWParametersSystemCodeManager manager = app.getCsPaysListe(getSession());
                vList = new Vector(manager.size());
                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                    String codePays = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                    list[0] = codePays;
                    list[1] = codePays + " - " + entity.getCurrentCodeUtilisateur().getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                return new Vector();
            }

            return vList;
        }
        return null;
    }

    public int getCount() {
        return 1;
    }

    public Vector getCountries(String keyChamp) {
        return new HEUtil().getCountries(keyChamp, getSession());
    }

    public HEMotifcodeapplication getHEMotifcodeapplication(int i) {
        return (HEMotifcodeapplication) motifCodeAppListe.getEntity(i);
    }

    public String getIdDernierAjout() {
        return idDernierAjout;
    }

    public java.lang.String getInputCritere() {
        return inputCritere;
    }

    public java.lang.String getInputMotif() {
        return inputMotif;
    }

    public String getLangueCorrespondance() {
        return langueCorrespondance;
    }

    public Vector getListeCat() {
        return new HEUtil().getListeCat(getSession());
    }

    public Vector getListeTitre() {
        return new HEUtil().getListeTitre(getSession());
    }

    public Vector getMotifCSListe() throws Exception {
        Vector vList = new Vector();
        // ajoute un blanc
        String[] list = new String[] { "", "" };
        vList.add(list);
        HEApplication app = (HEApplication) getSession().getApplication();
        BSession nSession = (BSession) app.newSession(getSession());
        FWParametersSystemCodeManager manager = app.getCsMotifsListe(nSession, true);
        for (int i = 0; i < manager.size(); i++) {
            list = new String[2];
            FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
            list[0] = entity.getCurrentCodeUtilisateur().getIdCodeSysteme();
            list[1] = entity.getCurrentCodeUtilisateur().getCodeUtilisateur() + " - "
                    + entity.getCurrentCodeUtilisateur().getLibelle();
            if (!list[0].equals(HEMotifsViewBean.CS_AUCUN_MOTIF)) {
                vList.add(list);
            }
        }
        Vector v2 = new Vector();
        int j;
        Object[] obj = vList.toArray();
        for (int i = 0; i <= obj.length - 1; i++) {
            String[] a = (String[]) obj[i];
            for (j = i + 1; j <= obj.length - 1; j++) {
                String[] b = (String[]) obj[j];
                if (a[1].compareTo(b[1]) > 0) {
                    Object tmp = obj[i];
                    obj[i] = obj[j];
                    obj[j] = tmp;
                    a = (String[]) obj[i];
                }
            }
        }
        for (int i = 0; i < obj.length; i++) {
            v2.addElement(obj[i]);
        }
        return v2;
    }

    public String getNumeroAgence() {
        try {
            return getSession().getApplication().getProperty("noAgence");
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    public String getNumeroCaisseDefault() {
        try {
            return getSession().getApplication().getProperty("noCaisse");
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    public int getOffset() {
        return 0;
    }

    public java.lang.String getParamNumeroAvs() {
        return paramNumeroAvs;
    }

    public java.lang.String getParamReferenceExterne() {
        return paramReferenceExterne;
    }

    public String getTitreAssure() {
        return titreAssure;
    }

    public String getTitreRentier() {
        return titreRentier;
    }

    public String getWantCheckNumAffilie() {
        return wantCheckNumAffilie;
    }

    public boolean isCheckRightActif() {
        return isCheckRightActif;
    }

    public boolean isWantCheckUnique() {
        return wantCheckUnique;
    }

    private void lierRetours(BSession session, BTransaction transaction) throws Exception {
        // Est-ce que le système a enregistré une demande pour l'annonce reçue ?
        boolean allEquals = true;
        // Recherche des attentes existantes
        HEAttenteRetourListViewBean retourListe = new HEAttenteRetourListViewBean();
        retourListe.setSession(getSession());
        retourListe.setForIdAnnonceRetour("0");
        retourListe.setForIdAnnonceRetourAttendue(paramAnnonce.getIdParametrageAnnonce());
        retourListe.setForMotif(getMotif());
        retourListe.setForNumeroCaisse(getNumeroCaisse());
        if (getChampEnregistrement() != null) {
            if (getChampEnregistrement().startsWith("2101")) {
                retourListe.setLikeNumeroAVS(getNumeroAVS());
            }
            if (getChampEnregistrement().startsWith("2001")) {
                // si il s'agit d'une confirmation, on peut rechercher les
                // attentes sur la référence unique
                // car elle est stockée dans la référence dans la confirmation
                // pas obligatoire car testé aussi après mais plus permformant,
                // permet de trouver plus rapidement le candidat
                retourListe.setForEndReferenceUnique(this.getField(IHEAnnoncesViewBean.NUMERO_ANNONCE));
            }
        }
        retourListe.wantCallMethodAfter(false);
        retourListe.find(transaction, BManager.SIZE_NOLIMIT);

        HEOutputAnnonceViewBean annonce = new HEOutputAnnonceViewBean();
        annonce.setSession(getSession());
        HELienannonceViewBean lienAnnonce = (HELienannonceViewBean) lienAnnonceListe.getEntity(0);
        // je récupère la liste des champs censés être identiques
        HELienChampAnnonceListViewBean lienChampListe = new HELienChampAnnonceListViewBean();
        lienChampListe.setSession(getSession());
        lienChampListe.setForIdLienAnnonce(lienAnnonce.getIdLienAnnonce());
        lienChampListe.find(transaction);
        // Dans un premier tableau je récupère la valeur des champs de l'annonce
        // en cours
        Hashtable currentFields = getFieldValues();
        int j = 0;
        for (; (j < retourListe.size()) && (lienChampListe.size() > 0); j++) {
            allEquals = true;
            HEAttenteRetourViewBean retour = (HEAttenteRetourViewBean) retourListe.getEntity(j);
            // on charge l'annonce associée au retour trouvé
            annonce.setIdAnnonce(retour.getIdAnnonce());
            annonce.retrieve(transaction);
            // j'ai la référence unique, il me faut tous les enregistrements
            // pour ce type d'ARC
            HEOutputAnnonceListViewBean tmpAnnonce = new HEOutputAnnonceListViewBean(getSession());
            tmpAnnonce.setForRefUnique(annonce.getRefUnique());
            tmpAnnonce.setForCodeApplication(annonce.getCodeApplication());
            tmpAnnonce.setForMotif(annonce.getMotif());
            tmpAnnonce.setForIdLot(annonce.getIdLot());
            tmpAnnonce.find(transaction);
            Hashtable suspectedFields = new Hashtable();
            for (int jj = 0; jj < tmpAnnonce.size(); jj++) {
                HEOutputAnnonceViewBean tmp = (HEOutputAnnonceViewBean) tmpAnnonce.getEntity(jj);
                suspectedFields.putAll(tmp.getFieldValues());
            }
            for (int k = 0; (k < lienChampListe.size()) && (lienChampListe.size() > 0); k++) {
                HELienChampAnnonceViewBean lienChamp = (HELienChampAnnonceViewBean) lienChampListe.getEntity(k);
                String field1 = (String) currentFields.get("" + lienChamp.getIdChampAnnonceRetour());
                String field2 = ((String) suspectedFields.get("" + lienChamp.getIdChampAnnonceDepart()));
                try {
                    if (!field1.equals(field2)) {
                        allEquals = false;
                    }
                } catch (NullPointerException ex) {
                    JadeLogger.error(this, ex);
                }
            }
            if (allEquals) {
                retour.setIdAnnonceRetour(getIdAnnonce());
                if (JadeStringUtil.isEmpty(retour.getNumeroAvs())) {
                    retour.setNumeroAvs(getNumeroAVS());
                    retour.setNumeroAvsNNSS(getNumeroAvsNNSS());
                }
                retour.update(transaction);
                if (JadeStringUtil.isEmpty(annonce.getNumeroAVS())) {
                    // si le numéro avs est vide, on le met à jour avec le
                    // numéro de la confirmation (2001)
                    HEOutputAnnonceViewBean arc11 = new HEOutputAnnonceViewBean();
                    arc11.setSession(getSession());
                    arc11.setIdAnnonce(annonce.getIdAnnonce());
                    arc11.setRefUnique(annonce.getRefUnique());
                    arc11.retrieve(transaction);
                    if (!arc11.isNew()) {
                        // mise à jour du numéro avs de la demande
                        arc11.wantCallMethodAfter(false);
                        arc11.wantCallValidate(false);
                        arc11.setNumeroAVS(getNumeroAVS());
                        arc11.setNumeroAvsNNSS(getNumeroAvsNNSS());
                        arc11.update(transaction);
                    }
                }
                // Ici traiter le cas des 2001
                if (getChampEnregistrement().startsWith("2001")) {
                    // cas en attente
                    if (this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT).equals("2")) {
                        HEAttenteRetourViewBean newAttente = new HEAttenteRetourViewBean();
                        newAttente.setSession(getSession());
                        newAttente.setIdAnnonce(retour.getIdAnnonce());
                        newAttente.setIdAnnonceRetourAttendue("6");
                        newAttente.setIdAnnonceRetour("0");
                        newAttente.setMotif(getMotif());
                        newAttente.setNumeroAvs(getNumeroAVS());
                        newAttente.setNumeroCaisse(getNumeroCaisse());
                        newAttente.setReferenceUnique(retour.getReferenceUnique());
                        newAttente.add(transaction);
                    } else if (this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT).equals("1")
                            || this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT).equals("3")
                            || this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT).equals("4")
                            || this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT).equals("6")
                            || this.getField(IHEAnnoncesViewBean.CODE_DE_TRAITEMENT).equals("7")) {
                        // ERREUR CENTRALE
                        setStatut(IHEAnnoncesViewBean.CS_PROBLEME);
                        annonce.setStatut(IHEAnnoncesViewBean.CS_PROBLEME);
                        this.update(transaction);
                        if (!JadeStringUtil.isEmpty(retour.getReferenceUnique())) {
                            // on met a jour la séquence
                            HEAnnoncesListViewBean annonceDepartListe = new HEAnnoncesListViewBean();
                            annonceDepartListe.setSession(getSession());
                            annonceDepartListe.setForRefUnique(retour.getReferenceUnique());
                            annonceDepartListe.wantCallMethodAfter(false);
                            annonceDepartListe.wantCallMethodBefore(false);
                            annonceDepartListe.wantCallMethodAfterFind(false);
                            annonceDepartListe.wantCallMethodBeforeFind(false);
                            annonceDepartListe.find(transaction, BManager.SIZE_NOLIMIT);
                            for (int l = 0; l < annonceDepartListe.size(); l++) {
                                HEAnnoncesViewBean annonceDepart = (HEAnnoncesViewBean) annonceDepartListe.getEntity(l);
                                annonceDepart.wantCallMethodAfter(false);
                                annonceDepart.wantCallMethodBefore(false);
                                annonceDepart.wantCallValidate(false);
                                annonceDepart.setStatut(IHEAnnoncesViewBean.CS_PROBLEME);
                                annonceDepart.update(transaction);
                            }
                            HEAttenteRetourListViewBean attentesDepart = new HEAttenteRetourListViewBean(getSession());
                            attentesDepart.setForReferenceUnique(retour.getReferenceUnique());
                            attentesDepart.setForIdAnnonceRetour("0");
                            attentesDepart.wantCallMethodAfter(false);
                            attentesDepart.wantCallMethodAfterFind(false);
                            attentesDepart.wantCallMethodBefore(false);
                            attentesDepart.wantCallMethodBeforeFind(false);
                            attentesDepart.find(transaction, BManager.SIZE_NOLIMIT);
                            for (int m = 0; m < attentesDepart.size(); m++) {
                                HEAttenteRetourViewBean attenteRetour = (HEAttenteRetourViewBean) attentesDepart
                                        .getEntity(m);
                                attenteRetour.delete(transaction);
                            }
                        }
                    }
                }
                if (!getRefUnique().equals(retour.getReferenceUnique())) {
                    wantCallMethodBefore(false);
                    setRefUnique(retour.getReferenceUnique());
                    setUtilisateur(annonce.getUtilisateur());
                    setIdProgramme(annonce.getIdProgramme());
                    setStatut(annonce.getStatut());
                    this.update(transaction);
                    wantCallMethodBefore(true);
                }
                if (!getUtilisateur().equals(annonce.getUtilisateur())) {
                    wantCallMethodBefore(false);
                    setUtilisateur(annonce.getUtilisateur());
                    setIdProgramme(annonce.getIdProgramme());
                    this.update(transaction);
                    wantCallMethodBefore(true);
                }
                break;
            }
        }
        if ((j == retourListe.size())
                && StringUtils.unPad(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT)).equals("1")) {
            wantCallMethodBefore(false);
            setStatut(IHEAnnoncesViewBean.CS_ORPHELIN);
            setRefUnique(refUnique);
            this.update(transaction);
            wantCallMethodBefore(true);
        }
    }

    /**
     * Method pushAnnonceCI.
     *
     * @param bSession
     * @param transaction
     */
    private void pushAnnonceCI(BSession bSession, BTransaction transaction) throws Exception {
        CIAnnonceSuspens annoncePush = new CIAnnonceSuspens();

        annoncePush.setIdAnnonce(getIdAnnonce());
        String motif = this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE);

        String numero_assure = this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
        String numero_caisse = StringUtils.formatCaisseAgence(this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE),
                this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE));
        String numero_caisse_commettante = StringUtils.formatCaisseAgence(
                this.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE),
                this.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
        String numero_assure_complete = this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_COMPLETE);
        String date = JACalendar.format(getDateAnnonce(), JACalendar.FORMAT_DDsMMsYYYY);
        BISession sessionPavo = ((HEApplication) getSession().getApplication()).getSessionCI(bSession);
        annoncePush.setSession((BSession) sessionPavo);
        switch (Integer.parseInt(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))) {
            case 21: {
                System.out.println("Push dans PAVO, annonce 21");
                annoncePush.setCodeApplication("21");
                annoncePush.setNumeroAvs(numero_assure);
                annoncePush.setIdMotifArc(motif);
                annoncePush.setNumeroCaisse(numero_caisse);
                annoncePush.setDateReception(date);
                annoncePush.add(transaction);
                break;
            }
            case 22: {
                System.out.println("Push dans PAVO, annonce 22");
                annoncePush.setCodeApplication("22");
                annoncePush.setNumeroAvs(numero_assure);
                annoncePush.setIdMotifArc(motif);

                annoncePush.setNumeroCaisse(numero_caisse_commettante);
                annoncePush.setDateReception(date);
                annoncePush.add(transaction);
                break;
            }
            case 23: {
                System.out.println("Push dans PAVO, annonce 23");
                annoncePush.setCodeApplication("23");
                annoncePush.setNumeroAvs(numero_assure_complete);
                annoncePush.setIdMotifArc(motif);
                annoncePush.setNumeroCaisse(numero_caisse);
                annoncePush.setDateReception(date);
                annoncePush.add(transaction);
                break;
            }
            case 29: {
                System.out.println("Push dans PAVO, annonce 29");
                annoncePush.setCodeApplication("29");
                annoncePush.setNumeroAvs(numero_assure);
                annoncePush.setIdMotifArc(motif);
                annoncePush.setNumeroCaisse(numero_caisse_commettante);
                annoncePush.setDateReception(date);
                annoncePush.add(transaction);
                break;
            }
            default: {
                return;
            }
        }
    }

    public void setActionMessage(String string) {
        actionMessage = string;
    }

    public void setAdresseAssure(String string) {
        adresseAssure = string;
    }

    public void setAdresseRentier(String adresseRentier) {
        this.adresseRentier = adresseRentier;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setCheckRightActif(boolean isCheckRightActif) {
        this.isCheckRightActif = isCheckRightActif;
    }

    public void setIdDernierAjout(String string) {
        idDernierAjout = string;
    }

    public void setInputCritere(java.lang.String newInputCritere) {
        inputCritere = newInputCritere;
    }

    public void setInputMotif(java.lang.String newInputMotif) {
        inputMotif = newInputMotif;
    }

    private void setIsValidated(boolean newIsValidated) {
        isValidated = newIsValidated;
    }

    public void setLangueCorrespondance(String string) {
        langueCorrespondance = string;
    }

    public void setParamNumeroAvs(java.lang.String newParamNumeroAvs) {
        if ((newParamNumeroAvs != null) && (newParamNumeroAvs.trim().length() != 0)) {
            paramNumeroAvs = newParamNumeroAvs;
        } else {
            paramNumeroAvs = "";
        }
    }

    public void setParamReferenceExterne(java.lang.String newParamReferenceExterne) {
        paramReferenceExterne = newParamReferenceExterne;
    }

    public void setTitreAssure(String string) {
        titreAssure = string;
    }

    public void setTitreRentier(String titreRentier) {
        this.titreRentier = titreRentier;
    }

    public void setToutParams(String s[]) {
        for (int i = 0; i < s.length; i++) {
            if (!toutParams.contains(s[i])) {
                toutParams.add(s[i]);
            }
        }
    }

    public void setWantCheckNumAffilie(String isCheckNumAff) {
        wantCheckNumAffilie = isCheckNumAff;
    }

    public int size() {
        return 1;
    }

    private boolean validate(BTransaction transaction) throws Exception { // Ajout

        // de
        // la
        // plausibilité
        // de
        // la
        // longueur
        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")
                && this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("04")
                && (HEAnnoncesViewBean.isMotifRCI(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE))
                        || HEAnnoncesViewBean.isMotifRevocation(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)))) { // vérification
            // de
            // la
            // date
            // de
            // cloture
            if (JAUtil.isStringEmpty(this.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA))) {
                String msg = FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                        ((((HEApplication) getSession().getApplication()).getCsChampsListe(getSession()))
                                .getCodeSysteme(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA)).getCurrentCodeUtilisateur()
                                        .getLibelle());
                _addError(transaction, msg);
            } else {
                String dateCloture = StringUtils.removeBeginChars(
                        StringUtils.removeDots(this.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA)), '0');
                if (Integer.parseInt(dateCloture) > IHEAnnoncesViewBean.DATE_CLOTURE_MMAA_MAX) {
                    // date sous format AA et entrée sous forme 1992
                    _addError(transaction, getSession().getLabel("HERMES_00003") + " " + dateCloture);
                }
                if ((this.getField(IHEAnnoncesViewBean.DOMICILE_EN_SUISSE_CODE_INFORMATION) != null)
                        && this.getField(IHEAnnoncesViewBean.DOMICILE_EN_SUISSE_CODE_INFORMATION).equals("1")) { // le
                    // domicile
                    // est
                    // obligatoire
                    if ("".equals(this.getField(IHEAnnoncesViewBean.DATE_DEBUT_1ER_DOMICILE_MMAA))
                            || "".equals(this.getField(IHEAnnoncesViewBean.DATE_FIN_1ER_DOMICILE_MMAA))) {
                        _addError(transaction, getSession().getLabel("HERMES_00025"));
                    }
                }
                // Si l'ayant droit est une autre personne, vérifier la présence
                // du numéro AVS
                if ((this.getField(IHEAnnoncesViewBean.AYANT_DROIT) != null)
                        && this.getField(IHEAnnoncesViewBean.AYANT_DROIT).equals("0")) {
                    String numAvsAyantDroit = this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT);
                    numAvsAyantDroit = StringUtils.removeDots(numAvsAyantDroit);
                    if ("".equals(numAvsAyantDroit)) {
                        String msg = FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                                ((((HEApplication) getSession().getApplication()).getCsChampsListe(getSession()))
                                        .getCodeSysteme(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT))
                                                .getCurrentCodeUtilisateur().getLibelle());
                        _addError(transaction, msg);
                    }
                }
                String numAssure = StringUtils.removeDots(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
                String numAssureAyantDroit = StringUtils
                        .removeDots(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT));
                if (!JAUtil.isStringEmpty(numAssure) && !JAUtil.isStringEmpty(numAssureAyantDroit)
                        && (numAssure.equals(numAssureAyantDroit))
                        && this.getField(IHEAnnoncesViewBean.AYANT_DROIT).equals("0")) {
                    _addError(transaction, getSession().getLabel("HERMES_00026"));
                }
            }
        }

        if (this.getField(IHEAnnoncesViewBean.CODE_APPLICATION).equals("11")) {
            // lors de la saisie d'un ARC 11, vérifier que chaque numero avs
            // soit sur chaque chiffre
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {

                if (JadeStringUtil.removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), '.')
                        .length() < NUM_AVS_LENGTH) {

                    if (getNnss().booleanValue()) {
                        if (JadeStringUtil.removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), '.')
                                .length() < NUM_NSS_LENGTH) {
                            _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10014"),
                                    String.valueOf(NUM_NSS_LENGTH)));
                        }
                    } else {
                        _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10014"),
                                String.valueOf(NUM_AVS_LENGTH)));
                    }
                }
            }

            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_1))) {
                if (JadeStringUtil.removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_1), '.')
                        .length() < NUM_AVS_LENGTH) {
                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10015"),
                            String.valueOf(NUM_AVS_LENGTH)));
                }
            }
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_2))) {
                if (JadeStringUtil.removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_2), '.')
                        .length() < NUM_AVS_LENGTH) {
                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10015"),
                            String.valueOf(NUM_AVS_LENGTH)));
                }
            }
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_3))) {
                if (JadeStringUtil.removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_3), '.')
                        .length() < NUM_AVS_LENGTH) {
                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10015"),
                            String.valueOf(NUM_AVS_LENGTH)));
                }
            }
            if (!JadeStringUtil.isEmpty((this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT)))) {
                if (JadeStringUtil.removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT), '.')
                        .length() < NUM_AVS_LENGTH) {
                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10016"),
                            String.valueOf(NUM_AVS_LENGTH)));
                }
            }
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE))) {
                if (JadeStringUtil
                        .removeChar(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE), '.')
                        .length() < NUM_AVS_LENGTH) {
                    _addError(transaction, FWMessageFormat.format(getSession().getLabel("HERMES_10017"),
                            String.valueOf(NUM_AVS_LENGTH)));
                }
            }
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.ETAT_ORIGINE))) {
                // checker que le code pays est correct
                if (!HEUtil.checkCodePays(this.getField(IHEAnnoncesViewBean.ETAT_ORIGINE))) {
                    _addError(transaction, getSession().getLabel("HERMES_10022"));
                }
            }
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF))) {
                // checker l'état nominatif en fonction des règles de la
                // centrale
                if (!HEUtil.checkEtatNominatif(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF))) {
                    _addError(transaction, getSession().getLabel("HERMES_00017"));
                }
                // BZ 8457 --> contrôle si le nom inséré ne contient pas de traits d'union suivi ou précédé d'espace
                if (!HEUtil.checkWordWithHyphen(this.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF))) {
                    _addError(transaction, getSession().getLabel("HERMES_00017"));
                }
            }
            // validation du nnss en fonction du chiffre-clé
            if (!JadeStringUtil.isEmpty(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))
                    && HENNSSUtils.isNNSSLength(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))) {
                String vnss = "";
                if (!globaz.commons.nss.NSUtil.nssCheckDigit(getNumeroAVS())) {
                    vnss = getSession().getLabel("HERMES_10055");
                    _addError(transaction, vnss);
                }
            }
        } // Je regarde si le code application existe (AJPPCOS)
        HECodeapplicationListViewBean caM = new HECodeapplicationListViewBean();
        caM.setForCodeUtilisateur(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION, false));
        caM.setSession(getSession());
        caM.find(transaction);
        // le code app
        HECodeapplicationViewBean codeApp = (HECodeapplicationViewBean) caM.getEntity(0);
        if (caM.size() == 0) {

            _addError(transaction,
                    "Ce code application n'existe pas : " + this.getField(IHEAnnoncesViewBean.CODE_APPLICATION));
        } else if (caM.size() == 2) { // cas particulier du 38
            String code = this.getField(IHEAnnoncesViewBean.CODE_1_OU_2, false);
            // code doit contenir 1 ou 2
            // Si c'est 1, l'id du code application est 111011
            // Sinon c'est 111040
            for (int i = 0; i < caM.size(); i++) {
                codeApp = (HECodeapplicationViewBean) caM.getEntity(i);
                if (code.equals("1") && codeApp.getIdCode().equals("111011")) {
                    codeApp = (HECodeapplicationViewBean) caM.getEntity(i);
                    break;
                } else if (code.equals("2") && codeApp.getIdCode().equals("111040")) {
                    codeApp = (HECodeapplicationViewBean) caM.getEntity(i);
                    break;
                }
            }
        } //
          // Je traite le motif
        HEMotifsViewBean motifViewBean;
        if (this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).equals(HEMotifsViewBean.CS_AUCUN_MOTIF)) {
            motifViewBean = new HEMotifsViewBean();
            // aucun motif n'a été spécifié !
            motifViewBean.setIdCode(HEMotifsViewBean.CS_AUCUN_MOTIF);
            motifViewBean.setSession(getSession());
            motifViewBean.retrieve(transaction);
        } else {
            // un motif a été spécifié
            HEMotifsListViewBean motifListViewBean = new HEMotifsListViewBean();
            motifListViewBean.setSession(getSession());
            motifListViewBean.setForCodeUtilisateur(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            motifListViewBean.setForActif(new Boolean(false));
            motifListViewBean.find(transaction);
            motifViewBean = (HEMotifsViewBean) motifListViewBean.getFirstEntity();
            if (motifViewBean == null) {

                _addError(transaction, "Motif inexistant : " + this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
                throw new HEInputAnnonceException(
                        "Motif inexistant : " + this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            }
        } //
          // /////////////////////////////////////////////////////////////////////////
          //
          // Je regarde s'il existe cet enregistrement pour ce code application
          // (HEPAREP)
          //
        HEParametrageannonceManager paramManager = new HEParametrageannonceManager();
        paramManager.setSession(getSession());
        paramManager.setForAfterCodeEnregistrementDebut(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        paramManager.setForBeforeCodeEnregistrementFin(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
        paramManager.setForIdCSCodeApplication(codeApp.getIdCode());
        paramManager.find(transaction);
        if (paramManager.size() < 1) {
            _addError(transaction, "Cet enregistrement pour ce code application est invalide");
        } // - je charge les champs de l'annonce
        paramAnnonce = (HEParametrageannonce) paramManager.getEntity(0);
        // on extrait la méthode selon ce code application - enregistrement -
        // motif
        // - On récupère le lien annonce
        lienAnnonceListe = new HELienannonceListViewBean();
        lienAnnonceListe.setSession(getSession());
        lienAnnonceListe.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
        lienAnnonceListe.setForIdMotif(motifViewBean.getIdCode());
        lienAnnonceListe.wantCallMethodAfter(false);
        lienAnnonceListe.find(transaction);
        // remplir la chaîne en fonction des paramètres
        // BZ 9043 enlever for input string
        if (!transaction.hasErrors()) {
            parseARC(transaction);
        }

        if (getNumeroAVS().trim().equals("")) {
            setNumeroAVS(this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
        }
        if (!this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).trim().equals("")) {
            setMotif(this.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        }

        // Validation suivant la catégorie. Mettre des labels.
        if (HEUtil.isNNSSActif(getSession()) && "11".equals(this.getField(IHEAnnoncesViewBean.CODE_APPLICATION))
                && "01".equals(this.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT))
                && globaz.hermes.utils.HEUtil.isMotifCert(getSession(), getMotif())) {
            String msg = "";

            if (JadeStringUtil.isEmpty(getCategorie())) {
                msg = FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                        getSession().getLabel("HERMES_10050"));
                _addError(transaction, msg);
            }

            if (getCategorie().equals(IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR)
                    && (JadeStringUtil.isEmpty(getNumeroAffilie()))) {
                if (JadeStringUtil.isEmpty(getNumeroAffilie())) {
                    msg = FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                            getSession().getLabel("HERMES_10051"));
                    _addError(transaction, msg);
                }
            }

            if (getCategorie().equals(IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT)
                    && JadeStringUtil.isEmpty(getNumeroAffilie())) {
                msg = FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                        getSession().getLabel("HERMES_10051"));
                _addError(transaction, msg);
            }

            if (getCategorie().equals(IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER)
                    && JadeStringUtil.isEmpty(getAdresseRentier()))// ||

            {
                if (JadeStringUtil.isEmpty(getAdresseRentier())) {
                    msg = FWMessageFormat.format(getSession().getLabel("HERMES_00004"),
                            getSession().getLabel("HERMES_10053"));
                    _addError(transaction, msg);
                }

            }
        }

        return !transaction.hasErrors();

    }

    /**
     * @param b
     */
    public void wantCheckUnique(boolean b) {
        wantCheckUnique = b;
    }

    /**
     * Getter de formulePolitesse
     *
     * @return the formulePolitesse
     */
    public String getFormulePolitesse() {
        return formulePolitesse;
    }

    /**
     * Ce getter est spécifique, il permettra de ne pas avoir un bug d'affichage au moment de l'action réafficher du
     * framework.
     *
     * @return the warningEmployeurSansPersoOrAccountZero
     */
    public String getWarningEmployeurSansPersoOrAccountZero() {
        String warningToReturn = warningEmployeurSansPersoOrAccountZero;
        warningEmployeurSansPersoOrAccountZero = "";
        return warningToReturn;
    }

    /**
     * @param warningEmployeurSansPersoOrAccountZero the warningEmployeurSansPersoOrAccountZero to set
     */
    public void setWarningEmployeurSansPersoOrAccountZero(String warningEmployeurSansPersoOrAccountZero) {
        this.warningEmployeurSansPersoOrAccountZero = warningEmployeurSansPersoOrAccountZero;
    }

    /**
     * Setter de formulePolitesse
     *
     * @param formulePolitesse the formulePolitesse to set
     */
    public void setFormulePolitesse(String formulePolitesse) {
        this.formulePolitesse = formulePolitesse;
    }

    public Boolean getChkCreerArc61() {
        return chkCreerArc61;
    }

    public void setChkCreerArc61(Boolean chkCreerArc61) {
        this.chkCreerArc61 = chkCreerArc61;
    }

}

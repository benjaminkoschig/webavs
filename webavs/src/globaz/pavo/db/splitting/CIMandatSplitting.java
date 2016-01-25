package globaz.pavo.db.splitting;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.utils.DateUtils;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIPeriodeSplitting;
import globaz.pavo.db.compte.CIPeriodeSplittingManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIPersonneAvs;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Classe représentant un mandat de splitting. Date de création : (14.10.2002 15:43:18)
 * 
 * @author: dgi
 */
public class CIMandatSplitting extends BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ANNEE_JEUNESSE = "306001";
    public final static String CS_ANNEE_JEUNESSE_MAN = "306011";
    public final static String CS_DEMANDE_REVOCATION = "307004";
    public final static String CS_ERREUR = "307006";
    public final static String CS_LACUNE_COTISATION_APPOINT = "306003";
    public final static String CS_LACUNE_COTISATION_APPOINT_MAN = "306013";
    public final static String CS_LACUNE_COTISATION_JEUNESSE = "306002";
    public final static String CS_LACUNE_COTISATION_JEUNESSE_MAN = "306012";
    public final static String CS_LICHTENSTEIN = "306100";
    // Genre de splitting
    public final static String CS_MANDAT_NORMAL = "306000";
    public final static String CS_MANDAT_VIDE_POUR_ASSURE = "306014";
    // constantes
    // Etat
    public final static String CS_OUVERT = "307001";
    public final static String CS_PARTAGE_CI_CLOTURES = "306005";
    public final static String CS_PARTAGE_RAM = "306004";
    public final static String CS_REVOQUE = "307005";
    public final static String CS_SPLITTING_EN_COURS = "307002";
    public final static String CS_TERMINE = "307003";
    // id annonces
    private final static String[] idAnnonces95 = new String[] {
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_01,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_02,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_03,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_04,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_05,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_06,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_07,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_08,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_09,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_10,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_11 };

    /**
     * Teste si le numéro AVS donné appartient à un homme. Date de création : (29.10.2002 14:59:19)
     * 
     * @return true si le propriétaire du numéro donné est un homme.
     * @param avs
     *            le numéro avs.
     */
    private static boolean isMale(String avs) {
        int pos = 5;
        if (avs.indexOf('.') != -1) {
            // ajouter 2 si les points de séparations sont présents
            pos += 2;
        }
        return Character.getNumericValue(avs.charAt(pos)) < 5;
    }

    private java.lang.String anneeDebut = new String();
    private java.lang.String anneeFin = new String();
    private java.lang.String degreInvalidite = new String();
    // dossier lié
    private CIDossierSplitting dossier = null;
    private java.lang.String idArc = new String();
    private java.lang.String idDossierSplitting = new String();
    private java.lang.String idEtat = new String();
    private java.lang.String idGenreSplitting = new String();
    private java.lang.String idJournal = new String();
    private java.lang.String idMandatSplitting = new String();
    private java.lang.String idTiersPartenaire = new String();
    private java.lang.String montant = new String();
    // en-tête de l'aperçu des revenus
    private String periodeMandat;
    private String tiersPartenaireNomComplet = new String();

    private String totalRevenus;

    /**
     * Constructeur
     */
    public CIMandatSplitting() {
        super();
        // genre de splitting par défault
        setIdGenreSplitting(CIMandatSplitting.CS_MANDAT_NORMAL);
        // état par défaut
        setIdEtat(CIMandatSplitting.CS_OUVERT);
    }

    /**
     * Supprime tous les revenus associés au mandat
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // effacement revenus
        CIRevenuSplittingManager revenus = new CIRevenuSplittingManager();
        revenus.setForIdMandatSplitting(getIdMandatSplitting());
        revenus.setSession(getSession());
        revenus.find(transaction);
        for (int i = 0; i < revenus.size(); i++) {
            BEntity _entity = (BEntity) revenus.getEntity(i);
            _entity.delete(transaction);
        }
    }

    /*
     * Traitement avant ajout
     */
    /**
     * Efface le montant et le degré d'invalidité si ceux-ci sont à zéro et qu'ils ne doivent pas être affichés puis les
     * formatte.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieveWithResultSet(globaz.globall.db.BStatement statement) throws java.lang.Exception {
        double degre = Double.parseDouble(degreInvalidite) * 100;
        degreInvalidite = JANumberFormatter.formatZeroValues(String.valueOf(degre), false, true);
        montant = JANumberFormatter.format(JANumberFormatter.formatZeroValues(montant, true, true));
        if (!JAUtil.isIntegerEmpty(getIdDossierSplitting())) {
            dossier = new CIDossierSplitting();
            dossier.read(statement);
        }
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // test état du dossier
        if (isOperationAllowed(transaction)) {
            // incrémente de +1 le numéro
            setIdMandatSplitting(this._incCounter(transaction, "0"));
        } else {
            // ajout impossible
            _addError(transaction, getSession().getLabel("MSG_MANDAT_ADD_ETAT"));
        }
        if (!transaction.hasErrors()) {
            checkChevauchement(transaction);
            checkChevauchementDomicile(transaction);
        }
        if (CIDossierSplitting.CS_DEMANDE_PROPRE_CAISSE.equals(dossier.getIdMotifSplitting()) && isMandatManuel()) {
            _addError(transaction, getSession().getLabel("MSG_SPLITTING_NON_AUTORISE"));
        }

    }

    /**
     * Teste si la supression est autorisée.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (!CIMandatSplitting.CS_OUVERT.equals(idEtat) && !CIMandatSplitting.CS_REVOQUE.equals(idEtat)
                && !CIMandatSplitting.CS_ERREUR.equals(idEtat)) {
            // suppression impossible
            _addError(transaction, getSession().getLabel("MSG_MANDAT_DEL_ETAT"));
        }
        if (getSession() == null) {
            _addError(transaction, getSession().getLabel("MSG_DOSSIER_DEL_USER"));
        } else {
            // role responsable ci
            String role = ((CIApplication) getSession().getApplication()).getRoleResponsableCI();
            String[] roleUser = JadeAdminServiceLocatorProvider.getLocator().getRoleService()
                    .findAllIdRoleForIdUser(getSession().getUserId());
            boolean hasRight = false;
            for (int i = 0; i < roleUser.length; i++) {
                if (role.trim().equals(roleUser[i])) {
                    hasRight = true;
                    break;
                }
            }
            if (!hasRight
                    && !loadDossier(transaction).getUser(transaction).getVisa().trim().equals(getSession().getUserId())) {
                // pas l'authorisation
                _addError(transaction, getSession().getLabel("MSG_DOSSIER_DEL_USER"));
            }
        }
    }

    /**
     * Teste si la supression est autorisée.
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (!CIMandatSplitting.CS_OUVERT.equals(idEtat)) {
            // modification impossible
            _addError(transaction, getSession().getLabel("MSG_MANDAT_MOD_ETAT"));
        }
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String joinStr = " inner join " + _getCollection() + "CISPDSP on " + _getCollection() + _getTableName()
                + ".KDID=" + _getCollection() + "CISPDSP.KDID";
        return _getCollection() + _getTableName() + joinStr;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CISPMAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idMandatSplitting = statement.dbReadNumeric("KEID");
        idDossierSplitting = statement.dbReadNumeric("KDID");
        idJournal = statement.dbReadNumeric("KCID");
        idTiersPartenaire = statement.dbReadNumeric("KEITPA");
        idGenreSplitting = statement.dbReadNumeric("KEIGSP");
        anneeDebut = statement.dbReadNumeric("KEADEB");
        anneeFin = statement.dbReadNumeric("KEAFIN");
        montant = statement.dbReadNumeric("KEMTOT", 2);
        degreInvalidite = statement.dbReadNumeric("KETDIN", 5);
        idArc = statement.dbReadNumeric("KEIARC");
        idEtat = statement.dbReadNumeric("KEIETA");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // ids - ne devrait jamais arriver
        if (JAUtil.isStringEmpty(getIdDossierSplitting()) || JAUtil.isStringEmpty(getIdTiersPartenaire())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_ID"));
            return;
        }
        // if
        // (CIDossierSplitting.CS_OUVERT.equals(loadDossier(statement.getTransaction()).getIdEtat()))
        // {
        // _addError(statement.getTransaction(),
        // getSession().getLabel("MSG_MANDAT_ETAT_DOSSIER"));
        // }
        // dates
        if (JAUtil.isDateEmpty("01.01." + anneeDebut)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_DEBUT"));
            return;
        }
        if (JAUtil.isDateEmpty("01.01." + anneeFin)) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_FIN"));
            return;
        }
        int anneeDebutInt = Integer.parseInt(getAnneeDebut());
        int anneeFinInt = Integer.parseInt(getAnneeFin());
        if (anneeFinInt < anneeDebutInt) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_DATE"));
            return;
        }
        if (!BSessionUtil.compareDateFirstLowerOrEqual(getSession(), "01.01.1948", "01.01." + getAnneeDebut())) {
            _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_DATE_1948"));
            return;
        }
        if (CIMandatSplitting.CS_ANNEE_JEUNESSE_MAN.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT_MAN.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT_MAN.equals(getIdGenreSplitting())) {
            // année début et fin identiques
            if (anneeDebutInt != anneeFinInt) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_DATE_IDENT"));
                return;
            }
        }
        /*
         * if (isAssure(null) || (!CS_ANNEE_JEUNESSE.equals(getIdGenreSplitting()) &&
         * !CS_ANNEE_JEUNESSE_MAN.equals(getIdGenreSplitting()))) { // test sur le mariage/divorce int anneeMariage =
         * Integer.parseInt(loadDossier(statement .getTransaction()).getDateMariage().substring(6)); int anneeDivorce =
         * Integer .parseInt(loadDossier(statement.getTransaction()).getDateDivorce ().substring(6)); if (anneeDebutInt
         * <= anneeMariage || anneeFinInt >= anneeDivorce) { _addError(statement.getTransaction(),
         * getSession().getLabel("MSG_MANDAT_DATE_PERIODE")); return; } }
         */
        CICompteIndividuel ciPart = CICompteIndividuel.loadCI(getIdTiersPartenaire(), statement.getTransaction());
        if (ciPart != null) {
            if (isAssure(null)
                    || (!CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE.equals(getIdGenreSplitting())
                            && !CIMandatSplitting.CS_ANNEE_JEUNESSE.equals(getIdGenreSplitting())
                            && !CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE_MAN.equals(getIdGenreSplitting()) && !CIMandatSplitting.CS_ANNEE_JEUNESSE_MAN
                                .equals(getIdGenreSplitting()))) {
                // doir avoir au moins 21 ans
                /*
                 * if (!CIUtil.isAgeSplitting(new JADate(ciPart.getDateNaissance()), anneeDebutInt)) { // pas 21 ans
                 * _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_DATE_21")); return; }
                 */
            }
            if (CIUtil.isRetraite(new JADate(ciPart.getDateNaissance()), ciPart.getSexe(), anneeFinInt)) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_DATE_RETRAITE"));
                return;
            }
        } else {
            // ci plus disponible, ne devrait jamais arriver
        }
        // tests pour le genre LICHTENSTEIN
        if (getIdGenreSplitting().equals(CIMandatSplitting.CS_LICHTENSTEIN)) {
            /*
             * if (!isMale(getIdTiersPartenaire())) { // seulement pour l'époux _addError( statement.getTransaction(),
             * java.text.MessageFormat.format( getSession().getLabel("MSG_MANDAT_VAL_GENRE"), new Object[] {
             * CodeSystem.getLibelle(globaz.pavo.db.splitting.CIMandatSplitting. CS_LICHTENSTEIN, getSession())})); }
             */
            if (Integer.parseInt(anneeFin) > 1996) {
                // impossible pour cette date
                _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_FIN_LI"));
            }
        }
        if (CIMandatSplitting.CS_PARTAGE_CI_CLOTURES.equals(idGenreSplitting)
                || CIMandatSplitting.CS_PARTAGE_RAM.equals(idGenreSplitting)
                || CIMandatSplitting.CS_LICHTENSTEIN.equals(idGenreSplitting)
                || CIMandatSplitting.CS_ANNEE_JEUNESSE_MAN.equals(idGenreSplitting)
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT_MAN.equals(idGenreSplitting)
                || CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE_MAN.equals(idGenreSplitting)) {
            // montant nécessaire pour ces genre de splitting
            montant = JANumberFormatter.deQuote(montant);
            if (JAUtil.isIntegerEmpty(montant)) {
                _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_MONTANT"));
                return;
            }
        } else {
            // effacement du montant dans les autres cas
            montant = "";
        }
        // tests pour le partage de ram
        if (CIMandatSplitting.CS_PARTAGE_RAM.equals(idGenreSplitting)) {
            // année de fin >= 1980 si partage de ram
            // changement 21.07.04 -> si < 1974, ok mais définir un montant par
            // année
            if (Integer.parseInt(anneeDebut) < 1980) {
                if (!getAnneeDebut().equals(getAnneeFin())) {
                    // si pas même dates, erreur
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_RAM"));
                    return;
                }
                // _addError(statement.getTransaction(),
                // getSession().getLabel("MSG_MANDAT_VAL_RAM"));
                // return;
            }
            // fin changement
            // ram multiple de rente minimal
            RenteMinimale rente = RenteMinimale.getRenteMinimale(anneeFin);
            int multiple = rente.getRmVeuf();
            if (!rente.estRenteVeufDéfinie()) {
                // pas d'information pour cette année (ne devrait pas arriver)
                _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_FICHIER"));
                return;
            } else {
                if (Double.parseDouble(montant) % multiple != 0) {
                    _addError(statement.getTransaction(), java.text.MessageFormat.format(
                            getSession().getLabel("MSG_MANDAT_VAL_RENTES"), new Object[] { String.valueOf(multiple),
                                    anneeFin }));
                    return;
                }
            }
            // degré d'invalidité
            double degre;
            try {
                degre = Double.parseDouble(degreInvalidite);
                if (degre > 1) {
                    degre = degre / 100;
                }
                if ((degre != 0.5) && (degre != 1)) {
                    _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_PRC"));
                } else {
                    degreInvalidite = String.valueOf(degre);
                }
            } catch (Exception e) {
                // pas un double
                _addError(statement.getTransaction(), getSession().getLabel("MSG_MANDAT_VAL_INVALID"));
            }
        } else {
            // effacement du taux si existe
            degreInvalidite = "";
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("KEID", this._dbWriteNumeric(statement.getTransaction(), getIdMandatSplitting(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("KEID",
                this._dbWriteNumeric(statement.getTransaction(), getIdMandatSplitting(), "idMandatSplitting"));
        statement.writeField("KDID",
                this._dbWriteNumeric(statement.getTransaction(), getIdDossierSplitting(), "idDossierSplitting"));
        statement.writeField("KCID", this._dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("KEITPA",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersPartenaire(), "idTiersPartenaire"));
        statement.writeField("KEIGSP",
                this._dbWriteNumeric(statement.getTransaction(), getIdGenreSplitting(), "idGenreSplitting"));
        statement.writeField("KEADEB", this._dbWriteNumeric(statement.getTransaction(), getAnneeDebut(), "anneeDebut"));
        statement.writeField("KEAFIN", this._dbWriteNumeric(statement.getTransaction(), getAnneeFin(), "anneeFin"));
        statement.writeField("KEMTOT", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("KETDIN",
                this._dbWriteNumeric(statement.getTransaction(), getDegreInvalidite(), "degreInvalidite"));
        statement.writeField("KEIARC", this._dbWriteNumeric(statement.getTransaction(), getIdArc(), "idArc"));
        statement.writeField("KEIETA", this._dbWriteNumeric(statement.getTransaction(), getIdEtat(), "idEtat"));
    }

    private void ajoutPeriodeSplManuel(String compteIndividuelId, String noAvsConjoint, BTransaction transaction)
            throws Exception {
        // ajout dans période splitting
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        CIPeriodeSplitting nouvellePeriode = new CIPeriodeSplitting();
        nouvellePeriode.setSession(getSession());
        nouvellePeriode.setCompteIndividuelId(compteIndividuelId);
        nouvellePeriode.setCaisseCommettante(application.getAdministrationLocale(getSession())
                .getIdTiersAdministration());
        if (application.isAnnoncesWA()) {
            nouvellePeriode.setTypeEnregistrement(CIPeriodeSplitting.CS_SPLITTING_MANUEL.substring(0, 3)
                    + application.getProperty(CIApplication.CODE_AGENCE).trim()
                    + CIPeriodeSplitting.CS_SPLITTING_MANUEL.substring(4));
        } else {
            nouvellePeriode.setTypeEnregistrement(CIPeriodeSplitting.CS_SPLITTING_MANUEL);
            // todo: ajout caisse tenant ci
            nouvellePeriode.setCaisseTenantCI(application.getProperty(CIApplication.CODE_AGENCE).trim());
        }
        nouvellePeriode.setPartenaireNumeroAvs(noAvsConjoint);
        nouvellePeriode.setAnneeDebut(getAnneeDebut());
        nouvellePeriode.setAnneeFin(getAnneeFin());
        nouvellePeriode.setParticulier(getIdGenreSplitting());
        nouvellePeriode.setIdJournal(getIdJournal());
        nouvellePeriode.add(transaction);
    }

    /**
     * Calcule la somme des tous les revenus et cotisations du mandat concerné. Date de création : (07.11.2002 16:44:45)
     * 
     * @return double le total
     * @param transaction
     *            la transaction à utiliser.
     */
    private String calculateRevenu(BTransaction transaction) throws Exception {
        // version avec BManager
        CIRevenuSplittingManager revenus = new CIRevenuSplittingManager();
        revenus.setSession(getSession());
        revenus.setForIdMandatSplitting(getIdMandatSplitting());
        BigDecimal result = revenus.getSum("KFMREV", transaction);
        if (result == null) {
            return "0.00";
        }
        return result.toString();
    }

    /**
     * Charge les informations nécessaire à lên-tête de la recherche.<br>
     * Effectué dans le bean puisque que la période et le total ne sont pas des attributs de l'objet métier. Date de
     * création : (29.10.2002 09:22:03)
     */
    public void chargeEntete() throws Exception {
        BTransaction transaction = new BTransaction(getSession());
        try {
            transaction.openTransaction();
            this.retrieve(transaction);
            // periode de mandat
            setPeriodeMandat(getAnneeDebut() + " - " + getAnneeFin());
            // total de contrôle
            setTotalRevenus(JANumberFormatter.format(calculateRevenu(transaction)));
            // nom du tiers
            try {
                CIApplication application = (CIApplication) getSession().getApplication();
                ITIPersonneAvs tiers = application.getTiersByAvs(transaction, getIdTiersPartenaire(),
                        new String[] { "getNom" });
                if (tiers.getNom() != null) {
                    setTiersPartenaireNomComplet(tiers.getNom());
                } else {
                    setTiersPartenaireNomComplet("");
                }
            } catch (Exception ex) {
                setTiersPartenaireNomComplet("");
            }
            if (transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            setPeriodeMandat("");
            setTotalRevenus("0.00");
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * Test si un mandat de même genre existe déjà dans cette période.
     * 
     * @param statement
     *            la requête actuelle
     * @throws Exception
     *             si une exception survient
     */
    private void checkChevauchement(BTransaction transaction) throws Exception {
        // même mandat pour la même année?
        CIMandatSplittingManager mdManager = new CIMandatSplittingManager();
        mdManager.setSession(getSession());
        mdManager.setForIdDossierSplitting(getIdDossierSplitting());
        mdManager.setForIdTiersPartenaire(getIdTiersPartenaire());
        mdManager.find(transaction);
        boolean normalFound = false;
        boolean renteFound = false;
        for (int i = 0; i < mdManager.size(); i++) {
            CIMandatSplitting mdt = (CIMandatSplitting) mdManager.getEntity(i);
            // test de l'ordre des genres de splitting
            if (!normalFound) {
                if (CIMandatSplitting.CS_PARTAGE_RAM.equals(getIdGenreSplitting())
                        || CIMandatSplitting.CS_PARTAGE_CI_CLOTURES.equals(getIdGenreSplitting())) {
                    renteFound = true;
                }
                if (CIMandatSplitting.CS_MANDAT_NORMAL.equals(getIdGenreSplitting())) {
                    normalFound = true;
                    if (renteFound) {
                        // mandat RAM ou revenus clôturés trouvé après mandat
                        // normal
                        _addError(transaction, getSession().getLabel("MSG_MANDAT_VAL_TYPE"));
                    }
                }
            }
            if (mdt.getIdGenreSplitting().equals(getIdGenreSplitting())
                    && !CIMandatSplitting.CS_REVOQUE.equals(mdt.getIdEtat())) {
                // même genre, tester période
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getAnneeDebut(), mdt.getAnneeDebut())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getAnneeDebut(), mdt.getAnneeFin())) {
                    // chevanchement date début
                    _addError(transaction, getSession().getLabel("MSG_MANDAT_VAL_CHEV"));
                    break;
                }
                if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getAnneeFin(), mdt.getAnneeDebut())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getAnneeFin(), mdt.getAnneeFin())) {
                    // chevanchement date fin
                    _addError(transaction, getSession().getLabel("MSG_MANDAT_VAL_CHEV"));
                    break;
                }
            }
        }
    }

    /**
     * Test si un domicile est défini dans cette période.
     * 
     * @param statement
     *            la requête actuelle
     * @throws Exception
     *             si une exception survient
     */
    private void checkChevauchementDomicile(BTransaction transaction) throws Exception {
        // même mandat pour la même année?
        CIDomicileSplittingManager domManager = new CIDomicileSplittingManager();
        domManager.setSession(getSession());
        domManager.setForIdDossierSplitting(getIdDossierSplitting());
        domManager.setForIdTiersPartenaire(getIdTiersPartenaire());
        domManager.find(transaction);
        for (int i = 0; i < domManager.size(); i++) {
            CIDomicileSplitting dom = (CIDomicileSplitting) domManager.getEntity(i);
            /*
             * if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getAnneeDebut(), dom.getDateDebut()) &&
             * BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getAnneeDebut(), dom.getDateFin())) { //
             * chevanchement date début _addError(transaction, getSession().getLabel("MSG_MANDAT_DOM_CHEV")); break; }
             * if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), getAnneeFin(), dom.getDateDebut()) &&
             * BSessionUtil.compareDateFirstLowerOrEqual(getSession(), getAnneeFin(), dom.getDateFin())) { //
             * chevanchement date fin _addError(transaction, getSession().getLabel("MSG_MANDAT_DOM_CHEV")); break; }
             */
            for (int ind = Integer.parseInt(getAnneeDebut()); ind <= Integer.parseInt(getAnneeFin()); ind++) {
                String annee = Integer.toString(ind);
                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dom.getDateDebut(), annee)
                        && BSessionUtil
                                .compareDateFirstGreaterOrEqual(getSession(), dom.getDateFin(), "31.12." + annee)) {
                    _addError(transaction, getSession().getLabel("MSG_MANDAT_DOM_CHEV"));
                    return;
                }
            }
        }
    }

    /**
     * Teste si des revenus existent et si le total correspond. Date de création : (08.11.2002 07:43:46)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return true si tous les tests ont passé.
     */
    private boolean checkRevenus(BTransaction transaction) throws Exception {
        CIRevenuSplittingManager revenus = loadRevenus(transaction);

        if (revenus.isEmpty()) {
            // pas de revenus: erreur
            transaction.addErrors(getSession().getLabel("MSG_MANDAT_SPLITTER_REV"));
        } else {
            // test le total de contrôle
            if (!calculateRevenu(transaction).equals(JANumberFormatter.deQuote(getMontant()))) {
                // le total des revenus et le total de contrôle ne correspondent
                // pas
                transaction.addErrors(getSession().getLabel("MSG_MANDAT_SPLITTER_TOT"));
            } else {
                return true;
            }
        }
        return false;
    }

    public java.lang.String getAnneeDebut() {
        return anneeDebut;
    }

    public java.lang.String getAnneeFin() {
        return anneeFin;
    }

    public String getDateNaissanceAss() {
        try {
            return dossier.getDateNaissanceAss();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDateNaissanceConj() {
        try {
            return dossier.getDateNaissanceConj();
        } catch (Exception e) {
            return "";
        }
    }

    public java.lang.String getDegreInvalidite() {
        return degreInvalidite;
    }

    public java.lang.String getIdArc() {
        return idArc;
    }

    public java.lang.String getIdDossierSplitting() {
        return idDossierSplitting;
    }

    public java.lang.String getIdEtat() {
        return idEtat;
    }

    public java.lang.String getIdGenreSplitting() {
        return idGenreSplitting;
    }

    public java.lang.String getIdJournal() {
        return idJournal;
    }

    /**
     * Getter
     */
    public java.lang.String getIdMandatSplitting() {
        return idMandatSplitting;
    }

    public java.lang.String getIdTiersPartenaire() {
        return idTiersPartenaire;
    }

    public java.lang.String getMontant() {
        return montant;
    }

    public String getPaysFormateAss() {
        try {
            return dossier.getPaysFormateAss();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaysFormateConj() {
        try {
            return dossier.getPaysFormateConj();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.10.2002 13:09:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPeriodeMandat() {
        return periodeMandat;
    }

    public String getSexeLibelleAss() {
        try {
            return dossier.getSexeLibelleAss();
        } catch (Exception e) {
            return "";
        }
    }

    public String getSexeLibelleConj() {
        try {
            return dossier.getSexeLibelleConj();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.12.2002 16:58:11)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTiersPartenaireNomComplet() {
        return tiersPartenaireNomComplet;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.10.2002 13:09:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalRevenus() {
        return totalRevenus;
    }

    /**
     * Indique si le mandat concerne l'assuré ou son conjoint. Date de création : (21.03.2003 09:13:45)
     * 
     * @return boolean
     */
    public boolean isAssure(CIDossierSplitting dossier) {
        if (dossier == null) {
            try {
                dossier = loadDossier(null);
            } catch (Exception ex) {
                return false;
            }
        }
        if (idTiersPartenaire.equals(dossier.getIdTiersAssure())) {
            return true;
        }
        return false;
    }

    /**
     * Indique si le mandat concerne l'assuré ou son conjoint. Date de création : (21.03.2003 09:13:45)
     * 
     * @return boolean
     */
    public boolean isConjoint(CIDossierSplitting dossier) {
        return !isAssure(dossier);
    }

    /**
     * Teste si le mandat est automatique ou manuel.<br>
     * Mandat automatique:
     * <ul>
     * <li>Mandat normal</li>
     * <li>Année jeunesse</li>
     * <li>Lacune cotisation jeunesse</li>
     * <li>Lacune cotisation d'appoint</li>
     * </ul>
     * Date de création : (04.11.2002 13:51:04)
     * 
     * @return true si le mandat est automatique.
     */
    public boolean isMandatAutomatique() {
        return CIMandatSplitting.CS_MANDAT_NORMAL.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_ANNEE_JEUNESSE.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT.equals(getIdGenreSplitting());
    }

    private boolean isMandatManuel() {
        if (CIMandatSplitting.CS_ANNEE_JEUNESSE.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_ANNEE_JEUNESSE_MAN.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT_MAN.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE.equals(getIdGenreSplitting())
                || CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE_MAN.equals(getIdGenreSplitting())) {
            return true;

        }
        return false;

    }

    /**
     * Teste si l'état actuel du mandat de splitting autorise une opération sur les revenus. Date de création :
     * (28.10.2002 16:39:58)
     * 
     * @return true si une modification est autorisée.
     */
    public boolean isModificationAllowedFromMandat() {
        return !CIMandatSplitting.CS_TERMINE.equals(getIdEtat());
    }

    /**
     * Teste si l'état actuel du dossier de splitting autorise une opération sur les domiciles à l'étranger. Date de
     * création : (28.10.2002 16:39:58)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return true si une modification est autorisée.
     * @exception Exception
     *                si le dossier ne peut pas être chrgé.
     */
    public boolean isOperationAllowed(BTransaction transaction) throws Exception {
        return loadDossier(transaction).isModificationAllowedFromDossier();
    }

    /**
     * Charge le CI du tiers donné. Date de création : (15.11.2002 08:39:41)
     * 
     * @return le CI du tiers.
     * @param idTiers
     *            le numéro avs du tiers.
     * @param transaction
     *            la transaction à utiliser.
     */
    private CICompteIndividuel loadCI(String idTiers, BTransaction transaction) throws Exception {
        CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
        ciManager.setSession(getSession());
        ciManager.orderByAvs(false);
        ciManager.setForNumeroAvs(idTiers);
        ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        ciManager.find(transaction);
        if (ciManager.size() == 0) {
            throw new CISplittingException(getSession().getLabel("MSG_MANDAT_SPLITTER_CI"));
        }
        return (CICompteIndividuel) ciManager.getEntity(0);
    }

    /**
     * Charge le dossier lié au mandat. Date de création : (31.10.2002 07:45:01)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return le dossier du mandat de splitting.
     * @exception Exception
     *                si le dossier ne peut pas être chrgé.
     */
    public CIDossierSplitting loadDossier(BTransaction transaction) throws Exception {
        // requête du dossier
        if (dossier == null) {
            dossier = new CIDossierSplitting();
            dossier.setIdDossierSplitting(getIdDossierSplitting());
            dossier.setSession(getSession());
            dossier.retrieve(transaction);
        }
        return dossier;
    }

    /**
     * Charge les revenus associés aux mandat. Date de création : (31.10.2002 07:45:01)
     * 
     * @param transaction
     *            la transaction à utiliser.
     * @return le manager contenant les revenus concernés.
     * @exception Exception
     *                si les revenus ne peuvent pas être chargés.
     */
    private CIRevenuSplittingManager loadRevenus(BTransaction transaction) throws Exception {
        CIRevenuSplittingManager revenus = new CIRevenuSplittingManager();
        revenus.setForIdMandatSplitting(getIdMandatSplitting());
        revenus.setSession(getSession());
        revenus.find(transaction);
        return revenus;
    }

    private boolean revoquePeriodeSplManuel(String compteIndividuelId, BTransaction transaction) throws Exception {
        boolean found = false;
        CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        CIPeriodeSplittingManager periodes = new CIPeriodeSplittingManager();
        periodes.setSession(getSession());
        periodes.setForCompteIndividuelId(compteIndividuelId);
        periodes.setForJournalId(getIdJournal());
        // periodes.setForPartenaireNumeroAvs(noAvsConjoint);
        periodes.find(transaction);
        for (int j = 0; j < periodes.size(); j++) {
            // modification des périodes
            CIPeriodeSplitting periode = (CIPeriodeSplitting) periodes.getEntity(j);
            if (JAUtil.isStringEmpty(periode.getDateRevocation())
                    && CIPeriodeSplitting.CS_SPLITTING_MANUEL.equals(periode.getTypeEnregistrementWA())) {
                periode.setDateRevocation(application.getCalendar().todayJJsMMsAAAA());
                periode.revoquerEcrituresSplitting(transaction);
                periode.wantCallMethodBefore(false);
                periode.update(transaction);
                found = true;
            }
        }
        return found;
    }

    /**
     * Révoque le mandat de splitting. Date de création : (15.10.2002 11:21:59)
     * 
     * @param transaction
     *            la transaction à utiliser
     */
    public void revoquer(BTransaction transaction, BITransaction remoteTransaction) throws Exception {
        // test état mandat
        if (!CIMandatSplitting.CS_TERMINE.equals(getIdEtat())) {
            transaction.addErrors(getSession().getLabel("MSG_MANDAT_REVOQUER"));
        } else {
            if (isMandatAutomatique()) {
                // mandats automatiques
                // ARC96 via Hermes
                CIApplication application = (CIApplication) getSession().getApplication();
                IHEInputAnnonce remoteAnnonceRevocation = (IHEInputAnnonce) remoteTransaction.getISession().getAPIFor(
                        IHEInputAnnonce.class);
                // attributs standard ARC
                remoteAnnonceRevocation.setIdProgramme(CIApplication.DEFAULT_APPLICATION_PAVO);
                try {
                    remoteAnnonceRevocation.setUtilisateur(dossier.getUser(transaction).getVisa());
                } catch (Exception e) {
                    remoteAnnonceRevocation.setUtilisateur(getSession().getUserId());
                }
                remoteAnnonceRevocation.setTypeLot(IHELotViewBean.TYPE_ENVOI);
                String service = "PAVO";
                if (!JadeStringUtil.isBlankOrZero(dossier.getReferenceService())) {
                    service = dossier.getReferenceService();
                }
                remoteAnnonceRevocation.put(
                        IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        service
                                + "/"
                                + DateUtils.convertDate(DateUtils.getCurrentDateAMJ(), DateUtils.AAAAMMJJ,
                                        DateUtils.JJMM_DOTS));
                remoteAnnonceRevocation.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
                remoteAnnonceRevocation.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                        application.getProperty(CIApplication.CODE_CAISSE));
                remoteAnnonceRevocation.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                        application.getProperty(CIApplication.CODE_AGENCE));
                remoteAnnonceRevocation.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
                remoteAnnonceRevocation.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "96");
                remoteAnnonceRevocation.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersPartenaire());
                remoteAnnonceRevocation.add(remoteTransaction);
                String idAnnonce = remoteAnnonceRevocation.getIdAnnonce();
                String idRef = remoteAnnonceRevocation.getRefUnique();
                if (idAnnonce != null) {
                    // recherche id conjoint
                    CIDossierSplitting dossier = loadDossier(transaction);
                    String conjoint;
                    if (dossier.getIdTiersAssure().equals(getIdTiersPartenaire())) {
                        conjoint = dossier.getIdTiersConjoint();
                    } else {
                        conjoint = dossier.getIdTiersAssure();
                    }
                    remoteAnnonceRevocation.setRefUnique(idRef);
                    remoteAnnonceRevocation.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "05");
                    remoteAnnonceRevocation.put(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE, conjoint);
                    // envoie de l'enregistrement 05
                    remoteAnnonceRevocation.add(remoteTransaction);
                    if (!remoteTransaction.hasErrors()) {
                        // cherche tous les mandats autom.
                        CIMandatSplittingManager mandats = new CIMandatSplittingManager();
                        mandats.setForIdDossierSplitting(getIdDossierSplitting());
                        mandats.setForIdTiersPartenaire(getIdTiersPartenaire());
                        mandats.setSession(getSession());
                        mandats.find(transaction);
                        for (int i = 0; i < mandats.size(); i++) {
                            CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
                            if (entity.isMandatAutomatique()) {
                                // assignation de l'id et de l'état du mandat
                                entity.setIdArc(idAnnonce);
                                entity.setIdEtat(CIMandatSplitting.CS_DEMANDE_REVOCATION);
                                entity.wantCallMethodBefore(false);
                                entity.update(transaction);
                            }
                        }
                    }
                }
                // retrieve pour l'affiche du mandat en cours
                this.retrieve(transaction);
            } else {
                CICompteIndividuel ciAssure = CICompteIndividuel.loadCI(getIdTiersPartenaire(), transaction);
                if (!CIMandatSplitting.CS_MANDAT_VIDE_POUR_ASSURE.equals(idGenreSplitting)) {
                    if (!revoquePeriodeSplManuel(ciAssure.getCompteIndividuelId(), transaction)) {
                        // suppression journal CI si pas déjà effectué
                        CIJournal journal = new CIJournal();
                        journal.setSession(getSession());
                        journal.setIdJournal(getIdJournal());
                        journal.retrieve(transaction);
                        journal.revoquerJournalSplitting(transaction);
                    }
                }
                setIdEtat(CIMandatSplitting.CS_REVOQUE);
                setIdJournal("");
                wantCallMethodBefore(false);
                this.update(transaction);
                wantCallMethodBefore(true);
            }
        }
    }

    /**
     * Révoque le mandat de splitting. A utiliser si l'action "révoquer mandat" vient de l'utilisateur (action du
     * browser) Date de création : (15.10.2002 11:21:59)
     */
    public void revoquerFromUserAction() throws Exception {
        if (isNew()) {
            // ouverture impossible si nouveau
            throw new CISplittingException(getSession().getLabel("MSG_MANDAT_REVOQUE_NOUVEAU"));
        }
        // test état dossier
        CIDossierSplitting _dossier = loadDossier(null);
        if (!CIDossierSplitting.CS_OUVERT.equals(_dossier.getIdEtat())) {
            throw new CISplittingException(getSession().getLabel("MSG_MANDAT_REVOQUER"));
        }
        BTransaction transaction = new BTransaction(getSession());
        BITransaction remoteTransaction = null;
        try {
            transaction.openTransaction();
            BISession remoteSession = ((CIApplication) getSession().getApplication()).getSessionAnnonce(getSession());
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            remoteTransaction.openTransaction();
            revoquer(transaction, remoteTransaction);
            if (transaction.isRollbackOnly() || remoteTransaction.isRollbackOnly()) {
                transaction.rollback();
                remoteTransaction.rollback();
            } else {
                transaction.commit();
                remoteTransaction.commit();
            }
        } finally {
            try {
                if (transaction != null) {
                    transaction.closeTransaction();
                }
            } finally {
                if (remoteTransaction != null) {
                    remoteTransaction.closeTransaction();
                }
            }
        }
    }

    public void setAnneeDebut(java.lang.String newAnneeDebut) {
        anneeDebut = newAnneeDebut;
    }

    public void setAnneeFin(java.lang.String newAnneeFin) {
        anneeFin = newAnneeFin;
    }

    public void setDegreInvalidite(java.lang.String newDegreInvalidite) {
        degreInvalidite = newDegreInvalidite;
    }

    public void setIdArc(java.lang.String newIdArc) {
        idArc = newIdArc;
    }

    public void setIdDossierSplitting(java.lang.String newIdDossierSplitting) {
        idDossierSplitting = newIdDossierSplitting;
    }

    public void setIdEtat(java.lang.String newIdEtat) {
        idEtat = newIdEtat;
    }

    public void setIdGenreSplitting(java.lang.String newIdGenreSplitting) {
        idGenreSplitting = newIdGenreSplitting;
    }

    public void setIdJournal(java.lang.String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Setter
     */
    public void setIdMandatSplitting(java.lang.String newIdMandatSplitting) {
        idMandatSplitting = newIdMandatSplitting;
    }

    public void setIdTiersPartenaire(java.lang.String newIdTiersPartenaire) {
        idTiersPartenaire = newIdTiersPartenaire;
    }

    public void setMontant(java.lang.String newMontant) {
        montant = newMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.10.2002 13:09:32)
     * 
     * @param newPeriodeMandat
     *            java.lang.String
     */
    public void setPeriodeMandat(java.lang.String newPeriodeMandat) {
        periodeMandat = newPeriodeMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.12.2002 16:58:11)
     * 
     * @param newTiersPartenaireNomComplet
     *            java.lang.String
     */
    public void setTiersPartenaireNomComplet(java.lang.String newTiersPartenaireNomComplet) {
        tiersPartenaireNomComplet = newTiersPartenaireNomComplet;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.10.2002 13:09:32)
     * 
     * @param newTotalRevenus
     *            java.lang.String
     */
    public void setTotalRevenus(java.lang.String newTotalRevenus) {
        totalRevenus = newTotalRevenus;
    }

    /**
     * Exécution du splitting. Date de création : (15.10.2002 11:21:14)
     * 
     * @param transaction
     *            la transaction à utilisé.
     * @param dossier
     *            le dossier de splitting
     */
    public void splitter(BTransaction transaction, BITransaction remoteTransaction, CIDossierSplitting dossier)
            throws Exception {
        // vérification
        if (!CIMandatSplitting.CS_OUVERT.equals(idEtat)) {
            // splitting refusé
            return;
        }
        boolean notSaved = true;
        // recherche id conjoint
        // CIDossierSplitting dossier = loadDossier(transaction);
        String conjoint;
        if (dossier.getIdTiersAssure().equals(getIdTiersPartenaire())) {
            conjoint = dossier.getIdTiersConjoint();
        } else {
            conjoint = dossier.getIdTiersAssure();
        }
        // recherche des CI
        CICompteIndividuel ciAssure = dossier.getContainer().getCI(getIdTiersPartenaire(), transaction);
        CICompteIndividuel ciConjoint = dossier.getContainer().getCI(conjoint, transaction);
        // splitting manuel, partage des revenus
        if (CIMandatSplitting.CS_MANDAT_VIDE_POUR_ASSURE.equals(idGenreSplitting)) {
            if (!transaction.hasErrors()) {
                setIdEtat(CIMandatSplitting.CS_TERMINE);
            }
        } else if (CIMandatSplitting.CS_PARTAGE_CI_CLOTURES.equals(idGenreSplitting)) {

            // test revenus
            if (checkRevenus(transaction)) {
                // création du journal
                CIJournal journal = new CIJournal();
                journal.setIdTypeInscription(CIJournal.CS_SPLITTING);
                journal.setIdTypeCompte(CIJournal.CS_CI);
                journal.setIdEtat(CIJournal.CS_COMPTABILISE);
                journal.setLibelle("Splitting " + NSUtil.formatAVSUnknown(getIdTiersPartenaire()));
                journal.setDateInscription(getSession().getApplication().getCalendar().todayJJsMMsAAAA());
                journal.add(transaction);
                // lien au mandat
                setIdJournal(journal.getIdJournal());
                // écriture pour chaque revenu
                CIRevenuSplittingManager revenus = loadRevenus(transaction);
                // ArrayList ecrSplAss = new ArrayList();
                // ArrayList ecrSplConj = new ArrayList();
                for (int i = 0; i < revenus.size(); i++) {
                    // effectuer les calculs
                    CIRevenuSplitting entity = (CIRevenuSplitting) revenus.getEntity(i);
                    double revenu;
                    if (!JAUtil.isIntegerEmpty(entity.getCotisation())) {
                        // cotisation saisie -> multiplication par 25
                        revenu = Double.parseDouble(entity.getCotisation()) * 25;
                    } else {
                        // revenu saisi
                        revenu = Double.parseDouble(entity.getRevenu());
                    }
                    // partie entière du revenu divisé par 2
                    revenu = Math.floor(revenu / 2);
                    CIEcriture ecritureAssure = new CIEcriture();
                    CIEcriture ecritureConjoint = new CIEcriture();
                    if (revenu > 0) {
                        ecritureAssure.setGre("185");
                        ecritureConjoint.setGre("085");
                    } else {
                        revenu = Math.abs(revenu);
                        ecritureAssure.setGre("085");
                        ecritureConjoint.setGre("185");
                    }
                    // id du CI et celui du conjoint
                    ecritureAssure.setCompteIndividuelId(ciAssure.getCompteIndividuelId());
                    ecritureAssure.setPartenaireId(ciConjoint.getCompteIndividuelId());
                    // idem pour conjoint (croisé)
                    ecritureConjoint.setCompteIndividuelId(ciConjoint.getCompteIndividuelId());
                    ecritureConjoint.setPartenaireId(ciAssure.getCompteIndividuelId());
                    // montant
                    ecritureAssure.setMontant(String.valueOf(revenu));
                    ecritureConjoint.setMontant(String.valueOf(revenu));
                    // année
                    ecritureAssure.setAnnee(entity.getAnnee());
                    ecritureConjoint.setAnnee(entity.getAnnee());
                    // id journal
                    ecritureAssure.setIdJournal(journal.getIdJournal());
                    ecritureConjoint.setIdJournal(journal.getIdJournal());
                    // id type compte
                    ecritureAssure.setIdTypeCompte(CIEcriture.CS_CI);
                    ecritureConjoint.setIdTypeCompte(CIEcriture.CS_CI);
                    // no avs pour passer validate() de CIEcriture
                    // ecritureAssure.setAvs(ciAssure.getNumeroAvs());
                    // ecritureAssure.setEmployeurPartenaire(ciConjoint.getNumeroAvs());
                    // ecritureConjoint.setAvs(ciConjoint.getNumeroAvs());
                    // ecritureConjoint.setEmployeurPartenaire(ciAssure.getNumeroAvs());
                    // sauve
                    ecritureAssure.simpleAdd(transaction);

                    dossier.getContainer().addEcriture(ecritureAssure);
                    ecritureConjoint.simpleAdd(transaction);

                    dossier.getContainer().addEcriture(ecritureConjoint);
                } // for
                  // test ci additionnel
                  // ciAssure.annonceCIAdditionnel(transaction,ecrSplAss);
                  // ciConjoint.annonceCIAdditionnel(transaction,ecrSplConj);
                  // maj journal
                journal.updateInscription(transaction);
                // journal.comptabiliser(null, null, transaction);
                // ajout de la période de splitting
                ajoutPeriodeSplManuel(ciAssure.getCompteIndividuelId(), ciConjoint.getNumeroAvs(), transaction);
                if (!transaction.hasErrors()) {
                    setIdEtat(CIMandatSplitting.CS_TERMINE);
                }
            }
        }
        // splitting manuel, partage des rams
        else if (CIMandatSplitting.CS_PARTAGE_RAM.equals(idGenreSplitting)) {
            // si montant et degreinvalidite non saisies -> erreur
            montant = JANumberFormatter.deQuote(montant);
            if (JAUtil.isIntegerEmpty(montant) || JAUtil.isIntegerEmpty(degreInvalidite)) {
                transaction.addErrors(getSession().getLabel("MSG_MANDAT_SPLITTER_RAM"));
            } else {
                // création du journal
                CIJournal journal = new CIJournal();
                journal.setIdTypeInscription(CIJournal.CS_SPLITTING);
                journal.setIdTypeCompte(CIJournal.CS_CI);
                journal.setIdEtat(CIJournal.CS_COMPTABILISE);
                journal.setDateInscription(JACalendar.todayJJsMMsAAAA());
                journal.setLibelle("Splitting " + NSUtil.formatAVSUnknown(getIdTiersPartenaire()));
                journal.add(transaction);
                // lien au mandat
                setIdJournal(journal.getIdJournal());
                // calcul du ram pour chaque année
                RenteMinimale rente = RenteMinimale.getRenteMinimale(getAnneeFin());
                double renteMinimale = rente.getRmAVS();
                if (renteMinimale == 1) {
                    // rente non trouvée dans la table
                    transaction.addErrors(getSession().getLabel("MSG_MANDAT_VAL_FICHIER"));
                } else {
                    double facteur = Double.parseDouble(montant) / renteMinimale;
                    long montant;
                    for (int i = Integer.parseInt(getAnneeFin()); i >= Integer.parseInt(getAnneeDebut()); i--) {
                        renteMinimale = RenteMinimale.getRenteMinimale(String.valueOf(i)).getRmAVS();
                        montant = Math.round(renteMinimale * facteur);
                        // écriture
                        CIEcriture ecritureConjoint = new CIEcriture();
                        ecritureConjoint.setGre("084");
                        // id du CI du conjoint
                        ecritureConjoint.setCompteIndividuelId(ciConjoint.getCompteIndividuelId());
                        ecritureConjoint.setPartenaireId(ciAssure.getCompteIndividuelId());
                        // montant
                        if (Double.parseDouble(getDegreInvalidite()) == 100) {
                            ecritureConjoint.setMontant(String.valueOf(Math.round(montant / 2)));
                        } else {
                            ecritureConjoint.setMontant(String.valueOf(Math.round(montant / 4)));
                        }
                        // année
                        ecritureConjoint.setAnnee(String.valueOf(i));
                        // id journal
                        ecritureConjoint.setIdJournal(journal.getId());
                        // id type compte
                        ecritureConjoint.setIdTypeCompte(CIEcriture.CS_CI);
                        // no avs pour check
                        // ecritureConjoint.setAvs(ciConjoint.getNumeroAvs());
                        // ecritureConjoint.setEmployeurPartenaire(ciAssure.getNumeroAvs());
                        // sauve
                        ecritureConjoint.simpleAdd(transaction);
                        dossier.getContainer().addEcriture(ecritureConjoint);
                        // calcule nouveau facteur
                        facteur = montant / renteMinimale;
                    } // for
                }
                // comptabiliser journal
                journal.updateInscription(transaction);
                // ajout de la période de splitting
                ajoutPeriodeSplManuel(ciAssure.getCompteIndividuelId(), ciConjoint.getNumeroAvs(), transaction);
                // journal.comptabiliser(null, null, transaction);
                if (!transaction.hasErrors()) {
                    setIdEtat(CIMandatSplitting.CS_TERMINE);
                }
            }
        }
        // splitting manuel, lichtenstein
        else if (CIMandatSplitting.CS_LICHTENSTEIN.equals(idGenreSplitting)) {
            // test revenus
            if (checkRevenus(transaction)) {
                // création du journal
                CIJournal journal = new CIJournal();
                journal.setIdTypeInscription(CIJournal.CS_SPLITTING);
                journal.setIdTypeCompte(CIJournal.CS_CI);
                journal.setIdEtat(CIJournal.CS_COMPTABILISE);
                journal.setLibelle("Splitting " + JAUtil.formatAvs(getIdTiersPartenaire()));
                journal.setDateInscription(JACalendar.todayJJsMMsAAAA());
                journal.add(transaction);
                // lien au mandat
                setIdJournal(journal.getIdJournal());
                // écriture pour chaque revenu
                CIRevenuSplittingManager revenus = loadRevenus(transaction);
                for (int i = 0; i < revenus.size(); i++) {
                    // effectuer les calculs
                    CIRevenuSplitting entity = (CIRevenuSplitting) revenus.getEntity(i);
                    double revenu;
                    if (!JAUtil.isIntegerEmpty(entity.getCotisation())) {
                        // cotisation saisie -> multiplication par 25
                        revenu = Double.parseDouble(entity.getCotisation()) * 25;
                    } else {
                        // revenu saisi
                        revenu = Double.parseDouble(entity.getRevenu());
                    }
                    // partie entière du revenu divisé par 2
                    revenu = Math.floor(revenu / 2);
                    CIEcriture ecritureConjoint = new CIEcriture();
                    if (revenu > 0) {
                        ecritureConjoint.setGre("085");
                    } else {
                        revenu = Math.abs(revenu);
                        ecritureConjoint.setGre("185");
                    }
                    // id du CI et celui du conjoint
                    ecritureConjoint.setCompteIndividuelId(ciConjoint.getCompteIndividuelId());
                    ecritureConjoint.setPartenaireId(ciAssure.getCompteIndividuelId());
                    // montant
                    ecritureConjoint.setMontant(String.valueOf(revenu));
                    // année
                    ecritureConjoint.setAnnee(entity.getAnnee());
                    // id journal
                    ecritureConjoint.setIdJournal(journal.getId());
                    // id type compte
                    ecritureConjoint.setIdTypeCompte(CIEcriture.CS_CI);
                    // no avs pour check
                    // ecritureConjoint.setAvs(ciConjoint.getNumeroAvs());
                    // ecritureConjoint.setEmployeurPartenaire(ciAssure.getNumeroAvs());
                    // sauve
                    ecritureConjoint.simpleAdd(transaction);
                    dossier.getContainer().addEcriture(ecritureConjoint);
                } // for
                  // comptabiliser journal.
                journal.updateInscription(transaction);
                // journal.comptabiliser(null, null, transaction);
                // ajout de la période de splitting
                ajoutPeriodeSplManuel(ciAssure.getCompteIndividuelId(), ciConjoint.getNumeroAvs(), transaction);
                if (!transaction.hasErrors()) {
                    setIdEtat(CIMandatSplitting.CS_TERMINE);
                }
            }
        } else if (CIMandatSplitting.CS_ANNEE_JEUNESSE_MAN.equals(idGenreSplitting)
                || CIMandatSplitting.CS_LACUNE_COTISATION_JEUNESSE_MAN.equals(idGenreSplitting)
                || CIMandatSplitting.CS_LACUNE_COTISATION_APPOINT_MAN.equals(idGenreSplitting)) {
            montant = JANumberFormatter.deQuote(montant);
            if (!JAUtil.isIntegerEmpty(montant)) {
                double revenu = Double.parseDouble(montant);
                // partie entière du revenu divisé par 2
                revenu = Math.floor(revenu / 2);
                // création du journal
                CIJournal journal = new CIJournal();
                journal.setIdTypeInscription(CIJournal.CS_SPLITTING);
                journal.setIdTypeCompte(CIJournal.CS_CI);
                journal.setIdEtat(CIJournal.CS_COMPTABILISE);
                journal.setLibelle("Splitting " + JAUtil.formatAvs(getIdTiersPartenaire()));
                journal.setDateInscription(getSession().getApplication().getCalendar().todayJJsMMsAAAA());
                journal.add(transaction);
                // lien au mandat
                setIdJournal(journal.getIdJournal());

                CIEcriture ecritureAssure = new CIEcriture();
                CIEcriture ecritureConjoint = new CIEcriture();
                String genreEcr = idGenreSplitting.substring(5);
                if (revenu >= 0) {
                    ecritureAssure.setGre("18" + genreEcr);
                    ecritureConjoint.setGre("08" + genreEcr);
                } else {
                    revenu = Math.abs(revenu);
                    ecritureAssure.setGre("08" + genreEcr);
                    ecritureConjoint.setGre("18" + genreEcr);
                }
                // id du CI et celui du conjoint
                ecritureAssure.setCompteIndividuelId(ciAssure.getCompteIndividuelId());
                ecritureAssure.setPartenaireId(ciConjoint.getCompteIndividuelId());
                // idem pour conjoint (croisé)
                ecritureConjoint.setCompteIndividuelId(ciConjoint.getCompteIndividuelId());
                ecritureConjoint.setPartenaireId(ciAssure.getCompteIndividuelId());
                // montant
                ecritureAssure.setMontant(String.valueOf(revenu));
                ecritureConjoint.setMontant(String.valueOf(revenu));
                // année
                ecritureAssure.setAnnee(getAnneeDebut());
                ecritureConjoint.setAnnee(getAnneeDebut());
                // id journal
                ecritureAssure.setIdJournal(journal.getIdJournal());
                ecritureConjoint.setIdJournal(journal.getIdJournal());
                // id type compte
                ecritureAssure.setIdTypeCompte(CIEcriture.CS_CI);
                ecritureConjoint.setIdTypeCompte(CIEcriture.CS_CI);
                // sauve
                ecritureAssure.simpleAdd(transaction);
                dossier.getContainer().addEcriture(ecritureAssure);
                ecritureConjoint.simpleAdd(transaction);
                dossier.getContainer().addEcriture(ecritureConjoint);
                // comptabiliser journal.
                journal.updateInscription(transaction);
                // journal.comptabiliser(null, null, transaction);
            }
            // ajout de la période de splitting
            ajoutPeriodeSplManuel(ciAssure.getCompteIndividuelId(), ciConjoint.getNumeroAvs(), transaction);
            if (!transaction.hasErrors()) {
                setIdEtat(CIMandatSplitting.CS_TERMINE);
            }
        }
        // splitting automatique
        // TODO enregistrement 04 pour les périodes à l'étranger (pas nécessaire
        // pour l'instant)
        else {
            // ARC95
            CIApplication application = (CIApplication) getSession().getApplication();
            IHEInputAnnonce remoteAnnonceSplitting = (IHEInputAnnonce) remoteTransaction.getISession().getAPIFor(
                    IHEInputAnnonce.class);
            // attributs standard ARC
            remoteAnnonceSplitting.setIdProgramme(CIApplication.DEFAULT_APPLICATION_PAVO);
            try {
                remoteAnnonceSplitting.setUtilisateur(dossier.getUser(transaction).getVisa());
            } catch (Exception e) {
                remoteAnnonceSplitting.setUtilisateur(getSession().getUserId());
            }
            remoteAnnonceSplitting.setTypeLot(IHELotViewBean.TYPE_ENVOI);
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.CODE_APPLICATION, "11");
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                    application.getProperty(CIApplication.CODE_CAISSE));
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                    application.getProperty(CIApplication.CODE_AGENCE));
            // ajout de attributs spécifique à l'annonce
            // remoteEcritureAnnonce.putAll(attributs);
            // remoteEcritureAnnonce.add(remoteTransaction);
            // if (!remoteTransaction.hasErrors()) {
            // idAnnonce = remoteEcritureAnnonce.getRefUnique();
            // }
            // HashMap attributs = new HashMap();
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, "95");
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.NUMERO_ASSURE, getIdTiersPartenaire());
            // ref interne
            String service = "PAVO";
            if (!JadeStringUtil.isBlankOrZero(dossier.getReferenceService())) {
                service = dossier.getReferenceService();
            }
            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                    service + "/" + loadDossier(transaction).getIdTiersAssure());
            remoteAnnonceSplitting.add(remoteTransaction);
            String idAnnonce = remoteAnnonceSplitting.getIdAnnonce();
            String idRef = remoteAnnonceSplitting.getRefUnique();
            // String idAnnonce = application.annonceARC(transaction,
            // remoteTransaction, attributs);
            if (!remoteTransaction.hasErrors()) {
                // annonce ok
                HashMap periodes = null;
                // recherche des mandats automatiques ouverts
                CIMandatSplittingManager mandats = new CIMandatSplittingManager();
                mandats.setForIdDossierSplitting(getIdDossierSplitting());
                mandats.setForIdTiersPartenaire(getIdTiersPartenaire());
                mandats.setSession(getSession());
                mandats.setOrderByPeriode();
                mandats.find(transaction);
                int modulo = 0;
                for (int i = 0; i < mandats.size(); i++) {
                    CIMandatSplitting entity = (CIMandatSplitting) mandats.getEntity(i);
                    if (entity.isMandatAutomatique() && CIMandatSplitting.CS_OUVERT.equals(entity.getIdEtat())) {
                        // attributs du 05
                        // l'annonce peut contenir 11 période au max
                        if (modulo % 11 == 0) {
                            // envoyer l'annonce (enregistrement 05) si elle
                            // existe
                            if (periodes != null) {
                                remoteAnnonceSplitting.putAll(periodes);
                                remoteAnnonceSplitting.add(remoteTransaction);
                            }
                            // nouvelle annonce
                            // Map des périodes de splitting (enregistrement 05)
                            periodes = new HashMap();
                            remoteAnnonceSplitting.setRefUnique(idRef);
                            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "05");
                            remoteAnnonceSplitting.put(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE,
                                    conjoint);
                        }
                        // définition du code particulier. Si 0 -> vide, sinon
                        // dernier chiffre de l'id du genre de splitting
                        char codeParticulier = ' ';
                        if (!CIMandatSplitting.CS_MANDAT_NORMAL.equals(entity.getIdGenreSplitting())) {
                            codeParticulier = entity.getIdGenreSplitting().charAt(
                                    entity.getIdGenreSplitting().length() - 1);
                        }
                        // chaîne contenant l'année de début, de fin et le code
                        // particulier à passer à HERMES
                        String triplet = entity.getAnneeDebut() + entity.getAnneeFin() + codeParticulier;
                        periodes.put(CIMandatSplitting.idAnnonces95[modulo], triplet);
                        // assignation de l'id et de l'état du mandat
                        entity.setIdArc(idAnnonce);
                        entity.setIdEtat(CIMandatSplitting.CS_SPLITTING_EN_COURS);
                        entity.wantCallMethodBefore(false);
                        entity.update(transaction);
                        modulo++;
                    }
                }
                // envoie du dernier enregistrement 05
                remoteAnnonceSplitting.putAll(periodes);
                remoteAnnonceSplitting.add(remoteTransaction);
                // remoteTransaction.commit();
                // annonce actuelle déjà sauvée dans la boucle for -> pas
                // d'update
                notSaved = false;
            }
        }
        if (notSaved) {
            wantCallMethodBefore(false);
            this.update(transaction);
            wantCallMethodBefore(true);
        }
    }

    /**
     * Mise à jour du mandat. Date de création : (15.10.2002 11:22:34)
     * 
     * @param transaction
     *            la transaction a utiliser.
     * @exception si
     *                une erreur survient.
     */
    public void updateMandat(BTransaction transaction) throws Exception {
        // traitement d'une révocation
        CIApplication application = (CIApplication) getSession().getApplication();
        BISession remoteSession = application.getSessionAnnonce(getSession());
        BITransaction remoteTransaction = ((BSession) remoteSession).newTransaction();
        try {
            remoteTransaction.openTransaction();
            IHEOutputAnnonce remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteLectureAnnonce.setMethodsToLoad(new String[] { "getIdAnnonce", "getAllAnnonceRetour",
                    "getInputTable", "getStatut" });
            remoteLectureAnnonce.setIdAnnonce(getIdArc());
            remoteLectureAnnonce.retrieve(remoteTransaction);
            if (CIMandatSplitting.CS_DEMANDE_REVOCATION.equals(getIdEtat())) {
                // révocation ok?
                if (remoteLectureAnnonce.getAllAnnonceRetour().booleanValue()) {
                    // si oui
                    setIdEtat(CIMandatSplitting.CS_REVOQUE);
                    // update(transaction);
                    // transaction.commit();
                }
            }
            // traitement d'un splitting
            if (CIMandatSplitting.CS_SPLITTING_EN_COURS.equals(getIdEtat())) {
                // spltting terminé?
                // if (remoteLectureAnnonce.getAllAnnonceRetour()) {
                if (IHEAnnoncesViewBean.CS_TERMINE.equals(remoteLectureAnnonce.getStatut())) {
                    setIdEtat(CIMandatSplitting.CS_TERMINE);
                    // update(transaction);
                    // transaction.commit();
                } else if (IHEAnnoncesViewBean.CS_PROBLEME.equals(remoteLectureAnnonce.getStatut())) {
                    // TDOD: enlever commentaire après ajouter du CS dans la
                    // base
                    setIdEtat(CIMandatSplitting.CS_ERREUR);
                    dossier.retrieve(transaction);
                    if (!CIDossierSplitting.CS_ERREUR.equals(dossier.getIdEtat())) {
                        // Envoyer une seule fois
                        dossier.setIdEtat(CIDossierSplitting.CS_ERREUR);
                        dossier.update(transaction);
                        JadeSmtpClient.getInstance().sendMail(
                                dossier.getEMailResponsable(transaction),
                                getSession().getLabel("ERREUR_SPLITTING_SUJET") + dossier.getIdDossierInterne() + " / "
                                        + dossier.getIdDossierSplitting(),
                                getSession().getLabel("ERREUR_SPLITTING_BODY"), null);

                    }
                }
            }
            this.update(transaction);
            if (remoteTransaction.isRollbackOnly()) {
                remoteTransaction.rollback();
            } else {
                remoteTransaction.commit();
            }
        } catch (Exception ex) {
            if (remoteTransaction != null) {
                remoteTransaction.rollback();
            }
            throw ex;
        } finally {
            if (remoteTransaction != null) {
                remoteTransaction.closeTransaction();
            }
        }
    }
}

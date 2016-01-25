package ch.globaz.common.FusionTiersMultiple;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.pyxis.api.ITIAvoirAdresse;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.ICommonConstantes;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.common.FusionTiersMultiple.exception.FusionTiersException;
import ch.globaz.common.FusionTiersMultiple.utils.MessageBundleUtil;

public class TIFusionTiersMultipleProcess extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class TiersInfo {
        private String dateNaissance;
        private String idAffilie;
        private String idTiersRentier;
        private String nomTiers;
        private String NSS;
        private String numAffilie;
        private String prenomTiers;
        private String remarque;
        private String resultFusion;

        public TiersInfo() {
        }

        // constructeur avec paramètres
        public TiersInfo(String idTiersAffilie, String idTiersRentier) {

            idAffilie = idTiersAffilie;
            this.idTiersRentier = idTiersRentier;
        }

        public String getDateNaissance() {
            return dateNaissance;
        }

        public String getidTiersAffilie() {
            return idAffilie;
        }

        public String getidTiersRentier() {
            return idTiersRentier;
        }

        public String getNom() {
            return nomTiers;
        }

        public String getNss() {
            return NSS;
        }

        public String getNumAffilie() {
            return numAffilie;
        }

        public String getPrenom() {
            return prenomTiers;
        }

        public String getRemarque() {
            return remarque;
        }

        public String getResultFusion() {
            return resultFusion;
        }

        public void setDateNaissance(String dateNaissance) {

            this.dateNaissance = dateNaissance;
        }

        public void setNom(String nom) {
            nomTiers = nom;
        }

        public void setNSS(String NSS) {
            this.NSS = NSS;
        }

        public void setNumAffilie(String numAffilie) {
            this.numAffilie = numAffilie;
        }

        public void setPrenom(String prenom) {

            prenomTiers = prenom;
        }

        public void setRemarque(String _remarque) {
            remarque = _remarque;
        }

        public void setResultFusion(String _resultFusion) {
            resultFusion = _resultFusion;
        }
    }

    public static final String TYPE_EXPLOITATION = "508021";

    private String datelimite = "";
    private String dateNaissance = "";
    // mode simulation
    private boolean isSimulation;
    private String langue = "";
    private String nom = "";

    // Constantes
    private String nomDoc = "";

    private String prenom = "";

    List<TiersInfo> tiersAControler = new ArrayList<TiersInfo>();

    // Constructeur
    public TIFusionTiersMultipleProcess() {
        super();
        isSimulation = true;
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws FusionTiersException, SQLException, Exception {

        langue = getSession().getIdLangueISO();

        TIListeTiersMultipleManager manager = new TIListeTiersMultipleManager();

        initManager(manager);

        if (manager.size() < 1) {
            JadeLogger.info(null, MessageBundleUtil.getMessage("FUSIONTIERS_AUCUN_TIERS_A_FUSIONNER", langue));
        }

        if (manager.size() >= 1) {
            setProgressScaleValue(manager.size());
            nomDoc = (MessageBundleUtil.getMessage("FUSIONTIERS_LISTE_TIERS_A_CONTROLER", langue));

            manager.setSession(getSession());

            fustionnerTiers(manager);

            createDocumentCsv();
        }

        return true;
    }

    // Méthode
    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        /**
         * sécurité supplémentaire ------------------------ mais on ne devrait pas se retrouver avec une adresse email
         * vide en effet, si l'email n'est pas renseigné getEMailAddress() prend l'email du parent ou à défaut, celui du
         * user connecté
         * 
         */
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("LISTE_EMPLOYEUR_RADIE_ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    /**
     * Cette méthode permet de définir les propriétés d'un tiersInfo
     * 
     * @param tiersInfo
     *            On prend en paramètre un objet TiersInfo dont on va par la suite modifier les paramètres
     * @param tiers
     *            : On y passe notre tiers affilié
     * @param tiersRentier
     *            : On y passe notre tiers Rentier pour récupérer son numéro NSS
     */
    private void addInfoTiers(TiersInfo tiersInfo, TIListeTiersMultiple tiers, TIListeTiersMultiple tiersRentier) {

        tiersInfo.setNom(tiers.getNom());
        tiersInfo.setPrenom(tiers.getPrenom());
        tiersInfo.setDateNaissance(tiers.getDateNaissance());
        tiersInfo.setNumAffilie(tiers.getNumAffilie());
        tiersInfo.setNSS(tiersRentier.getNss());
    }

    private void changerIdTiers(TIListeTiersMultiple tiersAffilie, TIListeTiersMultiple tiersRentier) throws Exception {

        TiersInfo infoTiers = new TiersInfo(tiersAffilie.getIdTiers(), tiersRentier.getIdTiers());
        // ajouter les informations du tiers
        if (JadeStringUtil.isBlankOrZero(infoTiers.getNom())) {
            addInfoTiers(infoTiers, tiersAffilie, tiersRentier);
        }

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) getSession().newTransaction();
            transaction.openTransaction();

            // création d'un spy pour la transaction
            String dbSpy = createSpy();

            // fusionner les adresses
            fusionnerAdresse(transaction, infoTiers, tiersAffilie, tiersRentier);
            fusionnerAdressePaiement(transaction, infoTiers, tiersAffilie, tiersRentier);

            // modifications avec requêtes de l'idTiers
            miseAjourIdTiersInCA(transaction, tiersAffilie, tiersRentier, dbSpy);
            modifIdTiersInCotPers(transaction, tiersAffilie, tiersRentier, dbSpy);
            modifIdTiersInSortiesCotPers(transaction, tiersAffilie, tiersRentier, dbSpy);
            modifIdTierInCommunicationFiscale(transaction, tiersAffilie, tiersRentier, dbSpy);
            miseAjourIdTiersInCE(transaction, tiersAffilie, tiersRentier, dbSpy);
            miseAjourTiersAffilié(transaction, tiersAffilie, dbSpy);
            modifIdTierInAffiliation(transaction, tiersAffilie, tiersRentier, dbSpy);

            infoTiers.setRemarque((MessageBundleUtil.getMessage("FUSIONTIERS_SUCCES_FUSION_TIERS", langue)));
            infoTiers.setResultFusion((MessageBundleUtil.getMessage("FUSIONTIERS_OK", langue)));

            if (isSimulation) {
                transaction.rollback();
            } else {
                transaction.commit();
            }

        } catch (JadePersistenceException e) {
            JadeLogger.error((MessageBundleUtil.getMessage("FUSIONTIERS_ECHEC_FUSION_TIERS_WITH", langue)) + " "
                    + tiersAffilie + (MessageBundleUtil.getMessage("FUSIONTIERS_WITH_TIERS", langue)) + " "
                    + tiersRentier, e);
            infoTiers.setRemarque((MessageBundleUtil.getMessage("FUSIONTIERS_ECHEC_FUSION_TIERS", langue)) + " " + e);
            infoTiers.setResultFusion((MessageBundleUtil.getMessage("FUSIONTIERS_KO", langue)));

        } catch (FusionTiersException e) {
            JadeLogger.error((MessageBundleUtil.getMessage("FUSIONTIERS_ECHEC_FUSION_TIERS_WITH", langue)) + " "
                    + tiersAffilie + (MessageBundleUtil.getMessage("FUSIONTIERS_WITH_TIERS", langue)) + " "
                    + tiersRentier, e);

            infoTiers.setRemarque((MessageBundleUtil.getMessage("FUSIONTIERS_ECHEC_FUSION_TIERS", langue)) + " " + e);
            infoTiers.setResultFusion((MessageBundleUtil.getMessage("FUSIONTIERS_KO", langue)));

        } finally {
            // On ferme la transaction
            if ((transaction != null) && transaction.isOpened()) {
                transaction.clearErrorBuffer();
                transaction.clearWarningBuffer();
                transaction.closeTransaction();
            }
            tiersAControler.add(infoTiers);
        }
    }

    private void createDocumentCsv() throws Exception {
        TIFusionTiersMultipleAControlerCSVFile doc = new TIFusionTiersMultipleAControlerCSVFile();
        doc.populateSheet(this, tiersAControler);
        doc.setFilename(nomDoc);

        // Publication du document
        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setApplicationDomain(TIApplication.DEFAULT_APPLICATION_PYXIS);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentTypeNumber("NUMINFOROM"); // TODO numéro inforom
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        this.registerAttachedDocument(docInfo, doc.getOutputFile());

    }

    private String createSpy() {
        Date actualDate = new Date();
        String date = JadeDateUtil.getDMYDate(actualDate);
        String time = JadeDateUtil.getHMTime(actualDate);
        String dbSpy = date + time + "Fusion";

        return dbSpy;
    }

    private void fusionnerAdresse(BTransaction transaction, TiersInfo infoTiers, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier) throws FusionTiersException {
        try {

            if (tiersAffilie.getDateNaissance().equals("19800101")) {
                addInfoTiers(infoTiers, tiersAffilie, tiersRentier);
            } else {
                TITiers tiersAff = new TITiers();
                try {
                    tiersAff.setSession(getSession());
                    tiersAff.setIdTiers(tiersAffilie.getIdTiers());
                    tiersAff.retrieve(transaction);
                } catch (Exception e) {
                    throw new FusionTiersException((MessageBundleUtil.getMessage(
                            "FUSIONTIERS_ERROR_UNABLE_TO_FIND_TIERS", langue)) + " " + tiersAffilie.getIdTiers() + ")",
                            e);
                }
                TITiers tiersRen = new TITiers();
                try {
                    tiersRen.setSession(getSession());
                    tiersRen.setIdTiers(tiersRentier.getIdTiers());
                    tiersRen.retrieve(transaction);
                } catch (Exception e) {
                    throw new FusionTiersException((MessageBundleUtil.getMessage(
                            "FUSIONTIERS_ERROR_UNABLE_TO_FIND_TIERS", langue)) + " " + tiersRentier.getIdTiers() + ")",
                            e);
                }
                if (!tiersAff.isNew()) {
                    // Le tiers affilié possède une adresse d'exploitation
                    TIAvoirAdresse adresseExploitationAff = tiersAff.getAvoirAdresse(
                            TIFusionTiersMultipleProcess.TYPE_EXPLOITATION, IConstantes.CS_APPLICATION_DEFAUT,
                            JACalendar.todayJJsMMsAAAA());
                    if (adresseExploitationAff != null) {
                        if (!JadeStringUtil.isBlank(tiersRen.getAdresseAsString(
                                TIFusionTiersMultipleProcess.TYPE_EXPLOITATION, false))) {
                            addInfoTiers(infoTiers, tiersAffilie, tiersRentier);
                        } else {
                            adresseExploitationAff.setIdTiers(tiersRentier.getIdTiers());
                            adresseExploitationAff.setId("");
                            adresseExploitationAff.add(transaction);
                        }

                    }

                    TIAvoirAdresse adresseCourrierAff = tiersAff.getAvoirAdresse(ITIAvoirAdresse.CS_COURRIER,
                            IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA());
                    if (adresseCourrierAff != null) {
                        if (!JadeStringUtil.isBlank(tiersRen.getAdresseAsString(ITIAvoirAdresse.CS_COURRIER, true))) {
                            if (JadeStringUtil.isBlank(tiersRen.getAdresseAsString(ITIAvoirAdresse.CS_COURRIER,
                                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), false))) {
                                adresseCourrierAff.setIdTiers(tiersRentier.getIdTiers());
                                adresseCourrierAff.setIdApplication(ICommonConstantes.CS_APPLICATION_COTISATION);
                                adresseCourrierAff.setId("");
                                adresseCourrierAff.add(transaction);
                            } else {
                                addInfoTiers(infoTiers, tiersAffilie, tiersRentier);
                            }
                        } else {
                            adresseCourrierAff.setIdTiers(tiersRentier.getIdTiers());
                            adresseCourrierAff.setId("");
                            adresseCourrierAff.add(transaction);
                            addInfoTiers(infoTiers, tiersAffilie, tiersRentier);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new FusionTiersException((MessageBundleUtil.getMessage(
                    "FUSIONTIERS_IMPOSSIBLE_DE_FUSIONNER_ADRESSE_TIERS", langue)), e);
        }
    }

    private void fusionnerAdressePaiement(BTransaction transaction, TiersInfo infoTiers,
            TIListeTiersMultiple tiersAffilie, TIListeTiersMultiple tiersRentier) throws FusionTiersException {
        if (tiersAffilie.getDateNaissance().equals("19800101")) {
            addInfoTiers(infoTiers, tiersAffilie, tiersRentier);
        } else {
            TITiers tiersAff = new TITiers();
            try {
                tiersAff.setSession(getSession());
                tiersAff.setIdTiers(tiersAffilie.getIdTiers());
                tiersAff.retrieve(transaction);
            } catch (Exception e) {
                throw new FusionTiersException((MessageBundleUtil.getMessage("FUSIONTIERS_ERROR_UNABLE_TO_FIND_TIERS",
                        langue)) + " " + tiersAffilie.getIdTiers() + ")", e);
            }
            TITiers tiersRen = new TITiers();
            try {
                tiersRen.setSession(getSession());
                tiersRen.setIdTiers(tiersRentier.getIdTiers());
                tiersRen.retrieve(transaction);
            } catch (Exception e) {
                throw new FusionTiersException((MessageBundleUtil.getMessage("FUSIONTIERS_ERROR_UNABLE_TO_FIND_TIERS",
                        langue)) + " " + tiersRentier.getIdTiers() + ")", e);
            }
            if (!tiersAff.isNew()) {

                try {
                    TIAdressePaiementData adressePaiementDataAffilie = new TIAdressePaiementData();
                    adressePaiementDataAffilie = TITiers.getAdressePaiementData(IConstantes.CS_APPLICATION_DEFAUT,
                            JACalendar.todayJJsMMsAAAA(), tiersAff.getIdTiers(), null, getSession());
                    TIAdressePaiementData adressePaiementDataRente = new TIAdressePaiementData();
                    adressePaiementDataRente = TITiers.getAdressePaiementData(IConstantes.CS_APPLICATION_DEFAUT,
                            JACalendar.todayJJsMMsAAAA(), tiersRen.getIdTiers(), null, getSession());

                    if ((adressePaiementDataAffilie != null) && (adressePaiementDataRente != null)) {
                        if (!JadeStringUtil.isBlankOrZero(adressePaiementDataAffilie.getCcp())
                                && !adressePaiementDataAffilie.getCcp().equals(adressePaiementDataRente.getCcp())) {
                            TIAvoirPaiement adressePaiementAffilie = new TIAvoirPaiement();
                            adressePaiementAffilie.setSession(getSession());
                            adressePaiementAffilie.setIdAdressePaiement(adressePaiementDataAffilie
                                    .getIdAdressePaiement());
                            adressePaiementAffilie.retrieve(transaction);

                            adressePaiementAffilie.setIdTiers(tiersRen.getIdTiers());
                            adressePaiementAffilie.setIdApplication(FAEnteteFacture.CS_REMBOURSEMENT_COTI);
                            adressePaiementAffilie.setId("");

                            adressePaiementAffilie.add(transaction);
                        }
                    } else if ((adressePaiementDataRente == null) && (adressePaiementDataAffilie != null)) {
                        TIAvoirPaiement adressePaiementAffilie = new TIAvoirPaiement();
                        adressePaiementAffilie.setSession(getSession());
                        adressePaiementAffilie.setIdAdressePaiement(adressePaiementDataAffilie.getIdAdressePaiement());
                        adressePaiementAffilie.retrieve(transaction);

                        adressePaiementAffilie.setIdTiers(tiersRen.getIdTiers());
                        adressePaiementAffilie.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                        adressePaiementAffilie.setId("");
                        adressePaiementAffilie.add(transaction);

                        addInfoTiers(infoTiers, tiersAffilie, tiersRentier);

                    }
                } catch (Exception e) {
                    throw new FusionTiersException((MessageBundleUtil.getMessage(
                            "FUSIONTIERS_ERROR_UNABLE_TO_FIND_TIERS", langue)) + " " + tiersAffilie.getIdTiers() + ")",
                            e);
                }
            }
        }
    }

    private void fustionnerTiers(TIListeTiersMultipleManager manager) throws Exception {

        TIListeTiersMultiple entityTiers = new TIListeTiersMultiple();
        TIListeTiersMultiple tiersPrecedent = new TIListeTiersMultiple();

        for (int i = 0; (i < manager.size()) && !isAborted(); i++) {

            entityTiers = (TIListeTiersMultiple) manager.getEntity(i);

            if (!tiersPrecedent.isNew()) {
                if (tiersPrecedent.getNom().equals(entityTiers.getNom())
                        && tiersPrecedent.getPrenom().equals(entityTiers.getPrenom())
                        && tiersPrecedent.getDateNaissance().equals(entityTiers.getDateNaissance())
                        && !tiersPrecedent.getIdTiers().equals(entityTiers.getIdTiers())) {
                    if (!JadeStringUtil.isEmpty(tiersPrecedent.getNss())
                            && !JadeStringUtil.isEmpty(entityTiers.getNumAffilie())) {
                        if (JadeStringUtil.isBlankOrZero(entityTiers.getDateRadiation())
                                || BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                                        entityTiers.getDateRadiation(), getDatelimite())) {
                            if (!JadeStringUtil.isBlankOrZero(tiersPrecedent.getNbrPrestation())) {

                                changerIdTiers(entityTiers, tiersPrecedent);

                            }
                        }
                    } else if (!JadeStringUtil.isEmpty(entityTiers.getNss())
                            && !JadeStringUtil.isEmpty(tiersPrecedent.getNumAffilie())) {

                        if (JadeStringUtil.isBlankOrZero(tiersPrecedent.getDateRadiation())
                                || BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                                        tiersPrecedent.getDateRadiation(), getDatelimite())) {
                            if (!JadeStringUtil.isBlankOrZero(entityTiers.getNbrPrestation())) {
                                changerIdTiers(tiersPrecedent, entityTiers);
                            }
                        }
                    }
                }

            }

            tiersPrecedent = entityTiers;
            incProgressCounter();
        }
    }

    public String getDatelimite() {
        return datelimite;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        if (isAborted()) {
            return (MessageBundleUtil.getMessage("FUSIONTIERS_PROCESSUS_ABORTED", langue));
        } else if (getSession().hasErrors()) {
            return (MessageBundleUtil.getMessage("FUSIONTIERS_A_RENCONTRE_ERREURS", langue));
        } else {
            return (MessageBundleUtil.getMessage("FUSIONTIERS_SUCCES_FUSION_DES_TIERS", langue));
        }
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    private void initManager(TIListeTiersMultipleManager manager) throws Exception {
        manager.setSession(getSession());
        // au cas par cas
        manager.setForNomTiers(getNom());
        manager.setForPrenomTiers(getPrenom());
        manager.setForDateNaissance(getDateNaissance());
        manager.find(BManager.SIZE_NOLIMIT);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Cette méthode permet de rechercher les comptes annexes du tiers et de remplacer l'id du tiers affilié par l'id du
     * tiers rentes
     * 
     * @param tiersAffilie
     * @param tiersRentier
     * @param transaction
     *            : la transaction à gérer
     * @throws Exception
     */
    private void miseAjourIdTiersInCA(BTransaction transaction, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier, String dbSpy) throws JadePersistenceException {

        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection())
                    .append("CACPTAP SET IDTIERS =  " + tiersRentier.getIdTiers()).append(", PSPY = '" + dbSpy + "'")
                    .append(" WHERE IDTIERS =  " + tiersAffilie.getIdTiers());
            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();
        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage("FUSIONTIERS_ECHEC_MAJ_TIERS_IN_CA",
                    langue + " ")) + req.toString(), e);
        }

    }

    /**
     * Cette méthode permet de modifier l'id tiers affilié dans le contrôle d'employeur
     * 
     * @param tiersAffilie
     * @param tiersRentier
     * @param transaction
     *            : la transaction à gérer
     * @throws JadePersistenceException
     */
    private void miseAjourIdTiersInCE(BTransaction transaction, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier, String dbSpy) throws JadePersistenceException, Exception {
        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection())
                    .append("CECONTP SET HTITIE =  " + tiersRentier.getIdTiers()).append(", PSPY = '" + dbSpy + "'")
                    .append(" WHERE HTITIE =  " + tiersAffilie.getIdTiers());
            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();
        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage("FUSIONTIERS_ECHEC_MAJ_TIERS_IN_CE",
                    langue + " ")) + req.toString(), e);
        }
    }

    /**
     * Cette méthode permet de mettre àjour le tiers affilié à inactif
     * 
     * @param transaction
     * @param tiersAffilie
     * @throws Exception
     */
    private void miseAjourTiersAffilié(BTransaction transaction, TIListeTiersMultiple tiersAffilie, String dbSpy)
            throws JadePersistenceException {

        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection()).append("TITIERP SET HTINAC = " + "'" + 1 + "'")
                    .append(", PSPY = '" + dbSpy + "'").append(" WHERE HTITIE = " + tiersAffilie.getIdTiers());

            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();

        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage(
                    "FUSIONTIERS_ECHEC_MAJ_TIERS_IN_AFFILIATION_INACTIF", langue + " ")) + req.toString(), e);
        }
    }

    /**
     * Cette méthode permet de modifier l'id du tiers dans son/ses affiliation(s) respective(s)
     * 
     * @param transaction
     *            : gérer la transaction
     * @param tiersAffilie
     * @param tiersRentier
     * @throws FusionTiersException
     * @throws Exception
     */
    private void modifIdTierInAffiliation(BTransaction transaction, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier, String dbSpy) throws JadePersistenceException {

        // on créer un preparedStatement pour modifier l'id du tiers
        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection())
                    .append("AFAFFIP SET HTITIE = " + tiersRentier.getIdTiers()).append(", PSPY = '" + dbSpy + "'")
                    .append(" WHERE HTITIE = " + tiersAffilie.getIdTiers());

            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();

        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage(
                    "FUSIONTIERS_ECHEC_MAJ_TIERS_IN_AFFILIATION", langue + " ")) + req.toString(), e);
        }

    }

    /**
     * Cette méthode permet de modifier l'idtiers en affilé en idrentiers dans les communications fiscales
     * 
     * @param transaction
     *            : la transaction à gérer
     * @param tiersAffilie
     * @param tiersRentier
     * @param dbSpy
     *            : MàJ du spy dans la DB
     * @throws JadePersistenceException
     * @throws FusionTiersException
     * @throws Exception
     */
    private void modifIdTierInCommunicationFiscale(BTransaction transaction, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier, String dbSpy) throws JadePersistenceException, FusionTiersException {

        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection())
                    .append("CPCOFIP SET HTITIE =  " + tiersRentier.getIdTiers()).append(", PSPY = '" + dbSpy + "'")
                    .append(" WHERE HTITIE =  " + tiersAffilie.getIdTiers());
            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();
        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage(
                    "FUSIONTIERS_ECHEC_MAJ_TIERS_IN_COMMUNICATION_FISCALES", langue + " ")) + req.toString(), e);
        }
    }

    /**
     * Cette méthode permet de remplacer l'id du tiers affilié par l'id tiers rentier dans les cotisations personnelles
     * du tiers
     * 
     * @param transaction
     *            : permet de gérer la transaction
     * @param tiersAffilie
     * @param tiersRentier
     * @param dbSpy
     *            : spy pour la DB
     * @throws Exception
     */
    private void modifIdTiersInCotPers(BTransaction transaction, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier, String dbSpy) throws Exception {

        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection())
                    .append("CPDECIP SET HTITIE =  " + tiersRentier.getIdTiers()).append(", PSPY = '" + dbSpy + "'")
                    .append(" WHERE HTITIE =  " + tiersAffilie.getIdTiers());
            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();
        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage(
                    "FUSIONTIERS_ECHEC_MAJ_TIERS_IN_COTISATIONS_PERSONNELLES", langue + " ")) + req.toString(), e);
        }
    }

    /**
     * Cette méthode permet de modifier l'id du tiers affilié en tiers rentier dans les sorties de cotisations
     * personnelles du tiers
     * 
     * @param transaction
     *            : gérer la transaction
     * @param tiersAffilie
     * @param tiersRentier
     * @param dbSpy
     *            : spy pour la DB
     * @throws Exception
     */
    private void modifIdTiersInSortiesCotPers(BTransaction transaction, TIListeTiersMultiple tiersAffilie,
            TIListeTiersMultiple tiersRentier, String dbSpy) throws Exception {

        PreparedStatement pstmt = null;
        StringBuilder req = new StringBuilder();
        try {
            req.append("UPDATE ").append(tiersAffilie.getCollection())
                    .append("CPSORTP SET HTITIE =  " + tiersRentier.getIdTiers()).append(", PSPY = '" + dbSpy + "'")
                    .append(" WHERE HTITIE =  " + tiersAffilie.getIdTiers());
            pstmt = transaction.getConnection().prepareStatement(req.toString());
            pstmt.execute();
        } catch (SQLException e) {
            throw new JadePersistenceException((MessageBundleUtil.getMessage(
                    "FUSIONTIERS_ECHEC_MAJ_TIERS_IN_SORTIES_COTISATIONS_PERSONNELLES", langue + " ")) + req.toString(),
                    e);
        }
    }

    public void setDatelimite(String datelimite) {
        this.datelimite = datelimite;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}

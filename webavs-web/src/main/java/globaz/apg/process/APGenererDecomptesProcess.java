/*
 * Créé le 28 juin 05
 */
package globaz.apg.process;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.lots.APLot;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.itext.decompte.APDecompteGenerationProcess;
import globaz.apg.properties.APProperties;
import globaz.apg.properties.APPropertyTypeDePrestationAcmValues;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.common.properties.CommonPropertiesUtils;

/**
 * <H1>Process effectuant la logique de génération de communications.</H1>
 * <p>
 * Si communication définitive, effectue les opérations suivante :
 * </p>
 * <ul>
 * <li>Mise à jour du lot en état définitif</li>
 * <li>Impression de la communication</li>
 * <li>Mise à jour des prestations à l'état définitif. Si une annonce est en type restitution, passe toutes les annonces
 * dont l'idRestitution = id de cette prestation en ANNULER et passe cette même prestation en ANNULER</li>
 * <li>Mise à jour de l'état des droits. Si un droit a toutes ses prestations en DEFINITIF, alors il passe en DEFINITIF,
 * sinon en PARTIEL</li>
 * <li>Lance le process de génération d'annonce pour les prestations du lot choisi</li>
 * <li>Lance le process d'inscription aux CI pour les répartitions des prestations du lot choisi</li>
 * <li>Ecritures comptable</li>
 * </ul>
 * <p>
 * Si Communication provisoire, ne fait qu'imprimer la communication
 * </p>
 * 
 * @author dvh
 */
public class APGenererDecomptesProcess extends BProcess {

    private static final long serialVersionUID = 1L;
    private String dateSurDocument = "";
    private String dateValeurComptable = "";
    private String descriptionLot = "";
    private String emailObject = "";
    private String idLot = "";
    private Boolean isDefinitif = null;
    private Boolean isSendToGed = null;

    public APGenererDecomptesProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererDecomptesProcess.
     * 
     * @param parent
     *            DOCUMENT ME!
     */
    public APGenererDecomptesProcess(final BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererDecomptesProcess.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APGenererDecomptesProcess(final BSession session) {
        super(session);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _executeProcess() {

        boolean succes = false;
        boolean envoiAnnoncesSuccess = false;
        final BSession session = getSession();
        final BTransaction transaction = getTransaction();

        try {
            try {
                _validate();

                if (getTransaction().hasErrors() || getSession().hasErrors()) {
                    succes = false;
                    throw new Exception("Validation failed !!!");
                }

                getMemoryLog().setTransaction(transaction);

                if (isDefinitif.booleanValue()) {

                    genereAnnonces(transaction);
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("ERROR_GENERATION_DECOMPTES_ANNONCES"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_ANNONCES"),
                            FWMessage.INFORMATION, "");
                    doInscriptionsCI(transaction);
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("ERROR_GENERATION_DECOMPTES_CI"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_CI"), FWMessage.INFORMATION,
                            "");
                    final Set<String> idsDroit = metAJourPrestations(transaction, session);
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("ERROR_GENERATION_DECOMPTES_M_A_J_PRESTATIONS"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_M_A_J_PRESTATIONS"),
                            FWMessage.INFORMATION, "");
                    metAJourDroits(idsDroit, transaction, session);
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("ERROR_GENERATION_DECOMPTES_M_A_J_DROIT"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_M_A_J_DROIT"),
                            FWMessage.INFORMATION, "");
                    valideLot(transaction);
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel("ERROR_GENERATION_DECOMPTES_VALIDATION_LOT"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_M_A_J_LOT"),
                            FWMessage.INFORMATION, "");
                    imprimerCommunication();
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel(
                                "ERROR_GENERATION_DECOMPTES_IMPRESSION_COMMUNIQUATION"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_IMPRESSION_COMMUNIQUATION"),
                            FWMessage.INFORMATION, "");

                    // Les ecritures comptable sont faites en dernier car on commit
                    // les ecritures dans ce process.
                    // Comme cela on est sur de gerer de mainiere atimique toutes
                    // les operations
                    // point ouvert 00540
                    doEcrituresComptables(transaction);
                    if (getMemoryLog().hasErrors()) {
                        System.out.println(getMemoryLog().getTransaction().getErrors());
                        throw new Exception(getSession().getLabel("ERROR_GENERATION_DECOMPTES_ECRITURES_COMPTABLES"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_ECRITURES_COMPTABLES"),
                            FWMessage.INFORMATION, "");
                } else {
                    imprimerCommunication();
                    if (getMemoryLog().hasErrors()) {
                        throw new Exception(getSession().getLabel(
                                "ERROR_GENERATION_DECOMPTES_IMPRESSION_COMMUNIQUATION"));
                    }

                    getMemoryLog().logMessage(getSession().getLabel("GENERATION_DECOMPTES_IMPRESSION_COMMUNIQUATION"),
                            FWMessage.INFORMATION, "");
                }
                if (transaction.hasErrors() || getSession().hasErrors()) {
                    throw new Exception();
                }
                transaction.commit();
                succes = true;
            } catch (final Exception e) {

                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());

                if (transaction.hasErrors()) {
                    getMemoryLog().logMessage(transaction.getErrors().toString(), FWMessage.ERREUR,
                            this.getClass().toString());
                }
                if (getSession().hasErrors()) {
                    getMemoryLog().logMessage(session.getErrors().toString(), FWMessage.ERREUR,
                            this.getClass().toString());
                }

                try {
                    transaction.rollback();
                } catch (final Exception e1) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                }
                return false;
            }
            try {
                // Faire l'envoi des annonces (dans une transaction séparée, si ça plante, on fera l'envoi manuellement
                if (isDefinitif.booleanValue()) {
                    if ("true"
                            .equalsIgnoreCase(GlobazSystem.getApplication("APG").getProperty("rapg.envoiAutomatique"))) {
                        envoyerAnnonces(transaction);

                        envoiAnnoncesSuccess = !getMemoryLog().hasErrors();

                        transaction.commit();
                        if (envoiAnnoncesSuccess) {
                            getMemoryLog().logMessage(getSession().getLabel("GENERATION_ENVOI_ANNONCES"),
                                    FWMessage.INFORMATION, "");
                        } else {
                            getMemoryLog().logMessage(getSession().getLabel("ERROR_GENERATION_ENVOI_ANNONCES"),
                                    FWMessage.INFORMATION, "");
                        }
                    } else {
                        // Pour pas qu'on se retrouve en partiel vu qu'on envoi pas les annonces
                        envoiAnnoncesSuccess = true;
                    }
                } else {
                    // Pour pas qu'on se retrouve en partiel vu qu'on envoi pas les annonces
                    envoiAnnoncesSuccess = true;
                }

            } catch (final Exception e) {

                getMemoryLog().logMessage(getSession().getLabel("ERROR_GENERATION_ENVOI_ANNONCES"),
                        FWMessage.INFORMATION, "");

                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());

                try {
                    transaction.rollback();
                } catch (final Exception e1) {
                    getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
                }
                return true;
            }
        } finally {

            String status = "";
            if (succes) {
                if (envoiAnnoncesSuccess) {
                    status = "SUCCESS";
                } else {
                    status = "PARTIEL";
                }
            } else {
                status = "ERROR";
            }

            emailObject = FWMessageFormat.format(getSession().getLabel("GENERATION_DECOMPTES_EMAIL_OBJECT"), status,
                    getIdLot());

            try {
                transaction.closeTransaction();
            } catch (final Exception e1) {
                e1.printStackTrace();
            }

        }
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        super._validate();

        final APLot lot = new APLot();
        lot.setSession(getSession());
        lot.setIdLot(getIdLot());
        lot.retrieve(getTransaction());
        if (lot.isNew()) {
            this._addError(getTransaction(), "Erreur : lot non renseigné !!!");
        } else if (!IAPLot.CS_COMPENSE.equals(lot.getEtat()) && getIsDefinitif().booleanValue()) {
            this._addError(getTransaction(), getSession().getLabel("GENERATION_COMMUNICATIONS_IMPOSSIBLE"));
        }

    }

    private void doEcrituresComptables(final BITransaction transaction) throws Exception {

        final APGenererEcrituresComptablesProcess genererEcrituresComptablesProcess = new APGenererEcrituresComptablesProcess(
                this);
        genererEcrituresComptablesProcess.setIdLot(idLot);
        genererEcrituresComptablesProcess.setDateComptable(dateValeurComptable);
        genererEcrituresComptablesProcess.setDateSurDocument(getDateSurDocument());
        genererEcrituresComptablesProcess.setSendMailOnError(false);
        genererEcrituresComptablesProcess.setTransaction(transaction);
        genererEcrituresComptablesProcess.executeProcess();
    }

    private void doInscriptionsCI(final BITransaction transaction) throws Exception {
        final APInscrireCIProcess inscrireCIProcess = new APInscrireCIProcess(this);
        inscrireCIProcess.setForIdLot(idLot);
        inscrireCIProcess.setSendMailOnError(false);
        inscrireCIProcess.setTransaction(transaction);
        inscrireCIProcess.executeProcess();
    }

    private void envoyerAnnonces(final BITransaction transaction) throws Exception {
        final APEnvoyerAnnoncesSedexProcess envoyerAnnoncesSedexProcess = new APEnvoyerAnnoncesSedexProcess(this);
        envoyerAnnoncesSedexProcess.setForMoisAnneeComptable(dateValeurComptable.substring(3));
        envoyerAnnoncesSedexProcess.setTransaction(transaction);
        envoyerAnnoncesSedexProcess.executeProcess();
    }

    private void genereAnnonces(final BITransaction transaction) throws Exception {
        final APGenererAnnoncesProcess genererAnnoncesProcess = new APGenererAnnoncesProcess(this);
        genererAnnoncesProcess.setForIdLot(idLot);

        genererAnnoncesProcess.setMoisAnneeComptable(dateValeurComptable.substring(3));
        genererAnnoncesProcess.setSendMailOnError(false);
        genererAnnoncesProcess.setTransaction(transaction);
        genererAnnoncesProcess.executeProcess();
    }

    /**
     * getter pour l'attribut date sur document
     * 
     * @return la valeur courante de l'attribut date sur document
     */
    public String getDateSurDocument() {
        return dateSurDocument;
    }

    /**
     * getter pour l'attribut date valeur comptable
     * 
     * @return la valeur courante de l'attribut date valeur comptable
     */
    public String getDateValeurComptable() {
        return dateValeurComptable;
    }

    /**
     * getter pour l'attribut description lot
     * 
     * @return la valeur courante de l'attribut description lot
     */
    public String getDescriptionLot() {
        return descriptionLot;
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    /**
     * getter pour l'attribut no lot
     * 
     * @return la valeur courante de l'attribut no lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut is definitif
     * 
     * @return la valeur courante de l'attribut is definitif
     */
    public Boolean getIsDefinitif() {
        return isDefinitif;
    }

    /**
     * @return
     */
    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    /**
     * Exécute le process de génération des décomptes
     * 
     * @throws Exception
     *             En cas d'exception lors de la génération des déccomptes
     */
    private void imprimerCommunication() throws Exception {
        try {
            final APDecompteGenerationProcess process = new APDecompteGenerationProcess();

            String tmpValue = null;

            int idAssuranceAvsParitaire = 0;
            tmpValue = PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE);
            if (!JadeStringUtil.isIntegerEmpty(tmpValue)) {
                idAssuranceAvsParitaire = Integer.valueOf(tmpValue);
            }

            int idAssuranceAvsPersonnelle = 0;
            tmpValue = PRAffiliationHelper.GENRE_AVS_AI.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PERSONNEL);
            if (!JadeStringUtil.isIntegerEmpty(tmpValue)) {
                idAssuranceAvsPersonnelle = Integer.valueOf(tmpValue);
            }

            int idAssuranceAcParitaire = 0;
            tmpValue = PRAffiliationHelper.GENRE_AC.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE);
            if (!JadeStringUtil.isIntegerEmpty(tmpValue)) {
                idAssuranceAcParitaire = Integer.valueOf(tmpValue);
            }

            int idAssuranceLfaParitaire = 0;
            tmpValue = PRAffiliationHelper.GENRE_LFA.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PARITAIRE);
            if (!JadeStringUtil.isIntegerEmpty(tmpValue)) {
                idAssuranceLfaParitaire = Integer.valueOf(tmpValue);
            }

            int idAssuranceLfaPersonnelle = 0;
            tmpValue = PRAffiliationHelper.GENRE_LFA.getIdAssurance(APApplication.DEFAULT_APPLICATION_APG,
                    PRAffiliationHelper.TYPE_PERSONNEL);
            if (!JadeStringUtil.isIntegerEmpty(tmpValue)) {
                idAssuranceLfaPersonnelle = Integer.valueOf(tmpValue);
            }

            int idAssuranceFneParitaire = 0;
            tmpValue = APProperties.ASSURANCE_FNE_ID.getValue();
            if (!JadeStringUtil.isIntegerEmpty(tmpValue)) {
                idAssuranceFneParitaire = Integer.valueOf(tmpValue);
            }

            final boolean isAfficherConfidentielSurDocument = Boolean.valueOf(getSession().getApplication()
                    .getProperty(APApplication.PROPERTY_DOC_CONFIDENTIEL));
            final boolean isAfficherNIPSurDocument = Boolean.valueOf(getSession().getApplication().getProperty(
                    APApplication.PROPERTY_DISPLAY_NIP));
            final boolean isDecompteRecapitulatif = Boolean.valueOf(getSession().getApplication().getProperty(
                    APApplication.PROPERTY_IS_RECAPITULATIF_DECOMPTE));

            final boolean isBlankIndexGedNssAZero = APProperties.BLANK_INDEX_GED_NSS_A_ZERO.getBooleanValue();
            final boolean isNumeroAffiliePourGEDForceAZeroSiVide = APProperties.NUMERO_AFFILIE_POUR_LA_GED_FORCES_A_ZERO_SI_VIDE
                    .getBooleanValue();

            final String propertyValue = APProperties.TYPE_DE_PRESTATION_ACM.getValue();

            // validation en fonction de son domaine de valeur. Exception si valeur ne fait pas partie du domaine
            CommonPropertiesUtils.validatePropertyValue(APProperties.TYPE_DE_PRESTATION_ACM, propertyValue,
                    APPropertyTypeDePrestationAcmValues.propertyValues());

            final APPropertyTypeDePrestationAcmValues typeDePrestationAcm = APPropertyTypeDePrestationAcmValues
                    .valueOf(propertyValue);
            if (typeDePrestationAcm == null) {
                if (!APProperties.TYPE_DE_PRESTATION_ACM.isValidValue(propertyValue)) {
                    throw new RuntimeException(
                            "Technical Exception : unable to retrieve the APTypeDePrestationAcmPropertyValues enum with the property APProperties.TYPE_DE_PRESTATION_ACM value = ["
                                    + propertyValue + "]");
                }
            }

            process.setSession(getSession());
            process.setIdLot(getIdLot());
            process.setIsSendToGED(isSendToGed.booleanValue());
            process.setDateDocument(new JADate(dateSurDocument));
            process.setDateComptable(new JADate(dateValeurComptable));
            process.setEMailAddress(getEMailAddress());
            process.setIdAssuranceAvsParitaire(idAssuranceAvsParitaire);
            process.setIdAssuranceAvsPersonnelle(idAssuranceAvsPersonnelle);
            process.setIdAssuranceAcParitaire(idAssuranceAcParitaire);
            process.setIdAssuranceLfaParitaire(idAssuranceLfaParitaire);
            process.setIdAssuranceLfaPersonnelle(idAssuranceLfaPersonnelle);
            process.setIdAssuranceFneParitaire(idAssuranceFneParitaire);
            process.setIsAfficherConfidentielSurDocument(isAfficherConfidentielSurDocument);
            process.setIsAfficherNIPSurDocument(isAfficherNIPSurDocument);
            process.setIsBlankIndexGedNssAZero(isBlankIndexGedNssAZero);
            process.setIsDecompteRecapitulatif(isDecompteRecapitulatif);
            process.setIsNumeroAffiliePourGEDForceAZeroSiVide(isNumeroAffiliePourGEDForceAZeroSiVide);
            process.setTypeDePrestationAcm(typeDePrestationAcm);
            process.validate();
            process.start();

        } catch (final Throwable t) {
            final String message = getSession().getLabel("GENERATION_DECOMPTE_EXCEPTION_PENDANT_GENERATION");
            throw new Exception(message + " : " + t.toString());
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @return DOCUMENT ME!
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void metAJourDroits(final Set<String> idsDroit, final BTransaction transaction, final BSession session)
            throws Exception {
        // Il faut pour chaque droit regarder si toutes ses prestations sont
        // définitives. Si oui, le droit est définitif, sinon il est partiel.

        final Iterator<String> iterator = idsDroit.iterator();
        APDroitLAPG droitLAPG = null;

        while (iterator.hasNext()) {
            droitLAPG = new APDroitLAPG();
            droitLAPG.setIdDroit((iterator.next()));
            droitLAPG.retrieve(transaction);

            final APPrestationManager prestationManager = new APPrestationManager();
            prestationManager.setSession(session);
            prestationManager.setForIdDroit(droitLAPG.getIdDroit());
            prestationManager.setForEtat(APPrestationManager.ETAT_NON_DEFINITIF);
            prestationManager.find(transaction);

            final boolean tousDefinitif = (prestationManager.size() == 0);

            if (tousDefinitif) {
                droitLAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
            } else {
                droitLAPG.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL);
            }

            droitLAPG.update(transaction);
            getMemoryLog().logMessage("Droit n°" + droitLAPG.getIdDroit() + " mis à jour", FWMessage.INFORMATION, "");
        }
    }

    /*
     * Cette méthode met à jours les prestations de restitutions en ANNULE. Dans l'état actuel des choses, cela ne pose
     * pas de problème car on ne traite que des restituions complètes. Si la possibilité de générer des restitutions
     * partielles et rétroactifs, était nécessaire, cette méthode devrait être changée de sorte à n'annuler que les
     * restituions complètes.
     */
    private Set<String> metAJourPrestations(final BTransaction transaction, final BSession session) throws Exception {
        // mise en état définitif des prestations et en même temps, stockage des
        // idDroits pour utilisation
        // ultérieure.
        final APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(session);
        prestationManager.setForIdLot(idLot);

        final BStatement statement = prestationManager.cursorOpen(transaction);
        APPrestation prestation = null;
        final HashSet<String> idsDroit = new HashSet<String>();
        final HashSet<String> idsPrestationsAnnulees = new HashSet<String>();

        // parcourt de toutes les prestations de ce lot
        while ((prestation = (APPrestation) (prestationManager.cursorReadNext(statement))) != null) {
            // cette prestation peut avoir été annulée pendant le traitement
            // d'une prestation précédente. Elle
            // n'est alors plus synchronisée avec la base et il faut la mettre à
            // jour.
            if (idsPrestationsAnnulees.contains(prestation.getIdPrestationApg())) {
                // retrieve pour récupérer les modifs (pspsy et type annulation)
                prestation.retrieve(transaction);
            }

            // Stockage de l'idDroit
            idsDroit.add(prestation.getIdDroit());
            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
            prestation.setDatePaiement(getDateValeurComptable());

            // si le contenuAnnonce de cette prestation est "restitution" il
            // fait mettre en état annulation
            // toutes les prestations qui ont pour idRestitution l'id de cette
            // prestation, et mettre cette
            // prestation en état annulation
            if (prestation.getContenuAnnonce().equals(IAPAnnonce.CS_RESTITUTION)) {
                APPrestationManager prestationManager2 = new APPrestationManager();
                prestationManager2.setSession(session);
                prestationManager2.setForIdRestitution(prestation.getIdPrestationApg());
                prestationManager2.find(transaction);

                APPrestation prestation2 = null;

                for (int i = 0; i < prestationManager2.size(); i++) {
                    prestation2 = (APPrestation) (prestationManager2.getEntity(i));
                    prestation2.setType(IAPPrestation.CS_TYPE_ANNULATION);
                    prestation2.update(transaction);
                    getMemoryLog().logMessage("prestation n°" + prestation2.getIdPrestationApg() + " annulée",
                            FWMessage.INFORMATION, "");

                    // il se peut que cette prestation2 se trouve dans la
                    // requête du premier manager. Il faut
                    // donc stocker son id et ensuite pour le tester avec les
                    // prestations qui sortiront du
                    // premier manager pour mettre leur type a annulation
                    idsPrestationsAnnulees.add(prestation2.getIdPrestationApg());
                }

                prestation.setType(IAPPrestation.CS_TYPE_ANNULATION);
                prestation2 = null;
                prestationManager2 = null;
            }

            // update de la prestation.
            prestation.update(transaction);
            getMemoryLog().logMessage("prestation n°" + prestation.getIdPrestationApg() + " mise à jour",
                    FWMessage.INFORMATION, "");
        }

        return idsDroit;
    }

    /**
     * setter pour l'attribut date sur document
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSurDocument(final String string) {
        dateSurDocument = string;
    }

    /**
     * setter pour l'attribut date valeur comptable
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateValeurComptable(final String string) {
        dateValeurComptable = string;
    }

    /**
     * setter pour l'attribut description lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionLot(final String string) {
        descriptionLot = string;
    }

    /**
     * setter pour l'attribut no lot
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(final String string) {
        idLot = string;
    }

    /**
     * setter pour l'attribut is definitif
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsDefinitif(final Boolean boolean1) {
        isDefinitif = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setIsSendToGed(final Boolean boolean1) {
        isSendToGed = boolean1;
    }

    private void valideLot(final BTransaction transaction) throws Exception {
        final APLot lot = new APLot();
        lot.setSession(getSession());
        lot.setIdLot(idLot);
        lot.retrieve(transaction);
        lot.setEtat(IAPLot.CS_VALIDE);
        lot.setDateImpressionComm(JACalendar.todayJJsMMsAAAA());
        lot.setDateComptable(dateValeurComptable);
        lot.update(transaction);
        getMemoryLog().logMessage("lot n°" + lot.getIdLot() + " passé en validé", "", "");
    }

}

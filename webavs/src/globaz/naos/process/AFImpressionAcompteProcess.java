package globaz.naos.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.acompte.AFAcompteManager;
import globaz.naos.db.acompte.AFAcompteViewBean;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.itext.acompte.AFBouclementAcompte_Doc;
import globaz.naos.itext.acompte.AFPrevisionAcompte_Doc;
import globaz.naos.itext.masse.AFConfirmationSalaires_Doc;
import globaz.naos.itext.masse.AFSalaires_Param;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * <p>
 * Cree les debuts de suivis pour les documents d'annonces de salaires.
 * </p>
 * <p>
 * Genere les documents pour tous les affilies non radies qui ont la case 'envois automatiques des annonces de salaires'
 * ou pour l'affilie dont l'identifiant est renseigne.
 * </p>
 * 
 * @author vre
 */
public class AFImpressionAcompteProcess extends BProcess {

    private static final long serialVersionUID = 2745167626586554687L;

    private String affiliationId;
    private transient List<AFAffiliation> affiliations;
    private String dateDebut;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String dateFin;
    private String dateRetour;
    private String declarationSalaire = "";
    private String fromIdExterneRole = "";

    private String masseSuperieur = "";

    private String planAffiliationId;

    private String tillIdExterneRole = "";

    private String typeDocument;

    public AFImpressionAcompteProcess() {
    }

    public AFImpressionAcompteProcess(BProcess parent) {
        super(parent);
    }

    public AFImpressionAcompteProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            AFBouclementAcompte_Doc bouclementAcompte_Doc; // = new
            // AFAnnonceSalaires_Doc();
            AFPrevisionAcompte_Doc previsionAcompte_Doc;
            AFConfirmationSalaires_Doc confirmationAcompte_Doc;
            AFPlanAffiliationManager plansManager = new AFPlanAffiliationManager();

            plansManager.setSession(getSession());

            // charge les affiliations
            loadAffiliations();

            if ((affiliations == null) || affiliations.isEmpty()) {
                getMemoryLog().logMessage(getSession().getLabel("NAOS_PAS_DE_FORMULAIRES"), FWMessage.AVERTISSEMENT,
                        this.getClass().getName());
            } else {
                // charger les affiliations dont on veut generer les debuts de
                // suivis
                for (Iterator<AFAffiliation> iter = affiliations.iterator(); iter.hasNext();) {
                    AFAffiliation affiliation = iter.next();

                    // Test s'il a du personnel et la particularité
                    // "décompte exact temporaire" activé
                    if ((AFParticulariteAffiliation.existeParticularite(getSession(), affiliation.getAffiliationId(),
                            CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, getDateDebut()))
                            || (AFParticulariteAffiliation.existeParticularite(getSession(),
                                    affiliation.getAffiliationId(), CodeSystem.PARTIC_AFFILIE_ADMINISTRATEUR,
                                    getDateDebut()))) {
                        continue;
                    } else {
                        if (AFParticulariteAffiliation.existeParticularite(getSession(),
                                affiliation.getAffiliationId(), CodeSystem.PARTIC_AFFILIE_DEROG_ACOMTEMP,
                                getDateDebut())
                                || !affiliation.isReleveParitaire().booleanValue()) {
                            // Test si une des assurances est active à la date
                            // donnée dateDebut.toStrAMJ()

                            AFAcompteManager manager = new AFAcompteManager();
                            manager.setSession(getSession());
                            manager.setForAffiliationId(affiliation.getAffiliationId());

                            if (JadeStringUtil.isBlankOrZero(planAffiliationId)) {
                                // pas d'impression pour un plan précis, itérer
                                // sur chaque plan
                                // charger les plans d'affiliations pour cet
                                // affilie
                                plansManager.setForAffiliationId(affiliation.getAffiliationId());
                                plansManager.find();

                                for (int idPlan = 0; idPlan < plansManager.size(); ++idPlan) {
                                    AFPlanAffiliation planAffiliation = (AFPlanAffiliation) plansManager.get(idPlan);
                                    boolean active = false;
                                    // créer le document
                                    // annonceSalaires_Doc.resetPlan();
                                    manager.setForIdPlanAffiliation(planAffiliation.getPlanAffiliationId());
                                    manager.find();
                                    if (manager.size() > 0) {
                                        if (typeDocument.equals("3")) {
                                            setDateDebut(dateEnvoi);
                                        }
                                        for (int i = 0; i < manager.size(); i++) {
                                            AFAcompteViewBean acompte = (AFAcompteViewBean) manager.getEntity(i);
                                            int anneeDebutAff = Integer.parseInt(acompte.getDateDebut().substring(6));
                                            int anneeDebutPeriode = Integer.parseInt(dateDebut.toString().substring(6));

                                            if (anneeDebutAff <= anneeDebutPeriode) {
                                                active = true;
                                            }
                                        }
                                    }
                                    if (active) {
                                        // Type de documents : 1 = Bouclement
                                        // d'acompte
                                        // 2 = Prévision d'acompte
                                        // 3 = Decision d'acompte
                                        if (typeDocument.equals("1")) {
                                            AFCotisationManager cotisations = new AFCotisationManager();
                                            cotisations.setSession(getSession());
                                            cotisations.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                                            cotisations.setForAffiliationId(affiliation.getAffiliationId());
                                            cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                                            cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                            cotisations.setForPeriode(dateDebut, dateFin);
                                            cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                                            cotisations.setForIsMaisonMere(new Boolean(false));

                                            cotisations.setOrder("IDEXTERNE");
                                            cotisations.find();

                                            bouclementAcompte_Doc = new AFBouclementAcompte_Doc(getSession());
                                            bouclementAcompte_Doc.setDateEnvoi(dateEnvoi);
                                            bouclementAcompte_Doc.setDateRetour(dateRetour);
                                            bouclementAcompte_Doc.setCotisations(cotisations);

                                            if (cotisations.size() > 0) {
                                                this.creerDocument(bouclementAcompte_Doc,
                                                        affiliation.getAffiliationId(),
                                                        planAffiliation.getPlanAffiliationId(), getDateDebut(),
                                                        getDateFin(), planAffiliation);
                                            }
                                        }
                                        if (typeDocument.equals("2")) {
                                            AFCotisationManager cotisations = new AFCotisationManager();
                                            cotisations.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                                            cotisations.setForAffiliationId(affiliation.getAffiliationId());
                                            cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                                            cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                            cotisations.setForPeriode(dateDebut, dateFin);
                                            cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                                            cotisations.setSession(getSession());
                                            cotisations.setOrder("IDEXTERNE");
                                            cotisations.find();

                                            previsionAcompte_Doc = new AFPrevisionAcompte_Doc(getSession());
                                            previsionAcompte_Doc.setDateEnvoi(dateEnvoi);
                                            previsionAcompte_Doc.setDateRetour(dateRetour);
                                            previsionAcompte_Doc.setCotisations(cotisations);

                                            boolean containACotisationWithMasseAnnuelleGreaterEqual = true;
                                            if (JadeNumericUtil.isNumeric(JANumberFormatter
                                                    .deQuote(getMasseSuperieur()))) {
                                                containACotisationWithMasseAnnuelleGreaterEqual = cotisationManagerContainACotisationWithMasseAnnuelleGreaterEqual(
                                                        cotisations, getMasseSuperieur());
                                            }

                                            if ((cotisations.size() > 0)
                                                    && containACotisationWithMasseAnnuelleGreaterEqual) {
                                                this.creerDocument(previsionAcompte_Doc,
                                                        affiliation.getAffiliationId(),
                                                        planAffiliation.getPlanAffiliationId(), getDateDebut(),
                                                        getDateFin(), planAffiliation);
                                            }
                                        }
                                        if (typeDocument.equals("3")) {
                                            AFCotisationManager cotisations = new AFCotisationManager();
                                            cotisations.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                                            cotisations.setForAffiliationId(affiliation.getAffiliationId());
                                            cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                                            cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                            cotisations.setForDate(dateEnvoi);
                                            cotisations.setSession(getSession());
                                            cotisations.setOrder("IDEXTERNE");
                                            cotisations.find();

                                            confirmationAcompte_Doc = new AFConfirmationSalaires_Doc(getSession());
                                            confirmationAcompte_Doc.setDateEnvoi(dateEnvoi);
                                            confirmationAcompte_Doc.setCotisations(cotisations);

                                            if (cotisations.size() > 0) {
                                                this.creerDocument(confirmationAcompte_Doc,
                                                        affiliation.getAffiliationId(),
                                                        planAffiliation.getPlanAffiliationId(), planAffiliation);
                                            }
                                        }
                                    }

                                }
                            } else {

                                AFPlanAffiliation planAffiliation = new AFPlanAffiliation();
                                planAffiliation.setPlanAffiliationId(planAffiliationId);
                                planAffiliation.setSession(getSession());
                                planAffiliation.retrieve();

                                boolean active = false;
                                manager.setForIdPlanAffiliation(planAffiliationId);
                                manager.find();
                                if (manager.size() > 0) {
                                    for (int i = 0; i < manager.size(); i++) {
                                        AFAcompteViewBean acompte = (AFAcompteViewBean) manager.getEntity(i);

                                        int anneeDebutAff = Integer.parseInt(acompte.getDateDebut().substring(6));
                                        int anneeDebutPeriode = Integer.parseInt(dateDebut.toString().substring(6));

                                        if (anneeDebutAff <= anneeDebutPeriode) {
                                            active = true;
                                        }
                                    }
                                }
                                if (active) {
                                    // Type de documents : 1 = Bouclement
                                    // d'acompte
                                    // 2 = Prévision d'acompte
                                    // 3 = Decision d'acompte
                                    if (typeDocument.equals("1")) {
                                        AFCotisationManager cotisations = new AFCotisationManager();
                                        cotisations.setSession(getSession());
                                        cotisations.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                                        cotisations.setForAffiliationId(affiliation.getAffiliationId());
                                        cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                                        cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                        cotisations.setForPeriode(dateDebut, dateFin);
                                        cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                                        cotisations.setForIsMaisonMere(new Boolean(false));

                                        cotisations.setOrder("IDEXTERNE");
                                        cotisations.find();

                                        bouclementAcompte_Doc = new AFBouclementAcompte_Doc(getSession());
                                        bouclementAcompte_Doc.setDateEnvoi(dateEnvoi);
                                        bouclementAcompte_Doc.setDateRetour(dateRetour);
                                        bouclementAcompte_Doc.setCotisations(cotisations);

                                        if (cotisations.size() > 0) {
                                            this.creerDocument(bouclementAcompte_Doc, affiliation.getAffiliationId(),
                                                    planAffiliationId, getDateDebut(), getDateFin(), planAffiliation);
                                        }
                                    }
                                    if (typeDocument.equals("2")) {
                                        AFCotisationManager cotisations = new AFCotisationManager();
                                        cotisations.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                                        cotisations.setForAffiliationId(affiliation.getAffiliationId());
                                        cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                                        cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                        cotisations.setForPeriode(dateDebut, dateFin);
                                        cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
                                        cotisations.setSession(getSession());
                                        cotisations.setOrder("IDEXTERNE");
                                        cotisations.find();

                                        previsionAcompte_Doc = new AFPrevisionAcompte_Doc(getSession());
                                        previsionAcompte_Doc.setDateEnvoi(dateEnvoi);
                                        previsionAcompte_Doc.setDateRetour(dateRetour);
                                        previsionAcompte_Doc.setCotisations(cotisations);

                                        boolean containACotisationWithMasseAnnuelleGreaterEqual = true;
                                        if (JadeNumericUtil.isNumeric(JANumberFormatter.deQuote(getMasseSuperieur()))) {
                                            containACotisationWithMasseAnnuelleGreaterEqual = cotisationManagerContainACotisationWithMasseAnnuelleGreaterEqual(
                                                    cotisations, getMasseSuperieur());
                                        }

                                        if ((cotisations.size() > 0) && containACotisationWithMasseAnnuelleGreaterEqual) {
                                            this.creerDocument(previsionAcompte_Doc, affiliation.getAffiliationId(),
                                                    planAffiliationId, getDateDebut(), getDateFin(), planAffiliation);
                                        }
                                    }
                                    if (typeDocument.equals("3")) {
                                        AFCotisationManager cotisations = new AFCotisationManager();
                                        cotisations.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                                        cotisations.setForAffiliationId(affiliation.getAffiliationId());
                                        cotisations.setNotForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                                        cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                                        cotisations.setForDate(dateEnvoi);
                                        cotisations.setSession(getSession());
                                        cotisations.setOrder("IDEXTERNE");
                                        cotisations.find();

                                        confirmationAcompte_Doc = new AFConfirmationSalaires_Doc(getSession());
                                        confirmationAcompte_Doc.setDateEnvoi(dateEnvoi);
                                        confirmationAcompte_Doc.setDateRetour(dateRetour);
                                        confirmationAcompte_Doc.setCotisations(cotisations);

                                        if (cotisations.size() > 0) {
                                            this.creerDocument(confirmationAcompte_Doc, affiliation.getAffiliationId(),
                                                    planAffiliationId, planAffiliation);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            affiliations = null;
            System.gc();
            String nbDocument = "50";

            try {
                nbDocument = ((AFApplication) getSession().getApplication()).getProperty("nbDocumentPourFusion",
                        nbDocument);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }

            System.out.println("DEBUT DU MERGE avec " + nbDocument + " documents par fichier");

            try {
                JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();

                mergedDocumentInfo.setPublishDocument(true);
                mergedDocumentInfo.setArchiveDocument(false);
                // Comme les documents vont d'office en GED on garde les fichiers d'origines
                this.mergePDF(mergedDocumentInfo, false, Integer.parseInt(nbDocument), false, null);

            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
            }

            return true;
        } catch (Exception e) {
            this._addError("erreur durant la génération des documents: " + e.getMessage());
            abort();

            return false;
        }
    }

    private boolean cotisationManagerContainACotisationWithMasseAnnuelleGreaterEqual(
            AFCotisationManager theCotiManager, String theMasseReference) throws Exception {

        if (theCotiManager == null) {
            throw new Exception(
                    "unable to indicate that the cotisations manager contain a cotisation with masse annuelle greater or equal to masse reference because the manager is null");
        }

        theMasseReference = JANumberFormatter.deQuote(theMasseReference);
        if (!JadeNumericUtil.isNumeric(theMasseReference)) {
            throw new Exception(
                    "unable to indicate that the cotisations manager contain a cotisation with masse annuelle greater or equal to masse reference because masse reference isn't numeric");
        }

        double theMasseReferenceDouble = Double.valueOf(theMasseReference).doubleValue();

        for (int i = 0; i < theCotiManager.size(); i++) {
            AFCotisation theCoti = (AFCotisation) theCotiManager.getEntity(i);
            String theMasseAnnuelleCoti = theCoti.getMasseAnnuelle();
            theMasseAnnuelleCoti = JANumberFormatter.deQuote(theMasseAnnuelleCoti);

            if (JadeNumericUtil.isNumeric(theMasseAnnuelleCoti)
                    && (Double.valueOf(theMasseAnnuelleCoti).doubleValue() >= theMasseReferenceDouble)) {
                return true;
            }

        }

        return false;
    }

    /**
     * démarre le suivi pour un document.
     * 
     * @param annonceSalaires_Doc
     *            DOCUMENT ME!
     * @param affiliationId
     *            DOCUMENT ME!
     * @param planAffiliationId
     *            DOCUMENT ME!
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void creerDocument(AFBouclementAcompte_Doc bouclementAcompte_Doc, String affiliationId,
            String planAffiliationId, String dateDebut, String dateFin, AFPlanAffiliation plan) throws Exception {

        if (plan != null) {
            Boolean bloquerEnvoi = plan.isBlocageEnvoi();
            if (bloquerEnvoi == null) {
                bloquerEnvoi = new Boolean(false);
            }

            bouclementAcompte_Doc.setBloquerEnvoi(bloquerEnvoi.booleanValue());
        }

        bouclementAcompte_Doc.setParent(this);
        bouclementAcompte_Doc.setDateDebut(dateDebut);
        bouclementAcompte_Doc.setDateFin(dateFin);
        bouclementAcompte_Doc.setAffiliationId(affiliationId);
        bouclementAcompte_Doc.setPlanAffiliationId(planAffiliationId);
        bouclementAcompte_Doc.setDateEnvoi(dateEnvoi);
        bouclementAcompte_Doc.executeProcess();
    }

    /**
     * démarre le suivi pour un document.
     * 
     * @param annonceSalaires_Doc
     *            DOCUMENT ME!
     * @param affiliationId
     *            DOCUMENT ME!
     * @param planAffiliationId
     *            DOCUMENT ME!
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void creerDocument(AFConfirmationSalaires_Doc confirmationSalaires_Doc, String affiliationId,
            String planAffiliationId, AFPlanAffiliation plan) throws Exception {

        if (plan != null) {
            Boolean bloquerEnvoi = plan.isBlocageEnvoi();
            if (bloquerEnvoi == null) {
                bloquerEnvoi = new Boolean(false);
            }

            confirmationSalaires_Doc.setBloquerEnvoi(bloquerEnvoi.booleanValue());
        }
        confirmationSalaires_Doc.setParent(this);
        confirmationSalaires_Doc.setAffiliationId(affiliationId);
        confirmationSalaires_Doc.setPlanAffiliationId(planAffiliationId);
        confirmationSalaires_Doc.setDateEnvoi(dateEnvoi);
        confirmationSalaires_Doc.executeProcess();
    }

    /**
     * démarre le suivi pour un document.
     * 
     * @param annonceSalaires_Doc
     *            DOCUMENT ME!
     * @param affiliationId
     *            DOCUMENT ME!
     * @param planAffiliationId
     *            DOCUMENT ME!
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void creerDocument(AFPrevisionAcompte_Doc previsionAcompte_Doc, String affiliationId,
            String planAffiliationId, String dateDebut, String dateFin, AFPlanAffiliation plan) throws Exception {
        if (plan != null) {
            Boolean bloquerEnvoi = plan.isBlocageEnvoi();
            if (bloquerEnvoi == null) {
                bloquerEnvoi = new Boolean(false);
            }

            previsionAcompte_Doc.setBloquerEnvoi(bloquerEnvoi.booleanValue());
        }

        previsionAcompte_Doc.setParent(this);
        previsionAcompte_Doc.setDateDebut(dateDebut);
        previsionAcompte_Doc.setDateFin(dateFin);
        previsionAcompte_Doc.setAffiliationId(affiliationId);
        previsionAcompte_Doc.setPlanAffiliationId(planAffiliationId);
        previsionAcompte_Doc.setDateEnvoi(dateEnvoi);
        // previsionAcompte_Doc.setEnvoyerGed(this.getEnvoyerGed());
        previsionAcompte_Doc.executeProcess();
    }

    protected String format(String message, String[] args) {
        StringBuffer msgBuf = new StringBuffer();

        msgBuf.append(message.charAt(0));

        for (int idChar = 1; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar - 1) == '\'') && (message.charAt(idChar) != '\'')) {
                msgBuf.append('\'');
            }

            msgBuf.append(message.charAt(idChar));
        }

        return MessageFormat.format(msgBuf.toString(), args).toString();
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date envoi.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public String getDeclarationSalaire() {
        return declarationSalaire;
    }

    @Override
    protected String getEMailObject() {
        if (typeDocument.equals("1")) {
            return getSession().getLabel(AFSalaires_Param.NOMDOC_BOUCLEMENT_ACOMPTE);
        } else if (typeDocument.equals("2")) {
            return getSession().getLabel(AFSalaires_Param.NOMDOC_PREVISION_ACOMPTE);
        } else if (typeDocument.equals("3")) {
            return getSession().getLabel(AFSalaires_Param.NOMDOC_CONFIRMATION_SALAIRES);
        }
        return "";
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public String getMasseSuperieur() {
        return masseSuperieur;
    }

    /**
     * getter pour l'attribut plan affiliation.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    /**
     * @return
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * charge tous les affilies a qui l'on envoie les documents automatiquement.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private List<AFAffiliation> loadAffiliations() throws Exception {
        String[] typeAffiliation = new String[3];
        typeAffiliation[0] = CodeSystem.TYPE_AFFILI_INDEP_EMPLOY;
        typeAffiliation[1] = CodeSystem.TYPE_AFFILI_EMPLOY;
        typeAffiliation[2] = CodeSystem.TYPE_AFFILI_EMPLOY_D_F;
        if (affiliations == null) {
            if (JadeStringUtil.isIntegerEmpty(affiliationId)) {
                // rechercher les affilies
                AFAffiliationManager affiliation = new AFAffiliationManager();
                if (!JADate.getMonth(dateFin).equals(new BigDecimal(3))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(6))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(9))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(12))) {
                    affiliation.setForPeriodicite(CodeSystem.PERIODICITE_MENSUELLE);
                } else if ((JADate.getMonth(dateFin).equals(new BigDecimal(3))
                        || JADate.getMonth(dateFin).equals(new BigDecimal(6)) || JADate.getMonth(dateFin).equals(
                        new BigDecimal(9)))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(12))) {
                    affiliation.setForPeriodiciteIn(new String[] { CodeSystem.PERIODICITE_MENSUELLE,
                            CodeSystem.PERIODICITE_TRIMESTRIELLE });
                }
                affiliation.setForTypeAffiliation(typeAffiliation);
                affiliation.setFromDateFin(dateFin);
                affiliation.setFromAffilieNumero(getFromIdExterneRole());
                affiliation.setToAffilieNumero(getTillIdExterneRole());
                // plus valable (que pour l'ancien système)
                // affiliation.setForIsNotReleveParitaire(new Boolean(true));
                affiliation.forIsTraitement(false);
                affiliation.setSession(getSession());
                // Ne pas prendre les bulletins neutres
                affiliation.setNotInCodeFacturation(CodeSystem.CODE_FACTU_MONTANT_LIBRE);
                affiliation.setForDeclarationSalaire(getDeclarationSalaire());
                affiliation.changeManagerSize(BManager.SIZE_NOLIMIT);
                affiliation.find();

                affiliations = affiliation.getContainer();
                // System.out.println("NOMBRE D'AFFILIE : " +
                // affiliations.size());
            } else {
                LinkedList<AFAffiliation> retValue = new LinkedList<AFAffiliation>();
                AFAffiliation affiliation = new AFAffiliation();

                affiliation.setAffiliationId(affiliationId);
                affiliation.setSession(getSession());
                affiliation.retrieve();
                retValue.add(affiliation);

                affiliations = retValue;

                if ((affiliation != null) && !JadeStringUtil.isEmpty(getDeclarationSalaire())
                        && !getDeclarationSalaire().equalsIgnoreCase(affiliation.getDeclarationSalaire())) {
                    affiliations = null;
                }
            }
        }

        return affiliations;
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date envoi.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public void setDeclarationSalaire(String declarationSalaire) {
        this.declarationSalaire = declarationSalaire;
    }

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    public void setMasseSuperieur(String masseSuperieur) {
        this.masseSuperieur = masseSuperieur;
    }

    /**
     * setter pour l'attribut plan affiliation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPlanAffiliationId(String string) {
        planAffiliationId = string;
    }

    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }

    /**
     * @param string
     */
    public void setTypeDocument(String string) {
        typeDocument = string;
    }

}

package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.preimpression.DSSqlStringForStatement;
import globaz.draco.process.DSProcessValidation;
import globaz.draco.util.DSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.hercule.service.CEDocumentItextService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishQueueInfo;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.process.LEGenererEnvoi;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationForDSManager;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pavo.application.CIApplication;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import globaz.pyxis.db.tiers.TITiers;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Permet de générer le document de pré-impression des déclarations de salaires
 */
public class Doc2_preImpressionDeclaration extends FWIDocumentManager {

    private static final long serialVersionUID = -87417721244564777L;
    public static final String CS_AMENDE = "125012";
    public static final String CS_ATTESTATION = "125001";
    public static final String CS_DOMAINE = "124001";
    public static final String CS_PLAINTEPENALE = "125004";
    public static final String CS_RAPPEL = "125002";
    public static final String CS_RAPPEL_LTN = "125007";
    public static final String CS_RAPPEL_E_BUSINESS = "125013";
    public static final String CS_SOMMATION = "125003";
    public static final String CS_SOMMATION_LTN = "125006";
    public static final String DOC_AF_SEUL = "DRACO_PREIMP_DECSAL_AF";
    public static final int NO_ERROR = 0;
    private static final String NUM_DOCUMENT = "088";
    private static final String NUM_DOCUMENT_LTN = "201";
    public static final int ON_ERROR = 200;

    AFAffiliation affDatePlusGrand = new AFAffiliation();

    AFAffiliation affEnCours = new AFAffiliation();
    String affiliationId = "";
    private boolean affilieTous = false;
    AFAffiliationForDSManager affManager = new AFAffiliationForDSManager();

    private String annee = new String();
    private String assuranceId = "";
    private Boolean convertnnss = new Boolean(false);
    private String dateValeur = JACalendar.todayJJsMMsAAAA();

    // Pour savoir si on veut que l'impression soit inscrite dans LEO ou NON
    private Boolean demarreSuivi = new Boolean(false);
    ICTDocument[] document = null;
    ICTDocument[] documentDe = null;
    ICTDocument[] documentIt = null;
    private String fromAffilies = new String();

    // Indice permettant de parcourir les affiliés
    private int i = 0;
    private String idDocument = "";
    protected String idDocumentDefaut = "";
    // Pour savoir si on veut qu'une pré-impression de la déclaration de
    // salaires soit imprimée
    private Boolean imprimerDeclaration = new Boolean(false);

    // Pour savoir si on veut qu'une lettre soit imprimée ou non
    private Boolean imprimerLettre = new Boolean(false);
    private Boolean imprimerReceptionnees = new Boolean(false);
    private Boolean imprimerVide = new Boolean(false);
    int ind = 0;
    // Permet de savoir quand on peut passer à l'affilie suivant
    //
    private Boolean isAf = new Boolean(false);

    private boolean isFirst = true;
    // Permet de savoir quels documents on a imprimer
    // false = aucun document n'a encore été imprimer
    // true = le document de pré-impression des déclarations de salaire a été
    // imprimé
    private boolean isPasse = false;

    private String langueIsoTiers = "";
    JadePublishDocumentInfo lastAffilieDocumentInfo = null;
    Doc1_PreImpr_DS manager = new Doc1_PreImpr_DS();
    String MODEL_NAME = "";
    int nbNiveaux = 0;
    private String periodeDebutCotPeriode = "01.10.2011";
    private String periodeFinCotPeriode = "31.12.2011";
    private Boolean provientEcranPreImpression = new Boolean(false);

    List publishDocuments = new LinkedList();
    int size = 0;
    private String typeAffiliation = "";
    private String typeDeclaration = "";

    private String untilAffilies = new String();

    public Doc2_preImpressionDeclaration() throws Exception {
        this(new BSession(DSApplication.DEFAULT_APPLICATION_DRACO));
    }

    public Doc2_preImpressionDeclaration(BProcess parent) throws Exception {
        super(parent, DSApplication.DEFAULT_APPLICATION_ROOT, parent.getSession().getLabel("ENVOI_DECL_SALAIRE"));
        super.setFileTitle(parent.getSession().getLabel("ENVOI_DECL_SALAIRE"));
    }

    public Doc2_preImpressionDeclaration(BSession session) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, session.getLabel("ENVOI_DECL_SALAIRE"));
        super.setFileTitle(session.getLabel("ENVOI_DECL_SALAIRE"));
    }

    public static void main(String[] args) {
        try {

            if (args.length < 3) {
                System.out.println("globaz.pavo.process.Doc2_preImpression ");
                throw new Exception("Le nombre d'argument n'est pas correct");
            }

            BSession session = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).newSession(args[0], args[1]);
            Doc2_preImpressionDeclaration process = new Doc2_preImpressionDeclaration();

            process.setEMailAddress(args[2]);
            System.out.println("Email : " + args[2]);
            process.setDateValeur(args[3]);
            System.out.println("Date Valeur : " + args[3]);
            process.setTypeDeclaration(args[4]);
            System.out.println("Type DS : " + args[3]);
            process.setIdDocument(args[5]);
            System.out.println("Id Document : " + args[3]);
            process.setFromAffilies(args[6]);
            System.out.println("From affilie : " + args[6]);
            process.setUntilAffilies(args[7]);
            System.out.println("Untill affilie : " + args[7]);
            process.setTypeAffiliation(args[8]);

            if ("true".equalsIgnoreCase(args[9])) {
                process.setImprimerLettre(new Boolean(true));
            } else {
                process.setImprimerLettre(new Boolean(false));
            }

            System.out.println("Imprimer lettre : " + args[9]);

            if ("true".equalsIgnoreCase(args[10])) {
                process.setImprimerDeclaration(new Boolean(true));
            } else {
                process.setImprimerDeclaration(new Boolean(false));
            }

            System.out.println("Imprimer DS : " + args[10]);

            if ("true".equalsIgnoreCase(args[11])) {
                process.setDemarreSuivi(new Boolean(true));
            } else {
                process.setDemarreSuivi(new Boolean(false));
            }

            System.out.println("Suivi : " + args[11]);

            if ("true".equalsIgnoreCase(args[12])) {
                process.setConvertnnss(new Boolean(true));
            } else {
                process.setConvertnnss(new Boolean(false));
            }

            System.out.println("Suivi : " + args[12]);

            process.setSession(session);
            JadeJobInfo job = BProcessLauncher.start(process);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }

            List<?> queues = JadePublishServerFacade.getServerInfo().getQueueNames();
            while (true) {
                Thread.sleep(1000);
                boolean stillRunning = false;

                for (Iterator<?> iter = queues.iterator(); iter.hasNext();) {
                    String queueName = (String) iter.next();
                    JadePublishQueueInfo queueInfo = JadePublishServerFacade.getQueueInfo(queueName);

                    if ((queueInfo.getNumberOfWaitingJobs() > 0) || (queueInfo.getNumberOfRunningJobs() > 0)) {
                        stillRunning = true;
                        break;
                    }
                }

                if (!stillRunning) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Process génération concordance has error(s) !");

            // erreur critique, je retourne un code d'erreur 200
            System.exit(Doc2_preImpressionDeclaration.ON_ERROR);
        } finally {

        }
        System.exit(Doc2_preImpressionDeclaration.NO_ERROR);

    }

    protected void _footerText() throws Exception {
        // Paramètres à setter si on imprime la déclaration
        if (getImprimerDeclaration().booleanValue()) {
            super.setParametres(Doc1_PreImp_Param.P_MONTANT_SALAIRE,
                    getSession().getApplication().getLabel("ENVOI_TOTAL_SALAIRE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_CERTIFIE,
                    getSession().getApplication().getLabel("ENVOI_EXACT_COMPLET", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_DATE,
                    getSession().getApplication().getLabel("ENVOI_DATE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_TIMBRE,
                    getSession().getApplication().getLabel("ENVOI_TIMBRE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_SIGNE,
                    getSession().getApplication().getLabel("ENVOI_SIGNATURE", langueIsoTiers));
        }
    }

    /**
     * Permet de setter les paramètres de l'en-tête des documents Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText() throws Exception {
        // Paramètres à setter si on imprime la déclaration
        if (getImprimerDeclaration().booleanValue()) {

            super.setParametres(Doc1_PreImp_Param.P_HEAD_1,
                    getSession().getApplication().getLabel("ENTETE_CAISSE", langueIsoTiers));

            if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
                super.setParametres(Doc1_PreImp_Param.P_HEAD_2,
                        getSession().getApplication().getLabel("DEC_SAL_TITRE_LTN", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.P_PAGETXT,
                        getSession().getApplication().getLabel("PAGE", langueIsoTiers));
            } else {
                super.setParametres(Doc1_PreImp_Param.P_HEAD_2,
                        getSession().getApplication().getLabel("ENTETE_DECLARATION", langueIsoTiers));
            }

            String labelNumAff = getSession().getApplication().getLabel("ENTETE_NUMEROAFFILIE", langueIsoTiers);

            if (!JadeStringUtil.isEmpty(AFIDEUtil.defineNumeroStatutForDoc(affEnCours.getNumeroIDE(),
                    affEnCours.getIdeStatut()))) {
                labelNumAff += "/" + getSession().getApplication().getLabel("ENTETE_NUMEROIDE", langueIsoTiers);
            }

            super.setParametres(Doc1_PreImp_Param.P_HEAD_3, labelNumAff);

            String numAffilie = AFIDEUtil.createNumAffAndNumeroIdeForPrint(affEnCours.getAffilieNumero(),
                    affEnCours.getNumeroIDE(), affEnCours.getIdeStatut());

            super.setParametres(Doc1_PreImp_Param.P_HEAD_4, numAffilie);
            super.setParametres(Doc1_PreImp_Param.P_HEAD_5,
                    getSession().getApplication().getLabel("ENTETE_DECLARATIONPERIODE", langueIsoTiers));
            super.setParametres(
                    Doc1_PreImp_Param.P_ADRESSE,
                    affEnCours.getTiers().getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA(),
                            affEnCours.getAffilieNumero()));
            super.setParametres(
                    Doc1_PreImp_Param.P_NUMEROAFFILIE,
                    getSession().getApplication().getLabel("ENVOI_NUM_AFFILIE", langueIsoTiers) + " "
                            + affEnCours.getAffilieNumero());
            super.setParametres(Doc1_PreImp_Param.P_NOMPRENOMAFFILIE, affEnCours.getTiers().getPrenomNom());
            super.setParametres(Doc1_PreImp_Param.P_PAS_PERSO,
                    getSession().getApplication().getLabel("ENVOI_PAS_PERSONNEL", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_SI_CHANGE,
                    getSession().getApplication().getLabel("ENVOI_SI_CHANGEMENT", langueIsoTiers));

            String agenceCommunale = affEnCours.getAgenceComNum(affEnCours.getAffiliationId(),
                    JACalendar.todayJJsMMsAAAA());

            super.setParametres(Doc1_PreImp_Param.P_AGENCE_COMMUNALE, agenceCommunale);

            String debutPeriode = "";
            String finPeriode = "";

            // Si la date de début de l'affiliation est plus grande que la date
            // de début de l'année, on prend comme date de début de période
            // la date de début de l'affiliation, sinon on lui met le premier
            // jour de l'année comme date de début de période
            try {
                if (BSessionUtil.compareDateFirstGreater(getSession(), affEnCours.getDateDebut(), "01.01." + annee)) {
                    debutPeriode = affEnCours.getDateDebut();
                } else {
                    debutPeriode = "01.01." + annee;
                }

                // Si la date de fin de l'affiliation est plus petite que la
                // date de fin de l'année, on prend comme date de fin de période
                // la date de fin de l'affiliation, sinon on lui met le dernier
                // jour de l'année comme date de fin de période
                if (BSessionUtil.compareDateFirstLower(getSession(), affEnCours.getDateFin(), "31.12." + annee)
                        && !JadeStringUtil.isEmpty(affEnCours.getDateFin())) {
                    finPeriode = affEnCours.getDateFin();
                } else {
                    finPeriode = "31.12." + annee;
                }
            } catch (Exception e) {
                debutPeriode = "";
                finPeriode = "";
            }

            super.setParametres(Doc1_PreImp_Param.P_HEAD_6, debutPeriode + " - " + finPeriode);
            super.setParametres(Doc1_PreImp_Param.P_ANNEEDECLARATION_2,
                    getSession().getApplication().getLabel("ENVOI_PERIODE_DS", langueIsoTiers) + ": " + debutPeriode
                            + " - " + finPeriode);

            resolveHeaderCaisseLPP();
            resolveHeaderCaisseLAA();
        }

        // Paramètres à setter si on imprime la lettre
        if (getImprimerLettre().booleanValue()) {
            super.setParametres(Doc1_PreImp_Param.P_CONCERNE, getTexte(1, langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_CORPS, getTexte(2, langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.P_SIGNATURE, getTexte(3, langueIsoTiers));

            if (!JadeStringUtil.isBlankOrZero(getTexte(4, langueIsoTiers))) {
                super.setParametres(Doc1_PreImp_Param.P_AGENCE_COM, getTexte(4, langueIsoTiers));
            }
        }
    }

    private void resolveHeaderCaisseLAA() throws Exception {
        TITiers tiersLAA = new TITiers();
        AFSuiviCaisseAffiliationManager manLAA = new AFSuiviCaisseAffiliationManager();

        manLAA.setSession(getSession());
        manLAA.setForAffiliationId(affDatePlusGrand.getAffiliationId());
        manLAA.setForGenreCaisse(CodeSystem.GENRE_CAISSE_LAA);
        manLAA.setForAnneeActive(getAnnee());

        manLAA.find(getTransaction(), BManager.SIZE_USEDEFAULT);

        AFSuiviCaisseAffiliation laa = new AFSuiviCaisseAffiliation();
        if (manLAA.size() != 0) {
            laa = (AFSuiviCaisseAffiliation) manLAA.getEntity(0);
            tiersLAA = getDescriptionCaisse(laa.getIdTiersCaisse());
        }

        // On regarde si la localité est vide afin de ne pas afficher une
        // virgule sans rien après
        if (tiersLAA.getLocalite().length() != 0) {
            super.setParametres(
                    Doc1_PreImp_Param.P_ASSURANCE,
                    getSession().getApplication().getLabel("ENVOI_ASSUR_LAA", langueIsoTiers) + ": "
                            + tiersLAA.getDesignation1() + " " + tiersLAA.getDesignation2() + ", "
                            + tiersLAA.getLocalite());
        } else {
            super.setParametres(
                    Doc1_PreImp_Param.P_ASSURANCE,
                    getSession().getApplication().getLabel("ENVOI_ASSUR_LAA", langueIsoTiers) + ": "
                            + tiersLAA.getDesignation1() + " " + tiersLAA.getDesignation2());
        }
    }

    private void resolveHeaderCaisseLPP() throws Exception {
        TITiers tiersLPP = new TITiers();
        AFSuiviCaisseAffiliationManager manLPP = new AFSuiviCaisseAffiliationManager();

        manLPP.setSession(getSession());
        manLPP.setForAffiliationId(affDatePlusGrand.getAffiliationId());
        manLPP.setForGenreCaisse(CodeSystem.GENRE_CAISSE_LPP);
        manLPP.setForAnneeActive(getAnnee());

        manLPP.find(getTransaction(), BManager.SIZE_USEDEFAULT);

        AFSuiviCaisseAffiliation lpp = new AFSuiviCaisseAffiliation();
        if (manLPP.size() != 0) {
            lpp = (AFSuiviCaisseAffiliation) manLPP.getEntity(0);
            tiersLPP = getDescriptionCaisse(lpp.getIdTiersCaisse());
        }

        // On regarde si la localité est vide afin de ne pas afficher une
        // virgule sans rien après
        if (tiersLPP.getLocalite().length() != 0) {
            super.setParametres(
                    Doc1_PreImp_Param.P_CAISSE,
                    getSession().getApplication().getLabel("ENVOI_PREVOYANCE_LPP", langueIsoTiers) + ": "
                            + tiersLPP.getDesignation1() + " " + tiersLPP.getDesignation2() + " "
                            + tiersLPP.getDesignation3() + ", " + tiersLPP.getLocalite());
        } else {
            super.setParametres(
                    Doc1_PreImp_Param.P_CAISSE,
                    getSession().getApplication().getLabel("ENVOI_PREVOYANCE_LPP", langueIsoTiers) + ": "
                            + tiersLPP.getDesignation1() + " " + tiersLPP.getDesignation2() + " "
                            + tiersLPP.getDesignation3());
        }
    }

    // Sette l'en-tête de la lettre d'accompagnement
    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        bean.setAdresse(affEnCours.getTiers().getAdresseAsString(getDocumentInfo(),
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, DSApplication.CS_DOMAINE_DECLARATION_SALAIRES,
                JACalendar.todayJJsMMsAAAA(), affEnCours.getAffilieNumero()));
        bean.setDate(JACalendar.format(dateValeur, langueIsoTiers));
        bean.setNoAffilie(affEnCours.getAffilieNumero());

        // ajout du numéro IDE
        AFIDEUtil.addNumeroIDEInDoc(bean, affEnCours.getNumeroIDE(), affEnCours.getIdeStatut());

        bean.setNoAvs(" ");
        bean.setEmailCollaborateur(getSession().getUserEMail());
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setUser(getSession().getUserInfo());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
    }

    /**
     * Permet de setter l'en-tête des collonnes du tableau Commentaire relatif à la méthode _tableHeader.
     */
    protected void _tableHeader() throws Exception {
        // Paramètres à setter si on imprime une déclaration
        if (getImprimerDeclaration().booleanValue()) {
            super.setParametres(Doc1_PreImp_Param.L_1,
                    getSession().getApplication().getLabel("ENTETE_MEMBRESPERSONNEL", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_2,
                    getSession().getApplication().getLabel("ENTETE_PERIODETRAVAIL", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_3,
                    getSession().getApplication().getLabel("ENTETE_SALAIRE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_4,
                    getSession().getApplication().getLabel("ENTETE_AVS", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_5,
                    getSession().getApplication().getLabel("ENTETE_NOMPRENOM", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_6,
                    getSession().getApplication().getLabel("ENTETE_DEBUT", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_7,
                    getSession().getApplication().getLabel("ENTETE_FIN", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_8,
                    getSession().getApplication().getLabel("ENTETE_AVSAIAPG", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_9,
                    getSession().getApplication().getLabel("ENTETE_ASSURANCECHOMAGE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_17,
                    getSession().getApplication().getLabel("PRE_ANNEE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_18, getSession().getApplication().getLabel("AC_II", langueIsoTiers));

            // Ici on sette les paramètres pour savoir si les collonnes doivent
            // être affichées ou non
            DSApplication application = null;
            application = (DSApplication) globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(
                    DSApplication.DEFAULT_APPLICATION_DRACO);

            super.setParametres(Doc1_PreImp_Param.P_AF, String.valueOf(true));
            super.setParametres(Doc1_PreImp_Param.P_LPP, String.valueOf(true));
            super.setParametres(Doc1_PreImp_Param.P_AFPRES, String.valueOf(true));
            super.setParametres(Doc1_PreImp_Param.P_AMAT, String.valueOf(true));
            super.setParametres(Doc1_PreImp_Param.P_AFPRESTEXTE,
                    getSession().getApplication().getLabel("ENVOI_AF", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_11,
                    getSession().getApplication().getLabel("ENVOI_PRESTATION", langueIsoTiers));

            if (existeCotisationPeriode()) {
                String realPeriodeDebut = "";
                String realPeriodeFin = "";

                if (BSessionUtil.compareDateFirstGreater(getSession(), affEnCours.getDateDebut(),
                        periodeDebutCotPeriode)) {
                    realPeriodeDebut = affEnCours.getDateDebut();
                } else {
                    realPeriodeDebut = periodeDebutCotPeriode;
                }

                // Si la date de fin de l'affiliation est plus petite que la
                // date de fin de l'année, on prend comme date de fin de période
                // la date de fin de l'affiliation, sinon on lui met le dernier
                // jour de l'année comme date de fin de période
                if (BSessionUtil.compareDateFirstLower(getSession(), affEnCours.getDateFin(), periodeFinCotPeriode)
                        && !JadeStringUtil.isEmpty(affEnCours.getDateFin())) {
                    realPeriodeFin = affEnCours.getDateFin();
                } else {
                    realPeriodeFin = periodeFinCotPeriode;
                }

                String[] args = { realPeriodeDebut, realPeriodeFin };

                if ("fr".equalsIgnoreCase(langueIsoTiers)) {
                    super.setParametres(Doc1_PreImp_Param.P_COTISATION_PERIODE, CEDocumentItextService.formatMessage(
                            application.getProperty("libelle.cotisationMembreFR"), args));
                } else if ("de".equals(langueIsoTiers)) {
                    super.setParametres(Doc1_PreImp_Param.P_COTISATION_PERIODE, CEDocumentItextService.formatMessage(
                            application.getProperty("libelle.cotisationMembreDE"), args));
                } else if ("it".equals(langueIsoTiers)) {
                    super.setParametres(Doc1_PreImp_Param.P_COTISATION_PERIODE, CEDocumentItextService.formatMessage(
                            application.getProperty("libelle.cotisationMembreIT"), args));

                } else {
                    super.setParametres(Doc1_PreImp_Param.P_COTISATION_PERIODE, CEDocumentItextService.formatMessage(
                            application.getProperty("libelle.cotisationMembreFR"), args));
                }
            }

            super.setParametres(Doc1_PreImp_Param.L_11,
                    getSession().getApplication().getLabel("ENVOI_AF", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_12,
                    getSession().getApplication().getLabel("ENVOI_AMAT_GE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_13,
                    getSession().getApplication().getLabel("ENVOI_JOUR", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_14,
                    getSession().getApplication().getLabel("ENVOI_MOIS", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_SEXE,
                    getSession().getApplication().getLabel("SEXE_LTN", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_DATE_NAISS,
                    getSession().getApplication().getLabel("DATE_NAISSANCE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_ADRESSE_ASS,
                    getSession().getApplication().getLabel("ADRESSE", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_RUE,
                    getSession().getApplication().getLabel("RUE_NO", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_NPA, getSession().getApplication().getLabel("NPA", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_LIEU, getSession().getApplication()
                    .getLabel("LIEU", langueIsoTiers));
            super.setParametres(Doc1_PreImp_Param.L_SALAIRE_BRUTS,
                    getSession().getApplication().getLabel("SALAIRE_BRUT", langueIsoTiers));

            // faire superboucle pour nbre de colonne à afficher
            // insérer dans une clé de tri pour trier par genre
            HashMap<String, String> map = new HashMap<String, String>();

            if (application.isCCVDTraitement()) {
                if (affEnCours.getDeclarationSalaire().equals(DSProcessValidation.CS_DECL_MIXTE)) {
                    super.setParametres(Doc1_PreImp_Param.P_TEXTE_CATEGORIE_PERSONNEL,
                            application.getTexteCategoriePersonnel(langueIsoTiers));
                    if (application.isCategoriePersonne()) {
                        map.put("categorie_personne", "categorie_personne");
                    }
                    if (application.isCanton()) {
                        map.put("canton", "canton");
                    }
                    if (application.isSaisonnier()) {
                        map.put("saisonnier", "saisonnier");
                    }
                } else {
                    if (application.isCanton()) {
                        map.put("canton", "canton");
                    }
                    if (application.isSaisonnier()) {
                        map.put("saisonnier", "saisonnier");
                    }
                }
            } else {
                if (application.isCategoriePersonne()) {
                    map.put("categorie_personne", "categorie_personne");
                }
                if (application.isCanton()) {
                    map.put("canton", "canton");
                }
                if (application.isSaisonnier()) {
                    map.put("saisonnier", "saisonnier");
                }
            }

            // si 3 colonnes --> TOUTES LES COLONNES
            if (isAf.booleanValue()) {
                super.setParametres(Doc1_PreImp_Param.L_15,
                        getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
            } else if (map.size() == 3) {
                super.setParametres(Doc1_PreImp_Param.P_TEXTE_CATEGORIE_PERSONNEL,
                        application.getTexteCategoriePersonnel(langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.L_10,
                        getSession().getApplication().getLabel("ENVOI_CAT_PERSON", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.P_2,
                        getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.P_1,
                        getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.P_3,
                        getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.L_16,
                        getSession().getApplication().getLabel("ENVOI_SAISONNIER", langueIsoTiers));
                super.setParametres(Doc1_PreImp_Param.L_15,
                        getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
            } else if (map.size() == 2) {
                // si juste 2 colonnes, voir ce qui doit être affiché
                int nb = 1;

                if (map.containsKey("categorie_personne")) {
                    super.setParametres(Doc1_PreImp_Param.P_TEXTE_CATEGORIE_PERSONNEL,
                            application.getTexteCategoriePersonnel(langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.P_2,
                            getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.L_15,
                            getSession().getApplication().getLabel("ENVOI_CAT_PERSON", langueIsoTiers));
                    nb++;
                }

                if (map.containsKey("canton")) {
                    if (nb > 1) {
                        super.setParametres(Doc1_PreImp_Param.P_1,
                                getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                        super.setParametres(Doc1_PreImp_Param.L_16,
                                getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
                    } else {
                        super.setParametres(Doc1_PreImp_Param.P_2,
                                getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                        super.setParametres(Doc1_PreImp_Param.L_15,
                                getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
                        nb++;
                    }
                }

                if (map.containsKey("saisonnier")) {
                    if (nb > 1) {
                        super.setParametres(Doc1_PreImp_Param.P_1,
                                getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                        super.setParametres(Doc1_PreImp_Param.L_16,
                                getSession().getApplication().getLabel("ENVOI_SAISONNIER", langueIsoTiers));
                    }
                }
            } else if (map.size() == 1) {
                // si juste 1 colonne, voir laquelle et l'afficher
                if (map.containsKey("categorie_personne")) {
                    super.setParametres(Doc1_PreImp_Param.P_TEXTE_CATEGORIE_PERSONNEL,
                            application.getTexteCategoriePersonnel(langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.P_1,
                            getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.L_16,
                            getSession().getApplication().getLabel("ENVOI_CAT_PERSON", langueIsoTiers));
                }
                if (map.containsKey("canton")) {
                    super.setParametres(Doc1_PreImp_Param.P_1,
                            getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.L_16,
                            getSession().getApplication().getLabel("ENVOI_CANTON", langueIsoTiers));
                }
                if (map.containsKey("saisonnier")) {
                    super.setParametres(Doc1_PreImp_Param.P_1,
                            getSession().getApplication().getLabel("ENVOI_OK", langueIsoTiers));
                    super.setParametres(Doc1_PreImp_Param.L_16,
                            getSession().getApplication().getLabel("ENVOI_SAISONNIER", langueIsoTiers));
                }

            }

            if (!JadeStringUtil.isEmpty(getSession().getApplication().getProperty("textelibreDS"))) {
                if ((langueIsoTiers != null) && langueIsoTiers.equals("de")
                        && !JadeStringUtil.isEmpty(getSession().getApplication().getProperty("textelibreDSDe"))) {
                    super.setParametres("P_TEXTELIBRE", getSession().getApplication().getProperty("textelibreDSDe"));
                } else if ((langueIsoTiers != null) && langueIsoTiers.equals("it")
                        && !JadeStringUtil.isEmpty(getSession().getApplication().getProperty("textelibreDSIt"))) {
                    super.setParametres("P_TEXTELIBRE", getSession().getApplication().getProperty("textelibreDSIt"));
                } else {
                    super.setParametres("P_TEXTELIBRE", getSession().getApplication().getProperty("textelibreDS"));
                }
            } else {
                super.setParametres("P_TEXTELIBRE", "");
            }
        }
    }

    @Override
    protected void _validate() throws Exception {

        // Controle que l'année ne soit pas vide
        if (JadeStringUtil.isBlank(getAnnee()) && !imprimerVide.booleanValue()) {
            this._addError(getTransaction(), getSession().getLabel("MSG_PREIMPRESSION_ANNEE_MANQUANTE"));
        }

        // Controle que l'adresse e-mail ne soit pas vide
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("MSG_EMAILADDRESS_VIDE"));
        }

        // Controle qu'une information sur les affilies soit entrée
        if (!getAffilieTous() && JadeStringUtil.isBlank(getFromAffilies())
                && JadeStringUtil.isBlank(getUntilAffilies())) {
            this._addError(getTransaction(), getSession().getLabel("MSG_AFFILIE_VIDE"));
        }

        if (!getImprimerDeclaration().booleanValue() && !getImprimerLettre().booleanValue()) {
            this._addError(getTransaction(), getSession().getLabel("MSG_DOCUMENTS_VIDE"));
        }

        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
            setSendMailOnError(true);
        }
    }

    @Override
    public void afterBuildReport() {

        // Dans le cas ou on veut imprimer la déclaration et la lettre
        // d'accompagnement le traitement est spécial
        if (getImprimerDeclaration().booleanValue() && getImprimerLettre().booleanValue()) {

            // On regarde si c'est le dernier affilie à imprimer
            if ((i + 1) == size) {

                // si la déclaration a déjà été imprimée on sette isFirst à true
                // pour qu'on repasse encore une fois dans la boucle
                if (isPasse) {
                    isFirst = true;
                } else {
                    // dans le cas contraire on lui dit que l'impression des
                    // documents est terminée
                    isFirst = false;
                }
            } else {
                // Si ce n'est pas le dernier affilie on lui dit que
                // l'impression des documents n'est pas terminée
                isFirst = true;
            }

            // si les deux documents on été imprimés on passe à l'affilie
            // suivant en incrémentant l'indice
            if (!isPasse) {
                i = i + 1;
            }
        } else {
            // Cas ou on imprime soit les lettres d'accompagnement soit la
            // déclaration
            // On regarde si c'est le dernier affilie à imprimer
            if ((i + 1) == size) {
                // Si c'est le cas on lui dit que l'impression des documents est
                // terminée
                isFirst = false;
            } else {
                // sinon on lui dit de repasser dans la boucle et on incrémente
                // l'indice pour passer à l'affilié suivant
                isFirst = true;
                i = i + 1;
            }
        }

        // On enleve le format du numéro d'affilié.
        String affilieNum = affEnCours.getAffilieNumero();
        IFormatData affilieFormater;

        try {
            // On formate les numéros d'affiliés
            CIApplication ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            affilieFormater = ciApp.getAffileFormater();
            if (affilieFormater != null) {
                affilieNum = affilieFormater.unformat(affilieNum);
            }
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Après la création de tous les documents Par defaut ne fait rien
     */
    @Override
    public void afterExecuteReport() {
        // on fusionne la lettre et la déclaration du dernier affilié
        if (getImprimerLettre().booleanValue() && getImprimerDeclaration().booleanValue()) {

            JadePublishDocumentInfo mergedDocumentInfo = getDocumentInfo().createCopy();

            try {
                this.mergePDF(mergedDocumentInfo, true, 0, false, null);
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Erreur pour l'affilié " + affEnCours.getAffilieNumero() + ": " + e.toString(),
                        FWViewBeanInterface.ERROR, this.getClass().toString());
            }

            // on déplace le document vers une liste temporaire (car la fusion
            // est faite à partir de attachedDocuments)
            for (Iterator iter = getAttachedDocuments().iterator(); iter.hasNext();) {
                publishDocuments.add(iter.next());
            }

            getAttachedDocuments().clear();
        }

        // on recopie les documents stockés ailleurs dans la liste des documents
        // du process
        for (Iterator<?> iter = publishDocuments.iterator(); iter.hasNext();) {
            getAttachedDocuments().add(iter.next());
        }

        JadePublishDocumentInfo mergedDocumentInfo = createDocumentInfo();
        mergedDocumentInfo.setPublishDocument(true);
        mergedDocumentInfo.setArchiveDocument(false);
        mergedDocumentInfo.setDocumentProperty("annee", getAnnee());

        if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
            mergedDocumentInfo.setDocumentType("0201CDS");
            mergedDocumentInfo.setDocumentTypeNumber("0201CDS");
        } else {
            mergedDocumentInfo.setDocumentType("0089CDS");
            mergedDocumentInfo.setDocumentTypeNumber("0089CDS");
        }

        String nbDocument = "500";

        try {
            nbDocument = ((DSApplication) getSession().getApplication())
                    .getProperty("nbDocumentPourFusion", nbDocument);
        } catch (Exception e) {
            JadeLogger
                    .warn(this,
                            "Unabled to find the property \"nbDocumentPourFusion\". The default property set by the process is 100.");
            nbDocument = "100";
        }

        try {

            this.mergePDF(mergedDocumentInfo, "DeclarationSalaire", false, Integer.parseInt(nbDocument), 2, null, null);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    @Override
    public void afterPrintDocument() {
        if ((lastAffilieDocumentInfo != null) && getImprimerLettre().booleanValue()
                && getImprimerDeclaration().booleanValue() && isPasse) {

            // on vient d'imprimer la lettre, puis la déclaration: on fusionne
            // les 2 dans le même document
            JadePublishDocumentInfo mergedDocumentInfo = lastAffilieDocumentInfo.createCopy();

            try {
                this.mergePDF(mergedDocumentInfo, true, 0, false, null);
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Erreur pour l'affilié " + mergedDocumentInfo.getDocumentProperty("numero.affilie.formatte")
                                + ": " + e.toString(), FWViewBeanInterface.ERROR, this.getClass().toString());
            }

            // on déplace le document vers une liste temporaire (car la fusion
            // est faite à partir de attachedDocuments)
            for (Iterator iter = getAttachedDocuments().iterator(); iter.hasNext();) {
                publishDocuments.add(iter.next());
            }

            getAttachedDocuments().clear();
        }

        // on remplit les données du docinfo après le merge car on est déjà sur
        // l'affilié suivant
        lastAffilieDocumentInfo = getDocumentInfo();
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        if (getImprimerDeclaration().booleanValue() && getImprimerLettre().booleanValue()) {
            // Teste si la déclaration de salaires a déjà été imprimée
            if (isPasse) {

                // Si ce n'est pas le cas on lui passe le nom du template jasper
                // report
                try {
                    if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
                        MODEL_NAME = "DRACO_PREIMP_DECSAL_MASTER_LTN";
                        // setImprimerVide(new Boolean(true));
                    } else if (isAf.booleanValue()) {
                        MODEL_NAME = Doc2_preImpressionDeclaration.DOC_AF_SEUL;
                    } else {
                        MODEL_NAME = getSession().getApplication().getProperty("modelDS");
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }

                // On set isPasse à true pour dire que la déclaration a été
                // imprimée
                isPasse = false;
            } else {
                // Dans le cas où la déclaration a été imprimée on lui passe le
                // nom du template jasper report correspondant à la lettre
                MODEL_NAME = "DRACO_CONTENTIEUX_PREIMPRESSION";

                // Dès que les deux documents ont été créés on repasse la
                // variable à false
                isPasse = true;
            }
        } else if (getImprimerDeclaration().booleanValue()) {
            // Cas ou on veut uniquement imprimer la déclaration
            // Si ce n'est pas le cas on lui passe le nom du template jasper
            // report
            try {
                if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
                    MODEL_NAME = "DRACO_PREIMP_DECSAL_MASTER_LTN";
                } else if (isAf.booleanValue()) {
                    MODEL_NAME = Doc2_preImpressionDeclaration.DOC_AF_SEUL;
                    // setImprimerVide(new Boolean(true));
                } else {
                    MODEL_NAME = getSession().getApplication().getProperty("modelDS");
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        } else {
            // Cas ou on veut uniquement imprimer la lettre
            MODEL_NAME = "DRACO_CONTENTIEUX_PREIMPRESSION";
        }

        // on sette le titre du document
        super.setDocumentTitle(getSession().getLabel("ENVOI_DECL_SALAIRE"));

        // On charge le bon jasper report
        super.setTemplateFile(MODEL_NAME);
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        // Si la case tous les affiliés est séléctionnée, on éxecute la requête
        // anti-doublons.
        if (affilieTous) {
            executeStatementBeforePrint(getTransaction());
        }

        setImpressionParLot(true);
        setTailleLot(0);
        document = getICTDocument("FR");
        documentDe = getICTDocument("DE");
        documentIt = getICTDocument("IT");

        try {
            nbNiveaux = document[0].size();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("DOC_VIDE"));
        }

        IFormatData affilieFormater;

        try {
            // On formate les numéros d'affiliés
            CIApplication ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            affilieFormater = ciApp.getAffileFormater();

            if (affilieFormater != null) {
                if (!JadeStringUtil.isBlank(fromAffilies)) {
                    fromAffilies = affilieFormater.format(fromAffilies);
                }

                if (!JadeStringUtil.isBlank(untilAffilies)) {
                    untilAffilies = affilieFormater.format(untilAffilies);
                }
            }
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        // On va rechercher tous les affiliés qui correspondent à notre requête
        affManager.setSession(getSession());
        affManager.setFromAffilieNumero(fromAffilies);
        affManager.setToAffilieNumero(untilAffilies);
        affManager.setForTypeAffiliation(typeAffiliation);

        // Spécifique CCJU, tri par agence communale
        try {
            if (DSUtil.wantTriAgenceComm()) {
                affManager.setWantTriAgenceCommunale(true);
                affManager.orderByAgencePuisNumAff();
                affManager.setForSingleAdresseMode(new Boolean(true));
                affManager.setForTypeLien("507007");
            } else {
                affManager.setOrderBy("MALNAF, MADDEB");
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        // On fixe la date de début et la date de fin
        JADate datedebut = new JADate();
        JADate datefin = new JADate();
        String anneedebut = "";
        String anneeFin = "";
        int anneeDeclaration = 0;

        if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
            anneeDeclaration = new Integer(getAnnee().trim()).intValue();

            datedebut.setYear(anneeDeclaration + 1);
            datedebut.setDay(01);
            datedebut.setMonth(01);
            anneedebut = datedebut.toString();

            datefin.setYear(anneeDeclaration - 1);
            datefin.setDay(31);
            datefin.setMonth(12);
            anneeFin = datefin.toString();
        }

        // Si un type d'assurance a été sélectionné
        if (!JadeStringUtil.isEmpty(getAssuranceId())) {
            affManager.setWantAssuranceSeule(true);
            setIsAf(new Boolean(true));
            affManager.setForAssuranceSeule(getAssuranceId());
            if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
                affManager.setForFinCotisation(anneeFin);
            }
        }

        if (JadeStringUtil.isEmpty(fromAffilies) && JadeStringUtil.isEmpty(untilAffilies)) {
            if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
                affManager.setFromDateFin(anneeFin);
            }
        } else if (!fromAffilies.equals(untilAffilies)) {
            if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
                affManager.setFromDateFin(anneeFin);
            }
        }
        // Si le processus a été lancé depuis la préimpression avec un seul affilié
        if (provientEcranPreImpression && fromAffilies.equals(untilAffilies)) {
            if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
                affManager.setFromDateFin(anneeFin);
            }
        }

        if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
            affManager.setForTillDateDebut(anneedebut);
        }

        affManager.setForTypeFacturation(AFAffiliationManager.PARITAIRE);

        if (!JadeStringUtil.isEmpty(getTypeDeclaration())) {
            affManager.setForTypeDeclaration(getTypeDeclaration());
        }

        // Si on a qu'un seul num affilié, on est en mode impression unitaire
        // Donc on veut imprimer même si il est radié
        if (fromAffilies.equals(untilAffilies)) {
            affManager.setWantOnlyRadie(false);
        } else {
            // Sinon on imprime tout sauf les radiés.
            affManager.setWantOnlyRadie(true);
        }

        affManager.setWantProvisoire(false);

        try {
            affManager.find(getTransaction(), BManager.SIZE_NOLIMIT);
        } catch (Exception e2) {
            getMemoryLog().logMessage(e2.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        // On initialise la variable size qui va nous permettre de savoir
        // combien d'affiliés ont doit imprimer
        size = affManager.size();
        setProgressScaleValue(size);

        if (size == 0) {
            getMemoryLog().logMessage(getSession().getLabel("MSG_PAS_DECLARATION"), FWMessage.INFORMATION,
                    getSession().getLabel("ENVOI_TRAIT_PREIMP"));
            abort();
        }
    }

    public void bindData() throws java.lang.Exception {
        super._executeProcess();
    }

    /**
     * Cette méthode va s'assurer qu'on n'imprime pas deux fois le meme affilié
     * 
     * @throws Exception
     */
    private void controlNumero() throws Exception {

        AFAffiliation affComparaison = new AFAffiliation();

        // On met affEnCours l'employeur en cours de traitement
        affEnCours = (AFAffiliation) affManager.getEntity(ind);
        // PO Fer v 4.12 => on n'imprime pas les sans personnel, sauf les SA
        // Modif V5.3 => Si on imprime au cas par cas, on peut imprimer les sans
        // personnel
        boolean lecture = true;
        if (!fromAffilies.equals(untilAffilies)
                || (JadeStringUtil.isBlank(fromAffilies) && JadeStringUtil.isBlank(untilAffilies))) {

            while (lecture
                    && AFParticulariteAffiliation.existeParticularite(getTransaction(), affEnCours.getAffiliationId(),
                            CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, "31.12." + annee)
                    && !CodeSystem.PERS_JURIDIQUE_SA.equals(affEnCours.getPersonnaliteJuridique())) {

                affEnCours.isRadie();
                if ((ind + 1) < affManager.getSize()) {
                    ind++;
                    size--;
                    if (ind <= affManager.size()) {
                        affEnCours = (AFAffiliation) affManager.getEntity(ind);
                    }
                } else {
                    lecture = false;
                }
            }
        }

        // On teste si c'est le dernier employeur
        if ((ind + 1) < affManager.size()) {
            // Si ce n'est pas le cas, on donne à affComparaison l'employeur
            // suivant
            affComparaison = (AFAffiliation) affManager.getEntity(ind + 1);
        } else {
            // Sinon on lui passe la meme valeur que affEnCours
            affComparaison = (AFAffiliation) affManager.getEntity(ind);
        }

        // on passe l'id affiliation dans la variable
        affiliationId = affEnCours.getAffiliationId();

        // on incrémente l'indice
        ind++;

        // On met l'affilié en cours dans la variable qui nous sortira l'affilié
        // le plus grand (Dans le cas de plusieurs affiliation pour un seul
        // affilié)
        if (affEnCours.getAffilieNumero().equals(affComparaison.getAffilieNumero())) {
            affDatePlusGrand.copyDataFromEntity(affComparaison);
        } else {
            // Si on a qu'une affiliation pour l'affilié en met dans
            // affDatePlusGrand la valeur de l'affilié en cours
            affDatePlusGrand.copyDataFromEntity(affEnCours);
        }

        // Tant qu'on a deux affiliations pour un affilié et que l'indice est
        // plus petit que la taille on va rentrer dans la boucle
        while ((ind < affManager.size()) && affEnCours.getAffilieNumero().equals(affComparaison.getAffilieNumero())) {
            if (BSessionUtil.compareDateFirstGreater(getSession(), affEnCours.getDateDebut(),
                    affDatePlusGrand.getDateDebut())) {
                affDatePlusGrand.copyDataFromEntity(affEnCours);
            }

            // On diminue la taille de 1 pour dire le nombre de documents à
            // imprimer
            size--;

            // On passe dans l'affiliationId l'id en cours de traitement
            affiliationId = affiliationId.concat("," + affComparaison.getAffiliationId());

            try {
                // On copie ce qu'on a dans comparaison dans affEnCours
                affEnCours.copyDataFromEntity(affComparaison);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            }

            // On incrémente l'indice
            ind++;

            // si l'indice est plus petit que la size on passe l'enregistrement
            // suivant à affComparaison
            // sinon on les laisse avec la meme valeur
            if (ind < affManager.size()) {
                affComparaison = (AFAffiliation) affManager.getEntity(ind);
            }
        }

        setProgressDescription(affEnCours.getAffilieNumero() + "<br>" + ind + "/" + size + "<br>");
        setProgressCounter(ind);

        if (isAborted()) {
            getMemoryLog().logMessage(getSession().getLabel("MSG_PROCESSUS_ANNULE"), FWMessage.FATAL, "");
        }

        // Si la date de début de l'affiliation "active" est dans l'année de la
        // déclaration, on va regarder chercher s'il y avait une affiliation
        // active pour cette affilié dans l'année précédente
        if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                String.valueOf(JACalendar.getYear(affDatePlusGrand.getDateDebut())), getAnnee())
                || BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
                        String.valueOf(JACalendar.getYear(affDatePlusGrand.getDateDebut())),
                        String.valueOf(Integer.parseInt(getAnnee()) - 1))) {

            if (!JadeStringUtil.isBlankOrZero(getAnnee())) {
                int anneeDeclaration = new Integer(getAnnee().trim()).intValue() - 1;

                JADate datefinPlusPetit = new JADate();
                String anneefinPlusPetit = "";
                datefinPlusPetit.setYear(Integer.parseInt(getAnnee()));
                datefinPlusPetit.setDay(01);
                datefinPlusPetit.setMonth(01);
                anneefinPlusPetit = datefinPlusPetit.toString();

                JADate datefinPlusGrand = new JADate();
                String anneefinPlusGrand = "";
                datefinPlusGrand.setYear(anneeDeclaration);
                datefinPlusGrand.setDay(01);
                datefinPlusGrand.setMonth(01);
                anneefinPlusGrand = datefinPlusGrand.toString();

                AFAffiliationManager affMan = new AFAffiliationManager();
                affMan.setSession(getSession());
                affMan.setToDateFin(anneefinPlusPetit);
                affMan.setFromDateFinNonVide(anneefinPlusGrand);
                affMan.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
                affMan.setForDeclarationSalaire("807001");
                affMan.setForAffilieNumero(affDatePlusGrand.getAffilieNumero());
                affMan.forIsTraitement(false);

                affMan.find(getTransaction());

                AFAffiliation aff = null;
                for (int i = 0; i < affMan.size(); i++) {
                    aff = (AFAffiliation) affMan.getEntity(i);
                    affiliationId = affiliationId.concat("," + aff.getAffiliationId());
                }
            }
        }
    }

    @Override
    public void createDataSource() throws Exception {

        try {
            // Si l'affilie est sans personnel et n'est pas une SA => on sort
            // Si on imprime la déclaration et la lettre il ne faut pas changer
            // d'affilié quand on imprime la lettre
            if ((imprimerDeclaration.booleanValue() && !imprimerLettre.booleanValue())
                    || (!imprimerDeclaration.booleanValue() && imprimerLettre.booleanValue())
                    || (imprimerDeclaration.booleanValue() && imprimerLettre.booleanValue() && !isPasse)) {
                controlNumero();
            }

            getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affEnCours.getAffilieNumero());
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);

            String numAffNonFormatte = formatNumAffilie(affEnCours.getAffilieNumero());

            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAffNonFormatte);
            TIDocumentInfoHelper.fill(getDocumentInfo(), affEnCours.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    affEnCours.getAffilieNumero(), numAffNonFormatte);

            if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
                getDocumentInfo()
                        .setBarcode(Doc2_preImpressionDeclaration.NUM_DOCUMENT_LTN + annee + numAffNonFormatte);
            } else {
                getDocumentInfo().setBarcode(Doc2_preImpressionDeclaration.NUM_DOCUMENT + annee + numAffNonFormatte);
            }
            // BZ 7360
            if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
                getDocumentInfo().setDocumentTypeNumber("0201CDS");
            } else {
                if (isPasse) {
                    getDocumentInfo().setDocumentTypeNumber("0089CDS");
                } else {
                    getDocumentInfo().setDocumentTypeNumber("0088CDS");
                }
            }

            getDocumentInfo().setDocumentProperty("annee", annee);

            if (!isPasse) {
                if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation())) {
                    MODEL_NAME = "DRACO_PREIMP_DECSAL_MASTER_LTN";
                } else {
                    MODEL_NAME = getSession().getApplication().getProperty("modelDS");
                }
            }

            // Si on imprime la lettre et que la case d'inscription dans la
            // gestion des envois est cochée,
            // on démarre le processus d'inscription de l'envoi de la lettre
            // dans LEO
            if (imprimerLettre.booleanValue() && demarreSuivi.booleanValue() && !isPasse) {
                if (isDejaJournalise(affDatePlusGrand)) {
                    getMemoryLog().logMessage(getSession().getLabel("ENVOI_ALREADY_EXISTS"), "", FWMessage.INFORMATION);
                } else {
                    if (imprimerReceptionnees.booleanValue()) {
                        if (!existsDSReceptionnnee(affEnCours.getAffiliationId(), annee)) {
                            genererControle(affDatePlusGrand);
                        }
                    } else {
                        genererControle(affDatePlusGrand);
                    }
                }
            }

            langueIsoTiers = affEnCours.getTiers().getLangueIso();

            // Si on doit imprimer la lettre et la déclaration
            // setImpressionParLot(true);
            if (getImprimerLettre().booleanValue() && getImprimerDeclaration().booleanValue()) {
                // si on a pas encore créé la déclaration on la créé
                if (!isPasse) {
                    createLettrePreImpression();
                    // setImpressionParLot(false);
                } else {
                    // dans le cas ou la déclaration a déjà été créée on créée
                    // la lettre
                    createPreImpression();
                }
            } else if (getImprimerDeclaration().booleanValue()) {
                // si on doit imprimer uniquement la déclaration
                createPreImpression();
            } else {
                // si on doit imprimer uniquement la lettre d'accompagnement
                createLettrePreImpression();
            }

            _headerText();
            _tableHeader();
            _footerText();

            // Modif jmc 04.01.2007, premier passage l'adresses était écrasée!
            if ((isPasse && imprimerDeclaration.booleanValue())
                    || (imprimerDeclaration.booleanValue() && !imprimerLettre.booleanValue())) {
                setTemplateFile(MODEL_NAME);
                ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                        getDocumentInfo(), getSession().getApplication(), langueIsoTiers);
                CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

                _setHeader(headerBean);
                caisseReportHelper.addHeaderParameters(this, headerBean);

                DSApplication application = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                        DSApplication.DEFAULT_APPLICATION_DRACO);
                if (application.isCCVDTraitement()
                        && affEnCours.getDeclarationSalaire().equals(DSProcessValidation.CS_DECL_MIXTE)) {
                    getImporter().getParametre().put(
                            ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                            ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                                    + getTemplateProperty(getDocumentInfo(), "header.filename.declaration2"));
                } else {
                    getImporter().getParametre().put(
                            ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                            ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                                    + getTemplateProperty(getDocumentInfo(), "header.filename.declaration"));
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

    }

    private String formatNumAffilie(String numAffilie) throws Exception {
        String numAffNonFormatte;
        IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();

        try {
            numAffNonFormatte = affilieFormater.unformat(numAffilie);
        } catch (Exception e) {
            numAffNonFormatte = numAffilie;
        }
        return numAffNonFormatte;
    }

    // Permet de créer la lettre d'accompagnement
    public void createLettrePreImpression() throws Exception {
        Doc1_PreImpLettre_DS managerLettre = new Doc1_PreImpLettre_DS();

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoTiers);

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        headerBean.setAdresse(affEnCours.getTiers().getAdresseAsString(getDocumentInfo(),
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, DSApplication.CS_DOMAINE_DECLARATION_SALAIRES,
                JACalendar.todayJJsMMsAAAA(), affEnCours.getAffilieNumero()));

        setTemplateFile(MODEL_NAME);
        _setHeader(headerBean);

        caisseReportHelper.addHeaderParameters(this, headerBean);

        DSApplication application = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                DSApplication.DEFAULT_APPLICATION_DRACO);

        if (application.isCCVDTraitement()
                && affEnCours.getDeclarationSalaire().equals(DSProcessValidation.CS_DECL_MIXTE)) {
            getImporter().getParametre().put(
                    ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                    ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), "header.filename.lettre.decl"));
        } else if (!JadeStringUtil.isBlankOrZero(application.getHeaderGris())) {
            getImporter().getParametre().put(
                    ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                    ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                            + application.getHeaderGris());
        }

        // différemment le createDataSource
        super.setDataSource(managerLettre.getCollectionData());
    }

    // Permet de créer la déclaration de salaires
    public void createPreImpression() throws Exception {
        manager = new Doc1_PreImpr_DS();
        manager.setSession(getSession());
        manager.setContainer();
        manager.moveFirst();
        manager.setForAffiliesNumero(affiliationId);
        manager.setAnnee(annee);
        manager.setIsAf(isAf);

        // Pour affilié ltn => toujours imprimer vide
        if (CodeSystem.TYPE_AFFILI_LTN.equals(affEnCours.getTypeAffiliation()) || imprimerVide.booleanValue()
                || isAf.booleanValue()) {
            manager.setImprimerVide(new Boolean(true));
        } else {
            manager.setImprimerVide(new Boolean(false));
        }

        manager.setConvertnnss(getConvertnnss());
        boolean orderByName = false;

        if (getConvertnnss().booleanValue() || DSUtil.isNNSSActif(getSession(), JACalendar.todayJJsMMsAAAA())) {
            orderByName = true;
        }

        manager.setDateProdNNSS(orderByName);

        // On charge les valeurs du manager dans le data source
        super.setDataSource(manager.getCollectionData());

        try {
            // On charge les valeurs dans le document
            super.setParametres("SUBREPORT_SOURCE", new JRBeanCollectionDataSource(manager.getCollectionData()));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Execute le statement pour la pré-impression
     * 
     * @param transaction
     */
    private void executeStatementBeforePrint(BTransaction transaction) {
        BStatement statement = new BStatement(transaction);

        try {
            DSSqlStringForStatement sql = new DSSqlStringForStatement();
            statement.createStatement();
            statement.execute(sql.getSqlForPreImpression(annee));

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
                getTransaction().clearErrorBuffer();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            statement.closeStatement();
        }
    }

    /**
     * Méthode qui définit s'il existe une coti de type cotisation période pour une année
     * 
     * @return
     * @throws Exception
     */
    public boolean existeCotisationPeriode() throws Exception {
        // Uniquement pour l'année 2011
        AFCotisationManager cotiMgr = new AFCotisationManager();

        cotiMgr.setSession(getSession());
        cotiMgr.setForAffiliationId(affEnCours.getAffiliationId());
        cotiMgr.setForAnneeActive("2011");
        cotiMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_PC_FAMILLE);

        return (cotiMgr.getCount() > 0) && "2011".equals(annee);
    }

    private boolean existsDSReceptionnnee(String idAffilie, String annee) throws Exception {
        DSDeclarationListViewBean mgr = new DSDeclarationListViewBean();

        mgr.setSession(getSession());
        mgr.setForAffiliationId(idAffilie);
        mgr.setForAnnee(annee);
        mgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);

        return mgr.getCount() > 0;
    }

    private String format(String paragraphe) throws Exception {
        String res = "";

        for (int i = 0; i < paragraphe.length(); i++) {
            if (paragraphe.charAt(i) != '{') {
                res += paragraphe.charAt(i);
            } else if (paragraphe.charAt(i + 1) == '0') {
                res += " " + annee;
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '2') {
                BigDecimal anneeBig = new BigDecimal(annee);
                anneeBig = anneeBig.add(new BigDecimal("1"));
                res += " " + anneeBig.toString();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '3') {
                // par les tiers !!
                String titre = affEnCours.getTiers().getFormulePolitesse(affEnCours.getTiers().getLangue());
                if (JadeStringUtil.isEmpty(titre)) {
                    titre = getSession().getApplication().getLabel("MADAME_MONSIEUR",
                            affEnCours.getTiers().getLangueIso());
                }

                res += titre;
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '4') {
                String agenceComm = affEnCours
                        .getAgenceCom(affEnCours.getAffiliationId(), JACalendar.todayJJsMMsAAAA());
                res += agenceComm;
                i = i + 2;
            }
        }
        return res;
    }

    public void genererControle(AFAffiliation affiliation) throws Exception {

        TIRoleManager roleMng = new TIRoleManager();
        roleMng.setSession(getSession());
        roleMng.setForIdTiers(affiliation.getIdTiers());
        roleMng.setForRole(TIRole.CS_BLOQUER_ENVOI);
        roleMng.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        if (roleMng.getCount() > 0) {
            // PO 8865 Avertir dans mail que l'utilisateur que l'affilié est bloqué dans les envois
            String msg = getSession().getLabel("DS_BLOQUE").trim() + " " + affiliation.getAffilieNumero() + ".";
            if ("D".equalsIgnoreCase(getSession().getIdLangue())) {
                msg += " " + getSession().getLabel("DS_BLOQUE1").trim();
            }

            getMemoryLog().logMessage(msg, FWMessage.AVERTISSEMENT, "");
        } else {
            // prépare les données pour l'envoi
            HashMap<String, String> params = new HashMap<String, String>();

            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getTiers().getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, DSApplication.DEFAULT_APPLICATION_DRACO);
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affiliation.getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

            // execute le process de génération
            LEGenererEnvoi gen = new LEGenererEnvoi();

            gen.setSession(getSession());
            gen.setParamEnvoiList(params);
            gen.setSendCompletionMail(false);
            gen.setGenerateEtapeSuivante(new Boolean(true));
            gen.setDateCreation(getDateValeur());

            if (CodeSystem.TYPE_AFFILI_LTN.equals(affiliation.getTypeAffiliation())) {
                gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_DS_LTN);
            } else {
                gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_DS);
            }

            gen.executeProcess();
        }
    }

    /**
     * Retourne un booleen qui nous dit si on doit imprimer les déclarations pour tous les affilies
     * 
     * @return affilieTous
     */
    public boolean getAffilieTous() {
        return affilieTous;
    }

    /**
     * Retourne l'année
     * 
     * @return annee
     */
    public String getAnnee() {
        return annee;
    }

    public String getAssuranceId() {
        return assuranceId;
    }

    public Boolean getConvertnnss() {
        return convertnnss;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * @return
     */
    public Boolean getDemarreSuivi() {
        return demarreSuivi;
    }

    public TITiers getDescriptionCaisse(String caisse) throws Exception {
        // On va rechercher dans les tiers la désignation et la localité de
        // l'institution
        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(caisse);
        tiers.retrieve();

        return tiers;
    }

    @Override
    protected String getEMailObject() {
        StringBuilder buffer = new StringBuilder();

        if (isOnError() || isAborted()) {
            buffer.append(getSession().getLabel("ENVOI_IMPRE_ERREUR"));
        } else {
            if (size == 1) {
                buffer.append(getSession().getLabel("ENVOI_DECL_EMPLOY") + " ");
                buffer.append(affEnCours.getAffilieNumero());
            } else {
                buffer.append(size);
                buffer.append(" " + getSession().getLabel("ENVOI_EMPL_SELECT"));
            }
        }

        return buffer.toString();
    }

    /**
     * Retourne le premier affilié à renvoyer
     * 
     * @return fromAffilies
     */
    public String getFromAffilies() {
        return fromAffilies;
    }

    private ICTDocument[] getICTDocument(String langue) {
        ICTDocument res[] = null;
        ICTDocument ictDocument = null;

        try {
            ictDocument = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_API"));
        }

        ictDocument.setISession(getSession());

        if (!JadeStringUtil.isEmpty(getIdDocument())) {
            ictDocument.setIdDocument(getIdDocument());
        } else {
            ictDocument.setIdDocument(getIdDocumentDefaut());
        }

        ictDocument.setCsDomaine(Doc2_preImpressionDeclaration.CS_DOMAINE);
        ictDocument.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_ATTESTATION);
        ictDocument.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        ictDocument.setCodeIsoLangue(langue);
        ictDocument.setActif(new Boolean(true));

        try {
            res = ictDocument.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_DOC"));
        }

        return res;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdDocumentDefaut() {
        return idDocumentDefaut;
    }

    /**
     * Retourne un booleen qui nous dit si on doit imprimer la déclaration
     * 
     * @return imprimerDeclaration
     */
    public Boolean getImprimerDeclaration() {
        return imprimerDeclaration;
    }

    /**
     * Retourne un booleen qui nous dit si on doit imprimer une lettre
     * 
     * @return imprimerLettre
     */
    public Boolean getImprimerLettre() {
        return imprimerLettre;
    }

    public Boolean getImprimerReceptionnees() {
        return imprimerReceptionnees;
    }

    /**
     * @return
     */
    public Boolean getImprimerVide() {
        return imprimerVide;
    }

    public Boolean getIsAf() {
        return isAf;
    }

    public Boolean getProvientEcranPreImpression() {
        return provientEcranPreImpression;
    }

    private String getTexte(int niveau, String langue) throws Exception {
        String resString = "";

        if ((langue != null) && "de".equals(langue)) {
            resString = getTexteAllemand(niveau);
        } else if ((langue != null) && "it".equals(langue)) {
            resString = getTexteItalien(niveau);
        } else {
            resString = getTexteFrancais(niveau);
        }

        return format(resString);
    }

    private String getTexteFrancais(int niveau) {

        String resString = "";
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            return resString;
        }

        ICTTexte texte;
        ICTListeTextes listeTextes = null;
        try {
            listeTextes = document[0].getTextes(niveau);
        } catch (Exception e) {
            // JadeLogger.warn(this, e.getMessage());
        }

        if (listeTextes != null) {
            for (int i = 0; i < listeTextes.size(); i++) {
                texte = listeTextes.getTexte(i + 1);
                if ((i + 1) < listeTextes.size()) {
                    resString = resString.concat(texte.getDescription() + "\n\n");
                } else {
                    resString = resString.concat(texte.getDescription());
                }
            }
        }

        return resString;
    }

    private String getTexteItalien(int niveau) {
        ICTTexte texte;
        String resString = "";
        if (documentIt == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;

            try {
                listeTextes = documentIt[0].getTextes(niveau);
            } catch (Exception e) {
                // JadeLogger.warn(this, e.getMessage());
            }

            if (listeTextes != null) {
                for (int i = 0; i < listeTextes.size(); i++) {
                    texte = listeTextes.getTexte(i + 1);
                    if ((i + 1) < listeTextes.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }
        return resString;
    }

    private String getTexteAllemand(int niveau) {
        ICTTexte texte;
        String resString = "";
        if (documentDe == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;

            try {
                listeTextes = documentDe[0].getTextes(niveau);
            } catch (Exception e) {
                // JadeLogger.warn(this, e.getMessage());
            }

            if (listeTextes != null) {
                for (int i = 0; i < listeTextes.size(); i++) {
                    texte = listeTextes.getTexte(i + 1);
                    if ((i + 1) < listeTextes.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }
        return resString;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    /**
     * Retourne le dernier affilie à renvoyer
     * 
     * @return untilAffilies
     */
    public String getUntilAffilies() {
        return untilAffilies;
    }

    private boolean isDejaJournalise(AFAffiliation aff) throws Exception {
        // On sette les critères qui font que l'envoi est unique
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();

        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                DSApplication.DEFAULT_APPLICATION_DRACO);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, aff.getAffilieNumero());
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, annee);

        LUJournalListViewBean viewBean = new LUJournalListViewBean();

        viewBean.setSession(getSession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);

        if (CodeSystem.TYPE_AFFILI_LTN.equals(aff.getTypeAffiliation())) {
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS_LTN);
        } else {
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
        }

        viewBean.find(getTransaction(), BManager.SIZE_USEDEFAULT);

        // Si le viewBean retourne un enregistrement c'est que l'envoi a déjà
        // été journalisé donc on retourne true
        if (viewBean.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isProvientEcranPreImpression() {
        return provientEcranPreImpression;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        // si First est à true on va au suivant sinon on s'arrête
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    /**
     * Sette un booleen qui nous dit si on doit imprimer les déclarations pour tous les affilies
     * 
     * @param b
     */
    public void setAffilieTous(boolean b) {
        affilieTous = b;
    }

    /**
     * Sette l'année
     * 
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    public void setAssuranceId(String assuranceId) {
        this.assuranceId = assuranceId;
    }

    public void setConvertnnss(Boolean convertnnss) {
        this.convertnnss = convertnnss;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * @param b
     */
    public void setDemarreSuivi(Boolean b) {
        demarreSuivi = b;
    }

    /**
     * Sette le premier affilié à renvoyer
     * 
     * @param string
     */
    public void setFromAffilies(String string) {
        fromAffilies = string;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdDocumentDefaut(String idDocumentDefaut) {
        this.idDocumentDefaut = idDocumentDefaut;
    }

    /**
     * Sette un booleen qui nous dit si on doit imprimer la déclaration
     * 
     * @param b
     */
    public void setImprimerDeclaration(Boolean b) {
        imprimerDeclaration = b;
    }

    /**
     * Sette un booleen qui nous dit si on doit imprimer la lettre
     * 
     * @param b
     */
    public void setImprimerLettre(Boolean b) {
        imprimerLettre = b;
    }

    public void setImprimerReceptionnees(Boolean imprimerReceptionnees) {
        this.imprimerReceptionnees = imprimerReceptionnees;
    }

    /**
     * @param boolean1
     */
    public void setImprimerVide(Boolean boolean1) {
        imprimerVide = boolean1;
    }

    public void setIsAf(Boolean isAf) {
        this.isAf = isAf;
    }

    public void setProvientEcranPreImpression(Boolean provientEcranPreImpression) {
        this.provientEcranPreImpression = provientEcranPreImpression;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    public void setUntilAffilies(String string) {
        untilAffilies = string;
    }

}
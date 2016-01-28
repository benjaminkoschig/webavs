package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.topaz.REStatistiqueOFASOO;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.naos.process.statOfas.AFStatistiquesOfasProcess;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import statofas.REStatOFAS;
import statofas.REStatOFASDemandeACalculer;
import statofas.REStatOFASDemandeACalculerManager;
import statofas.REStatOFASDemandeACalculerSansRenteAccordeManager;
import statofas.REStatOFASManager;

/**
 * @author PCA
 */
public class REGenererStatOFASProcess extends AbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JadePrintDocumentContainer allDoc;
    private String anneeStatistiqueOFAS = "";
    private int casAI = 0;
    private int casAVS = 0;
    private String emailAdresse = "";
    private LinkedList<REStatOFASDemandeACalculer> listeDemandeEnregistrer = new LinkedList<REStatOFASDemandeACalculer>();
    private List<REDemandeRenteVieillesse> listeDemandeRenteAnticipe = new ArrayList<REDemandeRenteVieillesse>();
    private FWCurrency montant = new FWCurrency("0.00");
    private Map<String, String> statistiqueRAAnticipe = new TreeMap<String, String>();
    private int totalDecisionIM = 0;

    public REGenererStatOFASProcess() {
        super();
    }

    protected void _executeCleanUp() {
    }

    private REStatistiqueOFASOO createStatistiqueOFASDocument() throws Exception {

        REStatistiqueOFASOO statDocument = new REStatistiqueOFASOO();
        statDocument.setSession(getSession());
        statDocument.setStatistiqueRAAnticipe(demandeVieillesseAjourneeSansRevocation());
        demandeVieillesseAjourneeACalculer();
        statDocument.setListeDemandeEnregistrer(listeDemandeEnregistrer);

        AFStatistiquesOfasProcess ofasProcess = new AFStatistiquesOfasProcess();

        statDocument.setDemPrevTaxe(Integer.parseInt(ofasProcess.getCalculAnticipeRente(anneeStatistiqueOFAS,
                getSession())));
        // statDocument.setDemPrevTaxe(this.demandePrevisionnelTaxeprelevee());

        statDocument.setAnneeStatOFAS(getAnneeStatistiqueOFAS());
        statDocument.setIntMorMontant(montant);
        decisionsValidees();
        statDocument.setTotalDecision(totalDecisionIM);
        statDocument.setDecisionAI(casAI);
        statDocument.setDecisionsAVS(casAVS);
        statDocument.generationDocument();

        return statDocument;
    }

    public void decisionsValidees() throws Exception {

        REDecisionsManager dm = new REDecisionsManager();
        dm.setSession(getSession());
        dm.setForCsEtat(IREDecision.CS_ETAT_VALIDE);
        dm.setForValideDes("0101" + getAnneeStatistiqueOFAS());
        dm.setForValideJusqua("3112" + getAnneeStatistiqueOFAS());
        dm.find(getTransaction(), BManager.SIZE_NOLIMIT);

        List<String> listeDecisionDejaTraiter = new ArrayList<String>();

        for (int i = 0; i < dm.size(); i++) {

            REDecisionEntity de = (REDecisionEntity) dm.get(i);

            if (PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(de.getDateValidation()).equals(getAnneeStatistiqueOFAS())) {

                REPrestationsManager prm = new REPrestationsManager();
                prm.setSession(getSession());
                prm.setForIdDecision(de.getIdDecision());
                prm.find();

                if (!prm.isEmpty()) {
                    for (int j = 0; j < prm.size(); j++) {

                        REPrestations pr = (REPrestations) prm.get(j);

                        REOrdresVersementsManager ovm = new REOrdresVersementsManager();
                        ovm.setSession(getSession());
                        ovm.setForIdPrestation(pr.getIdPrestation());
                        ovm.find();

                        if (!ovm.isEmpty()) {
                            for (int k = 0; k < ovm.size(); k++) {

                                REOrdresVersements ov = (REOrdresVersements) ovm.get(k);

                                if (IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE.equals(ov.getCsType())) {

                                    REDemandeRente dr = new REDemandeRente();
                                    dr.setSession(getSession());
                                    dr.setIdDemandeRente(de.getIdDemandeRente());
                                    dr.retrieve();

                                    if (dr.getCsTypeDemandeRente().equals(
                                            IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)
                                            || dr.getCsTypeDemandeRente().equals(
                                                    IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {
                                        if (!"0.00".equals(ov.getMontant())) {
                                            montant.add(new FWCurrency(ov.getMontant()));
                                            if (!listeDecisionDejaTraiter.contains(de.getIdDecision())) {
                                                listeDecisionDejaTraiter.add(de.getIdDecision());
                                                casAVS++;
                                                totalDecisionIM++;
                                            }
                                        }

                                    } else if (dr.getCsTypeDemandeRente().equals(
                                            IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {
                                        if (!"0.00".equals(ov.getMontant())) {
                                            montant.add(new FWCurrency(ov.getMontant()));
                                            if (!listeDecisionDejaTraiter.contains(de.getIdDecision())) {
                                                listeDecisionDejaTraiter.add(de.getIdDecision());
                                                casAI++;
                                                totalDecisionIM++;
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int demandePrevisionnelTaxeprelevee() throws Exception {
        REDemandeRenteJointDemandeManager drm = new REDemandeRenteJointDemandeManager();
        drm.setForCsTypeCalcul(IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL);
        drm.setSession(getSession());
        drm.find();

        int compteur = 0;
        for (int i = 0; i < drm.size(); i++) {
            REDemandeRenteJointDemande dr = (REDemandeRenteJointDemande) drm.get(i);
            // En cours pour l'année si l'année de la date de reception est la
            // même que celle entrée
            // par l'utilisateur dans l'ecran !
            if (getAnneeStatistiqueOFAS().equals(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dr.getDateReception()))) {
                compteur++;
            }
        }
        return compteur;
    }

    public void demandeVieillesseAjourneeACalculer() throws Exception {
        REStatOFASDemandeACalculerManager statOFASManager = new REStatOFASDemandeACalculerManager();
        statOFASManager.setSession(getSession());

        // this.listeDemandeEnregistrer ;
        // REDEVIE.YCBAJR = 2 + REDEREN.YATETA 1/2/3 + REPRACC.ZTTETA 5 ET
        // REDEVIE.YCBAJR = 2 + REDEREN.YATETA >4 + REPRACC.ZTTETA 5
        // Aucune demande d'ajournement
        statOFASManager.setForCsEtatRentePrestationAccordee(IREPrestationAccordee.CS_ETAT_AJOURNE);
        statOFASManager.setForCsAnneeStatOFAS(anneeStatistiqueOFAS);
        statOFASManager.setIsEqualRentePrestationAccordee(true);
        statOFASManager.setForCsEtatRenteDeVieillesse("2");

        statOFASManager.find();
        this.remplirListeDemandeEnregistrer(statOFASManager,
                getSession().getLabel("OFAS_DEMANDE_VIEILLESSE_AUCUNE_DEMANDE"));

        // -----------------------------------------------------------------------------------------
        // REDEVIE.YCBAJR = 1 + REDEREN.YATETA >4 + REPRACC.ZTTETA 5
        // Demande validée
        statOFASManager.setForCsEtatRentePrestationAccordee(IREPrestationAccordee.CS_ETAT_AJOURNE);
        statOFASManager.setForCsAnneeStatOFAS(anneeStatistiqueOFAS);
        statOFASManager.setIsEqualRentePrestationAccordee(true);
        statOFASManager.setForCsEtatRenteDeVieillesse("1");

        List<String> forCsEtatDemandeIn = new ArrayList<String>();
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
        statOFASManager.setForCsEtatDemandeIn(forCsEtatDemandeIn);

        statOFASManager.find();
        this.remplirListeDemandeEnregistrer(statOFASManager, getSession().getLabel("OFAS_DEMANDE_VIEILLESSE_VALIDEE"));

        // -----------------------------------------------------------------------------------------
        // REDEVIE.YCBAJR = 1 - REDEREN.YATETA 1/2/3 - REPRACC.ZTTETA !5
        // Aucune rente ajournée
        statOFASManager.setForCsEtatRentePrestationAccordee(IREPrestationAccordee.CS_ETAT_AJOURNE);
        statOFASManager.setIsEqualRentePrestationAccordee(false);
        statOFASManager.setForCsAnneeStatOFAS(anneeStatistiqueOFAS);
        statOFASManager.setForCsEtatRenteDeVieillesse("1");

        forCsEtatDemandeIn = new ArrayList<String>();
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
        statOFASManager.setForCsEtatDemandeIn(forCsEtatDemandeIn);

        statOFASManager.find();
        this.remplirListeDemandeEnregistrer(statOFASManager,
                getSession().getLabel("OFAS_DEMANDE_VIEILLESSE_AUCUNE_RENTE"));

        // --------------------------------------------
        REStatOFASDemandeACalculerSansRenteAccordeManager statOFASSansRenteAccordeManager = new REStatOFASDemandeACalculerSansRenteAccordeManager();

        statOFASSansRenteAccordeManager.setSession(getSession());
        statOFASSansRenteAccordeManager.setForCsEtatDemandeIn(forCsEtatDemandeIn);
        statOFASSansRenteAccordeManager.setForCsEtatRenteDeVieillesse("1");

        statOFASSansRenteAccordeManager.find();

        this.remplirListeDemandeEnregistrer(statOFASSansRenteAccordeManager,
                getSession().getLabel("OFAS_DEMANDE_VIEILLESSE_AUCUN_CALCUL_RENTE"));

    }

    public Map<String, String> demandeVieillesseAjourneeSansRevocation() throws Exception {

        // Compter toutes les demandes de vieillesse ajournées dont le champ de
        // la demande "Ajournement" est à "oui", le champ "AjournementRequerant est à oui et dont l'état de la demande
        // de rente est enregistré, au calcul ou calculé.
        statistiqueRAAnticipe = new TreeMap<String, String>();

        REStatOFASManager statOFASManager = new REStatOFASManager();
        statOFASManager.setSession(getSession());

        List<String> forCsEtatDemandeIn = new ArrayList<String>();
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL);
        forCsEtatDemandeIn.add(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
        statOFASManager.setForCsEtatDemandeIn(forCsEtatDemandeIn);

        statOFASManager.setForCsEtatRenteDeVieillesse("1");
        statOFASManager.setForCsEtatRentePrestationAccordee(IREPrestationAccordee.CS_ETAT_AJOURNE);
        statOFASManager.setForCsAnneeStatOFAS(anneeStatistiqueOFAS);

        statOFASManager.find();

        for (int i = 0; i < statOFASManager.getSize(); i++) {
            REStatOFAS statOFAS = (REStatOFAS) statOFASManager.get(i);
            statistiqueRAAnticipe.put(statOFAS.getAnnee(), statOFAS.getComptage());
        }

        // Si une année manque, on l'ajoute avec un 0 dans les stats
        for (int i = 0; i < 5; i++) {
            if (!statistiqueRAAnticipe.containsKey(Integer.toString(Integer.parseInt(getAnneeStatistiqueOFAS()) - i))) {
                statistiqueRAAnticipe.put(Integer.toString(Integer.parseInt(getAnneeStatistiqueOFAS()) - i), "0");
            }
        }

        return statistiqueRAAnticipe;
    }

    public String getAnneeStatistiqueOFAS() {
        return anneeStatistiqueOFAS;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("PROCESS_TITRE");
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    @Override
    public String getName() {
        return getSession().getLabel("TPROCESS_TITRE");
    }

    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private void remplirListeDemandeEnregistrer(REStatOFASDemandeACalculerManager statOFASManager, String label) {
        for (int i = 0; i < statOFASManager.getSize(); i++) {
            REStatOFASDemandeACalculer statOFAS = (REStatOFASDemandeACalculer) statOFASManager.get(i);
            statOFAS.setMessageOO(label);
            listeDemandeEnregistrer.add(statOFAS);
        }
    }

    private void remplirListeDemandeEnregistrer(REStatOFASDemandeACalculerSansRenteAccordeManager statOFASManager,
            String label) {
        for (int i = 0; i < statOFASManager.getSize(); i++) {
            REStatOFASDemandeACalculer statOFAS = (REStatOFASDemandeACalculer) statOFASManager.get(i);
            statOFAS.setMessageOO(label);
            listeDemandeEnregistrer.add(statOFAS);
        }
    }

    @Override
    public void run() {
        try {

            allDoc = new JadePrintDocumentContainer();

            JadePublishDocumentInfo pubInfosDestination = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfosDestination.setOwnerEmail(getEmailAdresse());
            pubInfosDestination.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubInfosDestination.setDocumentTitle(getSession().getLabel("STATOFAS_PROCESS_TITRE"));
            pubInfosDestination.setDocumentSubject(getSession().getLabel("STATOFAS_PROCESS_TITRE"));
            pubInfosDestination.setArchiveDocument(false);
            pubInfosDestination.setPublishDocument(true);

            allDoc.setMergedDocDestination(pubInfosDestination);

            JadePublishDocumentInfo pubInfos = JadePublishDocumentInfoProvider.newInstance(this);
            pubInfos.setDocumentTitle(getSession().getLabel("STATOFAS_PROCESS_TITRE"));
            pubInfos.setDocumentSubject(getSession().getLabel("STATOFAS_PROCESS_TITRE"));
            pubInfos.setOwnerEmail(getEmailAdresse());
            pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdresse());
            pubInfos.setArchiveDocument(false);
            pubInfos.setPublishDocument(false);
            pubInfos.setDocumentType(IRENoDocumentInfoRom.LISTE_STATISTIQUES_OFAS);
            pubInfos.setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_STATISTIQUES_OFAS);
            pubInfos.setDocumentDate(getAnneeStatistiqueOFAS()); // ou la date
            // du jours,
            // selon les
            // cas

            REStatistiqueOFASOO statofasOO = createStatistiqueOFASDocument();

            allDoc.addDocument(statofasOO.getDocumentData(), pubInfos);

            this.createDocuments(allDoc);

        } catch (Exception e) {
            getLogSession().addMessage(
                    new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, "REStatistiqueOFASOO", e.toString()));
        } finally {
            try {
                if (getLogSession().hasMessages()) {
                    sendCompletionMail(new ArrayList<String>() {
                        /**
                         * 
                         */
                        private static final long serialVersionUID = 1L;

                        {
                            this.add(REGenererStatOFASProcess.this.getEmailAdresse());
                        }
                    });
                }
            } catch (Exception e) {
                System.out.println(getSession().getLabel("ERREUR_ENVOI_MAIL_COMPLETION"));
            }
        }
    }

    public void setAnneeStatistiqueOFAS(String anneeStatistiqueOFAS) {
        this.anneeStatistiqueOFAS = anneeStatistiqueOFAS;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }
}

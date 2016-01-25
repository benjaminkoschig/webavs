/*
 * Créé le 8 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.db.envoi.LEEtapesSuivantesViewBean;
import globaz.leo.process.LEGenererEnvoi;
import globaz.leo.process.LEGenererListeFormulesEnAttente;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.leo.process.handler.LEGenererEtapeHandler;
import globaz.leo.process.handler.LEJournalHandler;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.releve.AFGestionReleveManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIRole;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Permet de générer un rappel, une sommation ou une taxation d'office
 * 
 * @author sda
 */
public class AFGestionReleveProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génère une journalisation dans LEO
     * 
     * @param affiliation
     * @parma plan
     * @param periode
     * @throws Exception
     */
    public static void genererControle(AFAffiliation affiliation, AFPlanAffiliation plan, String periode)
            throws Exception {
        // prépare les données pour l'envoi
        if (affiliation != null) {
            HashMap params = new HashMap();
            params.put(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, affiliation.getAffiliationId());
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS, affiliation.getTiers().getIdTiers());
            params.put(ILEConstantes.CS_PARAM_GEN_NUMERO, affiliation.getAffilieNumero());
            params.put(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
            params.put(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE, affiliation.getSession().getApplicationId());
            params.put(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, affiliation.getIdTiers());
            String periodeDebutFin = AFGestionReleveProcess.getPeriodiciteAReclamer(affiliation, periode);
            params.put(ILEConstantes.CS_PARAM_GEN_PERIODE, periodeDebutFin);
            params.put(ILEConstantes.CS_PARAM_GEN_PLAN, plan.getLibelleFactureNotEmpty());

            BIApplication remoteApplication = GlobazServer.getCurrentSystem().getApplication("LEO");

            BSession sessionLeo = (BSession) remoteApplication.newSession(affiliation.getSession());
            // execute le process de génération
            LEGenererEnvoi gen = new LEGenererEnvoi();
            gen.setSession(sessionLeo);
            gen.setParamEnvoiList(params);
            gen.setSendCompletionMail(false);
            gen.setGenerateEtapeSuivante(new Boolean(false));
            gen.setCsDocument(ILEConstantes.CS_DEBUT_SUIVI_RELEVES);
            // gen.start();
            gen.executeProcess();
        }
    }

    /**
     * Permet d'avoir la périodicité à réclamer
     * 
     * @param periode
     * @return
     */
    private static String getPeriodicite(String periode) {
        // Cas ou on doit réclamer un relevé mensuel
        String[] mensuel = { "1", "2", "4", "5", "7", "8", "10", "11" };
        // Cas ou on doit réclamer un relevé mensuel ou trimestriel
        String[] mensuelTrimestriel = { "3", "6", "9" };
        String res = null;
        int i = 0;
        boolean trouve = false;
        // On parcours les cas ou un relevé mensuel est réclamé
        // et on regarde sil a période correspon à un de ses cas
        while ((i < 8) && !trouve) {
            if (mensuel[i].equals(periode)) {
                trouve = true;
                res = CodeSystem.PERIODICITE_MENSUELLE;
            }
            i++;
        }
        // Si ce n'est pas le cas, on regarde si la période correspond
        // à un cas de relevé mensuel ou trimestriel
        if (!trouve) {
            i = 0;
            while ((i < 3) && !trouve) {
                if (mensuelTrimestriel[i].equals(periode)) {
                    trouve = true;
                    res = CodeSystem.PERIODICITE_MENSUELLE + "," + CodeSystem.PERIODICITE_TRIMESTRIELLE;
                }
                i++;
            }
            // Si on a toujours pas touvé c'est que le cas correspond à toutes
            // les périodicités
            if (!trouve) {
                res = CodeSystem.PERIODICITE_MENSUELLE + "," + CodeSystem.PERIODICITE_ANNUELLE + ","
                        + CodeSystem.PERIODICITE_TRIMESTRIELLE;
            }
        }
        return res;
    }

    /**
     * Retourne la période à réclamer formatée pour insérer dans la journalisation
     * 
     * @param affiliation
     * @param periode
     * @return
     * @throws JAException
     */
    public static String getPeriodiciteAReclamer(AFAffiliation affiliation, String periode) throws JAException {
        String periodeDebutFin;
        // Si c'est une période mensuelle
        if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
            periodeDebutFin = JADate.getMonth(periode) + "." + JADate.getYear(periode);
            // Si c'est une période annuelle
        } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
            periodeDebutFin = "1." + JADate.getYear(periode) + "12." + JADate.getYear(periode);
        } else {
            // Si c'est une période trimestrielle
            if (JADate.getMonth(periode).equals("3")) {
                periodeDebutFin = "1." + JADate.getYear(periode) + "-3." + JADate.getYear(periode);
            } else if (JADate.getMonth(periode).equals("6")) {
                periodeDebutFin = "4." + JADate.getYear(periode) + "-6." + JADate.getYear(periode);
            } else if (JADate.getMonth(periode).equals("9")) {
                periodeDebutFin = "7." + JADate.getYear(periode) + "-9." + JADate.getYear(periode);
            } else {
                periodeDebutFin = "10." + JADate.getYear(periode) + "-12." + JADate.getYear(periode);
            }
        }
        return periodeDebutFin;
    }

    /**
     * Permet de savoir si l'envoi a déjà été journalisé
     * 
     * @param aff
     * @param plan
     * @param perdiode
     * @return boolean
     * @throws Exception
     */
    private static boolean isDejaJournalise(AFAffiliation aff, AFPlanAffiliation plan, String periode) throws Exception {
        // On sette les critères qui font que l'envoi est unique
        if (aff != null) {
            LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();

            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                    AFApplication.DEFAULT_APPLICATION_NAOS);
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, aff.getAffilieNumero());
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE,
                    AFGestionReleveProcess.getPeriodiciteAReclamer(aff, periode));
            provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PLAN, plan.getLibelleFactureNotEmpty());

            /*
             * LUComplementDataSource complementCriteres = new LUComplementDataSource();
             * complementCriteres.addComplement(ILEConstantes .CS_PARAM_GEN_PERIODE, getPeriodiciteAReclamer(aff,
             * periode)); complementCriteres.addComplement(ILEConstantes.CS_PARAM_GEN_PLAN, plan.getLibelle());
             */

            LUJournalListViewBean viewBean = new LUJournalListViewBean();

            viewBean.setSession(aff.getSession());
            viewBean.setProvenance(provenanceCriteres);
            // viewBean.setF
            viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
            viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_RELEVES);
            viewBean.find();
            // Si le viewBean retourne un enregistrement c'est que l'envoi a
            // déjà été journalisé donc on retourne true
            if (viewBean.size() > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    // Permet de savoir si un rappel doit être généré
    private Boolean genererRappel = new Boolean(false);

    // Permet de savoir si une sommation doit être générée
    private Boolean genererSommation = new Boolean(false);

    // Permet de savoir si une taxation doit être générée
    private Boolean genererTaxation = new Boolean(false);

    // Permet de savoir la période à traiter
    private String periode = "";

    String titreFichier = "";

    public AFGestionReleveProcess() {
    }

    public AFGestionReleveProcess(BProcess parent) {
        super(parent);
    }

    public AFGestionReleveProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Execute le process de génération des rappel, sommation et taxation d'office
     */
    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            // Cas ou on veut générer un rappel
            if (genererRappel.booleanValue()) {

                // On recherche tous les affiliés pour lesquels un rappel doit
                // être généré
                AFGestionReleveManager gestionReleve = new AFGestionReleveManager();
                gestionReleve.setSession(getSession());
                gestionReleve.setPeriode(periode);
                gestionReleve.setPeriodicite(AFGestionReleveProcess.getPeriodicite(String.valueOf(JADate
                        .getMonth(periode))));
                gestionReleve.find();

                AFAffiliation aff = new AFAffiliation();
                if (gestionReleve.size() == 0) {
                    getMemoryLog().logMessage(getSession().getLabel("PAS_RAPPEL"), "", FWMessage.INFORMATION);
                } else {
                    getMemoryLog().logMessage(getSession().getLabel("DEBUT_SUIVI_SUCCES"), "", FWMessage.INFORMATION);
                }
                // On traite tous les affiliés rencontrés
                for (int i = 0; i < gestionReleve.size(); i++) {
                    aff = (AFAffiliation) gestionReleve.getEntity(i);
                    // recherche des plans de l'affilié
                    AFPlanAffiliationManager planAffManager = new AFPlanAffiliationManager();
                    planAffManager.setSession(getSession());
                    planAffManager.setForAffiliationId(aff.getAffiliationId());
                    planAffManager.find();
                    for (int planCnt = 0; planCnt < planAffManager.size(); planCnt++) {
                        AFPlanAffiliation plan = (AFPlanAffiliation) planAffManager.getEntity(planCnt);
                        // Si l'affilié possède déjà un rappel pour la période
                        // et le plan donné, on affiche un message d'information
                        // à l'utilisateur
                        if (AFGestionReleveProcess.isDejaJournalise(aff, plan, periode)) {
                            getMemoryLog().logMessage(
                                    getSession().getLabel("RELEVE_ALREADY_JOURNALISE") + aff.getAffilieNumero() + " "
                                            + periode, "", FWMessage.INFORMATION);
                        } else {
                            // Sinon on génère le rappel
                            AFGestionReleveProcess.genererControle(aff, plan, periode);
                        }
                    }
                }
            }

            // Cas où on veut générer une sommation
            if (genererSommation.booleanValue()) {
                LEEtapesSuivantesListViewBean etapesSuivantes = rechercheEtapes(ILEConstantes.CS_RELEVES_SOMMATIOM);
                if (etapesSuivantes.size() == 0) {
                    getMemoryLog().logMessage(getSession().getLabel("PAS_SOMMATION"), "", FWMessage.INFORMATION);
                }
                genererEtape(etapesSuivantes);
            }
            if (genererTaxation.booleanValue()) {
                LEEtapesSuivantesListViewBean etapesSuivantes = rechercheEtapes(ILEConstantes.CS_RELEVES_TAXATION_OFFICE);
                if (etapesSuivantes.size() == 0) {
                    getMemoryLog().logMessage(getSession().getLabel("PAS_TAXATION_OFFICE"), "", FWMessage.INFORMATION);
                }
                generateDoc();
                genererEtape(etapesSuivantes);

            }

            // mergePDF(createDocumentInfo(), true, 0, false, null);

            return true;
        } catch (Exception e) {
            this._addError(getSession().getLabel("ERREUR_GENERATION_DOCUMENTS") + e.getMessage());
            getMemoryLog().logMessage(getSession().getLabel("ERREUR_GENERATION_DOCUMENTS") + e.getMessage(), "",
                    FWViewBeanInterface.ERROR);
            getTransaction().closeTransaction();
            getTransaction().rollback();
            abort();

            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        BTransaction trans = new BTransaction(getSession());
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError("L'adresse e-mail doit être remplie");

            abort();
        }
        if (genererRappel.booleanValue() && JadeStringUtil.isBlank(getPeriode())) {
            getSession().addError("La période doit être remplie lorsqu'un génère un rappel");
            abort();
        }
        if (!genererRappel.booleanValue() && !genererSommation.booleanValue() && !genererTaxation.booleanValue()) {
            getSession().addError("Une vous dever saisir au moins une étape à générer");
            abort();
        }
    }

    private void generateDoc() {
        ArrayList csFormule = new ArrayList();
        csFormule.add(ILEConstantes.CS_RELEVES_TAXATION_OFFICE);

        LEGenererListeFormulesEnAttente etapes = new LEGenererListeFormulesEnAttente();
        etapes.setSession(getSession());
        etapes.setEMailAddress(getEMailAddress());
        etapes.setDateReference(JACalendar.today().toString());
        etapes.setCsFormule(csFormule);
        etapes.setIsFormatIText(new Boolean(true));
        etapes.setCategorie(ILEConstantes.CS_CATEGORIE_SUIVI_RELEVES);

        etapes.start();
    }

    /**
     * Permet de passer à l'étape suivante dans LEO
     * 
     * @param etapeAGenerer
     * @throws Exception
     */
    private void genererEtape(LEEtapesSuivantesListViewBean listeEtapes) throws Exception {

        LEJournalHandler complement = new LEJournalHandler();
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        for (int i = 0; i < listeEtapes.size(); i++) {
            LEEtapesSuivantesViewBean etapeSuivante = (LEEtapesSuivantesViewBean) listeEtapes.getEntity(i);
            String idDernier = envoiHandler.getDernierEnvoi(etapeSuivante.getIdJournalisation(), getSession(), null);
            if (complement
                    .getComplementJournal(idDernier, ILEConstantes.CS_EDITION_MANUELLE_GROUPE, getSession(), null) != null) {
                if (complement.getComplementJournal(idDernier, ILEConstantes.CS_EDITION_MANUELLE_GROUPE, getSession(),
                        null).equals(ILEConstantes.CS_NON)) {
                    LEGenererEtapeHandler etapeSuivant = new LEGenererEtapeHandler();
                    etapeSuivant.genererEtapeSuivante(etapeSuivante.getIdJournalisation(), getSession());
                }
            }
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("NAOS_EMAIL_OBJET_RELEVES");
    }

    /**
     * Renvoie si un rappel doit être généré
     * 
     * @return genererRappel
     */
    public Boolean getGenererRappel() {
        return genererRappel;
    }

    /**
     * Renvoie si une sommation doit être générée
     * 
     * @return genererSommation
     */
    public Boolean getGenererSommation() {
        return genererSommation;
    }

    /**
     * Renvoie si une taxation doit être générée
     * 
     * @return genererTaxation
     */
    public Boolean getGenererTaxation() {
        return genererTaxation;
    }

    /**
     * Renvoie la période à traiter
     * 
     * @return periode
     */
    public String getPeriode() {
        return periode;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private LEEtapesSuivantesListViewBean rechercheEtapes(String etapeAGenerer) throws Exception {
        ArrayList b = new ArrayList();
        b.add(etapeAGenerer);
        LEEtapesSuivantesListViewBean listeEtapes = new LEEtapesSuivantesListViewBean();
        listeEtapes.setSession(getSession());
        listeEtapes.setForCsFormule(b);
        listeEtapes.setForCategories(ILEConstantes.CS_CATEGORIE_SUIVI_RELEVES);
        listeEtapes.setWantOrderBy(false);
        listeEtapes.setWantOrderBy(false);
        listeEtapes.setDatePriseEnCompte(JACalendar.today().toString());
        listeEtapes.find(getTransaction());
        return listeEtapes;
    }

    /**
     * Permet de setter si un rappel doit être généré
     * 
     * @param string
     */
    public void setGenererRappel(Boolean b) {
        genererRappel = b;
    }

    /**
     * Permet de setter si une sommation doit être générée
     * 
     * @param string
     */
    public void setGenererSommation(Boolean b) {
        genererSommation = b;
    }

    /**
     * Permet de setter si une taxation doit être générée
     * 
     * @param string
     */
    public void setGenererTaxation(Boolean b) {
        genererTaxation = b;
    }

    /**
     * Permet de setter la période à traiter
     * 
     * @param string
     */
    public void setPeriode(String string) {
        periode = string;
    }

}

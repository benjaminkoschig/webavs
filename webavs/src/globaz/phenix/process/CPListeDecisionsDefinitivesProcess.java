package globaz.phenix.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.phenix.db.principale.CPDecisionAffiliationManager;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.listes.excel.CPListeDecisionsDefinitivesExcel;
import globaz.phenix.util.CPUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Processus de lancement de génération de la liste des décisions définitives
 * 
 * @author SCO
 * @since 12 juil. 2011
 */
public class CPListeDecisionsDefinitivesProcess extends BProcess {

    private static final long serialVersionUID = 8938995339332723886L;

    public static boolean _checkDate(BSession session, String fieldValue) {
        try {
            BSessionUtil.checkDateGregorian(session, fieldValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void validateData(BSession session, String dateDebut, String dateFin, String annee) throws Exception {

        // Si les dates ne sont pas renseignées et que la date non plus, on leve une erreur
        if (JadeStringUtil.isEmpty(dateDebut) && JadeStringUtil.isEmpty(dateFin) && JadeStringUtil.isEmpty(annee)) {
            session.addError(session.getLabel("VAL_PRECISER_CRITERES_SELECTIONS"));
        }

        if (!JadeStringUtil.isEmpty(dateDebut) && !CPListeDecisionsDefinitivesProcess._checkDate(session, dateDebut)) {
            session.addError(session.getLabel("VAL_DATE_DEBUT"));
        }
        if (!JadeStringUtil.isEmpty(dateFin) && !CPListeDecisionsDefinitivesProcess._checkDate(session, dateFin)) {
            session.addError(session.getLabel("VAL_DATE_FIN"));
        }

        if (!JadeStringUtil.isEmpty(dateDebut) && !JadeStringUtil.isEmpty(dateFin)
                && !BSessionUtil.compareDateFirstLower(session, dateDebut, dateFin)) {
            session.addError(session.getLabel("VAL_DATE_FIN_INFERIEUR"));
        }
    }

    private String annee;
    private String dateDebut;
    private String dateFin;
    private Boolean decisionActive = new Boolean(false);
    private String fromAffilie;
    private String genreDecision;

    private String toAffilie;

    private String typeDecision;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        CPDecisionAffiliationManager manager = new CPDecisionAffiliationManager();
        Collection<Map<String, String>> listeDecision = new ArrayList<Map<String, String>>();

        try {
            // Gestion du genre de décision
            if (!JadeStringUtil.isEmpty(getGenreDecision()) && CPDecision.CS_INDEPENDANT.equals(getGenreDecision())) {
                manager.setInGenreAffilie(CPDecision.CS_INDEPENDANT + "," + CPDecision.CS_TSE + ","
                        + CPDecision.CS_RENTIER + "," + CPDecision.CS_AGRICULTEUR);
            }
            if (!JadeStringUtil.isEmpty(getGenreDecision()) && CPDecision.CS_NON_ACTIF.equals(getGenreDecision())) {
                manager.setInGenreAffilie(CPDecision.CS_NON_ACTIF + "," + CPDecision.CS_ETUDIANT);
            }

            // Gestion du type de décision
            if (CPDecision.CS_DEFINITIVE.equals(getTypeDecision())) {
                manager.setInTypeDecision(CPDecision.CS_DEFINITIVE + "," + CPDecision.CS_RECTIFICATION);
            } else if (CPDecision.CS_PROVISOIRE.equals(getTypeDecision())) {
                manager.setInTypeDecision(CPDecision.CS_PROVISOIRE + "," + CPDecision.CS_ACOMPTE + ","
                        + CPDecision.CS_CORRECTION);
            } else {
                manager.setForTypeDecision(getTypeDecision());
            }

            // Prendre que les actifs
            manager.setIsActive(getDecisionActive());

            // Pour les affiliés suivant
            manager.setFromNoAffilie(getFromAffilie());
            manager.setTillNoAffilie(getToAffilie());

            // Pour une date de facturation
            manager.setFromPeriodeFacturation(getDateDebut());
            manager.setTillPeriodeFacturation(getDateFin());

            // Pour une annee donnée
            manager.setForAnneeDecision(getAnnee());

            manager.setSession(getSession());
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            setProgressScaleValue(manager.getSize() + 1);
            Object[] obj_listeDecision = manager.getContainer().toArray();

            for (Object obj_decision : obj_listeDecision) {

                CPDecisionAffiliation decision = (CPDecisionAffiliation) obj_decision;
                setProgressDescription("Num Affilie : " + decision.getNumAffilie());

                Map<String, String> map_decision = new HashMap<String, String>();

                map_decision.put(CPListeDecisionsDefinitivesExcel.ACTIF, decision.getActive().toString());
                map_decision.put(CPListeDecisionsDefinitivesExcel.NOM, decision.getTiers().getNomPrenom());
                map_decision.put(CPListeDecisionsDefinitivesExcel.NUMERO, decision.getNumAffilie());
                map_decision.put(CPListeDecisionsDefinitivesExcel.ANNEE, decision.getAnneeDecision());
                map_decision.put(CPListeDecisionsDefinitivesExcel.GENRE,
                        CodeSystem.getCode(getSession(), decision.getGenreAffilie()));
                map_decision.put(CPListeDecisionsDefinitivesExcel.TYPE_DECISION,
                        CodeSystem.getLibelle(getSession(), decision.getTypeDecision()));

                // ----------------------
                // Periode de la decision
                // ----------------------
                String periode = decision.getDebutDecision() + " - " + decision.getFinDecision();
                map_decision.put(CPListeDecisionsDefinitivesExcel.PERIODE, periode);

                // ----------------------
                // Calcul de la fortune/revenu
                // ----------------------
                String montant = "";
                CPDonneesCalcul donnee = new CPDonneesCalcul();
                donnee.setSession(getSession());
                if (!decision.getActive()) {
                    montant = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE);
                } else {
                    montant = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
                }
                map_decision.put(CPListeDecisionsDefinitivesExcel.REVENU, montant);

                // ----------------------
                // Revenu Ci
                // ----------------------
                montant = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
                map_decision.put(CPListeDecisionsDefinitivesExcel.REVENU_CI, montant);

                // ----------------------
                // Cotisation annuelle
                // ----------------------

                map_decision.put(CPListeDecisionsDefinitivesExcel.COTISATION,
                        retrieveMontantCotisation(decision.getIdDecision()));

                // On ajoute cette decision dans le tableau
                listeDecision.add(map_decision);

                // On fait avancer la progresse bar
                incProgressCounter();
            }

            setProgressDescription("Génération Excel");

            // Generation de la liste
            CPListeDecisionsDefinitivesExcel excelDoc = new CPListeDecisionsDefinitivesExcel(getSession());
            excelDoc.setListeDecision(listeDecision);
            excelDoc.setMapEntete(createMapEntete());
            excelDoc.createDocExcel();

            // Création du document
            String nomDoc = getSession().getLabel("LISTE_DECISION_DEFINITIVE");
            String docPath = excelDoc.getOutputFile(nomDoc);

            // Publication du document
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setApplicationDomain(CPApplication.DEFAULT_APPLICATION_PHENIX);
            docInfo.setDocumentTitle(nomDoc);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            docInfo.setDocumentTypeNumber(CPListeDecisionsDefinitivesExcel.NUM_INFOROM);
            this.registerAttachedDocument(docInfo, docPath);

        } catch (Exception e) {

            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_LISTE_DECISION_ERREUR"));

            String messageInformation = "Annee décisions : " + getAnnee() + "\n";
            messageInformation += "Date debut : " + getDateDebut() + "\n";
            messageInformation += "Date fin : " + getDateFin() + "\n";
            messageInformation += "From affilie : " + getFromAffilie() + "\n";
            messageInformation += "To affilie : " + getToAffilie() + "\n";
            messageInformation += "Type decision : " + getTypeDecision() + "\n";
            messageInformation += "Genre decision : " + getGenreDecision() + "\n";
            messageInformation += AFUtil.stack2string(e);

            CPUtil.addMailInformationsError(getMemoryLog(), messageInformation, this.getClass().getName());

            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {

        CPListeDecisionsDefinitivesProcess.validateData(getSession(), getDateDebut(), getDateFin(), getAnnee());

    }

    /**
     * Permet de creer
     * 
     * @return
     * @throws Exception
     */
    private Map<String, String> createMapEntete() throws Exception {
        Map<String, String> mapEntete = new HashMap<String, String>();
        mapEntete.put(CPListeDecisionsDefinitivesExcel.EN_TETE_AFFILIES, getFromAffilie() + " - " + getToAffilie());
        mapEntete.put(CPListeDecisionsDefinitivesExcel.EN_TETE_ANNEE, getAnnee());
        mapEntete.put(CPListeDecisionsDefinitivesExcel.EN_TETE_UNIQUEMENT_ACTIVE, getDecisionActive() ? getSession()
                .getLabel("OUI") : getSession().getLabel("NON"));
        mapEntete.put(CPListeDecisionsDefinitivesExcel.EN_TETE_GENRE_DECISION,
                CodeSystem.getLibelle(getSession(), getGenreDecision()));
        mapEntete.put(CPListeDecisionsDefinitivesExcel.EN_TETE_TYPE_DECISION,
                CodeSystem.getLibelle(getSession(), getTypeDecision()));
        mapEntete.put(CPListeDecisionsDefinitivesExcel.EN_TETE_PERIODE, getDateDebut() + " - " + getDateFin());

        return mapEntete;
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public Boolean getDecisionActive() {
        return decisionActive;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || isAborted()) {
            return getSession().getLabel("GENERATION_LISTE_DECISION_DEF_ERREUR");
        } else {
            return getSession().getLabel("GENERATION_LISTE_DECISION_DEF_OK");
        }
    }

    public String getFromAffilie() {
        return fromAffilie;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public String getToAffilie() {
        return toAffilie;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private String retrieveMontantCotisation(String idDecision) throws Exception {

        CPCotisation coti = new CPCotisation();
        coti.setSession(getSession());
        coti.setAlternateKey(2);
        coti.setIdDecision(idDecision);
        coti.setGenreCotisation("812001");
        try {
            coti.retrieve();

            if (!coti.isNew()) {
                return coti.getMontantAnnuel();
            }
        } catch (Exception e) {
            throw new Exception("Unabled to retrieve cotisation for id decision " + idDecision, e);
        }

        return "";
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDecisionActive(Boolean decisionActive) {
        this.decisionActive = decisionActive;
    }

    public void setFromAffilie(String fromAffilie) {
        this.fromAffilie = fromAffilie;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    public void setToAffilie(String toAffilie) {
        this.toAffilie = toAffilie;
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }
}

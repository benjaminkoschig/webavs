package globaz.al.process.dossiers;

import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.affiliation.RadiationAffilieService;
import ch.globaz.al.business.services.decision.DecisionBuilderService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class ALRadiationDossiersFromEbuProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateImpression = null;
    private String email = null;
    private boolean ged = false;
    private boolean printDecisions = false;
    private DossierComplexModel dossier;
    private String dateRadiation;
    private String periode;
    private String texteLibreCertificat;

    /**
     * Retourne la date d'impression sp�cifi�e lors de l'appel du process. Si elle est vide, retourne la date du jour
     * 
     * @return la date d'impression
     */
    public String getDateImpression() {
        return JadeStringUtil.isEmpty(dateImpression) ? JadeDateUtil.getGlobazFormattedDate(new Date())
                : dateImpression;
    }

    @Override
    public String getDescription() {
        return "Process de radiation de masse de dossiers AF";
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return ALRadiationDossiersFromEbuProcess.class.getName();
    }

    public boolean getPrintDecisions() {
        return printDecisions;
    }

    public boolean isGED() {
        return ged;
    }

    private HashMap<String, String> logError(HashMap<String, String> logError, DossierComplexModel dossier)
            throws Exception {
        JadeBusinessMessage[] logMessages = JadeThread.logMessages();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logMessages.length; i++) {
            sb.append(
                    JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), logMessages[i].getMessageId(),
                            logMessages[i].getParameters())).append("\n");
        }

        logError.put(dossier.getId(), sb.toString());
        return logError;
    }

    @Override
    protected void process() {

        HashMap<String, String> logError = new HashMap<String, String>();
        JadePrintDocumentContainer containerDecision = new JadePrintDocumentContainer();
        boolean hasDecisions = false;

        try {
            DecisionBuilderService serviceDecisionBuilder = ALServiceLocator.getDecisionBuilderService();

            String idDossier = dossier.getId();

            try {

                boolean updateNbJoursFin = true;

                String lastDayOfMonth = JadeDateUtil.getLastDateOfMonth(dateRadiation);

                if (dateRadiation.equals(lastDayOfMonth)) {
                    updateNbJoursFin = false;
                }

                DossierModel dossierRadie = ALServiceLocator.getDossierBusinessService().radierDossier(
                        dossier.getDossierModel(), dateRadiation, updateNbJoursFin, "");

                dossier.setDossierModel(dossierRadie);

                // erreur lors de la radiation/journalisation
                if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) != null) {
                    logError = logError(logError, dossier);
                    JadeThread.rollbackSession();
                    // JadeThread.logClear();
                    // OK
                } else if ((dossier != null) && !dossier.isNew()
                        && ALCSDossier.ETAT_RADIE.equals(dossier.getDossierModel().getEtatDossier())) {

                    // ///////////////////////////////////////////////////////////////////////////////////////
                    // Cr�ation de la d�cision
                    // ///////////////////////////////////////////////////////////////////////////////////////

                    if (getPrintDecisions()) {

                        ALServiceLocator.getCopiesBusinessService().createDefaultCopies(dossier,
                                ALCSCopie.TYPE_DECISION);

                        containerDecision = serviceDecisionBuilder.getDecisionEtCopies(containerDecision, dossier,
                                email, getSession().getUserFullName(), getSession().getUserInfo().getPhone(),
                                getSession().getUserInfo().getVisa(), dateImpression, isGED(), texteLibreCertificat,
                                true);

                        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                            journaliserDecision(serviceDecisionBuilder);
                        }

                        hasDecisions = true;
                    }

                    // G�n�ration des prestations � restituer
                    generationRestitutionPrestation(dossier);

                    // ///////////////////////////////////////////////////////////////////////////////////////

                    // ///////////////////////////////////////////////////////////////////////////////////////
                    // Commit/Rollback
                    // ///////////////////////////////////////////////////////////////////////////////////////

                    if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR) == null) {
                        JadeThread.commitSession();
                        JadeThread.logClear();
                    } else {
                        logError(logError, dossier);
                        JadeThread.rollbackSession();
                        // JadeThread.logClear();
                    }
                }

            } catch (Exception e) {
                getLogSession().error(
                        this.getClass().getName(),
                        "Une erreur s'est produite pendant la radiation du dossier " + idDossier + " : "
                                + e.getMessage());
                logError.put(idDossier, e.getMessage());
                JadeThread.rollbackSession();
                // JadeThread.logClear();
            }

            // impression des d�cisions
            if (hasDecisions) {
                containerDecision.setMergedDocDestination(serviceDecisionBuilder.getMergedDocumentInfos(getEmail(),
                        dateImpression));
                this.createDocuments(containerDecision);
            }

            // envoi du protocole
            try {
                // String file = ALServiceLocator.getRadiationAutomatiqueDossiersProtocoleService().createProtocole(
                // logDossiers, logError);
                // String description = "Protocole de radiation automatique des dossiers";
                // JadeSmtpClient.getInstance().sendMail(getEmail(), description, description, new String[] { file });
                // JadeLogger.info(this, "Protocole de radiation automatique des dossiers envoy�");
            } catch (Exception e) {
                getLogSession()
                        .error(this.getClass().getName(),
                                "Une erreur s'est produite pendant la g�n�ration du protocole des radiations automatiques de dossiers n'est pas disponible : "
                                        + e.getMessage());
                JadeLogger
                        .error(this,
                                "Une erreur s'est produite pendant la g�n�ration du protocole des radiations automatiques de dossiers n'est pas disponible : "
                                        + e.getMessage());
            }

        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant l'ex�cution du traitement : " + e.getMessage());
            JadeLogger.error(this, "Une erreur s'est produite pendant l'ex�cution du traitement : " + e.getMessage());
        } finally {
            ArrayList<String> emails = new ArrayList<String>();
            emails.add(getEmail());
            try {
                sendCompletionMail(emails);
            } catch (Exception e) {
                JadeLogger.error(
                        this,
                        "Impossible d'envoyer le mail de r�sultat du traitement. Raison : " + e.getMessage() + ", "
                                + e.getCause());
            }
        }
    }

    private void journaliserDecision(DecisionBuilderService serviceDecisionBuilder) {
        try {
            serviceDecisionBuilder.journaliserDecision(dossier);
        } catch (Exception e) {
            getLogSession().error(
                    this.getClass().getName(),
                    "Une erreur s'est produite pendant la journalisation d'une d�cision (dossier " + dossier.getId()
                            + ")");
            JadeThread.logError(ALRadiationDossiersFromEbuProcess.class.getName(), e.getMessage());
        }

    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGED(boolean ged) {
        this.ged = ged;
    }

    public void setPrintDecisions(boolean printDecisions) {
        this.printDecisions = printDecisions;
    }

    public DossierComplexModel getDossier() {
        return dossier;
    }

    public void setDossier(DossierComplexModel dossier) {
        this.dossier = dossier;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    private void generationRestitutionPrestation(DossierComplexModel dossier) throws JadePersistenceException,
            JadeApplicationException {
        if (isGeneratadRestitutionPrestationNeeded(dossier)) {
            RadiationAffilieService serviceRadiation = ALServiceLocator.getRadiationAffilieService();
            serviceRadiation.genererPrestationForDossier(dossier, false, true);
        }
    }

    public String getTexteLibreCertificat() {
        return texteLibreCertificat;
    }

    public void setTexteLibreCertificat(String texteLibre) {
        texteLibreCertificat = texteLibre;
    }

    /**
     * M�thode permettant de d�terminer si des prestations / restitutions doivent �tre g�n�r�es pour ce dossier selon certaines conditions
     * Dont la date de radiation et p�riode de la r�cap ainsi que les prestations comptabilis�es du dossier
     *
     * @param dossier
     * @return Vrai si des prestations / restitutions doivent �tre g�n�r�es
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private boolean isGeneratadRestitutionPrestationNeeded(DossierComplexModel dossier) throws JadeApplicationException, JadePersistenceException {
        return JadeDateUtil.isDateMonthYearBefore(JadeDateUtil.convertDateMonthYear(dateRadiation), periode) ||
                (isRadiationPeriodeSameMonth(dateRadiation) && hasPrestationsComptabilises(dossier));

    }

    /**
     * M�thode permettant de v�rifier si le dossier poss�de d�j� des prestations comptabilis�es pour une p�riode donn�e
     * Ce test a d� �tre rajout� suite au point PCA-584 car il doit �tre possible de cl�turer des r�cap (et donc radier des dossier)
     * m�me apr�s avoir lancer la g�n�ration des prestations provenant de la gestion des processus du module AF
     *
     * @param dossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private boolean hasPrestationsComptabilises(DossierComplexModel dossier) throws JadeApplicationException, JadePersistenceException {
            List<EntetePrestationModel> prestations = ALServiceLocator.getEntetePrestationModelService().searchEntetesPrestationsComptabilisees(dossier.getId(), periode);
            return !prestations.isEmpty();
    }

    /**
     * M�thode permettant de tester si deux date ont un mois et une ann�e identique (MM.yyyy)
     * Ce test a d� �tre rajout� suite au point PCA-584 car seul les dossiers qui sont radi�s pour le m�me mois de la r�cap
     * doivent �tre impact�
     *
     * @param dateRadiation
     * @return Vrai si le mois et l'ann�e de la date de radiation est la m�me que la p�riode
     */
    private boolean isRadiationPeriodeSameMonth(String dateRadiation) {
        ch.globaz.common.domaine.Date radiation = new ch.globaz.common.domaine.Date(dateRadiation);
        return radiation.equalsByMonthYear(new ch.globaz.common.domaine.Date(periode));
    }
}
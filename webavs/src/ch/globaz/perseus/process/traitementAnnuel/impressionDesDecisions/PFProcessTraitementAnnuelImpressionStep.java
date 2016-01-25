package ch.globaz.perseus.process.traitementAnnuel.impressionDesDecisions;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.decision.PFImpressionDecisionTraitementMasseProcess;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.exceptions.JadeProcessException;
import ch.globaz.jade.process.business.exceptions.StepException;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.business.models.entity.SimpleEntity;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasseComptageSearchModel;
import ch.globaz.perseus.process.traitementAdaptation.PFTypePopulationEnum;
import ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum;

public class PFProcessTraitementAnnuelImpressionStep implements JadeProcessStepInterface, JadeProcessStepAfterable,
        JadeProcessStepInfoCurrentStep {

    private String emailA = "";
    private String emailB = "";

    private List<Decision> listDecisions = Collections.synchronizedList(new ArrayList<Decision>());
    private Map<Enum<?>, Integer> listCompteur = Collections.synchronizedMap(new HashMap<Enum<?>, Integer>());

    private List<String> listCasAvecRetenu = Collections.synchronizedList(new ArrayList<String>());
    private List<String> listCasNonCalculable = Collections.synchronizedList(new ArrayList<String>());

    private Map<String, Map<Enum<?>, String>> mapSavedValue = new HashMap<String, Map<Enum<?>, String>>();

    private BSession theSession;
    private JadeAbstractModel[] modelEntities;
    private JadeProcessExecut jadeInfo;

    /**
     * Retourne le handler servant à manipuler les entités
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PFProcessTraitementAnnuelImpressionEntityHandler(listDecisions);
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        comptage();

        emailA = map.get(PFProcessTraitementAnnuelEnum.ADRESSE_MAIL_AGLAU);
        emailB = map.get(PFProcessTraitementAnnuelEnum.ADRESSE_MAIL_CCVD);

        theSession = BSessionUtil.getSessionFromThreadContext();

        try {
            sendCompletionMail();
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_ERREUR")
                            + e.toString());
        }

        imprimerDecision();
    }

    private void comptage() throws JadePersistenceException, JadeProcessException,
            JadeApplicationServiceNotAvailableException, StepException {
        // Demandes validées sans date de fin à traiter :
        listCompteur.put(PFTypePopulationEnum.SANS_DATE_DE_FIN, new Integer(0));

        // Demandes fermées :
        listCompteur.put(PFProcessTraitementAnnuelEnum.DEMANDE_FERME, new Integer(0));

        // Demandes réouvertes (fermées + partiel cloturé en fin d'année dernière) :
        listCompteur.put(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE, new Integer(0));

        // Décisions imprimées :
        listCompteur.put(PFProcessTraitementAnnuelEnum.DECISION_IMPRIMES, new Integer(listDecisions.size()));

        for (Entry<String, Map<Enum<?>, String>> entry : mapSavedValue.entrySet()) {
            Map<Enum<?>, String> values = entry.getValue();

            if (values.containsKey(PFProcessTraitementAnnuelEnum.TYPE_POPULATION)) {
                String typePopulation = values.get(PFProcessTraitementAnnuelEnum.TYPE_POPULATION);
                if (typePopulation.equals(PFTypePopulationEnum.SANS_DATE_DE_FIN.toString())) {
                    Integer compteur = listCompteur.get(PFTypePopulationEnum.SANS_DATE_DE_FIN);
                    compteur = new Integer(compteur.intValue() + 1);
                    listCompteur.put(PFTypePopulationEnum.SANS_DATE_DE_FIN, compteur);
                }
            }

            if (values.containsKey(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE)) {
                Integer compteur = listCompteur.get(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE);
                compteur = new Integer(compteur.intValue() + 1);
                listCompteur.put(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE, compteur);
            }

        }

        for (JadeAbstractModel entity : modelEntities) {
            SimpleEntity monEntity = (SimpleEntity) entity;

            Map<Enum<?>, String> values = mapSavedValue.get(monEntity.getIdEntite());
            if (values.containsKey(PFProcessTraitementAnnuelEnum.CAS_NON_CALCULABLE)) {
                listCasNonCalculable.add(monEntity.getDescription());
            }
        }

        for (JadeAbstractModel entity : modelEntities) {
            SimpleEntity monEntity = (SimpleEntity) entity;

            Map<Enum<?>, String> values = mapSavedValue.get(monEntity.getIdEntite());
            if (values.containsKey(PFProcessTraitementAnnuelEnum.CAS_AVEC_RETENU)) {
                listCasAvecRetenu.add(monEntity.getDescription());
            }
        }

        DemandeTraitementMasseComptageSearchModel demandeTraitementDeMasseComptageSearchModel = new DemandeTraitementMasseComptageSearchModel();
        demandeTraitementDeMasseComptageSearchModel
                .setForExecutionProcess(jadeInfo.getSimpleExecutionProcess().getId());
        demandeTraitementDeMasseComptageSearchModel.setForKeyProperty(PFProcessTraitementAnnuelEnum.DEMANDE_FERME
                .toString());
        listCompteur.put(PFProcessTraitementAnnuelEnum.DEMANDE_FERME,
                JadePersistenceManager.count(demandeTraitementDeMasseComptageSearchModel));

    }

    private void imprimerDecision() {
        try {

            // Impression des décisions
            if (!listDecisions.isEmpty()) {
                PFImpressionDecisionTraitementMasseProcess process = new PFImpressionDecisionTraitementMasseProcess();
                process.setSession(theSession);
                process.setListeDecision(listDecisions);
                process.setMailAddressCaissePrincipale(emailA);
                process.setMailAddressCaisseSecondaire(emailB);
                BProcessLauncher.startJob(process);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_ERREUR")
                            + e.toString());
        }

    }

    protected final void sendCompletionMail() throws Exception {

        if (theSession == null) {
            throw new IllegalStateException("cannot send completion mail: user session is null.");
        }

        String subject = "Le job s'est terminé avec succès";

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            subject += "\nErreur(s) dans le job";
        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            subject += "\nAvertissements(s) dans le job";
        }

        String body = creerBody();

        String[] emailsAsArray = new String[] { emailA, emailB };

        assert emailsAsArray != null : "emailsAsArray is null!";

        if (emailsAsArray.length != 0) {
            for (int i = 0; i < emailsAsArray.length; i++) {
                if (emailsAsArray[i] == null) {
                    throw new NullPointerException(
                            "Cannot send completion mails: an email is null in List. No single mail sent!");
                }
            }
            JadeSmtpClient.getInstance().sendMail(emailsAsArray, subject, body, null);
        }
    }

    private String creerBody() {
        StringBuilder body = new StringBuilder();

        // Demandes fermées :
        Integer demandeFermees = listCompteur.get(PFProcessTraitementAnnuelEnum.DEMANDE_FERME);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DEMANDES_FERMEES")
                + demandeFermees.toString() + System.getProperty("line.separator"));

        // Demandes réouvertes (fermées + partiel cloturé en fin d'année dernière) :
        Integer demandeReouvertes = listCompteur.get(PFProcessTraitementAnnuelEnum.ID_DEMANDE_COPIE);
        body.append(BSessionUtil.getSessionFromThreadContext()
                .getLabel("SERVICE_TRAITEMENT_ANNUEL_DEMANDES_REOUVERTES")
                + demandeReouvertes.toString()
                + System.getProperty("line.separator"));

        // Décisions imprimées :
        Integer decisionsImprimes = listCompteur.get(PFProcessTraitementAnnuelEnum.DECISION_IMPRIMES);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DECISION_IMPRIMEES")
                + decisionsImprimes.toString() + System.getProperty("line.separator"));

        body.append(System.getProperty("line.separator"));

        // Lister les cas avec retenu à sortir dans le mail (Cas en erreur non sortit)
        body.append(BSessionUtil.getSessionFromThreadContext()
                .getLabel("SERVICE_TRAITEMENT_ADAPTATION_CAS_AVEC_RETENU"));

        for (String str : listCasNonCalculable) {
            body.append(str + System.getProperty("line.separator"));
        }

        body.append(System.getProperty("line.separator"));

        for (String str : listCasAvecRetenu) {
            body.append(str + System.getProperty("line.separator"));
        }

        return body.toString();
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut jadeInfo) {
        mapSavedValue = jadeInfo.getPropertiesEntities();
        this.jadeInfo = jadeInfo;
        if (jadeInfo.getSimpleEntiteSearch() != null) {
            modelEntities = jadeInfo.getSimpleEntiteSearch().getSearchResults();
        }
    }
}

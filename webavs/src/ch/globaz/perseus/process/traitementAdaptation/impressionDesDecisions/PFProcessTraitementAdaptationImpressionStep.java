package ch.globaz.perseus.process.traitementAdaptation.impressionDesDecisions;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.decision.PFImpressionDecisionTraitementMasseProcess;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.business.models.entity.SimpleEntity;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.demande.DemandeTraitementMasseComptageSearchModel;
import ch.globaz.perseus.process.traitementAdaptation.PFProcessTraitementAdaptationEnum;
import ch.globaz.perseus.process.traitementAdaptation.PFTypePopulationEnum;

public class PFProcessTraitementAdaptationImpressionStep implements JadeProcessStepInterface, JadeProcessStepAfterable,
        JadeProcessStepInfoCurrentStep {

    private String emailA = "";
    private String emailB = "";

    private List<Decision> listDecisions = Collections.synchronizedList(new ArrayList<Decision>());
    private Map<Enum<?>, Integer> listCompteur = Collections.synchronizedMap(new HashMap<Enum<?>, Integer>());

    private List<String> listCasAvecRetenu = Collections.synchronizedList(new ArrayList<String>());

    private Map<String, Map<Enum<?>, String>> mapSavedValue = new HashMap<String, Map<Enum<?>, String>>();

    private BSession theSession;
    private JadeAbstractModel[] modelEntities;
    private JadeProcessExecut jadeInfo;

    /**
     * Retourne le handler servant à manipuler les entités
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PFProcessTraitementAdaptationImpressionEntityHandler(listDecisions);
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        comptage();

        emailA = map.get(PFProcessTraitementAdaptationEnum.ADRESSE_MAIL_AGLAU);
        emailB = map.get(PFProcessTraitementAdaptationEnum.ADRESSE_MAIL_CCVD);

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

    private void comptage() throws JadePersistenceException {
        // Demandes validées sans date de fin à traiter :
        listCompteur.put(PFTypePopulationEnum.SANS_DATE_DE_FIN, new Integer(0));

        // Demandes fermées :
        listCompteur.put(PFProcessTraitementAdaptationEnum.DEMANDE_FERME, new Integer(0));

        // Demandes réouvertes (fermées + partiel cloturé en fin d'année dernière) :
        listCompteur.put(PFProcessTraitementAdaptationEnum.ID_DEMANDE_COPIE, new Integer(0));

        // Partiels sont passés en octroi :
        listCompteur.put(PFProcessTraitementAdaptationEnum.PARTIEL_EN_OCTROI, new Integer(0));

        // Partiels restés en partiel mais avec changement d'excédent :
        listCompteur.put(PFProcessTraitementAdaptationEnum.DECISION_PARTIEL_CHANGEMENT_EXCEDANT, new Integer(0));

        // Décisions imprimées :
        listCompteur.put(PFProcessTraitementAdaptationEnum.DECISION_IMPRIMES, new Integer(listDecisions.size()));

        for (Entry<String, Map<Enum<?>, String>> entry : mapSavedValue.entrySet()) {
            Map<Enum<?>, String> values = entry.getValue();

            // if (values.containsKey(PFProcessTraitementAdaptationEnum.TYPE_POPULATION)) {
            // String typePopulation = values.get(PFProcessTraitementAdaptationEnum.TYPE_POPULATION);
            // if (typePopulation.equals(PFTypePopulationEnum.SANS_DATE_DE_FIN.toString())) {
            // Integer compteur = listCompteur.get(PFTypePopulationEnum.SANS_DATE_DE_FIN);
            // compteur = new Integer(compteur.intValue() + 1);
            // listCompteur.put(PFTypePopulationEnum.SANS_DATE_DE_FIN, compteur);
            // }
            // }

            if (values.containsKey(PFProcessTraitementAdaptationEnum.ID_DEMANDE_COPIE)) {
                Integer compteur = listCompteur.get(PFProcessTraitementAdaptationEnum.ID_DEMANDE_COPIE);
                compteur = new Integer(compteur.intValue() + 1);
                listCompteur.put(PFProcessTraitementAdaptationEnum.ID_DEMANDE_COPIE, compteur);
            }

            if (values.containsKey(PFProcessTraitementAdaptationEnum.DECISION_PARTIEL_CHANGEMENT_EXCEDANT)) {
                Integer compteur = listCompteur
                        .get(PFProcessTraitementAdaptationEnum.DECISION_PARTIEL_CHANGEMENT_EXCEDANT);
                compteur = new Integer(compteur.intValue() + 1);
                listCompteur.put(PFProcessTraitementAdaptationEnum.DECISION_PARTIEL_CHANGEMENT_EXCEDANT, compteur);
            }

            if (values.containsKey(PFProcessTraitementAdaptationEnum.PARTIEL_EN_OCTROI)) {
                Integer compteur = listCompteur.get(PFProcessTraitementAdaptationEnum.PARTIEL_EN_OCTROI);
                compteur = new Integer(compteur.intValue() + 1);
                listCompteur.put(PFProcessTraitementAdaptationEnum.PARTIEL_EN_OCTROI, compteur);
            }
        }

        for (JadeAbstractModel entity : modelEntities) {
            SimpleEntity monEntity = (SimpleEntity) entity;

            Map<Enum<?>, String> values = mapSavedValue.get(monEntity.getIdEntite());
            if (values.containsKey(PFProcessTraitementAdaptationEnum.CAS_AVEC_RETENU)) {
                listCasAvecRetenu.add(monEntity.getDescription());
            }
        }

        DemandeTraitementMasseComptageSearchModel demandeTraitementDeMasseComptageSearchModel = new DemandeTraitementMasseComptageSearchModel();
        demandeTraitementDeMasseComptageSearchModel
                .setForExecutionProcess(jadeInfo.getSimpleExecutionProcess().getId());
        demandeTraitementDeMasseComptageSearchModel.setForKeyProperty(PFProcessTraitementAdaptationEnum.DEMANDE_FERME
                .toString());
        listCompteur.put(PFProcessTraitementAdaptationEnum.DEMANDE_FERME,
                JadePersistenceManager.count(demandeTraitementDeMasseComptageSearchModel));

        String sql = "Select count(DISTINCT {schema}.JAPRENTI.DESCRI)  as NOMBRE " + " FROM {schema}.JAPRENTI "
                + " INNER JOIN {schema}.JAPRPROP ON ( {schema}.JAPRENTI.IDENTI = {schema}.JAPRPROP.IDENTI )"
                + " WHERE ( {schema}.JAPRPROP.IDEXPR={idEXPR} AND {schema}.JAPRPROP.PROVAL='SANS_DATE_DE_FIN' )";

        sql = sql.replace("{schema}", JadePersistenceUtil.getDbSchema());
        sql = sql.replace("{idEXPR}", jadeInfo.getSimpleExecutionProcess().getId());

        ArrayList<HashMap<String, Object>> result = PersistenceUtil.executeQuery(sql, this.getClass());

        int nbSansDateFin = 0;
        for (HashMap<String, Object> value : result) {
            nbSansDateFin = Integer.valueOf(value.get("NOMBRE").toString());
        }

        listCompteur.put(PFTypePopulationEnum.SANS_DATE_DE_FIN, nbSansDateFin);
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

        // Demandes validées sans date de fin à traiter :
        Integer demandeSansDateFin = listCompteur.get(PFTypePopulationEnum.SANS_DATE_DE_FIN);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DEMANDE_VALIDE_SANS_DATE_FIN")
                + demandeSansDateFin.toString() + System.getProperty("line.separator"));

        // Demandes fermées :
        Integer demandeFermees = listCompteur.get(PFProcessTraitementAdaptationEnum.DEMANDE_FERME);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DEMANDES_FERMEES")
                + demandeFermees.toString() + System.getProperty("line.separator"));

        // Demandes réouvertes (fermées + partiel cloturé en fin d'année dernière) :
        Integer demandeReouvertes = listCompteur.get(PFProcessTraitementAdaptationEnum.ID_DEMANDE_COPIE);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DEMANDES_REOUVERTES")
                + demandeReouvertes.toString() + System.getProperty("line.separator"));

        // Partiels sont passés en octroi :
        Integer demandePasseEnOctroi = listCompteur.get(PFProcessTraitementAdaptationEnum.PARTIEL_EN_OCTROI);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DECISION_PARTIEL_EN_OCTROI")
                + demandePasseEnOctroi.toString() + System.getProperty("line.separator"));

        // Partiels restés en partiel mais avec changement d'excédent :
        Integer demandeResteEnPartiel = listCompteur
                .get(PFProcessTraitementAdaptationEnum.DECISION_PARTIEL_CHANGEMENT_EXCEDANT);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DECISION_PARTIEL_EN_PARTIEL_CHANGEMENT_EXCEDENT")
                + demandeResteEnPartiel.toString() + System.getProperty("line.separator"));

        // Décisions imprimées :
        Integer decisionsImprimes = listCompteur.get(PFProcessTraitementAdaptationEnum.DECISION_IMPRIMES);
        body.append(BSessionUtil.getSessionFromThreadContext().getLabel(
                "SERVICE_TRAITEMENT_ADAPTATION_DECISION_IMPRIMEES")
                + decisionsImprimes.toString() + System.getProperty("line.separator"));

        // Lister les cas avec retenu à sortir dans le mail (Cas en erreur non sortit)
        body.append(System.getProperty("line.separator")
                + BSessionUtil.getSessionFromThreadContext().getLabel("SERVICE_TRAITEMENT_ADAPTATION_CAS_AVEC_RETENU")
                + System.getProperty("line.separator"));

        for (String str : listCasAvecRetenu) {
            body.append(str + System.getProperty("line.separator"));
        }

        return body.toString();
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut jadeInfo) {
        this.jadeInfo = jadeInfo;
        mapSavedValue = jadeInfo.getPropertiesEntities();
        if (jadeInfo.getSimpleEntiteSearch() != null) {
            modelEntities = jadeInfo.getSimpleEntiteSearch().getSearchResults();
        }
    }
}

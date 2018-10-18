package ch.globaz.pegasus.businessimpl.utils.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AnnexesDecisionHandler {

    private static Map<String, String> annexesAuto = null;

    /**
     * Retourne les annexes pour une décision en fonction des paramètre métier
     * 
     * @param versionDroit
     *            , la version du droit de la décision
     * @param isDecisionCourante
     *            , décision actuelle, courante
     * @return une liste de simpleAnnexesDecision
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static ArrayList<SimpleAnnexesDecision> getAnnexesList(VersionDroit versionDroit,
            Boolean isDecisionCourante, Boolean isDacRefus, String dateDemandeToCheck, Langues langueTiers)
            throws DecisionException, DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        ArrayList<SimpleAnnexesDecision> listeAnnexes = new ArrayList<SimpleAnnexesDecision>();

        AnnexesDecisionHandler.annexesAuto = AnnexesDecisionHandler.loadAnnexes(langueTiers);

        // Si version de droit = 1 et version courante on ajoute toute la map
        if (AnnexesDecisionHandler.isVersionInitiale(versionDroit, isDecisionCourante, dateDemandeToCheck)) {

            // on ajoute toutes les annexes présents dans la map
            for (String cle : AnnexesDecisionHandler.annexesAuto.keySet()) {
                SimpleAnnexesDecision annexesDecision = new SimpleAnnexesDecision();
                annexesDecision.setValeur(AnnexesDecisionHandler.annexesAuto.get(cle));

                // différenciation de l'annexe billag pour la gestion auto via la case a cocher dans l'écran
                if (cle.equals(IPCDecision.BILLAG_ANNEXES_STRING) && !isDacRefus) {
                    annexesDecision.setCsType(IPCDecision.ANNEXE_BILLAG_AUTO);
                    listeAnnexes.add(annexesDecision);
                } else if (!isDacRefus) {
                    annexesDecision.setCsType(IPCDecision.ANNEXE_COPIE_MANUEL);
                    listeAnnexes.add(annexesDecision);
                }

            }
        } else {
            // uniquement notice explicative, si présente
            if (AnnexesDecisionHandler.annexesAuto.containsKey(IPCDecision.NOTICE_ANNEXES_STRING)) {
                SimpleAnnexesDecision annexesDecision = new SimpleAnnexesDecision();
                annexesDecision.setValeur(AnnexesDecisionHandler.annexesAuto.get(IPCDecision.NOTICE_ANNEXES_STRING));
                annexesDecision.setCsType(IPCDecision.ANNEXE_COPIE_MANUEL);
                listeAnnexes.add(annexesDecision);
            }

        }

        return listeAnnexes;
    }

    /**
     * Retourne l'état version initiale de la demande et du droit Si la demande est en version initiales et si le droit
     * aussi Demande: ne doit pas être précéde par une autre demande contigue Droit: la version doit être 1
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    private static Boolean isVersionInitiale(VersionDroit versionDroit, Boolean isDecisionCourante,
            String dateDemandeToCheck) throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (versionDroit.getSimpleVersionDroit().getNoVersion().equals("1") && isDecisionCourante) {
            if (PegasusImplServiceLocator.getSimpleDemandeService().isDemandeInitial(
                    versionDroit.getDemande().getSimpleDemande(), dateDemandeToCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Chargement des annexes en fonction du fichier de properties pour la plupart Les annexes billag sont
     * automatiquement chargée
     * 
     * @return une map avec clé:idLabel, et valeur:label
     * @throws DecisionException
     */
    private static Map<String, String> loadAnnexes(Langues langueTiers) throws DecisionException {

        LinkedHashMap<String, String> annexeAuto = new LinkedHashMap<String, String>();
        // rcuépération de la session pur les labels
        BSession session = BSessionUtil.getSessionFromThreadContext();

        if (session == null) {
            throw new DecisionException("Unable to load annexes for decisions, the session is null");
        }

        try {
            // Ajout dans l'ordre!!!! LinkedHasMap
            // notice
            // Utiliser la langue et rechercher en fonction des labels
            if (Boolean.parseBoolean(session.getApplication().getProperty("pegasus.decision.annexe.notice.defaut"))) {
                annexeAuto.put(IPCDecision.NOTICE_ANNEXES_STRING, LanguageResolver.resolveLibelleFromLabel(
                        langueTiers.getCodeIso(), IPCDecision.NOTICE_ANNEXES_STRING, session));
            }

            // Annexes Billag
            String prop = session.getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
            annexeAuto.put(IPCDecision.BILLAG_ANNEXES_STRING, MessageFormat.format(LanguageResolver.resolveLibelleFromLabel(
                    langueTiers.getCodeIso(), IPCDecision.BILLAG_ANNEXES_STRING, session), prop));

            // exoneration telereseau
            if (Boolean.parseBoolean(session.getApplication().getProperty(
                    EPCProperties.DECISION_ANNEXES_EXO_TELERESEAU_DEFAUT.getPropertyName()))) {
                annexeAuto.put(IPCDecision.EXOTELE_ANNEXES_STRING, LanguageResolver.resolveLibelleFromLabel(
                        langueTiers.getCodeIso(), IPCDecision.EXOTELE_ANNEXES_STRING, session));
            }

            // carte de legitimation
            // if (Boolean.parseBoolean(session.getApplication()
            // .getProperty("pegasus.decision.annexe.cartelegitim.defaut"))) {
            //
            // annexeAuto.put(IPCDecision.CARTE_LEGITIMATION_ANNEXES_STRING,
            // session.getLabel(IPCDecision.CARTE_LEGITIMATION_ANNEXES_STRING));
            // }

            // carte de legitimation et guide
            if (Boolean.parseBoolean(session.getApplication().getProperty(
                    EPCProperties.DECISION_ANNEXES_CARTE_LEGITIM_DEFAUT.getPropertyName()))) {
                annexeAuto.put(IPCDecision.CARTE_LEGITIMATION_ANNEXES_STRING, LanguageResolver.resolveLibelleFromLabel(
                        langueTiers.getCodeIso(), IPCDecision.CARTE_LEGITIMATION_ANNEXES_STRING, session));
            }
            // impots chiens
            if (Boolean.parseBoolean(session.getApplication().getProperty(
                    EPCProperties.DECISION_ANNEXES_IMPOTS_CHIEN_DEFAUT.getPropertyName()))) {
                annexeAuto.put(IPCDecision.EXOCHIENS_ANNEXES_STRING, LanguageResolver.resolveLibelleFromLabel(
                        langueTiers.getCodeIso(), IPCDecision.EXOCHIENS_ANNEXES_STRING, session));
            }

            return annexeAuto;
        } catch (Exception e) {
            throw new DecisionException("Unable to load annexes, an error occured", e);
        }

    }
}

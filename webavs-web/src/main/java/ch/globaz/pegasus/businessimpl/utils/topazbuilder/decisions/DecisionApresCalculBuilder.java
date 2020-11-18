package ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions;

import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.pegasus.utils.PCApplicationUtil;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculOO;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.decision.DecisionBuilder;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.decision.DACPublishHandler;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.util.PegasusPubInfoBuilder;

/**
 * Classe de gestion des documents pour les décisions après-calculs
 * 
 * @author SCE
 */
public class DecisionApresCalculBuilder extends AbstractDecisionBuilder implements DecisionBuilder {

    private final static String DAC_DOC_SUBJECT = "Décision(s) n° ";
    private final static String DAC_DOC_SUBJECT_ADAPTATION = "Adaptation annuelle - Listes des décision - n° ";
    public final static String IS_FTP_PREVALID_AUTO = "isDacPreValidFtpAuto";
    public final static String IS_FTP_VALID_AUTO = "isDacValidFtpAuto";
    private final static String IS_VALID = "isDecisionValidee";
    private final static String IS_PROVISOIRE = "isDecisionProvisoire";
    private final static String IS_PROVISOIRE_DE = "isDecisionProvisoireDe";
    private final static String IS_FROM_ADAPTATION_ANNUELLE = "IsFromAdaptationAnnuelle";

    /* Container pour l 'impression des decuments */
    JadePrintDocumentContainer allDoc = new JadePrintDocumentContainer();
    private Map<Langues, CTDocumentImpl> documentsBabel = null;
    private String dateDoc = null;
    // private DACGedHandler miseEnGedHandler = null;
    DACPublishHandler handlerGlobal = null;
    // private ArrayList<String> decisionsId = null;
    private HashMap<String, DecisionApresCalculOO> listeDAC = new HashMap<String, DecisionApresCalculOO>();
    private HashMap<String, TupleDonneeRapport> listePcal = new HashMap<String, TupleDonneeRapport>();
    private String persRef = null;

    JadePublishDocumentInfo pubInfosGed = null;
    /* Pub infos publication */
    JadePublishDocumentInfo pubInfosGlobal = null;

    /**
     * Définit si la décision à imprimer contient un décompte Pour rappel condition du décompte: décision la plus
     * récente (courante ou plus récente), décision d'octroi et si de type préparation courante, doit avoir le retro
     * également, et version de droit > 1
     * 
     * @return
     * @throws DecisionException
     */
    private Boolean allowDecompteForDac(DecisionApresCalculOO decisionToCheck) throws DecisionException {
        return decisionToCheck.getSimpleDecisionApresCalcul().getIsMostRecent()
                && isDecisionOkForRetro(decisionToCheck)
                && decisionToCheck.getPcAccordee().getSimplePCAccordee().getIsCalculRetro();
    }

    /**
     * Point d'entrée des processus d'impression des décisions après calcul Gestion de la mise en ged le cas échéant
     */
    @Override
    public void build(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        /** instaciation du handler */
        handlerGlobal = handler;
        /** chargement des entités */
        loadDBEntity();
        /** instanciation pubInfos */
        pubInfosGlobal = createMainPubInfos(false);

        /**
         * Dans le cas de la GED, on instancie le pubInfos de d'archivage Si il reste null, aucune sortie pour la pertie
         * GED
         */
        if (handler.getForGed()) {
            pubInfosGed = createMainPubInfos(true);
        }

        /** generation des containers d'impressions */
        generateAllDecisions(handlerGlobal.getDecisionsId());
        /** définition des déstinations pour les containers */
        mergeAllUniqueDecision();
        mergeAll();

    }
    @Override
    public void buildDecisionForAdaptation(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        /** instaciation du handler */
        handlerGlobal = handler;
        /** chargement des entités */
        loadDBEntity();
        /** instanciation pubInfos */
        pubInfosGlobal = createMainPubInfos(false);

        /**
         * Dans le cas de la GED, on instancie le pubInfos de d'archivage Si il reste null, aucune sortie pour la pertie
         * GED
         */
        if (handler.getForGed()) {
            pubInfosGed = createMainPubInfos(true);
        }

        /** generation des containers d'impressions */
        generateAllDecisions(handlerGlobal.getDecisionsId());
        /** définition des déstinations pour les containers */
        mergeAll();
        mergeAllUniqueDecision();

    }

    @Override
    public void buildDecisionForGedOnly(DACPublishHandler handler) throws Exception {
        /** instaciation du handler */
        handlerGlobal = handler;
        /** chargement des entités */
        loadDBEntity();
        /** instanciation pubInfos */
        pubInfosGlobal = createMainPubInfos(false);

        /**
         * Dans le cas de la GED, on instancie le pubInfos de d'archivage Si il reste null, aucune sortie pour la pertie
         * GED
         */
        if (handler.getForGed()) {
            pubInfosGed = createMainPubInfos(true);
        }

        /** generation des containers d'impressions */
        generateAllDecisions(handlerGlobal.getDecisionsId());
        /** définition des déstinations pour les containers */
        if(!PCApplicationUtil.isCantonVS()){
            mergeAllUniqueDecision();
        }
        mergeAll();
    }

    /**
     * Point d'entrée pour la publication des décisions après-calculs via ftp Commme la Ged, chaque décision sera publié
     * séparément
     */
    @Override
    public void buildDecisionsForFtp(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        /** instaciation du handler */
        handlerGlobal = handler;
        /** chargement des entités */
        loadDBEntity();

        // etat de la décicions, on récupère la première, pour prévalidation ok il y a qu'une, validé --> ok elles sont
        // toutes validés
        Boolean isValide = listeDAC.get(handlerGlobal.getDecisionsId().get(0)).getDecisionHeader()
                .getSimpleDecisionHeader().getCsEtatDecision().equals(IPCDecision.CS_ETAT_DECISION_VALIDE);

        // definition du pubinfos pour la publication ftp
        if (isValide) {
            pubInfosGlobal = getBasePubInfo(new PegasusPubInfoBuilder().publish().validFtp().getPubInfo());
        } else {
            pubInfosGlobal = getBasePubInfo(new PegasusPubInfoBuilder().publish().preValidFtp().getPubInfo());
        }

        pubInfosGlobal.setDocumentProperty("persref", handler.getPersref());

        generateAllDecisions(handlerGlobal.getDecisionsId());
        /** définition des déstinations pour les containers */
        mergeAllUniqueDecisionFTP();

    }

    @Override
    public void buildForFtpValidation(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        /** instaciation du handler */
        handlerGlobal = handler;
        /** chargement des entités */
        loadDBEntity();

        pubInfosGlobal = getBasePubInfo(new PegasusPubInfoBuilder().publish().validFtp().getPubInfo());
        pubInfosGlobal.setDocumentProperty("persref", handler.getPersref());

        // this.pubInfosGed.setDocumentProperty("persref", handler.getPersref());

        /** generation des containers d'impressions */
        generateAllDecisions(handlerGlobal.getDecisionsId());
        /** définition des déstinations pour les containers */
        mergeAllUniqueDecision();
        mergeAll();
    }

    public void buildFtpValidation() {

    }

    /**
     * Creation du container pubInofs englobant
     * 
     * @param forGes défini si doit être destiné à la ged
     */
    protected JadePublishDocumentInfo createMainPubInfos(Boolean forGed) throws Exception {

        // Si pour ged on décore le pubInfos avec ajout gestion GED
        if (forGed) {
            return getBasePubInfo(new PegasusPubInfoBuilder().ged().getPubInfo());
        }
        // return this.getBasePubInfo(PegasusPubInfosFactory.getPubInfo(TypePubInfos.STANDARD_PUBLISH));
        return getBasePubInfo(new PegasusPubInfoBuilder().publish().getPubInfo());
    }

    /**
     * Création du pubInfos pour la ged uniquement
     * 
     * @return
     * @throws Exception
     */
    protected JadePublishDocumentInfo createMainPubInfosForGedOnly() throws Exception {
        // return this.getBasePubInfo(PegasusPubInfosFactory.getPubInfo(TypePubInfos.STANDARD_PUBLISH));
        return getBasePubInfo(new PegasusPubInfoBuilder().ged().getPubInfo());
    }

    /**
     * @param decisionsId
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws Exception
     */
    private void generateAllDecisions(ArrayList<String> decisionsId) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {

        /** Parcours des décisions à publier */
        for (String decision : decisionsId) {
            DecisionApresCalculOO decisionToPrint = listeDAC.get(decision);

            new SingleDACBuilder().build(decisionToPrint, listePcal.get(decision), documentsBabel, allDoc,
                    handlerGlobal, dateDoc, persRef, allowDecompteForDac(decisionToPrint));

        }

        handlerGlobal.setContainerPublication(allDoc);
    }

    /**
     * Retourne le conteneur PubInfo de base
     * 
     * @param toMerge
     * @return
     * @throws Exception
     */
    public JadePublishDocumentInfo getBasePubInfo(JadePublishDocumentInfo toMerge) throws Exception {
        if(handlerGlobal.getFromAdaptation()){
            toMerge.setDocumentTitle(DecisionApresCalculBuilder.DAC_DOC_SUBJECT_ADAPTATION
                    + handlerGlobal.getIdProcessusPC());
            toMerge.setDocumentSubject(DecisionApresCalculBuilder.DAC_DOC_SUBJECT_ADAPTATION
                    + handlerGlobal.getIdProcessusPC());
        }else{
            toMerge.setDocumentTitle(DecisionApresCalculBuilder.DAC_DOC_SUBJECT
                    + getDecisionsSubjects(handlerGlobal.getDecisionsId()));
            toMerge.setDocumentSubject(DecisionApresCalculBuilder.DAC_DOC_SUBJECT
                    + getDecisionsSubjects(handlerGlobal.getDecisionsId()));
        }

        toMerge.setOwnerEmail(handlerGlobal.getMailGest());
        toMerge.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, handlerGlobal.getMailGest());
        toMerge.setDocumentDate(handlerGlobal.getDateDoc());
        // n° inforom
        toMerge.setDocumentType(IPRConstantesExternes.PC_REF_INFOROM_DECISION_APRES_CALCUL);
        toMerge.setDocumentTypeNumber(IPRConstantesExternes.PC_REF_INFOROM_DECISION_APRES_CALCUL);
        toMerge.setOwnerId(getSession().getUserId());

        // pour l'ajout de la watermark decision non validee
        toMerge.setDocumentProperty(DecisionApresCalculBuilder.IS_VALID, getEtatValidationDecision());
        // pour l'ajout de la watermark decision provisoire
        toMerge.setDocumentProperty(DecisionApresCalculBuilder.IS_PROVISOIRE, getEtatValidationProvisoire());
        // pour l'ajout de la watermark decision provisoire allemand
        toMerge.setDocumentProperty(DecisionApresCalculBuilder.IS_PROVISOIRE_DE, getEtatValidationProvisoireDe());
        toMerge.setDocumentProperty(DecisionApresCalculBuilder.IS_FROM_ADAPTATION_ANNUELLE,handlerGlobal.getFromAdaptation().booleanValue() == true ? "true":"false");
        // Remplissage des propriétés du document pour le tiers (clés pixis.*)
        TIDocumentInfoHelper.fill(toMerge, getIdTiers(), getSession(), null, null, null);

        // pour gestion file
        return toMerge;
    }

    /**
     * Retourn les numéros de décisions a ajouté au sujet
     * 
     * @param decisionsId
     *            , Liste des idDecision
     * @return chaine de caratère
     */
    private String getDecisionsSubjects(ArrayList<String> decisionsId) {
        // Chaine contenant les no de décision pour le sujet du mail
        StringBuilder decsNo = new StringBuilder("");
        // Génération des containers par décisions
        for (String decision : decisionsId) {
            decsNo.append(listeDAC.get(decision).getDecisionHeader().getSimpleDecisionHeader().getNoDecision());
            decsNo.append(", ");
        }

        return decsNo.substring(0, decsNo.length() - 2).toString();
    }

    /**
     * Retourne true/false si une decision est validée, donc toutes pour ce droit
     * 
     * @return
     */
    private String getEtatValidationDecision() {

        for (String decision : handlerGlobal.getDecisionsId()) {
            if (IPCDecision.CS_ETAT_DECISION_VALIDE.equals(listeDAC.get(decision).getDecisionHeader()
                    .getSimpleDecisionHeader().getCsEtatDecision())) {
                return "true";
            }
        }
        return "false";

    }

    /**
     * Retourne true/false si une decision est provisoire
     *
     * @return
     */
    private String getEtatValidationProvisoire() throws Exception {

        for (String decision : handlerGlobal.getDecisionsId()) {
            if (IPCDecision.CS_ETAT_DECISION_VALIDE.equals(listeDAC.get(decision).getDecisionHeader()
                    .getSimpleDecisionHeader().getCsEtatDecision())
                    && listeDAC.get(decision).getDecisionHeader().getSimpleDecisionHeader().isDecisionProvisoire()) {
                if(!Langues.Allemand.equals(LanguageResolver.resolveISOCode(getTiers().getLangue()))) {
                    return "true";
                }
            }
        }
        return "false";
    }

    /**
     * Retourne true/false si une decision est provisoire en allemand
     *
     * @return
     */
    private String getEtatValidationProvisoireDe() throws Exception {
        for (String decision : handlerGlobal.getDecisionsId()) {
            if (IPCDecision.CS_ETAT_DECISION_VALIDE.equals(listeDAC.get(decision).getDecisionHeader()
                    .getSimpleDecisionHeader().getCsEtatDecision())
                    && listeDAC.get(decision).getDecisionHeader().getSimpleDecisionHeader().isDecisionProvisoire()) {
                if(Langues.Allemand.equals(LanguageResolver.resolveISOCode(getTiers().getLangue()))){
                    return "true";
                }
            }
        }
        return "false";
    }

    /**
     * Retourne l'idTier de la decision
     * 
     * @return
     */
    private String getIdTiers() {
        for (DecisionApresCalculOO dac : listeDAC.values()) {
            String idTier = dac.getDecisionHeader().getPersonneEtendue().getPersonneEtendue().getIdTiers();
            if (!JadeStringUtil.isBlank(idTier)) {
                return idTier;
            }
        }
        return null;
    }

    /**
     * Retourne l'idTier de la decision
     *
     * @return
     */
    private TiersSimpleModel getTiers() {
        for (DecisionApresCalculOO dac : listeDAC.values()) {
            TiersSimpleModel idTier = dac.getDecisionHeader().getPersonneEtendue().getTiers();
            if (idTier != null) {
                return idTier;
            }
        }
        return null;
    }

    private Boolean isDecisionOkForRetro(DecisionApresCalculOO decision) {
        boolean retroReady = true;

        // Si c'est une préparation courante on s'assure que le rétro est également pris
        if (decision.getSimpleDecisionApresCalcul().getCsTypePreparation().equals(IPCDecision.CS_PREP_COURANT)) {
            for (DecisionApresCalculOO decisionOO : listeDAC.values()) {
                if (decisionOO.getSimpleDecisionApresCalcul().getCsTypePreparation().equals(IPCDecision.CS_PREP_RETRO)) {
                    return true;
                }
            }
            return false;
        }
        // si c'est du retro...
        if (decision.getSimpleDecisionApresCalcul().getCsTypePreparation().equals(IPCDecision.CS_PREP_RETRO)) {
            return false;
        }
        return retroReady;
    }

    /**
     * Chargement des entités de bases de données. Decision, Babel, et TupleDonneesRapport
     * 
     * @param idDecision
     * @throws CatalogueTexteException
     * @throws Exception
     */
    private void loadDBEntity() throws CatalogueTexteException, Exception {

        // pour chaque decision contenue dans la liste des id de decisions, on charge la dac, et le pcal
        for (String decision : handlerGlobal.getDecisionsId()) {
            // liste dac
            DecisionApresCalculOO decisionApresCalculOO = PegasusServiceLocator.getDecisionApresCalculService()
                    .readForOO(decision);
            listeDAC.put(decision, decisionApresCalculOO);
            // liste pcal
            byte[] tupleRoot = listeDAC.get(decision).getPlanCalcul().getResultatCalcul();
            if (tupleRoot == null) {
                SimplePlanDeCalcul planDeCalcul = PegasusServiceLocator.getSimplePlanDeCalculService()
                        .readPlanRetenuForIdPca(decisionApresCalculOO.getPcAccordee().getId());
                tupleRoot = planDeCalcul.getResultatCalcul();
            }
            String byteArrayToString = new String(tupleRoot);

            listePcal.put(decision,
                    PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(byteArrayToString));
        }
        // Chargement BAbel
        documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForTypeDecision(
                IPCCatalogueTextes.BABEL_DOC_NAME_APRES_CALCUL);
    }

    /**
     * définition du pubInfos de sortie pour les documents destinés à la publication
     */
    private void mergeAll() {
        allDoc.setMergedDocDestination(pubInfosGlobal);
    }

    /**
     * définition du pubInfos de sortie pour les documents destinés à être publié (ou archivé) de manière unitaire
     */
    private void mergeAllUniqueDecision() {
        for (JadePrintDocumentContainer containerGed : handlerGlobal.getContainersGed()) {
            containerGed.setMergedDocDestination(pubInfosGed);
        }
    }

    private void mergeAllUniqueDecisionFTP() {
        for (JadePrintDocumentContainer containerGed : handlerGlobal.getContainersGed()) {
            containerGed.setMergedDocDestination(pubInfosGlobal);
        }
    }


}

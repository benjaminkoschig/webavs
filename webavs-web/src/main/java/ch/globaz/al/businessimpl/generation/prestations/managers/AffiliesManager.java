package ch.globaz.al.businessimpl.generation.prestations.managers;

import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.AffilieListComplexModel;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.generation.prestations.GenerationService;
import ch.globaz.al.business.services.models.dossier.DossierComplexModelService;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextGlobal;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.businessimpl.services.models.dossier.AffilieListComplexModelServiceImpl;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Manager d'affilié pour la génération globale de prestation. Il se charge de charger les affiliés et leurs dossier et
 * de les placer dans le conteneur
 * 
 * @author jts
 * 
 */
public class AffiliesManager extends AbstractManager {

    /**
     * Identifiant unique de la génération
     */
    private String genAffilieUUID;
    /**
     * Context Jade
     */
    private JadeContextImplementation jadeContextImpl;
    /**
     * Instance du logger devant contenir les messages des erreur survenant pendant la génération
     */
    private ProtocoleLogger logger = null;
    /**
     * Numéro de génération. Généralement l'id du traitement ayant exécuté la génération
     */
    private String numGeneration = null;
    /**
     * Période à générer
     */
    private String periode = null;
    /**
     * Type(s) de cotisations à traiter
     * 
     * @see ch.globaz.al.business.constantes.ALConstPrestations#TYPE_DIRECT
     * @see ch.globaz.al.business.constantes.ALConstPrestations#TYPE_COT_PAR
     * @see ch.globaz.al.business.constantes.ALConstPrestations#TYPE_COT_PERS
     * @see ch.globaz.al.business.constantes.ALConstPrestations#TYPE_INDIRECT_GROUPE
     */
    private String typeCot = null;

    /**
     * Constructeur
     * 
     * @param periode
     *            Période (Format MM.AAAA)
     * @param typeCot
     *            le type de cotisation à traiter {@link AffiliesManager#typeCot}
     * @param generationHandler
     *            Handler maître
     * @param numGeneration
     *            Numéro de génération. Généralement l'id du traitement ayant exécuté la génération
     * @param logger
     *            Instance du logger devant contenir les messages des erreur survenant pendant la génération
     * @param genAffilieUUID
     *            Identifiant unique de la génération
     * @param jadeContextImpl
     *            Contexte Jade
     */
    protected AffiliesManager(String periode, String typeCot, MainGenerationHandler generationHandler,
            String numGeneration, ProtocoleLogger logger, JadeContextImplementation jadeContextImpl,
            String genAffilieUUID) {
        super(generationHandler);
        this.periode = periode;
        this.typeCot = typeCot;
        this.numGeneration = numGeneration;
        this.logger = logger;
        this.jadeContextImpl = jadeContextImpl;
        this.genAffilieUUID = genAffilieUUID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.generation.prestations.managers.AbstractManager #handle()
     */
    @Override
    protected void handle() {

        ContainerAffilies container = ContainerAffilies.getInstance(genAffilieUUID);

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(),
                    ContextProvider.getNewJadeThreadContext(jadeContextImpl).getContext());

            // récupération des services
            AffilieListComplexModelServiceImpl aff = (AffilieListComplexModelServiceImpl) ALImplServiceLocator
                    .getAffilieListComplexModelService();
            DossierComplexModelService dos = ALServiceLocator.getDossierComplexModelService();
            GenerationService gen = ALImplServiceLocator.getGenerationService();

            // modèles de recherche
            AffilieListComplexSearchModel affilies = gen.initSearchAffilies(periode, typeCot);
            affilies.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            DossierComplexSearchModel dossiers = null;

            // recherche des affiliés
            affilies = aff.search(affilies);

            // récupération des dossiers de chaque affilié
            for (int i = 0; i < affilies.getSize(); i++) {

                String numAffilie = ((AffilieListComplexModel) affilies.getSearchResults()[i]).getNumeroAffilie();
                String periodicite = ((AffilieListComplexModel) affilies.getSearchResults()[i]).getPeriodicite();
                String debutPeriode = ALConstPrestations.TYPE_DIRECT.equals(typeCot) ? periode : ALDateUtils
                        .getDebutPeriode(periode, periodicite);

                dossiers = gen.initSearchDossiers(debutPeriode, typeCot);
                dossiers.setForNumeroAffilie(numAffilie);
                dossiers.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                dossiers = dos.search(dossiers);

                if ((dossiers.getSize() > 0)) {

                    // ajout des dossiers à traiter dans le conteneur
                    container.registerDossiers(ContextGlobal.getInstance(dossiers.getSearchResults(), ContextAffilie
                            .getContextAffilie(debutPeriode, periode, ALCSPrestation.GENERATION_TYPE_GEN_GLOBALE,
                                    numAffilie, periodicite, numGeneration, logger, jadeContextImpl)));
                }
            }
        } catch (Exception e) {
            // ajout du message + stackTrace dans le log de la génération
            StringBuffer message = new StringBuffer("Classe : AffiliesManager\n");
            message.append("Période : ").append(periode).append("\n");
            message.append("Type cotisation : ").append(typeCot).append("\n");
            message.append("Message : ").append(e.getMessage()).append("\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            message.append(sw.toString());
            logger.addFatalError(new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ContextPrestation.class
                    .getName(), message.toString()));
        } finally {
            container.notifyFinishedProducing();
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
}
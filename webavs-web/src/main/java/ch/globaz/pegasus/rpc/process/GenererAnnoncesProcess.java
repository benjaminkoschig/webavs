package ch.globaz.pegasus.rpc.process;

import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.job.common.JadeJobQueueNames;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.ExceptionFormatter;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.process.byitem.ProcessItemsHandlerJadeJob;
import ch.globaz.pegasus.businessimpl.services.rpc.RpcAnnonceGenerator;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.AnnonceRepositoryJade;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.LotAnnonceRepository;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.RpcDataConverter;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.RpcDataLoader;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.RpcDatasListConverter;
import ch.globaz.pegasus.rpc.businessImpl.sedex.ExecutionMode;
import ch.globaz.pegasus.rpc.domaine.EtatLot;
import ch.globaz.pegasus.rpc.domaine.LotAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.LotRpcWithNbAnnonces;
import ch.globaz.pegasus.rpc.domaine.TypeLot;

public class GenererAnnoncesProcess extends ProcessItemsHandlerJadeJob<AnnonceItem> {
    private static final Logger LOG = LoggerFactory.getLogger(GenererAnnoncesProcess.class);
    public static final String KEY = "pegasus.rpc.annonce";

    private transient LotAnnonceRpc lotAnnonce;
    private transient RpcDatasListConverter rpcDatasConvertors;
    private ExecutionMode executionMode;
    private final transient LotAnnonceRepository lotAnnonceRepository = new LotAnnonceRepository();
    private transient Protocol2469 protocol;
    private RpcAnnonceGenerator annoncesedexGenerator;
    private final transient List<AnnonceItem> annonces = new ArrayList<AnnonceItem>();
    private final transient AnnonceRepositoryJade annonceRepo = new AnnonceRepositoryJade();

    private transient ToleranceDifferenceAnnonce toleranceAnnonces = new ToleranceDifferenceAnnonce();

    public GenererAnnoncesProcess() {
    }

    public GenererAnnoncesProcess(ExecutionMode executionMode) {
        this.executionMode = executionMode;
    }

    public ExecutionMode getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(ExecutionMode executionMode) {
        this.executionMode = executionMode;
    }

    /**
     * Renvoie le nom de la file d'attente d'exécution.
     * 
     * @return le nom de la file d'attente d'exécution
     */
    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_BATCH_JOB_QUEUE;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("PEGASUS_PROCESS_GENERER_ANNONCE_RPC_DESC");
    }

    @Override
    public String getName() {
        return "PEGASUS_PROCESS_GENERER_ANNONCE_RPC_NAME";
    }

    @Override
    public void before() {
        LOG.info("The process is started: {} " + getName());

        notStopProcessIfHasTooMutchErrors().parallelWithOutSession();
        // on fait l'instansiation ici pour ne pas le faire 2 fois
        annoncesedexGenerator = new RpcAnnonceGenerator();
        protocol = new Protocol2469(annoncesedexGenerator.getInfoCaisse());

        LOG.info("{} run() : executionMode = [{}]", getClass().getSimpleName(), executionMode.name());

        protocol.setExecutionMode(executionMode);
        protocol.setSession(getSession());

        try {
            BSessionUtil.initContext(getSession(), this);

            // Clear previous annonces and lots created on current month
            if (executionMode == ExecutionMode.REGENERATE || executionMode == ExecutionMode.GENERATE_AND_SEND
                    || executionMode == ExecutionMode.SEND) {
                cleanUpAnnoncesAndLots();
            }

            RpcDataLoader annonceDataLoader = new RpcDataLoader();
            protocol.setInfosRpcDataLoader(annonceDataLoader.getInfos());
            rpcDatasConvertors = annonceDataLoader.loadByDernierPaiement();

            toleranceAnnonces.setToleranceAnnonce(annonceDataLoader.getToleranceDifferenceAnnonces());

            lotAnnonce = new LotAnnonce(Date.now(), EtatLot.EN_GENERATION, getJobInfos().getIdJob(), TypeLot.INITIAL);
            if (!executionMode.isSimultate()) {
                lotAnnonce = lotAnnonceRepository.create(lotAnnonce);
            }
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
        addFormatersForException();
    }

    private void cleanUpAnnoncesAndLots() {
        final List<LotRpcWithNbAnnonces> lotCollection = lotAnnonceRepository.searchLastsLots(1);
        if (!lotCollection.isEmpty()) {
            String annonceMonth = lotCollection.get(0).getLot().getDateEnvoi().getMois();
            String currentMonth = Date.now().getMois();

            if (annonceMonth.equals(currentMonth)) {

                String lotId = lotCollection.get(0).getLot().getId();
                AnnonceRepositoryJade annonceRepository = new AnnonceRepositoryJade();
                annonceRepository.deleteAnnonceByLot(lotId);

                LotAnnonceRepository lotRepository = new LotAnnonceRepository();
                lotRepository.deleteById(lotId);
            }
        }
    }

    @Override
    public List<AnnonceItem> resolveItems() {
        List<AnnonceItem> list = new ArrayList<AnnonceItem>();
        for (RpcDataConverter rpcDataConverter : rpcDatasConvertors) {
            list.add(new AnnonceItem(rpcDataConverter, lotAnnonce, annoncesedexGenerator));
        }
        LOG.info("Nb annonce resolved: {}", list.size());
        annonces.addAll(list);

        // Vérification si il y a une trop grand différence de cas annoncés entre les deux anciens lots et celui-ci.
        toleranceAnnonces.setDepasserTolerance(hasTooMuchDifferenceAnnoncesWithOlderLots(annonces.size()));
        protocol.setToleranceAnnonces(toleranceAnnonces);
        return list;
    }

    @Override
    public void after() {
        try {
            List<RpcPlausiResutForXls> listPlausis = new ArrayList<RpcPlausiResutForXls>();

            if (!annonces.isEmpty() && !isOnError()) {
                AnnonceItems annonceItems = new AnnonceItems(annonces);
                protocol.setAnnonceItemsPlausiKo(annonceItems.filtreAnnonceWithPlausiKo());
                protocol.setSummer(annonceItems.getSummer());
                protocol.setPlausiSummary(annonceItems.generatePlausiSummary());
                listPlausis = annonceItems.generateAnnoncesForDisplay();
                LOG.info("Nb annonce: {}", annonces.size());
                LOG.info("Nb lien decision: {}", annonceItems.getNbDecision());

                protocol.setNombreAvecCalcul(annonceItems.countAnnonce101());
                LOG.info("Nb item 2469_101: {}", protocol.getNombreAvecCalcul());

                protocol.setNombreSansCalcul(annonceItems.countAnnonce201());
                LOG.info("Nb item 2469_201: {}", protocol.getNombreSansCalcul());

                protocol.setNombreAnnulation(annonceItems.countAnnonce301());
                LOG.info("Nb item 2469_301: {}", protocol.getNombreAnnulation());

                if (!executionMode.isSimultate()) {
                    annonceRepo.buildPKproviders(annonces.size(), annonceItems.getNbDecision());

                    for (AnnonceItem annonceItem : annonces) {
                        annonceRepo.create(annonceItem.getAnnonce());
                    }
                    if (executionMode.mustSendAnnonce()) {
                        annoncesedexGenerator.sendMessages();
                    }
                }
            }
            if (lotAnnonce != null && !executionMode.isSimultate()) {
                lotAnnonce.changeEtatToGenerationTermine();
                lotAnnonceRepository.update(lotAnnonce);
            }
            if (!isOnError()) {
                protocol.setProcessInfos(getJobInfos());
            }
            sendMail(listPlausis);
        } finally {
            try {
                BSessionUtil.stopUsingContext(this);
                annoncesedexGenerator.close();
            } catch (IOException e) {
                LOG.error("Unable to close annoncesedexGenerator", e);
            }
        }
        LOG.info("The process is finished: {} ", getName());
    }

    private boolean hasTooMuchDifferenceAnnoncesWithOlderLots(final long nbCurrentAnnonces) {
        boolean hasTooMuchDiff = false;

        // Recherche le dernier lot avec son nombre d'annonce
        final List<LotRpcWithNbAnnonces> olderLots = lotAnnonceRepository.searchLastsLots(1);

        double currentLotSize = nbCurrentAnnonces;

        // Pour chaque ancien lot, nous allons comparer le nombre de ces annonces avec celui qui est courant afin de
        // savoir si il y a trop de différence afin d'avertir l'utilisateur plus tard
        for (LotRpcWithNbAnnonces olderLot : olderLots) {

            double olderLotSize = olderLot.getNbAnnonces();
            double solde;
            double biggerNumber;

            // Nous prenons le nombre le plus grand avant de le soustraire et l'utiliser comme nombre pour la division
            if (currentLotSize <= olderLotSize) {
                solde = olderLotSize - currentLotSize;
                biggerNumber = olderLotSize;
            } else {
                solde = currentLotSize - olderLotSize;
                biggerNumber = currentLotSize;
            }

            // Si il y a le pourcentage de la tolerance ou plus de différence.
            if (olderLotSize > 0 && solde / biggerNumber >= toleranceAnnonces.getToleranceAnnonce()) {
                LOG.info("Nb annonces {} for older lot with sent date : {}", olderLotSize, olderLot.getLot()
                        .getDateEnvoi());
                LOG.info("Nb annonces {} for current lot", currentLotSize);
                LOG.info("There is a pourcentage of {} between these lots", solde / biggerNumber * 100);
                hasTooMuchDiff = true;
            }
        }

        return hasTooMuchDiff;
    }

    private void sendMail(List<RpcPlausiResutForXls> listPlausis) {
        if (!itemsHasError() && !isOnError()) {
            if (listPlausis.isEmpty()) {
                protocol.sendMail();
            } else {
                protocol.sendMail(generateXlsForPlausi(listPlausis));
            }
        } else {
            sendMailIfHasErrorWithFile(protocol.buildMailBody(true), generateXlsForPlausi(listPlausis));
        }
    }

    private String generateXlsForPlausi(List<RpcPlausiResutForXls> plausis) {
        File file = SimpleOutputListBuilderJade.newInstance().addList(plausis)
                .classElementList(RpcPlausiResutForXls.class).outputName("RPC_PLAUSI").asXls().build();
        return file.getAbsolutePath();
    }

    private void addFormatersForException() {

        ExceptionFormatter<ValidationException> validationExceptionFormatter = new ExceptionFormatter<ValidationException>() {

            @Override
            public String formatte(ValidationException exception, Locale locale) {
                return exception.getFormattedMessage();
            }

            @Override
            public Class<ValidationException> getType() {
                return ValidationException.class;
            }
        };

        ExceptionFormatter<RpcBusinessException> rpcBusinessExceptionFormatter = new ExceptionFormatter<RpcBusinessException>() {

            @Override
            // TODO test unitaire
            public String formatte(RpcBusinessException exception, Locale locale) {
                if (!exception.getParams().isEmpty()) {
                    String[] params = new String[exception.getParams().size()]; // = exception.getParams().toArray(new
                                                                                // String[exception.getParams().size()]);
                    for (int i = 0; i < exception.getParams().size(); i++) {
                        Object param = exception.getParams().get(i);
                        if (param == null) {
                            param = "null";
                        }
                        params[i] = String.valueOf(param);
                    }
                    return JadeThread.getMessage(exception.getLabelMessage(), params);
                }
                return JadeThread.getMessage(exception.getLabelMessage());
            }

            @Override
            public Class<RpcBusinessException> getType() {
                return RpcBusinessException.class;
            }
        };

        validationExceptionFormatter.formatte(new ValidationException(new ArrayList<String>()), null);

        addExceptionFormatter(validationExceptionFormatter);

        addExceptionFormatter(rpcBusinessExceptionFormatter);
    }

}

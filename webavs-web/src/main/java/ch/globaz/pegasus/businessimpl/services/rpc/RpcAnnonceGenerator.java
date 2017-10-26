package ch.globaz.pegasus.businessimpl.services.rpc;

import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.sedex.JadeSedexService;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.ech.xmlns.ech_0058._5.SendingApplicationType;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.WebAvsVersion;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;
import ch.globaz.pegasus.rpc.businessImpl.converter.Converter2469_101;
import ch.globaz.pegasus.rpc.businessImpl.converter.Converter2469_201;
import ch.globaz.pegasus.rpc.businessImpl.converter.Converter2469_301;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.RpcDataLoader;
import ch.globaz.pegasus.rpc.businessImpl.sedex.Sedex2469_101;
import ch.globaz.pegasus.rpc.businessImpl.sedex.Sedex2469_201;
import ch.globaz.pegasus.rpc.businessImpl.sedex.Sedex2469_301;
import ch.globaz.pegasus.rpc.businessImpl.sedex.SedexGeneratorConvertor;
import ch.globaz.pegasus.rpc.businessImpl.sedex.SedexSender;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.plausi.core.PlausiContainer;
import ch.globaz.pegasus.rpc.plausi.core.PlausiResult;
import ch.globaz.pegasus.rpc.plausi.core.PlausisResults;
import ch.globaz.pegasus.utils.RpcUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class RpcAnnonceGenerator implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(RpcAnnonceGenerator.class);

    private final SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ContentType> sedexConvertor101;
    private final SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000201._1.ContentType> sedexConvertor201;
    private final SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ContentType> sedexConvertor301;
    private final InfoCaisse infoCaisse;

    public RpcAnnonceGenerator() {
        SendingApplicationType sendingApplicationType = buildSendingApplicationType();
        SedexSender sedexSender = buildSedexSender();
        infoCaisse = RpcUtil.createInfosCaisse();

        sedexConvertor101 = buildSedex101Convertor(sendingApplicationType, sedexSender);
        sedexConvertor201 = buildSedex201Convertor(sendingApplicationType, sedexSender);
        sedexConvertor301 = buildSedex301Convertor(sendingApplicationType, sedexSender);
    }

    public RpcAnnonceGenerator(SedexSender sedexSender) {
        SendingApplicationType sendingApplicationType = buildSendingApplicationType();
        infoCaisse = RpcUtil.createInfosCaisse();
        sedexConvertor101 = buildSedex101Convertor(sendingApplicationType, sedexSender);
        sedexConvertor201 = buildSedex201Convertor(sendingApplicationType, sedexSender);
        sedexConvertor301 = buildSedex301Convertor(sendingApplicationType, sedexSender);
    }

    public SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ContentType> getSedexConvertor101() {
        return sedexConvertor101;
    }

    public SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000201._1.ContentType> getSedexConvertor201() {
        return sedexConvertor201;
    }

    public SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ContentType> getSedexConvertor301() {
        return sedexConvertor301;
    }

    public InfoCaisse getInfoCaisse() {
        return infoCaisse;
    }

    public String generateByIdAnnonce(String idAnnonce) {
        RpcDataLoader loader = new RpcDataLoader();
        RpcData rpcData = loader.loadByIdAnnonce(idAnnonce);
        PlausisResults results = PlausiContainer.buildPlausis(rpcData);
        if (!results.isAllPlausiOk()) {
            Gson gson = new GsonBuilder().setPrettyPrinting()
                    .registerTypeAdapter(Montant.class, new com.google.gson.JsonSerializer<Montant>() {

                        @Override
                        public JsonElement serialize(Montant src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.getValueDouble());
                        }
                    }).create();
            LOG.error(rpcData.description());
            for (Entry<PlausiResult, Boolean> entry : results.filtrePlausiKo().getPlausis().entrySet()) {
                LOG.error(entry.getKey().getPlausi().getID() + " " + entry.getKey().getPlausi().getReferance() + " "
                        + entry.getKey().getPlausi().getType());
                LOG.error(gson.toJson(entry.getKey()));
            }
        }
        File file;
        try {
            if (!rpcData.hasVersionDroit()) {
                file = sedexConvertor201.generateMessageFile(rpcData);
            } else if (rpcData.isSuppressionDecesRequerant()) {
                RpcUtil.deleteSuffixDecisionId(rpcData.getRpcDecisionRequerantConjoints().get(0).getRequerant()
                        .getDecision(), rpcData.getRpcDecisionRequerantConjoints().get(0).getConjoint().getDecision());
                file = sedexConvertor301.generateMessageFile(rpcData);
            } else {
                file = sedexConvertor101.generateMessageFile(rpcData);
            }
        } catch (ValidationException e) {
            throw new RpcTechnicalException(e.getFormattedMessage(), e);
        }
        return JadeFilenameUtil.normalizePathComponents(file.getAbsolutePath());
    }

    public void sendMessages() {
        sedexConvertor101.sendMessages();
        // les annonces partielles sont envoyées dans le même fichier zip (même dossier temp sedex)
        // sedexConvertor201.sendMessages();
        sedexConvertor301.sendMessages();
    }

    private SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ContentType> buildSedex101Convertor(
            SendingApplicationType sendingApplicationType, SedexSender sedexSender) {
        Sedex2469_101 sedex101 = new Sedex2469_101(sedexSender, infoCaisse, sendingApplicationType, "RPC_101");
        return new SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ContentType>(sedex101,
                new Converter2469_101(infoCaisse));
    }

    private SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000201._1.ContentType> buildSedex201Convertor(
            SendingApplicationType sendingApplicationType, SedexSender sedexSender) {
        Sedex2469_201 sedex201 = new Sedex2469_201(sedexSender, infoCaisse, sendingApplicationType,
                sedexConvertor101.getSedexInfo());
        return new SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000201._1.ContentType>(sedex201,
                new Converter2469_201(infoCaisse));
    }

    private SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ContentType> buildSedex301Convertor(
            SendingApplicationType sendingApplicationType, SedexSender sedexSender) {
        Sedex2469_301 sedex301 = new Sedex2469_301(sedexSender, infoCaisse, sendingApplicationType, "RPC_301");
        return new SedexGeneratorConvertor<ch.eahv_iv.xmlns.eahv_iv_2469_000301._1.ContentType>(sedex301,
                new Converter2469_301(infoCaisse));
    }

    private static SendingApplicationType buildSendingApplicationType() {
        SendingApplicationType applicationType = new SendingApplicationType();
        applicationType.setManufacturer("Globaz");
        applicationType.setProduct("WebAVS");
        applicationType.setProductVersion(WebAvsVersion.getVersion());
        return applicationType;
    }

    private static SedexSender buildSedexSender() {
        try {
            String recipientId = PCproperties.getProperties(EPCProperties.RPC_DESTINATAIRE);
            if (JadeSedexService.getInstance().isServiceStarted()) {
                return new SedexSender(JadeSedexService.getInstance(), recipientId, Jade.getInstance()
                        .getPersistenceDir());
            }
            LOG.info("JadeSedexService is not started we will use a mock like !");
            return new SedexSender(recipientId, Jade.getInstance().getPersistenceDir());
        } catch (PropertiesException e) {
            throw new IllegalArgumentException("Property Exception " + e.toString(), e);
        }
    }

    @Override
    public void close() throws IOException {
        sedexConvertor101.close();
        sedexConvertor201.close();
        sedexConvertor301.close();
    }

}

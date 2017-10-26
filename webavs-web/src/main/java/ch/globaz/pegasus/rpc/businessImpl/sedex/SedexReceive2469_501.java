package ch.globaz.pegasus.rpc.businessImpl.sedex;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.GroupedSedexMessage;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.sedex.message.SimpleSedexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.Message;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce.RetourAnnonceRepository;
import ch.globaz.pegasus.rpc.businessImpl.sedex.converter.RetourPlausi501Converter;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.TypeViolationPlausi;
import ch.globaz.pegasus.rpc.domaine.plausi.AnnoncePlausiRetour;
import ch.globaz.pegasus.rpc.domaine.plausi.PlausiRetour;
import ch.globaz.pegasus.rpc.plausi.PlausiBeanRetour;
import ch.globaz.pegasus.rpc.plausi.PlausiBeanSummary;
import ch.globaz.pegasus.rpc.process.ProtocolRetour2469;
import ch.globaz.pegasus.web.application.PCApplication;

public class SedexReceive2469_501 {
    private static final Logger LOG = LoggerFactory.getLogger(SedexReceive2469_501.class);
    private BSession session;
    private ProtocolRetour2469 protocol;
    private InfoCaisse infoCaisse;

    // constante de DEV
    private String pathWhereXmlAre = "D:\\rpc\\retoursCentral";

    // methode de dev/test unit/utilitaire de dev
    List<AnnoncePlausiRetour> load() {
        RetourPlausi501Converter converter = new RetourPlausi501Converter();
        return UnmarshallerHelper.newInstance(Message.class, pathWhereXmlAre).converter(converter).parallel()
                .unmarshallAndConvert();

    }

    private List<AnnoncePlausiRetour> readXmls(ArrayList<String> listfile) throws FileNotFoundException, JAXBException {
        RetourPlausi501Converter converter = new RetourPlausi501Converter();
        return UnmarshallerHelper.newInstance(Message.class, listfile).converter(converter).parallel()
                .unmarshallAndConvert();
    }

    @Setup
    public void setUp(Properties properties) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        String encryptedUser = properties.getProperty("userSedex");
        if (encryptedUser == null) {
            JadeLogger.error(this, "AFIdeReceptionMessageInfoAboSedexProcess : user sedex null");
            throw new IllegalStateException("AFIdeReceptionMessageInfoAboSedexProcess : user sedex null");
        }
        String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

        String encryptedPass = properties.getProperty("passSedex");
        if (encryptedPass == null) {
            JadeLogger.error(this, "AFIdeReceptionMessageInfoAboSedexProcess : password sedex null");
            throw new IllegalStateException("AFIdeReceptionMessageInfoAboSedexProcess : password sedex null");
        }
        String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

        setSession((BSession) GlobazSystem.getApplication(PCApplication.DEFAULT_APPLICATION_PEGASUS).newSession(
                decryptedUser, decryptedPass));

        infoCaisse = createInfosCaisse();
    }

    @OnReceive
    public void onReceive(SedexMessage messageSedex) throws Exception {

        protocol = new ProtocolRetour2469(infoCaisse);

        ArrayList<String> listfile = new ArrayList<String>();
        // permet de gérer les messages groupés de Sedex
        if (messageSedex instanceof GroupedSedexMessage) {
            for (SimpleSedexMessage message : ((GroupedSedexMessage) messageSedex).simpleMessages) {
                listfile.add(message.getFileLocation());
            }
        } else if (messageSedex instanceof SimpleSedexMessage) {
            listfile.add(messageSedex.getFileLocation());
        }

        List<AnnoncePlausiRetour> annoncePlausiRetour = readXmls(listfile);
        LOG.info("Nombre de retours 2469-00501 :" + annoncePlausiRetour.size());

        BSessionUtil.initContext(getSession(), this);
        try {
            RetourAnnonceRepository repo = new RetourAnnonceRepository();
            repo.createRetourAnnonces(annoncePlausiRetour);
        } finally {
            BSessionUtil.stopUsingContext(this);
        }

        generateFile(annoncePlausiRetour);

    }

    public void generateFile(List<AnnoncePlausiRetour> messages) throws Exception {
        List<PlausiBeanRetour> beans = new ArrayList<PlausiBeanRetour>();
        Map<String, RetourAnnonce> mapPlausi = new HashMap<String, RetourAnnonce>();
        Map<String, Integer> mapCount = new HashMap<String, Integer>();
        Map<TypeViolationPlausi, Integer> mapTypeViolation = new HashMap<TypeViolationPlausi, Integer>();

        for (AnnoncePlausiRetour annoncePlausiRetour : messages) {
            for (PlausiRetour retour : annoncePlausiRetour.getPlausiRetours()) {
                for (RetourAnnonce plausi : retour.getRetours()) {
                    beans.add(new PlausiBeanRetour(annoncePlausiRetour.getBusinessCaseIdRPC(), retour.getNss(), retour
                            .getIdDecision(), plausi));
                    if (!mapPlausi.containsKey(plausi.getCodePlausi())) {
                        mapCount.put(plausi.getCodePlausi(), 1);
                        mapPlausi.put(plausi.getCodePlausi(), plausi);
                    } else {
                        mapCount.put(plausi.getCodePlausi(), mapCount.get(plausi.getCodePlausi()) + 1);
                    }
                    if (!mapTypeViolation.containsKey(plausi.getTypeViolationPlausi())) {
                        mapTypeViolation.put(plausi.getTypeViolationPlausi(), 1);
                    } else {
                        mapTypeViolation.put(plausi.getTypeViolationPlausi(),
                                mapTypeViolation.get(plausi.getTypeViolationPlausi()) + 1);
                    }

                }
            }
        }

        System.out.println("Nb annonce avec une plausi en retour: " + messages.size());

        List<PlausiBeanSummary> beanSummaries = new ArrayList<PlausiBeanSummary>();
        for (Entry<String, Integer> entry : mapCount.entrySet()) {
            beanSummaries.add(new PlausiBeanSummary(mapPlausi.get(entry.getKey()), entry.getValue()));
        }

        File file = SimpleOutputListBuilderJade.newInstance().session(getSession()).addTranslater()
                .addList(beanSummaries).classElementList(PlausiBeanSummary.class).addSubTitle("Summary").jump()
                .addList(beans).classElementList(PlausiBeanRetour.class).addSubTitle("Plausis")
                .outputName("RPC_RETOUR_PLAUSI").asXls().build();

        protocol.setSession(getSession());
        protocol.setNombreRetour(messages.size());
        protocol.setMapTypeViolation(mapTypeViolation);
        protocol.sendMail(file.getAbsolutePath());

    }

    private InfoCaisse createInfosCaisse() {
        try {
            String numeroCaisseFormate = EPCProperties.RPC_ELOFFICE.getValue();
            String numeroAgenceFormate = CommonProperties.NUMERO_AGENCE.getValue();
            int numeroCaisse = Integer.parseInt(numeroCaisseFormate);
            int numeroAgence = Integer.parseInt(numeroAgenceFormate);
            return new InfoCaisse(numeroCaisse, numeroAgence, numeroCaisseFormate, numeroAgenceFormate);
        } catch (PropertiesException exception) {
            throw new IllegalArgumentException("Property Exception " + exception.toString(), exception);
        }
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
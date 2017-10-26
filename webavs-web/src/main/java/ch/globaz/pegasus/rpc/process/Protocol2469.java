package ch.globaz.pegasus.rpc.process;

import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BSession;
import globaz.jade.smtp.JadeSmtpClient;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.process.byitem.ProcessItemsJobInfos;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.ProtocolUtils;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.InfosRpcDataLoader;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.PcaSummer;
import ch.globaz.pegasus.rpc.businessImpl.sedex.ExecutionMode;
import ch.globaz.pegasus.rpc.plausi.core.PlausiResult;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiSummary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

class Protocol2469 {
    private static final Logger LOG = LoggerFactory.getLogger(Protocol2469.class);

    private final InfoCaisse infoCaisse;
    private int nombreAvecCalcul;
    private int nombreSansCalcul;
    private int nombreAnnulation;
    private ProcessItemsJobInfos processInfos;
    private InfosRpcDataLoader infosRpcDataLoader;
    private BSession session;
    private ExecutionMode executionMode;
    private PcaSummer summer = new PcaSummer();
    private RpcPlausiSummary plausiSummary;
    private Set<AnnonceItem> annonceItemsPlausiKo = new HashSet<AnnonceItem>();
    private ToleranceDifferenceAnnonce toleranceAnnonces = new ToleranceDifferenceAnnonce();

    public Protocol2469(InfoCaisse infoCaisse) {
        this.infoCaisse = infoCaisse;
    }

    public void setSummer(PcaSummer summer) {
        this.summer = summer;
    }

    public PcaSummer getPcaSummer() {
        return summer;
    }

    public void setPlausiSummary(RpcPlausiSummary plausiSummary) {
        this.plausiSummary = plausiSummary;
    }

    public InfosRpcDataLoader getInfosRpcDataLoader() {
        return infosRpcDataLoader;
    }

    public void setInfosRpcDataLoader(InfosRpcDataLoader infosRpcDataLoader) {
        this.infosRpcDataLoader = infosRpcDataLoader;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setProcessInfos(ProcessItemsJobInfos processInfos) {
        this.processInfos = processInfos;
    }

    public void setNombreAvecCalcul(int nombreAvecCalcul) {
        this.nombreAvecCalcul = nombreAvecCalcul;
    }

    public void setNombreSansCalcul(int nombreSansCalcul) {
        this.nombreSansCalcul = nombreSansCalcul;
    }

    public void setNombreAnnulation(int nombre) {
        nombreAnnulation = nombre;
    }

    public void setExecutionMode(ExecutionMode executionMode) {
        this.executionMode = executionMode;
    }

    public int getNombreAvecCalcul() {
        return nombreAvecCalcul;
    }

    public int getNombreSansCalcul() {
        return nombreSansCalcul;
    }

    public int getNombreAnnulation() {
        return nombreAnnulation;
    }

    public InfoCaisse getInfoCaisse() {
        return infoCaisse;
    }

    public ToleranceDifferenceAnnonce getToleranceAnnonces() {
        return toleranceAnnonces;
    }

    public void setToleranceAnnonces(ToleranceDifferenceAnnonce toleranceAnnonces) {
        this.toleranceAnnonces = toleranceAnnonces;
    }

    public void sendMail(String pahtFilePlausiKo) {
        String body = buildMailBody(false);

        try {
            String[] files = null;
            if (pahtFilePlausiKo != null) {
                files = new String[] { pahtFilePlausiKo };
            }
            JadeSmtpClient.getInstance().sendMail(session.getUserEMail(), session.getLabel(resolveSubject(false)),
                    body, files);
        } catch (Exception e) {
            LOG.error("impossible d'envoyer le protocole d'execution", e);
        }
    }

    public void setAnnonceItemsPlausiKo(Set<AnnonceItem> annonceItemsPlausiKo) {
        this.annonceItemsPlausiKo = annonceItemsPlausiKo;
    }

    public void sendMail() {
        this.sendMail(null);
    }

    /**
     * 
     * @param sujet
     * @param processStartDate
     * @param sendingStartDate
     * @param session
     * @return une liste de string car peut être facilement transformable en file ou dans l'html
     */
    private List<String> generateProtocolContent(String sujet, Date processStartDate, Date sendingStartDate,
            BSession session) {

        List<String> data = new LinkedList<String>();

        warningIfToleranceExceeds(session, data);

        String ligne = "---------------------------------------------------------------------------------------------";
        data.add(ligne);
        data.add(session.getLabel(sujet));
        data.add(ligne);
        data.add("");

        String informationCaisse = getInfoCaisse().getNumeroCaisse() + "." + getInfoCaisse().getNumeroAgence();
        data.add(session.getLabel("RPC_PROTOCOL_ORGANE_PC") + " : " + informationCaisse);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_DEBUT_TRAITEMENT") + " : "
                + formatter.format(processStartDate));
        if (infosRpcDataLoader != null) {
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_DATE_DERNIER_PAIEMENT") + " : "
                    + infosRpcDataLoader.getDateDernierPaiement());
        }
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_ENVOI_SM") + " : " + formatter.format(sendingStartDate));
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NOMBRE_ANNONCE_101") + " : " + getNombreAvecCalcul());
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NOMBRE_ANNONCE_201") + " : " + getNombreSansCalcul());
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NOMBRE_ANNONCE_301") + " : " + getNombreAnnulation());
        data.add("");
        if (summer != null) {
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NOMBRE_PCA_COURANT") + " : " + summer.getNbPca());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_TOTAL_PCA_AI") + " : "
                    + summer.getTotalPcaAi().toStringFormat());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_TOTAL_PCA_AVS") + " : "
                    + summer.getTotalPcaAvs().toStringFormat());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_CAS") + " : " + summer.getNbRpcData());
        }

        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_ERROR") + " : " + processInfos.getNbEntityInError());
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_KO") + " : " + annonceItemsPlausiKo.size());
        if (plausiSummary != null) {
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_BLOCKING") + " : "
                    + plausiSummary.getNbPlausiBlocking());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_ERROR") + " : "
                    + plausiSummary.getNbPlausiError());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_WARN") + " : "
                    + plausiSummary.getNbPlausiWarning());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_INFOS") + " : "
                    + plausiSummary.getNbPlausiInfos());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_AUTO") + " : "
                    + plausiSummary.getNbPlausiAuto());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAUSIS_MANUEL") + " : "
                    + plausiSummary.getNbPlausiManuel());
            data.add("");
        }

        data.add(session.getLabel("RPC_PROTOCOL_STAT"));
        data.add(ligne);
        if (processInfos != null) {
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_STAT_GEN_ANNONCE_TOTAL_TIME") + " : "
                    + ProtocolUtils.formatDuration(processInfos.computeTotal()));
            if (processInfos.getTimeBefore() != null) {
                data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_STAT_GEN_ANNONCE_LOADER") + " : "
                        + ProtocolUtils.formatDuration(processInfos.getTimeBefore()));
            }
            data.add(ProtocolUtils.indent(session.getLabel("RPC_PROTOCOL_DETAIL_STAT_GEN_ANNONCE_AVG_LOAD_ITEM"))
                    + " : " + processInfos.computeItemAvrage() + "ms");
            data.add(ProtocolUtils.indent(session.getLabel("RPC_PROTOCOL_DETAIL_STAT_GEN_ANNONCE_AVG_ITEM")) + " : "
                    + processInfos.computeItemAvrageTreatment() + "ms");
            data.add("");
        }

        data.add(session.getLabel("RPC_PROTOCOL_DETAIL_INFOS_LOADER"));
        data.add(ligne);
        if (infosRpcDataLoader != null) {
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_DECISION_REFUS") + " : "
                    + infosRpcDataLoader.getNbDecisionsRefus());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_DECISION_AC") + " : "
                    + infosRpcDataLoader.getNbDecisionAc());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_MEMBREFAMILLE") + " : "
                    + infosRpcDataLoader.getNbMembreFamille());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_PLAN_CALCUL") + " : "
                    + infosRpcDataLoader.getNbPlanCalcul());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_VERSION_DROIT_RESTANT") + " : "
                    + infosRpcDataLoader.getNbVersionDroitRestant());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_ADRESSE_COURRIER") + " : "
                    + infosRpcDataLoader.getNbAdresseCourrier());
            data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_ADRESSE_DOMICILE") + " : "
                    + infosRpcDataLoader.getNbAdresseDomicile());
            data.add("AvailableProcessors: " + Runtime.getRuntime().availableProcessors());
        }

        if (!annonceItemsPlausiKo.isEmpty() && annonceItemsPlausiKo.size() < 5) {
            Gson gson = createGson();
            data.add("");
            data.add("");
            data.add(translateWithIndent("RPC_PROTOCOL_PLAUSIS_KO") + " " + annonceItemsPlausiKo.size());
            data.add("---------------------------------------------------------------------------------------------");
            data.add("");
            for (AnnonceItem annonce : annonceItemsPlausiKo) {
                data.add(annonce.getDescription());
                data.add("");
                for (Entry<PlausiResult, Boolean> entry : annonce.getPlausisResults().filtrePlausiKo().getPlausis()
                        .entrySet()) {
                    data.add(entry.getKey().getPlausi().getID() + " " + entry.getKey().getPlausi().getReferance() + " "
                            + entry.getKey().getPlausi().getCategory() + "<pre>" + gson.toJson(entry.getKey())
                            + "</pre>");

                    data.add("");
                }
            }
            data.add("");
        }

        // data.add(ProtocolUtils.indent(session.getLabel("RPC_PROTOCOL_DETAIL_STAT_GEN_ANNONCE_101")) + " : "
        // + ProtocolUtils.formatDuration(getProcessTime101()));
        // data.add(ProtocolUtils.indent(session.getLabel("RPC_PROTOCOL_DETAIL_STAT_GEN_ANNONCE_201")) + " : "
        // + ProtocolUtils.formatDuration(getProcessTime101()));
        //
        // data.add(ProtocolUtils.indent(session.getLabel("RPC_PROTOCOL_DETAIL_STAT_VALID_ANNONCE_101")) + " : "
        // + ProtocolUtils.formatDuration(getSendingTime101()));
        // data.add(ProtocolUtils.indent(session.getLabel("RPC_PROTOCOL_DETAIL_STAT_VALID_ANNONCE_201")) + " : "
        // + ProtocolUtils.formatDuration(getSendingTime101()));

        return data;

    }

    private void warningIfToleranceExceeds(BSession session, List<String> data) {
        if (toleranceAnnonces.isDepasserTolerance()) {

            final String message = session.getLabel("RPC_PROTOCOL_AVERTISSEMENT_DIFF_ANCIEN_LOT");
            final String messageFormated = FWMessageFormat.prepareQuotes(message, false);

            data.add("<b>"
                    + translateWithIndentWithoutSession(MessageFormat.format(messageFormated,
                            toleranceAnnonces.getToleranceAnnonce() * 100)) + "</b>");
            data.add("");
        }
    }

    private Gson createGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Montant.class, new com.google.gson.JsonSerializer<Montant>() {

                    @Override
                    public JsonElement serialize(Montant src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.getValueDouble());
                    }
                }).create();
    }

    private String translateWithIndent(String label) {
        return translateWithIndentWithoutSession(session.getLabel(label));
    }

    private String translateWithIndentWithoutSession(String message) {
        return indentLeft(message);
    }

    public static String indentLeft(String value) {
        return StringUtils.repeat(" ", 5) + value;
    }

    public String buildMailBody(boolean hasError) {
        try {
            return StringUtils.join(
                    generateProtocolContent(resolveSubject(hasError), new java.util.Date(), new java.util.Date(),
                            session).toArray(), "\n\r");
        } catch (Exception e) {
            LOG.error("impossible de construire le protocole d'execution", e);
        }
        return "body error";
    }

    private String resolveSubject(boolean hasError) {
        if (hasError || !annonceItemsPlausiKo.isEmpty()) {
            return "RPC_PROCESS_SUCCESS_MAIL_SUBJECT_KO_" + executionMode;
        } else {
            return "RPC_PROCESS_SUCCESS_MAIL_SUBJECT_" + executionMode;
        }
    }
}

package ch.globaz.naos.ree.protocol;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.ProtocolUtils;
import ch.globaz.naos.ree.tools.SedexInfo;

/**
 * Protocole de fin de process détaillants les opérations réalisées lors de l'exécution du process
 * 
 * @author lga
 * 
 */
public class Protocol5054 {

    private final InfoCaisse infoCaisse;
    private final SedexInfo sedexInfo;
    private final ProtocolAndMessages protocolAndMessages_101;
    private final SedexTechnicalProtocol5054 sendingTechnicalProtocol_101;
    private final ProtocolAndMessages protocolAndMessages_102;
    private final SedexTechnicalProtocol5054 sendingTechnicalProtocol_102;

    public Protocol5054(ProtocolAndMessages protocolAndMessages_101,
            SedexTechnicalProtocol5054 sendingTechnicalProtocol_101, ProtocolAndMessages protocolAndMessages_102,
            SedexTechnicalProtocol5054 sendingTechnicalProtocol_102, InfoCaisse infoCaisse, SedexInfo sedexInfo) {
        this.protocolAndMessages_101 = protocolAndMessages_101;
        this.sendingTechnicalProtocol_101 = sendingTechnicalProtocol_101;
        this.protocolAndMessages_102 = protocolAndMessages_102;
        this.sendingTechnicalProtocol_102 = sendingTechnicalProtocol_102;

        this.infoCaisse = infoCaisse;
        this.sedexInfo = sedexInfo;
    }

    public List<String> generateProtocol5054Content(String sujet) {
        List<String> data = new LinkedList<String>();

        data.add(sujet);
        data.add("--------------------------------------------------------------------------------");
        data.add("");

        String informationCaisse = getInfoCaisse().getNumeroCaisse() + "." + getInfoCaisse().getNumeroAgence();
        data.add(ProtocolUtils.indent("Caisse de compensation") + " : " + informationCaisse);

        // Traitement des messages 101
        Map<Integer, ProcessProtocolAndMessages> messages101 = ((ProcessProtocolAndMessages5054) protocolAndMessages_101
                .getProcessProtocolAndMessages()).getResultatParAnnee();
        Map<Integer, Integer> statistics101 = sendingTechnicalProtocol_101.getLotByYear();
        addProcessStatistics("Nombre de revenus salariés annoncés pour ", data, messages101, statistics101);

        // Traitement des messages 102
        Map<Integer, ProcessProtocolAndMessages> messages102 = ((ProcessProtocolAndMessages5054) protocolAndMessages_102
                .getProcessProtocolAndMessages()).getResultatParAnnee();
        Map<Integer, Integer> statistics102 = sendingTechnicalProtocol_102.getLotByYear();
        addProcessStatistics("Nombre de revenus indépendants annoncés pour ", data, messages102, statistics102);

        data.add("");
        data.add("Erreurs remontées");

        addWarningAndErrors("101", data, messages101);
        addWarningAndErrors("102", data, messages102);

        data.add("");
        data.add("Statistics");

        data.add(ProtocolUtils.indent("Génération des données 5054-101") + " : "
                + ProtocolUtils.formatDuration(getProcessTime101()));
        data.add(ProtocolUtils.indent("Génération des données 5054-102") + " : "
                + ProtocolUtils.formatDuration(getProcessTime102()));

        data.add(ProtocolUtils.indent("Envoi des données 5054-101") + " : "
                + ProtocolUtils.formatDuration(getSendingTime101()));
        data.add(ProtocolUtils.indent("Envoi des données 5054-102") + " : "
                + ProtocolUtils.formatDuration(getSendingTime102()));

        return data;

    }

    private List<String> addWarningAndErrors(String messageType, final List<String> data,
            Map<Integer, ProcessProtocolAndMessages> processProtocolAndMessagesParAnnee) {

        Set<Integer> annees = processProtocolAndMessagesParAnnee.keySet();

        for (Integer annee : annees) {
            ProcessProtocolAndMessages protocolAndMessages = processProtocolAndMessagesParAnnee.get(annee);
            // Error
            List<String> messages = protocolAndMessages.getProcessProtocol().getErrors();
            data.add("");
            data.add("Errors 5054-" + messageType + " (" + annee + ") : ");
            if (messages.size() == 0) {
                data.add("Aucune erreur");
            } else {
                data.addAll(messages);
            }
        }
        return data;
    }

    private void addProcessStatistics(String message, List<String> data,
            Map<Integer, ProcessProtocolAndMessages> messages101, Map<Integer, Integer> statistics101) {
        for (Integer annee : statistics101.keySet()) {
            data.add(ProtocolUtils.indent(message + annee) + " : "
                    + messages101.get(annee).getBusinessMessages().size());
            data.add(ProtocolUtils.indent(" dont nombre de paquets") + " : " + statistics101.get(annee));
        }
    }

    public InfoCaisse getInfoCaisse() {
        return infoCaisse;
    }

    public SedexInfo getSedexInfo() {
        return sedexInfo;
    }

    /**
     * Retourne le temps total du process des données 101 en ms
     * 
     * @return le temps total du process des données en ms
     */
    public long getProcessTime101() {
        return protocolAndMessages_101.getTechnicalProtocol().getTotalTime();
    }

    /**
     * Retourne le temps total de l'envoi des données 101 en ms
     * 
     * @return le temps total de l'envoi des données en ms
     */
    public long getSendingTime101() {
        return sendingTechnicalProtocol_101.getTotalTime();
    }

    /**
     * Retourne le temps total du process des données 102 en ms
     * 
     * @return le temps total du process des données en ms
     */
    public long getProcessTime102() {
        return protocolAndMessages_102.getTechnicalProtocol().getTotalTime();
    }

    /**
     * Retourne le temps total de l'envoi des données 102 en ms
     * 
     * @return le temps total de l'envoi des données en ms
     */
    public long getSendingTime102() {
        return sendingTechnicalProtocol_102.getTotalTime();
    }

}

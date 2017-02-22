package ch.globaz.naos.ree.protocol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.naos.ree.tools.ProtocolUtils;
import ch.globaz.naos.ree.tools.SedexInfo;

/**
 * Protocole de fin de process détaillants les opérations réalisées lors de l'exécution du process
 * 
 * @author lga
 * 
 */
public class Protocol5053 {

    private final ProtocolAndMessages protocolAndMessages_101;
    private final SedexTechnicalProtocol sendingTechnicalProtocol_101;
    private final ProtocolAndMessages protocolAndMessages_102;
    private final SedexTechnicalProtocol sendingTechnicalProtocol_102;

    private final InfoCaisse infoCaisse;
    private final SedexInfo sedexInfo;

    /**
     * Le nombre d'affilié annoncés
     */
    private int nombreAffilie;

    /**
     * Le nombre de paquets envoyés pour l'annonce des affiliés
     */
    private int nombreDePaquetAffilies;

    /**
     * Le nombre de liens annoncés
     */
    private int nombreLiensAffilies;

    /**
     * Le nombre de paquet pour les liens annoncés
     */
    private int nombreDePaquetsLiensAffilies;

    /**
     * @param nombreAffilie
     * @param nombreDePaquetAffilies
     * @param nombreLiensAffilies
     * @param nombreDePaquetsLiensAffilies
     */
    public Protocol5053(ProtocolAndMessages protocolAndMessages_101,
            SedexTechnicalProtocol sendingTechnicalProtocol_101, ProtocolAndMessages protocolAndMessages_102,

            SedexTechnicalProtocol sendingTechnicalProtocol_102, InfoCaisse infoCaisse, SedexInfo sedexInfo,
            int nombreAffilie, int nombreDePaquetAffilies, int nombreLiensAffilies, int nombreDePaquetsLiensAffilies) {
        this.protocolAndMessages_101 = protocolAndMessages_101;
        this.sendingTechnicalProtocol_101 = sendingTechnicalProtocol_101;
        this.protocolAndMessages_102 = protocolAndMessages_102;
        this.sendingTechnicalProtocol_102 = sendingTechnicalProtocol_102;
        this.infoCaisse = infoCaisse;
        this.sedexInfo = sedexInfo;
        this.nombreAffilie = nombreAffilie;
        this.nombreDePaquetAffilies = nombreDePaquetAffilies;
        this.nombreLiensAffilies = nombreLiensAffilies;
        this.nombreDePaquetsLiensAffilies = nombreDePaquetsLiensAffilies;

    }

    public InfoCaisse getInfoCaisse() {
        return infoCaisse;
    }

    public SedexInfo getSedexInfo() {
        return sedexInfo;
    }

    /**
     * Le nombre d'affilié annoncés
     */
    public final int getNombreAffilie() {
        return nombreAffilie;
    }

    /**
     * Le nombre de paquets envoyés pour l'annonce des affiliés
     */
    public final int getNombreDePaquetAffilies() {
        return nombreDePaquetAffilies;
    }

    /**
     * Le nombre de liens d'affiliation annoncés
     */
    public final int getNombreLiensAffilies() {
        return nombreLiensAffilies;
    }

    /**
     * Le nombre de paquet pour les liens annoncés
     */
    public final int getNombreDePaquetsLiensAffilies() {
        return nombreDePaquetsLiensAffilies;
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

    public ProtocolAndMessages getProtocolAndMessages_101() {
        return protocolAndMessages_101;
    }

    public ProtocolAndMessages getProtocolAndMessages_102() {
        return protocolAndMessages_102;
    }

    public TechnicalProtocol getProcessTechnicalProtocol_101() {
        return protocolAndMessages_101.getTechnicalProtocol();
    }

    public TechnicalProtocol getProcessTechnicalProtocol_102() {
        return protocolAndMessages_102.getTechnicalProtocol();
    }

    /**
     * Génère le fichier de protocole de fin de process et le sauve à la place qui va bien....
     * 
     * @throws Exception
     * 
     * @throws ParseException
     */
    public List<String> generateProtocolContent(String sujet, Date processStartDate, Date sendingStartDate)
            throws Exception {

        List<String> data = new LinkedList<String>();

        data.add(sujet);
        data.add("--------------------------------------------------------------------------------");
        data.add("");

        String informationCaisse = getInfoCaisse().getNumeroCaisse() + "." + getInfoCaisse().getNumeroAgence();
        data.add(ProtocolUtils.indent("Caisse de compensation") + " : " + informationCaisse);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        data.add(ProtocolUtils.indent("Date et heure du début du traitement") + " : "
                + formatter.format(processStartDate));
        data.add(ProtocolUtils.indent("Date et heure du dépôt sur SM-Client") + " : "
                + formatter.format(sendingStartDate));
        data.add(ProtocolUtils.indent("Nombre d'affiliés annoncés") + " : " + getNombreAffilie());
        data.add(ProtocolUtils.indent(" dont nombre de paquets") + " : " + getNombreDePaquetAffilies());
        data.add(ProtocolUtils.indent("Nombre de liens annoncés") + " : " + getNombreLiensAffilies());
        data.add(ProtocolUtils.indent(" dont nombre de paquets") + " : " + getNombreDePaquetsLiensAffilies());

        data.add("");
        data.add("Erreurs remontées");

        List<String> messages = null;

        // Error 53-101
        messages = getProtocolAndMessages_101().getProcessProtocol().getErrors();
        data.add("");
        data.add("Errors 5053-101 : ");
        if (messages.size() == 0) {
            data.add("Aucune erreur");
        } else {
            data.addAll(messages);
        }

        // Error 53-101
        messages = getProtocolAndMessages_102().getProcessProtocol().getErrors();
        data.add("");
        data.add("Errors 5053-102 : ");
        if (messages.size() == 0) {
            data.add("Aucune erreur");
        } else {
            data.addAll(messages);
        }

        data.add("");
        data.add("Statistics");

        data.add(ProtocolUtils.indent("Génération des données 5053-101") + " : "
                + ProtocolUtils.formatDuration(getProcessTime101()));
        data.add(ProtocolUtils.indent("Génération des données 5053-102") + " : "
                + ProtocolUtils.formatDuration(getProcessTime102()));

        data.add(ProtocolUtils.indent("Envoi des données 5053-102") + " : "
                + ProtocolUtils.formatDuration(getSendingTime101()));
        data.add(ProtocolUtils.indent("Envoi des données 5053-102") + " : "
                + ProtocolUtils.formatDuration(getSendingTime102()));

        return data;

    }

}

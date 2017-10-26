package ch.globaz.pegasus.rpc.process;

import globaz.globall.db.BSession;
import globaz.jade.smtp.JadeSmtpClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.rpc.domaine.TypeViolationPlausi;

public class ProtocolRetour2469 {
    private static final Logger LOG = LoggerFactory.getLogger(ProtocolRetour2469.class);

    private final InfoCaisse infoCaisse;
    private int nombreRetour;
    private Map<TypeViolationPlausi, Integer> mapTypeViolation;
    private BSession session;
    private Set<AnnonceItem> annonceItemsPlausiKo = new HashSet<AnnonceItem>();

    public ProtocolRetour2469(InfoCaisse infoCaisse) {
        this.infoCaisse = infoCaisse;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public InfoCaisse getInfoCaisse() {
        return infoCaisse;
    }

    public int getNombreRetour() {
        return nombreRetour;
    }

    public Map<TypeViolationPlausi, Integer> getMapTypeViolation() {
        return mapTypeViolation;
    }

    public void setNombreRetour(int nombreRetour) {
        this.nombreRetour = nombreRetour;
    }

    public void setMapTypeViolation(Map<TypeViolationPlausi, Integer> mapTypeViolation) {
        this.mapTypeViolation = mapTypeViolation;
    }

    public void sendMail(String pahtFile) {
        String body = buildMailBody(false);

        try {
            String[] files = null;
            if (pahtFile != null) {
                files = new String[] { pahtFile };
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
    private List<String> generateProtocolContent(String sujet, Date processStartDate, Date returnDate, BSession session) {
        List<String> data = new LinkedList<String>();
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

        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_ENVOI_SM") + " : " + formatter.format(returnDate));
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NOMBRE_ANNONCE_501") + " : " + nombreRetour);
        data.add("");
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_RETOUR_GENERAL") + " : "
                + getNbTypeViolation(TypeViolationPlausi.GENERAL));
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_RETOUR_PERSONNE") + " : "
                + getNbTypeViolation(TypeViolationPlausi.PERSON));
        data.add(translateWithIndent("RPC_PROTOCOL_DETAIL_NB_RETOUR_OVERLAP") + " : "
                + getNbTypeViolation(TypeViolationPlausi.OVERLAP));

        return data;
    }

    private Integer getNbTypeViolation(TypeViolationPlausi type) {
        return mapTypeViolation.get(type) != null ? mapTypeViolation.get(type) : 0;
    }

    private String translateWithIndent(String label) {
        return indentLeft(session.getLabel(label));
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
            return "RPC_PROCESS_SUCCESS_MAIL_SUBJECT_KO_RETURN";
        } else {
            return "RPC_PROCESS_SUCCESS_MAIL_SUBJECT_RETURN";
        }
    }
}

package globaz.phenix.listes.excel;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @revision SCO 15 déc. 2010
 */
public class CPXmlmlMappingCommunicationEnvoiProcess {

    public static final String CONCAT_ID_COMMUNICATION = "idcommunication:";

    private CPXmlmlMappingCommunicationEnvoiProcess() {
        // Nothing to do
    }

    private static void loadDetail(CommonExcelmlContainer container, CPCommunicationFiscaleAffichage entity,
            CPListeCommunicationEnvoiProcess process, String status, String commentaire) throws Exception {
        process.incProgressCounter();
        container.put(ICPListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(ICPListeColumns.NOM, entity.getNom());
        container.put(ICPListeColumns.PRENOM, entity.getPrenom());
        container.put(ICPListeColumns.GENRE, process.getSession().getCodeLibelle(entity.getGenreAffilie()));
        container.put(ICPListeColumns.CANTON, process.getSession().getCodeLibelle(entity.getCanton()));
        container.put(ICPListeColumns.ANNEE, entity.getAnneeDecision());
        container.put(ICPListeColumns.ETAT, status);
        container.put(ICPListeColumns.REMARK, commentaire);
    }

    private static void loadHeader(CommonExcelmlContainer container, CPListeCommunicationEnvoiProcess process,
            int nombreCommunications) throws JAException {
        container.put("headerBlank1", " ");
        container.put("headerBlank2", " ");
        container.put("headerBlank3", " ");

        container.put(ICPListeColumns.HEADER_ANNEE, process.getAnnee());
        if (!JadeStringUtil.isEmpty(process.getGenre())) {
            container.put(ICPListeColumns.HEADER_GENRE, process.getSession().getCodeLibelle(process.getGenre()));
        }

        if (!JadeStringUtil.isEmpty(process.getCanton())) {
            container.put(ICPListeColumns.HEADER_CANTON, process.getSession().getCodeLibelle(process.getCanton()));
        }

        container.put(ICPListeColumns.HEADER_TITRE, process.getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI"));
        container.put(ICPListeColumns.HEADER_NUM_INFOROM, " ");

        container.put("dateEnvoi", JACalendar.todayJJsMMsAAAA());
        container.put("nombreTotal", String.valueOf(nombreCommunications));
        container.put("nombreEnvoye", String.valueOf(nombreCommunications - process.getCommunicationEnErreur().size()));
        container.put("nombreErreur", String.valueOf(process.getCommunicationEnErreur().size()));
    }

    /**
     * Permet de charger les résultats de la communication fiscale en envoi.
     * 
     * @param manager Le manager.
     * @param process Le process.
     * @return Le container Excel rempli.
     * @throws Exception Une exception.
     */
    public static CommonExcelmlContainer loadResults(CPCommunicationFiscaleAffichageManager manager,
            CPListeCommunicationEnvoiProcess process) throws Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        CPXmlmlMappingCommunicationEnvoiProcess.loadHeader(container, process, manager.size());

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CPCommunicationFiscaleAffichage entity = (CPCommunicationFiscaleAffichage) manager.getEntity(i);

            String status = process.getSession().getLabel("COMMUNICATION_FISCALE_ENVOYE");
            String commentaire = "";

            if (isEnErreur(process, entity)) {
                status = process.getSession().getLabel("COMMUNICATION_FISCALE_NON_ENVOYE");
                commentaire = getCommentaireFromErreur(entity.getIdCommunication(), process);
            }

            CPXmlmlMappingCommunicationEnvoiProcess.loadDetail(container, entity, process, status, commentaire);
        }

        return container;
    }

    private static boolean isEnErreur(CPListeCommunicationEnvoiProcess process, CPCommunicationFiscaleAffichage entity) {
        boolean isEnErreur = false;
        // Le process contient la communication dans la liste en erreur
        isEnErreur |= process.getCommunicationEnErreur().contains(entity.getIdCommunication());
        // OU Dans les commentaires des erreurs contient le numéro de la communication
        isEnErreur |= !getCommentaireFromErreur(entity.getIdCommunication(), process).isEmpty();
        return isEnErreur;
    }

    private static String getCommentaireFromErreur(String idCommunication, CPListeCommunicationEnvoiProcess process) {
        String commentaire = extractMemoryLog(idCommunication, process);

        if (commentaire == null || commentaire.isEmpty()) {
            commentaire = extractError(idCommunication, process);
        }

        if (commentaire == null || commentaire.isEmpty()) {
            commentaire = extractWarning(idCommunication, process);
        }

        return commentaire;
    }

    private static String extractMemoryLog(String idCommunication, CPListeCommunicationEnvoiProcess process) {
        for (int i = 0; i < process.getMemoryLog().size(); i++) {
            // Attention au factoring de l'append de l id communication, sans vérifié ou est utiliser la
            // constante car on l'utilise avec une recherche contains.
            if (process.getMemoryLog().getMessage(i).getComplement()
                    .contains(CONCAT_ID_COMMUNICATION + idCommunication)) {
                return process.getMemoryLog().getMessage(i).getComplement();
            }
        }

        return "";
    }

    private static String extractError(String idCommunication, CPListeCommunicationEnvoiProcess process) {
        for (int i = 0; i < process.getResults().getErrors().size(); i++) {
            // Attention au factoring de l'append de l id communication, sans vérifié ou est utiliser la
            // constante car on l'utilise avec une recherche contains.
            if (process.getResults().getErrors().get(i).contains(CONCAT_ID_COMMUNICATION + idCommunication)) {
                return process.getResults().getErrors().get(i);
            }
        }

        return "";
    }

    private static String extractWarning(String idCommunication, CPListeCommunicationEnvoiProcess process) {
        for (int i = 0; i < process.getResults().getWarnings().size(); i++) {
            // Attention au factoring de l'append de l id communication, sans vérifié ou est utiliser la
            // constante car on l'utilise avec une recherche contains.
            if (process.getResults().getWarnings().get(i).contains(CONCAT_ID_COMMUNICATION + idCommunication)) {
                return process.getResults().getWarnings().get(i);
            }
        }
        return "";
    }
}

package globaz.phenix.listes.excel;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @revision SCO 15 d�c. 2010
 */
public class CPXmlmlMappingCommunicationEnvoiProcess {

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
     * Permet de charger les r�sultats de la communication fiscale en envoi.
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

            String status = process.getSession().getLabel("COMMUNICATION_FISCALE_OK");
            String commentaire = "";

            if (process.getCommunicationEnErreur().contains(entity.getIdCommunication())) {
                status = process.getSession().getLabel("COMMUNICATION_FISCALE_ERREUR");
                commentaire = getCommentaireFromErreur(entity.getIdCommunication(), process);
            }

            CPXmlmlMappingCommunicationEnvoiProcess.loadDetail(container, entity, process, status, commentaire);
        }

        return container;
    }

    private static String getCommentaireFromErreur(String idCommunication, CPListeCommunicationEnvoiProcess process) {
        String commentaire = "";

        for (int i = 0; i < process.getMemoryLog().size(); i++) {
            if (process.getMemoryLog().getMessage(i).getComplement().contains(idCommunication)) {
                commentaire = process.getMemoryLog().getMessage(i).getComplement();
                break;
            }
        }

        return commentaire;
    }
}

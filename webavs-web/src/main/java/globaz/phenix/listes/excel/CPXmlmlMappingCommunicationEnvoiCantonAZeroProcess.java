package globaz.phenix.listes.excel;

import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @revision SCO 15 d?c. 2010
 */
public class CPXmlmlMappingCommunicationEnvoiCantonAZeroProcess {

    private static void loadDetail(int i, CommonExcelmlContainer container, CPCommunicationFiscaleAffichage entity,
            CPListeCommunicationEnvoiCantonAZeroProcess process) throws Exception {
        process.incProgressCounter();
        container.put(ICPListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(ICPListeColumns.NOM, entity.getNom());
        container.put(ICPListeColumns.PRENOM, entity.getPrenom());
        container.put(ICPListeColumns.GENRE, process.getSession().getCodeLibelle(entity.getGenreAffilie()));
        container.put(ICPListeColumns.CANTON, process.getSession().getCodeLibelle(entity.getCanton()));
        container.put(ICPListeColumns.ANNEE, entity.getAnneeDecision());

    }

    private static void loadHeader(CommonExcelmlContainer container, CPListeCommunicationEnvoiCantonAZeroProcess process)
            throws JAException {
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
        container.put(ICPListeColumns.HEADER_TITRE,
                process.getSession().getLabel("TITRE_LISTE_COMMUNICATION_ENVOI_CANTON_ZERO"));
        container.put(ICPListeColumns.HEADER_NUM_INFOROM, " ");
    }

    public static CommonExcelmlContainer loadResults(CPCommunicationFiscaleAffichageManager manager,
            CPListeCommunicationEnvoiCantonAZeroProcess process) throws Exception, Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        CPXmlmlMappingCommunicationEnvoiCantonAZeroProcess.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            CPCommunicationFiscaleAffichage entity = (CPCommunicationFiscaleAffichage) manager.getEntity(i);
            CPXmlmlMappingCommunicationEnvoiCantonAZeroProcess.loadDetail(i, container, entity, process);
        }

        return container;
    }
}

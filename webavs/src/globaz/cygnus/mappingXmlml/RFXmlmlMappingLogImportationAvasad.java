package globaz.cygnus.mappingXmlml;

import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.util.List;

/**
 * 
 * @author JJE
 * 
 */
public class RFXmlmlMappingLogImportationAvasad {

    private static String getTypeMessage(String typeMessage) {

        int typeMessageint = new Integer(typeMessage).intValue();

        if (JadeBusinessMessageLevels.ERROR == (typeMessageint)) {
            return BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_PROCESS_MESSAGE_ERROR");
        } else if (JadeBusinessMessageLevels.WARN == (typeMessageint)) {
            return BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_AVASAD_PROCESS_MESSAGE_WARNING");
        } else {
            return "";
        }

    }

    private static void initDetail(RFXmlmlContainer container) throws RFXmlmlException, Exception {

        container.put(IRFImportationAvasadListeColumns.DETAIL_TYPE_DE_MESSAGE, "");
        container.put(IRFImportationAvasadListeColumns.DETAIL_NUMERO_LIGNE, "");
        container.put(IRFImportationAvasadListeColumns.DETAIL_NSS, "");
        container.put(IRFImportationAvasadListeColumns.DETAIL_DESCRIPTION, "");
    }

    /**
     * Methode permettant de creer le corps du document (log ->new String[] {typeDeMessage, numeroLigne, nss,
     * msgErreur})
     * 
     * @param container
     * @param entity
     * @param process
     * @throws RFXmlmlException
     * @throws Exception
     */
    private static void loadDetail(RFXmlmlContainer container, String[] logCourant) throws RFXmlmlException, Exception {

        container.put(IRFImportationAvasadListeColumns.DETAIL_TYPE_DE_MESSAGE,
                RFXmlmlMappingLogImportationAvasad.getTypeMessage(logCourant[0]));
        container.put(IRFImportationAvasadListeColumns.DETAIL_NUMERO_LIGNE, logCourant[1]);
        container.put(IRFImportationAvasadListeColumns.DETAIL_NSS, logCourant[2]);
        container.put(IRFImportationAvasadListeColumns.DETAIL_DESCRIPTION, logCourant[3]);
    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     */
    private static void loadHeader(RFXmlmlContainer container, String gestionnaire) {

        /**
         * astuce temporaire pour que le fichier xls généré contienne des lignes blanches d'entête du modèle xml
         */
        container.put(IRFImportationAvasadListeColumns.HEADER_BLANK_1, "");
        container.put(IRFImportationAvasadListeColumns.HEADER_BLANK_2, "");
        container.put(IRFImportationAvasadListeColumns.HEADER_BLANK_3, "");

        container.put(IRFImportationAvasadListeColumns.HEADER_DATE_EXECUTION, JACalendar.todayJJsMMsAAAA());
        container.put(IRFImportationAvasadListeColumns.HEADER_GESTIONNAIRE, gestionnaire);

    }

    /**
     * Chargement des résultats
     * 
     * @param manager
     * @param process
     * @return
     * @throws RFXmlmlException
     * @throws Exception
     */
    public static RFXmlmlContainer loadResults(List<String[]> logsList, String gestionnaire) throws RFXmlmlException,
            Exception {

        RFXmlmlContainer container = new RFXmlmlContainer();

        RFXmlmlMappingLogImportationAvasad.loadHeader(container, gestionnaire);

        if (logsList.size() == 0) {
            RFXmlmlMappingLogImportationAvasad.initDetail(container);
        }

        for (String[] logCourant : logsList) {
            /*
             * if (null != process) { process.incProgressCounter(); }
             */
            RFXmlmlMappingLogImportationAvasad.loadDetail(container, logCourant);
        }

        return container;
    }
}

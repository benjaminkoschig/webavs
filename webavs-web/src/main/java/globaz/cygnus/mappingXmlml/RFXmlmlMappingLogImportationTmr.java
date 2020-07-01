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
public class RFXmlmlMappingLogImportationTmr {

    private static String getTypeMessage(String typeMessage) {

        int typeMessageint = new Integer(typeMessage).intValue();

        if (JadeBusinessMessageLevels.ERROR == (typeMessageint)) {
            return BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_MESSAGE_ERROR");
        } else if (JadeBusinessMessageLevels.WARN == (typeMessageint)) {
            return BSessionUtil.getSessionFromThreadContext().getLabel("RF_IMPORT_TMR_PROCESS_MESSAGE_WARNING");
        } else {
            return "";
        }

    }

    private static void initDetail(RFXmlmlContainer container) throws RFXmlmlException, Exception {

        container.put(IRFImportationTmrListeColumns.DETAIL_TYPE_DE_MESSAGE, "");
        container.put(IRFImportationTmrListeColumns.DETAIL_NUMERO_LIGNE, "");
        container.put(IRFImportationTmrListeColumns.DETAIL_NSS, "");
        container.put(IRFImportationTmrListeColumns.DETAIL_DESCRIPTION, "");
    }

    /**
     * Methode permettant de creer le corps du document (log ->new String[] {typeDeMessage, numeroLigne, nss,
     * msgErreur})
     * 
     * @param container
     * @param logCourant
     * @throws RFXmlmlException
     * @throws Exception
     */
    private static void loadDetail(RFXmlmlContainer container, String[] logCourant) throws RFXmlmlException, Exception {

        container.put(IRFImportationTmrListeColumns.DETAIL_TYPE_DE_MESSAGE,
                RFXmlmlMappingLogImportationTmr.getTypeMessage(logCourant[0]));
        container.put(IRFImportationTmrListeColumns.DETAIL_NUMERO_LIGNE, logCourant[1]);
        container.put(IRFImportationTmrListeColumns.DETAIL_NSS, logCourant[2]);
        container.put(IRFImportationTmrListeColumns.DETAIL_DESCRIPTION, logCourant[3]);
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
        container.put(IRFImportationTmrListeColumns.HEADER_BLANK_1, "");
        container.put(IRFImportationTmrListeColumns.HEADER_BLANK_2, "");
        container.put(IRFImportationTmrListeColumns.HEADER_BLANK_3, "");

        container.put(IRFImportationTmrListeColumns.HEADER_DATE_EXECUTION, JACalendar.todayJJsMMsAAAA());
        container.put(IRFImportationTmrListeColumns.HEADER_GESTIONNAIRE, gestionnaire);

    }

    /**
     * Chargement des résultats
     * 
     * @param logsList
     * @param gestionnaire
     * @return
     * @throws RFXmlmlException
     * @throws Exception
     */
    public static RFXmlmlContainer loadResults(List<String[]> logsList, String gestionnaire) throws RFXmlmlException,
            Exception {

        RFXmlmlContainer container = new RFXmlmlContainer();

        RFXmlmlMappingLogImportationTmr.loadHeader(container, gestionnaire);

        if (logsList.size() == 0) {
            RFXmlmlMappingLogImportationTmr.initDetail(container);
        }

        for (String[] logCourant : logsList) {
            /*
             * if (null != process) { process.incProgressCounter(); }
             */
            RFXmlmlMappingLogImportationTmr.loadDetail(container, logCourant);
        }

        return container;
    }
}

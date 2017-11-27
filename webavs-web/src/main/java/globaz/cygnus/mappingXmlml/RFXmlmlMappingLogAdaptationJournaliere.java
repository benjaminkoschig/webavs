package globaz.cygnus.mappingXmlml;

import globaz.cygnus.exceptions.RFXmlmlException;
import globaz.cygnus.process.RFAdaptationsJournalieresProcess;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.util.JACalendar;
import java.util.List;

/**
 * 
 * @author JJE
 * 
 */
public class RFXmlmlMappingLogAdaptationJournaliere {

    /**
     * Methode permettant de creer le corps du document (log ->new String[] { typeDeMessage, idAdaptationJournaliere,
     * idTiersBeneficiaire, nss, msgErreur, idDecisionPc, numDecisionPc })
     * 
     * @param container
     * @param entity
     * @param process
     * @throws RFXmlmlException
     * @throws Exception
     */
    private static void loadDetail(RFXmlmlContainer container, String[] logCourant,
            RFAdaptationsJournalieresProcess process) throws RFXmlmlException, Exception {

        if (process != null && process.isAjouterCommunePolitique()) {
            container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_COMMUNE_POLITIQUE, logCourant[7]);
        }
        container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_DESCRIPTION, logCourant[4]);
        container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_ID_ADAPTATION, logCourant[1]);
        container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_ID_DECISION_PC, logCourant[5]);
        container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_NUMERO_DECISION_PC, logCourant[6]);
        container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_NSS, logCourant[3]);
        container.put(IRFAdaptationsJournalieresListeColumns.DETAIL_TYPE_DE_MESSAGE, logCourant[0]);
    }

    /**
     * Methode permettant de remplir le header du document
     * 
     * @param container
     */
    private static void loadHeader(RFXmlmlContainer container, List<String> dates,
            RFAdaptationsJournalieresProcess process) {

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IRFAdaptationsJournalieresListeColumns.HEADER_BLANK_1, "");
        container.put(IRFAdaptationsJournalieresListeColumns.HEADER_BLANK_2, "");
        container.put(IRFAdaptationsJournalieresListeColumns.HEADER_BLANK_3, "");

        if (null != dates) {
            StringBuffer datesDeTraitementStrBuf = new StringBuffer();
            int i = 0;
            for (String dateCourante : dates) {
                if (i != dates.size()) {
                    datesDeTraitementStrBuf.append(dateCourante + ",");
                } else {
                    datesDeTraitementStrBuf.append(dateCourante);
                }
                i++;
            }

            container.put(IRFAdaptationsJournalieresListeColumns.HEADER_DATE_DE_TRAITMENT,
                    datesDeTraitementStrBuf.toString());
        }

        container.put(IRFAdaptationsJournalieresListeColumns.HEADER_DATE_EXECUTION, JACalendar.todayJJsMMsAAAA());

        if (process != null && process.isAjouterCommunePolitique()) {
            container.put(IRFAdaptationsJournalieresListeColumns.HEADER_USER, process.getSession().getUserId());
        }
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
    public static RFXmlmlContainer loadResults(List<String[]> logsList, List<String> dates,
            RFAdaptationsJournalieresProcess process) throws RFXmlmlException, Exception {

        RFXmlmlContainer container = new RFXmlmlContainer();

        RFXmlmlMappingLogAdaptationJournaliere.loadHeader(container, dates, process);

        for (String[] logCourant : logsList) {
            if (null != process) {
                process.incProgressCounter();
            }
            RFXmlmlMappingLogAdaptationJournaliere.loadDetail(container, logCourant, process);
        }

        return container;
    }
}

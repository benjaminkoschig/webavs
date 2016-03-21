package globaz.naos.listes.excel;

import globaz.framework.util.FWMessage;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.process.AFListeExcelComparaisonAcompteMasseProcess;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import java.util.Map;

/**
 * @revision SCO 15 déc. 2010
 */
public class AFXmlmlComparaisonAcompteMasse {

    private static void loadDetail(CommonExcelmlContainer container, Map<String, String> m,
            AFListeExcelComparaisonAcompteMasseProcess process) throws Exception, Exception {
        process.incProgressCounter();
        container.put(IAFListeColumns.NUM_AFFILIE, m.get("NUMAFFILIE"));
        container.put(IAFListeColumns.NOM, m.get("NOM"));
        /**
         * recherche l'adresse dans le tiers put dans le container
         */
        AFExcelmlUtils.renseigneAdresse(process.getSession(), "519007", container, m.get("IDTIERS"));
        double dTaux = new Double("0");
        String sTaux = m.get("TAUX").replace(',', '.');
        if (!JadeStringUtil.isBlankOrZero(sTaux)) {
            dTaux = Double.parseDouble(sTaux) * 100;
        }
        container.put("rubrique", m.get("RUBRIQUE"));
        container.put("montantAcompte", m.get("MONTANTACOMPTE"));
        container.put("masseFacturee", m.get("MASSEFACTUREE"));
        container.put("difference", m.get("DIFFERENCE"));
        container.put("taux", sTaux);
        // container.addValue("taux", JANumberFormatter.format(dTaux, 0.01, 2, JANumberFormatter.NEAR));
    }

    private static void loadHeader(CommonExcelmlContainer container, AFListeExcelComparaisonAcompteMasseProcess process)
            throws Exception {

        container.put(IAFListeColumns.HEADER_NUM_INFOROM, AFListeExcelComparaisonAcompteMasseProcess.NUMERO_INFOROM);
        container.put("headerAnneeAcompte", process.getAnneeAcompte());
        container.put("headerAnneeMasse", process.getAnneeMasse());
        container.put("headerMontantToleree", process.getDifferenceTolereeFranc());
        container.put("headerPourcentageTolere", process.getDifferenceTolereeTaux());

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IAFListeColumns.HEADER_BLANK_1, "");
        container.put(IAFListeColumns.HEADER_BLANK_2, "");

    }

    public static CommonExcelmlContainer loadResults(List<Map<String, String>> manager,
            AFListeExcelComparaisonAcompteMasseProcess process) throws Exception, Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        AFXmlmlComparaisonAcompteMasse.loadHeader(container, process);

        if (manager.size() > 0) {
            for (Map<String, String> m : manager) {
                if (process.isAborted()) {
                    process.getMemoryLog()
                            .logMessage("Process aborded!", FWMessage.FATAL, process.getClass().getName());
                    return container;
                }
                AFXmlmlComparaisonAcompteMasse.loadDetail(container, m, process);
            }
        }
        return container;
    }
}

package globaz.naos.listes.excel.taxeCo2;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2Manager;
import globaz.naos.listes.excel.AFXmlmlMappingAgenceCommunale;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.process.taxeCo2.AFListeExcelTaxeCo2Process;
import globaz.webavs.common.CommonExcelmlContainer;

/**
 * @revision SCO 15 déc. 2010
 */
public class AFXmlmlMappingTaxeCo2 {

    public final static String CS_AFFILIATION = "519007";

    private static void loadDetail(int i, CommonExcelmlContainer container, AFListeExcelTaxeCo2 entity,
            AFListeExcelTaxeCo2Process process) throws Exception, Exception {

        process.incProgressCounter();
        AFExcelmlUtils.renseigneAdresse(process.getSession(), AFXmlmlMappingAgenceCommunale.CS_AFFILIATION, container,
                entity.getIdTiers());

        container.put(IAFListeColumns.ID_TAXE, entity.getIdTaxe());
        container.put(IAFListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        container.put(IAFListeColumns.DESCRIPTION, entity.getDescription());
        container.put(IAFListeColumns.MASSE, entity.getMasse());
        container.put(IAFListeColumns.TAUX, entity.getTaux());
        container.put(IAFListeColumns.DATE_AFFILIATION, entity.getDateFormate(entity.getDateDebutAffiliation()));
        container.put(IAFListeColumns.DATE_RADIATION, entity.getDateFormate(entity.getDateFinAffiliation()));
        container.put(IAFListeColumns.MOTIF_FIN, process.getSession().getCodeLibelle(entity.getMotifFin()));
        container.put(IAFListeColumns.DATE_DEBUT_CAISSE, entity.getDateFormate(entity.getDateDebutCaisse()));
        container.put(IAFListeColumns.DATE_FIN_CAISSE, entity.getDateFormate(entity.getDateFinCaisse()));
        container.put(IAFListeColumns.NUM_CAISSE_AVS, entity.getNumCaisseAvs());
        container.put(IAFListeColumns.ETAT, process.getSession().getCodeLibelle(entity.getEtat()));

    }

    private static void loadHeader(CommonExcelmlContainer container, AFListeExcelTaxeCo2Process process)
            throws Exception {

        container.put(IAFListeColumns.HEADER_NUM_INFOROM, AFListeExcelTaxeCo2Process.NUMERO_INFOROM);
        container.put(IAFListeColumns.HEADER_NOM_LISTE, process.getSession().getLabel("LISTE_TAXE_CO2"));
        container.put(IAFListeColumns.HEADER_ANNEE, process.getForAnnee());

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IAFListeColumns.HEADER_BLANK_1, "");
        container.put(IAFListeColumns.HEADER_BLANK_3, "");

    }

    public static CommonExcelmlContainer loadResults(AFListeExcelTaxeCo2Manager manager,
            AFListeExcelTaxeCo2Process process) throws Exception, Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();
        String tauxDefaut = manager.getTauxDefaut(process.getForAnnee());
        AFXmlmlMappingTaxeCo2.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            AFListeExcelTaxeCo2 entity = (AFListeExcelTaxeCo2) manager.getEntity(i);
            if (JadeStringUtil.isBlankOrZero(entity.getTaux())) {
                entity.setTaux(tauxDefaut);
            }
            AFXmlmlMappingTaxeCo2.loadDetail(i, container, entity, process);
        }

        return container;
    }
}

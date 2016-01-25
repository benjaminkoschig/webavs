package globaz.naos.listes.excel.taxeCo2;

import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2;
import globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2Manager;
import globaz.naos.listes.excel.AFXmlmlMappingAgenceCommunale;
import globaz.naos.listes.excel.util.AFExcelmlUtils;
import globaz.naos.listes.excel.util.IAFListeColumns;
import globaz.naos.listes.excel.util.NaosContainer;
import globaz.naos.process.taxeCo2.AFListeExcelRadieTaxeCo2Process;

/**
 * @revision SCO 15 déc. 2010
 */
public class AFXmlmlMappingRadieTaxeCo2 {

    public final static String CS_AFFILIATION = "519007";

    private static void loadDetail(int i, NaosContainer container, AFListeExcelTaxeCo2 entity,
            AFListeExcelRadieTaxeCo2Process process) throws Exception, Exception {

        AFExcelmlUtils.renseigneAdresse(process.getSession(), AFXmlmlMappingAgenceCommunale.CS_AFFILIATION, container,
                entity.getIdTiers());

        process.incProgressCounter();
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

    private static void loadHeader(NaosContainer container, AFListeExcelRadieTaxeCo2Process process) throws Exception {

        container.put(IAFListeColumns.HEADER_NUM_INFOROM, AFListeExcelRadieTaxeCo2Process.NUMERO_INFOROM);
        container.put(IAFListeColumns.HEADER_NOM_LISTE, process.getSession().getLabel("LISTE_RADIE_TAXE_CO2"));
        container.put(IAFListeColumns.HEADER_ANNEE, process.getForAnnee());

        /**
         * astuce temporaire pour que le fichier xls généré contienne les lignes blanches d'entête du modèle xml
         */
        container.put(IAFListeColumns.HEADER_BLANK_1, "");
        container.put(IAFListeColumns.HEADER_BLANK_3, "");

    }

    public static NaosContainer loadResults(AFListeExcelTaxeCo2Manager manager, AFListeExcelRadieTaxeCo2Process process)
            throws Exception, Exception {
        NaosContainer container = new NaosContainer();
        String tauxDefaut = manager.getTauxDefaut(process.getForAnnee());
        AFXmlmlMappingRadieTaxeCo2.loadHeader(container, process);

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {
            AFListeExcelTaxeCo2 entity = (AFListeExcelTaxeCo2) manager.getEntity(i);
            if (JadeStringUtil.isBlankOrZero(entity.getTaux())) {
                entity.setTaux(tauxDefaut);
            }
            AFXmlmlMappingRadieTaxeCo2.loadDetail(i, container, entity, process);
        }

        return container;
    }
}

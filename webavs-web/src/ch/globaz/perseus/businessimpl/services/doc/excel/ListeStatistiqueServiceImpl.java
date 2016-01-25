package ch.globaz.perseus.businessimpl.services.doc.excel;

import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.services.doc.excel.ListeStatistiqueService;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.StatsJournalieres;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.StatsMensuelles;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.StatsRentePont;

/**
 * 
 * @author MBO
 * 
 */

public class ListeStatistiqueServiceImpl implements ListeStatistiqueService {

    @Override
    public ExcelmlWorkbook createStatsDemandesJournalieres(String jourDebut, String jourFin) throws DocException {
        StatsJournalieres statsDemandesJournalieres = new StatsJournalieres();
        return statsDemandesJournalieres.createDoc(jourDebut, jourFin);
    }

    @Override
    public String createStatsDemandesJournalieresAndSave(String jourDebut, String jourFin) throws Exception {
        StatsJournalieres statsDemandesJournalieres = new StatsJournalieres();
        return statsDemandesJournalieres.createDocAndSave(jourDebut, jourFin);
    }

    @Override
    public ExcelmlWorkbook createStatsDemandesParRegion(String moisDebut, String moisFin) throws DocException {
        StatsMensuelles statsDemandesParRegion = new StatsMensuelles();
        return statsDemandesParRegion.createDoc(moisDebut, moisFin);
    }

    @Override
    public String createStatsDemandesParRegionAndSave(String moisDebut, String moisFin) throws Exception {
        StatsMensuelles statsDemandesParRegion = new StatsMensuelles();
        return statsDemandesParRegion.createDocAndSave(moisDebut, moisFin);
    }

    @Override
    public ExcelmlWorkbook createStatsRentePont(String moisDebut, String moisFin) throws DocException {
        StatsRentePont statsRentePont = new StatsRentePont();
        return statsRentePont.createDoc(moisDebut, moisFin);
    }

    @Override
    public String createStatsRentePontAndSave(String moisDebut, String moisFin) throws Exception {
        StatsRentePont statsRentePont = new StatsRentePont();
        return statsRentePont.createDocAndSave(moisDebut, moisFin);
    }
}

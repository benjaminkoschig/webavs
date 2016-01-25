package ch.globaz.perseus.business.services.doc.excel;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.perseus.business.exceptions.doc.DocException;

public interface ListeStatistiqueService extends JadeApplicationService {

    public ExcelmlWorkbook createStatsDemandesJournalieres(String jourDebut, String jourFin) throws DocException;

    public String createStatsDemandesJournalieresAndSave(String jourDebut, String jourFin) throws Exception;

    public ExcelmlWorkbook createStatsDemandesParRegion(String moisDebut, String moisFin) throws DocException;

    public String createStatsDemandesParRegionAndSave(String moisDebut, String moisFin) throws Exception;

    public ExcelmlWorkbook createStatsRentePont(String moisDebut, String moisFin) throws DocException;

    public String createStatsRentePontAndSave(String moisDebut, String moisFin) throws Exception;

}

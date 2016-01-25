package ch.globaz.perseus.business.services.doc.excel;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.perseus.business.exceptions.doc.DocException;

public interface ListeDeControleService extends JadeApplicationService {

    public ExcelmlWorkbook createListeOrdreDeVersement(String idLot) throws DocException;

    public String createListeOrdreDeVersementAndSave(String idLot) throws Exception;

    public ExcelmlWorkbook createListeVerification(String mois) throws DocException;

    public String createListeVerificationAndSave(String mois) throws Exception;

    public ExcelmlWorkbook createListeVerificationRentePont(String mois) throws DocException;

    public String createListeVerificationRentePontAndSave(String mois) throws Exception;

}

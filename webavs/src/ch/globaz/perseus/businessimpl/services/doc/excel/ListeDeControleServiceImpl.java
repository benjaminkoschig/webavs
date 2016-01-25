package ch.globaz.perseus.businessimpl.services.doc.excel;

import globaz.op.excelml.model.document.ExcelmlWorkbook;
import ch.globaz.perseus.business.exceptions.doc.DocException;
import ch.globaz.perseus.business.services.doc.excel.ListeDeControleService;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.ListeOrdresVersement;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.ListeVerification;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.ListeVerificationRentePont;

/**
 * 
 * @author MBO
 * 
 */

public class ListeDeControleServiceImpl implements ListeDeControleService {

    @Override
    public ExcelmlWorkbook createListeOrdreDeVersement(String idLot) throws DocException {
        ListeOrdresVersement listeOrdresVersement = new ListeOrdresVersement();
        return listeOrdresVersement.createDoc(idLot);
    }

    @Override
    public String createListeOrdreDeVersementAndSave(String idLot) throws Exception {
        ListeOrdresVersement listeOrdresVersement = new ListeOrdresVersement();
        return listeOrdresVersement.createDocAndSave(idLot);
    }

    @Override
    public ExcelmlWorkbook createListeVerification(String mois) throws DocException {
        ListeVerification listeVerification = new ListeVerification();
        return listeVerification.createDoc(mois);
    }

    @Override
    public String createListeVerificationAndSave(String mois) throws Exception {
        ListeVerification listeVerification = new ListeVerification();
        return listeVerification.createDocAndSave(mois);
    }

    @Override
    public ExcelmlWorkbook createListeVerificationRentePont(String mois) throws DocException {
        ListeVerificationRentePont listeVerificationRentePont = new ListeVerificationRentePont();
        return listeVerificationRentePont.createDoc(mois);
    }

    @Override
    public String createListeVerificationRentePontAndSave(String mois) throws Exception {
        ListeVerificationRentePont listeVerificationRentePont = new ListeVerificationRentePont();
        return listeVerificationRentePont.createDocAndSave(mois);
    }

}

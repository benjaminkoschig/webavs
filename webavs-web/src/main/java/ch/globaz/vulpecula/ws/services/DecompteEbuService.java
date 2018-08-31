package ch.globaz.vulpecula.ws.services;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.vulpecula.ws.bean.DecompteComplementaireEbu;
import ch.globaz.vulpecula.ws.bean.DecomptePeriodiqueEbu;
import ch.globaz.vulpecula.ws.bean.DecompteSpecialEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceEbu;

@WebService(serviceName = "DecompteEbuService", name = "DecompteService")
public interface DecompteEbuService {
    @WebMethod
    @WebResult(name = "status")
    StatusAnnonceEbu ackSyncDecomptes(@WebParam(name = "listSynchronize_id") List<String> synchronizeIdList);

    @WebMethod(operationName = "listDecomptes")
    @WebResult(name = "listDecomptes")
    List<DecomptePeriodiqueEbu> listDecomptes(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "yearsMonthFrom") String yearsMonthFrom,
            @WebParam(name = "yearsMonthTo") String yearsMonthTo, @WebParam(name = "status") String status,
            @WebParam(name = "synchronize") boolean synchronize);

    @WebMethod(operationName = "listDecomptesComplementaire")
    @WebResult(name = "listDecomptesComplementaire")
    List<DecompteComplementaireEbu> listDecomptesComnplementaire(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "year") String yearsMonthFrom, @WebParam(name = "status") String status,
            @WebParam(name = "synchronize") boolean synchronize);

    @WebMethod(operationName = "listDecomptesSpecial")
    @WebResult(name = "listDecomptesSpecial")
    List<DecompteSpecialEbu> listDecomptesSpecial(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "year") String yearsMonthFrom, @WebParam(name = "status") String status,
            @WebParam(name = "synchronize") boolean synchronize);

    @WebMethod(operationName = "updateDecompte")
    @WebResult(name = "decompte")
    DecomptePeriodiqueEbu updateDecompte(@WebParam(name = "decompte") DecomptePeriodiqueEbu decompte);

    @WebMethod(operationName = "updateDecompteComplementaire")
    @WebResult(name = "decompteComplementaire")
    DecompteComplementaireEbu updateDecompteComplementaire(
            @WebParam(name = "decompte") DecompteComplementaireEbu decompteEbu) throws JadePersistenceException;

    @WebMethod(operationName = "createDecompteComplementaire")
    @WebResult(name = "listDecomptes")
    List<DecompteComplementaireEbu> createDecompteComplementaire(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "year") String years);

    @WebMethod(operationName = "downloadDecomptePDF")
    @WebResult(name = "DecomptePDF")
    byte[] downloadDecomptePDF(@WebParam(name = "idDecompte") String idDecompte);

    @WebMethod(operationName = "downloadDecompteBvrPDF")
    @WebResult(name = "DecompteBvrPDF")
    byte[] downloadDecompteBvrPDF(@WebParam(name = "idDecompte") String idDecompte);

    @WebMethod(operationName = "getDecompte")
    @WebResult(name = "decompte")
    DecomptePeriodiqueEbu getDecompte(@WebParam(name = "idDecompte") String idDecompte);

    @WebMethod(operationName = "getDecompteComplementaire")
    @WebResult(name = "decompte")
    DecompteComplementaireEbu getDecompteComplementaire(@WebParam(name = "idDecompte") String idDecompte);

}

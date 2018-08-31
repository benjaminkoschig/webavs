package ch.globaz.vulpecula.ws.services;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.vulpecula.ws.bean.LocaliteEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceEbu;
import ch.globaz.vulpecula.ws.bean.TravailleurEbu;

/**
 * Services pour un travailleur
 * 
 * @since eBMS 1.0
 */
@WebService
public interface TravailleurEbuService {
    @WebMethod
    @WebResult(name = "status")
    StatusAnnonceEbu annonceTravailleur(@WebParam(name = "travailleur") TravailleurEbu nouveauTravailleur);

    @WebMethod(operationName = "listTravailleur")
    @WebResult(name = "listTravailleur")
    List<TravailleurEbu> listTravailleur(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "synchronize") boolean synchronize);

    @WebMethod
    @WebResult(name = "status")
    StatusAnnonceEbu modifierTravailleur(@WebParam(name = "travailleur") TravailleurEbu travailleur);

    @WebMethod
    @WebResult(name = "status")
    StatusAnnonceEbu ackSyncTravailleurs(@WebParam(name = "listSynchronize_id") List<String> idsTableSynchro);

    @WebMethod
    @WebResult(name = "listLocalite")
    List<LocaliteEbu> getListLocalite();
}
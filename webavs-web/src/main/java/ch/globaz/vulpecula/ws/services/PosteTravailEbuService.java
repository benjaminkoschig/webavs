package ch.globaz.vulpecula.ws.services;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.vulpecula.ws.bean.PosteTravailEbu;

@WebService
public interface PosteTravailEbuService {
    @WebMethod
    @WebResult(name = "listAllPosteTravail")
    List<PosteTravailEbu> listAllPosteTravail(@WebParam(name = "idEmployeur") String idEmployeur);

    @WebMethod
    @WebResult(name = "listPosteTravail")
    List<PosteTravailEbu> listPosteTravail(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "yearsMonth") String yearsMonth);

    @WebMethod
    @WebResult(name = "posteTravail")
    PosteTravailEbu readPosteTravail(@WebParam(name = "idPosteTravail") String idPosteTravail);

    @WebMethod
    @WebResult(name = "posteTravail")
    PosteTravailEbu readPosteTravailAnnonce(@WebParam(name = "correlationId") String correlationId);

}

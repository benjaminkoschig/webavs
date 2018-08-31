package ch.globaz.vulpecula.ws.services;

import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.vulpecula.ws.bean.EmployeurEbu;
import ch.globaz.vulpecula.ws.bean.PeriodeEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceEbu;

/**
 * Services pour un employeur
 * 
 * @since eBMS 1.0
 */
@WebService
public interface EmployeurEbuService {
    @WebMethod
    @WebResult(name = "periodeAmcab")
    List<PeriodeEbu> periodeAmcabEmployeur(@WebParam(name = "idEmployeur") String idEmployeur);

    @WebMethod
    @WebResult(name = "employeur")
    EmployeurEbu getEmployeur(@WebParam(name = "numeroAffilie") String numeroAffilie);

    @WebMethod
    @WebResult(name = "idEmployeur")
    String getIdEmployeur(@WebParam(name = "numeroAffilie") String numeroAffilie);

    @WebMethod
    @WebResult(name = "useEbusiness")
    Boolean useEbusiness(@WebParam(name = "numeroAffilie") String numeroAffilie);

    @WebMethod
    @WebResult(name = "status")
    StatusAnnonceEbu inscrireEntrepriseEbusiness(@WebParam(name = "numeroAffilie") String numeroAffilie);

    @WebMethod
    @WebResult(name = "status")
    String ajouterContactEbusinessUser(@WebParam(name = "idEmployeur") String idEmployeur,
            @WebParam(name = "nom") String nom, @WebParam(name = "prenom") String prenom,
            @WebParam(name = "numeroTel") String numeroTel, @WebParam(name = "email") String email)
            throws JadePersistenceException;
}

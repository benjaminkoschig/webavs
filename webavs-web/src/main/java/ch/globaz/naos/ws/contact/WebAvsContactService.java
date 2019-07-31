package ch.globaz.naos.ws.contact;

import ch.globaz.orion.ws.exceptions.WebAvsException;

import javax.jws.*;
import java.util.List;

@WebService
@HandlerChain(file = "handlers.xml")
public interface WebAvsContactService {

    /**
     * Retourne un contact
     *
     **/
    @WebMethod
    @WebResult(name = "contactFPV")
    public abstract Contact getContactFPV(@WebParam(name = "numeroAffilie") String numeroAffilie) throws WebAvsException;

    /**
     * Recherche la liste de tous les contacts
     **/
    @WebMethod
    @WebResult(name = "listContactFPV")
    public abstract List<Contact> getListContactFPV() throws WebAvsException ;

    /**
     * Mets à jour un contact
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "isSuccess")
    public abstract Boolean setContactFPV(
            @WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "sex") String sexe,
            @WebParam(name = "email") String email,
            @WebParam(name = "stopProspection") String stopProspection)
            throws WebAvsException;
}

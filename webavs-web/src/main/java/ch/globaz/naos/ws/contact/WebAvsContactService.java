package ch.globaz.naos.ws.contact;

import ch.globaz.orion.ws.exceptions.WebAvsException;

import javax.jws.*;
import javax.xml.bind.annotation.XmlElement;
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
    public abstract Contact getContactFPV(@WebParam(name = "numeroAffilie") @XmlElement(required=true) String numeroAffilie) throws WebAvsException;

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
            @WebParam(name = "numeroAffilie") @XmlElement(required=true) String numeroAffilie,
            @WebParam(name = "nom") @XmlElement(required=true)  String nom,
            @WebParam(name = "prenom") @XmlElement(required=true)  String prenom,
            @WebParam(name = "sex") @XmlElement(required=true) EnumSexe sexe,
            @WebParam(name = "email") @XmlElement(required=true)  String email,
            @WebParam(name = "stopProspection") @XmlElement(required=true)  boolean stopProspection)
            throws WebAvsException;

    /**
     * Supprime un contact
     *
     **/
    @WebMethod
    @WebResult(name = "isSuccess")
    public abstract Boolean deleteContactFPV(@WebParam(name = "numeroAffilie") @XmlElement(required=true) List<String> listNumeroAffilie) throws WebAvsException;

}

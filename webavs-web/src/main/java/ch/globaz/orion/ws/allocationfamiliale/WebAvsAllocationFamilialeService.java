package ch.globaz.orion.ws.allocationfamiliale;

import java.util.List;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;
import ch.globaz.orion.ws.exceptions.WebAvsException;

@WebService
@HandlerChain(file = "handlers.xml")
@XmlSeeAlso({ ALDossier.class, ALDossierAndAdresses.class })
public interface WebAvsAllocationFamilialeService {

    /**
     * Retourne les dossiers AF d'un affilié
     * 
     * @param forNumeroAffilie
     * @param likeNssAllocataire
     * @param likeNomAllocataire
     * @param likePrenomAllocataire
     * @param inEtatDossier
     * @param from
     * @param nb
     * @param orderBy
     * @param orderByDir
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "alDossierSearchResult")
    public abstract ALDossierResultSearch searchDossiersAf(
            @WebParam(name = "forNumeroAffilie") String forNumeroAffilie,
            @WebParam(name = "likeNssAllocataire") String likeNssAllocataire,
            @WebParam(name = "likeNomAllocataire") String likeNomAllocataire,
            @WebParam(name = "likePrenomAllocataire") String likePrenomAllocataire,
            @WebParam(name = "inEtatDossier") List<ALDossierEtat> inEtatDossier, @WebParam(name = "from") int from,
            @WebParam(name = "nb") int nb, @WebParam(name = "orderBy") ALDossierOrderBy orderBy,
            @WebParam(name = "orderByDir") OrderByDirWebAvs orderByDir) throws WebAvsException;

    /**
     * Retourne les dossier AF d'un affilié ainsi que l'adresse de courrier et de domicile des allocataires
     * 
     * @param forNumeroAffilie
     * @param likeNssAllocataire
     * @param likeNomAllocataire
     * @param likePrenomAllocataire
     * @param inEtatDossier
     * @param from
     * @param nb
     * @param orderBy
     * @param orderByDir
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "alDossierSearchResult")
    public abstract ALDossierAndAdressesResultSearch searchDossiersAfAndAdresses(
            @WebParam(name = "forNumeroAffilie") String forNumeroAffilie,
            @WebParam(name = "likeNssAllocataire") String likeNssAllocataire,
            @WebParam(name = "likeNomAllocataire") String likeNomAllocataire,
            @WebParam(name = "likePrenomAllocataire") String likePrenomAllocataire,
            @WebParam(name = "inEtatDossier") List<ALDossierEtat> inEtatDossier, @WebParam(name = "from") int from,
            @WebParam(name = "nb") int nb, @WebParam(name = "orderBy") ALDossierOrderBy orderBy,
            @WebParam(name = "orderByDir") OrderByDirWebAvs orderByDir) throws WebAvsException;

    /**
     * Retourne le dossier AF et les adresses domicile et courrier de l'allocataire en fonction du numéro de dossier
     * 
     * @param numeroDossier
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "aLDossierAndAdresses")
    public abstract ALDossierAndAdresses readDossiersAfAndAdresses(
            @WebParam(name = "numeroDossier") String numeroDossier) throws WebAvsException;

    /**
     * Met à jour l'adresse de l'allocataire relatif au dossier
     * 
     * @param numeroDossier
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "isSuccess")
    public abstract Boolean updateOrCreateAdressesAllocataire(@WebParam(name = "numeroDossier") String numeroDossier,
            @WebParam(name = "adresseDomicile") ALAdresse adresseDomicile,
            @WebParam(name = "adresseCourrier") ALAdresse adresseCourrier,
            @WebParam(name = "remarqueDomicile") String remarqueDomicile,
            @WebParam(name = "remarqueCourrier") String remarqueCourrier,
            @WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "email") String email) throws WebAvsException;
}

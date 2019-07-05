package ch.globaz.orion.ws.affiliation;

import java.util.List;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.orion.ws.enums.ModeDeclarationSalaireWebAvs;
import ch.globaz.orion.ws.enums.Role;
import ch.globaz.orion.ws.exceptions.WebAvsException;

@WebService
@HandlerChain(file = "handlers.xml")
public interface WebAvsAffiliationService {

    /**
     * Trouve la cat�gorie d'affiliation d'un affili� dans une p�riode donn�e
     * La m�thode retourne :
     * - 0 si l'affiliation est ni AF, ni AVS
     * - 1 si l'affiliation est de cat�gorie AVS seul
     * - 2 si l'affiliation est de cat�gorie AF seul
     * - 3 si l'affiliation est de cat�gorie AVS / AF
     * - 4 Si le num�ro d'affili� correspond � plusieurs affiliations paritaires
     **/
    @WebMethod
    @WebResult(name = "categorieAffiliation")
    public abstract Integer findCategorieAffiliation(@WebParam(name = "NumeroAffilie") String numeroAffilie,
            @WebParam(name = "DateDebutPeriode") String dateDebutPeriode,
            @WebParam(name = "DateFinPeriode") String dateFinPeriode);

    /**
     * Recherche un suivi de caisse valide pour un moment donn�e
     **/
    @WebMethod
    @WebResult(name = "idsCaisseLaaLpp")
    public abstract List<Integer> findActiveSuiviCaisse(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "annee") String dateValidite);

    /**
     * Retourne l'adresse de courrier de l'affili� sp�cifi�
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "adresseCourrierAffilie")
    public abstract AffiliationAdresse findAdresseCourrierAffilie(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * Retourne l'adresse de domicile de l'affili� sp�cifi�
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "adresseDomicileAffilie")
    public abstract AffiliationAdresse findAdresseDomicileAffilie(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * Retourne l'adresse format�e de l'affili� (courrier ou domicile si pas d'adresse de courrier)
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "adresseFormateeAffilie")
    public abstract String findAdresseFormateeAffilie(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * V�rifie que l'affiliation existe et met � jour le mode de d�claration le cas �ch�ant. Retourne true si succ�s
     * 
     * @param numeroAffilie
     * @param modeDeclarationSalaireWebAvs
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "isSuccess")
    public abstract boolean checkAffiliationAndUpdateModeDeclaration(
            @WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "modeDeclarationSalaire") ModeDeclarationSalaireWebAvs modeDeclarationSalaireWebAvs)
            throws WebAvsException;

    /**
     * Retourne le mode de d�claration de salaire de l'affili�
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "modeDeclarationSalaireWebAvs")
    public abstract ModeDeclarationSalaireWebAvs findModeDeclarationSalairesAffilie(
            @WebParam(name = "numeroAffilie") String numeroAffilie) throws WebAvsException;

    /**
     * Retourne le role de l'affili�
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "roles")
    public abstract List<Role> findRoleAffilie(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * Retourne des informations sur l'affiliation
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "infosAffiliation")
    public abstract InfosAffiliation retrieveInfosAffiliation(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * Annoncer des changements d'adresses
     * 
     * @param numeroAffilie
     * @param adresseDomicile
     * @param adresseCourrier
     * @param remarqueDomicile
     * @param remarqueCourrier
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "isSuccess")
    public abstract Boolean announceMutationsAdressesAffiliation(
            @WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "adresseDomicile") AffiliationAdresse adresseDomicile,
            @WebParam(name = "adresseCourrier") AffiliationAdresse adresseCourrier,
            @WebParam(name = "remarqueDomicile") String remarqueDomicile,
            @WebParam(name = "remarqueCourrier") String remarqueCourrier,
            @WebParam(name = "email") String email) throws WebAvsException;
}

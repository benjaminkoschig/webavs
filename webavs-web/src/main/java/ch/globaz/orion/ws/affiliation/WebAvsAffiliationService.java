package ch.globaz.orion.ws.affiliation;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import ch.globaz.orion.ws.enums.ModeDeclarationSalaire;
import ch.globaz.orion.ws.exceptions.WebAvsException;

@WebService
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
    public abstract Integer findCategorieAffiliation(@WebParam(name = "NumeroAffilie") String numeroAffilie,
            @WebParam(name = "DateDebutPeriode") String dateDebutPeriode,
            @WebParam(name = "DateFinPeriode") String dateFinPeriode);

    /**
     * Recherche un suivi de caisse valide pour un moment donn�e
     **/
    @WebMethod
    public abstract List<Integer> findActiveSuiviCaisse(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "annee") String dateValidite);

    /**
     * Retourne l'adresse de courrier de l'affili� sp�cifi�
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    public abstract String findAdresseCourrierAffilie(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * V�rifie que l'affiliation existe et met � jour le mode de d�claration le cas �ch�ant. Retourne true si succ�s
     * 
     * @param numeroAffilie
     * @param modeDeclarationSalaire
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    public abstract boolean checkAffiliationAndUpdateModeDeclaration(
            @WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "modeDeclarationSalaire") ModeDeclarationSalaire modeDeclarationSalaire)
            throws WebAvsException;
}

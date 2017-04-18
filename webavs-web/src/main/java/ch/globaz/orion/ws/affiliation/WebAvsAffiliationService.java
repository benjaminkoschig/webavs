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
     * Trouve la catégorie d'affiliation d'un affilié dans une période donnée
     * La méthode retourne :
     * - 0 si l'affiliation est ni AF, ni AVS
     * - 1 si l'affiliation est de catégorie AVS seul
     * - 2 si l'affiliation est de catégorie AF seul
     * - 3 si l'affiliation est de catégorie AVS / AF
     * - 4 Si le numéro d'affilié correspond à plusieurs affiliations paritaires
     **/
    @WebMethod
    public abstract Integer findCategorieAffiliation(@WebParam(name = "NumeroAffilie") String numeroAffilie,
            @WebParam(name = "DateDebutPeriode") String dateDebutPeriode,
            @WebParam(name = "DateFinPeriode") String dateFinPeriode);

    /**
     * Recherche un suivi de caisse valide pour un moment donnée
     **/
    @WebMethod
    public abstract List<Integer> findActiveSuiviCaisse(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "annee") String dateValidite);

    /**
     * Retourne l'adresse de courrier de l'affilié spécifié
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    public abstract String findAdresseCourrierAffilie(@WebParam(name = "numeroAffilie") String numeroAffilie)
            throws WebAvsException;

    /**
     * Vérifie que l'affiliation existe et met à jour le mode de déclaration le cas échéant. Retourne true si succès
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

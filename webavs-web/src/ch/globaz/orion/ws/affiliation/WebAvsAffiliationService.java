package ch.globaz.orion.ws.affiliation;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface WebAvsAffiliationService {

    /**
     * M�thode permettant de trouver la cat�gorie d'affiliation d'un affili� dans une p�riode donn�e
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
}

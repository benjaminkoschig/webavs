package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import ch.globaz.orion.ws.exceptions.WebAvsException;

/**
 * WebService permettant d'accéder à des informations sur les cotisations de WebAvs
 * 
 * @author sco
 * 
 */
@WebService
public interface WebAvsCotisationsService {
    @WebMethod
    /**
     * Permet de récupérer la liste des cotisations pour un numéro d'affilié
     * @param noAffilie
     * @return
     */
    public abstract MassesForAffilie listerMassesActuelles(String noAffilie);

    /**
     * Execute le process de pré-remplissage d'une DAN pour l'affilié et l'année spécifiée. Retourne true si le
     * pré-remplissage a été effectué avec succès
     * 
     * @param noAffilie
     * @param annee
     * @return
     */
    @WebMethod
    public abstract boolean executerPreRemplissageDan(String noAffilie, Integer annee, String loginName,
            String userEmail);

    /**
     * Génère un fichier déclaration de salaire lisible sur la base de l'id du fichier PUCS
     * 
     * @param idPucsEntry
     * @param format
     * @return
     */
    @WebMethod
    public abstract String genererDocumentPucsLisible(String idPucsEntry, String format, String loginName,
            String userEmail, String langue);

    /**
     * Retourne le taux d'assurance pour la cotisation
     * 
     * @param idCotisation
     * @param montant
     * @param date
     * @return
     */
    @WebMethod
    public abstract Double findTauxAssuranceForCotisation(@WebParam(name = "idCotisation") Integer idCotisation,
            @WebParam(name = "montant") BigDecimal montant, @WebParam(name = "date") String date)
            throws WebAvsException;

    /**
     * Recherche des décomptes pour le mois passé en paramètre.
     **/
    @WebMethod
    public abstract DecompteMensuel findDecompteMois(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "mois") String mois, @WebParam(name = "annee") String annee);

    @WebMethod
    /**
     * Permet de récupérer la liste des cotisations paritaires et personnelles pour un numéro d'affilié
     * @param noAffilie
     * @return
     */
    public abstract MassesForAffilie listerMassesActuellesConfigurable(String noAffilie, boolean cotParitaire,
            boolean cotPers);
}
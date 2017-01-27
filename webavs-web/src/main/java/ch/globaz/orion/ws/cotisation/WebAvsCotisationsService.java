package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import ch.globaz.orion.ws.exceptions.WebAvsException;

/**
 * WebService permettant d'acc�der � des informations sur les cotisations de WebAvs
 * 
 * @author sco
 * 
 */
@WebService
public interface WebAvsCotisationsService {
    @WebMethod
    /**
     * Permet de r�cup�rer la liste des cotisations pour un num�ro d'affili�
     * @param noAffilie
     * @return
     */
    public abstract MassesForAffilie listerMassesActuelles(String noAffilie);

    /**
     * Execute le process de pr�-remplissage d'une DAN pour l'affili� et l'ann�e sp�cifi�e. Retourne true si le
     * pr�-remplissage a �t� effectu� avec succ�s
     * 
     * @param noAffilie
     * @param annee
     * @return
     */
    @WebMethod
    public abstract boolean executerPreRemplissageDan(String noAffilie, Integer annee, String loginName,
            String userEmail);

    /**
     * G�n�re un fichier d�claration de salaire lisible sur la base de l'id du fichier PUCS
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
     * Recherche des d�comptes pour le mois pass� en param�tre.
     **/
    @WebMethod
    public abstract DecompteMensuel findDecompteMois(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "mois") String mois, @WebParam(name = "annee") String annee);

    @WebMethod
    /**
     * Permet de r�cup�rer la liste des cotisations paritaires et personnelles pour un num�ro d'affili�
     * @param noAffilie
     * @return
     */
    public abstract MassesForAffilie listerMassesActuellesConfigurable(String noAffilie, boolean cotParitaire,
            boolean cotPers);
}
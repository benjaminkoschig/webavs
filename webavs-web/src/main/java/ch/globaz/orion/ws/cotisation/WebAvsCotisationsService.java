package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import java.util.List;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;
import ch.globaz.orion.ws.exceptions.WebAvsException;

/**
 * WebService permettant d'accéder à des informations sur les cotisations de WebAvs
 * 
 * @author sco
 * 
 */
@WebService
@HandlerChain(file = "handlers.xml")
@XmlSeeAlso({ InfosDerniereDecisionActive.class })
public interface WebAvsCotisationsService {
    /**
     * Permet de récupérer la liste des cotisations pour un numéro d'affilié
     * 
     * @param noAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "massesForAffilie")
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
    @WebResult(name = "isSuccess")
    public abstract boolean executerPreRemplissageDan(String noAffilie, Integer annee, String loginName,
            String userEmail);

    /**
     * Génère un fichier déclaration de salaire lisible sur la base de l'id (idFileName de la classe EBPucsFileEntity
     * qui correspond à l'id DAN ou à l'id PUCS)
     * 
     * @param id
     * @param provenance
     * @param format
     * @param loginName
     * @param userEmail
     * @param langue
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "filePath")
    public abstract String genererDocumentPucsLisible(@WebParam(name = "id") String id,
            @WebParam(name = "provenance") DeclarationSalaireProvenance provenance,
            @WebParam(name = "format") String format, @WebParam(name = "loginName") String loginName,
            @WebParam(name = "userEmail") String userEmail, @WebParam(name = "langue") String langue)
            throws WebAvsException;

    /**
     * Retourne le taux d'assurance pour la cotisation
     * 
     * @param idCotisation
     * @param montant
     * @param date
     * @return
     */
    @WebMethod
    @WebResult(name = "tauxAssurance")
    public abstract Double findTauxAssuranceForCotisation(@WebParam(name = "idCotisation") Integer idCotisation,
            @WebParam(name = "montant") BigDecimal montant, @WebParam(name = "date") String date)
            throws WebAvsException;

    /**
     * Recherche des décomptes pour le mois passé en paramètre.
     **/
    @WebMethod
    @WebResult(name = "decompteMensuel")
    public abstract DecompteMensuel findDecompteMois(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "mois") String mois, @WebParam(name = "annee") String annee);

    /**
     * Permet de récupérer la liste des cotisations paritaires et personnelles pour un numéro d'affilié
     * 
     * @param noAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "massesForAffilie")
    public abstract MassesForAffilie listerMassesActuellesConfigurable(String noAffilie, boolean cotParitaire,
            boolean cotPers);

    /**
     * Permet de générer et récupérer le path d'une décision d'acompte d'indépendant en fonction de l'id décision
     * 
     * @param idDecision
     * @return
     */
    @WebMethod
    @WebResult(name = "filePath")
    public abstract String getPathDucplicataDecisionAcompteInd(Integer idDecision) throws WebAvsException;

    /**
     * Permet de retourner une liste des décisions d'acomptes d'indépendant pour le numéro d'affilié donnée.
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "decisionsAcomptesInd")
    public abstract List<DecisionAcompteInd> listerDecisionsAcomptesInd(String numeroAffilie) throws WebAvsException;

    /**
     * Calcule les cotisations personnelles selon un revenu net et un capital saisi pour une année.
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "calculAcomptesInd")
    public abstract CalculAcomptesInd calculerAcompteIndForAnnee(String numeroAffilie, Integer annee,
            BigDecimal resultatNet, BigDecimal capitalInvesti, String language);

    /**
     * Retourne les informations de la dernière décision active pour une année donnée.
     * 
     * @param numeroAffilie
     * @param anneeDecision
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "infosDerniereDecisionActive")
    public abstract InfosDerniereDecisionActive findInfosDerniereDecisionActive(
            @WebParam(name = "numeroAffilie") String numeroAffilie, @WebParam(name = "annee") Integer anneeDecision)
            throws WebAvsException;

    /**
     * Permet de retourner une liste des décisions d'acomptes d'indépendant pour le numéro d'affilié donnée.
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "cotisationsPersonnellesWebAvs")
    public abstract List<CotisationPersonnelleWebAvs> listerCotisationsPersonnellesActives(
            @WebParam(name = "numeroAffilie") String numeroAffilie, @WebParam(name = "language") String language)
            throws WebAvsException;

    /**
     * Retourne les information sur la déclaration de salaire de l'année donnée
     * 
     * @param numeroAffilie
     * @param annee
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "infosDeclarationSalaire")
    public abstract InfosDeclarationSalaire findInfosDeclarationSalaire(
            @WebParam(name = "numeroAffilie") String numeroAffilie, @WebParam(name = "annee") Integer annee)
            throws WebAvsException;

    /**
     * @param pucsFile
     * @param provenance
     * @param format
     * @param langue
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "filePath")
    public abstract String genererDocumentPucsLisibleFromByteCode(@WebParam(name = "pucsFile") byte[] pucsFile,
            @WebParam(name = "provenance") DeclarationSalaireProvenance provenance,
            @WebParam(name = "format") String format, @WebParam(name = "langue") String langue) throws WebAvsException;

    /**
     * Retourne le dernier revenu déterminant ainsi que le dernier revenu de base
     * 
     * @param numeroAffilie
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "dernierRevenuDeterminantEtBase")
    public abstract DernierRevenuDeterminantEtBase retrieveDernierRevenuDeterminantEtBase(
            @WebParam(name = "numeroAffilie") String numeroAffilie) throws WebAvsException;

    /**
     * @param numeroAffilie
     * @param annee
     * @param likeNss
     * @param likeNom
     * @param likePrenom
     * @param from
     * @param nb
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "salaries")
    SalarieResultSearch searchSalaries(@WebParam(name = "numeroAffilie") String numeroAffilie,
            @WebParam(name = "annee") Integer annee, @WebParam(name = "likeNss") String likeNss,
            @WebParam(name = "likeNom") String likeNom, @WebParam(name = "likePrenom") String likePrenom,
            @WebParam(name = "from") Integer from, @WebParam(name = "nb") Integer nb,
            @WebParam(name = "orderBy") SalarieOrderBy orderBy,
            @WebParam(name = "orderByDir") OrderByDirWebAvs orderByDir) throws WebAvsException;
}
package ch.globaz.orion.ws.comptabilite;

import java.util.List;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;
import ch.globaz.orion.ws.enums.Role;
import ch.globaz.orion.ws.exceptions.WebAvsException;

@WebService
@HandlerChain(file = "handlers.xml")
public interface WebAvsComptabiliteService {
    // ******************************************************************************************************
    // EXTRAIT COMPTE
    // ******************************************************************************************************

    /**
     * retourne la liste des aperçus de compte annexes en fonction du numéro d'affilié. Retourne tous les comptes, il
     * est possible d'exclure les comptes indépendant avec la propriété ecl.exclure.ca.affilie.personnel = true
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "apercusComptesAnnexes")
    public abstract List<ApercuCompteAnnexe> listerApercuCompteAnnexe(
            @WebParam(name = "numeroAffilie") String numeroAffilie, @WebParam(name = "langue") String langue);

    /**
     * retourne la liste des aperçus de compte annexes indépendant uniquement en fonction du numéro d'affilié
     * 
     * @param numeroAffilie
     * @return
     */
    @WebMethod
    @WebResult(name = "apercusComptesAnnexesInd")
    public abstract List<ApercuCompteAnnexe> listerApercuCompteAnnexeInd(
            @WebParam(name = "numeroAffilie") String numeroAffilie, @WebParam(name = "langue") String langue);

    /**
     * Génère le document Extrait de compte et retourne le path du fichier
     * 
     * @param idCompteAnnexe
     * @param dateDebut
     * @param dateFin
     * @param langue
     * @return
     */
    @WebMethod
    @WebResult(name = "filePath")
    public abstract String genererExtraitCompteAnnexe(@WebParam(name = "idCompteAnnexe") String idCompteAnnexe,
            @WebParam(name = "dateDebut") String dateDebut, @WebParam(name = "dateFin") String dateFin,
            @WebParam(name = "langue") String langue);

    /**
     * Permet de télécharger (tableau de byte) un fichier en fonction du path
     * 
     * @param filepath
     * @return
     */
    @WebMethod
    @WebResult(name = "file")
    public abstract byte[] downloadFile(@WebParam(name = "filepath") String filepath);

    // ******************************************************************************************************
    // FACTURE
    // ******************************************************************************************************

    /**
     * Retourne les factures de l'affilié
     * 
     * @param forNumeroAffilie
     * @param statut
     * @param forDateDebut
     * @param forDateFin
     * @param forDateDebutEcheance
     * @param forDateFinEcheance
     * @param from
     * @param nb
     * @param orderBy
     * @param orderByDir
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "factureSearchResult")
    public abstract FactureResultSearch searchFactures(@WebParam(name = "forNumeroAffilie") String forNumeroAffilie,
            @WebParam(name = "forStatut") FactureStatut statut, @WebParam(name = "forDateDebut") String forDateDebut,
            @WebParam(name = "forDateFin") String forDateFin,
            @WebParam(name = "forDateDebutEcheance") String forDateDebutEcheance,
            @WebParam(name = "forDateFinEcheance") String forDateFinEcheance, @WebParam(name = "forRole") Role forRole,
            @WebParam(name = "from") int from, @WebParam(name = "nb") int nb,
            @WebParam(name = "orderBy") FactureOrderBy orderBy,
            @WebParam(name = "orderByDir") OrderByDirWebAvs orderByDir) throws WebAvsException;

    /**
     * Retourne la facture correspondante à l'id
     * 
     * @param idFacture
     * @return
     */
    @WebMethod
    @WebResult(name = "facture")
    public abstract Facture readFacture(@WebParam(name = "idFacture") Integer idFacture) throws WebAvsException;

    /**
     * @param numeroAffilie
     * @param role
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "infosGeneralesFacturation")
    public abstract InfosGeneralesFacturation retrieveInfosGeneralesFacturation(
            @WebParam(name = "forNumeroAffilie") String forNumeroAffilie, @WebParam(name = "forRole") Role forRole)
            throws WebAvsException;

    /**
     * Génère la facture et retourne le path du fichier
     * 
     * @param idFacture
     * @param langue
     * @return
     * @throws WebAvsException
     */
    @WebMethod
    @WebResult(name = "filePath")
    public abstract String genererFacture(@WebParam(name = "idFacture") String idFacture,
            @WebParam(name = "langue") String langue) throws WebAvsException;
}

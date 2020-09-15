package ch.globaz.naos.business.service;

import ch.globaz.naos.business.model.*;
import ch.globaz.naos.exception.MajorationFraisAdminException;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.naos.business.data.AssuranceInfo;
import globaz.naos.db.affiliation.AFAffiliation;

import java.util.HashSet;

public interface AffiliationService extends JadeApplicationService {

    public static final String CS_TYPE_COTI_AF = "812002";
    public static final String CS_TYPE_COTI_AUTRE = "812004";
    public static final String CS_TYPE_COTI_AVS_AI = "812001";
    public static final String CS_TYPE_LIEN_SUCCURSALE = "819006";
    public static final String CS_TYPE_PARITAIRE = "801001";
    public static final String CS_TYPE_PERSONNEL = "801002";
    public static final String TYPE_AFFILI_BENEF_AF = "19150036";

    public static final String TYPE_AFFILI_TSE = "804008";
    public static final String TYPE_AFFILI_TSE_VOLONTAIRE = "804011";

    /**
     * Recherche d'affiliation
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public AffiliationSearchSimpleModel find(AffiliationSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoie un tableau de numéro d'affilié commencant par le numéro partiel FORMATE passé en paramètre.
     * 
     * @param numeroAffilie
     *            le numéro d'affilié partiel, FORMATE (par exemple "000.1" -> ["000.010-00", "000.011-00"]
     * @return un tableau de numéro d'affilié commencant par le numéro partiel passé en paramètre.
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String[] findAllForNumeroAffilieLike(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoi l'identifiant d'un tiers pour un numéro d'affilié complet et formaté.
     * 
     * @param numeroAffilie
     *            le numéro d'affilié complet formaté pour lequel on désire connaitre l'identifiant du tiers
     * @return L'identifiant du tiers ou null si aucun tiers trouvé pour ce numéro
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     *             si plusieurs identifiant de tiers sont trouvés pour le numéro d'affilié
     */
    public String findIdTiersForNumeroAffilie(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoie la maison mère, ou null si il n'y a pas de maison mère
     * 
     * @param numeroAffilieFormatte le numéro d'affilié formatte
     * @return la maison mère, ou null si il n'y a pas de maison mère
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public AffiliationSimpleModel findMaisonMere(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoi le nombre de succursale de la maison mère.
     * 
     * @param numeroAffilieFormatte le numéro d'affilié formatte
     * @return le nombre de succzrsale de la maison mère.
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public int findNombreSuccursales(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet de savoir si une assurance est couverte ou non
     * 
     * @param numeroAffilie
     * @param date
     * @param genreAssurance
     * @param typeAssurance
     * @param categorieCotisant
     *            (non géré pour le moment)
     * @return un objet de type AssuranceInfo qui contient le résultat
     * @throws JadePersistenceException
     *             si une erreur technique lié à la persistence des données survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs enregistrements sont trouvés pour les paramètres donnés.
     * @deprecated
     */
    @Deprecated
    AssuranceInfo getAssuranceInfo(String numeroAffilie, String date, String genreAssurance, // paritaire ou indep.
            String typeAssurance, // AF
            String categorieCotisant) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de savoir si une assurance est couverte ou non
     * 
     * @param numeroAffilie
     * @param date
     * @param typeDossier
     * @return un objet de type AssuranceInfo qui contient le résultat
     * @throws JadePersistenceException
     *             si une erreur technique lié à la persistence des données survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs enregistrements sont trouvés pour les paramètres donnés.
     */
    AssuranceInfo getAssuranceInfoAF(String numeroAffilie, String date, String typeDossier)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de savoir si il existe une affiliation ou non dans le system
     * 
     * @param numeroAffilie
     * @return vrai si une affiliation existe (même si elle est radiée), faux sinon.
     * @throws JadePersistenceException
     *             si une erreur technique lié à la persistence des données survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs affiliations sont trouvées pour le numéro passé en argument.
     */
    public Boolean isAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de savoir si un numéro d'affilié a le bon format
     * 
     * @param numeroAffilie
     * @return vrai si le numéro à un format correct, faux sinon
     * @throws JadeApplicationException
     *             si une erreur technique survient (par exemple si aucun formater n'est trouvé).
     */
    public Boolean isNumeroAffilieValide(String numeroAffilie) throws JadeApplicationException;

    /**
     * Retourne le nombre d'affiliation correspondant au n° affilié
     * 
     * @param numeroAffilie
     * @return nombre d'affiliation
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public int nombreAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException;

    /**
     * lit un enregistrement DB sur la table affiliation en fonction de l'id passé en paramètre
     * 
     * @param idAffiliation l'id de l'enregistrement à lire
     * @return le modèle représentant l'enregistrement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public AffiliationSimpleModel read(String idAffiliation) throws JadePersistenceException, JadeApplicationException;

    /**
     * La méthode principale de recherche sur le AffiliationAssuranceSearchComplexModel
     *
     * @param searchModel le AffiliationAssuranceSearchComplexModel
     * @return le modèle représentant l'enregistrement
     * @throws JadePersistenceException
     */
    public AffiliationAssuranceSearchComplexModel searchAffiliationAssurance(
            AffiliationAssuranceSearchComplexModel searchModel) throws JadePersistenceException;

    /**
     * Permet de retourner les ids des assurances associées aux cotisations d'un affilié
     *
     * @param session Une session
     * @param forNumeroAffilie l'id de l'affiliation
     * @return Tous les ids des assurance pour toutes les cotisations d'un affilié
     */
    public HashSet<String> getIdsAssurancesAffiliation(BSession session, String forNumeroAffilie);

    /**
     * Recherche la cotisation à l'assurance de majoration l'active si elle existe ou la crée s'il elle n'existe pas
     *
     * @param transaction la transaction
     * @param afAffiliation l'affiliation
     */
    public void addOrActivateCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation, String anneeDeclSalaire) throws MajorationFraisAdminException, JAException;

    /**
     * Recherche la cotisation à l'assurance de majoration et la désactive si elle existe
     *
     * @param transaction la transaction
     * @param afAffiliation l'affiliation
     */
    public void deactivateCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation) throws MajorationFraisAdminException;

    /**
     * Recherche d'affiliation complexe
     * 
     * @param searchComplexModel
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public AffiliationTiersSearchComplexModel widgetFind(AffiliationTiersSearchComplexModel searchComplexModel)
            throws JadePersistenceException, JadeApplicationException;
}

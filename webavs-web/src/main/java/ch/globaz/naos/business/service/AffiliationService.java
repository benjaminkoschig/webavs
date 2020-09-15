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
     * Renvoie un tableau de num�ro d'affili� commencant par le num�ro partiel FORMATE pass� en param�tre.
     * 
     * @param numeroAffilie
     *            le num�ro d'affili� partiel, FORMATE (par exemple "000.1" -> ["000.010-00", "000.011-00"]
     * @return un tableau de num�ro d'affili� commencant par le num�ro partiel pass� en param�tre.
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String[] findAllForNumeroAffilieLike(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoi l'identifiant d'un tiers pour un num�ro d'affili� complet et format�.
     * 
     * @param numeroAffilie
     *            le num�ro d'affili� complet format� pour lequel on d�sire connaitre l'identifiant du tiers
     * @return L'identifiant du tiers ou null si aucun tiers trouv� pour ce num�ro
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     *             si plusieurs identifiant de tiers sont trouv�s pour le num�ro d'affili�
     */
    public String findIdTiersForNumeroAffilie(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoie la maison m�re, ou null si il n'y a pas de maison m�re
     * 
     * @param numeroAffilieFormatte le num�ro d'affili� formatte
     * @return la maison m�re, ou null si il n'y a pas de maison m�re
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public AffiliationSimpleModel findMaisonMere(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoi le nombre de succursale de la maison m�re.
     * 
     * @param numeroAffilieFormatte le num�ro d'affili� formatte
     * @return le nombre de succzrsale de la maison m�re.
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
     *            (non g�r� pour le moment)
     * @return un objet de type AssuranceInfo qui contient le r�sultat
     * @throws JadePersistenceException
     *             si une erreur technique li� � la persistence des donn�es survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs enregistrements sont trouv�s pour les param�tres donn�s.
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
     * @return un objet de type AssuranceInfo qui contient le r�sultat
     * @throws JadePersistenceException
     *             si une erreur technique li� � la persistence des donn�es survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs enregistrements sont trouv�s pour les param�tres donn�s.
     */
    AssuranceInfo getAssuranceInfoAF(String numeroAffilie, String date, String typeDossier)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de savoir si il existe une affiliation ou non dans le system
     * 
     * @param numeroAffilie
     * @return vrai si une affiliation existe (m�me si elle est radi�e), faux sinon.
     * @throws JadePersistenceException
     *             si une erreur technique li� � la persistence des donn�es survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs affiliations sont trouv�es pour le num�ro pass� en argument.
     */
    public Boolean isAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de savoir si un num�ro d'affili� a le bon format
     * 
     * @param numeroAffilie
     * @return vrai si le num�ro � un format correct, faux sinon
     * @throws JadeApplicationException
     *             si une erreur technique survient (par exemple si aucun formater n'est trouv�).
     */
    public Boolean isNumeroAffilieValide(String numeroAffilie) throws JadeApplicationException;

    /**
     * Retourne le nombre d'affiliation correspondant au n� affili�
     * 
     * @param numeroAffilie
     * @return nombre d'affiliation
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public int nombreAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException;

    /**
     * lit un enregistrement DB sur la table affiliation en fonction de l'id pass� en param�tre
     * 
     * @param idAffiliation l'id de l'enregistrement � lire
     * @return le mod�le repr�sentant l'enregistrement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public AffiliationSimpleModel read(String idAffiliation) throws JadePersistenceException, JadeApplicationException;

    /**
     * La m�thode principale de recherche sur le AffiliationAssuranceSearchComplexModel
     *
     * @param searchModel le AffiliationAssuranceSearchComplexModel
     * @return le mod�le repr�sentant l'enregistrement
     * @throws JadePersistenceException
     */
    public AffiliationAssuranceSearchComplexModel searchAffiliationAssurance(
            AffiliationAssuranceSearchComplexModel searchModel) throws JadePersistenceException;

    /**
     * Permet de retourner les ids des assurances associ�es aux cotisations d'un affili�
     *
     * @param session Une session
     * @param forNumeroAffilie l'id de l'affiliation
     * @return Tous les ids des assurance pour toutes les cotisations d'un affili�
     */
    public HashSet<String> getIdsAssurancesAffiliation(BSession session, String forNumeroAffilie);

    /**
     * Recherche la cotisation � l'assurance de majoration l'active si elle existe ou la cr�e s'il elle n'existe pas
     *
     * @param transaction la transaction
     * @param afAffiliation l'affiliation
     */
    public void addOrActivateCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation, String anneeDeclSalaire) throws MajorationFraisAdminException, JAException;

    /**
     * Recherche la cotisation � l'assurance de majoration et la d�sactive si elle existe
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

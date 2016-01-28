package globaz.hera.api;

/**
 * Cette classe est la classe m�re pour extraire des informations depuis la Situation Familiale.En effet, tous les
 * objets sont retourn�s par les getter des m�thodes de cette classes. <br/>
 * <br/>
 * Aucune famille ne peut �tre cr�e ou modifi�e depuis l'interface.<br/>
 * <br/>
 * Les sp�cificit�s li�es � chaque domaine d'application doivent �tre d�crit dans la classe qui impl�mentera cette
 * interface<br/>
 * <br/>
 * Utilisation de l'interface : <br/>
 * <code>
 * BIApplication application = GlobazSystem.getApplication("HERA_REMOTE");<br/>
 * BSession session = (BSession) application.newSession("globazf", "ssiiadm");<br/>
 * ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_STANDARD);
 * </code><br/>
 * <br/>
 * A partir d'une instance de ISFSituationFamiliale en fonction du domaine, toutes les m�thodes concernant la situation
 * familiale peuvent �tre appel�e<br/>
 * <code>
 * ISFMembreFamilleRequerant[] result = sf.getMembresFamille("257667","12.12.1998");<br/>
 * </code>
 * 
 * @author MMU
 */
public interface ISFSituationFamiliale {
    public static final String CS_DOMAINE_ALLOCATIONS_FAMILIALES = "36000004";
    public static final String CS_DOMAINE_CALCUL_PREVISIONNEL = "36000005";
    public static final String CS_DOMAINE_ID_ALLOCATIONS_FAMILIALES = "AF";
    public static final String CS_DOMAINE_ID_CALCUL_PREVISIONNEL = "CP";
    public static final String CS_DOMAINE_ID_INDEMNITEE_JOURNALIERE = "IJ";
    public static final String CS_DOMAINE_ID_RENTES = "R";
    public static final String CS_DOMAINE_ID_STANDARD = "STD";
    public static final String CS_DOMAINE_INDEMNITEE_JOURNALIERE = "36000001";
    public static final String CS_DOMAINE_RENTES = "36000003";
    public static final String CS_DOMAINE_STANDARD = "36000002";

    public final static String CS_ETAT_CIVIL_CELIBATAIRE = "36004001";
    public final static String CS_ETAT_CIVIL_DIVORCE = "36004004";
    public final static String CS_ETAT_CIVIL_MARIE = "36004002";
    public final static String CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES = "36004009";
    public final static String CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_JUDICIAIREMENT = "36004008";
    public final static String CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE = "36004007";
    public final static String CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT = "36004010";
    public final static String CS_ETAT_CIVIL_PARTENARIAT_SEPARE_JUDICIAIREMENT = "36004011";
    public final static String CS_ETAT_CIVIL_SEPARE_DE_FAIT = "36004005";
    public final static String CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT = "36004006";
    public final static String CS_ETAT_CIVIL_VEUF = "36004003";

    public final static String CS_REL_CONJ_DIVORCE = "36001002";
    public final static String CS_REL_CONJ_ENFANT_COMMUN = "36001005";
    public final static String CS_REL_CONJ_MARIE = "36001001";
    public final static String CS_REL_CONJ_RELATION_INDEFINIE = "36001006";
    public final static String CS_REL_CONJ_SEPARE_DE_FAIT = "36001003";
    public final static String CS_REL_CONJ_SEPARE_JUDICIAIREMENT = "36001004";

    public final static String CS_TYPE_LIEN_DIVORCE = "36005003";
    public final static String CS_TYPE_LIEN_LPART_DECES = "36005007";
    public final static String CS_TYPE_LIEN_LPART_DISSOUT = "36005006";
    public final static String CS_TYPE_LIEN_LPART_ENREGISTRE = "36005005";
    public final static String CS_TYPE_LIEN_LPART_SEPARE = "36005008";
    public final static String CS_TYPE_LIEN_MARIE = "36005001";
    public final static String CS_TYPE_LIEN_SEPARE = "36005004";
    public final static String CS_TYPE_LIEN_VEUF = "36005002";

    public final static String CS_TYPE_PERIODE_AFFILIATION = "36002004";
    public final static String CS_TYPE_PERIODE_ASSURANCE_ETRANGERE = "36002006";
    public final static String CS_TYPE_PERIODE_CERTIFICAT_VIE = "36002012";
    public final static String CS_TYPE_PERIODE_COTISATION = "36002005";
    public final static String CS_TYPE_PERIODE_DOMICILE = "36002001";
    public final static String CS_TYPE_PERIODE_ENFANT = "36002007";
    public final static String CS_TYPE_PERIODE_ETUDE = "36002008";
    public final static String CS_TYPE_PERIODE_GARDE_BTE = "36002010";
    public final static String CS_TYPE_PERIODE_IJ = "36002009";
    public final static String CS_TYPE_PERIODE_NATIONALITE = "36002003";
    public final static String CS_TYPE_PERIODE_REFUS_AF = "36002011";
    public final static String CS_TYPE_PERIODE_TRAVAILLE = "36002002";
    public final static String CS_TYPE_PERIODE_INCARCERATION = "36002013";

    public final static String CS_TYPE_RELATION_CONJOINT = "36003002";
    public final static String CS_TYPE_RELATION_ENFANT = "36003001";
    public final static String CS_TYPE_RELATION_PARENT = "36003004";
    public final static String CS_TYPE_RELATION_REQUERANT = "36003003";

    public final static String ID_MEMBRE_FAMILLE_CONJOINT_INCONNU = "999999999999";

    /**
     * <p>
     * Ajoute un membre c�libataire sans enfants dans la situation familiale
     * </p>
     * 
     * @param idTiers
     * @return le {@link ISFMembreFamille membre} cr��
     * @throws Exception
     */
    public ISFMembreFamille addMembreCelibataire(String idTiers) throws Exception;

    /**
     * <p>
     * Retourne le d�tail de l'enfant ayant l'ID Membre Famille pass� en param�tre
     * </p>
     * 
     * @param idMembreFamille
     *            ID Membre Famille de l'enfant
     * @return {@link ISFEnfant l'enfant}
     * @throws Exception
     *             en cas d'erreur
     */
    public ISFEnfant getEnfant(String idMembreFamille) throws Exception;

    /**
     * <p>
     * Renvoie le d�tail d'un membre de famille.
     * </p>
     * 
     * @param idMembreFamille
     *            l'id du {@link ISFMembreFamille membre de la famille} que l'on souhaite conna�tre
     * @return le {@link ISFMembreFamille membre de la famille}
     * @throws Exception
     *             en cas d'erreur
     */
    public ISFMembreFamille getMembreFamille(String idMembreFamille) throws Exception;

    /**
     * <p>
     * Renvoie le d�tail d'un membre de famille � une date donn�e
     * </p>
     * 
     * @param idMembreFamille
     *            la personne que l'on souhaite conna�tre � la date donn�e
     * @param date
     *            la date � laquelle on aimerait avoir le d�tail du membre de la famille (au format "jj.mm.aaaa")
     * @return le d�tail du {@link ISFMembreFamille membre de la famille}
     * @throws Exception
     *             en cas d'erreur
     */
    public ISFMembreFamille getMembreFamille(String idMembreFamille, String date) throws Exception;

    /**
     * <p>
     * Renvoie les membres de la famille pour un idTiers donn�.<br/>
     * Les conjoints avec une relation ind�finie ainsi que leurs enfants ne sont pas retourn�s.
     * </p>
     * 
     * @param idTiers
     * @return {@link ISFMembreFamille}[] de taille 0 si aucun membre n'a �t� trouv�, <code>null</code> en cas de tiers
     *         invalide
     * @throws Exception
     */
    public ISFMembreFamilleRequerant[] getMembresFamille(String idTiers) throws Exception;

    /**
     * <p>
     * Retourne la liste de tous les membres de la famille par rapport � un membre donn�.
     * </p>
     * 
     * @param idMembreFamille
     * @param inclureParents
     *            Inclut les parents du membres
     * @return les {@link ISFMembreFamille membre de la famille} selon le membre pass� en param�tre
     * @throws Exception
     */
    public ISFMembreFamille[] getMembresFamilleEtendue(String idMembreFamille, Boolean inclureParents) throws Exception;

    /**
     * <p>
     * Renvoie les membres de la famille du requ�rant et le requ�rant lui-m�me pour son idTiers donn�.<br/>
     * Les conjoints avec une relation ind�finie ainsi que leurs enfants ne sont pas retourn�s
     * </p>
     * 
     * @param idTiers
     * @return {@link ISFMembreFamilleRequerant}[] de taille 0 si aucun membre n'a �t� trouv�, <code>null</code> en cas
     *         de tiers invalide
     * @throws Exception
     */
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerant(String idTiers) throws Exception;

    /**
     * <p>
     * Renvoie les membres de la famille du requ�rant et le requ�rant lui-m�me pour son idTiers donn�.<br/>
     * Les conjoints avec une relation ind�finie ne sont pas retourn�s
     * </p>
     * <p>
     * Les conjoints qui ont une relation de type "Enfant commun" sont retourn�s seulement si au moins un enfants des
     * conjoints est n� avant la date donn�e
     * </p>
     * 
     * <p>
     * Seuls les conjoints dont la date de d�but de la relation est avant la date donn�e sont retourn�s.<br/>
     * Seuls les enfants dont la date de naissance est avant la date donn�e sont retourn�s.
     * </p>
     * 
     * @param idTiers
     * @param date
     *            au format "jj.mm.aaaa"
     * @return les {@link ISFMembreFamille membres de la famille} du requ�rant
     * @throws Exception
     */
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerant(String idTiers, String date) throws Exception;

    /**
     * <p>
     * Renvoie les membres de la famille du requ�rant et le requ�rant lui-m�me pour son idMembreFamille donn�.<br/>
     * Les conjoints avec une relation ind�finie ainsi que leurs enfants ne sont pas retourn�s
     * </p>
     * 
     * @param idMembreFamille
     * @return {@link ISFMembreFamilleRequerant}[] de taille 0 si aucun membre n'a �t� trouv�, <code>null</code> en cas
     *         de membre invalide
     * @throws Exception
     */
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerantParMbrFamille(String idMembreFamille) throws Exception;

    /**
     * <p>
     * Renvoie les parents de l'idTiers
     * </p>
     * 
     * @param idTiers
     * @return {@link ISFMembreFamilleRequerant}[] de taille 0 si aucun membre n'a �t� trouv�, <code>null</code> en cas
     *         de tiers invalide
     * @throws Exception
     */
    public ISFMembreFamille[] getParents(String idTiers) throws Exception;

    /**
     * <p>
     * Renvoie les parents de l'idMembreFamille
     * </p>
     * 
     * @param idMembreFamille
     * @return {@link ISFMembreFamilleRequerant}[] de taille 0 si aucun membre n'a �t� trouv�, <code>null</code> en cas
     *         de membre invalide
     * @throws Exception
     */
    public ISFMembreFamille[] getParentsParMbrFamille(String idMembreFamille) throws Exception;

    /**
     * <p>
     * Renvoie les p�riodes d'un membre
     * </p>
     * 
     * @param idMembreFamille
     * 
     * @return les {@link ISFPeriode p�riodes} du membres de la famille
     * 
     * @throws Exception
     */
    public ISFPeriode[] getPeriodes(String idMembreFamille) throws Exception;

    /**
     * <p>
     * Renvoie les p�riodes d'un type sp�cifique d'un membre
     * </p>
     * 
     * @param idMembreFamille
     * @param typePeriode
     * @return les {@link ISFPeriode p�riodes} du membre selon le type
     * @throws Exception
     */
    public ISFPeriode[] getPeriodes(String idMembreFamille, String typePeriode) throws Exception;

    /**
     * <p>
     * Renvoie toutes les relations familiales d'un membre jusqu'� une date donn�e. <br/>
     * Entre deux conjoint seul la relation la plus r�cente est retourn�e.<br/>
     * Seules les relations de type mariage, s�par� et divorc� sont prises en comptes: les relation ind�finies ou enfant
     * commun sont ignor�es
     * </p>
     * 
     * @param idTiersRequerant
     * @param date
     *            au format "jj.mm.aaaa" ou null
     * @return les {@link ISFRelationFamiliale relations familiales} � la date donn�e
     * @throws Exception
     */
    public ISFRelationFamiliale[] getRelationsConjoints(String idTiersRequerant, String date) throws Exception;

    /**
     * <p>
     * Renvoie toutes les relations familiales �tendue d'un membre jusqu'� une date donn�e.<br/>
     * Entre deux conjoint seul la relation la plus r�cente est retourn�e.<br/>
     * Seules les relations de type mariage, s�par� et divorc� sont prises en comptes: les relation ind�finies ou enfant
     * commun sont ignor�es
     * </p>
     * <p>
     * La relation �tendue agrandit la famille aux conjoints des conjoints du requ�rant
     * </p>
     * 
     * @param idTiersRequerant
     * @param date
     *            au format "jj.mm.aaaa" ou null
     * @return un tableau de taille 0, si les membres n'ont aucune relation
     * @throws Exception
     */
    public ISFRelationFamiliale[] getRelationsConjointsEtendues(String idTiersRequerant, String date) throws Exception;

    /**
     * <p>
     * Renvoie toutes les relations entre deux membre de familles
     * </p>
     * 
     * @param idMembreFamille1
     *            le premier conjoint
     * @param idMembreFamille2
     *            le deuxi�me conjoint
     * @param triDateDebutDecroissant
     *            <code>true</code> pour un tri d�croissant, <code>false</code> pour un tri croissant des dates de d�but
     *            de relation
     * @return les {@link ISFRelationFamiliale relations familiales}
     * @throws Exception
     */
    public ISFRelationFamiliale[] getToutesRelationsConjoints(String idMembreFamille1, String idMembreFamille2,
            Boolean triDateDebutDecroissant) throws Exception;

    /**
     * <p>
     * Renvoie toutes les relations entre deux membres de familles
     * </p>
     * 
     * @param idTiers1
     *            le premier conjoint
     * @param idTiers2
     *            le deuxi�me conjoint
     * @param triDateDebutDecroissant
     *            <code>true</code> pour un tri d�croissant, <code>false</code> pour un tri croissant des dates de d�but
     *            de relation
     * @return les {@link ISFRelationFamiliale relations familiales}
     * @throws Exception
     */
    public ISFRelationFamiliale[] getToutesRelationsConjointsParIdTiers(String idTiers1, String idTiers2,
            Boolean triDateDebutDecroissant) throws Exception;

    /**
     * <p>
     * Retourne <code>true</code> si le requ�rant � �t� trouv� pour l'id tiers + domaine pass� en param�tre.
     * </p>
     * 
     * @param idTiersRequerant
     * @param csDomaine
     * @return <code>true</code> si requ�rant existe, <code>false</code> sinon.
     */
    public Boolean isRequerant(String idTiersRequerant, String csDomaine) throws Exception;

    /**
     * <p>
     * Permet de d�finir le domaine applicatif.
     * </p>
     * 
     * @param domaine
     *            le code syst�me du nouveau domaine
     * @throws Exception
     */
    public void setDomaine(String domaine) throws Exception;
}
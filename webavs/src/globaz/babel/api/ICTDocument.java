package globaz.babel.api;

import globaz.globall.api.BIEntity;
import java.util.Map;

/**
 * <h1>Description</h1>
 * 
 * <p>
 * Interface de chargement des documents Babel.
 * </p>
 * 
 * @author vre
 * @see #load()
 * @see #loadListeNoms()
 */
public interface ICTDocument extends BIEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CS_ASSURE = "34005001";
    public static final String CS_EDITABLE = "34000001";
    public static final String CS_EMPLOYEUR = "34005002";
    public static final String CS_GROUPE_DESTINATAIRE = "CTDESTINAT";

    public static final String CS_GROUPE_EDITABLE = "CTEDITABLE";
    public static final String CS_NON_EDITABLE = "34000003";
    public static final String CS_TEXTES_UNIQUEMENT = "34000002";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut code iso langue.
     * 
     * @return la valeur courante de l'attribut code iso langue
     */
    public String getCodeIsoLangue();

    /**
     * getter pour l'attribut cs destinataire.
     * 
     * @return la valeur courante de l'attribut cs destinataire
     */
    public String getCsDestinataire();

    /**
     * getter pour l'attribut cs domaine.
     * 
     * @return la valeur courante de l'attribut cs domaine
     */
    public String getCsDomaine();

    /**
     * getter pour l'attribut cs editable.
     * 
     * @return la valeur courante de l'attribut cs editable
     */
    public String getCsEditable();

    /**
     * getter pour l'attribut cs type document.
     * 
     * @return la valeur courante de l'attribut cs type document
     */
    public String getCsTypeDocument();

    /**
     * getter pour l'attribut date desactivation.
     * 
     * @return la valeur courante de l'attribut date desactivation
     */
    public String getDateDesactivation();

    /**
     * getter pour l'attribut id document.
     * 
     * @return la valeur courante de l'attribut id document
     */
    public String getIdDocument();

    /**
     * getter pour l'attribut nom.
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom();

    public String getNomLike();

    /**
     * Retourne une instance de {@link ICTListeTextes ICTListeTextes} enveloppant tous les textes du niveau d'indice
     * 'idNiveau'.
     * 
     * @param idNiveau
     *            l'indice du niveau dont on veut les textes
     * 
     * @return la valeur courante de l'attribut niveau ou null si non trouvé
     */
    public ICTListeTextes getTextes(int idNiveau);

    /**
     * getter pour l'attribut actif.
     * 
     * @return la valeur courante de l'attribut actif
     */
    public Boolean isActif();

    /**
     * getter pour l'attribut defaut.
     * 
     * @return la valeur courante de l'attribut defaut
     */
    public Boolean isDefaut();

    public Boolean isStyledDocument();

    /**
     * Charge le document ayant les critères définis par les setters (seule la date de désactivation n'est pas prise en
     * compte).
     * 
     * <p>
     * Utilise d'abord le code iso de langue setter grace à {@link #setCodeIsoLangue() setCodeIsoLangue()} puis celui de
     * la session si ce dernier n'a pas ete sette.
     * </p>
     * 
     * <p>
     * Si le boolean actif est null, retourne les documents actifs ET inactifs, sinon actifs OU inactifs.
     * </p>
     * 
     * <p>
     * Seules les instances retournées par cette méthode reflèteront le contenu de la base, l'instance ayant servi à la
     * recherche ne sera pas mise à jour.
     * </p>
     * 
     * @return un tableau d'instances de ICTDocument ou null si non trouvé
     * 
     * @throws Exception
     *             si erreur avec chargement
     */
    public ICTDocument[] load() throws Exception;

    /**
     * Charge la liste des noms des documents qui correspondent aux critères settés dans cet objet.
     * 
     * <p>
     * Note: le critère {@link #getCodeIsoLangue() langue} n'a pas de sens dans ce contexte car le nom d'un document ne
     * peut être internationalisé, il est donc ignoré. Le critère date de désactivation est également ignoré.
     * </p>
     * 
     * <p>
     * Note: cette méthode fournit une façon peu coûteuse de déterminer le nom pour un id et l'id pour un nom.
     * </p>
     * 
     * @return Une map (jamais nulle, peut-etre vide) de paires {clé = (String) idDocument, valeur = (String)
     *         nomDocument}
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Map loadListeNoms() throws Exception;

    /**
     * setter pour l'attribut actif.
     * 
     * @param actif
     *            une nouvelle valeur pour cet attribut
     */
    public void setActif(Boolean actif);

    /**
     * setter pour l'attribut code iso langue.
     * 
     * @param codeIsoLangue
     *            DOCUMENT ME!
     */
    public void setCodeIsoLangue(String codeIsoLangue);

    /**
     * setter pour l'attribut cs destinataire.
     * 
     * @param csDestinataire
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsDestinataire(String csDestinataire);

    /**
     * setter pour l'attribut cs domaine.
     * 
     * @param csDomaine
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsDomaine(String csDomaine);

    /**
     * setter pour l'attribut cs type document.
     * 
     * @param csTypeDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeDocument(String csTypeDocument);

    /**
     * setter pour l'attribut default.
     * 
     * @param defaut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDefault(Boolean defaut);

    /**
     * setter pour l'attribut id document.
     * 
     * @param idDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDocument(String idDocument);

    /**
     * setter pour l'attribut nom.
     * 
     * @param nom
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String nom);

    public void setNomLike(String nom);

    /**
     * retourne le nombre de niveaux de ce document.
     * 
     * @return le nombre de niveau de ce document
     */
    public int size();
}

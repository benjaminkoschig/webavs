package ch.globaz.pyxis.business.services;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseSearch;
import ch.globaz.pyxis.business.model.AdresseSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.LocaliteSearchSimpleModel;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;

public interface AdresseService extends JadeApplicationService {

    public final static String CS_DOMAINE_DEFAUT = "519004";

    public final static String CS_TYPE_COURRIER = "508001";
    public final static String CS_TYPE_DOMICILE = "508008";

    public final static int MODE_CASC_PRIORITY_DOMAINE = 1;
    public final static int MODE_CASC_PRIORITY_TYPE = 2;

    /**
     * Ajoute une adresse à un tiers. <br/>
     * <br/>
     * Si l'adresse existe déjà, on la met à jour avec les informations contenus dans le modèle. <br/>
     * <br/>
     * Les informations du modèles qui sont obligatoires sont : <b>
     * <ul>
     * <li>model.getTiers().getId()</li>
     * <li>model.getLocalite().getNumPostal()</li>
     * </ul>
     * </b> Auteur : CBU - 15.03.2011<br/>
     * <br/>
     * 
     * @author CBU
     * @param model
     *            - Le model contenant les infos de l'adresse
     * @param typeAdresse
     *            - Le type d'adresse
     * @param idApplication
     *            - L'id Application (domaine)
     * @param wantUpdatePaiement
     *            - true pour mettre à jour l'adresse de paiement (si possible)
     * @return l'AdresseComplexModel crée - NULL en cas d'erreur
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    AdresseComplexModel addAdresse(AdresseComplexModel model, String idApplication, String typeAdresse,
            Boolean wantUpdatePaiement) throws JadePersistenceException, JadeApplicationException;

    /**
     * Mets à jour l'adresse à un tiers. <br/>
     * <br/>
     */
    AdresseComplexModel updateAdresse(AdresseComplexModel model, String idApplication, String typeAdresse,
            Boolean wantUpdatePaiement) throws JadePersistenceException, JadeApplicationException;

    /**
     * Count des adresses
     * 
     * @param model
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    int countAdresse(AdresseSearchComplexModel model) throws JadePersistenceException, JadeApplicationException;

    /**
     * Count des adresses de paiement
     * 
     * @param model
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    int countAdressePaiement(AdressePaiementSearchComplexModel model) throws JadePersistenceException,
            JadeApplicationException;

    int countAdresseWithSimpleTiers(AdresseSearch adresseSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Count des localites
     * 
     * @param model
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    int countLocalite(LocaliteSearchSimpleModel model) throws JadePersistenceException, JadeApplicationException;

    /**
     * Count des pays
     * 
     * Le service permet d'effectuer un count non case sensitive que pour une des langues à la fois.
     * 
     * @param model
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    int countPays(PaysSearchSimpleModel model) throws JadePersistenceException, JadeApplicationException;

    /**
     * Recherche des adresses
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    AdresseSearchComplexModel findAdresse(AdresseSearchComplexModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Recherche des adresses de paiement
     * 
     * @param searchModel
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    AdressePaiementSearchComplexModel findAdressePaiement(AdressePaiementSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Recherche des localités
     */
    LocaliteSearchSimpleModel findLocalite(LocaliteSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Chargement d'une localité avec son id
     * 
     * @author sce (pc)
     * @param idLocalite
     * @return la localite correspondant a l'id passé en aparmetre
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    LocaliteSimpleModel readLocalite(String idLocalite) throws JadePersistenceException, JadeApplicationException;

    /**
     * Recherche des pays
     * 
     * Le service permet d'effectuer une recherche non case sensitive que pour une des langues à la fois.
     */
    PaysSearchSimpleModel findPays(PaysSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoie l'adresse de paiement sous forme d'une chaîne de caractère
     * 
     * @param idTiers
     * @param idApplication
     * @param date
     * @param numeroExterne
     * @return une entité AdresseTiersDetail contenant divers informations sur l'adresse du tiers
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    AdresseTiersDetail getAdressePaiementTiers(String idTiers, Boolean herite, String idApplication, String date,
            String numeroExterne) throws JadePersistenceException, JadeApplicationException;

    /**
     * Renvoie l'adresse d'un tiers
     * 
     * @param idTiers
     * @param herite
     *            permet de choisir si l'on désire acziver ou non la cascade des adresses
     * @param date
     * @param idApplication
     * @param typeAdresse
     * @param numeroExterne
     * @return une entité AdresseTiersDetail contenant divers informations sur l'adresse du tiers
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    AdresseTiersDetail getAdresseTiers(String idTiers, Boolean herite, String date, String idApplication,
            String typeAdresse, String numeroExterne) throws JadePersistenceException, JadeApplicationException;

    /**
     * <p>
     * Retourne l'adresse de courier d'un tiers d'un domaine spécifié selon les règles suivantes:
     * </p>
     * <p>
     * <code>idApplication=xxxxxxx =>domaine AF orderTypeAdresse = {"508021","508001"}
     * => Exploitation,Courier</code>
     * </p>
     * <p>
     * <b>-modePriority=1 => priorité au domaine</b>
     * 
     * le service cherche dans les adresses de type Exploitation, puis de courrier si une <b><i>adresse domaine
     * AF</i></b> existe. Si rien trouvé, il recherche dans le même ordre des types si une<b><i>adresse standard</i></b>
     * existe.
     * </p>
     * <p>
     * <b>- modePriority=2 => priorité au type </b>
     * 
     * le service cherche dans les adresses de type Exploitation, puis de courrier si une <b><i>adresse domaine AF ou
     * une adresse standard</i></b> existe. Dès qu'une adresse est trouvée dans un type, on ne parcourt pas les autres
     * types, même si ce n'est pas une adresse du domaine AF.
     * </p>
     * 
     * 
     * @param idTiers
     *            tiers dont on veut l'adresse
     * @param date
     *            date de validité de l'adresse
     * @param idApplication
     *            domaine de l'adresse
     * @param orderTypeAdresse
     *            cascade de type d'adresse, liste de code système
     * @param modePriority
     *            <ul>
     *            <li>1 - priorité au domaine de l'adresse</li>
     *            <li>2 - priorité au type de l'adresse</li>
     *            </ul>
     * @return une entité AdresseTiersDetail contenant divers informations sur l'adresse du tiers
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    AdresseTiersDetail getAdresseTiersCustomCascade(String idTiers, String date, String idApplication,
            Collection<String> orderTypeAdresse, int modePriority) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Renvoie true si le tiers a au moin une adresse de paiement pour le doamine et la date passé la date doit être au
     * format jj.mm.aaaa le idApplication est un code system représentatnt le domaine métier (rentes, af ij etc...) de
     * la famille PYAPPLICAT
     */
    Boolean hasAdressePaiement(String idTiers, String idApplication, String date) throws JadePersistenceException,
            JadeApplicationException;

    public AdresseSearch searchAdresseWithSimpleTiers(AdresseSearch adresseSearch) throws JadePersistenceException,
            JadeApplicationException;
}
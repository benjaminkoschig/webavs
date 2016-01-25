package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;

/**
 * Interface représentant un compte annexe
 * 
 * @author auteur
 */

public interface APICompteAnnexe extends BIEntity {
    /** Clé alternée sur l'identifiant externe */
    public final static int AK_IDEXTERNE = 1;
    /** Tri de la proposition de compensation par ordre du montant le plus élevé */
    public final static int PC_ORDRE_MONTANT_PLUS_ELEVE = 3;
    /** Tri de la proposition de compensation par ordre du montant le plus petit */
    public final static int PC_ORDRE_MONTANT_PLUS_PETIT = 4;
    /** Tri de la proposition de compensation par ordre du plus ancien */
    public final static int PC_ORDRE_PLUS_ANCIEN = 1;
    /** Tri de la proposition de compensation par ordre du plus récent */
    public final static int PC_ORDRE_PLUS_RECENT = 2;
    /** Type de compensation pour facturation */
    public final static int PC_TYPE_FACTURATION = 5;
    /**
     * Type de compensation, le solde à compenser est calculé, sections positives
     */
    public final static int PC_TYPE_INTERNE_CREANCIER = 4;
    /**
     * Type de compensation, le solde à compenser est calculé, sections négatives
     */
    public final static int PC_TYPE_INTERNE_DEBITEUR = 3;
    /** Type de compensation, le solde du compte doit être null */
    public final static int PC_TYPE_INTERNE_ZERO = 2;
    /** Type de compensation, le montant à compenser ne peut pas être null */
    public final static int PC_TYPE_MONTANT = 1;

    /**
     * Renvoie le domaine du rôle du compte annexe
     * 
     * @return domaine par défaut en fonction du rôle
     */
    public String _getDefaultDomainFromRole();

    /**
     * Cette méthode permet de créer un compte annexe sans passer par la création d'écritures
     * 
     * @param session
     * @param transaction
     * @param idTiers
     * @param idRole
     * @param idExterneRole
     * @return APICompteAnnexe
     * @throws Exception
     *             retourne une exception en cas d'erreur
     */
    public APICompteAnnexe createCompteAnnexe(BISession session, BITransaction transaction, String idTiers,
            String idRole, String idExterneRole) throws Exception;

    /**
     * Renvoie la valeur de la propriété contDateDebBloque du compte annexe Il s'agit de la date de début du blocage du
     * contentieux
     * 
     * @return la valeur de la propriété contDateDebBloque
     */
    public String getContDateDebBloque();

    /**
     * Renvoie la valeur de la propriété contDateFinBloque du compte annexe Il 'sagit la date de fin du blocage du
     * contentieux
     * 
     * @return la valeur de la propriété contDateFinBloque
     */
    public String getContDateFinBloque();

    /**
     * Renvoie la valeur de la propriété contEstBloque. Si cette propriété est vrai, le contentieux de ce compte annexe
     * sera bloqué depuis contDateDebBlque jusqu'à contDateFinBloque
     * 
     * @return true si le contentieux du compte annexe est bloqué
     */
    public Boolean getContEstBloque();

    /**
     * Renvoie la valeur de la propriété description du compte annexe (nom).
     * 
     * @return la valeur de la propriété description
     */
    public String getDescription();

    /**
     * Renvoie la valeur de la propriété estConfidentiel. Si cette propriété est vrai, le compte annexe est confidentiel
     * la consultation et la mutation du compte annexe seront restreintes
     * 
     * @return true si le compte annexe est confidentiel
     */
    public Boolean getEstConfidentiel();

    /**
     * Renvoie la valeur de la propriété estVerrouille. Lorsque le compte annexe est verrouillé, il ne pourra plus être
     * utilisé pour passer des écritures (comptabiliser des opérations)
     * 
     * @return true si le compte est vérouillé
     */
    public Boolean getEstVerrouille();

    /**
     * Renvoie la valeur de la propriété idCategorie, il s'agit de l'id de la catégorie du compte annexe
     * 
     * @return la valeur de la propriété idCategorie
     */
    public String getIdCategorie();

    /**
     * Renvoie la valeur de la propriété idCompteAnnexe, il s'agit de l'identifiant du compte annexe
     * 
     * @return la valeur de la propriété idCompteAnnexe
     */
    public String getIdCompteAnnexe();

    /**
     * Renvoie la valeur de la propriété idContMotifBloque, il s'agit du motif de blocage du contentieux Cette valeur
     * correspond à un code système
     * 
     * @return la valeur de la propriété idContMotifBloque (Code système)
     */
    public String getIdContMotifBloque();

    /**
     * Renvoie la valeur de la propriété idExterneRole, il s'agit de l'identifiant externe du tiers en fonction du rôle
     * Il s'agit par exemple du numéro d'affilié. C'est le numéro qui est connu et utilisé par l'utilisateur. Ce numéro
     * est formaté
     * 
     * @return la valeur de la propriété idExterneRole
     */
    public String getIdExterneRole();

    /**
     * Renvoie la valeur de la propriété idJournal, il s'agit de l'identifiant du journal dans lequel le compte annexe a
     * été créé
     * 
     * @return la valeur de la propriété idJournal
     */
    public String getIdJournal();

    /**
     * Renvoie la valeur de la propriété idRole, il s'agit de l'identifiant du rôle un rôle est par exemple un affilié <br>
     * idRole 517001 --> Assuré</br> <br>
     * idRole 517002 --> Affilié</br> <br>
     * idRole 517033 --> APG</br> <br>
     * idRole 517034 --> IJAI</br> <br>
     * idRole 517036 --> AF</br> <br>
     * idRole 517038 --> Rentier</br> <br>
     * idRole 517039 --> Affilié paritaire</br> <br>
     * idRole 517040 --> Affilié personnel</br> <br>
     * idRole 517041 --> Administrateur</br>
     * 
     * @return la valeur de la propriété idRole
     */
    public String getIdRole();

    /**
     * Renvoie la valeur de la propriété idTiers, il s'agit de l'identifiant du tiers du compte annexe
     * 
     * @return la valeur de la propriété idTiers
     */
    public String getIdTiers();

    /**
     * Renvoie la valeur de la propriété idTiers, il s'agit de l'identifiant du tiers du compte annexe
     * 
     * @return la valeur de la propriété idTiers
     */
    public String getIdTri();

    /**
     * Renvoie l'interface décrivant un rôle au format définit par la comptabilité auxiliaire
     * 
     * @return la valeur de la propriété role
     */
    public IntRole getRole();

    /**
     * Renvoie la valeur de la propriété solde, il s'agit du solde du compte annexe non formaté par exemple 12526.30
     * 
     * @return la valeur de la propriété solde
     */
    public String getSolde();

    /**
     * Renvoie la valeur de la propriété soldeAt, il s'agit du solde du compte annexe jusqu'à la date passée en
     * paramètre (comprise)
     * 
     * @return la valeur de la propriété soldeAt
     * @param date
     *            date jusqu'à laquelle le solde doit être pris en compte
     */
    public String getSoldeAt(String date);

    /**
     * Renvoie la valeur de la propriété soldeFormate, il s'agit du solde du compte annexe formaté pour l'affichage par
     * exemple 12'526.30
     * 
     * @return la valeur de la propriété soldeFormate
     */
    public String getSoldeFormate();

    /**
     * Renvoie la valeur de la propriété soldeInitialAt, il s'agit du solde du compte annexe jusqu'à la date passée en
     * paramètre (non comprise)
     * 
     * @return la valeur de la propriété soldeInitialAt
     * @param date
     *            date jusqu'à laquelle le solde doit être pris en compte
     */
    public String getSoldeInitialAt(String date, String forSelectionSections);

    /**
     * Renvoie l'interface décrivant le tiers au format définit par la comptabilité auxiliaire
     * 
     * @return la valeur de la propriété tiers
     */
    public IntTiers getTiers();

    /**
     * Renvoie la valeur de la propriété titulaireEntete, il s'agit du rôle selon la langue, du numéro externe du rôle,
     * du nom et du lieu du tiers. Par exemple:<br>
     * <br>
     * Affilié 123.1002<br>
     * Mueller Jacques<br>
     * 1000 Lausanne<br>
     * <br>
     * 
     * @return la valeur de la propriété titulaireEntete
     */
    public String getTitulaireEntete();

    /**
     * Un motif de blocage (idModifBlocage) existe-t'il pour l'année (year) pour ce compte annexe ?
     * 
     * @param idModifBlocage
     * @param year
     * @return
     * @throws CATechnicalException
     */
    public boolean hasMotifContentieuxForYear(String idModifBlocage, String year) throws CATechnicalException;

    /**
     * Indique si des écritures (opérations) ont été passée pour ce compte annexe
     * 
     * @return true si des écritures (opérations) ont été passées pour ce compte annexe
     */
    public boolean hasOperations();

    /**
     * Indique si des sections existent pour ce compte annexe
     * 
     * @return true si des sections existent pour ce compte annexe
     */
    public boolean hasSections();

    /**
     * Le compte annexe est-il de type auxiliaire ?
     * 
     * @return
     */
    public boolean isCompteAuxiliaire();

    /**
     * Le compte annexe est-il de type standard ?
     * 
     * @return
     */
    public boolean isCompteStantard();

    /**
     * Retourne vrai si le motif de blocage du compte annexe est dans les valeurs suivantes :
     * 
     * @param annee
     * @return
     */
    public boolean isEnFaillte(String annee) throws CATechnicalException;

    /**
     * Retourne un ensemble de sections qui correspondent aux critères de compensation désirés
     * 
     * @return java.util.Collection une collection de Sections à compenser
     * @param type
     *            java.lang.int type de compensation
     * @param ordre
     *            java.lang.int l'ordre dans lequel les factures doivent être compensées
     * @param args
     *            java.lang.String les autres arguments dépendant de la rèqle de compensation
     * @exception java.lang.Exception
     *                si échec
     */
    public java.util.Collection propositionCompensation(int type, int ordre, String montantACompenser)
            throws java.lang.Exception;

    /**
     * Chargement d'un compte annexe chargement d'un compte annexe depuis la DB<br>
     * <br>
     * utiliser la methode setIdCompteAnnexe(String id) préalablement pour<br>
     * specifier le tiers à charger<br>
     * <br>
     * <br>
     * 
     * @exception java.lang.Exception
     *                si echec
     */
    public void retrieve() throws java.lang.Exception;

    /**
     * Chargement d'un compte annexe chargement d'un compte annexe depuis la DB<br>
     * <br>
     * utiliser la methode setIdCompteAnnexe(String id) préalablement pour<br>
     * specifier le tiers à charger<br>
     * <br>
     * <br>
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si echec
     */

    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit la valeur de la propriété alternateKey, permet de faire un retrieve sans connaître l'idCompteAnnexe<br>
     * Clé alterné numéro 1 : idRole et idExterneRole<br>
     * avant de faire un retrieve, il faut utiliser les méthodes suivantes:<br>
     * setAlternateKey(AK_IDEXTERNE)<br>
     * setIdRole(String id)<br>
     * setIdExterneRole(String id)<br>
     * <br>
     * 
     * @param altKey
     *            altKey indique le type de clé alternée à utiliser
     */
    public void setAlternateKey(int altKey);

    /**
     * Définit la valeur de la propriété contDateDebBloque
     * 
     * @param contDateDebBloque
     *            la nouvelle date de début de blocage
     */
    public void setContDateDebBloque(String contDateDebBloque);

    /**
     * Définit la valeur de la propriété contDateFinBloque
     * 
     * @param contDateFinBloque
     *            la nouvelle date de fin de blocage
     */
    public void setContDateFinBloque(String contDateFinBloque);

    /**
     * Définit la valeur de la propriété contEstBloque
     * 
     * @param contEstBloque
     *            la nouvelle valeur de blocage
     */
    public void setContEstBloque(Boolean contEstBloque);

    /**
     * Définit la valeur de la propriété idCompteAnnexe
     * 
     * @param idCompteAnnexe
     *            le nouveau identifiant du compte annexe
     */
    public void setIdCompteAnnexe(String idCompteAnnexe);

    /**
     * Définit la valeur de la propriété idContMotifBloque
     * 
     * @param idContMotifBloque
     *            le nouveau code du motif de blocage
     */
    public void setIdContMotifBloque(String idContMotifBloque);

    /**
     * Définit la valeur de la propriété idExterneRole
     * 
     * @param idExterneRole
     *            le nouveau identifiant du role externe
     */
    public void setIdExterneRole(String idExterneRole);

    /**
     * Définit la valeur de la propriété idRole <br>
     * idRole 517001 --> Assuré</br> <br>
     * idRole 517002 --> Affilié</br> <br>
     * idRole 517033 --> APG</br> <br>
     * idRole 517034 --> IJAI</br> <br>
     * idRole 517036 --> AF</br>
     * 
     * @param idRole
     *            le nouveau identifiant du role
     */
    public void setIdRole(String idRole);

    /**
     * Définit la valeur de la propriété idTri
     * 
     * @param idTri
     *            la valeur du tri
     */
    public void setIdTri(String idTri);
}

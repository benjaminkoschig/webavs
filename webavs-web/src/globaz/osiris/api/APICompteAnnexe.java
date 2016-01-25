package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;

/**
 * Interface repr�sentant un compte annexe
 * 
 * @author auteur
 */

public interface APICompteAnnexe extends BIEntity {
    /** Cl� altern�e sur l'identifiant externe */
    public final static int AK_IDEXTERNE = 1;
    /** Tri de la proposition de compensation par ordre du montant le plus �lev� */
    public final static int PC_ORDRE_MONTANT_PLUS_ELEVE = 3;
    /** Tri de la proposition de compensation par ordre du montant le plus petit */
    public final static int PC_ORDRE_MONTANT_PLUS_PETIT = 4;
    /** Tri de la proposition de compensation par ordre du plus ancien */
    public final static int PC_ORDRE_PLUS_ANCIEN = 1;
    /** Tri de la proposition de compensation par ordre du plus r�cent */
    public final static int PC_ORDRE_PLUS_RECENT = 2;
    /** Type de compensation pour facturation */
    public final static int PC_TYPE_FACTURATION = 5;
    /**
     * Type de compensation, le solde � compenser est calcul�, sections positives
     */
    public final static int PC_TYPE_INTERNE_CREANCIER = 4;
    /**
     * Type de compensation, le solde � compenser est calcul�, sections n�gatives
     */
    public final static int PC_TYPE_INTERNE_DEBITEUR = 3;
    /** Type de compensation, le solde du compte doit �tre null */
    public final static int PC_TYPE_INTERNE_ZERO = 2;
    /** Type de compensation, le montant � compenser ne peut pas �tre null */
    public final static int PC_TYPE_MONTANT = 1;

    /**
     * Renvoie le domaine du r�le du compte annexe
     * 
     * @return domaine par d�faut en fonction du r�le
     */
    public String _getDefaultDomainFromRole();

    /**
     * Cette m�thode permet de cr�er un compte annexe sans passer par la cr�ation d'�critures
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
     * Renvoie la valeur de la propri�t� contDateDebBloque du compte annexe Il s'agit de la date de d�but du blocage du
     * contentieux
     * 
     * @return la valeur de la propri�t� contDateDebBloque
     */
    public String getContDateDebBloque();

    /**
     * Renvoie la valeur de la propri�t� contDateFinBloque du compte annexe Il 'sagit la date de fin du blocage du
     * contentieux
     * 
     * @return la valeur de la propri�t� contDateFinBloque
     */
    public String getContDateFinBloque();

    /**
     * Renvoie la valeur de la propri�t� contEstBloque. Si cette propri�t� est vrai, le contentieux de ce compte annexe
     * sera bloqu� depuis contDateDebBlque jusqu'� contDateFinBloque
     * 
     * @return true si le contentieux du compte annexe est bloqu�
     */
    public Boolean getContEstBloque();

    /**
     * Renvoie la valeur de la propri�t� description du compte annexe (nom).
     * 
     * @return la valeur de la propri�t� description
     */
    public String getDescription();

    /**
     * Renvoie la valeur de la propri�t� estConfidentiel. Si cette propri�t� est vrai, le compte annexe est confidentiel
     * la consultation et la mutation du compte annexe seront restreintes
     * 
     * @return true si le compte annexe est confidentiel
     */
    public Boolean getEstConfidentiel();

    /**
     * Renvoie la valeur de la propri�t� estVerrouille. Lorsque le compte annexe est verrouill�, il ne pourra plus �tre
     * utilis� pour passer des �critures (comptabiliser des op�rations)
     * 
     * @return true si le compte est v�rouill�
     */
    public Boolean getEstVerrouille();

    /**
     * Renvoie la valeur de la propri�t� idCategorie, il s'agit de l'id de la cat�gorie du compte annexe
     * 
     * @return la valeur de la propri�t� idCategorie
     */
    public String getIdCategorie();

    /**
     * Renvoie la valeur de la propri�t� idCompteAnnexe, il s'agit de l'identifiant du compte annexe
     * 
     * @return la valeur de la propri�t� idCompteAnnexe
     */
    public String getIdCompteAnnexe();

    /**
     * Renvoie la valeur de la propri�t� idContMotifBloque, il s'agit du motif de blocage du contentieux Cette valeur
     * correspond � un code syst�me
     * 
     * @return la valeur de la propri�t� idContMotifBloque (Code syst�me)
     */
    public String getIdContMotifBloque();

    /**
     * Renvoie la valeur de la propri�t� idExterneRole, il s'agit de l'identifiant externe du tiers en fonction du r�le
     * Il s'agit par exemple du num�ro d'affili�. C'est le num�ro qui est connu et utilis� par l'utilisateur. Ce num�ro
     * est format�
     * 
     * @return la valeur de la propri�t� idExterneRole
     */
    public String getIdExterneRole();

    /**
     * Renvoie la valeur de la propri�t� idJournal, il s'agit de l'identifiant du journal dans lequel le compte annexe a
     * �t� cr��
     * 
     * @return la valeur de la propri�t� idJournal
     */
    public String getIdJournal();

    /**
     * Renvoie la valeur de la propri�t� idRole, il s'agit de l'identifiant du r�le un r�le est par exemple un affili� <br>
     * idRole 517001 --> Assur�</br> <br>
     * idRole 517002 --> Affili�</br> <br>
     * idRole 517033 --> APG</br> <br>
     * idRole 517034 --> IJAI</br> <br>
     * idRole 517036 --> AF</br> <br>
     * idRole 517038 --> Rentier</br> <br>
     * idRole 517039 --> Affili� paritaire</br> <br>
     * idRole 517040 --> Affili� personnel</br> <br>
     * idRole 517041 --> Administrateur</br>
     * 
     * @return la valeur de la propri�t� idRole
     */
    public String getIdRole();

    /**
     * Renvoie la valeur de la propri�t� idTiers, il s'agit de l'identifiant du tiers du compte annexe
     * 
     * @return la valeur de la propri�t� idTiers
     */
    public String getIdTiers();

    /**
     * Renvoie la valeur de la propri�t� idTiers, il s'agit de l'identifiant du tiers du compte annexe
     * 
     * @return la valeur de la propri�t� idTiers
     */
    public String getIdTri();

    /**
     * Renvoie l'interface d�crivant un r�le au format d�finit par la comptabilit� auxiliaire
     * 
     * @return la valeur de la propri�t� role
     */
    public IntRole getRole();

    /**
     * Renvoie la valeur de la propri�t� solde, il s'agit du solde du compte annexe non format� par exemple 12526.30
     * 
     * @return la valeur de la propri�t� solde
     */
    public String getSolde();

    /**
     * Renvoie la valeur de la propri�t� soldeAt, il s'agit du solde du compte annexe jusqu'� la date pass�e en
     * param�tre (comprise)
     * 
     * @return la valeur de la propri�t� soldeAt
     * @param date
     *            date jusqu'� laquelle le solde doit �tre pris en compte
     */
    public String getSoldeAt(String date);

    /**
     * Renvoie la valeur de la propri�t� soldeFormate, il s'agit du solde du compte annexe format� pour l'affichage par
     * exemple 12'526.30
     * 
     * @return la valeur de la propri�t� soldeFormate
     */
    public String getSoldeFormate();

    /**
     * Renvoie la valeur de la propri�t� soldeInitialAt, il s'agit du solde du compte annexe jusqu'� la date pass�e en
     * param�tre (non comprise)
     * 
     * @return la valeur de la propri�t� soldeInitialAt
     * @param date
     *            date jusqu'� laquelle le solde doit �tre pris en compte
     */
    public String getSoldeInitialAt(String date, String forSelectionSections);

    /**
     * Renvoie l'interface d�crivant le tiers au format d�finit par la comptabilit� auxiliaire
     * 
     * @return la valeur de la propri�t� tiers
     */
    public IntTiers getTiers();

    /**
     * Renvoie la valeur de la propri�t� titulaireEntete, il s'agit du r�le selon la langue, du num�ro externe du r�le,
     * du nom et du lieu du tiers. Par exemple:<br>
     * <br>
     * Affili� 123.1002<br>
     * Mueller Jacques<br>
     * 1000 Lausanne<br>
     * <br>
     * 
     * @return la valeur de la propri�t� titulaireEntete
     */
    public String getTitulaireEntete();

    /**
     * Un motif de blocage (idModifBlocage) existe-t'il pour l'ann�e (year) pour ce compte annexe ?
     * 
     * @param idModifBlocage
     * @param year
     * @return
     * @throws CATechnicalException
     */
    public boolean hasMotifContentieuxForYear(String idModifBlocage, String year) throws CATechnicalException;

    /**
     * Indique si des �critures (op�rations) ont �t� pass�e pour ce compte annexe
     * 
     * @return true si des �critures (op�rations) ont �t� pass�es pour ce compte annexe
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
     * Retourne un ensemble de sections qui correspondent aux crit�res de compensation d�sir�s
     * 
     * @return java.util.Collection une collection de Sections � compenser
     * @param type
     *            java.lang.int type de compensation
     * @param ordre
     *            java.lang.int l'ordre dans lequel les factures doivent �tre compens�es
     * @param args
     *            java.lang.String les autres arguments d�pendant de la r�qle de compensation
     * @exception java.lang.Exception
     *                si �chec
     */
    public java.util.Collection propositionCompensation(int type, int ordre, String montantACompenser)
            throws java.lang.Exception;

    /**
     * Chargement d'un compte annexe chargement d'un compte annexe depuis la DB<br>
     * <br>
     * utiliser la methode setIdCompteAnnexe(String id) pr�alablement pour<br>
     * specifier le tiers � charger<br>
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
     * utiliser la methode setIdCompteAnnexe(String id) pr�alablement pour<br>
     * specifier le tiers � charger<br>
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
     * D�finit la valeur de la propri�t� alternateKey, permet de faire un retrieve sans conna�tre l'idCompteAnnexe<br>
     * Cl� altern� num�ro 1 : idRole et idExterneRole<br>
     * avant de faire un retrieve, il faut utiliser les m�thodes suivantes:<br>
     * setAlternateKey(AK_IDEXTERNE)<br>
     * setIdRole(String id)<br>
     * setIdExterneRole(String id)<br>
     * <br>
     * 
     * @param altKey
     *            altKey indique le type de cl� altern�e � utiliser
     */
    public void setAlternateKey(int altKey);

    /**
     * D�finit la valeur de la propri�t� contDateDebBloque
     * 
     * @param contDateDebBloque
     *            la nouvelle date de d�but de blocage
     */
    public void setContDateDebBloque(String contDateDebBloque);

    /**
     * D�finit la valeur de la propri�t� contDateFinBloque
     * 
     * @param contDateFinBloque
     *            la nouvelle date de fin de blocage
     */
    public void setContDateFinBloque(String contDateFinBloque);

    /**
     * D�finit la valeur de la propri�t� contEstBloque
     * 
     * @param contEstBloque
     *            la nouvelle valeur de blocage
     */
    public void setContEstBloque(Boolean contEstBloque);

    /**
     * D�finit la valeur de la propri�t� idCompteAnnexe
     * 
     * @param idCompteAnnexe
     *            le nouveau identifiant du compte annexe
     */
    public void setIdCompteAnnexe(String idCompteAnnexe);

    /**
     * D�finit la valeur de la propri�t� idContMotifBloque
     * 
     * @param idContMotifBloque
     *            le nouveau code du motif de blocage
     */
    public void setIdContMotifBloque(String idContMotifBloque);

    /**
     * D�finit la valeur de la propri�t� idExterneRole
     * 
     * @param idExterneRole
     *            le nouveau identifiant du role externe
     */
    public void setIdExterneRole(String idExterneRole);

    /**
     * D�finit la valeur de la propri�t� idRole <br>
     * idRole 517001 --> Assur�</br> <br>
     * idRole 517002 --> Affili�</br> <br>
     * idRole 517033 --> APG</br> <br>
     * idRole 517034 --> IJAI</br> <br>
     * idRole 517036 --> AF</br>
     * 
     * @param idRole
     *            le nouveau identifiant du role
     */
    public void setIdRole(String idRole);

    /**
     * D�finit la valeur de la propri�t� idTri
     * 
     * @param idTri
     *            la valeur du tri
     */
    public void setIdTri(String idTri);
}

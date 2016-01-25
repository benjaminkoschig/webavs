package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface des Affiliationd
 * 
 * @author David GIrardin
 */
public interface IAFAffiliation extends BIEntity {

    /**
     * Cl� de recherche par N� d'affili�
     */
    public static final String FIND_FOR_AFFILIATIONID = "setForAffiliationId";

    /**
     * Cl� de recherche par id Tiers
     */
    public static final String FIND_FOR_ENTREDATE = "setForEntreDate";

    /**
     * Cl� de recherche par id Tiers
     */
    public static final String FIND_FOR_IDTIERS = "setForIdTiers";

    /**
     * Cl� de recherche par type d'affiliation
     */
    public static final String FIND_FOR_LISTTYPEAFFILIATION = "setForListTypeAffiliation";
    /**
     * Cl� de recherche par N� d'affili�
     */
    public static final String FIND_FOR_NOAFFILIE = "setForAffilieNumero";

    /**
     * Cl� de recherche depuis num�ro d'affili�
     */
    public static final String FIND_FROM_NOAFFILIE = "setFromAffilieNumero";

    /**
     * Cl� de recherche depuis un nom
     */
    public static final String FIND_FROM_NOM = "setFromNom";

    /**
     * Cl� de recherche par N� d'affili� (like)
     */
    public static final String FIND_LIKE_AFFILIE = "setLikeAffilieNumero";
    /**
     * Motif de cr�ation "changement de nom"
     */
    public final static String MOTIF_CREATION_CHANGEMENT_DE_NOM = "816003";
    /**
     * Motif de fin "changement de nom"
     */
    public final static String MOTIF_FIN_CHANGEMENT_DE_NOM = "803005";

    /**
     * Type d'affiliation employeur
     */
    public final static String TYPE_AFFILI_EMPLOY = "804002";

    /**
     * Type d'affiliation employeur d/F
     */
    public final static String TYPE_AFFILI_EMPLOY_D_F = "804012";
    /**
     * Type d'affiliation Ind�pendant
     */
    public final static String TYPE_AFFILI_INDEP = "804001";
    /**
     * Type d'affiliation ind�pendant et employeur
     */
    public final static String TYPE_AFFILI_INDEP_EMPLOY = "804005";
    public static final String TYPE_AFFILI_LTN = "804010";
    /**
     * Type d'affiliation non actif
     */
    public final static String TYPE_AFFILI_NON_ACTIF = "804004";
    /**
     * Type d'affiliation provisoire
     */
    public final static String TYPE_AFFILI_PROVIS = "804007";
    /**
     * Type d'affiliation
     */
    public final static String TYPE_AFFILI_SELON_ART_1A = "804006";

    /**
     * Type d'affiliation soci�t� immobiliaire
     */
    public final static String TYPE_AFFILI_SOCI_IMMOB = "804009";
    /**
     * Type d'affiliation
     */
    public final static String TYPE_AFFILI_TSE = "804008";
    /**
     * Type d'affiliation
     */
    public final static String TYPE_AFFILI_TSE_VOLONTAIRE = "804011";

    /**
     * Type d'affiliation paritaires
     */
    public final static String TYPES_AFFILI_PARITAIRES = IAFAffiliation.TYPE_AFFILI_EMPLOY + ","
            + IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY + "," + IAFAffiliation.TYPE_AFFILI_EMPLOY_D_F + ","
            + IAFAffiliation.TYPE_AFFILI_LTN;

    /**
     * Type d'affiliation personnels
     */
    public final static String TYPES_AFFILI_PERSONNELLES = IAFAffiliation.TYPE_AFFILI_INDEP + ","
            + IAFAffiliation.TYPE_AFFILI_NON_ACTIF + "," + IAFAffiliation.TYPE_AFFILI_SELON_ART_1A + ","
            + IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY + "," + IAFAffiliation.TYPE_AFFILI_TSE + ","
            + IAFAffiliation.TYPE_AFFILI_TSE_VOLONTAIRE;

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des affiliations.<br>
     * <br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
     * <li>FIND_FOR_IDTIERS</li>
     * <li>FIND_FROM_NOM</li>
     * </ul>
     * </p>
     * 100 enregistrements au max sont retourn�s<br>
     * <br>
     * <br>
     * 
     * @return Object[] tableau d'objets, vide si aucune occurence n'est trouv�e
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau d'ffiliations.<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_NOAFFILIE<br> <li>FIND_FOR_IDTIERS<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retourn�s<br>
     * <br>
     * <br>
     * 
     * @return AFAffiliation[] tableau d'affiliation trouv�e, vie si aucun occurence n'est trouv�e
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public IAFAffiliation[] findAffiliation(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau d'ffiliations.<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivan te : <br>
     * <br>
     * <li>FIND_FOR_NOAFFILIE<br> <li>FIND_FOR_IDTIERS<br> <li>
     * FIND_FOR_ENTREDATE<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retourn�s<br>
     * <br>
     * <br>
     * 
     * @return AFAffiliation[] tableau d'affiliation trouv�e, vie si aucun occurence n'est trouv�e
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public IAFAffiliation[] findAffiliationAF(Hashtable params) throws Exception;

    /**
     * Indique si l'affili� donn� est valide pour les prestations AF
     * 
     * @param date
     *            date � laquelle le test doit s'effectuer. Renvoie false si non rensign�e
     * @param typeAllocataire
     *            type d'allocataire (salari�,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return true si l'affili� donn�es est valide, false sinon
     * @exception exception
     *                si un erreur survient lors de la recherche
     */
    public Boolean getActifAF(String date, String typeAllocataire) throws Exception;

    /**
     * Retourne l'affiliation � laquelle les cotisations AF sont factur�es.<br>
     * Si l'affiliation donn�e en param�tre contient une cotisation AF (AF factur�s), cette m�me affilition sera
     * retourn�e.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est factur� � la maison m�re, le lien de type
     * "Succursale de" est recherch� afin de retrouver l'affiliation maison m�re.<br>
     * Dans le cas o� le lien "Succursale de" n'est pas applicable, il est �galement possible d'utiliser le type de lien
     * "D�compte AF sous" que cette m�thode tentera �galement d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param date
     *            la date utilis�e pour la recherche.
     * @return l'affiliation � laquelle les cotisations AF sont factur�es ou null si inconnu.
     * @exception exception
     *                si une erreur survient lors de la recherche.
     */
    public IAFAffiliation getAffiliationFacturationAF(String date) throws Exception;

    /**
     * Retourne l'affiliation � laquelle les cotisations AF sont factur�es.<br>
     * Si l'affiliation donn�e en param�tre contient une cotisation AF (AF factur�s), cette m�me affilition sera
     * retourn�e.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est factur� � la maison m�re, le lien de type
     * "Succursale de" est recherch� afin de retrouver l'affiliation maison m�re.<br>
     * Dans le cas o� le lien "Succursale de" n'est pas applicable, il est �galement possible d'utiliser le type de lien
     * "D�compte AF sous" que cette m�thode tentera �galement d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param date
     *            la date utilis�e pour la recherche.
     * @param typeAllocataire
     *            type d'allocataire (salari�,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return l'affiliation � laquelle les cotisations AF sont factur�es ou null si inconnu.
     * @exception exception
     *                si une erreur survient lors de la recherche.
     */
    public IAFAffiliation getAffiliationFacturationAF(String date, String typeAllocataire) throws Exception;

    /**
     * Renvoie l'id de l'affiliation.
     * 
     * @return id de l'affiliation
     */
    public java.lang.String getAffiliationId();

    /**
     * Renvoie le num�ro format� de l'affiliation.
     * 
     * @return le num�ro format� de l'affiliation
     */
    public java.lang.String getAffilieNumero();

    /**
     * Retourne l'agence communale Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du genre de l'assurance (AVS, AF, etc)
     */
    public String getAgenceCom(String IdAffiliation, String dateJour) throws Exception;

    /**
     * Retourne le num de l'agence communale Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du genre de l'assurance (AVS, AF, etc)
     */
    public String getAgenceComNum(String IdAffiliation, String dateJour) throws Exception;

    /**
     * Renvoie l'ancien num�ro de cette l'affiliation.
     * 
     * @return l'ancien num�ro de cette l'affiliation
     */
    public java.lang.String getAncienAffilieNumero();

    /**
     * Renvoie l'information sur le bonus/malus.
     * 
     * @return l'information sur le bonus/malus
     */
    public java.lang.Boolean getBonusMalus();

    /**
     * Renvoie la branche �conomique.
     * 
     * @return la branche �conomique
     */
    public java.lang.String getBrancheEconomique();

    /**
     * Renvoie le num�ro de la caisse de partance.
     * 
     * @return le num�ro de la caisse de partance
     */
    public java.lang.String getCaissePartance();

    /**
     * Renvoie le num�ro de la caisse de provenance.
     * 
     * @return le num�ro de la caisse de provenance
     */
    public java.lang.String getCaisseProvenance();

    /**
     * Retourne le canton d�fini pour les AF. Si le canton est sp�cifi� dans la cotisation AF, celui-ci est retourn�.
     * Sinon, c'est le canton de l'adresse de l'affili� qui est utilis�
     * 
     * @param date
     *            la date utilis�e pour la recherche
     * @return le canton d�fini pour les AF ou vide si non d�fini
     * @throws Exception
     *             si une erreur survient lors de la recherche.
     */
    public String getCantonAF(String date) throws Exception;

    /**
     * Renvoie le code Noga.
     * 
     * @return le code Noga
     */
    public java.lang.String getCodeNoga();

    /**
     * Renvoie la date de cr�ation de l'affiliation au format jj.mm.aaaa.
     * 
     * @return la date de cr�ation de l'affiliation
     */
    public java.lang.String getDateCreation();

    /**
     * Renvoie la date de d�but d'affiliation au format jj.mm.aaaa.
     * 
     * @return la date de d�but d'affiliation
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin d'affiliation au format jj.mm.aaaa.
     * 
     * @return la date de d�but d'affiliation
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie le type de d�claration de salaire.
     * 
     * @return le type de d�claration de salaire
     */
    public java.lang.String getDeclarationSalaire();

    /**
     * Renvoie l'id du Tiers li�.
     * 
     * @return l'id du Tiers li�
     */
    public java.lang.String getIdTiers();

    /**
     * Renvoie la masse de salaire annuelle.
     * 
     * @return la masse de salaire annuelle
     */
    public java.lang.String getMasseAnnuelle();

    /**
     * Renvoie la masse de salaire en fonction de la p�riodicit�.
     * 
     * @return la masse de salaire en fonction de la p�riodicit�
     */
    public java.lang.String getMassePeriodicite();

    /**
     * Renvoie l'association dont l'affili� est membre.
     * 
     * @return l'association dont l'affili� est membre
     */
    public java.lang.String getMembreAssociation();

    /**
     * Renvoie le comit� dont l'affili� est membre.
     * 
     * @return le comit� dont l'affili� est membre
     */
    public java.lang.String getMembreComite();

    /**
     * Renvoie le motif de cr�ation.
     * 
     * @return le motif de cr�ation
     */
    public java.lang.String getMotifCreation();

    /**
     * Renvoie le motif de fin d'affiliation.
     * 
     * @return le motif de fin d'affiliation
     */
    public java.lang.String getMotifFin();

    /**
     * Renvoie le num�ro f�d�ral de l'entreprise.
     * 
     * @return le num�ro f�d�ral de l'entreprise
     */
    public java.lang.String getNumeroIDE();

    /**
     * Renvoie le statut du num�ro f�d�ral de l'entreprise.
     * 
     * @return le statut du num�ro f�d�ral de l'entreprise
     */
    public java.lang.String getIdeStatut();

    /**
     * Renvoie la p�riodicit� de l'affiliation.
     * 
     * @return la p�riodicit� de l'affiliation
     */
    public java.lang.String getPeriodicite();

    /**
     * Renvoie la personnalit� juridique.
     * 
     * @return la personnalit� juridique
     */
    public java.lang.String getPersonnaliteJuridique();

    /**
     * Renvoie la raison sociale de l'affili�
     * 
     * @return la raison sociale
     */
    public String getRaisonSociale();

    /**
     * Renvoie la raison sociale courte de l'affili�
     * 
     * @return la raison sociale courte
     */
    public String getRaisonSocialeCourt();

    /**
     * Renvoie si l'affiliation est factur� au d�compte exact
     * 
     * @return true si l'affiliation est factur� au d�compte exact
     */
    public java.lang.Boolean getReleveParitaire();

    /**
     * Renvoie le nom du tiers.
     * 
     * @return le nom du tiers
     */
    public java.lang.String getTiersNom();

    /**
     * Renvoie si l'affiliation doit �tre tra�t�e ou non (affiliation provisoire).
     * 
     * @return true si l'affiliation ne doit pas �tre tra�t�e (affiliation provisoire)
     */
    public java.lang.Boolean getTraitement();

    /**
     * Renvoie le type de l'affilaition. Se r�f�rer aux {@link #TYPE_AFFILI_INDEP types d'affiliations}
     * 
     * @return le type de l'affiliation
     */
    public java.lang.String getTypeAffiliation();

    /**
     * Renvoie le type d'associ�.
     * 
     * @return le type d'associ�
     */
    public java.lang.String getTypeAssocie();

    /**
     * Recuperer l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la recuperation a �chou�
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * D�finit l'id de l'affiliation.
     * 
     * @param newAffiliationId
     *            id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * D�finit le num�ro format� de l'affiliation.
     * 
     * @param newAffilieNumero
     *            le num�ro format� de l'affiliation
     */
    public void setAffilieNumero(java.lang.String newAffilieNumero);

    /**
     * D�finit l'ancien num�ro de cette l'affiliation.
     * 
     * @param newAffilieNumeroAncien
     *            l'ancien num�ro de cette l'affiliation
     */
    public void setAffilieNumeroAncien(java.lang.String newAffilieNumeroAncien);

    /**
     * D�finit l'information sur le bonus/malus.
     * 
     * @param newBonusMalus
     *            l'information sur le bonus/malus
     */
    public void setBonusMalus(java.lang.Boolean newBonusMalus);

    /**
     * D�finit la personnalit� juridique.
     * 
     * @param newBrancheEconomique
     *            la personnalit� juridique
     */
    public void setBrancheEconomique(java.lang.String newBrancheEconomique);

    /**
     * D�finit le num�ro de la caisse de partance.
     * 
     * @param newCaissePartance
     *            le num�ro de la caisse de partance
     */
    public void setCaissePartance(java.lang.String newCaissePartance);

    /**
     * D�finit le num�ro de la caisse de provenance.
     * 
     * @param newCaisseProvenance
     *            le num�ro de la caisse de provenance
     */
    public void setCaisseProvenance(java.lang.String newCaisseProvenance);

    /**
     * D�finit le code Noga.
     * 
     * @param newCodeNoga
     *            le code Noga
     */
    public void setCodeNoga(java.lang.String newCodeNoga);

    /**
     * D�finit la date de cr�ation de l'affiliation au format jj.mm.aaaa.
     * 
     * @param newDateCreation
     *            la date de cr�ation de l'affiliation
     */
    public void setDateCreation(java.lang.String newDateCreation);

    /**
     * D�finit la date de d�but d'affiliation au format jj.mm.aaaa.
     * 
     * @param newDateDebut
     *            la date de d�but d'affiliation
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la date de fin d'affiliation au format jj.mm.aaaa.
     * 
     * @param newDateFin
     *            la date de d�but d'affiliation
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * D�finit la p�riodicit� de l'affiliation.
     * 
     * @param newDeclarationSalaire
     *            la p�riodicit� de l'affiliation
     */
    public void setDeclarationSalaire(java.lang.String newDeclarationSalaire);

    /**
     * D�finit l'id du Tiers li�.
     * 
     * @param newIdTiers
     *            l'id du Tiers li�
     */
    public void setIdTiers(java.lang.String newIdTiers);

    /**
     * D�finit la masse de salaire annuelle.
     * 
     * @param newMasseAnnuelle
     *            la masse de salaire annuelle
     */
    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle);

    /**
     * D�finit la masse de salaire en fonction de la p�riodicit�.
     * 
     * @param newMassePeriodicite
     *            la masse de salaire en fonction de la p�riodicit�
     */
    public void setMassePeriodicite(java.lang.String newMassePeriodicite);

    /**
     * D�finit l'association dont l'affili� est membre.
     * 
     * @param newMembreAssociation
     *            l'association dont l'affili� est membre
     */
    public void setMembreAssociation(java.lang.String newMembreAssociation);

    /**
     * D�finit le comit� dont l'affili� est membre.
     * 
     * @param newMembreComite
     *            le comit� dont l'affili� est membre
     */
    public void setMembreComite(java.lang.String newMembreComite);

    /**
     * D�finit le motif de cr�ation.
     * 
     * @param newMotifCreation
     *            le motif de cr�ation
     */
    public void setMotifCreation(java.lang.String newMotifCreation);

    /**
     * D�finit la branche �conomique.
     * 
     * @param newMotifFin
     *            la branche �conomique
     */
    public void setMotifFin(java.lang.String newMotifFin);

    /**
     * D�finit le num�ro f�d�ral de l'entreprise.
     * 
     * @param newNumeroIDE
     *            le num�ro f�d�ral de l'entreprise
     */
    public void setNumeroIDE(java.lang.String newNumeroIDE);

    /**
     * D�finit le statut du num�ro f�d�ral de l'entreprise.
     * 
     * @param newNumeroIDE
     *            le statut du num�ro f�d�ral de l'entreprise
     */
    public void setIdeStatut(java.lang.String newIdeStatut);

    /**
     * D�finit la masse de salaire annuelle.
     * 
     * @param newPeriodicite
     *            la masse de salaire annuelle
     */
    public void setPeriodicite(java.lang.String newPeriodicite);

    /**
     * D�finit la personnalit� juridique.
     * 
     * @param newPersonnaliteJuridique
     *            la personnalit� juridique
     */
    public void setPersonnaliteJuridique(java.lang.String newPersonnaliteJuridique);

    /**
     * D�finit la raison sociale de l'affili�
     * 
     * @param string
     *            la raison sociale de l'affili�
     */
    public void setRaisonSociale(String string);

    /**
     * D�finit la raison sociale courte de l'affili�
     * 
     * @param string
     *            la raison sociale courte de l'affili�
     */
    public void setRaisonSocialeCourt(String string);

    /**
     * D�finit si l'affili� est factur� au d�compte exact
     * 
     * @param string
     *            la raison sociale de l'affili�
     */
    public void setReleveParitaire(Boolean bool);

    /**
     * D�finit le motif de cr�ation.
     * 
     * @param newMotifCreation
     *            le motif de cr�ation
     */
    public void setTiersNom(java.lang.String newTiersNom);

    /**
     * D�finit le type de l'affilaition. Se r�f�rer aux {@link #TYPE_AFFILI_INDEP types d'affiliations}
     * 
     * @param newTypeAffiliation
     *            le type de l'affiliation
     */
    public void setTypeAffiliation(java.lang.String newTypeAffiliation);

    /**
     * D�finit le type de l'associ�.
     * 
     * @param newTypeAssocie
     *            le type de l'associ�
     */
    public void setTypeAssocie(java.lang.String newTypeAssocie);

    /**
     * Mise � jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la mise � jour a �chou�
     */
    public void update(BITransaction transaction) throws Exception;
}

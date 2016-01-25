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
     * Clé de recherche par N° d'affilié
     */
    public static final String FIND_FOR_AFFILIATIONID = "setForAffiliationId";

    /**
     * Clé de recherche par id Tiers
     */
    public static final String FIND_FOR_ENTREDATE = "setForEntreDate";

    /**
     * Clé de recherche par id Tiers
     */
    public static final String FIND_FOR_IDTIERS = "setForIdTiers";

    /**
     * Clé de recherche par type d'affiliation
     */
    public static final String FIND_FOR_LISTTYPEAFFILIATION = "setForListTypeAffiliation";
    /**
     * Clé de recherche par N° d'affilié
     */
    public static final String FIND_FOR_NOAFFILIE = "setForAffilieNumero";

    /**
     * Clé de recherche depuis numéro d'affilié
     */
    public static final String FIND_FROM_NOAFFILIE = "setFromAffilieNumero";

    /**
     * Clé de recherche depuis un nom
     */
    public static final String FIND_FROM_NOM = "setFromNom";

    /**
     * Clé de recherche par N° d'affilié (like)
     */
    public static final String FIND_LIKE_AFFILIE = "setLikeAffilieNumero";
    /**
     * Motif de création "changement de nom"
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
     * Type d'affiliation Indépendant
     */
    public final static String TYPE_AFFILI_INDEP = "804001";
    /**
     * Type d'affiliation indépendant et employeur
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
     * Type d'affiliation société immobiliaire
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
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des affiliations.<br>
     * <br>
     * <p>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche dans la liste suivante : <br>
     * <ul>
     * <li>FIND_FOR_AFFILIATION_ID</li>
     * <li>FIND_FOR_IDTIERS</li>
     * <li>FIND_FROM_NOM</li>
     * </ul>
     * </p>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return Object[] tableau d'objets, vide si aucune occurence n'est trouvée
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
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_NOAFFILIE<br> <li>FIND_FOR_IDTIERS<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return AFAffiliation[] tableau d'affiliation trouvée, vie si aucun occurence n'est trouvée
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
     * critières de recherche ds la liste suivan te : <br>
     * <br>
     * <li>FIND_FOR_NOAFFILIE<br> <li>FIND_FOR_IDTIERS<br> <li>
     * FIND_FOR_ENTREDATE<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return AFAffiliation[] tableau d'affiliation trouvée, vie si aucun occurence n'est trouvée
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public IAFAffiliation[] findAffiliationAF(Hashtable params) throws Exception;

    /**
     * Indique si l'affilié donné est valide pour les prestations AF
     * 
     * @param date
     *            date à laquelle le test doit s'effectuer. Renvoie false si non rensignée
     * @param typeAllocataire
     *            type d'allocataire (salarié,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return true si l'affilié données est valide, false sinon
     * @exception exception
     *                si un erreur survient lors de la recherche
     */
    public Boolean getActifAF(String date, String typeAllocataire) throws Exception;

    /**
     * Retourne l'affiliation à laquelle les cotisations AF sont facturées.<br>
     * Si l'affiliation donnée en paramètre contient une cotisation AF (AF facturés), cette même affilition sera
     * retournée.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est facturé à la maison mère, le lien de type
     * "Succursale de" est recherché afin de retrouver l'affiliation maison mère.<br>
     * Dans le cas où le lien "Succursale de" n'est pas applicable, il est également possible d'utiliser le type de lien
     * "Décompte AF sous" que cette méthode tentera également d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param date
     *            la date utilisée pour la recherche.
     * @return l'affiliation à laquelle les cotisations AF sont facturées ou null si inconnu.
     * @exception exception
     *                si une erreur survient lors de la recherche.
     */
    public IAFAffiliation getAffiliationFacturationAF(String date) throws Exception;

    /**
     * Retourne l'affiliation à laquelle les cotisations AF sont facturées.<br>
     * Si l'affiliation donnée en paramètre contient une cotisation AF (AF facturés), cette même affilition sera
     * retournée.<br>
     * Par contre, si la cotisation AF contient l'indication qu'elle est facturé à la maison mère, le lien de type
     * "Succursale de" est recherché afin de retrouver l'affiliation maison mère.<br>
     * Dans le cas où le lien "Succursale de" n'est pas applicable, il est également possible d'utiliser le type de lien
     * "Décompte AF sous" que cette méthode tentera également d'utiliser pour rechercher l'affiliation de facturation.
     * 
     * @param date
     *            la date utilisée pour la recherche.
     * @param typeAllocataire
     *            type d'allocataire (salarié,non actif,paysan,collaborateur agricole, travailleur agricole)
     * @return l'affiliation à laquelle les cotisations AF sont facturées ou null si inconnu.
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
     * Renvoie le numéro formaté de l'affiliation.
     * 
     * @return le numéro formaté de l'affiliation
     */
    public java.lang.String getAffilieNumero();

    /**
     * Retourne l'agence communale Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du genre de l'assurance (AVS, AF, etc)
     */
    public String getAgenceCom(String IdAffiliation, String dateJour) throws Exception;

    /**
     * Retourne le num de l'agence communale Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du genre de l'assurance (AVS, AF, etc)
     */
    public String getAgenceComNum(String IdAffiliation, String dateJour) throws Exception;

    /**
     * Renvoie l'ancien numéro de cette l'affiliation.
     * 
     * @return l'ancien numéro de cette l'affiliation
     */
    public java.lang.String getAncienAffilieNumero();

    /**
     * Renvoie l'information sur le bonus/malus.
     * 
     * @return l'information sur le bonus/malus
     */
    public java.lang.Boolean getBonusMalus();

    /**
     * Renvoie la branche économique.
     * 
     * @return la branche économique
     */
    public java.lang.String getBrancheEconomique();

    /**
     * Renvoie le numéro de la caisse de partance.
     * 
     * @return le numéro de la caisse de partance
     */
    public java.lang.String getCaissePartance();

    /**
     * Renvoie le numéro de la caisse de provenance.
     * 
     * @return le numéro de la caisse de provenance
     */
    public java.lang.String getCaisseProvenance();

    /**
     * Retourne le canton défini pour les AF. Si le canton est spécifié dans la cotisation AF, celui-ci est retourné.
     * Sinon, c'est le canton de l'adresse de l'affilié qui est utilisé
     * 
     * @param date
     *            la date utilisée pour la recherche
     * @return le canton défini pour les AF ou vide si non défini
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
     * Renvoie la date de création de l'affiliation au format jj.mm.aaaa.
     * 
     * @return la date de création de l'affiliation
     */
    public java.lang.String getDateCreation();

    /**
     * Renvoie la date de début d'affiliation au format jj.mm.aaaa.
     * 
     * @return la date de début d'affiliation
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin d'affiliation au format jj.mm.aaaa.
     * 
     * @return la date de début d'affiliation
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie le type de déclaration de salaire.
     * 
     * @return le type de déclaration de salaire
     */
    public java.lang.String getDeclarationSalaire();

    /**
     * Renvoie l'id du Tiers lié.
     * 
     * @return l'id du Tiers lié
     */
    public java.lang.String getIdTiers();

    /**
     * Renvoie la masse de salaire annuelle.
     * 
     * @return la masse de salaire annuelle
     */
    public java.lang.String getMasseAnnuelle();

    /**
     * Renvoie la masse de salaire en fonction de la périodicité.
     * 
     * @return la masse de salaire en fonction de la périodicité
     */
    public java.lang.String getMassePeriodicite();

    /**
     * Renvoie l'association dont l'affilié est membre.
     * 
     * @return l'association dont l'affilié est membre
     */
    public java.lang.String getMembreAssociation();

    /**
     * Renvoie le comité dont l'affilié est membre.
     * 
     * @return le comité dont l'affilié est membre
     */
    public java.lang.String getMembreComite();

    /**
     * Renvoie le motif de création.
     * 
     * @return le motif de création
     */
    public java.lang.String getMotifCreation();

    /**
     * Renvoie le motif de fin d'affiliation.
     * 
     * @return le motif de fin d'affiliation
     */
    public java.lang.String getMotifFin();

    /**
     * Renvoie le numéro fédéral de l'entreprise.
     * 
     * @return le numéro fédéral de l'entreprise
     */
    public java.lang.String getNumeroIDE();

    /**
     * Renvoie le statut du numéro fédéral de l'entreprise.
     * 
     * @return le statut du numéro fédéral de l'entreprise
     */
    public java.lang.String getIdeStatut();

    /**
     * Renvoie la périodicité de l'affiliation.
     * 
     * @return la périodicité de l'affiliation
     */
    public java.lang.String getPeriodicite();

    /**
     * Renvoie la personnalité juridique.
     * 
     * @return la personnalité juridique
     */
    public java.lang.String getPersonnaliteJuridique();

    /**
     * Renvoie la raison sociale de l'affilié
     * 
     * @return la raison sociale
     */
    public String getRaisonSociale();

    /**
     * Renvoie la raison sociale courte de l'affilié
     * 
     * @return la raison sociale courte
     */
    public String getRaisonSocialeCourt();

    /**
     * Renvoie si l'affiliation est facturé au décompte exact
     * 
     * @return true si l'affiliation est facturé au décompte exact
     */
    public java.lang.Boolean getReleveParitaire();

    /**
     * Renvoie le nom du tiers.
     * 
     * @return le nom du tiers
     */
    public java.lang.String getTiersNom();

    /**
     * Renvoie si l'affiliation doit être traîtée ou non (affiliation provisoire).
     * 
     * @return true si l'affiliation ne doit pas être traîtée (affiliation provisoire)
     */
    public java.lang.Boolean getTraitement();

    /**
     * Renvoie le type de l'affilaition. Se référer aux {@link #TYPE_AFFILI_INDEP types d'affiliations}
     * 
     * @return le type de l'affiliation
     */
    public java.lang.String getTypeAffiliation();

    /**
     * Renvoie le type d'associé.
     * 
     * @return le type d'associé
     */
    public java.lang.String getTypeAssocie();

    /**
     * Recuperer l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la recuperation a échoué
     */
    public void retrieve(BITransaction transaction) throws Exception;

    /**
     * Définit l'id de l'affiliation.
     * 
     * @param newAffiliationId
     *            id de l'affiliation
     */
    public void setAffiliationId(java.lang.String newAffiliationId);

    /**
     * Définit le numéro formaté de l'affiliation.
     * 
     * @param newAffilieNumero
     *            le numéro formaté de l'affiliation
     */
    public void setAffilieNumero(java.lang.String newAffilieNumero);

    /**
     * Définit l'ancien numéro de cette l'affiliation.
     * 
     * @param newAffilieNumeroAncien
     *            l'ancien numéro de cette l'affiliation
     */
    public void setAffilieNumeroAncien(java.lang.String newAffilieNumeroAncien);

    /**
     * Définit l'information sur le bonus/malus.
     * 
     * @param newBonusMalus
     *            l'information sur le bonus/malus
     */
    public void setBonusMalus(java.lang.Boolean newBonusMalus);

    /**
     * Définit la personnalité juridique.
     * 
     * @param newBrancheEconomique
     *            la personnalité juridique
     */
    public void setBrancheEconomique(java.lang.String newBrancheEconomique);

    /**
     * Définit le numéro de la caisse de partance.
     * 
     * @param newCaissePartance
     *            le numéro de la caisse de partance
     */
    public void setCaissePartance(java.lang.String newCaissePartance);

    /**
     * Définit le numéro de la caisse de provenance.
     * 
     * @param newCaisseProvenance
     *            le numéro de la caisse de provenance
     */
    public void setCaisseProvenance(java.lang.String newCaisseProvenance);

    /**
     * Définit le code Noga.
     * 
     * @param newCodeNoga
     *            le code Noga
     */
    public void setCodeNoga(java.lang.String newCodeNoga);

    /**
     * Définit la date de création de l'affiliation au format jj.mm.aaaa.
     * 
     * @param newDateCreation
     *            la date de création de l'affiliation
     */
    public void setDateCreation(java.lang.String newDateCreation);

    /**
     * Définit la date de début d'affiliation au format jj.mm.aaaa.
     * 
     * @param newDateDebut
     *            la date de début d'affiliation
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la date de fin d'affiliation au format jj.mm.aaaa.
     * 
     * @param newDateFin
     *            la date de début d'affiliation
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * Définit la périodicité de l'affiliation.
     * 
     * @param newDeclarationSalaire
     *            la périodicité de l'affiliation
     */
    public void setDeclarationSalaire(java.lang.String newDeclarationSalaire);

    /**
     * Définit l'id du Tiers lié.
     * 
     * @param newIdTiers
     *            l'id du Tiers lié
     */
    public void setIdTiers(java.lang.String newIdTiers);

    /**
     * Définit la masse de salaire annuelle.
     * 
     * @param newMasseAnnuelle
     *            la masse de salaire annuelle
     */
    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle);

    /**
     * Définit la masse de salaire en fonction de la périodicité.
     * 
     * @param newMassePeriodicite
     *            la masse de salaire en fonction de la périodicité
     */
    public void setMassePeriodicite(java.lang.String newMassePeriodicite);

    /**
     * Définit l'association dont l'affilié est membre.
     * 
     * @param newMembreAssociation
     *            l'association dont l'affilié est membre
     */
    public void setMembreAssociation(java.lang.String newMembreAssociation);

    /**
     * Définit le comité dont l'affilié est membre.
     * 
     * @param newMembreComite
     *            le comité dont l'affilié est membre
     */
    public void setMembreComite(java.lang.String newMembreComite);

    /**
     * Définit le motif de création.
     * 
     * @param newMotifCreation
     *            le motif de création
     */
    public void setMotifCreation(java.lang.String newMotifCreation);

    /**
     * Définit la branche économique.
     * 
     * @param newMotifFin
     *            la branche économique
     */
    public void setMotifFin(java.lang.String newMotifFin);

    /**
     * Définit le numéro fédéral de l'entreprise.
     * 
     * @param newNumeroIDE
     *            le numéro fédéral de l'entreprise
     */
    public void setNumeroIDE(java.lang.String newNumeroIDE);

    /**
     * Définit le statut du numéro fédéral de l'entreprise.
     * 
     * @param newNumeroIDE
     *            le statut du numéro fédéral de l'entreprise
     */
    public void setIdeStatut(java.lang.String newIdeStatut);

    /**
     * Définit la masse de salaire annuelle.
     * 
     * @param newPeriodicite
     *            la masse de salaire annuelle
     */
    public void setPeriodicite(java.lang.String newPeriodicite);

    /**
     * Définit la personnalité juridique.
     * 
     * @param newPersonnaliteJuridique
     *            la personnalité juridique
     */
    public void setPersonnaliteJuridique(java.lang.String newPersonnaliteJuridique);

    /**
     * Définit la raison sociale de l'affilié
     * 
     * @param string
     *            la raison sociale de l'affilié
     */
    public void setRaisonSociale(String string);

    /**
     * Définit la raison sociale courte de l'affilié
     * 
     * @param string
     *            la raison sociale courte de l'affilié
     */
    public void setRaisonSocialeCourt(String string);

    /**
     * Définit si l'affilié est facturé au décompte exact
     * 
     * @param string
     *            la raison sociale de l'affilié
     */
    public void setReleveParitaire(Boolean bool);

    /**
     * Définit le motif de création.
     * 
     * @param newMotifCreation
     *            le motif de création
     */
    public void setTiersNom(java.lang.String newTiersNom);

    /**
     * Définit le type de l'affilaition. Se référer aux {@link #TYPE_AFFILI_INDEP types d'affiliations}
     * 
     * @param newTypeAffiliation
     *            le type de l'affiliation
     */
    public void setTypeAffiliation(java.lang.String newTypeAffiliation);

    /**
     * Définit le type de l'associé.
     * 
     * @param newTypeAssocie
     *            le type de l'associé
     */
    public void setTypeAssocie(java.lang.String newTypeAssocie);

    /**
     * Mise à jour de l'enregistrement dans la DB.
     * 
     * @param transaction
     *            la transaction a utiliser
     * @throws Exception
     *             si la mise é jour a échoué
     */
    public void update(BITransaction transaction) throws Exception;
}

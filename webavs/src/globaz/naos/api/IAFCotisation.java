package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface sur les cotisation d'une affiliation. Date de création : (28.05.2002 09:11:43)
 * 
 * @author David Girardin
 */
public interface IAFCotisation extends BIEntity {

    /**
     * Clé de recherche par Affiliation
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";
    /**
     * Clé de recherche par plan d'affiliation
     */
    public static final String FIND_FOR_PLAN_AFFILIATION_ID = "setForPlanAffiliationId";
    /**
     * inclu les inactifs dans la recherche Mette la chaine de caractère "true" comme valeur pour include les inactifs
     */
    public static final String FIND_SHOW_INACTIF = "setShowInactif";
    /** Motif de fin: aucun */
    public final static String MOTIF_FIN_AUCUN = "0";

    /** Motif de fin: divers */
    public final static String MOTIF_FIN_DIV = "803003";
    /** Motif de fin: fin de couverture */
    public final static String MOTIF_FIN_FIN_COUV_ASSURANCE = "803035";
    /** Motif de fin: retraite */
    public final static String MOTIF_FIN_RETRAITE = "803008";
    /** Périodicité annuelle */
    public final static String PERIODICITE_ANNUELLE = "802004";

    /** Périodicité mensuelle */
    public final static String PERIODICITE_MENSUELLE = "802001";

    /** Périodicité semestrielle */
    public final static String PERIODICITE_SEMESTRIELLE = "802003";

    /** Périodicité trimestrielle */
    public final static String PERIODICITE_TRIMESTRIELLE = "802002";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des cotisations<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return Object[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public Object[] find(Hashtable params) throws Exception;

    /**
     * Renvoie un tableau de cotisations<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * critières de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retournés<br>
     * <br>
     * <br>
     * 
     * @return Object[]
     * @param params
     *            params
     * @exception Exception
     *                si echec
     */
    public IAFCotisation[] findCotisations(Hashtable params) throws Exception;

    /**
     * Retourne l'id de l'adhésion liée Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'adhésion liée
     */
    public java.lang.String getAdhesionId();

    /**
     * Retourne si cet id Affiliation est affilié au AF pour les dates données Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du genre de l'assurance (AVS, AF, etc)
     */
    public String getAffilieAF(String IdAffiliation, String dateDebut, String dateFin) throws Exception;

    /**
     * Retourne l'id de l'assurance liée Date de création : (26.02.2003 09:54:44)
     * 
     * @return id de l'assurance liée
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne le canton de l'affilié Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du canton
     */
    public String getCantonAffilie(String IdAffiliation) throws Exception;

    /**
     * Retourne le libellé court de la cotisation AF correspondant à l'affilié
     * 
     * @param IdAffiliation
     *            identifiant de l'affiliation
     * @param dateValidite
     *            date de validité
     * @return libellé court correspondant à la cotisations AF trouvée
     * @throws Exception
     *             si plus d'une cotisation AF trouvée si aucune cotisation AF trouvée si l'affilié n'est pas de type
     *             employeur
     * 
     */
    public String getCotisationAF(String IdAffiliation, String dateValidite) throws Exception;

    /**
     * Retourne la date de début de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return la date de début de la cotisation
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la date de fin de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return la date de fin de la cotisation
     */
    public java.lang.String getDateFin();

    /**
     * Retourne le montant de la masse salariale de la périodicité Date de création : (26.02.2003 09:54:44)
     * 
     * @return le montant de la masse salariale de la périodicité
     */
    public java.lang.String getMassePeriodicite();

    /**
     * Retourne le montant annuel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return le montant annuel de la cotisation
     */
    public java.lang.String getMontantAnnuel();

    /**
     * Retourne le montant annuel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return le montant annuel de la cotisation
     */
    public java.lang.String getMontantMensuel();

    /**
     * Retourne le montant semestriel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return le montant semestriel de la cotisation
     */
    public java.lang.String getMontantSemestriel();

    /**
     * Retourne le montant trimestriel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return le montant trimestriel de la cotisation
     */
    public java.lang.String getMontantTrimestriel();

    /**
     * Retourne le code système du motif de fin de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système du motif de fin de la cotisation
     */
    public java.lang.String getMotifFin();

    /**
     * Retourne le code système de la périodicité de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @return le code système de la périodicité de la cotisation
     */
    public java.lang.String getPeriodicite();

    /**
     * Retourne l'id du plan de l'affiliation liée Date de création : (26.02.2003 09:54:44)
     * 
     * @return id du plan de l'affiliation liée
     */
    public java.lang.String getPlanAffiliationId();

    /**
     * Retourne l'id du plan de caisse liée Date de création : (26.02.2003 09:54:44)
     * 
     * @return id du plan de caisse liée
     */
    public java.lang.String getPlanCaisseId();

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
     * Définit l'id de l'adhésion liée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAdhesionId
     *            id de l'adhésion liée
     */
    public void setAdhesionId(java.lang.String newAdhesionId);

    /**
     * Définit l'id de l'assurance liée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceId
     *            id de l'assurance liée
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * Définit la date de début de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newDateDebut
     *            la date de début de la cotisation
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * Définit la date de fin de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newDateFin
     *            la date de fin de la cotisation
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * Définit le montant de la masse salariale de la périodicité Date de création : (26.02.2003 09:54:44)
     * 
     * @param newMassePeriodicit
     *            le montant de la masse salariale de la périodicité
     */
    public void setMassePeriodicite(java.lang.String newMassePeriodicit);

    /**
     * Définit le montant annuel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newMontantAnnuel
     *            le montant annuel de la cotisation
     */
    public void setMontantAnnuel(java.lang.String newMontantAnnuel);

    /**
     * Définit le montant annuel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newMontantMensuel
     *            le montant annuel de la cotisation
     */
    public void setMontantMensuel(java.lang.String newMontantMensuel);

    /**
     * Définit le montant semestriel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newMontantSemestriel
     *            le montant semestriel de la cotisation
     */
    public void setMontantSemestriel(java.lang.String newMontantSemestriel);

    /**
     * Définit le montant trimestriel de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newMontantTrimestriel
     *            le montant trimestriel de la cotisation
     */
    public void setMontantTrimestriel(java.lang.String newMontantTrimestriel);

    /**
     * Définit le code système du motif de fin de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newMotifFin
     *            le code système du motif de fin de la cotisation
     */
    public void setMotifFin(java.lang.String newMotifFin);

    /**
     * Définit le code système de la périodicité de la cotisation Date de création : (26.02.2003 09:54:44)
     * 
     * @param newPeriodicite
     *            le code système de la périodicité de la cotisation
     */
    public void setPeriodicite(java.lang.String newPeriodicite);

    /**
     * Définit l'id du plan de l'affiliation liée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newPlanAffiliationId
     *            id du plan de l'affiliation liée
     */
    public void setPlanAffiliationId(java.lang.String newPlanAffiliationId);

    /**
     * Définit l'id du plan de caisse liée Date de création : (26.02.2003 09:54:44)
     * 
     * @param newPlanCaisseId
     *            id du plan de caisse liée
     */
    public void setPlanCaisseId(java.lang.String newPlanCaisseId);

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

package globaz.naos.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Hashtable;

/**
 * Interface sur les cotisation d'une affiliation. Date de cr�ation : (28.05.2002 09:11:43)
 * 
 * @author David Girardin
 */
public interface IAFCotisation extends BIEntity {

    /**
     * Cl� de recherche par Affiliation
     */
    public static final String FIND_FOR_AFFILIATION_ID = "setForAffiliationId";
    /**
     * Cl� de recherche par plan d'affiliation
     */
    public static final String FIND_FOR_PLAN_AFFILIATION_ID = "setForPlanAffiliationId";
    /**
     * inclu les inactifs dans la recherche Mette la chaine de caract�re "true" comme valeur pour include les inactifs
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
    /** P�riodicit� annuelle */
    public final static String PERIODICITE_ANNUELLE = "802004";

    /** P�riodicit� mensuelle */
    public final static String PERIODICITE_MENSUELLE = "802001";

    /** P�riodicit� semestrielle */
    public final static String PERIODICITE_SEMESTRIELLE = "802003";

    /** P�riodicit� trimestrielle */
    public final static String PERIODICITE_TRIMESTRIELLE = "802002";

    /**
     * Ajoute l'enregistrement dans la DB.
     * 
     * @exception Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws Exception;

    /**
     * Renvoie un tableau d'objet representant des cotisations<br>
     * <br>
     * params est une table qui permet l'utilisation d'un / ou plusieurs<br>
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retourn�s<br>
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
     * criti�res de recherche ds la liste suivante : <br>
     * <br>
     * <li>FIND_FOR_AFFILIATION_ID<br>
     * <br>
     * <br>
     * <br>
     * 100 enregistrements au max sont retourn�s<br>
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
     * Retourne l'id de l'adh�sion li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return id de l'adh�sion li�e
     */
    public java.lang.String getAdhesionId();

    /**
     * Retourne si cet id Affiliation est affili� au AF pour les dates donn�es Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du genre de l'assurance (AVS, AF, etc)
     */
    public String getAffilieAF(String IdAffiliation, String dateDebut, String dateFin) throws Exception;

    /**
     * Retourne l'id de l'assurance li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return id de l'assurance li�e
     */
    public java.lang.String getAssuranceId();

    /**
     * Retourne le canton de l'affili� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du canton
     */
    public String getCantonAffilie(String IdAffiliation) throws Exception;

    /**
     * Retourne le libell� court de la cotisation AF correspondant � l'affili�
     * 
     * @param IdAffiliation
     *            identifiant de l'affiliation
     * @param dateValidite
     *            date de validit�
     * @return libell� court correspondant � la cotisations AF trouv�e
     * @throws Exception
     *             si plus d'une cotisation AF trouv�e si aucune cotisation AF trouv�e si l'affili� n'est pas de type
     *             employeur
     * 
     */
    public String getCotisationAF(String IdAffiliation, String dateValidite) throws Exception;

    /**
     * Retourne la date de d�but de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return la date de d�but de la cotisation
     */
    public java.lang.String getDateDebut();

    /**
     * Retourne la date de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return la date de fin de la cotisation
     */
    public java.lang.String getDateFin();

    /**
     * Retourne le montant de la masse salariale de la p�riodicit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le montant de la masse salariale de la p�riodicit�
     */
    public java.lang.String getMassePeriodicite();

    /**
     * Retourne le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le montant annuel de la cotisation
     */
    public java.lang.String getMontantAnnuel();

    /**
     * Retourne le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le montant annuel de la cotisation
     */
    public java.lang.String getMontantMensuel();

    /**
     * Retourne le montant semestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le montant semestriel de la cotisation
     */
    public java.lang.String getMontantSemestriel();

    /**
     * Retourne le montant trimestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le montant trimestriel de la cotisation
     */
    public java.lang.String getMontantTrimestriel();

    /**
     * Retourne le code syst�me du motif de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me du motif de fin de la cotisation
     */
    public java.lang.String getMotifFin();

    /**
     * Retourne le code syst�me de la p�riodicit� de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return le code syst�me de la p�riodicit� de la cotisation
     */
    public java.lang.String getPeriodicite();

    /**
     * Retourne l'id du plan de l'affiliation li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return id du plan de l'affiliation li�e
     */
    public java.lang.String getPlanAffiliationId();

    /**
     * Retourne l'id du plan de caisse li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @return id du plan de caisse li�e
     */
    public java.lang.String getPlanCaisseId();

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
     * D�finit l'id de l'adh�sion li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAdhesionId
     *            id de l'adh�sion li�e
     */
    public void setAdhesionId(java.lang.String newAdhesionId);

    /**
     * D�finit l'id de l'assurance li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newAssuranceId
     *            id de l'assurance li�e
     */
    public void setAssuranceId(java.lang.String newAssuranceId);

    /**
     * D�finit la date de d�but de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newDateDebut
     *            la date de d�but de la cotisation
     */
    public void setDateDebut(java.lang.String newDateDebut);

    /**
     * D�finit la date de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newDateFin
     *            la date de fin de la cotisation
     */
    public void setDateFin(java.lang.String newDateFin);

    /**
     * D�finit le montant de la masse salariale de la p�riodicit� Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newMassePeriodicit
     *            le montant de la masse salariale de la p�riodicit�
     */
    public void setMassePeriodicite(java.lang.String newMassePeriodicit);

    /**
     * D�finit le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newMontantAnnuel
     *            le montant annuel de la cotisation
     */
    public void setMontantAnnuel(java.lang.String newMontantAnnuel);

    /**
     * D�finit le montant annuel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newMontantMensuel
     *            le montant annuel de la cotisation
     */
    public void setMontantMensuel(java.lang.String newMontantMensuel);

    /**
     * D�finit le montant semestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newMontantSemestriel
     *            le montant semestriel de la cotisation
     */
    public void setMontantSemestriel(java.lang.String newMontantSemestriel);

    /**
     * D�finit le montant trimestriel de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newMontantTrimestriel
     *            le montant trimestriel de la cotisation
     */
    public void setMontantTrimestriel(java.lang.String newMontantTrimestriel);

    /**
     * D�finit le code syst�me du motif de fin de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newMotifFin
     *            le code syst�me du motif de fin de la cotisation
     */
    public void setMotifFin(java.lang.String newMotifFin);

    /**
     * D�finit le code syst�me de la p�riodicit� de la cotisation Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newPeriodicite
     *            le code syst�me de la p�riodicit� de la cotisation
     */
    public void setPeriodicite(java.lang.String newPeriodicite);

    /**
     * D�finit l'id du plan de l'affiliation li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newPlanAffiliationId
     *            id du plan de l'affiliation li�e
     */
    public void setPlanAffiliationId(java.lang.String newPlanAffiliationId);

    /**
     * D�finit l'id du plan de caisse li�e Date de cr�ation : (26.02.2003 09:54:44)
     * 
     * @param newPlanCaisseId
     *            id du plan de caisse li�e
     */
    public void setPlanCaisseId(java.lang.String newPlanCaisseId);

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

package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface du journal des inscriptions. Un journal permet de rassembler une s�rie d'inscriptions.
 * 
 * @author David Girardin
 */
public interface ICIJournal extends BIEntity {
    /** Type du journal */
    public final static String CS_APG = "301006";
    /** Type du journal */
    public final static String CS_ASSURANCE_CHOMAGE = "301009";
    /** Type du journal */
    public final static String CS_ASSURANCE_FACULTATIVE = "301012";
    /** Type du journal */
    public final static String CS_ASSURANCE_MILITAIRE = "301010";
    /** Etat du journal */
    public final static String CS_COMPTABILISE = "302002";
    /** Type du journal */
    public final static String CS_CONTROLE_EMPLOYEUR = "301003";
    /** Type du journal */
    public final static String CS_CORRECTIF = "301011";
    /** Type du journal */
    public final static String CS_COTISATIONS_PERSONNELLES = "301004";
    /** Type du journal */
    public final static String CS_DECISION_COT_PERS = "301005";
    /** Type du journal */
    public final static String CS_DECLARATION_COMPLEMENTAIRE = "301002";
    /** Type du journal */
    public final static String CS_DECLARATION_SALAIRES = "301001";
    /** Type du journal */
    public final static String CS_IJAI = "301007";
    /** Type du journal */
    public final static String CS_INSCRIPTIONS_JOURNALIERES = "301013";
    /** Etat du journal */
    public final static String CS_OUVERT = "302001";
    /** Type du journal */
    public final static String CS_SPLITTING = "301008";

    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @param transaction
     *            la transaction � utiliser pour la requ�te
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Passe les inscriptions du journal au CI. Il est �galement possible d'inscrire au CI qu'une partie du journal en
     * sp�cifiant un sous-ensemble � l'aide des param�tres <tt>fromAvs</tt> et <tt>toAvs</tt>.
     * 
     * @param fromAvs
     *            limite inf�rieure des �critures � inscrire au CI ou vide si le journal entier doit �tre inscrit au CI.
     * @param fromAvs
     *            limite sup�rieure des �critures � inscrire au CI ou vide si le journal entier doit �tre inscrit au CI.
     * @param transaction
     *            la transaction � utiliser pour l'op�ration.
     * @exception java.lang.Exception
     *                si l'appel distant de la fonction n'a pas pu �tre effectu�.
     */
    public void comptabiliser(String fromAvs, String toAvs, BITransaction transaction) throws Exception;

    /**
     * Supprime le journal de la BD
     * 
     * @param transaction
     *            la transaction � utiliser pour la requ�te
     * @exception java.lang.Exception
     *                si la suppression a �chou�e
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie l'ann�e de cotisation par d�faut pour les inscriptions associ�es.
     * 
     * @return l'ann�e de cotisation ou vide si non renseign�e.
     */
    public String getAnneeCotisation();

    /**
     * Renvoie le montant des corrections sp�ciales lorsque le total des inscriptions saisies ne correspond pas au total
     * factur�.
     * 
     * @return Le montant des corrections sp�ciales ou vide si non renseign�e. Format <tt>1234.45</tt>
     */
    public String getCorrectionSpeciale();

    /**
     * Renvoie la date de cr�ation du journal.
     * 
     * @return la date de cr�ation du journal. Format <tt>jj.mm.aaaa</tt>
     */
    public String getDate();

    /**
     * Renvoie l'�tat actuel du journal: ouvert ou comptabilis�
     * 
     * @return l'�tat actuel du journal. Peut �tre <tt>CS_OUVERT</tt> ou <tt>CS_COMPTABILISE</tt>
     */
    public String getIdEtat();

    /**
     * Renvoie l'id du journal
     * 
     * @return l'id du journal.
     */
    public String getIdJournal();

    /**
     * Renvoie le type des inscriptions du journal.
     * 
     * @return le type des inscriptions du journal. Les types d�finis sont:
     *         <ul>
     *         <li><tt>CS_DECLARATION_SALAIRES</tt></li>
     *         <li><tt>CS_DECLARATION_COMPLEMENTAIRE</tt></li>
     *         <li><tt>CS_CONTROLE_EMPLOYEUR</tt></li>
     *         <li><tt>CS_COTISATIONS_PERSONNELLES</tt></li>
     *         <li><tt>CS_DECISION_COT_PERS</tt></li>
     *         <li><tt>CS_APG</tt></li>
     *         <li><tt>CS_IJAI</tt></li>
     *         <li><tt>CS_SPLITTING</tt></li>
     *         <li><tt>CS_ASSURANCE_CHOMAGE</tt></li>
     *         <li><tt>CS_ASSURANCE_MILITAIRE</tt></li>
     *         <li><tt>CS_CORRECTIF</tt></li>
     *         <li><tt>CS_ASSURANCE_FACULTATIVE</tt></li>
     *         <li><tt>CS_INSCRIPTIONS_JOURNALIERES</tt></li>
     *         </ul>
     */
    public String getIdTypeInscription();

    /**
     * Renvoie le libell� du journal.
     * 
     * @return le libell� du journal ou vide si non renseign�
     */
    public String getLibelle();

    /**
     * Renvoie le motif des corrections sp�ciales lorsque le total des inscriptions saisies ne correspond pas au total
     * factur�.
     * 
     * @return Le motif des corrections sp�ciales ou vide si non renseign�.
     */
    public String getMotifCorrection();

    /**
     * Renvoie la remarque associ�e au journal.
     * 
     * @return la remarque associ�e au journal ou vide si non renseign�
     */
    public String getRemTexte();

    /**
     * Renvoie le total de contr�le facultatif utilis� pour tester le total avant d'inscrire au CI des inscriptions du
     * journal
     * 
     * @return Le total de contr�le ou vide si non renseign�. Format <tt>1234.45</tt>
     */
    public String getTotalControle();

    /**
     * Renvoie le total actuel des inscriptions du journal.
     * 
     * @return Le total actuel des inscription du journal. Format <tt>1234.45</tt>
     */
    public String getTotalInscrit();

    /**
     * Charge le journal depuis la BD
     * 
     * @param la
     *            transaction � utiliser pour la requ�te
     * @exception java.lang.Exception
     *                si le chargement a �chou�
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * D�finit l'ann�e de cotisation par d�faut pour les inscriptions associ�es.<br>
     * <B>Note:</b> obgligatoire pour les journaux de type <tt>CS_DECLARATION_SALAIRES</tt> et
     * <tt>DECLARATION_COMPLEMENTAIRE</tt>
     * 
     * @param inAnneeCotisation
     *            l'ann�e de cotisation au format <tt>AAAA</tt>.
     */
    public void setAnneeCotisation(String inAnneeCotisation);

    /**
     * D�finit les corrections sp�ciales lorsque le total des inscriptions saisies ne correspond pas au total factur�.
     * Ce montant est facultatif. N'en pris en compte que si le total de contr�le est renseign�. Ajouter ou soustraire
     * ce montant du total des revenus inscrits.
     * 
     * @param inCorrectionSpeciale
     *            le montant des correcetions sp�ciales. Formats: <tt>1'224.45</tt>, <tt>1234.45</tt> ou <tt>1224</tt>.
     */
    public void setCorrectionSpeciale(String inCorrectionSpeciale);

    /**
     * D�finit l'id du journal pour d'une recherche du journal. Ignor� lors d'un ajout (incr�mentation automatique)
     * 
     * @param inIdJournal
     *            l'id du journal.
     */
    public void setIdJournal(String inIdJournal);

    /**
     * D�finit le type des inscriptions du journal.
     * 
     * @param inIdTypeInscription
     *            le type des inscriptions du journal. Les types d�finis sont:
     *            <ul>
     *            <li><tt>CS_DECLARATION_SALAIRES</tt></li>
     *            <li><tt>CS_DECLARATION_COMPLEMENTAIRE</tt></li>
     *            <li><tt>CS_CONTROLE_EMPLOYEUR</tt></li>
     *            <li><tt>CS_COTISATIONS_PERSONNELLES</tt></li>
     *            <li><tt>CS_DECISION_COT_PERS</tt></li>
     *            <li><tt>CS_APG</tt></li>
     *            <li><tt>CS_IJAI</tt></li>
     *            <li><tt>CS_SPLITTING</tt></li>
     *            <li><tt>CS_ASSURANCE_CHOMAGE</tt></li>
     *            <li><tt>CS_ASSURANCE_MILITAIRE</tt></li>
     *            <li><tt>CS_CORRECTIF</tt></li>
     *            <li><tt>CS_ASSURANCE_FACULTATIVE</tt></li>
     *            <li><tt>CS_INSCRIPTIONS_JOURNALIERES</tt></li>
     *            </ul>
     */
    public void setIdTypeInscription(String inIdTypeInscription);

    /**
     * D�finit le libell� du journal.
     * 
     * @param inLibelle
     *            le libell� du journal (max. 50 char.)
     */
    public void setLibelle(String inLibelle);

    /**
     * D�finit le motif des corrections sp�ciales lorsque le total des inscriptions saisies ne correspond pas au total
     * factur�.
     * 
     * @param inMotifCorrection
     *            Le motif des corrections sp�ciales.
     */
    public void setMotifCorrection(String inMotifCorrection);

    /**
     * D�finit la remarque associ�e au journal.
     * 
     * @param inRemTexte
     *            la remarque associ�e au journal (max. 255 char.).
     */
    public void setRemTexte(String inRemTexte);

    /**
     * D�finit le total de contr�le facultatif utilis� pour tester le total avant d'inscrire au CI des inscriptions du
     * journal
     * 
     * @param inTotalControle
     *            Le total de contr�le. Format <tt>1234.45</tt> ou <tt>1'234.45</tt>
     */
    public void setTotalControle(String inTotalControle);

    /**
     * Met � jour le journal dans la BD
     * 
     * @param la
     *            transaction � utiliser pour la modification
     * @exception java.lang.Exception
     *                si la mise � jour a �chou�e
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}

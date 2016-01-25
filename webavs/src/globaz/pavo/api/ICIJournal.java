package globaz.pavo.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Interface du journal des inscriptions. Un journal permet de rassembler une série d'inscriptions.
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
     *            la transaction à utiliser pour la requête
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Passe les inscriptions du journal au CI. Il est également possible d'inscrire au CI qu'une partie du journal en
     * spécifiant un sous-ensemble à l'aide des paramètres <tt>fromAvs</tt> et <tt>toAvs</tt>.
     * 
     * @param fromAvs
     *            limite inférieure des écritures à inscrire au CI ou vide si le journal entier doit être inscrit au CI.
     * @param fromAvs
     *            limite supérieure des écritures à inscrire au CI ou vide si le journal entier doit être inscrit au CI.
     * @param transaction
     *            la transaction à utiliser pour l'opération.
     * @exception java.lang.Exception
     *                si l'appel distant de la fonction n'a pas pu être effectué.
     */
    public void comptabiliser(String fromAvs, String toAvs, BITransaction transaction) throws Exception;

    /**
     * Supprime le journal de la BD
     * 
     * @param transaction
     *            la transaction à utiliser pour la requête
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie l'année de cotisation par défaut pour les inscriptions associées.
     * 
     * @return l'année de cotisation ou vide si non renseignée.
     */
    public String getAnneeCotisation();

    /**
     * Renvoie le montant des corrections spéciales lorsque le total des inscriptions saisies ne correspond pas au total
     * facturé.
     * 
     * @return Le montant des corrections spéciales ou vide si non renseignée. Format <tt>1234.45</tt>
     */
    public String getCorrectionSpeciale();

    /**
     * Renvoie la date de création du journal.
     * 
     * @return la date de création du journal. Format <tt>jj.mm.aaaa</tt>
     */
    public String getDate();

    /**
     * Renvoie l'état actuel du journal: ouvert ou comptabilisé
     * 
     * @return l'état actuel du journal. Peut être <tt>CS_OUVERT</tt> ou <tt>CS_COMPTABILISE</tt>
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
     * @return le type des inscriptions du journal. Les types définis sont:
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
     * Renvoie le libellé du journal.
     * 
     * @return le libellé du journal ou vide si non renseigné
     */
    public String getLibelle();

    /**
     * Renvoie le motif des corrections spéciales lorsque le total des inscriptions saisies ne correspond pas au total
     * facturé.
     * 
     * @return Le motif des corrections spéciales ou vide si non renseigné.
     */
    public String getMotifCorrection();

    /**
     * Renvoie la remarque associée au journal.
     * 
     * @return la remarque associée au journal ou vide si non renseigné
     */
    public String getRemTexte();

    /**
     * Renvoie le total de contrôle facultatif utilisé pour tester le total avant d'inscrire au CI des inscriptions du
     * journal
     * 
     * @return Le total de contrôle ou vide si non renseigné. Format <tt>1234.45</tt>
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
     *            transaction à utiliser pour la requête
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit l'année de cotisation par défaut pour les inscriptions associées.<br>
     * <B>Note:</b> obgligatoire pour les journaux de type <tt>CS_DECLARATION_SALAIRES</tt> et
     * <tt>DECLARATION_COMPLEMENTAIRE</tt>
     * 
     * @param inAnneeCotisation
     *            l'année de cotisation au format <tt>AAAA</tt>.
     */
    public void setAnneeCotisation(String inAnneeCotisation);

    /**
     * Définit les corrections spéciales lorsque le total des inscriptions saisies ne correspond pas au total facturé.
     * Ce montant est facultatif. N'en pris en compte que si le total de contrôle est renseigné. Ajouter ou soustraire
     * ce montant du total des revenus inscrits.
     * 
     * @param inCorrectionSpeciale
     *            le montant des correcetions spéciales. Formats: <tt>1'224.45</tt>, <tt>1234.45</tt> ou <tt>1224</tt>.
     */
    public void setCorrectionSpeciale(String inCorrectionSpeciale);

    /**
     * Définit l'id du journal pour d'une recherche du journal. Ignoré lors d'un ajout (incrémentation automatique)
     * 
     * @param inIdJournal
     *            l'id du journal.
     */
    public void setIdJournal(String inIdJournal);

    /**
     * Définit le type des inscriptions du journal.
     * 
     * @param inIdTypeInscription
     *            le type des inscriptions du journal. Les types définis sont:
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
     * Définit le libellé du journal.
     * 
     * @param inLibelle
     *            le libellé du journal (max. 50 char.)
     */
    public void setLibelle(String inLibelle);

    /**
     * Définit le motif des corrections spéciales lorsque le total des inscriptions saisies ne correspond pas au total
     * facturé.
     * 
     * @param inMotifCorrection
     *            Le motif des corrections spéciales.
     */
    public void setMotifCorrection(String inMotifCorrection);

    /**
     * Définit la remarque associée au journal.
     * 
     * @param inRemTexte
     *            la remarque associée au journal (max. 255 char.).
     */
    public void setRemTexte(String inRemTexte);

    /**
     * Définit le total de contrôle facultatif utilisé pour tester le total avant d'inscrire au CI des inscriptions du
     * journal
     * 
     * @param inTotalControle
     *            Le total de contrôle. Format <tt>1234.45</tt> ou <tt>1'234.45</tt>
     */
    public void setTotalControle(String inTotalControle);

    /**
     * Met à jour le journal dans la BD
     * 
     * @param la
     *            transaction à utiliser pour la modification
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}

package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe représente une période comptable. Une période est liée à un <b>Exercice Comptable</b>
 * {@link ICGExerciceComptable} et peut être liée à un <b>Journal</b> {@link ICGJournal}.<br>
 * Pour récupérer une période de la base de données, il fournir un ID et exécuter un appel à la méthode <b>retrieve</b>.<br>
 * <br>
 * Si l'ID de la période est inconnu, on peut utiliser la méthode <code>setIdPeriodeComptableFrom</code> avec comme
 * paramètre un code standard AVS de période et l'ID d'un mandat.<br>
 * Pour rappel, les codes standards AVS sont : <li>01 : Janvier <li>02 : Février <li>03 : Mars <li><b>...</b> <li>12 :
 * Décembre <li>13 : Période annuelle <li>
 * 99 : Période de clôture<br>
 * <br>
 * Exemple complet :<br>
 * 
 * <pre>
 * <code>
 * try {
 *   BIApplication remoteApplication = GlobazSystem.getApplication("HELIOS");
 *   BISession remoteSession = remoteApplication.newSession("userfr", "userfr");
 *   BITransaction transaction = remoteSession.newTransaction();
 * 	 transaction.openTransaction();
 * 
 *   String idMandat = "900";
 *   String code = "03";
 * 
 *   String date="01.01.2003";
 *   
 *   ICGExerciceComptable exercice = (ICGExerciceComptable) remoteSession.getAPIFor(ICGExerciceComptable.class);
 *   ICGPeriodeComptable periode = (ICGPeriodeComptable) remoteSession.getAPIFor(ICGPeriodeComptable.class);
 * 
 *   try {
 * 
 *     // On ne connait pas l'ID de l'exercice, on le récupère via une date et l'ID du mandat
 *     exercice.setIdExerciceComptableFrom( date, idMandat );
 * 
 *     // Récupération de la période via l'ID d'un exercice et d'un code standard AVS
 *     exercice.setIdPeriodeComptableFrom( code, exercice.getIdExerciceComptable() );
 * 
 *     if (periode.isNew())
 *       System.err.println("Aucune période pour le code " +code+ " et l'exerice N° "+exercice.getIdExerciceComptable());
 * 
 *   } catch (Exception e) {
 *     transaction.closeTransaction();
 *     e.printStackTrace();
 *   }
 *   if (remoteSession.hasErrors()) {
 *     System.err.println("Non-fatal errors : " + remoteSession.getErrors().toString());
 *   }
 * } catch (Exception e) {
 *   System.err.println("Fatal errors : " + e.printStackTrace());
 * }
 * </code>
 * </pre>
 * 
 * @author Grégoire PICQUOT (gregoire.picquot@globaz.ch)
 * @see ICGJournal
 * @see ICGExerciceComptable
 */
public interface ICGPeriodeComptable extends BIEntity {
    public final static String CS_ANNUEL = "709004"; // Période comptable
    public final static String CS_MENSUEL = "709001"; // Période comptable
    public final static String CS_SEMESTRIEL = "709003"; // Période comptable
    public final static String CS_TRIMESTRIEL = "709002"; // Période comptable

    /**
     * Renvoie la valeur du code standard AVS.
     * 
     * @return la valeur du code standard AVS
     */
    public java.lang.String getCode();

    /**
     * Renvoie la date de début de la période.
     * 
     * @return la date de début de la période
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin de la période.
     * 
     * @return la date de fin de la période
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie la description normalisée de la période.
     * 
     * @return la description normalisée de la période
     */
    public String getFullDescription() throws Exception;

    /**
     * Renvoie l'ID du journal.
     * 
     * @return l'ID du journal
     */
    public java.lang.String getIdJournal();

    /**
     * Renvoie la valeur de la propriété idPeriodeComptable
     * 
     * @return la valeur de la propriété idPeriodeComptable
     */
    public java.lang.String getIdPeriodeComptable();

    /**
     * Renvoie le type de la période (annuelle, mensuelle ou clôture).
     * 
     * @return le type de la période
     */
    public java.lang.String getIdTypePeriode();

    /**
     * Récupère une période depuis la base de données grâce à son ID.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la récupération échoue
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit l'ID de la période comptable.
     * 
     * @param newIdPeriodeComptable
     *            newIdPeriodeComptable
     */
    public void setIdPeriodeComptable(java.lang.String newIdPeriodeComptable);

    /**
     * Définit l'ID de la période comptable via un code standard AVS et un ID d'exercice comptable.
     * 
     * @param code
     *            code
     * @param idExerciceComptable
     *            idExerciceComptable
     */
    public void setIdPeriodeComptableFrom(java.lang.String code, java.lang.String idExerciceComptable) throws Exception;
}

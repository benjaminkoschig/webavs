package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe représente un exercice comptable. Un exercice est lié à un <b>Mandat</b>.<br>
 * Pour récupérer un exercice de la base de données, il fournir un ID et exécuter un appel à la méthode <b>retrieve</b>.<br>
 * <br>
 * Si l'ID de l'exercice est inconnu, on peut utiliser la méthode <code>setIdExerciceComptableFrom</code> avec comme
 * paramètre une date et l'ID du mandat. <br>
 * Exemple complet :
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
 *   String idExerciceConnu = "123";
 * 
 *   String date="01.01.2003";
 *   
 *   ICGExerciceComptable exercice = (ICGExerciceComptable) remoteSession.getAPIFor(ICGExerciceComptable.class);
 * 
 *   try {
 * 
 *     // On connait l'ID de l'exercice
 *     exercice.setIdExerciceComptable(idExerciceConnu);
 *     exercice.retrieve(transaction);
 * 
 *     // On ne connait pas l'ID, on le récupère via une date et l'ID du mandat
 *     exercice.setIdExerciceComptableFrom( date, idMandat );
 *     exercice.retrieve(transaction);
 * 
 *     if (exercice.isNew())
 *       System.err.println("Aucun exercice pour la date " +date+ " et le mandat N° "+idMandat);
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
 */
public interface ICGExerciceComptable extends BIEntity {
    /**
     * Renvoie la date de début de l'exercice.
     * 
     * @return la date de début de l'exercice
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin de l'exercice.
     * 
     * @return la date de fin de l'exercice
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie la description de l'exercice.
     * 
     * @return la description de l'exercice
     */
    public String getFullDescription() throws Exception;

    /**
     * Renvoie l'ID de l'exercice comptable.
     * 
     * @return l'ID de l'exercice comptable
     */
    public java.lang.String getIdExerciceComptable();

    /**
     * Renvoie l'ID du mandat.
     * 
     * @return l'ID du mandat
     */
    public java.lang.String getIdMandat();

    /**
     * Récupère l'exercice de la base de données.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la récupération échoue
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit l'ID de l'exercice comptable.
     * 
     * @param newIdExerciceComptable
     *            newIdExerciceComptable
     */
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable);

    /**
     * Définit l'ID de l'exercice comptable grace à une date et à l'ID d'un mandat.
     * 
     * @param date
     *            date
     * @param idMandat
     *            idMandat
     */
    public void setIdExerciceComptableFrom(java.lang.String date, java.lang.String idMandat) throws Exception;
}

package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe repr�sente une p�riode comptable. Une p�riode est li�e � un <b>Exercice Comptable</b>
 * {@link ICGExerciceComptable} et peut �tre li�e � un <b>Journal</b> {@link ICGJournal}.<br>
 * Pour r�cup�rer une p�riode de la base de donn�es, il fournir un ID et ex�cuter un appel � la m�thode <b>retrieve</b>.<br>
 * <br>
 * Si l'ID de la p�riode est inconnu, on peut utiliser la m�thode <code>setIdPeriodeComptableFrom</code> avec comme
 * param�tre un code standard AVS de p�riode et l'ID d'un mandat.<br>
 * Pour rappel, les codes standards AVS sont : <li>01 : Janvier <li>02 : F�vrier <li>03 : Mars <li><b>...</b> <li>12 :
 * D�cembre <li>13 : P�riode annuelle <li>
 * 99 : P�riode de cl�ture<br>
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
 *     // On ne connait pas l'ID de l'exercice, on le r�cup�re via une date et l'ID du mandat
 *     exercice.setIdExerciceComptableFrom( date, idMandat );
 * 
 *     // R�cup�ration de la p�riode via l'ID d'un exercice et d'un code standard AVS
 *     exercice.setIdPeriodeComptableFrom( code, exercice.getIdExerciceComptable() );
 * 
 *     if (periode.isNew())
 *       System.err.println("Aucune p�riode pour le code " +code+ " et l'exerice N� "+exercice.getIdExerciceComptable());
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
 * @author Gr�goire PICQUOT (gregoire.picquot@globaz.ch)
 * @see ICGJournal
 * @see ICGExerciceComptable
 */
public interface ICGPeriodeComptable extends BIEntity {
    public final static String CS_ANNUEL = "709004"; // P�riode comptable
    public final static String CS_MENSUEL = "709001"; // P�riode comptable
    public final static String CS_SEMESTRIEL = "709003"; // P�riode comptable
    public final static String CS_TRIMESTRIEL = "709002"; // P�riode comptable

    /**
     * Renvoie la valeur du code standard AVS.
     * 
     * @return la valeur du code standard AVS
     */
    public java.lang.String getCode();

    /**
     * Renvoie la date de d�but de la p�riode.
     * 
     * @return la date de d�but de la p�riode
     */
    public java.lang.String getDateDebut();

    /**
     * Renvoie la date de fin de la p�riode.
     * 
     * @return la date de fin de la p�riode
     */
    public java.lang.String getDateFin();

    /**
     * Renvoie la description normalis�e de la p�riode.
     * 
     * @return la description normalis�e de la p�riode
     */
    public String getFullDescription() throws Exception;

    /**
     * Renvoie l'ID du journal.
     * 
     * @return l'ID du journal
     */
    public java.lang.String getIdJournal();

    /**
     * Renvoie la valeur de la propri�t� idPeriodeComptable
     * 
     * @return la valeur de la propri�t� idPeriodeComptable
     */
    public java.lang.String getIdPeriodeComptable();

    /**
     * Renvoie le type de la p�riode (annuelle, mensuelle ou cl�ture).
     * 
     * @return le type de la p�riode
     */
    public java.lang.String getIdTypePeriode();

    /**
     * R�cup�re une p�riode depuis la base de donn�es gr�ce � son ID.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la r�cup�ration �choue
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * D�finit l'ID de la p�riode comptable.
     * 
     * @param newIdPeriodeComptable
     *            newIdPeriodeComptable
     */
    public void setIdPeriodeComptable(java.lang.String newIdPeriodeComptable);

    /**
     * D�finit l'ID de la p�riode comptable via un code standard AVS et un ID d'exercice comptable.
     * 
     * @param code
     *            code
     * @param idExerciceComptable
     *            idExerciceComptable
     */
    public void setIdPeriodeComptableFrom(java.lang.String code, java.lang.String idExerciceComptable) throws Exception;
}

package globaz.osiris.external;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;
import globaz.globall.shared.GlobazValueObject;

/**
 * Interface vers un journal de comptabilit� g�n�rale Date de cr�ation : (11.02.2002 10:39:26)
 * 
 * @author: Administrator
 */
public interface IntJournalCG {

    public static final String NUMERO_NO_JOURNAL = "0";

    /**
     * Ins�re un nouveau journal de comptabilit� g�n�rale Date de cr�ation : (28.10.2002 10:03:54)
     * 
     * @return globaz.osiris.external.IntJournalCG L'interface vers le nouveau journal CG cr��
     * @param transaction
     *            globaz.globall.api.BTransaction La transaction courante
     * @param journalValueObject
     *            globaz.globall.shared.GlobazValueObject Le value object repr�sentant le journal
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    IntJournalCG addJournal(BTransaction transaction, GlobazValueObject journalValueObject) throws java.lang.Exception;

    /**
     * Annuler le journal de comptabilit� g�n�rale Date de cr�ation : (28.10.2002 09:57:01)
     * 
     * @param transaction
     *            globaz.globall.api.BTransaction La transaction
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    void annuler(BTransaction transaction) throws java.lang.Exception;

    void annuler(BTransaction transaction, String emailAddress) throws java.lang.Exception;

    /**
     * Ex�cuter la comptabilisation du journal Date de cr�ation : (28.10.2002 09:55:14)
     * 
     * @param globaz
     *            .globall.api.BTransaction la transaction
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    void comptabiliser(BTransaction transaction) throws java.lang.Exception;

    void comptabiliser(BTransaction transaction, String emailAddress) throws java.lang.Exception;

    /**
     * R�cup�re la date Date de cr�ation : (24.10.2002 16:35:52)
     * 
     * @return java.lang.String la date
     */
    String getDate();

    /**
     * R�cup�re la date de valeur Date de cr�ation : (08.11.2002 09:11:47)
     * 
     * @return java.lang.String la date de valeur
     */
    String getDateValeur();

    public String getIdEtat();

    public String getIdExerciceComptable();

    public String getIdJournal();

    public String getLibelle();

    public String getNumero();

    public BISession getSessionHelios(BISession session) throws Exception;

    /**
     * Imprimer le journal de comptabilit� g�n�rale
     * 
     * @exception java.lang.Exception
     *                La description de l'exception Date de cr�ation : (28.10.2002 09:58:17)
     */
    void imprimer() throws Exception;

    /**
     * Indiquer si le journal est en erreur Date de cr�ation : (24.10.2002 16:37:32)
     * 
     * @return boolean l'�tat de l'indicateur d'erreur
     */
    boolean isOnError();

    /**
     * R�cup�re un journal Date de cr�ation : (24.10.2002 16:32:33)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     * @param shouldAddErrors
     *            boolean propager les erreurs
     */
    void retrieve(BISession session, BITransaction transaction, boolean shouldAddErrors) throws Exception;

    /**
     * Affecter l'identifiant du journal Date de cr�ation : (24.10.2002 14:25:37)
     * 
     * @param newIdJournal
     *            java.lang.String le num�ro du journal
     */
    void setIdJournal(String newIdJournal);
}

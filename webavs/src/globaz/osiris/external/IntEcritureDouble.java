package globaz.osiris.external;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;

/**
 * Interface d'une écriture collective en comptabilité générale Date de création : (28.10.2002 08:31:17)
 * 
 * @author: Administrator
 */
public interface IntEcritureDouble {
    public static final String CODE_CREDIT = "CR";
    public static final String CODE_DEBIT = "DB";
    public static final String CODE_EXTOURNE_CREDIT = "EC";
    public static final String CODE_EXTOURNE_DEBIT = "ED";

    /**
     * Récupère le code débit / crédit de l'écriture <BR>
     * DB = Débit CR = Credit ED = Extourne débit EC = Extourne crédit Date de création : (28.10.2002 09:12:34)
     * 
     * @return java.lang.String le code débit crédit de l'écriture
     */
    String getCodeDebitCredit();

    /**
     * Récupère le compte de comptabilité générale associé à l'écriture Date de création : (28.10.2002 09:04:30)
     * 
     * @param globaz
     *            .globall.db.BTransaction la transaction courante
     * @return globaz.osiris.external.IntCompteCG le compte de comptabilité générale
     */
    IntCompteCG getCompteCG(BTransaction transaction);

    /**
     * Récupère le compte de contre écriture (une valeur null est possible si la contre écriture n'est pas disponible)
     * Date de création : (28.10.2002 10:08:33)
     * 
     * @param globaz
     *            .globall.db.BTransaction la transaction courante
     * @return globaz.osiris.external.IntCompteCG Le compte de contre écriture
     */
    IntCompteCG getContreEcriture(BTransaction transaction);

    /**
     * Récupère la date de l'écriture Date de création : (28.10.2002 08:49:35)
     * 
     * @return java.lang.String la date de l'écriture
     */
    String getDate();

    /**
     * Retourne la date de valeur de l'écriture Date de création : (28.10.2002 13:18:07)
     * 
     * @return java.lang.String La date de valeur
     */
    String getDateValeur();

    /**
     * Récupère l'identifiant de l'écriture collective Date de création : (28.10.2002 08:35:41)
     * 
     * @return java.lang.String l'identifiant de l'écriture collective
     */
    String getIdEcritureCollective();

    /**
     * Récupère l'identifiant du journal associé à l'écriture Date de création : (28.10.2002 13:17:01)
     * 
     * @return java.lang.String L'identifiant du journal
     */
    String getIdJournal();

    /**
     * Récupère l'identifiant du livre de comptabilité générale. La valeur null indique que cette possiblité n'est pas
     * implémentée. Date de création : (28.10.2002 10:12:54)
     * 
     * @return java.lang.String L'identifiant du livre de comptabilité générale
     */
    String getIdLivre();

    /**
     * Récupère le libellé de l'écriture collective Date de création : (28.10.2002 08:37:30)
     * 
     * @return java.lang.String le libellé de l'écriture collective
     */
    String getLibelle();

    /**
     * Récupère le montant de l'écriture Date de création : (28.10.2002 09:11:23)
     * 
     * @return java.lang.String le montant de l'écriture
     */
    String getMontant();

    /**
     * Récupère le numéro de la pièce comptable Date de création : (28.10.2002 09:11:54)
     * 
     * @return java.lang.String le numéro de la pièce comptable
     */
    String getPieceComptable();

    /**
     * Récupère la référence externe de l'écriture Date de création : (28.10.2002 10:06:19)
     * 
     * @return java.lang.String La référence externe de l'écriture
     */
    String getReferenceExterne();

    /**
     * Indiquer si l'entité est en erreur Date de création : (24.10.2002 16:37:32)
     * 
     * @return boolean l'état de l'indicateur d'erreur
     */
    boolean isOnError();

    /**
     * Récupère une écriture collective Date de création : (24.10.2002 16:32:33)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     * @param shouldAddErrors
     *            boolean propager les erreurs
     */
    void retrieve(BISession session, BITransaction transaction, boolean shouldAddErrors) throws Exception;

    /**
     * Affecter l'identifiant de l'écriture collective Date de création : (28.10.2002 08:36:33)
     * 
     * @param newIdEcritureCollective
     *            java.lang.String l'identifiant de l'écriture collective
     */
    void setIdEcritureCollective(String newIdEcritureCollective);
}

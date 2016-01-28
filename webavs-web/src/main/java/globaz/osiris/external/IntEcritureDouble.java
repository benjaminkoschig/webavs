package globaz.osiris.external;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;

/**
 * Interface d'une �criture collective en comptabilit� g�n�rale Date de cr�ation : (28.10.2002 08:31:17)
 * 
 * @author: Administrator
 */
public interface IntEcritureDouble {
    public static final String CODE_CREDIT = "CR";
    public static final String CODE_DEBIT = "DB";
    public static final String CODE_EXTOURNE_CREDIT = "EC";
    public static final String CODE_EXTOURNE_DEBIT = "ED";

    /**
     * R�cup�re le code d�bit / cr�dit de l'�criture <BR>
     * DB = D�bit CR = Credit ED = Extourne d�bit EC = Extourne cr�dit Date de cr�ation : (28.10.2002 09:12:34)
     * 
     * @return java.lang.String le code d�bit cr�dit de l'�criture
     */
    String getCodeDebitCredit();

    /**
     * R�cup�re le compte de comptabilit� g�n�rale associ� � l'�criture Date de cr�ation : (28.10.2002 09:04:30)
     * 
     * @param globaz
     *            .globall.db.BTransaction la transaction courante
     * @return globaz.osiris.external.IntCompteCG le compte de comptabilit� g�n�rale
     */
    IntCompteCG getCompteCG(BTransaction transaction);

    /**
     * R�cup�re le compte de contre �criture (une valeur null est possible si la contre �criture n'est pas disponible)
     * Date de cr�ation : (28.10.2002 10:08:33)
     * 
     * @param globaz
     *            .globall.db.BTransaction la transaction courante
     * @return globaz.osiris.external.IntCompteCG Le compte de contre �criture
     */
    IntCompteCG getContreEcriture(BTransaction transaction);

    /**
     * R�cup�re la date de l'�criture Date de cr�ation : (28.10.2002 08:49:35)
     * 
     * @return java.lang.String la date de l'�criture
     */
    String getDate();

    /**
     * Retourne la date de valeur de l'�criture Date de cr�ation : (28.10.2002 13:18:07)
     * 
     * @return java.lang.String La date de valeur
     */
    String getDateValeur();

    /**
     * R�cup�re l'identifiant de l'�criture collective Date de cr�ation : (28.10.2002 08:35:41)
     * 
     * @return java.lang.String l'identifiant de l'�criture collective
     */
    String getIdEcritureCollective();

    /**
     * R�cup�re l'identifiant du journal associ� � l'�criture Date de cr�ation : (28.10.2002 13:17:01)
     * 
     * @return java.lang.String L'identifiant du journal
     */
    String getIdJournal();

    /**
     * R�cup�re l'identifiant du livre de comptabilit� g�n�rale. La valeur null indique que cette possiblit� n'est pas
     * impl�ment�e. Date de cr�ation : (28.10.2002 10:12:54)
     * 
     * @return java.lang.String L'identifiant du livre de comptabilit� g�n�rale
     */
    String getIdLivre();

    /**
     * R�cup�re le libell� de l'�criture collective Date de cr�ation : (28.10.2002 08:37:30)
     * 
     * @return java.lang.String le libell� de l'�criture collective
     */
    String getLibelle();

    /**
     * R�cup�re le montant de l'�criture Date de cr�ation : (28.10.2002 09:11:23)
     * 
     * @return java.lang.String le montant de l'�criture
     */
    String getMontant();

    /**
     * R�cup�re le num�ro de la pi�ce comptable Date de cr�ation : (28.10.2002 09:11:54)
     * 
     * @return java.lang.String le num�ro de la pi�ce comptable
     */
    String getPieceComptable();

    /**
     * R�cup�re la r�f�rence externe de l'�criture Date de cr�ation : (28.10.2002 10:06:19)
     * 
     * @return java.lang.String La r�f�rence externe de l'�criture
     */
    String getReferenceExterne();

    /**
     * Indiquer si l'entit� est en erreur Date de cr�ation : (24.10.2002 16:37:32)
     * 
     * @return boolean l'�tat de l'indicateur d'erreur
     */
    boolean isOnError();

    /**
     * R�cup�re une �criture collective Date de cr�ation : (24.10.2002 16:32:33)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     * @param shouldAddErrors
     *            boolean propager les erreurs
     */
    void retrieve(BISession session, BITransaction transaction, boolean shouldAddErrors) throws Exception;

    /**
     * Affecter l'identifiant de l'�criture collective Date de cr�ation : (28.10.2002 08:36:33)
     * 
     * @param newIdEcritureCollective
     *            java.lang.String l'identifiant de l'�criture collective
     */
    void setIdEcritureCollective(String newIdEcritureCollective);
}

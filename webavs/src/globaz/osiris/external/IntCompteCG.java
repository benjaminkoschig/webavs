package globaz.osiris.external;

import globaz.globall.api.BITransaction;

/**
 * Interface d'un compte en comptabilité générale Date de création : (28.10.2002 08:54:33)
 * 
 * @author: Administrator
 */
public interface IntCompteCG {
    int AK_IDEXTERNECOMPTE = 1000;

    /**
     * Récupère la description du compte dans la langue de l'utilisateur Date de création : (28.10.2002 08:57:35)
     * 
     * @return java.lang.String la description du compte
     */
    String getDescription();

    /**
     * Insérez la description du compte dans la langue indiquée par le code ISO Date de création : (28.10.2002 09:01:57)
     * 
     * @param codeISOLangue
     *            java.lang.String le code ISO de la langue
     * @return java.lang.String La description du compte
     */
    String getDescription(String codeISOLangue);

    /**
     * Récupère l'identifiant du compte Date de création : (28.10.2002 08:57:02)
     * 
     * @return java.lang.String l'identifiant du compte
     */
    String getIdCompte();

    /**
     * Récupre l'identifiant externe du compte Date de création : (28.10.2002 08:57:19)
     * 
     * @return java.lang.String l'identifiant externe du compte
     */
    String getIdExterneCompte();

    boolean isNew();

    /**
     * Indiquer si l'entité est en erreur Date de création : (24.10.2002 16:37:32)
     * 
     * @return boolean l'état de l'indicateur d'erreur
     */
    boolean isOnError();

    /**
     * Récupère un compte CG Date de création : (24.10.2002 16:32:33)
     * 
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     * @param shouldAddErrors
     *            boolean propager les erreurs
     */
    void retrieve(BITransaction transaction, boolean shouldAddErrors) throws Exception;

    /**
     * Sélectionne une clé alternée pour la récupération du compte Date de création : (28.10.2002 08:59:05)
     * 
     * @param alternateKey
     *            int le numéro de la clé alternée
     */
    void setAlternateKey(int alternateKey);

    /**
     * Affecte l'identifiant du compte Date de création : (28.10.2002 08:57:59)
     * 
     * @param newIdCompte
     *            java.lang.String l'identifiant du compte
     */
    void setIdCompte(String newIdCompte);

    /**
     * Affecte l'identifiant externe du compte Date de création : (28.10.2002 08:58:22)
     * 
     * @param newIdExterneCompte
     *            java.lang.String l'identifiant externe du compte
     */
    void setIdExterneCompte(String newIdExterneCompte);
}

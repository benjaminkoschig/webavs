package ch.globaz.libra.api;

import globaz.globall.db.BTransaction;

/**
 * @author HPE
 * 
 */
public interface ILIJournalisationsInterfaces {

    /**
     * [1] Cr�ation de journalisation simple
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait�
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     */
    public void createJournalisation(BTransaction transaction, String idExterne, String libelle, boolean isDossier);

    /**
     * [3] Cr�ation de journalisation avec remarque
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait� -> Cr�ation d'une remarque
     * dans la journalisation
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     */
    public void createJournalisationAvecRemarque(BTransaction transaction, String idExterne, String libelle,
            String remarque, boolean isDossier);

    /**
     * [4] Cr�ation de journalisation avec remarque
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait� -> Cr�ation d'une remarque
     * dans la journalisation
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     */
    public void createJournalisationAvecRemarqueWithTestDossier(BTransaction transaction, String idExterne,
            String libelle, String idTiers, String csDomaine, String remarque, boolean isDossier);

    /**
     * [2] Cr�ation de journalisation simple
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     */
    public void createJournalisationWithTestDossier(BTransaction transaction, String idExterne, String libelle,
            String idTiers, String csDomaine, boolean isDossier);
}

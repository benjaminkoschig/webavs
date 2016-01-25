package ch.globaz.libra.api;

import globaz.globall.db.BTransaction;

/**
 * @author HPE
 * 
 */
public interface ILIJournalisationsInterfaces {

    /**
     * [1] Création de journalisation simple
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
     * 
     */
    public void createJournalisation(BTransaction transaction, String idExterne, String libelle, boolean isDossier);

    /**
     * [3] Création de journalisation avec remarque
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité -> Création d'une remarque
     * dans la journalisation
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
     * 
     */
    public void createJournalisationAvecRemarque(BTransaction transaction, String idExterne, String libelle,
            String remarque, boolean isDossier);

    /**
     * [4] Création de journalisation avec remarque
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lier au dossier si souhaité -> Création d'une remarque
     * dans la journalisation
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     */
    public void createJournalisationAvecRemarqueWithTestDossier(BTransaction transaction, String idExterne,
            String libelle, String idTiers, String csDomaine, String remarque, boolean isDossier);

    /**
     * [2] Création de journalisation simple
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     */
    public void createJournalisationWithTestDossier(BTransaction transaction, String idExterne, String libelle,
            String idTiers, String csDomaine, boolean isDossier);
}

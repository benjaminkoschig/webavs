package ch.globaz.libra.api;

import globaz.globall.db.BTransaction;

public interface ILIEcheancesInterfaces {

    /**
     * [1] Création d'échéance simple.
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode nécessite qu'un dossier existe déjà dans LIBRA
     * 
     */
    public void createRappel(BTransaction transaction, String dateRappel, String idExterne, String libelle,
            boolean isDossier);

    /**
     * [2] Création d'échéance simple.
     * 
     * -> Lié au tiers de toute façon -> boolean permettant de lié au dossier si souhaité
     * 
     * ==> Cette méthode permet de tester s'il existe un dossier ou non et de le créer s'il n'existe pas
     * 
     */
    public void createRappelWithTestDossier(BTransaction transaction, String dateRappel, String idExterne,
            String libelle, String idTiers, String csDomaine, boolean isDossier);

}

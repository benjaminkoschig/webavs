package ch.globaz.libra.api;

import globaz.globall.db.BTransaction;

public interface ILIEcheancesInterfaces {

    /**
     * [1] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     */
    public void createRappel(BTransaction transaction, String dateRappel, String idExterne, String libelle,
            boolean isDossier);

    /**
     * [2] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de li� au dossier si souhait�
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     */
    public void createRappelWithTestDossier(BTransaction transaction, String dateRappel, String idExterne,
            String libelle, String idTiers, String csDomaine, boolean isDossier);

}

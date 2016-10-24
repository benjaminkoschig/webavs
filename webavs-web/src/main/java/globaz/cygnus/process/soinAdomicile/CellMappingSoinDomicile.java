package globaz.cygnus.process.soinAdomicile;

/**
 * ATTENTION ne pas renommer l'énum, car le nom est utilisé pour la traduction des erreurs;
 * Si il y un renomage il faut aussi renommer les labels.
 * 
 * @author dma
 * 
 */

enum CellMappingSoinDomicile {
    SERVICE(1),
    NSS(2),
    NOM_PRENOM(3),
    FRAIS_JOURNALIER(4),
    DATE_DEBUT(5),
    DATE_FIN(6),
    NB_JOURS(7),
    MONTANT_TOTAL(8),
    DATE_DECOMPTE(9);

    private Integer indexCellule;

    private CellMappingSoinDomicile(Integer indexCellule) {
        this.indexCellule = indexCellule;
    }

    public Integer getIndex() {
        return indexCellule;
    }

}

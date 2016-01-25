package ch.globaz.pegasus.businessimpl.utils.calcul;

public interface IPeriodePCAccordee {

    /**
     * Retourne les calculs comparatifs de la pc accord�e
     * 
     * @return liste de plans de calcul retenus, dont le premier est celui du requerant. Si le couple est s�par� par la
     *         maladie, le 2e plan de calcul est celui du conjoint.
     */
    public abstract ICalculComparatif[] getCalculComparatifRetenus();

    /**
     * Retourne l'id de la simple pc accord�e li�e � la p�riode
     * 
     * @return id de la simple pc accord�e en BD
     */
    public abstract String getIdSimplePcAccordee();

}

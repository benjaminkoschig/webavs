package globaz.osiris.db.comptes;

/**
 * Insérez la description du type ici. Date de création : (10.12.2001 10:13:41)
 * 
 * @author: Administrator
 */
public class CACompteCourantLienCG extends CACompteCourant {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String compteCG = "";
    private String libelleCG = "";
    private String soldeCG = "";

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);

        compteCG = statement.dbReadString("compteCG");
        libelleCG = statement.dbReadString("libelleCG");
        soldeCG = statement.dbReadNumeric("soldeCG", 2);
    }

    /**
     * @return the compteCG
     */
    public String getCompteCG() {
        return compteCG;
    }

    /**
     * @return the libelleCG
     */
    public String getLibelleCG() {
        return libelleCG;
    }

    /**
     * @return the soldeCG
     */
    public String getSoldeCG() {
        return soldeCG;
    }

    /**
     * @param compteCG
     *            the compteCG to set
     */
    public void setCompteCG(String compteCG) {
        this.compteCG = compteCG;
    }

    /**
     * @param libelleCG
     *            the libelleCG to set
     */
    public void setLibelleCG(String libelleCG) {
        this.libelleCG = libelleCG;
    }

    /**
     * @param soldeCG
     *            the soldeCG to set
     */
    public void setSoldeCG(String soldeCG) {
        this.soldeCG = soldeCG;
    }

}

package globaz.naos.db.taxeCo2;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class AFLettreTaxeCo2 extends AFTaxeCo2 implements Serializable {

    // DB
    // Fields

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_MODULE = "IDMODFAC";
    public static final String FIELDNAME_ID_RUBRIQUE = "IDRUBRIQUE";
    public static final String FIELDNAME_MONTANT = "MONTANTFACTURE";

    private String idmodule = new String();
    private String idRubrique = new String();
    private String montant = new String();

    /**
     * Constructeur de AFAffiliation
     */
    public AFLettreTaxeCo2() {
        super();
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idRubrique = statement.dbReadNumeric(AFLettreTaxeCo2.FIELDNAME_ID_RUBRIQUE);
        montant = statement.dbReadNumeric(AFLettreTaxeCo2.FIELDNAME_MONTANT);
        idmodule = statement.dbReadNumeric(AFLettreTaxeCo2.FIELDNAME_ID_MODULE);
    }

    public String getIdmodule() {
        return idmodule;
    }

    @Override
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    @Override
    protected BManager getManager() {
        return new AFLettreTaxeCo2Manager();
    }

    public String getMontant() {
        return montant;
    }

    public void setIdmodule(String idmodule) {
        this.idmodule = idmodule;
    }

    @Override
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}

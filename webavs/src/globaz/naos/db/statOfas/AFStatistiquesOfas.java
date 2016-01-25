package globaz.naos.db.statOfas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFStatistiquesOfas extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_GENRE = "MBTGEN";
    public static final String FIELDNAME_IDCANTON = "HJICAN";
    public static final String FIELDNAME_MONTANT = "MONTANT";
    public static final String FIELDNAME_NOMBRE = "NOMBRE";
    public static final String FIELDNAME_TYPE_AFFILIE = "MATTAF";

    private String genre = new String();
    private String idCanton = new String();
    private String montant = new String();
    private String nombre = new String();
    // DB
    // Fields
    private String typeAffiliation = new String();

    /**
     * Constructeur de AFAffiliation
     */
    public AFStatistiquesOfas() {
        super();
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        typeAffiliation = statement.dbReadNumeric(AFStatistiquesOfas.FIELDNAME_TYPE_AFFILIE);
        genre = statement.dbReadNumeric(AFStatistiquesOfas.FIELDNAME_GENRE);
        nombre = statement.dbReadNumeric(AFStatistiquesOfas.FIELDNAME_NOMBRE);
        montant = statement.dbReadNumeric(AFStatistiquesOfas.FIELDNAME_MONTANT);
        idCanton = statement.dbReadNumeric(AFStatistiquesOfas.FIELDNAME_IDCANTON);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getGenre() {
        return genre;
    }

    public String getIdCanton() {
        return idCanton;
    }

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFStatistiquesOfasManager();
    }

    public String getMontant() {
        if (!JadeStringUtil.isBlankOrZero(montant)) {
            return JANumberFormatter.round(montant, 0.05, 2, JANumberFormatter.NEAR);
        } else {
            return "0.00";
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setIdCanton(String idCanton) {
        this.idCanton = idCanton;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }
}

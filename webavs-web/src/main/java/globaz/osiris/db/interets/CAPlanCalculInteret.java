package globaz.osiris.db.interets;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CAPlanCalculInteret extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_ESTACTIF = "ESTACTIF";
    public static final String FIELD_IDPLACALINT = "IDPLACALINT";
    public static final String FIELD_LIBELLE_DE = "libelleDE";
    public static final String FIELD_LIBELLE_FR = "libelleFR";
    public static final String FIELD_LIBELLE_IT = "libelleIT";

    public static final String TABLE_CAIMPLP = "CAIMPLP";

    private Boolean estActif = Boolean.FALSE;
    private String idPlanCalculInteret = new String();
    private String libelleDE = new String();
    private String libelleFR = new String();
    private String libelleIT = new String();

    /**
     * Commentaire relatif au constructeur CAPlanCalculInteret
     */
    public CAPlanCalculInteret() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CAPlanCalculInteret.TABLE_CAIMPLP;
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idPlanCalculInteret = statement.dbReadNumeric(CAPlanCalculInteret.FIELD_IDPLACALINT);
        libelleFR = statement.dbReadString(CAPlanCalculInteret.FIELD_LIBELLE_FR);
        libelleDE = statement.dbReadString(CAPlanCalculInteret.FIELD_LIBELLE_DE);
        libelleIT = statement.dbReadString(CAPlanCalculInteret.FIELD_LIBELLE_IT);
        estActif = statement.dbReadBoolean(CAPlanCalculInteret.FIELD_ESTACTIF);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**

	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAPlanCalculInteret.FIELD_IDPLACALINT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanCalculInteret(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(CAPlanCalculInteret.FIELD_IDPLACALINT,
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanCalculInteret(), "idPlanCalculInteret"));
        statement.writeField(CAPlanCalculInteret.FIELD_LIBELLE_FR,
                this._dbWriteString(statement.getTransaction(), getLibelleFR(), CAPlanCalculInteret.FIELD_LIBELLE_FR));
        statement.writeField(CAPlanCalculInteret.FIELD_LIBELLE_DE,
                this._dbWriteString(statement.getTransaction(), getLibelleDE(), CAPlanCalculInteret.FIELD_LIBELLE_DE));
        statement.writeField(CAPlanCalculInteret.FIELD_LIBELLE_IT,
                this._dbWriteString(statement.getTransaction(), getLibelleIT(), CAPlanCalculInteret.FIELD_LIBELLE_IT));
        statement.writeField(CAPlanCalculInteret.FIELD_ESTACTIF, this._dbWriteBoolean(statement.getTransaction(),
                getEstActif(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estActif"));
    }

    public Boolean getEstActif() {
        return estActif;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdPlanCalculInteret() {
        return idPlanCalculInteret;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 18:35:22)
     * 
     * @return java.lang.String
     */
    public String getLibelle() {
        if (getSession().getIdLangueISO().equalsIgnoreCase("FR")) {
            return libelleFR;
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
            return libelleDE;
        } else if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
            return libelleIT;
        } else {
            return libelleFR;
        }
    }

    public String getLibelleDE() {
        return libelleDE;
    }

    public String getLibelleFR() {
        return libelleFR;
    }

    public String getLibelleIT() {
        return libelleIT;
    }

    public void setEstActif(Boolean newEstactif) {
        estActif = newEstactif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdPlanCalculInteret(String newIdPlanCalculInteret) {
        idPlanCalculInteret = newIdPlanCalculInteret;
    }

    public void setLibelleDE(String newLibelleDE) {
        libelleDE = newLibelleDE;
    }

    public void setLibelleFR(String newLibelleFR) {
        libelleFR = newLibelleFR;
    }

    public void setLibelleIT(String newLibelleIT) {
        libelleIT = newLibelleIT;
    }
}

/*
 * Créé le 21 janvier 2010
 */
package globaz.cygnus.db.qds;

import globaz.globall.db.BStatement;
import globaz.prestation.tools.sql.PRFromStringBuffer;

/**
 * @author jje
 */
public class RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf extends RFPeriodeValiditeQdPrincipale {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ANCIEN_CONCERNE = "FCLCON_A";
    public static final String FIELDNAME_ANCIEN_DATE_DEBUT = "FCDDEB_A";
    public static final String FIELDNAME_ANCIEN_DATE_FIN = "FCDFIN_A";
    public static final String FIELDNAME_ANCIEN_DATE_MODIF = "FCDMOD_A";
    public static final String FIELDNAME_ANCIEN_GESTIONNAIRE = "FCIGES_A";

    public static final String FIELDNAME_ANCIEN_REMARQUE = "FCLREM_A";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > Jointure de l'augmentation de Qd sur elle même
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        PRFromStringBuffer fromClauseBuffer = new PRFromStringBuffer(schema);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPeriodeValiditeQdPrincipale.TABLE_NAME + " n");

        // jointure entre la table des Qd et la table des QdPrinicpale

        fromClauseBuffer.leftJoin(RFPeriodeValiditeQdPrincipale.TABLE_NAME + " a").appendOn()
                .append("n." + FIELDNAME_ID_PERIODE_VAL_MODIFIE_PAR);
        fromClauseBuffer.appendEgal().append("a." + FIELDNAME_ID_PERIODE_VALIDITE);

        return fromClauseBuffer.toString();
    }

    // private String ancienRemarque = "";
    private String ancienConcerne = "";
    private String ancienDateDebut = "";
    private String ancienDateFin = "";
    private String ancienDateModif = "";
    private String ancienGestionnaire = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "n.*,a." + FIELDNAME_DATE_MODIFICATION + " " + FIELDNAME_ANCIEN_DATE_MODIF + ",a." + FIELDNAME_CONCERNE
                + " " + FIELDNAME_ANCIEN_CONCERNE + ",a." + FIELDNAME_DATE_DEBUT + " " + FIELDNAME_ANCIEN_DATE_DEBUT
                + ",a." + FIELDNAME_DATE_FIN + " " + FIELDNAME_ANCIEN_DATE_FIN + ",a." + FIELDNAME_ID_GESTIONNAIRE
                + " " + FIELDNAME_ANCIEN_GESTIONNAIRE;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        ancienDateModif = statement.dbReadDateAMJ(FIELDNAME_ANCIEN_DATE_MODIF);
        ancienDateDebut = statement.dbReadDateAMJ(FIELDNAME_ANCIEN_DATE_DEBUT);
        ancienDateFin = statement.dbReadDateAMJ(FIELDNAME_ANCIEN_DATE_FIN);
        // ancienRemarque = statement.dbReadString(FIELDNAME_ANCIEN_REMARQUE);
        ancienConcerne = statement.dbReadString(FIELDNAME_ANCIEN_CONCERNE);
        ancienGestionnaire = statement.dbReadNumeric(FIELDNAME_ANCIEN_GESTIONNAIRE);

    }

    public String getAncienConcerne() {
        return ancienConcerne;
    }

    public String getAncienDateDebut() {
        return ancienDateDebut;
    }

    public String getAncienDateFin() {
        return ancienDateFin;
    }

    public String getAncienDateModif() {
        return ancienDateModif;
    }

    public String getAncienGestionnaire() throws Exception {
        return ancienGestionnaire;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setAncienConcerne(String ancienConcerne) {
        this.ancienConcerne = ancienConcerne;
    }

    public void setAncienDateDebut(String ancienDateDebut) {
        this.ancienDateDebut = ancienDateDebut;
    }

    public void setAncienDateFin(String ancienDateFin) {
        this.ancienDateFin = ancienDateFin;
    }

    public void setAncienDateModif(String ancienDateModif) {
        this.ancienDateModif = ancienDateModif;
    }

    public void setAncienGestionnaire(String ancienGestionnaire) {
        this.ancienGestionnaire = ancienGestionnaire;
    }

}

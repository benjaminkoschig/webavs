/*
 * Créé le 25 novembre 2009
 */
package globaz.cygnus.db.dossiers;

import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author jje
 */
public class RFDossierJointPrDemande extends RFDossier {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers, demandes jusqu'au tiers
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);

        // jointure entre la table des dossiers et la table des demandes
        // prestation
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDossier.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDossier.FIELDNAME_ID_PRDEM);

        return fromClauseBuffer.toString();
    }

    private transient String fromClause = null;
    private String idDossier = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // champs nécessaires description tiers
    private String idTiers = "";

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
     * Lecture des propriétés dans les champs de la requête
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idTiers = statement.dbReadNumeric(statement.dbReadString(PRDemande.FIELDNAME_IDTIERS));
        idDossier = statement.dbReadNumeric(statement.dbReadString(FIELDNAME_ID_DOSSIER));

    }

    @Override
    public String getIdDossier() {
        return idDossier;
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    @Override
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}

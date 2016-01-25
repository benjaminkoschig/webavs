/*
 * Créé le 19 février 2010
 */
package globaz.cygnus.db.motifsDeRefus;

import globaz.globall.db.BStatement;

/**
 * @author jje
 */
public class RFAssMotifsRefusDemandeJointMotifRefus extends RFMotifsDeRefus {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_ASS = "EGIADM";
    public static final String FIELDNAME_ID_DEMANDE = "EGIDEM";
    public static final String FIELDNAME_ID_MOTIF_REFUS = "EGIMOT";
    public static final String FIELDNAME_MNT_MOTIF_REFUS = "EGMNTM";

    public static final String TABLE_NAME = "RFADEMO";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * Génération de la clause from pour la requête > des Qds jusqu'au tiers
     * 
     * @param schema
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);

        // Jointure entre la table des motifs de refus et la table association motif de refus demande
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);

        return fromClauseBuffer.toString();
    }

    private String idAssMotifsRefus = "";
    private String idDemande = "";
    private String idMotifsRefus = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String mntMotifsDeRefus = "";

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFAssMotifsRefusDemande
     */
    public RFAssMotifsRefusDemandeJointMotifRefus() {
        super();
    }

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
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
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * Lecture des propriétés dans les champs de la table association motifs refus demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idAssMotifsRefus = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_ID_ASS);
        idDemande = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);
        idMotifsRefus = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);
        mntMotifsDeRefus = statement.dbReadNumeric(RFAssMotifsRefusDemande.FIELDNAME_MNT_MOTIF_REFUS);

    }

    public String getIdAssMotifsRefus() {
        return idAssMotifsRefus;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdMotifsRefus() {
        return idMotifsRefus;
    }

    public String getMntMotifsDeRefus() {
        return mntMotifsDeRefus;
    }

}

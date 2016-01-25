/*
 * Créé le 11 novembre 2009
 */
package globaz.cygnus.db.demandes;

import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoin;
import globaz.cygnus.db.typeDeSoins.RFTypeDeSoin;
import globaz.globall.db.BStatement;

/**
 * @author jje
 */
public class RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefus extends RFDemande {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Génération de la clause from pour la requête > Jointure depuis les dossiers jusqu'au décisions RFM
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String different = "<>";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);

        // jointure entre la table des demandes RFM et la table des sous-type de
        // soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_SOUS_TYPE_DE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_SOUS_TYPE_SOIN);

        // jointure entre la table des sous-types de soin et la table des types
        // de soin
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFSousTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFSousTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFTypeDeSoin.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFTypeDeSoin.FIELDNAME_ID_TYPE_SOIN);

        // jointure entre la table des demandes RFM et la table association
        // motifs de refus/demandes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFDemande.FIELDNAME_ID_DEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_DEMANDE);

        // jointure entre la table association motifs de refus/demande et la
        // table des motifs de refus
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFAssMotifsRefusDemande.FIELDNAME_ID_MOTIF_REFUS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFMotifsDeRefus.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);

        return fromClauseBuffer.toString();
    }

    private String codeSousTypeDeSoin = "";
    private String codeTypeDeSoin = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String idMotifRefus = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDemande.
     */
    public RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefus() {
        super();
    }

    /**
     * Lecture des propriétés dans les champs de la table des demandes
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        codeSousTypeDeSoin = statement.dbReadString(RFSousTypeDeSoin.FIELDNAME_CODE);
        codeTypeDeSoin = statement.dbReadString(RFTypeDeSoin.FIELDNAME_CODE);
        idMotifRefus = statement.dbReadNumeric(RFMotifsDeRefus.FIELDNAME_ID_MOTIF_REFUS);
    }

    public String getCodeSousTypeDeSoin() {
        return codeSousTypeDeSoin;
    }

    public String getCodeTypeDeSoin() {
        return codeTypeDeSoin;
    }

    public String getIdMotifRefus() {
        return idMotifRefus;
    }

    @Override
    public boolean hasCreationSpy() {
        return false;
    }

    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCodeSousTypeDeSoin(String codeSousTypeDeSoin) {
        this.codeSousTypeDeSoin = codeSousTypeDeSoin;
    }

    public void setCodeTypeDeSoin(String codeTypeDeSoin) {
        this.codeTypeDeSoin = codeTypeDeSoin;
    }

    public void setIdMotifRefus(String idMotifRefus) {
        this.idMotifRefus = idMotifRefus;
    }

}
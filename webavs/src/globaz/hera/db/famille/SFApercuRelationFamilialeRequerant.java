/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BStatement;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author jpa jointure entre les membres de la famille, la relation avec le requérant et donc le requérant
 */
public class SFApercuRelationFamilialeRequerant extends SFMembreFamille implements ISFMembreFamilleRequerant {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** DOCUMENT ME! */
    public static final String FIELD_IDRELATIONFAMILIALE = SFRelationFamilialeRequerant.FIELD_IDRELATIONFAMILIALE;

    /** DOCUMENT ME! */
    public static final String FIELD_IDREQUERANT = SFRelationFamilialeRequerant.FIELD_IDREQUERANT;

    /** DOCUMENT ME! */
    public static final String TABLE_MEMBREFAMILLE = SFMembreFamille.TABLE_NAME;

    /** DOCUMENT ME! */
    public static final String TABLE_RELATIONFAMILIALEREQUERENT = SFRelationFamilialeRequerant.TABLE_NAME;

    /**
     * Renvoie la clause from selon si idRequerant est renseigné ou non S'il n'est pas renseiné, le requête se fait
     * uniquement sur les membre famille sinon égallement sur la table RelationRequerant
     * 
     * @param schema
     *            , le nom de la base de donnée (p.ex webavsp)
     * @param idRequerant
     *            , le nom du requerant dont on souhaite avoir la famille, peut être null
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema, String idRequerant) {

        if (JadeStringUtil.isEmpty(idRequerant)) {
            return SFMembreFamille.createFromClause(schema);
        }
        StringBuffer fromClauseBuffer = new StringBuffer(SFMembreFamille.createFromClause(schema));
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";
        String lp = "(";
        String rp = ")";
        String as = " AS ";

        // jointure entre table des membres famille et
        // RelationFamilialeRequerent
        // ADO : était en commentaire, commentaire enlevé

        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_RELATIONFAMILIALEREQUERENT);
        fromClauseBuffer.append(as);
        fromClauseBuffer.append(TABLE_RELATIONFAMILIALEREQUERENT);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(lp);
        fromClauseBuffer.append(TABLE_MEMBREFAMILLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELD_IDMEMBREFAMILLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(TABLE_RELATIONFAMILIALEREQUERENT);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(SFRelationFamilialeRequerant.FIELD_IDMEMBREFAMILLE);
        fromClauseBuffer.append(rp);

        return fromClauseBuffer.toString();

    }

    private boolean checked_relationRequerant = false;

    private String fromClause = null;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String idRelationFamiliale = "";

    private String idRequerant = "";
    private String oldNoAvs = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    // Business fields
    private String relationRequerant = null;

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = createFromClause(_getCollection());
        }

        return fromClause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRelationFamiliale = statement.dbReadNumeric(FIELD_IDRELATIONFAMILIALE);
        idRequerant = statement.dbReadNumeric(FIELD_IDREQUERANT);

        super._readProperties(statement);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdRelationFamiliale() {
        return idRelationFamiliale;
    }

    // public static final String createFromClause(String schema) {
    // return createFromClause(schema, null);
    // }
    //

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /**
     * @return
     */
    public String getOldNoAvs() {
        return oldNoAvs;
    }

    /**
     * Renvoie le type de relation du membre de famille avec le requerant CodeSystem (enfant/conjoint) ou null si
     * relation indefinie ISFSituationFamiliale.CS_TYPE_RELATION_...
     */
    @Override
    public String getRelationAuRequerant() {
        if (!checked_relationRequerant) {
            checked_relationRequerant = true;
            relationRequerant = getRelationAuRequerant(getIdRequerant());
        }
        return relationRequerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdRelationFamiliale(String string) {
        idRelationFamiliale = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    /**
     * @param string
     */
    public void setOldNoAvs(String string) {
        oldNoAvs = string;
    }

}

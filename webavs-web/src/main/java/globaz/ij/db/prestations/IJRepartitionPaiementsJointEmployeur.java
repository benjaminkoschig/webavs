/*
 * Créé le 24 oct. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BStatement;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.ij.db.prononces.IJSituationProfessionnelle;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartitionPaiementsJointEmployeur extends IJRepartitionJointPrestation {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer = new StringBuffer(IJRepartitionJointPrestation.createFromClause(schema));

        // jointure avec sit pro
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJRepartitionPaiements.FIELDNAME_IDSITUATIONPROFESSIONNELLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJSituationProfessionnelle.FIELDNAME_ID_SITUATION_PROFESSIONNELLE);

        // jointure entre table des situations professionnelles et des
        // employeurs
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJEmployeur.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJSituationProfessionnelle.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJSituationProfessionnelle.FIELDNAME_ID_EMPLOYEUR);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJEmployeur.FIELDNAME_ID_EMPLOYEUR);

        return fromClauseBuffer.toString();
    }

    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idParticularite = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
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

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idParticularite = statement.dbReadNumeric(IJEmployeur.FIELDNAME_ID_PARTICULARITE);
    }

    /**
     * getter pour l'attribut id particulier
     * 
     * @return la valeur courante de l'attribut id particulier
     */
    public String getIdParticularite() {
        return idParticularite;
    }

    /**
     * setter pour l'attribut id particulier
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParticularite(String string) {
        idParticularite = string;
    }
}

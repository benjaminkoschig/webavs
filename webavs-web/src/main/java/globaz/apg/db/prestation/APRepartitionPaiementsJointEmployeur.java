/*
 * Créé le 24 oct. 05
 */
package globaz.apg.db.prestation;

import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APRepartitionPaiementsJointEmployeur extends APRepartitionJointPrestation {

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

        fromClauseBuffer = new StringBuffer(APRepartitionJointPrestation.createFromClause(schema));

        // jointure avec sit pro
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APRepartitionPaiements.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APRepartitionPaiements.FIELDNAME_IDSITUATIONPROFESSIONNELLE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APSituationProfessionnelle.FIELDNAME_IDSITUATIONPROF);

        // jointure entre table des situations professionnelles et des
        // employeurs
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APSituationProfessionnelle.TABLE_NAME_SITUATION_PROFESSIONNELLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APSituationProfessionnelle.FIELDNAME_IDEMPLOYEUR);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APEmployeur.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APEmployeur.FIELDNAME_ID_EMPLOYEUR);

        return fromClauseBuffer.toString();
    }

    private String fromClause = null;

    private String idParticularite = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private Boolean isIndependant = Boolean.FALSE;

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
        idParticularite = statement.dbReadNumeric(APEmployeur.FIELDNAME_ID_PARTICULARITE);
        isIndependant = statement.dbReadBoolean((APSituationProfessionnelle.FIELDNAME_ISINDEPENDANT));
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
     * @return
     */
    public Boolean getIsIndependant() {
        return isIndependant;
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

    /**
     * @param boolean1
     */
    public void setIsIndependant(Boolean boolean1) {
        isIndependant = boolean1;
    }

}

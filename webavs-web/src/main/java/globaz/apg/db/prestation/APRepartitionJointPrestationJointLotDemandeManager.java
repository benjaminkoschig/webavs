/*
 * Créé le 11 juil. 05
 */
package globaz.apg.db.prestation;

import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;

import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APRepartitionJointPrestationJointLotDemandeManager extends APRepartitionJointPrestationJointLotManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String forTypeDemande = "";

    /**
     * @see globaz.globall.db.BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String schema = _getCollection();
        String from = APRepartitionJointPrestationJointLot.createFromClause(schema);

        // jointure avec la table des droits
        final StringBuffer buffer = new StringBuffer(from);

        // jointure entre table droitlapg et demandes
        buffer.append(" INNER JOIN ");
        buffer.append(schema);
        buffer.append(PRDemande.TABLE_NAME);
        buffer.append(" ON ");
        buffer.append(schema);
        buffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        buffer.append(".");
        buffer.append(APDroitLAPG.FIELDNAME_IDDEMANDE);
        buffer.append("=");
        buffer.append(schema);
        buffer.append(PRDemande.TABLE_NAME);
        buffer.append(".");
        buffer.append(PRDemande.FIELDNAME_IDDEMANDE);

        return  buffer.toString();
    }

    /**
     * (non-Javadoc).
     *
     * @param statement
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @see globaz.globall.db.BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = super._getWhere(statement);
        String schema = _getCollection();

        if (!JadeStringUtil.isIntegerEmpty(forTypeDemande)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += schema + PRDemande.TABLE_NAME + "." + PRDemande.FIELDNAME_TYPE_DEMANDE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forTypeDemande);
        }

        return sqlWhere;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APRepartitionJointPrestation();
    }

    public String getForTypeDemande() {
        return forTypeDemande;
    }

    public void setForTypeDemande(String forTypeDemande) {
        this.forTypeDemande = forTypeDemande;
    }
}

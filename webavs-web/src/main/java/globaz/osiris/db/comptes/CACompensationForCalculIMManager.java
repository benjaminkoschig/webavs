/*
 * Globaz SA
 */
package globaz.osiris.db.comptes;

import globaz.aquila.api.ICOContentieuxConstante;
import globaz.aquila.api.ICOEtape;
import globaz.aquila.api.ICOEtapeConstante;
import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CACompensationForCalculIMManager extends BManager {

    private static final long serialVersionUID = 1L;
    private static final String AND = " AND ";
    private static final String INNER_JOIN = " INNER JOIN ";
    private static final String LIKE = " LIKE ";
    private static final String ON = " ON ";
    private String forIdJournal;

    @Override
    protected String _getFields(BStatement statement) {
        return "CAOPERP.*";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sqlFrom = new StringBuilder();

        // from db2cott.caoperp o
        sqlFrom.append(_getCollection()).append(CAOperation.TABLE_CAOPERP).append(" ")
                .append(CAOperation.TABLE_CAOPERP);

        // Section
        // INNER JOIN DB2COTT.CASECTP CASECTP ON casectp.IDSECTION= o.idsection
        sqlFrom.append(CACompensationForCalculIMManager.INNER_JOIN).append(_getCollection())
                .append(CASection.TABLE_CASECTP).append(" ").append(CASection.TABLE_CASECTP);
        sqlFrom.append(CACompensationForCalculIMManager.ON).append(CAOperation.TABLE_CAOPERP).append(".")
                .append(CAOperation.FIELD_IDSECTION);
        sqlFrom.append(" = ").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION);

        // Contentieux
        // INNER JOIN DB2COTT.COCAVSP COCAVSP ON COCAVSP.OAISEC=CASECTP.IDSECTION
        sqlFrom.append(CACompensationForCalculIMManager.INNER_JOIN).append(_getCollection())
                .append(ICOContentieuxConstante.TABLE_NAME_AVS).append(" ")
                .append(ICOContentieuxConstante.TABLE_NAME_AVS);
        sqlFrom.append(CACompensationForCalculIMManager.ON).append(ICOContentieuxConstante.TABLE_NAME_AVS).append(".")
                .append(ICOContentieuxConstante.FNAME_ID_SECTION);
        sqlFrom.append(" = ").append(CASection.TABLE_CASECTP).append(".").append(CASection.FIELD_IDSECTION);

        // Historique
        // INNER JOIN DB2COTT.COHISTP COHISTP ON COHISTP.OAICON=COCAVSP.OAICON
        sqlFrom.append(CACompensationForCalculIMManager.INNER_JOIN).append(_getCollection())
                .append(ICOHistoriqueConstante.TABLE_NAME).append(" ").append(ICOHistoriqueConstante.TABLE_NAME);
        sqlFrom.append(CACompensationForCalculIMManager.ON).append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_CONTENTIEUX);
        sqlFrom.append(" = ").append(ICOContentieuxConstante.TABLE_NAME_AVS).append(".")
                .append(ICOContentieuxConstante.FNAME_ID_CONTENTIEUX);

        // Etape
        // INNER JOIN DB2COTT.COETAPP COETAPP ON COETAPP.ODIETA=COHISTP.ODIETA
        sqlFrom.append(CACompensationForCalculIMManager.INNER_JOIN).append(_getCollection())
                .append(ICOEtapeConstante.TABLE_NAME).append(" ").append(ICOEtapeConstante.TABLE_NAME);
        sqlFrom.append(CACompensationForCalculIMManager.ON).append(ICOEtapeConstante.TABLE_NAME).append(".")
                .append(ICOEtapeConstante.FNAME_ID_ETAPE);
        sqlFrom.append(" = ").append(ICOHistoriqueConstante.TABLE_NAME).append(".")
                .append(ICOHistoriqueConstante.FNAME_ID_ETAPE);

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sqlWhere = new StringBuilder();
        sqlWhere.append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_IDTYPEOPERATION);
        sqlWhere.append(CACompensationForCalculIMManager.LIKE).append("'EC%'");

        sqlWhere.append(CACompensationForCalculIMManager.AND);
        sqlWhere.append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_CODEMASTER);
        sqlWhere.append(" IN (1, 2)");

        sqlWhere.append(CACompensationForCalculIMManager.AND);
        sqlWhere.append(ICOHistoriqueConstante.TABLE_NAME).append(".").append(ICOHistoriqueConstante.FNAME_EST_ANNULE);
        sqlWhere.append(" = ");
        sqlWhere.append(this._dbWriteBoolean(statement.getTransaction(), false, BConstants.DB_TYPE_BOOLEAN_CHAR,
                ICOHistoriqueConstante.FNAME_EST_ANNULE));

        sqlWhere.append(CACompensationForCalculIMManager.AND);
        sqlWhere.append(ICOEtapeConstante.TABLE_NAME).append(".").append(ICOEtapeConstante.FNAME_LIBETAPE);
        sqlWhere.append(" = ");
        sqlWhere.append(ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);

        if (!JadeStringUtil.isBlankOrZero(getForIdJournal())) {
            sqlWhere.append(CACompensationForCalculIMManager.AND);
            sqlWhere.append(CAOperation.TABLE_CAOPERP).append(".").append(CAOperation.FIELD_IDJOURNAL);
            sqlWhere.append(" = ").append(getForIdJournal());
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEcriture();
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

}

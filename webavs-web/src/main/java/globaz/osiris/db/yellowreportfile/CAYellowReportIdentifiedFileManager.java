package globaz.osiris.db.yellowreportfile;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CAYellowReportIdentifiedFileManager extends BManager {

    private static final long serialVersionUID = 4110359273298651207L;

    private String forFileName = null;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAYellowReportIdentifiedFile();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAYellowReportIdentifiedFile.TABLE_NAME;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();

        // Recherche sur le nom de fichier
        if (getForFileName() != null) {
            addANDIfNeeded(sqlWhere);
            sqlWhere.append(CAYellowReportIdentifiedFile.FIELD_FILENAME + " = "
                    + this._dbWriteString(statement.getTransaction(), getForFileName()));
        }

        return sqlWhere.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return CAYellowReportIdentifiedFile.FIELD_DATE_CREATED + " DESC ";
    }

    private void addANDIfNeeded(final StringBuilder sqlWhere) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
    }

    public String getForFileName() {
        return forFileName;
    }

    public void setForFileName(String forFileName) {
        this.forFileName = forFileName;
    }
}

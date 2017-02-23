package globaz.osiris.db.yellowreportfile;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CAYellowReportFileManager extends BManager {

    private static final long serialVersionUID = -1425555204154758158L;
    public static final String OUI = "OUI";
    public static final String NON = "NON";
    public static final String TOUS = "";

    private CAYellowReportFileType forTypeOfFile = null;
    private List<CAYellowReportFileState> forInStateOfFile = new ArrayList<CAYellowReportFileState>();
    private String forFileName = null;
    private String forTreatedFile = TOUS;
    private String sinceDate = null;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAYellowReportFile();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAYellowReportFile.TABLE_NAME;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();

        // Recherche sur le type de fichier
        if (getForTypeOfFile() != null) {
            addANDIfNeeded(sqlWhere);
            sqlWhere.append(CAYellowReportFile.FIELD_TYPEOF + " = "
                    + this._dbWriteString(statement.getTransaction(), getForTypeOfFile().name()));
        }

        // Recherche sur les différents états (IN)
        if (!getForInStateOfFile().isEmpty()) {
            addANDIfNeeded(sqlWhere);
            sqlWhere.append(concatForInStateIN(statement, getForInStateOfFile()));
        }

        // Recherche sur le nom de fichier
        if (getForFileName() != null) {
            addANDIfNeeded(sqlWhere);
            sqlWhere.append(CAYellowReportFile.FIELD_FILENAME + " = "
                    + this._dbWriteString(statement.getTransaction(), getForFileName()));
        }

        // Recherche que les fichiers traités (true), non traités (false) ou tous (null)
        if (getForTreatedFile() != null && getForTreatedFile().length() != 0) {
            addANDIfNeeded(sqlWhere);

            if (OUI.equals(getForTreatedFile())) {
                sqlWhere.append(concatForInStateIN(statement, Arrays.asList(CAYellowReportFileState.EXECUTED,
                        CAYellowReportFileState.FAILED, CAYellowReportFileState.PARTIAL,
                        CAYellowReportFileState.IN_TREATMENT)));
            } else if (NON.equals(getForTreatedFile())) {
                sqlWhere.append(concatForInStateIN(statement, Arrays.asList(CAYellowReportFileState.IDENTIFIED)));
            }
        }

        // Recherche la date de création d'une identification de fichier ISO à partir de la date donnée.
        if (getSinceDate() != null && getSinceDate().length() != 0) {
            final Date date = JadeDateUtil.getGlobazDate(getSinceDate());

            addANDIfNeeded(sqlWhere);
            sqlWhere.append(CAYellowReportFile.FIELD_DATE_CREATED + " >= " + date.getTime());
        }

        return sqlWhere.toString();
    }

    private String concatForInStateIN(final BStatement statement, final List<CAYellowReportFileState> states) {

        final StringBuilder builderForIn = new StringBuilder();
        for (CAYellowReportFileState state : states) {
            if (builderForIn.length() != 0) {
                builderForIn.append(",");
            }
            builderForIn.append(this._dbWriteString(statement.getTransaction(), state.name()));
        }

        return CAYellowReportFile.FIELD_STATE + " IN (" + builderForIn.toString() + ")";
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return CAYellowReportFile.FIELD_DATE_CREATED + " DESC ";
    }

    private void addANDIfNeeded(final StringBuilder sqlWhere) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
    }

    public void setForStateOfFile(final String forStateOfFile) {
        CAYellowReportFileState state = CAYellowReportFileState.getEnumFromName(forStateOfFile);

        if (forInStateOfFile.isEmpty()) {
            forInStateOfFile.add(state);
        } else {
            forInStateOfFile.set(0, state);
        }
    }

    public List<CAYellowReportFileState> getForInStateOfFile() {
        return forInStateOfFile;
    }

    public void setForInStateOfFile(final List<CAYellowReportFileState> forInStateOfFile) {
        this.forInStateOfFile = forInStateOfFile;
    }

    public CAYellowReportFileType getForTypeOfFile() {
        return forTypeOfFile;
    }

    public void setForTypeOfFile(final CAYellowReportFileType forTypeOfFile) {
        this.forTypeOfFile = forTypeOfFile;
    }

    public void setForTypeOfFile(final String forTypeOfFile) {
        setForTypeOfFile(CAYellowReportFileType.getEnumFromName(forTypeOfFile));
    }

    public String getForFileName() {
        return forFileName;
    }

    public void setForFileName(String forFileName) {
        this.forFileName = forFileName;
    }

    public String getForTreatedFile() {
        return forTreatedFile;
    }

    public void setForTreatedFile(String forTreatedFile) {
        this.forTreatedFile = forTreatedFile;
    }

    public String getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(String sinceDateDDxMMxYYYY) {
        sinceDate = sinceDateDDxMMxYYYY;
    }
}

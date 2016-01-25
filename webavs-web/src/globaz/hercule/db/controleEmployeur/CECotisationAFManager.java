package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 18 jan. 2011
 */
public class CECotisationAFManager extends BManager {

    private static final long serialVersionUID = 3922587240617608371L;
    private String idAffilie = "";

    @Override
    protected String _getSqlCount(BStatement statement) {

        StringBuffer sqlBuffer = new StringBuffer();

        // SELECT *
        // FROM webavsciam.AFAFFIP AF
        // INNER JOIN webavsciam.AFPLAFP PLA ON (AF.MAIAFF=PLA.MAIAFF)
        // INNER JOIN webavsciam.AFCOTIP COT ON (PLA.MUIPLA=COT.MUIPLA)
        // INNER JOIN webavsciam.AFASSUP ASS ON (COT.MBIASS = ASS.MBIASS)
        // WHERE ASS.MBTTYP=812002

        sqlBuffer.append("SELECT COUNT(*) FROM ").append(_getCollection()).append("AFAFFIP AF");
        sqlBuffer.append(" INNER JOIN ").append(_getCollection()).append("AFPLAFP PLA ON (AF.MAIAFF=PLA.MAIAFF)");
        sqlBuffer.append(" INNER JOIN ").append(_getCollection()).append("AFCOTIP COT ON (PLA.MUIPLA=COT.MUIPLA)");
        sqlBuffer.append(" INNER JOIN ").append(_getCollection()).append("AFASSUP ASS ON (COT.MBIASS = ASS.MBIASS)");
        sqlBuffer.append(" WHERE ASS.MBTTYP=812002");
        sqlBuffer.append(" AND AF.MAIAFF = ").append(getIdAffilie());

        return sqlBuffer.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }
}

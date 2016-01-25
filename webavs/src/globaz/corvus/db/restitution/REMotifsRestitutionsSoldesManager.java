package globaz.corvus.db.restitution;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.contentieux.CAMotifContentieux;

public class REMotifsRestitutionsSoldesManager extends BManager {

    private static final long serialVersionUID = 1L;
    private String forIdSectionIn = null;
    private String forLangue = null;

    public String getForLangue() {
        return forLangue;
    }

    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    public final static String CODE_SYSTEME_LABEL_FIELD = "PCOLUT";
    public final static String CODE_SYSTEME_LABEL_ID = "PCOSID";
    public final static String CODE_SYSTEME_LABEL_LANGUE = "PLAIDE";
    public final static String CODE_SYSTEME_LABEL_TABLE_NAME = "FWCOUP";

    @Override
    protected String _getSql(BStatement statement) {
        if (JadeStringUtil.isBlank(forIdSectionIn)) {
            throw new NullPointerException("must have a list of idSection for this request");
        }

        if (JadeStringUtil.isBlank(forLangue)) {
            throw new NullPointerException("must have a forLangue for this request");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(CAMotifContentieux.FIELD_IDMOTIFBLOCAGE);
        sql.append(", ").append(CAMotifContentieux.FIELD_DATEDEBUT);
        sql.append(", ").append(CAMotifContentieux.FIELD_DATEFIN);
        sql.append(", ").append(CAMotifContentieux.FIELD_COMMENTAIRE);
        sql.append(", ").append(CAMotifContentieux.FIELD_IDSECTION);
        sql.append(", ").append(CODE_SYSTEME_LABEL_FIELD);

        // Motifs contentieux
        sql.append(" FROM ").append(_getCollection()).append(CAMotifContentieux.TABLE_CAMOCOP).append(" ")
                .append(CAMotifContentieux.TABLE_CAMOCOP);

        // Code sytsème label motif contentieux
        sql.append(" LEFT JOIN ").append(_getCollection()).append(CODE_SYSTEME_LABEL_TABLE_NAME).append(" ")
                .append(CODE_SYSTEME_LABEL_TABLE_NAME);
        sql.append(" ON (").append(CODE_SYSTEME_LABEL_TABLE_NAME).append(".").append(CODE_SYSTEME_LABEL_ID);
        sql.append("=").append(CAMotifContentieux.TABLE_CAMOCOP).append(".")
                .append(CAMotifContentieux.FIELD_IDMOTIFBLOCAGE);
        sql.append(" AND ").append(CODE_SYSTEME_LABEL_TABLE_NAME).append(".").append(CODE_SYSTEME_LABEL_LANGUE)
                .append(" = '").append(getForLangue()).append("')");

        // where
        sql.append(" WHERE ").append(CAMotifContentieux.FIELD_IDSECTION).append(" IN (").append(getForIdSectionIn())
                .append(")");

        return sql.toString();
    }

    public String getForIdSectionIn() {
        return forIdSectionIn;
    }

    public void setForIdSectionIn(String forIdSectionIn) {
        this.forIdSectionIn = forIdSectionIn;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REMotifsRestitutionsSoldes();
    }

}

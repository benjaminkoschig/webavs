package globaz.naos.db.statOfas;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

public abstract class AFInfoRom060StatistiquesOfasManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String adhesion = "afadhep";
    public final static String affiliation = "afaffip";
    /**
     * Constantes
     */
    public final static String AND = " AND ";
    public final static String assurance = "afassup";
    public final static String cotisation = "afcotip";
    public final static String cptAnnexe = "cacptap";
    public final static String journal = "cajourp";
    public final static String operation = "caoperp";

    /**
     * Attributs
     */
    private String forAnnee = "";
    private String forBrancheEconomique = "";
    private String forNotBrancheEconomique = "";
    private Boolean forPersonnelMaison = null;
    private String forTypeAffiliation = "";

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer();

        from.append(_getCollection() + AFInfoRom060StatistiquesOfasManager.affiliation + " "
                + AFInfoRom060StatistiquesOfasManager.affiliation + " INNER JOIN " + _getCollection()
                + AFInfoRom060StatistiquesOfasManager.adhesion + " " + AFInfoRom060StatistiquesOfasManager.adhesion
                + " ON (" + AFInfoRom060StatistiquesOfasManager.affiliation + ".MAIAFF="
                + AFInfoRom060StatistiquesOfasManager.adhesion + ".MAIAFF   )" + " INNER JOIN " + _getCollection()
                + AFInfoRom060StatistiquesOfasManager.cotisation + " " + AFInfoRom060StatistiquesOfasManager.cotisation
                + " ON (" + AFInfoRom060StatistiquesOfasManager.adhesion + ".MRIADH="
                + AFInfoRom060StatistiquesOfasManager.cotisation + ".MRIADH   )" + " INNER JOIN " + _getCollection()
                + AFInfoRom060StatistiquesOfasManager.assurance + " " + AFInfoRom060StatistiquesOfasManager.assurance
                + " ON (" + AFInfoRom060StatistiquesOfasManager.cotisation + ".MBIASS="
                + AFInfoRom060StatistiquesOfasManager.assurance + ".MBIASS AND "
                + AFInfoRom060StatistiquesOfasManager.assurance + ".MBTTYP = " + CodeSystem.TYPE_ASS_COTISATION_AVS_AI
                + ")");

        return from.toString();
    }

    protected String _getHaving() {
        return "";
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isBlankOrZero(getForAnnee())) {
            sqlAddCondition(sqlWhere,
                    " (MADFIN=0 OR MADFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee())
                            + "1231)");
            sqlAddCondition(sqlWhere, " MADDEB<= " + this._dbWriteNumeric(statement.getTransaction(), getForAnnee())
                    + "1231");
            sqlAddCondition(sqlWhere,
                    " (MEDFIN=0 OR MEDFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee())
                            + "1231)");
            sqlAddCondition(sqlWhere, " MEDDEB<= " + this._dbWriteNumeric(statement.getTransaction(), getForAnnee())
                    + "1231");
            sqlAddCondition(sqlWhere,
                    " (MRDFIN=0 OR MRDFIN >=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee())
                            + "1231)");
            sqlAddCondition(sqlWhere, " MRDDEB<= " + this._dbWriteNumeric(statement.getTransaction(), getForAnnee())
                    + "1231");
        }

        if (!JadeStringUtil.isBlankOrZero(getForTypeAffiliation())) {
            sqlAddCondition(
                    sqlWhere,
                    AFInfoRom060StatistiquesOfasManager.affiliation + ".MATTAF = "
                            + this._dbWriteNumeric(statement.getTransaction(), getForTypeAffiliation()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForBrancheEconomique())) {
            sqlAddCondition(
                    sqlWhere,
                    AFInfoRom060StatistiquesOfasManager.affiliation + ".MATBRA = "
                            + this._dbWriteNumeric(statement.getTransaction(), getForBrancheEconomique()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForNotBrancheEconomique())) {
            sqlAddCondition(
                    sqlWhere,
                    AFInfoRom060StatistiquesOfasManager.affiliation + ".MATBRA <> "
                            + this._dbWriteNumeric(statement.getTransaction(), getForNotBrancheEconomique()));
        }

        if (getForPersonnelMaison() != null) {
            sqlAddCondition(
                    sqlWhere,
                    AFInfoRom060StatistiquesOfasManager.affiliation
                            + ".MABMAI = "
                            + this._dbWriteBoolean(statement.getTransaction(), getForPersonnelMaison(),
                                    BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        return sqlWhere.toString();
    }

    /**
     * Getter
     */
    public String getForAnnee() {
        return forAnnee;
    }

    public String getForBrancheEconomique() {
        return forBrancheEconomique;
    }

    public String getForNotBrancheEconomique() {
        return forNotBrancheEconomique;
    }

    public Boolean getForPersonnelMaison() {
        return forPersonnelMaison;
    }

    public String getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    /**
     * Setter
     */
    public void setForAnnee(String NewForAnnee) {
        forAnnee = NewForAnnee;
    }

    public void setForBrancheEconomique(String newForBrancheEconomique) {
        forBrancheEconomique = newForBrancheEconomique;
    }

    public void setForNotBrancheEconomique(String newForNotBrancheEconomique) {
        forNotBrancheEconomique = newForNotBrancheEconomique;
    }

    public void setForPersonnelMaison(Boolean newForPersonnelMaison) {
        forPersonnelMaison = newForPersonnelMaison;
    }

    public void setForTypeAffiliation(String newForTypeAffiliation) {
        forTypeAffiliation = newForTypeAffiliation;
    }

    protected void sqlAddCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(AFInfoRom060StatistiquesOfasManager.AND);
        }
        sqlWhere.append(condition);
    }

}

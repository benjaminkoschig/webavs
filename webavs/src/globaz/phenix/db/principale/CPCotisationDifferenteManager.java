/**
 * class CPDecisionsAvecMiseEnCompteManager écrit le 19/01/05 par JPA
 * 
 * class manager pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPCotisationDifferenteManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeDecisionAF = "";
    private String forAnneeDecisionCP = "";
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forDateInformation = "";
    private Boolean forDecisionsValideesPassageComptabilise = new Boolean(false);
    private String forEtat = "";
    private String forIdAffilie = "";
    private String forIdDecision = "";
    private String forIdPassage = "";
    private String forIdTiers = "";
    private String forMontantTrimestrielAF = "";
    private String forMontantTrimestrielCP = "";
    private String forNumAff = "";
    private String forTestSurCoti = "";
    private String fromDateFinAffiliation = "";
    private String order = "";
    private String toDateDebutDecision = "";

    @Override
    protected String _getDbSchema() {
        return super._getDbSchema();
    }

    /**
     * déclaration de la clause select :
     */
    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {
        return _getFieldsForRepriseCotPers();
    }

    protected String _getFieldsForRepriseCotPers() {
        return _getCollection() + CPDecision.TABLE_CPDECIP + "." + CPDecision.FIELD_MAIAFF + ", " + _getCollection()
                + CPDecision.TABLE_CPDECIP + "." + CPDecision.FIELD_HTITIE + ", " + _getCollection()
                + CPDecision.TABLE_CPDECIP + "." + CPDecision.FIELD_IAIDEC + ", " + _getCollection()
                + CPDecision.TABLE_CPDECIP + "." + CPDecision.FIELD_IATGAF + ", " + CPDecision.FIELD_IADDEB + ","
                + CPDecision.FIELD_IADFIN + ", " + CPCotisationDifferente.FIELD_MBTTYP + ", "
                + CPCotisationDifferente.FIELD_MBLLCF + ", " + CPCotisationDifferente.FIELD_MBLLCD + ", "
                + CPCotisationDifferente.FIELD_MBLLCI + ", " + CPDecision.FIELD_IATETA + ", "
                + CPCotisationDifferente.FIELD_MENANN + ", " + CPDecision.FIELD_IAANNE + ", "
                + CPCotisationDifferente.FIELD_MALNAF + ", " + CPCotisationDifferente.FIELD_MEMTRI + ", ISMCTR, "
                + CPCotisationDifferente.FIELD_MADDEB + ", " + CPCotisationDifferente.FIELD_MADFIN + ", "
                + CPDecision.FIELD_IADINF + ", " + CPDecision.FIELD_EBIPAS + ", ISMCAN, "
                + CPCotisationDifferente.FIELD_MEMSEM + ", ISMCSE, ISMCTR, " + CPCotisationDifferente.FIELD_MEMMEN
                + ", ISMCME, " + CPCotisationDifferente.FIELD_MEMANN;
    }

    /**
     * déclaration de la clause from :
     * 
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = "";
        sqlFrom += _getCollection() + "CPCOTIP INNER JOIN " + _getCollection() + "AFCOTIP ON (" + _getCollection()
                + "CPCOTIP.MEICOT=" + _getCollection() + "AFCOTIP.MEICOT) INNER JOIN " + _getCollection()
                + "AFPLAFP ON (" + _getCollection() + "AFCOTIP.MUIPLA=" + _getCollection() + "AFPLAFP.MUIPLA AND "
                + _getCollection() + "AFPLAFP.MUBINA="
                + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR)
                + ") INNER JOIN " + _getCollection() + "AFAFFIP ON (" + _getCollection() + "AFPLAFP."
                + CPDecision.FIELD_MAIAFF + "=" + _getCollection() + "AFAFFIP." + CPDecision.FIELD_MAIAFF
                + ") INNER JOIN " + _getCollection() + CPDecision.TABLE_CPDECIP + " ON (" + _getCollection()
                + "CPCOTIP." + CPDecision.FIELD_IAIDEC + "=" + _getCollection() + CPDecision.TABLE_CPDECIP + "."
                + CPDecision.FIELD_IAIDEC + ") INNER JOIN " + _getCollection() + "AFASSUP ON (" + _getCollection()
                + "AFCOTIP.MBIASS=" + _getCollection() + "AFASSUP.MBIASS) ";
        return sqlFrom;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return CPCotisationDifferente.FIELD_MALNAF + " ASC ";
    }

    /**
     * déclaration de la clause where :
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        sqlWhere += " ((COALESCE(" + CPCotisationDifferente.FIELD_MEMANN + ", 0) <> COALESCE(ISMCAN, 0))";
        if (JadeStringUtil.isEmpty(getForTestSurCoti())) {
            sqlWhere += " OR (COALESCE(" + CPCotisationDifferente.FIELD_MEMSEM + ", 0)" + " <> COALESCE(ISMCSE, 0))"
                    + " OR (COALESCE(" + CPCotisationDifferente.FIELD_MEMTRI + ", 0) <> COALESCE(ISMCTR, 0))"
                    + " OR (COALESCE(" + CPCotisationDifferente.FIELD_MEMMEN + ", 0) <> COALESCE(ISMCME,0))"
                    + " OR (COALESCE(" + CPCotisationDifferente.FIELD_MENANN + ", 0) <> COALESCE("
                    + CPDecision.FIELD_IAANNE + ",0))";
        }
        sqlWhere += ") AND " + _getCollection() + CPDecision.TABLE_CPDECIP + "." + CPDecision.FIELD_IAIDEC
                + "=(SELECT MAX(B." + CPDecision.FIELD_IAIDEC + ") FROM " + _getCollection() + CPDecision.TABLE_CPDECIP
                + " B " + "WHERE B." + CPDecision.FIELD_MAIAFF + "=" + _getCollection() + CPDecision.TABLE_CPDECIP
                + "." + CPDecision.FIELD_MAIAFF;
        if (getForAnneeDecisionCP().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CPDecision.FIELD_IAANNE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForAnneeDecisionCP()) + " ";
        }
        if (getToDateDebutDecision().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CPDecision.FIELD_IADDEB + "<="
                    + _dbWriteDateAMJ(statement.getTransaction(), getToDateDebutDecision()) + ") ";
        } else {
            sqlWhere += ") ";
        }
        sqlWhere += "AND " + CPDecision.FIELD_IATETA + " = (SELECT MAX(B." + CPDecision.FIELD_IATETA + ") FROM "
                + _getCollection() + CPDecision.TABLE_CPDECIP + " B WHERE " + _getCollection()
                + CPDecision.TABLE_CPDECIP + "." + CPDecision.FIELD_IAIDEC + "=B." + CPDecision.FIELD_IAIDEC
                + " ) AND (" + CPDecision.FIELD_IATETA + " in (" + CPDecision.CS_FACTURATION + ", "
                + CPDecision.CS_REPRISE + ", " + CPDecision.CS_PB_COMPTABILISATION;
        if (forDecisionsValideesPassageComptabilise.booleanValue()) {
            sqlWhere += ", " + CPDecision.CS_VALIDATION;
        }
        sqlWhere += ")";
        if (getFromDateFinAffiliation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " (" + CPCotisationDifferente.FIELD_MADFIN + " = 0 OR " + CPCotisationDifferente.FIELD_MADFIN
                    + ">" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateFinAffiliation()) + ")";
        }
        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CPDecision.FIELD_EBIPAS + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }
        sqlWhere += " AND " + CPDecision.FIELD_IATTDE + " <> " + CPDecision.CS_IMPUTATION + ")";
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPCotisationDifferente();
    }

    public String getForAnneeDecisionAF() {
        return forAnneeDecisionAF;
    }

    public String getForAnneeDecisionCP() {
        return forAnneeDecisionCP;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateInformation() {
        return forDateInformation;
    }

    /**
     * @return
     */
    public Boolean getForDecisionsValideesPassageComptabilise() {
        return forDecisionsValideesPassageComptabilise;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForMontantTrimestrielAF() {
        return forMontantTrimestrielAF;
    }

    public String getForMontantTrimestrielCP() {
        return forMontantTrimestrielCP;
    }

    public String getForNumAff() {
        return forNumAff;
    }

    /**
     * @return
     */
    public String getForTestSurCoti() {
        return forTestSurCoti;
    }

    public String getFromDateFinAffiliation() {
        return fromDateFinAffiliation;
    }

    public String getOrder() {
        return order;
    }

    public String getToDateDebutDecision() {
        return toDateDebutDecision;
    }

    public void setForAnneeDecisionAF(String string) {
        forAnneeDecisionAF = string;
    }

    public void setForAnneeDecisionCP(String string) {
        forAnneeDecisionCP = string;
    }

    public void setForDateDebut(String string) {
        forDateDebut = string;
    }

    public void setForDateFin(String string) {
        forDateFin = string;
    }

    public void setForDateInformation(String string) {
        forDateInformation = string;
    }

    /**
     * @param boolean1
     */
    public void setForDecisionsValideesPassageComptabilise(Boolean boolean1) {
        forDecisionsValideesPassageComptabilise = boolean1;
    }

    public void setForEtat(String string) {
        forEtat = string;
    }

    public void setForIdAffilie(String string) {
        forIdAffilie = string;
    }

    public void setForIdDecision(String string) {
        forIdDecision = string;
    }

    public void setForIdPassage(String string) {
        forIdPassage = string;
    }

    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    public void setForMontantTrimestrielAF(String string) {
        forMontantTrimestrielAF = string;
    }

    public void setForMontantTrimestrielCP(String string) {
        forMontantTrimestrielCP = string;
    }

    public void setForNumAff(String string) {
        forNumAff = string;
    }

    /**
     * @param string
     */
    public void setForTestSurCoti(String string) {
        forTestSurCoti = string;
    }

    public void setFromDateFinAffiliation(String string) {
        fromDateFinAffiliation = string;
    }

    public void setOrder(String string) {
        order = string;
    }

    public void setToDateDebutDecision(String string) {
        toDateDebutDecision = string;
    }

}

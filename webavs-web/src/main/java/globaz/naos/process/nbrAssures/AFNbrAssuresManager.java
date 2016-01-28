package globaz.naos.process.nbrAssures;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.util.AFUtil;

/**
 * Manager permettant la récupération en base du nombre d'assurés : <BR>
 * - par assurance <br>
 * - par affiliation <br>
 * - par année <br>
 */
public class AFNbrAssuresManager extends BManager {

    private static final long serialVersionUID = 7698242565332389354L;

    private String forAnnee = "";
    private String fromNumAffilie = "";
    private String toNumAffilie = "";
    private String forIdAssurance = "";

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFNbrAssures();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sqlFields = new StringBuilder();

        AFUtil.sqlAddField(sqlFields, "AFF.MALNAF");
        AFUtil.sqlAddField(sqlFields, "TIER.HTLDE1");
        AFUtil.sqlAddField(sqlFields, "TIER.HTLDE2");

        // Récupération du libellé suivant la langue de l'utilisateur
        if ("FR".equals(getSession().getIdLangueISO().toUpperCase())) {
            AFUtil.sqlAddField(sqlFields, "ASS.MBLLIF AS LIBELLE_ASS");
        } else if ("DE".equals(getSession().getIdLangueISO().toUpperCase())) {
            AFUtil.sqlAddField(sqlFields, "ASS.MBLLID AS LIBELLE_ASS");
        } else if ("IT".equals(getSession().getIdLangueISO().toUpperCase())) {
            AFUtil.sqlAddField(sqlFields, "ASS.MBLLII AS LIBELLE_ASS");
        } else {
            // Par defaut, on set la langue FR
            AFUtil.sqlAddField(sqlFields, "ASS.MBLLIF AS LIBELLE_ASS");
        }

        AFUtil.sqlAddField(sqlFields, "NBR.MVNANN");
        AFUtil.sqlAddField(sqlFields, "NBR.MVNNBR");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sqlFrom = new StringBuilder();

        sqlFrom.append(_getCollection() + "AFAFFIP AFF ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "TITIERP TIER on TIER.HTITIE = AFF.HTITIE ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "AFNASSP NBR on NBR.MAIAFF = AFF.MAIAFF ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "AFASSUP ASS on ASS.MBIASS = NBR.MBIASS ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return super._getOrder(statement);
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sqlWhere = new StringBuilder();

        // Si une année est précisée
        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            AFUtil.sqlAddCondition(sqlWhere, "NBR.MVNANN = " + getForAnnee());
        }

        // Si une assurance est précisée
        if (!JadeStringUtil.isEmpty(getForIdAssurance())) {
            AFUtil.sqlAddCondition(sqlWhere, "NBR.MBIASS = " + getForIdAssurance());
        }

        // Si une borne d'affilié est précisée
        if (!JadeStringUtil.isEmpty(getFromNumAffilie()) && !JadeStringUtil.isEmpty(getToNumAffilie())) {
            AFUtil.sqlAddCondition(sqlWhere,
                    "AFF.MALNAF >= " + _dbWriteString(statement.getTransaction(), getFromNumAffilie()));
            AFUtil.sqlAddCondition(sqlWhere,
                    "AFF.MALNAF <= " + _dbWriteString(statement.getTransaction(), getToNumAffilie()));
        }

        return sqlWhere.toString();
    }

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @param forAnnee the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @return the fromNumAffilie
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @param fromNumAffilie the fromNumAffilie to set
     */
    public void setFromNumAffilie(String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    /**
     * @return the toNumAffilie
     */
    public String getToNumAffilie() {
        return toNumAffilie;
    }

    /**
     * @param toNumAffilie the toNumAffilie to set
     */
    public void setToNumAffilie(String toNumAffilie) {
        this.toNumAffilie = toNumAffilie;
    }

    /**
     * @return the forIdAssurance
     */
    public String getForIdAssurance() {
        return forIdAssurance;
    }

    /**
     * @param forIdAssurance the forIdAssurance to set
     */
    public void setForIdAssurance(String forIdAssurance) {
        this.forIdAssurance = forIdAssurance;
    }

}

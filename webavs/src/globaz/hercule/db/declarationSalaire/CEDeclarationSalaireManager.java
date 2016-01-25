package globaz.hercule.db.declarationSalaire;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 25 juin 2010
 */
public class CEDeclarationSalaireManager extends BManager {

    private static final long serialVersionUID = 4382472772419454277L;
    private static final String DECLARATION_SALAIRE = "6200016";
    private static final String TAXATION_OFFICE = "6200022";

    private String forAnneeDebut = "";
    private String forAnneeFin = "";
    private Boolean isDeclarationSalaireOrTaxation = new Boolean(false);
    private String likeNumAffilie = "";

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer sqlFields = new StringBuffer();

        sqlFields.append("JOURNALISATION.JJOUDA, JOURNALISATION.jjouli, GROUPEJOURNAL.jgjore");

        return sqlFields.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        // select JOURNALISATION.jjouli, JOURNALISATION.jjouda,
        // GROUPEJOURNAL.jgjore from ccvdqua.jojpgjo GROUPEJOURNAL
        // inner join ccvdqua.jojpjou JOURNALISATION on JOURNALISATION.jgjoid =
        // GROUPEJOURNAL.jgjoid
        // inner join ccvdqua.jojpcjo COMPLEMENT on COMPLEMENT.jjouid =
        // JOURNALISATION.jjouid
        // where COMPLEMENT.jcjoty = 16000002 AND COMPLEMENT.JCJOVA = 6200016
        // and JOURNALISATION.jjouli = '1512498-10'

        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "jojpgjo GROUPEJOURNAL ");
        sqlFrom.append("INNER JOIN " + _getCollection()
                + "jojpjou JOURNALISATION ON JOURNALISATION.JGJOID = GROUPEJOURNAL.JGJOID ");
        sqlFrom.append("INNER JOIN " + _getCollection()
                + "JOJPCJO COMPLEMENT ON COMPLEMENT.jjouid = JOURNALISATION.jjouid ");

        return sqlFrom.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        sqlWhere.append("COMPLEMENT.jcjoty = 16000002 AND COMPLEMENT.JCJOVA = ");

        if (isDeclarationSalaireOrTaxation.booleanValue() == true) {
            sqlWhere.append(DECLARATION_SALAIRE);
        } else {
            sqlWhere.append(TAXATION_OFFICE);
        }

        if (!JadeStringUtil.isBlank(getLikeNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "JOURNALISATION.jjouli = " + _dbWriteString(statement.getTransaction(), getLikeNumAffilie()));
        }

        if (!JadeStringUtil.isBlank(getForAnneeDebut())) {
            CEUtils.sqlAddCondition(sqlWhere, "JOURNALISATION.jjouda > " + getForAnneeDebut() + "0101");
        }

        if (!JadeStringUtil.isBlank(getForAnneeFin())) {
            CEUtils.sqlAddCondition(sqlWhere, "JOURNALISATION.jjouda < " + getForAnneeFin() + "0101");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEDeclarationSalaire();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForAnneeDebut() {
        return forAnneeDebut;
    }

    public String getForAnneeFin() {
        return forAnneeFin;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAnneeDebut(String forAnneeDebut) {
        this.forAnneeDebut = forAnneeDebut;
    }

    public void setForAnneeFin(String forAnneeFin) {
        this.forAnneeFin = forAnneeFin;
    }

    public void setIsDeclarationSalaireOrTaxation(Boolean isDeclarationSalaireOrTaxation) {
        this.isDeclarationSalaireOrTaxation = isDeclarationSalaireOrTaxation;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }
}

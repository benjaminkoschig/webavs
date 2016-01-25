package globaz.osiris.db.comptes;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.helios.db.comptes.CGPeriodeComptable;

/**
 *
 */
public class CACompteCourantManagerListViewBean extends CACompteCourantManager implements FWListViewBeanInterface {

    // SELECT * FROM webavsciam.CACPTCP cc
    // LEFT JOIN (
    // SELECT WEBAVSCIAM.CGPLANP.IDEXTERNE compteCG, WEBAVSCIAM.CGPLANP.LIBELLEFR libelleCG,
    // SUM(WEBAVSCIAM.CGSOLDP.SOLDEprovisoire) soldeCG
    // FROM webavsciam.CGPLANP
    // INNER JOIN webavsciam.cgexerp ON (webavsciam.CGPLANP.idexercomptable = webavsciam.cgexerp.idexercomptable)
    // INNER JOIN webavsciam.CGCOMTP ON (webavsciam.CGPLANP.IDCOMPTE=webavsciam.CGCOMTP.IDCOMPTE)
    // LEFT OUTER JOIN webavsciam.CGSOLDP ON (webavsciam.CGPLANP.IDEXERCOMPTABLE = webavsciam.CGSOLDP.IDEXERCOMPTABLE
    // AND webavsciam.CGSOLDP.ESTPERIODE='1' AND webavsciam.CGPLANP.IDCOMPTE=webavsciam.CGSOLDP.IDCOMPTE)
    // INNER JOIN webavsciam.cgperip pr on (webavsciam.cgsoldp.idperiodecomptable = pr.idperiodecomptable AND
    // pr.idtypeperiode <> 709005)
    // WHERE ESTCLOTURE='2' AND (webavsciam.CGSOLDP.IDCENTRECHARGE=0 OR webavsciam.CGSOLDP.IDCENTRECHARGE IS NULL)
    // AND webavsciam.cgexerp.IDMANDAT=900 AND webavsciam.cgexerp.ESTCLOTURE='2'
    // GROUP BY WEBAVSCIAM.CGPLANP.IDEXTERNE, WEBAVSCIAM.CGPLANP.LIBELLEFR
    // ) cg ON (cc.IDEXTERNE=cg.compteCG)
    // WHERE cc.IDEXTERNE >= '2000.2111'
    // ORDER BY cc.IDEXTERNE

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return super._getFrom(statement) + " LEFT JOIN (" + subQuery() + " ) cg ON (" + _getCollection()
                + "CACPTCP.IDEXTERNE = cg.compteCG)";
    }

    /**
     * new entity
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACompteCourantLienCG();
    }

    /**
     * @return
     */
    private String subQuery() {
        // SELECT WEBAVSCIAM.CGPLANP.IDEXTERNE compteCG, WEBAVSCIAM.CGPLANP.LIBELLEFR libelleCG,
        // SUM(WEBAVSCIAM.CGSOLDP.SOLDEprovisoire) soldeCG
        // FROM webavsciam.CGPLANP
        // INNER JOIN webavsciam.cgexerp ON (webavsciam.CGPLANP.idexercomptable = webavsciam.cgexerp.idexercomptable)
        // INNER JOIN webavsciam.CGCOMTP ON (webavsciam.CGPLANP.IDCOMPTE=webavsciam.CGCOMTP.IDCOMPTE)
        // LEFT OUTER JOIN webavsciam.CGSOLDP ON (webavsciam.CGPLANP.IDEXERCOMPTABLE =
        // webavsciam.CGSOLDP.IDEXERCOMPTABLE
        // AND webavsciam.CGSOLDP.ESTPERIODE='1' AND webavsciam.CGPLANP.IDCOMPTE=webavsciam.CGSOLDP.IDCOMPTE)
        // INNER JOIN webavsciam.cgperip pr on (webavsciam.cgsoldp.idperiodecomptable = pr.idperiodecomptable AND
        // pr.idtypeperiode <> 709005)
        // WHERE ESTCLOTURE='2' AND (webavsciam.CGSOLDP.IDCENTRECHARGE=0 OR webavsciam.CGSOLDP.IDCENTRECHARGE IS NULL)
        // AND webavsciam.cgexerp.IDMANDAT=900 AND webavsciam.cgexerp.ESTCLOTURE='2'
        // GROUP BY WEBAVSCIAM.CGPLANP.IDEXTERNE, WEBAVSCIAM.CGPLANP.LIBELLEFR

        StringBuffer query = new StringBuffer();

        // SELECT WEBAVSCIAM.CGPLANP.IDEXTERNE compteCG, WEBAVSCIAM.CGPLANP.LIBELLEFR libelleCG,
        // SUM(WEBAVSCIAM.CGSOLDP.SOLDEprovisoire) soldeCG
        query.append("SELECT ");
        query.append(_getCollection()).append("CGPLANP").append(".IDEXTERNE compteCG");
        query.append(", ");
        query.append(_getCollection()).append("CGPLANP").append(".LIBELLEFR libelleCG");
        query.append(", SUM(");
        query.append(_getCollection()).append("CGSOLDP").append(".SOLDEPROVISOIRE) soldeCG");

        // FROM webavsciam.CGPLANP
        query.append(" FROM ").append(_getCollection()).append("CGPLANP");

        // INNER JOIN webavsciam.cgexerp ON (webavsciam.CGPLANP.idexercomptable = webavsciam.cgexerp.idexercomptable)
        query.append(" INNER JOIN ").append(_getCollection()).append("CGEXERP");
        query.append(" ON ").append("(");
        query.append(_getCollection()).append("CGPLANP").append(".IDEXERCOMPTABLE");
        query.append(" = ");
        query.append(_getCollection()).append("CGEXERP").append(".IDEXERCOMPTABLE");
        query.append(")");

        // INNER JOIN webavsciam.CGCOMTP ON (webavsciam.CGPLANP.IDCOMPTE=webavsciam.CGCOMTP.IDCOMPTE)
        query.append(" INNER JOIN ").append(_getCollection()).append("CGCOMTP");
        query.append(" ON ").append("(");
        query.append(_getCollection()).append("CGPLANP").append(".IDCOMPTE");
        query.append(" = ");
        query.append(_getCollection()).append("CGCOMTP").append(".IDCOMPTE");
        query.append(")");

        // LEFT OUTER JOIN webavsciam.CGSOLDP ON (webavsciam.CGPLANP.IDEXERCOMPTABLE =
        // webavsciam.CGSOLDP.IDEXERCOMPTABLE
        // AND webavsciam.CGSOLDP.ESTPERIODE='1' AND webavsciam.CGPLANP.IDCOMPTE=webavsciam.CGSOLDP.IDCOMPTE)
        query.append(" LEFT OUTER JOIN ").append(_getCollection()).append("CGSOLDP");
        query.append(" ON ").append("(");
        query.append(_getCollection()).append("CGPLANP").append(".IDEXERCOMPTABLE");
        query.append(" = ");
        query.append(_getCollection()).append("CGSOLDP").append(".IDEXERCOMPTABLE");
        query.append(" AND ").append(_getCollection()).append("CGSOLDP").append(".ESTPERIODE").append(" = ")
                .append("'1'");
        query.append(" AND ").append(_getCollection()).append("CGPLANP").append(".IDCOMPTE").append(" = ")
                .append(_getCollection()).append("CGSOLDP").append(".IDCOMPTE");
        query.append(")");

        // INNER JOIN webavsciam.cgperip pr on (webavsciam.cgsoldp.idperiodecomptable = pr.idperiodecomptable AND
        // pr.idtypeperiode <> 709005)
        query.append(" INNER JOIN ").append(_getCollection()).append("CGPERIP");
        query.append(" ON ").append("(");
        query.append(_getCollection()).append("CGSOLDP").append(".IDPERIODECOMPTABLE");
        query.append(" = ");
        query.append(_getCollection()).append("CGPERIP").append(".IDPERIODECOMPTABLE");
        query.append(" AND ");
        query.append(_getCollection()).append("CGPERIP").append(".IDTYPEPERIODE <> ")
                .append(CGPeriodeComptable.CS_CLOTURE);
        query.append(")");

        // WHERE ESTCLOTURE='2' AND (webavsciam.CGSOLDP.IDCENTRECHARGE=0 OR webavsciam.CGSOLDP.IDCENTRECHARGE IS NULL)
        // AND webavsciam.cgexerp.IDMANDAT=900 AND webavsciam.cgexerp.ESTCLOTURE='2'
        query.append(" WHERE ");
        query.append(_getCollection()).append("CGEXERP").append(".IDMANDAT=900");
        query.append(" AND ");
        query.append("ESTCLOTURE='2'");
        query.append(" AND ");
        query.append(" (");
        query.append(_getCollection()).append("CGSOLDP").append(".IDCENTRECHARGE");
        query.append(" = 0 OR ");
        query.append(_getCollection()).append("CGSOLDP").append(".IDCENTRECHARGE IS NULL");
        query.append(") ");

        // GROUP BY WEBAVSCIAM.CGPLANP.IDEXTERNE, WEBAVSCIAM.CGPLANP.LIBELLEFR
        query.append(" GROUP BY ");
        query.append(_getCollection()).append("CGPLANP").append(".IDEXTERNE");
        query.append(", ");
        query.append(_getCollection()).append("CGPLANP").append(".LIBELLEFR");
        return query.toString();
    }

}

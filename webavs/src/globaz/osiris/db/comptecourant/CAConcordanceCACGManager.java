/**
 *
 */
package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SEL
 * 
 */
public class CAConcordanceCACGManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";
    private String forIdExterne = "";
    private boolean printOnlyDiff = false;

    // SELECT
    // *
    // FROM
    // (
    // (
    // SELECT
    // ca.idjournal,
    // ca.idcompte,
    // ca.soldeca,
    // cg.idjournalexterne,
    // cg.numero,
    // cg.libelleCG,
    // cg.comptecg,
    // cg.soldeCG,
    // (
    // CASE WHEN ca.soldeca IS NULL THEN 0 ELSE ca.soldeca END
    // )
    // - (CASE WHEN cg.soldeCG IS NULL THEN 0 ELSE cg.soldeCG END) as diff
    // FROM
    // (
    // SELECT
    // op.idjournal, cc.idexterne idcompte, sum(montant) soldeca
    // FROM CCVDDEV.caoperp op
    // INNER JOIN CCVDDEV.cacptcp cc ON (op.idcomptecourant=cc.idcomptecourant)
    // INNER JOIN CCVDDEV.cajourp jo ON (op.idjournal = jo.idjournal)
    // WHERE op.etat=205002
    // AND jo.datevaleurcg BETWEEN 20120101
    // AND 20120331
    // AND cc.idexterne='2000.1101.0000'
    // GROUP BY op.idjournal, cc.idexterne
    // )
    // ca
    // LEFT JOIN
    // (
    // SELECT
    // jo.numero,
    // jo.libelle libelleCG,
    // pl.idexterne comptecg,
    // sum(montant) soldeCG,
    // CASE WHEN jo.referenceexterne ='' THEN 0 ELSE cast
    // (
    // jo.referenceexterne as numeric(9,0)
    // )
    // END idjournalexterne
    // FROM CCVDDEV.cgecrip ec
    // INNER JOIN CCVDDEV.cgjourp jo ON (ec.idjournal=jo.idjournal)
    // INNER JOIN CCVDDEV.cgplanp pl ON
    // (
    // ec.idcompte=pl.idcompte
    // and ec.idexercomptable = pl.idexercomptable
    // )
    // INNER JOIN CCVDDEV.cgexerp ex ON (ec.idexercomptable=ex.idexercomptable)
    // INNER JOIN CCVDDEV.cacptcp cc ON (pl.idexterne = cc.idexterne)
    // INNER JOIN CCVDDEV.cgperip pr ON
    // (
    // jo.idperiodecomptable = pr.idperiodecomptable
    // )
    // WHERE estactive = '1'
    // AND ex.datefin BETWEEN 20120101
    // AND 20120331
    // AND pr.idtypeperiode <> 709005
    // AND cc.idexterne='2000.1101.0000'
    // GROUP BY jo.referenceexterne, jo.numero, jo.libelle, pl.idexterne
    // )
    // cg ON ca.idjournal = coalesce(cg.idjournalexterne,0)
    // AND ca.idcompte=cg.comptecg
    // ORDER BY ca.idjournal, ca.idcompte
    // )
    // UNION
    // (
    // SELECT
    // ca.idjournal,
    // ca.idcompte,
    // ca.soldeca,
    // cg.idjournalexterne,
    // cg.numero,
    // cg.libelleCG,
    // cg.comptecg,
    // cg.soldeCG,
    // (
    // CASE WHEN ca.soldeca IS NULL THEN 0 ELSE ca.soldeca END
    // )
    // - (CASE WHEN cg.soldeCG IS NULL THEN 0 ELSE cg.soldeCG END) as diff
    // FROM
    // (
    // SELECT
    // op.idjournal, cc.idexterne idcompte, sum(montant) soldeca
    // FROM CCVDDEV.caoperp op
    // INNER JOIN CCVDDEV.cacptcp cc ON (op.idcomptecourant=cc.idcomptecourant)
    // INNER JOIN CCVDDEV.cajourp jo ON (op.idjournal = jo.idjournal)
    // WHERE op.etat=205002
    // AND jo.datevaleurcg BETWEEN 20120101
    // AND 20120331
    // AND cc.idexterne='2000.1101.0000'
    // GROUP BY op.idjournal, cc.idexterne
    // )
    // ca
    // RIGHT JOIN
    // (
    // SELECT
    // jo.numero,
    // jo.libelle libelleCG,
    // pl.idexterne comptecg,
    // sum(montant) soldeCG,
    // CASE WHEN jo.referenceexterne ='' THEN 0 ELSE cast
    // (
    // jo.referenceexterne as numeric(9,0)
    // )
    // END idjournalexterne
    // FROM CCVDDEV.cgecrip ec
    // INNER JOIN CCVDDEV.cgjourp jo ON (ec.idjournal=jo.idjournal)
    // INNER JOIN CCVDDEV.cgplanp pl ON
    // (
    // ec.idcompte=pl.idcompte
    // and ec.idexercomptable = pl.idexercomptable
    // )
    // INNER JOIN CCVDDEV.cgexerp ex ON (ec.idexercomptable=ex.idexercomptable)
    // INNER JOIN CCVDDEV.cacptcp cc ON (pl.idexterne = cc.idexterne)
    // INNER JOIN CCVDDEV.cgperip pr ON
    // (
    // jo.idperiodecomptable = pr.idperiodecomptable
    // )
    // WHERE estactive = '1'
    // AND ex.datefin BETWEEN 20120101
    // AND 20120331
    // AND pr.idtypeperiode <> 709005
    // AND cc.idexterne='2000.1101.0000'
    // GROUP BY jo.referenceexterne, jo.numero, jo.libelle, pl.idexterne
    // )
    // cg ON ca.idjournal = coalesce(cg.idjournalexterne,0)
    // AND ca.idcompte=cg.comptecg
    // ORDER BY ca.idjournal, ca.idcompte
    // )
    // ) res
    // ORDER BY idjournal, idcompte

    /**
     * Renvoie la clause FROM (par défaut, la clause FROM de l'entité).
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append("((SELECT ");
        sqlFrom.append("ca.idjournal, ca.idcompte, ca.soldeca, cg.idjournalexterne, cg.numero, cg.libelleCG, cg.comptecg, cg.soldeCG, (CASE WHEN ca.soldeca IS NULL THEN 0 ELSE ca.soldeca END) - (CASE WHEN cg.soldeCG IS NULL THEN 0 ELSE cg.soldeCG END) as diff ");
        sqlFrom.append(" FROM ");
        sqlFrom.append(" (");
        sqlFrom.append(subQueryCA());
        sqlFrom.append(") ca ");
        sqlFrom.append(" LEFT JOIN ");
        sqlFrom.append(" (");
        sqlFrom.append(subQueryCG());
        sqlFrom.append(") cg ON ca.idjournal = coalesce(cg.idjournalexterne,0) AND ca.idcompte=cg.comptecg");
        sqlFrom.append(" ORDER BY ca.idjournal, ca.idcompte");
        sqlFrom.append(") UNION (");
        sqlFrom.append("SELECT ");
        sqlFrom.append("ca.idjournal, ca.idcompte, ca.soldeca, cg.idjournalexterne, cg.numero, cg.libelleCG, cg.comptecg, cg.soldeCG, (CASE WHEN ca.soldeca IS NULL THEN 0 ELSE ca.soldeca END) - (CASE WHEN cg.soldeCG IS NULL THEN 0 ELSE cg.soldeCG END) as diff  ");
        sqlFrom.append(" FROM ");
        sqlFrom.append("(");
        sqlFrom.append(subQueryCA());
        sqlFrom.append(") ca");
        sqlFrom.append(" RIGHT JOIN ");
        sqlFrom.append(" (");
        sqlFrom.append(subQueryCG());
        sqlFrom.append(") cg ON ca.idjournal = coalesce(cg.idjournalexterne,0) AND ca.idcompte=cg.comptecg");
        sqlFrom.append(" ORDER BY ca.idjournal, ca.idcompte");
        sqlFrom.append(")) res ");

        return sqlFrom.toString();
    }

    /**
     * Renvoie la composante de tri de la requête SQL (sans le mot-clé ORDER BY).
     * 
     * @return la composante ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "idjournal, idcompte";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (isPrintOnlyDiff()) {
            sqlWhere.append("diff <> 0");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAConcordanceCACG();
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the forIdExterne
     */
    public String getForIdExterne() {
        return forIdExterne;
    }

    /**
     * @return the printOnlyDiff
     */
    public boolean isPrintOnlyDiff() {
        return printOnlyDiff;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param forIdExterne
     *            the forIdExterne to set
     */
    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    /**
     * @param printOnlyDiff
     *            the printOnlyDiff to set
     */
    public void setPrintOnlyDiff(boolean printOnlyDiff) {
        this.printOnlyDiff = printOnlyDiff;
    }

    /**
     * @return
     */
    private String subQueryCA() {
        // select op.idjournal, cc.idexterne idcompte, sum(montant) soldeca
        // from webavsciam.caoperp op
        // inner join webavsciam.cacptcp cc on (op.idcomptecourant=cc.idcomptecourant)
        // inner join webavsciam.cajourp jo on (op.idjournal = jo.idjournal)
        // where op.etat=205002 and jo.datevaleurcg between 20100101 and 20101231
        // and cc.idexterne='5511.1101.0000'
        // group by op.idjournal,cc.idexterne

        StringBuffer query = new StringBuffer();

        query.append("SELECT ");
        query.append("op.idjournal, cc.idexterne idcompte, sum(montant) soldeca");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append("caoperp op");

        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cacptcp cc");
        query.append(" ON (");
        query.append("op.idcomptecourant=cc.idcomptecourant");
        query.append(")");

        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cajourp jo");
        query.append(" ON (");
        query.append("op.idjournal = jo.idjournal");
        query.append(")");

        query.append(" WHERE ");
        query.append("op.etat=205002");

        if (!JadeStringUtil.isBlankOrZero(getDateDebut()) && !JadeStringUtil.isBlankOrZero(getDateFin())) {
            query.append(" AND ");
            query.append("jo.datevaleurcg BETWEEN ");
            query.append(getDateDebut());
            query.append(" AND ");
            query.append(getDateFin());
        }

        query.append(" AND ");
        query.append("cc.idexterne='");
        query.append(getForIdExterne());
        query.append("'");

        query.append(" GROUP BY ");
        query.append("op.idjournal, cc.idexterne");

        return query.toString();
    }

    /**
     * @return
     */
    private String subQueryCG() {
        // select jo.numero, pl.idexterne comptecg, sum(montant) soldeCG,
        // CASE WHEN jo.referenceexterne ='' THEN 0 ELSE cast(jo.referenceexterne as numeric(9,0)) END idjournalexterne
        // from webavsciam.cgecrip ec
        // inner join webavsciam.cgjourp jo on (ec.idjournal=jo.idjournal)
        // inner join webavsciam.cgplanp pl on (ec.idcompte=pl.idcompte and ec.idexercomptable = pl.idexercomptable)
        // inner join webavsciam.cgexerp ex on (ec.idexercomptable=ex.idexercomptable)
        // inner join webavsciam.cacptcp cc on (pl.idexterne = cc.idexterne)
        // inner join webavsciam.cgperip pr on (jo.idperiodecomptable = pr.idperiodecomptable)
        // where estactive = '1' and ex.datefin between 20100101 and 20101231
        // and pr.idtypeperiode <> 709005
        // and pl.idexterne='5511.1101.0000'
        // group by jo.referenceexterne, jo.numero, pl.idexterne

        StringBuffer query = new StringBuffer();

        query.append("SELECT ");
        query.append("jo.numero, jo.libelle libelleCG, pl.idexterne comptecg, sum(montant) soldeCG,");
        query.append("CASE WHEN jo.referenceexterne ='' THEN 0 ELSE cast(jo.referenceexterne as numeric(9,0)) END idjournalexterne");
        query.append(" FROM ");
        query.append(_getCollection());
        query.append("cgecrip ec");

        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cgjourp jo");
        query.append(" ON (");
        query.append("ec.idjournal=jo.idjournal");
        query.append(")");

        // inner join webavsciam.cgplanp pl on (ec.idcompte=pl.idcompte and ec.idexercomptable = pl.idexercomptable)
        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cgplanp pl");
        query.append(" ON (");
        query.append("ec.idcompte=pl.idcompte and ec.idexercomptable = pl.idexercomptable");
        query.append(")");

        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cgexerp ex");
        query.append(" ON (");
        query.append("ec.idexercomptable=ex.idexercomptable");
        query.append(")");

        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cacptcp cc");
        query.append(" ON (");
        query.append("pl.idexterne = cc.idexterne");
        query.append(")");

        query.append(" INNER JOIN ");
        query.append(_getCollection());
        query.append("cgperip pr");
        query.append(" ON (");
        query.append("jo.idperiodecomptable = pr.idperiodecomptable");
        query.append(")");

        // estactive = '1' and ex.datefin between 20100101 and 20101231
        query.append(" WHERE ");
        query.append("estactive = '1'");

        if (!JadeStringUtil.isBlankOrZero(getDateDebut()) && !JadeStringUtil.isBlankOrZero(getDateFin())) {
            query.append(" AND ");
            query.append("ex.datefin BETWEEN ");
            query.append(getDateDebut());
            query.append(" AND ");
            query.append(getDateFin());
        }

        query.append(" AND ");
        query.append("pr.idtypeperiode <> ").append(CGPeriodeComptable.CS_CLOTURE);
        query.append(" AND ");
        query.append("cc.idexterne='");
        query.append(getForIdExterne());
        query.append("'");

        query.append(" GROUP BY ");
        query.append("jo.referenceexterne, jo.numero, jo.libelle, pl.idexterne");

        return query.toString();
    }
}

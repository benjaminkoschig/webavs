package globaz.lynx.db.impression;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXUtils;
import java.util.ArrayList;

/**
 * 
 * @author DDA
 */
public class LXImpressionBalanceManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CREDIT = "CREDIT";
    public static final String FIELD_DEBIT = "DEBIT";
    public static final String FIELD_IDFOURCREDIT = "IDFOURCREDIT";
    public static final String FIELD_IDFOURDEBIT = "IDFOURDEBIT";
    public static final String FIELD_SOLDE = "SOLDE";

    public static final String SUBTABLE_DEBITCREDIT = "DEBITCREDIT";
    public static final String SUBTABLE_FOURNISSEUR = "FOURNISSEUR";
    public static final String SUBTABLE_TIERS = "TIERS";

    /**
     * Afficher facture bloquées
     */
    private Boolean estBloque;
    private String forCsCategorie;
    private ArrayList<String> forCsEtatIn;
    private String forCsMotifBlocage;
    private String forDateDebut;
    private String forDateFin;
    private String forIdSociete;
    private String forMontantMaxi;
    private String forMontantMini;

    private String forSelection;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sql = new StringBuffer();

        // Modification : SCO
        // BugZilla : Bug 4662
        // Probleme du "FULL" qui ne passe pas sur les AS400

        // ********************
        // Requete existante
        // ********************
        //
        // SELECT * FROM
        // (
        // SELECT DEBIT, CREDIT,COALESCE(DEBIT,0)+COALESCE(CREDIT,0) as SOLDE,
        // IDFOURDEBIT, IDFOURCREDIT from (
        // ( SELECT SUM(b.MONTANT) as DEBIT, a.IDFOURNISSEUR as IDFOURDEBIT
        // FROM WEBAVSCIAM.LXSECTP a,WEBAVSCIAM.LXOPERP b
        // WHERE a.IDSECTION = b.IDSECTION AND b.MONTANT <= 0 AND a.IDSOCIETE =
        // 7 AND b.CSETATOPERATION in (7800004, 7800005, 7800006) GROUP BY
        // a.IDFOURNISSEUR
        // ) table1
        //
        // FULL JOIN
        //
        // ( SELECT SUM(b.MONTANT) as CREDIT, a.IDFOURNISSEUR as IDFOURCREDIT
        // FROM WEBAVSCIAM.LXSECTP a,WEBAVSCIAM.LXOPERP b
        // WHERE a.IDSECTION = b.IDSECTION AND b.MONTANT > 0 AND a.IDSOCIETE = 7
        // AND b.CSETATOPERATION in (7800004, 7800005, 7800006) GROUP BY
        // a.IDFOURNISSEUR
        // ) table2
        // ON table1.IDFOURDEBIT = table2.IDFOURCREDIT
        // )
        // ) DEBITCREDIT, WEBAVSCIAM.LXFOURP FOURNISSEUR, WEBAVSCIAM.TITIERP
        // TIERS
        // WHERE (DEBITCREDIT.IDFOURCREDIT = FOURNISSEUR.IDFOURNISSEUR OR
        // DEBITCREDIT.IDFOURDEBIT = FOURNISSEUR.IDFOURNISSEUR) AND
        // FOURNISSEUR.IDTIERS = TIERS.HTITIE
        // ORDER BY TIERS.HTLDE1 ASC
        //
        //
        // ********************
        // Requete modifié :
        // ********************
        //
        // SELECT * FROM
        // (
        // SELECT DEBIT, CREDIT,COALESCE(DEBIT,0)+COALESCE(CREDIT,0) as SOLDE,
        // IDFOURDEBIT, IDFOURCREDIT from (
        // ( SELECT SUM(b.MONTANT) as DEBIT, a.IDFOURNISSEUR as IDFOURDEBIT
        // FROM WEBAVSCIAM.LXSECTP a,WEBAVSCIAM.LXOPERP b
        // WHERE a.IDSECTION = b.IDSECTION AND b.MONTANT <= 0 AND a.IDSOCIETE =
        // 7 AND b.CSETATOPERATION in (7800004, 7800005, 7800006) GROUP BY
        // a.IDFOURNISSEUR
        // ) table1
        //
        // LEFT JOIN
        //
        // ( SELECT SUM(b.MONTANT) as CREDIT, a.IDFOURNISSEUR as IDFOURCREDIT
        // FROM WEBAVSCIAM.LXSECTP a,WEBAVSCIAM.LXOPERP b
        // WHERE a.IDSECTION = b.IDSECTION AND b.MONTANT > 0 AND a.IDSOCIETE = 7
        // AND b.CSETATOPERATION in (7800004, 7800005, 7800006) GROUP BY
        // a.IDFOURNISSEUR
        // ) table2
        // ON table1.IDFOURDEBIT = table2.IDFOURCREDIT
        // )
        //
        // UNION
        //
        // SELECT DEBIT, CREDIT,COALESCE(DEBIT,0)+COALESCE(CREDIT,0) as SOLDE,
        // IDFOURDEBIT, IDFOURCREDIT from (
        // ( SELECT SUM(b.MONTANT) as DEBIT, a.IDFOURNISSEUR as IDFOURDEBIT
        // FROM WEBAVSCIAM.LXSECTP a,WEBAVSCIAM.LXOPERP b
        // WHERE a.IDSECTION = b.IDSECTION AND b.MONTANT <= 0 AND a.IDSOCIETE =
        // 7 AND b.CSETATOPERATION in (7800004, 7800005, 7800006) GROUP BY
        // a.IDFOURNISSEUR
        // ) table1
        //
        // RIGHT JOIN
        //
        // ( SELECT SUM(b.MONTANT) as CREDIT, a.IDFOURNISSEUR as IDFOURCREDIT
        // FROM WEBAVSCIAM.LXSECTP a,WEBAVSCIAM.LXOPERP b
        // WHERE a.IDSECTION = b.IDSECTION AND b.MONTANT > 0 AND a.IDSOCIETE = 7
        // AND b.CSETATOPERATION in (7800004, 7800005, 7800006) GROUP BY
        // a.IDFOURNISSEUR
        // ) table2
        // ON table1.IDFOURDEBIT = table2.IDFOURCREDIT
        // )
        // ) DEBITCREDIT, WEBAVSCIAM.LXFOURP FOURNISSEUR, WEBAVSCIAM.TITIERP
        // TIERS
        // WHERE (DEBITCREDIT.IDFOURCREDIT = FOURNISSEUR.IDFOURNISSEUR OR
        // DEBITCREDIT.IDFOURDEBIT = FOURNISSEUR.IDFOURNISSEUR) AND
        // FOURNISSEUR.IDTIERS = TIERS.HTITIE
        // ORDER BY TIERS.HTLDE1 ASC

        // Modification : sel
        // BugZilla : Bug 5323
        //
        // SELECT * FROM (
        // SELECT DEBIT, CREDIT,COALESCE(DEBIT,0)+COALESCE(CREDIT,0) as SOLDE, IDFOURDEBIT, IDFOURCREDIT
        // from ( (
        // SELECT SUM(DEBIT) as DEBIT, IDFOURDEBIT
        // from (
        // SELECT SUM(o.MONTANT) as DEBIT, s.IDFOURNISSEUR as IDFOURDEBIT
        // FROM ccvdqua.LXSECTP s,ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION AND o.MONTANT <= 0 AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // AND o.CSTYPEOPERATION <> 7700004
        // GROUP BY s.IDFOURNISSEUR
        // UNION
        // SELECT -SUM(o.MONTANT) as DEBIT, s.IDFOURNISSEUR as IDFOURDEBIT
        // FROM ccvdqua.LXSECTP s,ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006) AND o.CSTYPEOPERATION <> 7700004
        // AND o.CSTYPEOPERATION IN (7700010, 7700005)
        // GROUP BY s.IDFOURNISSEUR
        // ) tableA
        // GROUP BY IDFOURDEBIT
        // ) table1
        // LEFT JOIN (
        // SELECT SUM(o.MONTANT) as CREDIT, s.IDFOURNISSEUR as IDFOURCREDIT
        // FROM ccvdqua.LXSECTP s,ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION AND o.MONTANT > 0 AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // AND o.CSTYPEOPERATION NOT IN (7700004, 7700010, 7700005)
        // GROUP BY s.IDFOURNISSEUR
        // ) table2 on table1.IDFOURDEBIT = table2.IDFOURCREDIT )
        // UNION
        // SELECT DEBIT, CREDIT,COALESCE(DEBIT,0)+COALESCE(CREDIT,0) as SOLDE, IDFOURDEBIT, IDFOURCREDIT
        // from ( (
        // SELECT SUM(DEBIT) as DEBIT, IDFOURDEBIT
        // from (
        // SELECT SUM(o.MONTANT) as DEBIT, s.IDFOURNISSEUR as IDFOURDEBIT
        // FROM ccvdqua.LXSECTP s,ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION AND o.MONTANT <= 0
        // AND s.IDSOCIETE = 1 AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // AND o.CSTYPEOPERATION <> 7700004
        // GROUP BY s.IDFOURNISSEUR
        // UNION
        // SELECT -SUM(o.MONTANT) as DEBIT, s.IDFOURNISSEUR as IDFOURDEBIT
        // FROM ccvdqua.LXSECTP s,ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006) AND o.CSTYPEOPERATION <> 7700004
        // AND o.CSTYPEOPERATION IN (7700010, 7700005)
        // GROUP BY s.IDFOURNISSEUR
        // ) tableA GROUP BY IDFOURDEBIT) table1
        // RIGHT JOIN (
        // SELECT SUM(o.MONTANT) as CREDIT, s.IDFOURNISSEUR as IDFOURCREDIT
        // FROM ccvdqua.LXSECTP s,ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION AND o.MONTANT > 0 AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // AND o.CSTYPEOPERATION NOT IN (7700004, 7700010, 7700005)
        // GROUP BY s.IDFOURNISSEUR
        // ) table2 on table1.IDFOURDEBIT = table2.IDFOURCREDIT
        // )
        // ) DEBITCREDIT, ccvdqua.LXFOURP FOURNISSEUR, ccvdqua.TITIERP TIERS
        // WHERE (DEBITCREDIT.IDFOURCREDIT = FOURNISSEUR.IDFOURNISSEUR OR DEBITCREDIT.IDFOURDEBIT =
        // FOURNISSEUR.IDFOURNISSEUR)
        // AND FOURNISSEUR.IDTIERS = TIERS.HTITIE
        // ORDER BY TIERS.HTLDE1 ASC

        sql.append("(");

        sql.append("SELECT ").append(LXImpressionBalanceManager.FIELD_DEBIT).append(", ")
                .append(LXImpressionBalanceManager.FIELD_CREDIT).append(",COALESCE(")
                .append(LXImpressionBalanceManager.FIELD_DEBIT).append(",0)+COALESCE(")
                .append(LXImpressionBalanceManager.FIELD_CREDIT).append(",0) as ")
                .append(LXImpressionBalanceManager.FIELD_SOLDE).append(", ")
                .append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(", ")
                .append(LXImpressionBalanceManager.FIELD_IDFOURCREDIT).append(" from ( ");
        getTableDebit(statement, sql);

        sql.append(" LEFT JOIN ");

        getTableCredit(statement, sql);
        sql.append(") ");

        sql.append(" UNION ");

        sql.append("SELECT ").append(LXImpressionBalanceManager.FIELD_DEBIT).append(", ")
                .append(LXImpressionBalanceManager.FIELD_CREDIT).append(",COALESCE(")
                .append(LXImpressionBalanceManager.FIELD_DEBIT).append(",0)+COALESCE(")
                .append(LXImpressionBalanceManager.FIELD_CREDIT).append(",0) as ")
                .append(LXImpressionBalanceManager.FIELD_SOLDE).append(", ")
                .append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(", ")
                .append(LXImpressionBalanceManager.FIELD_IDFOURCREDIT).append(" from ( ");
        getTableDebit(statement, sql);

        sql.append(" RIGHT JOIN ");

        getTableCredit(statement, sql);
        sql.append(") ");

        sql.append(") ").append(LXImpressionBalanceManager.SUBTABLE_DEBITCREDIT).append(", ");
        sql.append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(" ")
                .append(LXImpressionBalanceManager.SUBTABLE_FOURNISSEUR).append(", ");
        sql.append(_getCollection()).append("TITIERP").append(" ").append(LXImpressionBalanceManager.SUBTABLE_TIERS)
                .append(" ");

        return sql.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer ordreBy = new StringBuffer();
        ordreBy.append(LXImpressionBalanceManager.SUBTABLE_TIERS + ".HTLDE1 ASC");
        return ordreBy.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append("(");
        sqlWhere.append(LXImpressionBalanceManager.SUBTABLE_DEBITCREDIT).append(".")
                .append(LXImpressionBalanceManager.FIELD_IDFOURCREDIT).append(" = ")
                .append(LXImpressionBalanceManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDFOURNISSEUR).append(" OR ")
                .append(LXImpressionBalanceManager.SUBTABLE_DEBITCREDIT).append(".")
                .append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(" = ")
                .append(LXImpressionBalanceManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDFOURNISSEUR);
        sqlWhere.append(")");
        sqlWhere.append(" AND ").append(LXImpressionBalanceManager.SUBTABLE_FOURNISSEUR).append(".")
                .append(LXFournisseur.FIELD_IDTIERS).append(" = ").append(LXImpressionBalanceManager.SUBTABLE_TIERS)
                .append(".HTITIE");

        if (!JadeStringUtil.isIntegerEmpty(getForSelection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            if (LXConstants.SELECTION_OUVERT.equals(getForSelection())) {
                sqlWhere.append(LXImpressionBalanceManager.FIELD_SOLDE).append(" <> 0");
            } else if (LXConstants.SELECTION_SOLDE.equals(getForSelection())) {
                sqlWhere.append(LXImpressionBalanceManager.FIELD_SOLDE).append(" = 0");
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMini())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXImpressionBalanceManager.FIELD_SOLDE).append(" >= ").append(getForMontantMini());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMontantMaxi())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(LXImpressionBalanceManager.FIELD_SOLDE).append(" <= ").append(getForMontantMaxi());
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXImpressionBalance();
    }

    /**
     * Afficher factures bloquées
     * 
     * @return
     */
    public Boolean getEstBloque() {
        return estBloque;
    }

    public String getForCsCategorie() {
        return forCsCategorie;
    }

    public ArrayList<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsMotifBlocage() {
        return forCsMotifBlocage;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdSociete() {
        return forIdSociete;
    }

    public String getForMontantMaxi() {
        return forMontantMaxi;
    }

    public String getForMontantMini() {
        return forMontantMini;
    }

    public String getForSelection() {
        return forSelection;
    }

    /**
     * Return le sous-select qui recherche les totaux de crédits.
     * 
     * @param statement
     * @param sqlFrom
     */
    private void getTableCredit(BStatement statement, StringBuffer sqlFrom) {
        // Modification : sel
        // BugZilla : Bug 5323
        // SELECT SUM(o.MONTANT) as CREDIT, s.IDFOURNISSEUR as IDFOURCREDIT
        // FROM ccvdqua.LXSECTP s, ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION
        // AND o.MONTANT > 0
        // AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // and o.cstypeoperation not in (7700004, 7700010, 7700005)
        // GROUP BY s.IDFOURNISSEUR
        // ) table2 on table1.IDFOURDEBIT = table2.IDFOURCREDIT

        sqlFrom.append("(SELECT SUM(o.").append(LXOperation.FIELD_MONTANT).append(") as ")
                .append(LXImpressionBalanceManager.FIELD_CREDIT).append(", s.").append(LXSection.FIELD_IDFOURNISSEUR)
                .append(" as ").append(LXImpressionBalanceManager.FIELD_IDFOURCREDIT).append(" ");
        sqlFrom.append(" FROM ").append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" s,")
                .append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" o,").append(_getCollection())
                .append(LXFournisseur.TABLE_LXFOURP).append(" f");
        // f.IDFOURNISSEUR=s.idfournisseur
        sqlFrom.append(" WHERE ").append("f.").append(LXFournisseur.FIELD_IDFOURNISSEUR).append(" = s.")
                .append(LXSection.FIELD_IDFOURNISSEUR);
        sqlFrom.append(" AND ").append("s.").append(LXSection.FIELD_IDSECTION).append(" = o.")
                .append(LXOperation.FIELD_IDSECTION);
        sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_MONTANT).append(" > 0");
        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete())) {
            sqlFrom.append(" AND ").append("s.").append(LXSection.FIELD_IDSOCIETE).append(" = ")
                    .append(getForIdSociete());
        }
        if (getForCsEtatIn() != null) {
            sqlFrom.append(" AND ").append("o.")
                    .append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateDebut())) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_DATEOPERATION).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateFin())) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_DATEOPERATION).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }

        sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_CSTYPEOPERATION).append(" NOT IN (")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE).append(", ")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE).append(", ")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE).append(")");

        if (!getEstBloque()) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_ESTBLOQUE).append("=")
                    .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
            // and f.estbloque = '2'
            sqlFrom.append(" AND ").append("f.").append(LXFournisseur.FIELD_ESTBLOQUE).append("=")
                    .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        }

        sqlFrom.append(" GROUP BY ").append("s.").append(LXSection.FIELD_IDFOURNISSEUR).append(") table2 on table1.")
                .append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(" = table2.")
                .append(LXImpressionBalanceManager.FIELD_IDFOURCREDIT).append(" ");
    }

    /**
     * Return le sous-select qui recherche les totaux de débits.
     * 
     * @param statement
     * @param sqlFrom
     */
    private void getTableDebit(BStatement statement, StringBuffer sqlFrom) {
        // Modification : sel
        // BugZilla : Bug 5323
        // select SUM(DEBIT) as DEBIT, IDFOURDEBIT from (
        // SELECT SUM(o.MONTANT) as DEBIT, s.IDFOURNISSEUR as IDFOURDEBIT
        // FROM ccvdqua.LXSECTP s, ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION
        // AND o.MONTANT <= 0
        // AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // and o.cstypeoperation <> 7700004
        // GROUP BY s.IDFOURNISSEUR
        // union
        // -- Note de crédit
        // SELECT -SUM(o.MONTANT) as DEBIT, s.IDFOURNISSEUR as IDFOURDEBIT
        // FROM ccvdqua.LXSECTP s, ccvdqua.LXOPERP o
        // WHERE s.IDSECTION = o.IDSECTION
        // --AND o.MONTANT <= 0
        // AND s.IDSOCIETE = 1
        // AND o.CSETATOPERATION in (7800004, 7800005, 7800006)
        // and o.cstypeoperation <> 7700004
        // and o.cstypeoperation in (7700010, 7700005)
        // GROUP BY s.IDFOURNISSEUR
        // ) tableA GROUP BY IDFOURDEBIT
        // ) table1

        sqlFrom.append("(SELECT SUM(").append(LXImpressionBalanceManager.FIELD_DEBIT).append(") as ")
                .append(LXImpressionBalanceManager.FIELD_DEBIT).append(", ")
                .append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(" from (");

        sqlFrom.append("SELECT SUM(o.").append(LXOperation.FIELD_MONTANT).append(") as ")
                .append(LXImpressionBalanceManager.FIELD_DEBIT).append(", s.").append(LXSection.FIELD_IDFOURNISSEUR)
                .append(" as ").append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(" ");

        sqlFrom.append(" FROM ").append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" s,")
                .append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" o,").append(_getCollection())
                .append(LXFournisseur.TABLE_LXFOURP).append(" f");
        // f.IDFOURNISSEUR=s.idfournisseur
        sqlFrom.append(" WHERE ").append("f.").append(LXFournisseur.FIELD_IDFOURNISSEUR).append(" = s.")
                .append(LXSection.FIELD_IDFOURNISSEUR);
        sqlFrom.append(" AND ").append("s.").append(LXSection.FIELD_IDSECTION).append(" = o.")
                .append(LXOperation.FIELD_IDSECTION);
        sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_MONTANT).append(" <= 0");

        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete())) {
            sqlFrom.append(" AND ").append("s.").append(LXSection.FIELD_IDSOCIETE).append(" = ")
                    .append(getForIdSociete());
        }
        if (getForCsEtatIn() != null) {
            sqlFrom.append(" AND ").append("o.")
                    .append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateDebut())) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_DATEOPERATION).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateFin())) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_DATEOPERATION).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }
        sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_CSTYPEOPERATION).append(" <> ")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);

        if (!getEstBloque()) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_ESTBLOQUE).append("=")
                    .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
            // and f.estbloque = '2'
            sqlFrom.append(" AND ").append("f.").append(LXFournisseur.FIELD_ESTBLOQUE).append("=")
                    .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        }

        sqlFrom.append(" GROUP BY ").append("s.").append(LXSection.FIELD_IDFOURNISSEUR);

        sqlFrom.append(" UNION ");

        sqlFrom.append("SELECT -SUM(o.").append(LXOperation.FIELD_MONTANT).append(") as ")
                .append(LXImpressionBalanceManager.FIELD_DEBIT).append(", s.").append(LXSection.FIELD_IDFOURNISSEUR)
                .append(" as ").append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(" ");
        sqlFrom.append(" FROM ").append(_getCollection()).append(LXSection.TABLE_LXSECTP).append(" s,")
                .append(_getCollection()).append(LXOperation.TABLE_LXOPERP).append(" o,").append(_getCollection())
                .append(LXFournisseur.TABLE_LXFOURP).append(" f");
        // f.IDFOURNISSEUR=s.idfournisseur
        sqlFrom.append(" WHERE ").append("f.").append(LXFournisseur.FIELD_IDFOURNISSEUR).append(" = s.")
                .append(LXSection.FIELD_IDFOURNISSEUR);
        sqlFrom.append(" AND ").append("s.").append(LXSection.FIELD_IDSECTION).append(" = o.")
                .append(LXOperation.FIELD_IDSECTION);
        if (!JadeStringUtil.isIntegerEmpty(getForIdSociete())) {
            sqlFrom.append(" AND ").append("s.").append(LXSection.FIELD_IDSOCIETE).append(" = ")
                    .append(getForIdSociete());
        }
        if (getForCsEtatIn() != null) {
            sqlFrom.append(" AND ").append("o.")
                    .append(LXUtils.getWhereValueMultiple(LXOperation.FIELD_CSETATOPERATION, getForCsEtatIn()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateDebut())) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_DATEOPERATION).append(" >= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
        }
        if (!JadeStringUtil.isIntegerEmpty(getForDateFin())) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_DATEOPERATION).append(" <= ")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }
        sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_CSTYPEOPERATION).append(" <> ")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE);
        sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_CSTYPEOPERATION).append(" IN (")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE).append(", ")
                .append(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE).append(")");

        if (!getEstBloque()) {
            sqlFrom.append(" AND ").append("o.").append(LXOperation.FIELD_ESTBLOQUE).append("=")
                    .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
            // and f.estbloque = '2'
            sqlFrom.append(" AND ").append("f.").append(LXFournisseur.FIELD_ESTBLOQUE).append("=")
                    .append(BConstants.DB_BOOLEAN_FALSE_DELIMITED);
        }

        sqlFrom.append(" GROUP BY ").append("s.").append(LXSection.FIELD_IDFOURNISSEUR);
        sqlFrom.append(") tableA GROUP BY ").append(LXImpressionBalanceManager.FIELD_IDFOURDEBIT).append(") table1");

    }

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setForCsCategorie(String forCsCategorie) {
        this.forCsCategorie = forCsCategorie;
    }

    public void setForCsEtatIn(ArrayList<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsMotifBlocage(String forCsMotifBlocage) {
        this.forCsMotifBlocage = forCsMotifBlocage;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdSociete(String forIdSociete) {
        this.forIdSociete = forIdSociete;
    }

    public void setForMontantMaxi(String forMontantMaxi) {
        this.forMontantMaxi = forMontantMaxi;
    }

    public void setForMontantMini(String forMontantMini) {
        this.forMontantMini = forMontantMini;
    }

    public void setForSelection(String forSelection) {
        this.forSelection = forSelection;
    }
}
package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;

/**
 * @author SCO
 * @since 12 oct. 2010
 */
public class CEControlesPrevusManager extends BManager {

    private static final long serialVersionUID = 6495395109913969794L;
    private String forAnnee;

    @Override
    protected String _getSql(final BStatement statement) {

        String idrole = CEAffiliationService.getRoleForAffilieParitaire(getSession());
        String idRubrique = CEUtils.getIdRubrique(getSession());
        String anneePrecedente = CEUtils.getAnneePrecedente(getForAnnee());

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append("TIERS.HTLDE1, ");
        sql.append("TIERS.HTLDE2, ");
        sql.append("CONT.MAIAFF, ");
        sql.append("CONT.MALNAF, ");
        sql.append("CONT.MDDCDE, ");
        sql.append("CONT.MDDCFI, ");
        sql.append("ACONT.MDDEFF, ");
        sql.append("REV.MILVIS, ");
        sql.append("CONT.HTITIE, ");
        sql.append("CONT.MDNTJO, ");
        sql.append("CONT.MDTGEN, ");
        sql.append("AF.MADDEB, ");
        sql.append("AF.MADFIN, ");
        sql.append("GROUPE.CELGRP,");// nom du groupe
        sql.append("COUP.PCOUID AS CODESUVA,");
        sql.append("COUP.PCOLUT AS LIBELLESUVA,");
        sql.append("(SELECT SUM(CUMULMASSE) FROM ").append(_getCollection())
                .append("CACPTRP AS CPTR1 WHERE CPTR1.ANNEE = ").append(anneePrecedente)
                .append(" AND CPTR1.IDRUBRIQUE IN(").append(idRubrique)
                .append(")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE, ");
        sql.append("(SELECT COUNT(*) FROM ").append(_getCollection())
                .append("CIECRIP AS CI WHERE CONT.MAIAFF = CI.KBITIE AND CI.KBNANN = ").append(anneePrecedente)
                .append(") AS NBCI ");
        sql.append("FROM ").append(_getCollection()).append("CECONTP AS CONT ");
        sql.append("INNER JOIN ").append(_getCollection()).append("AFAFFIP AS AF ON (CONT.MAIAFF = AF.MAIAFF) ");
        sql.append("LEFT OUTER JOIN ").append(_getCollection())
                .append("CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = AF.HTITIE  AND CA.IDROLE = ")
                .append(idrole).append(") ");
        sql.append("INNER JOIN ").append(_getCollection()).append("TITIERP AS TIERS ON (TIERS.HTITIE = CONT.HTITIE) ");
        sql.append("LEFT OUTER JOIN ").append(_getCollection()).append("CEREVIP AS REV on (CONT.MDICTL = REV.MIIREV) ");
        sql.append("LEFT JOIN ").append(_getCollection())
                .append("CECONTP ACONT ON (ACONT.MAIAFF = CONT.MAIAFF AND ACONT.MDDEFF <> 0 AND ACONT.MDBFDR = '1') ");
        sql.append("LEFT JOIN " + _getCollection() + "cememp as membre on (membre.maiaff = AF.maiaff) ");
        sql.append("LEFT JOIN " + _getCollection() + "cegrpp as groupe on (groupe.ceidgr = membre.ceidgr) ");
        sql.append("LEFT JOIN " + _getCollection() + "FWCOUP as coup on (AF.MATSUV = coup.pcosid AND coup.PLAIDE='"
                + getSession().getIdLangue() + "') ");
        sql.append("WHERE ");
        sql.append("CONT.MDDPRE >= ").append(getForAnnee()).append("0101 ");
        sql.append("AND CONT.MDDPRE <= ").append(getForAnnee()).append("1231 ");
        sql.append("AND CONT.MDBFDR = '1' ");
        sql.append("AND CONT.MDDEFF = 0 ");
        sql.append("AND (ACONT.MDDCFI = (SELECT MAX(X.MDDCFI) FROM " + _getCollection()
                + CEControleEmployeur.TABLE_CECONTP
                + " X WHERE X.MAIAFF = CONT.MAIAFF AND X.MDDEFF <> 0 AND X.MDBFDR = '1') OR ACONT.MDDCFI IS NULL)");

        sql.append("ORDER BY CONT.MALNAF ");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControlesPrevus();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(final String forAnnee) {
        this.forAnnee = forAnnee;
    }
}

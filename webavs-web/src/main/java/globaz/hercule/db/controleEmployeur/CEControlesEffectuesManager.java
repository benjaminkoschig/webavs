package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since 7 déc. 2010
 */
public class CEControlesEffectuesManager extends BManager {

    private static final long serialVersionUID = -7662697507653375777L;
    private String forAnnee;
    private String forVisaReviseur;
    private String fromDateImpression;
    private String toDateImpression;

    @Override
    protected String _getSql(final BStatement statement) {

        String idrole = CEAffiliationService.getRoleForAffilieParitaire(getSession());

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append("TIERS.HTLDE1, ");
        sql.append("TIERS.HTLDE2, ");
        sql.append("CONT.MAIAFF, ");
        sql.append("CONT.MALNAF, ");
        sql.append("CONT.MDDCDE, ");
        sql.append("CONT.MDDCFI, ");
        sql.append("REV.MILVIS, ");
        sql.append("CONT.HTITIE, ");
        sql.append("CONT.MDNTJO, ");
        sql.append("CONT.MDTGEN, ");
        sql.append("CONT.CEDIMP, ");
        sql.append("CONT.MDDEFF, ");
        sql.append("CONT.MDDPRE, ");
        sql.append("AF.MADDEB, ");
        sql.append("AF.MADFIN, ");
        sql.append("GROUPE.CELGRP, ");// nom du groupe
        sql.append("COUP.PCOUID AS CODESUVA, ");
        sql.append("COUP.PCOLUT AS LIBELLESUVA ");
        sql.append("FROM ").append(_getCollection()).append("CECONTP AS CONT ");
        sql.append("INNER JOIN ").append(_getCollection()).append("AFAFFIP AS AF ON (CONT.MAIAFF = AF.MAIAFF) ");
        sql.append("LEFT OUTER JOIN ").append(_getCollection())
                .append("CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = AF.HTITIE  AND CA.IDROLE = ")
                .append(idrole).append(") ");
        sql.append("INNER JOIN ").append(_getCollection()).append("TITIERP AS TIERS ON (TIERS.HTITIE = CONT.HTITIE) ");
        sql.append("LEFT OUTER JOIN ").append(_getCollection()).append("CEREVIP AS REV on (CONT.MDICTL = REV.MIIREV) ");
        sql.append("LEFT JOIN " + _getCollection() + "cememp as membre on (membre.maiaff = AF.maiaff) ");
        sql.append("LEFT JOIN " + _getCollection() + "cegrpp as groupe on (groupe.ceidgr = membre.ceidgr) ");
        sql.append("LEFT JOIN " + _getCollection() + "FWCOUP as coup on (AF.MATSUV = coup.pcosid AND coup.PLAIDE='"
                + getSession().getIdLangue() + "') ");
        sql.append("WHERE ");
        sql.append("CONT.MDBFDR = '1' ");
        sql.append("AND CONT.MDDEFF <> 0 ");

        if (!JadeStringUtil.isBlankOrZero(getFromDateImpression())) {
            sql.append("AND CONT.CEDIMP >= ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getFromDateImpression())).append(" ");
        }
        if (!JadeStringUtil.isBlankOrZero(getToDateImpression())) {
            sql.append("AND CONT.CEDIMP <= ")
                    .append(_dbWriteDateAMJ(statement.getTransaction(), getToDateImpression())).append(" ");
        }
        if (!JadeStringUtil.isBlankOrZero(getForAnnee())) {
            sql.append("AND CONT.MDDPRE >= ").append(getForAnnee()).append("0101 ");
            sql.append("AND CONT.MDDPRE <= ").append(getForAnnee()).append("1231 ");
        }
        if (!JadeStringUtil.isEmpty(getForVisaReviseur())) {
            sql.append("AND REV.MILVIS = \'").append(getForVisaReviseur()).append("\' ");
        }
        sql.append("ORDER BY CONT.MALNAF ");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControlesEffectues();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForVisaReviseur() {
        return forVisaReviseur;
    }

    public String getFromDateImpression() {
        return fromDateImpression;
    }

    public String getToDateImpression() {
        return toDateImpression;
    }

    public void setForAnnee(final String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForVisaReviseur(final String forVisaReviseur) {
        this.forVisaReviseur = forVisaReviseur;
    }

    public void setFromDateImpression(final String fromDateImpression) {
        this.fromDateImpression = fromDateImpression;
    }

    public void setToDateImpression(final String toDateImpression) {
        this.toDateImpression = toDateImpression;
    }

}

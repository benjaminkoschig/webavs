package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Class permettant de récupérer les contrôles attribués
 * 
 * @author Sullivann Corneille
 */
public class CEControlesAttribuesManager extends BManager {

    private static final long serialVersionUID = 1L;
    private Boolean aEffectuer = new Boolean(false);
    private Boolean dejaEffectuer = new Boolean(false);
    private String forAnnee = new String();
    private String forAnnee_1 = new String();
    private String forGenreControle = new String();
    private String forVisaReviseur = new String();
    private Boolean tousLesControles = new Boolean(false);

    public String _getForAnnee_1() {
        int forAnnee = Integer.parseInt(this.forAnnee);
        forAnnee_1 = String.valueOf(forAnnee - 1);
        return forAnnee_1;
    }

    @Override
    protected String _getSql(final BStatement statement) {

        // SELECT
        // TIERS.HTLDE1,
        // TIERS.HTLDE2,
        // CONT.MAIAFF,
        // CONT.MALNAF,
        // CONT.MDDCDE,
        // CONT.MDDCFI,
        // ACONT.MDDEFF AS MDDPRC,
        // CONT.MDDEFF,
        // milvis,
        // CONT.HTITIE,
        // CONT.MDNTJO,
        // CONT.MDTGEN,
        // AF.MADDEB,
        // AF.MADFIN,
        // (SELECT SUM(CUMULMASSE) FROM CCVDQUA.CACPTRP AS CPTR1 WHERE ANNEE =
        // 2009 AND CPTR1.IDRUBRIQUE IN( 37,224,225) AND CA.IDCOMPTEANNEXE =
        // CPTR1.IDCOMPTEANNEXE ) AS MASSE1,
        // (SELECT COUNT(*) FROM CCVDQUA.CIECRIP AS CI WHERE CONT.MAIAFF =
        // CI.KBITIE AND CI.KBNANN = 2009) AS NBCI
        // FROM CCVDQUA.CECONTP AS CONT
        // INNER JOIN CCVDQUA.AFAFFIP AS AF ON (CONT.MAIAFF = AF.MAIAFF)
        // LEFT OUTER JOIN CCVDQUA.CACPTAP AS CA ON (AF.MALNAF =
        // CA.IDEXTERNEROLE AND CA.IDTIERS = AF.HTITIE AND CA.IDROLE = 517002)
        // INNER JOIN CCVDQUA.TITIERP AS TIERS ON (TIERS.HTITIE = CONT.HTITIE)
        // INNER JOIN CCVDQUA.cerevip on (CONT.mdictl=miirev)
        // LEFT JOIN CCVDQUA.CECONTP ACONT ON ACONT.MAIAFF = CONT.MAIAFF AND
        // ACONT.MDDCFI = (SELECT MAX(X.MDDCFI) FROM CCVDQUA.CECONTP X WHERE
        // X.MAIAFF = CONT.MAIAFF AND X.MDDEFF <> 0) AND ACONT.MDDEFF <> 0 AND
        // ACONT.MDBFDR = '1'
        // where CONT.MDDPRE >= 20100101 AND CONT.MDDPRE <= 20101231 AND
        // CONT.mdbfdr = '1' AND milvis <> '' AND CONT.MDDEFF = 0

        String idrole = CEAffiliationService.getRoleForAffilieParitaire(getSession());
        String idRubrique = CEUtils.getIdRubrique(getSession());
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append("TIERS.HTLDE1,"); // nom du tiers
        sql.append("TIERS.HTLDE2,");
        sql.append("CONT.MAIAFF,"); // idAffilie
        sql.append("CONT.MALNAF,"); // numAffilie
        sql.append("CONT.MDDCDE,"); // dateDebutControle
        sql.append("CONT.MDDCFI,"); // dateFinControle
        sql.append("ACONT.MDDEFF AS MDDPRC,"); // datePrecedentControle
        sql.append("CONT.MDDEFF,"); // dateEffective
        sql.append("milvis,"); // visaReviseur
        sql.append("CONT.HTITIE,"); // idTiers
        sql.append("CONT.MDNTJO,"); // tempsjournalier
        sql.append("CONT.MDTGEN,"); // typeControle
        sql.append("AF.MADDEB,");// date de début
        sql.append("AF.MADFIN,");// date de fin
        sql.append("GROUPE.CELGRP,");// nom du groupe
        sql.append("COUP.PCOUID AS CODESUVA,"); // Code SUVA
        sql.append("COUP.PCOLUT AS LIBELLESUVA,"); // Libelle suva

        sql.append("(SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + _getForAnnee_1());
        sql.append(" AND CPTR1.IDRUBRIQUE IN( " + idRubrique + ") ");

        sql.append(" AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE1,");

        sql.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE CONT.MAIAFF = CI.KBITIE AND CI.KBNANN = " + _getForAnnee_1() + ") AS NBCI");
        sql.append(" FROM " + _getCollection() + CEControleEmployeur.TABLE_CECONTP + " AS CONT");
        sql.append(" INNER JOIN " + _getCollection() + "AFAFFIP AS AF ON (CONT.MAIAFF = AF.MAIAFF)");
        // LEFT OUTER JOIN, car l'affilié doit quand même sortir s'il n'a pas
        // compte annexe
        sql.append(" LEFT OUTER JOIN " + _getCollection()
                + "CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = AF.HTITIE ");
        sql.append(" AND CA.IDROLE = " + idrole + ") ");
        sql.append(" INNER JOIN " + _getCollection() + "TITIERP AS TIERS ON (TIERS.HTITIE = CONT.HTITIE)");
        sql.append(" inner join " + _getCollection() + "cerevip on (CONT.mdictl=miirev) ");

        sql.append(" LEFT JOIN ").append(_getCollection())
                .append("CECONTP ACONT ON (ACONT.MAIAFF = CONT.MAIAFF AND ACONT.MDDEFF <> 0 AND ACONT.MDBFDR = '1')");
        sql.append(" LEFT JOIN " + _getCollection() + "cememp as membre on (membre.maiaff = AF.maiaff)");
        sql.append(" LEFT JOIN " + _getCollection() + "cegrpp as groupe on (groupe.ceidgr = membre.ceidgr)");
        sql.append(" LEFT JOIN " + _getCollection() + "FWCOUP as coup on (AF.MATSUV = coup.pcosid AND coup.PLAIDE='"
                + getSession().getIdLangue() + "') ");

        sql.append(" where CONT.MDDPRE >= " + getForAnnee() + "0101");
        // date effective doit être à 0 par check box
        sql.append(" AND CONT.MDDPRE <= " + getForAnnee() + "1231");

        if (!JadeStringUtil.isEmpty(getForGenreControle()) && !getForGenreControle().equals("tous")) {
            sql.append(" AND CONT.MDTGEN = " + getForGenreControle());
        } else {
            // sql.append(" AND CONT.MDTGEN <> '' ");
        }

        sql.append(" AND (ACONT.MDDCFI = (SELECT MAX(X.MDDCFI) FROM " + _getCollection()
                + CEControleEmployeur.TABLE_CECONTP
                + " X WHERE X.MAIAFF = CONT.MAIAFF AND X.MDDEFF <> 0 AND X.MDBFDR = '1') OR ACONT.MDDCFI IS NULL)");

        sql.append(" AND CONT.mdbfdr = '1' ");

        if (!JadeStringUtil.isEmpty(getForVisaReviseur()) && !getForVisaReviseur().equals("tous")) {
            sql.append(" AND milvis = '" + getForVisaReviseur() + "'");
        } else {
            sql.append(" AND milvis <> '' ");
        }

        if (getAEffectuer()) {
            sql.append(" AND CONT.MDDEFF = 0");
        }
        if (getDejaEffectuer()) {
            sql.append(" AND CONT.MDDEFF <> 0");
        }

        sql.append(" ORDER BY CONT.MALNAF ");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControlesAttribues();
    }

    public Boolean getaEffectuer() {
        return aEffectuer;
    }

    public Boolean getAEffectuer() {
        return aEffectuer;
    }

    public Boolean getDejaEffectuer() {
        return dejaEffectuer;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForGenreControle() {
        return forGenreControle;
    }

    public String getForVisaReviseur() {
        return forVisaReviseur;
    }

    public Boolean getTousLesControles() {
        return tousLesControles;
    }

    public void setaEffectuer(final Boolean aEffectuer) {
        this.aEffectuer = aEffectuer;
    }

    public void setAEffectuer(final Boolean effectuer) {
        aEffectuer = effectuer;
    }

    public void setDejaEffectuer(final Boolean dejaEffectuer) {
        this.dejaEffectuer = dejaEffectuer;
    }

    public void setForAnnee(final String string) {
        forAnnee = string;
    }

    public void setForGenreControle(final String string) {
        forGenreControle = string;
    }

    public void setForVisaReviseur(final String string) {
        forVisaReviseur = string;
    }

    public void setTousLesControles(final Boolean tousLesControles) {
        this.tousLesControles = tousLesControles;
    }
}

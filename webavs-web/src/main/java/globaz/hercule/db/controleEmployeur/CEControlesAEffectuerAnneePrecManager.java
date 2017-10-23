package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEAffiliationService;
import globaz.hercule.utils.CEUtils;
import globaz.naos.translation.CodeSystem;

/**
 * Manager permettant de récupérer les contrôles des années antérieures à l'année passé en paramettre. - qui sont actifs
 * - qui n'ont pas de date effective Ce manager récupère également des informations sur les masses des 4 dernières
 * années.
 * 
 * 
 * @author Sullivann Corneille
 */
public class CEControlesAEffectuerAnneePrecManager extends BManager {

    private static final long serialVersionUID = -3072172473141611810L;

    private String forAnnee = "";
    private String forAnneeCptr = "";

    @Override
    protected String _getFields(final BStatement statement) {

        String idRubriques = CEUtils.getIdRubrique(getSession());

        StringBuilder sqlSelect = new StringBuilder();

        sqlSelect
                .append("couverture.CEICOU, cont.mdicon, aff.malnaf, aff.maiaff, tiers.htitie,TIERS.HTLDE1,TIERS.HTLDE2,groupe.celgrp,aff.MADDEB,aff.madfin,aff.MATBRA,aff.MATCDN,aff.MALFED,");
        sqlSelect.append("COUP.PCOUID AS CODESUVA,COUP.PCOLUT AS LIBELLESUVA,");
        sqlSelect.append("(SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnneeCptr(), 0) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")   AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE1,");
        sqlSelect.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE aff.MAIAFF = CI.KBITIE AND CI.KBNANN = "
                + CEUtils.subAnnee(getForAnneeCptr(), 0) + ") AS NBCI1,");
        sqlSelect.append("(SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnneeCptr(), 1) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE2,");
        sqlSelect.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE aff.MAIAFF = CI.KBITIE AND CI.KBNANN = "
                + CEUtils.subAnnee(getForAnneeCptr(), 1) + ") AS NBCI2,");
        sqlSelect.append("(SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnneeCptr(), 2) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + " )  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE3,");
        sqlSelect.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE aff.MAIAFF = CI.KBITIE AND CI.KBNANN = "
                + CEUtils.subAnnee(getForAnneeCptr(), 2) + ") AS NBCI3,");
        sqlSelect.append("(SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnneeCptr(), 3) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + " )  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE4,");
        sqlSelect.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE aff.MAIAFF = CI.KBITIE AND CI.KBNANN = "
                + CEUtils.subAnnee(getForAnneeCptr(), 3) + ") AS NBCI4,");
        sqlSelect.append("(SELECT SUM(CUMULMASSE) FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + CEUtils.subAnnee(getForAnneeCptr(), 4) + " AND CPTR1.IDRUBRIQUE IN( " + idRubriques
                + ")  AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE5,");
        sqlSelect.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE aff.MAIAFF = CI.KBITIE AND CI.KBNANN = "
                + CEUtils.subAnnee(getForAnneeCptr(), 4) + ") AS NBCI5,");
        sqlSelect.append("(select max(MFDDEB) from " + _getCollection() + "afpartp as particularite where mftpar = "
                + CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL + " and mfddeb >= " + getForAnnee() + "0101 and mfdfin <= "
                + getForAnnee() + "1231 and aff.maiaff=particularite.maiaff) AS SANSPERSDEB,");
        sqlSelect.append("(select min(MFDFIN) from " + _getCollection() + "afpartp as particularite where mftpar = "
                + CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL + " and mfddeb >= " + getForAnnee() + "0101 and mfdfin <= "
                + getForAnnee() + "1231 and aff.maiaff=particularite.maiaff) AS SANSPERSFIN");

        return sqlSelect.toString();
    }

    @Override
    protected String _getFrom(final BStatement statement) {
        String idRole = CEAffiliationService.getRoleForAffilieParitaire(getSession());

        StringBuilder sqlFrom = new StringBuilder();

        sqlFrom.append(_getCollection() + "afaffip aff ");
        sqlFrom.append(" left outer join " + _getCollection()
                + "cecouvp as couverture on (couverture.maiaff = aff.maiaff and cebcav ='1')");
        sqlFrom.append(" left outer join " + _getCollection() + "cememp as membre on (membre.maiaff = aff.maiaff)");
        sqlFrom.append(" left outer join " + _getCollection() + "cegrpp as groupe on (groupe.ceidgr = membre.ceidgr)");
        sqlFrom.append(" LEFT OUTER JOIN " + _getCollection()
                + "CACPTAP AS CA ON (aff.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = aff.HTITIE  AND CA.IDROLE = "
                + idRole + ")");
        sqlFrom.append(" INNER JOIN " + _getCollection() + "titierp AS tiers ON (tiers.htitie = aff.htitie)");
        sqlFrom.append(" INNER JOIN " + _getCollection() + "cecontp as cont on cont.maiaff = aff.maiaff  ");
        sqlFrom.append(" LEFT JOIN " + _getCollection()
                + "FWCOUP as coup on (aff.MATSUV = coup.pcosid AND coup.PLAIDE='" + getSession().getIdLangue() + "') ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getOrder(final BStatement statement) {
        return " aff.malnaf ";
    }

    @Override
    protected String _getWhere(final BStatement statement) {

        StringBuilder sqlWhere = new StringBuilder();

        sqlWhere.append(" CONT.MDDPRE < " + getForAnnee() + "0101 ");
        sqlWhere.append(" AND CONT.MDBFDR = '1'");
        sqlWhere.append(" AND CONT.MDDEFF = 0 ");
        sqlWhere.append(" AND AFF.MATTAF IN (" + CodeSystem.TYPE_AFFILI_EMPLOY + ", "
                + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ")");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEControlesAEffectuer();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForAnneeCptr() {
        return forAnneeCptr;
    }

    public void setForAnnee(final String string) {
        forAnnee = string;
    }

    public void setForAnneeCptr(final String forAnneeCptr) {
        this.forAnneeCptr = forAnneeCptr;
    }

}

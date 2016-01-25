/*
 * Créé le 14 févr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;

/**
 * @author hpe
 * 
 */

public class AFControlesAEffectuerManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_PERIODICITE_AFFILIE = "818016";
    private String forAnnee = new String();
    private String forGenreControle = new String();
    private String forMasseSalA = new String();
    private String forMasseSalDe = new String();
    private Boolean isAvecReviseur = null;
    private int periodicite = 0;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * selectionne en une seule requete toutes les infos necessaires à la création de la liste des contrôles à effectuer
     * pour le contrôles des employeurs.
     * 
     * <p>
     * redefini car la requete est un peu compliquee.
     * </p>
     * 
     * 
     * SELECT * FROM
     * 
     * ( SELECT AF.MAIAFF,AF.MALNAF,AF.MADESL,CONT.MDDCDE,CONT.MDDCFI,CONT.MDDPRC ,CONT.MDLNOM,
     * AF.HTITIE,CONT.MDNTJO,CONT.MDTGEN,CONT.MDDPRE,CONT.MDICON, AF.MADFIN, AF.MADDEB,CONT.MDDEFF,
     * 
     * (SELECT CUMULMASSE FROM WEBAVS.CACPTRP AS CPTR1 WHERE ANNEE = 2006 AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE AND
     * CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE1,
     * 
     * (SELECT CUMULMASSE FROM WEBAVS.CACPTRP AS CPTR1 WHERE ANNEE = 2005 AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE AND
     * CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE2,
     * 
     * (SELECT CUMULMASSE FROM WEBAVS.CACPTRP AS CPTR1 WHERE ANNEE = 2004 AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE AND
     * CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE3,
     * 
     * (SELECT CUMULMASSE FROM WEBAVS.CACPTRP AS CPTR1 WHERE ANNEE = 2003 AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE AND
     * CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE4,
     * 
     * (SELECT COUNT(*) FROM WEBAVS.CIECRIP AS CI WHERE CONT.MAIAFF = CI.KBITIE AND CI.KBNANN = 2006) AS NBCI,
     * 
     * (SELECT MFMNUM FROM WEBAVS.AFPARTP AS PARTICULARITE WHERE AF.MAIAFF = PARTICULARITE.MAIAFF AND MFDDEB >= 20070101
     * AND MFDFIN <= 20071231 ) AS PERIODICITE
     * 
     * FROM WEBAVS.AFAFFIP AS AF INNER JOIN WEBAVS.CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE) INNER JOIN
     * WEBAVS.CACPTRP AS CPTR ON (CA.IDCOMPTEANNEXE = CPTR.IDCOMPTEANNEXE) INNER JOIN WEBAVS.CARUBRP AS RU ON
     * (CPTR.IDRUBRIQUE = RU.IDRUBRIQUE) INNER JOIN WEBAVS.AFASSUP AS ASS ON (RU.IDRUBRIQUE = ASS.MBIRUB) LEFT JOIN
     * WEBAVS.AFCONTP AS CONT ON (CONT.MAIAFF = AF.MAIAFF AND CONT.MDDPRE BETWEEN 20030101 AND 20071231)
     * 
     * WHERE ASS.MBTGEN = 801001 AND ASS.MBTTYP=812001
     * 
     * GROUP BY AF.MAIAFF,CPTR.IDRUBRIQUE,CPTR.IDCOMPTEANNEXE,CONT.MDLNOM,CONT.MDDPRC ,AF.MALNAF,AF.MADESL,
     * CONT.MDDCFI,CONT.MDDCDE,AF.HTITIE,CONT.MAIAFF,CONT.MDNTJO
     * ,CONT.MDTGEN,CONT.MDDPRE,CONT.MDICON,AF.MADFIN,AF.MADDEB
     * 
     * ORDER BY AF.MALNAF
     * 
     * ) AS a WHERE (
     * 
     * ( (a.MASSE1 > 1 AND a.MASSE1 < 9000000) OR (a.MASSE2 > 1 AND a.MASSE2 < 9000000) OR (a.MASSE3 > 1 AND a.MASSE3 <
     * 9000000) OR (a.MASSE4 > 1 AND a.MASSE4 < 9000000) ) ) GROUP BY MALNAF, MAIAFF, MADESL, MDICON, MDDCDE, MDDCFI,
     * MDDPRC, MDLNOM, HTITIE, MDNTJO, MDTGEN, MDDPRE, MADFIN, MADDEB, MDDEFF, MASSE1, MASSE2, MASSE3, MASSE4, NBCI,
     * PERIODICITE ORDER BY MALNAF, MDDPRE DESC
     * 
     * 
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(BStatement statement) {

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT * FROM ( ");

        sql.append("SELECT ");
        sql.append("AF.MAIAFF,"); // idAffilie
        sql.append("AF.MALNAF,"); // numAffilie
        sql.append("CONT.MDDCDE,"); // dateDebutControle
        sql.append("CONT.MDDCFI,"); // dateFinControle
        sql.append("CONT.MDDPRC,"); // datePrecedentControle
        sql.append("CONT.MDLNOM,"); // visaReviseur
        sql.append("AF.HTITIE,"); // idTiers
        sql.append("CONT.MDNTJO,"); // tempsjournalier
        sql.append("CONT.MDTGEN,"); // typeControle
        sql.append("CONT.MDDPRE, CONT.MDICON, AF.MADFIN, AF.MADDEB, CONT.MDDEFF, ");

        sql.append("(SELECT CUMULMASSE FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + getAnneePrecControle() + " AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE"
                + " AND CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE1,");

        sql.append("(SELECT CUMULMASSE FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + (Integer.parseInt(getAnneePrecControle()) - 1) + " AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE"
                + " AND CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE2,");

        sql.append("(SELECT CUMULMASSE FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + (Integer.parseInt(getAnneePrecControle()) - 2) + " AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE"
                + " AND CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE3,");

        sql.append("(SELECT CUMULMASSE FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = "
                + (Integer.parseInt(getAnneePrecControle()) - 3) + " AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE"
                + " AND CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE4,");

        sql.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE CONT.MAIAFF = CI.KBITIE AND CI.KBNANN = "
                + (Integer.parseInt(getAnneePrecControle()) - 1) + ") AS NBCI,");

        sql.append("(SELECT MFMNUM FROM " + _getCollection() + "AFPARTP AS PARTICULARITE WHERE "
                + " AF.MAIAFF = PARTICULARITE.MAIAFF" + " AND MFDDEB <=	" + getAnneeControle() + "0101"
                + " AND MFDFIN >= " + getAnneeControle() + "1231" + " AND MFTPAR = "
                + AFControlesAEffectuerManager.CS_PERIODICITE_AFFILIE + " ) AS PERIODICITE ");

        sql.append(" FROM " + _getCollection() + "AFAFFIP AS AF");

        sql.append(" INNER JOIN " + _getCollection() + "CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE)");
        sql.append(" INNER JOIN " + _getCollection() + "CACPTRP AS CPTR ON (CA.IDCOMPTEANNEXE = CPTR.IDCOMPTEANNEXE)");
        sql.append(" INNER JOIN " + _getCollection() + "CARUBRP AS RU ON (CPTR.IDRUBRIQUE = RU.IDRUBRIQUE)");
        sql.append(" INNER JOIN " + _getCollection() + "AFASSUP AS ASS ON (RU.IDRUBRIQUE = ASS.MBIRUB)");

        sql.append(" LEFT JOIN " + _getCollection()
                + "AFCONTP AS CONT ON (CONT.MALNAF = AF.MALNAF AND CONT.MDDPRE BETWEEN "
                + +(Integer.parseInt(getAnneePrecControle()) - 3) + "0101 AND " + Integer.parseInt(getForAnnee())
                + "1231)");

        sql.append(" WHERE ASS.MBTGEN=801001 AND ASS.MBTTYP=812001 AND (MADFIN >=" + getAnnee_1()
                + "0101 OR MADFIN = 0 )");

        // //s'il faut prendre aussi ceux attribués ou pas
        // if (!isAvecReviseur.booleanValue()){
        // sql.append(" AND (CONT.MDLNOM = '' OR CONT.MDLNOM IS NULL) ");
        // }

        // DGI 27.11.07 implémentation du setForGenreControle
        if (!JadeStringUtil.isEmpty(getForGenreControle())) {
            sql.append(" AND CONT.MDTGEN=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreControle()));
        }
        // DGI fin

        sql.append(" GROUP BY " + " AF.MAIAFF," + " CPTR.IDRUBRIQUE, " + " CPTR.IDCOMPTEANNEXE, " + " CONT.MDLNOM, "
                + " CONT.MDDPRC, " + " AF.MALNAF, " + " CONT.MDDCFI, " + " CONT.MDDCDE, " + " AF.HTITIE, "
                + " CONT.MAIAFF, " + " CONT.MDNTJO, " + " CONT.MDTGEN," + " CONT.MDDEFF,"
                + " CONT.MDDPRE, CONT.MDICON, AF.MADFIN, AF.MADDEB");

        sql.append(" ORDER BY AF.MALNAF ");

        sql.append(") AS a WHERE (");

        if (!JadeStringUtil.isIntegerEmpty(getForMasseSalA())) {

            sql.append("((a.MASSE1 > " + getForMasseSalDe() + " AND a.MASSE1 < " + getForMasseSalA());
            sql.append(") OR ");
            sql.append("(a.MASSE2 > " + getForMasseSalDe() + " AND a.MASSE2 < " + getForMasseSalA());
            sql.append(") OR ");
            sql.append("(a.MASSE3 > " + getForMasseSalDe() + " AND a.MASSE3 < " + getForMasseSalA());
            sql.append(") OR ");
            sql.append("(a.MASSE4 > " + getForMasseSalDe() + " AND a.MASSE4 < " + getForMasseSalA() + ")))");

        } else {

            sql.append("((a.MASSE1 > " + getForMasseSalDe());
            sql.append(") OR ");
            sql.append("(a.MASSE2 > " + getForMasseSalDe());
            sql.append(") OR ");
            sql.append("(a.MASSE3 > " + getForMasseSalDe());
            sql.append(") OR ");
            sql.append("(a.MASSE4 > " + getForMasseSalDe() + ")))");

        }

        sql.append(" GROUP BY MALNAF, MAIAFF, MDICON, MDDCDE, MDDCFI, MDDPRC, MDLNOM, HTITIE, MDNTJO, MDTGEN, "
                + "MDDPRE, MADFIN, MADDEB, MDDEFF, MASSE1, MASSE2, MASSE3, MASSE4, NBCI, PERIODICITE");

        sql.append(" ORDER BY MALNAF, MDDPRE DESC");

        return sql.toString();

    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFControlesAEffectuer();
    }

    public String getAnnee_1() {
        return String.valueOf((Integer.parseInt(getAnneeControle()) - 1));
    }

    /**
     * @return
     */
    public String getAnneeControle() {
        return getForAnnee();
    }

    /**
     * @return
     */
    public String getAnneeDebutPerContr() {
        return String.valueOf(Integer.parseInt(getForAnnee()) - 4);
    }

    /**
     * @return
     */
    public String getAnneeFinPerContr() {
        return String.valueOf(Integer.parseInt(getForAnnee()) - 1);
    }

    /**
     * @return
     */
    public String getAnneePrecControle() {
        return String.valueOf(Integer.parseInt(getForAnnee()) - 1);
    }

    /**
     * @return
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return
     */
    public String getForGenreControle() {
        return forGenreControle;
    }

    /**
     * @return
     */
    public String getForMasseSalA() {

        if (JadeStringUtil.isIntegerEmpty(forMasseSalA)) {
            return "";
        } else {
            return JANumberFormatter.round(JANumberFormatter.deQuote(forMasseSalA), 1, 0, JANumberFormatter.SUP);
        }

    }

    /**
     * @return
     */
    public String getForMasseSalDe() {
        if (JadeStringUtil.isIntegerEmpty(forMasseSalDe)) {
            return "";
        } else {
            return JANumberFormatter.round(JANumberFormatter.deQuote(forMasseSalDe), 1, 0, JANumberFormatter.SUP);
        }
    }

    /**
     * @return
     */
    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    /**
     * @return
     */
    public int getPeriodicite() throws NumberFormatException, Exception {
        return Integer.parseInt(getSession().getApplication().getProperty(
                AFApplication.PERIODICITECONTROLEEMPLOYEURCAISSE));

    }

    /**
     * @param string
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }

    /**
     * @param string
     */
    public void setForGenreControle(String string) {
        forGenreControle = string;
    }

    /**
     * @param string
     */
    public void setForMasseSalA(String string) {
        forMasseSalA = string;
    }

    /**
     * @param string
     */
    public void setForMasseSalDe(String string) {
        forMasseSalDe = string;
    }

    /**
     * @param boolean1
     */
    public void setIsAvecReviseur(Boolean boolean1) {
        isAvecReviseur = boolean1;
    }

    public void setPeriodicite(int periodicite) {
        this.periodicite = periodicite;
    }

}

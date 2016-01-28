/*
 * Créé le 13 févr. 07
 */
package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author hpe
 * 
 */

public class AFControlesAttribuesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = new String();
    private String forAnnee_1 = new String();
    private String forAnnee_2 = new String();
    private String forAnnee_3 = new String();
    private String forAnnee_4 = new String();
    private String forAnnee_5 = new String();
    private String forGenreControle = new String();
    private String forVisaReviseur = new String();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        // TODO Auto-generated method stub
        return "cont.malnaf";
    }

    /**
     * selectionne en une seule requete toutes les infos necessaires au bouclement ALFA des caisses horlogeres.
     * 
     * <p>
     * redefini car la requete est un peu compliquee.
     * </p>
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(BStatement statement) {

        /*
         * 
         * Requête =
         * 
         * SELECT CONT.MAIAFF, CONT.MALNAF, CONT.MDDCDE, CONT.MDDCFI, CONT.MDDPRC, CONT.MDLNOM, CONT.HTITIE,
         * CONT.MDNTJO, CONT.MDTGEN,
         * 
         * (SELECT CUMULMASSE FROM WEBAVS.CACPTRP AS CPTR1 WHERE ANNEE = getForAnnee_1() AND CPTR.IDRUBRIQUE =
         * CPTR1.IDRUBRIQUE AND CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE2,
         * 
         * (SELECT COUNT(*) FROM WEBAVS.CIECRIP AS CI WHERE CONT.MAIAFF = CI.KBITIE AND CI.KBNANN = getForAnnee_1()) AS
         * NBCI
         * 
         * 
         * FROM WEBAVS.AFAFFIP AS AF INNER JOIN WEBAVS.CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE) INNER JOIN
         * WEBAVS.CACPTRP AS CPTR ON (CA.IDCOMPTEANNEXE = CPTR.IDCOMPTEANNEXE) INNER JOIN WEBAVS.CARUBRP AS RU ON
         * (CPTR.IDRUBRIQUE = RU.IDRUBRIQUE) INNER JOIN WEBAVS.AFASSUP AS ASS ON (RU.IDRUBRIQUE = ASS.MBIRUB) INNER JOIN
         * WEBAVS.AFCONTP AS CONT ON (CONT.MAIAFF = AF.MAIAFF)
         * 
         * WHERE
         * 
         * ASS.MBIASS = 10 AND CONT.MDDEFF >= getForAnnee()0101 AND CONT.MDDEFF <= getForAnnee()1231 AND CONT.MDTGEN =
         * getForGenreControle() AND CONT.MDLNOM <> ''
         * 
         * GROUP BY CONT.MAIAFF, CPTR.IDRUBRIQUE, CPTR.IDCOMPTEANNEXE, CONT.MDLNOM, CONT.MDDPRC, CONT.MALNAF,
         * CONT.MDDCFI, CONT.MDDCDE, CONT.HTITIE, CONT.MDNTJO, CONT.MDTGEN
         */

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append("CONT.MAIAFF,"); // idAffilie
        sql.append("CONT.MALNAF,"); // numAffilie
        sql.append("CONT.MDDCDE,"); // dateDebutControle
        sql.append("CONT.MDDCFI,"); // dateFinControle
        sql.append("CONT.MDDPRC,"); // datePrecedentControle
        sql.append("CONT.MDLNOM,"); // visaReviseur
        sql.append("CONT.HTITIE,"); // idTiers
        sql.append("CONT.MDNTJO,"); // tempsjournalier
        sql.append("CONT.MDTGEN,"); // typeControle

        sql.append("(SELECT CUMULMASSE FROM " + _getCollection() + "CACPTRP AS CPTR1 WHERE ANNEE = " + getForAnnee_1()
                + " AND CPTR.IDRUBRIQUE = CPTR1.IDRUBRIQUE"
                + " AND CPTR.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE ) AS MASSE1,");

        sql.append("(SELECT COUNT(*) FROM " + _getCollection()
                + "CIECRIP AS CI WHERE CONT.MAIAFF = CI.KBITIE AND CI.KBNANN = " + getForAnnee_1() + ") AS NBCI");

        sql.append(" FROM " + _getCollection() + "AFCONTP AS CONT");

        sql.append(" INNER JOIN " + _getCollection() + "AFAFFIP AS AF ON (CONT.MALNAF = AF.MALNAF)");
        sql.append(" INNER JOIN " + _getCollection() + "CACPTAP AS CA ON (AF.MALNAF = CA.IDEXTERNEROLE)");
        sql.append(" INNER JOIN " + _getCollection() + "CACPTRP AS CPTR ON (CA.IDCOMPTEANNEXE = CPTR.IDCOMPTEANNEXE)");
        sql.append(" INNER JOIN " + _getCollection() + "CARUBRP AS RU ON (CPTR.IDRUBRIQUE = RU.IDRUBRIQUE)");
        sql.append(" INNER JOIN " + _getCollection() + "AFASSUP AS ASS ON (RU.IDRUBRIQUE = ASS.MBIRUB)");

        sql.append(" WHERE ");

        sql.append("ASS.MBTGEN=801001 AND ASS.MBTTYP=812001 ");

        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            sql.append(" AND CONT.MDDPRE >= " + getForAnnee() + "0101");
            sql.append(" AND CONT.MDDPRE <= " + getForAnnee() + "1231");
        }
        sql.append(" AND CONT.MDDEFF = 0 ");
        if (!JadeStringUtil.isEmpty(getForGenreControle()) && !getForGenreControle().equals("tous")) {
            sql.append(" AND CONT.MDTGEN = " + getForGenreControle());
        } else {
            // sql.append(" AND CONT.MDTGEN <> '' ");
        }

        if (!JadeStringUtil.isEmpty(getForVisaReviseur()) && !getForVisaReviseur().equals("tous")) {
            sql.append(" AND CONT.MDLNOM = '" + getForVisaReviseur() + "'");
        }

        sql.append(" GROUP BY " + " CONT.MAIAFF," + " CPTR.IDRUBRIQUE, " + " CPTR.IDCOMPTEANNEXE, " + " CONT.MDLNOM, "
                + " CONT.MDDPRC, " + " CONT.MALNAF, " + " CONT.MDDCFI, " + " CONT.MDDCDE, " + " CONT.HTITIE, "
                + " CONT.MDNTJO, " + " CONT.MDTGEN");

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
        return new AFControlesAttribues();
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
    public String getForAnnee_1() {

        int forAnnee = Integer.parseInt(this.forAnnee);
        forAnnee_1 = String.valueOf(forAnnee - 1);

        return forAnnee_1;
    }

    /**
     * @return
     */
    public String getForAnnee_2() {

        int forAnnee = Integer.parseInt(this.forAnnee);
        forAnnee_2 = String.valueOf(forAnnee - 2);

        return forAnnee_2;
    }

    /**
     * @return
     */
    public String getForAnnee_3() {

        int forAnnee = Integer.parseInt(this.forAnnee);
        forAnnee_3 = String.valueOf(forAnnee - 3);

        return forAnnee_3;
    }

    /**
     * @return
     */
    public String getForAnnee_4() {

        int forAnnee = Integer.parseInt(this.forAnnee);
        forAnnee_4 = String.valueOf(forAnnee - 4);

        return forAnnee_4;
    }

    /**
     * @return
     */
    public String getForAnnee_5() {

        int forAnnee = Integer.parseInt(this.forAnnee);
        forAnnee_5 = String.valueOf(forAnnee - 5);

        return forAnnee_5;
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
    public String getForVisaReviseur() {
        return forVisaReviseur;
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
    public void setForVisaReviseur(String string) {
        forVisaReviseur = string;
    }

}

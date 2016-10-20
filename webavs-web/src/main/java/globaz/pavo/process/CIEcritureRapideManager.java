package globaz.pavo.process;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CIEcriture;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIEcritureRapideManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeCotisation;
    private String forCode;
    private String forCodeSpecial;
    private String forExtourne;
    private String forGenre;
    private String forGenreSixSept;
    private String forMoisFin;
    private java.lang.String forNomEspion;
    private String forNotExtourne;
    private java.lang.String forNumeroAffilie;
    private String fromAnneeCotisation;
    private java.lang.String fromDateInscription;
    private java.lang.String fromNumeroJournal;
    private String notForCodeSpecial3;
    private String toAnneeCotisation;
    private java.lang.String toDateInscription;
    private java.lang.String toNumeroJournal;
    private java.lang.String tri;
    private String inCodeSpecial;

    public String getInCodeSpecial() {
        return inCodeSpecial;
    }

    public void setInCodeSpecial(String inCodeSpecial) {
        this.inCodeSpecial = inCodeSpecial;
    }

    /**
     * Constructor for CIEcritureRapideManager.
     */
    public CIEcritureRapideManager() {
        super();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = _getCollection() + "CIECRIP ";
        return sqlFrom;
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        String sqlOrder;
        // Tri sur le numéro AVS ou date d'inscription
        if ("avs".equals(getTri())) {
            sqlOrder = _getCollection() + "CIINDIP.KANAVS, MALNAF";
        } else if ("numAff".equals(getTri())) {
            sqlOrder = _getCollection() + "AFAFFIP.MALNAF, KANAVS";
        } else if ("date".equals(getTri())) {
            sqlOrder = "SUBSTR(" + _getCollection() + "CIECRIP.KBLESP,1,8), MALNAF, KANAVS";
            // sqlOrder="";
        } else if ("anneeCotisation".equals(getTri())) {
            sqlOrder = _getCollection() + "CIECRIP.KBNANN";
        } else {
            sqlOrder = "";
        }
        return sqlOrder;
    }

    /**
     * retourne la clause WHERE de la requete SQL + jointure
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        String sqlWhere = "";

        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromAnneeCotisation))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toAnneeCotisation))) {
            // Si from et to sont égaux
            if (fromAnneeCotisation.equals(toAnneeCotisation)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN=" + fromAnneeCotisation;
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN>=" + fromAnneeCotisation + " AND "
                        + _getCollection() + "CIECRIP.KBNANN<=" + toAnneeCotisation;
            }
        }
        // Seul from est non vide
        else if (!globaz.globall.util.JAUtil.isStringEmpty(fromAnneeCotisation)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN>=" + fromAnneeCotisation;
        } else if (!globaz.globall.util.JAUtil.isStringEmpty(toAnneeCotisation)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN<=" + toAnneeCotisation;
        }
        // Test sur la date d'inscription
        if ((!globaz.globall.util.JAUtil.isStringEmpty(fromDateInscription))
                && (!globaz.globall.util.JAUtil.isStringEmpty(toDateInscription))) {
            // Si from et to sont égaux
            if (fromDateInscription.equals(toDateInscription)) {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP like '"
                        + _dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "%'";
            } else {
                sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP>='"
                        + _dbWriteDateAMJ(statement.getTransaction(), fromDateInscription) + "' AND "
                        + _getCollection() + "CIECRIP.KBLESP<='"
                        + _dbWriteDateAMJ(statement.getTransaction(), toDateInscription) + "999999zzzzzz" + "'";
            }
        }
        if (!globaz.globall.util.JAUtil.isStringEmpty(forNomEspion)) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBLESP LIKE '%" + forNomEspion + "%' ";
        }

        if (!JAUtil.isStringEmpty(getForCode())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTCOD=  " + getForCode();
        }

        if (!JAUtil.isStringEmpty(getForGenre())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN= " + getForGenre();
        }
        if (!JAUtil.isStringEmpty(getForGenreSixSept())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTGEN IN ( " + CIEcriture.CS_CIGENRE_6 + ", "
                    + CIEcriture.CS_CIGENRE_7 + " ) ";
        }
        if (!JAUtil.isStringEmpty(getForAnneeCotisation())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNANN = "
                    + _dbWriteNumeric(statement.getTransaction(), getForAnneeCotisation());
        }
        if (!JAUtil.isStringEmpty(getForExtourne())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT = "
                    + _dbWriteNumeric(statement.getTransaction(), getForExtourne());
        }
        // /fonctionne seulement pour les extourne de genre 1
        if (!JAUtil.isStringEmpty(getForNotExtourne())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTEXT <> "
                    + _dbWriteNumeric(statement.getTransaction(), CIEcriture.CS_EXTOURNE_1);
        }
        if (!JAUtil.isStringEmpty(getForCodeSpecial())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBTSPE = " + getForCodeSpecial();

        }

        if (!JadeStringUtil.isBlank(getInCodeSpecial())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CIECRIP.KBTSPE IN (" + getInCodeSpecial() + ")";
        }

        if (!JadeStringUtil.isBlank(getForMoisFin())) {
            sqlWhere += " AND " + _getCollection() + "CIECRIP.KBNMOF = "
                    + _dbWriteNumeric(statement.getTransaction(), getForMoisFin());
        }

        if (sqlWhere.startsWith(" AND")) {
            sqlWhere = sqlWhere.substring(5);
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIEcritureRapide();
    }

    /**
     * Returns the forAnneeCotisation.
     * 
     * @return String
     */
    public String getForAnneeCotisation() {
        return forAnneeCotisation;
    }

    /**
     * Returns the forCode.
     * 
     * @return String
     */
    public String getForCode() {
        return forCode;
    }

    /**
     * Returns the forCodeSpecial.
     * 
     * @return String
     */
    public String getForCodeSpecial() {
        return forCodeSpecial;
    }

    /**
     * Returns the forExtourne.
     * 
     * @return String
     */
    public String getForExtourne() {
        return forExtourne;
    }

    /**
     * Returns the forGenre.
     * 
     * @return String
     */
    public String getForGenre() {
        return forGenre;
    }

    /**
     * Returns the forGenreSixSept.
     * 
     * @return String
     */
    public String getForGenreSixSept() {
        return forGenreSixSept;
    }

    /**
     * @return
     */
    public String getForMoisFin() {
        return forMoisFin;
    }

    /**
     * Returns the forNomEspion.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNomEspion() {
        return forNomEspion;
    }

    /**
     * Returns the forNotExtourne.
     * 
     * @return String
     */
    public String getForNotExtourne() {
        return forNotExtourne;
    }

    /**
     * Returns the forNumeroAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    /**
     * Returns the fromDateInscription.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateInscription() {
        return fromDateInscription;
    }

    /**
     * Returns the fromNumeroJournal.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumeroJournal() {
        return fromNumeroJournal;
    }

    /**
     * Returns the notForCodeSpecial3.
     * 
     * @return String
     */
    public String getNotForCodeSpecial3() {
        return notForCodeSpecial3;
    }

    /**
     * Returns the toDateInscription.
     * 
     * @return java.lang.String
     */
    public java.lang.String getToDateInscription() {
        return toDateInscription;
    }

    /**
     * Returns the toNumeroJournal.
     * 
     * @return java.lang.String
     */
    public java.lang.String getToNumeroJournal() {
        return toNumeroJournal;
    }

    /**
     * Returns the tri.
     * 
     * @return java.lang.String
     */
    public java.lang.String getTri() {
        return tri;
    }

    /**
     * Sets the forAnneeCotisation.
     * 
     * @param forAnneeCotisation
     *            The forAnneeCotisation to set
     */
    public void setForAnneeCotisation(String forAnneeCotisation) {
        this.forAnneeCotisation = forAnneeCotisation;
    }

    /**
     * Sets the forCode.
     * 
     * @param forCode
     *            The forCode to set
     */
    public void setForCode(String forCode) {
        this.forCode = forCode;
    }

    /**
     * Sets the forCodeSpecial.
     * 
     * @param forCodeSpecial
     *            The forCodeSpecial to set
     */
    public void setForCodeSpecial(String forCodeSpecial) {
        this.forCodeSpecial = forCodeSpecial;
    }

    /**
     * Sets the forExtourne.
     * 
     * @param forExtourne
     *            The forExtourne to set
     */
    public void setForExtourne(String forExtourne) {
        this.forExtourne = forExtourne;
    }

    /**
     * Sets the forGenre.
     * 
     * @param forGenre
     *            The forGenre to set
     */
    public void setForGenre(String forGenre) {
        this.forGenre = forGenre;
    }

    /**
     * Sets the forGenreSixSept.
     * 
     * @param forGenreSixSept
     *            The forGenreSixSept to set
     */
    public void setForGenreSixSept(String forGenreSixSept) {
        this.forGenreSixSept = forGenreSixSept;
    }

    /**
     * @param string
     */
    public void setForMoisFin(String string) {
        forMoisFin = string;
    }

    /**
     * Sets the forNomEspion.
     * 
     * @param forNomEspion
     *            The forNomEspion to set
     */
    public void setForNomEspion(java.lang.String forNomEspion) {
        this.forNomEspion = forNomEspion;
    }

    /**
     * Sets the forNotExtourne.
     * 
     * @param forNotExtourne
     *            The forNotExtourne to set
     */
    public void setForNotExtourne(String forNotExtourne) {
        this.forNotExtourne = forNotExtourne;
    }

    /**
     * Sets the forNumeroAffilie.
     * 
     * @param forNumeroAffilie
     *            The forNumeroAffilie to set
     */
    public void setForNumeroAffilie(java.lang.String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * Sets the fromDateInscription.
     * 
     * @param fromDateInscription
     *            The fromDateInscription to set
     */
    public void setFromDateInscription(java.lang.String fromDateInscription) {
        this.fromDateInscription = fromDateInscription;
    }

    /**
     * Sets the fromNumeroJournal.
     * 
     * @param fromNumeroJournal
     *            The fromNumeroJournal to set
     */
    public void setFromNumeroJournal(java.lang.String fromNumeroJournal) {
        this.fromNumeroJournal = fromNumeroJournal;
    }

    /**
     * Sets the notForCodeSpecial3.
     * 
     * @param notForCodeSpecial3
     *            The notForCodeSpecial3 to set
     */
    public void setNotForCodeSpecial3(String notForCodeSpecial3) {
        this.notForCodeSpecial3 = notForCodeSpecial3;
    }

    /**
     * Sets the toDateInscription.
     * 
     * @param toDateInscription
     *            The toDateInscription to set
     */
    public void setToDateInscription(java.lang.String toDateInscription) {
        this.toDateInscription = toDateInscription;
    }

    /**
     * Sets the toNumeroJournal.
     * 
     * @param toNumeroJournal
     *            The toNumeroJournal to set
     */
    public void setToNumeroJournal(java.lang.String toNumeroJournal) {
        this.toNumeroJournal = toNumeroJournal;
    }

    /**
     * Sets the tri.
     * 
     * @param tri
     *            The tri to set
     */
    public void setTri(java.lang.String tri) {
        this.tri = tri;
    }

}

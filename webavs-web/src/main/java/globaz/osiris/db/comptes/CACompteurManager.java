package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRubrique;
import java.io.Serializable;

public class CACompteurManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_ANNEE = "2";
    public final static String ORDER_COMPTEUR = "1";
    public final static String SELECTION_TOUS = "-1";
    public final static String SELECTION_STANDARD = "-2";
    public final static String SELECTION_IRRECOUVRABLE = "-3";
    public final static String SELECTION_AMORTISSEMENT = "-4";
    public final static String SELECTION_RECOUVREMENT = "-5";
    public final static String SELECTION_COT_AVEC_MASSE = "-6";
    public final static String SELECTION_COT_SANS_MASSE = "-7";

    private String forAnnee = new String();
    private String forAnneeBefore;
    private String forIdCompteAnnexe = new String();
    private String forIdRubrique = new String();
    private String forIdRubriqueIn = "";
    private boolean forSelectDistinct = false;
    private String forSelectionCompteur = new String();

    private String forSelectionTri = new String();

    /**
     * @see BManager#_getFields(BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (isForSelectDistinct()) {
            return "DISTINCT " + _getCollection() + CACompteur.TABLE_CACPTRP + "." + CACompteur.FIELD_IDRUBRIQUE + ", "
                    + _getCollection() + CACompteur.TABLE_CACPTRP + "." + CACompteur.FIELD_IDCOMPTEANNEXE + ", "
                    + _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDEXTERNE;
        }

        return super._getFields(statement);
    }

    /**
     * @see BManager#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CACompteur.TABLE_CACPTRP + " INNER JOIN " + _getCollection()
                + CARubrique.TABLE_CARUBRP + " ON " + _getCollection() + CACompteur.TABLE_CACPTRP + "."
                + CACompteur.FIELD_IDRUBRIQUE + "=" + _getCollection() + CARubrique.TABLE_CARUBRP + "."
                + CARubrique.FIELD_IDRUBRIQUE;
    }

    /**
     * @see BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (isForSelectDistinct()) {
            return _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDEXTERNE;
        }

        if (getForSelectionTri().equalsIgnoreCase(CACompteurManager.ORDER_COMPTEUR)) {
            return _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDEXTERNE + ", "
                    + CACompteur.FIELD_ANNEE + " DESC";
        }

        if (getForSelectionTri().equalsIgnoreCase(CACompteurManager.ORDER_ANNEE)) {
            return CACompteur.FIELD_ANNEE + " DESC," + _getCollection() + CARubrique.TABLE_CARUBRP + "."
                    + CARubrique.FIELD_IDEXTERNE;
        }

        return "";
    }

    /**
     * @see BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // Traitement du positionnement pour un id compte annexe
        if (getForIdCompteAnnexe().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteur.FIELD_IDCOMPTEANNEXE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
        }

        // Traitement du positionnement pour un id rubrique
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CACompteur.TABLE_CACPTRP + "." + CACompteur.FIELD_IDRUBRIQUE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // Traitement du positionnement pour l'année
        if (getForAnnee().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteur.FIELD_ANNEE + "=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }

        // Traitement du positionnement pour un compteur
        if ((getForSelectionCompteur().length() != 0)
                && !getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_TOUS)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_STANDARD)) {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                        + " IN (" + APIRubrique.COTISATION_AVEC_MASSE + "," + APIRubrique.COTISATION_SANS_MASSE + ")";
            } else if (getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_IRRECOUVRABLE)) {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                        + " IN (" + APIRubrique.RECOUVREMENT + "," + APIRubrique.AMORTISSEMENT + ")";
            } else if (getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_AMORTISSEMENT)) {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                        + " IN (" + APIRubrique.AMORTISSEMENT + ")";
            } else if (getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_RECOUVREMENT)) {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                        + " IN (" + APIRubrique.RECOUVREMENT + ")";
            } else if (getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_COT_AVEC_MASSE)) {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                        + " IN (" + APIRubrique.COTISATION_AVEC_MASSE + ")";
            } else if (getForSelectionCompteur().equalsIgnoreCase(CACompteurManager.SELECTION_COT_SANS_MASSE)) {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_NATURERUBRIQUE
                        + " IN (" + APIRubrique.COTISATION_SANS_MASSE + ")";
            } else {
                sqlWhere += _getCollection() + CARubrique.TABLE_CARUBRP + "." + CARubrique.FIELD_IDEXTERNE + "="
                        + this._dbWriteString(statement.getTransaction(), getForSelectionCompteur());
            }
        }

        if (getForIdRubriqueIn().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CACompteur.TABLE_CACPTRP + "." + CACompteur.FIELD_IDRUBRIQUE + " IN ("
                    + getForIdRubriqueIn() + ") ";
        }

        if (!JadeStringUtil.isBlank(getForAnneeBefore())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CACompteur.FIELD_ANNEE + "<"
                    + this._dbWriteNumeric(statement.getTransaction(), getForAnneeBefore());
        }

        return sqlWhere;
    }

    /**
     * @see BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CACompteur();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:57:03)
     * 
     * @return String
     */
    public String getForAnnee() {
        return forAnnee;
    }

    public String getForAnneeBefore() {
        return forAnneeBefore;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:56:32)
     * 
     * @return String
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:56:48)
     * 
     * @return String
     */
    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getForIdRubriqueIn() {
        return forIdRubriqueIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2003 10:12:40)
     * 
     * @return String
     */
    public String getForSelectionCompteur() {
        return forSelectionCompteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2003 09:20:17)
     * 
     * @return String
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 07:56:08)
     * 
     * @return boolean
     */
    public boolean isForSelectDistinct() {
        return forSelectDistinct;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:57:03)
     * 
     * @param newForAnnee
     *            String
     */
    public void setForAnnee(String newForAnnee) {
        forAnnee = newForAnnee;
    }

    public void setForAnneeBefore(String forAnneeBefore) {
        this.forAnneeBefore = forAnneeBefore;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:56:32)
     * 
     * @param newForIdCompteAnnexe
     *            String
     */
    public void setForIdCompteAnnexe(String newForIdCompteAnnexe) {
        forIdCompteAnnexe = newForIdCompteAnnexe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 10:56:48)
     * 
     * @param newForIdRubrique
     *            String
     */
    public void setForIdRubrique(String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    public void setForIdRubriqueIn(String forIdRubriqueIn) {
        this.forIdRubriqueIn = forIdRubriqueIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 07:56:08)
     * 
     * @param newForSelectDistinct
     *            boolean
     */
    public void setForSelectDistinct(boolean newForSelectDistinct) {
        forSelectDistinct = newForSelectDistinct;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2003 10:12:40)
     * 
     * @param newForSelectionCompteur
     *            String
     */
    public void setForSelectionCompteur(String newForSelectionCompteur) {
        forSelectionCompteur = newForSelectionCompteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2003 09:20:17)
     * 
     * @param newForSelectionTri
     *            String
     */
    public void setForSelectionTri(String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

}

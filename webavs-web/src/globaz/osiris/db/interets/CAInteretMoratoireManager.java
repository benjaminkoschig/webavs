package globaz.osiris.db.interets;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Insérez la description du type ici. Date de création : (30.12.2002 09:15:06)
 * 
 * @author: Administrator
 */
public class CAInteretMoratoireManager extends BManager {
    /** Fichier CAIMDCP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IDJOURFAC) */
    private boolean forFacturable = false;
    private String forIdCompteAnnexe = new String();
    /** (IDGENREINTERET) */
    private String forIdGenreInteret = new String();
    private ArrayList forIdGenreInteretIn;
    /** (IDINTERETMORATOIRE) */
    private String forIdInteretMoratoire = new String();
    /** (IDJOURNALCALCUL) */
    private String forIdJournalCalcul = new String();
    private String forIdJournalFacturation = new String();
    private java.lang.String forIdPlan = new String();
    /** (IDRUBRIQUE) */
    private String forIdRubrique = new String();
    /** (IDSECTION) */
    private String forIdSection = new String();

    /** (IDSECTIONFACTURE) */
    private String forIdSectionFacture = new String();
    private String forMotifCalcul;

    private String forMotifCalculNot;
    /** (IDINTERETMORATOIRE) */
    private String fromIdInteretMoratoire = new String();

    private String IdCompteAnnexe = new String();

    private String inIdInteretMoratoire = new String();

    /**
     * Commentaire relatif au constructeur CAInteretMoratoireManager.
     */
    public CAInteretMoratoireManager() {
        super();
    }

    /**
     * Retourne la clause FROM de la requete SQL (la table)
     * 
     * @return String le nom de la table
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CAInteretMoratoire.TABLE_CAIMDCP;
    }

    /**
     * Retourne la clause ORDER BY de la requete SQL (la table)
     * 
     * @param BStatement
     *            le statement
     * @return String le ORDER BY
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Retourne la clause WHERE de la requete SQL
     * 
     * @param BStatement
     *            le statement
     * @return la clause WHERE
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(getInIdInteretMoratoire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDINTERETMORATOIRE + " in (" + getInIdInteretMoratoire() + ") ";
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdInteretMoratoire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDINTERETMORATOIRE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdInteretMoratoire());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSection())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDSECTION + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdRubrique())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDRUBRIQUE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdGenreInteret())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDGENREINTERET + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdGenreInteret());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdSectionFacture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDSECTIONFACTURE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSectionFacture());
        }

        if (!JadeStringUtil.isBlank(getForIdJournalCalcul())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDJOURNALCALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalCalcul());
        }

        if (!JadeStringUtil.isBlank(getForIdJournalFacturation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDJOUFAC + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdJournalFacturation());
        }

        if (isForFacturable()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_MOTIFCALCUL + " IN ("
                    + this._dbWriteNumeric(statement.getTransaction(), CAInteretMoratoire.CS_SOUMIS) + ","
                    + this._dbWriteNumeric(statement.getTransaction(), CAInteretMoratoire.CS_MANUEL) + ")";
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromIdInteretMoratoire())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDINTERETMORATOIRE + ">="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromIdInteretMoratoire());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdPlan())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_IDPLAN + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlan());
        }

        if (getForIdGenreInteretIn() != null) {
            String tmp = new String();

            Iterator iter = getForIdGenreInteretIn().iterator();
            while (iter.hasNext()) {
                String element = (String) iter.next();

                if (tmp.length() != 0) {
                    tmp += ", ";
                }

                tmp += this._dbWriteNumeric(statement.getTransaction(), element);
            }

            if (!JadeStringUtil.isBlank(tmp)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += CAInteretMoratoire.FIELD_IDGENREINTERET + " in (" + tmp + ")";
            }
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMotifCalcul())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_MOTIFCALCUL + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForMotifCalcul());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForMotifCalculNot())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CAInteretMoratoire.FIELD_MOTIFCALCUL + "<>"
                    + this._dbWriteNumeric(statement.getTransaction(), getForMotifCalculNot());
        }

        return sqlWhere;
    }

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAInteretMoratoire();
    }

    /**
     * @return
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public String getForIdGenreInteret() {
        return forIdGenreInteret;
    }

    public ArrayList getForIdGenreInteretIn() {
        return forIdGenreInteretIn;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getForIdInteretMoratoire() {
        return forIdInteretMoratoire;
    }

    public String getForIdJournalCalcul() {
        return forIdJournalCalcul;
    }

    /**
     * Returns the forIdJournalFacturation.
     * 
     * @return String
     */
    public String getForIdJournalFacturation() {
        return forIdJournalFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 15:30:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdPlan() {
        return forIdPlan;
    }

    public String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getForIdSection() {
        return forIdSection;
    }

    public String getForIdSectionFacture() {
        return forIdSectionFacture;
    }

    public String getForMotifCalcul() {
        return forMotifCalcul;
    }

    public String getForMotifCalculNot() {
        return forMotifCalculNot;
    }

    public String getFromIdInteretMoratoire() {
        return fromIdInteretMoratoire;
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return IdCompteAnnexe;
    }

    public String getInIdInteretMoratoire() {
        return inIdInteretMoratoire;
    }

    /**
     * Returns the forFacturable.
     * 
     * @return boolean
     */
    public boolean isForFacturable() {
        return forFacturable;
    }

    /**
     * Sets the forFacturable.
     * 
     * @param forFacturable
     *            The forFacturable to set
     */
    public void setForFacturable(boolean forFacturable) {
        this.forFacturable = forFacturable;
    }

    /**
     * @param string
     */
    public void setForIdCompteAnnexe(String string) {
        forIdCompteAnnexe = string;
    }

    public void setForIdGenreInteret(String newForIdGenreInteret) {
        forIdGenreInteret = newForIdGenreInteret;
    }

    public void setForIdGenreInteretIn(ArrayList forIdGenreInteretIn) {
        this.forIdGenreInteretIn = forIdGenreInteretIn;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */

    public void setForIdInteretMoratoire(String newForIdInteretMoratoire) {
        forIdInteretMoratoire = newForIdInteretMoratoire;
    }

    public void setForIdJournalCalcul(String newForIdJournalCalcul) {
        forIdJournalCalcul = newForIdJournalCalcul;
    }

    /**
     * Sets the forIdJournalFacturation.
     * 
     * @param forIdJournalFacturation
     *            The forIdJournalFacturation to set
     */
    public void setForIdJournalFacturation(String forIdJournalFacturation) {
        this.forIdJournalFacturation = forIdJournalFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.05.2003 15:30:30)
     * 
     * @param newForIdPlan
     *            java.lang.String
     */
    public void setForIdPlan(java.lang.String newForIdPlan) {
        forIdPlan = newForIdPlan;
    }

    public void setForIdRubrique(String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    public void setForIdSection(String newForIdSection) {
        forIdSection = newForIdSection;
    }

    public void setForIdSectionFacture(String newForIdSectionFacture) {
        forIdSectionFacture = newForIdSectionFacture;
    }

    public void setForMotifCalcul(String forMotifCalcul) {
        this.forMotifCalcul = forMotifCalcul;
    }

    public void setForMotifCalculNot(String forMotifCalculNot) {
        this.forMotifCalculNot = forMotifCalculNot;
    }

    public void setFromIdInteretMoratoire(String newFromIdInteretMoratoire) {
        fromIdInteretMoratoire = newFromIdInteretMoratoire;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        IdCompteAnnexe = string;
    }

    public void setInIdInteretMoratoire(String newInIdInteretMoratoire) {
        inIdInteretMoratoire = newInIdInteretMoratoire;
    }

}

package globaz.musca.db.facturation;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.musca.util.FARightHelper;

public class FAPassageManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forDateComptabilisation = new String();
    private java.lang.String forDateCreation = new String();
    private java.lang.String forDateEcheance = new String();
    private java.lang.String forDateFacturation = new String();
    private java.lang.String forDatePeriode = new String();
    private String forExceptIdPassage = new String();
    private String forExceptStatus = new String();
    private java.lang.String forIdJournal = new String();
    private java.lang.String forIdPassage = new String();
    private java.lang.String forIdPlanFacturation = new String();
    private java.lang.String forIdRemarque = new String();
    private java.lang.String forIdTypeFacturation = new String();
    private Boolean forIsAuto = null;
    private java.lang.String forLibelleLike = new String();
    private Boolean forPassageBloque = null;
    private java.lang.String forStatus = new String();
    private String forUserCreateur = "";
    private java.lang.String fromDateComptabilisation = new String();
    private java.lang.String fromDateCreation = new String();
    private java.lang.String fromDateEcheance = new String();
    private java.lang.String fromDateFacturation = new String();
    private java.lang.String fromDatePeriode = new String();
    private java.lang.String fromIdPassage = new String();
    private java.lang.String fromLibelle = new String();
    protected java.lang.String order = "IDPASSAGE";
    /** Permet de filtrer les passages selon les droits accordé à l'utilisateur */
    private Boolean wantFilterByPlanFacturation = new Boolean(false);

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAPassage.TABLE_FIELDS;
    }

    public Boolean _getForIsAuto() {
        return forIsAuto;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAPASSP AS FAPASSP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (_getForIsAuto() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " FAPASSP.AUTO = "
                    + this._dbWriteBoolean(statement.getTransaction(), _getForIsAuto(), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (getForUserCreateur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " FAPASSP.PROP = " + this._dbWriteString(statement.getTransaction(), getForUserCreateur());
        }

        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDPASSAGE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());

        }
        if (getForExceptIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDPASSAGE <>"
                    + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdPassage());
        }
        if (getFromIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDPASSAGE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdPassage());
        }

        if (getForIdPlanFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDPLANFACTURATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdPlanFacturation());
        }

        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDREMARQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        if (getForIdTypeFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDTYPEFACTURATION="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeFacturation());
        }

        if (getForDatePeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATEPERIODE=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDatePeriode());
        }

        if (getForDateFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATEFACTURATION="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFacturation());
        }

        if (getForDateCreation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATECREATION="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateCreation());
        }

        if (getForDateComptabilisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATECOMPTABILISATION="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateComptabilisation());
        }

        if (getForDateEcheance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATEECHEANCE="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getForDateEcheance());
        }

        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDJOURNAL=" + this._dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        if (getForStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            // La requête de NON-COMPATABLISE affiche tout sauf les
            // comptabilisés ET les annulés
            if (FAPassage.CS_ETAT_NON_COMPTABILISE.equals(getForStatus())) {
                sqlWhere += "FAPASSP.STATUS<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAPassage.CS_ETAT_COMPTABILISE);
                sqlWhere += " AND FAPASSP.STATUS<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAPassage.CS_ETAT_ANNULE);
            } else {
                sqlWhere += "FAPASSP.STATUS=" + this._dbWriteNumeric(statement.getTransaction(), getForStatus());
            }
        }
        if (getForExceptStatus().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // La requête de NON-COMPATABLISE affiche tout sauf les
            // comptabilisés ET les annulés
            if (FAPassage.CS_ETAT_OUVERT.equals(getForExceptStatus())) {
                sqlWhere += "FAPASSP.STATUS<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAPassage.CS_ETAT_OUVERT);
                sqlWhere += " AND FAPASSP.STATUS<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAPassage.CS_ETAT_ANNULE);
            } else {
                sqlWhere += "FAPASSP.STATUS <>"
                        + this._dbWriteNumeric(statement.getTransaction(), getForExceptStatus());
            }
        }

        if (getFromLibelle().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.LIBELLEPASSAGE>=" + this._dbWriteString(statement.getTransaction(), getFromLibelle());
        }

        if (getForLibelleLike().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER(FAPASSP.LIBELLEPASSAGE) like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForLibelleLike().toUpperCase() + "%");
        }

        if (getFromDatePeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATEPERIODE>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDatePeriode());
        }

        if (getFromDateFacturation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATEFACTURATION>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFacturation());
        }

        if (getFromDateCreation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATECREATION>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateCreation());
        }

        if (getFromDateComptabilisation().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATECOMPTABILISTION>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateComptabilisation());
        }

        if (getFromDateEcheance().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.DATEECHEANCE>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateEcheance());
        }

        if ((getWantFilterByPlanFacturation() != null) && getWantFilterByPlanFacturation().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAPASSP.IDPLANFACTURATION in ("
                    + FARightHelper.getIdPlanFacturationRightsInString(getSession(), FWSecureConstants.READ) + ")";
        }

        if (getForPassageBloque() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " FAPASSP.ESTVERROUILLE = "
                    + this._dbWriteBoolean(statement.getTransaction(), getForPassageBloque(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAPassage();
    }

    /**
     * Le nom de la méthode a été prefixé par un _ pour éviter que setBeanProperties initialise forIsAuto à false Ainsi
     * si cette méthode n'est pas explicitement appelée forIsAuto est null et tous les journaux sont pris (voir
     * _getWhere)
     */
    public void _setForIsAuto(Boolean newForIsAuto) {
        forIsAuto = newForIsAuto;
    }

    public java.lang.String getForDateComptabilisation() {
        return forDateComptabilisation;
    }

    public java.lang.String getForDateCreation() {
        return forDateCreation;
    }

    public java.lang.String getForDateEcheance() {
        return forDateEcheance;
    }

    public java.lang.String getForDateFacturation() {
        return forDateFacturation;
    }

    public java.lang.String getForDatePeriode() {
        return forDatePeriode;
    }

    public String getForExceptIdPassage() {
        return forExceptIdPassage;
    }

    public String getForExceptStatus() {
        return forExceptStatus;
    }

    public java.lang.String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdPassage() {
        return forIdPassage;
    }

    public java.lang.String getForIdPlanFacturation() {
        return forIdPlanFacturation;
    }

    public java.lang.String getForIdRemarque() {
        return forIdRemarque;
    }

    public java.lang.String getForIdTypeFacturation() {
        return forIdTypeFacturation;
    }

    /**
     * Returns the forLibelleLike.
     * 
     * @return java.lang.String
     */
    public java.lang.String getForLibelleLike() {
        return forLibelleLike;
    }

    public Boolean getForPassageBloque() {
        return forPassageBloque;
    }

    public java.lang.String getForStatus() {
        return forStatus;
    }

    public String getForUserCreateur() {
        return forUserCreateur;
    }

    public java.lang.String getFromDateComptabilisation() {
        return fromDateComptabilisation;
    }

    public java.lang.String getFromDateCreation() {
        return fromDateCreation;
    }

    public java.lang.String getFromDateEcheance() {
        return fromDateEcheance;
    }

    public java.lang.String getFromDateFacturation() {
        return fromDateFacturation;
    }

    public java.lang.String getFromDatePeriode() {
        return fromDatePeriode;
    }

    /**
     * Returns the fromIdPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromIdPassage() {
        return fromIdPassage;
    }

    public java.lang.String getFromLibelle() {
        return fromLibelle;
    }

    /**
     * Permet de filtrer les passages selon les droits accordé à l'utilisateur
     */
    public Boolean getWantFilterByPlanFacturation() {
        return wantFilterByPlanFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateFacturationCroissant() {
        order = "DATEFACTURATION";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByDateFacturationDecroissant() {
        order = "DATEFACTURATION DESC";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdPassageCroissant() {
        order = "IDPASSAGE";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByIdPassageDecroissant() {
        order = "IDPASSAGE DESC";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelle() {
        order = "UPPER(LIBELLEPASSAGE)";
    }

    /*
     * Nécessaire à cause de l'initilaisation de l'order à l'IDPASSAGE et conmme la programmagation est faite de façon
     * qu'in ne peut avoir qu'un seul order (écrasement et non concaténation) => dommage!
     */
    public void orderDesire(String orderVoulu) {
        order = orderVoulu;
    }

    public void setForDateComptabilisation(java.lang.String newForDateComptabilisation) {
        forDateComptabilisation = newForDateComptabilisation;
    }

    public void setForDateCreation(java.lang.String newForDateCreation) {
        forDateCreation = newForDateCreation;
    }

    public void setForDateEcheance(java.lang.String newForDateEcheance) {
        forDateEcheance = newForDateEcheance;
    }

    public void setForDateFacturation(java.lang.String newForDateFacturation) {
        forDateFacturation = newForDateFacturation;
    }

    public void setForDatePeriode(java.lang.String newForDatePeriode) {
        forDatePeriode = newForDatePeriode;
    }

    public void setForExceptIdPassage(String forExceptIdPassage) {
        this.forExceptIdPassage = forExceptIdPassage;
    }

    public void setForExceptStatus(String forExceptStatus) {
        this.forExceptStatus = forExceptStatus;
    }

    public void setForIdJournal(java.lang.String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    /**
     * Setter
     */
    public void setForIdPassage(java.lang.String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    public void setForIdPlanFacturation(java.lang.String newForIdPlanFacturation) {
        forIdPlanFacturation = newForIdPlanFacturation;
    }

    public void setForIdRemarque(java.lang.String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    public void setForIdTypeFacturation(java.lang.String newForIdTypeFacturation) {
        forIdTypeFacturation = newForIdTypeFacturation;
    }

    /**
     * Sets the forLibelleLike.
     * 
     * @param forLibelleLike
     *            The forLibelleLike to set
     */
    public void setForLibelleLike(java.lang.String forLibelleLike) {
        this.forLibelleLike = forLibelleLike;
    }

    public void setForPassageBloque(Boolean forPassageBloque) {
        this.forPassageBloque = forPassageBloque;
    }

    public void setForStatus(java.lang.String newForStatus) {
        forStatus = newForStatus;
    }

    public void setForUserCreateur(String newForUserCreateur) {
        forUserCreateur = newForUserCreateur;
    }

    public void setFromDateComptabilisation(java.lang.String newFromDateComptabilisation) {
        fromDateComptabilisation = newFromDateComptabilisation;
    }

    public void setFromDateCreation(java.lang.String newFromDateCreation) {
        fromDateCreation = newFromDateCreation;
    }

    public void setFromDateEcheance(java.lang.String newFromDateEcheance) {
        fromDateEcheance = newFromDateEcheance;
    }

    public void setFromDateFacturation(java.lang.String newFromDateFacturation) {
        fromDateFacturation = newFromDateFacturation;
    }

    public void setFromDatePeriode(java.lang.String newFromDatePeriode) {
        fromDatePeriode = newFromDatePeriode;
    }

    /**
     * Sets the fromIdPassage.
     * 
     * @param fromIdPassage
     *            The fromIdPassage to set
     */
    public void setFromIdPassage(java.lang.String fromIdPassage) {
        this.fromIdPassage = fromIdPassage;
    }

    public void setFromLibelle(java.lang.String newFromLibelle) {
        fromLibelle = newFromLibelle;
    }

    /**
     * Permet de filtrer les passages selon les droits accordé à l'utilisateur
     */
    public void setWantFilterByPlanFacturation(Boolean boolean1) {
        wantFilterByPlanFacturation = boolean1;
    }

}

package globaz.osiris.db.comptes;

/**
 */
public class CACompteCourantManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String beginWithIdExterne = new String();
    private java.lang.String forIdCompteCourant = new String();
    private java.lang.String forIdExterne = new String();
    private java.lang.String forIdRubrique = new String();
    private Boolean forJournalDebit = false;
    private java.lang.String forSelectionCompte = new String();
    private java.lang.String fromDescription = new String();
    private java.lang.String fromNumero = new String();

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        if (((getFromNumero().length() != 0) || (getFromDescription().length() != 0))
                && !(this instanceof CACompteCourantManagerListViewBean)) {
            // return _getCollection()+"CACPTCV1";
            return _getCollection() + "CACPTCP LEFT OUTER JOIN " + _getCollection() + "CARUBRP ON " + _getCollection()
                    + "CACPTCP.IDRUBRIQUE=" + _getCollection() + "CARUBRP.IDRUBRIQUE" + " LEFT OUTER JOIN "
                    + _getCollection() + "PMTRADP ON " + _getCollection() + "CARUBRP.IDTRADUCTION=" + _getCollection()
                    + "PMTRADP.IDTRADUCTION";
        } else {
            return _getCollection() + "CACPTCP ";
        }
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getFromDescription().length() != 0) {
            return "LIBELLE";
        } else {
            return _getCollection() + "CACPTCP.IDEXTERNE";
        }

    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdCompteCourant().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTECOURANT=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteCourant());
        }

        // traitement du positionnement
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDRUBRIQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // Traitement du positionnement pour une sélection des comptes
        if ((getForSelectionCompte().length() != 0) && !getForSelectionCompte().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            switch (java.lang.Integer.parseInt(getForSelectionCompte())) {
                case 1:
                    sqlWhere += "SOLDE <> 0";
                    break;
                case 2:
                    sqlWhere += "SOLDE = 0";
                    break;
                default:
                    break;
            }
        }

        // Sélection du choix de la langue si recherche par numéro ou/et par
        // description
        if (((getFromNumero().length() != 0) || (getFromDescription().length() != 0))
                && !(this instanceof CACompteCourantManagerListViewBean)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CODEISOLANGUE="
                    + this._dbWriteString(statement.getTransaction(), statement.getTransaction().getSession()
                            .getIdLangueISO());
        }

        // traitement du positionnement depuis un numéro
        if (getFromNumero().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CACPTCP.IDEXTERNE>="
                    + this._dbWriteString(statement.getTransaction(), getFromNumero());
        }

        // traitement du positionnement depuis un numéro
        if (getForIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CACPTCP.IDEXTERNE="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterne());
        }

        // traitement du positionnement depuis une description
        if (getFromDescription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LIBELLE>=" + this._dbWriteString(statement.getTransaction(), getFromDescription());
        }

        // traitement du positionnement pour IdExterne
        if (getBeginWithIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CACPTCP.IDEXTERNE LIKE '" + getBeginWithIdExterne() + "%'";
        }

        if (getForJournalDebit()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + CACompteCourant.TABLE_CACPTCP + "." + CACompteCourant.FIELD_JOURNALDESDEBIT
                    + "='1'";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CACompteCourant();
    }

    public java.lang.String getBeginWithIdExterne() {
        return beginWithIdExterne;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 13:32:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdExterne() {
        return forIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 14:34:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    /**
     * @return the forJournalDebit
     */
    public Boolean getForJournalDebit() {
        return forJournalDebit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2002 14:18:00)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionCompte() {
        return forSelectionCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 12:09:33)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDescription() {
        return fromDescription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 11:22:06)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromNumero() {
        return fromNumero;
    }

    public void setBeginWithIdExterne(java.lang.String newBeginWithIdExterne) {
        beginWithIdExterne = newBeginWithIdExterne;
    }

    /**
     * Setter
     */
    public void setForIdCompteCourant(java.lang.String newForIdCompteCourant) {
        forIdCompteCourant = newForIdCompteCourant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2002 13:32:58)
     * 
     * @param newForIdExterne
     *            java.lang.String
     */
    public void setForIdExterne(java.lang.String newForIdExterne) {
        forIdExterne = newForIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 14:34:34)
     * 
     * @param newForIdRubrique
     *            java.lang.String
     */
    public void setForIdRubrique(java.lang.String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    /**
     * @param forJournalDebit
     *            the forJournalDebit to set
     */
    public void setForJournalDebit(Boolean forJournalDebit) {
        this.forJournalDebit = forJournalDebit;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2002 14:18:00)
     * 
     * @param newForSelectionCompte
     *            java.lang.String
     */
    public void setForSelectionCompte(java.lang.String newForSelectionCompte) {
        forSelectionCompte = newForSelectionCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 12:09:33)
     * 
     * @param newFromDescription
     *            java.lang.String
     */
    public void setFromDescription(java.lang.String newFromDescription) {
        fromDescription = newFromDescription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.02.2002 11:22:06)
     * 
     * @param newFromNumero
     *            java.lang.String
     */
    public void setFromNumero(java.lang.String newFromNumero) {
        fromNumero = newFromNumero;
    }
}

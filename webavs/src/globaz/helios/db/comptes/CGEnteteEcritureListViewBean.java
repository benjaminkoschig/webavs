package globaz.helios.db.comptes;

public class CGEnteteEcritureListViewBean extends globaz.globall.db.BManager implements
        globaz.framework.bean.FWListViewBeanInterface, java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forContrepartieAvoir = "";
    private java.lang.String forContrepartieDoit = "";
    private java.lang.String forDate = "";
    private java.lang.String forIdEnteteEcriture = "";
    private java.lang.String forIdJournal = "";
    private java.lang.String forIdSecteurAVS = "";
    private java.lang.String forIdTypeEcriture = "";
    private java.lang.String forNombreAvoir = "";
    private java.lang.String forNombreDoit = "";
    private java.lang.String notForTypeEcriture = "";

    /**
     * Getter
     */

    /**
     * Setter
     */

    /**
     * Commentaire relatif au constructeur CGEnteteEcritureManager.
     */
    public CGEnteteEcritureListViewBean() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGECREP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";
        // traitement du positionnement
        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDJOURNAL=" + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }
        if (getForIdSecteurAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEURAVS=" + _dbWriteNumeric(statement.getTransaction(), getForIdSecteurAVS());
        }
        if (getForIdTypeEcriture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEECRITURE=" + _dbWriteNumeric(statement.getTransaction(), getForIdTypeEcriture());
        }

        if (getForIdEnteteEcriture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDENTETEECRITURE=" + _dbWriteNumeric(statement.getTransaction(), getForIdEnteteEcriture());
        }

        if (getNotForTypeEcriture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTYPEECRITURE<>" + _dbWriteNumeric(statement.getTransaction(), getNotForTypeEcriture());
        }
        if (getForNombreAvoir().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NOMBREAVOIR=" + _dbWriteNumeric(statement.getTransaction(), getForNombreAvoir());
        }
        if (getForNombreDoit().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NOMBREDOIT=" + _dbWriteNumeric(statement.getTransaction(), getForNombreDoit());
        }

        if (getForContrepartieAvoir().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCONTREPARTIEAVOIR=" + _dbWriteNumeric(statement.getTransaction(), getForContrepartieAvoir());
        }
        if (getForContrepartieDoit().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCONTREPARTIEDOIT=" + _dbWriteNumeric(statement.getTransaction(), getForContrepartieDoit());
        }

        if (getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATE=" + _dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGEnteteEcritureViewBean();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 13:27:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForContrepartieAvoir() {
        return forContrepartieAvoir;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 13:27:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForContrepartieDoit() {
        return forContrepartieDoit;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.05.2003 13:50:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForDate() {
        return forDate;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 15:45:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdEnteteEcriture() {
        return forIdEnteteEcriture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.12.2002 16:13:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 15:05:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdSecteurAVS() {
        return forIdSecteurAVS;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.05.2003 13:58:54)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTypeEcriture() {
        return forIdTypeEcriture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 11:25:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNombreAvoir() {
        return forNombreAvoir;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 11:25:15)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForNombreDoit() {
        return forNombreDoit;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 15:45:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNotForTypeEcriture() {
        return notForTypeEcriture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 13:27:36)
     * 
     * @param newForContrepartieAvoir
     *            java.lang.String
     */
    public void setForContrepartieAvoir(java.lang.String newForContrepartieAvoir) {
        forContrepartieAvoir = newForContrepartieAvoir;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 13:27:18)
     * 
     * @param newForContrepartieDoit
     *            java.lang.String
     */
    public void setForContrepartieDoit(java.lang.String newForContrepartieDoit) {
        forContrepartieDoit = newForContrepartieDoit;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.05.2003 13:50:48)
     * 
     * @param newForDate
     *            java.lang.String
     */
    public void setForDate(java.lang.String newForDate) {
        forDate = newForDate;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 15:45:26)
     * 
     * @param newForIdEnteteEcriture
     *            java.lang.String
     */
    public void setForIdEnteteEcriture(java.lang.String newForIdEnteteEcriture) {
        forIdEnteteEcriture = newForIdEnteteEcriture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.12.2002 16:13:44)
     * 
     * @param newForIdJournal
     *            java.lang.String
     */
    public void setForIdJournal(java.lang.String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.03.2003 15:05:49)
     * 
     * @param newForIdSecteurAVS
     *            java.lang.String
     */
    public void setForIdSecteurAVS(java.lang.String newForIdSecteurAVS) {
        forIdSecteurAVS = newForIdSecteurAVS;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.05.2003 13:58:54)
     * 
     * @param newForIdTypeEcriture
     *            java.lang.String
     */
    public void setForIdTypeEcriture(java.lang.String newForIdTypeEcriture) {
        forIdTypeEcriture = newForIdTypeEcriture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 11:25:29)
     * 
     * @param newNombreAvoir
     *            java.lang.String
     */
    public void setForNombreAvoir(java.lang.String newNombreAvoir) {
        forNombreAvoir = newNombreAvoir;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 11:25:15)
     * 
     * @param newNombreDoit
     *            java.lang.String
     */
    public void setForNombreDoit(java.lang.String newNombreDoit) {
        forNombreDoit = newNombreDoit;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.07.2003 15:45:43)
     * 
     * @param newNotForTypeEcriture
     *            java.lang.String
     */
    public void setNotForTypeEcriture(java.lang.String newNotForTypeEcriture) {
        notForTypeEcriture = newNotForTypeEcriture;
    }
}

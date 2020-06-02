/*
 * Créé le 6 févr. 06
 */
package globaz.osiris.db.contentieux;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * @author sch date : 6 févr. 06
 */
public class CAMotifContentieuxManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = "";



    private String fromDateDebut = "";
    private String forDateFin = "";
    private String forDateInYear = "";
    private String forIdCompteAnnexe = "";
    private String forIdMotifBlocage = "";
    private String forIdSection = "";
    private String fromDateBetweenDebutFin = "";
    private String fromIdMotifContentieux = "";
    private Set<String> forIdMotifBlocageIn = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "CAMOCOP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "IDMOTIFCONT";
    }

    public Set<String> getForIdMotifBlocageIn() {
        return forIdMotifBlocageIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement du from pour l'id du motif de
        // contentieux
        if (getFromIdMotifContentieux().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMOTIFCONT>=" + this._dbWriteNumeric(statement.getTransaction(), getFromIdMotifContentieux());
        }
        // Si le compte annexe et la section sont sélectionnés, on fait un or
        if ((getForIdCompteAnnexe().length() != 0) && (getForIdSection().length() != 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(IDCOMPTEANNEXE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
            sqlWhere += " OR ";
            sqlWhere += "IDSECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection()) + ")";
        } else {
            // traitement du positionnement du for pour l'id du compte annexe
            if (getForIdCompteAnnexe().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IDCOMPTEANNEXE="
                        + this._dbWriteNumeric(statement.getTransaction(), getForIdCompteAnnexe());
            }

            // traitement du positionnement du for pour l'id de la section
            if (getForIdSection().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "IDSECTION=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSection());
            }
        }
        // traitement du positionnement du for pour l'id motif du blocage
        if (getForIdMotifBlocage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMOTIFBLOCAGE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdMotifBlocage());
        }

        if (getForIdMotifBlocageIn() != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMOTIFBLOCAGE IN(";
            for (Iterator<String> iterator = getForIdMotifBlocageIn().iterator(); iterator.hasNext();) {
                sqlWhere += iterator.next();
                if (iterator.hasNext()) {
                    sqlWhere += ",";
                }
            }
            sqlWhere += ")";
        }

        // traitement du positionnement de la date entre début et fin
        if (getFromDateBetweenDebutFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += this._dbWriteDateAMJ(statement.getTransaction(), getFromDateBetweenDebutFin());
            sqlWhere += " BETWEEN DATEDEBUT AND DATEFIN";
        }
        // traitement du positionnement de la date de début
        if (getForDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEDEBUT=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }
        //Depuis date de début
        if (getFromDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEDEBUT>=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }
        // traitement du positionnement de la date de fin
        if (getForDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEFIN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFin());
        }

        if (!JadeStringUtil.isBlank(getForDateInYear())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String firstDate = this._dbWriteDateAMJ(statement.getTransaction(), "01.01." + getForDateInYear());
            String lastDate = this._dbWriteDateAMJ(statement.getTransaction(), "31.12." + getForDateInYear());

            sqlWhere += "((DATEDEBUT >= " + firstDate + " and DATEDEBUT <= " + lastDate + ") or ";
            sqlWhere += "(DATEFIN >= " + firstDate + " and DATEFIN <= " + lastDate + ")) ";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAMotifContentieux();
    }

    /**
     * @return
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return
     */
    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateInYear() {
        return forDateInYear;
    }

    /**
     * @return
     */
    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    /**
     * @return idMotifBlocage du contentieux
     */
    public String getForIdMotifBlocage() {
        return forIdMotifBlocage;
    }

    /**
     * @return
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return
     */
    public String getFromDateBetweenDebutFin() {
        return fromDateBetweenDebutFin;
    }

    /**
     * @return
     */
    public java.lang.String getFromIdMotifContentieux() {
        return fromIdMotifContentieux;
    }

    /**
     * @param string
     */
    public void setForDateDebut(String string) {
        forDateDebut = string;
    }

    /**
     * @param string
     */
    public void setForDateFin(String string) {
        forDateFin = string;
    }

    public void setForDateInYear(String forYear) {
        forDateInYear = forYear;
    }

    /**
     * Set le for de l'id du compte annexe
     * 
     * @param String
     *            forIdCompteAnnexe
     */
    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    /**
     * @param string
     *            forIdMotifBlocage du contentieux
     */
    public void setForIdMotifBlocage(String forIdMotifBlocage) {
        this.forIdMotifBlocage = forIdMotifBlocage;
    }

    public void setForIdMotifBlocageIn(Set<String> forIdMotifBlocageIn) {
        this.forIdMotifBlocageIn = forIdMotifBlocageIn;
    }

    /**
     * Set le for de l'id de la section
     * 
     * @param String
     *            forIdSection
     */
    public void setForIdSection(String forIdSection) {
        this.forIdSection = forIdSection;
    }

    /**
     * @param string
     */
    public void setFromDateBetweenDebutFin(String string) {
        fromDateBetweenDebutFin = string;
    }

    /**
     * @param string
     */
    public void setFromIdMotifContentieux(java.lang.String string) {
        fromIdMotifContentieux = string;
    }
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }
}

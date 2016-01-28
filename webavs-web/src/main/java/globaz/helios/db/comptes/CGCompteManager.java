package globaz.helios.db.comptes;

import globaz.globall.db.BManager;
import java.io.Serializable;
import java.util.ArrayList;

public class CGCompteManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompteTVA = new String();
    private String forIdDomaine = "";
    private String forIdGenre = "";
    private ArrayList forIdGenreCompteIn;
    private String forIdMandat = new String();
    private String forIdParametreBouclement = new String();
    private String forIdRemarque = new String();
    private String forIdSecteurAVS = new String();

    private String forNumeroCompteAVS = new String();

    /**
     * Commentaire relatif au constructeur CGCompteManager.
     */
    public CGCompteManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CGCOMTP";
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
        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDREMARQUE=" + _dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        // traitement du positionnement
        if (getForIdCompteTVA().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCOMPTETVA=" + _dbWriteNumeric(statement.getTransaction(), getForIdCompteTVA());
        }

        // traitement du positionnement
        if (getForIdSecteurAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEURAVS=" + _dbWriteNumeric(statement.getTransaction(), getForIdSecteurAVS());
        }

        // traitement du positionnement
        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDMANDAT=" + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        // traitement du positionnement
        if (getForNumeroCompteAVS().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NUMEROCOMPTEAVS=" + _dbWriteNumeric(statement.getTransaction(), getForNumeroCompteAVS());
        }

        // traitement du positionnement
        if (getForIdParametreBouclement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPARAMETREBOUCL="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdParametreBouclement());
        }

        // traitement du positionnement pour forIdGenre
        if (getForIdGenre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDGENRE=" + _dbWriteNumeric(statement.getTransaction(), getForIdGenre());
        }

        // traitement du positionnement pour forIdDomaine
        if (getForIdDomaine().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDDOMAINE=" + _dbWriteNumeric(statement.getTransaction(), getForIdDomaine());
        }

        if (getForIdGenreIn() != null && getForIdGenreIn().size() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "IDGENRE in (";

            for (int i = 0; i < getForIdGenreIn().size(); i++) {
                sqlWhere += _dbWriteNumeric(statement.getTransaction(), "" + getForIdGenreIn().get(i));

                if (i < getForIdGenreIn().size() - 1) {
                    sqlWhere += ", ";
                }
            }

            sqlWhere += ") ";
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGCompte();
    }

    public String getForIdCompteTVA() {
        return forIdCompteTVA;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2003 11:16:21)
     * 
     * @return String
     */
    public String getForIdDomaine() {
        return forIdDomaine;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2003 11:15:41)
     * 
     * @return double
     */
    public String getForIdGenre() {
        return forIdGenre;
    }

    /**
     * @return
     */
    public ArrayList getForIdGenreIn() {
        return forIdGenreCompteIn;
    }

    public String getForIdMandat() {
        return forIdMandat;
    }

    public String getForIdParametreBouclement() {
        return forIdParametreBouclement;
    }

    /**
     * Getter
     */
    public String getForIdRemarque() {
        return forIdRemarque;
    }

    public String getForIdSecteurAVS() {
        return forIdSecteurAVS;
    }

    public String getForNumeroCompteAVS() {
        return forNumeroCompteAVS;
    }

    public void setForIdCompteTVA(String newForIdCompteTVA) {
        forIdCompteTVA = newForIdCompteTVA;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2003 11:16:21)
     * 
     * @param newForIdDomaine
     *            String
     */
    public void setForIdDomaine(String newForIdDomaine) {
        forIdDomaine = newForIdDomaine;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2003 11:15:41)
     * 
     * @param newForIdGenre
     *            double
     */
    public void setForIdGenre(String newForIdGenre) {
        forIdGenre = newForIdGenre;
    }

    /**
     * @param list
     */
    public void setForIdGenreIn(ArrayList list) {
        forIdGenreCompteIn = list;
    }

    public void setForIdMandat(String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    public void setForIdParametreBouclement(String newForIdParametreBouclement) {
        forIdParametreBouclement = newForIdParametreBouclement;
    }

    /**
     * Setter
     */
    public void setForIdRemarque(String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    public void setForIdSecteurAVS(String newForIdSecteurAVS) {
        forIdSecteurAVS = newForIdSecteurAVS;
    }

    public void setForNumeroCompteAVS(String newForNumeroCompteAVS) {
        forNumeroCompteAVS = newForNumeroCompteAVS;
    }
}

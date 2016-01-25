package globaz.osiris.db.comptes;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;

public class CASecteurListViewBean extends CASecteurManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDescription = new String();
    private String forIdSecteur = new String();

    /**
     * Cette méthode est surchargée afin de pouvoir faire une jointure sur la table PMTRADP
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CASECOP A INNER JOIN " + _getCollection() + "PMTRADP B "
                + "ON A.IDTRADUCTION = B.IDTRADUCTION";
    }

    /**
     * Surcharge pour effectuer la recherche sur les champs id secteur et libelle
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement depuis un numéro
        if (getForIdSecteur().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDSECTEUR LIKE " + this._dbWriteString(statement.getTransaction(), getForIdSecteur() + "%");
        }

        if (getForDescription().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "LOWER(LIBELLE) LIKE "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForDescription().toLowerCase() + "%");
        }
        // affichage des libelle pour la langue de la session
        if (!JadeStringUtil.isBlankOrZero(statement.getTransaction().getSession().getIdLangueISO())) {
            String langue = statement.getTransaction().getSession().getIdLangueISO().toUpperCase();
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CODEISOLANGUE = " + this._dbWriteString(statement.getTransaction(), langue);
        }

        return sqlWhere;
    }

    public String getForDescription() {
        return forDescription;
    }

    public String getForIdSecteur() {
        return forIdSecteur;
    }

    public void setForDescription(String forDescription) {
        this.forDescription = forDescription;
    }

    public void setForIdSecteur(String forIdSecteur) {
        this.forIdSecteur = forIdSecteur;
    }
}

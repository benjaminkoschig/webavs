package globaz.lynx.db.fournisseur;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class LXFournisseurManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_BY_NOM = "order_by_nom";
    public static final String ORDER_BY_NOTVA = "order_by_notva";
    public static final String ORDER_BY_NUMERO = "order_by_numero";

    private Boolean estBloque = new Boolean(false);
    private String forCsCategorie;
    private String forCsMotifBlocage;
    private String forIdExterne;
    private String forIdFournisseur;
    private String forNoTva;
    private String forTri;
    private String likeComplement;
    private String likeIdExterne;
    private String likeNomFournisseur;

    private String likeNoTva;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + LXFournisseur.TABLE_LXFOURP + " INNER JOIN " + _getCollection() + "TITIERP ON "
                + _getCollection() + LXFournisseur.TABLE_LXFOURP + "." + LXFournisseur.FIELD_IDTIERS + "="
                + _getCollection() + "TITIERP.HTITIE";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer ordreBy = new StringBuffer();

        if (ORDER_BY_NOM.equals(getForTri())) {
            ordreBy.append(_getCollection()).append("TITIERP.HTLDU1, ").append(_getCollection())
                    .append("TITIERP.HTLDU2");
        } else if (ORDER_BY_NUMERO.equals(getForTri())) {
            ordreBy.append("INTEGER(").append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                    .append(LXFournisseur.FIELD_IDEXTERNE).append(") DESC");
        } else if (ORDER_BY_NOTVA.equals(getForTri())) {
            ordreBy.append("INTEGER(").append(_getCollection()).append(LXFournisseur.TABLE_LXFOURP).append(".")
                    .append(LXFournisseur.FIELD_NOTVA).append(") DESC");
        }

        return ordreBy.toString();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(getForIdFournisseur()) && JadeStringUtil.isDigit(getForIdFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_IDFOURNISSEUR + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdFournisseur());
        }

        if (!JadeStringUtil.isBlank(getForNoTva())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_NOTVA + " = " + _dbWriteString(statement.getTransaction(), getForNoTva());
        }

        if (!JadeStringUtil.isBlank(getLikeNoTva())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_NOTVA + " like "
                    + _dbWriteString(statement.getTransaction(), "%" + getLikeNoTva() + "%");
        }

        if (!JadeStringUtil.isBlank(getLikeNomFournisseur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String nomFormatte = JadeStringUtil.convertSpecialChars(getLikeNomFournisseur());
            nomFormatte = JadeStringUtil.toUpperCase(nomFormatte);

            sqlWhere += _getCollection() + "TITIERP.HTLDU1 like "
                    + _dbWriteString(statement.getTransaction(), "%" + nomFormatte + "%");
        }

        if (!JadeStringUtil.isBlank(getLikeComplement())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String complementFormatte = JadeStringUtil.convertSpecialChars(getLikeComplement());
            complementFormatte = JadeStringUtil.toUpperCase(complementFormatte);

            sqlWhere += _getCollection() + "TITIERP.HTLDU2 like "
                    + _dbWriteString(statement.getTransaction(), "%" + complementFormatte + "%");
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsCategorie()) && JadeStringUtil.isDigit(getForCsCategorie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_CSCATEGORIE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForCsCategorie());
        }

        if (!JadeStringUtil.isBlank(getLikeIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_IDEXTERNE + " like "
                    + _dbWriteString(statement.getTransaction(), "%" + getLikeIdExterne() + "%");
        }

        if (!JadeStringUtil.isBlank(getForIdExterne())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_IDEXTERNE + " = "
                    + _dbWriteString(statement.getTransaction(), getForIdExterne());
        }

        if (getEstBloque().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += LXFournisseur.FIELD_ESTBLOQUE + " = "
                    + _dbWriteBoolean(statement.getTransaction(), getEstBloque(), BConstants.DB_TYPE_BOOLEAN_CHAR);

            if (!JadeStringUtil.isBlank(getForCsMotifBlocage())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += LXFournisseur.FIELD_CSMOTIFBLOCAGE + " = "
                        + _dbWriteString(statement.getTransaction(), getForCsMotifBlocage());
            }
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXFournisseur();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public Boolean getEstBloque() {
        return estBloque;
    }

    public String getForCsCategorie() {
        return forCsCategorie;
    }

    public String getForCsMotifBlocage() {
        return forCsMotifBlocage;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdFournisseur() {
        return forIdFournisseur;
    }

    public String getForNoTva() {
        return forNoTva;
    }

    public String getForTri() {
        return forTri;
    }

    public String getLikeComplement() {
        return likeComplement;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getLikeNomFournisseur() {
        return likeNomFournisseur;
    }

    public String getLikeNoTva() {
        return likeNoTva;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }

    public void setForCsCategorie(String forCsCategorie) {
        this.forCsCategorie = forCsCategorie;
    }

    public void setForCsMotifBlocage(String forCsMotifBlocage) {
        this.forCsMotifBlocage = forCsMotifBlocage;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setForIdFournisseur(String forIdFournisseur) {
        this.forIdFournisseur = forIdFournisseur;
    }

    public void setForNoTva(String forNoTva) {
        this.forNoTva = forNoTva;
    }

    public void setForTri(String forTri) {
        this.forTri = forTri;
    }

    public void setLikeComplement(String likeComplement) {
        this.likeComplement = likeComplement;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public void setLikeNomFournisseur(String likeNomFournisseur) {
        this.likeNomFournisseur = likeNomFournisseur;
    }

    public void setLikeNoTva(String likeNoTva) {
        this.likeNoTva = likeNoTva;
    }
}

/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.lienAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Manager pour l'entité LienAffiliation. avec jointure sur les 2 affiliations concernées
 * 
 * @author oca
 */
public class AFLienAffiliationJoinManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forDateEntreDebutEtFin = "";

    private String forIdAffiliationEnfant = "";
    private String forIdAffiliationParent = "";
    private String forNumeroAffilieEnfant = "";
    private String forNumeroAffilieParent = "";
    private String forTypeLien = "";

    private String order = "";

    @Override
    protected String _getFields(BStatement statement) {
        return "l.MWILIE lMWILIE," + // id du lien
                "p.MAIAFF pMAIAFF," + // id aff du parent
                "p.MALNAF pMALNAF," + // num aff du parent
                "e.MAIAFF eMAIAFF," + // id aff de l'enfant
                "e.MALNAF eMALNAF," + // num aff de l'enfant
                "l.MWTLIE lMWTLIE," + // type de lien (code system)
                "l.MWDDEB lMWDDEB," + // date de début du lien
                "l.MWDFIN lMWDFIN" // date de fin du lien
        ;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        String fromClause = _getCollection() + "AFLIENP l";
        fromClause += " INNER JOIN " + _getCollection() + "AFAFFIP p ON (" + "p.MAIAFF = " + "l.MAIAFF)";
        fromClause += " INNER JOIN " + _getCollection() + "AFAFFIP e ON (" + "e.MAIAFF = " + "l.AFA_MAIAFF)";
        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        /*
         * Numéro affilie du parent de la relation
         */
        if (!JadeStringUtil.isEmpty((getForNumeroAffilieParent()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "p.MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumeroAffilieParent());
        }

        /*
         * Numéro affilie de l'enfant de la relation
         */
        if (!JadeStringUtil.isEmpty((getForNumeroAffilieEnfant()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "e.MALNAF = " + this._dbWriteString(statement.getTransaction(), getForNumeroAffilieEnfant());
        }

        /*
         * Id Affiliation parent de la relation
         */
        if (!JadeStringUtil.isEmpty((getForIdAffiliationParent()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "l.MAIAFF = " + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliationParent());
        }
        /*
         * Id affiliation enfant de la relation
         */
        if (!JadeStringUtil.isEmpty((getForIdAffiliationEnfant()))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "l.AFA_MAIAFF = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAffiliationEnfant());
        }
        /*
         * Type de lien
         */
        if (!JadeStringUtil.isEmpty(getForTypeLien())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MWTLIE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeLien());
        }

        /*
         * Date (liens actifs pour une date donnée)
         */
        if (!JadeStringUtil.isEmpty(forDateEntreDebutEtFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MWDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), forDateEntreDebutEtFin)
                    + " AND (MWDFIN=0 OR MWDFIN>="
                    + this._dbWriteDateAMJ(statement.getTransaction(), forDateEntreDebutEtFin) + ")";
        }

        /*
         * 
         */
        if (JadeStringUtil.isEmpty(_getOrder(statement))) {
            if ((!JadeStringUtil.isEmpty(forIdAffiliationEnfant)) || (!JadeStringUtil.isEmpty(forNumeroAffilieEnfant))) {
                /*
                 * Si on fait une recherche par rapport a un enefat, on trie par le parent (pour autant qu'il n'y ait
                 * pas de tri déjà spécifié)
                 */
                setOrder("p.MALNAF");
            } else {
                // Sinon , on trie par enfant, puis par parent
                setOrder("e.MALNAF, p.MALNAF");
            }
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {

        return new AFLienAffiliationJoin();
    }

    public String getForDateEntreDebutEtFin() {
        return forDateEntreDebutEtFin;
    }

    public String getForIdAffiliationEnfant() {
        return forIdAffiliationEnfant;
    }

    /*
     * Getter et Setter
     */
    public String getForIdAffiliationParent() {
        return forIdAffiliationParent;
    }

    public String getForNumeroAffilieEnfant() {
        return forNumeroAffilieEnfant;
    }

    public String getForNumeroAffilieParent() {
        return forNumeroAffilieParent;
    }

    public String getForTypeLien() {
        return forTypeLien;
    }

    public void setForDateEntreDebutEtFin(String forDateEntreDebutEtFin) {
        this.forDateEntreDebutEtFin = forDateEntreDebutEtFin;
    }

    public void setForIdAffiliationEnfant(String forIdAffiliationEnfant) {
        this.forIdAffiliationEnfant = forIdAffiliationEnfant;
    }

    public void setForIdAffiliationParent(String forIdAffiliationParent) {
        this.forIdAffiliationParent = forIdAffiliationParent;
    }

    public void setForNumeroAffilieEnfant(String forNumeroAffilieEnfant) {
        this.forNumeroAffilieEnfant = forNumeroAffilieEnfant;
    }

    public void setForNumeroAffilieParent(String forNumeroAffilieParent) {
        this.forNumeroAffilieParent = forNumeroAffilieParent;
    }

    public void setForTypeLien(String forTypeLien) {
        this.forTypeLien = forTypeLien;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}

/*
 * Créé le 18 avr. 05
 */
package globaz.naos.db.releve;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Le Manager pour l'entité Relevé.
 * 
 * @author jts, sau 18 avr. 05 13:43:01
 */
public class AFApercuReleveManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAffilieNumero;
    private String forCollaborateur;
    private String forDateDebut;
    private String forEtat;
    private String forIdEnteteFacture;
    private String forIdExterneFacture;
    private String forIdTiers;
    private String notForIdReleve;
    private String forJob;
    private String forPlanAffiliationId;
    private String forType;
    private String fromDateDebut;
    private String order = "MALNAF, MMDDEB DESC";
    private boolean ordreByDateFin = false;
    private String untilDateFin;
    private List<String> inEtats = new ArrayList<String>();

    public AFApercuReleveManager() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "MMIREL, MMEBID, MALNAF, HTITIE, MMDDEB, MMDFIN, MMTOTA, MMTYRE, MMETAT, MMINTE, MMDREC, "
                + "IDEXTERNEFACTURE, MALNAF, REFCOLLABORATEUR, " + _getCollection() + "AFREVEP.PSPY"; /*
                                                                                                       * +
                                                                                                       * _getCollection
                                                                                                       * ( ) +
                                                                                                       * "AFREVEP.MAIAFF
                                                                                                       */
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFREVEP LEFT OUTER JOIN " + _getCollection() + "FAENTFP ON " + _getCollection()
                + "FAENTFP.IDENTETEFACTURE = " + _getCollection() + "AFREVEP.MMEBID";
        // LEFT OUTER JOIN " +
        // _getCollection() + "AFAFFIP ON " + _getCollection() +
        // "AFAFFIP.MAIAFF = " + _getCollection() + "AFREVEP.MAIAFF";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {

        // Tri par date de fin
        if (isOrdreByDateFin()) {
            return "MMDFIN DESC";
        }

        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // id de relevé différent
        if (!JadeStringUtil.isEmpty(getNotForIdReleve())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMIREL <> " + this._dbWriteNumeric(statement.getTransaction(), getNotForIdReleve());
        }

        // Numéro d'affilié
        if (!JadeStringUtil.isEmpty(getForAffilieNumero())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF LIKE " + this._dbWriteString(statement.getTransaction(), getForAffilieNumero() + "%");
        }

        // Id tiers
        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE = " + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        // Numéro de facture
        if (!JadeStringUtil.isEmpty(getForIdExterneFacture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNEFACTURE LIKE "
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneFacture() + "%");
        }

        // Date de début
        if (!JadeStringUtil.isEmpty(getForDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMDDEB = " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }

        // Entete de facture
        if (!JadeStringUtil.isEmpty(getForIdEnteteFacture())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMEBID = " + this._dbWriteNumeric(statement.getTransaction(), getForIdEnteteFacture());
        }

        // Date de début
        if (!JadeStringUtil.isEmpty(getFromDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMDDEB >= " + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }

        // Date de fin
        if (!JadeStringUtil.isEmpty(getUntilDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMDFIN <= " + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDateFin());
        }

        // Type
        if (!JadeStringUtil.isEmpty(getForType())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMTYRE = " + this._dbWriteNumeric(statement.getTransaction(), getForType());
        }

        // Etat
        if (!JadeStringUtil.isEmpty(getForEtat())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMETAT = " + this._dbWriteNumeric(statement.getTransaction(), getForEtat());
        }

        if (inEtats != null && inEtats.size() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MMETAT IN (";

            for (String etat : inEtats) {
                sqlWhere += etat + ", ";
            }

            sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 2);
            sqlWhere += ") ";
        }

        // Collaborateur
        if (!JadeStringUtil.isEmpty(getForCollaborateur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "AFREVEP.PSPY like "
                    + this._dbWriteString(statement.getTransaction(), "%" + getForCollaborateur() + "%");
        }

        // Job
        if (!JadeStringUtil.isEmpty(getForJob())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "FAENTFP.IDPASSAGE = "
                    + this._dbWriteNumeric(statement.getTransaction(), getForJob());
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFApercuReleve();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForAffilieNumero() {
        return forAffilieNumero;
    }

    public String getForCollaborateur() {
        return forCollaborateur;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForEtat() {
        return forEtat;
    }

    public String getForIdEnteteFacture() {
        return forIdEnteteFacture;
    }

    public String getForIdExterneFacture() {
        return forIdExterneFacture;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForJob() {
        return forJob;
    }

    /**
     * @return
     */
    public String getForPlanAffiliationId() {
        return forPlanAffiliationId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getForType() {
        return forType;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getUntilDateFin() {
        return untilDateFin;
    }

    public boolean isOrdreByDateFin() {
        return ordreByDateFin;
    }

    public void setForAffilieNumero(String string) {
        forAffilieNumero = string;
    }

    public void setForCollaborateur(String string) {
        forCollaborateur = string;
    }

    public void setForDateDebut(String string) {
        forDateDebut = string;
    }

    public void setForEtat(String string) {
        forEtat = string;
    }

    public void setForIdEnteteFacture(String string) {
        forIdEnteteFacture = string;
    }

    public void setForIdExterneFacture(String string) {
        forIdExterneFacture = string;
    }

    public void setForIdTiers(String string) {
        forIdTiers = string;
    }

    public void setForJob(String string) {
        forJob = string;
    }

    /**
     * @param string
     */
    public void setForPlanAffiliationId(String string) {
        forPlanAffiliationId = string;
    }

    public void setForType(String string) {
        forType = string;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setOrdreByDateFin(boolean ordreByDateFin) {
        this.ordreByDateFin = ordreByDateFin;
    }

    public void setUntilDateFin(String untilDateFin) {
        this.untilDateFin = untilDateFin;
    }

    public String getNotForIdReleve() {
        return notForIdReleve;
    }

    public void setNotForIdReleve(String notForIdReleve) {
        this.notForIdReleve = notForIdReleve;
    }

    public void setInEtats(List<String> etats) {
        inEtats = etats;
    }
}

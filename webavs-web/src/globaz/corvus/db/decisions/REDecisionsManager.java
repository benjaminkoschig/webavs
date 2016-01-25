package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author BSC
 */
public class REDecisionsManager extends PRAbstractManager implements BIGenericManager<REDecisionEntity> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = "";
    private Set<String> forCsEtatIn = new HashSet<String>();
    private String forCsTypeDecision = "";
    private String forIdDemandeRente = "";
    private String forIdTiersBeneficaire = "";
    private Set<String> forIdTiersBeneficaireIn = new HashSet<String>();
    private String forValideDes = "";
    private String forValideJusqua = "";

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isIntegerEmpty(getForCsEtat())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_ETAT).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsEtat()));
        }

        if ((getForCsEtatIn() != null) && !getForCsEtatIn().isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_ETAT).append(" IN ( ");

            for (Iterator<String> iterator = getForCsEtatIn().iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }

            sql.append(" ) ");
        }

        if (!JadeStringUtil.isIntegerEmpty(forValideJusqua)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_DATE_VALIDATION).append("<=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forValideJusqua));
        }

        if (!JadeStringUtil.isIntegerEmpty(forValideDes)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_DATE_VALIDATION).append(">=")
                    .append(this._dbWriteDateAMJ(statement.getTransaction(), forValideDes));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForCsTypeDecision())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_TYPE_DECISION).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForCsTypeDecision()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdDemandeRente())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdDemandeRente()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdTiersBeneficaire())) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiersBeneficaire()));
        }

        if ((getForIdTiersBeneficaireIn() != null) && !getForIdTiersBeneficaireIn().isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL).append(" IN (");

            for (Iterator<String> iterator = getForIdTiersBeneficaireIn().iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }

            sql.append(" ) ");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDecisionEntity();
    }

    @Override
    public List<REDecisionEntity> getContainerAsList() {
        List<REDecisionEntity> list = new ArrayList<REDecisionEntity>();
        for (int i = 0; i < size(); i++) {
            list.add((REDecisionEntity) get(i));
        }
        return list;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public Set<String> getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public String getForIdTiersBeneficaire() {
        return forIdTiersBeneficaire;
    }

    public Set<String> getForIdTiersBeneficaireIn() {
        return forIdTiersBeneficaireIn;
    }

    public String getForValideDes() {
        return forValideDes;
    }

    public String getForValideJusqua() {
        return forValideJusqua;
    }

    @Override
    public String getOrderByDefaut() {
        return REDecisionEntity.FIELDNAME_ID_DECISION;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtatIn(Set<String> forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    public void setForIdDemandeRente(String forIdDemande) {
        forIdDemandeRente = forIdDemande;
    }

    public void setForIdTiersBeneficaire(String forIdTiersBeneficaire) {
        this.forIdTiersBeneficaire = forIdTiersBeneficaire;
    }

    public void setForIdTiersBeneficaireIn(Set<String> forIdTiersBeneficaireIn) {
        this.forIdTiersBeneficaireIn = forIdTiersBeneficaireIn;
    }

    public void setForValideDes(String forValideDes) {
        this.forValideDes = forValideDes;
    }

    public void setForValideJusqua(String forValideJusqua) {
        this.forValideJusqua = forValideJusqua;
    }
}

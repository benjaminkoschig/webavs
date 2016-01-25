package globaz.corvus.db.decisions;

import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class REDecisionJointOrdresVersementsManager extends BManager implements
        BIGenericManager<REDecisionJointOrdresVersements> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Set<String> forCsEtatDecisionIn;
    private Long forIdDecision;
    private Set<Long> forIdDecisionIn;
    private Set<Long> forIdDecisionNotIn;
    private Long forIdOrdreVersement;
    private Long forIdRenteVerseeATort;
    private Set<Long> forIdTiersBeneficiairePrincipalIn;

    public REDecisionJointOrdresVersementsManager() {
        super();

        forCsEtatDecisionIn = null;
        forIdDecision = null;
        forIdDecisionIn = null;
        forIdDecisionNotIn = null;
        forIdOrdreVersement = null;
        forIdRenteVerseeATort = null;
        forIdTiersBeneficiairePrincipalIn = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;

        if ((forCsEtatDecisionIn != null) && !forCsEtatDecisionIn.isEmpty()) {
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ETAT).append(" IN(");
            for (Iterator<String> iterator = forCsEtatDecisionIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if (forIdDecision != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DECISION).append("=")
                    .append(forIdDecision);
        }

        if ((forIdDecisionNotIn != null) && !forIdDecisionNotIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DECISION).append(" NOT IN(");
            for (Iterator<Long> iterator = forIdDecisionNotIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if ((forIdDecisionIn != null) && !forIdDecisionIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DECISION).append(" IN(");
            for (Iterator<Long> iterator = forIdDecisionIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if (forIdOrdreVersement != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT)
                    .append("=").append(forIdOrdreVersement);
        }

        if (forIdRenteVerseeATort != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableOrdreVersement).append(".").append(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT)
                    .append("=").append(forIdRenteVerseeATort);
        }

        if ((forIdTiersBeneficiairePrincipalIn != null) && !forIdTiersBeneficiairePrincipalIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL)
                    .append(" IN(");
            for (Iterator<Long> iterator = forIdTiersBeneficiairePrincipalIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        return sql.toString();
    }

    @Override
    protected REDecisionJointOrdresVersements _newEntity() throws Exception {
        return new REDecisionJointOrdresVersements();
    }

    @Override
    public List<REDecisionJointOrdresVersements> getContainerAsList() {
        List<REDecisionJointOrdresVersements> containerAsList = new ArrayList<REDecisionJointOrdresVersements>();

        for (int i = 0; i < size(); i++) {
            containerAsList.add((REDecisionJointOrdresVersements) get(i));
        }

        return containerAsList;
    }

    public Set<String> getForCsEtatDecisionIn() {
        return forCsEtatDecisionIn;
    }

    public Long getForIdDecision() {
        return forIdDecision;
    }

    public Set<Long> getForIdDecisionIn() {
        return forIdDecisionIn;
    }

    public Set<Long> getForIdDecisionNotIn() {
        return forIdDecisionNotIn;
    }

    public Long getForIdOrdreVersement() {
        return forIdOrdreVersement;
    }

    public Long getForIdRenteVerseeATort() {
        return forIdRenteVerseeATort;
    }

    public Set<Long> getForIdTiersBeneficiairePrincipalIn() {
        return forIdTiersBeneficiairePrincipalIn;
    }

    public void setForCsEtatDecisionIn(Set<String> forCsEtatDecisionIn) {
        this.forCsEtatDecisionIn = forCsEtatDecisionIn;
    }

    public void setForIdDecision(Long forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdDecisionIn(Set<Long> idsDecisions) {
        forIdDecisionIn = idsDecisions;
    }

    public void setForIdDecisionNotIn(Set<Long> forIdDecisionNotIn) {
        this.forIdDecisionNotIn = forIdDecisionNotIn;
    }

    public void setForIdOrdreVersement(Long idOrdreVersement) {
        forIdOrdreVersement = idOrdreVersement;
    }

    public void setForIdRenteVerseeATort(Long idRenteVerseeATort) {
        forIdRenteVerseeATort = idRenteVerseeATort;
    }

    public void setForIdTiersBeneficiairePrincipalIn(Set<Long> forIdTiersBeneficiairePrincipalIn) {
        this.forIdTiersBeneficiairePrincipalIn = forIdTiersBeneficiairePrincipalIn;
    }
}

package globaz.corvus.db.rentesverseesatort;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RERenteVerseeATortJointRenteAccordeeManager extends BManager implements
        BIGenericManager<RERenteVerseeATortJointRenteAccordee> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdDemandeRente;
    private Long forIdRenteVerseeATort;
    private Collection<Long> forIdsRentesNouveauDroitIn;
    private Long forIdTiers;
    private boolean sansLesSaisiesManuelles;

    public RERenteVerseeATortJointRenteAccordeeManager() {
        super();

        forIdDemandeRente = null;
        forIdRenteVerseeATort = null;
        forIdsRentesNouveauDroitIn = null;
        forIdTiers = null;
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;
        String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;

        if (forIdDemandeRente != null) {
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_DEMANDE_RENTE).append("=")
                    .append(forIdDemandeRente);
        }

        if (forIdRenteVerseeATort != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT).append("=")
                    .append(forIdRenteVerseeATort);
        }

        if ((forIdsRentesNouveauDroitIn != null) && !forIdsRentesNouveauDroitIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT)
                    .append(" IN(");

            for (Iterator<Long> iterator = forIdsRentesNouveauDroitIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }

            sql.append(")");
        }

        if (forIdTiers != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_TIERS).append("=")
                    .append(forIdTiers);
        }

        if (isSansLesSaisiesManuelles()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append("(");
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.IS_SAISIE_MANUELLE).append("=")
                    .append(this._dbWriteBoolean(statement.getTransaction(), false, BConstants.DB_TYPE_BOOLEAN_CHAR));
            sql.append(" OR ");
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.IS_SAISIE_MANUELLE)
                    .append(" IS NULL");
            sql.append(")");
        }

        if (sql.length() > 0) {
            sql.append(" AND ");
        }
        sql.append("(");
        sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_CS_TYPE).append("=")
                .append(IREPrestationDue.CS_TYPE_PMT_MENS);
        sql.append(" OR ");
        sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_CS_TYPE).append(" IS NULL");
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteVerseeATortJointRenteAccordee();
    }

    @Override
    public List<RERenteVerseeATortJointRenteAccordee> getContainerAsList() {

        List<RERenteVerseeATortJointRenteAccordee> list = new ArrayList<RERenteVerseeATortJointRenteAccordee>();

        if (container != null) {
            for (int i = 0; i < container.size(); i++) {
                list.add((RERenteVerseeATortJointRenteAccordee) container.get(i));
            }
        }

        return list;
    }

    public Long getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public Long getForIdRenteVerseeATort() {
        return forIdRenteVerseeATort;
    }

    public Collection<Long> getForIdsRentesNouveauDroitIn() {
        return forIdsRentesNouveauDroitIn;
    }

    public Long getForIdTiers() {
        return forIdTiers;
    }

    public boolean isSansLesSaisiesManuelles() {
        return sansLesSaisiesManuelles;
    }

    public void setForIdDemandeRente(final Long idDemande) {
        forIdDemandeRente = idDemande;
    }

    public void setForIdRenteVerseeATort(final Long forIdRenteVerseeATort) {
        this.forIdRenteVerseeATort = forIdRenteVerseeATort;
    }

    public void setForIdsRentesNouveauDroitIn(final Collection<Long> idsRA) {
        forIdsRentesNouveauDroitIn = idsRA;
    }

    public void setForIdTiers(final Long forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setSansLesSaisiesManuelles(final boolean sansLesSaisiesManuelles) {
        this.sansLesSaisiesManuelles = sansLesSaisiesManuelles;
    }
}

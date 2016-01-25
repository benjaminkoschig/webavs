package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author PBA
 */
public class RERenteVerseeATortManager extends PRAbstractManager implements BIGenericManager<RERenteVerseeATort> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdDemandeRente;
    private Long forIdRenteAncienDroit;
    private Long forIdRenteNouveauDroit;
    private Collection<Long> forIdsRentesNouveauDroitIn;
    private Long forIdTiers;
    private boolean seulementLesSaisiesManuelles;
    private boolean sansRenteDuNouveauDroit;

    public RERenteVerseeATortManager() {
        super();

        forIdDemandeRente = null;
        forIdRenteAncienDroit = null;
        forIdRenteNouveauDroit = null;
        seulementLesSaisiesManuelles = false;
    }

    @Override
    protected String _getWhere(final BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if ((forIdDemandeRente != null) && (forIdDemandeRente.intValue() > 0)) {
            sql.append(RERenteVerseeATort.ID_DEMANDE_RENTE).append("=").append(getForIdDemandeRente());
        }

        if ((forIdRenteAncienDroit != null) && (forIdRenteAncienDroit.intValue() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT).append("=").append(getForIdRenteAncienDroit());
        }

        if ((forIdRenteNouveauDroit != null) && (forIdRenteNouveauDroit.intValue() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT).append("=").append(getForIdRenteNouveauDroit());
        }

        if ((forIdTiers != null) && (forIdTiers.intValue() > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RERenteVerseeATort.ID_TIERS).append("=").append(getForIdTiers());
        }

        if (isSeulementLesSaisiesManuelles()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RERenteVerseeATort.IS_SAISIE_MANUELLE)
                    .append("=")
                    .append(this._dbWriteBoolean(statement.getTransaction(), isSeulementLesSaisiesManuelles(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if ((forIdsRentesNouveauDroitIn != null) && !forIdsRentesNouveauDroitIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT).append(" IN(");

            for (Iterator<Long> iterator = forIdsRentesNouveauDroitIn.iterator(); iterator.hasNext();) {
                sql.append(iterator.next());
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }

            sql.append(")");
        }

        if (isSansRenteDuNouveauDroit()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT).append(" IS NULL");
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERenteVerseeATort();
    }

    @Override
    public List<RERenteVerseeATort> getContainerAsList() {
        List<RERenteVerseeATort> list = new ArrayList<RERenteVerseeATort>();

        for (int i = 0; i < container.size(); i++) {
            list.add((RERenteVerseeATort) container.get(i));
        }

        return list;
    }

    public Long getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public Long getForIdRenteAncienDroit() {
        return forIdRenteAncienDroit;
    }

    public Long getForIdRenteNouveauDroit() {
        return forIdRenteNouveauDroit;
    }

    public Collection<Long> getForIdsRentesNouveauDroitIn() {
        return forIdsRentesNouveauDroitIn;
    }

    public Long getForIdTiers() {
        return forIdTiers;
    }

    @Override
    public String getOrderByDefaut() {
        return RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT;
    }

    public boolean isSeulementLesSaisiesManuelles() {
        return seulementLesSaisiesManuelles;
    }

    public void setForIdDemandeRente(final Long forIdDemandeRente) {
        this.forIdDemandeRente = forIdDemandeRente;
    }

    public void setForIdRenteAncienDroit(final Long forIdRenteAncienDroit) {
        this.forIdRenteAncienDroit = forIdRenteAncienDroit;
    }

    public void setForIdRenteNouveauDroit(final Long forIdRenteNouveauDroit) {
        this.forIdRenteNouveauDroit = forIdRenteNouveauDroit;
    }

    public void setForIdsRentesNouveauDroitIn(final Collection<Long> idsRA) {
        forIdsRentesNouveauDroitIn = idsRA;
    }

    public void setForIdTiers(final Long forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setSeulementLesSaisiesManuelles(final boolean seulementLesSaisiesManuelles) {
        this.seulementLesSaisiesManuelles = seulementLesSaisiesManuelles;
    }

    public boolean isSansRenteDuNouveauDroit() {
        return sansRenteDuNouveauDroit;
    }

    public void setSansRenteDuNouveauDroit(boolean sansRenteDuNouveauDroit) {
        this.sansRenteDuNouveauDroit = sansRenteDuNouveauDroit;
    }
}

package globaz.corvus.db.rentesverseesatort;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Permet de rechercher les informations relatives à un calcul de rente versée à tort
 * 
 * @author PBA
 */
public class RECalculRentesVerseesATortManager extends BManager implements
        BIGenericManager<RECalculRentesVerseesATortEntity> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdDemandeRente;
    private Long forIdRenteVerseeATort;
    private Set<Long> idsRenteAccordeeNouveauDroit;

    public RECalculRentesVerseesATortManager() {
        super();

        idsRenteAccordeeNouveauDroit = null;
        forIdDemandeRente = null;
        forIdRenteVerseeATort = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationVerseeDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;

        if ((idsRenteAccordeeNouveauDroit != null) && (idsRenteAccordeeNouveauDroit.size() > 0)) {
            StringBuilder listeIdsFormatInSql = new StringBuilder();

            listeIdsFormatInSql.append("(");
            for (Iterator<Long> iterator = idsRenteAccordeeNouveauDroit.iterator(); iterator.hasNext();) {
                listeIdsFormatInSql.append(iterator.next());
                if (iterator.hasNext()) {
                    listeIdsFormatInSql.append(",");
                }
            }
            listeIdsFormatInSql.append(")");

            sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                    .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" IN ")
                    .append(listeIdsFormatInSql.toString());
            sql.append(" AND ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                    .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" NOT IN ")
                    .append(listeIdsFormatInSql.toString());
        }

        if ((forIdDemandeRente != null) && (forIdDemandeRente > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append("=")
                    .append(forIdDemandeRente);
        }

        if ((forIdRenteVerseeATort != null) && (forIdRenteVerseeATort > 0)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT).append("=")
                    .append(forIdRenteVerseeATort);
        }

        if (sql.length() > 0) {
            sql.append(" AND ");
        }
        sql.append(tablePrestationVerseeDue).append(".").append(REPrestationDue.FIELDNAME_CS_TYPE).append("=")
                .append(IREPrestationDue.CS_TYPE_PMT_MENS);

        sql.append(" AND ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" IN(");
        sql.append(IREPrestationAccordee.CS_ETAT_DIMINUE).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_PARTIEL).append(",");
        sql.append(IREPrestationAccordee.CS_ETAT_VALIDE);
        sql.append(")");

        sql.append(" AND (");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(">=")
                .append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        sql.append(" OR ");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL");
        sql.append(" OR ");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append("=0");
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected RECalculRentesVerseesATortEntity _newEntity() throws Exception {
        return new RECalculRentesVerseesATortEntity();
    }

    @Override
    public List<RECalculRentesVerseesATortEntity> getContainerAsList() {
        List<RECalculRentesVerseesATortEntity> list = new ArrayList<RECalculRentesVerseesATortEntity>();

        for (int i = 0; i < container.size(); i++) {
            RECalculRentesVerseesATortEntity uneEntitee = (RECalculRentesVerseesATortEntity) container.get(i);
            list.add(uneEntitee);
        }

        return list;
    }

    public Long getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public Long getForIdRenteVerseeATort() {
        return forIdRenteVerseeATort;
    }

    public Set<Long> getIdsRenteAccordeeNouveauDroit() {
        return idsRenteAccordeeNouveauDroit;
    }

    public void setForIdRenteVerseeATort(Long forIdRenteVerseeATort) {
        this.forIdRenteVerseeATort = forIdRenteVerseeATort;
    }

    public void setIdsRenteAccordeeNouveauDroit(Set<Long> idsRenteAccordeeNouveauDroit) {
        this.idsRenteAccordeeNouveauDroit = idsRenteAccordeeNouveauDroit;
    }
}

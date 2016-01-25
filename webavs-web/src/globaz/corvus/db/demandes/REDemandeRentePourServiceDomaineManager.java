package globaz.corvus.db.demandes;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.services.models.demande.DemandeDaoService;

/**
 * Manager pour l'implémentation Jade du service de chargement des demandes de rente {@link DemandeDaoService}
 */
public class REDemandeRentePourServiceDomaineManager extends BManager implements
        BIGenericManager<REDemandeRentePourServiceDomaine> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdDemandeRente;

    public REDemandeRentePourServiceDomaineManager() {
        forIdDemandeRente = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;

        if (forIdDemandeRente != null) {
            sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append("=")
                    .append(forIdDemandeRente);
        }

        return sql.toString();
    }

    @Override
    protected REDemandeRentePourServiceDomaine _newEntity() throws Exception {
        return new REDemandeRentePourServiceDomaine();
    }

    @Override
    public List<REDemandeRentePourServiceDomaine> getContainerAsList() {
        List<REDemandeRentePourServiceDomaine> list = new ArrayList<REDemandeRentePourServiceDomaine>();

        for (int i = 0; i < size(); i++) {
            list.add((REDemandeRentePourServiceDomaine) get(i));
        }

        return list;
    }

    public Long getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public void setForIdDemandeRente(Long idDemandeRente) {
        forIdDemandeRente = idDemandeRente;
    }
}

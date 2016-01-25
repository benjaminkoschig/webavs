package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.services.models.rentesaccordees.RenteAccordeeCrudService;

/**
 * Manager dédié à l'implémentation Jade du service de CRUD de la rente accordée {@link RenteAccordeeCrudService}
 */
public class RERenteAccordeePourServiceManager extends BManager implements
        BIGenericManager<RERenteAccordeePourServiceEntity> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long forIdRenteAccordee;

    public RERenteAccordeePourServiceManager() {
        super();

        forIdRenteAccordee = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;

        if (forIdRenteAccordee != null) {
            sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append("=")
                    .append(forIdRenteAccordee);
        }

        return sql.toString();
    }

    @Override
    protected RERenteAccordeePourServiceEntity _newEntity() throws Exception {
        return new RERenteAccordeePourServiceEntity();
    }

    @Override
    public List<RERenteAccordeePourServiceEntity> getContainerAsList() {
        List<RERenteAccordeePourServiceEntity> list = new ArrayList<RERenteAccordeePourServiceEntity>();
        for (int i = 0; i < size(); i++) {
            list.add((RERenteAccordeePourServiceEntity) get(i));
        }
        return list;
    }

    public Long getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public void setForIdRenteAccordee(Long forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }
}

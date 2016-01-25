package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class REAnnonceRenteManager extends PRAbstractManager implements BIGenericManager<REAnnonceRente> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnonceHeader;
    private String forIdAnnonceRente;
    private String forIdDecision;
    private String forIdRenteAccordee;
    private Set<Integer> forIdsRenteAccordeeIn;

    public REAnnonceRenteManager() {
        super();

        forIdAnnonceHeader = null;
        forIdAnnonceRente = null;
        forIdDecision = null;
        forIdRenteAccordee = null;
        forIdsRenteAccordeeIn = null;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isIntegerEmpty(forIdAnnonceRente)) {
            sql.append(REAnnonceRente.FIELDNAME_ID_ANNONCE_RENTE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdAnnonceRente));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdAnnonceHeader)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAnnonceRente.FIELDNAME_ID_ANNONCE_HEADER).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdAnnonceHeader));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRenteAccordee)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAnnonceRente.FIELDNAME_ID_RENTE_ACCORDEE).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdRenteAccordee));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdDecision)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAnnonceRente.FIELDNAME_ID_DECISION).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), forIdDecision));
        }

        if ((forIdsRenteAccordeeIn != null) && !forIdsRenteAccordeeIn.isEmpty()) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(REAnnonceRente.FIELDNAME_ID_RENTE_ACCORDEE).append(" IN(");
            for (Iterator<Integer> iterator = forIdsRenteAccordeeIn.iterator(); iterator.hasNext();) {
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
    protected BEntity _newEntity() throws Exception {
        return new REAnnonceRente();
    }

    @Override
    public List<REAnnonceRente> getContainerAsList() {
        List<REAnnonceRente> list = new ArrayList<REAnnonceRente>();
        for (int i = 0; i < size(); i++) {
            list.add((REAnnonceRente) get(i));
        }
        return list;
    }

    public String getForIdAnnonceHeader() {
        return forIdAnnonceHeader;
    }

    public String getForIdAnnonceRente() {
        return forIdAnnonceRente;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdRenteAccordee() {
        return forIdRenteAccordee;
    }

    public Set<Integer> getForIdsRenteAccordeeIn() {
        return forIdsRenteAccordeeIn;
    }

    @Override
    public String getOrderByDefaut() {
        return REAnnonceRente.FIELDNAME_ID_ANNONCE_RENTE;
    }

    public void setForIdAnnonceHeader(String forIdAnnonceHeader) {
        this.forIdAnnonceHeader = forIdAnnonceHeader;
    }

    public void setForIdAnnonceRente(String forIdAnnonceRente) {
        this.forIdAnnonceRente = forIdAnnonceRente;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdRenteAccordee(String forIdRenteAccordee) {
        this.forIdRenteAccordee = forIdRenteAccordee;
    }

    public void setForIdsRenteAccordeeIn(Set<Integer> forIdsRenteAccordeeIn) {
        this.forIdsRenteAccordeeIn = forIdsRenteAccordeeIn;
    }
}

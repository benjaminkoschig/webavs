package globaz.osiris.db.interet.util.ecriturenonsoumise;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;

public class CAEcritureNonSoumiseParSectionManager extends CAEcritureNonSoumiseManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean groupBySection = false;

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        String fields = super._getFields(statement);

        if (isGroupBySection()) {
            fields += ", a." + CAOperation.FIELD_IDSECTION + ", c." + CASection.FIELD_CATEGORIESECTION;
        }

        return fields;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);

        if (isGroupBySection()) {
            from += ", " + _getCollection() + CASection.TABLE_CASECTP + " c ";
        }

        return from;
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = getWhereSql();
        where.append(" and a." + CAOperation.FIELD_IDSECTION + " = c." + CASection.FIELD_IDSECTION);

        where.append(getGroupBySql());

        if (isGroupBySection()) {
            where.append(", a." + CAOperation.FIELD_IDSECTION + ", c." + CASection.FIELD_CATEGORIESECTION + " ");
        }

        where.append(getOrderSql());

        return where.toString();

    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAEcritureNonSoumiseParSection();
    }

    /**
     * Return la partie order by.
     * 
     * @return
     */
    @Override
    protected String getOrderSql() {
        return "order by a." + CAOperation.FIELD_IDSECTION + ", a." + CAOperation.FIELD_DATE + " asc";
    }

    public boolean isGroupBySection() {
        return groupBySection;
    }

    public void setGroupBySection(boolean groupBySection) {
        this.groupBySection = groupBySection;
    }
}

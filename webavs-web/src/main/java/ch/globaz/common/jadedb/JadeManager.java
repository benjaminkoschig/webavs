/*
 * Globaz SA.
 */
package ch.globaz.common.jadedb;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.util.List;
import ch.globaz.common.jadedb.exception.JadeDataBaseException;
import ch.globaz.common.sql.SQLWriter;
import com.google.common.base.Joiner;

public abstract class JadeManager<T extends JadeEntity> extends BManager {

    private static final long serialVersionUID = 1L;
    private transient BStatement statement;
    private String sqlOrder;

    protected abstract void createWhere(SQLWriter sqlWhere);

    protected void createOrderBy(String... orders) {
        sqlOrder = Joiner.on(",").join(orders);
    }

    protected void createOrderBy(TableDefinition... orders) {

        StringBuilder sqlOrderBuild = new StringBuilder();

        for (TableDefinition tableDefinition : orders) {
            if (sqlOrderBuild.length() != 0) {
                sqlOrderBuild.append(",");
            }

            sqlOrderBuild.append(tableDefinition.getColumnName());
        }
        sqlOrder = sqlOrderBuild.toString();
    }

    @Override
    protected final String _getWhere(BStatement statement) {
        this.statement = statement;
        SQLWriter sqlWhere = SQLWriter.write(_getCollection());
        createWhere(sqlWhere);
        return sqlWhere.toSql();
    }

    @Override
    protected final String _getOrder(BStatement statement) {
        this.statement = statement;
        return sqlOrder;
    }

    protected final String dbWriteDateAMJ(String column) {
        return _dbWriteDateAMJ(this.statement.getTransaction(), column, null);
    }

    public List<T> search() {
        try {
            this.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new JadeDataBaseException(e);
        }
        return toList();
    }

    public List<T> search(BITransaction transaction) {
        try {
            this.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new JadeDataBaseException(e);
        }
        return toList();
    }

    public T searchFirst() {
        try {
            this.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new JadeDataBaseException(e);
        }
        if (size() > 0) {
            return (T) getFirstEntity();
        }
        try {
            return (T) _newEntity();
        } catch (Exception e) {
            throw new JadeDataBaseException(e);
        }
    }
}

package ch.globaz.common.jadedb;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.util.List;
import ch.globaz.common.sql.SQLWriter;

@SuppressWarnings("serial")
public abstract class JadeManager<T extends JadeEntity> extends BManager {

    private transient BStatement statement;

    protected abstract void createWhere(SQLWriter sqlWhere);

    @Override
    protected final String _getWhere(BStatement statement) {
        this.statement = statement;
        SQLWriter sqlWhere = SQLWriter.write(_getCollection());
        createWhere(sqlWhere);
        return sqlWhere.toSql();
    }

    protected final String dbWriteDateAMJ(String column) {
        return _dbWriteDateAMJ(this.statement.getTransaction(), column, null);
    }

    public List<T> search() {
        try {
            this.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toList();
    }

    public List<T> search(BITransaction transaction) {
        try {
            this.find(transaction, BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toList();
    }

    public T searchFirst() {
        try {
            this.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (size() > 0) {
            return (T) getFirstEntity();
        }
        try {
            return (T) _newEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package ch.globaz.common.sql;

import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import ch.globaz.common.exceptions.CommonTechnicalException;

/**
 * Le but de cette class est de pouvoir générer une requête sql et de l'exectuter en utilisant le {@link QueryExecutor}
 * 
 * @author dma
 * 
 */
public class QueryWriterExecutor {

    private SQLWriter writer;
    private BISession session;
    private Set<ConverterDb<?>> converters;
    private Class<?> beanClass;
    private BITransaction transaction;

    private QueryWriterExecutor() {
        writer = SQLWriter.write();
    }

    public QueryWriterExecutor useSession(BISession session) {
        this.session = session;
        return this;
    }

    public QueryWriterExecutor useTransaction(BITransaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public static QueryWriterExecutor select(String sqlFragement) {
        QueryWriterExecutor queryWriterExecutor = new QueryWriterExecutor();
        queryWriterExecutor.writer.select(sqlFragement);
        return queryWriterExecutor;
    }

    public static QueryWriterExecutor query(String sql, String... params) {
        QueryWriterExecutor queryWriterExecutor = new QueryWriterExecutor();
        queryWriterExecutor.writer.append(sql, params);
        return queryWriterExecutor;
    }

    public <T> QueryWriterExecutor forClass(Class<T> beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    public QueryWriterExecutor withConverters(Set<ConverterDb<?>> converters) {
        this.converters = converters;
        return this;
    }

    public QueryWriterExecutor withConverters(ConverterDb<?>... converters) {
        this.converters = QueryExecutor.newSetConverter(converters);
        return this;
    }

    public QueryWriterExecutor from(String sqlFragement) {
        writer.from(sqlFragement);
        return this;
    }

    public QueryWriterExecutor where() {
        writer.where();
        return this;
    }

    public QueryWriterExecutor where(String sqlFragement) {
        writer.where(sqlFragement);
        return this;
    }

    public QueryWriterExecutor where(String sqlFragement, String... params) {
        writer.where(sqlFragement, params);
        return this;
    }

    public QueryWriterExecutor and(String sqlFragement) {
        writer.and(sqlFragement);
        return this;
    }

    public QueryWriterExecutor and(String sqlFragement, Integer... params) {
        writer.and(sqlFragement, params);
        return this;
    }

    public QueryWriterExecutor and(String sql, String... params) {
        writer.and(sql, params);
        return this;
    }

    public QueryWriterExecutor and(String sql, Collection<String> params) {
        writer.and(sql, params);
        return this;
    }

    public QueryWriterExecutor or(String sqlFragement) {
        writer.or(sqlFragement);
        return this;
    }

    public QueryWriterExecutor or(String sql, Integer... params) {
        writer.or(sql, params);
        return this;
    }

    public QueryWriterExecutor or(String sql, String... params) {
        writer.or(sql, params);
        return this;
    }

    public QueryWriterExecutor or(String sql, Collection<String> params) {
        writer.or(sql, params);
        return this;
    }

    public QueryWriterExecutor append(String sqlFragment) {
        writer.append(sqlFragment);
        return this;
    }

    public QueryWriterExecutor append(boolean condition, String sqlFragment) {
        writer.append(condition, sqlFragment);
        return this;
    }

    public QueryWriterExecutor join(String sql) {
        writer.join(sql);
        return this;
    }

    public QueryWriterExecutor join(boolean condition, String sql) {
        writer.join(condition, sql);
        return this;
    }

    public QueryWriterExecutor leftJoin(String sql) {
        writer.leftJoin(sql);
        return this;
    }

    public QueryWriterExecutor leftJoin(boolean condition, String sql) {
        writer.leftJoin(condition, sql);
        return this;
    }

    public QueryWriterExecutor rightJoin(String sql) {
        writer.rightJoin(sql);
        return this;
    }

    public QueryWriterExecutor rightJoin(boolean condition, String sql) {
        writer.rightJoin(condition, sql);
        return this;
    }

    public BigDecimal executeAggregate() {
        checkQuery();
        if (hasSession()) {
            return QueryExecutor.executeAggregate(writer.toSql(), (BSession) session);
        } else if (hasTransaction()) {
            return QueryExecutor.executeAggregate(writer.toSql(), transaction);

        }
        return QueryExecutor.executeAggregate(writer.toSql());
    }

    public int executeAggregateToInt() {
        return executeAggregate().stripTrailingZeros().intValue();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> execute() {
        checkQuery();
        if (beanClass == null) {
            throw new CommonTechnicalException("Unable to execute query (" + writer.toSql()
                    + ") without defined the classBean! Please use the forClassBeau(...) function ");
        }
        if (hasSession()) {
            return (List<T>) QueryExecutor.execute(writer.toSql(), beanClass, (BSession) session, converters);
        }
        return (List<T>) QueryExecutor.execute(writer.toSql(), beanClass, converters);
    }

    private void checkQuery() {
        if (writer.isQueryEmpty()) {
            throw new CommonTechnicalException("Unable to execute a empty query !!");
        }
        if (hasSession() && hasTransaction()) {
            throw new CommonTechnicalException(
                    "Unable to execute the query !, the session is defined to use and the transaction is also defined to use. Please use one !");
        }
    }

    private boolean hasSession() {
        return session != null;
    }

    private boolean hasTransaction() {
        return transaction != null;
    }

}

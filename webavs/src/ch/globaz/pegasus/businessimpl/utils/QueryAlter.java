package ch.globaz.pegasus.businessimpl.utils;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition;

public interface QueryAlter {

    public String alterSql(String sql, JadeAbstractSearchModel search, JadeAbstractSqlModelDefinition modelDefinition)
            throws JadePersistenceException;

}

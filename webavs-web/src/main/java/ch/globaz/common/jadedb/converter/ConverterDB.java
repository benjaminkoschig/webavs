/*
 * Globaz SA.
 */
package ch.globaz.common.jadedb.converter;

public interface ConverterDB<D, B> {

    D fromDB(B valueDB);

    B toDB(D valueObject);
}

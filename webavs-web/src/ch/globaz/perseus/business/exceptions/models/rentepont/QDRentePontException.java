/**
 * 
 */
package ch.globaz.perseus.business.exceptions.models.rentepont;

import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author JSI
 * 
 */
public class QDRentePontException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public QDRentePontException(String string) {
        super(string);
    }

    public QDRentePontException(String string, JadeApplicationServiceNotAvailableException e) {
        super(string, e);
    }

}

package ch.globaz.naos.ree.domain.converter;

/**
 * Exception triggered when business needs not completed
 * 
 * @author cel
 * 
 */
public class REEBusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    public REEBusinessException(String string) {
        super(string);
    }

}

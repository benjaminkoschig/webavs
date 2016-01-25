package globaz.aquila.service.config;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public interface COConfigurationOption {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    boolean booleanValue();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    double doubleValue();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    int intValue();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    COConfigurationKey key();

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    String stringValue();
}

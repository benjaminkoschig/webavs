package globaz.aquila.service.config;

class COConfigurationOptionImpl implements COConfigurationOption {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private COConfigurationKey key;
    private String value;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new COConfigurationOptionImpl object.
     * 
     * @param key
     *            DOCUMENT ME!
     * @param value
     *            DOCUMENT ME!
     */
    public COConfigurationOptionImpl(COConfigurationKey key, String value) {
        this.key = key;
        this.value = value;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public boolean booleanValue() {
        return value.equals("true") ? true : false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public double doubleValue() {
        return Double.parseDouble(value);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public int intValue() {
        return Integer.parseInt(value);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public COConfigurationKey key() {
        return key;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    @Override
    public String stringValue() {
        return value;
    }

}

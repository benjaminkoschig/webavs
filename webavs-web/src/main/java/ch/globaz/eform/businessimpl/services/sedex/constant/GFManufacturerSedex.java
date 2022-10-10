package ch.globaz.eform.businessimpl.services.sedex.constant;

//TODO à retravaillé pour une présentation à l'ensemble de l'application
public enum GFManufacturerSedex {
    WEBAVS("Globaz SA", "WEBAVS", "1.29.0");

    private final String manufacturer;
    private final String product;
    private final String productVersion;

    GFManufacturerSedex(String manufacturer, String product, String productVersion) {
        this.manufacturer = manufacturer;
        this.product = product;
        this.productVersion = productVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getProduct() {
        return product;
    }

    public String getProductVersion() {
        return productVersion;
    }
}

package ch.globaz.eavs.model.eahviv2011000102.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eCH0058.common.AbstractManufacturer;
import ch.globaz.eavs.model.eCH0058.common.AbstractProduct;
import ch.globaz.eavs.model.eCH0058.common.AbstractProductVersion;
import ch.globaz.eavs.model.eCH0058.v2.Manufacturer;
import ch.globaz.eavs.model.eCH0058.v2.Product;
import ch.globaz.eavs.model.eCH0058.v2.ProductVersion;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractSendingApplication;

public class SendingApplication extends AbstractSendingApplication {
    private Manufacturer manufacturer = null;
    private Product product = null;
    private ProductVersion productVersion = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(manufacturer);
        result.add(product);
        result.add(productVersion);
        return result;
    }

    @Override
    public AbstractManufacturer getManufacturer() {
        if (manufacturer == null) {
            manufacturer = new Manufacturer();
        }
        return manufacturer;
    }

    @Override
    public AbstractProduct getProduct() {
        if (product == null) {
            product = new Product();
        }
        return product;
    }

    @Override
    public AbstractProductVersion getProductVersion() {
        if (productVersion == null) {
            productVersion = new ProductVersion();
        }
        return productVersion;
    }
}

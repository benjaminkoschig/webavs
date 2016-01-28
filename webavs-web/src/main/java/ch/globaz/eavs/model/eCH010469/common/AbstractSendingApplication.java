package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0058.common.AbstractManufacturer;
import ch.globaz.eavs.model.eCH0058.common.AbstractProduct;
import ch.globaz.eavs.model.eCH0058.common.AbstractProductVersion;

public abstract class AbstractSendingApplication extends Ech010469Model implements EAVSNonFinalNode {
    public abstract AbstractManufacturer getManufacturer();

    public abstract AbstractProduct getProduct();

    public abstract AbstractProductVersion getProductVersion();

    public abstract void setManufacturer(EAVSAbstractModel manufacturer);

    public abstract void setProduct(EAVSAbstractModel product);

    public abstract void setProductVersion(EAVSAbstractModel productVersion);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

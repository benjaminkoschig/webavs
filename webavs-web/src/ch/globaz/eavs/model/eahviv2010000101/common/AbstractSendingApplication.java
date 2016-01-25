package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0058.common.AbstractManufacturer;
import ch.globaz.eavs.model.eCH0058.common.AbstractProduct;
import ch.globaz.eavs.model.eCH0058.common.AbstractProductVersion;

public abstract class AbstractSendingApplication extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    public abstract AbstractManufacturer getManufacturer();

    public abstract AbstractProduct getProduct();

    public abstract AbstractProductVersion getProductVersion();
}

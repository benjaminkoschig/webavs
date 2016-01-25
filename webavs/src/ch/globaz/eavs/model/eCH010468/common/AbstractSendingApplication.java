package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0058.common.AbstractManufacturer;
import ch.globaz.eavs.model.eCH0058.common.AbstractProduct;
import ch.globaz.eavs.model.eCH0058.common.AbstractProductVersion;

public abstract class AbstractSendingApplication extends Ech010468Model implements EAVSNonFinalNode {
    public abstract AbstractManufacturer getManufacturer();

    public abstract AbstractProduct getProduct();

    public abstract AbstractProductVersion getProductVersion();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

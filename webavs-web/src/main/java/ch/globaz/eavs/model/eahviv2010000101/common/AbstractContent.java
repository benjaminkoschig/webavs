package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractContent extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    public abstract AbstractOrderScope getOrderScope();

    public abstract AbstractTaxpayer getTaxpayer();

    public abstract AbstractSpouse getSpouse();

    public abstract AbstractMutation getMutation();
}

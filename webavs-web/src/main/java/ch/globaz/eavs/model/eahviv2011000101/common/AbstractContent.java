package ch.globaz.eavs.model.eahviv2011000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractContent extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractMutation getMutation();

    public abstract AbstractOrderScope getOrderScope();

    public abstract AbstractSpouse getSpouse();

    public abstract AbstractTaxpayer getTaxpayer();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

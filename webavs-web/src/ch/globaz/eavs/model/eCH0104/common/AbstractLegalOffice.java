package ch.globaz.eavs.model.eCH0104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractLegalOffice extends Ech0104Model implements EAVSNonFinalNode {

    public abstract AbstractOfficeIdentifier getOfficeIdentifier();

    public abstract void setOfficeIdentifier(EAVSAbstractModel _officeIdentifier);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }

}

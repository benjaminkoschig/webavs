package ch.globaz.eavs.model.eCH0104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractErrorPeriod extends Ech0104Model implements EAVSNonFinalNode {

    public abstract AbstractEnd getEnd();

    public abstract AbstractStart getStart();

    public abstract void setEnd(EAVSAbstractModel _end);

    public abstract void setStart(EAVSAbstractModel _start);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }

}

package ch.globaz.eavs.model.eCH0044.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractOtherPersonId extends Ech0044Model implements EAVSNonFinalNode {

    public abstract AbstractPersonId getPersonId();

    public abstract AbstractPersonIdCategory getPersonIdCategory();

    public abstract void setPersonId(EAVSAbstractModel value);

    public abstract void setPersonIdCategory(EAVSAbstractModel value);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

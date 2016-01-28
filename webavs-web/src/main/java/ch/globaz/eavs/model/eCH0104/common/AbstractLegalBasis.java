package ch.globaz.eavs.model.eCH0104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractLegalBasis extends Ech0104Model implements EAVSNonFinalNode {

    public abstract AbstractCanton getCanton();

    public abstract AbstractLaw getLaw();

    public abstract void setCanton(EAVSAbstractModel _canton);

    public abstract void setLaw(EAVSAbstractModel _law);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

}

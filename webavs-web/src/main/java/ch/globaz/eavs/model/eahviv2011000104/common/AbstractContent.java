package ch.globaz.eavs.model.eahviv2011000104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractContent extends CommonModel implements EAVSNonFinalNode {

    public abstract AbstractReasonOfRejection getReasonOfRejection();

    public abstract AbstractRemark getRemark();

    public abstract void setReasonOfRejection(EAVSAbstractModel _reasonOfRejection);

    public abstract void setRemark(EAVSAbstractModel _remark);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}

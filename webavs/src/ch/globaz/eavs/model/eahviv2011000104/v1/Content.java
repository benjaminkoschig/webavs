package ch.globaz.eavs.model.eahviv2011000104.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000104.common.AbstractContent;
import ch.globaz.eavs.model.eahviv2011000104.common.AbstractReasonOfRejection;
import ch.globaz.eavs.model.eahviv2011000104.common.AbstractRemark;

public class Content extends AbstractContent {
    private ReasonOfRejection reasonOfRejection = null;
    private Remark remark = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(remark);
        result.add(reasonOfRejection);
        return result;
    }

    @Override
    public AbstractReasonOfRejection getReasonOfRejection() {
        if (reasonOfRejection == null) {
            reasonOfRejection = new ReasonOfRejection();
        }
        return reasonOfRejection;
    }

    @Override
    public AbstractRemark getRemark() {
        if (remark == null) {
            remark = new Remark();
        }
        return remark;
    }

    @Override
    public void setReasonOfRejection(EAVSAbstractModel _reasonOfRejection) {
        reasonOfRejection = (ReasonOfRejection) _reasonOfRejection;
    }

    @Override
    public void setRemark(EAVSAbstractModel _remark) {
        remark = (Remark) _remark;
    }

}

package ch.globaz.eavs.model.eCH010469.common;

import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractResponse extends Ech010469Model implements EAVSNonFinalNode {

    public abstract void addNotice(EAVSAbstractModel _notice);

    public abstract void addReceipt(EAVSAbstractModel _receipt);

    public abstract void addRegisterStatusRecord(EAVSAbstractModel _registerStatusRecord);

    public abstract void addUPISynchronizationRecord(EAVSAbstractModel _uPISynchronizationRecord);

    public abstract List getNotice();

    public abstract List getReceipt();

    public abstract List getRegisterStatusRecord();

    public abstract List getUPISynchronizationRecord();
}

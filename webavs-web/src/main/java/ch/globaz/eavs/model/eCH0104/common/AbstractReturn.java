package ch.globaz.eavs.model.eCH0104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH010468.common.AbstractBeneficiary;
import ch.globaz.eavs.model.eCH010468.common.AbstractChild;
import ch.globaz.eavs.model.eCH010469.common.AbstractReturnCode;

public abstract class AbstractReturn extends Ech0104Model implements EAVSNonFinalNode {

    public abstract AbstractBeneficiary getBeneficiary();

    public abstract AbstractChild getChild();

    public abstract AbstractCreationDate getCreationDate();

    public abstract AbstractDeliveryOffice getDeliveryOffice();

    public abstract AbstractError getError();

    public abstract AbstractFamilyAllowanceType getFamilyAllowanceType();

    public abstract AbstractMutationDate getMutationDate();

    public abstract AbstractErrorPeriod getOverlapPeriod();

    public abstract AbstractRecordNumber getRecordNumber();

    public abstract AbstractReturnCode getReturnCode();

    public abstract void setBeneficiary(EAVSAbstractModel _beneficiary);

    public abstract void setChild(EAVSAbstractModel _child);

    public abstract void setCreationDate(EAVSAbstractModel _creationDate);

    public abstract void setDeliveryOffice(EAVSAbstractModel _deliveryOffice);

    public abstract void setError(EAVSAbstractModel _error);

    public abstract void setFamilyAllowanceType(EAVSAbstractModel _familyAllowanceType);

    public abstract void setMutationDate(EAVSAbstractModel _mutationDate);

    public abstract void setOverlapPeriod(EAVSAbstractModel _overlapPeriod);

    public abstract void setRecordNumber(EAVSAbstractModel _recordNumber);

    public abstract void setReturnCode(EAVSAbstractModel _returnCode);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }

}

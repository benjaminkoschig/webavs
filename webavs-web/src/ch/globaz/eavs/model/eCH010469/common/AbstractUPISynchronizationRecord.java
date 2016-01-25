package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0104.common.AbstractCreationDate;
import ch.globaz.eavs.model.eCH0104.common.AbstractDeliveryOffice;
import ch.globaz.eavs.model.eCH0104.common.AbstractError;
import ch.globaz.eavs.model.eCH0104.common.AbstractFamilyAllowanceType;
import ch.globaz.eavs.model.eCH0104.common.AbstractMutationDate;
import ch.globaz.eavs.model.eCH0104.common.AbstractRecordNumber;

public abstract class AbstractUPISynchronizationRecord extends Ech010469Model implements EAVSNonFinalNode {

    public abstract AbstractBeneficiary getBeneficiary();

    public abstract AbstractChild getChild();

    public abstract AbstractCreationDate getCreationDate();

    public abstract AbstractDeliveryOffice getDeliveryOffice();

    public abstract AbstractError getError();

    public abstract AbstractFamilyAllowanceType getFamilyAllowanceType();

    public abstract AbstractMutationDate getMutationDate();

    public abstract AbstractRecordNumber getRecordNumber();

    public abstract AbstractReturnCode getReturnCode();

    public abstract void setBeneficiary(EAVSAbstractModel _beneficiary);

    public abstract void setChild(EAVSAbstractModel _child);

    public abstract void setCreationDate(EAVSAbstractModel _creationDate);

    public abstract void setDeliveryOffice(EAVSAbstractModel _deliveryOffice);

    public abstract void setError(EAVSAbstractModel _error);

    public abstract void setFamilyAllowanceType(EAVSAbstractModel _familyAllowanceType);

    public abstract void setMutationDate(EAVSAbstractModel _mutationDate);

    public abstract void setRecordNumber(EAVSAbstractModel _recordNumber);

    public abstract void setReturnCode(EAVSAbstractModel _returnCode);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }
}

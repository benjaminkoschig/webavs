package ch.globaz.eavs.model.eCH010469.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0104.common.AbstractComment;
import ch.globaz.eavs.model.eCH0104.common.AbstractCreationDate;
import ch.globaz.eavs.model.eCH0104.common.AbstractDeliveryOffice;
import ch.globaz.eavs.model.eCH0104.common.AbstractError;
import ch.globaz.eavs.model.eCH0104.common.AbstractErrorPeriod;
import ch.globaz.eavs.model.eCH0104.common.AbstractFamilyAllowanceType;
import ch.globaz.eavs.model.eCH0104.common.AbstractLegalBasis;
import ch.globaz.eavs.model.eCH0104.common.AbstractLegalOffice;
import ch.globaz.eavs.model.eCH0104.common.AbstractMutationDate;
import ch.globaz.eavs.model.eCH0104.common.AbstractRecordNumber;
import ch.globaz.eavs.model.eCH0104.common.AbstractSpecialTreatment;
import ch.globaz.eavs.model.eCH0104.common.AbstractValidityPeriod;

public abstract class AbstractRegisterStatusRecord extends Ech010469Model implements EAVSNonFinalNode {

    public abstract AbstractBeneficiary getBeneficiary();

    public abstract AbstractChild getChild();

    public abstract AbstractComment getComment();

    public abstract AbstractCreationDate getCreationDate();

    public abstract AbstractDeliveryOffice getDeliveryOffice();

    public abstract AbstractError getError();

    public abstract AbstractErrorPeriod getErrorPeriod();

    public abstract AbstractFamilyAllowanceType getFamilyAllowanceType();

    public abstract AbstractLegalBasis getLegalBasis();

    public abstract AbstractLegalOffice getLegalOffice();

    public abstract AbstractMutationDate getMutationDate();

    public abstract AbstractRecordNumber getRecordNumber();

    public abstract AbstractReturnCode getReturnCode();

    public abstract AbstractSpecialTreatment getSpecialTreatment();

    public abstract AbstractValidityPeriod getValidityPeriod();

    public abstract void setBeneficiary(EAVSAbstractModel _beneficiary);

    public abstract void setChild(EAVSAbstractModel _child);

    public abstract void setComment(EAVSAbstractModel _comment);

    public abstract void setCreationDate(EAVSAbstractModel _creationDate);

    public abstract void setDeliveryOffice(EAVSAbstractModel _deliveryOffice);

    public abstract void setError(EAVSAbstractModel _error);

    public abstract void setErrorPeriod(EAVSAbstractModel _errorPeriod);

    public abstract void setFamilyAllowanceType(EAVSAbstractModel _familyAllowanceType);

    public abstract void setLegalBasis(EAVSAbstractModel _legalBasis);

    public abstract void setLegalOffice(EAVSAbstractModel _legalOffice);

    public abstract void setMutationDate(EAVSAbstractModel _mutationDate);

    public abstract void setRecordNumber(EAVSAbstractModel _recordNumber);

    public abstract void setReturnCode(EAVSAbstractModel _returnCode);

    public abstract void setSpecialTreatment(EAVSAbstractModel _specialTreatmentt);

    public abstract void setValidityPeriod(EAVSAbstractModel _validityPeriod);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }

}

package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0104.common.AbstractComment;
import ch.globaz.eavs.model.eCH0104.common.AbstractContact;
import ch.globaz.eavs.model.eCH0104.common.AbstractDeliveryOffice;
import ch.globaz.eavs.model.eCH0104.common.AbstractFamilyAllowanceType;
import ch.globaz.eavs.model.eCH0104.common.AbstractLegalBasis;
import ch.globaz.eavs.model.eCH0104.common.AbstractLegalOffice;
import ch.globaz.eavs.model.eCH0104.common.AbstractRecordNumber;
import ch.globaz.eavs.model.eCH0104.common.AbstractValidityPeriod;

public abstract class AbstractNewBenefit extends Ech010468Model implements EAVSNonFinalNode {

    public abstract AbstractBeneficiary getBeneficiary();

    public abstract AbstractChild getChild();

    public abstract AbstractComment getComment();

    public abstract AbstractContact getContact();

    public abstract AbstractDeliveryOffice getDeliveryOffice();

    public abstract AbstractFamilyAllowanceType getFamilyAllowanceType();

    public abstract AbstractLegalBasis getLegalBasis();

    public abstract AbstractLegalOffice getLegalOffice();

    public abstract AbstractRecordNumber getRecordNumber();

    public abstract AbstractValidityPeriod getValidityPeriod();

    public abstract void setBeneficiary(EAVSAbstractModel _beneficiary);

    public abstract void setChild(EAVSAbstractModel _child);

    public abstract void setComment(EAVSAbstractModel _comment);

    public abstract void setContact(EAVSAbstractModel _contact);

    public abstract void setDeliveryOffice(EAVSAbstractModel _deliveryOffice);

    public abstract void setFamilyAllowanceType(EAVSAbstractModel _familyAllowanceType);

    public abstract void setLegalBasis(EAVSAbstractModel _legalBasis);

    public abstract void setLegalOffice(EAVSAbstractModel _legalOffice);

    public abstract void setRecordNumber(EAVSAbstractModel _recordNumber);

    public abstract void setValidityPeriod(EAVSAbstractModel _validityPeriod);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }
}

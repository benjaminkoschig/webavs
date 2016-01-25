package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0104.common.AbstractComment;
import ch.globaz.eavs.model.eCH0104.common.AbstractDeliveryOffice;
import ch.globaz.eavs.model.eCH0104.common.AbstractFamilyAllowanceType;
import ch.globaz.eavs.model.eCH0104.common.AbstractRecordNumber;

public abstract class AbstractBenefitCancellationType extends Ech010468Model implements EAVSNonFinalNode {

    public abstract AbstractChild getChild();

    public abstract AbstractComment getComment();

    public abstract AbstractDeliveryOffice getDeliveryOffice();

    public abstract AbstractFamilyAllowanceType getFamilyAllowanceType();

    public abstract AbstractRecordNumber getRecordNumber();

    public abstract void setChild(EAVSAbstractModel _child);

    public abstract void setComment(EAVSAbstractModel _comment);

    public abstract void setDeliveryOffice(EAVSAbstractModel _deliveryOffice);

    public abstract void setFamilyAllowanceType(EAVSAbstractModel _familyAllowanceType);

    public abstract void setRecordNumber(EAVSAbstractModel _recordNumber);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }
}

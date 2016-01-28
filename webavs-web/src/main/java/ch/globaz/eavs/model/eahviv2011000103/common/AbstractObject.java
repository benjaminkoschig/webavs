package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractAddress;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfBirth;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfEntry;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractFirstName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractLocalPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOfficialName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOtherPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractSex;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractVn;

public abstract class AbstractObject extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAddress getAddress();

    public abstract AbstractDateOfBirth getDateOfBirth();

    public abstract AbstractDateOfEntry getDateOfEntry();

    public abstract AbstractDateOfMaritalStatus getDateOfMaritalStatus();

    public abstract AbstractFirstName getFirstName();

    public abstract AbstractLocalPersonId getLocalPersonId();

    public abstract AbstractMaritalStatus getMaritalStatus();

    public abstract AbstractOfficialName getOfficialName();

    public abstract AbstractOtherPersonId getOtherPersonId();

    public abstract AbstractSex getSex();

    public abstract AbstractVn getVn();

    public abstract void setAddress(EAVSAbstractModel _address);

    public abstract void setDateOfBirth(EAVSAbstractModel _dateOfBirth);

    public abstract void setDateOfDeparture(EAVSAbstractModel _dateOfDeparture);

    public abstract void setDateOfEntry(EAVSAbstractModel _dateOfEntry);

    public abstract void setDateOfMaritalStatus(EAVSAbstractModel _dateOfMaritalStatus);

    public abstract void setEuPersonId(EAVSAbstractModel _euPersonId);

    public abstract void setFirstName(EAVSAbstractModel _firstName);

    public abstract void setLocalPersonId(EAVSAbstractModel _localPersonId);

    public abstract void setMaritalStatus(EAVSAbstractModel _maritalStatus);

    public abstract void setOfficialName(EAVSAbstractModel _officialName);

    public abstract void setOtherPersonId(EAVSAbstractModel _otherPersonId);

    public abstract void setSex(EAVSAbstractModel _sex);

    public abstract void setVn(EAVSAbstractModel _vn);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}

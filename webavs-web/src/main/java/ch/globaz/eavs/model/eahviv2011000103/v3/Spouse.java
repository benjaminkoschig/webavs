package ch.globaz.eavs.model.eahviv2011000103.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractSpouse;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractAddress;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfBirth;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfDeparture;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfEntry;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEuPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractFirstName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractLocalPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOfficialName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOtherPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractSex;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractVn;
import ch.globaz.eavs.model.eahvivcommon.v1.Address;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfBirth;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfDeparture;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfEntry;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.v1.EuPersonId;
import ch.globaz.eavs.model.eahvivcommon.v1.FirstName;
import ch.globaz.eavs.model.eahvivcommon.v1.LocalPersonId;
import ch.globaz.eavs.model.eahvivcommon.v1.MaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.v1.OfficialName;
import ch.globaz.eavs.model.eahvivcommon.v1.OtherPersonId;
import ch.globaz.eavs.model.eahvivcommon.v1.Sex;
import ch.globaz.eavs.model.eahvivcommon.v1.Vn;

public class Spouse extends AbstractSpouse {
    private Address address = null;
    private DateOfBirth dateOfBirth = null;
    private DateOfDeparture dateOfDeparture = null;
    private DateOfEntry dateOfEntry = null;
    private DateOfMaritalStatus dateOfMaritalStatus = null;
    private EuPersonId euPersonId = null;
    private FirstName firstName = null;
    private LocalPersonId localPersonId = null;
    private MaritalStatus maritalStatus = null;
    private OfficialName officialName = null;
    private OtherPersonId otherPersonId = null;
    private Sex sex = null;
    private Vn vn = null;

    @Override
    public AbstractAddress getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(vn);
        result.add(otherPersonId);
        result.add(officialName);
        result.add(firstName);
        result.add(sex);
        result.add(dateOfBirth);
        result.add(address);
        result.add(maritalStatus);
        result.add(dateOfMaritalStatus);
        result.add(dateOfEntry);
        result.add(localPersonId);
        result.add(euPersonId);
        result.add(dateOfDeparture);
        return result;
    }

    @Override
    public AbstractDateOfBirth getDateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new DateOfBirth();
        }
        return dateOfBirth;
    }

    public AbstractDateOfDeparture getDateOfDeparture() {
        if (dateOfDeparture == null) {
            dateOfDeparture = new DateOfDeparture();
        }
        return dateOfDeparture;
    }

    @Override
    public AbstractDateOfEntry getDateOfEntry() {
        if (dateOfEntry == null) {
            dateOfEntry = new DateOfEntry();
        }
        return dateOfEntry;
    }

    @Override
    public AbstractDateOfMaritalStatus getDateOfMaritalStatus() {
        if (dateOfMaritalStatus == null) {
            dateOfMaritalStatus = new DateOfMaritalStatus();
        }
        return dateOfMaritalStatus;
    }

    public AbstractEuPersonId getEuPersonId() {
        if (euPersonId == null) {
            euPersonId = new EuPersonId();
        }
        return euPersonId;
    }

    @Override
    public AbstractFirstName getFirstName() {
        if (firstName == null) {
            firstName = new FirstName();
        }
        return firstName;
    }

    public AbstractLocalPersonId getLocalPersonId() {
        if (localPersonId == null) {
            localPersonId = new LocalPersonId();
        }
        return localPersonId;
    }

    @Override
    public AbstractMaritalStatus getMaritalStatus() {
        if (maritalStatus == null) {
            maritalStatus = new MaritalStatus();
        }
        return maritalStatus;
    }

    @Override
    public AbstractOfficialName getOfficialName() {
        if (officialName == null) {
            officialName = new OfficialName();
        }
        return officialName;
    }

    @Override
    public AbstractOtherPersonId getOtherPersonId() {
        if (otherPersonId == null) {
            otherPersonId = new OtherPersonId();
        }
        return otherPersonId;
    }

    @Override
    public AbstractSex getSex() {
        if (sex == null) {
            sex = new Sex();
        }
        return sex;
    }

    @Override
    public AbstractVn getVn() {
        if (vn == null) {
            vn = new Vn();
        }
        return vn;
    }

    public void setAddress(EAVSAbstractModel _address) {
        address = (Address) _address;
    }

    public void setDateOfBirth(EAVSAbstractModel _dateOfBirth) {
        dateOfBirth = (DateOfBirth) _dateOfBirth;
    }

    public void setDateOfDeparture(EAVSAbstractModel _dateOfDeparture) {
        dateOfDeparture = (DateOfDeparture) _dateOfDeparture;
    }

    public void setDateOfEntry(EAVSAbstractModel _dateOfEntry) {
        dateOfEntry = (DateOfEntry) _dateOfEntry;
    }

    public void setDateOfMaritalStatus(EAVSAbstractModel _dateOfMaritalStatus) {
        dateOfMaritalStatus = (DateOfMaritalStatus) _dateOfMaritalStatus;
    }

    public void setEuPersonId(EAVSAbstractModel _euPersonId) {
        euPersonId = (EuPersonId) _euPersonId;
    }

    public void setFirstName(EAVSAbstractModel _firstName) {
        firstName = (FirstName) _firstName;
    }

    public void setLocalPersonId(EAVSAbstractModel _localPersonId) {
        localPersonId = (LocalPersonId) _localPersonId;
    }

    public void setMaritalStatus(EAVSAbstractModel _maritalStatus) {
        maritalStatus = (MaritalStatus) _maritalStatus;
    }

    public void setOfficialName(EAVSAbstractModel _officialName) {
        officialName = (OfficialName) _officialName;
    }

    public void setOtherPersonId(EAVSAbstractModel _otherPersonId) {
        otherPersonId = (OtherPersonId) _otherPersonId;
    }

    public void setSex(EAVSAbstractModel _sex) {
        sex = (Sex) _sex;
    }

    public void setVn(EAVSAbstractModel _vn) {
        vn = (Vn) _vn;
    }
}

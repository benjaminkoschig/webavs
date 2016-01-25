package ch.globaz.eavs.model.eahviv2010000101.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractObject;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractAddress;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfBirth;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfEntry;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractFirstName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOfficialName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOtherPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractSex;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractVn;
import ch.globaz.eavs.model.eahvivcommon.v1.Address;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfBirth;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfEntry;
import ch.globaz.eavs.model.eahvivcommon.v1.DateOfMaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.v1.FirstName;
import ch.globaz.eavs.model.eahvivcommon.v1.MaritalStatus;
import ch.globaz.eavs.model.eahvivcommon.v1.OfficialName;
import ch.globaz.eavs.model.eahvivcommon.v1.OtherPersonId;
import ch.globaz.eavs.model.eahvivcommon.v1.Sex;
import ch.globaz.eavs.model.eahvivcommon.v1.Vn;

public class Object extends AbstractObject {
    private Address address = null;
    private DateOfBirth dateOfBirth = null;
    private DateOfEntry dateOfEntry = null;
    private DateOfMaritalStatus dateOfMaritalStatus = null;
    private MaritalStatus maritalStatus = null;
    private OtherPersonId otherPersonId = null;
    private Sex sex = null;
    private Vn vn = null;
    private OfficialName officialName = null;
    private FirstName firstName = null;

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
        return result;
    }

    @Override
    public AbstractAddress getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    @Override
    public AbstractDateOfBirth getDateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new DateOfBirth();
        }
        return dateOfBirth;
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

    @Override
    public AbstractMaritalStatus getMaritalStatus() {
        if (maritalStatus == null) {
            maritalStatus = new MaritalStatus();
        }
        return maritalStatus;
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

    @Override
    public AbstractFirstName getFirstName() {
        if (firstName == null) {
            firstName = new FirstName();
        }
        return firstName;
    }

    @Override
    public AbstractOfficialName getOfficialName() {
        if (officialName == null) {
            officialName = new OfficialName();
        }
        return officialName;
    }

}

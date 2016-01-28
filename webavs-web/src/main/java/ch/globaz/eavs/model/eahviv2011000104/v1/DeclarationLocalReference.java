package ch.globaz.eavs.model.eahviv2011000104.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000104.common.AbstractDeclarationLocalReference;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDepartment;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEmail;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOther;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractPhone;
import ch.globaz.eavs.model.eahvivcommon.v0.Department;
import ch.globaz.eavs.model.eahvivcommon.v0.Email;
import ch.globaz.eavs.model.eahvivcommon.v0.Name;
import ch.globaz.eavs.model.eahvivcommon.v0.Phone;
import ch.globaz.eavs.model.eahvivcommon.v1.Other;

public class DeclarationLocalReference extends AbstractDeclarationLocalReference {
    private Department department = null;
    private Email email = null;
    private Name name = null;
    private Other other = null;
    private Phone phone = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(name);
        result.add(department);
        result.add(phone);
        result.add(email);
        result.add(other);
        return result;
    }

    @Override
    public AbstractDepartment getDepartment() {
        if (department == null) {
            department = new Department();
        }
        return department;
    }

    @Override
    public AbstractEmail getEmail() {
        if (email == null) {
            email = new Email();
        }
        return email;
    }

    @Override
    public AbstractName getName() {
        if (name == null) {
            name = new Name();
        }
        return name;
    }

    @Override
    public AbstractOther getOther() {
        if (other == null) {
            other = new Other();
        }
        return other;
    }

    @Override
    public AbstractPhone getPhone() {
        if (phone == null) {
            phone = new Phone();
        }
        return phone;
    }

    @Override
    public void setDepartment(EAVSAbstractModel _department) {
        department = (Department) _department;
    }

    @Override
    public void setEmail(EAVSAbstractModel _email) {
        email = (Email) _email;
    }

    @Override
    public void setName(EAVSAbstractModel _name) {
        name = (Name) _name;
    }

    @Override
    public void setOther(EAVSAbstractModel _other) {
        other = (Other) _other;
    }

    @Override
    public void setPhone(EAVSAbstractModel _phone) {
        phone = (Phone) _phone;
    }

}

package ch.globaz.eavs.model.eahviv2010000101.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractDeclarationLocalReference;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDepartment;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEmail;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractPhone;
import ch.globaz.eavs.model.eahvivcommon.v0.Department;
import ch.globaz.eavs.model.eahvivcommon.v0.Email;
import ch.globaz.eavs.model.eahvivcommon.v0.Name;
import ch.globaz.eavs.model.eahvivcommon.v0.Phone;

public class DeclarationLocalReference extends AbstractDeclarationLocalReference implements EAVSNonFinalNode {
    private Name name = null;
    private Email email = null;
    private Department department = null;
    private Phone phone = null;

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
    public AbstractPhone getPhone() {
        if (phone == null) {
            phone = new Phone();
        }
        return phone;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(name);
        result.add(department);
        result.add(phone);
        result.add(email);
        return result;
    }

}

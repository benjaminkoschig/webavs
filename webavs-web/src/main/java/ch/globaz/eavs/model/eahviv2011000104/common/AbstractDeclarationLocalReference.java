package ch.globaz.eavs.model.eahviv2011000104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDepartment;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEmail;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOther;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractPhone;

public abstract class AbstractDeclarationLocalReference extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractDepartment getDepartment();

    public abstract AbstractEmail getEmail();

    public abstract AbstractName getName();

    public abstract AbstractOther getOther();

    public abstract AbstractPhone getPhone();

    public abstract void setDepartment(EAVSAbstractModel _department);

    public abstract void setEmail(EAVSAbstractModel _email);

    public abstract void setName(EAVSAbstractModel _name);

    public abstract void setOther(EAVSAbstractModel _other);

    public abstract void setPhone(EAVSAbstractModel _phone);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }
}

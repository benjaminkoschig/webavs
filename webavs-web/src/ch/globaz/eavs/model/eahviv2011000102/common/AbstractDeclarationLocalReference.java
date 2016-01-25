package ch.globaz.eavs.model.eahviv2011000102.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDepartment;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEmail;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractPhone;

public abstract class AbstractDeclarationLocalReference extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractDepartment getDepartment();

    public abstract AbstractEmail getEmail();

    public abstract AbstractName getName();

    public abstract AbstractPhone getPhone();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

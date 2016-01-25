package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDepartment;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractEmail;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractName;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractPhone;

public abstract class AbstractDeclarationLocalReference extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    public abstract AbstractName getName();

    public abstract AbstractDepartment getDepartment();

    public abstract AbstractPhone getPhone();

    public abstract AbstractEmail getEmail();
}

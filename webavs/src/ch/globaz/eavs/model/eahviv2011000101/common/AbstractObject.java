package ch.globaz.eavs.model.eahviv2011000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
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

public abstract class AbstractObject extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAddress getAddress();

    public abstract AbstractDateOfBirth getDateOfBirth();

    public abstract AbstractDateOfEntry getDateOfEntry();

    public abstract AbstractDateOfMaritalStatus getDateOfMaritalStatus();

    public abstract AbstractFirstName getFirstName();

    public abstract AbstractMaritalStatus getMaritalStatus();

    public abstract AbstractOfficialName getOfficialName();

    public abstract AbstractOtherPersonId getOtherPersonId();

    public abstract AbstractSex getSex();

    public abstract AbstractVn getVn();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

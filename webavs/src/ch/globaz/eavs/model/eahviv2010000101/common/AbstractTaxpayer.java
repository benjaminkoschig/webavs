package ch.globaz.eavs.model.eahviv2010000101.common;

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

public abstract class AbstractTaxpayer extends CommonModel implements EAVSNonFinalNode {
    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    public abstract AbstractVn getVn();

    public abstract AbstractOtherPersonId getOtherPersonId();

    public abstract AbstractOfficialName getOfficialName();

    public abstract AbstractFirstName getFirstName();

    public abstract AbstractSex getSex();

    public abstract AbstractDateOfBirth getDateOfBirth();

    public abstract AbstractAddress getAddress();

    public abstract AbstractMaritalStatus getMaritalStatus();

    public abstract AbstractDateOfMaritalStatus getDateOfMaritalStatus();

    public abstract AbstractDateOfEntry getDateOfEntry();

}

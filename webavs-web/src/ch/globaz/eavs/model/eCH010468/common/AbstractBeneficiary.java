package ch.globaz.eavs.model.eCH010468.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0044.common.AbstractDateOfBirth;
import ch.globaz.eavs.model.eCH0044.common.AbstractEuPersonId;
import ch.globaz.eavs.model.eCH0044.common.AbstractFirstName;
import ch.globaz.eavs.model.eCH0044.common.AbstractOfficialName;
import ch.globaz.eavs.model.eCH0044.common.AbstractSex;
import ch.globaz.eavs.model.eCH0104.common.AbstractDateOfDeath;
import ch.globaz.eavs.model.eCH0104.common.AbstractFamilialStatus;
import ch.globaz.eavs.model.eCH0104.common.AbstractOccupationStatus;
import ch.globaz.eavs.model.eCH0104.common.AbstractVn;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractLocalPersonId;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractOtherPersonId;

public abstract class AbstractBeneficiary extends Ech010468Model implements EAVSNonFinalNode {

    public abstract AbstractDateOfBirth getDateOfBirth();

    public abstract AbstractDateOfDeath getDateOfDeath();

    public abstract AbstractEuPersonId getEuPersonId();

    public abstract AbstractFamilialStatus getFamilialStatus();

    public abstract AbstractFirstName getFirstName();

    public abstract AbstractLocalPersonId getLocalPersonId();

    public abstract AbstractOccupationStatus getOccupationStatus();

    public abstract AbstractOfficialName getOfficialName();

    public abstract AbstractOtherPersonId getOtherPersonId();

    public abstract AbstractSex getSex();

    public abstract AbstractVn getVn();

    public abstract void setDateOfBirth(EAVSAbstractModel _dateOfBirth);

    public abstract void setDateOfDeath(EAVSAbstractModel _dateOfDeath);

    public abstract void setEuPersonId(EAVSAbstractModel _euPersonId);

    public abstract void setFamilialStatus(EAVSAbstractModel _familialStatus);

    public abstract void setFirstName(EAVSAbstractModel _firstName);

    public abstract void setLocalPersonId(EAVSAbstractModel _localPersonId);

    public abstract void setOccupationStatus(EAVSAbstractModel _occupationStatus);

    public abstract void setOfficialName(EAVSAbstractModel _officialName);

    public abstract void setOtherPersonId(EAVSAbstractModel _otherPersonId);

    public abstract void setSex(EAVSAbstractModel _sex);

    public abstract void setVn(EAVSAbstractModel _vn);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }

}

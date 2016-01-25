package ch.globaz.eavs.model.eahviv2011000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMutation extends CommonModel implements EAVSNonFinalNode {

    public abstract AbstractAccountNumber getAccountNumber();

    public abstract AbstractDateOfBirth getDateOfBirth();

    public abstract AbstractFirstName getFirstName();

    public abstract AbstractLegalForm getLegalForm();

    public abstract AbstractOfficialName getOfficialName();

    public abstract AbstractOrganisationName getOrganisationName();

    public abstract AbstractReason getReason();

    public abstract AbstractSex getSex();

    public abstract AbstractTitle getTitle();

    public abstract AbstractVn getVn();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

}

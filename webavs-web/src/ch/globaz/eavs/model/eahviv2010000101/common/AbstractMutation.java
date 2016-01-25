package ch.globaz.eavs.model.eahviv2010000101.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractMutation extends CommonModel implements EAVSNonFinalNode {

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
    }

    public abstract AbstractReason getReason();

    public abstract AbstractAccountNumber getAccountNumber();

    public abstract AbstractOtherOrganisationID getOtherOrganisationID();

    public abstract AbstractLegalForm getLegalForm();

    public abstract AbstractTitle getTitle();

    public abstract AbstractVn getVn();

    public abstract AbstractDateOfBirth getDateOfBirth();

    public abstract AbstractSex getSex();

    public abstract AbstractOfficialName getOfficialName();

    public abstract AbstractFirstName getFirstName();

    public abstract AbstractOrganisationName1 getOrganisationName1();

    public abstract AbstractOrganisationName2 getOrganisationName2();

    public abstract AbstractKindOfActivity getKindOfActivity();

    public abstract AbstractDateOfValidity getDateOfValidity();

    public abstract AbstractOasiDateOfAcceptance getOasiDateOfAcceptance();

    public abstract AbstractOasiDateOfDeletion getOasiDateOfDeletion();

    public abstract AbstractOasiCompensationOffice getOasiCompensationOffice();

    public abstract AbstractFaoCompensationOffice getFaoCompensationOffice();

    public abstract AbstractFaoDateOfAcceptance getFaoDateOfAcceptance();

    public abstract AbstractFaoDateOfDeletion getFaoDateOfDeletion();

    public abstract AbstractOrganisationNameReturnAddress getOrganisationNameReturnAddress();

    public abstract AbstractAddress getAddress();

    public abstract AbstractOasiCommunication getOasiCommunication();

    public abstract AbstractFaoCommunication getFaoCommunication();

    public abstract AbstractNameReturnAddress1 getNameReturnAddress1();

    public abstract AbstractNameReturnAddress2 getNameReturnAddress2();

    public abstract AbstractNameReturnAddress3 getNameReturnAddress3();

    public abstract AbstractReturnAddress getReturnAddress();

    public abstract AbstractAdditionalObservations getAdditionalObservations();

}

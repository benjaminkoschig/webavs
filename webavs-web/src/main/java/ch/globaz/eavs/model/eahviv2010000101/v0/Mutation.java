package ch.globaz.eavs.model.eahviv2010000101.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractAccountNumber;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractAdditionalObservations;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractAddress;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractDateOfBirth;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractDateOfValidity;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractFaoCommunication;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractFaoCompensationOffice;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractFaoDateOfAcceptance;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractFaoDateOfDeletion;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractFirstName;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractKindOfActivity;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractLegalForm;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractMutation;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractNameReturnAddress1;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractNameReturnAddress2;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractNameReturnAddress3;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOasiCommunication;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOasiCompensationOffice;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOasiDateOfAcceptance;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOasiDateOfDeletion;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOfficialName;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOrganisationName1;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOrganisationName2;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOrganisationNameReturnAddress;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractOtherOrganisationID;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractReason;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractReturnAddress;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractSex;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractTitle;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractVn;

public class Mutation extends AbstractMutation {

    private AccountNumber accountNumber = null;
    private DateOfBirth dateOfBirth = null;
    private FirstName firstName = null;
    private LegalForm legalForm = null;
    private ch.globaz.eavs.model.eahviv2010000101.v0.OfficialName officialName = null;
    private OrganisationName organisationName = null;
    private Reason reason = null;
    private TypeOfActivity typeOfActivity = null;
    private Sex sex = null;
    private Title title = null;
    private Vn vn = null;
    private DateOfValidity dateOfValidity = null;
    private FaoCompensationOffice faoCompensationOffice = null;
    private OasiCompensationOffice oasiCompensationOffice = null;
    private FaoDateOfAcceptance faoDateOfAcceptance = null;
    private OasiDateOfAcceptance oasiDateOfAcceptance = null;
    private OrganisationNameReturnAddress organisationNameReturnAddress = null;
    private FaoDateOfDeletion faoDateOfDeletion = null;
    private OasiDateOfDeletion oasiDateOfDeletion = null;
    private FaoCommunication faoCommunication = null;
    private OasiCommunication oasiCommunication = null;

    @Override
    public AbstractAccountNumber getAccountNumber() {
        if (accountNumber == null) {
            accountNumber = new AccountNumber();
        }
        return accountNumber;
    }

    @Override
    public AbstractDateOfBirth getDateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new DateOfBirth();
        }
        return dateOfBirth;
    }

    @Override
    public AbstractFirstName getFirstName() {
        if (firstName == null) {
            firstName = new FirstName();
        }
        return firstName;
    }

    @Override
    public AbstractLegalForm getLegalForm() {
        if (legalForm == null) {
            legalForm = new LegalForm();
        }
        return legalForm;
    }

    @Override
    public AbstractOfficialName getOfficialName() {
        if (officialName == null) {
            officialName = new OfficialName();
        }
        return officialName;
    }

    public AbstractKindOfActivity getTypeOfActivity() {
        if (typeOfActivity == null) {
            typeOfActivity = new TypeOfActivity();
        }
        return typeOfActivity;
    }

    public AbstractOrganisationName1 getOrganisationName() {
        if (organisationName == null) {
            organisationName = new OrganisationName();
        }
        return organisationName;
    }

    @Override
    public AbstractReason getReason() {
        if (reason == null) {
            reason = new Reason();
        }
        return reason;
    }

    @Override
    public AbstractSex getSex() {
        if (sex == null) {
            sex = new Sex();
        }
        return sex;
    }

    @Override
    public AbstractTitle getTitle() {
        if (title == null) {
            title = new Title();
        }
        return title;
    }

    @Override
    public AbstractVn getVn() {
        if (vn == null) {
            vn = new Vn();
        }
        return vn;
    }

    @Override
    public AbstractDateOfValidity getDateOfValidity() {
        if (dateOfValidity == null) {
            dateOfValidity = new DateOfValidity();
        }
        return dateOfValidity;
    }

    @Override
    public AbstractFaoCompensationOffice getFaoCompensationOffice() {
        if (faoCompensationOffice == null) {
            faoCompensationOffice = new FaoCompensationOffice();
        }
        return faoCompensationOffice;
    }

    @Override
    public AbstractOasiCompensationOffice getOasiCompensationOffice() {
        if (oasiCompensationOffice == null) {
            oasiCompensationOffice = new OasiCompensationOffice();
        }
        return oasiCompensationOffice;
    }

    @Override
    public AbstractFaoDateOfAcceptance getFaoDateOfAcceptance() {
        if (faoDateOfAcceptance == null) {
            faoDateOfAcceptance = new FaoDateOfAcceptance();
        }
        return faoDateOfAcceptance;
    }

    @Override
    public AbstractOasiDateOfAcceptance getOasiDateOfAcceptance() {
        if (oasiDateOfAcceptance == null) {
            oasiDateOfAcceptance = new OasiDateOfAcceptance();
        }
        return oasiDateOfAcceptance;
    }

    @Override
    public AbstractOasiCommunication getOasiCommunication() {
        if (oasiCommunication == null) {
            oasiCommunication = new OasiCommunication();
        }
        return oasiCommunication;
    }

    @Override
    public AbstractFaoCommunication getFaoCommunication() {
        if (faoCommunication == null) {
            faoCommunication = new FaoCommunication();
        }
        return faoCommunication;
    }

    @Override
    public AbstractOrganisationNameReturnAddress getOrganisationNameReturnAddress() {
        if (organisationNameReturnAddress == null) {
            organisationNameReturnAddress = new OrganisationNameReturnAddress();
        }
        return organisationNameReturnAddress;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(accountNumber);
        result.add(dateOfBirth);
        result.add(firstName);
        result.add(legalForm);
        result.add(officialName);
        result.add(organisationName);
        result.add(reason);
        result.add(sex);
        result.add(title);
        result.add(vn);
        result.add(organisationNameReturnAddress);
        result.add(oasiDateOfAcceptance);
        result.add(oasiDateOfDeletion);
        result.add(oasiCommunication);
        result.add(faoDateOfAcceptance);
        result.add(faoDateOfDeletion);
        result.add(faoCompensationOffice);
        result.add(dateOfValidity);
        result.add(faoCommunication);
        return result;
    }

    @Override
    public AbstractAddress getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractFaoDateOfDeletion getFaoDateOfDeletion() {
        if (faoDateOfDeletion == null) {
            faoDateOfDeletion = new FaoDateOfDeletion();
        }
        return faoDateOfDeletion;
    }

    @Override
    public AbstractOasiDateOfDeletion getOasiDateOfDeletion() {
        if (oasiDateOfDeletion == null) {
            oasiDateOfDeletion = new OasiDateOfDeletion();
        }
        return oasiDateOfDeletion;
    }

    @Override
    public AbstractAdditionalObservations getAdditionalObservations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractKindOfActivity getKindOfActivity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractNameReturnAddress1 getNameReturnAddress1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractNameReturnAddress2 getNameReturnAddress2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractNameReturnAddress3 getNameReturnAddress3() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractOrganisationName1 getOrganisationName1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractOrganisationName2 getOrganisationName2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractOtherOrganisationID getOtherOrganisationID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractReturnAddress getReturnAddress() {
        // TODO Auto-generated method stub
        return null;
    }
}
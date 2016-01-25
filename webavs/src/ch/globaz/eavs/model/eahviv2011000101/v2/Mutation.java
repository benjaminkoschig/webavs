package ch.globaz.eavs.model.eahviv2011000101.v2;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractAccountNumber;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractDateOfBirth;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractFirstName;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractLegalForm;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractMutation;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractOfficialName;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractOrganisationName;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractReason;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractSex;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractTitle;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractVn;

public class Mutation extends AbstractMutation {

    private AccountNumber accountNumber = null;
    private DateOfBirth dateOfBirth = null;
    private FirstName firstName = null;
    private LegalForm legalForm = null;
    private OfficialName officialName = null;
    private OrganisationName organisationName = null;
    private Reason reason = null;
    private Sex sex = null;
    private Title title = null;
    private Vn vn = null;

    @Override
    public AbstractAccountNumber getAccountNumber() {
        if (accountNumber == null) {
            accountNumber = new AccountNumber();
        }
        return accountNumber;
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

        return result;
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

    @Override
    public AbstractOrganisationName getOrganisationName() {
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

}
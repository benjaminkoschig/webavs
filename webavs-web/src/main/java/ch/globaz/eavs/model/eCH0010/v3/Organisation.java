package ch.globaz.eavs.model.eCH0010.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0010.common.AbstractFirstName;
import ch.globaz.eavs.model.eCH0010.common.AbstractLastName;
import ch.globaz.eavs.model.eCH0010.common.AbstractOrganisation;
import ch.globaz.eavs.model.eCH0010.common.AbstractOrganisationName;
import ch.globaz.eavs.model.eCH0010.common.AbstractOrganisationNameAddOn1;
import ch.globaz.eavs.model.eCH0010.common.AbstractOrganisationNameAddOn2;
import ch.globaz.eavs.model.eCH0010.common.AbstractTitle;

public class Organisation extends AbstractOrganisation implements EAVSNonFinalNode {
    private FirstName firstName = null;
    private LastName lastName = null;
    private OrganisationName organisationName = null;
    private OrganisationNameAddOn1 organisationNameAddOn1 = null;
    private OrganisationNameAddOn2 organisationNameAddOn2 = null;
    private Title title = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(organisationName);
        result.add(organisationNameAddOn1);
        result.add(organisationNameAddOn2);
        result.add(title);
        result.add(firstName);
        result.add(lastName);
        return result;
    }

    @Override
    public AbstractFirstName getFirstName() {
        if (firstName == null) {
            firstName = new FirstName();
        }
        return firstName;
    }

    @Override
    public AbstractLastName getLastName() {
        if (lastName == null) {
            lastName = new LastName();
        }
        return lastName;
    }

    @Override
    public AbstractOrganisationName getOrganisationName() {
        if (organisationName == null) {
            organisationName = new OrganisationName();
        }
        return organisationName;
    }

    @Override
    public AbstractOrganisationNameAddOn1 getOrganisationNameAddOn1() {
        if (organisationNameAddOn1 == null) {
            organisationNameAddOn1 = new OrganisationNameAddOn1();
        }
        return organisationNameAddOn1;
    }

    @Override
    public AbstractOrganisationNameAddOn2 getOrganisationNameAddOn2() {
        if (organisationNameAddOn2 == null) {
            organisationNameAddOn2 = new OrganisationNameAddOn2();
        }
        return organisationNameAddOn2;
    }

    @Override
    public AbstractTitle getTitle() {
        if (title == null) {
            title = new Title();
        }
        return title;
    }

}

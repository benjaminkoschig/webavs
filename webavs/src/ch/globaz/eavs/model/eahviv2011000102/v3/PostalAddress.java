package ch.globaz.eavs.model.eahviv2011000102.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eCH0010.common.AbstractAddressInformation;
import ch.globaz.eavs.model.eCH0010.common.AbstractOrganisation;
import ch.globaz.eavs.model.eCH0010.v3.AddressInformation;
import ch.globaz.eavs.model.eCH0010.v3.Organisation;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractPostalAddress;

public class PostalAddress extends AbstractPostalAddress {

    private AddressInformation addressInformation = null;
    private Organisation organisation = null;

    @Override
    public AbstractAddressInformation getAddressInformation() {
        if (addressInformation == null) {
            addressInformation = new AddressInformation();
        }
        return addressInformation;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(organisation);
        result.add(addressInformation);
        return result;
    }

    @Override
    public AbstractOrganisation getOrganisation() {
        if (organisation == null) {
            organisation = new Organisation();
        }
        return organisation;
    }

}
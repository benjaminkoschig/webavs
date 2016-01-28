package ch.globaz.eavs.model.eCH0010.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractOrganisation extends Ech0010Model implements EAVSNonFinalNode {
    public abstract AbstractFirstName getFirstName();

    public abstract AbstractLastName getLastName();

    public abstract AbstractOrganisationName getOrganisationName();

    public abstract AbstractOrganisationNameAddOn1 getOrganisationNameAddOn1();

    public abstract AbstractOrganisationNameAddOn2 getOrganisationNameAddOn2();

    public abstract AbstractTitle getTitle();

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

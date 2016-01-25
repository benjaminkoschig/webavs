package ch.globaz.eavs.model.eCH0104.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractEmployer extends Ech0104Model implements EAVSNonFinalNode {

    public abstract AbstractContactInformation getContactInformation();

    public abstract AbstractName getName();

    public abstract AbstractWebSite getWebSite();

    public abstract void setContactInformation(EAVSAbstractModel _contactInformation);

    public abstract void setName(EAVSAbstractModel _name);

    public abstract void setWebSite(EAVSAbstractModel _webSite);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {
        // TODO Auto-generated method stub

    }

}

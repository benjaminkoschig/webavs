package ch.globaz.eavs.model.eahviv2011000101.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNameSpaceListener;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractContent;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractHeader;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractMessage;
import ch.globaz.eavs.utils.EAVSUtils;

public class Message extends AbstractMessage {
    private Content content = null;
    private Header header = null;

    public Message() {
        setNameSpaceListener(new EAVSNameSpaceListener());
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(header);
        result.add(content);
        return result;
    }

    @Override
    public AbstractContent getContent() {
        if (content == null) {
            content = new Content();
        }
        return content;
    }

    @Override
    public AbstractHeader getHeader() {
        if (header == null) {
            header = new Header();
        }
        return header;
    }

    public StringBuffer getHeaderXML() throws EAVSInvalidXmlFormatException {
        StringBuffer nameSpaces = getNameSpacesForHeader();
        StringBuffer headerXML = getHeader().asXml();
        String debutHeader = "<" + getTargetNameSpace() + ":" + EAVSUtils.getNomClasse(getHeader().getClass()) + " "
                + nameSpaces.toString();
        headerXML = headerXML.replace(0, getTargetNameSpace().length()
                + EAVSUtils.getNomClasse(getHeader().getClass()).length() + 2, debutHeader);
        return headerXML;
    }

}

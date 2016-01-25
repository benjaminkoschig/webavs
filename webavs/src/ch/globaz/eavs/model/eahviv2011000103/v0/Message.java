package ch.globaz.eavs.model.eahviv2011000103.v0;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractContent;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractHeader;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMessage;

public class Message extends AbstractMessage {
    private Content content = null;
    private Header header = null;

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

    @Override
    public void setContent(EAVSAbstractModel _content) {
        content = (Content) _content;
    }

    @Override
    public void setHeader(EAVSAbstractModel _header) {
        header = (Header) _header;
    }
}

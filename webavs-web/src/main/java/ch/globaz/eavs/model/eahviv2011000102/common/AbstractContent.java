package ch.globaz.eavs.model.eahviv2011000102.common;

import java.util.Vector;
import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eahviv2011000101.v3.Message;

public abstract class AbstractContent extends CommonModel implements EAVSNonFinalNode {
    private Vector tabMessages101 = new Vector();

    public void addMessage101(Message message101) {
        tabMessages101.add(message101);
    }

    public Vector getTabMessages101() {
        return tabMessages101;
    }

    public void removeMessage101(Message message101) {
        tabMessages101.remove(message101);
    }

    public void setTabMessages101(Vector tabMessages101) {
        this.tabMessages101 = tabMessages101;
    }

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}

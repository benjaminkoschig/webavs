package globaz.naos.db.avisMutation;

import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.HeaderType;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.Message;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.ObjectFactory;

public class ExemplePasCommitter {
    public void doIt() {
        ObjectFactory of2001 = new ObjectFactory();
        Message m = of2001.createMessage();
        // set header...
        HeaderType h = of2001.createHeaderType();
        m.setHeader(h);
        h.setMessageType(2010);

    }
}

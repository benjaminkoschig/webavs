package globaz.aries.vb;

import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.aries.exceptions.AriesNotImplementedException;

public abstract class ARAbstractDefaultViewBean extends BJadePersistentObjectViewBean {

    @Override
    public void add() throws Exception {
        throw new AriesNotImplementedException();
    }

    @Override
    public void delete() throws Exception {
        throw new AriesNotImplementedException();
    }

    @Override
    public void update() throws Exception {
        throw new AriesNotImplementedException();
    }

}

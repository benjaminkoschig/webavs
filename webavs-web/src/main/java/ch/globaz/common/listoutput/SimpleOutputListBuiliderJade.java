package ch.globaz.common.listoutput;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import java.util.Locale;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class SimpleOutputListBuiliderJade extends SimpleOutputListBuilder {

    private boolean isContextInizialised = false;

    public SimpleOutputListBuiliderJade() {
        super();
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session != null) {
            local(new Locale(session.getIdLangueISO()));
        }
    }

    public SimpleOutputListBuiliderJade session(BSession session) {

        local(new Locale(session.getIdLangueISO()));
        try {
            BSessionUtil.initContext(session, this);
            isContextInizialised = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public static SimpleOutputListBuiliderJade newInstance() {
        return new SimpleOutputListBuiliderJade();
    }

    public SimpleOutputListBuiliderJade outputNameAndAddPath(String name) {
        String path = (Jade.getInstance().getPersistenceDir() + name + JadeUUIDGenerator.createStringUUID());
        outputName(path);
        return this;
    }

    @Override
    public void close() {
        if (isContextInizialised) {
            BSessionUtil.stopUsingContext(this);
        }
    }
}

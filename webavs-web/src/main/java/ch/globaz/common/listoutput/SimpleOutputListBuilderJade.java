package ch.globaz.common.listoutput;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import java.util.Locale;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class SimpleOutputListBuilderJade extends SimpleOutputListBuilder {

    private boolean isContextInizialised = false;

    public SimpleOutputListBuilderJade() {
        super();
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if (session != null) {
            local(new Locale(session.getIdLangueISO()));
        }
    }

    public SimpleOutputListBuilderJade session(BSession session) {

        local(new Locale(session.getIdLangueISO()));
        try {
            BSessionUtil.initContext(session, this);
            isContextInizialised = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public static SimpleOutputListBuilderJade newInstance() {
        return new SimpleOutputListBuilderJade();
    }

    public SimpleOutputListBuilderJade outputNameAndAddPath(String name) {
        String path = (Jade.getInstance().getPersistenceDir() + name + "_" + JadeUUIDGenerator.createStringUUID());
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

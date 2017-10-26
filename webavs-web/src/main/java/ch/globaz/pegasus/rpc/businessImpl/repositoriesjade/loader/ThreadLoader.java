package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import java.util.List;

public abstract class ThreadLoader<I, O> implements Runnable {

    private final BSession session;
    private final List<String> roles;

    public ThreadLoader(BSession session, List<String> roles) {
        this.session = session;
        this.roles = roles;
    }

    public abstract O getDatas();

    public abstract void load();

    @Override
    public final void run() {
        try {
            initContext(session, this, roles);
            this.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    public static final void initContext(BSession session, Object objetAppelant, List<String> roles) throws Exception {

        JadeContextImplementation ctxtImpl = new JadeContextImplementation();

        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());

        ctxtImpl.setUserRoles(roles);

        JadeThreadActivator.startUsingJdbcContext(objetAppelant, ctxtImpl);
    }

}

package ch.globaz.common.process.byitem;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.job.JadeJob;

public class ProcessItemsFactory {

    private BSession session;
    private ProcessItems<?> processItems;

    private ProcessItemsFactory() {
    }

    public ProcessItemsFactory start(ProcessItems<?> processItems) {
        this.processItems = processItems;
        return this;
    }

    public void build() {
        if (session == null) {
            session = BSessionUtil.getSessionFromThreadContext();
        }
        if (session == null) {
            throw new NullPointerException("The session is null, use the session() function to set it");
        }
        try {
            if (BProcess.class.isAssignableFrom(processItems.getClass())) {
                ((BProcess) processItems).setSession(session);
            } else if (JadeJob.class.isAssignableFrom(processItems.getClass())) {
                ((JadeJob) processItems).setSession(session);
            } else {
                throw new RuntimeException("The process must implement BProcess or JadeJob");
            }
            BProcessLauncher.startJob(processItems);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ProcessItemsFactory newInstance() {
        return new ProcessItemsFactory();
    }

    public ProcessItemsFactory session(BSession session) {
        this.session = session;
        return this;
    }
}

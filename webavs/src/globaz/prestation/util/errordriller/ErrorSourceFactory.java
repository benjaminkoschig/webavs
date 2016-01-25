package globaz.prestation.util.errordriller;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;

class ErrorSourceFactory {
    public static ErrorSource createSourceFrom(BSession aSession) {
        return new SessionErrorSourceAdapter(aSession);
    }

    public static ErrorSource createSourceFromThreadContext() {
        // voir dans le ThreadContext et dans l'éventuelle session contenue!
        // voir dans BSessionUtil

        return new ThreadContextAdapter();
    }

    public static ErrorSource createSourceFrom(FWViewBeanInterface aViewBean) {
        return new FWViewBeanSourceAdapter(aViewBean);
    }
}

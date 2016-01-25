package globaz.prestation.util.errordriller;

import globaz.globall.db.BSession;
import globaz.prestation.util.errordriller.ErrorDriller.DrilledError;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SessionErrorSourceAdapter implements ErrorSource {
    private final BSession managedBSession;

    SessionErrorSourceAdapter(BSession aSession) {
        managedBSession = aSession;
    }

    @Override
    public List<? extends DrilledError> drill() {
        List<DrilledError> errors = new ArrayList<ErrorDriller.DrilledError>();
        if (managedBSession.hasErrors()) {
            errors.add(new DrilledError(managedBSession.getErrors().toString(), managedBSession.getClass().getName()
                    + " " + managedBSession.toString() + " (user: " + managedBSession.getUserName() + ")", new Date()));
        }
        return errors;
    }
}

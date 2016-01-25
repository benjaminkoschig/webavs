package globaz.prestation.util.errordriller;

import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererUtil;
import globaz.jade.log.business.renderer.JadeTranslatedMessage;
import globaz.prestation.util.errordriller.ErrorDriller.DrilledError;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ThreadContextAdapter implements ErrorSource {

    @Override
    public List<? extends DrilledError> drill() {
        JadeThreadContext ctxt = JadeThread.currentContext();
        List<DrilledError> errors = new ArrayList<ErrorDriller.DrilledError>();
        if (ctxt != null) {
            if (JadeThread.logMaxLevel() == JadeBusinessMessageLevels.ERROR) {
                JadeBusinessMessage[] errorMessages = JadeThread.logMessagesOfLevel(JadeBusinessMessageLevels.ERROR);
                for (JadeBusinessMessage msg : errorMessages) {
                    JadeTranslatedMessage mess = JadeBusinessMessageRendererUtil.createMessage(msg, null);
                    errors.add(new DrilledError(mess.getMessage(), ctxt.toString() + " " + ctxt.getUserName(),
                            new Date()));
                }
            }
        }
        return errors;
    }
}

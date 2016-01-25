package globaz.naos.stack.rules;

import globaz.framework.utils.rules.FWExecutable;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;

/**
 * Efface les URL's à peek-1// et peek-2.
 * 
 * @author user
 */
public class ExecTauxAssurance extends FWExecutable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FWUrlsStack m_monitoredStack = null;

    public ExecTauxAssurance(FWUrlsStack aStack) {
        m_monitoredStack = aStack;
    }

    /**
     * @see globaz.framework.utils.rules.FWExecutable#exec()
     */
    @Override
    protected void exec() {
        if (m_monitoredStack == null) {
            return;
        }
        if (m_monitoredStack.size() < 3) {
            return;
        }
        synchronized (m_monitoredStack) {
            boolean oldAutoexec = m_monitoredStack.setAutoexec(false); // autoexec
            // to
            // false,
            // so we
            // don't
            // have
            // side-effects
            FWUrl savedUrl = m_monitoredStack.pop(); // save the top
            m_monitoredStack.pop(); // remove -1
            m_monitoredStack.pop(); // remove -2
            m_monitoredStack.push(savedUrl); // push the saved url
            m_monitoredStack.setAutoexec(oldAutoexec); // restores stack's
            // autoexec
        }
    }

}

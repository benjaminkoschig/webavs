/**
 * 
 */
package globaz.amal.vb.process;

import java.util.HashMap;
import ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean;

/**
 * @author dhi
 * 
 */
public class AMRepriseFinAnneeViewBean extends JadeProcessAbstractViewBean {

    @Override
    public String getKeyProcess() {
        return "Amal.RepriseFinAnnee";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean#getProperties()
     */
    @Override
    public HashMap<String, String> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

}

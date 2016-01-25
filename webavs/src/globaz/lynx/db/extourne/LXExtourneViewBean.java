package globaz.lynx.db.extourne;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.lynx.db.operation.LXOperation;

public class LXExtourneViewBean extends LXExtourne implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LXOperation operationSrc = null;

    /**
     * Permet de retourner le type de l'opération source
     * 
     * @return
     * @throws Exception
     */
    public String getCsTypeOperartionSrc() throws Exception {

        retrieveOperationSrc();

        if (operationSrc != null) {
            return operationSrc.getCsTypeOperation();
        } else {
            return "";
        }
    }

    /**
     * Permet de charger l'opération source de l'extourne
     */
    private void retrieveOperationSrc() {
        if (!JadeStringUtil.isIntegerEmpty(getIdOperationSrc()) && operationSrc == null) {
            try {
                operationSrc = new LXOperation();
                operationSrc.setSession(getSession());
                operationSrc.setIdOperation(getIdOperationSrc());
                operationSrc.retrieve();

                if (operationSrc.hasErrors() || operationSrc.isNew()) {
                    operationSrc = null;
                    return;
                }
            } catch (Exception e) {
                // nothing
            }
        }
    }
}

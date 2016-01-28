/**
 *
 */
package ch.globaz.vulpecula.business.models.codesystem;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author jpa
 * 
 */
public class CodeSystemLibelleSearchSimpleModel extends JadeSearchSimpleModel {
    private String forLangue = "";

    public String getForLangue() {
        return forLangue;
    }

    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    @Override
    public Class<CodeSystemLibelleSimpleModel> whichModelClass() {
        return CodeSystemLibelleSimpleModel.class;
    }
}

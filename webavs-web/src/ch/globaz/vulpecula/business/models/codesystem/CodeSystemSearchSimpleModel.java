/**
 *
 */
package ch.globaz.vulpecula.business.models.codesystem;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author jpa
 * 
 */
public class CodeSystemSearchSimpleModel extends JadeSearchSimpleModel {
    private String forGroupe;

    public String getForGroupe() {
        return forGroupe;
    }

    public void setForGroupe(String forGroupe) {
        this.forGroupe = forGroupe;
    }

    @Override
    public Class<CodeSystemSimpleModel> whichModelClass() {
        return CodeSystemSimpleModel.class;
    }
}

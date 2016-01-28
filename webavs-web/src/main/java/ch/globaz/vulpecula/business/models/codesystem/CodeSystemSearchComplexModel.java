/**
 *
 */
package ch.globaz.vulpecula.business.models.codesystem;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author sel
 * 
 */
public class CodeSystemSearchComplexModel extends JadeSearchComplexModel {

    private String forGroupe = null;
    private String forLangue = null;

    public String getForGroupe() {
        return forGroupe;
    }

    public String getForLangue() {
        return forLangue;
    }

    public void setForGroupe(String forGroupe) {
        this.forGroupe = forGroupe;
    }

    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<CodeSystemComplexModel> whichModelClass() {
        return CodeSystemComplexModel.class;
    }

}

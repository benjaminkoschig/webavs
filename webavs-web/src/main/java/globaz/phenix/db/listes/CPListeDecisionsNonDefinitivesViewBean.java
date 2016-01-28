/**
 * class CPListeDecisionsAvecMiseEnCompteViewBean écrit le 19/01/05 par JPA
 * 
 * class ViewBean pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.listes.CPProcessListeDecisionsNonDefinitives;

public class CPListeDecisionsNonDefinitivesViewBean extends CPProcessListeDecisionsNonDefinitives implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Déclarations
    private String anneeDecision = null;
    private String id = null;

    // Constructeur
    public CPListeDecisionsNonDefinitivesViewBean() throws Exception {
        super();
    }

    public CPListeDecisionsNonDefinitivesViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("CP_MSG_0145"));
        }
    }

    // Méthodes BIPersistentObject
    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    // Getter
    @Override
    public String getAnneeDecision() {
        return anneeDecision;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void retrieve() throws Exception {
    }

    // Setter
    @Override
    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void update() throws Exception {
    }
}

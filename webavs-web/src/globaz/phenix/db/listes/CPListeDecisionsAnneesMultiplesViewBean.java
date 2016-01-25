/**
 * class CPListeAnneeEnDoubleViewBean écrit le 19/01/05 par JPA
 * 
 * class ViewBean
 * 
 * @author JPA
 **/
package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.listes.CPProcessListeDecisionsAnneesMultiples;

public class CPListeDecisionsAnneesMultiplesViewBean extends CPProcessListeDecisionsAnneesMultiples implements
        BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;
    private String idPassage = "";
    private String LibellePassage = "";

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("CP_MSG_0145"));
        }
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    // Méthodes obligadoire pour BIPersistentObject
    @Override
    public String getId() {
        return id;
    }

    // Getter et setter
    @Override
    public String getIdPassage() {
        return idPassage;
    }

    public String getLibellePassage() {
        return LibellePassage;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void setIdPassage(String string) {
        idPassage = string;
    }

    public void setLibellePassage(String string) {
        LibellePassage = string;
    }

    @Override
    public void update() throws Exception {
    }
}

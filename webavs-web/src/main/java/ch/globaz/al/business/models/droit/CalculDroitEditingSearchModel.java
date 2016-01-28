/**
 * 
 */
package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

/**
 * Classe modèle de recherche pour un droit calculé
 * 
 * @author pta
 * 
 */
public class CalculDroitEditingSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * recherche utilisateur
     */
    private String forUser = null;
    /**
     * recherche idDroit
     */
    private Collection<?> inIdDroit = null;

    public String getForUser() {
        return forUser;
    }

    public Collection<?> getInIdDroit() {
        return inIdDroit;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public void setInIdDroit(Collection<?> inIdDroit) {
        this.inIdDroit = inIdDroit;
    }

    @Override
    public Class<CalculDroitEditingModel> whichModelClass() {
        // TODO Auto-generated method stub
        return CalculDroitEditingModel.class;
    }

}

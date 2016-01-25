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
import globaz.phenix.process.listes.CPProcessListeAcompteCotisationAnnuelleDifferente;

public class CPListeAcompteCotisationAnnuelleDifferenteViewBean extends
        CPProcessListeAcompteCotisationAnnuelleDifferente implements BIPersistentObject, FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;
    private String idPassage = "";

    public CPListeAcompteCotisationAnnuelleDifferenteViewBean() throws Exception {
        super();
    }

    // Constructeurs
    public CPListeAcompteCotisationAnnuelleDifferenteViewBean(BProcess parent) throws Exception {
        super(parent);
    }

    // Méthodes BIPersistentObject
    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * @return
     */
    @Override
    public String getIdPassage() {
        return idPassage;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    /**
     * @param string
     */
    @Override
    public void setIdPassage(String string) {
        idPassage = string;
    }

    @Override
    public void update() throws Exception {
    }

}

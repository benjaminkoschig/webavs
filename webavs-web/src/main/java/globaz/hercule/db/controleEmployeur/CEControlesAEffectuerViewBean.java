package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.hercule.process.CEListeControlesAEffectuerProcess;

/**
 * @author hpe
 * @since Créé le 14 févr. 07
 */
public class CEControlesAEffectuerViewBean extends CEListeControlesAEffectuerProcess implements FWViewBeanInterface,
        BIPersistentObject {

    private static final long serialVersionUID = -8391526121818485527L;

    private String masseSalA = "";
    private String masseSalDe = "";

    public CEControlesAEffectuerViewBean() {
        // nothing
    }

    @Override
    public void add() throws Exception {
        // nothing
    }

    @Override
    public void delete() throws Exception {
        // nothing
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return
     */
    public String getMasseSalA() {
        return masseSalA;
    }

    /**
     * @return
     */
    public String getMasseSalDe() {
        return masseSalDe;
    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setId(String newId) {
        // nothing
    }

    /**
     * @param string
     */
    public void setMasseSalA(String string) {
        masseSalA = string;
    }

    /**
     * @param string
     */
    public void setMasseSalDe(String string) {
        masseSalDe = string;
    }

    @Override
    public void update() throws Exception {
        // nothing
    }

}

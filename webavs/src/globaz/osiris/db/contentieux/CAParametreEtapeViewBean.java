package globaz.osiris.db.contentieux;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.db.comptes.CASection;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAParametreEtapeViewBean extends CAParametreEtape implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CASection _section = null;

    /**
     * Constructor for CAParametreEtapeViewBean.
     */
    public CAParametreEtapeViewBean() {
        super();
    }

    public CAParametreEtapeViewBean(CASection element) {
        super();
        setSection(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIParametreEtape#getDateDeclenchement(APISection)
     */
    public String getDateDeclenchement() {
        return super.getDateDeclenchement(_section);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.db.contentieux.CAParametreEtape# getEvenementContentieuxPrecedent(CASection)
     */
    public CAEvenementContentieux getEvenementContentieuxPrecedent() {
        return super.getEvenementContentieuxPrecedent(_section);
    }

    void setSection(CASection element) {
        _section = element;
        if (_section != null) {
            super.setIdSection(_section.getIdSection());
        }
    }

}

package globaz.osiris.db.contentieux;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.osiris.db.comptes.CASection;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAParametreEtapeManagerListViewBean extends CAParametreEtapeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CASection _section = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAParametreEtapeViewBean(_section);
    }

    /**
     * Returns the _section.
     * 
     * @return CASection
     */
    CASection getSection() {
        return _section;
    }

    /**
     * Sets the _section.
     * 
     * @param _section
     *            The _section to set
     */
    public void setSection(CASection _section) {
        this._section = _section;
    }

}

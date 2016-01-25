package globaz.osiris.db.print;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.osiris.print.itext.list.CAProcessImpressionExtraitCompteAnnexe;

public class CAListExtraitCompteAnnexeViewBean extends CAProcessImpressionExtraitCompteAnnexe implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String descriptionAffilie = null;
    private String idExterneRole = null;

    public CAListExtraitCompteAnnexeViewBean() throws Exception {
        super();
    }

    /**
     * Returns the descriptionAffilie.
     * 
     * @return String
     */
    @Override
    public String getDescriptionAffilie() {
        return descriptionAffilie;
    }

    /**
     * Returns the idExterneRole.
     * 
     * @return String
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * Sets the descriptionAffilie.
     * 
     * @param descriptionAffilie
     *            The descriptionAffilie to set
     */
    @Override
    public void setDescriptionAffilie(String descriptionAffilie) {
        this.descriptionAffilie = descriptionAffilie;
    }

    /**
     * Sets the idExterneRole.
     * 
     * @param idExterneRole
     *            The idExterneRole to set
     */
    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }
}

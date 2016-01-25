package globaz.tucana.db.communs;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersSystemCode;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class TUCodeSystemeViewBean extends FWParametersSystemCode implements FWViewBeanInterface {

    // private String csCodeSysteme = new String();
    // private String codeUtilisateur = new String();
    // private String libelle = new String();

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for AICodeInfirmiteViewBean.
     */
    public TUCodeSystemeViewBean() {
        super();
    }

    /**
     * Returns the csCodeSysteme.
     * 
     * @return String
     */
    public String getCsCodeSysteme() {
        return getCurrentCodeUtilisateur().getIdCodeSysteme();
    }

    /**
     * Returns the csCodeUtilisateur.
     * 
     * @return String
     */
    public String getCsCodeUtilisateur() {
        return getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    /**
     * Returns the CodeLibelle.
     * 
     * @return String
     */
    @Override
    public String getLibelle() {
        return getCurrentCodeUtilisateur().getLibelle();
    }

}
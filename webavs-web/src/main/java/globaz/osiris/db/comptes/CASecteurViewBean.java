package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;

public class CASecteurViewBean extends CASecteur implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CASecteurViewBean() {
        super();
    }

    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise � jour du libell�
        this.setDescription(newDescription, "DE");
    }

    /*
     * Description dans la langue fournie Date de cr�ation : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise � jour du libell�
        this.setDescription(newDescription, "FR");
    }

    /*
     * Description dans la langue fournie Date de cr�ation : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise � jour du libell�
        this.setDescription(newDescription, "IT");
    }

}

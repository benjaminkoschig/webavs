package globaz.lynx.db.extourne;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.process.LXExtourneProcess;

public class LXExtourneProcessViewBean extends LXExtourneProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String idSection;

    private LXSection section = null;

    /**
     * Commentaire relatif au constructeur LXExtourneProcessViewBean.
     */
    public LXExtourneProcessViewBean() {
        super();
    }

    public String getIdFournisseur() throws Exception {
        return getSection().getIdFournisseur();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getIdSection() {
        return idSection;
    }

    public String getIdSociete() throws Exception {
        return getSection().getIdSociete();
    }

    public String getLibelle() throws Exception {
        return getOperation().getLibelle();
    }

    public String getMontant() throws Exception {
        return getOperation().getMontant();
    }

    /**
     * Charge et return le journal fournisseur.
     * 
     * @return
     */
    private LXSection getSection() throws Exception {
        if (section == null) {
            section = new LXSection();
            section.setSession(getSession());
            section.setIdSection(idSection);

            section.retrieve(getTransaction());

            if (section.hasErrors()) {
                throw new Exception(section.getErrors().toString());
            }

            if (section.isNew()) {
                throw new Exception(getSession().getLabel(""));
            }
        }

        return section;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

}

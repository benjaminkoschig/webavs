package globaz.osiris.db.interets;

import globaz.framework.bean.FWViewBeanInterface;

public class CAGenreInteretViewBean extends CAGenreInteret implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idExterneRubriqueEcran = new String();

    public CAGenreInteretViewBean() {
        super();
    }

    public String getIdExterneRubriqueEcran() {
        return idExterneRubriqueEcran;
    }

    public void setIdExterneRubriqueEcran(String idExterneRubriqueEcran) {
        this.idExterneRubriqueEcran = idExterneRubriqueEcran;
    }
}

package globaz.lynx.db.notedecreditlier;

import globaz.globall.db.BEntity;
import globaz.lynx.utils.LXNoteDeCreditUtil;

public class LXNoteDeCreditLierListViewBean extends LXNoteDeCreditLierManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public LXNoteDeCreditLierListViewBean() {
        super();
    }

    /**
     * @see globaz.lynx.db.notedecreditlier.LXNoteDeCreditLierManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new LXNoteDeCreditLierViewBean();
    }

    /**
     * Retourne la somme des montants des notes de credit de la section
     * 
     * @return
     * @throws Exception
     */
    public String getMontantRestant() throws Exception {
        return LXNoteDeCreditUtil.getMontantRestantNoteDeCredit(getSession(), getForIdSection(),
                getForIdOperationSrc(), null);
    }
}

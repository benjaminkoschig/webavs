package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.tucana.db.bouclement.access.TUDetail;
import globaz.tucana.db.bouclement.access.TUNoPassage;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Tue May 09 11:24:41 CEST 2006
 */
public class TUDetailViewBean extends TUDetail implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public TUBouclementViewBean bouclement = null;

    /** Table : TUBPDET */

    /**
     * Constructeur de la classe TUDetailViewBean
     */
    public TUDetailViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        bouclement = new TUBouclementViewBean();
        bouclement.setSession(getSession());
        bouclement.setId(getIdBouclement());
        bouclement.retrieve();
        if (bouclement.hasErrors()) {
            setBouclement(new TUBouclementViewBean());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.tucana.db.bouclement.access.TUDetail#_validate(globaz.globall. db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        super._validate(statement);
        if (!hasErrors()) {
            TUNoPassage passage = new TUNoPassage();
            passage.setSession(statement.getTransaction().getSession());
            passage.setAlternateKey(TUNoPassage.KEY_ALTERNATE_BOUCLEMENT);
            passage.setIdBouclement(getIdBouclement());
            passage.setCsApplication(getCsApplication());
            try {
                passage.retrieve();
            } catch (Exception e) {
                _addError(statement.getTransaction(),
                        getClass().getName().concat("._validate() : Error on load TUNoPassage"));
            }
            if (!passage.hasErrors()) {
                // lève un message si pas d'enregistrement trouvé
                if (passage.isNew()) {
                    _addError(statement.getTransaction(),
                            getClass().getName().concat("._validate() : No record TUNoPassage found"));
                } else {
                    setNoPassage(passage.getNoPassage());
                }
            }
        }

    }

    /**
     * Récupère un bouclement
     * 
     * @return
     */
    public TUBouclementViewBean getBouclement() {
        return bouclement;
    }

    /**
     * Modifie un bouclement
     * 
     * @param bouclement
     */
    public void setBouclement(TUBouclementViewBean bouclement) {
        this.bouclement = bouclement;
    }

}

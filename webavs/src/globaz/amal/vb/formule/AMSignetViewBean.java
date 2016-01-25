/**
 * 
 */
package globaz.amal.vb.formule;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import java.util.Arrays;
import java.util.Iterator;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleChamp;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleChampSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * @author LFO
 * 
 */
@SuppressWarnings("unchecked")
public class AMSignetViewBean extends BJadePersistentObjectViewBean {
    private SimpleChamp champ = null;

    // actionDetail = targetLocation + "='" + detailLink +
    // line.getIdChampFormule()+"&idFormule="+line.getIdFormule()+"'";
    // private String IdChampFormule = null;
    // private String IdFormule = null;

    /**
	 * 
	 */
    public AMSignetViewBean() {
        super();
        champ = new SimpleChamp();
    }

    public AMSignetViewBean(SimpleChamp champ) {
        super();
        this.champ = champ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // this.champformule = ENServiceLocator.getChampFormuleListService().create(this.champformule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public SimpleChamp getChamp() {
        return champ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return champ.getId();
    }

    public String getIdChampFormule() {
        return champ.getId();// this.IdChampFormule;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return new BSpy(champ.getSpy());
    }

    public boolean isNew() {

        if ((champ.getSpy() == null) || (champ.getSpy() == "")) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        SimpleChampSearch search = new SimpleChampSearch();
        search.setForIdChamp(getId());

        search = ENServiceLocator.getSimpleChampService().search(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            champ = (SimpleChamp) it.next();
        }
    }

    public void setChampformule(SimpleChamp champ) {
        this.champ = champ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        champ.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // this.champformule = AmalServiceLocator..update(this.formule);
        // this.champformule = ENServiceLocator.getChampFormuleListService().update(this.champformule);
    }
}

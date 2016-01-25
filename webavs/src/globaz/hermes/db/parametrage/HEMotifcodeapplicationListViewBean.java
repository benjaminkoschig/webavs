package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (22.10.2002 16:23:37)
 * 
 * @author: Administrator
 */
public class HEMotifcodeapplicationListViewBean extends HEMotifcodeapplicationManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEMotifCodeApplicationListViewBean.
     */
    public HEMotifcodeapplicationListViewBean() {
        super();
    }

    public String getIdCodeApplication(int pos) {
        HEMotifcodeapplication entity = (HEMotifcodeapplication) getEntity(pos);
        return entity.getIdCodeApplication();
        // return "idCodeApplication " + pos;
    }

    public String getIdCritereMotif(int pos) {
        HEMotifcodeapplication entity = (HEMotifcodeapplication) getEntity(pos);
        return entity.getIdCritereMotif();
        // return "idCritereMotif " + pos;
    }

    public String getIdMotif(int pos) {
        HEMotifcodeapplication entity = (HEMotifcodeapplication) getEntity(pos);
        return entity.getIdMotif();
        // return "idMotif " + pos;
    }

    public String getIdMotifCodeApplication(int pos) {
        HEMotifcodeapplication entity = (HEMotifcodeapplication) getEntity(pos);
        return entity.getIdMotifCodeApplication();
        // return "idMotifCodeApplication " + pos;
    }
}

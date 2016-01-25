/**
 * 
 */
package globaz.amal.vb.parametremodel;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMParametreModelViewBean extends BJadePersistentObjectViewBean {
    private ParametreModelComplex parametreModelComplex = null;

    public AMParametreModelViewBean() {
        super();
        parametreModelComplex = new ParametreModelComplex();
    }

    public AMParametreModelViewBean(ParametreModelComplex parametreModelComplex) {
        this();
        this.parametreModelComplex = parametreModelComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().create(parametreModelComplex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().delete(parametreModelComplex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return parametreModelComplex.getFormuleList().getId();
    }

    public ParametreModelComplex getParametreModelComplex() {
        return parametreModelComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if ((parametreModelComplex != null) && (parametreModelComplex.getSimpleParametreModel() != null)) {
            return new BSpy(parametreModelComplex.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // this.parametreModelComplex = AmalServiceLocator.getParametreModelService().read(
        // this.parametreModelComplex.getId());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        parametreModelComplex.getFormuleList().setId(newId);
    }

    public void setParametreModelComplex(ParametreModelComplex parametreModelComplex) {
        this.parametreModelComplex = parametreModelComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().update(parametreModelComplex);
    }

}

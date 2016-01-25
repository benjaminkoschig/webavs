package globaz.libra.vb.utilisateurs;

import globaz.globall.db.BIPersistentObject;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.libra.utils.LIJadeUserService;
import globaz.libra.vb.LIAbstractPersistentObjectListViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author hpe
 * 
 */
public class LIUtilisateursFXListViewBean extends LIAbstractPersistentObjectListViewBean {
    private String forIdUser = null;

    private String forVisa = null;

    private String likeFirstName = null;

    private String likeLastName = null;

    protected Collection utilisateurs = null;

    /**
     * Constructeur de la classe AIUtilisateurListViewBean
     */
    public LIUtilisateursFXListViewBean() {
        super();
        utilisateurs = new ArrayList();
    }

    /**
     * Constructeur de la classe AIUtilisateurListViewBean
     * 
     * @param _jadeUsers
     */
    public LIUtilisateursFXListViewBean(JadeUser[] _jadeUsers) {
        this();
        for (int i = 0; i < _jadeUsers.length; i++) {
            utilisateurs.add(new LIUtilisateursFXViewBean(_jadeUsers[i]));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ai.vb.AIAbstractListViewBean#canDoNext()
     */
    @Override
    public boolean canDoNext() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ai.vb.AIAbstractListViewBean#canDoPrev()
     */
    @Override
    public boolean canDoPrev() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        if (forIdUser != null) {
            utilisateurs = LIJadeUserService.getInstance().findUtilisateursForIdUserLikeAsCollection(forIdUser);
        } else if ((forVisa != null && !"".equals(forVisa)) && (likeFirstName != null && !"".equals(likeFirstName))
                && (likeLastName != null && !"".equals(likeLastName))) {
            utilisateurs = LIJadeUserService.getInstance().findUtilisateursAsCollection(forVisa, likeFirstName,
                    likeLastName);
        } else if ((likeFirstName != null && !"".equals(likeFirstName))
                && (likeLastName != null && !"".equals(likeLastName))) {
            utilisateurs = LIJadeUserService.getInstance().findUtilisateursAsCollection(likeFirstName, likeLastName);
        } else if (forVisa != null && !"".equals(forVisa)) {
            utilisateurs = LIJadeUserService.getInstance().findUtilisateursForVisaLikeAsCollection(forVisa);
        } else if (likeFirstName != null && !"".equals(likeFirstName)) {
            utilisateurs = LIJadeUserService.getInstance().findUtilisateursLikeFirstNameLikeAsCollection(likeFirstName);
        } else if (likeLastName != null && !"".equals(likeLastName)) {
            utilisateurs = LIJadeUserService.getInstance().findUtilisateursLikeLastNameLikeAsCollection(likeLastName);
        } else {
            utilisateurs = LIJadeUserService.getInstance().getAllUtilisateursAsCollection();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#findNext()
     */
    @Override
    public void findNext() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#findPrev()
     */
    @Override
    public void findPrev() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return (BIPersistentObject) (idx <= utilisateurs.size() ? ((ArrayList) utilisateurs).get(idx) : null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ai.vb.AIAbstractListViewBean#getCount()
     */
    @Override
    public int getCount() throws Exception {
        return utilisateurs.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ai.vb.AIAbstractListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject getEntity(int idx) {
        return idx <= utilisateurs.size() ? (BIPersistentObject) ((ArrayList) utilisateurs).get(idx) : null;
    }

    /**
     * @return
     */
    public String getForIdUser() {
        return forIdUser;
    }

    /**
     * @return
     */
    public String getForVisa() {
        return forVisa;
    }

    public String getLikeFirstName() {
        return likeFirstName;
    }

    public String getLikeLastName() {
        return likeLastName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ai.vb.AIAbstractListViewBean#getOffset()
     */
    @Override
    public int getOffset() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWListViewBeanInterface#getSize()
     */
    @Override
    public int getSize() {
        return utilisateurs.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#iterator()
     */
    @Override
    public Iterator iterator() {
        return utilisateurs.iterator();
    }

    /**
     * @param string
     */
    public void setForIdUser(String string) {
        forIdUser = string;
    }

    /**
     * @param string
     */
    public void setForVisa(String string) {
        forVisa = string;
    }

    public void setLikeFirstName(String likeFirstName) {
        this.likeFirstName = likeFirstName;
    }

    public void setLikeLastName(String likeLastName) {
        this.likeLastName = likeLastName;
    }

}

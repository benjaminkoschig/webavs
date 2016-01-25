package globaz.libra.vb.utilisateurs;

import globaz.globall.api.BIEntity;
import globaz.globall.db.BSpy;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.utils.LIJadeUserService;
import globaz.libra.vb.LIAbstractPersistentObjectViewBean;

/**
 * @author hpe
 * 
 */
public class LIUtilisateursFXViewBean extends LIAbstractPersistentObjectViewBean implements BIEntity, Comparable {
    private JadeUser jadeUser = null;

    /**
     * Constructeur de la Classe AIUtilisateurViewBean
     */
    public LIUtilisateursFXViewBean() {
        jadeUser = new JadeUser();
    }

    /**
     * Constructeur de la Classe AIUtilisateurViewBean
     * 
     * @param _jadeUser
     */
    public LIUtilisateursFXViewBean(JadeUser _jadeUser) {
        super();
        jadeUser = _jadeUser != null ? _jadeUser : new JadeUser();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object obj) {
        return obj instanceof LIUtilisateursFXViewBean ? getVisa().toUpperCase().compareTo(
                ((LIUtilisateursFXViewBean) obj).getVisa().toUpperCase()) : -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {

    }

    /**
     * Renvoie la methode de cryptage du mot de passe.
     * 
     * @return
     */
    public String getCryptMethod() {
        return jadeUser.getCryptMethod();
    }

    /**
     * Renvoie l'adresse E-mail de l'utilisateur.
     * 
     * @return l'adresse E-mail de l'utilisateur.
     */
    public String getEmail() {
        return jadeUser.getEmail();
    }

    /**
     * Renvoie le prénom de l'utilisateur.
     * 
     * @return le prénom de l'utilisateur.
     */
    public String getFirstname() {
        return jadeUser.getFirstname();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return jadeUser.getIdUser();
    }

    /**
     * Renvoie l'id de l'utilisateur. (unique, non modifiable).
     * 
     * @return l'id de l'utilisateur.
     */
    public String getIdUser() {
        return jadeUser.getIdUser();
    }

    /**
     * Renvoie les initiales de l'utilisateur
     */
    public String getInitiales() {
        String initialePrenom = "";
        String InitialeNom = "";
        if (!JadeStringUtil.isEmpty(getFirstname())) {
            initialePrenom = getFirstname().substring(0, 1).toUpperCase();
        }
        if (!JadeStringUtil.isEmpty(getLastname())) {
            InitialeNom = getLastname().substring(0, 1).toUpperCase();
        }

        return initialePrenom + InitialeNom;
    }

    /**
     * @return
     */
    protected JadeUser getJadeUser() {
        return jadeUser;
    }

    /**
     * Renvoie la langue de l'utilisateur.
     * 
     * @return
     */
    public String getLanguage() {
        return jadeUser.getLanguage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIEntity#getLastModifiedDate()
     */
    @Override
    public String getLastModifiedDate() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIEntity#getLastModifiedTime()
     */
    @Override
    public String getLastModifiedTime() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIEntity#getLastModifiedUser()
     */
    @Override
    public String getLastModifiedUser() {
        return null;
    }

    /**
     * Renvoie le nom de l'utilisateur.
     * 
     * @return le nom de l'utilisateur.
     */
    public String getLastname() {
        return jadeUser.getLastname();
    }

    /**
     * Renvoie le nom suivi du prénom
     */
    public String getNomPrenom() {
        return getLastname() + " " + getFirstname();
    }

    /**
     * Renvoie le mot de passe de l'utilisateur.
     * 
     * @return le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return jadeUser.getPassword();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.ai.vb.AIAbstractPersistantObject#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Renvoie le visa de l'utilisateur (unique, modifiable).
     * 
     * @return
     */
    public String getVisa() {
        return jadeUser.getVisa();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIEntity#isNew()
     */
    @Override
    public boolean isNew() {
        // HACK on renvoie juste le fait que l'id existe.
        return JadeStringUtil.isIntegerEmpty(jadeUser.getIdUser());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        jadeUser = LIJadeUserService.getInstance().loadByUserId(jadeUser.getIdUser());
    }

    /**
     * Renvoie l'adresse E-mail de l'utilisateur.
     * 
     * @return l'adresse E-mail de l'utilisateur.
     */
    public void setEmail(String email) {
        jadeUser.setEmail(email);
    }

    /**
     * Renvoie le prénom de l'utilisateur.
     * 
     * @return le prénom de l'utilisateur.
     */
    public void setFirstname(String firstName) {
        jadeUser.setFirstname(firstName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        jadeUser.setIdUser(newId);
    }

    /**
     * Renvoie l'id de l'utilisateur. (unique, non modifiable).
     * 
     * @return l'id de l'utilisateur.
     */
    public void setIdUser(String idUser) {
        jadeUser.setIdUser(idUser);
    }

    /**
     * Renvoie la langue de l'utilisateur.
     * 
     * @return
     */
    public void setLanguage(String language) {
        jadeUser.setLanguage(language);
    }

    /**
     * Renvoie le nom de l'utilisateur.
     * 
     * @return le nom de l'utilisateur.
     */
    public void setLastname(String lastName) {
        jadeUser.setLastname(lastName);
    }

    /**
     * Renvoie le mot de passe de l'utilisateur.
     * 
     * @return le mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        jadeUser.setPassword(password);
    }

    /**
     * Renvoie le visa de l'utilisateur (unique, modifiable).
     * 
     * @return
     */
    public void setVisa(String visa) {
        jadeUser.setVisa(visa);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }

}

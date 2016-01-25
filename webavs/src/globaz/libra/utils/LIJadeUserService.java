package globaz.libra.utils;

import globaz.framework.voloader.FWVOUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.admin.user.service.JadeUserDetailService;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.log.JadeLogger;
import globaz.libra.vb.utilisateurs.LIUtilisateursFXListViewBean;
import globaz.libra.vb.utilisateurs.LIUtilisateursFXViewBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hpe
 * 
 */
public class LIJadeUserService {

    // Correspond à une heure en mls
    private static long CACHE_LIFE_TIME = 3600000l;

    private static LIJadeUserService instance = null;

    private static final String SUFFIX_UTILISATEUR_OLD = "_OLD";

    private static final String SUFFIX_UTILISATEUR_TRANSFERT = "_TRF";

    /**
     * Instance de la classe AIJadeUserService
     */
    public static LIJadeUserService getInstance() {
        if (instance == null) {
            instance = new LIJadeUserService();
        }
        instance.synchronizeUsers();
        return instance;
    }

    private JadeUser[] allUsers = null;

    // Time en mls de la prochaine synchronisation
    private long nextSynchronization = 0l;

    private JadeUserDetailService userDetailService = null;

    private JadeUserService userService = null;

    /**
     * Constructeur de AIJadeUserService
     */
    private LIJadeUserService() {
        super();
        userService = JadeAdminServiceLocatorProvider.getLocator().getUserService();
        userDetailService = JadeAdminServiceLocatorProvider.getLocator().getUserDetailService();
    }

    private JadeUser createPseudoUser(String idUser, String suffix) {
        // HACK: on construit un pseudo utilisateur Jade juste pour l'affichage
        JadeUser jadeUser = new JadeUser();

        /*
         * ATTENTION: il faut éviter à tout prix que le visa ou l'identifiant d'un pseudo-utilisateur se retrouve dans
         * la base sans son suffixe. C'est pourquoi ici on renseigne les champs identifiant et visa sans omettre le
         * suffixe. Dans ce cas, si l'identifiant est copié dans une autre entité (ce qui peut arriver) et que cette
         * entité est sauvée en base, son chargement n'entraînera pas d'erreur car l'utilisateur sera correctement
         * interprété comme un pseudo-utilisateur.
         */
        jadeUser.setIdUser(idUser);
        jadeUser.setVisa(idUser);

        /*
         * Pour l'affichage du nom et prénom du pseudo-utilisateur par contre on décompose. L'idéal serait de mettre un
         * label multilingue mais nous n'avons pas accès à la session ici.
         */
        jadeUser.setFirstname(toIdUtilisateurJade(idUser));
        jadeUser.setLastname(suffix);

        return jadeUser;
    }

    private JadeUser createUserOld(String idUser) {
        return createPseudoUser(idUser, SUFFIX_UTILISATEUR_OLD);
    }

    private JadeUser createUserTransfert(String idUser) {
        return createPseudoUser(idUser, SUFFIX_UTILISATEUR_TRANSFERT);
    }

    public JadeUserDetail[] findUserDetailForIdUser(String idUser) throws Exception {
        return userDetailService.findForIdUser(idUser);
    }

    /**
     * Méthode qui charge une liste de JadeUser par idUser (like)
     * 
     * @param forIdUser
     * @return
     * @throws Exception
     */
    public JadeUser[] findUserForIdUserLike(String forIdUser) throws Exception {
        return userService.findUsersForIdUserLike(forIdUser);
    }

    /**
     * Méthode qui charge une liste de JadeUser par visa (like)
     * 
     * @param forVisa
     * @return
     * @throws Exception
     */
    public JadeUser[] findUserForVisaLike(String forVisa) throws Exception {
        return userService.findUsersForVisaLike(forVisa);
    }

    // /**
    // * @param idUser
    // * @return
    // */
    // public String getUserCsLangue(String idUser) {
    // String userLangue = getUserLangue(idUser);
    // if (userLangue.equalsIgnoreCase("FR"))
    // return IConstantes.CS_FRANCAIS;
    // else if (userLangue.equalsIgnoreCase("DE"))
    // return IConstantes.CS_ALLEMAND;
    // else
    // return IConstantes.CS_ITALIEN;
    // }

    /**
     * Méthode qui charge une liste de JadeUser par prénom et nom
     * 
     * @param likeFirstName
     * @param likeLastName
     * @return
     * @throws Exception
     */
    public JadeUser[] findUserLike(String likeFirstName, String likeLastName) throws Exception {
        Map map = new HashMap();
        map.put(JadeUserService.CR_FOR_FIRSTNAME_LIKE, likeFirstName);
        map.put(JadeUserService.CR_FOR_LASTNAME_LIKE, likeLastName);
        return userService.findUsersForCritere(map);
    }

    /**
     * Méthode qui charge une liste de JadeUser par visa, prénom et nom
     * 
     * @param forVisa
     * @param likeFirstName
     * @param likeLastName
     * @return
     * @throws Exception
     */
    public JadeUser[] findUserLike(String forVisa, String likeFirstName, String likeLastName) throws Exception {
        Map map = new HashMap();
        map.put(JadeUserService.CR_FOR_VISA_LIKE, forVisa);
        map.put(JadeUserService.CR_FOR_FIRSTNAME_LIKE, likeFirstName);
        map.put(JadeUserService.CR_FOR_LASTNAME_LIKE, likeLastName);
        return userService.findUsersForCritere(map);
    }

    /**
     * Méthode qui charge une liste de JadeUser par prenom (like)
     * 
     * @param likeFirstName
     * @return
     * @throws Exception
     */
    public JadeUser[] findUserLikeFirstNameLike(String likeFirstName) throws Exception {
        Map map = new HashMap();
        map.put(JadeUserService.CR_FOR_FIRSTNAME_LIKE, likeFirstName);
        return userService.findUsersForCritere(map);
    }

    /**
     * Méthode qui charge une liste de JadeUser par nom (like)
     * 
     * @param likeLastName
     * @return
     * @throws Exception
     */
    public JadeUser[] findUserLikeLastNameLike(String likeLastName) throws Exception {
        Map map = new HashMap();
        map.put(JadeUserService.CR_FOR_LASTNAME_LIKE, likeLastName);
        return userService.findUsersForCritere(map);
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par prénom et nom
     * 
     * @param likeFirstName
     * @param likeLastName
     * @return
     * @throws Exception
     */
    public Collection findUtilisateursAsCollection(String likeFirstName, String likeLastName) throws Exception {
        return getUtilisateurCollection(findUserLike(likeFirstName, likeLastName));
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par visa, prénom et nom
     * 
     * @param forVisa
     * @param likeFirstName
     * @param likeLastName
     * @return
     * @throws Exception
     */
    public Collection findUtilisateursAsCollection(String forVisa, String likeFirstName, String likeLastName)
            throws Exception {
        return getUtilisateurCollection(findUserLike(forVisa, likeFirstName, likeLastName));
    }

    /**
     * Méthode qui charge un LIUtilisateursFXListViewBean de tous les utilisateurs par IdUser (like)
     * 
     * @param forIdUser
     * @return
     * @throws Exception
     */
    public LIUtilisateursFXListViewBean findUtilisateursForIdUserLike(String forIdUser) throws Exception {
        return new LIUtilisateursFXListViewBean(findUserForIdUserLike(forIdUser));
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par idUser
     * 
     * @param forIdUser
     * @return
     * @throws Exception
     */
    public Collection findUtilisateursForIdUserLikeAsCollection(String forIdUser) throws Exception {
        return getUtilisateurCollection(findUserForIdUserLike(forIdUser));
    }

    /**
     * Méthode qui charge un LIUtilisateursFXListViewBean de tous les utilisateurs par visa (like)
     * 
     * @param forVisa
     * @return
     * @throws Exception
     */
    public LIUtilisateursFXListViewBean findUtilisateursForVisaLike(String forVisa) throws Exception {
        return new LIUtilisateursFXListViewBean(findUserForVisaLike(forVisa));
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par visa
     * 
     * @param forVisa
     * @return
     * @throws Exception
     */
    public Collection findUtilisateursForVisaLikeAsCollection(String forVisa) throws Exception {
        return getUtilisateurCollection(findUserForVisaLike(forVisa));
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par prenom
     * 
     * @param likeFirstName
     * @return
     * @throws Exception
     */
    public Collection findUtilisateursLikeFirstNameLikeAsCollection(String likeFirstName) throws Exception {
        return getUtilisateurCollection(findUserLikeFirstNameLike(likeFirstName));
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par nom
     * 
     * @param likeLastName
     * @return
     * @throws Exception
     */
    public Collection findUtilisateursLikeLastNameLikeAsCollection(String likeLastName) throws Exception {
        return getUtilisateurCollection(findUserLikeLastNameLike(likeLastName));
    }

    /**
     * Méthode qui charge une liste de JadeUser
     * 
     * @return
     * @throws Exception
     */
    public JadeUser[] getAllUsers() throws Exception {
        JadeUser[] copiedUser = new JadeUser[allUsers.length];
        for (int i = 0; i < copiedUser.length; i++) {
            copiedUser[i] = new JadeUser();
            try {
                FWVOUtil.copyProperties(allUsers[i], copiedUser[i]);
            } catch (Exception e) {
                JadeLogger.warn(this, "Exception during cloning entity : Exception : " + e.getClass().getName()
                        + " - Message : " + e.getMessage());
            }

        }
        return copiedUser;
    }

    /**
     * Méthode qui charge un LIUtilisateursFXListViewBean de tous les utilisateurs
     * 
     * @return
     * @throws Exception
     */
    public LIUtilisateursFXListViewBean getAllUtilisateurs() throws Exception {
        return new LIUtilisateursFXListViewBean(getAllUsers());
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs
     * 
     * @return
     * @throws Exception
     */
    public Collection getAllUtilisateursAsCollection() throws Exception {
        return getUtilisateurCollection(getAllUsers());
    }

    /**
     * Méthode qui retourne l'id de l'utilisateur (KUSER)
     * 
     * @param visa
     * @param session
     * @return
     */
    public String getUserId(String visa) {
        return loadByVisa(visa).getIdUser();
    }

    /**
     * Méthode qui retourne la langue de l'utilisateur (FLANGUAGE)
     * 
     * @param idUser
     * @return
     */
    public String getUserLangue(String idUser) {
        return loadByUserId(idUser).getLanguage();
    }

    /**
     * Méthode qui retourne le nom de l'utilisateur (FLASTNAME)
     * 
     * @param idUser
     * @return
     */
    public String getUserNom(String idUser) {
        return loadByUserId(idUser).getLastname();
    }

    /**
     * Méthode qui retourne le prénom de l'utilisateur (FFIRSTNAME)
     * 
     * @param idUser
     * @return
     */
    public String getUserPrenom(String idUser) {
        return loadByUserId(idUser).getFirstname();
    }

    // /**
    // * Méthode qui charge une Collection de tous les utilisateurs par une
    // liste
    // * de JadeUserDetail
    // *
    // * @param users
    // * @return
    // */
    // private Collection getUtilisateurDetailCollection(JadeUserDetail[] users)
    // {
    // Collection theUsers = new ArrayList();
    // for (int i = 0; i < users.length; i++) {
    // theUsers.add(new AIUtilisateurDetailViewBean(users[i]));
    // }
    // return theUsers;
    // }

    /**
     * Méthode qui retourne le visa de l'utilisateur (FVISA)
     * 
     * @param idUser
     * @return
     */
    public String getUserVisa(String idUser) {
        return loadByUserId(idUser).getVisa();
    }

    /**
     * Méthode qui charge une Collection de tous les utilisateurs par une liste de JadeUser
     * 
     * @param users
     * @return
     */
    private Collection getUtilisateurCollection(JadeUser[] users) {
        Collection theUsers = new ArrayList();
        for (int i = 0; i < users.length; i++) {
            theUsers.add(new LIUtilisateursFXViewBean(users[i]));
        }
        return theUsers;
    }

    /**
     * Retourne vrai si l'id utilisateur transmis en argument est un utilisateur issu d'une reprise, c'est-à-dire qui
     * n'est pas un vrai JadeUser.
     * 
     * @param idUtilisateur
     *            l'id utilisateur a vérifier, par exemple 'vre'
     * 
     * @return vrai si l'id utilisateur est issu d'une reprise, pas un vrai JadeUser
     */
    public boolean isIdUtilisateurOld(String idUtilisateur) {
        return idUtilisateur != null && idUtilisateur.trim().endsWith(SUFFIX_UTILISATEUR_OLD);
    }

    // public LIUtilisateursFXViewBean loadUtilisateurDetail(String userId,
    // String key) throws Exception {
    // return new LIUtilisateursFXViewBean(loadUserDetail(userId, key));
    // }

    /**
     * Retourne vrai si l'id utilisateur transmis en argument est un utilisateur issu d'un transfert, c'est-à-dire qui
     * n'est pas un vrai JadeUser.
     * 
     * @param idUtilisateur
     *            l'id utilisateur a vérifier, par exemple 'vre'
     * 
     * @return vrai si l'id utilisateur est issu d'un transfert, pas un vrai JadeUser
     */
    public boolean isIdUtilisateurTransfert(String idUtilisateur) {
        return idUtilisateur != null && idUtilisateur.trim().endsWith(SUFFIX_UTILISATEUR_TRANSFERT);
    }

    /**
     * Méthode qui charge un LIUtilisateursFXViewBean par l'idUser
     * 
     * @param idUser
     * @return
     */
    public LIUtilisateursFXViewBean loadAIUtilisateurByIdUser(String idUser) {
        return new LIUtilisateursFXViewBean(loadByUserId(idUser));
    }

    // public LIUtilisateursFXListViewBean
    // findUtilisateursDetailForIdUser(String forIdUser) throws Exception {
    // return new
    // LIUtilisateursFXListViewBean(findUserDetailForIdUser(forIdUser));
    // }

    // /**
    // * Méthode qui charge une Collection de tous les utilisateurs par idUser
    // *
    // * @param forIdUser
    // * @return
    // * @throws Exception
    // */
    // public Collection findUtilisateursDetailForIdUserLikeAsCollection(String
    // forIdUser) throws Exception {
    // return
    // getUtilisateurDetailCollection(findUserDetailForIdUser(forIdUser));
    // }

    /**
     * Méthode qui charge un LIUtilisateursFXViewBean par visa
     * 
     * @param visa
     * @return
     */
    public LIUtilisateursFXViewBean loadAIUtilisateurByIdVisa(String visa) {
        return new LIUtilisateursFXViewBean(loadByVisa(visa));
    }

    /**
     * Méthode qui charge un JadeUser par l'idUser
     * 
     * @param isUser
     * @return
     */
    public JadeUser loadByUserId(String idUser) {
        JadeUser jadeUser = null;
        if (isIdUtilisateurTransfert(idUser)) {
            jadeUser = createUserTransfert(idUser);
        } else if (isIdUtilisateurOld(idUser)) {
            jadeUser = createUserOld(idUser);
        } else {
            for (int i = 0; i < allUsers.length && jadeUser == null; i++) {
                if (allUsers[i].getIdUser().trim().toUpperCase().equals(idUser.trim().toUpperCase())) {
                    jadeUser = allUsers[i];
                }
            }
        }
        JadeUser copiedUser = new JadeUser();
        if (jadeUser != null) {
            try {
                FWVOUtil.copyProperties(jadeUser, copiedUser);
            } catch (Exception e) {
                JadeLogger.warn(this, "Exception during cloning entity : Exception : " + e.getClass().getName()
                        + " - Message : " + e.getMessage());
            }
        }
        return copiedUser;
    }

    /**
     * Méthode qui charge un JadeUser par le visa
     * 
     * @param visa
     * @return
     */
    public JadeUser loadByVisa(String visa) {
        JadeUser jadeUser = null;

        if (isIdUtilisateurTransfert(visa)) {
            jadeUser = createUserTransfert(visa);
        } else if (isIdUtilisateurOld(visa)) {
            jadeUser = createUserOld(visa);
        } else {
            try {
                for (int i = 0; i < allUsers.length && jadeUser == null; i++) {
                    if (allUsers[i].getVisa().trim().toUpperCase().equals(visa.trim().toUpperCase())) {
                        jadeUser = allUsers[i];
                    }
                }
            } catch (Exception e) {
                JadeCodingUtil.catchException(this, "getUserLangue", e);
            }
        }
        JadeUser copiedUser = new JadeUser();
        if (jadeUser != null) {
            try {
                FWVOUtil.copyProperties(jadeUser, copiedUser);
            } catch (Exception e) {
                JadeLogger.warn(this, "Exception during cloning entity : Exception : " + e.getClass().getName()
                        + " - Message : " + e.getMessage());
            }
        }
        return copiedUser;
    }

    public JadeUserDetail loadUserDetail(String idUser, String key) throws Exception {
        return userDetailService.load(idUser, key);
    }

    /**
	 * 
	 */
    private void synchronizeUsers() {
        if (allUsers == null || System.currentTimeMillis() > nextSynchronization) {
            nextSynchronization = System.currentTimeMillis() + CACHE_LIFE_TIME;
            try {
                allUsers = userService.findAllUsers();
            } catch (Exception e) {
                JadeLogger.error(this, "User synchronization problem ! Exception : " + e.getClass().getName()
                        + " -  Message : " + e.getMessage());
            }
        }

    }

    /**
     * Transforme un pseudo-id utilisateur de transfert ou un utilisateur issu d'une reprise en id utilisateur Jade.
     * 
     * @param idUtilisateur
     *            un pseudo-id utilisateur de transfert ou de reprise.
     * 
     * @return un id utilisateur de type Jade (sans le suffixe de transfert)
     */
    public String toIdUtilisateurJade(String idUtilisateur) {
        if (isIdUtilisateurTransfert(idUtilisateur)) {
            return idUtilisateur.substring(0, idUtilisateur.indexOf(SUFFIX_UTILISATEUR_TRANSFERT));
        }
        if (isIdUtilisateurOld(idUtilisateur)) {
            return idUtilisateur.substring(0, idUtilisateur.indexOf(SUFFIX_UTILISATEUR_OLD));
        }

        return idUtilisateur;
    }

    /**
     * Transforme un id utilisateur en un pseudo-id utilisateur tel que la méthode {@link #isIdUtilisateurOld(String)
     * isIdUtilisateurOld} retournera vrai.
     * 
     * @param idUtilisateur
     *            l'id utilisateur à transformer
     * 
     * @return un pseudo-id utilisateur (à vrai dire, l'id utilisateur avec un préfixe).
     */
    public String toIdUtilisateurOld(String idUtilisateurJade) {
        if (!isIdUtilisateurOld(idUtilisateurJade)) {
            return idUtilisateurJade.trim() + SUFFIX_UTILISATEUR_OLD;
        }

        return idUtilisateurJade;
    }

    /**
     * Transforme un id utilisateur en un pseudo-id utilisateur tel que la méthode
     * {@link #isIdUtilisateurTransfert(String) isIdUtilisateurTransfert} retournera vrai.
     * 
     * @param idUtilisateur
     *            l'id utilisateur à transformer
     * 
     * @return un pseudo-id utilisateur (à vrai dire, l'id utilisateur avec un préfixe).
     */
    public String toIdUtilisateurTransfert(String idUtilisateurJade) {
        if (!isIdUtilisateurTransfert(idUtilisateurJade)) {
            return idUtilisateurJade.trim() + SUFFIX_UTILISATEUR_TRANSFERT;
        }

        return idUtilisateurJade;
    }
}

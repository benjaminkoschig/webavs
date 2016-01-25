package globaz.osiris.db.comptes;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 10:10:35)
 * 
 * @author: Administrator
 */
public class CARole extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FX_ELEMENT_PREFIX = "osiris.comptes.roles.";

    private java.lang.String idRole = new String();

    private java.lang.String idTraduction = new String();

    private PATraductionHelper trLibelles = null;

    // code systeme

    /**
     * Retourne la liste des ids des roles pour lesquels l'utilisateur actuellement connecté à le droit de lecture.
     * 
     * @param session
     *            la session a utiliser pour retrouver l'utilisateur courant.
     * @return la liste des ids séparés par des ',' ou -1 s'il n'y a pas de roles permis pour l'utilisateur courant.
     */
    public static final String listeIdsRolesPourUtilisateurCourant(BSession session) throws Exception {
        SortedMap<Integer, CARole> roles = CARole.rolesPourUtilisateurCourant(session);

        if (roles.isEmpty()) {
            // s'il n'y a pas de roles permis pour l'utilisateur courant, on met
            // un id bidon qui va annuler la requete
            return "-1";
        } else {
            StringBuffer retValue = new StringBuffer();

            for (Iterator<CARole> rolesIter = roles.values().iterator(); rolesIter.hasNext();) {
                if (retValue.length() > 0) {
                    retValue.append(',');
                }

                retValue.append(rolesIter.next().getIdRole());
            }

            return retValue.toString();
        }
    }

    /**
     * De tous les rôles définis dans l'application, retourne l'ensemble de ceux pour lesquels l'utilisateur
     * actuellement connecté à {@link BSession#hasRight(java.lang.String, java.lang.String) a le droit de lecture}.
     * 
     * @param session
     *            la session a utiliser pour retrouver l'utilisateur et ses droits.
     * @return un ensemble d'instances de CARole, jamais nul, peut-être vide.
     */
    public static final SortedMap /* CARole */<Integer, CARole> rolesPourUtilisateurCourant(BSession session)
            throws Exception {
        SortedMap<Integer, CARole> retValue = new TreeMap<Integer, CARole>();
        CARoleManager rolesMgr = new CARoleManager();

        rolesMgr.setSession(session);
        rolesMgr.find();

        for (int id = 0; id < rolesMgr.size(); ++id) {
            CARole role = (CARole) rolesMgr.get(id);

            if (session.hasRight(CARole.FX_ELEMENT_PREFIX + role.getIdRole(), FWSecureConstants.READ)) {
                retValue.put(new Integer(id), role);
            }
        }

        return retValue;
    }

    /**
     * Commentaire relatif au constructeur CARole
     */
    public CARole() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().add(transaction);
    }

    /**
     * Après supression
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
    }

    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        trLibelles = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CARROLP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRole = statement.dbReadNumeric("IDROLE");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDROLE", this._dbWriteNumeric(statement.getTransaction(), getIdRole(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDROLE", this._dbWriteNumeric(statement.getTransaction(), getIdRole(), "idRole"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription() {
        // Description dans la langue de l'utilisateur
        // return getDescription(null);
        return this.getDescription(getSession().getIdLangueISO());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        return s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.12.2001 11:02:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    /**
     * Getter
     */
    public java.lang.String getIdRole() {
        return idRole;
    }

    @Override
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 14:57:23)
     * 
     * @return globaz.norma.db.fondation.PATraductionHelper
     */
    private PATraductionHelper getTraductionHelper() {
        if (trLibelles == null) {
            try {
                trLibelles = new PATraductionHelper(this);
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return trLibelles;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(java.lang.String newDescription) throws Exception {
        this.setDescription(newDescription, null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    /**
     * Setter
     */
    public void setIdRole(java.lang.String newIdRole) {
        idRole = newIdRole;
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }
}

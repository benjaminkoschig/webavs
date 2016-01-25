package globaz.norma.db.fondation;

import globaz.globall.api.BITransaction;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (18.12.2001 14:51:06)
 * 
 * @author: Administrator
 */
public class PATraductionManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * Getter
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Setter
     */

    public java.lang.String forIdTraduction;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "PMTRADP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdTraduction().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDTRADUCTION=" + _dbWriteNumeric(statement.getTransaction(), getForIdTraduction());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new PATraduction();
    }

    /**
     * Supprimer tous les libell�s Date de cr�ation : (20.12.2001 13:31:39)
     * 
     * @param trans
     *            globaz.norma.db.interfaceext.fondation.IntTranslatable
     */
    public void deleteLibelleTraduction(IntTranslatable trans, BITransaction transaction) throws Exception {
        String _id = trans.getIdTraduction();

        // V�rifier si l'id traduction est diff�rent de z�ro (rien � d�truire)
        if (JadeStringUtil.isBlank(_id)) {
            return;
        }

        // Si la transaction n'est pas active, on refuse la mise � jour
        if (!transaction.isOpened()) {
            throw new Exception("Transaction must be opened to delete translation no " + _id);
        }

        // V�rifier si l'id traduction est diff�rente
        if (!_id.equals(forIdTraduction)) {
            // On charge les libelles
            setForIdTraduction(_id);
            findWithoutErrors(transaction);
        }

        // V�rifier si il y a des lignes
        if (size() == 0) {
            return;
        }

        // Lecture des instances
        for (int i = 0; i < size(); i++) {
            // R�cup�rer la traduction et la supprimer
            PATraduction entity = (PATraduction) getEntity(i);
            entity.delete(transaction);
        }

        // Invalider forid
        forIdTraduction = null;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (20.12.2001 13:27:10)
     * 
     * @param trans
     *            globaz.norma.db.interfaceext.fondation.IntTranslatable
     * @param codeISOLangue
     *            java.lang.String
     */
    public void deleteLibelleTraduction(IntTranslatable trans, BITransaction transaction, String codeISOLangue)
            throws Exception {
        String _id = trans.getIdTraduction();

        // Si la transaction n'est pas active, on refuse la mise � jour
        if (!transaction.isOpened()) {
            throw new Exception("Transaction must be opened to update translation no " + _id);
        }

        // Si code langue non fourni, r�cup�rer la langue de l'utilisateur
        if (JadeStringUtil.isBlank(codeISOLangue)) {
            codeISOLangue = trans.getISession().getIdLangueISO();
        }

        // Si identifiant vide, on sort
        if (JadeStringUtil.isBlank(_id)) {
            return;
        }

        // Supprimer le record
        PATraduction tr = new PATraduction();
        tr.setISession(trans.getISession());
        tr.setIdTraduction(_id);
        tr.setCodeISOLangue(codeISOLangue);
        tr.delete(transaction);

        // Invalider l'id pour rechargement
        forIdTraduction = null;

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.12.2001 15:56:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdTraduction() {
        return forIdTraduction;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.12.2001 10:15:19)
     * 
     * @return java.lang.String
     * @param trans
     *            globaz.norma.db.interfaceext.fondation.IntTranslatable
     * @param codeISOLangue
     *            java.lang.String
     */
    public String getLibelleTraduction(IntTranslatable trans, BITransaction transaction, String codeISOLangue) {

        String _VIDE = "";
        String _id = trans.getIdTraduction();

        // V�rifier si l'id traduction est diff�rent de z�ro
        if (JadeStringUtil.isBlank(_id)) {
            return _VIDE;
        }
        // V�rifier si l'id traduction est diff�rente
        if (forIdTraduction != _id) {
            // On charge les libelles
            setForIdTraduction(_id);
            try {
                setISession(trans.getISession());
                find(transaction);
            } catch (Exception e) {
                return _VIDE;
            }
        }
        // V�rifier si il y a des lignes
        if (size() == 0) {
            return _VIDE;
        }
        // Si code langue non fourni, r�cup�rer le d�faut de l'utilisateur
        if (JadeStringUtil.isBlank(codeISOLangue)) {
            try {
                codeISOLangue = trans.getISession().getIdLangueISO();
            } catch (Exception e) {
            }
        }

        // Lecture des instances
        for (int i = 0; i < size(); i++) {
            // R�cup�rer la traduction
            PATraduction entity = (PATraduction) getEntity(i);
            if (entity.getCodeISOLangue().equals(codeISOLangue)) {
                return entity.getLibelle();
            }

        }
        // Si aucune instance n'a �t� trouv�e, on retourne vide
        return _VIDE;

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.12.2001 15:56:10)
     * 
     * @param newForIdTraduction
     *            java.lang.String
     */
    public void setForIdTraduction(java.lang.String newForIdTraduction) {
        forIdTraduction = newForIdTraduction;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.12.2001 10:59:46)
     * 
     * @param trans
     *            globaz.norma.db.interfaceext.fondation.IntTranslatable
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setLibelleTraduction(IntTranslatable trans, BITransaction transaction, String newLibelleTraduction,
            String codeISOLangue) throws Exception {
        String _id = trans.getIdTraduction();

        // Si la transaction n'est pas active, on refuse la mise � jour
        if (!transaction.isOpened()) {
            throw new Exception("Transaction must be opened to update translation no " + _id);
        }

        // Si code langue non fourni, r�cup�rer la langue de l'utilisateur
        if (JadeStringUtil.isBlank(codeISOLangue)) {
            codeISOLangue = trans.getISession().getIdLangueISO();
        }

        // V�rifier l'existence de la traduction
        PATraduction tr = new PATraduction();
        tr.setISession(trans.getISession());

        // V�rifier l'existence
        tr.setIdTraduction(_id);
        tr.setCodeISOLangue(codeISOLangue);
        tr.retrieve(transaction);

        // Mise � jour du libell�
        tr.setLibelle(newLibelleTraduction);

        // Add
        if (tr.isNew() && tr.hasErrors()) {
            tr.setEntiteSource(trans.getIdentificationSource());
            tr.add(transaction);
            // Renseigner le nouvel identifiant traduction si vide
            if (JadeStringUtil.isIntegerEmpty(_id)) {
                trans.setIdTraduction(tr.getIdTraduction());
            }

            // Update
        } else {
            // Si l'objet source correspond
            if ((tr.getEntiteSource()).equals(trans.getIdentificationSource())) {
                tr.update(transaction);
                // L'objet source ne correspond pas
            } else {
                throw new Exception("Source name does not match when updating translation no." + _id);
            }

        }

        // Forcer un nouveau chargement en vidant l'identifiant de la traduction
        forIdTraduction = null;

    }
}

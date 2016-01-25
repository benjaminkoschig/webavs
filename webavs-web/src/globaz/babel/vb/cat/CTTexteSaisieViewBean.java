/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.vb.cat;

import globaz.babel.application.CTApplication;
import globaz.babel.db.ICTCompteurBorne;
import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.cat.CTElement;
import globaz.babel.db.cat.CTElementManager;
import globaz.babel.db.cat.CTTexte;
import globaz.babel.db.cat.CTTexteManager;
import globaz.babel.utils.CTUserUtils;
import globaz.babel.vb.CTAbstractViewBeanSupport;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;
import java.util.Locale;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un viewbean qui permet la saisie, la sauvegarde, la mise à jour et la suppression simultanée de tous les
 * {@link globaz.babel.db.cat.CTTexte textes} d'un {@link globaz.babel.db.cat.CTElement element}.
 * </p>
 * 
 * <p>
 * Seules trois langues nationales sont prises en compte: l'allemand, le francais et l'italien.
 * </p>
 * 
 * @author vre
 */
public class CTTexteSaisieViewBean extends CTAbstractViewBeanSupport implements ICTCompteurBorne {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CODE_ISO_ALLEMAND = Locale.GERMAN.getLanguage();
    public static final String CODE_ISO_FRANCAIS = Locale.FRENCH.getLanguage();
    public static final String CODE_ISO_ITALIEN = Locale.ITALIAN.getLanguage();

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private CTElement element = new CTElement();
    private String nouveauNiveau = null;
    private CTTexte texteAllemand = null;
    private CTTexte texteFrancais = null;
    private CTTexte texteItalien = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        BITransaction transaction = getSession().newTransaction();

        try {
            transaction.openTransaction();
            // sauvegarde de l'element
            element.add(transaction);

            // set les bornes des textes
            getTexteAllemand().setIdElement(element.getIdElement());
            getTexteAllemand().setBorneInferieure(element.getBorneInferieure());
            getTexteFrancais().setIdElement(element.getIdElement());
            getTexteFrancais().setBorneInferieure(element.getBorneInferieure());
            getTexteItalien().setIdElement(element.getIdElement());
            getTexteItalien().setBorneInferieure(element.getBorneInferieure());

            // sauvegarde des textes
            getTexteAllemand().add(transaction);
            getTexteFrancais().add(transaction);
            getTexteItalien().add(transaction);

            // mise a jour des champs isSelectable, isEditable et description de
            // element de meme niveau
            CTElementManager manager = new CTElementManager();
            manager.setSession(getSession());
            manager.setForIdDocument(element.getIdDocument());
            manager.setForNiveau(element.getNiveau());
            manager.find(BManager.SIZE_NOLIMIT);

            Iterator iter = manager.iterator();
            CTElement e = null;
            while (iter.hasNext()) {
                e = (CTElement) iter.next();
                e.setIsSelectable(element.getIsSelectable());
                e.setIsEditable(element.getIsEditable());
                e.setDescription(element.getDescription());
                e.update(transaction);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.isRollbackOnly()) {
                        transaction.rollback();

                        // effacer les id qui auraient eventuellement ete sette
                        // par beforeAdd()
                        element.setIdElement("");
                        getTexteAllemand().setIdTexte("");
                        getTexteFrancais().setIdTexte("");
                        getTexteItalien().setIdTexte("");
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        BITransaction transaction = getSession().newTransaction();
        try {
            transaction.openTransaction();
            element.delete(transaction);
            // getTexteAllemand().delete(transaction);
            // getTexteFrancais().delete(transaction);
            // getTexteItalien().delete(transaction);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * @see globaz.babel.db.cat.ICTCompteurBorne#getBorneInferieure()
     */
    @Override
    public String getBorneInferieure() {
        return element.getBorneInferieure();
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription() {
        return element.getDescription();
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescriptionDE() {
        return getTexteAllemand().getDescription();
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescriptionFR() {
        return getTexteFrancais().getDescription();
    }

    /**
     * getter pour l'attribut description
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescriptionIT() {
        return getTexteItalien().getDescription();
    }

    /**
     * getter pour l'attribut id document
     * 
     * @return la valeur courante de l'attribut id document
     */
    public String getIdDocument() {
        return element.getIdDocument();
    }

    /**
     * getter pour l'attribut id element
     * 
     * @return la valeur courante de l'attribut id element
     */
    public String getIdElement() {
        return element.getIdElement();
    }

    /**
     * getter pour l'attribut isEditable
     * 
     * @return la valeur courante de l'attribut isEditable
     */
    public Boolean getIsEditable() {
        return element.getIsEditable();
    }

    /**
     * getter pour l'attribut isSelectable
     * 
     * @return la valeur courante de l'attribut isSelectable
     */
    public Boolean getIsSelectable() {
        return element.getIsSelectable();
    }

    /**
     * getter pour l'attribut isSelectedByDefault
     * 
     * @return la valeur courante de l'attribut isSelectedByDefault
     */
    public Boolean getIsSelectedByDefault() {
        return element.getIsSelectedByDefault();
    }

    /**
     * getter pour l'attribut niveau
     * 
     * @return la valeur courante de l'attribut niveau
     */
    public String getNiveau() {
        return element.getNiveau();
    }

    /**
     * @return
     */
    public String getNouveauNiveau() {
        return nouveauNiveau;
    }

    /**
     * getter pour l'attribut position
     * 
     * @return la valeur courante de l'attribut position
     */
    public String getPosition() {
        return element.getPosition();
    }

    /**
     * @see globaz.globall.db.BEntity#getSpy()
     */
    public BSpy getSpy() {
        return element.getSpy();
    }

    /**
     * getter pour l'attribut texte allemand
     * 
     * @return la valeur courante de l'attribut texte allemand
     */
    public CTTexte getTexteAllemand() {
        if (texteAllemand == null) {
            texteAllemand = new CTTexte();
            texteAllemand.setCodeIsoLangue(CODE_ISO_ALLEMAND);
        }

        return texteAllemand;
    }

    /**
     * getter pour l'attribut texte francais
     * 
     * @return la valeur courante de l'attribut texte francais
     */
    public CTTexte getTexteFrancais() {
        if (texteFrancais == null) {
            texteFrancais = new CTTexte();
            texteFrancais.setCodeIsoLangue(CODE_ISO_FRANCAIS);
        }

        return texteFrancais;
    }

    /**
     * getter pour l'attribut texte italien
     * 
     * @return la valeur courante de l'attribut texte italien
     */
    public CTTexte getTexteItalien() {
        if (texteItalien == null) {
            texteItalien = new CTTexte();
            texteItalien.setCodeIsoLangue(CODE_ISO_ITALIEN);
        }

        return texteItalien;
    }

    /**
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    public boolean hasSpy() {
        return element.hasSpy();
    }

    /**
     * getter pour l'attribut id role administrateur.
     * 
     * @return la valeur courante de l'attribut id role administrateur
     */
    public boolean isAdministrateur() {
        try {
            CTApplication application = (CTApplication) GlobazSystem
                    .getApplication(CTApplication.DEFAULT_APPLICATION_BABEL);

            return CTUserUtils.isUtilisateurARole(getSession(), application.getIdRoleAdministrateur());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStyledDocument() {
        if (!JadeStringUtil.isIntegerEmpty(getIdDocument())) {
            CTDocument doc = new CTDocument();
            doc.setSession(getSession());
            doc.setIdDocument(getIdDocument());
            try {
                doc.retrieve();
            } catch (Exception e) {
                return false;
            }
            return doc.getIsStyledDocument().booleanValue();
        } else {
            return false;
        }
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        element.retrieve();

        // charger les textes pour ce element
        CTTexteManager mgr = new CTTexteManager();

        mgr.setSession(getSession());
        mgr.setForIdElement(element.getIdElement());
        mgr.find();

        for (int idTexte = mgr.size(); --idTexte >= 0;) {
            CTTexte texte = (CTTexte) mgr.get(idTexte);

            if (CODE_ISO_ALLEMAND.equalsIgnoreCase(texte.getCodeIsoLangue())) {
                texteAllemand = texte;
            } else if (CODE_ISO_FRANCAIS.equalsIgnoreCase(texte.getCodeIsoLangue())) {
                texteFrancais = texte;
            } else if (CODE_ISO_ITALIEN.equalsIgnoreCase(texte.getCodeIsoLangue())) {
                texteItalien = texte;
            }
        }
    }

    /**
     * @see globaz.babel.db.cat.ICTCompteurBorne#setBorneInferieure(java.lang.String)
     */
    @Override
    public void setBorneInferieure(String borneInferieure) {
        element.setBorneInferieure(borneInferieure);
    }

    /**
     * setter pour l'attribut description
     * 
     * @param description
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescription(String description) {
        element.setDescription(description);
    }

    /**
     * setter pour l'attribut description
     * 
     * @param descriptionDE
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionDE(String descriptionDE) {
        getTexteAllemand().setDescription(descriptionDE);
    }

    /**
     * setter pour l'attribut description
     * 
     * @param descriptionFR
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionFR(String descriptionFR) {
        getTexteFrancais().setDescription(descriptionFR);
    }

    /**
     * setter pour l'attribut description
     * 
     * @param descriptionIT
     *            une nouvelle valeur pour cet attribut
     */
    public void setDescriptionIT(String descriptionIT) {
        getTexteItalien().setDescription(descriptionIT);
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        super.setId(id);
        element.setIdElement(id);
    }

    /**
     * setter pour l'attribut id document
     * 
     * @param idDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDocument(String idDocument) {
        element.setIdDocument(idDocument);
    }

    /**
     * setter pour l'attribut id element
     * 
     * @param idElement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdElement(String idElement) {
        element.setIdElement(idElement);
        getTexteAllemand().setIdElement(idElement);
        getTexteFrancais().setIdElement(idElement);
        getTexteItalien().setIdElement(idElement);
    }

    /**
     * setter pour l'attribut isEditable
     * 
     * @param isEditable
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsEditable(Boolean isEditable) {
        element.setIsEditable(isEditable);
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        super.setISession(newSession);
        element.setISession(newSession);
        getTexteAllemand().setISession(newSession);
        getTexteFrancais().setISession(newSession);
        getTexteItalien().setISession(newSession);
    }

    /**
     * setter pour l'attribut isSelectable
     * 
     * @param isSelectable
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsSelectable(Boolean isSelectable) {
        element.setIsSelectable(isSelectable);
    }

    /**
     * setter pour l'attribut isSelectedByDefault
     * 
     * @param isSelectedByDefault
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsSelectedByDefault(Boolean isSelectedByDefault) {
        element.setIsSelectedByDefault(isSelectedByDefault);
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        super.setMessage(message);
        element.setMessage(message);
        getTexteAllemand().setMessage(message);
        getTexteFrancais().setMessage(message);
        getTexteItalien().setMessage(message);
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        super.setMsgType(msgType);
        element.setMsgType(msgType);
        getTexteAllemand().setMsgType(msgType);
        getTexteFrancais().setMsgType(msgType);
        getTexteItalien().setMsgType(msgType);
    }

    /**
     * setter pour l'attribut niveau
     * 
     * @param niveau
     *            une nouvelle valeur pour cet attribut
     */
    public void setNiveau(String niveau) {
        element.setNiveau(niveau);
    }

    /**
     * @param string
     */
    public void setNouveauNiveau(String string) {
        nouveauNiveau = string;
    }

    /**
     * setter pour l'attribut position
     * 
     * @param position
     *            une nouvelle valeur pour cet attribut
     */
    public void setPosition(String position) {
        element.setPosition(position);
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        BITransaction transaction = getSession().newTransaction();
        try {
            transaction.openTransaction();
            // mise a jour de l'element
            element.update(transaction);

            // mise a jour des textes de l'element
            getTexteAllemand().update(transaction);
            getTexteFrancais().update(transaction);
            getTexteItalien().update(transaction);

            // mise a jour des champs isSelectable, isEditable et description de
            // element de meme niveau
            CTElementManager manager = new CTElementManager();
            manager.setSession(getSession());
            manager.setForIdDocument(element.getIdDocument());
            manager.setForNiveau(element.getNiveau());
            manager.find(BManager.SIZE_NOLIMIT);

            Iterator iter = manager.iterator();
            CTElement e = null;
            while (iter.hasNext()) {
                e = (CTElement) iter.next();
                e.setIsSelectable(element.getIsSelectable());
                e.setIsEditable(element.getIsEditable());
                e.setDescription(element.getDescription());
                // isSelectedByDefault mis a jour uniquement pour l'element
                // courant
                if (element.getIdElement().equals(e.getIdElement())) {
                    e.setIsSelectedByDefault(element.getIsSelectedByDefault());
                }
                e.update(transaction);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * @see CTAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}

package globaz.norma.db.fondation;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.utils.CACachedManager;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (15.05.2002 13:22:24)
 * 
 * @author: Administrator
 */
public class PATraductionHelper {
    private static CACachedManager<PATraduction> cachedMgr = null;

    public static void resetCache() {
        PATraductionHelper.cachedMgr = null;
    }

    public static String translate(BSession session, String idTraduction, String idLangueIso) throws Exception {
        if (JadeStringUtil.isBlank(idLangueIso)) {
            idLangueIso = session.getIdLangueISO();
        }
        if (PATraductionHelper.cachedMgr == null) {

            PATraductionManager mgr = new PATraductionManager();
            mgr.setSession(session);
            mgr.setForIdTraduction(""); // sinon nullPointer voir _getWhere...
            mgr.find(0);
            PATraductionHelper.cachedMgr = new CACachedManager(mgr);

        }

        PATraduction tr = PATraductionHelper.cachedMgr.findById(idLangueIso.toUpperCase() + "$" + idTraduction); // cl�
        return (tr != null) ? tr.getLibelle() : "";

    }

    private Throwable error = null;
    private Hashtable tr = new Hashtable();

    public IntTranslatable translatable = null;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 13:22:49)
     * 
     * @param translatable
     *            globaz.norma.db.interfaceext.fondation.IntTranslatable
     */
    public PATraductionHelper(IntTranslatable newTranslatable) throws Exception {
        // V�rifier le param�tre
        if (newTranslatable == null) {
            throw new Exception("L'objet � traduire ne peut pas �tre nul");
        }

        translatable = newTranslatable;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 14:23:07)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void add(BITransaction transaction) throws java.lang.Exception {
        save(transaction);
        PATraductionHelper.resetCache();

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 14:23:47)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public synchronized void delete(BITransaction transaction) throws java.lang.Exception {
        // V�rifier la transaction
        if (!transaction.isOpened()) {
            throw new Exception("La transaction doit �tre active lors de la mise � jour de la traduction");
        }

        // Instancier un manager
        PATraductionManager mgr = new PATraductionManager();
        mgr.setISession(getTranslatable().getISession());
        mgr.setForIdTraduction(getTranslatable().getIdTraduction());
        mgr.find(transaction);

        // Parcourir les traductions
        for (int i = 0; i < mgr.size(); i++) {
            PATraduction traduction = (PATraduction) mgr.getEntity(i);
            traduction.setISession(getTranslatable().getISession());
            traduction.delete(transaction);
            if (traduction.hasErrors()) {
                throw new Exception("La suppression de la traduction a �chou�");
            }
        }

        // Mise � jour de l'identifiant
        getTranslatable().setIdTraduction("0");
        PATraductionHelper.resetCache();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 13:24:00)
     */
    public String getDescription() {
        return this.getDescription(null);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 13:24:24)
     * 
     * @param codeISOLangue
     *            java.lang.String
     */
    public synchronized String getDescription(String codeISOLangue) {
        // Retourner blanc si l'identifiant est vide
        if (JadeStringUtil.isIntegerEmpty(getTranslatable().getIdTraduction())) {
            return "";
        }

        // Code ISO par d�faut
        if (JadeStringUtil.isBlank(codeISOLangue)) {
            try {
                codeISOLangue = getTranslatable().getISession().getIdLangueISO().toUpperCase();
            } catch (Exception e) {
            }
        }

        // V�rifier si la traduction existe
        PATraduction sTraduction = (PATraduction) tr.get(codeISOLangue);
        if (sTraduction != null) {
            return sTraduction.getLibelle();
        } else {
            // Sinon, la r�cup�rer
            sTraduction = new PATraduction();
            sTraduction.setISession(getTranslatable().getISession());
            sTraduction.setIdTraduction(getTranslatable().getIdTraduction());
            sTraduction.setCodeISOLangue(codeISOLangue);

            try {
                sTraduction.retrieve();
            } catch (Exception e) {
                error = e;
                return new String();
            }

            // Si trouv� le stocker et l'envoyer
            if (!sTraduction.isNew()) {
                tr.put(sTraduction.getCodeISOLangue(), sTraduction);
                return sTraduction.getLibelle();
            } else {
                return new String();
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 15:32:35)
     * 
     * @return java.lang.Throwable
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 13:25:40)
     * 
     * @return globaz.norma.db.interfaceext.fondation.IntTranslatable
     */
    public IntTranslatable getTranslatable() {
        return translatable;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 14:22:26)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    private synchronized void save(BITransaction transaction) throws java.lang.Exception {
        // V�rifier la transaction
        if (!transaction.isOpened()) {
            throw new Exception("La transaction doit �tre active lors de la mise � jour de la traduction");
        }

        // Parcourir la table
        Enumeration e = tr.elements();
        while (e.hasMoreElements()) {
            // R�cup�rer l'objet depuis la table
            PATraduction traduction = (PATraduction) e.nextElement();
            traduction.setISession(getTranslatable().getISession());
            traduction.setIdTraduction(getTranslatable().getIdTraduction());
            traduction.setEntiteSource(getTranslatable().getIdentificationSource());

            // Mise � jour ou cr�ation
            if (traduction.isNew()) {
                if (!JadeStringUtil.isBlank(traduction.getLibelle().trim())) {
                    traduction.add(transaction);
                }
            } else {
                if (JadeStringUtil.isBlank(traduction.getLibelle().trim())) {
                    traduction.delete(transaction);
                } else {
                    traduction.update(transaction);
                }
            }

            // Tester la valeur de retour
            if (traduction.hasErrors()) {
                throw new Exception("La mise � jour de la traduction a �chou�");
            }

            // Sauvegarder l'identifiant si vide
            if (JadeStringUtil.isIntegerEmpty(getTranslatable().getIdTraduction())) {
                getTranslatable().setIdTraduction(traduction.getIdTraduction());
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 14:13:29)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    public void setDescription(String newDescription, String codeISOLangue) {
        if (!JadeStringUtil.isBlank(codeISOLangue)) {
            // R�cup�rer le objet depuis la table
            PATraduction sTraduction = (PATraduction) tr.get(codeISOLangue);

            // Affecter nouvelle valeur si trouv�
            if (sTraduction != null) {
                sTraduction.setLibelle(newDescription);
            } else {
                // Stocker nouvelle valeur si pas trouv�
                sTraduction = new PATraduction();
                sTraduction.setISession(getTranslatable().getISession());
                sTraduction.setIdTraduction(getTranslatable().getIdTraduction());
                sTraduction.setCodeISOLangue(codeISOLangue);
                try {
                    sTraduction.retrieve();
                } catch (Exception e) {
                    error = e;
                    return;
                }

                // Si pas trouv�e, on instancie un nouvel objet
                if (sTraduction.hasErrors() || sTraduction.isNew()) {
                    sTraduction = new PATraduction();
                    sTraduction.setISession(getTranslatable().getISession());
                    sTraduction.setIdTraduction(getTranslatable().getIdTraduction());
                    sTraduction.setCodeISOLangue(codeISOLangue);
                }

                // Stocker
                sTraduction.setLibelle(newDescription);
                tr.put(sTraduction.getCodeISOLangue(), sTraduction);
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.05.2002 14:21:47)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public void update(BITransaction transaction) throws java.lang.Exception {
        save(transaction);
        PATraductionHelper.resetCache();
    }

}

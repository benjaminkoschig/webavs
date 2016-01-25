/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.util;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class LEViewBeanSupport implements FWViewBeanInterface {
    private String action = "";
    private String eMailAddress = "";
    String forCategorie = new String();
    String forDateReference = new String();
    String forEmail = new String();
    public Vector formule = new Vector();
    String forOrderBy = new String();
    private String message = "";
    private String msgType = "";
    /* FWViewBeanInterface */
    private BISession session = null;

    /*
     * ------------------------------------------------------------------------- FWViewBeanInterface
     */
    public String getAction() {
        return (action);
    }

    /**
     * Returns the eMailAddress.
     * 
     * @return String
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * @return
     */
    public String getForCategorie() {
        return forCategorie;
    }

    /**
     * @return
     */
    public String getForDateReference() {
        return forDateReference;
    }

    /**
     * @return
     */
    public String getForEmail() {
        return forEmail;
    }

    /**
     * @return
     */
    public Vector getFormule() {
        if (formule.size() > 0) {
            return formule;
        } else {
            return new Vector();
        }

    }

    public Vector getFormulesList() {
        return getFormulesList((BSession) getISession(), "");
    }

    /*
     * @author oca fonction qui nous retourne la liste des formules par rapport à une catégorie, en tenant compte de
     * l'ordre des étapes dans le suivi
     * 
     * Les étapes de début de suivis ne font pas partie de la liste PO4728
     */
    public Vector getFormulesList(BSession sessionUt, String categorie) {
        Vector formule = new Vector();
        formule.add(new String[] { "", "" });
        String col = TIToolBox.getCollection();
        String forCat = "";
        if (!JadeStringUtil.isBlank(categorie)) {
            forCat = " and cat.pfo2va=" + categorie;
        }
        String fields = "d2.PDEFDO SOURCE, d1.PDEFDO TARGET ";
        String req = " from " + col + "ENPPRAP r " + "inner join " + col + "ENPPDEF d1 on (r.PDEFID = d1.PDEFID) "
                + "inner join " + col + "ENPPFO1 f on (r.PFO1ID = f.PFO1ID) " + "inner join " + col
                + "ENPPFO2 cat on (cat.PFO1ID = f.PFO1ID and cat.pfo2fo=16000007 " + forCat + ") " + "inner join "
                + col + "ENPPDEF d2 on (f.PDEFID = d2.PDEFID) " + "order by cat.pfo2va";
        try {
            List<Map<String, String>> rList = TISQL.query(sessionUt, fields, req);
            Set<String> sources = new HashSet<String>();
            Set<String> starts = new HashSet<String>();
            Set<String> targets = new HashSet<String>();
            Map<String, String> sourceTargetMap = new HashMap<String, String>();
            for (Map<String, String> row : rList) {
                sources.add(row.get("SOURCE"));
                targets.add(row.get("TARGET"));
                starts.add(row.get("SOURCE")); // utile pour la suite ...
                sourceTargetMap.put(row.get("SOURCE"), row.get("TARGET"));
            }

            /*
             * Trouve les debuts de suivis (donc les étapes présentes dans sources, mais pas de targets)
             */
            starts.removeAll(targets);

            for (String n : starts) {
                int cpt = 0;
                while (n != null) {
                    String target = sourceTargetMap.get(n);
                    n = (target != null) ? target : null;
                    if (n != null) {
                        // j'aoute ici de manière a ne pas inclure dans la liste
                        // les étapes de début de suivi
                        formule.add(new String[] { n, sessionUt.getCodeLibelle(n) });
                    }
                    cpt++;
                    if (cpt > 100000) {
                        // préventif
                        throw new Exception(
                                "Une boucle infinie a été détectée dans LEViewBeanSupport.getFormulesList(...)");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formule;
    }

    /**
     * @return
     */
    public String getForOrderBy() {
        return forOrderBy;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Sets the eMailAddress.
     * 
     * @param eMailAddress
     *            The eMailAddress to set
     */
    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    /**
     * @param string
     */
    public void setForCategorie(String string) {
        forCategorie = string;
    }

    /**
     * @param string
     */
    public void setForDateReference(String string) {
        forDateReference = string;
    }

    /**
     * @param string
     */
    public void setForEmail(String string) {
        forEmail = string;
    }

    /**
     * @param vector
     */
    public void setFormule(Vector vector) {
        formule = vector;
    }

    /**
     * @param string
     */
    public void setForOrderBy(String string) {
        forOrderBy = string;
    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

}

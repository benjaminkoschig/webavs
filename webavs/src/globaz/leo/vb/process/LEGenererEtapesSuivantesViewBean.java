/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.vb.process;

import globaz.globall.db.BIPersistentObject;
import globaz.leo.util.LEViewBeanSupport;
import java.util.Vector;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEGenererEtapesSuivantesViewBean extends LEViewBeanSupport implements BIPersistentObject {
    public static final String SIMULATION_FALSE = "false";
    // String forCsFormule2 = new String();
    public static final String SIMULATION_TRUE = "true";

    /**
     * @return
     */
    public static String getSIMULATION_FALSE() {
        return SIMULATION_FALSE;
    }

    /**
     * @return
     */
    public static String getSIMULATION_TRUE() {
        return SIMULATION_TRUE;
    }

    String commentaire = new String();
    String forCsFormule = new String();
    String forCsFormule1 = new String();
    String forDateImpression = new String();
    String forDatePriseEnCompte = new String();
    String forIsSimulation = SIMULATION_FALSE;

    String order1 = new String();

    String order2 = new String();

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * @return
     */
    public String getForCsFormule() {
        return forCsFormule;
    }

    /**
     * @return
     */
    public String getForCsFormule1() {
        return forCsFormule1;
    }

    /**
     * @return
     */
    public String getForDateImpression() {
        return forDateImpression;
    }

    /**
     * @return
     */
    public String getForDatePriseEnCompte() {
        return forDatePriseEnCompte;
    }

    /**
     * @return
     */
    public String getForIsSimulation() {
        return forIsSimulation;
    }

    /**
     * @return
     */
    @Override
    public Vector getFormule() {
        return formule;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return
     */
    public String getOrder1() {
        return order1;
    }

    /**
     * @return
     */
    public String getOrder2() {
        return order2;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @param string
     */
    /**
     * @param string
     */

    /**
     * @param string
     */
    public void setCommentaire(String string) {
        commentaire = string;
    }

    /**
     * @param string
     */
    public void setForCsFormule(String string) {
        forCsFormule = string;
    }

    /**
     * @param string
     */
    public void setForCsFormule1(String string) {
        forCsFormule1 = string;
    }

    /**
     * @param string
     */
    public void setForDateImpression(String string) {
        forDateImpression = string;
    }

    /**
     * @param string
     */
    public void setForDatePriseEnCompte(String string) {
        forDatePriseEnCompte = string;
    }

    /**
     * @param string
     */
    public void setForIsSimulation(String string) {
        forIsSimulation = string;
    }

    /**
     * @param vector
     */
    @Override
    public void setFormule(Vector vector) {
        formule = vector;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    /**
     * @param string
     */
    public void setOrder1(String string) {
        order1 = string;
    }

    /**
     * @param string
     */
    public void setOrder2(String string) {
        order2 = string;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}

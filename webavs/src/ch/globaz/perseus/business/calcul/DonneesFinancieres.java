/**
 * 
 */
package ch.globaz.perseus.business.calcul;

import java.util.Hashtable;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteType;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;

/**
 * Classe repr�sentant un ensemble de donn�es financi�res pour un membre famille
 * 
 * @author DDE
 * 
 */
public class DonneesFinancieres {

    private Hashtable<DepenseReconnueType, DepenseReconnue> depensesReconnues = null;
    private Hashtable<DetteType, Dette> dettes = null;
    private Hashtable<FortuneType, Fortune> fortunes = null;
    private MembreFamille membreFamille = null;
    private Hashtable<RevenuType, Revenu> revenus = null;

    public DonneesFinancieres() {
        fortunes = new Hashtable<FortuneType, Fortune>();
        dettes = new Hashtable<DetteType, Dette>();
        revenus = new Hashtable<RevenuType, Revenu>();
        depensesReconnues = new Hashtable<DepenseReconnueType, DepenseReconnue>();
        membreFamille = new MembreFamille();

    }

    /**
     * @return the depensesReconnues
     */
    public Hashtable<DepenseReconnueType, DepenseReconnue> getDepensesReconnues() {
        return depensesReconnues;
    }

    /**
     * @return the dettes
     */
    public Hashtable<DetteType, Dette> getDettes() {
        return dettes;
    }

    /**
     * Permet de r�cup�rer un �l�ment, si celui-ci n'a pas �t� renseign�, l'�l�ment est retourn� avec une valeur de 0
     * 
     * @param DepenseReconnueType
     *            : Type d'�l�ment de depenseReconnue (enum)
     * @return Element de DepenseReconnue
     */
    public DepenseReconnue getElementDepenseReconnue(DepenseReconnueType type) {
        if (depensesReconnues.containsKey(type)) {
            return depensesReconnues.get(type);
        } else {
            DepenseReconnue dr = new DepenseReconnue(type);
            dr.setValeur(new Float(0));
            dr.setValeurModifieeTaxateur(new Float(0));
            return dr;
        }
    }

    /**
     * Permet de r�cup�rer un �l�ment, si celui-ci n'a pas �t� renseign�, l'�l�ment est retourn� avec une valeur de 0
     * 
     * @param DetteType
     *            Type d'�l�ment de dette (enum)
     * @return Element de Dette
     */
    public Dette getElementDette(DetteType type) {
        if (dettes.containsKey(type)) {
            return dettes.get(type);
        } else {
            Dette d = new Dette(type);
            d.setValeur(new Float(0));
            d.setValeurModifieeTaxateur(new Float(0));
            return d;
        }
    }

    /**
     * Permet de r�cup�rer un �l�ment, si celui-ci n'a pas �t� renseign�, l'�l�ment est retourn� avec une valeur de 0
     * 
     * @param FortuneType
     *            : Type d'�l�ment de fortune (enum)
     * @return Element de Fortune
     */
    public Fortune getElementFortune(FortuneType type) {
        if (fortunes.containsKey(type)) {
            return fortunes.get(type);
        } else {
            Fortune f = new Fortune(type);
            f.setValeur(new Float(0));
            f.setValeurModifieeTaxateur(new Float(0));
            return f;
        }
    }

    /**
     * Permet de r�cup�rer un �l�ment, si celui-ci n'a pas �t� renseign�, l'�l�ment est retourn� avec une valeur de 0
     * 
     * @param RevenuType
     *            : Type d'�l�ment de revenu (enum)
     * @return Element de Revenu
     */
    public Revenu getElementRevenu(RevenuType type) {
        if (revenus.containsKey(type)) {
            return revenus.get(type);
        } else {
            Revenu r = new Revenu(type);
            r.setValeur(new Float(0));
            r.setValeurModifieeTaxateur(new Float(0));
            return r;
        }
    }

    /**
     * @return the fortunes
     */
    public Hashtable<FortuneType, Fortune> getFortunes() {
        return fortunes;
    }

    /**
     * @return the membreFamille
     */
    public MembreFamille getMembreFamille() {
        return membreFamille;
    }

    /**
     * @return the revenus
     */
    public Hashtable<RevenuType, Revenu> getRevenus() {
        return revenus;
    }

    /**
     * @param depensesReconnues
     *            the depensesReconnues to set
     */
    public void setDepensesReconnues(Hashtable<DepenseReconnueType, DepenseReconnue> depensesReconnues) {
        this.depensesReconnues = depensesReconnues;
    }

    /**
     * @param dettes
     *            the dettes to set
     */
    public void setDettes(Hashtable<DetteType, Dette> dettes) {
        this.dettes = dettes;
    }

    /**
     * @param fortunes
     *            the fortunes to set
     */
    public void setFortunes(Hashtable<FortuneType, Fortune> fortunes) {
        this.fortunes = fortunes;
    }

    /**
     * @param membreFamille
     *            the membreFamille to set
     */
    public void setMembreFamille(MembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    /**
     * @param revenus
     *            the revenus to set
     */
    public void setRevenus(Hashtable<RevenuType, Revenu> revenus) {
        this.revenus = revenus;
    }

}

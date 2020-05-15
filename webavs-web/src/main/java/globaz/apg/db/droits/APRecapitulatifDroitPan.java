/*
 * Créé le 31 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAVector;

import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APRecapitulatifDroitPan extends APAbstractRecapitulatifDroit {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String dateDeces = "";

    private String dateRepriseActivite = "";
    private transient JAVector enfants = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        super._afterRetrieve(transaction);

        // chargement des enfants
        APEnfantPanManager mgr = new APEnfantPanManager();

        mgr.setSession(getSession());
        mgr.setForIdDroit(idDroit);
        mgr.find(transaction);

        enfants = mgr.getContainer();
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromBase(_getCollection()).toString();
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
    }

    /**
     * getter pour l'attribut date deces
     * 
     * @return la valeur courante de l'attribut date deces
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * getter pour l'attribut date reprise activite
     * 
     * @return la valeur courante de l'attribut date reprise activite
     */
    public String getDateRepriseActivite() {
        return dateRepriseActivite;
    }

    /**
     * getter pour l'attribut enfants
     * 
     * @return la valeur courante de l'attribut enfants
     */
    public List getEnfants() {
        return enfants;
    }

    /**
     * setter pour l'attribut date deces
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    /**
     * setter pour l'attribut date reprise activite
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateRepriseActivite(String string) {
        dateRepriseActivite = string;
    }
}

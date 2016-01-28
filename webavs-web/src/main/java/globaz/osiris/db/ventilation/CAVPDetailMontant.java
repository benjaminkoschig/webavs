/*
 * Créé le 15 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.ventilation;

import globaz.osiris.api.APIVPDetailMontant;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ald
 */
public class CAVPDetailMontant implements APIVPDetailMontant {

    private BigDecimal montantBase = new BigDecimal("0");
    private Map<String, BigDecimal> montantParAnnee = new HashMap<String, BigDecimal>();
    private BigDecimal montantVentile = new BigDecimal("0");
    private String typeMontant = "";

    /**
     * @param montant
     */
    public void addMontant(BigDecimal montant) {
        montantBase = montantBase.add(montant);
    }

    /**
     * @param montant
     * @param annee
     */
    public void addMontant(BigDecimal montant, String annee) {
        if (annee != null) {
            this.addMontant(montant);
            if (montantParAnnee.get(annee) != null) {
                montantParAnnee.put(annee, montantParAnnee.get(annee).add(montant));
            } else {
                montantParAnnee.put(annee, new BigDecimal(montant.toString()));
            }
        }
    }

    /**
     * @param montantVen
     */
    public void addMontantVentile(BigDecimal montantVen) {
        montantVentile = montantVentile.add(montantVen);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPDetailMontant#getMontantBase()
     */
    @Override
    public BigDecimal getMontantBase() {
        return montantBase;
    }

    /**
     * @return
     */
    public Map<String, BigDecimal> getMontantParAnnee() {
        return montantParAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPDetailMontant#getMontantVentile()
     */
    @Override
    public BigDecimal getMontantVentile() {
        return montantVentile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPDetailMontant#getTypeMontant()
     */
    @Override
    public String getTypeMontant() {
        return typeMontant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPDetailMontant#isPenal()
     */
    @Override
    public boolean isPenal() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPDetailMontant#setPenal(boolean)
     */
    @Override
    public void setPenal(boolean isPenal) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIVPDetailMontant#setTypeMontant(java.lang.String)
     */
    @Override
    public void setTypeMontant(String type) {
        typeMontant = type;
    }
}

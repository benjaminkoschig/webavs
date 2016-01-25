/*
 * Créé le 15 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

import java.math.BigDecimal;

/**
 * @author ald
 */
public interface APIVPDetailMontant {
    // Type de montant
    public final static String CS_VP_MONTANT_EMPLOYEUR = "239001";
    public final static String CS_VP_MONTANT_SALARIE = "239002";
    public final static String CS_VP_MONTANT_SIMPLE = "239003";

    public BigDecimal getMontantBase();

    public BigDecimal getMontantVentile();

    public String getTypeMontant();

    public boolean isPenal();

    public void setPenal(boolean isPenal);

    public void setTypeMontant(String type);
}

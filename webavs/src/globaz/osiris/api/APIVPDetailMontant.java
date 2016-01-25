/*
 * Cr�� le 15 nov. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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

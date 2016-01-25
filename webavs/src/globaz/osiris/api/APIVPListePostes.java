/*
 * Cr�� le 15 nov. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;

/**
 * @author ald Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface APIVPListePostes {
    public void addPoste(APISection section, APIRubrique rubrique, APIVPPoste poste);

    public String[] getListePostes();

    public BigDecimal getPartPenal();

    public APIVPPoste getPoste(APISection section, APIRubrique rubrique);

    public void initTableauFinal(String idCompteAnnexe, String periodedebut, String periodeFin) throws Exception;

    public void setMontantAVentiler(FWCurrency montantAVentiler);

    public int size();
}

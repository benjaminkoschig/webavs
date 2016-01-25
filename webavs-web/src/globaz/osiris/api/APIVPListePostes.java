/*
 * Créé le 15 nov. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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

/*
 * Créé le 14 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.suivi;

import globaz.leo.constantes.ILEConstantes;
import globaz.naos.db.affiliation.AFAffiliation;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFSuiviLPP extends AFSuiviGeneral {
    @Override
    public String getDefinitionFormule() {
        return ILEConstantes.CS_DEBUT_SUIVI_LPP;
    }

    @Override
    public String getIdDestinataire(AFAffiliation affiliation) {
        return affiliation.getIdTiers();
    }

    @Override
    public boolean isAffiliationConcerne(AFAffiliation affiliation) {
        return super.isAffiliationConcerne(affiliation);
    }
}

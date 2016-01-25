/*
 * Créé le 6 févr. 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hermes.service;

import globaz.globall.db.BSession;
import globaz.hermes.utils.HECompareAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.util.Comparator;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class HEExtraitService {
    public Comparator getSortExtrait(BSession session) {
        try {
            String className = session.getApplication().getProperty("extraitCI.impression.tri.classe");
            if (!JadeStringUtil.isEmpty(className)) {
                return (Comparator) Class.forName(className).newInstance();
            } else {
                return new HECompareAnnonce();
            }
        } catch (Exception e) {
            JadeLogger.info("Erreur dans le chargement de la classe de tri", e.getMessage());
        } finally {
            return new HECompareAnnonce();
        }
    }
}

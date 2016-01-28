/*
 * Créé le 4 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.handler;

import globaz.globall.db.BSession;
import globaz.globall.util.JAUtil;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.process.generation.ILEGeneration;
import globaz.leo.process.generation.LEGeneratorFactory;

/**
 * @author ald
 * 
 * 
 */
public class LEDocumentHandler {
    /**
     * 
     * @param envoiDS
     * @param session
     * @return null si aucun generateur de document n'est configuré pour les données envoiDS
     * @throws Exception
     */
    public ILEGeneration chargeGenerateurDocument(LEEnvoiDataSource envoiDS, BSession session) throws Exception {
        if (!JAUtil.isStringEmpty(envoiDS.getField(LEEnvoiDataSource.NOM_CLASSE))) {
            ILEGeneration genDoc = LEGeneratorFactory.newInstance(envoiDS.getField(LEEnvoiDataSource.NOM_CLASSE),
                    session);
            genDoc.setSessionModule(session);
            genDoc.setDocumentDataSource(envoiDS);
            return genDoc;
        } else {
            return null;
        }
    }
}

/*
 * Cr�� le 4 avr. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
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
     * @return null si aucun generateur de document n'est configur� pour les donn�es envoiDS
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

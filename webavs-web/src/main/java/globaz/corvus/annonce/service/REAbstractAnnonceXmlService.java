package globaz.corvus.annonce.service;

import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.globall.db.BSession;
import java.util.Enumeration;
import ch.admin.ofit.anakin.donnee.AnnonceErreur;

public abstract class REAbstractAnnonceXmlService {
    /**
     * Valide une annonce d'augmentation avec ANAKIN <br/>
     * Lance une exception si une erreur de parsing survient
     * 
     * @param enregistrement01
     * @param enregistrement02
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    void parseAugmentationAvecAnakin(REAnnoncesAbstractLevel1A enregistrement01,
            REAnnoncesAbstractLevel1A enregistrement02, BSession session, String forMoisAnneeComptable)
            throws Exception {

        Enumeration erreurs = REAnakinParser.getInstance().parse(session, enregistrement01, enregistrement02,
                forMoisAnneeComptable);

        StringBuilder stringBuilder = new StringBuilder();
        while ((erreurs != null) && erreurs.hasMoreElements()) {
            AnnonceErreur erreur = (AnnonceErreur) erreurs.nextElement();
            stringBuilder.append(erreur.getMessage()).append("\n");
        }
        if (stringBuilder.length() > 0) {
            throw new Exception(stringBuilder.toString());
        }

    }

    void parseDiminutionAvecAnakin(REAnnoncesAbstractLevel1A enregistrement01, BSession session,
            String forMoisAnneeComptable) throws Exception {
        parseAugmentationAvecAnakin(enregistrement01, null, session, forMoisAnneeComptable);
    }
}

package globaz.corvus.annonce.service;

import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;

public class REAnnonces10eXmlService extends REAbstractAnnonceXmlService implements REAnnonceXmlService {

    private static REAnnonceXmlService instance = new REAnnonces10eXmlService();

    private ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();

    public static REAnnonceXmlService getInstance() {
        return instance;
    }

    @Override
    public Object getAnnonceXml(REAnnoncesAbstractLevel1A annonce, String forMoisAnneeComptable, BSession session,
            BITransaction transaction) throws Exception {

        int codeApplication = Integer.parseInt(annonce.getCodeApplication());
        switch (codeApplication) {
            case 44:
            case 46:
                REAnnoncesAugmentationModification10Eme augmentation10eme01 = new REAnnoncesAugmentationModification10Eme();
                augmentation10eme01.setSession(session);
                augmentation10eme01.setIdAnnonce(annonce.getIdAnnonce());
                augmentation10eme01.retrieve();

                REAnnoncesAugmentationModification10Eme augmentation10eme02 = new REAnnoncesAugmentationModification10Eme();
                augmentation10eme02.setSession(session);
                augmentation10eme02.setIdAnnonce(augmentation10eme01.getIdLienAnnonce());
                augmentation10eme02.retrieve();

                parseAugmentationAvecAnakin(augmentation10eme01, augmentation10eme02, session, forMoisAnneeComptable);

                if (!augmentation10eme02.isNew()) {
                    augmentation10eme02.setEtat(IREAnnonces.CS_ETAT_ENVOYE);
                    augmentation10eme02.update(transaction);
                }
                break;
            case 45:
                REAnnoncesDiminution10Eme diminution10eme01 = new REAnnoncesDiminution10Eme();
                diminution10eme01.setSession(session);
                diminution10eme01.setIdAnnonce(annonce.getIdAnnonce());
                diminution10eme01.retrieve();

                parseDiminutionAvecAnakin(diminution10eme01, session, forMoisAnneeComptable);
        }
        throw new Exception("no match into the expected variable paussibilities");
    }

}

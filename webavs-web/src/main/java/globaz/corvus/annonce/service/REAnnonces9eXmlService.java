package globaz.corvus.annonce.service;

import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import ch.admin.zas.rc.RRMeldung9Type;
import ch.admin.zas.rc.ZuwachsmeldungO9Type;
import ch.globaz.common.properties.CommonProperties;

public class REAnnonces9eXmlService {

    private String retourneCaisseAgence() throws Exception {
        return CommonProperties.KEY_NO_CAISSE.getValue() + "." + CommonProperties.NUMERO_AGENCE.getValue();
    }

    private Long retourneNoDAnnonceSur6Position(String noAnnonce) {
        if (noAnnonce.length() > 6) {
            noAnnonce = noAnnonce.substring(noAnnonce.length() - 6);
        }
        return new Long(noAnnonce);
    }

    public RRMeldung9Type annonceAugmentationOrdinaire9e(REAnnoncesAugmentationModification9Eme enr01,
            REAnnoncesAugmentationModification9Eme enr02) throws Exception {
        ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();
        RRMeldung9Type annonce9 = factoryType.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente renteOrdinaire = factoryType.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type augmentation = factoryType.createZuwachsmeldungO9Type();
        augmentation.setKasseZweigstelle(retourneCaisseAgence());
        augmentation.setMeldungsnummer(retourneNoDAnnonceSur6Position(enr01.getIdAnnonce()));
        annonce9.setOrdentlicheRente(renteOrdinaire);
        return new RRMeldung9Type();

    }

}

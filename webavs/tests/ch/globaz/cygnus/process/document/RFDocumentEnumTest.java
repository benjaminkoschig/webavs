package ch.globaz.cygnus.process.document;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class RFDocumentEnumTest {

    @Test
    @Ignore
    public void testGedErreur() {
        Assert.assertNull(RFDocumentEnum.RFM_LETTRE_TYPE_DENTISTE_ENVOIS_FACTURE_MEDECIN_CONSEIL_DE_LA_FACTURE_TRAITEMENT_ETRANGER
                .getIsGed());

        Assert.assertNull(RFDocumentEnum.RFM_LETTRE_TYPE_DENTISTE_RAPPEL_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE
                .getIsGed());

        Assert.assertNull(RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_DECISION_REFUS_1.getIsGed());
    }

    @Test
    @Ignore
    public void testSimpleEnumCs() {
        Assert.assertEquals(
                RFDocumentEnum.RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DOSSIER_COMPLET_AU_MEDECIN_CONSEIL.getCsDocument(),
                "66001422");
        Assert.assertEquals(RFDocumentEnum.RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_DEMANDE_EVALUATION_DES_BESOINS_AU_CMS
                .getCsDocument(), "66001433");
        Assert.assertEquals(
                RFDocumentEnum.RFM_LETTRE_TYPE_DENTISTE_ENVOIS_FACTURE_MEDECIN_CONSEIL_DE_LA_FACTURE_TRAITEMENT_ETRANGER
                        .getCsDocument(), "66001427");
    }

    @Test
    public void testSimpleEnumNoInforom() {
        Assert.assertEquals(
                RFDocumentEnum.RFM_LETTRE_TYPE_DENTISTE_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE.getNoInforom(),
                "7032PRF");
        Assert.assertEquals(
                RFDocumentEnum.RFM_LETTRE_TYPE_DENTISTE_LETTRE_ASSURE_SUITE_AU_DEVIS_ETRANGER.getNoInforom(), "7036PRF");
        Assert.assertEquals(RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_DROIT_MAINTENU_SUITE_A_REVISION.getNoInforom(),
                "7018PRF");
    }

}

package globaz.cygnus.mappingXmlml;

import globaz.cygnus.utils.RFUtils;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

class RFXmlmlMappingFormatAdminAdressePaiement {
    private BSession session;

    RFXmlmlMappingFormatAdminAdressePaiement(BSession aSession) {
        session = aSession;
    }

    String getIt(String idAdministration) throws Exception {
        TIAdministrationViewBean adminVB = RFUtils.loadAdministration(idAdministration, session);
        String result = "";
        if (adminVB == null) {
            return result;
        }
        String adrPaiementNom = adminVB.getNom();
        // recherche de l'adresse de paiement
        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), idAdministration,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();
            source.load(adresse);

            ITIAdresseFormater tiAdrPaiBanFor;

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                tiAdrPaiBanFor = new TIAdressePaiementBanqueFormater();
            } else {
                tiAdrPaiBanFor = new TIAdressePaiementCppFormater();
            }

            if (!JadeStringUtil.isBlank(adrPaiementNom)) {
                result = "\n" + adrPaiementNom + "\n" + tiAdrPaiBanFor.format(source) + "\n";
            } else {
                result = "\n" + tiAdrPaiBanFor.format(source) + "\n";
            }

        } else {
            result = adrPaiementNom;
        }
        return result;
    }
}

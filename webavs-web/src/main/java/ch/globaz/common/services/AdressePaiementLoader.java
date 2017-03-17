/*
 * Globaz SA
 */
package ch.globaz.common.services;

import globaz.globall.db.BSession;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.domaine.AdressePaiement;
import ch.globaz.common.domaine.Banque;
import ch.globaz.common.domaine.Tiers;

public class AdressePaiementLoader {

    BSession session;

    public AdressePaiementLoader(BSession session) {
        super();
        this.session = session;
    }

    public Map<Long, AdressePaiement> searchAdressePaiement(Map<Long, Long> mapIdTiersAndDomain) {

        if (mapIdTiersAndDomain == null) {
            throw new IllegalArgumentException("Unabled to find adresses, mapIdTiersAndDomain is null");
        }

        Map<Long, AdressePaiement> adresses = new HashMap<Long, AdressePaiement>();

        for (Entry<Long, Long> entry : mapIdTiersAndDomain.entrySet()) {
            try {
                TIAdressePaiementData adresseData = PRTiersHelper.getAdressePaiementData(session, null, entry.getKey()
                        .toString(), entry.getValue().toString(), null, null);
                adresses.put(entry.getKey(), convertAdressePaiement(adresseData));
            } catch (Exception e) {
                throw new RuntimeException("Unabled to find adresse paiement for " + entry.getKey(), e);
            }
        }

        return adresses;

        // TIAdressePaiementDataManager mgr = new TIAdressePaiementDataManager();
        //
        // mgr.setSession(session);
        // mgr.setForIdTiers(idTiersAdressePmt);
        // mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        // mgr.find(BManager.SIZE_NOLIMIT);
        //
        // java.util.Collection<BIEntity> c = TIAdressePmtResolver.resolve(mgr, domainePaiement, idExterne);
        //
        // if (c.size() > 0) {
        // return (TIAdressePaiementData) c.toArray()[0];
        // }
        //
        // TIAdressePaiementData data = new TIAdressePaiementData();
    }

    private static AdressePaiement convertAdressePaiement(TIAdressePaiementData adresseData) {

        if (adresseData != null) {

            Banque banque = new Banque();
            banque.setAdresse1(adresseData.getLigneAdresse1_banque());
            banque.setAdresse2(adresseData.getLigneAdresse2_banque());
            banque.setAdresse3(adresseData.getLigneAdresse3_banque());
            banque.setAdresse4(adresseData.getLigneAdresse4_banque());
            // banque.setCanton(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON));
            // banque.setCantonCour(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_PAYS));
            banque.setCasePostal(adresseData.getCasePostale_banque());
            banque.setCompte(adresseData.getCompte());
            banque.setDesignation1(adresseData.getDesignation1_banque());
            banque.setDesignation2(adresseData.getDesignation2_banque());
            banque.setLocalite(adresseData.getLocalite_banque());
            banque.setNpa(adresseData.getNpa_banque());
            banque.setNumero(adresseData.getNumero_banque());
            banque.setPaysIso(adresseData.getPaysIso_banque());
            banque.setPays(adresseData.getPays_banque());
            banque.setRue(adresseData.getRue_banque());
            banque.setCcp(adresseData.getCcp_banque());

            Tiers tiers = new Tiers();
            // tiers.setCanton(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON));
            tiers.setDesignation1(adresseData.getDesignation1_tiers());
            tiers.setDesignation2(adresseData.getDesignation2_tiers());

            return new AdressePaiement(banque, tiers, adresseData.getIdAvoirPaiementUnique());
        }

        return null;
    }
}

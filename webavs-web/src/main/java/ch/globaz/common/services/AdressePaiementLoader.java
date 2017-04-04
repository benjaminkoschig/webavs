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
import ch.globaz.common.business.exceptions.CommonTechnicalException;
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
                throw new CommonTechnicalException("Unabled to find adresse paiement for " + entry.getKey(), e);
            }
        }

        return adresses;
    }

    public AdressePaiement searchAdressePaiement(Long idTiersAdressePaiement, Long idApplicationAdressePaiement) {

        if (idTiersAdressePaiement == null) {
            throw new IllegalArgumentException("Unabled to find adresses, idTiersAdressePaiement is null");
        }

        if (idApplicationAdressePaiement == null) {
            throw new IllegalArgumentException("Unabled to find adresses, idApplicationAdressePaiement is null");
        }

        TIAdressePaiementData adresseData;
        try {
            adresseData = PRTiersHelper.getAdressePaiementData(session, null, idTiersAdressePaiement.toString(),
                    idApplicationAdressePaiement.toString(), null, null);

            return convertAdressePaiement(adresseData);
        } catch (Exception e) {
            throw new CommonTechnicalException("Unabled to find adresse paiement for " + idTiersAdressePaiement, e);
        }
    }

    private static AdressePaiement convertAdressePaiement(TIAdressePaiementData adresseData) {

        if (adresseData != null) {

            Banque banque = new Banque();
            banque.setAdresse1(adresseData.getLigneAdresse1_banque());
            banque.setAdresse2(adresseData.getLigneAdresse2_banque());
            banque.setAdresse3(adresseData.getLigneAdresse3_banque());
            banque.setAdresse4(adresseData.getLigneAdresse4_banque());
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
            banque.setClearing(adresseData.getClearing());

            Tiers tiers = new Tiers();
            tiers.setDesignation1(adresseData.getDesignation1_tiers());
            tiers.setDesignation2(adresseData.getDesignation2_tiers());
            tiers.setRue(adresseData.getRue());
            tiers.setLocalite(adresseData.getLocalite());
            tiers.setPaysIso(adresseData.getPaysIso());
            tiers.setPays(adresseData.getPays());
            tiers.setNumero(adresseData.getNumero());
            tiers.setNpa(adresseData.getNpa());
            tiers.setCasePostal(adresseData.getCasePostale());
            tiers.setTitre(adresseData.getTitre_tiers());

            return new AdressePaiement(banque, tiers, adresseData.getIdAvoirPaiementUnique());
        }

        return null;
    }
}

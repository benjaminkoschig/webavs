package ch.globaz.pegasus.businessimpl.utils.adresse;

import ch.globaz.pegasus.business.vo.adresse.AdressePaiement;
import ch.globaz.pegasus.business.vo.adresse.Banque;
import ch.globaz.pegasus.business.vo.adresse.Tiers;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

public class AdresseHandler {
    public static AdressePaiement convertAdressePaiement(AdresseTiersDetail adressePaiement) {
        Banque banque = new Banque();
        Tiers tiers = new Tiers();
        AdressePaiement adresse = new AdressePaiement();

        if (adressePaiement.getFields() != null) {
            banque.setAdresse1(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_BANQUE_ADRESSE1));
            banque.setAdresse2(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_BANQUE_ADRESSE2));
            banque.setAdresse3(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_BANQUE_ADRESSE3));
            banque.setAdresse4(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_BANQUE_ADRESSE4));
            banque.setCanton(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON));
            banque.setCantonCour(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_PAYS));
            banque.setCasePostal(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_CASE_POSTALE));
            banque.setCompte(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE));
            banque.setDesignation1(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1));
            banque.setDesignation2(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D2));
            banque.setLocalite(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_LOCALITE));
            banque.setNpa(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NPA));
            banque.setNumero(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NUMERO));
            banque.setPaysIso(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_PAYS_ISO));
            banque.setPays(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_PAYS));
            banque.setRue(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_RUE));
            banque.setCcp(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_CCP));

            tiers.setCanton(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CANTON));
            tiers.setDesignation1(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T1));
            tiers.setDesignation2(adressePaiement.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T2));

            adresse.setBanque(banque);
            adresse.setTiers(tiers);
        }
        return adresse;

    }
}

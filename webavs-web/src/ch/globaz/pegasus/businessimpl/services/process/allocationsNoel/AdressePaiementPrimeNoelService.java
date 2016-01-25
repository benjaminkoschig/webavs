package ch.globaz.pegasus.businessimpl.services.process.allocationsNoel;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import java.util.Date;
import ch.globaz.pyxis.business.model.AdressePaiementSimpleModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.common.Messages;
import ch.globaz.pyxis.exception.PyxisException;

public class AdressePaiementPrimeNoelService {
    private enum OPERATION {
        ADD
    }

    public final static String CS_CODE_MONNAIE_FRANC_SUISSE = "510002";

    public final static String CS_CODE_PAYS_SUISSE = "100";

    private TIAdressePaiement _adapt(AdresseTiersDetail model, String idTiers) throws Exception {
        TIAdressePaiement bean = new TIAdressePaiement(); // destination

        // Préconditions
        if (model == null) {
            // Préventif, ne devrait jamais arriver...
            throw new Exception(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        return bean;
    }

    private AdressePaiementSimpleModel _adapt(TIAdressePaiement bean) throws PyxisException {
        AdressePaiementSimpleModel model = new AdressePaiementSimpleModel();

        // Préconditions
        if (bean == null) {
            // Préventif, ne devrait jamais arriver...
            throw new PyxisException(Messages.MAPPING_ERROR + " - " + this.getClass().getName() + "._adapt(...)");
        }

        model.setId(bean.getId());
        model.setIdAdressePaiement(bean.getIdAdressePaiement());
        model.setIdAdressePmtUnique(bean.getIdAdressePmtUnique());
        model.setIdMonnaie(bean.getIdMonnaie());
        model.setIdPays(bean.getIdPays());
        model.setIdTiersAdresse(bean.getIdTiersAdresse());
        model.setIdTiersBanque(bean.getIdTiersBanque());
        // if (bean.getCreationSpy() != null) {
        model.setCreationSpy(bean.getCreationSpy().getFullData());
        // }
        model.setNumCcp(bean.getNumCcp());
        model.setNumCompteBancaire(bean.getNumCompteBancaireFormateIban());
        model.setSpy(bean.getSpy().getFullData());
        return model;
    }

    public AdressePaiementSimpleModel create(String idAdresse, String idTiers) throws PyxisException {
        if (JadeStringUtil.isBlankOrZero(idAdresse)) {
            throw new PyxisException("Unable to create the adress, the idAdresse passed is null!");
        }
        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new PyxisException("Unable to create the adress, the idTiers passed is null!");
        }
        TIAdressePaiement adressePaiement = new TIAdressePaiement(); // destination

        try {
            adressePaiement.setIdTiersAdresse(idTiers);
            adressePaiement.setIdAdresse(idAdresse);
            adressePaiement.setIdMonnaie(AdressePaiementPrimeNoelService.CS_CODE_MONNAIE_FRANC_SUISSE);
            adressePaiement.setIdPays(AdressePaiementPrimeNoelService.CS_CODE_PAYS_SUISSE);
            adressePaiement.add();

            TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
            avoirPaiement.setIdApplication(IPRConstantesExternes.TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL);
            avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
            avoirPaiement.setDateDebutRelation(JadeDateUtil.getDMYDate(new Date()));
            avoirPaiement.setIdTiers(idTiers);
            avoirPaiement.setSession(BSessionUtil.getSessionFromThreadContext());
            avoirPaiement.add();

        } catch (Exception e) {
            throw new PyxisException("Unalbel to create the adresse", e);
        }

        return this._adapt(adressePaiement);
    }
}

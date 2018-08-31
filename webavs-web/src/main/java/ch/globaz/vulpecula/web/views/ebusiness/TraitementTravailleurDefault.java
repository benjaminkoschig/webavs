package ch.globaz.vulpecula.web.views.ebusiness;

import ch.globaz.pyxis.business.model.BanqueSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.web.gson.BanqueInfoGSON;
import ch.globaz.vulpecula.ws.services.VulpeculaAbstractService;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.db.tiers.TIBanqueManager;
import globaz.pyxis.db.tiers.TIBanqueViewBean;
import globaz.pyxis.util.TIIbanFormater;

public class TraitementTravailleurDefault extends VulpeculaAbstractService {
    protected static final String CS_TYPE_DOMICILE = "508008";
    protected static final String CS_TYPE_COURRIER = "508001";
    protected static final String CS_DOMAINE_DEFAUT = "519004";

    protected String traitementIban(BanqueInfoGSON banqueGSON) throws Exception {
        String clearing = "";
        TIIbanFormater ibanFormater = new TIIbanFormater();
        String ibanUnformatted = null;
        try {
            ibanUnformatted = ibanFormater.check(banqueGSON.getIban());
            // la banque n'est pas connue. nous faisons une tentative pour la trouvée à
            // du compte iban non formaté, position 4..8

            if (ibanUnformatted.length() < 10) {
                throw new Exception("IBAN Incorrect");
            }
            clearing = ibanUnformatted.substring(4, 9);
        } catch (Exception e) {
            addError(e.getMessage(), e);
        }
        boolean findSiegeOnly = false;
        if (!JadeStringUtil.isIntegerEmpty(clearing)) {
            if (banqueGSON.getIdLocalite() != null && !JadeStringUtil.isEmpty(banqueGSON.getIdLocalite())) {
                BanqueSearchComplexModel search = new BanqueSearchComplexModel();

                String idTiersBanque = String.valueOf(VulpeculaServiceLocator.getNouveauTravailleurService()
                        .findIdTiersBanqueByClearingAndIdLocalite(clearing, banqueGSON.getIdLocalite()));
                if (!JadeStringUtil.isEmpty(idTiersBanque)) {
                    return idTiersBanque;
                } else {
                    findSiegeOnly = true;
                }
            } else {
                findSiegeOnly = true;
            }

            // TODO Améliorer recherche banque
            // String test =
            // "SELECT * FROM WEBAVSS.TIBANQP INNER JOIN WEBAVSS.TITIERP ON
            // (WEBAVSS.TIBANQP.HTITIE=WEBAVSS.TITIERP.HTITIE) WHERE HUCLEA="+
            // clearing +"AND (HUNMJ - floor(HUNMJ/100) * 100)=0 AND HTINAC='2';";
            if (findSiegeOnly) {
                TIBanqueManager manager = new globaz.pyxis.db.tiers.TIBanqueManager();
                manager.setForClearing(clearing);
                manager.setForIncludeInactif(new Boolean(false));
                manager.setForSiegeOnly(new Boolean(true));
                manager.setSession(BSessionUtil.getSessionFromThreadContext());

                manager.find(1);

                if (manager.size() == 0) {
                    throw new Exception(
                            BSessionUtil.getSessionFromThreadContext().getLabel("AUCUNE_BANQUE_POUR_CLEARING"));
                } else {
                    // une seule banque trouvée
                    TIBanqueViewBean banque = (TIBanqueViewBean) manager.getEntity(0);
                    return banque.getIdTiersBanque();
                }
            }

        }

        return null;
    }

    protected void addError(String message, Exception ex) {
        BSessionUtil.getSessionFromThreadContext().addError(message);
        if (ex != null) {
            JadeLogger.error(ex, ex.getMessage());
        }
    }
}

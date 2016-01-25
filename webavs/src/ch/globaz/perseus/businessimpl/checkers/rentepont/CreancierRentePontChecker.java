/**
 * 
 */
package ch.globaz.perseus.businessimpl.checkers.rentepont;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSTypeCreance;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePont;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author MBO
 * 
 */
public class CreancierRentePontChecker extends PerseusAbstractChecker {

    public static void checkForCreate(CreancierRentePont creancierRentePont)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PerseusException {
        CreancierRentePontChecker.checkMandatory(creancierRentePont);
    }

    public static void checkForDelete(CreancierRentePont creancierRentePont) throws RentePontException {

    }

    public static void checkForUpdate(CreancierRentePont creancierRentePont)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PerseusException {
        CreancierRentePontChecker.checkMandatory(creancierRentePont);
    }

    private static void checkMandatory(CreancierRentePont creancierRentePont)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, PerseusException {
        // Voir si le tiers est déjà dans les créanciers
        if (!CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(
                creancierRentePont.getSimpleCreancierRentePont().getCsTypeCreance())) {
            CreancierRentePontSearchModel csm = new CreancierRentePontSearchModel();
            csm.setForIdRentePont(creancierRentePont.getSimpleCreancierRentePont().getIdRentePont());
            csm.setForIdTiers(creancierRentePont.getSimpleCreancierRentePont().getIdTiers());
            csm.setForNotIdCreancier(creancierRentePont.getId());
            if (PerseusServiceLocator.getCreancierRentePontService().count(csm) > 0) {
                JadeThread.logError(CreancierRentePontChecker.class.getName(),
                        "perseus.creancierRentePont.creancierRentePont.tiers.existdeja");
            }
        }

        // VOir si le montant attribué n'est pas plus grand que le montant déjà attribué
        RentePont rentePont = PerseusServiceLocator.getRentePontService().read(
                creancierRentePont.getSimpleCreancierRentePont().getIdRentePont());
        Float montantAttribuable = PerseusServiceLocator.getRentePontService().calculerRetro(rentePont);
        Float montantAttribue = new Float(0);
        CreancierRentePontSearchModel searchModel = new CreancierRentePontSearchModel();
        searchModel.setForIdRentePont(creancierRentePont.getSimpleCreancierRentePont().getIdRentePont());
        searchModel.setForNotIdCreancier(creancierRentePont.getId());
        searchModel = PerseusServiceLocator.getCreancierRentePontService().search(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            CreancierRentePont c = (CreancierRentePont) model;
            montantAttribue += Float.parseFloat(c.getSimpleCreancierRentePont().getMontantAccorde());
        }
        Float montantAttribuePourCreancier = new Float(0);
        if (!JadeStringUtil.isEmpty(creancierRentePont.getSimpleCreancierRentePont().getMontantAccorde())) {
            montantAttribuePourCreancier = Float.parseFloat(creancierRentePont.getSimpleCreancierRentePont()
                    .getMontantAccorde().replace("'", ""));
        }
        if (montantAttribue + montantAttribuePourCreancier > montantAttribuable) {
            JadeThread.logError(CreancierRentePontChecker.class.getName(),
                    "perseus.creancierRentePont.creancierRentePont.repartition.tomuch");
        }

    }
}

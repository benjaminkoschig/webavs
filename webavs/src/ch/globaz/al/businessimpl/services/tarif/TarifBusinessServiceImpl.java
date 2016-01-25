package ch.globaz.al.businessimpl.services.tarif;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALTarifComplexModelException;
import ch.globaz.al.business.models.tarif.CategorieTarifComplexSearchModel;
import ch.globaz.al.business.services.tarif.TarifBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

public class TarifBusinessServiceImpl implements TarifBusinessService {

    @Override
    public boolean isTarifCantonal(String csTarif) throws JadePersistenceException, JadeApplicationException {

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE, csTarif)) {
                return false;
            }
        } catch (Exception e) {
            throw new ALTarifComplexModelException(
                    "TarifBusinessServiceImpl#isTarifCantonal: Probl�me lors de la v�rification du code syst�me (csTarif) pass� en param�tre",
                    e);
        }
        CategorieTarifComplexSearchModel searchTarif = new CategorieTarifComplexSearchModel();
        searchTarif.setForLegislationTarif(ALCSTarif.LEGISLATION_CANTONAL);
        searchTarif.setForCategorieTarif(csTarif);
        searchTarif = ALImplServiceLocator.getCategorieTarifComplexModelService().search(searchTarif);
        return (searchTarif.getSize() > 0 ? true : false);
    }

}

package ch.globaz.pegasus.businessimpl.services.loader;

import ch.globaz.pegasus.business.constantes.EPCForfaitType;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.List;
import java.util.Set;
import ch.globaz.common.converter.ConvertValueEnum;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.pegasus.business.domaine.parametre.forfaitPrime.ForfaitPrimeAssuranceMaladie;
import ch.globaz.pegasus.business.domaine.parametre.forfaitPrime.ForfaitsPrimeAssuranceMaladie;
import ch.globaz.pegasus.business.domaine.parametre.forfaitPrime.TypePrime;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocalite;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

class ForfaitsPrimesAssuranceMaladieLoader {

    ConvertValueEnum<String, TypePrime> typConverter = new ConvertValueEnum<String, TypePrime>();
    private Set<String> idsLocalite;

    public ForfaitsPrimesAssuranceMaladieLoader(Set<String> idsLocalite) {
        this.idsLocalite = idsLocalite;
        typConverter.put("64049001", TypePrime.ADULTE);
        typConverter.put("64049002", TypePrime.JEUNE_ADULTE);
        typConverter.put("64049003", TypePrime.ENFANT);
    }

    public ForfaitsPrimeAssuranceMaladie load() {
        ForfaitPrimeAssuranceMaladieLocaliteSearch assuranceMaladieLocaliteSearch = new ForfaitPrimeAssuranceMaladieLocaliteSearch();
        assuranceMaladieLocaliteSearch.setForDateDebut("01.01.2000");
        assuranceMaladieLocaliteSearch.setForIdsLocalite(idsLocalite);
        assuranceMaladieLocaliteSearch.setForType(EPCForfaitType.LAMAL.getCode().toString());
        // ajout d'une année pour reprendre les forfais pour les decisions futures
        // la date de fin est obligatoire
        assuranceMaladieLocaliteSearch.setForDateFin(new Date().addYear(1).getSwissValue());

        assuranceMaladieLocaliteSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            assuranceMaladieLocaliteSearch = PegasusServiceLocator.getParametreServicesLocator()
                    .getForfaitPrimeAssuranceMaladieLocaliteService().search(assuranceMaladieLocaliteSearch);
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }

        List<ForfaitPrimeAssuranceMaladieLocalite> forfaits = PersistenceUtil
                .typeSearch(assuranceMaladieLocaliteSearch);

        return convert(forfaits);
    }

    private ForfaitsPrimeAssuranceMaladie convert(List<ForfaitPrimeAssuranceMaladieLocalite> forfaits) {
        ForfaitsPrimeAssuranceMaladie list = new ForfaitsPrimeAssuranceMaladie();
        for (ForfaitPrimeAssuranceMaladieLocalite model : forfaits) {
            list.addPrime(convert(model));
        }
        return list;
    }

    private ForfaitPrimeAssuranceMaladie convert(ForfaitPrimeAssuranceMaladieLocalite model) {
        ForfaitPrimeAssuranceMaladie forfaitPrimeAssuranceMaladie = new ForfaitPrimeAssuranceMaladie();
        forfaitPrimeAssuranceMaladie.setDateDebut(new Date(model.getSimpleForfaitPrimesAssuranceMaladie()
                .getDateDebut()));
        if (!JadeStringUtil.isBlankOrZero(model.getSimpleForfaitPrimesAssuranceMaladie().getDateFin())) {
            forfaitPrimeAssuranceMaladie.setDateFin(new Date(model.getSimpleForfaitPrimesAssuranceMaladie()
                    .getDateFin()));
        }
        forfaitPrimeAssuranceMaladie.setId(model.getSimpleLienZoneLocalite().getId());
        forfaitPrimeAssuranceMaladie.setIdLocalite(model.getSimpleLienZoneLocalite().getIdLocalite());
        forfaitPrimeAssuranceMaladie
                .setMontant(new Montant(model.getSimpleForfaitPrimesAssuranceMaladie().getMontantPrimeMoy()));
        forfaitPrimeAssuranceMaladie.setType(typConverter.convert(model.getSimpleForfaitPrimesAssuranceMaladie()
                .getCsTypePrime()));
        return forfaitPrimeAssuranceMaladie;
    }
}

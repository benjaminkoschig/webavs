/**
 * 
 */
package ch.globaz.vulpecula.repositoriesjade.ebusiness.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuSearchComplexModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.ebusiness.Synchronisation;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * @author sel
 * 
 */
public class SynchronisationEbuConverter implements
        DomaineConverterJade<Synchronisation, SynchronisationEbuComplexModel, SynchronisationEbuSimpleModel> {

    @Override
    public SynchronisationEbuSimpleModel convertToPersistence(Synchronisation synch) {

        SynchronisationEbuSimpleModel simpleModel = new SynchronisationEbuSimpleModel();
        simpleModel.setId(synch.getId());
        simpleModel.setIdDecompte(synch.getDecompte().getId());
        if (synch.getDateAjout() != null) {
            simpleModel.setDateAjout(synch.getDateAjout().getSwissValue());
        }
        if (synch.getDateSynchronisation() != null) {
            simpleModel.setDateSynchronisation(synch.getDateSynchronisation().getSwissValue());
        }

        simpleModel.setSpy(synch.getSpy());

        return simpleModel;
    }

    /**
     * @param listComplexModel
     * @param decompteSalaire
     * @return
     */
    // public List<Synchronisation> convertToDomain(List<SynchronisationEbuComplexModel> listComplexModel,
    // DecompteSalaire decompteSalaire) {
    // List<CodeErreurDecompteSalaire> list = new ArrayList<CodeErreurDecompteSalaire>();
    // for (CodeErreurDecompteSalaireComplexModel complexModel : listComplexModel) {
    // CodeErreurDecompteSalaire codeErreur = convertToDomain(complexModel, decompteSalaire);
    // list.add(codeErreur);
    // }
    // return list;
    // }

    @Override
    public Synchronisation convertToDomain(SynchronisationEbuComplexModel complexModel) {
        SynchronisationEbuSimpleModel simpleModel = complexModel.getSynchronisationEbuSimpleModel();
        Synchronisation synch = convertToDomain(simpleModel);

        return synch;
    }

    @Override
    public Synchronisation convertToDomain(SynchronisationEbuSimpleModel simpleModel) {
        if (JadeStringUtil.isEmpty(simpleModel.getId())) {
            return null;
        }
        Synchronisation synchronisation = new Synchronisation();
        synchronisation.setId(simpleModel.getId());

        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(simpleModel.getIdDecompte());
        synchronisation.setDecompte(decompte);
        if (!JadeStringUtil.isEmpty(simpleModel.getDateAjout())) {
            synchronisation.setDateAjout(new Date(simpleModel.getDateAjout()));
        }
        if (!JadeStringUtil.isEmpty(simpleModel.getDateSynchronisation())) {
            synchronisation.setDateSynchronisation(new Date(simpleModel.getDateSynchronisation()));
        }
        synchronisation.setSpy(simpleModel.getSpy());
        return synchronisation;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new SynchronisationEbuSearchComplexModel();
    }
}

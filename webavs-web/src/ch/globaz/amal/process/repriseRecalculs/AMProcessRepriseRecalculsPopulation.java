package ch.globaz.amal.process.repriseRecalculs;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;

public class AMProcessRepriseRecalculsPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<AMProcessRepriseRecalculsTaxationsEnum> {

    @Override
    @BusinessKey(unique = false, messageKey = "amal.")
    public String getBusinessKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<AMProcessRepriseRecalculsTaxationsEnum> getEnumForProperties() {
        return AMProcessRepriseRecalculsTaxationsEnum.class;
    }

    private String getNomCourtDocument(String noModeles) {
        String nomCourt = "";
        if (IAMCodeSysteme.AMDocumentModeles.ACREP10.getValue().equals(noModeles)) {
            nomCourt = IAMCodeSysteme.AMDocumentModeles.ACREP10.toString();
        } else if (IAMCodeSysteme.AMDocumentModeles.ACREP11.getValue().equals(noModeles)) {
            nomCourt = IAMCodeSysteme.AMDocumentModeles.ACREP11.toString();
        } else if (IAMCodeSysteme.AMDocumentModeles.ACREP12.getValue().equals(noModeles)) {
            nomCourt = IAMCodeSysteme.AMDocumentModeles.ACREP12.toString();
        } else if (IAMCodeSysteme.AMDocumentModeles.ACREP13.getValue().equals(noModeles)) {
            nomCourt = IAMCodeSysteme.AMDocumentModeles.ACREP13.toString();
        }

        return nomCourt;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {

        return null;
    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {
        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();
        try {
            // On commence par rechercher tout les subsides qui ont reçu un ACREP10+
            SimpleDetailFamilleSearch subsideWithACREP10PlusSearch = new SimpleDetailFamilleSearch();
            ArrayList<String> arrayNoModeles = new ArrayList<String>();
            arrayNoModeles.add(IAMCodeSysteme.AMDocumentModeles.ACREP10.getValue());
            arrayNoModeles.add(IAMCodeSysteme.AMDocumentModeles.ACREP11.getValue());
            arrayNoModeles.add(IAMCodeSysteme.AMDocumentModeles.ACREP12.getValue());
            arrayNoModeles.add(IAMCodeSysteme.AMDocumentModeles.ACREP13.getValue());
            subsideWithACREP10PlusSearch.setInNoModeles(arrayNoModeles);
            subsideWithACREP10PlusSearch.setForCodeActif(true);
            subsideWithACREP10PlusSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
                    subsideWithACREP10PlusSearch);

            for (JadeAbstractModel modelSubsideWithACREP10Plus : subsideWithACREP10PlusSearch.getSearchResults()) {
                SimpleDetailFamille subsideWithACREP10Plus = (SimpleDetailFamille) modelSubsideWithACREP10Plus;

                SimpleFamille membreFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                        subsideWithACREP10Plus.getIdFamille());

                JadeProcessEntity entite = new JadeProcessEntity();
                entite.setDescription(membreFamille.getNomPrenom() + " - "
                        + getNomCourtDocument(subsideWithACREP10Plus.getNoModeles()));
                entite.setIdRef(membreFamille.getIdContribuable());
                entite.setValue1(subsideWithACREP10Plus.getIdDetailFamille());
                entites.add(entite);
            }

            // // Iteration sur les subsides trouvés pour récupérer l'id contribuable
            // for (JadeAbstractModel modelSubsideWithACREP10Plus : subsideWithACREP10PlusSearch.getSearchResults()) {
            // SimpleDetailFamille subsideWithACREP10Plus = (SimpleDetailFamille) modelSubsideWithACREP10Plus;
            //
            // String anneeToProcess = subsideWithACREP10Plus.getAnneeHistorique();
            //
            // SimpleDetailFamilleSearch allSubsideFromContribuableSearch = new SimpleDetailFamilleSearch();
            // allSubsideFromContribuableSearch.setForIdContribuable(subsideWithACREP10Plus.getIdContribuable());
            // allSubsideFromContribuableSearch.setForAnneeHistorique(anneeToProcess);
            // allSubsideFromContribuableSearch.setOrderKey("orderByIdContIdDefaDesc");
            // allSubsideFromContribuableSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
            // allSubsideFromContribuableSearch);
            //
            // // Le premier subside est le dernier traité pour l'année, ça doit être l'ACREP10+, sinon on vérifie que
            // // ce n'est pas un DECMST5 ou DECMST10+
            // for (JadeAbstractModel modelAllSubsideFromContribuableSearch : allSubsideFromContribuableSearch
            // .getSearchResults()) {
            // SimpleDetailFamille allSubsideFromContribuable = (SimpleDetailFamille)
            // modelAllSubsideFromContribuableSearch;
            //
            // if (IAMCodeSysteme.AMDocumentModeles.ACREP10.getValue().equals(
            // allSubsideFromContribuable.getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.ACREP11.getValue().equals(
            // allSubsideFromContribuable.getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.ACREP12.getValue().equals(
            // allSubsideFromContribuable.getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.ACREP13.getValue().equals(
            // allSubsideFromContribuable.getNoModeles())) {
            // // On prend, il a un ACREP10+ comme dernier document
            // SimpleFamille membreFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
            // allSubsideFromContribuable.getIdFamille());
            //
            // JadeProcessEntity entite = new JadeProcessEntity();
            // entite.setDescription(membreFamille.getNomPrenom() + " - "
            // + this.getNomCourtDocument(allSubsideFromContribuable.getNoModeles()));
            // entite.setIdRef(membreFamille.getIdContribuable());
            // entite.setValue1("");
            // entites.add(entite);
            // break;
            // } else if (IAMCodeSysteme.AMDocumentModeles.DECMST5.equals(allSubsideFromContribuable
            // .getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.DECMST10.equals(allSubsideFromContribuable
            // .getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.DECMST11.equals(allSubsideFromContribuable
            // .getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.DECMST12.equals(allSubsideFromContribuable
            // .getNoModeles())
            // || IAMCodeSysteme.AMDocumentModeles.DECMST13.equals(allSubsideFromContribuable
            // .getNoModeles())) {
            // // On ne prend pas, la décision a déjà été prise
            // break;
            // }
            //
            // }
            // }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            throw new JadePersistenceException(e.getMessage());
        }

        return entites;

    }

    @Override
    public void setProperties(Map<AMProcessRepriseRecalculsTaxationsEnum, String> hashMap) {
        // TODO Auto-generated method stub

    }
}

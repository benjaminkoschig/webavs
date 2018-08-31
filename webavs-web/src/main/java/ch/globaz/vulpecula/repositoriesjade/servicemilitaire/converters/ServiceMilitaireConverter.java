package ch.globaz.vulpecula.repositoriesjade.servicemilitaire.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.ServiceMilitaireComplexModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.ServiceMilitaireSearchSimpleModel;
import ch.globaz.vulpecula.business.models.servicemilitaire.ServiceMilitaireSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.servicemilitaire.GenreSM;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.Journal;
import ch.globaz.vulpecula.external.repositoriesjade.musca.converters.PassageConverter;
import ch.globaz.vulpecula.external.repositoriesjade.osiris.converters.JournalConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.PosteTravailConverter;

public class ServiceMilitaireConverter implements
        DomaineConverterJade<ServiceMilitaire, ServiceMilitaireComplexModel, ServiceMilitaireSimpleModel> {
    private static final ServiceMilitaireConverter INSTANCE = new ServiceMilitaireConverter();

    public static ServiceMilitaireConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public ServiceMilitaire convertToDomain(ServiceMilitaireComplexModel serviceMilitaireComplexModel) {
        ServiceMilitaireSimpleModel serviceMilitaireSimpleModel = serviceMilitaireComplexModel
                .getServiceMilitaireSimpleModel();
        PosteTravailComplexModel posteTravailComplexModel = serviceMilitaireComplexModel.getPosteTravailComplexModel();
        PassageModel passageModel = serviceMilitaireComplexModel.getPassageModel();
        JournalSimpleModel journalSimpleModel = serviceMilitaireComplexModel.getJournalSimpleModel();

        PosteTravail posteTravail = PosteTravailConverter.getInstance().convertToDomain(posteTravailComplexModel);
        Passage passage = PassageConverter.getInstance().convertToDomain(passageModel);
        Journal journal = JournalConverter.getInstance().convertToDomain(journalSimpleModel);

        ServiceMilitaire serviceMilitaire = convertToDomain(serviceMilitaireSimpleModel);
        serviceMilitaire.setPosteTravail(posteTravail);
        serviceMilitaire.setPassage(passage);
        serviceMilitaire.setJournal(journal);
        return serviceMilitaire;
    }

    @Override
    public ServiceMilitaireSimpleModel convertToPersistence(ServiceMilitaire serviceMilitaire) {
        ServiceMilitaireSimpleModel serviceMilitaireSimpleModel = new ServiceMilitaireSimpleModel();
        serviceMilitaireSimpleModel.setId(serviceMilitaire.getId());
        serviceMilitaireSimpleModel.setIdPosteTravail(serviceMilitaire.getIdPosteTravail());
        serviceMilitaireSimpleModel.setGenre(serviceMilitaire.getGenre().getValue());
        serviceMilitaireSimpleModel.setDateDebut(serviceMilitaire.getDateDebutAsString());
        serviceMilitaireSimpleModel.setDateFin(serviceMilitaire.getDateFinAsString());
        serviceMilitaireSimpleModel.setBeneficiaire(serviceMilitaire.getBeneficiaire().getValue());
        serviceMilitaireSimpleModel.setEtat(serviceMilitaire.getEtat().getValue());
        serviceMilitaireSimpleModel.setNbJours(String.valueOf(serviceMilitaire.getNbJours()));
        serviceMilitaireSimpleModel.setNbHeuresJour(String.valueOf(serviceMilitaire.getNbHeuresParJour()));
        serviceMilitaireSimpleModel.setSalaireHoraire(serviceMilitaire.getSalaireHoraire().getValue());
        serviceMilitaireSimpleModel.setCouvertureAPG(serviceMilitaire.getCouvertureAPG().getValue());
        serviceMilitaireSimpleModel.setVersementAPG(serviceMilitaire.getVersementAPG().getValue());
        serviceMilitaireSimpleModel.setCompensationAPG(serviceMilitaire.getCompensationAPG().getValue());
        serviceMilitaireSimpleModel.setMontantBrut(serviceMilitaire.getMontantBrut().getValue());
        serviceMilitaireSimpleModel.setMontantAVerser(serviceMilitaire.getMontantAVerser().getValue());
        serviceMilitaireSimpleModel.setIdPassageFacturation(serviceMilitaire.getIdPassageFacturation());
        serviceMilitaireSimpleModel.setBaseSalaire(serviceMilitaire.getBaseSalaire().getValue());
        serviceMilitaireSimpleModel.setTauxCP(serviceMilitaire.getTauxCP().getValue());
        serviceMilitaireSimpleModel.setTauxGratification(serviceMilitaire.getTauxGratification().getValue());
        serviceMilitaireSimpleModel.setSpy(serviceMilitaire.getSpy());
        if (serviceMilitaire.getDateVersement() != null) {
            serviceMilitaireSimpleModel.setDateVersement(serviceMilitaire.getDateVersement().getSwissValue());
        }
        return serviceMilitaireSimpleModel;
    }

    @Override
    public ServiceMilitaire convertToDomain(ServiceMilitaireSimpleModel serviceMilitaireSimpleModel) {
        ServiceMilitaire serviceMilitaire = new ServiceMilitaire();
        serviceMilitaire.setId(serviceMilitaireSimpleModel.getId());
        serviceMilitaire.setGenre(GenreSM.fromValue(serviceMilitaireSimpleModel.getGenre()));
        serviceMilitaire.setPeriode(new Periode(serviceMilitaireSimpleModel.getDateDebut(), serviceMilitaireSimpleModel
                .getDateFin()));
        serviceMilitaire.setBeneficiaire(Beneficiaire.fromValue(serviceMilitaireSimpleModel.getBeneficiaire()));
        serviceMilitaire.setEtat(Etat.fromValue(serviceMilitaireSimpleModel.getEtat()));
        serviceMilitaire.setNbJours(Double.parseDouble(serviceMilitaireSimpleModel.getNbJours()));
        serviceMilitaire.setNbHeuresParJour(Double.parseDouble(serviceMilitaireSimpleModel.getNbHeuresJour()));
        serviceMilitaire.setSalaireHoraire(new Montant(serviceMilitaireSimpleModel.getSalaireHoraire()));
        serviceMilitaire.setCouvertureAPG(new Taux(serviceMilitaireSimpleModel.getCouvertureAPG()));
        serviceMilitaire.setVersementAPG(new Montant(serviceMilitaireSimpleModel.getVersementAPG()));
        serviceMilitaire.setCompensationAPG(new Montant(serviceMilitaireSimpleModel.getCompensationAPG()));
        serviceMilitaire.setMontantBrut(new Montant(serviceMilitaireSimpleModel.getMontantBrut()));
        serviceMilitaire.setMontantAVerser(new Montant(serviceMilitaireSimpleModel.getMontantAVerser()));
        serviceMilitaire.setIdPassageFacturation(serviceMilitaireSimpleModel.getIdPassageFacturation());
        serviceMilitaire.setBaseSalaire(new Montant(serviceMilitaireSimpleModel.getBaseSalaire()));
        serviceMilitaire.setTauxCP(new Taux(serviceMilitaireSimpleModel.getTauxCP()));
        serviceMilitaire.setTauxGratification(new Taux(serviceMilitaireSimpleModel.getTauxGratification()));
        serviceMilitaire.setSpy(serviceMilitaireSimpleModel.getSpy());
        if (!JadeStringUtil.isEmpty(serviceMilitaireSimpleModel.getDateVersement())) {
            serviceMilitaire.setDateVersement(new Date(serviceMilitaireSimpleModel.getDateVersement()));
        }

        return serviceMilitaire;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new ServiceMilitaireSearchSimpleModel();
    }
}

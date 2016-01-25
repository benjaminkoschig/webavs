package ch.globaz.vulpecula.repositoriesjade.congepaye.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeComplexModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeSearchSimpleModel;
import ch.globaz.vulpecula.business.models.congepaye.CongePayeSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.Journal;
import ch.globaz.vulpecula.external.repositoriesjade.musca.converters.PassageConverter;
import ch.globaz.vulpecula.external.repositoriesjade.osiris.converters.JournalConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.PosteTravailConverter;

public class CongePayeConverter implements DomaineConverterJade<CongePaye, CongePayeComplexModel, CongePayeSimpleModel> {

    @Override
    public CongePaye convertToDomain(CongePayeComplexModel model) {
        CongePayeSimpleModel congePayeSimpleModel = model.getCongePayeSimpleModel();
        PosteTravailComplexModel posteTravailComplexModel = model.getPosteTravailComplexModel();
        PassageModel passageModel = model.getPassageModel();
        JournalSimpleModel journalSimpleModel = model.getJournalSimpleModel();

        CongePaye congePaye = convertToDomain(congePayeSimpleModel);
        PosteTravail posteTravail = PosteTravailConverter.getInstance().convertToDomain(posteTravailComplexModel);
        Passage passage = PassageConverter.getInstance().convertToDomain(passageModel);
        Journal journal = JournalConverter.getInstance().convertToDomain(journalSimpleModel);

        congePaye.setPosteTravail(posteTravail);
        congePaye.setPassage(passage);
        congePaye.setJournal(journal);

        return congePaye;
    }

    @Override
    public CongePayeSimpleModel convertToPersistence(CongePaye entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The parameter CongePaye is NULL !");
        }
        CongePayeSimpleModel congePayeSimpleModel = new CongePayeSimpleModel();
        congePayeSimpleModel.setId(entity.getId());
        congePayeSimpleModel.setSpy(entity.getSpy());
        if (entity.getAnneeDebut() != null) {
            congePayeSimpleModel.setAnneeDebut(String.valueOf(entity.getAnneeDebut().getValue()));
        }
        congePayeSimpleModel.setAnneeFin(String.valueOf(entity.getAnneeFin().getValue()));
        congePayeSimpleModel.setBeneficiaire(entity.getBeneficiaire().getValue());
        if (entity.getEtat() != null) {
            congePayeSimpleModel.setEtat(entity.getEtat().getValue());
        } else {
            congePayeSimpleModel.setEtat(Etat.SAISIE.getValue());
        }

        if (entity.getPosteTravail() != null) {
            congePayeSimpleModel.setIdPosteTravail(entity.getPosteTravail().getId());
        }

        congePayeSimpleModel.setIdPassageFacturation(entity.getIdPassageFacturation());
        if (entity.getDateSalaireNonDeclare() != null) {
            congePayeSimpleModel.setDateSalaireNonDeclare(entity.getDateSalaireNonDeclare().getSwissValue());
        }
        if (entity.getSalaireNonDeclare() != null) {
            congePayeSimpleModel.setSalaireNonDeclare(String.valueOf(entity.getSalaireNonDeclare().getValue()));
        }
        if (entity.getTauxCP() != null) {
            congePayeSimpleModel.setTauxCP(String.valueOf(entity.getTauxCP().getValueWith(5)));
        }
        congePayeSimpleModel.setTotalSalaire(entity.getTotalSalaire().getValue());
        congePayeSimpleModel.setSalaireDeclare(entity.getSalaireDeclare().getValue());
        congePayeSimpleModel.setMontantNet(entity.getMontantNet().getValue());
        return congePayeSimpleModel;
    }

    @Override
    public CongePaye convertToDomain(CongePayeSimpleModel simpleModel) {
        CongePaye congePaye = new CongePaye();
        congePaye.setId(simpleModel.getId());
        congePaye.setSpy(simpleModel.getSpy());
        congePaye.setAnneeDebut(new Annee(Integer.valueOf(simpleModel.getAnneeDebut())));
        congePaye.setAnneeFin(new Annee(Integer.valueOf(simpleModel.getAnneeFin())));
        congePaye.setBeneficiaire(Beneficiaire.fromValue(simpleModel.getBeneficiaire()));
        congePaye.setEtat(Etat.fromValue(simpleModel.getEtat()));
        congePaye.setSalaireDeclare(new Montant(simpleModel.getSalaireDeclare()));
        congePaye.setMontantNet(new Montant(simpleModel.getMontantNet()));

        if (simpleModel.getDateSalaireNonDeclare() != null && simpleModel.getDateSalaireNonDeclare().length() > 0) {
            congePaye.setDateSalaireNonDeclare(new Date(simpleModel.getDateSalaireNonDeclare()));
        }
        if (simpleModel.getSalaireNonDeclare() != null && simpleModel.getSalaireNonDeclare().length() > 0) {
            congePaye.setSalaireNonDeclare(new Montant(simpleModel.getSalaireNonDeclare()));
        }
        congePaye.setTauxCP(new Taux(simpleModel.getTauxCP()));
        congePaye.setIdPassageFacturation(simpleModel.getIdPassageFacturation());

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setId(simpleModel.getIdPosteTravail());
        congePaye.setPosteTravail(posteTravail);

        return congePaye;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new CongePayeSearchSimpleModel();
    }

}

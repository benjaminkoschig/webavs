package ch.globaz.vulpecula.repositoriesjade.absencejustifiee.converters;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeComplexModel;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeSearchSimpleModel;
import ch.globaz.vulpecula.business.models.absencejustifiee.AbsenceJustifieeSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.LienParente;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.models.osiris.Journal;
import ch.globaz.vulpecula.external.repositoriesjade.musca.converters.PassageConverter;
import ch.globaz.vulpecula.external.repositoriesjade.osiris.converters.JournalConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.PosteTravailConverter;

public class AbsenceJustifieeConverter implements
        DomaineConverterJade<AbsenceJustifiee, AbsenceJustifieeComplexModel, AbsenceJustifieeSimpleModel> {

    @Override
    public AbsenceJustifiee convertToDomain(AbsenceJustifieeComplexModel model) {
        AbsenceJustifieeSimpleModel absenceJustifieeSimpleModel = model.getAbsenceJustifieeSimpleModel();
        PosteTravailComplexModel posteTravailComplexModel = model.getPosteTravailComplexModel();
        PassageModel passageModel = model.getPassageModel();
        JournalSimpleModel journalSimpleModel = model.getJournalSimpleModel();

        AbsenceJustifiee absenceJustifiee = convertToDomain(absenceJustifieeSimpleModel);
        PosteTravail posteTravail = PosteTravailConverter.getInstance().convertToDomain(posteTravailComplexModel);
        Passage passage = PassageConverter.getInstance().convertToDomain(passageModel);
        Journal journal = JournalConverter.getInstance().convertToDomain(journalSimpleModel);

        absenceJustifiee.setPosteTravail(posteTravail);
        absenceJustifiee.setPassage(passage);
        absenceJustifiee.setJournal(journal);

        return absenceJustifiee;
    }

    @Override
    public AbsenceJustifieeSimpleModel convertToPersistence(AbsenceJustifiee entity) {
        AbsenceJustifieeSimpleModel absenceJustifieeSimpleModel = new AbsenceJustifieeSimpleModel();

        absenceJustifieeSimpleModel.setId(entity.getId());
        absenceJustifieeSimpleModel.setDateDebutAbsence(entity.getPeriode().getDateDebutAsSwissValue());
        absenceJustifieeSimpleModel.setDateFinAbsence(entity.getPeriode().getDateFinAsSwissValue());
        absenceJustifieeSimpleModel.setEtat(entity.getEtat().getValue());
        absenceJustifieeSimpleModel.setType(entity.getType().getValue());
        absenceJustifieeSimpleModel.setMontantBrut(String.valueOf(entity.getMontantBrut().getValue()));
        absenceJustifieeSimpleModel.setMontantVerse(String.valueOf(entity.getMontantVerse().getValue()));
        absenceJustifieeSimpleModel.setIdPosteTravail(entity.getIdPosteTravail());
        if (entity.getDateTraitementSalaires() != null) {
            absenceJustifieeSimpleModel.setDateTraitementSalaires(entity.getDateTraitementSalaires().getSwissValue());
        }
        absenceJustifieeSimpleModel.setTraitementSalaires(entity.getTraitementSalaires());
        absenceJustifieeSimpleModel.setSpy(entity.getSpy());
        if (entity.getTauxAVS() != null) {
            absenceJustifieeSimpleModel.setTauxAVS(String.valueOf(entity.getTauxAVS().getValue()));
        }
        if (entity.getTauxAC() != null) {
            absenceJustifieeSimpleModel.setTauxAC(String.valueOf(entity.getTauxAC().getValue()));
        }
        if (entity.getBeneficiaire() != null) {
            absenceJustifieeSimpleModel.setBeneficiaire(entity.getBeneficiaire().getValue());
        }
        if (entity.getLienParente() != null && entity.getLienParente() != LienParente.AUCUN) { // il n'y pas de code
                                                                                               // système pour le cas
                                                                                               // sans lien de
            // parenté (=> donc où l'AJ n'est pas pour un décès)
            absenceJustifieeSimpleModel.setLienParente(entity.getLienParente().getValue());
        }
        absenceJustifieeSimpleModel.setIdPassageFacturation(entity.getIdPassageFacturation());
        absenceJustifieeSimpleModel.setNombreDeJours(String.valueOf(entity.getNombreDeJours()));
        absenceJustifieeSimpleModel.setNombreHeuresParJour(String.valueOf(entity.getNombreHeuresParJour()));
        absenceJustifieeSimpleModel.setSalaireHoraire(String.valueOf(entity.getSalaireHoraire().getValue()));
        if (entity.getDateVersement() != null) {
            absenceJustifieeSimpleModel.setDateVersement(entity.getDateVersement().getSwissValue());
        }
        return absenceJustifieeSimpleModel;
    }

    @Override
    public AbsenceJustifiee convertToDomain(AbsenceJustifieeSimpleModel simpleModel) {
        AbsenceJustifiee absenceJustifiee = new AbsenceJustifiee();
        absenceJustifiee.setId(simpleModel.getId());
        absenceJustifiee.setType(TypeAbsenceJustifiee.fromValue(simpleModel.getType()));
        Periode periode = new Periode(simpleModel.getDateDebutAbsence(), simpleModel.getDateFinAbsence());
        absenceJustifiee.setPeriode(periode);
        absenceJustifiee.setEtat(Etat.fromValue(simpleModel.getEtat()));
        absenceJustifiee.setMontantBrut(new Montant(simpleModel.getMontantBrut()));
        absenceJustifiee.setMontantVerse(new Montant(simpleModel.getMontantVerse()));

        PosteTravail posteTravail = new PosteTravail();
        posteTravail.setId(simpleModel.getIdPosteTravail());
        absenceJustifiee.setPosteTravail(posteTravail);

        absenceJustifiee.setSpy(simpleModel.getSpy());

        absenceJustifiee.setTauxAVS(new Taux(simpleModel.getTauxAVS()));
        absenceJustifiee.setTauxAC(new Taux(simpleModel.getTauxAC()));
        absenceJustifiee.setBeneficiaire(Beneficiaire.fromValue(simpleModel.getBeneficiaire()));
        if (simpleModel.getLienParente().equals("0")) {
            absenceJustifiee.setLienParente(LienParente.AUCUN);
        } else {
            absenceJustifiee.setLienParente(LienParente.fromValue(simpleModel.getLienParente()));
        }
        absenceJustifiee.setIdPassageFacturation(simpleModel.getIdPassageFacturation());
        absenceJustifiee.setNombreDeJours(Double.valueOf(simpleModel.getNombreDeJours()));
        absenceJustifiee.setNombreHeuresParJour(Double.valueOf(simpleModel.getNombreHeuresParJour()));
        absenceJustifiee.setSalaireHoraire(new Montant(simpleModel.getSalaireHoraire()));

        if (!JadeStringUtil.isEmpty(simpleModel.getDateTraitementSalaires())) {
            absenceJustifiee.setDateTraitementSalaires(new Date(simpleModel.getDateTraitementSalaires()));
        }
        absenceJustifiee.setTraitementSalaires(simpleModel.getTraitementSalaires());

        if (!JadeStringUtil.isEmpty(simpleModel.getDateVersement())) {
            absenceJustifiee.setDateVersement(new Date(simpleModel.getDateVersement()));
        }

        return absenceJustifiee;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new AbsenceJustifieeSearchSimpleModel();
    }

}

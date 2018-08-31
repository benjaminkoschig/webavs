package ch.globaz.vulpecula.repositoriesjade.decompte.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComptableComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.MotifProlongation;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.hercule.InteretsMoratoires;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.repositoriesjade.musca.converters.PassageConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.EmployeurConverter;

/***
 * Convertisseur de {@link DecompteComplexModel}/{@link DecompteSimpleModel} Decompte <-> {@link Decompte}
 * 
 * @author Jonas Paratte (JPA) | Créé le 06.02.2014
 * 
 */
public final class DecompteConverter implements
        DomaineConverterJade<Decompte, DecompteComplexModel, DecompteSimpleModel> {

    private static DecompteConverter INSTANCE = new DecompteConverter();

    public static DecompteConverter getInstance() {
        return INSTANCE;
    }

    /**
     * Conversion d'un {@link DecompteComplexModel} en un {@link Decompte} où
     * les relations 1-1 ont été chargées.
     * 
     * @param employeurComplexModel
     *            {@link DecompteComplexModel} représentant la structure global
     *            d'un objet {@link Decompte}
     * @return {@link Decompte} employeur
     */
    @Override
    public Decompte convertToDomain(final DecompteComplexModel decompteComplexModel) {
        EmployeurComplexModel employeurComplexModel = decompteComplexModel.getEmployeurComplexModel();
        DecompteSimpleModel decompteSimpleModel = decompteComplexModel.getDecompteSimpleModel();

        Decompte decompte = convertToDomain(decompteSimpleModel);
        Employeur employeur = EmployeurConverter.getInstance().convertToDomain(employeurComplexModel);

        decompte.setEmployeur(employeur);

        return decompte;
    }

    public Decompte convertToDomain(final DecompteComptableComplexModel decompteComptableComplexModel) {
        Employeur employeur = EmployeurConverter.getInstance().convertToDomain(
                decompteComptableComplexModel.getEmployeurComplexModel());
        Passage passage = PassageConverter.getInstance().convertToDomain(
                decompteComptableComplexModel.getPassageModel());
        Decompte decompte = convertToDomain(decompteComptableComplexModel.getDecompteSimpleModel());
        decompte.setEmployeur(employeur);
        decompte.setPassage(passage);
        return decompte;
    }

    /**
     * @param decompteSimpleModel
     * @return un décompte du domaine sur la base d'une décompte de la
     *         persistence JADE
     */
    @Override
    public Decompte convertToDomain(final DecompteSimpleModel decompteSimpleModel) {
        Decompte decompte = new Decompte();
        decompte.setSpy(decompteSimpleModel.getSpy());
        decompte.setId(decompteSimpleModel.getId());
        decompte.setIdPassageFacturation(decompteSimpleModel.getIdPassageFacturation());
        decompte.setIdRapportControle(decompteSimpleModel.getIdRapportControle());
        if (decompteSimpleModel.getDateEtablissement() != null) {
            decompte.setDateEtablissement(new Date(decompteSimpleModel.getDateEtablissement()));
        }
        if (Date.isValid(decompteSimpleModel.getDateRappel())) {
            decompte.setDateRappel(new Date(decompteSimpleModel.getDateRappel()));
        }
        if (Date.isValid(decompteSimpleModel.getDateReception())) {
            decompte.setDateReception(new Date(decompteSimpleModel.getDateReception()));
        }
        if (decompteSimpleModel.getMontantControle() != null) {
            decompte.setMontantVerse(new Montant(decompteSimpleModel.getMontantControle()));
        }
        PeriodeMensuelle periode = null;
        if (decompteSimpleModel.getPeriodeDebut() != null) {
            periode = new PeriodeMensuelle(decompteSimpleModel.getPeriodeDebut(), decompteSimpleModel.getPeriodeFin());
        }
        decompte.setPeriode(periode);
        if (decompteSimpleModel.getType() != null) {
            decompte.setType(TypeDecompte.fromValue(decompteSimpleModel.getType()));
        }
        if (decompteSimpleModel.getTypeProvenance() != null) {
            decompte.setTypeProvenance(TypeProvenance.fromValue(decompteSimpleModel.getTypeProvenance()));
        }
        if (decompteSimpleModel.getEtat() != null) {
            decompte.setEtat(EtatDecompte.fromValue(decompteSimpleModel.getEtat()));
        }
        if (decompteSimpleModel.getManuel() != null) {
            decompte.setManuel(decompteSimpleModel.getManuel());
        }
        if ((decompteSimpleModel.getMotifProlongation() != null)
                && (decompteSimpleModel.getMotifProlongation().length() > 0)
                && (!decompteSimpleModel.getMotifProlongation().equals("0"))) {
            decompte.setMotifProlongation(MotifProlongation.fromValue(decompteSimpleModel.getMotifProlongation()));
        }
        if (decompteSimpleModel.getNumeroDecompte() != null) {
            decompte.setNumeroDecompte(new NumeroDecompte(decompteSimpleModel.getNumeroDecompte()));
        }
        if (InteretsMoratoires.isValid(decompteSimpleModel.getInteretsMoratoires())) {
            decompte.setInteretsMoratoires(InteretsMoratoires.fromValue(decompteSimpleModel.getInteretsMoratoires()));
        }

        if (decompteSimpleModel.getControleAC2() != null) {
            decompte.setControleAC2(decompteSimpleModel.getControleAC2());
        }
        
        if (decompteSimpleModel.getTypeProvenance() != null) {
            decompte.setTypeProvenance(TypeProvenance.fromValue(decompteSimpleModel.getTypeProvenance()));
        }

        Employeur employeur = new Employeur();
        employeur.setId(decompteSimpleModel.getIdEmployeur());
        decompte.setEmployeur(employeur);
        decompte.setRemarqueRectification(decompteSimpleModel.getRemarqueRectification());
        if (decompteSimpleModel.getRectifie() != null) {
            decompte.setRectifie(decompteSimpleModel.getRectifie());
        }

        return decompte;
    }

    /**
     * Conversion d'un {@link Decompte} en un {@link DecompteSimpleModel} pouvant être manipulé en base de données.
     * 
     * @param decompte
     *            {@link Decompte} représentant une Décompte affilié
     * @return {@link DecompteSimpleModel} pouvant être manipulé en base de
     *         données
     */
    @Override
    public DecompteSimpleModel convertToPersistence(final Decompte decompte) {
        DecompteSimpleModel decompteSimpleModel = new DecompteSimpleModel();
        decompteSimpleModel.setId(decompte.getId());
        decompteSimpleModel.setSpy(decompte.getSpy());
        decompteSimpleModel.setEtat(decompte.getEtat().getValue());
        decompteSimpleModel.setDateEtablissement(decompte.getDateEtablissement().getSwissValue());
        if (decompte.getDateReception() != null) {
            decompteSimpleModel.setDateReception(decompte.getDateReception().getSwissValue());
        }
        if (decompte.getDateRappel() != null) {
            decompteSimpleModel.setDateRappel(decompte.getDateRappel().getSwissValue());
        }
        decompteSimpleModel.setIdEmployeur(decompte.getEmployeur().getId());
        decompteSimpleModel.setIdPassageFacturation(decompte.getIdPassageFacturation());
        decompteSimpleModel.setIdRapportControle(decompte.getIdRapportControle());
        if (decompte.getNumeroDecompte() != null) {
            decompteSimpleModel.setNumeroDecompte(decompte.getNumeroDecompte().toString());
        }
        if (decompte.getMontantControle() != null) {
            decompteSimpleModel.setMontantControle(String.valueOf(decompte.getMontantControle().getValue()));
        }
        if (decompte.getPeriode() != null) {
            decompteSimpleModel.setPeriodeDebut(decompte.getPeriode().getPeriodeDebutAsValue());
            decompteSimpleModel.setPeriodeFin(decompte.getPeriode().getPeriodeFinAsValue());
        }
        if (decompte.getType() != null) {
            decompteSimpleModel.setType(decompte.getType().getValue());
        }
        if (decompte.getEtat() != null) {
            decompteSimpleModel.setEtat(decompte.getEtat().getValue());
        }
        if (decompte.getInteretsMoratoires() != null) {
            decompteSimpleModel.setInteretsMoratoires(decompte.getInteretsMoratoires().getValue());
        }
        decompteSimpleModel.setRemarqueRectification(decompte.getRemarqueRectification());
        decompteSimpleModel.setManuel(decompte.isManuel());
        decompteSimpleModel.setControleAC2(decompte.getControleAC2());
        if (decompte.getMotifProlongation() != null) {
            decompteSimpleModel.setMotifProlongation(decompte.getMotifProlongation().getValue());
        }
        if (decompte.isRectifie()) {
            decompteSimpleModel.setRectifie(true);
        } else {
            decompteSimpleModel.setRectifie(false);
        }
        
        if (decompte.getTypeProvenance() != null) {
        	decompteSimpleModel.setTypeProvenance(decompte.getTypeProvenance().getValue());
        }
        
        return decompteSimpleModel;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new DecompteSearchSimpleModel();
    }
}

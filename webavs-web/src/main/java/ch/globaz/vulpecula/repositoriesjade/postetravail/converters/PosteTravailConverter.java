/**
 * 
 */
package ch.globaz.vulpecula.repositoriesjade.postetravail.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSearchSimpleModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/***
 * Convertisseur de {@link PosteTravailSimpleModel} PosteTravail <-> {@link PosteTravail}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public final class PosteTravailConverter implements
        DomaineConverterJade<PosteTravail, PosteTravailComplexModel, PosteTravailSimpleModel> {

    private static final PosteTravailConverter INSTANCE = new PosteTravailConverter();
    private static final Logger LOGGER = LoggerFactory.getLogger(DomaineConverterJade.class);

    public static PosteTravailConverter getInstance() {
        return INSTANCE;
    }

    /***
     * Constructeur privé afin de ne pas pouvoir instancier la classe
     */
    private PosteTravailConverter() {
    }

    /**
     * Conversion d'un objet {@link PosteTravail} en {@link PosteTravailSimpleModel}
     * 
     * @param posteTravail
     *            Objet métier {@link PosteTravail}
     * @return Représentation de l'objet en base de données
     */
    @Override
    public PosteTravailSimpleModel convertToPersistence(final PosteTravail posteTravail) {
        // TODO Gérer si PosteTravail est null, si PosteTravail.getId() est
        // null, si PosteTravail.getEmployeur()
        // est null, ....
        PosteTravailSimpleModel posteTravailSimpleModel = new PosteTravailSimpleModel();
        posteTravailSimpleModel.setId(String.valueOf(posteTravail.getId()));
        posteTravailSimpleModel.setPosteCorrelationId(posteTravail.getPosteCorrelationId());
        posteTravailSimpleModel.setIdEmployeur(String.valueOf(posteTravail.getEmployeur().getId()));
        posteTravailSimpleModel.setPosteFranchiseAVS(posteTravail.isFranchiseAVS());
        if (posteTravail.getPeriodeActivite().getDateDebut() != null) {
            posteTravailSimpleModel.setDebutActivite(posteTravail.getPeriodeActivite().getDateDebut().getSwissValue());
        }
        if (posteTravail.getPeriodeActivite().getDateFin() != null) {
            posteTravailSimpleModel.setFinActivite(posteTravail.getPeriodeActivite().getDateFin().getSwissValue());
        }
        if (posteTravail.getQualification() != null) {
            posteTravailSimpleModel.setQualification(posteTravail.getQualification().getValue());
        }
        posteTravailSimpleModel.setDateValiditeQualification(posteTravail.getDateValiditeQualificationAsSwissValue());

        posteTravailSimpleModel.setIdTravailleur(String.valueOf(posteTravail.getTravailleur().getId()));
        if (posteTravail.getTypeSalaire() != null) {
            posteTravailSimpleModel.setGenreSalaire(posteTravail.getTypeSalaire().getValue());
        }
        posteTravailSimpleModel.setDateValiditeTypeSalaire(posteTravail.getDateValiditeTypeSalaireAsSwissValue());

        posteTravailSimpleModel.setSpy(posteTravail.getSpy());
        return posteTravailSimpleModel;
    }

    /**
     * Conversion d'un objet {@link PosteTravailComplexModel} en {@link PosteTravail}
     * 
     * @param posteTravailComplexModel
     *            Représentation d'un poste de travail et de ses relations 1..1
     * @return Objet métier {@link PosteTravail}
     */
    @Override
    public PosteTravail convertToDomain(final PosteTravailComplexModel posteTravailComplexModel) {
        PosteTravailSimpleModel posteTravailSimpleModel = posteTravailComplexModel.getPosteTravailSimpleModel();
        EmployeurComplexModel employeurComplexModel = posteTravailComplexModel.getEmployeurComplexModel();
        TravailleurComplexModel travailleurComplexModel = posteTravailComplexModel.getTravailleurComplexModel();

        Employeur employeur = EmployeurConverter.getInstance().convertToDomain(employeurComplexModel);
        Travailleur travailleur = TravailleurConverter.getInstance().convertToDomain(travailleurComplexModel);

        PosteTravail poste = convertToDomain(posteTravailSimpleModel);

        poste.setEmployeur(employeur);
        poste.setTravailleur(travailleur);

        return poste;
    }

    /**
     * Conversion d'un objet {@link PosteTravailSimpleModel} en un objet du
     * domaine {@link PosteTravail}
     * 
     * @param posteTravailSimpleModel
     *            {@link PosteTravailSimpleModel} représentation d'un poste de
     *            travail en base de données
     * @return {@link PosteTravail} représentant un objet du domaine
     */
    @Override
    public PosteTravail convertToDomain(final PosteTravailSimpleModel posteTravailSimpleModel) {
        PosteTravail poste = new PosteTravail();

        poste.setId(posteTravailSimpleModel.getId());
        poste.setPosteCorrelationId(posteTravailSimpleModel.getPosteCorrelationId());

        poste.setFranchiseAVS(posteTravailSimpleModel.getPosteFranchiseAVS());

        String periodeDebut = posteTravailSimpleModel.getDebutActivite();
        String periodeFin = posteTravailSimpleModel.getFinActivite();

        Date dateDebut = null;
        if (Date.isValid(periodeDebut)) {
            dateDebut = new Date(periodeDebut);
        }
        Date dateFin = null;
        if (Date.isValid(periodeFin)) {
            dateFin = new Date(periodeFin);
        }
        Periode periodeActivite = null;
        try {
            if (dateDebut != null) {
                periodeActivite = new Periode(dateDebut, dateFin);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }

        poste.setPeriodeActivite(periodeActivite);
        try {
            if (posteTravailSimpleModel.getQualification() != null
                    && !posteTravailSimpleModel.getQualification().isEmpty()) {
                poste.setQualification(Qualification.fromValue(posteTravailSimpleModel.getQualification()));
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
        if (Date.isValid(posteTravailSimpleModel.getDateValiditeQualification())) {
            poste.setDateValiditeQualification(new Date(posteTravailSimpleModel.getDateValiditeQualification()));
        }
        try {
            if (posteTravailSimpleModel.getGenreSalaire() != null
                    && !posteTravailSimpleModel.getGenreSalaire().isEmpty()) {
                poste.setTypeSalaire(TypeSalaire.fromValue(posteTravailSimpleModel.getGenreSalaire()));
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
        if (Date.isValid(posteTravailSimpleModel.getDateValiditeTypeSalaire())) {
            poste.setDateValiditeTypeSalaire(new Date(posteTravailSimpleModel.getDateValiditeTypeSalaire()));
        }
        poste.setSpy(posteTravailSimpleModel.getSpy());

        return poste;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new PosteTravailSearchSimpleModel();
    }
}

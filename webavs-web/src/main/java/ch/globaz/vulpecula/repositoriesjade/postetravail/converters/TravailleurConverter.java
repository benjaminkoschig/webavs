package ch.globaz.vulpecula.repositoriesjade.postetravail.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PermisTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;
import ch.globaz.vulpecula.external.repositoriesjade.pyxis.converters.PersonneEtendueConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/***
 * Convertisseur de {@link TravailleurComplexModel} Travailleur <-> {@link Travailleur}
 * 
 */
public final class TravailleurConverter implements
        DomaineConverterJade<Travailleur, TravailleurComplexModel, TravailleurSimpleModel> {

    public static final TravailleurConverter INSTANCE = new TravailleurConverter();

    public static TravailleurConverter getInstance() {
        return INSTANCE;
    }

    /**
     * Conversion d'un {@link TravailleurComplexModel} en un objet métier {@link Travailleur}
     * 
     * @param travailleurComplexModel
     *            {@link TravailleurComplexModel} représentant la structure d'un
     *            travailleur depuis la base de données
     * @return {@link Travailleur} objet métier
     */
    @Override
    public Travailleur convertToDomain(final TravailleurComplexModel travailleurComplexModel) {
        PersonneEtendue personneEtendue = PersonneEtendueConverter.convertToDomain(travailleurComplexModel
                .getPersonneEtendueComplexModel());
        TravailleurSimpleModel travailleurSimpleModel = travailleurComplexModel.getTravailleurSimpleModel();
        Travailleur travailleur = new Travailleur(personneEtendue);
        setFields(travailleur, travailleurSimpleModel);
        return travailleur;
    }

    @Override
    public Travailleur convertToDomain(TravailleurSimpleModel travailleurSimpleModel) {
        Travailleur travailleur = new Travailleur();
        setFields(travailleur, travailleurSimpleModel);
        return travailleur;
    }

    public void setFields(Travailleur travailleur, TravailleurSimpleModel travailleurSimpleModel) {
        travailleur.setId(travailleurSimpleModel.getId());
        travailleur.setAnnonceMeroba(travailleurSimpleModel.getAnnonceMeroba());
        travailleur.setReferencePermis(travailleurSimpleModel.getReferencePermis());
        travailleur.setSpy(travailleurSimpleModel.getSpy());
        if (!JadeStringUtil.isEmpty(travailleurSimpleModel.getDateAnnonceMeroba())) {
            travailleur.setDateAnnonceMeroba(new Date(travailleurSimpleModel.getDateAnnonceMeroba()));
        }
        if (!JadeStringUtil.isEmpty(travailleurSimpleModel.getCorrelationId())) {
            travailleur.setCorrelationId(travailleurSimpleModel.getCorrelationId());
        }

        // Si le travailleur dispose d'une référence sur un permis de
        // travail, il doit posséder une référence de permis.
        if (!JadeNumericUtil.isEmptyOrZero(travailleurSimpleModel.getGenrePermisTravail())) {
            travailleur.setPermisTravail(PermisTravail.fromValue(travailleurSimpleModel.getGenrePermisTravail()));
        }
    }

    /**
     * Conversion d'un objet du domaine {@link Travailleur} en objet {@link TravailleurSimpleModel} *
     * 
     * @param travailleur
     *            {@link Travailleur} représentant un travailleur d'un employeur
     * @return {@link TravailleurSimpleModel}
     */
    @Override
    public TravailleurSimpleModel convertToPersistence(final Travailleur travailleur) {
        TravailleurSimpleModel travailleurSimpleModel = new TravailleurSimpleModel();
        travailleurSimpleModel.setId(String.valueOf(travailleur.getId()));
        travailleurSimpleModel.setIdTiers(String.valueOf(travailleur.getIdTiers()));
        travailleurSimpleModel.setAnnonceMeroba(travailleur.getAnnonceMeroba());
        if (travailleur.getDateAnnonceMeroba() != null) {
            travailleurSimpleModel.setDateAnnonceMeroba(travailleur.getDateAnnonceMeroba().getSwissValue());
        }
        if (travailleur.getPermisTravail() != null) {
            travailleurSimpleModel.setGenrePermisTravail(travailleur.getPermisTravail().getValue());
            travailleurSimpleModel.setReferencePermis(travailleur.getReferencePermis());
        } else {
            travailleurSimpleModel.setReferencePermis("");
        }
        if (!JadeStringUtil.isEmpty(travailleur.getCorrelationId())) {
            travailleurSimpleModel.setCorrelationId(travailleur.getCorrelationId());
        }

        travailleurSimpleModel.setSpy(travailleur.getSpy());
        return travailleurSimpleModel;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new TravailleurSearchSimpleModel();
    }
}

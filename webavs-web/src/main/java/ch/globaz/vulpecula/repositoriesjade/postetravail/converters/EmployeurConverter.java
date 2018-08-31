package ch.globaz.vulpecula.repositoriesjade.postetravail.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurParticulariteComplexModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurSimpleModel;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;
import ch.globaz.vulpecula.external.models.affiliation.Affilie;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.AffilieConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.registre.converters.ConventionConverter;

/***
 * Convertisseur de {@link EmployeurSimpleModel}/{@link EmployeurComplexModel} Employeur <-> {@link Employeur}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 20 déc. 2013
 * 
 */
public final class EmployeurConverter implements
        DomaineConverterJade<Employeur, EmployeurComplexModel, EmployeurSimpleModel> {
    private static final EmployeurConverter INSTANCE = new EmployeurConverter();

    public static EmployeurConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Employeur convertToDomain(EmployeurComplexModel employeurComplexModel) {
        EmployeurSimpleModel employeurSimpleModel = employeurComplexModel.getEmployeurSimpleModel();
        Affilie affilie = AffilieConverter.convertToDomain(employeurComplexModel.getAffiliationTiersComplexModel());

        Employeur employeur = new Employeur(affilie);
        employeur.setConvention(ConventionConverter.convertToDomain(employeurComplexModel
                .getAdministrationComplexModel()));
        if (!JadeNumericUtil.isEmptyOrZero(employeurSimpleModel.getTypeFacturation())) {
            employeur.setTypeFacturation(TypeFacturation.fromValue(employeurSimpleModel.getTypeFacturation()));
        }

        employeur.setBvr(employeurSimpleModel.getBvr());
        employeur.setSpy(employeurSimpleModel.getSpy());
        return employeur;
    }

    public Employeur convertToDomain(EmployeurParticulariteComplexModel employeurComplexModel) {
        EmployeurSimpleModel employeurSimpleModel = employeurComplexModel.getEmployeurSimpleModel();
        Affilie affilie = AffilieConverter.convertToDomain(employeurComplexModel.getAffiliationTiersComplexModel());

        Employeur employeur = new Employeur(affilie);
        employeur.setConvention(ConventionConverter.convertToDomain(employeurComplexModel
                .getAdministrationComplexModel()));
        if (!JadeStringUtil.isBlankOrZero(employeurSimpleModel.getTypeFacturation())) {
            employeur.setTypeFacturation(TypeFacturation.fromValue(employeurSimpleModel.getTypeFacturation()));
        }
        employeur.setBvr(employeurSimpleModel.getBvr());
        employeur.setSpy(employeurSimpleModel.getSpy());
        return employeur;
    }

    @Override
    public EmployeurSimpleModel convertToPersistence(Employeur employeur) {
        EmployeurSimpleModel employeurSimpleModel = new EmployeurSimpleModel();
        employeurSimpleModel.setId(String.valueOf(employeur.getId()));
        if (employeur.getTypeFacturation() != null) {
            employeurSimpleModel.setTypeFacturation(employeur.getTypeFacturation().getValue());
        }
        employeurSimpleModel.setBvr(employeur.isBvr());
        employeurSimpleModel.setSpy(employeur.getSpy());
        return employeurSimpleModel;
    }

    @Override
    public Employeur convertToDomain(EmployeurSimpleModel employeurSimpleModel) {
        Employeur employeur = new Employeur();
        employeur.setBvr(employeurSimpleModel.getBvr());
        employeur.setSpy(employeurSimpleModel.getSpy());
        return employeur;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new EmployeurSearchSimpleModel();
    }
}

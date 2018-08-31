package ch.globaz.vulpecula.repositoriesjade.controleemployeur.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.controleemployeur.ControleEmployeurComplexModel;
import ch.globaz.vulpecula.business.models.controleemployeur.ControleEmployeurSearchSimpleModel;
import ch.globaz.vulpecula.business.models.controleemployeur.ControleEmployeurSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.domain.models.controleemployeur.TypeControle;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.EmployeurConverter;

public class ControleEmployeurConverter implements
        DomaineConverterJade<ControleEmployeur, ControleEmployeurComplexModel, ControleEmployeurSimpleModel> {

    public static ControleEmployeurConverter INSTANCE = new ControleEmployeurConverter();

    public static DomaineConverterJade<ControleEmployeur, ControleEmployeurComplexModel, ControleEmployeurSimpleModel> getInstance() {
        return INSTANCE;
    }

    @Override
    public ControleEmployeur convertToDomain(ControleEmployeurComplexModel model) {
        EmployeurConverter employeurConverter = EmployeurConverter.getInstance();
        ControleEmployeur controleEmployeur = convertToDomain(model.getControleEmployeurSimpleModel());
        controleEmployeur.setEmployeur(employeurConverter.convertToDomain(model.getEmployeurComplexModel()));
        return controleEmployeur;
    }

    @Override
    public ControleEmployeurSimpleModel convertToPersistence(ControleEmployeur entity) {
        ControleEmployeurSimpleModel model = new ControleEmployeurSimpleModel();
        model.setId(entity.getId());
        model.setIdEmployeur(entity.getIdEmployeur());
        model.setIdUtilisateur(entity.getIdUtilisateur());
        model.setNumeroMeroba(entity.getNumeroMeroba());
        model.setMontant(entity.getMontant().getValue());
        if (entity.getDateAu() == null) {
            model.setDateAu(null);
        } else {
            model.setDateAu(entity.getDateAu().getSwissValue());
        }
        if (entity.getDateControle() == null) {
            model.setDateControle(null);
        } else {
            model.setDateControle(entity.getDateControle().getSwissValue());
        }
        if (entity.getType() != null) {
            model.setType(entity.getType().getValue());
        }
        model.setAutresMesures(entity.isAutresMesures());
        model.setSpy(entity.getSpy());
        return model;
    }

    @Override
    public ControleEmployeur convertToDomain(ControleEmployeurSimpleModel model) {
        ControleEmployeur controleEmployeur = new ControleEmployeur();
        controleEmployeur.setId(model.getId());
        controleEmployeur.setIdUtilisateur(model.getIdUtilisateur());
        controleEmployeur.setNumeroMeroba(model.getNumeroMeroba());
        controleEmployeur.setMontant(new Montant(model.getMontant()));
        if (model.getDateControle() != null && model.getDateControle().length() > 0) {
            controleEmployeur.setDateControle(new Date(model.getDateControle()));
        }
        if (model.getDateAu() != null && model.getDateAu().length() > 0) {
            controleEmployeur.setDateAu(new Date(model.getDateAu()));
        }
        if (!JadeNumericUtil.isEmptyOrZero(model.getType())) {
            controleEmployeur.setType(TypeControle.fromValue(model.getType()));
        }
        controleEmployeur.setAutresMesures(model.getAutresMesures());
        controleEmployeur.setSpy(model.getSpy());
        return controleEmployeur;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new ControleEmployeurSearchSimpleModel();
    }
}

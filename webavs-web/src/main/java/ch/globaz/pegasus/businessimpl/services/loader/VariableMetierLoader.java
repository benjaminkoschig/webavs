package ch.globaz.pegasus.businessimpl.services.loader;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;
import ch.globaz.common.domaine.Taux;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetier;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierTypeDeDonnee;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.variablemetier.SimpleVariableMetier;
import ch.globaz.pegasus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

class VariableMetierLoader {

    private List<ch.globaz.pegasus.business.models.variablemetier.VariableMetier> search() {
        VariableMetierSearch search = new VariableMetierSearch();
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        // search.setForDateValable(JACalendar.todayJJsMMsAAAA().substring(3));
        List<ch.globaz.pegasus.business.models.variablemetier.VariableMetier> varialbeMetiers = new ArrayList<ch.globaz.pegasus.business.models.variablemetier.VariableMetier>();
        try {
            varialbeMetiers = PersistenceUtil.typeSearch(PegasusServiceLocator.getVariableMetierService()
                    .search(search));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        } catch (JadePersistenceException e) {
            throw new RuntimeException(e);
        } catch (VariableMetierException e) {
            throw new RuntimeException(e);
        }
        return varialbeMetiers;
    }

    public VariablesMetier load() {
        List<ch.globaz.pegasus.business.models.variablemetier.VariableMetier> variablesMetier = search();
        VariablesMetier list = new VariablesMetier();
        for (ch.globaz.pegasus.business.models.variablemetier.VariableMetier variableMetier : variablesMetier) {
            VariableMetier variableMetier2 = converter(variableMetier.getSimpleVariableMetier());
            list.add(variableMetier2);
        }
        return list;
    }

    private VariableMetier converter(SimpleVariableMetier varMetier) {
        VariableMetier variableMetier = new VariableMetier();

        variableMetier.setType(VariableMetierType.fromValue(varMetier.getCsTypeVariableMetier()));
        variableMetier.setTypeDeDonnee(VariableMetierTypeDeDonnee.fromValue(varMetier.getTypeDeDonnee()));
        variableMetier.setDateDebut(new Date(varMetier.getDateDebut()));
        if (varMetier.getDateFin() != null && !varMetier.getDateFin().trim().isEmpty()
                && varMetier.getDateFin().trim().equals("0")) {
            variableMetier.setDateFin(new Date(varMetier.getDateFin()));
        }
        variableMetier.setId(varMetier.getId());
        switch (variableMetier.getTypeDeDonnee()) {
            case FRACTION:
                variableMetier
                        .setPart(new Part(varMetier.getFractionNumerateur(), varMetier.getFractionDenominateur()));
                break;
            case MONTANT:
                variableMetier.setMontant(new Montant(varMetier.getMontant()));
                break;
            case TAUX:
                variableMetier.setTaux(new Taux(varMetier.getTaux()));
                break;
            default:
                throw new RuntimeException(
                        "Il est impossible de détérminer le type de donnée pour la variable métier suivante: "
                                + variableMetier.toString());
        }
        return variableMetier;
    }
}

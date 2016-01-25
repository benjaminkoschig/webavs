package ch.globaz.corvus.business.services.models.rentesaccordees;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.RERenteJoinPeriodeServiceException;
import ch.globaz.corvus.business.models.echeances.REEnfantADiminuer;

public interface REDiminutionRenteEnfantService extends JadeApplicationService {

    /**
     * Retourne la liste des rentes, dont le bénéficiaire est un enfant (code prestation spécifiques) et a une période
     * d'étude se terminant dans le mois passé en paramètre.
     * 
     * @param mois
     *            au format MM.AAAA
     * @return Une liste, vide si aucune rente correspondant au critère
     */
    public List<REEnfantADiminuer> getRenteEnfantDontPeriodeFiniDansMois(String mois)
            throws RERenteJoinPeriodeServiceException, JadePersistenceException;
}

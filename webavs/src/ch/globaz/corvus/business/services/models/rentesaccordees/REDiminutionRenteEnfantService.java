package ch.globaz.corvus.business.services.models.rentesaccordees;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.RERenteJoinPeriodeServiceException;
import ch.globaz.corvus.business.models.echeances.REEnfantADiminuer;

public interface REDiminutionRenteEnfantService extends JadeApplicationService {

    /**
     * Retourne la liste des rentes, dont le b�n�ficiaire est un enfant (code prestation sp�cifiques) et a une p�riode
     * d'�tude se terminant dans le mois pass� en param�tre.
     * 
     * @param mois
     *            au format MM.AAAA
     * @return Une liste, vide si aucune rente correspondant au crit�re
     */
    public List<REEnfantADiminuer> getRenteEnfantDontPeriodeFiniDansMois(String mois)
            throws RERenteJoinPeriodeServiceException, JadePersistenceException;
}

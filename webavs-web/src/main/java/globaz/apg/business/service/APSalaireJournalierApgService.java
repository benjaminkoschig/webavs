package globaz.apg.business.service;

import globaz.apg.interfaces.SituationProfessionnelle;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.math.BigDecimal;

/**
 * Calcul le salaire journalier selon la situation professionnelle.<br/>
 * Cette donn�e permettra de calcul le montant journalier d'une APG avec le service
 * {@link APMontantJournalierApgService}.<br/>
 * <br/>
 * Se r�f�rer aux documents de la conf�d�ration (Prescriptions de calculs des allocations journali�res APG) pour les
 * formules de calculs
 * 
 * @author PBA
 */
public interface APSalaireJournalierApgService extends JadeApplicationService {

    /**
     * Calcul et retourne le salaire journalier du demandeur APG, selon sa situation professionnelle.
     * 
     * @param situationProfessionnelle
     *            la situation professionnelle du demandeur APG
     * @return le salaire journalier calcul� selon sa situation professionnelle
     */
    public BigDecimal getSalaireJournalier(SituationProfessionnelle situationProfessionnelle);
}

package globaz.apg.rapg.rules;

import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rule de base des Rule inter-APG permettant de vérifier que certains genre de service, couplés à certains type de
 * protection civile, n'exède pas un certain nombre de jours soldés sur une période donnée.
 * 
 * @author PBA
 */
public class RuleInterAPG extends Rule {

    private List<APGenreServiceAPG> genresServicesVoulus;
    private int nombreAnneeCouvert;
    private int nombreJoursSoldesMax;
    private List<APTypeProtectionCivile> typesProtectionCivile;

    public RuleInterAPG(String errorCode, boolean breakable, List<APGenreServiceAPG> genresServices,
            int nombreAnneeCouvert, int nombreJoursSoldesMax) {
        this(errorCode, breakable, genresServices, Arrays.asList(APTypeProtectionCivile.Indefini), nombreAnneeCouvert,
                nombreJoursSoldesMax);
    }

    public RuleInterAPG(String errorCode, boolean breakable, List<APGenreServiceAPG> genresServices,
            List<APTypeProtectionCivile> typesPC, int nombreAnneeCouvert, int nombreJoursSoldesMax) {
        super(errorCode, false);

        this.nombreAnneeCouvert = nombreAnneeCouvert;
        this.nombreJoursSoldesMax = nombreJoursSoldesMax;
        genresServicesVoulus = genresServices;
        typesProtectionCivile = typesPC;
    }

    @Override
    public final boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        try {
            if (isBonGenreService(champsAnnonce.getServiceType())) {

                if (!JadeStringUtil.isBlank(champsAnnonce.getReferenceNumber())
                        && (champsAnnonce.getReferenceNumber().length() < 4)) {
                    JadeThread.logError(this.getClass().getName(), "apg.rapg.referenceNumber.tooShort");

                    return false;
                }

                APTypeProtectionCivile typePC = getTypePCenFonctionDuRefNumber(champsAnnonce.getReferenceNumber());

                if (isBonTypeProtectionCivile(typePC)) {

                    String finPeriodeCouverte = champsAnnonce.getStartOfPeriod();
                    String debutPeriodeCouverte;
                    if (nombreAnneeCouvert == 0) {
                        debutPeriodeCouverte = "01.01.1970";
                    } else {
                        debutPeriodeCouverte = getDateMoinsXAnneeCiviles(finPeriodeCouverte, nombreAnneeCouvert);
                    }

                    List<String> csGenreService = new ArrayList<String>();
                    for (APGenreServiceAPG unGenre : genresServicesVoulus) {
                        csGenreService.add(unGenre.getCodeSysteme());
                    }
                    APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
                    manager.setLikeNumeroAvs(champsAnnonce.getInsurant());
                    manager.setForDroitContenuDansDateDebut(debutPeriodeCouverte);
                    manager.setForDroitContenuDansDateFin(finPeriodeCouverte);
                    manager.setForCsGenreServiceIn(csGenreService);
                    manager.setSession(getSession());
                    try {
                        manager.find();
                    } catch (Exception e) {
                        throwRuleExecutionException(e);
                    }
                    int nbJoursSoldes = 0;

                    if (!JadeStringUtil.isBlank(champsAnnonce.getNumberOfDays())) {
                        nbJoursSoldes += Integer.parseInt(champsAnnonce.getNumberOfDays());
                    }

                    for (int i = 0; i < manager.size(); i++) {
                        APDroitAPGJointTiers unDroit = (APDroitAPGJointTiers) manager.get(i);
                        APTypeProtectionCivile typePcDuDroit = getTypePCenFonctionDuRefNumber(unDroit
                                .getNumeroReference());
                        if (!unDroit.getIdDroit().equals(champsAnnonce.getIdDroit())
                                && !unDroit.getIdDroit().equals(champsAnnonce.getIdDroitParent())
                                && !JadeStringUtil.isBlank(unDroit.getNbrJourSoldes())
                                && (isBonTypeProtectionCivile(typePcDuDroit))) {
                            int nbJoursDuDroit = Integer.parseInt(unDroit.getNbrJourSoldes());
                            if (nbJoursDuDroit != -1) {
                                nbJoursSoldes += nbJoursDuDroit;
                            }
                        }
                    }
                    if (nbJoursSoldes > nombreJoursSoldesMax) {
                        return false;
                    }
                }
            }
        } catch (Exception exception) {
            throw new APRuleExecutionException(exception);
        }

        return true;
    }

    private boolean isBonGenreService(String genre) {
        for (APGenreServiceAPG unGenreVoulu : genresServicesVoulus) {
            if (unGenreVoulu.getCodePourAnnonce().equals(genre)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBonTypeProtectionCivile(APTypeProtectionCivile type) {
        for (APTypeProtectionCivile unTypeVoulu : typesProtectionCivile) {
            if (unTypeVoulu.equals(type)) {
                return true;
            }
        }
        return false;
    }
}

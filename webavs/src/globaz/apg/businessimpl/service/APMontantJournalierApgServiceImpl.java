package globaz.apg.businessimpl.service;

import globaz.apg.business.service.APMontantJournalierApgService;
import globaz.apg.module.calcul.constantes.IAPConstantes;
import globaz.apg.pojo.NombreEnfants;
import java.math.BigDecimal;

public class APMontantJournalierApgServiceImpl implements APMontantJournalierApgService {

    public APMontantJournalierApgServiceImpl() {
        super();
    }

    private BigDecimal ajouterIndemnitePourEnfant(BigDecimal indemniteJournaliere, NombreEnfants nombreEnfants) {
        return indemniteJournaliere.add(IAPConstantes.ALLOCATION_POUR_ENFANT.multiply(nombreEnfants.getNombreEnfant()));
    }

    @Override
    public BigDecimal getMontantJournalierApgCadresServiceLong(BigDecimal salaireJournalier, NombreEnfants nombreEnfants) {

        BigDecimal indemniteJournaliere = null;

        BigDecimal salaireJournalierNormaliser = normaliserSalaireJournalier(salaireJournalier);
        indemniteJournaliere = normaliserIndemniteJournaliere(salaireJournalierNormaliser);
        indemniteJournaliere = ajouterIndemnitePourEnfant(indemniteJournaliere, nombreEnfants);

        switch (nombreEnfants) {
            case SansEnfant:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_SANS_ENFANT) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_SANS_ENFANT;
                }
                break;
            case UnEnfant:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_1_ENFANT) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_1_ENFANT;
                }
                break;
            case DeuxEnfants:
            case DesTroisEnfants:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_2_ENFANT_OU_PLUS) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_2_ENFANT_OU_PLUS;
                } else if (indemniteJournaliere.compareTo(salaireJournalierNormaliser) > 0) {
                    if (salaireJournalierNormaliser
                            .compareTo(IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_2_ENFANT_OU_PLUS) > 0) {
                        indemniteJournaliere = salaireJournalierNormaliser;
                    } else {
                        indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_CADRE_SERVICE_LONG_2_ENFANT_OU_PLUS;
                    }
                }
                break;
        }

        return indemniteJournaliere;
    }

    @Override
    public BigDecimal getMontantJournalierApgMaternite(BigDecimal salaireJournalier) {
        BigDecimal indemniteJournaliere = null;

        BigDecimal salaireJournalierNormaliser = normaliserSalaireJournalier(salaireJournalier);
        indemniteJournaliere = normaliserIndemniteJournaliere(salaireJournalierNormaliser);

        return indemniteJournaliere;
    }

    @Override
    public BigDecimal getMontantJournalierApgRecrue(BigDecimal salaireJournalier, NombreEnfants nombreEnfants) {
        switch (nombreEnfants) {
            case SansEnfant:
                return new BigDecimal(IAPConstantes.GARANTIE_MINIMALE_GENERALE_SANS_ENFANT.doubleValue());
            default:
                return getMontantJournalierApgServiceNormal(salaireJournalier, nombreEnfants);
        }
    }

    @Override
    public BigDecimal getMontantJournalierApgServiceAvancement(BigDecimal salaireJournalier, NombreEnfants nombreEnfants) {

        BigDecimal indemniteJournaliere = null;

        BigDecimal salaireJournalierNormaliser = normaliserSalaireJournalier(salaireJournalier);
        indemniteJournaliere = normaliserIndemniteJournaliere(salaireJournalierNormaliser);
        indemniteJournaliere = ajouterIndemnitePourEnfant(indemniteJournaliere, nombreEnfants);

        switch (nombreEnfants) {
            case SansEnfant:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_SANS_ENFANT) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_SANS_ENFANT;
                }
                break;
            case UnEnfant:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_1_ENFANT) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_1_ENFANT;
                }
                break;
            case DeuxEnfants:
            case DesTroisEnfants:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_2_ENFANT_OU_PLUS) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_2_ENFANT_OU_PLUS;
                } else if (indemniteJournaliere.compareTo(salaireJournalierNormaliser) > 0) {
                    if (salaireJournalierNormaliser
                            .compareTo(IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_2_ENFANT_OU_PLUS) > 0) {
                        indemniteJournaliere = salaireJournalierNormaliser;
                    } else {
                        indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_SERVICE_AVANCEMENT_2_ENFANT_OU_PLUS;
                    }
                }
                break;
        }

        return indemniteJournaliere;
    }

    @Override
    public BigDecimal getMontantJournalierApgServiceNormal(BigDecimal salaireJournalier, NombreEnfants nombreEnfants) {

        BigDecimal indemniteJournaliere = null;

        BigDecimal salaireJournalierNormaliser = normaliserSalaireJournalier(salaireJournalier);
        indemniteJournaliere = normaliserIndemniteJournaliere(salaireJournalierNormaliser);
        indemniteJournaliere = ajouterIndemnitePourEnfant(indemniteJournaliere, nombreEnfants);

        switch (nombreEnfants) {
            case SansEnfant:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_GENERALE_SANS_ENFANT) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_GENERALE_SANS_ENFANT;
                }
                break;
            case UnEnfant:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_GENERALE_1_ENFANT) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_GENERALE_1_ENFANT;
                } else if (indemniteJournaliere.compareTo(salaireJournalierNormaliser) > 0) {
                    if (salaireJournalierNormaliser.compareTo(IAPConstantes.GARANTIE_MINIMALE_GENERALE_1_ENFANT) > 0) {
                        indemniteJournaliere = salaireJournalierNormaliser;
                    } else {
                        indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_GENERALE_1_ENFANT;
                    }
                }
                break;
            case DeuxEnfants:
            case DesTroisEnfants:
                if (indemniteJournaliere.compareTo(IAPConstantes.GARANTIE_MINIMALE_GENERALE_2_ENFANT_OU_PLUS) < 0) {
                    indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_GENERALE_2_ENFANT_OU_PLUS;
                } else if (indemniteJournaliere.compareTo(salaireJournalierNormaliser) > 0) {
                    if (salaireJournalierNormaliser
                            .compareTo(IAPConstantes.GARANTIE_MINIMALE_GENERALE_2_ENFANT_OU_PLUS) > 0) {
                        indemniteJournaliere = salaireJournalierNormaliser;
                    } else {
                        indemniteJournaliere = IAPConstantes.GARANTIE_MINIMALE_GENERALE_2_ENFANT_OU_PLUS;
                    }
                }
                break;
        }

        return indemniteJournaliere;
    }

    private BigDecimal normaliserIndemniteJournaliere(BigDecimal salaireJournalier) {

        if (salaireJournalier.compareTo(IAPConstantes.MONTANT_JOURNALIER_MAX) == 0) {
            return IAPConstantes.APG_JOURNALIERE_MAX;
        }
        // calcul selon le fascicule de la Confédération
        double apgJournaliere = ((8.0 * salaireJournalier.doubleValue()) + 0.9) / 10.0;
        // arrondi au 10 centimes
        return BigDecimal.valueOf(apgJournaliere).setScale(1, BigDecimal.ROUND_DOWN);
    }

    private BigDecimal normaliserSalaireJournalier(BigDecimal salaireJournalier) {
        if (salaireJournalier.compareTo(IAPConstantes.MONTANT_JOURNALIER_MAX) > 0) {
            return IAPConstantes.MONTANT_JOURNALIER_MAX;
        }
        return salaireJournalier.setScale(0, BigDecimal.ROUND_DOWN);
    }
}

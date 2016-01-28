package globaz.apg.module.calcul.salaire;

import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.module.calcul.constantes.IAPConstantes;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.tools.PRCalcul;
import java.math.BigDecimal;

public class APSalaireJournalierCalculateur {

    public static BigDecimal getSalaireJournalierMoyen(APSituationProfessionnelle situationProfessionnelle) {
        return APSalaireJournalierCalculateur.getSalaireJournalierMoyen(situationProfessionnelle, true);
    }

    public static BigDecimal getSalaireJournalierMoyen(APSituationProfessionnelle situationProfessionnelle,
            boolean avecPlaffonementEtArrondi) {
        BigDecimal montantJournalierMoyen = null;

        // pour un indépendant
        if (situationProfessionnelle.getIsIndependant().booleanValue()) {
            BigDecimal revenuIndependant = new BigDecimal(situationProfessionnelle.getRevenuIndependant());
            montantJournalierMoyen = revenuIndependant.divide(BigDecimal.valueOf(360.0), 10, BigDecimal.ROUND_DOWN);
        } else { // pour les employés
            // si le salaire versé n'est pas un pourcentage mais un montant fixe
            if (!JadeStringUtil.isBlank(situationProfessionnelle.getMontantVerse())
                    && (Double.parseDouble(situationProfessionnelle.getMontantVerse()) > 0.0)
                    && !situationProfessionnelle.getIsPourcentMontantVerse().booleanValue()) {

                BigDecimal montantVerse = new BigDecimal(situationProfessionnelle.getMontantVerse());

                // si périodicité horaire
                if (situationProfessionnelle.getPeriodiciteMontantVerse().equals(
                        IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
                    BigDecimal nbHeuresSemaine = new BigDecimal(situationProfessionnelle.getHeuresSemaine());
                    montantJournalierMoyen = montantVerse.multiply(nbHeuresSemaine).divide(BigDecimal.valueOf(7.0), 10,
                            BigDecimal.ROUND_DOWN);
                }

                // si périodicité mois
                else if (situationProfessionnelle.getPeriodiciteMontantVerse().equals(
                        IPRSituationProfessionnelle.CS_PERIODICITE_MOIS)) {
                    montantJournalierMoyen = montantVerse.divide(BigDecimal.valueOf(30.0), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité 4 semaines
                else if (situationProfessionnelle.getPeriodiciteMontantVerse().equals(
                        IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                    montantJournalierMoyen = montantVerse.divide(BigDecimal.valueOf(28.0), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité année
                else if (situationProfessionnelle.getPeriodiciteMontantVerse().equals(
                        IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                    montantJournalierMoyen = montantVerse.divide(BigDecimal.valueOf(360.0), 10, BigDecimal.ROUND_DOWN);
                }
            } else {

                BigDecimal revenuIntermediaire = BigDecimal.ZERO;
                BigDecimal montant = null;

                // si un salaire horaire
                if (!JadeStringUtil.isBlank(situationProfessionnelle.getSalaireHoraire())
                        && (Double.parseDouble(situationProfessionnelle.getSalaireHoraire()) > 0.0)) {
                    BigDecimal nbHeuresSemaine = new BigDecimal(situationProfessionnelle.getHeuresSemaine());
                    montant = new BigDecimal(situationProfessionnelle.getSalaireHoraire());
                    montant = montant.multiply(nbHeuresSemaine);
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(7.0), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si un salaire mensuel
                if (!JadeStringUtil.isBlank(situationProfessionnelle.getSalaireMensuel())
                        && (Double.parseDouble(situationProfessionnelle.getSalaireMensuel()) > 0.0)) {
                    montant = new BigDecimal(situationProfessionnelle.getSalaireMensuel());
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(30.0), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si autre salaire
                if (!JadeStringUtil.isBlank(situationProfessionnelle.getAutreSalaire())
                        && (Double.parseDouble(situationProfessionnelle.getAutreSalaire()) > 0.0)) {
                    montant = new BigDecimal(situationProfessionnelle.getAutreSalaire());

                    // si périodicité 4 semaines
                    if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES.equalsIgnoreCase(situationProfessionnelle
                            .getPeriodiciteAutreSalaire())) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(28.0), 10,
                                BigDecimal.ROUND_DOWN));
                    } else { // si périodicité année
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(360.0), 10,
                                BigDecimal.ROUND_DOWN));
                    }
                }

                // si autre rémunération
                if (!JadeStringUtil.isBlank(situationProfessionnelle.getAutreRemuneration())
                        && (Double.parseDouble(situationProfessionnelle.getAutreRemuneration()) > 0.0)) {
                    // si pourcentage, le montant considéré est le montant du revenu principal
                    if (situationProfessionnelle.getIsPourcentAutreRemun().booleanValue()) {
                        montant = new BigDecimal(revenuIntermediaire.toString());

                        revenuIntermediaire = revenuIntermediaire.add(new BigDecimal(JANumberFormatter.format(
                                PRCalcul.pourcentage100(montant.toString(),
                                        situationProfessionnelle.getAutreRemuneration()), 0.05, 2,
                                JANumberFormatter.NEAR)));
                    } else {
                        montant = new BigDecimal(situationProfessionnelle.getAutreRemuneration());

                        // si périodicité horaire
                        if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equalsIgnoreCase(situationProfessionnelle
                                .getPeriodiciteAutreRemun())) {
                            BigDecimal nbHeuresSemaine = new BigDecimal(situationProfessionnelle.getHeuresSemaine());
                            montant = montant.multiply(nbHeuresSemaine);
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(7.0), 10,
                                    BigDecimal.ROUND_DOWN));
                        }

                        // si périodicité mois
                        else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS
                                .equalsIgnoreCase(situationProfessionnelle.getPeriodiciteAutreRemun())) {
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(30.0), 10,
                                    BigDecimal.ROUND_DOWN));
                        }

                        // si périodicité 4 semaines
                        else if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES
                                .equalsIgnoreCase(situationProfessionnelle.getPeriodiciteAutreRemun())) {
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(28.0), 10,
                                    BigDecimal.ROUND_DOWN));
                        }

                        // si périodicité année
                        else if (IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE
                                .equalsIgnoreCase(situationProfessionnelle.getPeriodiciteAutreRemun())) {
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal(360.0), 10,
                                    BigDecimal.ROUND_DOWN));
                        }
                    }
                }

                // si un salaire nature
                if (!JadeStringUtil.isBlank(situationProfessionnelle.getSalaireNature())
                        && (Double.parseDouble(situationProfessionnelle.getSalaireNature()) > 0.0)) {

                    montant = new BigDecimal(situationProfessionnelle.getSalaireNature());

                    // si périodicité horaire
                    if (IPRSituationProfessionnelle.CS_PERIODICITE_HEURE.equalsIgnoreCase(situationProfessionnelle
                            .getPeriodiciteSalaireNature())) {
                        BigDecimal nbHeuresSemaine = new BigDecimal(situationProfessionnelle.getHeuresSemaine());
                        montant = montant.multiply(nbHeuresSemaine);
                        montantJournalierMoyen = revenuIntermediaire.add(montant.divide(new BigDecimal(7.0), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité mois
                    else if (IPRSituationProfessionnelle.CS_PERIODICITE_MOIS.equalsIgnoreCase(situationProfessionnelle
                            .getPeriodiciteSalaireNature())) {
                        montantJournalierMoyen = revenuIntermediaire.add(montant.divide(new BigDecimal(30.0), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité 4 semaines
                    else if (IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES
                            .equalsIgnoreCase(situationProfessionnelle.getPeriodiciteSalaireNature())) {
                        montantJournalierMoyen = revenuIntermediaire.add(montant.divide(new BigDecimal(28.0), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité année
                    else if (IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE.equalsIgnoreCase(situationProfessionnelle
                            .getPeriodiciteSalaireNature())) {
                        montantJournalierMoyen = revenuIntermediaire.add(montant.divide(new BigDecimal(360.0), 10,
                                BigDecimal.ROUND_DOWN));
                    }
                }

                // l'employeur ne verse qu'un pourcentage du salaire
                if ((Float.parseFloat(situationProfessionnelle.getMontantVerse()) > 0)
                        && situationProfessionnelle.getIsPourcentMontantVerse().booleanValue()) {

                    // multiplie par le % du salaire verse
                    montantJournalierMoyen = BigDecimal.valueOf(Double.parseDouble(JANumberFormatter.format(
                            PRCalcul.pourcentage100(revenuIntermediaire.toString(),
                                    situationProfessionnelle.getMontantVerse()), 0.05, 2, JANumberFormatter.NEAR)));
                } else {
                    montantJournalierMoyen = revenuIntermediaire;
                }
            }
        }

        if (avecPlaffonementEtArrondi && (montantJournalierMoyen.compareTo(IAPConstantes.MONTANT_JOURNALIER_MAX) > 0)) {
            return IAPConstantes.MONTANT_JOURNALIER_MAX;
        }
        if (avecPlaffonementEtArrondi) {
            return montantJournalierMoyen.add(BigDecimal.valueOf(0.99999)).setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return montantJournalierMoyen;
        }
    }
}
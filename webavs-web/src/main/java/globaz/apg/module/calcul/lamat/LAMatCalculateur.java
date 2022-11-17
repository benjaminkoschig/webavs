/*
 * Cr�� le 10 avr. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul.lamat;

import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.helpers.droits.APSituationProfessionnelleHelper;
import globaz.apg.module.calcul.APCalculParametresAMAT;
import globaz.apg.properties.APProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author bsc
 * 
 */
public class LAMatCalculateur {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    /**
     * Cr�e une nouvelle instance de la classe LAMatalculateur.
     */
    public LAMatCalculateur() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     * @param genreService
     *            DOCUMENT ME!
     * @param idDroit
     *            DOCUMENT ME!
     * @param revenuMoyenDeterminant
     *            DOCUMENT ME!
     * @param montantJournalier
     *            DOCUMENT ME!
     * @param dateDebutPrestation
     *            DOCUMENT ME!
     * @param dateFinPrestation
     *            DOCUMENT ME!
     * @param isAllocationMax
     *            DOCUMENT ME!
     * @param joursSupplementairesPrisEnCompte
     *            DOCUMENT ME!
     * @param lastPrestation
     *            DOCUMENT ME!
     * @param deductionComplementLamatPossible
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public BigDecimal calculerMontantLAMat(BSession session, BTransaction transaction, String genreService,
                                           String idDroit, String revenuMoyenDeterminant, String montantJournalier, String dateDebutPrestation,
                                           String dateFinPrestation, boolean isAllocationMax, int joursSupplementairesPrisEnCompte, APPrestation lastPrestation, boolean deductionComplementLamatPossible) throws Exception {

        APCalculParametresAMAT parametresCalcul = new APCalculParametresAMAT().getParametres(transaction,
                dateDebutPrestation);
        final BigDecimal montantMax = parametresCalcul.getMontantMax();
        final BigDecimal montantMin = parametresCalcul.getMontantMin();

        // on recalcule le revenu moyen determinant en tenant compte uniquement
        // situation prof.
        // qui ont la case LAMat cachee
        revenuMoyenDeterminant = retrieveRevenuMoyenDeterminantLAMat(session, transaction, idDroit).toString();

        // Pas de LAMat pour les prestations maternite avant 01.07.2001
        if (BSessionUtil.compareDateFirstLower(session, dateDebutPrestation, "01.07.2001")) {
            // throw new
            // Exception(session.getLabel("LAMAT_NON_ALLOUE_POUR_PRESTATIONS_MAT_AVANT_01072001"));
            return new BigDecimal(0);
        }

        // Arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
        BigDecimal mj = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(montantJournalier, 0.05, 2,
                JANumberFormatter.NEAR)));
        BigDecimal montantLAMat = null;

        // entre le 01.07.2001 et le 01.07.2005, ou si il n'y a pas de prime
        // AMat (du 99e au 112e jour)
        // le droit genevois s'applique.Comme, il n'y a pas de prestations AMat,
        // on se base sur
        // le revenu moyen determinant pour calculer la LAMat
        // aussi calculer comme �a pour les adoptions
        if ((BSessionUtil.compareDateFirstGreaterOrEqual(session, dateDebutPrestation, "01.07.2001") && BSessionUtil
                .compareDateFirstLower(session, dateDebutPrestation, "01.07.2005"))
                || (mj.compareTo(new BigDecimal("0.00")) == 0)) {

            // 80% du revenu moyen determinant arrondi au franc superieur,
            // arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
            BigDecimal rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(
                    revenuMoyenDeterminant, 1.0, 2, JANumberFormatter.SUP))).multiply(new BigDecimal("0.8"));
            rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(rmd80.toString(), 0.05, 2,
                    JANumberFormatter.NEAR)));

            // Ceci n'est utile que pour les cas ou il n'y a pas de prime AMat
            // (du 99e au 112e jour)
            // Avant le 01.01.2008, le montant maximum est de 237.6 CHF.
            // D�s le 01.01.2008, le montant maximum est de 280 CHF.
            // D�s le 01.01.2009, le montant minimal passe � 62 CHF.
            if (BSessionUtil.compareDateFirstLower(session, dateDebutPrestation, "01.01.2008")) {

                // si le 80% du revenu moyen determinant est inferieur a 54 CHF,
                // la LAMat vaut 54 CHF
                if ((rmd80.compareTo(montantMin) <= 0) && !isAllocationMax) {

                    montantLAMat = montantMin;

                    // si le 80% du revenu moyen determinant est compris entre
                    // 54 et 237.60 CHF, la
                    // LAMat vaut ce 80% du revenu moyen determinant
                } else if ((rmd80.compareTo(montantMin) > 0) && (rmd80.compareTo(new BigDecimal("237.60")) <= 0)
                        && !isAllocationMax) {

                    montantLAMat = rmd80;

                    // si le 80% du revenu moyen determinant est superieur a
                    // 237.60 CHF,
                    // on donne le maximum LAMat soit 237.60 CHF
                } else if ((rmd80.compareTo(new BigDecimal("237.60")) > 0) || isAllocationMax) {

                    montantLAMat = new BigDecimal("237.60");
                }

            } else if (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateDebutPrestation, "01.01.2008")
                    && BSessionUtil.compareDateFirstLower(session, dateDebutPrestation, "01.01.2009")) {
                // si le 80% du revenu moyen determinant est inferieur a 54 CHF,
                // la LAMat vaut 54 CHF
                if ((rmd80.compareTo(montantMin) <= 0) && !isAllocationMax) {

                    montantLAMat = montantMin;

                    // si le 80% du revenu moyen determinant est compris entre
                    // 54 et 280 CHF, la
                    // LAMat vaut ce 80% du revenu moyen determinant
                } else if ((rmd80.compareTo(montantMin) > 0) && (rmd80.compareTo(montantMax) <= 0) && !isAllocationMax) {

                    montantLAMat = rmd80;

                    // si le 80% du revenu moyen determinant est superieur a 280
                    // CHF,
                    // on donne le maximum LAMat soit 280 CHF
                } else if ((rmd80.compareTo(montantMax) > 0) || isAllocationMax) {

                    montantLAMat = montantMax;
                }
            } else {
                // si le 80% du revenu moyen determinant est inferieur a 62 CHF,
                // la LAMat vaut 62 CHF
                if ((rmd80.compareTo(new BigDecimal("62")) <= 0) && !isAllocationMax) {

                    montantLAMat = new BigDecimal("62");

                    // Adaptations des montants LAMat compl�ment 14 jours dans le cas d'une extension maternit�
                    // Les prestations f�d�rales devront �tre d�duites entre le 99e et le 112e jour.
                    if ((mj.compareTo(new BigDecimal("0.00")) == 0)) {
                        if (joursSupplementairesPrisEnCompte != 0 && deductionComplementLamatPossible && lastPrestation != null) {
                            montantLAMat = montantLAMat.subtract(new BigDecimal(lastPrestation.getMontantJournalier()));
                        }
                    }

                    // si le 80% du revenu moyen determinant est compris entre
                    // 62 et 280 CHF, la
                    // LAMat vaut ce 80% du revenu moyen determinant
                } else if ((rmd80.compareTo(new BigDecimal("62")) > 0) && (rmd80.compareTo(montantMax) <= 0)
                        && !isAllocationMax) {

                    montantLAMat = rmd80;

                    // Adaptations des montants LAMat compl�ment 14 jours salaires dans le cas d'une extension maternit�
                    // Les prestations f�d�rales devront �tre d�duites entre le 99e et le 112e jour.
                    if ((mj.compareTo(new BigDecimal("0.00")) == 0)) {
                        if (joursSupplementairesPrisEnCompte != 0 && deductionComplementLamatPossible && lastPrestation != null) {
                            montantLAMat = montantLAMat.subtract(new BigDecimal(lastPrestation.getMontantJournalier()));
                        }
                    }

                    // si le 80% du revenu moyen determinant est superieur a 280
                    // CHF,
                    // on donne le maximum LAMat soit 280 CHF
                } else if ((rmd80.compareTo(montantMax) > 0) || isAllocationMax) {

                    montantLAMat = montantMax;

                    // Adaptations des montants LAMat compl�ment 14 jours dans le cas d'une extension maternit�
                    // Les prestations f�d�rales devront �tre d�duites entre le 99e et le 112e jour.
                    if ((mj.compareTo(new BigDecimal("0.00")) == 0)) {
                        if (joursSupplementairesPrisEnCompte != 0 && deductionComplementLamatPossible && lastPrestation != null) {
                            montantLAMat = montantLAMat.subtract(new BigDecimal(lastPrestation.getMontantJournalier()));
                        }
                    }

                }
            }

            // d�s le 01.07.2005, on se base sur la prestation AMat (ici montant
            // journalier) pour calculer
            // la compensation selon LAMAt
            // Avant le 01.01.2008, le montant maximum est de 237.6 CHF.
            // D�s le 01.014.2008, le montant maximum est de 280 CHF.
        } else if (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateDebutPrestation, "01.07.2005")
                && BSessionUtil.compareDateFirstLower(session, dateDebutPrestation, "01.01.2008")) {

            // si le montant journalier est inferieur a 54 CHF, on compense a
            // hauteur de 54 CHF
            if ((mj.compareTo(montantMin) <= 0) && !isAllocationMax) {

                montantLAMat = montantMin.subtract(mj);

                // si le montant journalier est compris entre 54 et 172 CHF, on
                // ne compense rien
            } else if ((mj.compareTo(montantMin) > 0) && (mj.compareTo(new BigDecimal("172")) < 0) && !isAllocationMax) {

                montantLAMat = new BigDecimal(0);

                // si le montant journalier vaut 172 CHF, on calcul la
                // compensation LAMat en fonction du
                // 80% du revenu moyen determinant (au maximum 237.60)
            } else if ((mj.compareTo(new BigDecimal("172")) >= 0) || isAllocationMax) {

                // 80% du revenu moyen determinant arrondi au franc superieur,
                // arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
                BigDecimal rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(
                        revenuMoyenDeterminant, 1.0, 2, JANumberFormatter.SUP))).multiply(new BigDecimal("0.8"));
                rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(rmd80.toString(), 0.05, 2,
                        JANumberFormatter.NEAR)));

                // si le 80% du revenu moyen determinant est inferieur a 237.60
                // CHF, on donne
                // la difference entre le maximum AMat et le 80% du revenu moyen
                // determinant
                if ((rmd80.compareTo(new BigDecimal("237.60")) <= 0) && !isAllocationMax) {

                    montantLAMat = rmd80.subtract(new BigDecimal("172"));

                    // si le 80% du revenu moyen determinant est superieur a
                    // 237.60 CHF,
                    // on donne la difference entre le maximum AMat et le
                    // maximum LAMat
                } else if ((rmd80.compareTo(new BigDecimal("237.60")) > 0) || isAllocationMax) {

                    montantLAMat = new BigDecimal("237.60").subtract(new BigDecimal("172"));
                }
            }
        } else if (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateDebutPrestation, "01.01.2008")
                && BSessionUtil.compareDateFirstLower(session, dateDebutPrestation, "01.01.2009")) {
            // si le montant journalier est inferieur a 54 CHF, on compense a
            // hauteur de 54 CHF
            if ((mj.compareTo(montantMin) <= 0) && !isAllocationMax) {

                montantLAMat = montantMin.subtract(mj);

                // si le montant journalier est compris entre 54 et 172 CHF, on
                // ne compense rien
            } else if ((mj.compareTo(montantMin) > 0) && (mj.compareTo(new BigDecimal("172")) < 0) && !isAllocationMax) {

                montantLAMat = new BigDecimal(0);

                // si le montant journalier vaut 172 CHF, on calcul la
                // compensation LAMat en fonction du
                // 80% du revenu moyen determinant (au maximum 280)
            } else if ((mj.compareTo(new BigDecimal("172")) >= 0) || isAllocationMax) {

                // 80% du revenu moyen determinant arrondi au franc superieur,
                // arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
                BigDecimal rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(
                        revenuMoyenDeterminant, 1.0, 2, JANumberFormatter.SUP))).multiply(new BigDecimal("0.8"));
                rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(rmd80.toString(), 0.05, 2,
                        JANumberFormatter.NEAR)));

                // si le 80% du revenu moyen determinant est inferieur a 280
                // CHF, on donne
                // la difference entre le maximum AMat et le 80% du revenu moyen
                // determinant
                if ((rmd80.compareTo(montantMax) <= 0) && !isAllocationMax) {

                    montantLAMat = rmd80.subtract(new BigDecimal("172"));

                    // si le 80% du revenu moyen determinant est superieur a 280
                    // CHF,
                    // on donne la difference entre le maximum AMat et le
                    // maximum LAMat
                } else if ((rmd80.compareTo(montantMax) > 0) || isAllocationMax) {

                    montantLAMat = montantMax.subtract(new BigDecimal("172"));
                }
            }
        } else if (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateDebutPrestation, "01.01.2009")) {
            // si le montant journalier est inferieur a 62 CHF, on compense a
            // hauteur de 62 CHF
            if ((mj.compareTo(new BigDecimal("62")) <= 0) && !isAllocationMax) {

                montantLAMat = new BigDecimal("62").subtract(mj);

                // si le montant journalier est compris entre 62 et 196 CHF, on
                // ne compense rien
            } else if ((mj.compareTo(new BigDecimal("62")) > 0) && (mj.compareTo(new BigDecimal("196")) < 0)
                    && !isAllocationMax) {

                montantLAMat = new BigDecimal(0);

                // si le montant journalier vaut 196 CHF, on calcul la
                // compensation LAMat en fonction du
                // 80% du revenu moyen determinant (au maximum 280)
            } else if ((mj.compareTo(new BigDecimal("196")) >= 0) || isAllocationMax) {

                // 80% du revenu moyen determinant arrondi au franc superieur,
                // arrondi � 2 chiffres apr�s la virgule, � 5cts pr�s.
                BigDecimal rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(
                        revenuMoyenDeterminant, 1.0, 2, JANumberFormatter.SUP))).multiply(new BigDecimal("0.8"));
                rmd80 = new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(rmd80.toString(), 0.05, 2,
                        JANumberFormatter.NEAR)));

                // si le 80% du revenu moyen determinant est inferieur a 280
                // CHF, on donne
                // la difference entre le maximum AMat et le 80% du revenu moyen
                // determinant
                if ((rmd80.compareTo(montantMax) <= 0) && !isAllocationMax) {

                    montantLAMat = rmd80.subtract(new BigDecimal("196"));

                    // si le 80% du revenu moyen determinant est superieur a 280
                    // CHF,
                    // on donne la difference entre le maximum AMat et le
                    // maximum LAMat
                } else if ((rmd80.compareTo(montantMax) > 0) || isAllocationMax) {

                    montantLAMat = montantMax.subtract(new BigDecimal("196"));
                }
            }
        }

        // Le montant LAMat est <= 0
        if (montantLAMat.compareTo(new BigDecimal(0)) <= 0) {
            return new BigDecimal(0);
        } else {
            return montantLAMat;
        }
    }

    /**
     * Calcule le revenu moyen derterminant LAMat en se basant sur la situation prof. du droit.
     * 
     * @param idDroit
     */
    private FWCurrency retrieveRevenuMoyenDeterminantLAMat(BSession session, BTransaction transaction, String idDroit)
            throws Exception {

        FWCurrency revenuLAMat = new FWCurrency("0");

        APSituationProfessionnelleManager sitProManager = new APSituationProfessionnelleManager();
        sitProManager.setSession(session);
        sitProManager.setForIdDroit(idDroit);
        sitProManager.find(transaction);

        Iterator iter = sitProManager.iterator();
        while (iter.hasNext()) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) iter.next();

            // si LAMat on additionne le revenu journalier
            if (sitPro.getHasLaMatPrestations().booleanValue()) {
                revenuLAMat.add(APSituationProfessionnelleHelper.getSalaireJournalierVerse(sitPro, true));
            }
        }

        return revenuLAMat;
    }

}

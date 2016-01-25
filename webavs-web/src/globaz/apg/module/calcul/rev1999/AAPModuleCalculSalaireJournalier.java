/*
 * Créé le 27 avr. 05
 * 
 * Calcul du salaire journalier (Revenu déterminant moyen) selon la révision 1999
 */
package globaz.apg.module.calcul.rev1999;

import globaz.apg.exceptions.APRulesException;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APBaseCalculSalaireJournalier;
import globaz.apg.module.calcul.APBaseCalculSituationProfessionnel;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.APResultatCalculSituationProfessionnel;
import globaz.apg.module.calcul.APSituationProfessionnelleHelper;
import globaz.apg.module.calcul.exceptions.APBaseCalculDataMissingException;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description
 * 
 * @author scr
 */

public abstract class AAPModuleCalculSalaireJournalier {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param baseCalcul
     *            DOCUMENT ME!
     * @param refData
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected APResultatCalcul calculerMontantAllocation(APBaseCalcul baseCalcul, IAPReferenceDataPrestation refData)
            throws Exception {
        APResultatCalcul result = new APResultatCalcul();
        result.setAllocationJournaliereMaxFraisGarde(((APReferenceDataAPG) refData).getBKmax());

        if (baseCalcul.isAllocationExploitation()) {
            result.setAllocationJournaliereExploitation(((APReferenceDataAPG) refData).getBZ());
        }

        List listSitProfHelper = loadSituationProfessionnelleDataHelper(baseCalcul, refData);
        if (listSitProfHelper == null) {
            result.setRevenuDeterminantMoyen(new FWCurrency(0));
            result.setTL(new FWCurrency(0));
            return result;
        }

        // Salaire journalier
        FWCurrency TL = new FWCurrency(0);
        FWCurrency rmd = new FWCurrency(0);

        for (Iterator iter = listSitProfHelper.iterator(); iter.hasNext();) {
            APSituationProfessionnelleHelper element = (APSituationProfessionnelleHelper) iter.next();

            TL.add(element.getTL());
            rmd.add(element.getRevenuMoyenDeterminantSansArrondi());
        }

        // Initialisation des salaires par employeurs, et maj des taux calculé
        // au prorata par rapport à l'alloc.
        // journalière totale (TL)
        result = loadResultatSituationProfessionnelle(result, listSitProfHelper, rmd);

        // Arrondi au francs
        TL = new FWCurrency(JANumberFormatter.round(TL.getBigDecimalValue(), 1, 0, JANumberFormatter.INF).toString());

        result.setTL(TL);

        FWCurrency revDetMoy = result.getSalaireJournalierTotal();
        if (revDetMoy == null) {
            revDetMoy = new FWCurrency(0);
        }
        revDetMoy.add(result.getRevenuJournalierIndependantTotal());
        result.setRevenuDeterminantMoyen(revDetMoy);

        return result;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param situationProfessionnelle
     *            DOCUMENT ME!
     * @param refData
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws APRulesException
     *             DOCUMENT ME!
     */
    protected FWCurrency calculerSalaireJournalierVerse(APBaseCalculSituationProfessionnel situationProfessionnelle,
            IAPReferenceDataPrestation refData) throws APBaseCalculDataMissingException {
        APBaseCalculSalaireJournalier bcj = situationProfessionnelle.getVersementEmployeur();

        BigDecimal bTL = new BigDecimal(0);

        // Salaire horaire
        if ((bcj.getSalaireHoraire() != null) && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireHoraire().toString())) {
            BigDecimal L = bcj.getSalaireHoraire().getBigDecimalValue();

            if ((bcj.getNbrHeuresSemaine() == null)
                    || JadeStringUtil.isDecimalEmpty(bcj.getNbrHeuresSemaine().toString())) {
                throw new APBaseCalculDataMissingException(null, "Nbre heures/semaines non renseigné");
            }

            BigDecimal AS = bcj.getNbrHeuresSemaine();

            bTL = L.multiply(AS);
            bTL = bTL.divide(new BigDecimal(7), BigDecimal.ROUND_HALF_DOWN);
        }

        // Salaire journalier
        else if ((bcj.getSalaireJournalier() != null)
                && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireJournalier().toString())) {
            bTL = bcj.getSalaireJournalier().getBigDecimalValue();
        }
        // Salaire hebdomadaire
        else if ((bcj.getSalaireHebdomadaire() != null)
                && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireHebdomadaire().toString())) {
            bTL = bcj.getSalaireHebdomadaire().getBigDecimalValue();
            bTL = bTL.divide(new BigDecimal(7), 5, BigDecimal.ROUND_DOWN);
        }
        // Salaire 4 semaines
        else if ((bcj.getSalaire4Semaines() != null)
                && !JadeStringUtil.isDecimalEmpty(bcj.getSalaire4Semaines().toString())) {
            bTL = bcj.getSalaire4Semaines().getBigDecimalValue();
            bTL = bTL.divide(new BigDecimal(28), 5, BigDecimal.ROUND_DOWN);
        }
        // Salaire mensuel
        else if ((bcj.getSalaireMensuel() != null)
                && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireMensuel().toString())) {
            bTL = bcj.getSalaireMensuel().getBigDecimalValue();
            bTL = bTL.divide(new BigDecimal(30), 5, BigDecimal.ROUND_DOWN);
        }
        // Salaire annuel
        else if ((bcj.getSalaireAnnuel() != null) && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireAnnuel().toString())) {
            bTL = bcj.getSalaireAnnuel().getBigDecimalValue();
            bTL = bTL.divide(new BigDecimal(360), 5, BigDecimal.ROUND_DOWN);
        } else {
            throw new APBaseCalculDataMissingException(null, "Les données servant de base de calcul sont incomplètes");
        }

        // // TL>GE
        // BigDecimal tmp = ((APReferenceDataAPG)
        // refData).getGE().getBigDecimalValue();
        //
        // if (bTL.compareTo(tmp) == 1) {
        // // TL=GE
        // bTL = ((APReferenceDataAPG) refData).getGE().getBigDecimalValue();
        // }
        // // TL<=GE
        // else {
        // // TL = TL + 0.99999
        // bTL = bTL.add(new BigDecimal(0.99999));
        // }

        return new FWCurrency(bTL.toString());
    }

    /**
     * DOCUMENT ME!
     * 
     * @param result
     *            DOCUMENT ME!
     * @param salairesParEmployeur
     *            DOCUMENT ME!
     * @param salaireJournalierTotal
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected APResultatCalcul loadResultatSituationProfessionnelle(APResultatCalcul result, List salairesParEmployeur,
            FWCurrency salaireJournalierTotal) {
        BigDecimal cumulDesTaux = new BigDecimal(0);

        for (Iterator iter = salairesParEmployeur.iterator(); iter.hasNext();) {
            APSituationProfessionnelleHelper element = (APSituationProfessionnelleHelper) iter.next();

            BigDecimal tauxProRata = null;

            // Dernier element
            if (!iter.hasNext()) {
                tauxProRata = new BigDecimal(1);
                tauxProRata = tauxProRata.subtract(cumulDesTaux);
            } else {
                tauxProRata = element.getRevenuMoyenDeterminantSansArrondi().getBigDecimalValue();
                tauxProRata = tauxProRata.divide(salaireJournalierTotal.getBigDecimalValue(), 5,
                        BigDecimal.ROUND_HALF_DOWN);
            }

            cumulDesTaux = cumulDesTaux.add(tauxProRata);

            APResultatCalculSituationProfessionnel resultatSitProf = new APResultatCalculSituationProfessionnel();
            resultatSitProf.setIdAffilie(element.getIdAffilie());
            resultatSitProf.setIdTiers(element.getIdTiers());
            resultatSitProf.setSalaireJournalierNonArrondi(element.getSalaireJournalier());
            resultatSitProf.setTauxProRata(new FWCurrency(tauxProRata.toString(), 5));
            resultatSitProf.setVersementEmployeur(element.isVersementEmployeur());
            resultatSitProf.setSalaireJournalierVerse(element.getSalaireJournalierVerse());
            resultatSitProf.setNom(element.getNom());
            resultatSitProf.setIndependant(element.isIndependant());
            resultatSitProf.setTravailleurSansEmployeur(element.isTravailleurSansEmployeur());
            resultatSitProf.setCollaborateurAgricole(element.isCollaborateurAgricole());
            resultatSitProf.setTravailleurAgricole(element.isTravailleurAgricole());
            resultatSitProf.setSoumisCotisation(element.isSoumisCotisation());
            resultatSitProf.setIdSituationProfessionnelle(element.getIdSituationProfessionnelle());

            result.addResultatCalculSitProfessionnelle(resultatSitProf);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param baseCalcul
     *            DOCUMENT ME!
     * @param refData
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws APRulesException
     *             DOCUMENT ME!
     */
    protected List loadSituationProfessionnelleDataHelper(APBaseCalcul baseCalcul, IAPReferenceDataPrestation refData)
            throws APBaseCalculDataMissingException {
        List result = new ArrayList();

        if (baseCalcul.isSansActiviteLucrative()) {
            return null;
        }

        if (baseCalcul.getBasesCalculSituationProfessionnel() == null) {
            return null;
        }

        // Itération sur toutes les bases de calculs pour totaliser le salaire
        // journalier.
        for (Iterator iter = baseCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {
            BigDecimal bTL = new BigDecimal(0);

            APBaseCalculSituationProfessionnel bcSitProf = (APBaseCalculSituationProfessionnel) iter.next();
            APSituationProfessionnelleHelper helper = new APSituationProfessionnelleHelper();

            // Calcul du salaire journalier verse par l'employeur
            if (bcSitProf.getVersementEmployeur() != null) {
                helper.setSalaireJournalierVerse(calculerSalaireJournalierVerse(bcSitProf, refData));
            }

            helper.setIdAffilie(bcSitProf.getIdAffilie());
            helper.setIdTiers(bcSitProf.getIdTiers());
            helper.setNom(bcSitProf.getNom());
            helper.setVersementEmployeur(bcSitProf.isPaiementEmployeur());
            helper.setIdSituationProfessionnelle(bcSitProf.getIdSituationProfessionnelle());

            helper.setIndependant(bcSitProf.isIndependant());
            helper.setSoumisCotisation(bcSitProf.isSoumisCotisation());
            helper.setCollaborateurAgricole(bcSitProf.isCollaborateurAgricole());
            helper.setTravailleurAgricole(bcSitProf.isTravailleurAgricole());
            helper.setTravailleurSansEmployeur(bcSitProf.isTravailleurSansEmployeur());

            // Si allocation maximum, on ne calcul rien, on alloue l'allocation
            // maximum.
            if (baseCalcul.isAllocationMaximum()) {
                helper.setRevenuMoyenDeterminantSansArrondi(((APReferenceDataAPG) refData).getGE());

                // pour eviter une nulPointerException plus tard
                helper.setSalaireJournalier(helper.getRevenuMoyenDeterminantSansArrondi());
                helper.setTL(helper.getRevenuMoyenDeterminantSansArrondi());

                result.add(helper);

                continue;
            }

            FWCurrency salaireJournalierPourCalculRMD = new FWCurrency(0, 5);
            FWCurrency salaireJournalier = new FWCurrency(0, 5);
            FWCurrency GE = ((APReferenceDataAPG) refData).getGE();

            for (Iterator iterator = bcSitProf.getBasesCalculSalaireJournalier().iterator(); iterator.hasNext();) {
                APBaseCalculSalaireJournalier bcj = (APBaseCalculSalaireJournalier) iterator.next();

                // Salaire horaire
                if ((bcj.getSalaireHoraire() != null)
                        && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireHoraire().toString())) {
                    BigDecimal L = bcj.getSalaireHoraire().getBigDecimalValue();

                    if ((bcj.getNbrHeuresSemaine() == null)
                            || JadeStringUtil.isDecimalEmpty(bcj.getNbrHeuresSemaine().toString())) {
                        throw new APBaseCalculDataMissingException(null, "Nbre heures/semaines non renseigné");
                    }

                    BigDecimal AS = bcj.getNbrHeuresSemaine();

                    bTL = L.multiply(AS);
                    bTL = bTL.divide(new BigDecimal(7), 5, BigDecimal.ROUND_DOWN);
                }

                // Salaire journalier
                else if ((bcj.getSalaireJournalier() != null)
                        && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireJournalier().toString())) {
                    bTL = bcj.getSalaireJournalier().getBigDecimalValue();
                }
                // Salaire hebdomadaire
                else if ((bcj.getSalaireHebdomadaire() != null)
                        && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireHebdomadaire().toString())) {
                    bTL = bcj.getSalaireHebdomadaire().getBigDecimalValue();
                    bTL = bTL.divide(new BigDecimal(7), 5, BigDecimal.ROUND_DOWN);
                }
                // Salaire 4 semaines
                else if ((bcj.getSalaire4Semaines() != null)
                        && !JadeStringUtil.isDecimalEmpty(bcj.getSalaire4Semaines().toString())) {
                    bTL = bcj.getSalaire4Semaines().getBigDecimalValue();
                    bTL = bTL.divide(new BigDecimal(28), 5, BigDecimal.ROUND_DOWN);
                }
                // Salaire mensuel
                else if ((bcj.getSalaireMensuel() != null)
                        && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireMensuel().toString())) {
                    bTL = bcj.getSalaireMensuel().getBigDecimalValue();
                    bTL = bTL.divide(new BigDecimal(30), 5, BigDecimal.ROUND_DOWN);
                }
                // Salaire annuel
                else if ((bcj.getSalaireAnnuel() != null)
                        && !JadeStringUtil.isDecimalEmpty(bcj.getSalaireAnnuel().toString())) {
                    bTL = bcj.getSalaireAnnuel().getBigDecimalValue();
                    bTL = bTL.divide(new BigDecimal(360), 5, BigDecimal.ROUND_DOWN);
                } else {
                    throw new APBaseCalculDataMissingException(null,
                            "Les données servant de base de calcul sont incomplètes");
                }

                // On a a ce stade le salaire journalier sans arrondi.
                salaireJournalier.add(new FWCurrency(bTL.toString(), 5));

                // tronquage du bTL (sinon il risque d'être arrondit au
                // supérieur dans le constructeur de FWCurency
                bTL = bTL.setScale(5, BigDecimal.ROUND_DOWN);

                salaireJournalierPourCalculRMD.add(new FWCurrency(bTL.toString(), 5));
            }

            FWCurrency tmp = new FWCurrency(salaireJournalierPourCalculRMD.toString());
            // arrondi
            tmp.add(new FWCurrency(0.99999, 5));
            helper.setRevenuMoyenDeterminantSansArrondi(new FWCurrency(JANumberFormatter.round(
                    tmp.getBigDecimalValue(), 1, 0, JANumberFormatter.INF).toString()));

            // TL>GE
            if (salaireJournalierPourCalculRMD.compareTo(GE) > 0) {
                // TL=GE
                salaireJournalierPourCalculRMD = GE;
            }
            // on ajoute les 0.9999 une fois et une seule
            else {
                // TL = TL + 0.99999
                salaireJournalierPourCalculRMD.add(new FWCurrency(0.99999, 5));
            }
            helper.setTL(salaireJournalierPourCalculRMD);
            helper.setSalaireJournalier(salaireJournalier);
            result.add(helper);
        }

        return result;
    }
}

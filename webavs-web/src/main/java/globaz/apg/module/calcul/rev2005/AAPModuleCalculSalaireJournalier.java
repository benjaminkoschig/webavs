/*
 * Créé le 27 avr. 05
 * 
 * Calcul du salaire journalier (Revenu déterminant moyen) selon la révision 2005
 */
package globaz.apg.module.calcul.rev2005;

import ch.globaz.common.domaine.Montant;
import globaz.apg.exceptions.APRulesException;
import globaz.apg.module.calcul.*;
import globaz.apg.module.calcul.exceptions.APBaseCalculDataMissingException;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.wrapper.APMontantJourCurrency;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import lombok.Value;

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

        List<APSituationProfessionnelleHelper> listSitProfHelper = loadSituationProfessionnelleDataHelper(baseCalcul, refData);
        if (listSitProfHelper == null) {
            result.setRevenuDeterminantMoyen(new FWCurrency(0));
            result.setTL(new FWCurrency(0));
            return result;
        }

        // Salaire journalier
        FWCurrency TL = new FWCurrency(0);
        BigDecimal bTL = new BigDecimal(0);
        // Initialisation des salaires par employeurs, et maj des taux calculé
        // au prorata par rapport à l'alloc.
        // journalière totale (TL)
        result = loadResultatSituationProfessionnelle(result, listSitProfHelper);

        if(result.isJourIndemnise()) {
            result.setEmployeursTL(new ArrayList<>());
            for(APSituationProfessionnelleHelper sit : listSitProfHelper) {
                FWCurrency cTL = new FWCurrency(JANumberFormatter.round(sit.getTL().doubleValue(), 1, 0, JANumberFormatter.INF));
                bTL = BigDecimal.valueOf(cTL.doubleValue() * sit.getNbJourIndemnise() / sit.getNbJourTotal());
                FWCurrency cTL2 = new FWCurrency(JANumberFormatter.round(bTL.doubleValue(), 0.0005, 4, JANumberFormatter.NEAR), 4);
                result.getEmployeursTL().add(APMontantJourCurrency.of(cTL, sit.getNbJourIndemnise(), sit.getIdSituationProfessionnelle()));
                TL.add(cTL2);
            }

            result.setTL(TL);
        } else {
            if(listSitProfHelper.size() > 1) {
                for (Iterator<APSituationProfessionnelleHelper> iter = listSitProfHelper.iterator(); iter.hasNext();) {
                    APSituationProfessionnelleHelper element = iter.next();
                    bTL = bTL.add(element.getRevenuMoyenDeterminantSansArrondi().getBigDecimalValue());
                    TL.add(element.getRevenuMoyenDeterminantSansArrondi());
                }
                bTL = TL.getBigDecimalValue();
            } else {
                APSituationProfessionnelleHelper element = listSitProfHelper.get(0);
                bTL = bTL.add(element.getTL().getBigDecimalValue());
                TL.add(element.getTL());
            }

            // Arrondi au francs
            TL = new FWCurrency(JANumberFormatter.round(bTL, 1, 0, JANumberFormatter.INF).toString());

            result.setTL(TL);
        }



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
    protected APResultatCalcul loadResultatSituationProfessionnelle(APResultatCalcul result, List<APSituationProfessionnelleHelper> salairesParEmployeur) {
        BigDecimal cumulDesTaux = new BigDecimal(0);

        TauxCalculateur tauxCalculateur = TauxCalculateur.of(salairesParEmployeur);
        Integer nbJoursIndemnisesTotal = salairesParEmployeur.stream()
                .filter(APSituationProfessionnelleHelper::hasNbJourIndemnise)
                .map(APSituationProfessionnelleHelper::getNbJourIndemnise)
                .reduce(0, Integer::sum);

        boolean hasJourIndemnise = salairesParEmployeur.stream().anyMatch(sit -> sit.hasNbJourIndemnise() && (sit.getNbJourIndemnise() != sit.getNbJourTotal()
                || salairesParEmployeur.size() > 1));
        hasJourIndemnise = salairesParEmployeur.stream().allMatch(sit -> !sit.getIsJoursIdentiques()) && hasJourIndemnise;

        result.setJourIndemnise(hasJourIndemnise);

        for (Iterator iter = salairesParEmployeur.iterator(); iter.hasNext();) {
            APSituationProfessionnelleHelper element = (APSituationProfessionnelleHelper) iter.next();

            BigDecimal tauxProRata = null;
            // Dernier element
            if (!iter.hasNext()
                    // Seulement si le nombre de jour saisie dans les situation professionnelle = nb jour total
                    && (nbJoursIndemnisesTotal == element.getNbJourTotal()
                        || nbJoursIndemnisesTotal == 0)) {
                tauxProRata = new BigDecimal(1);
                tauxProRata = tauxProRata.subtract(cumulDesTaux);
            } else {
                tauxProRata = tauxCalculateur.calculTaux(element, hasJourIndemnise);
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
    protected List<APSituationProfessionnelleHelper> loadSituationProfessionnelleDataHelper(APBaseCalcul baseCalcul, IAPReferenceDataPrestation refData)
            throws APBaseCalculDataMissingException {
        List<APSituationProfessionnelleHelper> result = new ArrayList();

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
            helper.setNbJourIndemnise(bcSitProf.getNbJourIndemnise());
            helper.setNbJourTotal(baseCalcul.getNombreJoursSoldes());
            helper.setIsJoursIdentiques(bcSitProf.getIsJoursIdentiques());

            // Si allocation maximum, on ne calcul rien, on alloue l'allocation
            // maximum.
            if (baseCalcul.isAllocationMaximum()) {
                helper.setRevenuMoyenDeterminantSansArrondi(((APReferenceDataAPG) refData).getGE());

                // pour eviter un nullpointer plus tard
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

            FWCurrency tmp = new FWCurrency(salaireJournalierPourCalculRMD.toString(), 5);
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

            helper.setSalaireJournalier(salaireJournalier);
            helper.setTL(salaireJournalierPourCalculRMD);

            result.add(helper);
        }

        return result;
    }

    @Value(staticConstructor = "of")
    static class TauxCalculateur {
        private Montant montantJournalierTotal;
        private Montant montantTotal;

        public static TauxCalculateur of(List<APSituationProfessionnelleHelper> situationProfessionnelle) {

            Montant salaireJournalierTotal = situationProfessionnelle.stream()
                    .map(APSituationProfessionnelleHelper::getSalaireJournalier)
                    .map(FWCurrency::getBigDecimalValue)
                    .map(Montant::new)
                    .reduce(Montant.ZERO, Montant::add);

            Montant montantTotal = situationProfessionnelle.stream()
                    .filter(APSituationProfessionnelleHelper::hasNbJourIndemnise)
                    .map(sit -> sit.getSalaireJournalier().getBigDecimalValue().multiply(new BigDecimal(sit.getNbJourIndemnise())))
                    .map(Montant::new)
                    .reduce(Montant.ZERO, Montant::add);

            return new TauxCalculateur(salaireJournalierTotal, montantTotal);

        }

        public BigDecimal calculTaux(APSituationProfessionnelleHelper situationProfessionnelle, boolean plusiseursSituationsProfessionnelles){
            if(plusiseursSituationsProfessionnelles) {
                return this.calculTauxAvecNbJourIndemnise(situationProfessionnelle);
            }

            return this.calculTauxStandard(situationProfessionnelle);
        }


        private BigDecimal calculTauxAvecNbJourIndemnise(APSituationProfessionnelleHelper situationProfessionnelle) {
            TauxCalculateur calculSituationUnique = TauxCalculateur.of(new Montant(situationProfessionnelle.getSalaireJournalier().getBigDecimalValue()),
                    new Montant(situationProfessionnelle.getNbJourIndemnise()).multiply(situationProfessionnelle.getSalaireJournalier().getBigDecimalValue()));

            return calculTaux(calculSituationUnique.montantTotal.getBigDecimalValue(), this.montantTotal);
        }

        private BigDecimal calculTauxStandard(APSituationProfessionnelleHelper situationProfessionnelle) {
            return calculTaux(situationProfessionnelle.getRevenuMoyenDeterminantSansArrondi().getBigDecimalValue(), this.getMontantJournalierTotal());
        }

        private BigDecimal calculTaux(BigDecimal bigDecimalValue, Montant calculSalaireNbjourTotal) {
            return bigDecimalValue.divide(calculSalaireNbjourTotal.getBigDecimalValue(), 10,
                    BigDecimal.ROUND_HALF_DOWN);
        }


    }
}

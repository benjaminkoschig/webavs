package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.PegasusCalculUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.TypeRenteMap;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import globaz.jade.client.util.JadeStringUtil;

public class StrategieFinalRevenuTotalDeterminant implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        final float TAUX_REVENU_ACTIVITE_LUCRATIVE = Float.parseFloat(((ControlleurVariablesMetier) context
                .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getValeurCourante());

        // long test = dateDebut.getTime();

        final String TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE = ((ControlleurVariablesMetier) context
                .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getLegendeCourante();

        // ajout de l'imputation de la fortune nette
        TupleDonneeRapport tupleImputationFortuneNette = getFractionFortuneTuple(context, donnee);

        float somme = tupleImputationFortuneNette.getValeur();

        donnee.addEnfantTuple(tupleImputationFortuneNette);

        // ajout du rendement de la fortune mobiliere
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL);
        // Ajout du dessaisissement de fortune
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL);

        // ajout du dessaisissement de revenu
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL);

        // ajout du rendement de la fortune immobiliere
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL);

        // ajout du revenu de l'activité lucrative arrondi si aucunes IJAJ ET activite lucrative simultanément
        float revenuPrivilegie = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL);
        TupleDonneeRapport tupleActiviteLucrativeRevenuPrivilegie;

        if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)) {
            tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIS_EN_COMPTE, revenuPrivilegie);

        } else {
            revenuPrivilegie = Math.round(donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL)
                    * TAUX_REVENU_ACTIVITE_LUCRATIVE);

            tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
                    IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE, revenuPrivilegie);
            tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE);
        }
        donnee.addEnfantTuple(tupleActiviteLucrativeRevenuPrivilegie);

        somme += revenuPrivilegie;

        // ajout des rentes avs ai
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL);

        // ajout du revenu des autres rentes
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL);

        // ajout du revenu allocations pour impotent
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_TOTAL);

        // ajout des revenus autres
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL);

        // ajout revenu loyer
        somme += donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_LOYER_SOUS_LOCATION_NET);
        // TODO clé à verifier
        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_REV_SOUS_TOTAL_RECONNU, somme));

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL, somme));

    }

    /**
     * Calcule la fraction d'imputation de la fortune
     * 
     * @param context
     *            contexte de calcul
     * @param donnee
     *            tuple root des resultats de calcul
     * @return tuple de la fraction (est pas ajouté automatiquement au tuple root)
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    private TupleDonneeRapport getFractionFortuneTuple(CalculContext context, TupleDonneeRapport donnee)
            throws CalculException {

        Attribut legende = null;
        String legendeValue = null;

        String typeRenteRequerant;
        if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)) {
            typeRenteRequerant = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)
                    .getLegende();

            // K141111_003 si on a une SR avec une IJAJ on met un message d'erreur
            if ((typeRenteRequerant.equals(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_INVALIDITE)
                    || typeRenteRequerant.equals(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_VIEILLESSE) || typeRenteRequerant
                        .equals(IPCRenteAvsAi.CS_TYPE_SANS_RENTE_SURVIVANT))
                    && donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)) {
                throw new CalculBusinessException("pegasus.calcul.strategie.revenuTotal.typeRenteRequerant.srijai");
            }
        } else if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)) {
            // K141111_003
            typeRenteRequerant = IPCRenteAvsAi.CS_TYPE_RENTE_IJAI;
        } else {
            String dateDebutPeriode = (String) context.get(Attribut.DATE_DEBUT_PERIODE);
            throw new CalculBusinessException("pegasus.calcul.strategie.revenuTotal.typeRenteRequerant.integrity",
                    dateDebutPeriode);
        }
        String typeRenteConjoint = null;
        if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_CONJOINT)) {
            typeRenteConjoint = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_CONJOINT)
                    .getLegende();
        }

        if (typeRenteRequerant == null) {
            throw new CalculException("TypeRenteRequerant should not be null");
        }

        // D0173
        int nbPersonnes = (Integer) context.get(Attribut.NB_PARENTS);
        float nbHomes = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES);
        boolean isAllHome = ((int) nbHomes >= nbPersonnes);

        // détermination de la fraction pour
        Float fractionFortune = null;

        if(IPCRenteAvsAi.CS_TYPE_RENTE_13.equals(typeRenteRequerant) 
                && !JadeStringUtil.isEmpty(donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL))){
            String value = donnee.getLegendeEnfant(IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL);
            legendeValue = value;
            if (value.contains("/")) {
                String[] rat = value.split("/");
                fractionFortune =  Float.parseFloat(rat[0]) / Float.parseFloat(rat[1]);
            } else {
                fractionFortune =  Float.parseFloat(value);
            }
        } else if ((Boolean) context.get(Attribut.IS_FRATRIE)) {
            if (isAllHome) {
                fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME)).getValeurCourante());
                legende = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME;
            } else {
                fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON)).getValeurCourante());
                legende = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON;
            }
        } else {

            if (TypeRenteMap.listeCsRenteSurvivant.contains(typeRenteRequerant)
                    || TypeRenteMap.listeCsRenteInvalidite.contains(typeRenteRequerant)) {
                // rente survivant ou invalidite
                if (isAllHome) {
                    fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME)).getValeurCourante());
                    legende = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME;
                } else {
                    fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON)).getValeurCourante());
                    legende = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON;
                }
            } else if (TypeRenteMap.listeCsRenteVieillesse.contains(typeRenteRequerant)) {
                // rente vieillesse

                if (TypeRenteMap.listeCsRenteInvalidite.contains(typeRenteConjoint)) {
                    if (isAllHome) {
                        fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME)).getValeurCourante());
                        legende = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME;
                    } else {
                        fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON)).getValeurCourante());
                        legende = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON;
                    }
                } else {
                    if (isAllHome) {
                        fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME)).getValeurCourante());
                        legende = Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME;
                    } else {
                        fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON)).getValeurCourante());
                        legende = Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON;
                    }
                }
            } else {
                // sans rente

                fractionFortune = Float.parseFloat(((ControlleurVariablesMetier) context
                        .get(Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME)).getValeurCourante());
                legende = Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME;
            }

            // comparaison avec le type de rente du conjoint, seulement s'il y a un cas de 2 rentes principales à
            // domicile
            Float fractionFortuneConjoint = 0f;
            Attribut legendeConjoint = null;
            if (PegasusCalculUtil.isRentesPrincipalesCoupleADom(context)) {

                if (TypeRenteMap.listeCsRenteSurvivant.contains(typeRenteConjoint)
                        || TypeRenteMap.listeCsRenteInvalidite.contains(typeRenteConjoint)) {
                    fractionFortuneConjoint = Float.parseFloat(((ControlleurVariablesMetier) context
                            .get(Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON)).getValeurCourante());
                    legendeConjoint = Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON;
                } else if (TypeRenteMap.listeCsRenteVieillesse.contains(typeRenteConjoint)) {
                    if (donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES) == 0) {
                        // correction de Home en Maison (découvert dans le cadre de D0173)
                        fractionFortuneConjoint = Float.parseFloat(((ControlleurVariablesMetier) context
                                .get(Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON)).getValeurCourante());
                        legendeConjoint = Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON;
                    }
                }

                if ((legendeConjoint != null) && (fractionFortuneConjoint < fractionFortune)) {
                    fractionFortune = fractionFortuneConjoint;
                    legende = legendeConjoint;
                }

            }
        }

        // création du tuple de la fraction
        float somme = Math.round(donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL)
                * fractionFortune);
        TupleDonneeRapport tupleImputationFortuneNette = new TupleDonneeRapport(
                IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL, somme);

        if(legendeValue != null) {
            tupleImputationFortuneNette.setLegende(legendeValue);
        } else {
            tupleImputationFortuneNette
                .setLegende(((ControlleurVariablesMetier) context.get(legende)).getLegendeCourante());
        }

        return tupleImputationFortuneNette;
    }

}

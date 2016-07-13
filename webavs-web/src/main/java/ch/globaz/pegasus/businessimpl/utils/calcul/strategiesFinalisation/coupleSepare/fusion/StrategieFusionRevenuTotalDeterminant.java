package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.TypeRenteMap;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFusion;

public class StrategieFusionRevenuTotalDeterminant implements StrategieCalculFusion {

    @Override
    public void calcule(TupleDonneeRapport donneeCommun, TupleDonneeRapport donneeAvecEnfants,
            TupleDonneeRapport donneeSeul, TupleDonneeRapport donneeFusionne, CalculContext context, Date dateDebut)
            throws CalculException {

        // ajout de l'imputation de la fortune nette
        TupleDonneeRapport tupleImputationFortuneNette = getFractionFortuneTuple(context, donneeFusionne);
        donneeFusionne.addEnfantTuple(tupleImputationFortuneNette);

        float sommeRevenuCommun = calculRevenuCommun(donneeFusionne, donneeCommun, context,
                tupleImputationFortuneNette.getValeur());

        float sommeRevenuPropre = calculRevenuCommun(donneeFusionne, donneeAvecEnfants, context, 0);

        float sommeFinale = (sommeRevenuCommun / 2) + sommeRevenuPropre;

        donneeFusionne.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_REV_SOUS_TOTAL_RECONNU,
                sommeFinale));
        // donneeFusionne.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_DEP_RECO_SOUS_TOTAL,
        // sommeFinale));
        donneeFusionne
                .addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_REV_DETE_TOTAL, sommeFinale));

        donneeFusionne.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN,
                sommeRevenuCommun));
        donneeFusionne.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_REVENUS_COMMUN_POUR_MOITIE,
                sommeRevenuCommun / 2));

    }

    private float calculRevenuCommun(TupleDonneeRapport donneeFusionne, TupleDonneeRapport donneeCommun,
            CalculContext context, float sommeRevenuCommun) throws CalculException {
        // final float TAUX_REVENU_ACTIVITE_LUCRATIVE = Float.parseFloat(((ControlleurVariablesMetier) context
        // .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getValeurCourante());
        // final String TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE = ((ControlleurVariablesMetier) context
        // .get(Attribut.CS_FRACTION_REVENUS_PRIVILEGIES)).getLegendeCourante();
        // ajout du rendement de la fortune mobiliere
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL);
        // ajout du dessaisissement de revenu
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_DESS_REV_TOTAL);
        // ajout des interets de la fortune
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_INTDESFO_TOTAL);
        // ajout du rendement de la fortune immobiliere
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_TOTAL);

        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE);
        // ajout du revenu de l'activité lucrative
        // float revenuPrivilegie =
        // Math.round(donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_TOTAL)
        // * TAUX_REVENU_ACTIVITE_LUCRATIVE);
        // sommeRevenuCommun += revenuPrivilegie;
        //
        // // Ajout revenu privilgié
        // if (revenuPrivilegie > 0.0f) {
        // TupleDonneeRapport tupleActiviteLucrativeRevenuPrivilegie = new TupleDonneeRapport(
        // IPCValeursPlanCalcul.CLE_REVEN_ACT_LUCR_REVENU_PRIVILEGIE, revenuPrivilegie);
        // tupleActiviteLucrativeRevenuPrivilegie.setLegende(TAUX_REVENU_ACTIVITE_LUCRATIVE_LEGENDE);
        // donneeFusionne.addEnfantTuple(tupleActiviteLucrativeRevenuPrivilegie);
        // }

        // ajout des rentes avs ai
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAVSAI_TOTAL);

        // ajout du revenu des autres rentes
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL);

        // ajout du revenu allocations pour impotent
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_TOTAL);

        // ajout des revenus autres
        sommeRevenuCommun += donneeCommun.getValeurEnfant(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_TOTAL);
        return sommeRevenuCommun;
    }

    private TupleDonneeRapport getFractionFortuneTuple(CalculContext context, TupleDonneeRapport donnee)
            throws CalculException {

        Attribut legende = null;

        String typeRenteRequerant;
        if (donnee.getEnfants().containsKey(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)) {
            typeRenteRequerant = donnee.getEnfants().get(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)
                    .getLegende();
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

        Float fractionFortune = null;

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

        float somme = Math.round(donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_TOTALNET_TOTAL)
                * fractionFortune);

        TupleDonneeRapport tupleImputationFortuneNette = new TupleDonneeRapport(
                IPCValeursPlanCalcul.CLE_REVEN_IMP_FORT_TOTAL, somme);

        tupleImputationFortuneNette
                .setLegende(((ControlleurVariablesMetier) context.get(legende)).getLegendeCourante());

        return tupleImputationFortuneNette;
    }

}

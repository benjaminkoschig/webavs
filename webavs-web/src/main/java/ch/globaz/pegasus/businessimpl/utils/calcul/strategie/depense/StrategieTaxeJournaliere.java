/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.utils.PCApplicationUtil;

/**
 * @author ECO
 * 
 */
public class StrategieTaxeJournaliere extends StrategieCalculDepense {

    // Determine si le prix d ela chambre est plus élevé que le pafond
    private boolean isPrixJournalierSuperieurPlafond(String loyer, String plafond) {
        return Float.parseFloat(loyer) > Float.parseFloat(plafond);
    }

    // Retourne le sinformations du home (plus psécifiquement du type d echambre) correspondant à celui de la donnée
    // financière
    private CalculDonneesHome getHomeForTypeChambre(List<CalculDonneesHome> donneesHomes, String idTypeChambre) {

        for (CalculDonneesHome home : donneesHomes) {
            if (idTypeChambre.equals(home.getIdTypeChambre())) {
                return home;
            }
        }
        // home pas trouvé
        return null;
    }

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        // données des homes servant à récupérer des informations génériques liés au calcul de la taxe journalière
        List<CalculDonneesHome> donneesHomes = (List<CalculDonneesHome>) context.get(Attribut.DONNEES_HOMES);

        // stocke temporairement le type de chambre pour la strategie finale qui calcule les dépenses personnelles
        String idTypeChambre = donnee.getTaxeJournaliereIdTypeChambre();

        String csTypeChambre = null;
        String csCategorieTypeChambre = null;
        String idHome = null;
        String strPrixChambre = null;
        String csMbrFamille = donnee.getCsRoleFamille();
        String strFraisLongueDuree = null;
        String csPeriodeServiceEtat = null;

        CalculDonneesHome homeCalcul = getHomeForTypeChambre(donneesHomes, idTypeChambre);

        if (null == homeCalcul) {
            throw new CalculBusinessException("pegasus.calcul.strategie.taxeJournaliere.home.integrity");
        } else {
            strPrixChambre = homeCalcul.getPrixJournalier();
            csTypeChambre = homeCalcul.getCsTypeChambre();
            csCategorieTypeChambre = homeCalcul.getCsCategorieArgentPoche();
            idHome = homeCalcul.getIdHome();
            csPeriodeServiceEtat = homeCalcul.getCsServiceEtatPeriode();
        }

        if (null == csTypeChambre) {
            throw new CalculBusinessException("pegasus.calcul.strategie.taxeJournaliere.home.integrity");
        }
        if (null == csCategorieTypeChambre) {
            throw new CalculBusinessException("pegasus.calcul.strategie.taxeJournaliere.home.integrity");
        }

        if (!JadeNumericUtil.isNumericPositif(strPrixChambre)) {
            throw new CalculException(
                    "The field prix journalier of taxeJournaliere du home must be a valid positive number!");
        }

        // spécifique valais
        if (PCApplicationUtil.isCantonVS()) {
            // plafond pour le home
            // String plafond = (((ControlleurVariablesMetier) context.get(Attribut.CS_PLAFOND_ANNUEL_EMS))
            // .getValeurCourante());

            String plafond = getPlafondForTaxeJournaliere(context, csPeriodeServiceEtat);

            if (!donnee.getTaxeJournaliereIsDeplafonner() && isPrixJournalierSuperieurPlafond(strPrixChambre, plafond)) {
                strPrixChambre = plafond;
            }

            // S160429 frais longue durée (spécifique VS)
            strFraisLongueDuree = donnee.getTaxeJournaliereMontantFraisLongueDuree();
        }

        float prixChambre = Float.parseFloat(strPrixChambre) * (Integer) context.get(Attribut.DUREE_ANNEE);

        TupleDonneeRapport tupleLoyer = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE, prixChambre);
        // ajout du prix journalier utilisé au calcul pour affichage dans le libelle
        tupleLoyer.setLegende(strPrixChambre);

        // si frais longue durée renseigné et dif. de zero
        if (strFraisLongueDuree != null) {
            float fraisLongueDuree = Float.parseFloat(strFraisLongueDuree);
            if (fraisLongueDuree > 0) {
                float fraisLongueDureeAnnee = fraisLongueDuree * (Integer) context.get(Attribut.DUREE_ANNEE);
                // ajout des frais longue durée
                this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_LONGUE_DUREE,
                        fraisLongueDureeAnnee);
            }
        }

        // ajout clé intermediaire idHome
        TupleDonneeRapport tupleParentIdHome = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_INTER_ID_HOME, 0f);
        // ajout clé enfants, idhome pour csPersonne
        TupleDonneeRapport tupleEnfantIdHome = this.getOrCreateChild(tupleParentIdHome, csMbrFamille, 0f);
        tupleEnfantIdHome.setLegende(idHome);

        TupleDonneeRapport tupleTypeChambre = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_INTER_DEPENSE_CS_TYPE_CHAMBRE, csTypeChambre);
        tupleTypeChambre.setLegende(csTypeChambre);

        TupleDonneeRapport tupleCsArgentDePocheTypeChambre = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_INTER_DEPENSE_CS_TYPE_ARGENT_DE_POCHE_TYPE_CHAMBRE, csCategorieTypeChambre);
        tupleCsArgentDePocheTypeChambre.setLegende(csCategorieTypeChambre);

        TupleDonneeRapport tupleParent = this.getOrCreateChild(resultatExistant,
                IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES, 1f);

        this.getOrCreateChild(tupleParent, donnee.getCsRoleFamille(), idTypeChambre);

        if (!JadeStringUtil.isNull(donnee.getTaxeJournaliereDateEntreeHome())) {
            TupleDonneeRapport tupleEntreeHome = this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_INTER_DATE_ENTREE_HOME, 0f);
            tupleEntreeHome.setLegende(donnee.getTaxeJournaliereDateEntreeHome());
        }

        return resultatExistant;
    }

    private String getPlafondForTaxeJournaliere(CalculContext context, String csPeriodeServiceEtat)
            throws CalculBusinessException, CalculException {

        if (csPeriodeServiceEtat.equals(IPCTaxeJournaliere.CS_PERIODE_SERVICE_ETAT_EMS)) {
            return (((ControlleurVariablesMetier) context.get(Attribut.CS_PLAFOND_ANNUEL_EMS)).getValeurCourante());
        } else if (csPeriodeServiceEtat.equals(IPCTaxeJournaliere.CS_PERIODE_SERVICE_ETAT_INSTITUTION)) {
            return (((ControlleurVariablesMetier) context.get(Attribut.CS_PLAFOND_ANNUEL_INSTITUTION))
                    .getValeurCourante());
        } else if (csPeriodeServiceEtat.equals(IPCTaxeJournaliere.CS_PERIODE_SERVICE_ETAT_LITS_ATTENTE)) {
            return (((ControlleurVariablesMetier) context.get(Attribut.CS_PLAFOND_ANNUEL_LITS_ATTENTE))
                    .getValeurCourante());
        } else {
            throw new CalculException("The plafond cant be found with the cs periode servce etat["
                    + csPeriodeServiceEtat + "]");
}
    }
}

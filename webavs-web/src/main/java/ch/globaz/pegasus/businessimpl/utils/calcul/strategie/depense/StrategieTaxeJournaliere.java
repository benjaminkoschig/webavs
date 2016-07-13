/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCLoiCantonaleProperty;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;

/**
 * @author ECO
 * 
 */
public class StrategieTaxeJournaliere extends StrategieCalculDepense {

    private boolean isSupPlafond(String loyer, String plafond) {
        return Float.parseFloat(loyer) > Float.parseFloat(plafond);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense. StrategieCalculDepense
     * #calculeDepense(ch.globaz.pegasus.business.models.calcul.CalculDonneesCC,
     * ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext,
     * ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport)
     */
    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        List<CalculDonneesHome> donneesHomes = (List<CalculDonneesHome>) context.get(Attribut.DONNEES_HOMES);

        // stocke temporairement le type de chambre pour la strategie finale qui
        // calcule les dépenses personnelles
        String idTypeChambre = donnee.getTaxeJournaliereIdTypeChambre();
        String csTypeChambre = null;
        String csCategorieTypeChambre = null;
        String idHome = null;
        String strPrixChambre = null;
        String csMbrFamille = donnee.getCsRoleFamille();
        String strFraisLongueDuree = null;

        String plafond = (((ControlleurVariablesMetier) context.get(Attribut.CS_PLAFOND_ANNUEL_HOME))
                .getValeurCourante());

        // recherche du home concerné
        for (CalculDonneesHome home : donneesHomes) {
            if (idTypeChambre.equals(home.getIdTypeChambre())) {

                strPrixChambre = home.getPrixJournalier();
                // D0173
                try {
                    if (EPCLoiCantonaleProperty.VALAIS.isLoiCantonPC()) {
                        if (!home.getIsDeplafonner() && isSupPlafond(home.getPrixJournalier(), plafond)) {
                            strPrixChambre = plafond;

                        }
                        // S160429 frais longue durée (spécifique VS)
                        strFraisLongueDuree = home.getMontantFraisLongueDuree();
                    }
                } catch (PropertiesException e) {
                    throw new CalculException(e.getMessage(), e);
                }
                csTypeChambre = home.getCsTypeChambre();
                csCategorieTypeChambre = home.getCsCategorieArgentPoche();
                idHome = home.getIdHome();
                break;
            }
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
}

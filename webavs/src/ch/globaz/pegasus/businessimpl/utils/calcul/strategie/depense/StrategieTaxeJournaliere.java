/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * @author ECO
 * 
 */
public class StrategieTaxeJournaliere extends StrategieCalculDepense {

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
        // calcule les depenses personnelles
        String idTypeChambre = donnee.getTaxeJournaliereIdTypeChambre();
        String csTypeChambre = null;
        String csCategorieTypeChambre = null;
        String idHome = null;
        String strPrixChambre = null;
        String csMbrFamille = donnee.getCsRoleFamille();

        // recherche du home concerné
        for (CalculDonneesHome home : donneesHomes) {
            if (idTypeChambre.equals(home.getIdTypeChambre())) {
                strPrixChambre = home.getPrixJournalier();
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

        resultatExistant.addEnfantTuple(new TupleDonneeRapport(
                IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE, prixChambre));

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

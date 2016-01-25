/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import globaz.globall.db.BSessionUtil;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCRenteijapi;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurMonnaieEtrangere;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieAutresRentes extends StrategieCalculRevenu implements IStrategieDessaisissable {

    private final static String CS_FRANCS_SUISSE = "510002";

    private final static Map<String, String> mappingGenre = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            // this.put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_LAA, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAA);
            put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_LPP, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LPP);
            put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_ASSURANCE_PRIVEE,
                    IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_PRIVEE);
            put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_3EME_PILIER,
                    IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_3EME_PILLIER);
            put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_LAM, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAM);
            put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_LAA, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAA);
            put(IPCRenteijapi.CS_AUTRES_RENTES_GENRE_RENTE_ETRANGERE,
                    IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE);

        }
    };

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return checkAmountAndParseAsFloat(donnee.getAutresRentesMontant());
    }

    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        String autresRentesCsGenre = donnee.getAutresRentesCsGenre();

        String montant = "";

        // Si rente étrangère conversion
        if (IPCRenteijapi.CS_AUTRES_RENTES_GENRE_RENTE_ETRANGERE.equals(autresRentesCsGenre)) {
            String tauxDonnee = donnee.getAutreRentesEtrangeresCSTypeDevise();

            // Si taux null ou francs suisse
            if ((tauxDonnee == null) || StrategieAutresRentes.CS_FRANCS_SUISSE.equals(tauxDonnee)) {
                montant = donnee.getAutresRentesMontant();
            } else {
                HashMap<String, ControlleurMonnaieEtrangere> listeTaux = (HashMap) context
                        .get(Attribut.TAUX_DEVISES_ETRANGERES);

                Float taux;

                // Si la liste des taux ne contient pas de taux pour la devise de la donnée financière
                if (listeTaux.get(donnee.getAutreRentesEtrangeresCSTypeDevise()) == null) {
                    throw new CalculBusinessException(BSessionUtil.getSessionFromThreadContext().getLabel(
                            "CALCUL_RENTRE_ETRANGERE_TAUX_INCONNU")
                            + BSessionUtil.getSessionFromThreadContext().getCodeLibelle(tauxDonnee));
                } else {
                    String tauxForPeriod = listeTaux.get(donnee.getAutreRentesEtrangeresCSTypeDevise())
                            .getMonnaieCourante();

                    // Si pas de taux pour la période
                    if (tauxForPeriod == null) {
                        String errorTauxPeriodMsg = "pegasus.calcul.rente.etrangere.taux.periode.inconnu";
                        throw new CalculBusinessException(errorTauxPeriodMsg, BSessionUtil
                                .getSessionFromThreadContext().getCodeLibelle(tauxDonnee),
                                donnee.getDateDebutDonneeFinanciere());
                    }
                    taux = Float.parseFloat(tauxForPeriod);
                }

                Float mont = checkAmoutAndParseAsFloat(donnee.getAutresRentesMontant()) * taux;
                montant = mont.toString();
            }
        } else {
            montant = donnee.getAutresRentesMontant();
        }

        if (IPCRenteijapi.CS_AUTRES_RENTES_AUTRE.equals(autresRentesCsGenre)) {
            // Gestion de la legende autre texte libre
            String legende = donnee.getAutresRentesAutreGenre();

            TupleDonneeRapport tupleAutresRentes = this.getOrCreateChild(resultatExistant,
                    IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_AUTRE_RENTE, 0f);

            TupleDonneeRapport tuple = this.getOrCreateChild(tupleAutresRentes, String.valueOf(legende.hashCode()),
                    montant);
            tuple.setLegende(legende);

        } else {
            String cle = StrategieAutresRentes.mappingGenre.get(autresRentesCsGenre);
            this.getOrCreateChild(resultatExistant, cle, montant);
        }
        return resultatExistant;
    }

}

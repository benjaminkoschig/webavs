/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.ConstantesCalcul;
import ch.globaz.pegasus.business.constantes.IPCHabitat;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCDonneeFinanciere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetierSearch;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.ChargesLoyer;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;

/**
 * Cette interface déclare la base de la stratégie de calcul d'une donnée financière pour un droit. Ses implémentations
 * calculeront la fortune, revenu et dépense d'une donnée financière selon ses règles métier.<br>
 * <br>
 * L'usage normale de cette classe consiste à d'abords l'implémenter dans une classe abstraite spécifique à une
 * catégorie de stratégies, laquelle aura des implémentations finales pour chaque stratégie. Cette architecture permet
 * une meilleure lisibilité du fait que la classe finale d'une stratégie spécifie dans sa méthode de calcul son type
 * (revenu, dépense ou fortune).
 * 
 * @author ECO
 * 
 */
public abstract class StrategieCalcul {

    protected static final float NB_MOIS = 12;
    protected static final float tauxChangePrecision = 100000;

    /**
     * ajoute un montant à la valeur d'un tuple. Si le tuple est null, il est créé avec la clé et le montant en
     * paramètre.
     * 
     * @param base
     *            le tuple auquel le montant est ajouté.
     * @param cle
     *            clé du tuple dans le cas ou le tuple est créé
     * @param montant
     *            le montant à ajouter
     * @return le tuple modifié, ou créé
     */
    protected TupleDonneeRapport ajouteSomme(TupleDonneeRapport base, String cle, float montant) {

        if (base == null) {
            base = new TupleDonneeRapport(cle);
        }

        base.addValeur(montant);

        return base;

    }

    /**
     * ajoute un montant à la valeur d'un tuple. Si le tuple est null, il est créé avec la clé et le montant en
     * paramètre.
     * 
     * @param base
     *            le tuple auquel le montant est ajouté.
     * @param cle
     *            clé du tuple dans le cas ou le tuple est créé
     * @param montant
     *            le montant à ajouter, en format texte. Si le texte ne contient pas un nombre valide, un
     *            NumberFormatException est levé.
     * @return le tuple modifié, ou créé
     * @throws CalculException
     */
    protected TupleDonneeRapport ajouteSomme(TupleDonneeRapport base, String cle, String montant)
            throws CalculException {
        return this.ajouteSomme(base, cle, checkAmoutAndParseAsFloat(montant));
    }

    /**
     * Méthode de lancement du calcul de la stratégie. Elle est appellée par le service de calcul pour la consolidation
     * des données BD brut. Cette méthode ne devrait être implémentée que par des sous-classes abstraites spécifiques à
     * une catégorie de stratégie pour appeller leur propre méthode abstraite de calcul avec une signature de méthode
     * plus explicite.
     * 
     * @param donnee
     *            La donnée à calculer, selon l'implémentation finale de la stratégie.
     * @param context
     *            Le contexte de calcul, contenant des données communes aux stratégies.
     * @param resultatExistant
     *            tuple de données parent contenant les données déjà générées des précédentes stratégies.
     * @return le tuple de données parent, enrichi par la stratégie.
     * @throws CalculException
     *             si il se produit une erreur durant le calcul.
     */
    public abstract TupleDonneeRapport calcule(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException;

    /**
     * Méthode qui s'assure que le montant ne soit pas vide. Leve une CalculException dans le cas contraire
     * 
     * @param amount
     * @throws CalculException
     */
    public final String checkAmount(String amount) throws CalculException {
        if (JadeStringUtil.isBlank(amount)) {
            throw new CalculBusinessException(
                    "Financial data not present, the string which should contain the value is empty - "
                            + this.getClass().getName());
        }
        return amount;
    }

    /**
     * Convertit un texte en float. Si null ou vide, retourne 0. Si le format n'est pas valide, un FormatNumberException
     * est levé.
     * 
     * @param value
     * @return
     */
    protected final float checkAmountAndParseAsFloat(String value) {
        if (JadeStringUtil.isEmpty(value)) {
            return 0;
        } else {
            return Float.valueOf(value);
        }
    }

    /**
     * Méthode qui convertir en float un montant précédémmnet checker afin qu'il ne soit pas vide. Leve une calcul
     * Exception dans le cas contaire.
     * 
     * @param amount
     * @return
     * @throws CalculException
     */
    public final float checkAmoutAndParseAsFloat(String amount) throws CalculException {
        return Float.parseFloat(checkAmount(amount));
    }

    protected TupleDonneeRapport ecraseChildExistant(TupleDonneeRapport base, String cle, Float valeur) {
        TupleDonneeRapport result = base.getEnfants().get(cle);

        result.setValeur(valeur);
        return result;
    }


    protected TupleDonneeRapport ecraseChildExistant(TupleDonneeRapport base, String cle, String valeur) throws CalculException {
        TupleDonneeRapport result = base.getEnfants().get(cle);

        result.setValeur(checkAmoutAndParseAsFloat(valeur));

        return result;
    }

    protected TupleDonneeRapport ecraseChildExistant(TupleDonneeRapport base, String cle, boolean valeur) {
        TupleDonneeRapport result = base.getEnfants().get(cle);

        result.setValeur(TupleDonneeRapport.writeBoolean(valeur));
        return result;
    }

    protected ChargesLoyer getMontantChargesLoyer(String csTypeLoyer, CalculContext context,
            float montantChargesMensuels) throws CalculBusinessException, NumberFormatException, CalculException {
        // calcul du montant de charge
        float montantChargesAnnuels = 0f;
        String cleDepenseMontantCharges = null;

        if (IPCHabitat.CS_LOYER_NET_AVEC_CHARGE_FORFAITAIRES.equals(csTypeLoyer)) {
            montantChargesAnnuels = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.CS_FORFAIT_FRAIS_CHAUFFAGE)).getValeurCourante());
            cleDepenseMontantCharges = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE;
        } else if (IPCHabitat.CS_VALEUR_LOCATIVE_CHEZ_PROPRIETAIRE.equals(csTypeLoyer)) {
            // verification integrité données
            if (context.contains(Attribut.HAS_BIEN_IMMO_PRINCIPAL)) {
                throw new CalculBusinessException("pegasus.calcul.habitat.integrity");
            }
            context.put(Attribut.HAS_HABITAT_CHEZ_PROPRIETAIRE, true);

            montantChargesAnnuels = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(Attribut.CS_FORFAIT_CHARGES)).getValeurCourante());
            cleDepenseMontantCharges = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_FRAIS_CHAUFFAGE;
        } else if (IPCHabitat.CS_LOYER_NET_AVEC_CHARGE.equals(csTypeLoyer)) {
            // annualise le montant des charges
            montantChargesAnnuels = montantChargesMensuels * StrategieCalcul.NB_MOIS;
            cleDepenseMontantCharges = IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_ACCOMPTE_CHARGES;
        }
        ChargesLoyer toReturn = new ChargesLoyer(cleDepenseMontantCharges, montantChargesAnnuels);
        return toReturn;
    }

    /**
     * Récupère le tuple associé à une clé et ajoute au montant existant la somme en paramètre. Si la clé n'existe pas,
     * elle est créé avec comme montant initial la somme en paramètre.
     * 
     * @param base
     *            le tuple parent
     * @param cle
     *            la clé du tuple
     * @param valeur
     *            le montant à ajouter
     * @return tuple modifié, ou créé
     */
    protected TupleDonneeRapport getOrCreateChild(TupleDonneeRapport base, String cle, Float valeur) {
        TupleDonneeRapport result = base.getEnfants().get(cle);
        if (result == null) {
            result = new TupleDonneeRapport(cle, 0);
            base.getEnfants().put(cle, result);
        }
        this.ajouteSomme(result, cle, valeur);
        return result;
    }

    /**
     * Récupère le tuple associé à la clé/valeur. Si la clé n'existe pas,
     * elle est créé avec comme valeur '0' qui représente <code>false</code>.
     * 
     * @param base
     * @param cle
     * @param value
     * @return
     * @throws CalculException
     */
    protected TupleDonneeRapport getOrCreateChild(TupleDonneeRapport base, String cle, boolean value)
            throws CalculException {
        TupleDonneeRapport result = base.getEnfants().get(cle);
        if (result == null) {
            result = new TupleDonneeRapport(cle, 0);
            base.getEnfants().put(cle, result);
        }
        this.ajouteSomme(result, cle, TupleDonneeRapport.writeBoolean(value));
        return result;
    }

    /**
     * Récupère le tuple associé à une clé et ajoute au montant existant la somme en paramètre. Si la clé n'existe pas,
     * elle est créé avec comme montant initial la somme en paramètre.
     * 
     * @param base
     *            le tuple parent
     * @param cle
     *            la clé du tuple
     * @param valeur
     *            le montant, en format texte, à ajouter. Si le texte n'est pas un nombre valide, un
     *            NumberFormatException est lancé.
     * @return tuple modifié, ou créé
     * @throws CalculException
     */
    protected TupleDonneeRapport getOrCreateChild(TupleDonneeRapport base, String cle, String valeur)
            throws CalculException {
        return this.getOrCreateChild(base, cle, checkAmoutAndParseAsFloat(valeur));
    }

    protected final CalculVariableMetierSearch getVariablesMetier(Map<String, JadeAbstractSearchModel> cacheDonnees) {
        return (CalculVariableMetierSearch) cacheDonnees.get(ConstantesCalcul.CONTAINER_DONNEES_VARIABLES_METIER);
    }

    protected float setTauxDeChange(String tauxChange) throws CalculException {
        return Float.parseFloat(checkAmount(tauxChange)) * tauxChangePrecision;
    }

    protected String getTauxDeChange(String tauxChange) throws CalculException {
        return String.valueOf(Float.parseFloat(checkAmount(tauxChange)) / tauxChangePrecision);
    }

    protected Boolean isDroitHabitation(String csTypePropriete) {
        return csTypePropriete.equals(IPCDonneeFinanciere.CS_TYPE_PROPRIETE_DROIT_HABITATION);
    }

    protected Boolean isNuProprietaire(String csTypePropriete) {
        return csTypePropriete.equals(IPCDonneeFinanciere.CS_TYPE_PROPRIETE_NU_PROPRIETAIRE);
    }

    protected Boolean isProprietaire(String csTypePropriete) {
        return csTypePropriete.equals(IPCDonneeFinanciere.CS_TYPE_PROPRIETE_PROPRIETAIRE);
    }

    protected Boolean isUsufruit(String csTypePropriete) {
        return csTypePropriete.equals(IPCDonneeFinanciere.CS_TYPE_PROPRIETE_USUFRUITIER);
    }
}

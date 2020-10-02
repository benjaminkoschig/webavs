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
 * Cette interface d�clare la base de la strat�gie de calcul d'une donn�e financi�re pour un droit. Ses impl�mentations
 * calculeront la fortune, revenu et d�pense d'une donn�e financi�re selon ses r�gles m�tier.<br>
 * <br>
 * L'usage normale de cette classe consiste � d'abords l'impl�menter dans une classe abstraite sp�cifique � une
 * cat�gorie de strat�gies, laquelle aura des impl�mentations finales pour chaque strat�gie. Cette architecture permet
 * une meilleure lisibilit� du fait que la classe finale d'une strat�gie sp�cifie dans sa m�thode de calcul son type
 * (revenu, d�pense ou fortune).
 * 
 * @author ECO
 * 
 */
public abstract class StrategieCalcul {

    protected static final float NB_MOIS = 12;
    protected static final float tauxChangePrecision = 100000;

    /**
     * ajoute un montant � la valeur d'un tuple. Si le tuple est null, il est cr�� avec la cl� et le montant en
     * param�tre.
     * 
     * @param base
     *            le tuple auquel le montant est ajout�.
     * @param cle
     *            cl� du tuple dans le cas ou le tuple est cr��
     * @param montant
     *            le montant � ajouter
     * @return le tuple modifi�, ou cr��
     */
    protected TupleDonneeRapport ajouteSomme(TupleDonneeRapport base, String cle, float montant) {

        if (base == null) {
            base = new TupleDonneeRapport(cle);
        }

        base.addValeur(montant);

        return base;

    }

    /**
     * ajoute un montant � la valeur d'un tuple. Si le tuple est null, il est cr�� avec la cl� et le montant en
     * param�tre.
     * 
     * @param base
     *            le tuple auquel le montant est ajout�.
     * @param cle
     *            cl� du tuple dans le cas ou le tuple est cr��
     * @param montant
     *            le montant � ajouter, en format texte. Si le texte ne contient pas un nombre valide, un
     *            NumberFormatException est lev�.
     * @return le tuple modifi�, ou cr��
     * @throws CalculException
     */
    protected TupleDonneeRapport ajouteSomme(TupleDonneeRapport base, String cle, String montant)
            throws CalculException {
        return this.ajouteSomme(base, cle, checkAmoutAndParseAsFloat(montant));
    }

    /**
     * M�thode de lancement du calcul de la strat�gie. Elle est appell�e par le service de calcul pour la consolidation
     * des donn�es BD brut. Cette m�thode ne devrait �tre impl�ment�e que par des sous-classes abstraites sp�cifiques �
     * une cat�gorie de strat�gie pour appeller leur propre m�thode abstraite de calcul avec une signature de m�thode
     * plus explicite.
     * 
     * @param donnee
     *            La donn�e � calculer, selon l'impl�mentation finale de la strat�gie.
     * @param context
     *            Le contexte de calcul, contenant des donn�es communes aux strat�gies.
     * @param resultatExistant
     *            tuple de donn�es parent contenant les donn�es d�j� g�n�r�es des pr�c�dentes strat�gies.
     * @return le tuple de donn�es parent, enrichi par la strat�gie.
     * @throws CalculException
     *             si il se produit une erreur durant le calcul.
     */
    public abstract TupleDonneeRapport calcule(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException;

    /**
     * M�thode qui s'assure que le montant ne soit pas vide. Leve une CalculException dans le cas contraire
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
     * est lev�.
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
     * M�thode qui convertir en float un montant pr�c�d�mmnet checker afin qu'il ne soit pas vide. Leve une calcul
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
            // verification integrit� donn�es
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
     * R�cup�re le tuple associ� � une cl� et ajoute au montant existant la somme en param�tre. Si la cl� n'existe pas,
     * elle est cr�� avec comme montant initial la somme en param�tre.
     * 
     * @param base
     *            le tuple parent
     * @param cle
     *            la cl� du tuple
     * @param valeur
     *            le montant � ajouter
     * @return tuple modifi�, ou cr��
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
     * R�cup�re le tuple associ� � la cl�/valeur. Si la cl� n'existe pas,
     * elle est cr�� avec comme valeur '0' qui repr�sente <code>false</code>.
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
     * R�cup�re le tuple associ� � une cl� et ajoute au montant existant la somme en param�tre. Si la cl� n'existe pas,
     * elle est cr�� avec comme montant initial la somme en param�tre.
     * 
     * @param base
     *            le tuple parent
     * @param cle
     *            la cl� du tuple
     * @param valeur
     *            le montant, en format texte, � ajouter. Si le texte n'est pas un nombre valide, un
     *            NumberFormatException est lanc�.
     * @return tuple modifi�, ou cr��
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

/**
 * 
 */
package ch.globaz.perseus.business.calcul;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Hashtable;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DDE
 * 
 */
public class InputCalcul {

    private Demande demande = null;
    /**
     * Identifiant du membreFamille pour la cl� du hashtable
     */
    private Hashtable<String, DonneesFinancieres> donneesFinancieres = null;
    private DonneesFinancieres donneesFinancieresRegroupees = null;
    private ArrayList<EnfantFamille> listeEnfants = null;
    private Hashtable<CSVariableMetier, VariableMetier> variablesMetiers = null;

    public InputCalcul() {
        demande = new Demande();
        donneesFinancieres = new Hashtable<String, DonneesFinancieres>();
        variablesMetiers = new Hashtable<CSVariableMetier, VariableMetier>();
        donneesFinancieresRegroupees = new DonneesFinancieres();
        listeEnfants = new ArrayList<EnfantFamille>();

    }

    /**
     * Constructeur permettant de cr�er directement la structure des donn�es financi�res pour chaque membre de famille
     * 
     * @param demande
     * @throws JadePersistenceException
     * @throws CalculException
     */
    public InputCalcul(Demande demande) throws JadePersistenceException, CalculException {
        this();
        this.demande = demande;
        // Cr�ation des donn�es financi�res pour les parents
        DonneesFinancieres dfRequerant = new DonneesFinancieres();
        dfRequerant.setMembreFamille(demande.getSituationFamiliale().getRequerant().getMembreFamille());
        donneesFinancieres.put(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId(), dfRequerant);
        DonneesFinancieres dfConjoint = new DonneesFinancieres();
        dfRequerant.setMembreFamille(demande.getSituationFamiliale().getRequerant().getMembreFamille());
        donneesFinancieres.put(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId(), dfConjoint);

        // Cr�ation des donn�es financi�res pour les enfants
        try {
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(this.demande.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);
            for (JadeAbstractModel abstractModel : enfantFamilleSearchModel.getSearchResults()) {
                EnfantFamille enfantfamille = (EnfantFamille) abstractModel;
                DonneesFinancieres dfEnfant = new DonneesFinancieres();
                dfEnfant.setMembreFamille(enfantfamille.getEnfant().getMembreFamille());
                donneesFinancieres.put(enfantfamille.getEnfant().getMembreFamille().getId(), dfEnfant);
                // Ajout de l'enfant dans la liste des enfants
                listeEnfants.add(enfantfamille);
            }
        } catch (SituationFamilleException e) {
            throw new CalculException("SituationFamilleException during data loading in calcul : " + e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("JadeApplicationServiceNotAvailableException during data loading in calcul : "
                    + e.getMessage());
        }
    }

    /**
     * Ajout d'une d�pense reconnue elle sera automatiquement attribu� au bon membre famille et automatiquement
     * additionn� dans les donn�es financi�res regroup�es
     * 
     * @param depenseReconnue
     * @throws CalculException
     */
    public void addDepenseReconnue(DepenseReconnue depenseReconnue) throws CalculException {
        // R�cup�ration des donn�es financi�res pour le membre Famille
        DonneesFinancieres df = getDonneesFinancieresMembreFamille(depenseReconnue.getMembreFamille().getId());
        df.getDepensesReconnues().put(depenseReconnue.getTypeAsEnum(), depenseReconnue);

        // Si il s'agit du requ�rant ou du conjoint
        if (depenseReconnue.getMembreFamille().getId()
                .equals(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId())
                || depenseReconnue.getMembreFamille().getId()
                        .equals(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId())) {

            // Ajout des donn�es financi�res dans les donn�es regroup�es
            if (!donneesFinancieresRegroupees.getDepensesReconnues().containsKey(depenseReconnue.getTypeAsEnum())) {
                // Cr�ation d'une nouvelle donn�e financi�re du m�me type mais initialis�e � 0
                DepenseReconnue newDepenseReconnue = new DepenseReconnue(depenseReconnue.getTypeAsEnum());
                newDepenseReconnue.setValeur(new Float(0));
                donneesFinancieresRegroupees.getDepensesReconnues().put(depenseReconnue.getTypeAsEnum(),
                        newDepenseReconnue);
            }
            Float montant = donneesFinancieresRegroupees.getDepensesReconnues().get(depenseReconnue.getTypeAsEnum())
                    .getValeur();
            montant += depenseReconnue.getValeur();
            donneesFinancieresRegroupees.getDepensesReconnues().get(depenseReconnue.getTypeAsEnum()).setValeur(montant);
        }
    }

    /**
     * Ajout d'une dette ell sera automatiquement attribu� au bon membre famille et automatiquement additionn� dans les
     * donn�es financi�res regroup�es
     * 
     * @param dette
     * @throws CalculException
     */
    public void addDette(Dette dette) throws CalculException {
        // R�cup�ration des donn�es financi�res pour le membre Famille
        DonneesFinancieres df = getDonneesFinancieresMembreFamille(dette.getMembreFamille().getId());
        df.getDettes().put(dette.getTypeAsEnum(), dette);

        // Si il s'agit du requ�rant ou du conjoint
        if (dette.getMembreFamille().getId()
                .equals(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId())
                || dette.getMembreFamille().getId()
                        .equals(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId())) {

            // Ajout des donn�es financi�res dans les donn�es regroup�es
            if (!donneesFinancieresRegroupees.getDettes().containsKey(dette.getTypeAsEnum())) {
                // Cr�ation d'une nouvelle donn�e financi�re du m�me type mais initialis�e � 0
                Dette newDette = new Dette(dette.getTypeAsEnum());
                newDette.setValeur(new Float(0));
                donneesFinancieresRegroupees.getDettes().put(dette.getTypeAsEnum(), newDette);
            }
            Float montant = donneesFinancieresRegroupees.getDettes().get(dette.getTypeAsEnum()).getValeur();
            montant += dette.getValeur();
            donneesFinancieresRegroupees.getDettes().get(dette.getTypeAsEnum()).setValeur(montant);
        }
    }

    /**
     * Ajout d'un �l�ment de fortune Il sera automatiquement attribu� au bon membre famille et automatiquement
     * additionn� dans les donn�es financi�res regroup�es
     * 
     * @param fortune
     * @throws CalculException
     */
    public void addFortune(Fortune fortune) throws CalculException {
        // R�cup�ration des donn�es financi�res pour le membre Famille
        DonneesFinancieres df = getDonneesFinancieresMembreFamille(fortune.getMembreFamille().getId());
        df.getFortunes().put(fortune.getTypeAsEnum(), fortune);

        // Si il s'agit du requ�rant ou du conjoint
        if (fortune.getMembreFamille().getId()
                .equals(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId())
                || fortune.getMembreFamille().getId()
                        .equals(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId())) {

            // Ajout des donn�es financi�res dans les donn�es regroup�es
            if (!donneesFinancieresRegroupees.getFortunes().containsKey(fortune.getTypeAsEnum())) {
                // Cr�ation d'une nouvelle donn�e financi�re du m�me type mais initialis�e � 0
                Fortune newFortune = new Fortune(fortune.getTypeAsEnum());
                newFortune.setValeur(new Float(0));
                donneesFinancieresRegroupees.getFortunes().put(fortune.getTypeAsEnum(), newFortune);
            }
            Float montant = donneesFinancieresRegroupees.getFortunes().get(fortune.getTypeAsEnum()).getValeur();
            montant += fortune.getValeur();
            donneesFinancieresRegroupees.getFortunes().get(fortune.getTypeAsEnum()).setValeur(montant);
        }
    }

    /**
     * Ajout d'un revenu Il sera automatiquement attribu� au bon membre famille et automatiquement additionn� dans les
     * donn�es financi�res regroup�es
     * 
     * @param revenu
     * @throws CalculException
     */
    public void addRevenu(Revenu revenu) throws CalculException {
        // R�cup�ration des donn�es financi�res pour le membre Famille
        DonneesFinancieres df = getDonneesFinancieresMembreFamille(revenu.getMembreFamille().getId());
        df.getRevenus().put(revenu.getTypeAsEnum(), revenu);

        // Si il s'agit du requ�rant ou du conjoint
        if (revenu.getMembreFamille().getId()
                .equals(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId())
                || revenu.getMembreFamille().getId()
                        .equals(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId())) {

            // Ajout des donn�es financi�res dans les donn�es regroup�es
            if (!donneesFinancieresRegroupees.getRevenus().containsKey(revenu.getTypeAsEnum())) {
                // Cr�ation d'une nouvelle donn�e financi�re du m�me type mais initialis�e � 0
                Revenu newRevenu = new Revenu(revenu.getTypeAsEnum());
                newRevenu.setValeur(new Float(0));
                donneesFinancieresRegroupees.getRevenus().put(revenu.getTypeAsEnum(), newRevenu);
            }
            Float montant = donneesFinancieresRegroupees.getRevenus().get(revenu.getTypeAsEnum()).getValeur();
            montant += revenu.getValeur();
            donneesFinancieresRegroupees.getRevenus().get(revenu.getTypeAsEnum()).setValeur(montant);
        }
    }

    /**
     * M�thode permettant d'ajouter un variable m�tier pour le calcul
     * 
     * @param variableMetier
     */
    public void addVariableMetier(VariableMetier variableMetier) {
        variablesMetiers.put(CSVariableMetier.getEnumFromCodeSystem(variableMetier.getSimpleVariableMetier()
                .getCsTypeVariableMetier()), variableMetier);
    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    /**
     * @return Les donn�es financi�res du conjoint
     * @throws CalculException
     */
    public DonneesFinancieres getDonneesFinancieresConjoint() throws CalculException {
        if (demande == null) {
            throw new CalculException(
                    "Demande is null, class must be constructed with InputCalcul(Demande) constructor !");
        }

        return getDonneesFinancieresMembreFamille(demande.getSituationFamiliale().getConjoint().getMembreFamille()
                .getId());
    }

    /**
     * @param id
     *            du membre famille
     * @return les donn�es financi�res pour le membre famille pass� en param�tre
     * @throws CalculException
     *             si les donn�es financi�res n'existe pas pour le membre famille
     */
    public DonneesFinancieres getDonneesFinancieresMembreFamille(String idMembreFamille) throws CalculException {
        if (donneesFinancieres.containsKey(idMembreFamille)) {
            return donneesFinancieres.get(idMembreFamille);
        } else {
            throw new CalculException(
                    "Il y'a des donn�es financi�res pour cette demande, pour lesquelles le membre famille ne se trouve plus dans la situation familiale");
        }
    }

    /**
     * @return the donneesFinancieresRegroupees
     */
    public DonneesFinancieres getDonneesFinancieresRegroupees() {
        return donneesFinancieresRegroupees;
    }

    /**
     * @return Les donn�es financi�res du requerant
     * @throws CalculException
     */
    public DonneesFinancieres getDonneesFinancieresRequerant() throws CalculException {
        if (demande == null) {
            throw new CalculException(
                    "Demande is null, class must be constructed with InputCalcul(Demande) constructor !");
        }

        return getDonneesFinancieresMembreFamille(demande.getSituationFamiliale().getRequerant().getMembreFamille()
                .getId());
    }

    /**
     * Liste des enfants pour la demande
     * 
     * @return the enfants
     */
    public ArrayList<EnfantFamille> getListeEnfants() {
        return listeEnfants;
    }

    public VariableMetier getVariableMetier(CSVariableMetier csVariableMetier) {
        if (variablesMetiers.containsKey(csVariableMetier)) {
            return variablesMetiers.get(csVariableMetier);
        } else {
            JadeThread.logError(InputCalcul.class.getName(), "perseus.calcul.variablemetier.missing");
            return null;
        }
    }

}

/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.revenu;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.calcul.CalculsRevenuFormules;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuFullComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminantSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.revenu.RevenuService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class RevenuServiceImpl implements RevenuService {

    /**
     * Cette méthode va checker si la taxation est déjà utilisée et si elle a été modifiée
     * 
     * @param revenuFullComplex
     * @return null si pas modifiée ou si pas utilisée sinon on retourne le revenuHistoriqueComplex
     * @throws RevenuException
     */
    private RevenuHistoriqueComplex checkIfTaxationUsedAndDifferent(RevenuFullComplex revenuFullComplex)
            throws RevenuException {
        try {
            RevenuHistoriqueComplex revenuHistoriqueComplex = getRevenuHistoriqueComplex(revenuFullComplex
                    .getSimpleRevenu());

            if (!(revenuHistoriqueComplex == null)) {
                String revDetOld = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul();
                CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();
                revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
                if (IAMCodeSysteme.CS_TYPE_SOURCIER.equals(revenuFullComplex.getSimpleRevenu().getTypeRevenu())) {
                    calculsRevenuFormules.doCalculSourcier(revenuHistoriqueComplex.getRevenuFullComplex());
                }
                revenuHistoriqueComplex = calculsRevenuFormules.doCalcul(revenuHistoriqueComplex);
                String revDetNew = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul();

                Double revDetOld_Double = Double.valueOf(JANumberFormatter.fmt(revDetOld, false, false, false, 2));
                Double revDetNew_Double = Double.valueOf(JANumberFormatter.fmt(revDetNew, false, false, false, 2));

                if (!revDetOld_Double.equals(revDetNew_Double)) {
                    return revenuHistoriqueComplex;
                }
            }
        } catch (Exception e) {
            throw new RevenuException("Error while searching RevenuHistoriqueComplex !");
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#count(ch
     * .globaz.amal.business.models.revenu.RevenuSearch)
     */
    @Override
    public int count(RevenuSearch search) throws RevenuException, JadePersistenceException {
        if (search == null) {
            throw new RevenuException("Unable to search Revenu, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#create(ch
     * .globaz.amal.business.models.revenu.RevenuFullComplex)
     */
    @Override
    public RevenuFullComplex create(RevenuFullComplex revenuFullComplex) throws JadePersistenceException,
            RevenuException {
        // Check value null and generate exception
        if (revenuFullComplex == null) {
            throw new RevenuException("Unable to create revenuFullComplex, the given model is null!");
        } else {
            if (revenuFullComplex.getSimpleRevenu() == null) {
                throw new RevenuException("Unable to create revenuFullComplex, the Simple Revenu model is null!");
            } else if (revenuFullComplex.getSimpleRevenuContribuable() == null) {
                throw new RevenuException(
                        "Unable to create revenuFullComplex, the Simple Revenu Contribuable model is null!");
            } else if (revenuFullComplex.getSimpleRevenuSourcier() == null) {
                throw new RevenuException(
                        "Unable to create revenuFullComplex, the Simple Revenu Sourcier model is null!");
            }
        }
        // Check the type de revenu
        if (JadeStringUtil.isBlankOrZero(revenuFullComplex.getSimpleRevenu().getTypeRevenu())) {
            throw new RevenuException("Error ! Revenu type not defined !");
        }
        try {
            // Create the simple revenu
            SimpleRevenu simpleRevenu = revenuFullComplex.getSimpleRevenu();
            simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().create(simpleRevenu);
            revenuFullComplex.setSimpleRevenu(simpleRevenu);
            // Create the simple revenu contribuable ou simple revenu sourcier
            if (IAMCodeSysteme.CS_TYPE_CONTRIBUABLE.equals(revenuFullComplex.getSimpleRevenu().getTypeRevenu())) {
                SimpleRevenuContribuable simpleRevenuContribuable = revenuFullComplex.getSimpleRevenuContribuable();
                simpleRevenuContribuable.setIdRevenu(simpleRevenu.getId());
                simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().create(
                        simpleRevenuContribuable);
                revenuFullComplex.setSimpleRevenuContribuable(simpleRevenuContribuable);
            } else if (IAMCodeSysteme.CS_TYPE_SOURCIER.equals(revenuFullComplex.getSimpleRevenu().getTypeRevenu())) {
                SimpleRevenuSourcier simpleRevenuSourcier = revenuFullComplex.getSimpleRevenuSourcier();
                simpleRevenuSourcier.setIdRevenu(simpleRevenu.getId());
                simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().create(
                        simpleRevenuSourcier);
                revenuFullComplex.setSimpleRevenuSourcier(simpleRevenuSourcier);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuException("Service not available - " + e.getMessage());
        }

        return revenuFullComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#create(ch.globaz.amal.business.models.revenu.
     * RevenuFullComplex)
     */
    @Override
    public RevenuHistoriqueComplex create(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws JadePersistenceException, RevenuException {
        // Check value null and generate exception
        if (revenuHistoriqueComplex == null) {
            throw new RevenuException("Unable to create revenuFullComplex, the given model is null!");
        } else {
            if (revenuHistoriqueComplex.getRevenuFullComplex() == null) {
                throw new RevenuException(
                        "Unable to create revenuHistoriqueComplex, the revenuFullComplex model is null!");
            } else if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu() == null) {
                throw new RevenuException("Unable to create revenuHistoriqueComplex, the Simple Revenu model is null!");
            } else if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable() == null) {
                throw new RevenuException(
                        "Unable to create revenuHistoriqueComplex, the Simple Revenu Contribuable model is null!");
            } else if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuSourcier() == null) {
                throw new RevenuException(
                        "Unable to create revenuHistoriqueComplex, the Simple Revenu Sourcier model is null!");
            } else if (revenuHistoriqueComplex.getSimpleRevenuDeterminant() == null) {
                throw new RevenuException(
                        "Unable to create revenuHistoriqueComplex, the Simple Revenu Determinant model is null!");
            } else if (revenuHistoriqueComplex.getSimpleRevenuHistorique() == null) {
                throw new RevenuException(
                        "Unable to create revenuHistoriqueComplex, the Simple Revenu Historique model is null!");
            }
        }
        // Check the type de revenu
        if (JadeStringUtil.isBlankOrZero(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                .getTypeRevenu())) {
            throw new RevenuException("Error ! Revenu type not defined !");
        }
        // Generate revenu determinant
        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();
        if (IAMCodeSysteme.CS_TYPE_SOURCIER.equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                .getTypeRevenu())) {
            calculsRevenuFormules.doCalculSourcier(revenuHistoriqueComplex.getRevenuFullComplex());
            calculsRevenuFormules = new CalculsRevenuFormules(revenuHistoriqueComplex);
        }
        revenuHistoriqueComplex = calculsRevenuFormules.doCalcul(revenuHistoriqueComplex);

        try {
            // create the simple revenu historique
            SimpleRevenuHistorique simpleRevenuHistorique = revenuHistoriqueComplex.getSimpleRevenuHistorique();
            simpleRevenuHistorique.setIdContribuable(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                    .getIdContribuable());
            if (!simpleRevenuHistorique.getIsRecalcul()) {
                simpleRevenuHistorique.setCodeActif(true);
            }
            simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().create(
                    simpleRevenuHistorique);
            revenuHistoriqueComplex.setSimpleRevenuHistorique(simpleRevenuHistorique);

            // create the simple revenu determinant
            SimpleRevenuDeterminant simpleRevenuDeterminant = revenuHistoriqueComplex.getSimpleRevenuDeterminant();
            simpleRevenuDeterminant.setIdRevenuHistorique(simpleRevenuHistorique.getIdRevenuHistorique());
            simpleRevenuDeterminant.setIdContribuable(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                    .getIdContribuable());
            simpleRevenuDeterminant = AmalImplServiceLocator.getSimpleRevenuDeterminantService().create(
                    simpleRevenuDeterminant);
            revenuHistoriqueComplex.setSimpleRevenuDeterminant(simpleRevenuDeterminant);

            // The full complex revenu exist. Do the link with revenu historique only
            SimpleRevenu simpleRevenu = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu();
            simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read(simpleRevenuHistorique.getIdRevenu());
            simpleRevenu.setIdRevenuHistorique(simpleRevenuHistorique.getIdRevenuHistorique());
            simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().update(simpleRevenu);

            // update ids of simplerevenuHistorique
            simpleRevenuHistorique.setIdRevenu(simpleRevenu.getIdRevenu());
            simpleRevenuHistorique.setIdRevenuDeterminant(simpleRevenuDeterminant.getIdRevenuDeterminant());
            simpleRevenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(
                    simpleRevenuHistorique);
            revenuHistoriqueComplex.setSimpleRevenuHistorique(simpleRevenuHistorique);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuException("Service not available - " + e.getMessage());
        }

        return revenuHistoriqueComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#delete(ch
     * .globaz.amal.business.models.revenu.RevenuFullComplex)
     */
    @Override
    public RevenuFullComplex delete(RevenuFullComplex revenuFullComplex) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (revenuFullComplex == null) {
            throw new RevenuException("Unable to delete revenuFullComplex, the given model is null!");
        }
        // Read the revenu
        // ---------------------------------------------------------------------------
        revenuFullComplex = readFullComplex(revenuFullComplex.getSimpleRevenu().getId());
        // Delete the simple revenu
        // ---------------------------------------------------------------------------
        SimpleRevenu simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().delete(
                revenuFullComplex.getSimpleRevenu());
        revenuFullComplex.setSimpleRevenu(simpleRevenu);
        // Delete the simple revenu contribuable
        // ---------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(revenuFullComplex.getSimpleRevenuContribuable().getId())) {
            SimpleRevenuContribuable simpleRevenuContribuable = AmalImplServiceLocator
                    .getSimpleRevenuContribuableService().delete(revenuFullComplex.getSimpleRevenuContribuable());
            revenuFullComplex.setSimpleRevenuContribuable(simpleRevenuContribuable);
        }
        // Delete the simple revenu sourcier
        // ---------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(revenuFullComplex.getSimpleRevenuSourcier().getId())) {
            SimpleRevenuSourcier simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().delete(
                    revenuFullComplex.getSimpleRevenuSourcier());
            revenuFullComplex.setSimpleRevenuSourcier(simpleRevenuSourcier);
        }
        return revenuFullComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#delete(ch
     * .globaz.amal.business.models.revenu.RevenuHistoriqueComplex)
     */
    @Override
    public RevenuHistoriqueComplex delete(RevenuHistoriqueComplex revenuHistoriqueComplex) throws RevenuException,
            JadePersistenceException {
        if (revenuHistoriqueComplex == null) {
            throw new RevenuException("Unable to delete revenuHistoriqueComplex, the given model is null!");
        }
        // TODO : CREATE PHILOSOPHY TO ARCHIVE THE REVENUS
        return revenuHistoriqueComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#getRevenuForSubsideYear(ch
     * .globaz.amal.business.models.revenu.Revenu)
     */
    @Override
    public RevenuHistoriqueComplex getRevenuForSubsideYear(String anneeHistorique, String idContribuable,
            String idRevenu) throws RevenuException, JadePersistenceException {
        // Work on idRevenu
        if ((idRevenu == null) || (idRevenu.length() == 0)) {
            return null;
        }
        Boolean isRDU = false;
        if (idRevenu.indexOf("RDU_") > -1) {
            idRevenu = idRevenu.substring(4);
            isRDU = true;
        }
        ArrayList<RevenuHistoriqueComplex> allRevenus = getRevenusForSubsideYear(anneeHistorique, idContribuable);
        for (int iRevenu = 0; iRevenu < allRevenus.size(); iRevenu++) {
            RevenuHistoriqueComplex currentRevenu = allRevenus.get(iRevenu);
            if (currentRevenu != null) {
                if (currentRevenu.getId().equals(idRevenu)) {
                    currentRevenu.getRevenuFullComplex().getSimpleRevenu().setRevDetUniqueOuiNon(isRDU);
                    return currentRevenu;
                }
            }
        }
        return null;
    }

    private RevenuHistoriqueComplex getRevenuHistoriqueComplex(SimpleRevenu simpleRevenu)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {
        RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch = new RevenuHistoriqueComplexSearch();
        revenuHistoriqueComplexSearch.setForIdRevenuHistorique(simpleRevenu.getIdRevenuHistorique());
        revenuHistoriqueComplexSearch = AmalServiceLocator.getRevenuService().search(revenuHistoriqueComplexSearch);

        if (revenuHistoriqueComplexSearch.getSize() == 0) {
            return null;
            // throw new RevenuException("Error ! No matching revenuHistoriqueComplexSearch with idRevenuHistorique : "
            // + simpleRevenu.getIdRevenuHistorique());
        }

        if (revenuHistoriqueComplexSearch.getSize() > 1) {
            throw new RevenuException("Error ! Multiple revenuHistoriqueComplexSearch with idRevenuHistorique : "
                    + simpleRevenu.getIdRevenuHistorique());
        }

        RevenuHistoriqueComplex revenuHistoriqueComplex = (RevenuHistoriqueComplex) revenuHistoriqueComplexSearch
                .getSearchResults()[0];
        return revenuHistoriqueComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#getRevenusForSubsideYear(ch
     * .globaz.amal.business.models.revenu.Revenu)
     */
    @Override
    public ArrayList<RevenuHistoriqueComplex> getRevenusForSubsideYear(String anneeHistorique, String idContribuable)
            throws RevenuException, JadePersistenceException {

        // Work on anneeHistorique and get the anneeTaxation to fit
        int iAnneeHistorique = Integer.parseInt(anneeHistorique);
        ArrayList<String> anneesTaxation = new ArrayList<String>();
        for (int iAnnee = iAnneeHistorique - 2; iAnnee <= iAnneeHistorique; iAnnee++) {
            anneesTaxation.add(Integer.toString(iAnnee));
        }

        ArrayList<RevenuHistoriqueComplex> revenus = new ArrayList<RevenuHistoriqueComplex>();

        // add revenu for anneeHistorique
        RevenuHistoriqueComplexSearch revenuSearch = new RevenuHistoriqueComplexSearch();
        revenuSearch.setForAnneeHistorique(anneeHistorique);
        revenuSearch.setForIdContribuable(idContribuable);
        revenuSearch = this.search(revenuSearch);
        for (int iRevenu = 0; iRevenu < revenuSearch.getSize(); iRevenu++) {
            RevenuHistoriqueComplex currentRevenu = (RevenuHistoriqueComplex) revenuSearch.getSearchResults()[iRevenu];
            if (currentRevenu.getSimpleRevenuHistorique().getCodeActif()) {
                // Add this revenu to the list
                revenus.add(currentRevenu);
                // Remove the annee taxation from the list
                if (currentRevenu.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation().equals("0")
                        || currentRevenu.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation().equals("")) {
                    anneesTaxation.remove(currentRevenu.getSimpleRevenuHistorique().getAnneeHistorique());
                } else {
                    anneesTaxation.remove(currentRevenu.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation());
                }
            }
        }
        // Gets revenus for the rest of anneesTaxation
        RevenuHistoriqueComplexSearch revenuTaxationSearch = new RevenuHistoriqueComplexSearch();
        revenuTaxationSearch.setForIdContribuable(idContribuable);
        revenuTaxationSearch = this.search(revenuTaxationSearch);

        for (int iAnnee = 0; iAnnee < anneesTaxation.size(); iAnnee++) {
            String anneeTaxation = anneesTaxation.get(iAnnee);
            for (int iRevenu = 0; iRevenu < revenuTaxationSearch.getSize(); iRevenu++) {
                RevenuHistoriqueComplex taxationRevenu = (RevenuHistoriqueComplex) revenuTaxationSearch
                        .getSearchResults()[iRevenu];
                Boolean useRevenu = false;
                // check annee de taxation du revenu
                if (taxationRevenu.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation().equals("0")
                        || taxationRevenu.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation().equals("")) {
                    // Annee taxation non renseignée
                    if (taxationRevenu.getSimpleRevenuHistorique().getAnneeHistorique().equals(anneeTaxation)) {
                        useRevenu = true;
                    }
                } else {
                    // Annee taxation renseignée
                    if (taxationRevenu.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation()
                            .equals(anneeTaxation)) {
                        useRevenu = true;
                    }
                }
                if (useRevenu) {
                    // Set annee historique to the new one >> goal : get the correct parameters
                    taxationRevenu.getSimpleRevenuHistorique().setAnneeHistorique(anneeHistorique);
                    taxationRevenu.getRevenuFullComplex().getSimpleRevenu().setRevDetUniqueOuiNon(false);
                    if (IAMCodeSysteme.CS_TYPE_SOURCIER.equals(taxationRevenu.getRevenuFullComplex().getSimpleRevenu()
                            .getTypeRevenu())) {
                        taxationRevenu.getRevenuFullComplex().getSimpleRevenu().setIsSourcier(true);
                    } else {
                        taxationRevenu.getRevenuFullComplex().getSimpleRevenu().setIsSourcier(false);
                    }
                    // calculate the new rev det
                    taxationRevenu.getRevenuFullComplex().getSimpleRevenu()
                            .setId("NEW_" + taxationRevenu.getRevenuFullComplex().getSimpleRevenu().getId());
                    CalculsRevenuFormules calcul = taxationRevenu.getRevenuFullComplex().getCalculsRevenuFormules();
                    if (taxationRevenu.getRevenuFullComplex().getSimpleRevenu().getIsSourcier()) {
                        taxationRevenu.setRevenuFullComplex(calcul.doCalculSourcier(taxationRevenu
                                .getRevenuFullComplex()));
                        taxationRevenu
                                .getRevenuFullComplex()
                                .getSimpleRevenuSourcier()
                                .setId("NEW_" + taxationRevenu.getRevenuFullComplex().getSimpleRevenuSourcier().getId());
                    } else {
                        taxationRevenu = calcul.doCalcul(taxationRevenu);
                        taxationRevenu
                                .getRevenuFullComplex()
                                .getSimpleRevenuContribuable()
                                .setId("NEW_"
                                        + taxationRevenu.getRevenuFullComplex().getSimpleRevenuContribuable().getId());
                    }
                    // add the revenu to the list
                    revenus.add(taxationRevenu);
                }
            }
        }

        // At the end, add null to indicate a Assiste revenu
        revenus.add(null);

        return revenus;
    }

    private SimpleRevenuDeterminant getSimpleRevenuDeterminant(SimpleRevenu simpleRevenu)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {
        SimpleRevenuDeterminantSearch simpleRevenuDeterminantSearch = new SimpleRevenuDeterminantSearch();
        simpleRevenuDeterminantSearch.setForIdRevenuHistorique(simpleRevenu.getIdRevenuHistorique());
        simpleRevenuDeterminantSearch = AmalImplServiceLocator.getSimpleRevenuDeterminantService().search(
                simpleRevenuDeterminantSearch);

        if (simpleRevenuDeterminantSearch.getSize() == 0) {
            throw new RevenuException("Error ! No matching simpleRevenuDeterminantSearch with idRevenuHistorique : "
                    + simpleRevenu.getIdRevenuHistorique());
        }
        if (simpleRevenuDeterminantSearch.getSize() > 1) {
            throw new RevenuException("Error ! Multiple simpleRevenuDeterminantSearch with idRevenuHistorique : "
                    + simpleRevenu.getIdRevenuHistorique());
        }

        SimpleRevenuDeterminant simpleRevenuDeterminant = (SimpleRevenuDeterminant) simpleRevenuDeterminantSearch
                .getSearchResults()[0];
        return simpleRevenuDeterminant;
    }

    private RevenuHistoriqueComplex historise(RevenuHistoriqueComplex revenuHistoriqueComplex, String anneeHistorique,
            String idRevenu) throws RevenuException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        // SimpleRevenuHistorique revenuHistorique = revenuHistoriqueComplex.getSimpleRevenuHistorique();
        SimpleRevenuHistorique revenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().read(
                revenuHistoriqueComplex.getSimpleRevenuHistorique().getId());
        revenuHistorique.setCodeActif(false);
        revenuHistorique = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().update(revenuHistorique);
        // Création d'un nouveau revenu historique complexe
        RevenuHistoriqueComplex newRevenuHistoriqueComplex = new RevenuHistoriqueComplex();
        // On relis revenuFullComplex pour obtenir les valeurs de la nouvelle taxation liée
        RevenuFullComplex revenuFullComplex = AmalServiceLocator.getRevenuService().readFullComplex(idRevenu);
        revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
        newRevenuHistoriqueComplex.getSimpleRevenuHistorique().setAnneeHistorique(anneeHistorique);
        newRevenuHistoriqueComplex.getSimpleRevenuHistorique().setIdRevenu(idRevenu);
        newRevenuHistoriqueComplex.getSimpleRevenuHistorique().setIdContribuable(revenuHistorique.getIdContribuable());
        newRevenuHistoriqueComplex.getSimpleRevenuHistorique().setCodeActif(true);
        // Set full revenu with linked value
        newRevenuHistoriqueComplex.setRevenuFullComplex(revenuHistoriqueComplex.getRevenuFullComplex());
        revenuHistoriqueComplex = this.create(newRevenuHistoriqueComplex);
        return revenuHistoriqueComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#read(ch
     * .globaz.amal.business.models.revenu.RevenuFullComplex)
     */
    @Override
    public RevenuFullComplex readFullComplex(String idRevenuFullComplex) throws JadePersistenceException,
            RevenuException {
        if (JadeStringUtil.isEmpty(idRevenuFullComplex)) {
            throw new RevenuException("Unable to read revenuFullComplex, the id passed is null!");
        }
        // Read the revenufullcomplex
        RevenuFullComplexSearch revenuFullSearch = new RevenuFullComplexSearch();
        revenuFullSearch.setForIdRevenu(idRevenuFullComplex);
        revenuFullSearch = (RevenuFullComplexSearch) JadePersistenceManager.search(revenuFullSearch);
        // Set the model to the main
        if (revenuFullSearch.getSize() > 0) {
            RevenuFullComplex revenuFullComplex = (RevenuFullComplex) revenuFullSearch.getSearchResults()[0];
            return revenuFullComplex;
        } else {
            RevenuFullComplex returnRevenu = new RevenuFullComplex();
            returnRevenu.setId(idRevenuFullComplex);
            return returnRevenu;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#read(ch
     * .globaz.amal.business.models.revenu.RevenuHistoriqueComplex)
     */
    @Override
    public RevenuHistoriqueComplex readHistoriqueComplex(String idRevenuHistoriqueComplex)
            throws JadePersistenceException, RevenuException {
        if (JadeStringUtil.isEmpty(idRevenuHistoriqueComplex)) {
            throw new RevenuException("Unable to read revenuHistoriqueComplex, the id passed is null!");
        }
        RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
        revenuHistoriqueComplex.setId(idRevenuHistoriqueComplex);
        revenuHistoriqueComplex = (RevenuHistoriqueComplex) JadePersistenceManager.read(revenuHistoriqueComplex);

        // if (revenuHistoriqueComplex.isNew()) {
        // throw new RevenuException("Unable to readHistoriqueComplex, the model is new !");
        // }

        // Read the simple revenu historique
        SimpleRevenuHistorique simpleRevenuHistorique = new SimpleRevenuHistorique();
        simpleRevenuHistorique.setId(revenuHistoriqueComplex.getId());
        simpleRevenuHistorique = (SimpleRevenuHistorique) JadePersistenceManager.read(simpleRevenuHistorique);
        // Read the revenu déterminant
        SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();
        simpleRevenuDeterminant.setIdRevenuDeterminant(simpleRevenuHistorique.getIdRevenuDeterminant());
        simpleRevenuDeterminant = (SimpleRevenuDeterminant) JadePersistenceManager.read(simpleRevenuDeterminant);
        // Read the revenufullcomplex
        RevenuFullComplexSearch revenuFullSearch = new RevenuFullComplexSearch();
        revenuFullSearch.setForIdRevenu(simpleRevenuHistorique.getIdRevenu());
        revenuFullSearch = (RevenuFullComplexSearch) JadePersistenceManager.search(revenuFullSearch);
        // Set the model to the main
        if (revenuFullSearch.getSize() > 0) {
            RevenuFullComplex revenuFullComplex = (RevenuFullComplex) revenuFullSearch.getSearchResults()[0];
            revenuHistoriqueComplex.setRevenuFullComplex(revenuFullComplex);
        }
        revenuHistoriqueComplex.setSimpleRevenuDeterminant(simpleRevenuDeterminant);
        revenuHistoriqueComplex.setSimpleRevenuHistorique(simpleRevenuHistorique);

        // Set the model to the complex model
        return revenuHistoriqueComplex;
    }

    @Override
    public RevenuHistoriqueComplexSearch search(RevenuHistoriqueComplexSearch revenuHistoriqueComplexSearch)
            throws JadePersistenceException, RevenuException {
        if (revenuHistoriqueComplexSearch == null) {
            throw new RevenuException("Unable to search revenuHistoriqueComplex, the search model passed is null!");
        }
        return (RevenuHistoriqueComplexSearch) JadePersistenceManager.search(revenuHistoriqueComplexSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#search(ch
     * .globaz.amal.business.models.revenu.RevenuSearch)
     */
    @Override
    public RevenuSearch search(RevenuSearch revenuSearch) throws JadePersistenceException, RevenuException {
        if (revenuSearch == null) {
            throw new RevenuException("Unable to search dossier, the search model passed is null!");
        }
        return (RevenuSearch) JadePersistenceManager.search(revenuSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#update(ch
     * .globaz.amal.business.models.revenu.RevenuFullComplex)
     */
    @Override
    public RevenuFullComplex update(RevenuFullComplex revenuFullComplex) throws JadePersistenceException,
            RevenuException {
        // Contrôle du paramètre
        if (revenuFullComplex == null) {
            throw new RevenuException("Unable to update revenuFullComplex, the given model is null!");
        }
        // Contrôle du type de revenu
        if (JadeStringUtil.isBlankOrZero(revenuFullComplex.getSimpleRevenu().getTypeRevenu())) {
            throw new RevenuException("Error ! Revenu type not defined !");
        }

        RevenuHistoriqueComplex taxationUsed = checkIfTaxationUsedAndDifferent(revenuFullComplex);

        // Si type de source == MANUEL CCJU >> OK procédons à la mise à jour
        // Si type de source == AUTO FISC >> NOK, copie et création d'un nouveau revenu
        if (IAMCodeSysteme.AMTypeSourceTaxation.MANUELLE.getValue().equals(
                revenuFullComplex.getSimpleRevenu().getTypeSource())
                && (taxationUsed == null)) {
            try {
                // update the simple revenu
                SimpleRevenu simpleRevenu = revenuFullComplex.getSimpleRevenu();
                simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().update(simpleRevenu);
                // udpate the simple revenu contribuable
                SimpleRevenuContribuable simpleRevenuContribuable = revenuFullComplex.getSimpleRevenuContribuable();
                if (!simpleRevenuContribuable.isNew()) {
                    simpleRevenuContribuable = AmalImplServiceLocator.getSimpleRevenuContribuableService().update(
                            simpleRevenuContribuable);
                    revenuFullComplex.setSimpleRevenuContribuable(simpleRevenuContribuable);
                }
                // update the simple revenu sourcier
                SimpleRevenuSourcier simpleRevenuSourcier = revenuFullComplex.getSimpleRevenuSourcier();
                if (!simpleRevenuSourcier.isNew()) {
                    simpleRevenuSourcier = AmalImplServiceLocator.getSimpleRevenuSourcierService().update(
                            simpleRevenuSourcier);
                    revenuFullComplex.setSimpleRevenuSourcier(simpleRevenuSourcier);
                }
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new RevenuException("Service not available - " + e.getMessage());
            }
        } else {
            // Remove the spy and ids for simple Revenu new
            SimpleRevenu simpleRevenuNew = revenuFullComplex.getSimpleRevenu();
            simpleRevenuNew.setTypeSource(IAMCodeSysteme.AMTypeSourceTaxation.MANUELLE.getValue());
            simpleRevenuNew.setSpy(null);
            simpleRevenuNew.setId(null);
            simpleRevenuNew.setIdRevenuHistorique(null);
            // Remove the spy and ids for simplerevenusourcier
            SimpleRevenuSourcier simpleRevenuSourcierNew = revenuFullComplex.getSimpleRevenuSourcier();
            simpleRevenuSourcierNew.setSpy(null);
            simpleRevenuSourcierNew.setId(null);
            simpleRevenuSourcierNew.setIdRevenu(null);
            // Remove the spy and ids for simplerevenucontribuable
            SimpleRevenuContribuable simpleRevenuContribuableNew = revenuFullComplex.getSimpleRevenuContribuable();
            simpleRevenuContribuableNew.setSpy(null);
            simpleRevenuContribuableNew.setId(null);
            simpleRevenuContribuableNew.setIdRevenu(null);
            // Set the model to the revenufullcomplex and call creation
            revenuFullComplex.setSimpleRevenu(simpleRevenuNew);
            revenuFullComplex.setSimpleRevenuSourcier(simpleRevenuSourcierNew);
            revenuFullComplex.setSimpleRevenuContribuable(simpleRevenuContribuableNew);
            revenuFullComplex = this.create(revenuFullComplex);

            // Si on a du créer une nouvelle taxation à la suite de la modification, on la lie au revenu historique en
            // effectuant un update
            // if (taxationUsed != null) {
            // this.update(taxationUsed);
            // }
        }
        return revenuFullComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.RevenuService#update(ch
     * .globaz.amal.business.models.revenu.RevenuHistoriqueComplex)
     */
    @Override
    public RevenuHistoriqueComplex update(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws JadePersistenceException, RevenuException {
        if (revenuHistoriqueComplex == null) {
            throw new RevenuException("Unable to update revenuHistoriqueComplex, the given model is null!");
        }

        if (JadeStringUtil.isBlankOrZero(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                .getTypeRevenu())) {
            throw new RevenuException("Error ! Revenu type not defined !");
        }

        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();
        if (IAMCodeSysteme.CS_TYPE_SOURCIER.equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu()
                .getTypeRevenu())) {
            calculsRevenuFormules.doCalculSourcier(revenuHistoriqueComplex.getRevenuFullComplex());
            calculsRevenuFormules = new CalculsRevenuFormules(revenuHistoriqueComplex);
        }
        revenuHistoriqueComplex = calculsRevenuFormules.doCalcul(revenuHistoriqueComplex);

        try {
            // Contrôle le lien anneehistorique - revenu
            String anneeHistorique = revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique();
            String idRevenu = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getId();
            String idRevenuHistorique = revenuHistoriqueComplex.getSimpleRevenuHistorique().getId();
            RevenuHistoriqueComplex relativHistoriqueComplex = AmalServiceLocator.getRevenuService()
                    .readHistoriqueComplex(idRevenuHistorique);
            // si identique à current revenu historique, do nothing
            if (relativHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique().equals(anneeHistorique)
                    && relativHistoriqueComplex.getSimpleRevenuHistorique().getIdRevenu().equals(idRevenu)) {
                // DO NOTHING
                revenuHistoriqueComplex = relativHistoriqueComplex;
            } else {
                // si changement, historisation et appel du mode création de revenu historique
                revenuHistoriqueComplex = historise(revenuHistoriqueComplex, anneeHistorique, idRevenu);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuException("Service not available - " + e.getMessage());
        }
        return revenuHistoriqueComplex;
    }

}

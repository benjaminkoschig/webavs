package ch.globaz.amal.business.calcul;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Iterator;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.detailFamille.CalculSubsidesException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * Container d'un résultat de calcul de subsides, pour une annee particulière
 * 
 * @author dhi
 * 
 */
public class CalculsSubsidesContainer {

    private String allSubsidesAsString;
    private String anneeHistorique;
    private ArrayList<SimpleFamille> famille;
    private String idContribuable;
    private String idRevenu;
    private String mtRevenuDet;
    private boolean revenuIsTaxation;
    private ArrayList<SimpleDetailFamille> subsides;
    private String typeDemande;

    /**
     * No parameters constructor
     */
    public CalculsSubsidesContainer() {

    }

    /**
     * Default constructor
     * 
     * @param idContribuable
     *            id du contribuable
     * @param anneeHistorique
     *            année du subside
     * @param typeDemande
     *            type de demande (CS)
     * @param idRevenu
     *            l'id du revenu employé, si existant
     * @param revenuIsTaxation
     *            indication si le revenu est une simple taxation
     */
    public CalculsSubsidesContainer(String idContribuable, String anneeHistorique, String typeDemande, String idRevenu,
            boolean revenuIsTaxation) {
        initializeObject(idContribuable, anneeHistorique, typeDemande, idRevenu, revenuIsTaxation);
        try {
            generateSubsides();
        } catch (Exception ex) {
            // JadeThread.logError("Erreur", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Génération des subsides (array list de détail famille) en fonction des paramètres
     * 
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     * @throws CalculSubsidesException
     * @throws FamilleException
     * @throws ParametreAnnuelException
     * @throws DetailFamilleException
     */
    private void generateSubsides() throws RevenuException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, CalculSubsidesException, FamilleException, ParametreAnnuelException,
            DetailFamilleException {

        // new variable
        RevenuHistoriqueComplex currentRevenuHistorique = null;
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();

        // Check le type de demande
        if (getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())
                || getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {

            // récupération des primes moyennes

        } else {

            // Récupération des taxations/revenus
            if (getRevenuIsTaxation()) {
                // Read current taxation
                RevenuFullComplex currentTaxation = AmalServiceLocator.getRevenuService()
                        .readFullComplex(getIdRevenu());

                // Initialization d'un revenu full complex et
                // d'un calcul revenuformule
                // nécessaires aux calculs
                currentRevenuHistorique = new RevenuHistoriqueComplex();
                CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();

                // Set the minimal values
                SimpleRevenuHistorique revenuHistorique = new SimpleRevenuHistorique();
                revenuHistorique.setAnneeHistorique(getAnneeHistorique());
                currentRevenuHistorique.setRevenuFullComplex(currentTaxation);
                currentRevenuHistorique.setSimpleRevenuHistorique(revenuHistorique);
                currentRevenuHistorique = calculsRevenuFormules.doCalcul(currentRevenuHistorique);
            } else {
                currentRevenuHistorique = AmalServiceLocator.getRevenuService().readHistoriqueComplex(getIdRevenu());
            }
        }

        // Les données fiscales et calcul du revenu déterminant se trouve maintenant à jour
        // dans la variable currentRevenuHistorique
        // Récupération des calculs depuis l'ancienne classe CBU
        // TODO : à améliorer
        SimpleDetailFamille simpleDetailFamilleContribuable = new SimpleDetailFamille();
        simpleDetailFamilleContribuable.setAnneeHistorique(getAnneeHistorique());
        simpleDetailFamilleContribuable.setIdContribuable(getIdContribuable());

        AmalSubsideContainer subsideContainer = new AmalSubsideContainer();

        CalculsSubsides calculsSubside = new CalculsSubsides(getAnneeHistorique());
        subsideContainer = calculsSubside.calculSubside(currentRevenuHistorique, simpleDetailFamilleContribuable,
                getTypeDemande());

        // Set arrayList subsides et famille
        subsides = new ArrayList<SimpleDetailFamille>();
        famille = new ArrayList<SimpleFamille>();
        Iterator<String> keyIterator = subsideContainer.getMapSimpleDetailFamille().keySet().iterator();
        while (keyIterator.hasNext()) {
            // Subside
            String detailKey = keyIterator.next();
            SimpleDetailFamille currentDetail = subsideContainer.getMapSimpleDetailFamille().get(detailKey);
            if ((currentDetail != null) && !JadeStringUtil.isEmpty(currentDetail.getIdFamille())) {
                subsides.add(currentDetail);
                // famille
                FamilleContribuable membreFamille = AmalServiceLocator.getFamilleContribuableService().read(
                        currentDetail.getIdFamille());
                String pereMereEnfant = membreFamille.getSimpleFamille().getPereMereEnfant();
                membreFamille.getSimpleFamille()
                        .setPereMereEnfantLibelle(currentSession.getCodeLibelle(pereMereEnfant));
                famille.add(membreFamille.getSimpleFamille());
            }

        }

        if ((currentRevenuHistorique == null)
                || JadeStringUtil.isBlankOrZero(currentRevenuHistorique.getSimpleRevenuDeterminant()
                        .getRevenuDeterminantCalcul())
                || !JadeNumericUtil.isNumeric(currentRevenuHistorique.getSimpleRevenuDeterminant()
                        .getRevenuDeterminantCalcul())) {
            mtRevenuDet = "0.00";
        } else {
            mtRevenuDet = JANumberFormatter.fmt(currentRevenuHistorique.getSimpleRevenuDeterminant()
                    .getRevenuDeterminantCalcul(), false, true, false, 2);
        }
    }

    /**
     * @return the allSubsidesAsString
     */
    public String getAllSubsidesAsString() {
        return allSubsidesAsString;
    }

    /**
     * Get the annee historique
     * 
     * @return
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the famille
     */
    public ArrayList<SimpleFamille> getFamille() {
        return famille;
    }

    /**
     * Get the id contribuable
     * 
     * @return
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * get the id revenu. can be null
     * 
     * @return
     */
    public String getIdRevenu() {
        return idRevenu;
    }

    public String getMtRevenuDet() {
        return mtRevenuDet;
    }

    /**
     * Gets the indication of revenu type (taxation or revenu historique)
     * 
     * @return
     */
    public boolean getRevenuIsTaxation() {
        return revenuIsTaxation;
    }

    /**
     * Gets the generated subsides
     * 
     * @return
     */
    public ArrayList<SimpleDetailFamille> getSubsides() {
        return subsides;
    }

    /**
     * Get the type de demande (CS)
     * 
     * @return
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * Initialisation de l'objet, idem paramètre constructeur
     * 
     * @param idContribuable
     * @param anneeHistorique
     * @param typeDemande
     * @param idRevenu
     * @param revenuIsTaxation
     */
    private void initializeObject(String idContribuable, String anneeHistorique, String typeDemande, String idRevenu,
            boolean revenuIsTaxation) {
        setIdContribuable(idContribuable);
        setAnneeHistorique(anneeHistorique);
        setTypeDemande(typeDemande);
        setIdRevenu(idRevenu);
        setRevenuIsTaxation(revenuIsTaxation);
    }

    /**
     * En entrée, dans getAllSubsidesAsString :
     * 
     * |idContribuable;idMembreFamille;anneeHistorique;typeDemande;montantContribution;montantSupplExtra;codeRefus;
     * debutDroit;FinDroit;noDocument;noCaisse;noAssure;dateReceptionDemande|
     * 
     * Tous les champs se terminent par '_';
     * 
     */
    public void parseAllSubsidesAsStringAndGenerateSubsides() {

        // Création du tableau de subside
        subsides = new ArrayList<SimpleDetailFamille>();

        if (JadeStringUtil.isEmpty(allSubsidesAsString)) {
            return;
        }

        // String d'entrée splittée
        String[] allSubsides = getAllSubsidesAsString().split("\\|");

        // oncharge
        for (int iSubside = 0; iSubside < allSubsides.length; iSubside++) {
            // Split le subside en cours
            String[] currentSubsideValues = allSubsides[iSubside].split("\\;");
            if (currentSubsideValues.length < 13) {
                continue;
            }
            SimpleDetailFamille currentSubside = new SimpleDetailFamille();
            currentSubside
                    .setIdContribuable(currentSubsideValues[0].substring(0, currentSubsideValues[0].length() - 1));
            currentSubside.setIdFamille(currentSubsideValues[1].substring(0, currentSubsideValues[1].length() - 1));
            currentSubside
                    .setAnneeHistorique(currentSubsideValues[2].substring(0, currentSubsideValues[2].length() - 1));
            currentSubside.setTypeDemande(currentSubsideValues[3].substring(0, currentSubsideValues[3].length() - 1));
            currentSubside.setMontantContribution(currentSubsideValues[4].substring(0,
                    currentSubsideValues[4].length() - 1));
            currentSubside.setSupplExtra(currentSubsideValues[5].substring(0, currentSubsideValues[5].length() - 1));
            currentSubside.setRefus(Boolean.parseBoolean(currentSubsideValues[6].substring(0,
                    currentSubsideValues[6].length() - 1)));
            currentSubside.setDebutDroit(currentSubsideValues[7].substring(0, currentSubsideValues[7].length() - 1));
            currentSubside.setFinDroit(currentSubsideValues[8].substring(0, currentSubsideValues[8].length() - 1));
            currentSubside.setNoModeles(currentSubsideValues[9].substring(0, currentSubsideValues[9].length() - 1));
            currentSubside.setNoCaisseMaladie(currentSubsideValues[10].substring(0,
                    currentSubsideValues[10].length() - 1));
            currentSubside.setNoAssure(currentSubsideValues[11].substring(0, currentSubsideValues[11].length() - 1));
            currentSubside.setDateRecepDemande(currentSubsideValues[12].substring(0,
                    currentSubsideValues[12].length() - 1));

            subsides.add(currentSubside);
        }

    }

    /**
     * @param allSubsidesAsString
     *            the allSubsidesAsString to set
     */
    public void setAllSubsidesAsString(String allSubsidesAsString) {
        this.allSubsidesAsString = allSubsidesAsString;
    }

    /**
     * Set the année historique
     * 
     * @param anneeHistorique
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param famille
     *            the famille to set
     */
    public void setFamille(ArrayList<SimpleFamille> famille) {
        this.famille = famille;
    }

    /**
     * Set the id contribuable
     * 
     * @param idContribuable
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * Set the id Revenu
     * 
     * @param idRevenu
     */
    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    public void setMtRevenuDet(String mtRevenuDet) {
        this.mtRevenuDet = mtRevenuDet;
    }

    /**
     * set the flag taxation or revenu historique
     * 
     * @param revenuIsTaxation
     */
    public void setRevenuIsTaxation(boolean revenuIsTaxation) {
        this.revenuIsTaxation = revenuIsTaxation;
    }

    /**
     * Set the subsides
     * 
     * @param subsides
     */
    public void setSubsides(ArrayList<SimpleDetailFamille> subsides) {
        this.subsides = subsides;
    }

    /**
     * Set the type of demande
     * 
     * @param typeDemande
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

}

/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.caissemaladie;

import globaz.amal.process.annonce.cosama.AMCosamaRecord;
import globaz.amal.process.annonce.cosama.AMCosamaRecordDetail;
import globaz.amal.process.annonce.cosama.AMCosamaRecordEnTete;
import globaz.amal.process.annonce.cosama.AMCosamaRecordTotal;
import globaz.amal.process.annonce.cosama.IAMCosamaRecord;
import globaz.amal.process.annonce.fileHelper.AnnonceCMProcessCosamaFileHelper;
import globaz.amal.process.annonce.fileHelper.AnnonceCMProcessCsvFileHelper;
import globaz.amal.process.annonce.fileHelper.AnnonceCMProcessFileHelper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.CaisseMaladieException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieGroupeRCListeSearch;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author cbu
 * 
 */
public class CaisseMaladieServiceImpl implements CaisseMaladieService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService#count(ch.globaz.amal.business.models
     * .caissemaladie.CaisseMaladieSearch)
     */
    @Override
    public int count(CaisseMaladieSearch caisseMaladieSearch) throws CaisseMaladieException, JadePersistenceException {
        if (caisseMaladieSearch == null) {
            throw new CaisseMaladieException("Unable to count CaisseMaladie, the model passed is null!");
        }
        return JadePersistenceManager.count(caisseMaladieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService#createFichierCosamaAnnonce(java.lang
     * .String, java.lang.String)
     */
    @Override
    public String createFichierCosamaAnnonce(String dateAnnonce, String idTiersCM) throws CaisseMaladieException,
            JadePersistenceException {

        List<AMCosamaRecord> allRecords = getCosamaRecords(dateAnnonce, idTiersCM);
        // écriture du fichier
        AnnonceCMProcessFileHelper cosamaFileHelper = AnnonceCMProcessFileHelper
                .getInstance(AnnonceCMProcessFileHelper.FILE_TYPE_COSAMA);
        if ((cosamaFileHelper != null) && (cosamaFileHelper instanceof AnnonceCMProcessCosamaFileHelper)) {
            cosamaFileHelper.setShortFileName("cosamaSubsidesAnnonces");
            cosamaFileHelper.writeFile(allRecords);
            return cosamaFileHelper.getFullFileName();
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService#createFichierListeAnnonce(java.lang
     * .String, java.lang.String)
     */
    @Override
    public String createFichierListeAnnonce(String dateAnnonce, String idTiersCM) throws CaisseMaladieException,
            JadePersistenceException {
        List<AMCosamaRecord> allRecords = getCosamaRecords(dateAnnonce, idTiersCM);
        Collections.sort(allRecords);
        // écriture du fichier
        AnnonceCMProcessFileHelper csvFileHelper = AnnonceCMProcessFileHelper
                .getInstance(AnnonceCMProcessFileHelper.FILE_TYPE_CSV);
        if ((csvFileHelper != null) && (csvFileHelper instanceof AnnonceCMProcessCsvFileHelper)) {
            csvFileHelper.setShortFileName("listeSubsidesAnnonces");
            csvFileHelper.writeFile(allRecords);
            String returnPath = csvFileHelper.getFullFileName();
            return returnPath;
        } else {
            return null;
        }
    }

    /**
     * Génération d'un record cosama en fonction d'un enregistrement annonce
     * 
     * @param currentAnnonce
     * @return
     */
    private AMCosamaRecord generateCosamaRecordDetail(SimpleAnnonce currentAnnonce) {
        AMCosamaRecord returnRecord = null;
        try {
            SimpleDetailFamille currentRelatedSubside = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                    currentAnnonce.getIdDetailFamille());
            if (!currentRelatedSubside.getCodeActif()) {
                // Subside désactivé !
                return null;
            }
            SimpleFamille currentFamille = AmalImplServiceLocator.getSimpleFamilleService().read(
                    currentRelatedSubside.getIdFamille());
            Contribuable currentContribuable = AmalServiceLocator.getContribuableService().read(
                    currentRelatedSubside.getIdContribuable());
            if (JadeStringUtil.isBlankOrZero(currentContribuable.getContribuable().getIdTier())) {
                // Contribuable historique !
                return null;
            }
            AdresseComplexModel currentAdresse = AmalServiceLocator.getContribuableService().getContribuableAdresse(
                    currentContribuable.getContribuable().getIdTier());
            AMCosamaRecord currentRecord = AMCosamaRecord.getInstance(IAMCosamaRecord._TypeEnregistrementDetail);
            if (currentRecord instanceof AMCosamaRecordDetail) {
                // no assuré CM
                ((AMCosamaRecordDetail) currentRecord).setNoAssure(currentAnnonce.getNoAssure());
                // no personnel cantonal >> no contribuable
                ((AMCosamaRecordDetail) currentRecord).setNoPersonnelCantonal(currentContribuable.getPersonneEtendue()
                        .getPersonneEtendue().getNumContribuableActuel());
                // nom prénom
                ((AMCosamaRecordDetail) currentRecord).setNomPrenomUsuel(currentFamille.getNomPrenomUpper());
                // état civil cosama C, D, M, S, V
                if (currentFamille.getIsContribuable()) {
                    String etatCivil = currentContribuable.getPersonneEtendue().getPersonne().getEtatCivil();
                    if (etatCivil.equals(TITiersViewBean.CS_CELIBATAIRE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("C");
                    } else if (etatCivil.equals(TITiersViewBean.CS_DIVORCE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("D");
                    } else if (etatCivil.equals(TITiersViewBean.CS_MARIE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("M");
                    } else if (etatCivil.equals(TITiersViewBean.CS_SEPARE)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("S");
                    } else if (etatCivil.equals(TITiersViewBean.CS_VEUF)) {
                        ((AMCosamaRecordDetail) currentRecord).setEtatCivil("V");
                    }
                }
                if ((currentAdresse != null) && (currentAdresse.getAdresse() != null)
                        && (currentAdresse.getLocalite() != null)) {
                    // Rue ou case postale
                    if (JadeStringUtil.isBlankOrZero(currentAdresse.getAdresse().getCasePostale())) {
                        ((AMCosamaRecordDetail) currentRecord).setNomOfficielRue(currentAdresse.getAdresse()
                                .getNumeroRue().toUpperCase()
                                + ", " + currentAdresse.getAdresse().getRue().toUpperCase());// "NO, RUE");
                    } else {
                        ((AMCosamaRecordDetail) currentRecord).setNomOfficielRue("CASE POSTALE "
                                + currentAdresse.getAdresse().getCasePostale());// "NO, RUE");
                    }
                    // npa
                    ((AMCosamaRecordDetail) currentRecord).setCodePostal(currentAdresse.getLocalite().getNumPostal()
                            .substring(0, 4));
                    // localité
                    ((AMCosamaRecordDetail) currentRecord).setLocalite(currentAdresse.getLocalite().getLocalite()
                            .toUpperCase());
                }
                // date naissance YYYYMMdd
                String dateNaissance = "";
                String sexe = "";
                String noAVS = "";
                // Set Personne Etendue
                // ---------------------------------------------------------------------------
                if (!JadeStringUtil.isEmpty(currentFamille.getIdTier())) {
                    // Set Personne Etendue
                    PersonneEtendueComplexModel personne = new PersonneEtendueComplexModel();
                    personne.setId(currentFamille.getIdTier());
                    personne.getTiers().setIdTiers(currentFamille.getIdTier());
                    try {
                        personne = TIBusinessServiceLocator.getPersonneEtendueService().read(personne.getId());
                        dateNaissance = personne.getPersonne().getDateNaissance();
                        sexe = personne.getPersonne().getSexe();
                        if (JadeStringUtil.isBlankOrZero(dateNaissance)) {
                            dateNaissance = currentFamille.getDateNaissance();
                        }
                        noAVS = personne.getPersonneEtendue().getNumAvsActuel();
                    } catch (Exception exTiers) {
                        exTiers.printStackTrace();
                    }
                } else {
                    dateNaissance = currentFamille.getDateNaissance();
                    noAVS = currentFamille.getNoAVS();
                }
                ((AMCosamaRecordDetail) currentRecord).setDateNaissance(JadeDateUtil.getYMDDate(JadeDateUtil
                        .getGlobazDate(dateNaissance)));
                ((AMCosamaRecordDetail) currentRecord).setNoAVS(noAVS);
                // sexe 1-homme, 2-femme
                if (sexe.equals(TITiersViewBean.CS_FEMME)) {
                    ((AMCosamaRecordDetail) currentRecord).setSexe("2");
                } else {
                    ((AMCosamaRecordDetail) currentRecord).setSexe("1");
                }
                // bénéficiaire pc 0-non, 1-oui
                if (currentAnnonce.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue())) {
                    ((AMCosamaRecordDetail) currentRecord).setBeneficiairePC("1");
                }
                // bénéficiaire aide sociale 0-non, 1 - oui
                if (currentAnnonce.getTypeDemande().equals(IAMCodeSysteme.AMTypeDemandeSubside.ASSISTE.getValue())) {
                    ((AMCosamaRecordDetail) currentRecord).setBeneficiaireAssiste("1");
                }
                // type d'octroi - 4 octroi
                ((AMCosamaRecordDetail) currentRecord).setTypeDecision("4");

                // travail avec les date de fin et de début
                String dateFin = JadeDateUtil.getYMDDate(JadeDateUtil.getGlobazDate("01."
                        + currentAnnonce.getFinDroit()));
                String dateDebut = JadeDateUtil.getYMDDate(JadeDateUtil.getGlobazDate("01."
                        + currentAnnonce.getDebutDroit()));
                String currentYear = "2999";
                if (dateDebut.length() > 3) {
                    currentYear = dateDebut.substring(0, 4);
                }
                String montantAnnuel = "";
                String montantMensuel = "";
                try {
                    int iNbMonth = 0;
                    if (JadeStringUtil.isEmpty(dateFin)) {
                        iNbMonth = JadeDateUtil.getNbMonthsBetween("01." + currentAnnonce.getDebutDroit(), "02.12."
                                + currentYear);
                    } else {
                        iNbMonth = JadeDateUtil.getNbMonthsBetween("01." + currentAnnonce.getDebutDroit(), "02."
                                + currentAnnonce.getFinDroit());
                    }
                    float fMontantContribution = Float.parseFloat(currentAnnonce.getMontantContribution());
                    montantMensuel = "" + ((int) fMontantContribution) + "00";
                    int iTotal = iNbMonth * ((int) fMontantContribution);
                    montantAnnuel = "" + iTotal + "00";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // montant mensuel du subside
                ((AMCosamaRecordDetail) currentRecord).setMontantEffectifSubside(montantMensuel);
                // montant du décompte (mensuel * période)
                ((AMCosamaRecordDetail) currentRecord).setMontantDecompte(montantAnnuel);

                // type de réduction de prime : 1 - rabais en francs, 2- en %, 3- charge assuré
                ((AMCosamaRecordDetail) currentRecord).setTypeReductionPrime("1");
                // montant de la réduction maximale mensuelle en francs
                ((AMCosamaRecordDetail) currentRecord).setMontantMaximumReduction(montantMensuel);
                // début du subside - YYYYMM
                if (dateDebut.length() > 5) {
                    dateDebut = dateDebut.substring(0, 6);
                }
                ((AMCosamaRecordDetail) currentRecord).setDateDebutSubside(dateDebut);
                // fin du subside - YYYYMM
                if (dateFin.length() > 5) {
                    dateFin = dateFin.substring(0, 6);
                } else {
                    dateFin = currentYear + "12";
                }
                ((AMCosamaRecordDetail) currentRecord).setDateFinSubside(dateFin);
            }
            returnRecord = currentRecord;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return returnRecord;
    }

    /**
     * Création des records cosama pour les paramètres donnés
     * 
     * @param dateAnnonce
     * @param idTiersCM
     * @return
     */
    private List<AMCosamaRecord> getCosamaRecords(String dateAnnonce, String idTiersCM) {

        List<AMCosamaRecord> allRecords = new ArrayList<AMCosamaRecord>();

        String noCaisseMaladie = idTiersCM;
        try {
            AdministrationComplexModel caisseModel = TIBusinessServiceLocator.getAdministrationService()
                    .read(idTiersCM);
            noCaisseMaladie = caisseModel.getAdmin().getCodeAdministration();
            noCaisseMaladie = JadeStringUtil.fillWithZeroes(noCaisseMaladie, 4);
        } catch (Exception exAdmin) {
            exAdmin.printStackTrace();
        }
        // -----------------------------------------------------------
        // 1) Génération en-tête
        // -----------------------------------------------------------
        AMCosamaRecord enTete = AMCosamaRecord.getInstance(IAMCosamaRecord._TypeEnregistrementEnTete);
        if (enTete instanceof AMCosamaRecordEnTete) {
            ((AMCosamaRecordEnTete) enTete).setAnnee(dateAnnonce.substring(dateAnnonce.length() - 4));
            ((AMCosamaRecordEnTete) enTete).setCanton(IAMCosamaRecord._CantonJura);
            ((AMCosamaRecordEnTete) enTete).setDateCreation(dateAnnonce);
            ((AMCosamaRecordEnTete) enTete).setMoisDebut("01");
            ((AMCosamaRecordEnTete) enTete).setMoisFin("12");
            ((AMCosamaRecordEnTete) enTete).setNoOrdre("0000");
            ((AMCosamaRecordEnTete) enTete).setNoPartition("1");
            ((AMCosamaRecordEnTete) enTete).setTypeTransmission("12");
            ((AMCosamaRecordEnTete) enTete).setNoCaisseMaladie(noCaisseMaladie);
        }
        allRecords.add(enTete);
        // -----------------------------------------------------------
        // 2) Recherche des annonces pour une date donnée et caisse
        // -----------------------------------------------------------
        SimpleAnnonceSearch annonceSearch = new SimpleAnnonceSearch();
        annonceSearch.setForNoCaisseMaladie(idTiersCM);
        annonceSearch.setForDateAvisRIP(dateAnnonce);
        annonceSearch.setDefinedSearchSize(0);
        try {
            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int iCounter = 0;
        int iCurrentTotal = 0;
        for (int iAnnonce = 0; iAnnonce < annonceSearch.getSize(); iAnnonce++) {
            SimpleAnnonce currentAnnonce = (SimpleAnnonce) annonceSearch.getSearchResults()[iAnnonce];
            // -----------------------------------------------------------
            // 4) Générer les objets cosama subsides
            // -----------------------------------------------------------
            AMCosamaRecord recordDetail = generateCosamaRecordDetail(currentAnnonce);
            if (recordDetail != null) {
                iCounter++;
                ((AMCosamaRecordDetail) recordDetail).setNoArticle("" + iCounter);
                String montantDecompte = ((AMCosamaRecordDetail) recordDetail).getMontantDecompte();
                try {
                    int iMontantDecompte = Integer.parseInt(montantDecompte);
                    iCurrentTotal += iMontantDecompte;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                allRecords.add(recordDetail);
            }
        }
        // ---------------------------------------------------
        // 5) génération total
        // ---------------------------------------------------
        AMCosamaRecord total = AMCosamaRecord.getInstance(IAMCosamaRecord._TypeEnregistrementTotal);
        if (total instanceof AMCosamaRecordTotal) {
            ((AMCosamaRecordTotal) total).setNombreArticles("" + iCounter);
            ((AMCosamaRecordTotal) total).setMontantTotalCumule("" + iCurrentTotal);
        }
        allRecords.add(total);

        return allRecords;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService#read(java.lang.String)
     */
    @Override
    public CaisseMaladie read(String idCaisseMaladie) throws CaisseMaladieException, JadePersistenceException {
        if (idCaisseMaladie == null) {
            throw new CaisseMaladieException("Unable to read CaisseMaladie, id passed is null!");
        }
        CaisseMaladie caisseMaladie = new CaisseMaladie();
        caisseMaladie.setId(idCaisseMaladie);
        return (CaisseMaladie) JadePersistenceManager.read(caisseMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService#search(ch.globaz.amal.business.models
     * .caissemaladie.CaisseMaladieSearch)
     */
    @Override
    public CaisseMaladieSearch search(CaisseMaladieSearch caisseMaladieSearch) throws CaisseMaladieException,
            JadePersistenceException {
        if (caisseMaladieSearch == null) {
            throw new CaisseMaladieException("Unable to search simpleDetailCaisseMaladie, the model passed is null!");
        }
        return (CaisseMaladieSearch) JadePersistenceManager.search(caisseMaladieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.CaisseMaladieService#searchGroupe(ch.globaz.amal.business
     * .models.caissemaladie.CaisseMaladieGroupeRCListeSearch)
     */
    @Override
    public CaisseMaladieGroupeRCListeSearch searchGroupe(
            CaisseMaladieGroupeRCListeSearch caisseMaladieGroupeRCListeSearch) throws CaisseMaladieException,
            JadePersistenceException {
        if (caisseMaladieGroupeRCListeSearch == null) {
            throw new CaisseMaladieException(
                    "Unable to search caisseMaladieGroupeRCListeSearch, the model passed is null!");
        }

        return (CaisseMaladieGroupeRCListeSearch) JadePersistenceManager.search(caisseMaladieGroupeRCListeSearch);
    }

}

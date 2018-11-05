package ch.globaz.amal.process.repriseDecisionsTaxations.step3;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMRubriqueRevenu;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.exceptions.models.uploadfichierreprise.AMUploadFichierRepriseException;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListe;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch;
import ch.globaz.amal.business.models.revenu.Revenu;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuSearch;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSearch;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierReprise;
import ch.globaz.amal.business.models.uploadfichierreprise.SimpleUploadFichierRepriseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.process.repriseDecisionsTaxations.AMProcessRepriseDecisionsTaxationsEnum;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.horizon.jaspe.util.JANumberFormatter;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pyxis.util.TINSSFormater;

public class AMProcessRepriseDecisionsTaxationsEntityHandler
        implements JadeProcessEntityInterface, JadeProcessEntityNeedProperties {
    private static final String RUBRIQUE_DEDUCTION_PERSONNE_CHARGE = "620";
    private static final String TAXATION_OFFICE_SELON_FICHIER_FISC = "5";
    private ContainerParametres containerParametres = null;
    private ch.globaz.amal.xmlns.am_0001._1.Personne currentContribuablePrincipal;
    private ch.globaz.amal.xmlns.am_0001._1.Contribuables currentContribuables = null;
    private JadeProcessEntity currentEntity = null;
    private String currentNoContribuable = null;
    private String currentTypeContribuable = null;
    private String dateTraitement = null;
    private String idContribuable = null;
    private String idJob = null;
    private boolean isRepriseNonTaxes = false;
    private Map<String, ch.globaz.amal.xmlns.am_0001._1.Taxation> mapTaxation = null;
    private int mtTotal = 0;
    private int nbEnfants = 0;
    private Double nbEnfantsAvecDemi = 0.0;
    private int nbEnfantsSuspens = 0;
    private Map<Enum<?>, String> properties = null;
    private Unmarshaller unmarshaller = null;

    public AMProcessRepriseDecisionsTaxationsEntityHandler(ContainerParametres container, Unmarshaller _unmarshaller,
            String _idJob) {
        unmarshaller = _unmarshaller;
        containerParametres = container;
        idJob = _idJob;
    }

    private void addError(Exception e) throws JadeNoBusinessLogSessionError {
        this.addError(e, null);
    }

    private void addError(Exception e, String[] param) throws JadeNoBusinessLogSessionError {

        JadeThread.logError("ERROR", (e.getMessage() != null) ? e.getMessage() : e.toString() + ";", param);
        if (e.getCause() != null) {
            String cause = "<br />" + e.getCause().toString();
            JadeThread.logError("CAUSE", cause + ";");
        }

    }

    private void calculNbEnfantSelonNombre620(int rubPersCharge, String anneeTaxation) {
        try {
            float fRubPersCharge = rubPersCharge;

            countChildrens(anneeTaxation);

            if ((fRubPersCharge == 0) && ((nbEnfants > 0) || (nbEnfantsSuspens > 0))) {
                JadeThread.logWarn("WARNING", "Nombre d'enfants (" + nbEnfantsAvecDemi.intValue()
                        + ") incorrect par rapport au nombre 620 (0);");
                return;
            }

            SimpleDeductionsFiscalesEnfantsSearch deductionsFiscalesEnfantsSearch = new SimpleDeductionsFiscalesEnfantsSearch();
            deductionsFiscalesEnfantsSearch.setForNbEnfant("2");
            deductionsFiscalesEnfantsSearch.setForAnneeTaxationLOE(anneeTaxation);
            deductionsFiscalesEnfantsSearch = AmalServiceLocator.getDeductionsFiscalesEnfantsService()
                    .search(deductionsFiscalesEnfantsSearch);
            float nbDeductionUntil2Enfants = 0;
            if (deductionsFiscalesEnfantsSearch.getSize() > 0) {
                String mt = ((SimpleDeductionsFiscalesEnfants) deductionsFiscalesEnfantsSearch.getSearchResults()[0])
                        .getMontantDeductionParEnfant();
                mt = JANumberFormatter.fmt(mt, false, false, false, 0);
                nbDeductionUntil2Enfants = Float.parseFloat(mt);
            }

            deductionsFiscalesEnfantsSearch = new SimpleDeductionsFiscalesEnfantsSearch();
            deductionsFiscalesEnfantsSearch.setForNbEnfant("3");
            deductionsFiscalesEnfantsSearch.setForAnneeTaxationLOE(anneeTaxation);
            deductionsFiscalesEnfantsSearch = AmalServiceLocator.getDeductionsFiscalesEnfantsService()
                    .search(deductionsFiscalesEnfantsSearch);
            float nbDeduction3MoreEnfants = 0;
            if (deductionsFiscalesEnfantsSearch.getSize() > 0) {
                String mt = ((SimpleDeductionsFiscalesEnfants) deductionsFiscalesEnfantsSearch.getSearchResults()[0])
                        .getMontantDeductionParEnfant();
                mt = JANumberFormatter.fmt(mt, false, false, false, 0);
                nbDeduction3MoreEnfants = Float.parseFloat(mt);
            }

            int iTot = 0;
            int iTotEnfants = nbEnfants + nbEnfantsSuspens;
            if (iTotEnfants > 2) {
                int nb100 = (int) (nbEnfants * nbDeduction3MoreEnfants);
                int nb50 = (int) ((nbEnfantsSuspens * nbDeduction3MoreEnfants) / 2);
                iTot = nb100 + nb50;
            } else {
                int nb100 = (int) (nbEnfants * nbDeductionUntil2Enfants);
                int nb50 = (int) ((nbEnfantsSuspens * nbDeductionUntil2Enfants) / 2);
                iTot = nb100 + nb50;
            }

            if (iTot != rubPersCharge) {
                JadeThread.logWarn("WARNING", "Nombre d'enfants (" + nbEnfantsAvecDemi.intValue()
                        + ") incorrect par rapport au nombre 620 (" + rubPersCharge + ");");
            }
        } catch (Exception e) {
            JadeThread.logError("ERREUR",
                    "Erreur récupération montants déductions enfants pour l'année " + anneeTaxation + ";");
        }
    }

    private void countChildrens(String anneeTaxation) throws JadePersistenceException, AMUploadFichierRepriseException,
            JadeApplicationServiceNotAvailableException, JAXBException, JAException {
        SimpleUploadFichierRepriseSearch simpleUploadFichierRepriseSearch = new SimpleUploadFichierRepriseSearch();
        simpleUploadFichierRepriseSearch.setForNoContribuable(currentNoContribuable);
        simpleUploadFichierRepriseSearch.setLikeTypeReprise("PCHARG");
        simpleUploadFichierRepriseSearch.setForIdJob(idJob);
        simpleUploadFichierRepriseSearch = AmalServiceLocator.getSimpleUploadFichierService()
                .search(simpleUploadFichierRepriseSearch);

        if (simpleUploadFichierRepriseSearch.getSize() > 0) {
            SimpleUploadFichierReprise simpleUploadFichierReprise = (SimpleUploadFichierReprise) simpleUploadFichierRepriseSearch
                    .getSearchResults()[0];
            StringBuffer ios = new StringBuffer(new StringBuffer(simpleUploadFichierReprise.getXmlLignes()));
            ch.globaz.amal.xmlns.am_0002._1.Contribuables contribuablesPersCharge = new ch.globaz.amal.xmlns.am_0002._1.Contribuables();

            contribuablesPersCharge = (ch.globaz.amal.xmlns.am_0002._1.Contribuables) unmarshaller
                    .unmarshal(new StreamSource(new StringReader(ios.toString())));

            int i_nbEnfants = 0;
            int i_nbEnfantsDemis = 0;
            for (ch.globaz.amal.xmlns.am_0002._1.Taxation taxPersCharge : contribuablesPersCharge.getContribuable()
                    .get(0).getTaxations().getTaxation()) {
                if (taxPersCharge.getPeriode().toString().equals(anneeTaxation)) {
                    for (ch.globaz.amal.xmlns.am_0002._1.Personne enfant : taxPersCharge.getPersonne()) {
                        String dateNaiss = enfant.getDateNaiss();
                        JADate dateNaissanceEnfant = new JADate(dateNaiss);
                        if (dateNaissanceEnfant.getYear() <= taxPersCharge.getPeriode().intValue()) {
                            if ("100".equals(enfant.getTauxDeductIc())) {
                                i_nbEnfants++;
                            } else if ("50".equals(enfant.getTauxDeductIc())) {
                                i_nbEnfantsDemis++;
                            }
                        }
                    }
                }
            }

            nbEnfants = i_nbEnfants;
            nbEnfantsSuspens = i_nbEnfantsDemis;
        }
    }

    /**
     * format le numéro au format : xxx.xxx.xxx.xx
     */
    private String formatNoContribuable(String val) {
        if (val == null) {
            return "";
        }

        String str = "";
        for (int i = 0; i < val.length(); i++) {
            str += val.charAt(i);

            switch (i) {
                case 2:
                case 5:
                case 8:
                    str += ".";
                    break;
            }
        }

        return str;
    }

    /**
     * Recherche du contribuable dans les dossiers actifs
     *
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private ContribuableRCListe getContribuable(String searchedNoContribuable, String searchedNSS,
            String searchedFamilyName, String searchedGivenName, String searchedDateOfBirth)
            throws JadeApplicationServiceNotAvailableException {

        ContribuableRCListe currentContribuableRCListe = null;
        ContribuableRCListeSearch currentContribuableRCListeSearch = new ContribuableRCListeSearch();

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoContribuable = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByNSS(currentContribuableRCListeSearch, searchedNSS);
            if (currentContribuableRCListeSearch.getSize() == 1) {
                bSearchByNoContribuable = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoContribuable = false;
                bSearchByName = false;
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableRCListeSearch, searchedDateOfBirth);
                if (currentContribuableRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableRCListeSearch);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }

        // Recherche par N° contribuable, puis date de naissance
        if (bSearchByNoContribuable && !JadeStringUtil.isBlankOrZero(searchedNoContribuable)) {
            JadeLogger.info(null, "RECHERCHE PAR NO CONTRIBUABLE : " + searchedNoContribuable);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByNoContribuable(currentContribuableRCListeSearch, searchedNoContribuable);
            if (currentContribuableRCListeSearch.getSize() == 1) {
                // bSearchByNoNSS = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByName = false;
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableRCListeSearch, searchedDateOfBirth);
                if (currentContribuableRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableRCListeSearch);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByDateOfBirth(currentContribuableRCListeSearch, searchedDateOfBirth);
            if (currentContribuableRCListeSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM : " + searchedFamilyName);
                currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByFamilyName(currentContribuableRCListeSearch, searchedFamilyName);
                if (currentContribuableRCListeSearch.getSize() >= 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR PRENOM : " + searchedGivenName);
                    currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierByGivenName(currentContribuableRCListeSearch, searchedGivenName);
                    if (currentContribuableRCListeSearch.getSize() == 1) {
                        bFound = true;
                    } else if (currentContribuableRCListeSearch.getSize() > 1) {
                        JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                        currentContribuableRCListeSearch = AmalServiceLocator.getContribuableService()
                                .getDossierLastSubside(currentContribuableRCListeSearch);
                        if (currentContribuableRCListeSearch.getSize() == 1) {
                            bFound = true;
                        }
                    }
                }
            }
        }

        if (bFound) {
            currentContribuableRCListe = (ContribuableRCListe) currentContribuableRCListeSearch.getSearchResults()[0];
            String idContribuable = currentContribuableRCListe.getIdContribuable();
            JadeLogger.info(null, "FOUND : " + idContribuable);
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            try {
                return currentContribuableRCListe;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            JadeLogger.info(null, "DOSSIER ACTIF NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }
    }

    /**
     * Recherche du contribuable dans les dossiers historiques
     *
     * @param searchedNSS
     * @param searchedAVS
     * @param searchedFamilyName
     * @param searchedGivenName
     * @param searchedDateOfBirth
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     */
    private SimpleContribuableInfos getContribuableHistorique(String searchedNoContribuable, String searchedNSS,
            String searchedFamilyName, String searchedGivenName, String searchedDateOfBirth)
            throws JadeApplicationServiceNotAvailableException {

        ContribuableHistoriqueRCListe currentContribuableHistoriqueRCListe = null;
        ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch = new ContribuableHistoriqueRCListeSearch();

        boolean bFound = false;
        boolean bSearchByNoNSS = true;
        boolean bSearchByNoContribuable = true;
        boolean bSearchByName = true;

        JadeLogger.info(null, "----------------------------------------------------------------------------------");
        // Recherche par No contribuable, puis date de naissance
        if (bSearchByNoContribuable && !JadeStringUtil.isBlankOrZero(searchedNoContribuable)) {
            JadeLogger.info(null, "RECHERCHE PAR NO CONTRIBUABLE : " + searchedNoContribuable);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByNoContribuable(currentContribuableHistoriqueRCListeSearch, searchedNoContribuable);
            if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                bSearchByNoContribuable = false;
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByNoContribuable = false;
                bSearchByName = false;
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
                if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par NSS, puis date de naissance
        if (bSearchByNoNSS && !JadeStringUtil.isBlankOrZero(searchedNSS)) {
            JadeLogger.info(null, "RECHERCHE PAR NSS : " + searchedNSS);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByNSS(currentContribuableHistoriqueRCListeSearch, searchedNSS);
            if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                bSearchByName = false;
                bFound = true;
            } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR DOB : " + searchedDateOfBirth);
                bSearchByName = false;
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
                if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                    bFound = true;
                } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    }
                }
            }
        }
        // Recherche par date de naissance, puis nom prénom
        if (bSearchByName && !JadeStringUtil.isBlankOrZero(searchedDateOfBirth)
                && !JadeStringUtil.isEmpty(searchedFamilyName) && !JadeStringUtil.isEmpty(searchedGivenName)) {
            JadeLogger.info(null, "RECHERCHE PAR DOB : " + searchedDateOfBirth);
            currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                    .getDossierByDateOfBirth(currentContribuableHistoriqueRCListeSearch, searchedDateOfBirth);
            if (currentContribuableHistoriqueRCListeSearch.getSize() >= 1) {
                JadeLogger.info(null, "SOUS RECHERCHE PAR NOM : " + searchedFamilyName);
                currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                        .getDossierByFamilyName(currentContribuableHistoriqueRCListeSearch, searchedFamilyName);
                if (currentContribuableHistoriqueRCListeSearch.getSize() >= 1) {
                    JadeLogger.info(null, "SOUS RECHERCHE PAR PRENOM : " + searchedGivenName);
                    currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                            .getDossierByGivenName(currentContribuableHistoriqueRCListeSearch, searchedGivenName);
                    if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                        bFound = true;
                    } else if (currentContribuableHistoriqueRCListeSearch.getSize() > 1) {
                        JadeLogger.info(null, "SOUS RECHERCHE PAR DOSSIER ");
                        currentContribuableHistoriqueRCListeSearch = AmalServiceLocator.getContribuableService()
                                .getDossierLastSubside(currentContribuableHistoriqueRCListeSearch);
                        if (currentContribuableHistoriqueRCListeSearch.getSize() == 1) {
                            bFound = true;
                        }
                    }
                }
            }
        }

        if (bFound) {
            currentContribuableHistoriqueRCListe = (ContribuableHistoriqueRCListe) currentContribuableHistoriqueRCListeSearch
                    .getSearchResults()[0];
            String idContribuable = currentContribuableHistoriqueRCListe.getIdContribuableInfo();
            JadeLogger.info(null, "FOUND HISTORIQUE : " + idContribuable);
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            try {
                return AmalServiceLocator.getContribuableService().readInfos(idContribuable);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            JadeLogger.info(null, "DOSSIER HISTORIQUE NOT FOUND");
            JadeLogger.info(null, "----------------------------------------------------------------------------------");
            return null;
        }

    }

    private String getCSCodeProfession(String codeProfession) {
        String codeProfUpper = codeProfession.toUpperCase();
        String codeProf = "";

        if ("S".equals(codeProfUpper)) {
            codeProf = IAMCodeSysteme.CS_CODE_PROF_SALARIE;
        } else if ("I".equals(codeProfUpper)) {
            codeProf = IAMCodeSysteme.CS_CODE_PROF_INDEPENDANT;
        } else if ("A".equals(codeProfUpper)) {
            codeProf = IAMCodeSysteme.CS_CODE_PROF_AGRICULTEUR;
        } else if ("R".equals(codeProfUpper)) {
            codeProf = IAMCodeSysteme.CS_CODE_PROF_RENTIER;
        } else if ("?".equalsIgnoreCase(codeProfUpper)) {
            codeProf = IAMCodeSysteme.CS_CODE_PROF_INCONNU;
        } else {
            codeProf = "0";
        }

        return codeProf;
    }

    private String getCsEtatCivil(int etatCivil) {
        String etatCivilCS = "";
        switch (etatCivil) {
            case 1:
                etatCivilCS = IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE;
                break;
            case 2:
                etatCivilCS = IAMCodeSysteme.CS_ETAT_CIVIL_MARIED;
                break;
            case 3:
                etatCivilCS = IAMCodeSysteme.CS_ETAT_CIVIL_VEUF;
                break;
            case 4:
                etatCivilCS = IAMCodeSysteme.CS_ETAT_CIVIL_DIVORCE;
                break;
            case 5:
                etatCivilCS = IAMCodeSysteme.CS_ETAT_CIVIL_SEPARE;
                break;
            case 9:
                etatCivilCS = IAMCodeSysteme.CS_ETAT_CIVIL_HOIRIE;
                break;
            default:
                etatCivilCS = "0";
                break;
        }

        return etatCivilCS;
    }

    private String getCSTypeTaxation(int typeTaxation) {
        String typeTaxationCS = "";
        switch (typeTaxation) {
            case 1:
                typeTaxationCS = IAMCodeSysteme.CS_GENRE_TAXATION_ORDINAIRE;
                break;
            case 2:
                typeTaxationCS = IAMCodeSysteme.CS_GENRE_TAXATION_PROVISOIRE;
                break;
            case 5:
                typeTaxationCS = IAMCodeSysteme.CS_GENRE_TAXATION_OFFICE;
                break;
            default:
                typeTaxationCS = "0";
                break;
        }

        return typeTaxationCS;
    }

    /**
     * Récupération du nss formatté
     *
     * @return
     */
    private String getCurrentValidFormattedNSS(String val) {
        String currentNSS = "";
        // ------------------------------------------------------------------------------------
        // Récupération du noAVS
        // ------------------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(val)) {
            try {
                String nssFormatted = TINSSFormater.format(val, TINSSFormater.findType(val));
                currentNSS = nssFormatted;
            } catch (Exception e) {
                currentNSS = "";
            }
        } else {
            currentNSS = "";
        }
        return currentNSS;
    }

    private void insertAmountRubrique(RevenuFullComplex revenu, ch.globaz.amal.xmlns.am_0001._1.Rubrique rub) {
        String idRubrique = rub.getId();
        BigInteger value = rub.getValue();

        if (value == null) {
            value = new BigInteger("0");
        }

        if (AMRubriqueRevenu.ALLOCATION_FAMILIALE.getValue().equals(idRubrique)
                || AMRubriqueRevenu.ALLOCATION_FAMILIALE_C.getValue().equals(idRubrique)) {
            if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getAllocationFamiliale())) {
                BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getAllocationFamiliale());
                value = mt.add(value);
            }
            revenu.getSimpleRevenuContribuable().setAllocationFamiliale(value.toString());
        } else if (AMRubriqueRevenu.DEDUCTION_APPRENTIS_ETUDIANTS.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setDeducAppEtu(value.toString());
        } else if (AMRubriqueRevenu.DEDUCTION_COUPLES_MARIES.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setDeductionCouplesMaries(value.toString());
        } else if (AMRubriqueRevenu.EXCEDENT_DEPENSE_PROP_IMMO_COMMERCIALE.getValue().equals(idRubrique)
                || AMRubriqueRevenu.EXCEDENT_DEPENSE_PROP_IMMO_COMMERCIALE_C.getValue().equals(idRubrique)) {
            if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getExcedDepPropImmoComm())) {
                BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getExcedDepPropImmoComm());
                value = mt.add(value);
            }
            revenu.getSimpleRevenuContribuable().setExcedDepPropImmoComm(value.toString());
        } else if (AMRubriqueRevenu.EXCEDENT_DEPENSE_PROP_IMMOB_PRIVE.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setExcedDepPropImmoPriv(value.toString());
        } else if (AMRubriqueRevenu.EXCEDENT_DEPENSE_SUCCESSION_NON_PARTAGEE.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setExcDepSuccNp(value.toString());
        } else if (AMRubriqueRevenu.FORTUNE_IMPOSABLE.getValue().equals(idRubrique)) {
            if (value.compareTo(new BigInteger("0")) == 1) {
                revenu.getSimpleRevenuContribuable().setFortuneImposable(value.toString());
            } else {
                revenu.getSimpleRevenuContribuable().setFortuneImposable("0");
            }
        } else if (AMRubriqueRevenu.FORTUNE_TAUX.getValue().equals(idRubrique)) {
            if (value.compareTo(new BigInteger("0")) == 1) {
                revenu.getSimpleRevenuContribuable().setFortuneTaux(value.toString());
            } else {
                revenu.getSimpleRevenuContribuable().setFortuneTaux("0");
            }
        } else if (AMRubriqueRevenu.INDEMNITE_IMPOSABLE.getValue().equals(idRubrique)
                || AMRubriqueRevenu.INDEMNITE_IMPOSABLE_C.getValue().equals(idRubrique)) {
            if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getIndemniteImposable())) {
                BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getIndemniteImposable());
                value = mt.add(value);
            }
            revenu.getSimpleRevenuContribuable().setIndemniteImposable(value.toString());
        } else if (AMRubriqueRevenu.INTERETS_PASSIFS_COMMERCIAUX.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setInteretsPassifsComm(value.toString());
        } else if (AMRubriqueRevenu.INTERETS_PASSIFS_PRIVE.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setInteretsPassifsPrive(value.toString());
        } else if (AMRubriqueRevenu.PERSONNE_CHARGE_OU_ENFANTS.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setPersChargeEnf(value.toString());
        } else if (AMRubriqueRevenu.PERTE_ACTIVITE_ACC_INDEP.getValue().equals(idRubrique)
                || AMRubriqueRevenu.PERTE_ACTIVITE_ACC_INDEP_C.getValue().equals(idRubrique)) {
            if (value.compareTo(new BigInteger("0")) == -1) {
                value = value.negate();
                if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getPerteActAccInd())) {
                    BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getPerteActAccInd());
                    value = mt.add(value);
                }
                revenu.getSimpleRevenuContribuable().setPerteActAccInd(value.toString());
            }
        } else if (AMRubriqueRevenu.REVENU_ACTIVITE_INDEP.getValue().equals(idRubrique)
                || AMRubriqueRevenu.REVENU_ACTIVITE_INDEP_C.getValue().equals(idRubrique)) {
            // Set la valeur reel dans les champs Independante et Independante épouse
            if (AMRubriqueRevenu.REVENU_ACTIVITE_INDEP.getValue().equals(idRubrique)) {
                revenu.getSimpleRevenuContribuable().setRevenuActIndep(value.toString());
            } else {
                revenu.getSimpleRevenuContribuable().setRevenuActIndepEpouse(value.toString());
            }

            // Prends la valeurs absolute pour la perte si la valeur est negative
            if (value.compareTo(new BigInteger("0")) == -1) {
                value = value.negate();
                if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getPerteActIndep())) {
                    BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getPerteActIndep());
                    value = mt.add(value);
                }
                revenu.getSimpleRevenuContribuable().setPerteActIndep(value.toString());
            }
        } else if (AMRubriqueRevenu.REVENU_ACTIVITE_AGRICOLE.getValue().equals(idRubrique)
                || AMRubriqueRevenu.REVENU_ACTIVITE_AGRICOLE_C.getValue().equals(idRubrique)) {
            // Set la valeur reel dans les champs Agricole et Agricole épouse
            if (AMRubriqueRevenu.REVENU_ACTIVITE_AGRICOLE.getValue().equals(idRubrique)) {
                revenu.getSimpleRevenuContribuable().setRevenuActAgricole(value.toString());
            } else {
                revenu.getSimpleRevenuContribuable().setRevenuActAgricoleEpouse(value.toString());
            }
            // Prends la valeurs absolute pour la perte si la valeur est negative
            if (value.compareTo(new BigInteger("0")) == -1) {
                value = value.negate();
                if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getPerteActAgricole())) {
                    BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getPerteActAgricole());
                    value = mt.add(value);
                }
                revenu.getSimpleRevenuContribuable().setPerteActAgricole(value.toString());
            }
        } else if (AMRubriqueRevenu.PERTE_EXERCICES_COMM.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setPerteExercicesComm(value.toString());
        } else if (AMRubriqueRevenu.PERTE_REPORTEE_EXERCICES_COMM.getValue().equals(idRubrique)
                || AMRubriqueRevenu.PERTE_REPORTEE_EXERCICES_COMM_C.getValue().equals(idRubrique)) {
            if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getPerteCommercial())) {
                BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getPerteCommercial());
                value = mt.add(value);
            }
            revenu.getSimpleRevenuContribuable().setPerteCommercial(value.toString());
        } else if (AMRubriqueRevenu.PERTE_LIQUIDATION.getValue().equals(idRubrique)
                || AMRubriqueRevenu.PERTE_LIQUIDATION_C.getValue().equals(idRubrique)) {
            if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getPerteLiquidation())) {
                BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getPerteLiquidation());
                value = mt.add(value);
            }
            revenu.getSimpleRevenuContribuable().setPerteLiquidation(value.toString());
        } else if (AMRubriqueRevenu.PERTE_SOCIETE.getValue().equals(idRubrique)
                || AMRubriqueRevenu.PERTE_SOCIETE_C.getValue().equals(idRubrique)) {
            if (value.compareTo(new BigInteger("0")) == -1) {
                value = value.negate();
                if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getPerteSociete())) {
                    BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getPerteSociete());
                    value = mt.add(value);
                }
                revenu.getSimpleRevenuContribuable().setPerteSociete(value.toString());
            }
        } else if (AMRubriqueRevenu.RENDEMENT_FORTUNE_IMMOB_COMMERCIALE.getValue().equals(idRubrique)
                || AMRubriqueRevenu.RENDEMENT_FORTUNE_IMMOB_COMMERCIALE_C.getValue().equals(idRubrique)) {
            if (!JadeStringUtil.isBlankOrZero(revenu.getSimpleRevenuContribuable().getRendFortImmobComm())) {
                BigInteger mt = new BigInteger(revenu.getSimpleRevenuContribuable().getRendFortImmobComm());
                value = mt.add(value);
            }
            revenu.getSimpleRevenuContribuable().setRendFortImmobComm(value.toString());
        } else if (AMRubriqueRevenu.RENDEMENT_FORTUNE_IMMOB_PRIVE.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setRendFortImmobPrive(value.toString());
        } else if (AMRubriqueRevenu.REVENU_IMPOSABLE.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setRevenuImposable(value.toString());
        } else if (AMRubriqueRevenu.REVENU_NET_EMPLOI.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setRevenuNetEmploi(value.toString());
        } else if (AMRubriqueRevenu.REVENU_NET_EPOUSE.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setRevenuNetEpouse(value.toString());
        } else if (AMRubriqueRevenu.REVENU_TAUX.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setRevenuTaux(value.toString());
        } else if (AMRubriqueRevenu.TOTAL_REVENUS_NETS.getValue().equals(idRubrique)) {
            revenu.getSimpleRevenuContribuable().setTotalRevenusNets(value.toString());
        } else if (AMRubriqueRevenu.TOTAUX_100_400.getValue().equals(idRubrique)
                || AMRubriqueRevenu.TOTAUX_100C_400C.getValue().equals(idRubrique)) {
            mtTotal += value.intValue();
        } else {
            JadeThread.logError("RUBRIQUE INCONNUE", "LA RUBRIQUE '" + idRubrique + "' N'EXISTE PAS !;");
        }

    }

    private boolean isTaxationAlreadyExist(ch.globaz.amal.xmlns.am_0001._1.Taxation taxation)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {

        ArrayList<String> arrayTypesTaxations = new ArrayList<String>();
        if (isRepriseNonTaxes) {
            arrayTypesTaxations.add(IAMCodeSysteme.AMTaxationType.PROVISOIRE.getValue());
        } else {
            arrayTypesTaxations.add(IAMCodeSysteme.AMTaxationType.ORDINAIRE.getValue());
            arrayTypesTaxations.add(IAMCodeSysteme.AMTaxationType.OFFICE_DETAIL.getValue());
            arrayTypesTaxations.add(IAMCodeSysteme.AMTaxationType.OFFICE_TOTAUX.getValue());
        }

        RevenuSearch revenuSearch = new RevenuSearch();
        revenuSearch.setForIdContribuable(idContribuable);
        revenuSearch.setForTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
        revenuSearch.setForAnneeTaxation(taxation.getPeriode().toString());
        revenuSearch.setInTypeTaxation(arrayTypesTaxations);
        revenuSearch = AmalServiceLocator.getRevenuService().search(revenuSearch);

        for (JadeAbstractModel model : revenuSearch.getSearchResults()) {
            Revenu revenu = (Revenu) model;

            // Si c'est une reprise des non-taxés on ne cherche pas a comparer les montants, si on est la c'est qu'on a
            // deja une taxation provisoire
            if (isRepriseNonTaxes) {
                return true;
            }

            // Si c'est la même date, on sors
            if (JadeDateUtil.areDatesEquals(taxation.getDateDec(), revenu.getDateAvisTaxation())) {
                nbEnfants = Integer.parseInt(revenu.getNbEnfants());
                nbEnfantsSuspens = Integer.parseInt(revenu.getNbEnfantSuspens());
                return true;
            }
        }
        return false;
    }

    /**
     * Recherche du contribuable
     *
     * @return
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws ContribuableException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private String rechercheContribuable() throws JadeNoBusinessLogSessionError, JadePersistenceException,
            ContribuableException, JadeApplicationServiceNotAvailableException {
        String searchedNSS = getCurrentValidFormattedNSS(currentContribuablePrincipal.getNavs13().toString());
        String searchedNoContribuable = formatNoContribuable(currentNoContribuable);
        String searchedFamilyName = currentContribuablePrincipal.getNom();
        String searchedGivenName = currentContribuablePrincipal.getPrenom();
        String searchedDateOfBirth = currentContribuablePrincipal.getDateNaiss();

        ContribuableRCListe currentContribuable = null;
        SimpleContribuableInfos currentContribuableHistorique = null;
        String idContribuableDossier = null;
        // ---------------------------------------------------------------------------------------------
        // 1 - RECHERCHE DOSSIER ACTIF - CONTRIBUABLES STANDARDS
        // ---------------------------------------------------------------------------------------------
        try {
            currentContribuable = getContribuable(searchedNoContribuable, searchedNSS, searchedFamilyName,
                    searchedGivenName, searchedDateOfBirth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------------
        // 2 - RECHERCHE DOSSIER HISTORIQUE - CONTRIBUABLES STANDARDS
        // ---------------------------------------------------------------------------------------------
        try {
            if (currentContribuable == null) {
                currentContribuableHistorique = getContribuableHistorique(searchedNoContribuable, searchedNSS,
                        searchedFamilyName, searchedGivenName, searchedDateOfBirth);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------------
        // 4 - CONCLUSION DOSSIER TROUVE OU NON
        // ---------------------------------------------------------------------------------------------
        if ((currentContribuable == null) && (currentContribuableHistorique == null)) {
            JadeThread.logError("ERREUR", "Dossier inexistant !");
            return null;
        } else {
            if (currentContribuable != null) {
                idContribuableDossier = currentContribuable.getId();
            } else if (currentContribuableHistorique != null) {
                idContribuableDossier = currentContribuableHistorique.getId();
            }
        }

        return idContribuableDossier;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        try {
            if (!"1".equals(currentTypeContribuable)) {
                if ("2".equals(currentTypeContribuable)) {
                    JadeThread.logInfo("INFO",
                            "Contribuable de type 'Confédéré' (typeCtr = 2) --> Aucun enregistrement de la taxation;");
                } else if ("3".equals(currentTypeContribuable)) {
                    JadeThread.logInfo("INFO",
                            "Contribuable de type 'Etranger' (typeCtr = 3) --> Aucun enregistrement de la taxation;");
                } else if ("4".equals(currentTypeContribuable)) {
                    JadeThread.logInfo("INFO",
                            "Contribuable de type 'Parti-pour' (typeCtr = 4) --> Aucun enregistrement de la taxation;");
                } else {
                    JadeThread.logInfo("INFO", "Contribuable de type inconnu (" + currentTypeContribuable
                            + ") --> Aucun enregistrement de la taxation;");
                }
                return;
            }

            // ---------------------------------------------------------------------------------------------
            // 0 - CHECK VALIDITE DE L'ENTITE et du contribuable
            // ---------------------------------------------------------------------------------------------
            if (JadeStringUtil.isEmpty(idContribuable)) {
                idContribuable = rechercheContribuable();
                if (JadeStringUtil.isEmpty(idContribuable)) {
                    JadeThread.logError("ERREUR", "Id Contribuable manquant " + currentNoContribuable + ";");
                    return;
                }
            }
            // ---------------------------------------------------------------------------------------------
            // Itération sur les revenus du fichier XML
            // ---------------------------------------------------------------------------------------------
            for (Iterator it = mapTaxation.keySet().iterator(); it.hasNext();) {
                try {
                    String key = (String) it.next();
                    ch.globaz.amal.xmlns.am_0001._1.Taxation taxation = mapTaxation.get(key);

                    boolean taxationExist = isTaxationAlreadyExist(taxation);

                    if (taxationExist) {
                        JadeThread.logInfo("INFO",
                                "Taxation " + taxation.getPeriode().toString() + " déjà présente !;");
                    } else {

                        if ((taxation.getDateDec() == null) && (taxation.getEcTax() == null)
                                && (taxation.getRdu() == null)) {
                            SimpleRevenuSearch revenuPrecedentSearch = new SimpleRevenuSearch();
                            revenuPrecedentSearch.setForIdContribuable(idContribuable);
                            revenuPrecedentSearch.setOrderKey("orderByAnneeTaxation");
                            revenuPrecedentSearch = AmalImplServiceLocator.getSimpleRevenuService()
                                    .search(revenuPrecedentSearch);
                            SimpleRevenu revenuPrecedent = new SimpleRevenu();
                            String etatCivil = "";
                            String codeProf = "";
                            if (revenuPrecedentSearch.getSize() > 0) {
                                revenuPrecedent = (SimpleRevenu) revenuPrecedentSearch.getSearchResults()[0];
                                if (JadeStringUtil.isBlankOrZero(currentContribuablePrincipal.getEcPers().toString())) {
                                    etatCivil = revenuPrecedent.getEtatCivil();
                                } else {
                                    etatCivil = getCsEtatCivil(currentContribuablePrincipal.getEcPers().intValue());
                                }

                                codeProf = revenuPrecedent.getProfession();
                            } else {
                                etatCivil = getCsEtatCivil(currentContribuablePrincipal.getEcPers().intValue());
                                codeProf = "";
                            }
                            // ---------------------------------------------------------------------------------------------
                            // 1 - INSCRIPTION DU REVENU
                            // ---------------------------------------------------------------------------------------------
                            RevenuFullComplex revenuToCreate = new RevenuFullComplex();
                            // ---------------------------------------------------------------------------------------------
                            // TODO Reprendre les valeurs de la taxation précédente pour EC, NbEnfants, NbJours,....
                            revenuToCreate.getSimpleRevenu().setAnneeTaxation(taxation.getPeriode().toString());
                            revenuToCreate.getSimpleRevenu().setDateTraitement(dateTraitement);
                            revenuToCreate.getSimpleRevenu().setCodeSuspendu("2");
                            revenuToCreate.getSimpleRevenu().setIsSourcier(false);
                            revenuToCreate.getSimpleRevenu().setNbJours("365");
                            revenuToCreate.getSimpleRevenu().setEtatCivil(etatCivil);
                            revenuToCreate.getSimpleRevenu().setProfession(codeProf);
                            revenuToCreate.getSimpleRevenu().setTypeTaxation(getCSTypeTaxation(2));
                            revenuToCreate.getSimpleRevenu().setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
                            revenuToCreate.getSimpleRevenu().setIdContribuable(idContribuable);
                            revenuToCreate.getSimpleRevenu()
                                    .setTypeSource(IAMCodeSysteme.AMTypeSourceTaxation.AUTO_FISC.getValue());
                            revenuToCreate.getSimpleRevenu().setRevDetUniqueOuiNon(false);
                            revenuToCreate.getSimpleRevenu().setRevDetUnique("0");

                            RevenuHistoriqueComplex revenuHistorique = new RevenuHistoriqueComplex();
                            revenuHistorique.setRevenuFullComplex(revenuToCreate);

                            // Création du revenu
                            AmalServiceLocator.getRevenuService().create(revenuHistorique.getRevenuFullComplex());
                            JadeThread.logInfo("INFO", "Taxation provisoire crée pour l'année :"
                                    + taxation.getPeriode().toString() + " !;");
                        } else {
                            RevenuFullComplex revenuToCreate = new RevenuFullComplex();
                            // Simple Revenu
                            // ---------------------------------------------------------------------------------------------
                            revenuToCreate.getSimpleRevenu().setAnneeTaxation(taxation.getPeriode().toString());
                            revenuToCreate.getSimpleRevenu().setDateAvisTaxation(taxation.getDateDec());
                            revenuToCreate.getSimpleRevenu().setDateTraitement(dateTraitement);
                            revenuToCreate.getSimpleRevenu().setDateSaisie(taxation.getDateDec());
                            revenuToCreate.getSimpleRevenu().setCodeSuspendu("2");
                            revenuToCreate.getSimpleRevenu().setIsSourcier(false);
                            int rubPersCharge = 0;
                            for (ch.globaz.amal.xmlns.am_0001._1.Rubrique rub : taxation.getRub()) {
                                if (AMProcessRepriseDecisionsTaxationsEntityHandler.RUBRIQUE_DEDUCTION_PERSONNE_CHARGE
                                        .equals(rub.getId())) {
                                    rubPersCharge = rub.getValue().intValue();
                                    if (!AMProcessRepriseDecisionsTaxationsEntityHandler.TAXATION_OFFICE_SELON_FICHIER_FISC
                                            .equals(taxation.getGenre().toString())) {
                                        calculNbEnfantSelonNombre620(rubPersCharge, taxation.getPeriode().toString());
                                    }
                                }
                            }

                            revenuToCreate.getSimpleRevenu().setNbEnfants(String.valueOf(nbEnfants));
                            revenuToCreate.getSimpleRevenu().setNbEnfantSuspens(String.valueOf(nbEnfantsSuspens));
                            revenuToCreate.getSimpleRevenu()
                                    .setEtatCivil(getCsEtatCivil(taxation.getEcTax().intValue()));
                            revenuToCreate.getSimpleRevenu().setNbJours(taxation.getNbJours().toString());
                            revenuToCreate.getSimpleRevenu()
                                    .setTypeTaxation(getCSTypeTaxation(taxation.getGenre().intValue()));
                            revenuToCreate.getSimpleRevenu()
                                    .setProfession(getCSCodeProfession(taxation.getCodeProfession()));
                            revenuToCreate.getSimpleRevenu().setTypeRevenu(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE);
                            revenuToCreate.getSimpleRevenu().setIdContribuable(idContribuable);
                            revenuToCreate.getSimpleRevenu()
                                    .setTypeSource(IAMCodeSysteme.AMTypeSourceTaxation.AUTO_FISC.getValue());
                            if (taxation.getRdu().compareTo(new BigInteger("0")) == 1) {
                                revenuToCreate.getSimpleRevenu().setRevDetUniqueOuiNon(true);
                                revenuToCreate.getSimpleRevenu().setRevDetUnique(taxation.getRdu().toString());
                            } else {
                                revenuToCreate.getSimpleRevenu().setRevDetUniqueOuiNon(false);
                                revenuToCreate.getSimpleRevenu().setRevDetUnique("0");
                            }

                            RevenuHistoriqueComplex revenuHistorique = new RevenuHistoriqueComplex();
                            revenuHistorique.setRevenuFullComplex(revenuToCreate);

                            // Insertion des montants dans la bonne rubrique
                            String rduMax = containerParametres.getParametresAnnuelsProvider()
                                    .getListeParametresAnnuels().get(IAMParametresAnnuels.CS_RDU_MAXIMUM)
                                    .getFormatedLastValue(false, false, false, 0);
                            if (taxation.getRdu().compareTo(new BigInteger(rduMax)) == -1) {
                                mtTotal = 0;
                                for (ch.globaz.amal.xmlns.am_0001._1.Rubrique rub : taxation.getRub()) {
                                    insertAmountRubrique(revenuHistorique.getRevenuFullComplex(), rub);
                                }
                                if (!JadeStringUtil.isBlankOrZero(revenuHistorique.getRevenuFullComplex()
                                        .getSimpleRevenuContribuable().getTotalRevenusNets())
                                        && (Integer.parseInt(revenuHistorique.getRevenuFullComplex()
                                                .getSimpleRevenuContribuable().getTotalRevenusNets()) != mtTotal)) {
                                    JadeThread.logInfo("INFO", "Le montant total dans la rubrique 490 ("
                                            + revenuHistorique.getRevenuFullComplex().getSimpleRevenuContribuable()
                                                    .getTotalRevenusNets()
                                            + ") ne correspond pas au résultat du calcul 480+480C (" + mtTotal + ");");
                                }

                                BigInteger anneeHistorique = taxation.getPeriode().add(new BigInteger("2"));
                                revenuHistorique.getSimpleRevenuHistorique()
                                        .setAnneeHistorique(anneeHistorique.toString());
                                revenuHistorique.setRevenuFullComplex(revenuHistorique.getRevenuFullComplex());
                            } else {
                                revenuHistorique.getRevenuFullComplex().getSimpleRevenuContribuable()
                                        .setRevenuImposable("70000");
                            }

                            if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                                // Création du revenu
                                AmalServiceLocator.getRevenuService().create(revenuHistorique.getRevenuFullComplex());
                                JadeThread.logInfo("INFO",
                                        "Taxation année  " + taxation.getPeriode().toString() + " crée;");
                            } else {
                                JadeThread.logInfo("INFO", "Erreur lors de la création de la taxation "
                                        + taxation.getPeriode().toString() + ";");
                            }
                        }
                    }
                    saveInfosDossier(idContribuable);
                } catch (Exception e) {
                    JadeThread.logError("ERREUR REVENU CONTRIBUABLE",
                            "ERREUR ENREGISTREMENT REVENU CONTRIBUABLE " + e.getMessage() + ";");
                }
            }
        } catch (Exception e) {
            this.addError(e);
        } finally {
            currentContribuablePrincipal = null;
            mapTaxation = null;
        }
    }

    /**
     * Enregistrement de informations utiles aux étapes suivantes
     *
     * @param idContribuableDossier
     * @param newDossier
     */
    private void saveInfosDossier(String idContribuableDossier) {
        SimpleUploadFichierReprise fileUploaded = null;
        try {
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(currentEntity.getIdRef());
            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                String typeDossier = "A";
                fileUploaded.setCustomValue(
                        idContribuableDossier + ";" + nbEnfants + ";" + nbEnfantsSuspens + ";" + typeDossier);
                fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().update(fileUploaded);
                JadeThread.logInfo("INFO", "Informations de reprise mis à jour !;");
            }
        } catch (Exception e) {
            currentContribuables = null;
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        currentEntity = entity;
        String idUpload = entity.getIdRef();
        SimpleUploadFichierReprise fileUploaded = null;
        try {
            fileUploaded = AmalServiceLocator.getSimpleUploadFichierService().read(idUpload);
            // Si plusieurs données dans custom values, voici la séquence :
            // idContribuable;nbEnfants;nbEnfantsSuspens;Type (Actif ou historique)
            if (fileUploaded.getCustomValue().indexOf(";") >= 0) {
                String[] infos = fileUploaded.getCustomValue().split(";");
                idContribuable = infos[0];
                nbEnfants = new Integer(infos[1]);
                nbEnfantsSuspens = new Integer(infos[2]);
                nbEnfantsAvecDemi = nbEnfants + (0.5 * nbEnfantsSuspens);
            } else {
                idContribuable = fileUploaded.getCustomValue();
            }

            if (!JadeStringUtil.isEmpty(fileUploaded.getXmlLignes())) {
                StringBuffer ios = new StringBuffer(new StringBuffer(fileUploaded.getXmlLignes()));
                currentContribuables = (ch.globaz.amal.xmlns.am_0001._1.Contribuables) unmarshaller
                        .unmarshal(new StreamSource(new StringReader(ios.toString())));
                dateTraitement = currentContribuables.getParamJobDate();
                for (ch.globaz.amal.xmlns.am_0001._1.Contribuable c : currentContribuables.getContribuable()) {
                    currentTypeContribuable = c.getTypeCtr().toString();
                    currentNoContribuable = c.getNdc();
                    for (ch.globaz.amal.xmlns.am_0001._1.Personne p : c.getPersonne()) {
                        if (ch.globaz.amal.xmlns.am_0001._1.TypePersonne.PRINCIPAL.equals(p.getType())) {
                            currentContribuablePrincipal = p;
                        }
                    }
                    mapTaxation = new HashMap<String, ch.globaz.amal.xmlns.am_0001._1.Taxation>();
                    for (ch.globaz.amal.xmlns.am_0001._1.Taxation taxation : c.getTaxations().getTaxation()) {
                        mapTaxation.put(taxation.getPeriode().toString(), taxation);
                    }
                }
            }
            currentContribuables = null;
        } catch (Exception e) {
            currentContribuables = null;
        }
    }

    @Override
    public void setProperties(Map<Enum<?>, String> hashMap) {
        properties = hashMap;

        if (properties.containsKey(AMProcessRepriseDecisionsTaxationsEnum.IS_FIN_ANNEE)) {
            isRepriseNonTaxes = true;
        }
    }

}

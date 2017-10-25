package globaz.amal.process.statistiques;

import globaz.amal.process.AMALabstractProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageAdresse;
import ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageCarteCulture;
import ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostagePyxis;
import ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageSimpleDetailFamille;
import ch.globaz.amal.business.constantes.IAMPublipostage.AMPublipostageSimpleFamille;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AMStatistiquesPublipostageProcess extends AMALabstractProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_DOMAINE_AMAL = "42002700";
    public final static String CS_TYPE_COURRIER = "508001";
    private String inNumeroContribuable = new String();
    private String inTypeDemande = new String();
    private boolean isCodeActif = false;
    private boolean isContribuable = false;
    private boolean isCarteCulture = false;
    private ArrayList<String> listValues = new ArrayList<String>();
    private int recordsSize = 0;
    private StringBuffer sbCols = new StringBuffer();
    private String wantedFields = new String();
    private String wantedNpa = new String();
    private String yearSubside = new String();

    /**
     * Génère le fichier selon les colonnes choisies dans la liste déroulante. Génération dans un Stringbuffer dans un
     * premier temps
     * 
     * @param mapFamille
     * @throws Exception
     */
    private void generateFile(Map<String, Map<String, Map<String, String>>> mapFamille) throws Exception {
        StringBuffer sb = new StringBuffer();
        List<String> wantedCols = new ArrayList<String>();
        try {
            wantedCols = JadeStringUtil.tokenize(wantedFields, ",");
            int pos = 0;
            for (Map<String, Map<String, String>> valueMapFamille : mapFamille.values()) {
                for (Map<String, String> valueMapMembreFamille : valueMapFamille.values()) {
                    for (String colName : valueMapMembreFamille.keySet()) {
                        if (wantedCols.contains(colName)) {
                            if (pos == 0) {
                                sbCols.append(colName + ";");
                            }
                            sb.append(valueMapMembreFamille.get(colName) + ";");
                        }
                    }
                    listValues.add(sb.toString());
                    sb = new StringBuffer();
                    pos++;
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur génération fichier publipostage ! ==> " + e.getMessage());
        }
    }

    /**
     * Récupération des valeurs de l'adresse
     * 
     * @param mapValues
     * @param idContribuable
     * @param adresseTiersDetail
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws Exception
     */
    private void getAdresseValues(Map<String, String> mapValues, String idContribuable,
            AdresseTiersDetail adresseTiersDetail) throws FamilleException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, Exception {
        String dateToday = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateToday = sdf.format(cal.getTime());
        // On récupère le chef de famille pour l'adresse
        SimpleFamilleSearch familleSearch = AmalImplServiceLocator.getSimpleFamilleService().getChefDeFamille(
                idContribuable);
        if (familleSearch.getSize() == 1) {
            SimpleFamille sf = (SimpleFamille) familleSearch.getSearchResults()[0];

            try {
                adresseTiersDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(sf.getIdTier(), true,
                        dateToday, AMStatistiquesPublipostageProcess.CS_DOMAINE_AMAL,
                        AMStatistiquesPublipostageProcess.CS_TYPE_COURRIER, null);
            } catch (Exception e) {
                throw new Exception("Erreur récupération de l'adresse ==> " + e.getMessage());
            }

        } else {
            throw new Exception("Erreur récupération chef de famille !");
        }
        try {
            mapValues.put(AMPublipostageAdresse.TITRE.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_TITRE));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.TITRE.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.NOM.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T1));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.NOM.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.PRENOM.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T2));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.PRENOM.getValue(), "");
        }

        boolean getDesignation = false;
        try {
            String designation1 = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1);
            String designation2 = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2);
            String nom = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T1);
            String prenom = adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_T2);
            String nomPrenom = nom + " " + prenom;
            String prenomNom = prenom + " " + nom;

            // -------------------------------------------------
            // Contrôle que NomPrenom != Designation1
            // Contrôle que PrenomNom != Designation1
            // Contrôle que Designation1 ne contienne pas Nom
            // Contrôle que Designation1 ne contienne pas Prénom
            // -------------------------------------------------
            if (nomPrenom.equalsIgnoreCase(designation1) || prenomNom.equalsIgnoreCase(designation1)
                    || (JadeStringUtil.contains(designation1, nom) && JadeStringUtil.contains(designation1, prenom))) {
                getDesignation = false;
            } else {
                getDesignation = true;
            }

            // -------------------------------------------------
            // Contrôle que D1!=T1 && D2!=T2 (ou Designation1 != Nom && Designation2 != Prenom)
            // Contrôle que D1!=T2 && D2!=T1 (ou Designation1 != Prenom && Designation2 != Nom)
            // -------------------------------------------------
            if (getDesignation) {
                if ((prenom.equals(designation1) && nom.equals(designation2))
                        || (nom.equals(designation1) && prenom.equals(designation2))) {
                    getDesignation = false;
                } else {
                    getDesignation = true;
                }
            }
        } catch (Exception e) {
            getDesignation = false;
        }

        try {
            if (getDesignation) {
                mapValues.put(AMPublipostageAdresse.DESIGNATION1.getValue(),
                        adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1));
            } else {
                mapValues.put(AMPublipostageAdresse.DESIGNATION1.getValue(), "");
            }
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.DESIGNATION1.getValue(), "");
        }
        try {
            if (getDesignation) {
                mapValues.put(AMPublipostageAdresse.DESIGNATION2.getValue(),
                        adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2));
            } else {
                mapValues.put(AMPublipostageAdresse.DESIGNATION2.getValue(), "");
            }
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.DESIGNATION2.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.DESIGNATION3.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D3));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.DESIGNATION3.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.DESIGNATION4.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D4));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.DESIGNATION4.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.RUE.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.RUE.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.NUMERO.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.NUMERO.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.CASEPOSTALE.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.CASEPOSTALE.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.NPA.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.NPA.getValue(), "");
        }
        try {
            mapValues.put(AMPublipostageAdresse.LOCALITE.getValue(),
                    adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
        } catch (Exception e) {
            mapValues.put(AMPublipostageAdresse.LOCALITE.getValue(), "");
        }
    }

    @Override
    public String getDescription() {
        return "Process de création d'une liste de publipostage";

    }

    public String getInNumeroContribuable() {
        return inNumeroContribuable;
    }

    public String getInTypeDemande() {
        return inTypeDemande;
    }

    /**
     * 
     * @param id
     *            code system to find
     * @param codeGroupe
     *            AMMODELES, AMTYDE, etc...
     * @return
     */
    public String getLibelleCodeSysteme(String id) {

        if ((id == null) || (id.length() <= 0)) {
            return "";
        }

        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        cm.setSession(getSession());
        cm.setForCodeUtilisateur(id);
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(getSession().getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            // Si la recherche pète on ne fait rien, le libelle sera "" (vide)
        }

        if (cm.getContainer().size() < 1) {
            return "";
        }

        FWParametersCode code = (FWParametersCode) cm.getEntity(0);

        if (code == null) {
            return "";
        } else {
            return code.getLibelle();
        }
    }

    @Override
    public String getName() {
        return toString();
    }

    /**
     * Récupération des valeurs PYXIS
     * 
     * @param mapValues
     * @param familleContribuable
     */
    private void getPyxisValues(Map<String, String> mapValues, FamilleContribuable familleContribuable) {
        mapValues.put(AMPublipostagePyxis.NUMCONTRIBUABLE.getValue(), familleContribuable.getPersonneEtendue()
                .getPersonneEtendue().getNumContribuableActuel());
        mapValues.put(AMPublipostagePyxis.NNSS.getValue(), familleContribuable.getPersonneEtendue()
                .getPersonneEtendue().getNumAvsActuel());
    }

    /**
     * Récupération des valeurs CARTECULTURE
     * 
     * @param mapValues
     * @param familleContribuable
     */
    private void getCarteCultureValues(Map<String, String> mapValues, FamilleContribuable familleContribuable) {
        mapValues.put(AMPublipostageCarteCulture.CARTECULTURE.getValue(), familleContribuable.getSimpleFamille()
                .getCarteCulture() ? "Oui" : "Non");
    }

    public int getRecordsSize() {
        return recordsSize;
    }

    /**
     * Récupération des valeurs de SimpleDetailFamille
     * 
     * @param mapValues
     * @param familleContribuable
     */
    private void getSimpleDetailFamilleValues(Map<String, String> mapValues, FamilleContribuable familleContribuable) {
        mapValues.put(AMPublipostageSimpleDetailFamille.TYPEDEMANDE.getValue(),
                getSession().getCode(familleContribuable.getSimpleDetailFamille().getTypeDemande()));
        mapValues.put(AMPublipostageSimpleDetailFamille.MONTANTCONTRIBUTION.getValue(), familleContribuable
                .getSimpleDetailFamille().getMontantContribution());
        if (familleContribuable.getSimpleDetailFamille().getSupplExtra() == null) {
            mapValues.put(AMPublipostageSimpleDetailFamille.MONTANTCONTRIBUTIONSUPPLEMENT.getValue(), "0.00");
        } else {
            mapValues.put(AMPublipostageSimpleDetailFamille.MONTANTCONTRIBUTIONSUPPLEMENT.getValue(),
                    familleContribuable.getSimpleDetailFamille().getSupplExtra());
        }
        mapValues.put(AMPublipostageSimpleDetailFamille.DOCUMENT.getValue(), getLibelleCodeSysteme(getSession()
                .getCode(familleContribuable.getSimpleDetailFamille().getNoModeles())));
        mapValues.put(AMPublipostageSimpleDetailFamille.CODETRAITEMENTDOSSIER.getValue(),
                getSession().getCode(familleContribuable.getSimpleDetailFamille().getCodeTraitementDossier()));

        String noCaisseMaladie = familleContribuable.getSimpleDetailFamille().getNoCaisseMaladie();
        if (!JadeStringUtil.isBlankOrZero(noCaisseMaladie)) {
            try {
                AdministrationComplexModel cm = TIBusinessServiceLocator.getAdministrationService().read(
                        familleContribuable.getSimpleDetailFamille().getNoCaisseMaladie());
                mapValues.put(AMPublipostageSimpleDetailFamille.ASSUREUR.getValue(), cm.getTiers().getDesignation1());
            } catch (Exception e) {
                mapValues.put(AMPublipostageSimpleDetailFamille.ASSUREUR.getValue(), "");
            }
        } else {
            mapValues.put(AMPublipostageSimpleDetailFamille.ASSUREUR.getValue(), "");
        }
        mapValues.put(AMPublipostageSimpleDetailFamille.DEBUTDROIT.getValue(), familleContribuable
                .getSimpleDetailFamille().getDebutDroit());
        mapValues.put(AMPublipostageSimpleDetailFamille.FINDROIT.getValue(), familleContribuable
                .getSimpleDetailFamille().getFinDroit());
        mapValues.put(AMPublipostageSimpleDetailFamille.CODE_ACTIF.getValue(), familleContribuable
                .getSimpleDetailFamille().getCodeActif() ? "Oui" : "Non");
    }

    /**
     * Récupération des valeurs de SimpleFamille
     * 
     * @param mapValues
     * @param familleContribuable
     */
    private void getSimpleFamilleValues(Map<String, String> mapValues, FamilleContribuable familleContribuable) {
        mapValues.put(AMPublipostageSimpleFamille.IDCONTRIBUABLE.getValue(), familleContribuable.getSimpleFamille()
                .getIdContribuable());
        mapValues.put(AMPublipostageSimpleFamille.ISCONTRIBUABLEPRINCIPAL.getValue(), familleContribuable
                .getSimpleFamille().getIsContribuable() ? "Oui" : "Non");
        // mapValues.put(AMPublipostageSimpleFamille.ISCARTECULTURE.getValue(), familleContribuable.getSimpleFamille()
        // .getIsCarteCulture() ? "Oui" : "Non");
        mapValues.put(AMPublipostageSimpleFamille.NOMPRENOM.getValue(), familleContribuable.getSimpleFamille()
                .getNomPrenom());
        mapValues.put(AMPublipostageSimpleFamille.PEREMEREENFANT.getValue(),
                getSession().getCode(familleContribuable.getSimpleFamille().getPereMereEnfant()));
        mapValues.put(AMPublipostageSimpleFamille.DATENAISSANCE.getValue(), familleContribuable.getSimpleFamille()
                .getDateNaissance());
        try {
            JADate d = new JADate(familleContribuable.getSimpleFamille().getDateNaissance());
            mapValues.put(AMPublipostageSimpleFamille.DATENAISSANCE_YYYYMMDD.getValue(), d.toAMJ().toString());
        } catch (JAException e) {
            mapValues.put(AMPublipostageSimpleFamille.DATENAISSANCE_YYYYMMDD.getValue(), "");
        }

        mapValues.put(AMPublipostageSimpleFamille.DATEFINDEFINITIVE.getValue(), familleContribuable.getSimpleFamille()
                .getFinDefinitive());
        mapValues.put(AMPublipostageSimpleFamille.CODEFIN.getValue(),
                getSession().getCodeLibelle(familleContribuable.getSimpleFamille().getCodeTraitementDossier()));
        mapValues.put(AMPublipostageSimpleFamille.SEXE.getValue(),
                getSession().getCode(familleContribuable.getSimpleFamille().getSexe()));
        mapValues.put(AMPublipostageSimpleFamille.NOPERSONNE.getValue(),
                getSession().getCode(familleContribuable.getSimpleFamille().getNoPersonne()));
    }

    /**
     * Insert le contenu du stackTrace dans une String pour affichage dans le mail
     * 
     * @param aThrowable
     * @return Stacktrace sous forme de String
     */
    public String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public String getWantedFields() {
        return wantedFields;
    }

    public String getWantedNpa() {
        return wantedNpa;
    }

    public String getYearSubside() {
        return yearSubside;
    }

    public boolean isCodeActif() {
        return isCodeActif;
    }

    public boolean isContribuable() {
        return isContribuable;
    }

    public boolean isCarteCulture() {
        return isCarteCulture;
    }

    @Override
    protected void process() {
        Map<String, String> mapValues = new LinkedHashMap<String, String>();
        Map<String, String> mapValuesToSave = new LinkedHashMap<String, String>();
        Map<String, Map<String, String>> mapMembreFamille = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> mapMembreFamilleToSave = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Map<String, Map<String, String>>> mapFamille = new LinkedHashMap<String, Map<String, Map<String, String>>>();
        int size = 0;
        try {
            try {
                FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
                familleContribuableSearch.setForAnneeHistorique(yearSubside);
                if (!JadeStringUtil.isBlankOrZero(inTypeDemande) && !JadeStringUtil.isNull(inTypeDemande)) {
                    List listInTypeDemande = JadeStringUtil.tokenize(inTypeDemande, ",");
                    familleContribuableSearch.setInTypeDemande(listInTypeDemande);
                }
                if (!JadeStringUtil.isBlankOrZero(inNumeroContribuable) && !JadeStringUtil.isNull(inNumeroContribuable)) {
                    List listInNoContribuables = JadeStringUtil.tokenize(inNumeroContribuable, ",");
                    familleContribuableSearch.setInNumerosContribuables(listInNoContribuables);
                }
                if (isContribuable) {
                    familleContribuableSearch.setForIsContribuable(true);
                }
                if (isCarteCulture) {
                    familleContribuableSearch.setForIsCarteCulture(true);
                }
                if (isCodeActif) {
                    familleContribuableSearch.setForCodeActif(true);
                }
                familleContribuableSearch.setDefinedSearchSize(recordsSize);
                familleContribuableSearch.setOrderKey("processPublipostage");
                // familleContribuableSearch.setForContribuableActif(true);
                familleContribuableSearch = AmalServiceLocator.getFamilleContribuableService().search(
                        familleContribuableSearch);

                String idContribuable = "";
                String idMembreFamille = "";
                String idDetailFamille = "";
                size = familleContribuableSearch.getSize();
                int ind = 0;
                for (JadeAbstractModel model : familleContribuableSearch.getSearchResults()) {
                    FamilleContribuable familleContribuable = (FamilleContribuable) model;

                    SimpleFamille sf = AmalImplServiceLocator.getSimpleFamilleService().read(
                            familleContribuable.getSimpleFamille().getIdFamille());

                    familleContribuable.setSimpleFamille(sf);

                    ind++;

                    if (!JadeStringUtil.isBlankOrZero(idContribuable)
                            && !idContribuable.equals(familleContribuable.getSimpleFamille().getIdContribuable())) {
                        if (!mapMembreFamille.isEmpty()) {
                            mapMembreFamilleToSave = new LinkedHashMap<String, Map<String, String>>();
                            mapMembreFamilleToSave.putAll(mapMembreFamille);
                            mapFamille.put(idContribuable, mapMembreFamilleToSave);
                            mapMembreFamille.clear();
                        }
                    }

                    idContribuable = familleContribuable.getSimpleFamille().getIdContribuable();
                    idMembreFamille = familleContribuable.getSimpleFamille().getId();
                    idDetailFamille = familleContribuable.getSimpleDetailFamille().getId();

                    AdresseTiersDetail adresseTiersDetail = null;

                    // *************************RECUPERATION DATA SIMPLEFAMILLE************************//
                    getSimpleFamilleValues(mapValues, familleContribuable);

                    // *************************RECUPERATION DATA SIMPLEDETAILFAMILLE******************//
                    getSimpleDetailFamilleValues(mapValues, familleContribuable);

                    // *************************RECUPERATION DATA PYXIS********************************//
                    getPyxisValues(mapValues, familleContribuable);

                    // *************************RECUPERATION ADRESSE***********************************//
                    getAdresseValues(mapValues, idContribuable, adresseTiersDetail);

                    // *************************RECUPERATION CARTECULTURE***********************************//
                    getCarteCultureValues(mapValues, familleContribuable);

                    List listNpa = traitementWantedNpa();
                    if (!JadeStringUtil.isBlankOrZero(wantedNpa)
                            && !listNpa.contains(mapValues.get(AMPublipostageAdresse.NPA.getValue()))) {
                        continue;
                    }

                    mapValuesToSave = new LinkedHashMap<String, String>();
                    mapValuesToSave.putAll(mapValues);
                    mapMembreFamille.put(idMembreFamille + idDetailFamille, mapValuesToSave);
                    mapValues.clear();

                    if (size == ind) {
                        // Ajout du dernier enregistrement dans la map
                        mapMembreFamilleToSave = new LinkedHashMap<String, Map<String, String>>();
                        mapMembreFamilleToSave.putAll(mapMembreFamille);
                        mapFamille.put(idContribuable, mapMembreFamilleToSave);
                    }
                }

            } catch (Exception e) {
                throw new Exception("Erreur récupération valeurs fichier publipostage ! ==> " + e.getMessage());
            }

            String[] fileNames = null;
            if (size > 0) {
                generateFile(mapFamille);
                fileNames = writeFile();
            }

            sendConfirmationMail(fileNames, size);
        } catch (Exception e) {
            // Send mail
            String subject = "Web@AMal - Publipostage en erreur !";
            String message = "Le publipostage ne s'est pas terminé correctement ! \n\n\n" + e.getMessage() + "\n\n\n"
                    + getStackTrace(e);
            try {
                JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                        subject, message, null);
            } catch (Exception e2) {
                JadeThread.logError("AMStatistiquesPublipostages.SendMail",
                        "Erreur envoi du mail ! ==> " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    private void sendConfirmationMail(String[] fileNames, int size) throws Exception {
        String subject = "Web@AMal - Publipostage terminé avec succès !";
        String message = "Le publipostage s'est terminé correctement.\n\n";

        message += "Critères :\n";
        if (isContribuable) {
            message += "Uniquement contribuable principal ? : Oui \n";
        } else {
            message += "Uniquement contribuable principal ? : Non \n";
        }
        if (isCarteCulture) {
            message += "Uniquement cas avec CarteCulture ? : Oui \n";
        } else {
            message += "Uniquement cas avec CarteCulture  ? : Non \n";
        }
        if (isCodeActif) {
            message += "Uniquement subside actifs ? : Oui \n";
        }
        message += "Année : " + yearSubside + "\n";
        if (!JadeStringUtil.isBlankOrZero(wantedNpa)) {
            String s_inNPA = JadeStringUtil.change(wantedNpa, ",", " / ");
            message += "NPA : " + s_inNPA + "\n";
        }
        if (!JadeStringUtil.isBlankOrZero(inNumeroContribuable) && !JadeStringUtil.isNull(inNumeroContribuable)) {
            String s_inNumeroContribuable = JadeStringUtil.change(inNumeroContribuable, ",", " / ");
            message += "No contribuables : " + s_inNumeroContribuable + "\n";
        }
        List listInTypeDemande = JadeStringUtil.tokenize(inTypeDemande, ",");
        message += "Type demande : ";
        int nb = 0;
        for (Object object : listInTypeDemande) {
            nb++;
            String tyDe = (String) object;
            message += getSession().getCodeLibelle(tyDe);

            if (nb < listInTypeDemande.size()) {
                message += ", ";
            }
        }

        if (size == 0) {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    "Aucun dossier trouvé !", null);
        } else {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    message, fileNames);
        }
    }

    public void setCodeActif(boolean isCodeActif) {
        this.isCodeActif = isCodeActif;
    }

    public void setInNumeroContribuable(String inNumeroContribuable) {
        this.inNumeroContribuable = inNumeroContribuable;
    }

    public void setInTypeDemande(String inTypeDemande) {
        this.inTypeDemande = inTypeDemande;
    }

    public void setIsContribuable(boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    public void setIsCarteCulture(boolean isCarteCulture) {
        this.isCarteCulture = isCarteCulture;
    }

    public void setRecordsSize(int recordsSize) {
        this.recordsSize = recordsSize;
    }

    public void setWantedFields(String wantedFields) {
        this.wantedFields = wantedFields;
    }

    public void setWantedNpa(String wantedNpa) {
        this.wantedNpa = wantedNpa;
    }

    public void setYearSubside(String yearSubside) {
        this.yearSubside = yearSubside;
    }

    private List traitementWantedNpa() {
        List arrayListNpa = JadeStringUtil.tokenize(wantedNpa, ",");

        return arrayListNpa;

    }

    /**
     * Ecriture du contenu dans un fichier
     * 
     * @return
     * @throws Exception
     */
    private String[] writeFile() throws Exception {
        String[] fileNames = new String[1];
        try {
            // Create file
            FileWriter fstream;
            BufferedWriter out;
            fileNames[0] = Jade.getInstance().getPersistenceDir() + "publipostage_" + yearSubside + ".csv";
            try {
                fstream = new FileWriter(new File(fileNames[0]));
                out = new BufferedWriter(fstream);
            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }

            try {
                out.write(sbCols.toString());
                out.write("\n");
                for (String s : listValues) {
                    out.write(s);
                    out.write("\n");
                }
            } catch (Exception e) {// Catch exception if any
                System.err.println("Error: " + e.getMessage());
                throw new Exception(e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (Exception ex) {
                    throw new Exception(ex.getMessage());
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur écriture fichier publipostage ! ==> " + e.getMessage());
        }
        return fileNames;
    }
}

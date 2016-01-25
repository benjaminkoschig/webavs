package globaz.corvus.vb.demandes;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.utils.REGestionnaireHelper;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.INSSViewBean;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author HPE
 * 
 */

public class RESaisieDemandeRenteViewBean extends PRAbstractViewBeanSupport implements INSSViewBean {

    public static final String CS_ADMIN_GENRE_OFFICE_AI = "509004";
    private static final String PROP_DEFAULT_CANTON_CAISSE_LOCATION = "default.canton.caisse.location";

    private String codeOfficeAiApi = "";
    private String codeOfficeAiInv = "";
    private String csAnneeAnticipationVie = "";
    private String csAtteinteApi = "";
    private String csAtteinteInv = "";
    private String csCantonRequerant = "";
    private String csDegreImpotence = "";
    private String csEtat = "";
    private String csGenreDroitApi = "";
    private String csGenrePrononceAiApi = "";
    private String csGenrePrononceAiInv = "";
    private String csInfirmiteApi = "";
    private String csInfirmiteInv = "";
    private String csNationaliteRequerant = "";
    private String csSexeRequerant = "";
    private String csTypeCalcul = "";
    private String csTypeDemandeRente = "";
    private String dateDebut = "";
    private String dateDebutImpotence = "";
    private String dateDebutInvalidite = "";
    private String dateDebutRedNonCollaboration = "";
    private String dateDecesRequerant = "";
    private String dateDepot = "";
    private String dateFin = "";
    private String dateFinImpotence = "";
    private String dateFinInvalidite = "";
    private String dateFinRedNonCollaboration = "";
    private String dateNaissanceRequerant = "";
    private String dateReception = "";
    private String dateRevocationRequerantVie = "";
    private String dateSurvenanceEvenementAssureAPI = "";
    private String dateSurvenanceEvenementAssureInv = "";
    private String dateTraitement = "";
    private String degreInvalidite = "";
    private String idDemandePrestation = "";
    private String idDemandeRente = "";
    private String idDemandeRenteParent = "";
    private String idGestionnaire = "";
    private String idInfoComplementaire = "";
    private String idPeriodeAPI = "";
    private String idPeriodeInvalidite = "";
    private Long idProvisoirePeriodeAPI = 0l;
    private Long idProvisoirePeriodeINV = 0l;
    private String idRenteCalculee = "";
    private String idRequerant = "";
    private String idTiers = "";
    private PRInfoCompl infoCompl = null;
    private String infoComplCsTiersResponsable = "";
    private Boolean isAccuseDeReception = Boolean.FALSE;
    private Boolean isAjournementRequerantVie = Boolean.FALSE;
    private Boolean isAssistancePratique = Boolean.FALSE;
    private Boolean isCalculerCopie = Boolean.FALSE;
    private Boolean isCelibataireSansEnfantsInv = Boolean.FALSE;
    private Boolean isCelibataireSansEnfantsVieillesse = Boolean.FALSE;
    private boolean isPassageHera = false;
    private Boolean isResidenceDansHome = Boolean.FALSE;
    private boolean modifie = false;
    private String nbPagesMotivationApi = "";
    private String nbPagesMotivationInv = "";
    private Integer nextKeyPeriodeAPI = new Integer(0);
    private Integer nextKeyPeriodeINV = new Integer(0);
    private String nomOfficeAiApi = "";
    private String nomOfficeAiInv = "";
    private String nomRequerant = "";
    private String nssRequerant = "";
    private PRTiersWrapper officeAICantonal = null;
    private TreeSet<REPeriodeAPIViewBean> periodesAPI = new TreeSet<REPeriodeAPIViewBean>();
    private TreeSet<REPeriodeInvaliditeViewBean> periodesInvalidite = new TreeSet<REPeriodeInvaliditeViewBean>();
    private String pourcentageReductionInv = "";
    private String pourcentageReductionSur = "";
    private String pourcentRedFauteGrave = "";
    private String pourcentRedNonCollaboration = "";
    private String prenomRequerant = "";
    private String provenance = "";
    private transient Vector<String[]> responsables = null;
    private BSession session;
    private BSpy spy;
    private boolean trouveDansCI = false;
    private boolean trouveDansTiers = false;
    private String typePrestationHistorique = "";

    public String getTypePrestationHistorique() {
        return typePrestationHistorique;
    }

    public void setTypePrestationHistorique(String typePrestationHistorique) {
        this.typePrestationHistorique = typePrestationHistorique;
    }

    public String getCodeCsAtteinteApi() throws Exception {
        return getSession().getCode(getCsAtteinteApi());
    }

    public String getCodeCsAtteinteInv() throws Exception {
        return getSession().getCode(getCsAtteinteInv());
    }

    public String getCodeCsInfirmiteApi() throws Exception {
        return getSession().getCode(getCsInfirmiteApi());
    }

    public String getCodeCsInfirmiteInv() throws Exception {
        return getSession().getCode(getCsInfirmiteInv());
    }

    public String getCodeOfficeAiApi() {
        return codeOfficeAiApi;
    }

    public String getCodeOfficeAiInv() {
        return codeOfficeAiInv;
    }

    public BSpy getCreationSpy() {

        REDemandeRente demandeRente = new REDemandeRente();

        try {
            demandeRente = REDemandeRente.loadDemandeRente(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdDemandeRente(), getCsTypeDemandeRente());
        } catch (Exception e) {
        }

        if (demandeRente instanceof REDemandeRenteAPI) {
            REDemandeRenteAPI rente = (REDemandeRenteAPI) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteInvalidite) {
            REDemandeRenteInvalidite rente = (REDemandeRenteInvalidite) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteSurvivant) {
            REDemandeRenteSurvivant rente = (REDemandeRenteSurvivant) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteVieillesse) {
            REDemandeRenteVieillesse rente = (REDemandeRenteVieillesse) demandeRente;
            return rente.getCreationSpy();
        } else {
            return null;
        }

    }

    public String getCsAnneeAnticipationVie() {
        return csAnneeAnticipationVie;
    }

    public String getCsAtteinteApi() {
        return csAtteinteApi;
    }

    public String getCsAtteinteInv() {
        return csAtteinteInv;
    }

    @Override
    public String getCsCantonDomicile() {
        return null;
    }

    public String getCsCantonRequerant() {
        return csCantonRequerant;
    }

    public String getCsDegreImpotence() {
        return csDegreImpotence;
    }

    public String getCsEtat() {
        return csEtat;
    }

    @Override
    public String getCsEtatCivil() {
        return null;
    }

    public String getCsGenreDroitApi() {
        return csGenreDroitApi;
    }

    public String getCsGenrePrononceAiApi() {
        return csGenrePrononceAiApi;
    }

    public String getCsGenrePrononceAiInv() {
        return csGenrePrononceAiInv;
    }

    public String getCsInfirmiteApi() {
        return csInfirmiteApi;
    }

    public String getCsInfirmiteInv() {
        return csInfirmiteInv;
    }

    @Override
    public String getCsNationalite() {
        return null;
    }

    public String getCsNationaliteRequerant() {
        return csNationaliteRequerant;
    }

    @Override
    public String getCsSexe() {
        return null;
    }

    public String getCsSexeRequerant() {
        return csSexeRequerant;
    }

    public String getCsTypeCalcul() {
        return csTypeCalcul;
    }

    public String getCsTypeDemandeRente() {
        return csTypeDemandeRente;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDebutImpotence() {
        return dateDebutImpotence;
    }

    public String getDateDebutInvalidite() {
        return dateDebutInvalidite;
    }

    public String getDateDebutRedNonCollaboration() {
        return dateDebutRedNonCollaboration;
    }

    @Override
    public String getDateDeces() {
        return null;
    }

    public String getDateDecesRequerant() {
        return dateDecesRequerant;
    }

    public String getDateDepot() {
        return dateDepot;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateFinImpotence() {
        return dateFinImpotence;
    }

    public String getDateFinInvalidite() {
        return dateFinInvalidite;
    }

    public String getDateFinRedNonCollaboration() {
        return dateFinRedNonCollaboration;
    }

    @Override
    public String getDateNaissance() {
        return null;
    }

    public String getDateNaissanceRequerant() {
        return dateNaissanceRequerant;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateRevocationRequerantVie() {
        return dateRevocationRequerantVie;
    }

    public String getDateSurvenanceEvenementAssureAPI() {
        return dateSurvenanceEvenementAssureAPI;
    }

    public String getDateSurvenanceEvenementAssureInv() {
        return dateSurvenanceEvenementAssureInv;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    @Override
    public String getIdAssure() {
        return null;
    }

    public String getIdDemandePrestation() {
        return idDemandePrestation;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdDemandeRenteParent() {
        return idDemandeRenteParent;
    }

    public String getIdGestionnaire() {

        if (JadeStringUtil.isEmpty(idGestionnaire)) {
            setIdGestionnaire(getSession().getUserId());
        }

        return idGestionnaire;
    }

    public String getIdInfoComplementaire() {
        return idInfoComplementaire;
    }

    public String getIdPeriodeAPI() {
        return idPeriodeAPI;
    }

    public String getIdPeriodeInvalidite() {
        return idPeriodeInvalidite;
    }

    public Long getIdProvisoirePeriodeAPI() {
        return idProvisoirePeriodeAPI;
    }

    public Long getIdProvisoirePeriodeINV() {
        return idProvisoirePeriodeINV;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getInfoComplCsTiersResponsable() {

        if (JadeStringUtil.isIntegerEmpty(infoComplCsTiersResponsable)) {

            if (!JadeStringUtil.isIntegerEmpty(getIdInfoComplementaire())) {

                PRInfoCompl ic = new PRInfoCompl();
                ic.setSession(getSession());
                ic.setIdInfoCompl(getIdInfoComplementaire());
                try {
                    ic.retrieve();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!ic.isNew()) {
                    infoComplCsTiersResponsable = ic.getCsTiersResponsable();
                }
            }
        }

        return infoComplCsTiersResponsable;
    }

    public String getInfosRequerant() {

        if (JadeStringUtil.isEmpty(getNssRequerant())) {
            return "Requérant pas encore saisi";
        } else {
            return getNssRequerant() + " - " + getNomRequerant() + " " + getPrenomRequerant() + " - "
                    + getDateNaissanceRequerant();
        }

    }

    public Boolean getIsAccuseDeReception() {
        return isAccuseDeReception;
    }

    public Boolean getIsAjournementRequerantVie() {
        return isAjournementRequerantVie;
    }

    public Boolean getIsAssistancePratique() {
        return isAssistancePratique;
    }

    public Boolean getIsCalculerCopie() {
        return isCalculerCopie;
    }

    public Boolean getIsCelibataireSansEnfantsInv() {
        return isCelibataireSansEnfantsInv;
    }

    public Boolean getIsCelibataireSansEnfantsVieillesse() {
        return isCelibataireSansEnfantsVieillesse;
    }

    public Boolean getIsResidenceDansHome() {
        return isResidenceDansHome;
    }

    /**
     * Retourne la liste des codes systèmes pour l'état du droit augmentée des champs 'tous' et 'non définitif'.
     * 
     * @return un Vector de String[2]{noOfficeAI, nomOfficeAI}.
     */
    public List<String[]> getListeOfficeAI() {
        List<String[]> result = new ArrayList<String[]>();
        PRTiersWrapper[] officesAI = null;

        try {
            officesAI = PRTiersHelper.getAdministrationActiveForGenre(getSession(),
                    RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        } catch (Exception e) {
            // erreur dans la recherche dews offoce AI
            e.printStackTrace();
        }

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {
                result.add(0, new String[] { officesAI[i].getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION),
                        officesAI[i].getProperty(PRTiersWrapper.PROPERTY_NOM) });

            }
        }

        return result;
    }

    public Map<String, String> getMapOfficesAi() {
        SortedMap<String, String> sortedOffices = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        try {
            for (PRTiersWrapper office : PRTiersHelper.getAdministrationActiveForGenre(getSession(),
                    RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI)) {
                sortedOffices.put(office.getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION),
                        office.getProperty(PRTiersWrapper.PROPERTY_NOM));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sortedOffices;
    }

    public String getNbPagesMotivationApi() {
        return nbPagesMotivationApi;
    }

    public String getNbPagesMotivationInv() {
        return nbPagesMotivationInv;
    }

    public synchronized Integer getNextKeyPeriodeAPI() {
        Integer key = nextKeyPeriodeAPI;

        if (nextKeyPeriodeAPI.intValue() == Integer.MAX_VALUE) {
            nextKeyPeriodeAPI = new Integer(0);
        } else {
            nextKeyPeriodeAPI = new Integer(nextKeyPeriodeAPI.intValue() + 1);
        }

        return key;
    }

    public synchronized Integer getNextKeyPeriodeINV() {
        Integer key = nextKeyPeriodeINV;

        if (nextKeyPeriodeINV.intValue() == Integer.MAX_VALUE) {
            nextKeyPeriodeINV = new Integer(0);
        } else {
            nextKeyPeriodeINV = new Integer(nextKeyPeriodeINV.intValue() + 1);
        }

        return key;
    }

    @Override
    public String getNom() {
        return null;
    }

    public String getNomOfficeAiApi() {
        return nomOfficeAiApi;
    }

    public String getNomOfficeAICantonal() {
        if (officeAICantonal == null) {
            officeAICantonal = getOfficeAICantonal();
        }

        if (officeAICantonal != null) {
            return officeAICantonal.getProperty(PRTiersWrapper.PROPERTY_NOM);
        }
        return "";
    }

    public String getNomOfficeAiInv() {
        return nomOfficeAiInv;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    /**
     * Retourne le no de l'office AI du canton de la caisse
     * 
     * @return
     */
    public String getNoOfficeAICantonal() {
        if (officeAICantonal == null) {
            officeAICantonal = getOfficeAICantonal();
        }

        if (officeAICantonal != null) {
            return officeAICantonal.getProperty(PRTiersWrapper.PROPERTY_CODE_ADMINISTRATION);
        }
        return "";
    }

    @Override
    public String getNss() {
        return null;
    }

    public String getNssRequerant() {
        return nssRequerant;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNssRequerant(), isNNSS().equals("true") ? true : false);
    }

    private PRTiersWrapper getOfficeAICantonal() {

        PRTiersWrapper[] officesAI = null;

        try {
            officesAI = PRTiersHelper.getAdministrationActiveForGenre(getSession(),
                    RESaisieDemandeRenteViewBean.CS_ADMIN_GENRE_OFFICE_AI);
        } catch (Exception e) {
            // erreur dans la recherche des offices AI
            e.printStackTrace();
            return null;
        }

        if (officesAI != null) {
            for (int i = 0; i < officesAI.length; i++) {

                try {
                    // si canton caisse
                    if (officesAI[i].getProperty(PRTiersWrapper.PROPERTY_ID_CANTON).equalsIgnoreCase(
                            getSession().getApplication().getProperty(
                                    RESaisieDemandeRenteViewBean.PROP_DEFAULT_CANTON_CAISSE_LOCATION))) {
                        return officesAI[i];
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        }

        return null;
    }

    public TreeSet<REPeriodeAPIViewBean> getPeriodesAPI() {
        return periodesAPI;
    }

    public TreeSet<REPeriodeInvaliditeViewBean> getPeriodesInvalidite() {
        return periodesInvalidite;
    }

    public String getPourcentageReductionInv() {
        return pourcentageReductionInv;
    }

    public String getPourcentageReductionSur() {
        return pourcentageReductionSur;
    }

    public String getPourcentRedFauteGrave() {
        return pourcentRedFauteGrave;
    }

    public String getPourcentRedNonCollaboration() {
        return pourcentRedNonCollaboration;
    }

    @Override
    public String getPrenom() {
        return null;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    /**
     * Retourne le vecteur de tableaux a 2 entrées {userId, userFullName} des gestionnaires pour le type de prestation
     * défini pour ce view bean.
     * 
     * <p>
     * Le vecteur n'est créé qu'une seule fois pour chaque instance de ce view bean.
     * </p>
     * 
     * @return la valeur courante de l'attribut responsable data
     */
    public Vector getResponsableData() {
        if (responsables == null) {

            try {
                responsables = PRGestionnaireHelper.getIdsEtNomsGestionnaires(((REApplication) GlobazSystem
                        .getApplication(REApplication.DEFAULT_APPLICATION_CORVUS))
                        .getProperty(REApplication.PROPERTY_GROUPE_CORVUS_GESTIONNAIRE));

            } catch (Exception e) {
                session.addError(session.getLabel(REGestionnaireHelper.ERREUR_GESTIONNAIRES_INTROUVABLE));
            }

            // on veut une ligne vide
            if (responsables == null) {
                responsables = new Vector<String[]>();
            }

        }

        responsables.insertElementAt(new String[] { "", "" }, 0);

        return responsables;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    @Override
    public BSpy getSpy() {
        return spy;
    }

    public Vector getTiPays() throws Exception {
        return PRTiersHelper.getPays(getSession());
    }

    public boolean isInfoComplRenteVeuvePerdure() {

        loadInfoCompl();
        if (null != infoCompl) {
            if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE.equals(infoCompl
                    .getTypeInfoCompl())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isModifiable() throws Exception {

        // si la demande est dans l'état validé, aucunes modifications n'est
        // possible
        if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(getCsEtat())
                || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(getCsEtat())) {
            return false;

        } else {
            return true;
        }

    }

    public boolean isModifie() {
        return modifie;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNssRequerant())) {
            return "";
        }

        if (getNssRequerant().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    public boolean isPassageHera() {
        return isPassageHera;
    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return true or false
     */
    public boolean isPreparationDecisionValide() {

        try {

            // Recherche des dates dt,dj,dpmt,ddeb
            REDemandeRente dem = new REDemandeRente();
            dem.setSession(getSession());
            dem.setIdDemandeRente(getIdDemandeRente());
            dem.retrieve();
            PRAssert.notIsNew(dem, "Entity not found");

            JACalendar cal = new JACalendarGregorian();
            JADate datePmtMensuel = null;

            if (!JadeStringUtil.isBlankOrZero(REPmtMensuel.getDateDernierPmt(getSession()))) {
                datePmtMensuel = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(REPmtMensuel
                        .getDateDernierPmt(getSession())));
            }

            JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateDebut()));
            JADate dateTraitement = new JADate(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dem.getDateTraitement()));
            JADate dateJour = JACalendar.today();
            dateJour.setDay(1);

            // Si (dt=dj et dt=dpmt) ou (dt<dj et dt<dpmt et ddeb > dpmt) la
            // préparation de la décision peut s'effectuer
            if (datePmtMensuel != null) {
                return (((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_FIRSTLOWER)
                        && (cal.compare(dateTraitement, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER) && (cal
                            .compare(dateDebutDroit, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER)) ||

                ((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_EQUALS) && (cal.compare(dateTraitement,
                        datePmtMensuel) == JACalendar.COMPARE_EQUALS)));
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public boolean isTrouveDansCI() {
        return trouveDansCI;
    }

    public boolean isTrouveDansTiers() {
        return trouveDansTiers;
    }

    private void loadInfoCompl() {
        if ((infoCompl == null) && !"0".equals(idInfoComplementaire)) {
            infoCompl = new PRInfoCompl();
            infoCompl.setSession(getSession());
            infoCompl.setIdInfoCompl(idInfoComplementaire);
            try {
                infoCompl.retrieve();
            } catch (Exception e) {
                infoCompl = null;
            }
        }
    }

    public void setCodeOfficeAiApi(String string) {
        codeOfficeAiApi = string;
    }

    public void setCodeOfficeAiInv(String string) {
        codeOfficeAiInv = string;
    }

    public void setCsAnneeAnticipationVie(String string) {
        csAnneeAnticipationVie = string;
    }

    public void setCsAtteinteApi(String string) {
        csAtteinteApi = string;
    }

    public void setCsAtteinteInv(String string) {
        csAtteinteInv = string;
    }

    @Override
    public void setCsCantonDomicile(String string) {

    }

    public void setCsCantonRequerant(String string) {
        csCantonRequerant = string;
    }

    public void setCsDegreImpotence(String string) {
        csDegreImpotence = string;
    }

    public void setCsEtat(String string) {
        csEtat = string;
    }

    @Override
    public void setCsEtatCivil(String s) {
    }

    public void setCsGenreDroitApi(String string) {
        csGenreDroitApi = string;
    }

    public void setCsGenrePrononceAiApi(String string) {
        csGenrePrononceAiApi = string;
    }

    public void setCsGenrePrononceAiInv(String string) {
        csGenrePrononceAiInv = string;
    }

    public void setCsInfirmiteApi(String string) {
        csInfirmiteApi = string;
    }

    public void setCsInfirmiteInv(String string) {
        csInfirmiteInv = string;
    }

    @Override
    public void setCsNationalite(String string) {

    }

    public void setCsNationaliteRequerant(String string) {
        csNationaliteRequerant = string;
    }

    @Override
    public void setCsSexe(String string) {

    }

    public void setCsSexeRequerant(String string) {
        csSexeRequerant = string;
    }

    public void setCsTypeCalcul(String string) {
        csTypeCalcul = string;
    }

    public void setCsTypeDemandeRente(String string) {
        csTypeDemandeRente = string;
    }

    public void setDateDebut(String string) {
        dateDebut = string;
    }

    public void setDateDebutImpotence(String string) {
        dateDebutImpotence = string;
    }

    public void setDateDebutInvalidite(String string) {
        dateDebutInvalidite = string;
    }

    public void setDateDebutRedNonCollaboration(String dateDebutRedNonCollaboration) {
        this.dateDebutRedNonCollaboration = dateDebutRedNonCollaboration;
    }

    @Override
    public void setDateDeces(String string) {

    }

    public void setDateDecesRequerant(String string) {
        dateDecesRequerant = string;
    }

    public void setDateDepot(String string) {
        dateDepot = string;
    }

    public void setDateFin(String string) {
        dateFin = string;
    }

    public void setDateFinImpotence(String string) {
        dateFinImpotence = string;
    }

    public void setDateFinInvalidite(String string) {
        dateFinInvalidite = string;
    }

    public void setDateFinRedNonCollaboration(String dateFinRedNonCollaboration) {
        this.dateFinRedNonCollaboration = dateFinRedNonCollaboration;
    }

    @Override
    public void setDateNaissance(String string) {

    }

    public void setDateNaissanceRequerant(String string) {
        dateNaissanceRequerant = string;
    }

    public void setDateReception(String string) {
        dateReception = string;
    }

    public void setDateRevocationRequerantVie(String string) {
        dateRevocationRequerantVie = string;
    }

    public void setDateSurvenanceEvenementAssureAPI(String dateSurvenanceEvenementAssureAPI) {
        this.dateSurvenanceEvenementAssureAPI = dateSurvenanceEvenementAssureAPI;
    }

    public void setDateSurvenanceEvenementAssureInv(String dateSurvenanceEvenementAssureInv) {
        this.dateSurvenanceEvenementAssureInv = dateSurvenanceEvenementAssureInv;
    }

    public void setDateTraitement(String string) {
        dateTraitement = string;
    }

    public void setDegreInvalidite(String string) {
        degreInvalidite = string;
    }

    @Override
    public void setIdAssure(String string) {

    }

    public void setIdDemandePrestation(String string) {
        idDemandePrestation = string;
    }

    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    public void setIdDemandeRenteParent(String string) {
        idDemandeRenteParent = string;
    }

    public void setIdGestionnaire(String string) {
        idGestionnaire = string;
    }

    public void setIdInfoComplementaire(String string) {
        idInfoComplementaire = string;
    }

    public void setIdPeriodeAPI(String string) {
        idPeriodeAPI = string;
    }

    public void setIdPeriodeInvalidite(String string) {
        idPeriodeInvalidite = string;
    }

    public void setIdProvisoirePeriodeAPI(Long i) {
        idProvisoirePeriodeAPI = i;
    }

    public void setIdProvisoirePeriodeINV(Long i) {
        idProvisoirePeriodeINV = i;
    }

    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }

    public void setIdRequerant(String string) {
        idRequerant = string;
    }

    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setInfoComplCsTiersResponsable(String infoComplCsTiersResponsable) {
        this.infoComplCsTiersResponsable = infoComplCsTiersResponsable;
    }

    public void setIsAccuseDeReception(Boolean isAccuseDeReception) {
        this.isAccuseDeReception = isAccuseDeReception;
    }

    public void setIsAjournementRequerantVie(Boolean boolean1) {
        isAjournementRequerantVie = boolean1;
    }

    public void setIsAssistancePratique(Boolean boolean1) {
        isAssistancePratique = boolean1;
    }

    public void setIsCalculerCopie(Boolean isCalculerCopie) {
        this.isCalculerCopie = isCalculerCopie;
    }

    public void setIsCelibataireSansEnfantsInv(Boolean isCelibataireSansEnfantsInv) {
        this.isCelibataireSansEnfantsInv = isCelibataireSansEnfantsInv;
    }

    public void setIsCelibataireSansEnfantsVieillesse(Boolean isCelibataireSansEnfantsVieillesse) {
        this.isCelibataireSansEnfantsVieillesse = isCelibataireSansEnfantsVieillesse;
    }

    public void setIsResidenceDansHome(Boolean boolean1) {
        isResidenceDansHome = boolean1;
    }

    public void setModifie(boolean modifie) {
        this.modifie = modifie;
    }

    public void setNbPagesMotivationApi(String nbPagesMotivationApi) {
        this.nbPagesMotivationApi = nbPagesMotivationApi;
    }

    public void setNbPagesMotivationInv(String nbPagesMotivationInv) {
        this.nbPagesMotivationInv = nbPagesMotivationInv;
    }

    @Override
    public void setNom(String string) {

    }

    public void setNomOfficeAiApi(String nomOfficeAiApi) {
        this.nomOfficeAiApi = nomOfficeAiApi;
    }

    public void setNomOfficeAiInv(String nomOfficeAiInv) {
        this.nomOfficeAiInv = nomOfficeAiInv;
    }

    public void setNomRequerant(String string) {
        nomRequerant = string;
    }

    @Override
    public void setNss(String string) {

    }

    public void setNssRequerant(String string) {
        nssRequerant = string;
    }

    public void setPassageHera(boolean isPassageHera) {
        this.isPassageHera = isPassageHera;
    }

    public void setPeriodesAPI(TreeSet<REPeriodeAPIViewBean> set) {
        periodesAPI = set;
    }

    public void setPeriodesInvalidite(TreeSet<REPeriodeInvaliditeViewBean> set) {
        periodesInvalidite = set;
    }

    public void setPourcentageReductionInv(String string) {
        pourcentageReductionInv = string;
    }

    public void setPourcentageReductionSur(String string) {
        pourcentageReductionSur = string;
    }

    public void setPourcentRedFauteGrave(String pourcentRedFauteGrave) {
        this.pourcentRedFauteGrave = pourcentRedFauteGrave;
    }

    public void setPourcentRedNonCollaboration(String pourcentRedNonCollaboration) {
        this.pourcentRedNonCollaboration = pourcentRedNonCollaboration;
    }

    @Override
    public void setPrenom(String string) {

    }

    public void setPrenomRequerant(String string) {
        prenomRequerant = string;
    }

    @Override
    public void setProvenance(String string) {
        if (PRUtil.PROVENANCE_TIERS.equals(string)) {
            trouveDansCI = false;
            trouveDansTiers = true;
        } else if (PRUtil.PROVENANCE_CI.equals(string)) {
            trouveDansCI = true;
            trouveDansTiers = false;
        } else {
            trouveDansCI = false;
            trouveDansTiers = false;
        }
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSpy(BSpy spy) {
        this.spy = spy;
    }

    public void setTrouveDansCI(boolean b) {
        trouveDansCI = b;
    }

    public void setTrouveDansTiers(boolean b) {
        trouveDansTiers = b;
    }

    @Override
    public boolean validate() {
        return false;
    }
}

package globaz.corvus.helpers.mutation;

import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.corvus.process.communicationMutation.RECommunicationMutationParameterValidationError;
import globaz.corvus.process.communicationMutation.RECommunicationMutationProcess;
import globaz.corvus.vb.mutation.RECommunicationMutationOaiViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.ITIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.tiers.TIPersonne;
import globaz.pyxis.db.tiers.TITiersAdresseManager;
import globaz.pyxis.util.TIAdresseResolver;
import java.util.Collection;
import java.util.List;

public class RECommunicationMutationOaiHelper extends FWHelper {

    public static final String CS_ADMIN_GENRE_OFFICE_AI = "509004";
    public static final String CS_DOMAINE_RENTES = "62300003";

    @Override
    public void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        if (!(viewBean instanceof RECommunicationMutationOaiViewBean)) {
            throw new ClassCastException(
                    "RECommunicationMutationOaiHelper:_retrieve() : Is not instance of RECommunicationMutationOaiViewBean");
        }

        RECommunicationMutationOaiViewBean vb = (RECommunicationMutationOaiViewBean) viewBean;

        // Récupération de la demande
        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession((BSession) session);
        demandeRente.setIdDemandeRente(vb.getIdDemande());
        demandeRente.retrieve();

        if (demandeRente != null) {
            setInfoTiersAssure((BSession) session, demandeRente, (RECommunicationMutationOaiViewBean) viewBean);
            setAdresseEtLangueOfficeAi((BSession) session, demandeRente, vb);
        }
    }

    @Override
    protected void _start(FWViewBeanInterface vb, FWAction action, BISession iSession) {

        // Préparation des paramètres du process
        BSession session = (BSession) iSession;
        RECommunicationMutationOaiViewBean viewBean = (RECommunicationMutationOaiViewBean) vb;

        // Création du process
        RECommunicationMutationProcess process = new RECommunicationMutationProcess();
        process.setSession(session);
        process.setEmailAdresse(session.getUserEMail());
        process.setCodeIsoLangueOfficeAI(PRUtil.getISOLangueTiers(viewBean.getLangueOfficeAI()));
        process.setSendToGed(viewBean.getSendToGed());
        process.setAdresseOfficeAi(viewBean.getAdresseOfficeAi().toString());
        process.setAdresseActuelleTiers(viewBean.getAdresseActuelleTiers());
        process.setAdresseAncienneTiers(viewBean.getAdresseAncienneTiers());
        process.setDateDecesTiers(viewBean.getDateDecesAssure());
        process.setIsNouvelleAdresseTiers(viewBean.getIsNouvelleAdresseAssure());
        process.setIsNouvelleAdresseAutre(viewBean.getIsNouvelleAdresseRepresentantAutorite());
        process.setDateDebutHospitalisation(viewBean.getDateDebutHospitalisation());
        process.setDateFinHospitalisation(viewBean.getDateFinHospitalisation());
        process.setDateDebutHome(viewBean.getDateDebutEntreeHome());
        process.setDateFinHome(viewBean.getDateFinEntreeHome());
        process.setTexteObservation(viewBean.getTexteObservation());
        process.setListeAnnexes(viewBean.getListeAnnexe());
        process.setIdTiers(viewBean.getIdTiers());
        process.setNssTiers(viewBean.getNssTiers());
        process.setNomTiers(viewBean.getNomTiers());
        process.setPrenomTiers(viewBean.getPrenomTiers());
        process.setChangementNom(viewBean.getChangementNom());
        process.setChangementPrenom(viewBean.getChangementPrenom());
        process.setChangementNSS(viewBean.getChangementNSS());
        process.setChangementAutre(viewBean.getChangementAutre());
        process.setInputChangementAutre(viewBean.getInputChangementAutre());

        // Validation des paramètres du process
        List<RECommunicationMutationParameterValidationError> errors = process.validate();
        if (errors != null && errors.size() > 0) {
            StringBuilder message = new StringBuilder();
            for (RECommunicationMutationParameterValidationError error : errors) {
                message.append(" - ");
                message.append(session.getLabel(error.getLabelKey()));
            }
            throw new IllegalArgumentException(message.toString());
        }

        // Exécution du process
        try {
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw new IllegalArgumentException(e.toString(), e);
        }

    }

    /**
     * Methode pour retrouver les informations sur le tiers
     * 
     * @param session
     * @param demandeRente
     * @param vb
     * @throws Exception
     */
    private void setInfoTiersAssure(BSession session, REDemandeRente demandeRente, RECommunicationMutationOaiViewBean vb)
            throws Exception {

        // Recherche de la première demande
        PRDemande prDemande = new PRDemande();
        prDemande.setSession(session);
        prDemande.setIdDemande(demandeRente.getIdDemandePrestation());
        prDemande.retrieve();

        PRTiersWrapper tiersRequerant = PRTiersHelper.getTiersParId(session, prDemande.getIdTiers());
        vb.setIdTiers(prDemande.getIdTiers());
        vb.setNomTiers(tiersRequerant.getNom());
        vb.setPrenomTiers(tiersRequerant.getPrenom());

        findNssTiers(session, prDemande, vb);
        findDateDecesTiers(session, prDemande, vb);
        renseigneAdresseTiersAncienneEtActuelle(session, prDemande.getIdTiers(), vb);

    }

    private void renseigneAdresseTiersAncienneEtActuelle(BSession session, String idTiers,
            RECommunicationMutationOaiViewBean vb) throws Exception {

        try {

            ITIAdresseFormater formater = new TIAdresseFormater();
            String csLangue = "";

            TIAbstractAdresseData adresseDataActuelle = findAdresseTiersForDate(JACalendar.todayJJsMMsAAAA(), session,
                    idTiers);
            if (adresseDataActuelle != null) {

                TIAdresseDataSource dataSourceNouvelleAdresse = new TIAdresseDataSource();
                dataSourceNouvelleAdresse.setLangue(csLangue);
                dataSourceNouvelleAdresse.load(adresseDataActuelle, "");
                vb.setAdresseActuelleTiers(formater.format(dataSourceNouvelleAdresse));

                try {

                    String dateDebutAdresseActuelle = adresseDataActuelle.getDateDebutRelation();
                    if (!JadeStringUtil.isBlankOrZero(dateDebutAdresseActuelle)) {
                        String dateFinAncienneAdresse = new JACalendarGregorian().addDays(dateDebutAdresseActuelle, -1);

                        TIAbstractAdresseData adresseDataAncienne = findAdresseTiersForDate(dateFinAncienneAdresse,
                                session, idTiers);
                        if (adresseDataAncienne != null) {

                            TIAdresseDataSource dataSourceAncienneAdresse = new TIAdresseDataSource();
                            dataSourceAncienneAdresse.setLangue(csLangue);
                            dataSourceAncienneAdresse.load(adresseDataAncienne, "");
                            vb.setAdresseAncienneTiers(formater.format(dataSourceAncienneAdresse));
                        }

                    }

                } catch (Exception e2) {
                    vb.setAdresseAncienneTiers("");
                }

            }

        } catch (Exception e) {
            vb.setAdresseActuelleTiers("");
        }

    }

    private TIAbstractAdresseData findAdresseTiersForDate(String dateJJsMMsAAAA, BSession session, String idTiers)
            throws Exception {

        String csDomaine = IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;
        String csType = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        String idExterne = null;

        TIAdresseDataManager mgr = new TITiersAdresseManager();
        mgr.setSession(session);
        mgr.setForIdTiers(idTiers);
        mgr.setForDateEntreDebutEtFin(dateJJsMMsAAAA);
        mgr.changeManagerSize(0);
        mgr.find();

        Collection<BIEntity> col = null;
        // On recherche la bonne adresse parmis toutes celles possibles.
        col = TIAdresseResolver.resolveForOneTiers(mgr, csType, csDomaine, idExterne);

        /*
         * Formatage de l'adresse
         */
        if (col.size() == 1) {
            TIAbstractAdresseData adresseData = (TIAbstractAdresseData) col.iterator().next();
            return adresseData;
        }
        return null;

    }

    /**
     * Methode pour retrouver la date de décès de la personne
     * 
     * @param session
     * @param prDemande
     * @param vb
     * @throws Exception
     */
    private void findDateDecesTiers(BSession session, PRDemande prDemande, RECommunicationMutationOaiViewBean vb)
            throws Exception {

        TIPersonne personne = new TIPersonne();
        personne.setSession(session);
        personne.setIdTiers(prDemande.getIdTiers());
        personne.retrieve();

        if (personne != null && !JadeStringUtil.isBlank(personne.getDateDeces())) {
            vb.setDateDecesAssure(personne.getDateDeces());
        } else {

            PRTiersWrapper tiersW = PRTiersHelper.getTiersParId(session, prDemande.getIdTiers());

            if (tiersW != null && JadeStringUtil.isBlank(tiersW.getDateDeces())) {
                vb.setDateDecesAssure(tiersW.getDateDeces());
            } else {
                tiersW = PRTiersHelper.getTiers(session, vb.getNssTiers());

                if (tiersW != null && JadeStringUtil.isBlank(tiersW.getDateDeces())) {
                    vb.setDateDecesAssure(tiersW.getDateDeces());

                } else {
                    if (!JadeStringUtil.isBlank(tiersW.getProperty("DATE_DECES"))) {
                        vb.setDateDecesAssure(tiersW.getProperty("DATE_DECES"));
                    }
                }
            }
        }
    }

    /**
     * Methode pour retrouver l'adresse de l'office AI
     * 
     * @param session
     * @param demandeRente
     * @param vb
     * @throws Exception
     */
    private String setAdresseEtLangueOfficeAi(BSession session, REDemandeRente demandeRente,
            RECommunicationMutationOaiViewBean viewBean) throws Exception {

        String codeOfficeAI = retrieveCodeOfficeAI(session, demandeRente);

        if (!JadeStringUtil.isBlankOrZero(codeOfficeAI)) {

            PRTiersWrapper[] tabTiers = PRTiersHelper.getAdministrationActiveForGenreAndCode(session,
                    CS_ADMIN_GENRE_OFFICE_AI, codeOfficeAI);

            if (tabTiers.length > 0) {

                String adresseCourrierFormattee = PRTiersHelper.getAdresseGeneriqueFormatee(session,
                        tabTiers[0].getIdTiers(), "", "", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);

                viewBean.setAdresseOfficeAi(new StringBuffer(adresseCourrierFormattee));
                viewBean.setLangueOfficeAI(tabTiers[0].getLangue());
            }
        }

        return null;
    }

    /**
     * Recherche le code de l'office AI correspondant à la demande de rente
     * 
     * @param session
     * @param demandeRente
     * @return
     * @throws Exception
     */
    private String retrieveCodeOfficeAI(BSession session, REDemandeRente demandeRente) throws Exception {

        String codeOfficeAI = "";

        if (globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demandeRente
                .getCsTypeDemandeRente())) {

            REDemandeRenteInvalidite renteInvalidite = new REDemandeRenteInvalidite();
            renteInvalidite.setSession(session);
            renteInvalidite.setIdDemandeRente(demandeRente.getIdDemandeRente());
            renteInvalidite.retrieve();

            codeOfficeAI = renteInvalidite.getCodeOfficeAI();

        } else if (globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(demandeRente
                .getCsTypeDemandeRente())) {
            REDemandeRenteAPI renteAPI = new REDemandeRenteAPI();
            renteAPI.setSession(session);
            renteAPI.setIdDemandeRente(demandeRente.getIdDemandeRente());
            renteAPI.retrieve();

            codeOfficeAI = renteAPI.getCodeOfficeAI();
        }

        return codeOfficeAI;
    }

    private void findNssTiers(BSession session, PRDemande prDemande, RECommunicationMutationOaiViewBean vb)
            throws Exception {

        PRTiersWrapper tiersW = PRTiersHelper.getTiersParId(session, prDemande.getIdTiers());
        if (tiersW != null) {
            vb.setNssTiers(tiersW.getNSS());

        } else {
            REDemandeRenteJointPrestationAccordee demandeRente = new REDemandeRenteJointPrestationAccordee();
            demandeRente.setSession(session);
            demandeRente.setIdDemandeRente(prDemande.getIdDemande());
            demandeRente.retrieve();

            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session,
                    demandeRente.getIdTiersBeneficiairePrestationAccordee());

            if (tiers != null) {
                vb.setNssTiers(tiers.getNSS());
            }
        }
    }

}
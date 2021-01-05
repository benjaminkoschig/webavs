package ch.globaz.naos.businessimpl.service;

import ch.globaz.naos.business.model.*;
import ch.globaz.naos.exception.MajorationFraisAdminException;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.application.TIApplication;

import java.util.*;

import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.naos.business.service.AffiliationService;
import ch.globaz.naos.exception.NaosException;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Service pour le domaine Affiliation
 */
public class AffiliationServiceImpl implements AffiliationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AffiliationServiceImpl.class);

    public void addOrActivateCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation, String anneeDeclSalaire) throws MajorationFraisAdminException, JAException {
        CotisationSimpleModel cotisationAssuranceFraisAdmin = getCotisationAssuranceFraisAdmin(transaction.getSession(), afAffiliation.getAffilieNumero(), anneeDeclSalaire);
        CotisationSimpleModel cotisationAssuranceMajoration = getCotisationAssuranceMajoration(transaction.getSession(), afAffiliation.getAffilieNumero());

        if (cotisationAssuranceMajoration != null) {
            activateCotisationAssuranceMajoration(transaction, afAffiliation, cotisationAssuranceFraisAdmin, cotisationAssuranceMajoration, anneeDeclSalaire);
        } else if (cotisationAssuranceFraisAdmin != null) {
            addCotisationAssuranceMajoration(transaction, afAffiliation, cotisationAssuranceFraisAdmin, anneeDeclSalaire);
        }
    }

    public void deactivateCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation) throws MajorationFraisAdminException {
        CotisationSimpleModel cotisationAssuranceMajoration = getCotisationAssuranceMajoration(transaction.getSession(), afAffiliation.getAffilieNumero());
        if (cotisationAssuranceMajoration != null) {
            deactivateCotisationAssuranceMajoration(transaction, cotisationAssuranceMajoration);
        }
    }

    /**
     * Désactive la cotisation à l'assurance de majoration des frais d'admin ur l'affiliation
     *
     * @param transaction la transaction
     * @param cotisationAssuranceMajoration la cotisation à l'assurance de majoration des frais d'admin
     */
    private void deactivateCotisationAssuranceMajoration(BTransaction transaction, CotisationSimpleModel cotisationAssuranceMajoration) throws MajorationFraisAdminException {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(transaction.getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            cotisationAssuranceMajoration.setDateFin(cotisationAssuranceMajoration.getDateDebut());
            cotisationAssuranceMajoration.setMotifFin(CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE);
            JadePersistenceManager.update(cotisationAssuranceMajoration);
        } catch (Exception e) {
            String message = "Erreur durant la désactivation de la cotisation à l'assurance de majoration des frais d'admin.";
            LOGGER.error(message, e);
            throw new MajorationFraisAdminException(message, e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Active la cotisation à l'assurance de majoration des frais d'admin sur l'affiliation
     *
     * @param transaction la transaction
     * @param cotisationAssuranceFraisAdmin la cotisation à l'assurance de frais d'admin
     * @param cotisationAssuranceMajoration la cotisation à l'assurance de majoration des frais d'admin
     */
    private void activateCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation, CotisationSimpleModel cotisationAssuranceFraisAdmin, CotisationSimpleModel cotisationAssuranceMajoration, String anneeDeclSalaire) throws MajorationFraisAdminException {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(transaction.getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // Update de la cotisation de majoration des frais d'admin avec les valeures de la cotisations au frais d'admin
            String dateDebut = (anneeDeclSalaire != null
                    ? "01.01." + anneeDeclSalaire
                    : cotisationAssuranceFraisAdmin.getDateDebut());

            if (JadeDateUtil.isDateBefore(afAffiliation.getDateDebut(), dateDebut)) {
                cotisationAssuranceMajoration.setDateDebut(dateDebut);
            } else {
                cotisationAssuranceMajoration.setDateDebut(afAffiliation.getDateDebut());
            }

            cotisationAssuranceMajoration.setDateFin(cotisationAssuranceFraisAdmin.getDateFin());
            // Ne reprends pas le motif de fin exception autrement il faudrait créer une deuxième cotisation utilisé comme référence pour l'exception
            cotisationAssuranceMajoration.setMotifFin(cotisationAssuranceFraisAdmin.getMotifFin().equals(CodeSystem.MOTIF_FIN_EXCEPTION)
                    ? CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE
                    : cotisationAssuranceFraisAdmin.getMotifFin());
            cotisationAssuranceMajoration.setPeriodicite(cotisationAssuranceFraisAdmin.getPeriodicite());
            cotisationAssuranceMajoration.setPlanAffiliationId(cotisationAssuranceFraisAdmin.getPlanAffiliationId());
            cotisationAssuranceMajoration.setPlanCaisseId(cotisationAssuranceFraisAdmin.getPlanCaisseId());
            cotisationAssuranceMajoration.setAnneeDecision(cotisationAssuranceFraisAdmin.getAnneeDecision());
            cotisationAssuranceMajoration.setMasseAnnuelle(cotisationAssuranceFraisAdmin.getMasseAnnuelle());
            //cotisationAssuranceMajoration.setMiseAjourDepuisEcran(Boolean.FALSE);
            cotisationAssuranceMajoration.setMaisonMere(String.valueOf(cotisationAssuranceFraisAdmin.getMaisonMere()));
            cotisationAssuranceMajoration.setMontantTrimestriel(cotisationAssuranceFraisAdmin.getMontantTrimestriel());
            cotisationAssuranceMajoration.setMontantMensuel(cotisationAssuranceFraisAdmin.getMontantMensuel());
            cotisationAssuranceMajoration.setMontantAnnuel(cotisationAssuranceFraisAdmin.getMontantAnnuel());
            //cotisationAssuranceMajoration.setAffiliationId(afAffiliation.getAffiliationId());

            // Update de la cotisation
            JadePersistenceManager.update(cotisationAssuranceMajoration);

        } catch (Exception e) {
            String message = "Error durant l'activation de la cotisation à l'assurance de majoration des frais d'admin.";
            LOGGER.error(message, e);
            throw new MajorationFraisAdminException(message, e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Ajoute la cotisation à l'assurance de majoration des frais d'admin sur l'affiliation
     *
     * @param transaction la transaction
     * @param afAffiliation l'affiliation
     * @param cotisationAssuranceFraisAdmin la cotisation a l'assurance de frais d'admin
     */
    private void addCotisationAssuranceMajoration(BTransaction transaction, AFAffiliation afAffiliation, CotisationSimpleModel cotisationAssuranceFraisAdmin, String anneeDeclSalaire) throws MajorationFraisAdminException {
        try {
            // Création d'une nouvelle cotisation de majoration des frais d'admin avec les valeures de la cotisation aux frais d'admin
            AFCotisation cotisationAssuranceMajoration = new AFCotisation();
            cotisationAssuranceMajoration.setSession(transaction.getSession());

            String dateDebut = (anneeDeclSalaire != null
                    ? "01.01." + anneeDeclSalaire
                    : cotisationAssuranceFraisAdmin.getDateDebut());

            if (JadeDateUtil.isDateBefore(afAffiliation.getDateDebut(), dateDebut)) {
                cotisationAssuranceMajoration.setDateDebut(dateDebut);
            } else {
                cotisationAssuranceMajoration.setDateDebut(afAffiliation.getDateDebut());
            }

            cotisationAssuranceMajoration.setDateFin(cotisationAssuranceFraisAdmin.getDateFin());
            // Ne reprends pas le motif de fin exception autrement il faudrait créer une deuxième cotisation utilisé comme référence pour l'exception
            cotisationAssuranceMajoration.setMotifFin(cotisationAssuranceFraisAdmin.getMotifFin().equals(CodeSystem.MOTIF_FIN_EXCEPTION)
                    ? CodeSystem.MOTIF_FIN_FIN_COUV_ASSURANCE
                    : cotisationAssuranceFraisAdmin.getMotifFin());
            cotisationAssuranceMajoration.setPeriodicite(cotisationAssuranceFraisAdmin.getPeriodicite());
            cotisationAssuranceMajoration.setPlanAffiliationId(cotisationAssuranceFraisAdmin.getPlanAffiliationId());
            cotisationAssuranceMajoration.setPlanCaisseId(cotisationAssuranceFraisAdmin.getPlanCaisseId());
            cotisationAssuranceMajoration.setAnneeDecision(cotisationAssuranceFraisAdmin.getAnneeDecision());
            cotisationAssuranceMajoration.setMasseAnnuelle(cotisationAssuranceFraisAdmin.getMasseAnnuelle());
            cotisationAssuranceMajoration.setMiseAjourDepuisEcran(Boolean.FALSE);
            cotisationAssuranceMajoration.setMaisonMere(Boolean.valueOf(cotisationAssuranceFraisAdmin.getMaisonMere()));
            cotisationAssuranceMajoration.setMontantTrimestriel(cotisationAssuranceFraisAdmin.getMontantTrimestriel());
            cotisationAssuranceMajoration.setMontantMensuel(cotisationAssuranceFraisAdmin.getMontantMensuel());
            cotisationAssuranceMajoration.setMontantAnnuel(cotisationAssuranceFraisAdmin.getMontantAnnuel());
            cotisationAssuranceMajoration.setAffiliationId(afAffiliation.getAffiliationId());

            //Recherche l'assurance de majoration des frais d'admin
            AFAssurance afAssuranceFraisAdminMajoration = getAssuranceFraisAdminMajoration(transaction);

            //Set l'id de l'assurance de frais de majoration dans la cotisation
            cotisationAssuranceMajoration.setAssuranceId(afAssuranceFraisAdminMajoration.getAssuranceId());

            //Sauvegarde la nouvelle cotisation
            cotisationAssuranceMajoration.save(transaction);

        } catch (Exception e) {
            String message = "Error durant la création de la cotisation à l'assurance de majoration des frais d'admin.";
            LOGGER.error(message, e);
            throw new MajorationFraisAdminException(message, e);
        }
    }

    /**
     * Permet de retourner l'assurance de majoration des frais d'admin
     * en fonction du genre de l'assurance de frais d'admin
     *
     * @param transaction la transaction
     * @return L'assurance de type majoration des frais d'administration
     */
    private AFAssurance getAssuranceFraisAdminMajoration(BTransaction transaction) throws Exception {
        AFAssuranceManager assMng = new AFAssuranceManager();
        AFAssurance afAssuranceFraisAdminMajoration = null;
        assMng.setSession(transaction.getSession());
        assMng.setForTypeAssurance(CodeSystem.TYPE_ASS_FRAIS_ADMIN_MAJ);
        assMng.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        assMng.find();
        if (assMng.size() > 0) {
            afAssuranceFraisAdminMajoration = (AFAssurance) assMng.getFirstEntity();
        } else {
            LOGGER.warn("Impossible de trouver l'assurance de majoration des frais d'admin.");
        }

        return afAssuranceFraisAdminMajoration;
    }

    /**
     * Permet de retourner l'assurance de frais d'amin
     * en fonction de l'id d'assurance
     *
     * @param transaction la transaction
     * @param assuranceId l'id de l'assurance à rechercher
     * @return L'assurance de frais d'administration
     */
    private AFAssurance getAssuranceFraisAdmin(BTransaction transaction, String assuranceId) throws Exception {
        AFAssuranceManager assMng = new AFAssuranceManager();
        AFAssurance afAssuranceFraisAdmin = null;
        assMng.setSession(transaction.getSession());
        assMng.setForIdAssurance(assuranceId);
        assMng.setForTypeAssurance(CodeSystem.TYPE_ASS_FRAIS_ADMIN);
        assMng.find();
        if (assMng.size() > 0) {
            afAssuranceFraisAdmin = (AFAssurance) assMng.getFirstEntity();
        } else {
            LOGGER.warn("Impossible de trouver une assurance de frais d'admin");
        }

        return afAssuranceFraisAdmin;
    }

    public HashSet<String> getIdsAssurancesAffiliation(BSession session, String forNumeroAffilie) {
        AffiliationAssuranceSearchComplexModel affiliationAssuranceSearchComplexModel = new AffiliationAssuranceSearchComplexModel();
        affiliationAssuranceSearchComplexModel.setForNumeroAffilie(forNumeroAffilie);

        HashSet<String> results = new HashSet<String>();
        HashSet<CotisationSimpleModel> cotisationSimpleModels = searchCotisationS(session, affiliationAssuranceSearchComplexModel);
        for (CotisationSimpleModel cotisationSimpleModel : cotisationSimpleModels) {
            results.add(cotisationSimpleModel.getAssuranceId());
        }

        return results;
    }

    /**
     * Permet de retourner la cotisation associé a l'assurance de type frais de majoration d'un affilié
     *
     * @param session une session
     * @param forNumeroAffilie le numéro de l'affiliation
     * @return Une cotisation a l'assurance de type frais de majoration d'un affilié
     */
    private CotisationSimpleModel getCotisationAssuranceMajoration(BSession session, String forNumeroAffilie) {
        AffiliationAssuranceSearchComplexModel affiliationAssuranceSearchComplexModel = new AffiliationAssuranceSearchComplexModel();
        affiliationAssuranceSearchComplexModel.setForNumeroAffilie(forNumeroAffilie);
        affiliationAssuranceSearchComplexModel.setForTypeAssurance(CodeSystem.TYPE_ASS_FRAIS_ADMIN_MAJ);
        affiliationAssuranceSearchComplexModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        affiliationAssuranceSearchComplexModel.setWhereKey("searchCotisationActiveAndNotActive");

        CotisationSimpleModel cotisation = null;
        HashSet<CotisationSimpleModel> cotisationSimpleModels = searchCotisationS(session, affiliationAssuranceSearchComplexModel);
        Iterator<CotisationSimpleModel> it = cotisationSimpleModels.iterator();
        if (it.hasNext()) {
            cotisation = it.next();
        } else {
            LOGGER.warn("Impossible de trouver une cotisation à l'assurance de majoration des frais d'admin");
        }

        return cotisation;
    }

    /**
     * Permet de retourner la cotisation associé a l'assurance de type frais d'administration d'un affilié
     *
     * @param session une session
     * @param forNumeroAffilie le numéro de l'affiliation
     * @return Une cotisation l'assurance de type frais d'administrationd'un affilié
     */
    private CotisationSimpleModel getCotisationAssuranceFraisAdmin(BSession session, String forNumeroAffilie, String anneeDeclSalaire) throws JAException {
        AffiliationAssuranceSearchComplexModel affiliationAssuranceSearchComplexModel = new AffiliationAssuranceSearchComplexModel();
        affiliationAssuranceSearchComplexModel.setForNumeroAffilie(forNumeroAffilie);
        affiliationAssuranceSearchComplexModel.setForTypeAssurance(CodeSystem.TYPE_ASS_FRAIS_ADMIN);
        affiliationAssuranceSearchComplexModel.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        affiliationAssuranceSearchComplexModel.setForDateCotisation((anneeDeclSalaire != null ? "31.12." + anneeDeclSalaire : JACalendar.todayJJsMMsAAAA()));

        CotisationSimpleModel cotisation = null;
        HashSet<CotisationSimpleModel> cotisationSimpleModels = searchCotisationS(session, affiliationAssuranceSearchComplexModel);
        Iterator<CotisationSimpleModel> it = cotisationSimpleModels.iterator();
        if (it.hasNext()) {
            cotisation = it.next();
        } else {
            LOGGER.warn("Impossible de trouver une cotisation à l'assurance de frais d'admin");
        }

        return cotisation;
    }


    /**
     * Permet de retourner les cotisations en fonction du searchComplexModel fourni en paramètre
     *
     * @param session Une session
     * @param searchComplexModel le search model
     * @return Les cotisations de l'affilié en fonction du searchModel
     */
    private HashSet<CotisationSimpleModel> searchCotisationS(BSession session, AffiliationAssuranceSearchComplexModel searchComplexModel) {
        HashSet<CotisationSimpleModel> cotisations = new HashSet<CotisationSimpleModel>();

        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(session);
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            searchComplexModel = this.searchAffiliationAssurance(searchComplexModel);

            if (searchComplexModel.getSize() <= 0) {
                LOGGER.info("Aucune cotisation n'a été trouvé pour les critères de recherche passé en parametre: " + searchComplexModel.getForNumeroAffilie());
            } else {
                for (JadeAbstractModel element : searchComplexModel.getSearchResults()) {
                    cotisations.add((((AffiliationAssuranceComplexModel) element).getCotisation()));
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error durant la recherche de la cotisation.", e);
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return cotisations;
    }

    private String _getCantonAFTiers(String idTiers, String date) throws JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail adressTiers = new AdresseTiersDetail();
        try {

            ArrayList listOrder = new ArrayList();
            // exploitation
            // TODO: Ajouter constante AdresseService.CS_TYPE_EXPLOITATION dans service pyxis
            listOrder.add(new String("508021"));
            // domicile
            listOrder.add(AdresseService.CS_TYPE_DOMICILE);// 508008
            // courrier
            listOrder.add(new String(AdresseService.CS_TYPE_COURRIER));

            adressTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiersCustomCascade(idTiers, date,
                    ALCSTiers.DOMAINE_AF, listOrder, 2);

            // adressTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers, new Boolean(true),
            // date, ch.globaz.pyxis.business.service.AdresseService.CS_DOMAINE_AF,
            // ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_COURRIER, "");
        } catch (Exception e) {
            throw new NaosException("ERROR OCCURED IN " + this.getClass().getName()
                    + "._getCantonAFTiers : Tiers Adress not found ! : " + e.getMessage());
        }

        return adressTiers.getFields().get("canton_id").toString();

    }

    /**
     * @deprecated remplacé par _getInfosAF
     */
    @Deprecated
    private void _getInfos(AssuranceInfo infoResult, AffiliationAssuranceComplexModel data, String date,
            boolean withCoti) throws JadePersistenceException, JadeApplicationException {

        if (!withCoti) {
            infoResult.setCanton(_getCantonAFTiers(data.getTiersAffiliation().getId(), date));
        }
        if (!data.getAssurance().isNew() && withCoti) {

            infoResult.setCategorieCotisant(data.getParametreAssurance().getValeurNum());
            // canton lié à la cotisation si AF, sinon canton exploitation
            if (AffiliationService.CS_TYPE_COTI_AF.equals(data.getAssurance().getTypeAssurance())) {
                infoResult.setCanton(data.getAssurance().getAssuranceCanton());
            } else {
                infoResult.setCanton(_getCantonAFTiers(data.getTiersAffiliation().getId(), date));
            }
        }

        infoResult.setCodeCaisseProf(data.getCaisseProf().getCodeAdministration());
        // infoResult.setCouvert => déjà défini selon règles
        if (!data.getCotisation().isNew() && withCoti) {
            infoResult.setDateDebutCotisation(data.getCotisation().getDateDebut());
            infoResult.setDateFinCotisation(data.getCotisation().getDateFin());
            infoResult.setIdCotisation(data.getCotisation().getId());
        }
        infoResult.setDateDebutAffiliation(data.getAffiliation().getDateDebut());
        infoResult.setDateFinAffiliation(data.getAffiliation().getDateFin());
        infoResult.setDesignation(data.getAffiliation().getRaisonSociale());
        infoResult.setDesignationAbrege(data.getAffiliation().getRaisonSocialeCourt());
        infoResult.setGenreAffiliation(data.getAffiliation().getTypeAffiliation());
        infoResult.setIdAffiliation(data.getAffiliation().getId());
        infoResult.setIdCotisation(data.getCotisation().getId());
        infoResult.setIdTiersAffiliation(data.getTiersAffiliation().getIdTiers());
        infoResult.setIdTiersCaisseProf(data.getCaisseProf().getIdTiersAdministration());
        infoResult.setLangue(data.getTiersAffiliation().getLangue());
        infoResult.setNumeroAffilie(data.getAffiliation().getAffilieNumero());
        infoResult.setPeriodicitieAffiliation(data.getAffiliation().getPeriodicite());
        infoResult.setPeriodicitieCotisation(data.getCotisation().getPeriodicite());
        infoResult.setTitre(data.getTiersAffiliation().getTitreTiers());

    }

    /*
     * Remplit la structure infoResult avec les valeurs dans data, sauf activité et canton la partie cotisation est
     * utilisé que si de type AF
     */
    private void _getInfosAF(AssuranceInfo infoResult, AffiliationAssuranceComplexModel data)
            throws JadePersistenceException, JadeApplicationException {

        infoResult.setDateDebutAffiliation(data.getAffiliation().getDateDebut());
        infoResult.setDateFinAffiliation(data.getAffiliation().getDateFin());
        infoResult.setPeriodicitieAffiliation(data.getAffiliation().getPeriodicite());

        infoResult.setCodeCaisseProf(data.getCaisseProf().getCodeAdministration());
        infoResult.setIdTiersCaisseProf(data.getCaisseProf().getIdTiersAdministration());

        // info a remplir que si coti AF ? TODO:vérifier si seulement cotiAF
        if (AffiliationService.CS_TYPE_COTI_AF.equals(data.getAssurance().getTypeAssurance())) {
            infoResult.setCategorieCotisant(data.getParametreAssurance().getValeurNum());
            infoResult.setIdCotisation(data.getCotisation().getId());
            infoResult.setDateDebutCotisation(data.getCotisation().getDateDebut());
            infoResult.setDateFinCotisation(data.getCotisation().getDateFin());
            infoResult.setPeriodicitieCotisation(data.getCotisation().getPeriodicite());
        }
        infoResult.setLangue(data.getTiersAffiliation().getLangue());
        infoResult.setNumeroAffilie(data.getAffiliation().getAffilieNumero());
        infoResult.setTitre(data.getTiersAffiliation().getTitreTiers());
        infoResult.setDesignation(data.getAffiliation().getRaisonSociale());
        infoResult.setDesignationAbrege(data.getAffiliation().getRaisonSocialeCourt());
        infoResult.setGenreAffiliation(data.getAffiliation().getTypeAffiliation());
        infoResult.setIdAffiliation(data.getAffiliation().getAffiliationId());
        infoResult.setIdTiersAffiliation(data.getAffiliation().getIdTiers());

        infoResult.setLibelleCourt(data.getAssurance().getAssuranceLibelleCourtFr());
        infoResult.setLibelleLong(data.getAssurance().getAssuranceLibelleFr());
        infoResult.setIdAssurance(data.getAssurance().getAssuranceId());
    }

    @Override
    public AffiliationSearchSimpleModel find(AffiliationSearchSimpleModel searchModel) throws JadePersistenceException,
            JadeApplicationException {
        return (AffiliationSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     *
     * @seech.globaz.naos.business.service.AffiliationService# findAllForNumeroAffilieLike(java.lang.String)
     */
    @Override
    public String[] findAllForNumeroAffilieLike(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException {
        try {
            String numeroAffilieLike = numeroAffilie;
            AffiliationSearchSimpleModel model = new AffiliationSearchSimpleModel();
            model.setForNumeroAffilieLike(numeroAffilieLike);
            JadePersistenceManager.search(model);
            int size = model.getSize();
            String[] tabNumAff = new String[size];
            for (int i = 0; i < size; i++) {
                tabNumAff[i] = ((AffiliationSimpleModel) model.getSearchResults()[i]).getAffilieNumero();
            }
            return tabNumAff;
        } catch (Exception e) {
            // TODO : i14n
            throw new NaosException("Impossible de trouver les numéros d'affiliés començant par " + numeroAffilie, e);
        }
    }

    @Override
    public String findIdTiersForNumeroAffilie(String numeroAffilie) throws JadePersistenceException,
            JadeApplicationException {

        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();

        searchModel.setForNumeroAffilie(numeroAffilie);
        searchModel = find(searchModel);
        AffiliationSimpleModel firstModel = (AffiliationSimpleModel) searchModel.getSearchResults()[0];

        switch (searchModel.getSize()) {
            case 0:
                return null;
            case 1:
                return firstModel.getIdTiers();
            default:
                return firstModel.getIdTiers();
        }
    }

    @Override
    public AffiliationSimpleModel findMaisonMere(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException {
        LienAffiliationSearchComplexModel model = new LienAffiliationSearchComplexModel();
        model.setForChildNumeroAffilie(numeroAffilieFormatte);
        model.setForDateLien(JACalendar.todayJJsMMsAAAA());
        model.setForTypeLien(AffiliationService.CS_TYPE_LIEN_SUCCURSALE);
        try {
            JadePersistenceManager.search(model);
            if (model.getSearchResults().length > 1) {
                // Préventif, ne devrait pas être possible
                // TODO : i14n
                throw new Exception("Plusieurs succursales trouvées pour l'affiliation " + numeroAffilieFormatte);
            } else if (model.getSearchResults().length == 1) {
                LienAffiliationComplexModel m = (LienAffiliationComplexModel) model.getSearchResults()[0];
                return m.getParent();

            } else {
                return null;
            }

        } catch (Exception e) {
            // TODO : i14n
            throw new NaosException("Un problème technique est survenu lors de la recherche de la maison mère", e);
        }
    }

    // public AssuranceInfo getAssuranceInfo(String numeroAffilie, String date,
    // String typeDossier) throws JadePersistenceException,
    // JadeApplicationException {
    // return this.getAssuranceInfo(numeroAffilie, date, typeDossier, false,
    // "0");
    // }

    @Override
    public int findNombreSuccursales(String numeroAffilieFormatte) throws JadePersistenceException,
            JadeApplicationException {
        LienAffiliationSearchComplexModel model = new LienAffiliationSearchComplexModel();
        model.setForParentNumeroAffilie(numeroAffilieFormatte);
        model.setForDateLien(JACalendar.todayJJsMMsAAAA());
        model.setForTypeLien(AffiliationService.CS_TYPE_LIEN_SUCCURSALE);
        try {
            return JadePersistenceManager.count(model);
        } catch (Exception e) {
            // TODO : i14n
            throw new NaosException("Un problème technique est survenu lors de la recherche du nombre de succursales",
                    e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.naos.business.service.AffiliationService#getAssuranceInfo(java .lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public AssuranceInfo getAssuranceInfo(String numeroAffilie, String date, String genreAssurance,
            String typeAssurance, String categorieCotisant) throws JadePersistenceException, JadeApplicationException {

        AssuranceCouverteSearchComplexModel model = new AssuranceCouverteSearchComplexModel();
        model.setForNumeroAffilie(numeroAffilie);
        model.setForDateCotisation(date);
        model.setForGenreAssurance(genreAssurance);
        model.setForTypeAssurance(typeAssurance);
        model.setForValeurNumParamAssurance(categorieCotisant);
        JadePersistenceManager.search(model);

        int nbRecords = model.getSize();
        AssuranceInfo infoResult = new AssuranceInfo();
        infoResult.setNumeroAffilie(numeroAffilie);
        switch (nbRecords) {
            case 0:
                /*
                 * Pas de couverture trouvée pour cette assurance, elle n'est pas couverte
                 */
                infoResult.setCouvert(new Boolean(false));
                return infoResult;
            case 1:
                /*
                 * Assurance couverte (et 1 seul enregistrement trouvé) transfert les informations dans le resultat
                 */
                infoResult.setCouvert(new Boolean(true));
                AssuranceCouverteComplexModel data = (AssuranceCouverteComplexModel) model.getSearchResults()[0];
                infoResult.setLangue(data.getTiersAffiliation().getLangue());
                infoResult.setTitre(data.getTiersAffiliation().getTitreTiers());
                infoResult.setDesignation(data.getAffiliation().getRaisonSociale());
                infoResult.setDesignationAbrege(data.getAffiliation().getRaisonSocialeCourt());
                infoResult.setDateDebutCotisation(data.getCotisation().getDateDebut());
                infoResult.setDateFinCotisation(data.getCotisation().getDateFin());
                infoResult.setCanton(data.getAssurance().getAssuranceCanton());
                infoResult.setIdTiersCaisseProf(data.getCaisseProf().getIdTiersAdministration());
                infoResult.setCodeCaisseProf(data.getCaisseProf().getCodeAdministration());
                infoResult.setPeriodicitieCotisation(data.getCotisation().getPeriodicite());
                infoResult.setPeriodicitieAffiliation(data.getAffiliation().getPeriodicite());
                infoResult.setCategorieCotisant(data.getParametreAssurance().getValeurNum());
                infoResult.setIdAffiliation(data.getAffiliation().getAffiliationId());
                infoResult.setIdTiersAffiliation(data.getAffiliation().getIdTiers());
                return infoResult;

            default:
                /*
                 * Il ne doit pas y avoir plusieurs enregistrement pour les critères passé en paramètres.
                 */
                // TODO : i14n
                throw new NaosException(
                        "Plusieurs enregistrements trouvés, alors que 0 ou 1 enregistrement sont attendu.");
        } // fin switch
    }

    private void handleWarningCotiAfWithoutMasse(AssuranceInfo infoResult,AffiliationAssuranceComplexModel dataWithCotiAF) throws JadeApplicationException, JadePersistenceException {

        // Affiliation vérification le type (maison mère, succursale, normal)
        AffiliationSimpleModel maisonMere = AFBusinessServiceLocator.getAffiliationService()
                .findMaisonMere(dataWithCotiAF.getAffiliation().getAffilieNumero());
        // pas une succursale tester la masse salariale si facturé par accompte,
        // sinon ok
        if ((maisonMere == null) && !dataWithCotiAF.getAffiliation().getReleveParitaire()) {
            if (JadeStringUtil.isIntegerEmpty(dataWithCotiAF.getCotisation().getMasseAnnuelle())) {

                List<String> warnings = infoResult.getWarningsContainer();
                warnings.add("naos.cotisation.af.aucuneMasse");
                infoResult.setWarningsContainer(warnings);
            }
        }

    }

    /**
     *
     * Permet de savoir si une assurance est couverte ou non
     *
     * @param numeroAffilie
     * @param date
     * @param typeDossier
     * @param numeroAffilie
     * @return un objet de type AssuranceInfo qui contient le résultat
     * @throws JadePersistenceException
     *             si une erreur technique lié à la persistence des données survient lors de la recherche
     * @throws JadeApplicationException
     *             si plusieurs enregistrements sont trouvés pour les paramètres donnés.
     */
    @Override
    public AssuranceInfo getAssuranceInfoAF(String numeroAffilie, String date, String typeDossier)
            throws JadePersistenceException, JadeApplicationException {
        // état initial du résultat retourné
        AssuranceInfo infoResult = new AssuranceInfo();
        infoResult.setNumeroAffilie(numeroAffilie);
        infoResult.setCouvert(false);

        AffiliationAssuranceSearchComplexModel searchModel = new AffiliationAssuranceSearchComplexModel();
        searchModel.setForNumeroAffilie(numeroAffilie);
        searchModel.setForDateCotisation(date);
        searchModel.setForDateAffiliation(date);
        JadePersistenceManager.search(searchModel);

        int nbRecordsCotiActive = searchModel.getSize();
        // si on trouve pas de cotisation active, on recherche que pour avoir
        // l'affiliation active même sans coti
        if (nbRecordsCotiActive == 0) {
            searchModel.setWhereKey("ifNoAssurance");
            JadePersistenceManager.search(searchModel);
            nbRecordsCotiActive = searchModel.getSize();
            // si on trouve pas d'affiliation active à ce date on recherche que pour avoir l'affiliation
            if (nbRecordsCotiActive == 0) {
                searchModel.setWhereKey("ifNoAffiliationActive");
                JadePersistenceManager.search(searchModel);
                nbRecordsCotiActive = searchModel.getSize();
                // si toujours rien ici, on retourne le résultat, car rien trouvé de plus
                if (nbRecordsCotiActive == 0) {
                    return infoResult;
                }
            }
        }

        AffiliationSimpleModel affiliationResult = ((AffiliationAssuranceComplexModel) searchModel.getSearchResults()[0])
                .getAffiliation();
        AffiliationAssuranceComplexModel data = (AffiliationAssuranceComplexModel) searchModel.getSearchResults()[0];
        AffiliationAssuranceComplexModel dataWithCotiAF = new AffiliationAssuranceComplexModel();
        AffiliationAssuranceComplexModel dataWithCoti = new AffiliationAssuranceComplexModel();

        int nbCotiAFFound = 0;
        // stocke dans une map les id coti AF pour être sûr d'itérer correctement
        Map<String, AffiliationAssuranceComplexModel> resultsChecked = new HashMap<String, AffiliationAssuranceComplexModel>();

        // on boucle sur les coti active pour savoir si bon type de coti selon typeDossier et avoir les infos
        // de celle déterminée comme active
        for (int ind = 0; ind < nbRecordsCotiActive; ind++) {
            dataWithCoti = (AffiliationAssuranceComplexModel) searchModel.getSearchResults()[ind];

            if ("S".equalsIgnoreCase(typeDossier)) {
                // TODO (lot x) : gérer le cas affiliation genre indépendant et
                // employeur
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PARITAIRE.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    // si c'est une maison mère on pourrait avoir plusieurs coti AF définie (1 par succursale)
                    // on vérifie si on a pas déjà itérer sur cette cotisation (car possible avec la jointure de
                    // AffiliationAssuranceComplexModel)
                    if (!resultsChecked.containsKey(dataWithCoti.getCotisation().getCotisationId())) {
                        nbCotiAFFound++;

                    }
                    dataWithCotiAF = dataWithCoti;
                    resultsChecked.put(dataWithCoti.getCotisation().getCotisationId(), dataWithCoti);

                    // si on tombe sur une exception, c'est celle ci qui fait foi et pas un autre
                    if (CodeSystem.MOTIF_FIN_EXCEPTION.equals(dataWithCoti.getCotisation().getMotifFin())) {
                        nbCotiAFFound = 1;
                        break;
                    }
                }

            }
            if ("N".equalsIgnoreCase(typeDossier) || ("P".equalsIgnoreCase(typeDossier))) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AVS_AI
                                .equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;
                }
                if ("N".equalsIgnoreCase(typeDossier)
                        && AffiliationService.TYPE_AFFILI_BENEF_AF.equals(data.getAffiliation().getTypeAffiliation())) {
                    infoResult.setCouvert(true);
                    break;
                }

            }
            if ("A".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AVS_AI
                                .equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;

                }
            }
            if ("T".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())) {

                    infoResult.setCouvert(true);
                    break;

                } else {
                    if ((dataWithCoti.getAssurance() != null)
                            && AffiliationService.CS_TYPE_COTI_AUTRE.equals(dataWithCoti.getAssurance()
                                    .getTypeAssurance())
                            && AffiliationService.CS_TYPE_PARITAIRE.equals(dataWithCoti.getAssurance()
                                    .getAssuranceGenre())) {

                        infoResult.setCouvert(true);
                    }
                }
            }

            if ("C".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AVS_AI
                                .equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PARITAIRE.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;
                }
            }

            if ("I".equalsIgnoreCase(typeDossier)) {
                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;

                } else {
                    if ((dataWithCoti.getAssurance() != null)
                            && AffiliationService.CS_TYPE_COTI_AVS_AI.equals(dataWithCoti.getAssurance()
                                    .getTypeAssurance())
                            && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance()
                                    .getAssuranceGenre())) {

                        infoResult.setCouvert(true);
                    }
                }
            }

            // si l'affiliation est de type TSE, on vérifie les coti AF sans tenir compte du type d'activité
            if ((AffiliationService.TYPE_AFFILI_TSE.equals(dataWithCoti.getAffiliation().getTypeAffiliation()) || AffiliationService.TYPE_AFFILI_TSE_VOLONTAIRE
                    .equals(dataWithCoti.getAffiliation().getTypeAffiliation()))) {

                if ((dataWithCoti.getAssurance() != null)
                        && AffiliationService.CS_TYPE_COTI_AF.equals(dataWithCoti.getAssurance().getTypeAssurance())
                        && AffiliationService.CS_TYPE_PERSONNEL.equals(dataWithCoti.getAssurance().getAssuranceGenre())) {

                    infoResult.setCouvert(true);
                    break;

                }

            }

        }// fin FOR

        if (nbCotiAFFound == 1) {
            infoResult.setCanton(dataWithCotiAF.getAssurance().getAssuranceCanton());
            _getInfosAF(infoResult, dataWithCotiAF);
            handleWarningCotiAfWithoutMasse(infoResult,dataWithCotiAF);

            // TODO: si coti facturé à la maison mère => tester maison mère et si inactive => warning aucune coti maison
            // mère
            // et tester si maison mère existe

        } else {
            if(ALConstCaisse.CAISSE_CICI.equalsIgnoreCase(ALServiceLocator.getParametersServices().getNomCaisse()) && nbCotiAFFound > 1){
                handleWarningCotiAfWithoutMasse(infoResult,dataWithCotiAF);
            }
            // FIXME: infoResult.setCanton(this._getCantonAFTiers(affiliationResult.getIdTiers(), date));
            // et plus besoin de le faire dans chaque condition en dessous
            if (ALConstCaisse.CAISSE_CCVD.equals(ALServiceLocator.getParametersServices().getNomCaisse())
                    && "A".equals(typeDossier)) {
                infoResult.setCouvert(true);
            }
            if ("S".equals(typeDossier) && (nbCotiAFFound == 0)) {
                List<String> warnings = infoResult.getWarningsContainer();
                warnings.add("naos.cotisation.af.aucuneCotiAF");
                infoResult.setWarningsContainer(warnings);
            }
            if ("S".equals(typeDossier) && (nbCotiAFFound > 1)) {
                // FIXME:améliorer la gestion des cas affiliation ayant plusieurs coti AF active
                if (ALConstCaisse.CAISSE_CCVD.equals(ALServiceLocator.getParametersServices().getNomCaisse())) {
                    List<String> warnings = infoResult.getWarningsContainer();
                    warnings.add("naos.cotisation.af.plusieursCotiAF");
                    infoResult.setWarningsContainer(warnings);
                }
            }
            // si on a un coti AF, on se base sur celle là pour récupérer les infos affiliation
            if (!dataWithCotiAF.isNew()) {
                infoResult.setCanton(_getCantonAFTiers(affiliationResult.getIdTiers(), date));
                _getInfosAF(infoResult, dataWithCotiAF);
            } else {
                // si dataWithCoti est nouveau, c'est qu'on a pas trouvé de coti qu'on cherchait selon type de dossier
                // AF
                // donc on prend data pour avoir les infos affiliation au moins, car on les veut pour afficher les infos
                // et
                // trouver le canton selon tiers affiliation
                if (dataWithCoti.isNew()) {
                    infoResult.setCanton(_getCantonAFTiers(affiliationResult.getIdTiers(), date));
                    _getInfosAF(infoResult, data);
                } else {
                    infoResult.setCanton(_getCantonAFTiers(dataWithCoti.getTiersAffiliation().getId(), date));
                    _getInfosAF(infoResult, dataWithCoti);

                }
            }

        }

        // on met inactif si date passée pas dans période affiliation
        if (JadeDateUtil.isDateAfter(date, affiliationResult.getDateDebut())) {
            if (JadeStringUtil.isBlankOrZero(affiliationResult.getDateFin())
                    || JadeDateUtil.isDateBefore(date, affiliationResult.getDateFin())) {
                // NOTHING
            } else {
                infoResult.setCouvert(false);
            }
        }
        // si pas actif et que cotiAF active, on logge un avertissement et on met actif
        if (!infoResult.getCouvert() && hasCotisationAFSansAdhesion(affiliationResult.getAffiliationId(), date)) {
            List<String> warnings = infoResult.getWarningsContainer();
            warnings.add("naos.cotisation.af.aucuneAdhesion");
            warnings.remove("naos.cotisation.af.aucuneCotiAF");
            infoResult.setWarningsContainer(warnings);
            infoResult.setCouvert(true);
        }

        return infoResult;

    }

    /**
     * retourne si cotiAF active pour la date donnée mais sans adhésion
     *
     * @param idAffiliation
     * @param date
     * @return
     */
    private boolean hasCotisationAFSansAdhesion(String idAffiliation, String date) {

        PlanAffiliationCotisationSearchComplexModel searchModel = new PlanAffiliationCotisationSearchComplexModel();
        searchModel.setForAffiliationId(idAffiliation);
        searchModel.setForTypeAssurance(AffiliationService.CS_TYPE_COTI_AF);
        searchModel.setForDateCotisation(date);
        searchModel.setDefinedSearchSize(0);
        try {
            searchModel = (PlanAffiliationCotisationSearchComplexModel) JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            return false;
        }

        for (int i = 0; i < searchModel.getSize(); i++) {
            PlanAffiliationCotisationComplexModel currentPlanCoti = (PlanAffiliationCotisationComplexModel) searchModel
                    .getSearchResults()[i];
            if (JadeNumericUtil.isEmptyOrZero(currentPlanCoti.getCotisation().getAdhesionId())) {
                return true;
            }

        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.naos.business.service.AffiliationService#isAffiliationExists (java.lang.String)
     */
    @Override
    public Boolean isAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException {

        Boolean result = null;
        AffiliationSearchSimpleModel model = new AffiliationSearchSimpleModel();
        model.setForNumeroAffilie(numeroAffilie);
        int size = JadePersistenceManager.count(model);

        if (size < 0) {
            // préventif, ne devrait jamais arriver !
            // TODO : i14n
            throw new NaosException("isAffiliationExists, nb result < 0 !");
        } else if (size > 1) {
            // préventif, ne devrait jamais arriver !
            // TODO : i14n
            throw new NaosException("(" + size + ") pour le numeroAffilie :" + numeroAffilie);
        } else if (size == 0) {
            result = new Boolean(false);
        } else if (size == 1) {
            result = new Boolean(true);
        }

        if (result == null) {
            // something got really wrong...
            // TODO : i14n
            throw new NaosException("impossible de déterminer le nombre d'affiliation pour le numéro d'affilié :"
                    + numeroAffilie);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.naos.business.service.AffiliationService#isNumeroAffilieValide (java.lang.String)
     */
    @Override
    public Boolean isNumeroAffilieValide(String numeroAffilie) throws JadeApplicationException {

        Boolean result = new Boolean(false);
        IFormatData formater = null;
        try {
            TIApplication app = (TIApplication) GlobazServer.getCurrentSystem().getApplication("PYXIS");
            formater = app.getAffileFormater();
        } catch (Exception e) {
            throw new NaosException(e.getMessage());
        }

        if (formater == null) {
            // Préventif, ne doit jamais arriver
            // TODO : i14n
            throw new NaosException("ERROR OCCURED IN " + this.getClass().getName()
                    + ".isNumeroAffilieValide : formater is null");
        }
        try {
            formater.check(numeroAffilie);
            result = new Boolean(true);
        } catch (Exception e) {
            result = new Boolean(false);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.naos.business.service.AffiliationService#isAffiliationExists (java.lang.String)
     */
    @Override
    public int nombreAffiliationExists(String numeroAffilie) throws JadePersistenceException, JadeApplicationException {

        int result = 0;
        AffiliationSearchSimpleModel model = new AffiliationSearchSimpleModel();
        model.setForNumeroAffilie(numeroAffilie);
        int size = JadePersistenceManager.count(model);

        if (size >= 0) {
            result = size;

        } else if (size < 0) {

            throw new NaosException("impossible de determiner le nombre d'affiliation pour le numéro d'affilié :"
                    + numeroAffilie);
        }

        return result;
    }

    @Override
    public AffiliationSimpleModel read(String idAffiliation) throws JadeApplicationException, JadePersistenceException {
        AffiliationSimpleModel affiliationModel = new AffiliationSimpleModel();
        affiliationModel.setId(idAffiliation);
        return (AffiliationSimpleModel) JadePersistenceManager.read(affiliationModel);

    }

    @Override
    public AffiliationAssuranceSearchComplexModel searchAffiliationAssurance(
            AffiliationAssuranceSearchComplexModel searchModel) throws JadePersistenceException {
        return (AffiliationAssuranceSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AffiliationTiersSearchComplexModel widgetFind(AffiliationTiersSearchComplexModel searchComplexModel)
            throws JadePersistenceException, JadeApplicationException {

        if (!JadeStringUtil.isBlankOrZero(searchComplexModel.getLikeDesignationUpper())) {
            searchComplexModel.setLikeDesignationUpper(searchComplexModel.getLikeDesignationUpper().toUpperCase());
        }
        return (AffiliationTiersSearchComplexModel) JadePersistenceManager.search(searchComplexModel);
    }

}

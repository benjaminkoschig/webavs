package ch.globaz.vulpecula.ws.services;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.api.IAFAssurance;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.lang.Validate;
import ch.globaz.common.sql.QueryUpdateExecutor;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.AffiliationTiersComplexModel;
import ch.globaz.naos.business.model.AffiliationTiersSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.external.models.ContactTiersSimpleModel;
import ch.globaz.vulpecula.external.models.MoyenContactTiersSimpleModel;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.CSTiers;
import ch.globaz.vulpecula.external.models.pyxis.Contact;
import ch.globaz.vulpecula.external.models.pyxis.MoyenContact;
import ch.globaz.vulpecula.external.models.pyxis.TypeContact;
import ch.globaz.vulpecula.ws.bean.AdresseEbu;
import ch.globaz.vulpecula.ws.bean.CotisationEbu;
import ch.globaz.vulpecula.ws.bean.EmployeurEbu;
import ch.globaz.vulpecula.ws.bean.PeriodeEbu;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceEbu;
import ch.globaz.vulpecula.ws.bean.StatusReponse;
import ch.globaz.vulpecula.ws.utils.UtilsService;

/**
 * 
 * @since eBMS 1.0
 */
@WebService(endpointInterface = "ch.globaz.vulpecula.ws.services.EmployeurEbuService")
public class EmployeurEbuServiceImpl extends VulpeculaAbstractService implements EmployeurEbuService {

    private final static int DAY_OF_MONTH_LIMIT = 20;

    @Override
    public List<PeriodeEbu> periodeAmcabEmployeur(String idEmployeur) {

        List<PeriodeEbu> listePeriode = new ArrayList<PeriodeEbu>();

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            List<Adhesion> listeAdhesion = VulpeculaRepositoryLocator.getAdhesionRepository().findByIdAffilie(
                    idEmployeur);
            for (Adhesion adhesion : listeAdhesion) {
                if (adhesion.getPlanCaisse().getAdministration().getGenre()
                        .equalsIgnoreCase(CSTiers.CS_TIERS_GENRE_ADMINISTRATION_CAISSE_METIER)
                        && adhesion.getPlanCaisse().getAdministration().getCodeAdministration().equals("34")) {
                    if (adhesion.getDateFin() != null) {
                        if (!adhesion.getDateDebut().equals(adhesion.getDateFin())) {
                            listePeriode.add(new PeriodeEbu(adhesion.getDateDebut().toString(), adhesion.getDateFin()
                                    .toString()));
                        }
                    } else {
                        listePeriode.add(new PeriodeEbu(adhesion.getDateDebut().toString(), ""));
                    }

                }
            }
        } catch (SQLException e) {
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return listePeriode;

    }

    @Override
    public EmployeurEbu getEmployeur(String numeroAffilie) {
        Validate.notEmpty(numeroAffilie.trim(), "numeroAffilie must not be empty !");
        Date dateCoti;
        dateCoti = new Date();
        EmployeurEbu employEbu = null;
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByNumAffilie(numeroAffilie);

            // Remove test for eBusiness, cart pro
            // Validate.isTrue(employeur.isEBusiness(), "The employer " + numeroAffilie +
            // " is not set up for e-business");

            AdresseEbu adresseDomicile = getAdresseEbu(employeur.getIdTiers(), dateCoti.getSwissValue(),
                    AdresseService.CS_TYPE_DOMICILE);
            AdresseEbu adresseCourrier = getAdresseEbu(employeur.getIdTiers(), dateCoti.getSwissValue(),
                    AdresseService.CS_TYPE_COURRIER);

            List<CotisationEbu> cotisationsEbu = getCotisationsEbu(employeur.getId(), dateCoti);

            employEbu = new EmployeurEbu(employeur);
            employEbu.setCotisations(cotisationsEbu);
            employEbu.setAdresseCourrier(adresseCourrier);
            employEbu.setAdresseDomicile(adresseDomicile);

        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        return employEbu;
    }

    private AdresseEbu getAdresseEbu(String idTiers, String dateCotisation, String typeAdresse) {
        AdresseTiersDetail adresse = getAdresse(idTiers, dateCotisation, typeAdresse);
        AdresseEbu adresseEbu = new AdresseEbu();
        if (adresse.getFields() != null) {
            adresseEbu.setALAttention(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION));
            adresseEbu.setRue(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
            adresseEbu.setRueNumero(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
            adresseEbu.setCasePostale(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_CASE_POSTALE));
            adresseEbu.setNpa(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));
            adresseEbu.setLocalite(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
            // adresseEbu.setIdLocalite(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID));
            adresseEbu.setPays(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_PAYS_ISO));
            // adresseEbu.setDescription1(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1));
            // adresseEbu.setDescription2(adresse.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2));
        }
        return adresseEbu;
    }

    private AdresseTiersDetail getAdresse(String idTiers, String date, String typeAdresse) {
        BSession session = UtilsService.initSession();
        try {
            return VulpeculaServiceLocator.getAdresseService().getAdresseTiers(idTiers, false, date,
                    AdresseService.CS_DOMAINE_DEFAUT, typeAdresse, "");
        } catch (JadeApplicationServiceNotAvailableException e) {
            session.addError(e.getMessage());
        } catch (JadePersistenceException e) {
            session.addError(e.getMessage());
        } catch (JadeApplicationException e) {
            session.addError(e.getMessage());
        }
        return null;
    }

    private List<CotisationEbu> getCotisationsEbu(String idAffilie, Date dateCoti) {
        List<CotisationEbu> cotisationsEbu = new ArrayList<CotisationEbu>();
        List<Cotisation> cotisations = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(idAffilie,
                dateCoti);
        for (Cotisation coti : cotisations) {
            // On ne n'ajoute que les cotisations de type paritaire
            if (IAFAssurance.PARITAIRE.equals(coti.getAssurance().getAssuranceGenre())) {
                CotisationEbu cotiEbu = new CotisationEbu(coti);
                cotisationsEbu.add(cotiEbu);
            }
        }
        return cotisationsEbu;
    }

    @Override
    public String getIdEmployeur(String numeroAffilie) {
        Validate.notEmpty(numeroAffilie.trim(), "numeroAffilie must not be empty !");
        String id = "";
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByNumAffilie(numeroAffilie);

            // Remove test for eBusiness, cart pro
            // Validate.isTrue(employeur.isEBusiness(), "The employer " + numeroAffilie +
            // " is not set up for e-business");
            id = employeur.getId();
        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return id;
    }

    @Override
    public Boolean useEbusiness(String numeroAffilie) {
        Validate.notEmpty(numeroAffilie.trim(), "numeroAffilie must not be empty !");
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByNumAffilie(numeroAffilie);
            return employeur.isEBusiness();
        } catch (SQLException e) {
            session.addError(e.getMessage());
            JadeLogger.error(e, e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        return true;
    }

    @Override
    public StatusAnnonceEbu inscrireEntrepriseEbusiness(String numeroAffilie) {
        String message = "";
        StatusAnnonceEbu status = new StatusAnnonceEbu();
        status.setReponse(StatusReponse.OK);
        Validate.notEmpty(numeroAffilie.trim(), "numeroAffilie must not be empty !");
        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            // Mettre à jour ebusiness
            AffiliationTiersSearchComplexModel search = new AffiliationTiersSearchComplexModel();
            search.setLikeNumeroAffilie(numeroAffilie);
            JadePersistenceManager.search(search);
            for (int i = 0; i < search.getSearchResults().length; i++) {
                AffiliationTiersComplexModel tiersComplex = (AffiliationTiersComplexModel) search.getSearchResults()[i];
                AffiliationSimpleModel affiliation = tiersComplex.getAffiliation();
                affiliation.setDeclarationSalaire(Employeur.EBUSINESS);
                JadePersistenceManager.update(affiliation);
            }
            // Efface décomptes futures lors de l'inscription
            message = effacerDecomptesFutures(numeroAffilie, status, session);

        } catch (SQLException e) {
            session.addError(e.getMessage());
            status.setReponse(StatusReponse.ERROR);
            message = e.getMessage();
        } catch (Exception e) {
            session.addError(e.getMessage());
            status.setReponse(StatusReponse.ERROR);
            message = e.getMessage();
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
        status.setMessage(message);
        return status;
    }

    public String effacerDecomptesFutures(String numeroAffilie, StatusAnnonceEbu status, BSession session) {
        String message = "";
        Date today = Date.now();
        int jour = Integer.parseInt(today.getJour());
        int mois = Integer.parseInt(today.getMois());

        Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByNumAffilie(numeroAffilie);

        DecompteSearchComplexModel search = new DecompteSearchComplexModel();
        search.setForIdEmployeur(employeur.getId());

        try {
            search = (DecompteSearchComplexModel) JadePersistenceManager.search(search);
            for (int i = 0; i < search.getSearchResults().length; i++) {
                DecompteComplexModel decompteComplex = (DecompteComplexModel) search.getSearchResults()[i];
                DecompteSimpleModel decompte = decompteComplex.getDecompteSimpleModel();
                Date decompteDate = new Date(decompte.getPeriodeDebut());
                if (decompteDate.after(today)) {
                    DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();
                    decompteRepository.deleteById(decompte.getId());
                } else if (decompteDate.getAnnee().equals(today.getAnnee())) {
                    if (isDateBeforeJour20DuMois(decompteDate)) {
                        DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();
                        decompteRepository.deleteById(decompte.getId());
                    } else {
                        effacerDecomptesTrimestriel(decompte, mois, jour);
                    }
                }
            }
        } catch (JadePersistenceException e) {
            session.addError(e.getMessage());
            status.setReponse(StatusReponse.ERROR);
            message = e.getMessage();
        }

        return message;
    }

    private boolean isDateBeforeJour20DuMois(Date decompteDate) {
        Date today = Date.now();
        int jour = Integer.parseInt(today.getJour());
        if (decompteDate.getMois().equals(today.getMois()) && jour < DAY_OF_MONTH_LIMIT) {
            return true;
        }
        return false;
    }

    private boolean isDateBeforeJour20DuMoisForTrimerstriel(Date decompteDate) {
        Date today = Date.now();
        int jour = Integer.parseInt(today.getJour());
        int mois = Integer.parseInt(today.getMois());
        if (mois <= decompteDate.getNumeroMois() || (mois == decompteDate.getNumeroMois() && jour < DAY_OF_MONTH_LIMIT)) {
            return true;
        }
        return false;
    }

    private void effacerDecomptesTrimestriel(DecompteSimpleModel decompte, int mois, int jour) {
        // Si périodique
        // si décompte trimestriel
        // Si on se trouve dans cette période trimestriel
        // Si on est before le 20 du troisième mois
        if (TypeDecompte.PERIODIQUE.getValue().equals(decompte.getType()) && isDecompteTrimestriel(decompte)
                && isTodayDansDecompteTrimestriel(decompte, mois)
                && isDateBeforeJour20DuMoisForTrimerstriel(new Date(decompte.getPeriodeFin()).getLastDayOfMonth())) {
            DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();
            decompteRepository.deleteById(decompte.getId());
        }
    }

    private boolean isTodayDansDecompteTrimestriel(DecompteSimpleModel decompte, int moisActuel) {
        int nbMoisDebut = new Date(decompte.getPeriodeDebut()).getNumeroMois();
        int nbMoisFin = new Date(decompte.getPeriodeFin()).getNumeroMois();
        if (moisActuel >= nbMoisDebut && moisActuel <= nbMoisFin) {
            return true;
        }
        return false;

    }

    private boolean isDecompteTrimestriel(DecompteSimpleModel decompte) {
        int nbMoisDebut = new Date(decompte.getPeriodeDebut()).getNumeroMois();
        int nbMoisFin = new Date(decompte.getPeriodeFin()).getNumeroMois();
        if (nbMoisFin - nbMoisDebut == 2) {
            return true;
        }
        return false;
    }

    private String createEbuContact(Employeur employeur, String nom, String prenom, String numeroTel, String email)
            throws JadePersistenceException {

        Contact contact = new Contact();

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            contact.setPrenom(prenom);
            contact.setNom(nom);
            contact = VulpeculaRepositoryLocator.getContactRepository().create(contact);

            ContactTiersSimpleModel model = new ContactTiersSimpleModel();
            model.setIdContact(contact.getId());
            model.setIdTiers(employeur.getIdTiers());
            JadePersistenceManager.add(model);

            MoyenContactTiersSimpleModel modelTel = new MoyenContactTiersSimpleModel();
            modelTel.setApplication("519004");
            modelTel.setIdContact(contact.getId());
            modelTel.setTypeContact(TypeContact.EBUSINESS.getValue());
            modelTel.setValeur(numeroTel);
            JadePersistenceManager.add(modelTel);

            MoyenContactTiersSimpleModel modelEmail = new MoyenContactTiersSimpleModel();
            modelEmail.setApplication("519004");
            modelEmail.setIdContact(contact.getId());
            modelEmail.setTypeContact(TypeContact.EMAIL.getValue());
            modelEmail.setValeur(email);
            JadePersistenceManager.add(modelEmail);

        } catch (SQLException e) {
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (contact != null && !JadeStringUtil.isEmpty(contact.getId())) {
            return contact.getId();
        } else {
            return "Error while creating contact";
        }
    }

    private String getStatementUpdateContact(String idContact, String valeur, String type) {

        return "UPDATE SCHEMA.TIMCOMP SET HLCONT='" + valeur + "' WHERE HLICON=" + idContact + " AND HLTTCO=" + type;

    }

    private String updateEbuContact(Employeur employeur, String nom, String prenom, String newNumeroTel,
            String newEmail, Contact contact) throws JadePersistenceException {

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            QueryUpdateExecutor.executeUpdate(
                    getStatementUpdateContact(contact.getId(), newNumeroTel, TypeContact.EBUSINESS.getValue()),
                    MoyenContact.class, session);

            QueryUpdateExecutor.executeUpdate(
                    getStatementUpdateContact(contact.getId(), newEmail, TypeContact.EMAIL.getValue()),
                    MoyenContact.class, session);

        } catch (SQLException e) {
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (contact != null && !JadeStringUtil.isEmpty(contact.getId())) {
            return "Contact with id : " + contact.getId() + " updated";
        } else {
            return "Erreur";
        }

    }

    @Override
    public String ajouterContactEbusinessUser(String idEmployeur, String nom, String prenom, String numeroTel,
            String email) throws JadePersistenceException {

        Contact contact = new Contact();
        Employeur employeur = null;
        boolean exist = false;
        List<Contact> contactsExistants = new ArrayList<Contact>();

        BSession session = UtilsService.initSession();
        JadeThreadContext threadContext = initThreadContext(session);

        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);
            contactsExistants = VulpeculaRepositoryLocator.getContactRepository().findContactsByIdTiers(
                    employeur.getIdTiers());
        } catch (SQLException e) {
            session.addError(e.getMessage());
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        for (Contact cont : contactsExistants) {
            if (nom.equals(cont.getNom()) && prenom.equals(cont.getPrenom())) {
                exist = true;
                contact = cont;
            }
        }

        if (!exist) {
            return createEbuContact(employeur, nom, prenom, numeroTel, email);
        } else {
            return updateEbuContact(employeur, nom, prenom, numeroTel, email, contact);
        }
    }
}

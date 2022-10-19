package globaz.pyxis.web.service;

import ch.globaz.pyxis.business.model.*;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.pyxis.business.services.AdresseService;
import ch.globaz.pyxis.businessimpl.services.AdresseServiceImpl;
import ch.globaz.pyxis.domaine.DomaineApplication;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.naos.application.AFApplication;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIPersonneAvs;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiementManager;
import globaz.pyxis.db.divers.TICodeSystemRightChecker;
import globaz.pyxis.db.tiers.*;
import globaz.pyxis.util.TIIbanFormater;
import globaz.pyxis.web.DTO.*;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYInternalException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static ch.globaz.pyxis.business.services.AdresseService.CS_TYPE_COURRIER;

@Slf4j
public class PYExecuteService extends BProcess {

    /**
     * Création de tiers
     *
     * @param dto   JSON mappé en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers créé
     */
    public final PYTiersDTO createTiers(PYTiersDTO dto, String token) {
        String idAddress = null;
        try {
            createTiersPage1(getSession(), dto);

            // Only add a mail address if the DTO contains an address
            if (dto.getAddresses().size() != 0) {
                idAddress = addTiersAddresses(getSession(), dto);
            }
            if (idAddress != null) {
                addTiersPaymentAddresses(getSession(), idAddress, null, true, dto);
            }
            addTiersPage2(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du tiers: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du tiers: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création du tiers: " + e);
            throw new PYInternalException(e);
        }

        return dto;
    }


    public final PYTiersPage1DTO createTiersPage1(PYTiersPage1DTO dto, String token) {
        try {
            createTiersPage1(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du tiers page 1: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du tiers page 1: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création du tiers page 1: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour les web services CCB/CCVS afin d'ajouter un tiers - page 1 (détails généraux)
     *
     * @param session
     * @param dto
     * @throws Exception
     */
    private void createTiersPage1(BSession session, PYTiersPage1DTO dto) throws Exception {
        ITIPersonneAvs avsPerson = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);

        // Fields in TITIERP
        avsPerson.setTypeTiers(ITITiers.CS_TIERS);
        avsPerson.setTitreTiers(dto.getTitle());
        avsPerson.setDesignation1(dto.getSurname());
        avsPerson.setDesignation2(dto.getName());
        avsPerson.setDesignation3(dto.getName1());
        avsPerson.setDesignation4(dto.getName2());
        avsPerson.setLangue(dto.getLanguage());
        avsPerson.setIdPays(dto.getNationality());
        avsPerson.setPersonnePhysique(dto.getIsPhysicalPerson());
        avsPerson.setPersonneMorale(!dto.getIsPhysicalPerson());
        avsPerson.setInactif(dto.getIsInactive());
        avsPerson.setNomJeuneFille(dto.getMaidenName());
        // Fields in TIPERSP
        avsPerson.setDateNaissance(dto.getBirthDate());
        avsPerson.setDateDeces(dto.getDeathDate());
        avsPerson.setSexe(dto.getSex());
        avsPerson.setEtatCivil(dto.getCivilStatus());

        // Fields in TIPAVSP
        avsPerson.setNumAvsActuel(dto.getNss());
        avsPerson.setNumContribuableActuel(dto.getTaxpayerNumber());

        avsPerson.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));

        if (session.getCurrentThreadTransaction() != null) {
            avsPerson.add(session.getCurrentThreadTransaction());
            checkForErrorsAndThrow(session, "Erreur DB lors de la création de tiers");
        } else {
            throw new PYInternalException("Erreur de session lors de la création du tiers.");
        }
        dto.setId(avsPerson.getIdTiers());
    }

    /**
     * Modification de tiers page 1
     *
     * @param dto   JSON mappé en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers créé et la date de mise à jour
     */
    public final PYTiersPage1DTO updateTiersPage1(PYTiersPage1DTO dto, String token) {
        try {
            updateTiersPage1(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification du tiers page 1: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du tiers page 1: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification du tiers page 1: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour mettre à jour un tiers (page 1)
     *
     * @param session
     * @param dto
     * @return
     * @throws Exception
     */
    private void updateTiersPage1(BSession session, PYTiersPage1DTO dto) throws Exception {
        //Vérifier si le tiers est existant
        TITiers existingTiers = AFApplication.retrieveTiers(session, dto.getId());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#updateTiersPage1 - L'idTiers renseigné ne correspond à aucun Tiers.");
        }

        ITIPersonneAvs avsPerson = (ITIPersonneAvs) session.getAPIFor(ITIPersonneAvs.class);

        String reason = TIHistoriqueContribuable.CS_CREATION; // TODO: This should probably be given by the user, maybe extend the DTO to add this

        // Get the tiers from database
        avsPerson.setIdTiers(dto.getId());
        avsPerson.retrieve(session.getCurrentThreadTransaction());

        // We need to set a date and a reason for the update(s)
        avsPerson.setMotifModifTitre(reason);
        avsPerson.setDateModifTitre(dto.getModificationDate());
        avsPerson.setMotifModifDesignation1(reason);
        avsPerson.setDateModifDesignation1(dto.getModificationDate());
        avsPerson.setMotifModifDesignation2(reason);
        avsPerson.setDateModifDesignation2(dto.getModificationDate());
        avsPerson.setMotifModifDesignation3(reason);
        avsPerson.setDateModifDesignation3(dto.getModificationDate());
        avsPerson.setMotifModifDesignation4(reason);
        avsPerson.setDateModifDesignation4(dto.getModificationDate());
        avsPerson.setMotifModifAvs(reason);
        avsPerson.setDateModifAvs(dto.getModificationDate());
        avsPerson.setMotifModifContribuable(reason);
        avsPerson.setDateModifContribuable(dto.getModificationDate());
        avsPerson.setMotifModifPays(reason);
        avsPerson.setDateModifPays(dto.getModificationDate());

        // Update avsPerson with all the new values as long as they aren't null
        if (dto.getTitle() != null)
            avsPerson.setTitreTiers(dto.getTitle());
        if (dto.getSurname() != null)
            avsPerson.setDesignation1(dto.getSurname());
        if (dto.getName() != null)
            avsPerson.setDesignation2(dto.getName());
        if (dto.getName1() != null)
            avsPerson.setDesignation3(dto.getName1());
        if (dto.getName2() != null)
            avsPerson.setDesignation4(dto.getName2());
        if (dto.getMaidenName() != null)
            avsPerson.setNomJeuneFille(dto.getMaidenName());
        if (dto.getNss() != null)
            avsPerson.setNumAvsActuel(dto.getNss());
        if (dto.getBirthDate() != null)
            avsPerson.setDateNaissance(dto.getBirthDate());
        if (dto.getDeathDate() != null)
            avsPerson.setDateDeces(dto.getDeathDate());
        if (dto.getSex() != null)
            avsPerson.setSexe(dto.getSex());
        if (dto.getCivilStatus() != null)
            avsPerson.setEtatCivil(dto.getCivilStatus());
        if (dto.getLanguage() != null)
            avsPerson.setLangue(dto.getLanguage());
        if (dto.getNationality() != null)
            avsPerson.setIdPays(dto.getNationality());
        if (dto.getTaxpayerNumber() != null)
            avsPerson.setNumContribuableActuel(dto.getTaxpayerNumber());
        if (dto.getIsPhysicalPerson() != null) {
            avsPerson.setPersonnePhysique(dto.getIsPhysicalPerson());
            avsPerson.setPersonneMorale(!dto.getIsPhysicalPerson());
        }
        if (dto.getIsInactive() != null)
            avsPerson.setInactif(dto.getIsInactive());

        // Make the actual transaction with the database in order to update the tiers
        avsPerson.update(session.getCurrentThreadTransaction());
        checkForErrorsAndThrow(session, "PYExecuteService#updateTiersPage1 - Erreur rencontrée lors de l'update du tiers");

        dto.setModificationDate(dto.getModificationDate());
    }

    /**
     * Modification d'un contact
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYContactDTO updateContact(PYContactDTO dto, String token) {
        try {
            updateContact(getSession(), dto.getId(), dto.getLastName(), dto.getFirstName());
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification du contact: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du contact: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification du contact: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour mettre à jour un contact (table TICONTP).
     *
     * @param session
     * @param idContact    clé primaire
     * @param newLastName  valeur à mettre à jour
     * @param newFirstName valeur à mettre à jour
     * @throws Exception
     */
    private void updateContact(BSession session, String idContact, String newLastName, String newFirstName) throws Exception {
        TIContact tiContact = new TIContact();
        tiContact.setIdContact(idContact);
        tiContact.retrieve(session.getCurrentThreadTransaction());

        if (newLastName != null && !newLastName.isEmpty())
            tiContact.setNom(newLastName);
        if (newFirstName != null && !newFirstName.isEmpty())
            tiContact.setPrenom(newFirstName);

        tiContact.update(session.getCurrentThreadTransaction());
    }

    /**
     * Modification d'un moyen de communication.
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYMeanOfCommunicationDTO updateMeanOfCommunication(PYMeanOfCommunicationDTO dto, String token) {
        try {
            updateMeanOfCommunication(getSession(), dto.getIdContact(), dto.getMeanOfCommunicationType(), dto.getApplicationDomain(), dto.getMeanOfCommunicationValue());
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification du moyen de communication: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du moyen de communication: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification du moyen de communication: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Methode pour mettre à jour un moyen de communication (table TIMCOMP).
     * <p>
     * Seul la valeur (newMoyen) peut être modifiée.
     *
     * @param session
     * @param idContact          clé composite
     * @param typeCommunication  clé composite
     * @param domaineApplication clé composite
     * @param newMoyen           valeur à mettre à jour
     * @throws Exception
     */
    private void updateMeanOfCommunication(BSession session, String idContact, String typeCommunication, String domaineApplication, String newMoyen) throws Exception {
        // Get the contact
        TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
        tiMoyenCommunication.setIdApplication(domaineApplication);
        tiMoyenCommunication.setIdContact(idContact);
        tiMoyenCommunication.setTypeCommunication(typeCommunication);
        tiMoyenCommunication.retrieve(session.getCurrentThreadTransaction());

        // Update the contact
        if (!newMoyen.isEmpty() && newMoyen != null)
            tiMoyenCommunication.setMoyen(newMoyen);
        //tiMoyenCommunication.setIdApplication(newDomaineApplication);         // Those two don't seem to work
        //tiMoyenCommunication.setTypeCommunication(newTypeCommunication);      // Maybe because they're part of the composite key ?
        tiMoyenCommunication.update(session.getCurrentThreadTransaction());
    }

    /**
     * Création d'un contact et de ses moyens de communication
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYContactDTO createContact(PYContactCreateDTO dto, String token) {
        try {
            String idContact = createContact(getSession(), dto.getIdTiers(), dto.getLastName(), dto.getFirstName());
            dto.setId(idContact);
            for (PYMeanOfCommunicationDTO mean : dto.getMeansOfCommunication()) {
                createMeanOfCommunication(getSession(), dto.getId(), mean.getApplicationDomain(), mean.getMeanOfCommunicationType(), mean.getMeanOfCommunicationValue());
            }
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du contact: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du contact: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création du contact: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour créer un contact avec des moyens de communication.
     *
     * @param session
     * @param idTiers
     * @param lastName
     * @param firstName
     * @return
     * @throws Exception
     */
    private String createContact(BSession session, String idTiers, String lastName, String firstName) throws Exception {
        TITiers existingTiers = AFApplication.retrieveTiers(session, idTiers);

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#createContact- L'idTiers renseigné ne correspond à aucun Tiers.");
        }


        TIContact tiContact = new TIContact();
        tiContact.setNom(lastName);
        tiContact.setPrenom(firstName);
        tiContact.add(session.getCurrentThreadTransaction());

        TIAvoirContact tiAvoirContact = new TIAvoirContact();
        tiAvoirContact.setIdContact(tiContact.getIdContact());
        tiAvoirContact.setIdTiers(idTiers);
        tiAvoirContact.add(session.getCurrentThreadTransaction());

        return tiContact.getIdContact();
    }

    /**
     * Suppression d'un contact
     *
     * @param dto
     * @param token
     * @return
     */
    public final String deleteContact(PYContactCreateDTO dto, String token) {
        try {
            deleteContact(getSession(), dto.getId(), dto.getIdTiers());
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la suppression du contact: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression du contact: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la suppression du contact: " + e);
            throw new PYInternalException(e);
        }
        return "Deletion successful";
    }

    /**
     * Méthode pour supprimer un contact.
     *
     * @param session
     * @param id
     * @param idTiers
     * @throws Exception
     */
    private void deleteContact(BSession session, String id, String idTiers) throws Exception {
        TIContact tiContact = new TIContact();
        tiContact.setIdContact(id);
        tiContact.retrieve(session.getCurrentThreadTransaction());
        tiContact.delete(session.getCurrentThreadTransaction());
    }

    /**
     * Création d'un moyen de communication.
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYMeanOfCommunicationDTO createMeanOfCommunication(PYMeanOfCommunicationDTO dto, String token) {
        try {
            createMeanOfCommunication(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du moyen de communication: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du moyen de communication: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création du moyen de communication: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour créer un moyen de communication pour un contact.
     *
     * @param session
     * @param dto
     * @throws Exception
     */
    private void createMeanOfCommunication(BSession session, PYMeanOfCommunicationDTO dto) throws Exception {
        TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
        tiMoyenCommunication.setMoyen(dto.getMeanOfCommunicationValue());
        tiMoyenCommunication.setTypeCommunication(dto.getMeanOfCommunicationType());
        tiMoyenCommunication.setIdApplication(dto.getApplicationDomain());
        tiMoyenCommunication.setIdContact(dto.getIdContact());
        tiMoyenCommunication.add(session.getCurrentThreadTransaction());
    }

    /**
     * Méthode pour créer un moyen de communication pour un contact.
     *
     * @param session
     * @param idContact
     * @param application
     * @param type
     * @param value
     * @throws Exception
     */
    private void createMeanOfCommunication(BSession session, String idContact, String application, String type, String value) throws Exception {
        TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
        tiMoyenCommunication.setMoyen(value);
        tiMoyenCommunication.setTypeCommunication(type);
        tiMoyenCommunication.setIdApplication(application);
        tiMoyenCommunication.setIdContact(idContact);
        tiMoyenCommunication.add(session.getCurrentThreadTransaction());
    }

    /**
     * Suppression d'un moyen de communication.
     *
     * @param dto
     * @param token
     * @return
     */
    public final String deleteMeanOfCommunication(PYMeanOfCommunicationDTO dto, String token) {
        try {
            deleteMeanOfCommunication(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la suppression du moyen de communication: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression du moyen de communication: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la suppression du moyen de communication: " + e);
            throw new PYInternalException(e);
        }
        return "Deletion successful";
    }

    /**
     * Méthode pour supprimer un moyen de communication d'un contact.
     *
     * @param session
     * @param dto
     * @throws Exception
     */
    private void deleteMeanOfCommunication(BSession session, PYMeanOfCommunicationDTO dto) throws Exception {
        TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
        tiMoyenCommunication.setTypeCommunication(dto.getMeanOfCommunicationType());
        tiMoyenCommunication.setIdApplication(dto.getApplicationDomain());
        tiMoyenCommunication.setIdContact(dto.getIdContact());
        tiMoyenCommunication.retrieve(session.getCurrentThreadTransaction());
        tiMoyenCommunication.delete(session.getCurrentThreadTransaction());
    }

    /**
     * Méthode pour les web services CCB/CCVS afin d'ajouter un tiers - page 2 (les contacts/moyens de communication)
     *
     * @param session
     * @param dto
     * @throws Exception
     */
    private void addTiersPage2(BSession session, PYTiersDTO dto) throws Exception {
        for (PYContactCreateDTO contactDTO : dto.getContacts()) {
            TIContact contact = new TIContact();
            contact.setSession(session);
            contact.setNom(contactDTO.getLastName());
            contact.setPrenom(contactDTO.getFirstName());
            contact.add();

            for (PYMeanOfCommunicationDTO meanDTO : contactDTO.getMeansOfCommunication()) {
                TIMoyenCommunication meanOfCommunication = new TIMoyenCommunication();
                meanOfCommunication.setSession(session);
                meanOfCommunication.setTypeCommunication(meanDTO.getMeanOfCommunicationType());
                meanOfCommunication.setMoyen(meanDTO.getMeanOfCommunicationValue());
                meanOfCommunication.setIdContact(contact.getIdContact());
                if (meanDTO.getApplicationDomain() != null)
                    meanOfCommunication.setIdApplication(meanDTO.getApplicationDomain());
                meanOfCommunication.add();
            }

            TIAvoirContact hasContact = new TIAvoirContact();
            hasContact.setSession(session);
            hasContact.setIdTiers(dto.getId());
            hasContact.setIdContact(contact.getIdContact());
            hasContact.add();
        }

        checkForErrorsAndThrow(session, "PYExecuteService#addTiersPage2 - Erreur rencontrée lors de la création de contact");
    }

    /**
     * Méthode pour mettre à jour un tiers (page 2 - contacts/moyens de communication)
     *
     * @param session
     * @param dto
     * @return
     * @throws Exception
     */
    private void updateTiersPage2(BSession session, PYTiersDTO dto) throws Exception {
        for (PYContactCreateDTO contact : dto.getContacts()) {
            TIContact tiContact = new TIContact();
            tiContact.setIdContact(contact.getId());
            tiContact.retrieve(session.getCurrentThreadTransaction());
            if (!tiContact.isNew()) {
                throw new PYBadRequestException("PYExecuteService#updateTiersPage2 - Le contact à modifier n'existe pas");
            }

            updateContact(session, contact.getId(), contact.getLastName(), contact.getFirstName());

            Vector<PYMeanOfCommunicationDTO> meansOfCommunication = contact.getMeansOfCommunication();
            for (PYMeanOfCommunicationDTO mean : meansOfCommunication) {
                // Check that mean's id belongs to the contact (i.e. check that a Contact/TICONTP with HLICON same as MoyenDeCommunication/TIMCOMP exists)
                TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
                tiMoyenCommunication.setIdApplication(mean.getApplicationDomain());
                tiMoyenCommunication.setIdContact(contact.getId());
                tiMoyenCommunication.setTypeCommunication(mean.getMeanOfCommunicationType());
                tiMoyenCommunication.retrieve(session.getCurrentThreadTransaction());
                if (!tiMoyenCommunication.isNew()) {
                    throw new PYBadRequestException("PYExecuteService#updateTiersPage2 - Le moyen de communication à modifier n'existe pas");
                }

                updateMeanOfCommunication(session, contact.getId(), mean.getMeanOfCommunicationType(), mean.getApplicationDomain(), mean.getMeanOfCommunicationValue());
            }
        }

        checkForErrorsAndThrow(session, "PYExecuteService#updateTiersPage2 - Erreur rencontrée lors de l'update de contact");
    }


    /**
     * Création d'une adresse.
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYAddressDTO createAddress(PYAddressDTO dto, String token) {
        try {
            addTiersAddress(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création de l'adresse: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création de l'adresse: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création de l'adresse: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour les web services CCB/CCVS afin d'ajouter une adresse (domicile / courrier) à un tiers
     *
     * @param session
     * @param dto
     * @return l'id de l'adresse courrier standard si présente, sinon l'id de l'adresse de domicile standard, sinon null
     * @throws Exception
     */
    private String addTiersAddresses(BSession session, PYTiersDTO dto) throws Exception {
        Boolean hasCourrierStandard = false;
        String idAddress = null;

        TITiers existingTiers = AFApplication.retrieveTiers(session, dto.getId());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#addTiersAddresses - L'idTiers renseigné ne correspond à aucun Tiers.");
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersById(session, dto.getId());

        AdresseComplexModel homeAddress = null;

        //Using a Vector for adding multiple addresses
        for (PYAddressDTO addressDTO : dto.getAddresses()) {

            //Special need for CCVS. The domain is not always set to "Default".
            if (addressDTO.getDomainAddress() != null)
                addressDTO.setDomainAddress(addressDTO.getDomainAddress());
            else {
                addressDTO.setDomainAddress(String.valueOf(DomaineApplication.STANDARD.getSystemCode()));
            }


            String typeAddress = null;
            if (PRTiersHelper.CS_ADRESSE_DOMICILE.equals(addressDTO.getTypeAddress())) {
                typeAddress = PRTiersHelper.CS_ADRESSE_DOMICILE;
            } else if (PRTiersHelper.CS_ADRESSE_COURRIER.equals(addressDTO.getTypeAddress())) {
                typeAddress = PRTiersHelper.CS_ADRESSE_COURRIER;
            }


            AdresseSimpleModel adresseSimpleModel = new AdresseSimpleModel();
            adresseSimpleModel.setAttention(addressDTO.getAttention());
            adresseSimpleModel.setRue(addressDTO.getStreet());
            adresseSimpleModel.setNumeroRue(addressDTO.getStreetNumber());

            LocaliteSimpleModel localiteSimpleModel = new LocaliteSimpleModel();
            localiteSimpleModel.setNumPostal(addressDTO.getPostalCode());
            localiteSimpleModel.setLocalite(addressDTO.getLocality());
            localiteSimpleModel.setIdPays(addressDTO.getCountry());

            AvoirAdresseSimpleModel avoirAdresseSimpleModel = new AvoirAdresseSimpleModel();
            avoirAdresseSimpleModel.setIdTiers(tiers.getIdTiers());
            avoirAdresseSimpleModel.setIdAdresse(adresseSimpleModel.getIdAdresse());
            if (addressDTO.getModificationDate() != null)
                avoirAdresseSimpleModel.setDateDebutRelation(addressDTO.getModificationDate());

            AdresseTiersDetail mailAddress = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(tiers.getIdTiers(), false, new ch.globaz.common.domaine.Date().getSwissValue(), addressDTO.getDomainAddress(), typeAddress, "");

            PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
            searchTiers.setForIdTiers(tiers.getIdTiers());
            searchTiers = TIBusinessServiceLocator.getPersonneEtendueService().find(searchTiers);

            if (searchTiers.getNbOfResultMatchingQuery() == 1) {
                PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) searchTiers.getSearchResults()[0];

                AdresseComplexModel adresseComplexModel = new AdresseComplexModel();
                adresseComplexModel.setTiers(personneEtendueComplexModel);
                adresseComplexModel.setAdresse(adresseSimpleModel);
                adresseComplexModel.setLocalite(localiteSimpleModel);
                adresseComplexModel.setAvoirAdresse(avoirAdresseSimpleModel);

                homeAddress = TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, addressDTO.getDomainAddress(), typeAddress, false);

                if (!Objects.isNull(homeAddress)) {
                    addressDTO.setIdTiers(homeAddress.getTiers().getId());
                    addressDTO.setIdAvoirAddress(homeAddress.getAvoirAdresse().getIdAdresseIntUnique());
                    addressDTO.setIdAddress(homeAddress.getAdresse().getId());
                }


                if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
                    LOG.error("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré");
                    throw new PYBadRequestException("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré: " + session.getCurrentThreadTransaction().getErrors().toString());

                } else if (!JadeThread.logIsEmpty()) {
                    LOG.error("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré");
                    throw new PYBadRequestException("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()).toString());
                }
            } else if (searchTiers.getNbOfResultMatchingQuery() != 1 || mailAddress.getFields() != null) {
                throw new PYInternalException("Une erreur s'est produite pendant la récupération de l'adresse.");
            }


            //utilisé pour le retour de la fonction. On veut retourner si possible une adresse de courrier standard afin de l'utiliser pour créer une adresse de paiement.
            if (typeAddress.equals(CS_TYPE_COURRIER) && addressDTO.getDomainAddress().equals(String.valueOf(DomaineApplication.STANDARD.getSystemCode()))) {
                hasCourrierStandard = true;
                idAddress = homeAddress.getAdresse().getId();
            } else if (!hasCourrierStandard && addressDTO.getDomainAddress().equals(String.valueOf(DomaineApplication.STANDARD.getSystemCode())))
                idAddress = homeAddress.getAdresse().getId();

        }
        //TODO retourner une List d'idAddress
        return idAddress;
    }


    private void addTiersAddress(BSession session, PYAddressDTO addressDTO) throws Exception {

        TITiers existingTiers = AFApplication.retrieveTiers(session, addressDTO.getIdTiers());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#addTiersAddress - L'idTiers renseigné ne correspond à aucun Tiers.");
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersById(session, addressDTO.getIdTiers());

        AdresseComplexModel homeAddress = null;


        //Special need for CCVS. The domain is not always set to "Default".
        if (addressDTO.getDomainAddress() != null)
            addressDTO.setDomainAddress(addressDTO.getDomainAddress());
        else {
            addressDTO.setDomainAddress(String.valueOf(DomaineApplication.STANDARD.getSystemCode()));
        }


        String typeAddress = null;
        if (PRTiersHelper.CS_ADRESSE_DOMICILE.equals(addressDTO.getTypeAddress())) {
            typeAddress = PRTiersHelper.CS_ADRESSE_DOMICILE;
        } else if (PRTiersHelper.CS_ADRESSE_COURRIER.equals(addressDTO.getTypeAddress())) {
            typeAddress = PRTiersHelper.CS_ADRESSE_COURRIER;
        }


        AdresseSimpleModel adresseSimpleModel = new AdresseSimpleModel();
        adresseSimpleModel.setAttention(addressDTO.getAttention());
        adresseSimpleModel.setRue(addressDTO.getStreet());
        adresseSimpleModel.setNumeroRue(addressDTO.getStreetNumber());
        adresseSimpleModel.setLigneAdresse2(addressDTO.getComplementAdresse1());
        adresseSimpleModel.setLigneAdresse3(addressDTO.getComplementAdresse2());


        LocaliteSimpleModel localiteSimpleModel = new LocaliteSimpleModel();
        localiteSimpleModel.setNumPostal(addressDTO.getPostalCode());
        localiteSimpleModel.setLocalite(addressDTO.getLocality());
        localiteSimpleModel.setIdPays(addressDTO.getCountry());

        AvoirAdresseSimpleModel avoirAdresseSimpleModel = new AvoirAdresseSimpleModel();
        avoirAdresseSimpleModel.setIdTiers(tiers.getIdTiers());
        avoirAdresseSimpleModel.setIdAdresse(adresseSimpleModel.getIdAdresse());
        if (addressDTO.getModificationDate() != null)
            avoirAdresseSimpleModel.setDateDebutRelation(addressDTO.getModificationDate());

        AdresseTiersDetail mailAddress = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(tiers.getIdTiers(), false, new ch.globaz.common.domaine.Date().getSwissValue(), addressDTO.getDomainAddress(), typeAddress, "");

        PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
        searchTiers.setForIdTiers(tiers.getIdTiers());
        searchTiers = TIBusinessServiceLocator.getPersonneEtendueService().find(searchTiers);

        if (searchTiers.getNbOfResultMatchingQuery() == 1) {
            PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) searchTiers.getSearchResults()[0];

            AdresseComplexModel adresseComplexModel = new AdresseComplexModel();
            adresseComplexModel.setTiers(personneEtendueComplexModel);
            adresseComplexModel.setAdresse(adresseSimpleModel);
            adresseComplexModel.setLocalite(localiteSimpleModel);
            adresseComplexModel.setAvoirAdresse(avoirAdresseSimpleModel);

            homeAddress = TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, addressDTO.getDomainAddress(), typeAddress, false);

            if (!Objects.isNull(homeAddress)) {
                addressDTO.setIdAddress(homeAddress.getAdresse().getId());
                addressDTO.setIdAvoirAddress(homeAddress.getAvoirAdresse().getIdAdresseIntUnique());
            }


            if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
                LOG.error("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré");
                throw new PYBadRequestException("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré: " + session.getCurrentThreadTransaction().getErrors().toString());

            } else if (!JadeThread.logIsEmpty()) {
                LOG.error("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré");
                throw new PYBadRequestException("PRTiersHelper#addTiersAddress - Erreur rencontrée lors de la création de l'adresse pour l'assuré: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()).toString());
            }
        } else if (searchTiers.getNbOfResultMatchingQuery() != 1 || mailAddress.getFields() != null) {
            throw new PYInternalException("Une erreur s'est produite pendant la récupération de l'adresse.");
        }
    }


    /**
     * Modification d'une adresse.
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYAddressDTO updateAddress(PYAddressDTO dto, String token) {
        try {
            updateTiersAddress(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification de l'adresse: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification de l'adresse: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification de l'adresse: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }


    private void updateTiersAddress(BSession session, PYAddressDTO pyAddressDTO) throws Exception {
        TITiers existingTiers = AFApplication.retrieveTiers(session, pyAddressDTO.getIdTiers());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#updateTiersAddress - L'idTiers renseigné ne correspond à aucun Tiers.");
        }


        //Special need for CCVS. The domain is not always set to "Default".
        if (pyAddressDTO.getDomainAddress() != null) {
            pyAddressDTO.setDomainAddress(pyAddressDTO.getDomainAddress());
        } else {
            pyAddressDTO.setDomainAddress(String.valueOf(DomaineApplication.STANDARD.getSystemCode()));
        }


        //Check if idAvoirAdresse and idAdresse are linked
        //Retrieve avoirAdresse
        TIAvoirAdresseViewBean currentAvoirAdresse = new TIAvoirAdresseViewBean();
        currentAvoirAdresse.setSession(session);
        currentAvoirAdresse.setIdTiers(pyAddressDTO.getIdTiers());
        currentAvoirAdresse.setIdApplication(pyAddressDTO.getDomainAddress());
        currentAvoirAdresse.setIdAdresseIntUnique(pyAddressDTO.getIdAvoirAddress()); //HEIAAU from AvoirAdresse
        currentAvoirAdresse.retrieve(session.getCurrentThreadTransaction());
        if (currentAvoirAdresse.isNew()) {
            throw new PYBadRequestException("PRTiersHelper#updateTiersAddress - L'adresse à modifier n'existe pas");
        }

        String currentIdAdresse = currentAvoirAdresse.getIdAdresse();

        if (!currentAvoirAdresse.getIdAdresse().equals(pyAddressDTO.getIdAddress())) {
            LOG.error("PRTiersHelper#updateTiersMailAddress - Erreur rencontrée lors de la modification de l'adresse pour l'assuré");
            throw new PYBadRequestException("PRTiersHelper#updateTiersMailAddress - Erreur rencontrée lors de la modification de l'adresse pour l'assuré: les id ne matchent pas");
        }


        //--------------------------------------------------------------------------------
        //In any scenario, create a new address with JSON's content.
        PRTiersWrapper tiers = PRTiersHelper.getTiersById(session, pyAddressDTO.getIdTiers());

        AdresseSimpleModel adresseSimpleModel = new AdresseSimpleModel();
        adresseSimpleModel.setAttention(pyAddressDTO.getAttention());
        adresseSimpleModel.setRue(pyAddressDTO.getStreet());
        adresseSimpleModel.setNumeroRue(pyAddressDTO.getStreetNumber());
        adresseSimpleModel.setLigneAdresse2(pyAddressDTO.getComplementAdresse1());
        adresseSimpleModel.setLigneAdresse3(pyAddressDTO.getComplementAdresse2());


        LocaliteSimpleModel localiteSimpleModel = new LocaliteSimpleModel();
        localiteSimpleModel.setNumPostal(pyAddressDTO.getPostalCode());
        localiteSimpleModel.setLocalite(pyAddressDTO.getLocality());
        localiteSimpleModel.setIdPays(pyAddressDTO.getCountry());

        PersonneEtendueSearchComplexModel searchTiers = new PersonneEtendueSearchComplexModel();
        searchTiers.setForIdTiers(tiers.getIdTiers());
        searchTiers = TIBusinessServiceLocator.getPersonneEtendueService().find(searchTiers);

        AdresseComplexModel adresseComplexModel = null;
        if (searchTiers.getNbOfResultMatchingQuery() == 1) {
            PersonneEtendueComplexModel personneEtendueComplexModel = (PersonneEtendueComplexModel) searchTiers.getSearchResults()[0];

            adresseComplexModel = new AdresseComplexModel();
            adresseComplexModel.setTiers(personneEtendueComplexModel);
            adresseComplexModel.setAdresse(adresseSimpleModel);
            adresseComplexModel.setLocalite(localiteSimpleModel);
        }


        if (!(pyAddressDTO.getModificationDate().equals(JadeDateUtil.getDMYDate(new Date())))) {
            //MAJ
            //If it's a MAJ there will be a new AvoirAdresse.
            AvoirAdresseSimpleModel avoirAdresseSimpleModel = new AvoirAdresseSimpleModel();
            avoirAdresseSimpleModel.setIdTiers(tiers.getIdTiers());
            avoirAdresseSimpleModel.setIdAdresse(adresseSimpleModel.getIdAdresse());
            if (pyAddressDTO.getModificationDate() != null)
                avoirAdresseSimpleModel.setDateDebutRelation(pyAddressDTO.getModificationDate());

            adresseComplexModel.setAvoirAdresse(avoirAdresseSimpleModel);
            AdresseComplexModel acm = TIBusinessServiceLocator.getAdresseService().addAdresse(adresseComplexModel, pyAddressDTO.getDomainAddress(), pyAddressDTO.getTypeAddress(), true);

            pyAddressDTO.setIdAddress(acm.getAdresse().getIdAdresse());
            pyAddressDTO.setIdAvoirAddress(acm.getAvoirAdresse().getIdAdresseIntUnique());

        } else {
            //COR
            BTransaction trans = null;
            AdresseService adresseService = new AdresseServiceImpl();
            AdresseComplexModel acm = adresseService.updateAdresse(adresseComplexModel, pyAddressDTO.getDomainAddress(), pyAddressDTO.getTypeAddress(), false);

            pyAddressDTO.setIdAddress(acm.getAdresse().getIdAdresse());


            //Code inspired from TIActionAdresse#actionAjouter
            trans = (BTransaction) session.newTransaction();
            trans.openTransaction();


            //Retrieve avoirAdresse
            currentAvoirAdresse = new TIAvoirAdresseViewBean();
            currentAvoirAdresse.setSession(session);
            currentAvoirAdresse.setIdTiers(pyAddressDTO.getIdTiers());
            currentAvoirAdresse.setIdApplication(pyAddressDTO.getDomainAddress());
            currentAvoirAdresse.setIdAdresseIntUnique(pyAddressDTO.getIdAvoirAddress()); //HEIAAU from AvoirAdresse
            currentAvoirAdresse.retrieve(session.getCurrentThreadTransaction());
            currentIdAdresse = currentAvoirAdresse.getIdAdresse();


            TIAvoirPaiementManager avPaiementManager = new TIAvoirPaiementManager();
            avPaiementManager.setSession(session);
            avPaiementManager.setForDateFinRelation("0");
            avPaiementManager.setForIdTiers(pyAddressDTO.getIdTiers());
            avPaiementManager.changeManagerSize(0);
            avPaiementManager.find(0);
            if (avPaiementManager.size() > 0) {
                for (int i = 0; i < avPaiementManager.size(); ++i) {
                    TIAvoirPaiement avPmt = (TIAvoirPaiement) avPaiementManager.getEntity(i);
                    String idAdressePmt = avPmt.getIdAdressePaiement();
                    TIAdressePaiement adrPmt = new TIAdressePaiement();
                    adrPmt.setSession(session);
                    adrPmt.setIdAdressePmtUnique(idAdressePmt);
                    adrPmt.retrieve();
                    if (pyAddressDTO.getIdAddress().equals(adrPmt.getIdAdresse())) { //Si cette adresse de paiement est liée à l'adresse qu'on veut MAJ
                        boolean droitDomaine = TICodeSystemRightChecker.check(session, "PYAPPLICAT", "avoirPaiement", avPmt.getIdApplication(), avPmt.getIdApplication(), 1);
                        if (droitDomaine) {
                            adrPmt.setIdAdressePmtUnique("");
                            adrPmt.setIdAdressePaiement("");
                            adrPmt.setIdAdresse(currentIdAdresse);
                            adrPmt.setAllowModification(true);
                            adrPmt.add(trans);
                            avPmt.setIdAdressePaiement(adrPmt.getIdAdressePmtUnique());
                            avPmt.update(trans);
                        }
                    }
                }
            }
            if (trans.hasErrors()) {
                trans.rollback();
            } else {
                trans.commit();
            }
            trans.closeTransaction();

        }
        if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
            LOG.error("PRTiersHelper#updateTiersMailAddress - Erreur rencontrée lors de la modification de l'adresse pour l'assuré");
            throw new PYBadRequestException("PRTiersHelper#updateTiersMailAddress - Erreur rencontrée lors de la modification de l'adresse pour l'assuré: " + session.getCurrentThreadTransaction().getErrors().toString());

        } else if (!JadeThread.logIsEmpty()) {
            LOG.error("PRTiersHelper#updateTiersMailAddress - Erreur rencontrée lors de la modification de l'adresse pour l'assuré");
            throw new PYBadRequestException("PRTiersHelper#updateTiersMailAddress - Erreur rencontrée lors de la modification de l'adresse pour l'assuré: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()));
        }
    }

    /**
     * Suppression d'une adresse.
     *
     * @param dto
     * @param token
     * @return
     */
    public final String deleteAddress(PYAddressDTO dto, String token) {
        try {
            deleteAddress(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la suppression de l'adresse: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression de l'adresse: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la suppression de l'adresse: " + e);
            throw new PYInternalException(e);
        }
        return "Deletion successful";
    }

    /**
     * Méthode pour supprimer une adresse.
     *
     * @param session
     * @param pyAddressDTO
     * @throws Exception
     */
    private void deleteAddress(BSession session, PYAddressDTO pyAddressDTO) throws Exception {
        //TODO delete address
        TIAvoirAdresseViewBean currentAvoirAdresse = new TIAvoirAdresseViewBean();
        currentAvoirAdresse.setSession(session);
//        currentAvoirAdresse.setIdTiers(pyAddressDTO.getIdTiers());
//        currentAvoirAdresse.setIdApplication(pyAddressDTO.getDomainAddress());
        currentAvoirAdresse.setIdAdresseIntUnique(pyAddressDTO.getIdAvoirAddress()); //HEIAAU from AvoirAdresse
        currentAvoirAdresse.retrieve(session.getCurrentThreadTransaction());
        currentAvoirAdresse.delete(session.getCurrentThreadTransaction());
    }

    /**
     * Création d'une adresse de paiement.
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYPaymentAddressDTO createPaymentAddress(PYPaymentAddressDTO dto, String token) {
        try {
            addTiersPaymentAddress(getSession(), true, dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création de l'adresse de paiement: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création de l'adresse de paiement: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création de l'adresse de paiement: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour les web services CCB/CCVS afin d'ajouter un tiers (adresse de paiement)
     *
     * @param session
     * @param idAddress
     * @param modificationDate
     * @param withAvoirPaymentAddress
     * @param dto
     * @throws Exception
     */
    private static List<TIAdressePaiement> addTiersPaymentAddresses(BSession session, String idAddress, String modificationDate, boolean withAvoirPaymentAddress, PYTiersDTO dto) throws Exception {
        //TODO Vérifier l'existence d'une adresse en DB afin de créer une adresse de paiement.
        // Si adresse courrier existante, celle-ci est utilisée pour lier l'adresse de paiement.
        // Sinon on prend l'adresse de domicile, sinon, la création d'une adresse de paiement n'est pas possible.


        TITiers existingTiers = AFApplication.retrieveTiers(session, dto.getId());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#addTiersPaymentAddresses - L'id renseigné ne correspond à aucun Tiers.");
        }

        List<TIAdressePaiement> adressePaiementList = new ArrayList<>();

        TIAdressePaiement adressePaiement = null;

        //Using a Vector for adding multiple payment addresses
        for (PYPaymentAddressDTO pyPaymentAddressDTO : dto.getPaymentAddress()) {
            if (pyPaymentAddressDTO.getIdAddressRelatedToPaymentAddress() != null)
                idAddress = pyPaymentAddressDTO.getIdAddressRelatedToPaymentAddress();


            TIIbanFormater ibanFormatter = new TIIbanFormater();

            adressePaiement = new TIAdressePaiement();
            adressePaiement.setIdTiersAdresse(dto.getId());
            adressePaiement.setIdAdresse(idAddress);

            //Special need for CCVS. The domain is not always set to "Default".
            pyPaymentAddressDTO.setDomainPaymentAddress(setDomainPaymentAddress(pyPaymentAddressDTO));

            //Renseigner soit N°Compte soit N°CCP
            if (!(Objects.isNull(pyPaymentAddressDTO.getAccountNumber()))) {
                String iban = ibanFormatter.unformat(pyPaymentAddressDTO.getAccountNumber());
                if (checkIban(iban)) {
                    adressePaiement.setIdTiersBanque(retrieveBankId(pyPaymentAddressDTO.getClearingNumber(), pyPaymentAddressDTO.getBranchOfficePostalCode()));
                    adressePaiement.setNumCompteBancaire(pyPaymentAddressDTO.getAccountNumber());
                } else {
                    LOG.error("Paiement adresse non créée : IBAN non valide : " + iban);
                }
            } else {
                adressePaiement.setNumCcp(pyPaymentAddressDTO.getCcpNumber());
            }
            adressePaiement.setIdPays(pyPaymentAddressDTO.getBankCountry());
            adressePaiement.setSession(session);
            if (modificationDate != null)
                adressePaiement.setDateDebutPaiement(modificationDate);
            adressePaiement.add();

            if (withAvoirPaymentAddress) {
                TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
                avoirPaiement.setIdApplication(pyPaymentAddressDTO.getDomainPaymentAddress());
                avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
                avoirPaiement.setIdTiers(dto.getId());
                avoirPaiement.setSession(session);
                avoirPaiement.add();
            }

            //set les champs pour les afficher dans la réponse de la requête.
            pyPaymentAddressDTO.setIdTiers(adressePaiement.getIdTiersAdresse());
            pyPaymentAddressDTO.setIdPaymentAddress(adressePaiement.getIdAdressePaiement());
            pyPaymentAddressDTO.setIdAddressRelatedToPaymentAddress((adressePaiement.getIdAdresse()));

            //Add payment address into list
            adressePaiementList.add(adressePaiement);


            if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
                LOG.error("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré");
                throw new PYBadRequestException("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré: " + session.getCurrentThreadTransaction().getErrors().toString());

            } else if (!JadeThread.logIsEmpty()) {
                LOG.error("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré");
                throw new PYBadRequestException("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()).toString());
            }
        }

        return adressePaiementList;
    }

    private static PYPaymentAddressDTO addTiersPaymentAddress(BSession session, boolean withAvoirPaymentAddress, PYPaymentAddressDTO pyPaymentAddressDTO) throws Exception {
        TITiers existingTiers = AFApplication.retrieveTiers(session, pyPaymentAddressDTO.getIdTiers());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#addTiersPaymentAddress - L'idTiers renseigné ne correspond à aucun Tiers.");
        }

        TIAdressePaiement adressePaiement = null;


        TIIbanFormater ibanFormatter = new TIIbanFormater();

        adressePaiement = new TIAdressePaiement();
        adressePaiement.setIdTiersAdresse(pyPaymentAddressDTO.getIdTiers());
        adressePaiement.setIdAdresse(pyPaymentAddressDTO.getIdAddressRelatedToPaymentAddress());

        //Special need for CCVS. The domain is not always set to "Default".
        pyPaymentAddressDTO.setDomainPaymentAddress(setDomainPaymentAddress(pyPaymentAddressDTO));

        //Renseigner soit N°Compte soit N°CCP
        if (!(Objects.isNull(pyPaymentAddressDTO.getAccountNumber()))) {
            String iban = ibanFormatter.unformat(pyPaymentAddressDTO.getAccountNumber());
            if (checkIban(iban)) {
                adressePaiement.setIdTiersBanque(retrieveBankId(pyPaymentAddressDTO.getClearingNumber(), pyPaymentAddressDTO.getBranchOfficePostalCode()));
                adressePaiement.setNumCompteBancaire(pyPaymentAddressDTO.getAccountNumber());
            } else {
                LOG.error("Paiement adresse non créée : IBAN non valide : " + iban);
            }
        } else {
            adressePaiement.setNumCcp(pyPaymentAddressDTO.getCcpNumber());
        }
        adressePaiement.setIdPays(pyPaymentAddressDTO.getBankCountry());
        adressePaiement.setSession(session);
        adressePaiement.add();
        if (withAvoirPaymentAddress) {
            TIAvoirPaiement avoirPaiement = new TIAvoirPaiement();
            avoirPaiement.setIdApplication(pyPaymentAddressDTO.getDomainPaymentAddress());
            avoirPaiement.setIdAdressePaiement(adressePaiement.getIdAdressePaiement());
            avoirPaiement.setIdTiers(pyPaymentAddressDTO.getIdTiers());
            avoirPaiement.setSession(session);
            avoirPaiement.add();
        }

        //set les champs pour les afficher dans la réponse de la requête.
        pyPaymentAddressDTO.setIdPaymentAddress(adressePaiement.getIdAdressePaiement());
        pyPaymentAddressDTO.setIdAddressRelatedToPaymentAddress((adressePaiement.getIdAdresse()));


        if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
            LOG.error("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré");
            throw new PYBadRequestException("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré: " + session.getCurrentThreadTransaction().getErrors().toString());

        } else if (!JadeThread.logIsEmpty()) {
            LOG.error("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré");
            throw new PYBadRequestException("PRTiersHelper#addTiersPaymentAddress - Erreur rencontrée lors de la création de l'adresse de paiement pour l'assuré: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()));
        }

        return pyPaymentAddressDTO;
    }

    /**
     * Méthode permettant d'aller chercher chez quelle banque un compte se trouve, en fonction de l'IBAN du compte et du NPA de la banque
     *
     * @param clearingNumber
     * @param npa
     * @return l'id de la première banque trouvée
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private static String retrieveBankId(String clearingNumber, String npa) throws JadeApplicationException, JadePersistenceException {
        BanqueComplexModel banque = new BanqueComplexModel();

        BanqueSearchComplexModel banqueSearchModel = new BanqueSearchComplexModel();
        banqueSearchModel.setForNpaLike(npa);
        banqueSearchModel.setForClearing(clearingNumber);
        banqueSearchModel.setDefinedSearchSize(1);
        banqueSearchModel = TIBusinessServiceLocator.getBanqueService().find(banqueSearchModel);

        if (banqueSearchModel.getSize() == 1) {
            banque = (BanqueComplexModel) banqueSearchModel.getSearchResults()[0];
        }
        return banque.getTiersBanque().getId();
    }

    /**
     * Méthode pour vérifier si l'IBAN est valide
     *
     * @param chIban
     * @return true si chIban est valide, une erreur sinon
     */
    private static boolean checkIban(String chIban) {
        TIIbanFormater ibanFormatter = new TIIbanFormater();
        chIban = ibanFormatter.format(chIban);

        try {
            ibanFormatter.check(chIban);
        } catch (Exception e) {
            throw new PYBadRequestException("Erreur lors du traitement du numéro de compte: " + e);
        }
        return true;
    }

    /**
     * Méthode pour définir le domaine d'une adresse de paiement en fonction de la présence ou non du champ dans le JSON
     *
     * @param pyPaymentAddressDTO
     * @return
     */
    public static final String setDomainPaymentAddress(PYPaymentAddressDTO pyPaymentAddressDTO) {
        if (pyPaymentAddressDTO.getDomainPaymentAddress() != null) {
            pyPaymentAddressDTO.setDomainPaymentAddress(pyPaymentAddressDTO.getDomainPaymentAddress());
        } else {
            pyPaymentAddressDTO.setDomainPaymentAddress(String.valueOf(DomaineApplication.STANDARD.getSystemCode()));
        }
        return pyPaymentAddressDTO.getDomainPaymentAddress();
    }

    /**
     * Modification d'une adresse de paiement.
     *
     * @param dto
     * @param token
     * @return
     */
    public final PYPaymentAddressDTO updatePaymentAddress(PYPaymentAddressDTO dto, String token) {
        try {
            updateTiersPaymentAddress(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification de l'adresse: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification de l'adresse: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification de l'adresse: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour mettre à jour une adresse de paiement
     *
     * @param session
     * @param pyPaymentAddressDTO
     * @return
     * @throws Exception
     */
    public static final void updateTiersPaymentAddress(BSession session, PYPaymentAddressDTO pyPaymentAddressDTO) throws Exception {
        TITiers existingTiers = AFApplication.retrieveTiers(session, pyPaymentAddressDTO.getIdTiers());

        if (existingTiers.isNew()) {
            throw new AFBadRequestException("PYExecuteService#updateTiersPaymentAddress - L'idTiers renseigné ne correspond à aucun Tiers.");
        }

        //Special need for CCVS. The domain is not always set to "Default".
        pyPaymentAddressDTO.setDomainPaymentAddress(setDomainPaymentAddress(pyPaymentAddressDTO));

        //Si le tiers n'a aucune adresse de paiement pour ce domaine, on en crée une nouvelle + AvoirPaiement. (create smth from REST update..)
        if (Objects.isNull(TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(pyPaymentAddressDTO.getIdTiers(), false, pyPaymentAddressDTO.getDomainPaymentAddress(), JadeDateUtil.getGlobazFormattedDate(new Date()), "").getFields())) {
            addTiersPaymentAddress(session, true, pyPaymentAddressDTO);
        } else { //Une adresse de paiement est existante pour ce domaine, on peut faire une MAJ ou une COR
            //Utiliser l'idAddress pour créer une nouvelle adresse de paiement
            String idAddress = null;
            //Si on a renseigné le champ idAddressRelatedToPaymentAddress on l'utilise.
            if (pyPaymentAddressDTO.getIdAddressRelatedToPaymentAddress() != null)
                idAddress = pyPaymentAddressDTO.getIdAddressRelatedToPaymentAddress();
                //Sinon, on récupère l'idAddress (de courrier si existante, sinon domicile, sinon throw)
            else {
                if (null != TIBusinessServiceLocator.getAdresseService().getAdresseTiers(pyPaymentAddressDTO.getIdTiers(), false, JadeDateUtil.getGlobazFormattedDate(new Date()), "", PRTiersHelper.CS_ADRESSE_COURRIER, "").getFields())
                    idAddress = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(pyPaymentAddressDTO.getIdTiers(), false, JadeDateUtil.getGlobazFormattedDate(new Date()), "", PRTiersHelper.CS_ADRESSE_COURRIER, "").getFields().get("id_adresse");
                else if (null != TIBusinessServiceLocator.getAdresseService().getAdresseTiers(pyPaymentAddressDTO.getIdTiers(), false, JadeDateUtil.getGlobazFormattedDate(new Date()), "", PRTiersHelper.CS_ADRESSE_DOMICILE, "").getFields())
                    idAddress = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(pyPaymentAddressDTO.getIdTiers(), false, JadeDateUtil.getGlobazFormattedDate(new Date()), "", PRTiersHelper.CS_ADRESSE_DOMICILE, "").getFields().get("id_adresse");
                else {
                    LOG.error("PRTiersHelper#updateTiersPaymentAddress - Erreur rencontrée lors de la màj d'une adresse de paiement pour l'assuré. Aucune adresse pour ce tiers.");
                    throw new NullPointerException("Aucune adresse pour ce tiers.");
                }
            }

            //Dans tous les cas (MAJ ou COR), on crée une nouvelle adresse de paiement, mais sans AvoirAdressePaiement.
            PYPaymentAddressDTO adressePaiement = addTiersPaymentAddress(session, false, pyPaymentAddressDTO);


            if (!(pyPaymentAddressDTO.getModificationDate().equals(JadeDateUtil.getDMYDate(new Date())))) {
                //MAJ
                //Nouvelle ligne DB dans AvoirPaiement
                TIAvoirPaiement tiAvoirPaiement = new TIAvoirPaiement();
                tiAvoirPaiement.setIdTiers(pyPaymentAddressDTO.getIdTiers());
                tiAvoirPaiement.setIdApplication(pyPaymentAddressDTO.getDomainPaymentAddress());
                tiAvoirPaiement.setIdAdressePaiement(adressePaiement.getIdPaymentAddress()); //Pointer sur une adresse de paiement existante. (celle créée juste au-dessus)
                tiAvoirPaiement.setDateDebutRelation(pyPaymentAddressDTO.getModificationDate());
                tiAvoirPaiement.add();
            } else {
                //COR (possible uniquement pour le jour même)
                //Update colonne HIIAPA de la table TIAPAIP
                BITransaction trans = null;
                try {
                    //Récupérer l'idAvoirPaiementUnique (HCIAIU)
                    String idAvoirPaiementUnique = null;
                    //TODO null pointer si le tiers n'a pas d'adresse de paiement
                    //Comme une seule adresse de paiement est possible par domaine, on aura qu'un seul idAvoirPaiementUnique.
                    if (null != TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(pyPaymentAddressDTO.getIdTiers(), false, pyPaymentAddressDTO.getDomainPaymentAddress(), JadeDateUtil.getGlobazFormattedDate(new Date()), "").getFields())
                        idAvoirPaiementUnique = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(pyPaymentAddressDTO.getIdTiers(), false, pyPaymentAddressDTO.getDomainPaymentAddress(), JadeDateUtil.getGlobazFormattedDate(new Date()), "").getFields().get("id_avoir_paiement_unique");
                    else {
                        LOG.error("PRTiersHelper#updateTiersPaymentAddress - Erreur rencontrée lors de la màj d'une adresse de paiement pour l'assuré. Aucune adresse de paiement pour ce tiers.");
                        throw new NullPointerException("Aucune adresse de paiement pour ce tiers.");
                    }

                    //Code from TIActionAdressePaiement#actionAjouter
                    TIAvoirPaiement currentAvoirPaiement = new TIAvoirPaiement();
                    currentAvoirPaiement.setISession(session);
                    currentAvoirPaiement.setIdAdrPmtIntUnique(idAvoirPaiementUnique);
                    currentAvoirPaiement.retrieve();


                    trans = session.newTransaction();
                    trans.openTransaction();

                    currentAvoirPaiement.setIdAdressePaiement(adressePaiement.getIdPaymentAddress());
                    currentAvoirPaiement.update(trans);

                    if (trans.hasErrors()) {
                        trans.rollback();
                        LOG.error(trans.getErrors().toString());
                    } else {
                        trans.commit();
                    }
                } catch (NullPointerException nullPointerException) {
                    throw new PYBadRequestException("Aucune adresse de paiement pour ce tiers.");
                } finally {
                    if (trans != null) {
                        try {
                            trans.closeTransaction();
                        } catch (Exception var25) {
                            LOG.error(trans.getErrors().toString());
                        }
                    }
                }
            }

            if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
                LOG.error("PRTiersHelper#updateTiersPaymentAddress - Erreur rencontrée lors de la màj d'une adresse de paiement pour l'assuré");
                throw new PYBadRequestException("PRTiersHelper#updateTiersPaymentAddress - Erreur rencontrée lors de la màj d'une adresse de paiement pour l'assuré: " + session.getCurrentThreadTransaction().getErrors().toString());

            } else if (!JadeThread.logIsEmpty()) {
                LOG.error("PRTiersHelper#updateTiersPaymentAddress - Erreur rencontrée lors de la màj d'une adresse de paiement pour l'assuré");
                throw new PYBadRequestException("PRTiersHelper#updateTiersPaymentAddress - Erreur rencontrée lors de la màj d'une adresse de paiement pour l'assuré: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()));
            }
        }
    }

    /**
     * Suppression d'une adresse de paiement.
     *
     * @param dto
     * @param token
     * @return
     */
    public final String deletePaymentAddress(PYPaymentAddressDTO dto, String token) {
        try {
            deletePaymentAddress(getSession(), dto);
        } catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la suppression de l'adresse de paiement: " + e);
            throw e;
        } catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression de l'adresse de paiement: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la suppression de l'adresse de paiement: " + e);
            throw new PYInternalException(e);
        }
        return "Deletion successful";
    }

    /**
     * Méthode pour supprimer une adresse de paiement.
     *
     * @param session
     * @param pyAddressDTO
     * @throws Exception
     */
    private void deletePaymentAddress(BSession session, PYPaymentAddressDTO pyAddressDTO) throws Exception {
        TIAvoirPaiement tiAvoirPaiement = new TIAvoirPaiement();
        tiAvoirPaiement.setIdAdrPmtIntUnique(pyAddressDTO.getIdPaymentAddress());
        tiAvoirPaiement.retrieve();
        tiAvoirPaiement.delete();
    }


    /**
     * Vérifie si JadeThread log et la transaction ont des erreurs, et lance une exception si besoin.
     *
     * @param session
     * @param message
     * @throws Exception
     */
    private void checkForErrorsAndThrow(BSession session, String message) throws Exception {
        if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
            LOG.error(message);
            throw new PYBadRequestException(message + ": " + session.getCurrentThreadTransaction().getErrors().toString());
        } else if (!JadeThread.logIsEmpty()) {
            LOG.error(message);
            throw new PYBadRequestException(message + ": " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()));
        }
    }

    /**
     *
     */
    public final PYConjointDTO createConjoint(PYConjointDTO dto, String token) {
        PYValidateDTO.checkIfExist(dto);
        dto.setTypeLien(TICompositionTiers.CS_CONJOINT);
        dto = (PYConjointDTO) createLienEntreTiers(dto,token);
        return dto;
    }

    public PYLienEntreTiersDTO createLienEntreTiers(PYLienEntreTiersDTO dto, String token) {
        TICompositionTiers composition = new TICompositionTiers();
        composition.setSession(getSession());
        composition.setIdTiersParent(dto.getIdTiersPrincipal());
        setValuesLienEntreTiers(dto, composition);
        try {
            composition.add();
            if(dto instanceof PYConjointDTO && !JadeStringUtil.isBlankOrZero( ((PYConjointDTO) dto).getEtatCivil())){
                updateEtatCivil((PYConjointDTO) dto);
            }

        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new PYInternalException(e + ": " + getSession().getCurrentThreadTransaction().getErrors().toString());
        }
        dto.setIdComposition(composition.getIdComposition());
        return dto;
    }



    public final PYConjointDTO updateConjoint(PYConjointDTO dto, String token) {
        dto.setTypeLien(TICompositionTiers.CS_CONJOINT);
        updateLienEntreTiers(dto,token);
        return dto;
    }

    public final PYLienEntreTiersDTO updateLienEntreTiers(PYLienEntreTiersDTO dto,String token) {
        TICompositionTiers composition = new TICompositionTiers();
        composition.setSession(getSession());
        composition.setId(dto.getIdComposition());
        try {
            composition.retrieve();
            if (!composition.isNew()) {
                setValuesLienEntreTiers(dto, composition);
                composition.update();
                if(dto instanceof PYConjointDTO && !JadeStringUtil.isBlankOrZero( ((PYConjointDTO) dto).getEtatCivil())){
                    updateEtatCivil((PYConjointDTO) dto);
                }
            } else {
                LOG.error("Tiers non trouvé");
                throw new PYInternalException("Tiers non trouvé" + getSession().getCurrentThreadTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new PYInternalException(e + ": " + getSession().getCurrentThreadTransaction().getErrors().toString());
        }
        return dto;
    }
    public String deleteConjoint(PYLienEntreTiersDTO dto, String token) {
        deleteLienEntreTiers(dto,token);
        return "Deletion successful";
    }
    public String deleteLienEntreTiers(PYLienEntreTiersDTO dto, String token) {
        TICompositionTiers composition = new TICompositionTiers();
        composition.setSession(getSession());
        composition.setId(dto.getIdComposition());
        try {
            composition.retrieve();
            if (!composition.isNew()) {
                composition.delete();
            } else {
                LOG.error("Tiers non trouvé");
                throw new PYInternalException("Tiers non trouvé" + getSession().getCurrentThreadTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new PYInternalException(e + ": " + getSession().getCurrentThreadTransaction().getErrors().toString());
        }
        return "Deletion successful";
    }
    private static void setValuesLienEntreTiers(PYLienEntreTiersDTO dto, TICompositionTiers composition) {
        composition.setIdTiersEnfant(dto.getIdTiersSecondaire());
        composition.setTypeLien(!JadeStringUtil.isBlankOrZero(dto.getTypeLien() ) ? dto.getTypeLien() : TICompositionTiers.CS_CONJOINT);
        composition.setDebutRelation(!JadeStringUtil.isBlankOrZero(dto.getDebutRelation()) ? dto.getDebutRelation() : "");
        composition.setFinRelation(!JadeStringUtil.isBlankOrZero(dto.getFinRelation()) ? dto.getFinRelation() : "");
    }

    private void updateEtatCivil(PYConjointDTO dto) throws Exception {
        TITiersViewBean t1 = new TITiersViewBean();
        t1.setSession(getSession());
        t1.setIdTiers(dto.getIdTiersPrincipal());
        t1.retrieve();
        t1.setEtatCivil(dto.getEtatCivil());
        t1.update();

        TITiersViewBean t2 = new TITiersViewBean();
        t2.setSession(getSession());
        t2.setIdTiers(dto.getIdTiersSecondaire());
        t2.retrieve();
        t2.setEtatCivil(dto.getEtatCivil());
        t2.update();
    }
    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        return false;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }


}

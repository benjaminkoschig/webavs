package globaz.pyxis.web.service;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.pyxis.db.tiers.TIAvoirContact;
import globaz.pyxis.db.tiers.TIContact;
import globaz.pyxis.db.tiers.TIMoyenCommunication;
import globaz.pyxis.web.DTO.PYContactCreateDTO;
import globaz.pyxis.web.DTO.PYContactDTO;
import globaz.pyxis.web.DTO.PYMeanOfCommunicationCreationDTO;
import globaz.pyxis.web.DTO.PYMeanOfCommunicationDTO;
import globaz.pyxis.web.DTO.PYTiersDTO;
import globaz.pyxis.web.DTO.PYTiersUpdateDTO;
import globaz.pyxis.web.exceptions.PYBadRequestException;
import globaz.pyxis.web.exceptions.PYInternalException;
import lombok.extern.slf4j.Slf4j;

import java.util.Vector;

@Slf4j
public class PYExecuteService extends BProcess {
    /**
     * Création de tiers
     *
     * @param dto JSON mappé en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers créé
     */
    public final PYTiersDTO createTiers(PYTiersDTO dto, String token) {
        String idAddress = null;
        try {
            PRTiersHelper.addTiersPage1(getSession(), dto);

            // Only add a mail address if the DTO contains an address
            if (dto.getAddresses().size() != 0) {
                idAddress = PRTiersHelper.addTiersAddress(getSession(), dto);
            }
            // TODO: This is kinda wrong, we probably shouldn't be relying on mail address creation for payment address creation. Better check for payment info's fields.
            //  NO, the link between payment address and domicile/courrier address is mandatory. Define which address should be linked.
            //  So, check for address AND payment infos
            if (idAddress != null) {
                PRTiersHelper.addTiersPaymentAddress(getSession(), idAddress, null, true, dto);
            }
            addTiersPage2(getSession(), dto);
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du tiers: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du tiers: " + e);
            throw e;
        }
        catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création du tiers: " + e);
            throw new PYInternalException(e);
        }

        return dto;
    }

    /**
     * Modification de tiers
     *
     * @param dto JSON mappé en objet qui contient des informations sur les tiers
     * @param token header d'authentification
     * @return dto JSON contenant l'id du tiers créé et la date de mise à jour
     */
    public final PYTiersDTO updateTiers(PYTiersUpdateDTO dto, String token) {
        // TODO: upgrade the updating. If at least one field is present then update. Faire un vecteur de champs pour les update comme pour la création et isValid.
        try {
            PRTiersHelper.updateTiersPage1(getSession(), dto);
            if (!dto.getPaymentAddress().isEmpty())
                PRTiersHelper.updateTiersPaymentAddress(getSession(), dto);
            if (!dto.getContacts().isEmpty())
                updateTiersPage2(getSession(), dto);
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification du tiers: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du tiers: " + e);
            throw e;
        }
        catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification du tiers: " + e);
            throw new PYInternalException(e);
        }

        return dto;
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
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification du contact: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du contact: " + e);
            throw e;
        }
        catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification du contact: " + e);
            throw new PYInternalException(e);
        }
        return dto;
    }

    /**
     * Méthode pour mettre à jour un contact (table TICONTP).
     *
     * @param session
     * @param idContact clé primaire
     * @param newLastName valeur à mettre à jour
     * @param newFirstName valeur à mettre à jour
     * @throws Exception
     */
    public final void updateContact(BSession session, String idContact, String newLastName, String newFirstName) throws Exception {
        TIContact tiContact = new TIContact();
        tiContact.setIdContact(idContact);
        tiContact.retrieve(session.getCurrentThreadTransaction());

        if(newLastName != null && !newLastName.isEmpty())
            tiContact.setNom(newLastName);
        if(newFirstName != null && !newFirstName.isEmpty())
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
    public final PYMeanOfCommunicationCreationDTO updateMeanOfCommunication(PYMeanOfCommunicationCreationDTO dto, String token) {
        try {
            updateMeanOfCommunication(getSession(), dto.getIdContact(), dto.getMeanOfCommunicationType(), dto.getApplicationDomain(), dto.getMeanOfCommunicationValue());
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la modification du moyen de communication: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification du moyen de communication: " + e);
            throw e;
        }
        catch (Exception e) {
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
     * @param idContact clé composite
     * @param typeCommunication clé composite
     * @param domaineApplication clé composite
     * @param newMoyen valeur à mettre à jour
     * @throws Exception
     */
    public final void updateMeanOfCommunication(BSession session, String idContact, String typeCommunication, String domaineApplication, String newMoyen) throws Exception {
        // Get the contact
        TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
        tiMoyenCommunication.setIdApplication(domaineApplication);
        tiMoyenCommunication.setIdContact(idContact);
        tiMoyenCommunication.setTypeCommunication(typeCommunication);
        tiMoyenCommunication.retrieve(session.getCurrentThreadTransaction());

        // Update the contact
        if(!newMoyen.isEmpty() && newMoyen != null)
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
            for (PYMeanOfCommunicationDTO mean: dto.getMeansOfCommunication()) {
                createMeanOfCommunication(getSession(), dto.getId(), mean.getApplicationDomain(), mean.getMeanOfCommunicationType(), mean.getMeanOfCommunicationValue());
            }
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du contact: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du contact: " + e);
            throw e;
        }
        catch (Exception e) {
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
    public final String createContact(BSession session, String idTiers, String lastName, String firstName) throws Exception {
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
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la suppression du contact: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression du contact: " + e);
            throw e;
        }
        catch (Exception e) {
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
    public final void deleteContact(BSession session, String id, String idTiers) throws Exception {
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
    public final PYMeanOfCommunicationCreationDTO createMeanOfCommunication(PYMeanOfCommunicationCreationDTO dto, String token) {
        try {
            createMeanOfCommunication(getSession(), dto);
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création du moyen de communication: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création du moyen de communication: " + e);
            throw e;
        }
        catch (Exception e) {
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
    public final void createMeanOfCommunication(BSession session, PYMeanOfCommunicationCreationDTO dto) throws Exception {
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
    public final void createMeanOfCommunication(BSession session, String idContact, String application, String type, String value) throws Exception {
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
    public final String deleteMeanOfCommunication(PYMeanOfCommunicationCreationDTO dto, String token) {
        try {
            deleteMeanOfCommunication(getSession(), dto);
        }
        catch (PYBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la suppression du moyen de communication: " + e);
            throw e;
        }
        catch (PYInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression du moyen de communication: " + e);
            throw e;
        }
        catch (Exception e) {
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
    public final void deleteMeanOfCommunication(BSession session, PYMeanOfCommunicationCreationDTO dto) throws Exception {
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
    public final void addTiersPage2(BSession session, PYTiersDTO dto) throws Exception {
        for (PYContactDTO contactDTO: dto.getContacts()) {
            TIContact contact = new TIContact();
            contact.setSession(session);
            contact.setNom(dto.getSurname());
            contact.setPrenom(dto.getName());
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

        if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
            LOG.error("PRTiersHelper#addTiersPage2 - Erreur rencontrée lors de la création de contact");
            throw new PYBadRequestException("PRTiersHelper#addTiersPage2 - Erreur rencontrée lors de la création de contact: " + session.getCurrentThreadTransaction().getErrors().toString());
        } else if (!JadeThread.logIsEmpty()) {
            LOG.error("PRTiersHelper#addTiersPage2 - Erreur rencontrée lors de la création de contact");
            throw new PYBadRequestException("PRTiersHelper#addTiersPage2 - Erreur rencontrée lors de la création de contact: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()).toString());
        }
    }

    /**
     * Méthode pour mettre à jour un tiers (page 2 - contacts/moyens de communication)
     *
     * @param session
     * @param dto
     * @return
     * @throws Exception
     */
    public final void updateTiersPage2(BSession session, PYTiersDTO dto) throws Exception {
        for (PYContactDTO contact: dto.getContacts()) {
            TIContact tiContact = new TIContact();
            tiContact.setIdContact(contact.getId());
            tiContact.retrieve(session.getCurrentThreadTransaction());
            if (tiContact.getId().isEmpty()) {
                throw new PYBadRequestException("PRTiersHelper#updateTiersPage2 - Le contact à modifier n'existe pas");
            }

            updateContact(session, contact.getId(), contact.getLastName(), contact.getFirstName());

            Vector<PYMeanOfCommunicationDTO> meansOfCommunication = contact.getMeansOfCommunication();
            for (PYMeanOfCommunicationDTO mean: meansOfCommunication) {
                // Check that mean's id belongs to the contact (i.e. check that a Contact/TICONTP with HLICON same as MoyenDeCommunication/TIMCOMP exists)
                TIMoyenCommunication tiMoyenCommunication = new TIMoyenCommunication();
                tiMoyenCommunication.setIdApplication(mean.getApplicationDomain());
                tiMoyenCommunication.setIdContact(contact.getId());
                tiMoyenCommunication.setTypeCommunication(mean.getMeanOfCommunicationType());
                tiMoyenCommunication.retrieve(session.getCurrentThreadTransaction());
                if (tiMoyenCommunication.getId().isEmpty()) {
                    throw new PYBadRequestException("PRTiersHelper#updateTiersPage2 - Le moyen de communication à modifier n'existe pas");
                }

                updateMeanOfCommunication(session, contact.getId(), mean.getMeanOfCommunicationType(), mean.getApplicationDomain(), mean.getMeanOfCommunicationValue());
            }
        }

        if (!JadeStringUtil.isEmpty(String.valueOf(session.getCurrentThreadTransaction().getErrors()))) {
            LOG.error("PRTiersHelper#updateTiersPage2 - Erreur rencontrée lors de l'update de contact");
            throw new PYBadRequestException("PRTiersHelper#updateTiersPage2 - Erreur rencontrée lors de l'update de contact: " + session.getCurrentThreadTransaction().getErrors().toString());
        } else if (!JadeThread.logIsEmpty()) {
            LOG.error("PRTiersHelper#updateTiersPage2 - Erreur rencontrée lors de l'update de contact");
            throw new PYBadRequestException("PRTiersHelper#updateTiersPage2 - Erreur rencontrée lors de l'update de contact: " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()).toString());
        }
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

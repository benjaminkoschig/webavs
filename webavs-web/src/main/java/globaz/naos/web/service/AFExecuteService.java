package globaz.naos.web.service;

import globaz.globall.db.*;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.web.DTO.AFAffiliationDTO;
import globaz.naos.web.exceptions.AFBadRequestException;
import globaz.naos.web.exceptions.AFInternalException;
import globaz.pyxis.db.tiers.TITiers;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;

@Slf4j
public class AFExecuteService extends BProcess {
    /**
     * Création d'affiliation
     *
     * @param dto   JSON mappé en objet qui contient des informations sur l'affiliation
     * @param token header d'authentification
     * @return dto JSON contenant l'id de l'affiliation créé
     */
    public final AFAffiliationDTO createAffiliation(AFAffiliationDTO dto, String token) {
        try {
            createAffiliation(getSession(), dto);
        } catch (AFBadRequestException e) {
            LOG.error("Une erreur de paramètre est survenue lors de la création de l'affiliation: " + e);
            throw e;
        } catch (AFInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la création de l'affiliation: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la création de l'affiliation: " + e);
            throw new AFInternalException(e);
        }

        return dto;
    }

    /**
     * Méthode pour les web services CCB/CCVS/VFE afin d'ajouter une une affiliation
     *
     * @param session
     * @param dto
     * @return dto
     * @throws Exception
     */

    public final AFAffiliationDTO createAffiliation(BSession session, AFAffiliationDTO dto) throws Exception {
        TITiers tiers = AFApplication.retrieveTiers(session, dto.getIdTiers());

        if (tiers.isNew()) {
            throw new AFBadRequestException("AFExecuteService#updateAffiliationPage1 - L'idTiers renseigné ne correspond à aucun Tiers.");
        }


        BTransaction transaction = new BTransaction(session);
        try {
            transaction.openTransaction();

            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(session);
            affiliation.setIdTiers(dto.getIdTiers());
            affiliation.setRaisonSociale(tiers.getDesignation1() + " " + tiers.getDesignation2());
            affiliation.setAffilieNumero(dto.getNumeroAffilie());
            affiliation.setAncienAffilieNumero(dto.getAncien_numero_affilie());
            affiliation.setDateCreation(JadeDateUtil.getGlobazFormattedDate(new Date())); //Today
            affiliation.setDateDebut(dto.getDateDebutAffiliation());
            if (dto.getDateFinAffiliation() != null) {
                affiliation.setDateFin(dto.getDateFinAffiliation());
                affiliation.setMotifFin(dto.getMotifFin());
            }
            affiliation.setTypeAffiliation(dto.getGenreAffiliation());
            String raisonSocialeCourt = tiers.getDesignation1() + " " + tiers.getDesignation2();
            if (raisonSocialeCourt.length() > 30) {
                raisonSocialeCourt = raisonSocialeCourt.substring(0, 30);
            }
            affiliation.setRaisonSocialeCourt(raisonSocialeCourt);
            affiliation.setMotifCreation(dto.getMotifCreation());
            affiliation.setPersonnaliteJuridique(dto.getPersonnaliteJuridique());
            affiliation.setPeriodicite(dto.getPeriodicite());
            affiliation.setBrancheEconomique(dto.getBrancheEconomique());

            affiliation.setAccesSecurite(dto.getAffiliationSecurisee());

            //Fields non-mandatory
            if (!JadeStringUtil.isEmpty(dto.getCodeNoga()))
                affiliation.setCodeNoga(dto.getCodeNoga());
            if (!Objects.isNull(dto.getFacturationParReleve()))
                affiliation.setReleveParitaire(dto.getFacturationParReleve());
            if (!Objects.isNull(dto.getFacturationAcompteCarte()))
                affiliation.setRelevePersonnel(dto.getFacturationAcompteCarte());
            if (!JadeStringUtil.isEmpty(dto.getFacturationCodeFacturation()))
                affiliation.setCodeFacturation(dto.getFacturationCodeFacturation());
            if (!Objects.isNull(dto.getExoneration()))
                affiliation.setExonerationGenerale(dto.getExoneration() != null ? dto.getExoneration() : Boolean.FALSE);
            if (!Objects.isNull(dto.getPersonnelOccasionnel()))
                affiliation.setOccasionnel(dto.getPersonnelOccasionnel());
            if (!Objects.isNull(dto.getAffiliationProvisoire()))
                affiliation.setTraitement(dto.getAffiliationProvisoire());
            if (!JadeStringUtil.isEmpty(dto.getDateDemandeAffiliation()))
                affiliation.setDateDemandeAffiliation(dto.getDateDemandeAffiliation());
            if (!JadeStringUtil.isEmpty(dto.getDeclarationSalaire()))
                affiliation.setDeclarationSalaire(dto.getDeclarationSalaire());
            if (!JadeStringUtil.isEmpty(dto.getActivite()))
                affiliation.setActivite(dto.getActivite());
            if (!JadeStringUtil.isEmpty(dto.getNumeroIDE()))
                affiliation.setNumeroIDE(dto.getNumeroIDE());
            if (!Objects.isNull(dto.getEntiteIDENonAnnoncante()))
                affiliation.setIdeNonAnnoncante(dto.getEntiteIDENonAnnoncante());


            affiliation.add(transaction);

            if (transaction.hasErrors()) {
                transaction.rollback();
                throw new Exception(transaction.getErrors().toString());
            }

            transaction.commit();

            //set champs pour la réponse
            dto.setId(affiliation.getAffiliationId());
            dto.setRaisonSocialeLong(affiliation.getRaisonSociale());
            dto.setRaisonSocialeCourt(affiliation.getRaisonSocialeCourt());
        } catch (Exception e) {
            throw new Exception(transaction.getErrors().toString());
        } finally {
            transaction.closeTransaction();
        }

        return dto;
    }

    /**
     * Modification d'une affiliation (page 1 et 2)
     *
     * @param dto
     * @param token
     * @return
     */
    public final AFAffiliationDTO updateAffiliation(AFAffiliationDTO dto, String token) {
        try {
            updateAffiliationPage1(getSession(), dto);
            updateAffiliationPage2(getSession(), dto);
        } catch (AFBadRequestException e) {
            LOG.error("Une erreur de parametre est survenue lors de la modification de l'affiliation " + e);
            throw e;
        } catch (AFInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la modification de l'affiliation: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la modification de l'affiliation: " + e);
            throw new AFInternalException(e);
        }
        return dto;
    }

    /**
     * Methode pour modifier une affiliation (page 1).
     *
     * @param session
     * @param dto
     * @throws Exception
     */
    private void updateAffiliationPage1(BSession session, AFAffiliationDTO dto) throws Exception {
        AFAffiliation affiliation = retrieveAffiliation(session, dto);

        if (!JadeStringUtil.isNull(dto.getIdTiers())) {
            affiliation.setIdTiers(dto.getIdTiers());
        }
        if (!JadeStringUtil.isNull(dto.getRaisonSocialeLong())) {
            affiliation.setRaisonSociale(dto.getRaisonSocialeLong());
        }
        if (!JadeStringUtil.isNull(dto.getRaisonSocialeCourt())) {
            affiliation.setRaisonSocialeCourt(dto.getRaisonSocialeCourt());
        }
        if (!JadeStringUtil.isNull(dto.getNumeroAffilie())) {
            affiliation.setAffilieNumero(dto.getNumeroAffilie());
        }
        if (!JadeStringUtil.isNull(dto.getAncien_numero_affilie())) {
            affiliation.setAncienAffilieNumero(dto.getAncien_numero_affilie());
        }
        if (!JadeStringUtil.isNull(dto.getDateDebutAffiliation())) {
            affiliation.setDateDebut(dto.getDateDebutAffiliation());
        }
        if (!JadeStringUtil.isNull(dto.getDateFinAffiliation())) {
            affiliation.setDateFin(dto.getDateFinAffiliation());
        }
        if (!JadeStringUtil.isNull(dto.getMotifFin())) {
            affiliation.setMotifFin(dto.getMotifFin());
        }
        if (!JadeStringUtil.isNull(dto.getGenreAffiliation())) {
            affiliation.setTypeAffiliation(dto.getGenreAffiliation());
        }
        if (!JadeStringUtil.isNull(dto.getMotifCreation())) {
            affiliation.setMotifCreation(dto.getMotifCreation());
        }
        if (!JadeStringUtil.isNull(dto.getPersonnaliteJuridique())) {
            affiliation.setPersonnaliteJuridique(dto.getPersonnaliteJuridique());
        }
        if (!JadeStringUtil.isNull(dto.getPeriodicite())) {
            affiliation.setPeriodicite(dto.getPeriodicite());
        }
        if (!JadeStringUtil.isNull(dto.getBrancheEconomique())) {
            affiliation.setBrancheEconomique(dto.getBrancheEconomique());
        }
        if (!JadeStringUtil.isNull(dto.getCodeNoga())) {
            affiliation.setCodeNoga(dto.getCodeNoga());
        }
        if (dto.getFacturationParReleve() != null) {
            affiliation.setReleveParitaire(dto.getFacturationParReleve());
        }
        if (dto.getFacturationAcompteCarte() != null) {
            affiliation.setRelevePersonnel(dto.getFacturationAcompteCarte());
        }
        if (dto.getFacturationCodeFacturation() != null) {
            affiliation.setCodeFacturation(dto.getFacturationCodeFacturation());
        }
        if (dto.getExoneration()!= null) {
            affiliation.setExonerationGenerale(dto.getExoneration());
        }
        if (dto.getPersonnelOccasionnel() != null) {
            affiliation.setOccasionnel(dto.getPersonnelOccasionnel());
        }
        if (dto.getAffiliationProvisoire() != null) {
            affiliation.setTraitement(dto.getAffiliationProvisoire());
        }
        //Affiliation EBusiness -> ne pas traiter
        if (!JadeStringUtil.isNull(dto.getDateDemandeAffiliation())) {
            affiliation.setDateDemandeAffiliation(dto.getDateDemandeAffiliation());
        }
        // TODO Type d'associé ?
        if (!JadeStringUtil.isNull(dto.getDeclarationSalaire())) {
            affiliation.setDeclarationSalaire(dto.getDeclarationSalaire());
        }
        if (!JadeStringUtil.isNull(dto.getNumeroIDE())) {
            affiliation.setNumeroIDE(dto.getNumeroIDE());
        }
        // TODO Raison sociale IDE ?
        if (dto.getEntiteIDENonAnnoncante() != null) {
            affiliation.setIdeNonAnnoncante(dto.getEntiteIDENonAnnoncante());
        }
        // Make the actual transaction with the database in order to update the tiers
        affiliation.update(session.getCurrentThreadTransaction());
        checkForErrorsAndThrow(session, "AFExecuteService#updateAffiliationPage1 - Erreur rencontrée lors de l'update de l'affiliation");
    }

    /**
     * Récupère l'affiliation avec son numéro d'affilié ou son id.
     * Si on a les 2 informations, c'est l'id qui prend le dessus.
     *
     * @param session
     * @param dto
     * @return
     * @throws Exception
     */
    private static AFAffiliation retrieveAffiliation(BSession session, AFAffiliationDTO dto) throws Exception {
        // On part du principe qu'on a forcément l'id ET/OU le numéro d'affilié --> validé dans isValidForUpdate
        AFAffiliation affiliation = new AFAffiliation();
        if (!JadeStringUtil.isEmpty(dto.getId())) {
            affiliation.setSession(session);
            affiliation.setAffiliationId(dto.getId());
            affiliation.retrieve(session.getCurrentThreadTransaction());
        } else {
            AFAffiliationManager afAffiliationManager = new AFAffiliationManager();
            afAffiliationManager.setSession(session);
            afAffiliationManager.setForAffilieNumero(dto.getNumeroAffilie());
            afAffiliationManager.find(BManager.SIZE_NOLIMIT);
            if (afAffiliationManager.getSize() == 1) {
                affiliation = (AFAffiliation) afAffiliationManager.getFirstEntity();
            } else if (afAffiliationManager.getSize() == 0) {
                throw new AFBadRequestException("AFExecuteService#updateAffiliationPage1 - L'affiliation à modifier n'a pas été trouvée");
            } else {
                throw new AFBadRequestException("AFExecuteService#updateAffiliationPage1 - Plusieurs affiliation trouvées pour le numéro " + dto.getNumeroAffilie());
            }
        }

        if (affiliation.isNew()) {
            throw new AFBadRequestException("AFExecuteService#updateAffiliationPage1 - L'affiliation à modifier n'a pas été trouvée");
        }
        return affiliation;
    }

    /**
     * Methode pour modifier une affiliation (page 2).
     *
     * @param session
     * @param dto
     * @throws Exception
     */
    private void updateAffiliationPage2(BSession session, AFAffiliationDTO dto) throws Exception {
        AFAffiliation affiliation = retrieveAffiliation(session, dto);

        if (!JadeStringUtil.isEmpty(dto.getAffiliationSecurisee())) {
            affiliation.setAccesSecurite(dto.getAffiliationSecurisee());
        }

        // Make the actual transaction with the database in order to update the tiers
        affiliation.update(session.getCurrentThreadTransaction());
        checkForErrorsAndThrow(session, "AFExecuteService#updateAffiliationPage2 - Erreur rencontrée lors de l'update de l'affiliation");
    }

    /**
     * Suppression d'une affiliation.
     *
     * @param dto
     * @param token
     * @return
     */
    public final String deleteAffiliation(AFAffiliationDTO dto, String token) {
        try {
            deleteAffiliation(getSession(), dto);
        } catch (AFBadRequestException e) {
            LOG.error("Une erreur de paramtre est survenue lors de la suppression de l'adresse: " + e);
            throw e;
        } catch (AFInternalException e) {
            LOG.error("Une erreur interne est survenue lors de la suppression de l'adresse: " + e);
            throw e;
        } catch (Exception e) {
            LOG.error("Une erreur est survenue lors de la suppression de l'adresse: " + e);
            throw new AFInternalException(e);
        }
        return "Deletion successful";
    }

    public void deleteAffiliation(BSession session, AFAffiliationDTO dto) throws Exception {
        //TODO delete affiliation
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
            throw new AFBadRequestException(message + ": " + session.getCurrentThreadTransaction().getErrors().toString());
        } else if (!JadeThread.logIsEmpty()) {
            LOG.error(message);
            throw new AFBadRequestException(message + ": " + JadeThread.getMessage(JadeThread.logMessages()[0].getMessageId()));
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

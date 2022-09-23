package globaz.naos.web.service;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
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
     * Modification d'une affiliation.
     *
     * @param dto
     * @param token
     * @return
     */
    public final AFAffiliationDTO updateAffiliation(AFAffiliationDTO dto, String token) {
        try {
            updateAffiliation(getSession(), dto);
        } catch (AFBadRequestException e) {
            LOG.error("Une erreur de paramtre est survenue lors de la modification de l'affiliation " + e);
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
     * Mthode pour modifier une adresse.
     *
     * @param session
     * @param dto
     * @throws Exception
     */

    private void updateAffiliation(BSession session, AFAffiliationDTO dto) throws Exception {
        //TODO update affililiation
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

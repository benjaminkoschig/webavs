package ch.globaz.al.businessimpl.checker.model.droit;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSAnnonceRafam;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.model.droit.ALDroitModelException;
import ch.globaz.al.business.models.dossier.DossierFkSearchModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.droit.EnfantSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSearchSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * classe de validation des donn�es de DroitModel
 * 
 * @author PTA
 * 
 */
public abstract class DroitModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(DroitModel droitModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de l'existence de l'identifiant du tiers
        // b�n�ficiaire
        if (!JadeNumericUtil.isEmptyOrZero(droitModel.getIdTiersBeneficiaire())) {
            TiersSearchSimpleModel ts = new TiersSearchSimpleModel();
            ts.setForIdTiers(droitModel.getIdTiersBeneficiaire());
            if (0 == JadePersistenceManager.count(ts)) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.idTiersBeneficiaire.businessIntegrity.ExistingId");

            }
        }

        // v�rification de l'existence de l'id du dossier
        DossierFkSearchModel sd = new DossierFkSearchModel();
        sd.setForIdDossier(droitModel.getIdDossier());
        if (0 == ALImplServiceLocator.getDossierFkModelService().count(sd)) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.idDossier.businessIntegrity.ExistingId");
        }

        // si le type de prestation est diff�rent de m�nage...
        if (!ALCSDroit.TYPE_MEN.equals(droitModel.getTypeDroit())) {

            // ...l'enfant est obligatoire
            EnfantSearchModel se = new EnfantSearchModel();
            se.setForIdEnfant(droitModel.getIdEnfant());
            if (!JadeNumericUtil.isIntegerPositif(droitModel.getIdEnfant())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.idEnfant.businessIntegrity.mandatoryIfNotMenage");

                // v�rification de l'existance de l'enfant
            } else if (0 == ALImplServiceLocator.getEnfantModelService().count(se)) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.idEnfant.businessIntegrity.existingId");
            }

            // ...ainsi que le statut familial
            if (JadeStringUtil.isEmpty(droitModel.getStatutFamilial())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.statutFamilial.businessIntegrity.mandatory");
            }
            // ...le statut familial est interdit pour les prestations de m�nage
        } else if (!JadeNumericUtil.isEmptyOrZero(droitModel.getStatutFamilial())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.statutFamilial.businessIntegrity.denied");

        }

        // Taux de versement ne peut pas �tre saisi � 0
        if ((Double.parseDouble(droitModel.getTauxVersement()) == 0)) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.tauxVersement.businessIntegrity.noZeroValue");
        }
        // v�rification du motif de r�duction
        // le motif est obligatoire si le taux est inf�rieur � 100%
        if ((Double.parseDouble(droitModel.getTauxVersement()) < 100)
                && JadeStringUtil.isEmpty(droitModel.getMotifReduction())) {

            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.motifReduction.businessIntegrity.mandatoryIfReduction");
        }

        // chronologie des date de d�but et de fin de droit forc�e
        if (!JadeStringUtil.isEmpty(droitModel.getFinDroitForcee())
                && (!JadeDateUtil.isDateBefore(droitModel.getDebutDroit(), droitModel.getFinDroitForcee()) && !JadeDateUtil
                        .areDatesEquals(droitModel.getDebutDroit(), droitModel.getFinDroitForcee()))) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.debutFinDroitForcee.businessIntegrity.dateChronology");
        }

        // Controle de l'�ch�ance rempli si c'est un droit Formation
        if ((JadeStringUtil.isEmpty(droitModel.getFinDroitForcee()))
                && (ALCSDroit.TYPE_FORM.equals(droitModel.getTypeDroit()))) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.finDroitForcee.businessIntegrity.mandatoryIfTypeFormation");
        }

        // le motif de fin est obligatoire si une date de fin est renseign�e
        if (!JadeStringUtil.isEmpty(droitModel.getFinDroitForcee())
                && JadeStringUtil.isBlankOrZero(droitModel.getMotifFin())) {

            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.finDroitForcee.businessIntegrity.mandatoryMotifFin");

        }

        // date de fin de droit forc�e est obligatoire pour les types de droits
        // autres que m�nages
        if (!ALCSDroit.TYPE_MEN.equals(droitModel.getTypeDroit())
                && JadeStringUtil.isEmpty(droitModel.getFinDroitForcee())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.finDroitForcee.businessIntegrity.mandatory");
        }

        // controle si le taux AF est = 100 alors motif = COMP
        // new taux = "100.0" A CONTROLER
        if (100.0 == Double.parseDouble(droitModel.getTauxVersement())) {
            // si oui le motif doit etre complet
            if (!ALCSDroit.MOTIF_REDUC_COMP.equals(droitModel.getMotifReduction())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.motifReduction.businessIntegrity.compSiTaux100");
            }
        } else {
            // sinon il ne DOIT PAS etre complet
            if (ALCSDroit.MOTIF_REDUC_COMP.equals(droitModel.getMotifReduction())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.motifReduction.businessIntegrity.notMotifComp");
            }
        }

        // Gestion de la date d'attestation
        if (JadeDateUtil.isGlobazDate(droitModel.getDateAttestationEtude())) {

            boolean attestationOkWithEcheance = JadeDateUtil.isDateBefore(droitModel.getFinDroitForcee(),
                    droitModel.getDateAttestationEtude())
                    || JadeDateUtil
                            .areDatesEquals(droitModel.getFinDroitForcee(), droitModel.getDateAttestationEtude()) ? true
                    : false;
            if (!attestationOkWithEcheance) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.dateAttestation.businessIntegrity.chronology");
            }

            boolean attestationOkWithDebut = JadeDateUtil.isDateBefore(droitModel.getDebutDroit(),
                    droitModel.getDateAttestationEtude()) ? true : false;
            if (!attestationOkWithDebut) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.dateAttestation.businessIntegrity.chronology");
            }
        }

        if (!JadeNumericUtil.isEmptyOrZero(droitModel.getIdTiersBeneficiaire())) {
            AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                    droitModel.getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF,
                    JadeDateUtil.getGlobazFormattedDate(new Date()), "");

            if (adress.getFields() == null) {
                JadeThread.logWarn(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.idDroit.updateBusinessIntegrity.adresseBeneficiaire.popup-redirect");

            }
        }

    }

    /**
     * V�rification des codesSystems
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(DroitModel droitModel) throws JadeApplicationException,
            JadePersistenceException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        try {
            // �tat du droit
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDroit.GROUP_ETAT, droitModel.getEtatDroit())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.etatDroit.codesystemIntegrity");
            }

            // statut familial
            if (!JadeNumericUtil.isEmptyOrZero(droitModel.getStatutFamilial())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSAnnonceRafam.GROUP_STATUT_FAMILIAL_BENEFICIAIRE,
                            droitModel.getStatutFamilial())) {
                JadeThread.logError(EnfantModelChecker.class.getName(),
                        "al.droit.droitModel.statutFamilial.codesystemIntegrity");
            }

            // tarif forc�
            if (!JadeNumericUtil.isEmptyOrZero(droitModel.getTarifForce())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE, droitModel.getTarifForce())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.tarifForce.codesystemIntegrity");
            }

            // type de droit
            if (!ALCSDroit.TYPE_ENF.equals(droitModel.getTypeDroit())
                    && !ALCSDroit.TYPE_FORM.equals(droitModel.getTypeDroit())
                    && !ALCSDroit.TYPE_MEN.equals(droitModel.getTypeDroit())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.typeDroit.codesystemIntegrity");
            }

            // motif de fin
            if (!JadeStringUtil.isBlankOrZero(droitModel.getMotifFin())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDroit.GROUP_MOTIF_FIN, droitModel.getMotifFin())) {
                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.motifFin.codesystemIntegrity.type");
            }

            // motif de r�duction
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDroit.GROUP_MOTIF_REDUC, droitModel.getMotifReduction())) {

                JadeThread.logError(DroitModelChecker.class.getName(),
                        "al.droit.droitModel.motifReduction.type.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALDroitModelException("DroitModelChecker problem during checking codes system integrity", e);
        }

    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param droitModel
     *            Mod�le � valider
     */
    private static void checkDatabaseIntegrity(DroitModel droitModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // �tat du droit
        if (!JadeNumericUtil.isIntegerPositif(droitModel.getEtatDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.etatDroit.databaseIntegrity.type");
        }

        // statut familial
        if (!JadeNumericUtil.isEmptyOrZero(droitModel.getStatutFamilial())
                && !JadeNumericUtil.isIntegerPositif(droitModel.getStatutFamilial())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.statutFamilial.databaseIntegrity.type");
        }

        // tarif forc�
        if (!JadeStringUtil.isEmpty(droitModel.getTarifForce())
                && !JadeNumericUtil.isInteger(droitModel.getTarifForce())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.tarifForce.databaseIntegrity.type");
        }

        // date de d�but de droit
        if (!JadeDateUtil.isGlobazDate(droitModel.getDebutDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.debutDroit.databaseIntegrity.dateFormat");
        }

        // date de fin droit forc�e
        if (!JadeStringUtil.isEmpty(droitModel.getFinDroitForcee())
                && !JadeDateUtil.isGlobazDate(droitModel.getFinDroitForcee())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.finDroitForcee.databaseIntegrity.dateFormat");
        }

        // type de droit
        if (!JadeNumericUtil.isIntegerPositif(droitModel.getTypeDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.typeDroit.databaseIntegrity.type");
        }

        // motif de fin de droit
        if (!JadeStringUtil.isEmpty(droitModel.getMotifFin()) && !JadeNumericUtil.isInteger(droitModel.getMotifFin())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.motifFin.databaseIntegrity.type");
        }

        // motif de r�duction
        if (!JadeNumericUtil.isIntegerPositif(droitModel.getMotifReduction())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.motifReduction.databaseIntegrity.type");

        }

        // id de l'enfant
        if (!JadeStringUtil.isEmpty(droitModel.getIdEnfant()) && !JadeNumericUtil.isInteger(droitModel.getIdEnfant())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.idEnfant.databaseIntegrity.type");
        }

        // id du dossier
        if (!JadeNumericUtil.isIntegerPositif(droitModel.getIdDossier())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.idDossier.databaseIntegrity.type");
        }

        // id du tiers b�n�ficiaire
        if (!JadeNumericUtil.isEmptyOrZero(droitModel.getIdTiersBeneficiaire())
                && !JadeNumericUtil.isIntegerPositif(droitModel.getIdTiersBeneficiaire())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.idTiersBeneficiaire.databaseIntegrity.type");
        }

        // taux de versement
        if (!JadeNumericUtil.isNumericPositif(droitModel.getTauxVersement())
                && !JadeNumericUtil.isZeroValue(droitModel.getTauxVersement())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.tauxVersement.databaseIntegrity.type");
        }

        // montant forc�
        if (!JadeStringUtil.isEmpty(droitModel.getMontantForce())
                && !JadeNumericUtil.isNumeric(droitModel.getMontantForce())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.montantForce.databaseIntegrity.type");
        }

    }

    /**
     * V�rification de l'int�grit� m�tier avant suppression
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(DroitModel droitModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification du nombre de prestations li�es � ce droit
        if (ALServiceLocator.getDroitBusinessService().hasPrestations(droitModel.getIdDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.idDroit.deleteIntegrity.hasDroit");
        }

        // si le droit a �t� d�j� �t� transmis � la centrale, on emp�che son
        // effacement pour garder le lien avec les annonces cr��es
        if (ALImplServiceLocator.getAnnoncesRafamSearchService().hasSentAnnonces(droitModel.getIdDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.idDroit.deleteIntegrity.hasAnnonce");
        }
    }

    /**
     * v�rification des donn�es requises
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException {

        // id du dossier
        if (JadeStringUtil.isEmpty(droitModel.getIdDossier())) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.idDossier.mandatory");

        }

        // date de d�but de droit
        if (JadeStringUtil.isEmpty(droitModel.getDebutDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.debutDroit.mandatory");
        }

        // �tat du droit
        if (JadeStringUtil.isEmpty(droitModel.getEtatDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.EtatPrestation.mandatory");
        }

        // taux de versement
        if (JadeStringUtil.isEmpty(droitModel.getTauxVersement())) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.tauxVersement.mandatory");
        }

        // motif de r�duction
        if (JadeStringUtil.isEmpty(droitModel.getMotifReduction())) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.motifReduction.mandatory");
        }

        // impression de l'�ch�ance
        if (null == droitModel.getImprimerEcheance()) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.imprimerEcheance.mandatory");
        }

        // impression de l'�ch�ance
        if (null == droitModel.getAttestationAlloc()) {
            JadeThread.logError(DroitModelChecker.class.getName(),
                    "al.droit.droitModel.attestationAllocataire.mandatory");
        }

        // type du droit
        if (JadeStringUtil.isEmpty(droitModel.getTypeDroit())) {
            JadeThread.logError(DroitModelChecker.class.getName(), "al.droit.droitModel.typeDroit.mandatory");
        }

    }

    /**
     * validation des donn�es de droitModel
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(DroitModel droitModel) throws JadeApplicationException, JadePersistenceException {
        DroitModelChecker.checkMandatory(droitModel);
        DroitModelChecker.checkDatabaseIntegrity(droitModel);
        DroitModelChecker.checkCodesystemIntegrity(droitModel);
        DroitModelChecker.checkBusinessIntegrity(droitModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param droitModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(DroitModel droitModel) throws JadeApplicationException,
            JadePersistenceException {
        DroitModelChecker.checkDeleteIntegrity(droitModel);
    }

}

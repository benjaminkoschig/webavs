package ch.globaz.al.businessimpl.checker.model.dossier;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstAttributsEntite;
import ch.globaz.al.business.constantes.ALConstNumeric;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALImportUtils;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.TiersSearchSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe de v�rification des donn�es de <code>DossierModel</code>
 * 
 * @author jts
 * 
 */
public abstract class DossierModelChecker extends ALAbstractChecker {

    /**
     * Longueur maximale du num�ro de salari� externe
     */
    private static final int NUM_SALARIE_EXTERNE_MAX_LENGTH = 50;
    /**
     * Longueur maximale de la r�f�rence pour le dossier
     */
    private static final int REF_DOSSIER_MAX_LENGTH = 15;

    /**
     * Contr�le l'int�grit� m�tier du mod�le
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(DossierModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // chronologie des date de d�but et de fin d'activit�
        if (!JadeStringUtil.isEmpty(model.getDebutActivite())
                && !JadeStringUtil.isEmpty(model.getFinActivite())
                && (!JadeDateUtil.isDateBefore(model.getDebutActivite(), model.getFinActivite()) && !JadeDateUtil
                        .areDatesEquals(model.getDebutActivite(), model.getFinActivite()))) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.debutFinActivite.businessIntegrity.dateChronology");
        }

        if (!ALImportUtils.importFromAlfaGest) {

            // au moins l'une des deux date de validit� doit �tre indiqu�e
            if (JadeStringUtil.isEmpty(model.getDebutValidite()) && JadeStringUtil.isEmpty(model.getFinValidite())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.debutFinValidite.businessIntegrity.dateMandatory");

                // si seulement d�but
            } else if (!JadeStringUtil.isEmpty(model.getDebutValidite())
                    && JadeStringUtil.isEmpty(model.getFinValidite())) {

                // si le jour de d�but n'est pas le premier jour du mois et que
                // le nombre de jour n'a pas �t� indiqu�
                if (!ALDateUtils.isFirstDay(model.getDebutValidite())
                        && JadeNumericUtil.isEmptyOrZero(model.getNbJoursDebut())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursDebut.businessIntegrity.mandatory");
                    // si le jour est le premier jour du mois et que le nombre
                    // de jour a �t� indiqu�
                } else if (ALDateUtils.isFirstDay(model.getDebutValidite())
                        && !JadeNumericUtil.isEmptyOrZero(model.getNbJoursDebut())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursDebut.businessIntegrity.denied");
                }

                // si le nombre de jour de fin a �t� indiqu�
                if (!JadeNumericUtil.isEmptyOrZero(model.getNbJoursFin())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursFin.businessIntegrity.denied");
                }
            }
            // si seulement fin
            else if (JadeStringUtil.isEmpty(model.getDebutValidite())
                    && !JadeStringUtil.isEmpty(model.getFinValidite())) {

                // si le jour de fin n'est pas le dernier jour du mois et que le
                // nombre de jour n'a pas �t� indiqu� et que l'�tat du dossier
                // n'est pas radi�
                if (!ALDateUtils.isLastDay(model.getFinValidite())
                        && JadeNumericUtil.isEmptyOrZero(model.getNbJoursFin())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursFin.businessIntegrity.mandatory");
                    // si le jour est le dernier jour du mois et que le nombre
                    // de jour a �t� indiqu�
                } else if (ALDateUtils.isLastDay(model.getFinValidite())
                        && !JadeNumericUtil.isEmptyOrZero(model.getNbJoursFin())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursFin.businessIntegrity.denied");
                }

                // si le nombre de jour de d�but a �t� indiqu�
                if (!JadeNumericUtil.isEmptyOrZero(model.getNbJoursDebut())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursDebut.businessIntegrity.denied");
                }
            } else {
                // chronologie des dates de d�but et de fin de validit�
                if (!JadeDateUtil.isDateBefore(model.getDebutValidite(), model.getFinValidite())
                        && !JadeDateUtil.areDatesEquals(model.getDebutValidite(), model.getFinValidite())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.debutFinValidite.businessIntegrity.dateChronology");
                }

                // si le jour de d�but est le premier jour du mois et que le
                // nombre de jour a �t� indiqu�
                if (ALDateUtils.isFirstDay(model.getDebutValidite())
                        && !JadeNumericUtil.isEmptyOrZero(model.getNbJoursDebut())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursDebut.businessIntegrity.denied");
                }

                // si le jour de fin est le dernier jour du mois et que le
                // nombre de jour a �t� indiqu�
                if (ALDateUtils.isLastDay(model.getFinValidite())
                        && !JadeNumericUtil.isEmptyOrZero(model.getNbJoursFin())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursFin.businessIntegrity.denied");
                }

                // si le nombre de jour de d�but et de fin ont �t� indiqu�s
                // alors que la date de d�but et de fin de validit�s sont dans
                // le m�me mois et la m�me ann�e
                if (ALDateUtils.getDateDebutMois(model.getDebutValidite()).equals(
                        ALDateUtils.getDateDebutMois(model.getFinValidite()))
                        && !JadeNumericUtil.isEmptyOrZero(model.getNbJoursFin())
                        && !JadeNumericUtil.isEmptyOrZero(model.getNbJoursDebut())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursDebutFin.businessIntegrity.bothValues");
                }

                if ((!ALDateUtils.isFirstDay(model.getDebutValidite()) || !ALDateUtils
                        .isLastDay(model.getFinValidite()))
                        && JadeNumericUtil.isEmptyOrZero(model.getNbJoursFin())
                        && JadeNumericUtil.isEmptyOrZero(model.getNbJoursDebut())) {
                    JadeThread.logError(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.nbJoursDebutFin.businessIntegrity.mandatory");

                }
            }

            if (!JadeNumericUtil.isEmptyOrZero(model.getIdTiersBeneficiaire())) {
                AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                        model.getIdTiersBeneficiaire(), true, ALCSTiers.DOMAINE_AF,
                        JadeDateUtil.getGlobazFormattedDate(new Date()), "");

                if (adress.getFields() == null) {
                    JadeThread
                            .logWarn(DossierModelChecker.class.getName(),
                                    "al.dossier.dossierModel.idDossier.updateBusinessIntegrity.adresseBeneficiaire.popup-redirect");

                }
            }

        }

        AllocataireComplexModel alloc = ALImplServiceLocator.getAllocataireComplexModelService().read(
                model.getIdAllocataire());

        // on v�rifie que l'id de l'allocataire fourni correspond bien � un
        // allocataire existant en base de donn�es
        if (alloc.isNew() || JadeStringUtil.isBlankOrZero(alloc.getId())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idAllocataire.businessIntegrity.ExistingId");
        } else {
            // v�rification de la pr�sence de plusieurs dossier pour le m�me NSS et le m�me affili�

            if (!ALImportUtils.importFromAlfaGest) {
                if (ALImplServiceLocator.getDossierBusinessService().getIdDossiersActifs(
                        alloc.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel(),
                        model.getNumeroAffilie()).length > 1) {
                    JadeThread.logWarn(DossierModelChecker.class.getName(),
                            "al.dossier.dossierModel.idAllocataire.businessIntegrity.severalDossier");
                }
            }
        }

        // contr�le si le taux AF est = 100 alors motif = COMP
        if (((Double.valueOf(model.getTauxVersement())).compareTo(ALConstNumeric.CENT_POURCENT) == 0)) {
            // si oui le motif doit �tre complet
            if (!model.getMotifReduction().equals(ALCSDossier.MOTIF_REDUC_COMP)) {

                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.tauxVersementMotifReduction.businessIntegrity.rightCodeSystem");

            }
            // sinon il ne DOIT PAS �tre complet
        } else if (model.getMotifReduction().equals(ALCSDossier.MOTIF_REDUC_COMP)) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.tauxVersementMotifReduction.businessIntegrity.notRightCodeSystem");
        }

        // v�rification du montant forc� sur un dossier
        if (!JadeNumericUtil.isEmptyOrZero(model.getMontantForce())
                && (!model.getUniteCalcul().equals(ALCSDossier.UNITE_CALCUL_MOIS) || ((Double.valueOf(model
                        .getTauxVersement())).compareTo(ALConstNumeric.CENT_POURCENT) != 0))) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.montantForce.businessIntegrity.unforced");
        }

        // v�rification de l'existence de l'identifiant du tiers
        // b�n�ficiaire
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdTiersBeneficiaire())) {
            TiersSearchSimpleModel ts = new TiersSearchSimpleModel();
            ts.setForIdTiers(model.getIdTiersBeneficiaire());
            if (0 == JadePersistenceManager.count(ts)) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.idTiersBeneficiaire.businessIntegrity.existingId");

            }
        }

        // v�rification de l'existence de l'affili�
        if (AFBusinessServiceLocator.getAffiliationService().nombreAffiliationExists(model.getNumeroAffilie()) == 0) {

            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.numeroAffilie.businessIntegrity.existingId",
                    new String[] { model.getNumeroAffilie() });
        }

        // v�rifier que le mode de paiement du dossier respecte l'attribut
        // affili�
        if ("true".equals(ALServiceLocator.getAttributEntiteModelService()
                .getAttributAffilieByNumAffilie(ALConstAttributsEntite.PAIEMENT_DIRECT, model.getNumeroAffilie())
                .getValeurAlpha())
                && JadeNumericUtil.isEmptyOrZero(model.getIdTiersBeneficiaire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idTiersBeneficiaire.businessIntegrity.paiementMode");
        }

        // On v�rifie si on trouve une affiliation couverte AF � la date
        // actuelle pour des coti pers ou paritaire
        // selon le dossier
        if (!ALImportUtils.importFromAlfaGest) {
            if (JadeNumericUtil.isEmptyOrZero(ALServiceLocator.getAffiliationBusinessService()
                    .getAssuranceInfo(model, JACalendar.todayJJsMMsAAAA()).getIdAffiliation())) {
                JadeThread.logWarn(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.numeroAffilie.businessIntegrity.typeAF");

            }
        }

        // Contr�le que le canton du parent soit bien indiqu� si le dossier
        // est
        // en CS
        if (ALCSDossier.STATUT_CS.equals(model.getStatut()) && JadeStringUtil.isEmpty(model.getLoiConjoint())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.loiConjoint.businessIntegrity.mandatoryIfCS");
        }
    }

    /**
     * V�rifie que les codes syst�me appartiennent � la famille de code attendue
     * 
     * @param dossierModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(DossierModel dossierModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // �tat du dossier
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ETAT, dossierModel.getEtatDossier())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.etatDossier.codesystemIntegrity");
            }

            // statut du dossier
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_STATUT, dossierModel.getStatut())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.statutDossier.codesystemIntegrity");
            }

            // activit� de l'allocataire
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_ACTIVITE_ALLOC,
                    dossierModel.getActiviteAllocataire())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.activiteAllocataire.codesystemIntegrity");
            }

            // unit� de calcul
            if (!JadeCodesSystemsUtil
                    .checkCodeSystemType(ALCSDossier.GROUP_UNITE_CALCUL, dossierModel.getUniteCalcul())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.uniteCalcul.codesystemIntegrity");
            }

            // motif de r�duction
            if (!JadeStringUtil.isEmpty(dossierModel.getMotifReduction())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_MOTIF_REDUC,
                            dossierModel.getMotifReduction())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.motifReduction.codesystemIntegrity");
            }

            // tarif forc�
            if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getTarifForce())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                            dossierModel.getTarifForce())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.tarifForce.codesystemIntegrity");
            }

            // loi AF conjoint
            if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getLoiConjoint())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                            dossierModel.getLoiConjoint())) {
                JadeThread.logError(DossierModelChecker.class.getName(),
                        "al.dossier.dossierModel.loiConjoint.codesystemIntegrity");
            }
        } catch (Exception e) {
            throw new ALDossierModelException("DossierModelChecker problem during checking codes system integrity", e);
        }
    }

    /**
     * V�rifie que toutes les donn�es ait le format attendu par la base de donn�es
     * 
     * @param dossierModel
     *            Mod�le � valider
     * 
     */
    private static void checkDatabaseIntegrity(DossierModel dossierModel) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id du dossier du conjoint (peut valoir 0 si pas d�fini...)
        if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getIdDossierConjoint())
                && !JadeNumericUtil.isIntegerPositif(dossierModel.getIdDossierConjoint())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idDossierConjoint.databaseIntegrity.type");
        }

        // id de l'allocataire
        if (!JadeNumericUtil.isIntegerPositif(dossierModel.getIdAllocataire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idAllocataire.databaseIntegrity.type");
        }

        // id du tiers b�n�ficiaire
        if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getIdTiersBeneficiaire())
                && !JadeNumericUtil.isIntegerPositif(dossierModel.getIdTiersBeneficiaire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idTiersBeneficiaire.databaseIntegrity.type");
        }

        // id de la caisse AF du conjoint
        if (!JadeStringUtil.isEmpty(dossierModel.getIdTiersCaisseConjoint())
                && !JadeNumericUtil.isIntegerPositif(dossierModel.getIdTiersCaisseConjoint())
                && !JadeNumericUtil.isEmptyOrZero(dossierModel.getIdTiersCaisseConjoint())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idTiersCaisseConjoint.databaseIntegrity.type");
        }

        // activit� de l'allocataire
        if (!JadeNumericUtil.isIntegerPositif(dossierModel.getActiviteAllocataire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.activiteAllocataire.databaseIntegrity.type");
        }

        // �tat du dossier
        if (!JadeNumericUtil.isIntegerPositif(dossierModel.getEtatDossier())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.etatDossier.databaseIntegrity.type");
        }

        // statut du dossier
        if (!JadeNumericUtil.isIntegerPositif(dossierModel.getStatut())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.statutDossier.databaseIntegrity.type");
        }

        // num�ro de salari� externe
        if (!JadeStringUtil.isEmpty(dossierModel.getNumSalarieExterne())
                && (dossierModel.getNumSalarieExterne().length() > DossierModelChecker.NUM_SALARIE_EXTERNE_MAX_LENGTH)) {

            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.numSalarieExterne.databaseIntegrity.length",
                    new String[] { String.valueOf(DossierModelChecker.NUM_SALARIE_EXTERNE_MAX_LENGTH) });
        }

        // tarif forc�e
        if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getTarifForce())
                && !JadeNumericUtil.isNumericPositif(dossierModel.getTarifForce())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.tarifForce.databaseIntegrity.type");

        }

        // loi AF conjoint
        if (!JadeStringUtil.isEmpty(dossierModel.getLoiConjoint())
                && !JadeNumericUtil.isNumeric(dossierModel.getLoiConjoint())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.loiConjoint.databaseIntegrity.type");
        }
        // r�f�rence
        if (dossierModel.getReference().length() > DossierModelChecker.REF_DOSSIER_MAX_LENGTH) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.reference.databaseIntegrity.length",
                    new String[] { String.valueOf(DossierModelChecker.REF_DOSSIER_MAX_LENGTH) });
        }

        // TODO (lot 2) v�rification categorieProf

        // date de d�but de validit�
        if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getDebutValidite())
                && !JadeDateUtil.isGlobazDate(dossierModel.getDebutValidite())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.debutValidite.databaseIntegrity.dateFormat");
        }

        // date de fin de validit�
        if (!JadeNumericUtil.isEmptyOrZero(dossierModel.getFinValidite())
                && !JadeDateUtil.isGlobazDate(dossierModel.getFinValidite())) {

            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.finValidite.databaseIntegrity.dateFormat");
        }

        // v�rification du nombre de jour "d�but"
        if (!JadeStringUtil.isEmpty(dossierModel.getNbJoursDebut())
                && !JadeNumericUtil.isInteger(dossierModel.getNbJoursDebut())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.nbJourDebut.databaseIntegrity.type");

        }

        // v�rification du nombre de jour "fin"
        if (!JadeStringUtil.isEmpty(dossierModel.getNbJoursFin())
                && !JadeNumericUtil.isInteger(dossierModel.getNbJoursFin())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.nbJourFin.databaseIntegrity.type");
        }

        // date de d�but d'activit�
        if (!JadeStringUtil.isEmpty(dossierModel.getDebutActivite())
                && !JadeDateUtil.isGlobazDate(dossierModel.getDebutActivite())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.debutActivite.databaseIntegrity.dateFormat");
        }

        // date de fin d'activit�
        if (!JadeStringUtil.isEmpty(dossierModel.getFinActivite())
                && !JadeDateUtil.isGlobazDate(dossierModel.getFinActivite())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.finActivite.databaseIntegrity.dateFormat");
        }

        // v�rification du taux d'occupation
        if (!JadeStringUtil.isEmpty(dossierModel.getTauxOccupation())
                && !JadeNumericUtil.isNumeric(dossierModel.getTauxOccupation())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.tauxOccupation.databaseIntegrity.type");
        }

        // unit� de calcul
        if (!JadeNumericUtil.isIntegerPositif(dossierModel.getUniteCalcul())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.uniteCalcul.databaseIntegrity.type");
        }

        // taux de versement
        if (!JadeNumericUtil.isNumeric(dossierModel.getTauxVersement())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.tauxVersement.databaseIntegrity.type");
        }

        // motif de r�duction
        if (!JadeNumericUtil.isIntegerPositif(dossierModel.getEtatDossier())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.motifReduction.databaseIntegrity.type");
        }

        // montant forc�
        if (!JadeStringUtil.isEmpty(dossierModel.getMontantForce())
                && !JadeNumericUtil.isNumeric(dossierModel.getMontantForce())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.montantForce.databaseIntegrity.type");
        }
    }

    /**
     * Effectue les v�rifications
     * 
     * @param dossierModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(DossierModel dossierModel) throws JadePersistenceException,
            JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification du nombre de prestations li�e � ce dossier (un dossier
        // ayant des prestations ne DOIT PAS �tre supprim�)
        EntetePrestationSearchModel etp = new EntetePrestationSearchModel();
        etp.setForIdDossier(dossierModel.getIdDossier());
        if (0 != ALImplServiceLocator.getEntetePrestationModelService().count(etp)) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.idDossier.deleteIntegrity.hasPrestations");
        }
    }

    /**
     * V�rifie si tous les param�tres requis ont �t� indiqu�s
     * 
     * @param dossierModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(DossierModel dossierModel) throws JadeApplicationException,
            JadePersistenceException {

        // id de l'allocataire
        if (JadeStringUtil.isEmpty(dossierModel.getIdAllocataire())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.idAllocataire.mandatory");
        }

        // num�ro de l'affili�
        if (JadeStringUtil.isEmpty(dossierModel.getNumeroAffilie())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.numeroAffilie.mandatory");
        }

        // activit� de l'allocataire
        if (JadeStringUtil.isEmpty(dossierModel.getActiviteAllocataire())) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.activiteAllocataire.mandatory");
        }

        // �tat du dossier
        if (JadeStringUtil.isEmpty(dossierModel.getEtatDossier())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.etatDossier.mandatory");
        }

        // statut du dossier
        if (JadeStringUtil.isEmpty(dossierModel.getStatut())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.statutDossier.mandatory");
        }

        // cat�gorie prof.
        if (JadeStringUtil.isEmpty(dossierModel.getCategorieProf())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.categorieProf.mandatory");
        }

        // date de d�but d'activit�
        if (JadeStringUtil.isEmpty(dossierModel.getDebutActivite())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.debutActivite.mandatory");
        }

        // unit� de calcul
        if (JadeStringUtil.isEmpty(dossierModel.getUniteCalcul())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.uniteCalcul.mandatory");
        }

        // taux de versement
        if (JadeStringUtil.isEmpty(dossierModel.getTauxVersement())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.tauxVersement.mandatory");
        }

        // motif de r�duction
        if (JadeStringUtil.isEmpty(dossierModel.getMotifReduction())) {
            JadeThread
                    .logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.motifReduction.mandatory");
        }

        // retenue d'imp�t
        if (null == dossierModel.getRetenueImpot()) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.retenueImpot.mandatory");
        }

        // impression de la d�cision
        if (null == dossierModel.getImprimerDecision()) {
            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.dossier.dossierModel.imprimerDecision.mandatory");
        }
        // r�f�rence
        if (JadeStringUtil.isEmpty(dossierModel.getReference())) {
            JadeThread.logError(DossierModelChecker.class.getName(), "al.dossier.dossierModel.reference.mandatory");
        }
    }

    /**
     * validation des donn�es
     * 
     * @param dossierModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(DossierModel dossierModel) throws JadeApplicationException, JadePersistenceException {

        DossierModelChecker.checkMandatory(dossierModel);
        DossierModelChecker.checkDatabaseIntegrity(dossierModel);
        DossierModelChecker.checkCodesystemIntegrity(dossierModel);
        DossierModelChecker.checkBusinessIntegrity(dossierModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param dossierModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validateForDelete(DossierModel dossierModel) throws JadePersistenceException,
            JadeApplicationException {
        DossierModelChecker.checkDeleteIntegrity(dossierModel);
    }

}

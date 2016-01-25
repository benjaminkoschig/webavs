package ch.globaz.al.businessimpl.checker.model.prestation;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.droit.DroitSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.checker.model.dossier.DossierModelChecker;
import ch.globaz.al.businessimpl.checker.model.tarif.CategorieTarifModelChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALImportUtils;
import ch.globaz.pyxis.business.model.TiersSearchSimpleModel;

/**
 * classe de validit� des donn�es de DetailPrestationModel
 * 
 * @author PTA
 */
public abstract class DetailPrestationModelChecker extends ALAbstractChecker {

    /**
     * Longueur maximale du num�ro de compte
     */
    private static final int NUM_COMPTE_MAX_LENGTH = 20;

    /**
     * Contr�le l'int�grit� m�tier des donn�es du d�tail de prestation
     * 
     * @param detail
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkBusinessIntegrity(DetailPrestationModel detail) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de l'existence du droit
        DroitSearchModel sd = new DroitSearchModel();
        sd.setForIdDroit(detail.getIdDroit());
        if (0 == ALImplServiceLocator.getDroitModelService().count(sd)) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idDroit.businessIntegrity.ExistingId");
        }

        // v�rification de l'existence de l'id de l'en-t�te
        EntetePrestationSearchModel setp = new EntetePrestationSearchModel();
        setp.setForIdEntete(detail.getIdEntete());
        setp = ALImplServiceLocator.getEntetePrestationModelService().search(setp);

        if (0 == setp.getSize()) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idEntetePrestation.businessIntegrity.ExistingId");
            // v�rification de la pr�sence d'un id de tiers b�n�ficiaire si la
            // prestation est en paiement direct
        } else if (ALCSPrestation.BONI_DIRECT.equals(((EntetePrestationModel) setp.getSearchResults()[0])
                .getBonification()) && JadeNumericUtil.isEmptyOrZero(detail.getIdTiersBeneficiaire())) {
            JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idTiersBeneficiaire.businessIntegrity.mandatoryIfDirect");

        }

        // cat�gorie de tarif
        if (!ALImportUtils.importFromAlfaGest) {
            if (detail.getTarifForce().booleanValue() && JadeStringUtil.isEmpty(detail.getCategorieTarif())) {
                JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                        "al.prestation.detailPrestationModel.categorieTarif.businessIntegrity.mandatory");
            }
        }

        // v�rification de l'�ge de l'enfant
        if (!JadeNumericUtil.isInteger(detail.getAgeEnfant())
                && (ALCSDroit.TYPE_ENF.equals(detail.getTypePrestation()) || ALCSDroit.TYPE_FORM.equals(detail
                        .getTypePrestation()))) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.ageEnfant.businessIntegrity.defined");
        }

        // v�rification de l'existence de l'identifiant du tiers b�n�ficiaire
        if (!JadeNumericUtil.isEmptyOrZero(detail.getIdTiersBeneficiaire())) {
            TiersSearchSimpleModel ts = new TiersSearchSimpleModel();
            ts.setForIdTiers(detail.getIdTiersBeneficiaire());
            if (0 == JadePersistenceManager.count(ts)) {
                JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                        "al.prestation.detailPrestationModel.idTiersBeneficiaire.businessIntegrity.ExistingId");
            }
        }
    }

    /**
     * v�rifie l'int�grit� des donn�es codes syst�me
     * 
     * @param detailPrestModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(DetailPrestationModel detailPrestModel)
            throws JadeApplicationException, JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // type de prestation
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDroit.GROUP_TYPE, detailPrestModel.getTypePrestation())) {

                JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                        "al.prestation.detailPrestationModel.typePrestation.codesystemIntegrity",
                        new String[] { detailPrestModel.getTypePrestation() });

            }

            // contr�le de cat�gorie de tarif
            if (!JadeStringUtil.isBlankOrZero(detailPrestModel.getCategorieTarif())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                            detailPrestModel.getCategorieTarif())) {
                JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                        "al.tarif.categorieTarifModel.categorieTarif.codesystemIntegrity");
            }

            // cat�gorie caisse
            if (!JadeStringUtil.isBlankOrZero(detailPrestModel.getCategorieTarifCaisse())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                            detailPrestModel.getCategorieTarifCaisse())) {
                JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                        "al.prestation.detailPrestationModel.categorieTarifCaisse.codesystemIntegrity");
            }

            // cat�gorie canton
            if (!JadeStringUtil.isBlankOrZero(detailPrestModel.getCategorieTarifCanton())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSTarif.GROUP_CATEGORIE,
                            detailPrestModel.getCategorieTarifCanton())) {
                JadeThread.logError(CategorieTarifModelChecker.class.getName(),
                        "al.prestation.detailPrestationModel.categorieTarifCanton.codesystemIntegrity");
            }

        } catch (Exception e) {
            throw new ALCategorieTarifModelException(
                    "DetailPrestationModelChecker#checkCodesystemIntegrity : problem during checking codes system integrity",
                    e);
        }

        // rang
        if (!JadeStringUtil.isEmpty(detailPrestModel.getTypePrestation())
                && (ALCSDroit.TYPE_ENF.equals(detailPrestModel.getTypePrestation()) || ALCSDroit.TYPE_FORM
                        .equals(detailPrestModel.getTypePrestation()))
                && JadeNumericUtil.isEmptyOrZero(detailPrestModel.getRang())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.rang.businessIntegrity");
        }
    }

    /**
     * v�rifie l'int�grit� des donn�es de DetailPrestationModel, si non respect�e lance un message sur l'int�grit�
     * 
     * @param detail
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(DetailPrestationModel detail) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id de l'en-t�te
        if (!JadeNumericUtil.isIntegerPositif(detail.getIdEntete())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idEntetePrestation.databaseIntegrity.type");
        }

        // id du droit
        if (!JadeStringUtil.isEmpty(detail.getIdDroit()) && !JadeNumericUtil.isIntegerPositif(detail.getIdDroit())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idDroit.databaseIntegrity.type");
        }

        // type de prestation
        if (!JadeNumericUtil.isIntegerPositif(detail.getTypePrestation())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.typePrestation.databaseIntegrity.type");
        }

        // p�riode de validit�
        if (!JadeDateUtil.isGlobazDateMonthYear(detail.getPeriodeValidite())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.periodeValidite.databaseIntegrity.dateFormat");

        }

        // montant
        if (!JadeNumericUtil.isNumeric(detail.getMontant())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.montant.databaseIntegrity.type",
                    new String[] { detail.getMontant() });
        }

        // montant caisse
        if (!JadeStringUtil.isEmpty(detail.getMontantCaisse()) && !JadeNumericUtil.isNumeric(detail.getMontantCaisse())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.montantCaisse.databaseIntegrity.type");
        }

        // montant canton
        if (!JadeStringUtil.isEmpty(detail.getMontantCaisse()) && !JadeNumericUtil.isNumeric(detail.getMontantCaisse())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.montantCanton.databaseIntegrity.type");
        }

        // cat�gorie de tarif
        if (!JadeStringUtil.isBlankOrZero(detail.getCategorieTarif())
                && !JadeNumericUtil.isIntegerPositif(detail.getCategorieTarif())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.categorieTarif.databaseIntegrity.type");

        }

        // cat�gorie de tarif caisse
        if (!JadeStringUtil.isEmpty(detail.getCategorieTarifCaisse())
                && !JadeNumericUtil.isInteger(detail.getCategorieTarifCaisse())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.categorieTarifCaisse.databaseIntegrity.type");

        }

        // cat�gorie de tarif canton
        if (!JadeStringUtil.isEmpty(detail.getCategorieTarifCanton())
                && !JadeNumericUtil.isInteger(detail.getCategorieTarifCanton())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.categorieTarifCanton.databaseIntegrity.type");

        }

        // �ge de l'enfant
        if (!JadeStringUtil.isEmpty(detail.getAgeEnfant()) && !JadeNumericUtil.isInteger(detail.getAgeEnfant())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.ageEnfant.databaseIntegrity.type");
        }

        // id du tiers b�n�ficiaire
        if (!JadeStringUtil.isBlankOrZero(detail.getIdTiersBeneficiaire())
                && !JadeNumericUtil.isIntegerPositif(detail.getIdTiersBeneficiaire())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idTiersBeneficiaire.databaseIntegrity.type");
        }

        // num�ro de compte
        if (!JadeStringUtil.isEmpty(detail.getNumeroCompte())
                && (detail.getNumeroCompte().length() > DetailPrestationModelChecker.NUM_COMPTE_MAX_LENGTH)) {

            JadeThread.logError(DossierModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.numeroCompte.databaseIntegrity.length",
                    new String[] { String.valueOf(DetailPrestationModelChecker.NUM_COMPTE_MAX_LENGTH) });
        }

        // rang
        if (!JadeStringUtil.isEmpty(detail.getRang()) && !JadeNumericUtil.isInteger(detail.getRang())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.rang.databaseIntegrity.type");
        }
    }

    /**
     * V�rification de l'int�grit� m�tier des donn�es avant suppression
     * 
     * @param detailPrestModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(DetailPrestationModel detailPrestModel) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // si on est dans une horlog�re et que la r�cap est d�j� li�e, on peut pas cr��r /modifier cette ent�te de
        // prestation
        EntetePrestationModel enteteLiee = ALServiceLocator.getEntetePrestationModelService().read(
                detailPrestModel.getIdEntete());
        String idRecap = enteteLiee.getIdRecap();
        if (!JadeStringUtil.isBlankOrZero(idRecap)) {
            if (ALServiceLocator.getRecapitulatifEntrepriseBusinessService().isRecapVerouillee(
                    ALServiceLocator.getRecapitulatifEntrepriseModelService().read(idRecap))) {
                JadeThread.logError(EntetePrestationModelChecker.class.getName(),
                        "al.prestation.recapEntrepriseModel.etat.businessIntegrity.verrouille");
            }
        }

        // implementation pour recherche d'un d�tail de prestation dans
        // TransfertTucana
        TransfertTucanaSearchModel sd = new TransfertTucanaSearchModel();
        sd.setForIdDetailPrestation(detailPrestModel.getIdDetailPrestation());
        // compte si detailPrestation
        if (0 != ALImplServiceLocator.getTransfertTucanaModelService().count(sd)) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestation.idDetailPrestation.deleteIntegrity.hasTransfertTucana");
        }

    }

    /**
     * v�rifie l'obligation des donn�es de DetailPrestationModel, si non respect�e lance un message sur l'obligation
     * 
     * @param detail
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(DetailPrestationModel detail) throws JadeApplicationException,
            JadePersistenceException {

        // id de l'en-t�te
        if (JadeStringUtil.isEmpty(detail.getIdEntete())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idEntetePrestation.mandatory");
        }

        // id du droit
        if (JadeStringUtil.isEmpty(detail.getIdDroit()) && !ALCSTarif.CATEGORIE_LJU.equals(detail.getCategorieTarif())
                && !ALCSDroit.TYPE_FNB.equals(detail.getTypePrestation())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.idDroit.mandatory");
        }

        // type de prestation
        if (JadeStringUtil.isEmpty(detail.getTypePrestation())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.typePrestation.mandatory");
        }

        // p�riode de validit�
        if (JadeStringUtil.isEmpty(detail.getPeriodeValidite())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.periodeValidite.mandatory");
        }

        // montant
        if (JadeStringUtil.isEmpty(detail.getMontant())) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.montant.mandatory");
        }

        // indication de tarif forc�
        if (detail.getTarifForce() == null) {
            JadeThread.logError(DetailPrestationModelChecker.class.getName(),
                    "al.prestation.detailPrestationModel.tarifForce.mandatory");
        }
    }

    /**
     * valide les donn�es de DetailPrestationModel
     * 
     * @param detailPrestModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(DetailPrestationModel detailPrestModel) throws JadeApplicationException,
            JadePersistenceException {
        DetailPrestationModelChecker.checkMandatory(detailPrestModel);
        DetailPrestationModelChecker.checkDatabaseIntegrity(detailPrestModel);
        DetailPrestationModelChecker.checkCodesystemIntegrity(detailPrestModel);
        DetailPrestationModelChecker.checkBusinessIntegrity(detailPrestModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param detailPrestModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(DetailPrestationModel detailPrestModel) throws JadeApplicationException,
            JadePersistenceException {
        DetailPrestationModelChecker.checkDeleteIntegrity(detailPrestModel);
    }
}

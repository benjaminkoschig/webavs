package ch.globaz.al.businessimpl.services.rafam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import al.ch.ech.xmlns.ech_0104_69._4.NoticeType;
import al.ch.ech.xmlns.ech_0104_69._4.ReceiptType;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamNotFoundException;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.rafam.AnnoncesRafamNewXSDVersionSearchService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pyxis.util.CommonNSSFormater;

/**
 * Implémentation du service de recherche d'annonces RAFam. Il fournit diverses méthode permettant de rechercher des
 * annonces selon des critères précis (dernière annonce, annonce correspondant à une notice, ...)
 *
 * @author jts
 *
 */
public class AnnoncesRafamNewXSDVersionSearchServiceImpl extends ALAbstractBusinessServiceImpl
        implements AnnoncesRafamNewXSDVersionSearchService {

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getAnnonceForNotice(ch.ech.xmlns.ech_0104_69.
     * _3.NoticeType)
     */
    @Override
    public AnnonceRafamModel getAnnonceForNotice(NoticeType notice)
            throws JadeApplicationException, JadePersistenceException {

        if (notice == null) {
            throw new ALAnnonceRafamException("AnnoncesRafamSearchServiceImpl#getAnnonceForNotice : notice is null");
        }

        CommonNSSFormater nssf = new CommonNSSFormater();

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();

        ArrayList<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.RECU.getCS());
        etats.add(RafamEtatAnnonce.VALIDE.getCS());
        etats.add(RafamEtatAnnonce.SUSPENDU.getCS());
        // FIXME : ajouter les annonces 'transmises' ? (Incident FPV : I130430_001)
        search.setInEtatAnnonce(etats);

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        search.setForRecordNumber(notice.getRecordNumber().toString());
        search.setForGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(notice.getFamilyAllowanceType()).getCodeCentrale());
        try {
            search.setForNssEnfant(nssf.format(Long.toString(notice.getChild().getVn())));
        } catch (Exception e) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getAnnonceForNotice : unable to format NSS");
        }

        search.setOrderKey("idAnnonce");
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        if (search.getSize() >= 1) {
            return (AnnonceRafamModel) search.getSearchResults()[0];
        } else {
            throw new ALAnnonceRafamNotFoundException(
                    "AnnoncesRafamSearchServiceImpl#getAnnonceForNotice : Aucune annonce n'a été trouvées pour ce record number : "
                            + search.getForRecordNumber());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getAnnonceForReceipt(ch.ech.xmlns.ech_0104_69
     * ._3.ReceiptType)
     */
    @Override
    public AnnonceRafamModel getAnnonceForReceipt(ReceiptType receipt)
            throws JadeApplicationException, JadePersistenceException {

        if (receipt == null) {
            throw new ALAnnonceRafamException("AnnoncesRafamSearchServiceImpl#getAnnonceForReceipt : receipt is null");
        }

        CommonNSSFormater nssf = new CommonNSSFormater();

        // (
        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForEtatAnnonce(RafamEtatAnnonce.TRANSMIS.getCS());
        search.setForRecordNumber(receipt.getRecordNumber().toString());
        // OR
        if (!RafamReturnCode.getRafamReturnCode(String.valueOf(receipt.getReturnCode()))
                .equals(RafamReturnCode.EN_ATTENTE)) {
            search.setForCodeRetour(RafamReturnCode.EN_ATTENTE.getCode());
        }
        // )

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        search.setForGenrePrestation(
                RafamFamilyAllowanceType.getFamilyAllowanceType(receipt.getFamilyAllowanceType()).getCodeCentrale());
        try {
            search.setForNssEnfant(nssf.format(receipt.getChild().getVn().toString()));
        } catch (Exception e) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getAnnonceForReceipt : unable to format NSS "
                            + receipt.getChild().getVn().toString());
        }

        search.setWhereKey("AnnonceForReceipt");
        search.setOrderKey("AnnonceForReceipt");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        if (search.getSize() >= 1) {
            return (AnnonceRafamModel) search.getSearchResults()[0];
        } else {
            throw new ALAnnonceRafamNotFoundException(
                    "AnnoncesRafamSearchServiceImpl#getAnnonceForReceipt : Aucune annonce n'a été trouvées pour ce record number : "
                            + search.getForRecordNumber());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getAnnonceRegisterStatus(java.lang.String)
     */
    @Override
    public AnnonceRafamModel getAnnonceRegisterStatus(String recordNumber)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getAnnonceRegisterStatus : recordNumber is empty");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForEtatAnnonce(RafamEtatAnnonce.RECU.getCS());
        search.setForRecordNumber(recordNumber);
        search.setForTypeAnnonce(RafamTypeAnnonce._69C_REGISTER_STATUS.getCode());
        search.setOrderKey("idAnnonce");
        search.setDefinedSearchSize(0);
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        if (search.getSize() >= 1) {
            return (AnnonceRafamModel) search.getSearchResults()[0];
        } else {
            return new AnnonceRafamModel();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getLastActive(java.lang.String,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType)
     */
    @Override
    public AnnonceRafamModel getLastActive(String idDroit, RafamFamilyAllowanceType type)
            throws JadeApplicationException, JadePersistenceException {

        if (type == null) {
            throw new ALAnnonceRafamException("AnnoncesRafamSearchServiceImpl#getLastActive68a : type is null");
        }

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getLastActive68a : idDroit is not defined");
        }

        ArrayList<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.RECU.getCS());
        etats.add(RafamEtatAnnonce.SUSPENDU.getCS());
        etats.add(RafamEtatAnnonce.TRANSMIS.getCS());
        etats.add(RafamEtatAnnonce.VALIDE.getCS());
        etats.add(RafamEtatAnnonce.ARCHIVE.getCS());

        return this.getLastActive(idDroit, type, etats);

    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getLastActive(java.lang.String,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType, java.util.ArrayList)
     */
    @Override
    public AnnonceRafamModel getLastActive(String idDroit, RafamFamilyAllowanceType type, ArrayList<String> etats)
            throws JadeApplicationException, JadePersistenceException {

        if (type == null) {
            throw new ALAnnonceRafamException("AnnoncesRafamSearchServiceImpl#getLastActive68a : type is null");
        }

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getLastActive68a : idDroit is not defined");
        }

        // recherche des 68a pour le droit
        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();

        search.setInEtatAnnonce(etats);

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        search.setInTypeAnnonce(types);

        search.setForGenrePrestation(type.getCodeCentrale());
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("lastActive");
        search.setOrderKey("idAnnonce");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        AnnonceRafamModel last68c = null;
        AnnonceRafamModel last68ab = null;
        ArrayList<String> processedRecordNumbers = new ArrayList<String>();

        // pour chaque 68a on regarde si ca a déjà été annulé
        for (int i = 0; i < search.getSize(); i++) {

            AnnonceRafamModel annonce68ab = (AnnonceRafamModel) search.getSearchResults()[i];

            if (!processedRecordNumbers.contains(annonce68ab.getRecordNumber())) {

                // recherche si annulation avec même record number
                AnnonceRafamSearchModel search68c = new AnnonceRafamSearchModel();
                search68c.setForRecordNumber(annonce68ab.getRecordNumber());
                search68c.setForTypeAnnonce(RafamTypeAnnonce._68C_ANNULATION.getCode());
                search68c.setForCodeRetour(RafamReturnCode.REJETEE.getCode()); // NOT_EQUALS dans le xml
                search68c.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                search68c.setWhereKey("lastActive68c");
                search68c = ALServiceLocator.getAnnonceRafamModelService().search(search68c);

                if (search68c.getSize() == 0) {
                    if (last68ab == null) {
                        last68ab = annonce68ab;
                    } else {
                        throw new ALAnnonceRafamException(
                                "AnnoncesRafamSearchServiceImpl#getLastActive : Plusieurs annonces actives ont été trouvées pour le droit "
                                        + annonce68ab.getIdDroit());
                    }
                } else if (search68c.getSize() == 1) {
                    if (last68c == null) {
                        last68c = (AnnonceRafamModel) search68c.getSearchResults()[0];
                    }
                } else {

                    throw new ALAnnonceRafamException(
                            "AnnoncesRafamSearchServiceImpl#getLastActive : Plusieurs annonces 68c ont été trouvées pour le record number "
                                    + annonce68ab.getRecordNumber());

                }

                processedRecordNumbers.add(annonce68ab.getRecordNumber());
            }
        }

        if (last68ab != null) {
            return last68ab;
        } else if (last68c != null) {
            return last68c;
        } else {
            return new AnnonceRafamModel();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getLastAnnonceForRecordNumber(java.lang.String)
     */
    @Override
    public AnnonceRafamModel getLastAnnonceForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getLastAnnonceForRecordNumber : recordNumber is empty or zero");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        search.setDefinedSearchSize(1);
        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);
        search.setOrderKey("idAnnonce");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        if (search.getSize() == 0) {
            return new AnnonceRafamModel();
        } else {
            return (AnnonceRafamModel) search.getSearchResults()[0];
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getLastAnnonceForRecordNumber(java.lang.String)
     */
    @Override
    public AnnonceRafamSearchModel getAnnoncesForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getLastAnnonceForRecordNumber : recordNumber is empty or zero");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);
        search.setOrderKey("idAnnonce");
        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        return search;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#hasAnnonceOfType(java.lang.String,
     * ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType)
     */
    @Override
    public boolean hasAnnonceOfType(String idDroit, RafamFamilyAllowanceType type)
            throws JadeApplicationException, JadePersistenceException {

        if (type == null) {
            throw new ALAnnonceRafamException("AnnoncesRafamSearchServiceImpl#getLastActive68a : type is null");
        }

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#getLastActive68a : idDroit is not defined");
        }

        return !this.getLastActive(idDroit, type).isNew();
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#hasSentAnnonces(java.lang.String)
     */
    @Override
    public boolean hasSentAnnonces(String idDroit) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#hasSentAnnonces : idDroit is not defined");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        ArrayList<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.RECU.getCS());
        etats.add(RafamEtatAnnonce.SUSPENDU.getCS());
        etats.add(RafamEtatAnnonce.TRANSMIS.getCS());
        etats.add(RafamEtatAnnonce.VALIDE.getCS());
        search.setInEtatAnnonce(etats);
        search.setForIdDroit(idDroit);

        return ALServiceLocator.getAnnonceRafamModelService().count(search) > 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#getAnnoncesToSend()
     */
    @Override
    public AnnonceRafamComplexSearchModel loadAnnoncesToSend()
            throws JadeApplicationException, JadePersistenceException {

        AnnonceRafamComplexSearchModel search = new AnnonceRafamComplexSearchModel();
        search.setForEtat(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
        search.setForDelegated(false);
        search.setForDebutDroit(JadeDateUtil.addMonths(JadeDateUtil.getGlobazFormattedDate(new Date()), 6));
        search.setWhereKey("annoncesToSend");
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return ALServiceLocator.getAnnonceRafamComplexModelService().search(search);
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.business.services.rafam.AnnoncesRafamSearchService#loadAnnoncesToSendForDroit(java.lang.String)
     */
    @Override
    public AnnonceRafamSearchModel loadAnnoncesToSendForDroit(String idDroit)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#loadAnnoncesToSendForDroit : idDroit is empty or zero");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForEtatAnnonce(RafamEtatAnnonce.A_TRANSMETTRE.getCS());
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return ALServiceLocator.getAnnonceRafamModelService().search(search);
    }

    @Override
    public AnnonceRafamSearchModel loadAnnoncesForDroit(String idDroit)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(idDroit)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#loadAnnoncesForDroit : idDroit is empty or zero");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForIdDroit(idDroit);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        return ALServiceLocator.getAnnonceRafamModelService().search(search);
    }

    @Override
    public AnnonceRafamModel getLastAnnonceUPIForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(recordNumber)) {
            throw new ALAnnonceRafamException(
                    "AnnoncesRafamSearchServiceImpl#loadAnnoncesForDroit : recordNumber is empty or zero");
        }

        AnnonceRafamSearchModel search = new AnnonceRafamSearchModel();
        search.setForRecordNumber(recordNumber);
        List<String> etats = new ArrayList<String>();
        etats.add(RafamEtatAnnonce.RECU.getCS());
        etats.add(RafamEtatAnnonce.SUSPENDU.getCS());
        search.setInEtatAnnonce(etats);
        search.setForTypeAnnonce(RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode());
        search.setDefinedSearchSize(1);
        search.setOrderKey("upi");

        search = ALServiceLocator.getAnnonceRafamModelService().search(search);

        if (search.getSize() == 0) {
            return null;
        } else {
            return (AnnonceRafamModel) search.getSearchResults()[0];
        }
    }
}

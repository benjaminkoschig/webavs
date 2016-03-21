package ch.globaz.al.businessimpl.services.rafam;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import ch.globaz.al.business.constantes.enumerations.RafamError;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamReturnCode;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.exceptions.rafam.ALRafamEnumException;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamErrorComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamProtocoleComplexModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamProtocoleComplexSearchModel;
import ch.globaz.al.business.services.rafam.AnnoncesRafamProtocoleService;
import ch.globaz.al.businessimpl.documents.excel.ALAbstractExcelServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALEncodingUtils;

/**
 * 
 * @author jts
 * 
 */
public class AnnoncesRafamProtocoleServiceImpl extends ALAbstractExcelServiceImpl implements
        AnnoncesRafamProtocoleService {

    public static final String tagErreur = "E";
    public static final String tagRappel = "R";
    public static final String tagSuspendu = "S";
    public static final String tagUPI = "U";

    @Override
    public String createProtocole() throws JadeApplicationException {
        try {
            return createDocAndSave(null);
        } catch (Exception e) {
            throw new ALDocumentException(
                    "AnnoncesRafamProtocoleServiceImpl#createProtocole : Unable to create the RAFam protocole : "
                            + e.getMessage(), e);
        }
    }

    @Override
    protected String getModelName() {
        return "protocoleRAFam.xml";
    }

    @Override
    protected String getOutputName() {
        return "Protocole_RAFam_" + JadeDateUtil.getGlobazFormattedDate(new Date());
    }

    /**
     * 
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private AnnonceRafamProtocoleComplexSearchModel loadErreurs() throws JadeApplicationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        AnnonceRafamProtocoleComplexSearchModel search = new AnnonceRafamProtocoleComplexSearchModel();

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        ArrayList<String> returnCodes = new ArrayList<String>();
        returnCodes.add(RafamReturnCode.EN_ERREUR.getCode());
        returnCodes.add(RafamReturnCode.REJETEE.getCode());
        search.setInCodeRetour(returnCodes);

        search.setForEtat(RafamEtatAnnonce.RECU.getCS());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("protocoleErreurs");
        return ALImplServiceLocator.getAnnonceRafamProtocoleComplexModelService().search(search);
    }

    private AnnonceRafamProtocoleComplexSearchModel loadRappel() throws JadeApplicationException,
            JadePersistenceException {
        AnnonceRafamProtocoleComplexSearchModel search = new AnnonceRafamProtocoleComplexSearchModel();

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        ArrayList<String> returnCodes = new ArrayList<String>();
        returnCodes.add(RafamReturnCode.RAPPEL.getCode());
        search.setInCodeRetour(returnCodes);

        search.setForEtat(RafamEtatAnnonce.RECU.getCS());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("protocoleErreurs");
        return ALImplServiceLocator.getAnnonceRafamProtocoleComplexModelService().search(search);
    }

    private AnnonceRafamProtocoleComplexSearchModel loadSuspendus() throws JadeApplicationException,
            JadePersistenceException {
        AnnonceRafamProtocoleComplexSearchModel search = new AnnonceRafamProtocoleComplexSearchModel();

        ArrayList<String> types = new ArrayList<String>();
        types.add(RafamTypeAnnonce._68A_CREATION.getCode());
        types.add(RafamTypeAnnonce._68B_MUTATION.getCode());
        types.add(RafamTypeAnnonce._68C_ANNULATION.getCode());
        search.setInTypeAnnonce(types);

        search.setForEtat(RafamEtatAnnonce.SUSPENDU.getCS());
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("protocoleErreurs");
        return ALImplServiceLocator.getAnnonceRafamProtocoleComplexModelService().search(search);
    }

    private AnnonceRafamProtocoleComplexSearchModel loadUPI() throws JadeApplicationException, JadePersistenceException {
        AnnonceRafamProtocoleComplexSearchModel search = new AnnonceRafamProtocoleComplexSearchModel();

        search.setForTypeAnnonce(RafamTypeAnnonce._69B_SYNCHRO_UPI.getCode());
        search.setForEtat(RafamEtatAnnonce.RECU.getCS());

        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey("upi");
        return ALImplServiceLocator.getAnnonceRafamProtocoleComplexModelService().search(search);
    }

    protected CommonExcelmlContainer populate(CommonExcelmlContainer container,
            AnnonceRafamProtocoleComplexSearchModel search, String tag) throws JadeApplicationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        for (JadeAbstractModel annonces : search.getSearchResults()) {
            AnnonceRafamProtocoleComplexModel annonce = (AnnonceRafamProtocoleComplexModel) annonces;

            container.put("VALUE_" + tag + "_NSS_ENF", annonce.getNssEnfant());
            container
                    .put("VALUE_" + tag + "_NOM_ENF", ALEncodingUtils.checkAndReplaceByWilcard(annonce.getNomEnfant()));
            container.put("VALUE_" + tag + "_PRENOM_ENF",
                    ALEncodingUtils.checkAndReplaceByWilcard(annonce.getPrenomEnfant()));
            container.put("VALUE_" + tag + "_NAISSANCE_ENF", annonce.getNaissanceEnfant());
            container.put("VALUE_" + tag + "_NO_DOSSIER", annonce.getIdDossier());
            container.put("VALUE_" + tag + "_RECORD_NUMBER", annonce.getRecordNumber());
            container.put("VALUE_" + tag + "_NO_ANNONCE", annonce.getIdAnnonce());
            container.put("VALUE_" + tag + "_NOM_ALLOC",
                    ALEncodingUtils.checkAndReplaceByWilcard(annonce.getNomAllocataire()));
            container.put("VALUE_" + tag + "_PRENOM_ALLOC",
                    ALEncodingUtils.checkAndReplaceByWilcard(annonce.getPrenomAllocataire()));

            AnnonceRafamErrorComplexSearchModel searchErr = new AnnonceRafamErrorComplexSearchModel();
            searchErr.setForIdAnnonce(annonce.getIdAnnonce());
            searchErr = ALImplServiceLocator.getAnnonceRafamErrorComplexModelService().search(searchErr);

            StringBuffer code = new StringBuffer(searchErr.getSize() > 1 ? "(+)" : "");
            StringBuffer libelle = new StringBuffer();
            StringBuffer periode = new StringBuffer();
            StringBuffer office = new StringBuffer();

            for (JadeAbstractModel erreurs : searchErr.getSearchResults()) {
                AnnonceRafamErrorComplexModel erreur = (AnnonceRafamErrorComplexModel) erreurs;

                code.append(erreur.getErreurAnnonceRafamModel().getCode()).append("\n");

                try {
                    libelle.append(
                            JadeCodesSystemsUtil.getCodeLibelle(RafamError.getRafamError(
                                    erreur.getErreurAnnonceRafamModel().getCode()).getCS())).append("\n");
                } catch (ALRafamEnumException e1) {
                    libelle.append(
                            JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                                    "al.enum.rafam.errorCode.codeInconnu")).append("\n");
                }

                if (!JadeStringUtil.isBlank(erreur.getOverlapInformationModel().getOverlapPeriodeStart())) {
                    periode.append(erreur.getOverlapInformationModel().getOverlapPeriodeStart()).append(" - ")
                            .append(erreur.getOverlapInformationModel().getOverlapPeriodeEnd()).append("\n");
                    office.append(erreur.getOverlapInformationModel().getOfficeIdentifier()).append(".")
                            .append(erreur.getOverlapInformationModel().getBranch()).append("\n");
                }
            }

            container.put("VALUE_" + tag + "_CODE_ERREUR", code.toString().trim());
            container.put("VALUE_" + tag + "_ERREUR", libelle.toString().trim());
            container.put("VALUE_" + tag + "_PERIODE_ERREUR", periode.toString().trim());
            container.put("VALUE_" + tag + "_OFFICE", office.toString().trim());
        }

        return container;
    }

    @Override
    protected IMergingContainer prepareData(Map<String, Object> parameters)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        // remplacement des entêtes
        // BSession session = BSessionUtil.getSessionFromThreadContext();
        // String[] champs = new String[] { "AF_CHAMP_NSS", "AF_CHAMP_NOM", "AF_CHAMP_NSS_ENFANT",
        // "AF_CHAMP_NOM_ENFANT",
        // "AF_CHAMP_MONTANT", "INDE_CHAMP_NSS", "INDE_CHAMP_NOM", "INDE_CHAMP_NSS_ENFANT",
        // "INDE_CHAMP_NOM_ENFANT" };
        // for (String champ : champs) {
        // container.put(champ, session.getLabel("EXCEL_LISTE_CAS_AF_" + champ));
        // }

        CommonExcelmlContainer container = new CommonExcelmlContainer();
        container = populate(container, loadErreurs(), AnnoncesRafamProtocoleServiceImpl.tagErreur);
        container = populate(container, loadRappel(), AnnoncesRafamProtocoleServiceImpl.tagRappel);
        container = populate(container, loadSuspendus(), AnnoncesRafamProtocoleServiceImpl.tagSuspendu);
        container = populate(container, loadUPI(), AnnoncesRafamProtocoleServiceImpl.tagUPI);
        return container;
    }
}

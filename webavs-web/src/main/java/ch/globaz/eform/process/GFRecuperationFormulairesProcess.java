package ch.globaz.eform.process;


import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.common.process.ProcessMailUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.search.GFFormulaireSearch;
import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.constant.GFTypeEForm;
import ch.globaz.eform.process.container.GFFFormulaireReceiveContainer;
import ch.globaz.eform.properties.GFProperties;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Process qui envoie un récapitulatif des formulaires à l'état reçu
 */
@Slf4j
public class GFRecuperationFormulairesProcess extends BProcess {
    public static final String NUM_INFOROM = "0345GFE";

    private BSession bsession;
    private List<GFFFormulaireReceiveContainer> formulaireContainerList;

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        LOG.info("Lancement du process d'information de la liste des formulaire au statut " + GFStatusEForm.RECEIVE.getDesignation(getSession()) + " P14.");

        initBsession();
        this.setSendMailOnError(true);
        this.setSendCompletionMail(false);

        findFormulaireContainer();

        File fileXls = generatedXls();
        sendMail(fileXls.getAbsolutePath());

        closeBsession();
        LOG.info("Fin du process d'information.");
        return true;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Initialisation de la session
     *
     * @throws Exception : lance une exception si un problème intervient lors de l'initialisation du contexte
     */
    private void initBsession() throws Exception {
        bsession = getSession();
        BSessionUtil.initContext(bsession, this);
    }

    /**
     *  Fermeture de la session
     */
    private void closeBsession() {
        BSessionUtil.stopUsingContext(this);
    }

    private void findFormulaireContainer() throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
        GFFormulaireSearch formulaireSearch = new GFFormulaireSearch();
        formulaireSearch.setWhereKey(GFFormulaireSearch.WHERE_DEFINITION_FORMULAIRE);
        formulaireSearch.setByStatus(GFStatusEForm.RECEIVE.getCodeSystem());
        formulaireSearch = GFEFormServiceLocator.getGFEFormDBService().search(formulaireSearch);
        formulaireContainerList = Arrays.stream(formulaireSearch.getSearchResults())
                .map(o -> (GFFormulaireModel) o)
                .map(model -> {
                    GFFFormulaireReceiveContainer container = new GFFFormulaireReceiveContainer(getSession());

                    container.setSubject(GFTypeEForm.getGFTypeEForm(model.getSubject()));
                    container.setMessageId(model.getMessageId());
                    container.setDate(model.getDate());
                    container.setStatus(GFStatusEForm.getStatusByCodeSystem(model.getStatus()));
                    container.setBeneficiaireNss(model.getBeneficiaireNss());
                    container.setBeneficiaireNom(model.getBeneficiaireNom());
                    container.setBeneficiairePrenom(model.getBeneficiairePrenom());
                    container.setBeneficiaireDateNaissance(model.getBeneficiaireDateNaissance());
                    container.setUserGestionnaire(model.getUserGestionnaire());

                    return container;
                }).collect(Collectors.toList());
    }

    private void sendMail(String joinFilePath) throws PropertiesException {
        String email = GFProperties.EMAIL_RECAP_FORMULAIRE.getValue();

        String subject;
        String body;

        subject = getSession().getLabel("FORMULAIRE_RECEIVE_SUBJECT");

        body = getSession().getLabel("FORMULAIRE_RECEIVE_BODY");

        ProcessMailUtils.sendMail(Collections.singletonList(email), subject, body, Collections.singletonList(joinFilePath));
    }

    private File generatedXls() {
        Details detail = new Details();
        detail.add(getSession().getLabel("DETAIL_NUM_INFOROM"), NUM_INFOROM);
        detail.newLigne();
        detail.add(getSession().getLabel("DETAIL_NB_FORMULAIRE"), Integer.toString(formulaireContainerList.size()));
        detail.newLigne();
        detail.add(getSession().getLabel("DETAIL_TRAITEMENT_DU"), Date.now().toString());

        SimpleOutputListBuilderJade simpleOutputListBuilderJade = SimpleOutputListBuilderJade.newInstance();

        File file = simpleOutputListBuilderJade.session(getSession())
                .outputNameAndAddPath(NUM_INFOROM + "_" + getSession().getLabel("EXCEL_NAME"))
                .globazTheme()
                .addTranslater()
                .addList(formulaireContainerList)
                .classElementList(GFFFormulaireReceiveContainer.class)
                .addTitle(getSession().getLabel("FORMULAIRE_RECEIVE_TITRE"), Align.CENTER)
                .addHeaderDetails(detail)
                .asXls()
                .build();

        simpleOutputListBuilderJade.close();
        return file;
    }
}

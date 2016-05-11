package globaz.al.process.decision;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.models.dossier.DossierModelService;
import globaz.al.vb.decision.ALDecisionFileAttenteViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;

/**
 * Process permettant l'impression des décisions flagguö 'file d'attente'
 *
 * @author LGA
 */
public class ALDecisionsFileAttenteProcess extends ALDecisionsMasseProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ALDecisionsFileAttenteProcess.class);

    private static final long serialVersionUID = 1L;

    /**
     * Au moins une personne doit être renseignée
     * Seul les décisions pour ce(s) gestionnaire(s) seront générées
     *
     */
    private String idGestionnaireSelectionne;

    /**
     * Contient l'ensemble des ids des dossier à reseter. Par reseter on entend mettre l'idGestionnaire à null
     */
    private Set<String> idDossierAReseter;

    public ALDecisionsFileAttenteProcess() {
        idDossierAReseter = new HashSet<String>();
    }

    private void validate() {
        if (JadeStringUtil.isBlankOrZero(idGestionnaireSelectionne)) {
            throw new IllegalArgumentException(getClass().getName() + "idGestionnaireSelectionne can not be null ");
        }
        if (JadeStringUtil.isBlankOrZero(getEmail())) {
            throw new IllegalArgumentException(getClass().getName() + "email can not be empty");
        } else if ((!getEmail().contains("@")) || (!getEmail().contains("."))) {
            throw new IllegalArgumentException(getClass().getName() + "email must contain '@' symbol and '.' symbol");
        }

    }

    /**
     * Charge la liste des dossiers en file d'attente.
     * Pour qu'un dossier soit en file d'attente, le champ 'isFileAttente' [ALDOS.BDFIAT] doit être à true ET
     * le champ 'isGestionnaire' [ALDOS.IDGEST] doit être renseigné.</br>
     * <strong>Si le champ [ALDOS.BDFIAT] est à true alors [ALDOS.IDGEST] doit impérativement être renseigné</strong>
     *
     *
     * @return Les dossiers chargés
     * @throws IOException
     *             Exception levée en cas d'erreur de lecture du fichier
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private JadeAbstractModel[] getDossierEnFileAttente()
            throws IOException, JadeApplicationException, JadePersistenceException {

        // chargement des dossiers
        DossierComplexSearchModel searchDossier = new DossierComplexSearchModel();

        if (ALDecisionFileAttenteViewBean.TOUS[0].equals(idGestionnaireSelectionne)) {
            searchDossier.setForIdGestionnaire("");
        } else {
            searchDossier.setForIdGestionnaire(idGestionnaireSelectionne);
        }
        searchDossier.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_FILE_ATTENTE);

        return getSearchResults(searchDossier);
    }

    @Override
    protected void process() {

        // Paramétrages du process parent
        super.setGestionTexteLibre(true);
        super.setGestionCopie(true);

        validate();
        try {

            serviceDecision = ALServiceLocator.getDecisionBuilderService();
            serviceDossier = ALServiceLocator.getDossierComplexModelService();

            JadeAbstractModel[] dossiers = getDossierEnFileAttente();
            getProgressHelper().setMax(dossiers.length);

            for (JadeAbstractModel dossier : dossiers) {
                DossierComplexModel dossierComplex = (DossierComplexModel) dossier;
                try {
                    addDecision(dossierComplex);
                    journaliser(dossierComplex);
                    idDossierAReseter.add(dossierComplex.getDossierModel().getIdDossier());
                } catch (JadeApplicationException e) {
                    JadeBusinessMessage message = new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR,
                            ALDecisionsMasseProcess.class.getName(),
                            "al.decision.file.attente.process.erreur.dossierTraiteDecisionNonImprimee",
                            new String[] { e.getMessage() });
                    addProtocoleMessage(dossierComplex, message);
                }
                getProgressHelper().setCurrent(getProgressHelper().getCurrent() + 1);
            }
            printDecisions();
            resetDossier();

        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                            "globaz.al.process.generic.err_process") + e.getMessage());
            LOG.error(this.getClass().getName(), JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "globaz.al.process.generic.err_process") + e.getMessage());
        } finally {
            sendMail();
        }
    }

    /**
     * Reset les idGestionnaire pour les dossier dont l'id est stocké dans le Set idDossierAReseter
     *
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void resetDossier() throws JadeApplicationException, JadePersistenceException {
        DossierModelService dossierService = ALServiceLocator.getDossierModelService();
        for (String id : idDossierAReseter) {
            DossierModel dossier = dossierService.read(id);
            dossier.setIdGestionnaire(null);
            dossierService.update(dossier);
        }
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.ALDecisionsFileAttenteProcess.name");
    }

    /**
     * Définit pour quel gestionnaire les décisions doivent être générées</br>
     * Un seul gestionnaire peut et doit être saisit.
     * Si l'ensemble des décisions en file d'attente doit être généré, il est possible de passer la valeur
     * {@link ALDecisionFileAttenteViewBean}.TOUS
     *
     * @param idsGestionnaires the idsGestionnaires to set
     */
    public void setIdGestionnaireSelectionne(String idGestionnaireSelectionne) {
        this.idGestionnaireSelectionne = idGestionnaireSelectionne;
    }

    public String getIdGestionnaireSelectionne() {
        return idGestionnaireSelectionne;
    }

    /**
     * Définit la date d'impression.</br>
     * <strong>La date doit être au format dd.MM.yyyy</strong>
     *
     * @param dateImpression la date d'impression
     */
    @Override
    public void setDateImpression(String dateImpression) {
        super.setDateImpression(dateImpression);
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                "globaz.al.process.ALDecisionsFileAttenteProcess.description");
    }

    @Override
    protected void sendMail() {
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(getEmail());
        try {
            sendCompletionMail(emails);
        } catch (Exception e) {
            JadeLogger
                    .error(this,
                            JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                                    "globaz.al.process.generic.err_mail_result") + e.getMessage() + ", "
                            + e.getCause());
        }
    }
}

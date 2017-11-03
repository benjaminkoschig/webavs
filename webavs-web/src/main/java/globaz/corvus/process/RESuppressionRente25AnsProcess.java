package globaz.corvus.process;

import globaz.corvus.api.basescalcul.IRERenteAccordee;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.echeances.REEcheancesManager;
import globaz.corvus.db.rentesaccordees.REListerEcheanceRenteJoinMembresFamille;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BManager;
import globaz.lyra.process.LYAbstractEcheanceProcess;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.IREEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory;
import ch.globaz.corvus.process.echeances.analyseur.REAnalyseurEcheancesFactory.TypeAnalyseurEcheances;
import ch.globaz.corvus.process.echeances.analyseur.modules.REReponseModuleAnalyseEcheance;
import ch.globaz.prestation.domaine.CodePrestation;

public class RESuppressionRente25AnsProcess extends LYAbstractEcheanceProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RESuppressionRente25AnsProcess() {
        super();
    }

    @Override
    protected void beforeExecute() throws Exception {
        // rien
    }

    private List<REListerEcheanceRenteJoinMembresFamille> convertToOldEcheance(List<IREEcheances> echeances) {
        ArrayList<REListerEcheanceRenteJoinMembresFamille> newList = new ArrayList<REListerEcheanceRenteJoinMembresFamille>();

        for (IREEcheances ireEcheances : echeances) {

            for (IRERenteEcheances uneRenteDuTiers : ireEcheances.getRentesDuTiers()) {
                CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneRenteDuTiers
                        .getCodePrestation()));

                if (codePrestation.isRenteComplementairePourEnfant()) {
                    REListerEcheanceRenteJoinMembresFamille newBean = new REListerEcheanceRenteJoinMembresFamille();
                    newBean.setCsSexe(ireEcheances.getCsSexeTiers());
                    newBean.setDateDeces(ireEcheances.getDateDecesTiers());
                    newBean.setDateNaissance(ireEcheances.getDateNaissanceTiers());
                    newBean.setIdTiers(ireEcheances.getIdTiers());
                    newBean.setNom(ireEcheances.getNomTiers());
                    newBean.setPrenom(ireEcheances.getPrenomTiers());
                    newBean.setNss(ireEcheances.getNssTiers());
                    newBean.setIdRenteAccordee(uneRenteDuTiers.getIdPrestationAccordee());
                    newBean.setDateDebutDroit(uneRenteDuTiers.getDateDebutDroit());
                    newBean.setCodePrestation(uneRenteDuTiers.getCodePrestation());
                    newList.add(newBean);
                }
            }
        }
        return newList;
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("ERREUR_AUCUNERENTE_SUPPRESSIONRENTES25ANSPROCESS");
    }

    @Override
    public String getName() {
        return RESuppressionRente25AnsProcess.class.getName();
    }

    @Override
    protected String getSessionApplicationName() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    private List<IREEcheances> listerEcheances25AnsProcess() throws Exception {

        REAnalyseurEcheancesFactory factory = new REAnalyseurEcheancesFactory(getSession());
        REAnalyseurEcheances analyseur = factory.getInstance(getMoisTraitement(), TypeAnalyseurEcheances.Echeance25ans);

        REEcheancesManager manager = new REEcheancesManager(getSession(), getMoisTraitement());
        manager.find(BManager.SIZE_NOLIMIT);

        List<IREEcheances> echeances = new ArrayList<IREEcheances>();

        for (int i = 0; i < manager.size(); i++) {
            IREEcheances uneEcheance = (IREEcheances) manager.get(i);
            for (REReponseModuleAnalyseEcheance uneReponse : analyseur.analyserEcheance(uneEcheance)) {
                switch (uneReponse.getMotif()) {
                    case Echeance25ans:
                        // case Echeance25ansDepassee: -> retiré car on ne devrait jamais reprendre les 25 ans qui sont
                        // dépassés. Ces cas nécessitent un traitement manuel.
                    case Echeance25ansRenteBloquee:
                        echeances.add(uneEcheance);
                        break;

                    default:
                        break;
                }
            }
        }
        return echeances;
    }

    @Override
    protected void runProcess() throws Exception {
        // Control si l'autorisation des validations est ok
        if (!REPmtMensuel.isValidationDecisionAuthorise(getSession())) {
            throw new RETechnicalException(getSession().getLabel("JSP_RE_VALIDATION_DECISIONS_INTERDITES_ECHEANCES"));
        }

        // Lister les échéances
        List<IREEcheances> echeances = listerEcheances25AnsProcess();

        if (!echeances.isEmpty()) {
            // Supprimer les échéances
            REDiminutionListeRenteAccordeeProcess process = new REDiminutionListeRenteAccordeeProcess();
            process.setSession(getSession());
            process.setEMailAddress(getEmailAddress());

            process.setCsCodeMutation(IRERenteAccordee.CS_CODE_MUTATION_EVENEMENT_TOUCHANT_PROCHE);
            process.setCsCodeTraitement("");

            process.setListeEcheances(convertToOldEcheance(echeances));
            process.setDateFinDroit(getMoisTraitement());

            process.start();
        }
    }
}

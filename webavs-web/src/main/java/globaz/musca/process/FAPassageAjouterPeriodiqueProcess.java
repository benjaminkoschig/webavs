package globaz.musca.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAFacturationExt;
import globaz.musca.db.facturation.FAFacturationExtManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APISection;
import globaz.pyxis.db.tiers.TIRole;
import java.util.ArrayList;
import java.util.List;

public class FAPassageAjouterPeriodiqueProcess extends FAPassageAjouterTableProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String TYPE_FACTU_PERIODIQUE = "P";
    private List<String> categories;
    private String numDecompte = "";
    private boolean paritaire = false;
    private FAPassage passage = null;
    private boolean personnel = false;

    /**
     * Nettoyage après erreur ou exécution.
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        BStatement statement = null;
        FAFacturationExtManager mgr = new FAFacturationExtManager();
        try {
            //
            passage = new FAPassage();
            passage.setSession(getSession());
            passage.setIdPassage(getNumPassage());
            passage.retrieve(getTransaction());
            if (passage.isNew()) {
                throw new Exception("Le passage '" + getNumPassage() + "' n'existe pas");
            }
            //
            String periode = passage.getDatePeriode();
            if ((periode == null) || (periode.length() != 7)) {
                throw new Exception("La période '" + periode + "' du passage '" + getNumPassage() + "' est invalide");
            }
            numDecompte = periode.substring(3) + periode.substring(0, 2) + "000";
            //
            checkParitairePersonnel();
            if (!paritaire && !personnel) {
                getMemoryLog()
                        .logMessage(
                                "Il ne s'agit pas d'une facturation périodique; impossible d'importer les éléments de facturation externe",
                                FWMessage.ERREUR, "FAPassageAjouterPeriodiqueProcess._executeProcess()");
                return false;
            }
            mgr.setSession(getSession());
            mgr.setForTypeFactu(FAPassageAjouterPeriodiqueProcess.TYPE_FACTU_PERIODIQUE);
            statement = mgr.cursorOpen(getTransaction());
            FAEnteteFacture enteteFacture = null;
            while (((entity = (FAFacturationExt) mgr.cursorReadNext(statement)) != null) && !isAborted()) {
                enteteFacture = _getEnteteFacturePeriodique(entity);
                if (enteteFacture != null) {
                    // on rajoute l'affact dans l'en-tête de facture trouvée
                    super.createLigneFacture(enteteFacture);
                } else {
                    continue;
                }

            }
            mgr.cursorClose(statement);
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                    "FAPassageAjouterPeriodiqueProcess._executeProcess()");
            if (entity != null) {
                getMemoryLog().logMessage("> " + entity.getNumAffilie(), FWMessage.INFORMATION,
                        "FAPassageAjouterPeriodiqueProcess._executeProcess()");
            }
            return false;
        } finally {
            if (statement != null) {
                mgr.cursorClose(statement);
                statement.closeStatement();
            }
        }
        return true;
    }

    /**
     * Exécute le processus de facturation en appelant la méthode protected
     */
    @Override
    public boolean _executeProcessFacturation() throws Exception {
        return _executeProcess();
    }

    protected FAEnteteFacture _getEnteteFacturePeriodique(FAFacturationExt entity) throws Exception {
        if (entity != null) {
            FAEnteteFactureManager mgr = new FAEnteteFactureManager();
            mgr.setSession(getSession());
            mgr.setForIdExterneRole(entity.getNumAffilie());
            mgr.setForIdPassage(getNumPassage());
            mgr.setForIdRole(entity.getRole());
            if (!JadeStringUtil.isIntegerEmpty(entity.getNumPeriode())) {
                mgr.setForIdExterneFacture(entity.getNumPeriode());
            }
            mgr.find(getTransaction());
            for (int i = 0; i < mgr.size(); i++) {
                FAEnteteFacture ent = (FAEnteteFacture) mgr.getEntity(i);
                if (isDecomptePeriodique(ent.getIdSousType()) || !JadeStringUtil.isIntegerEmpty(entity.getNumPeriode())) {
                    return ent;
                }
            }
        }
        // Création du décompte si non trouvé
        if ((CaisseHelperFactory.CS_AFFILIE_PARITAIRE.equals(entity.getRole()) && paritaire)
                || (CaisseHelperFactory.CS_AFFILIE_PERSONNEL.equals(entity.getRole()) && personnel)
                || TIRole.CS_AFFILIE.equals(entity.getRole())) {
            if (JadeStringUtil.isIntegerEmpty(entity.getNumPeriode())) {
                entity.setNumPeriode(numDecompte);
            }
            FAEnteteFacture enteteFac = new FAEnteteFacture();
            enteteFac.setSession(getSession());
            enteteFac.setIdPassage(getNumPassage());
            enteteFac.setIdExterneRole(entity.getNumAffilie());
            enteteFac.setIdTiers(getIdTiers(entity.getNumAffilie()));
            enteteFac.setIdRole(entity.getRole());
            enteteFac.setIdAdresse(getIdAdresse(entity.getNumAffilie()));
            enteteFac.setIdTypeFacture("1");
            enteteFac.setIdSousType(getIdSousType(entity.getNumPeriode()));
            enteteFac.setIdExterneFacture(entity.getNumPeriode());
            enteteFac.setNonImprimable(new Boolean(false));
            enteteFac.setIdSoumisInteretsMoratoires("229001");
            enteteFac.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_AUTOMATIQUE);
            // DGI init plan
            enteteFac.initDefaultPlanValue(enteteFac.getIdRole());
            enteteFac.add(getTransaction());
            return enteteFac;
        }
        return null;
    }

    private void checkParitairePersonnel() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getNumPassage())) {
            FAModulePassageManager modules = new FAModulePassageManager();
            modules.setSession(getSession());
            modules.setForIdPassage(getNumPassage());
            modules.find(getTransaction());
            for (int i = 0; i < modules.size(); i++) {
                FAModulePassage module = (FAModulePassage) modules.getEntity(i);
                if (FAModuleFacturation.CS_MODULE_PERIODIQUE_PARITAIRE.equals(module.getIdTypeModule())) {
                    paritaire = true;
                } else if (FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS.equals(module.getIdTypeModule())
                        || FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_IND.equals(module.getIdTypeModule())
                        || FAModuleFacturation.CS_MODULE_PERIODIQUE_PERS_NAC.equals(module.getIdTypeModule())) {
                    personnel = true;
                }
            }
        }

    }

    public boolean isDecomptePeriodique(String categorie) {
        if (JadeStringUtil.isIntegerEmpty(categorie)) {
            return false;
        }
        if (categories == null) {
            categories = new ArrayList<String>();
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_JANVIER);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_FEVRIER);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_MARS);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_AVRIL);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_MAI);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUIN);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUILLET);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_AOUT);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_SEPTEMBRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_OCTOBRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_NOVEMBRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_1SEMESTRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_2SEMESTRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_1TRIMESTRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_2TRIMESTRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_3TRIMESTRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_4TRIMESTRE);
            categories.add(APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE);
        }
        return categories.contains(categorie);
    }

}

package globaz.draco.process;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.services.DSDeclarationServices;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.nombreAssures.AFNombreAssures;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.naos.translation.CodeSystem;

public class DSRattrapageFFPP extends BProcess {

    private static final long serialVersionUID = 6057839784628068837L;
    public final static int NO_ERROR = 0;
    public final static int ON_ERROR = 200;

    private String annee = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        int nbTreat = 0;
        try {
            DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
            dsMgr.setSession(getSession());
            dsMgr.setForAnnee(annee);
            dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
            dsMgr.setForEtat(DSDeclarationViewBean.CS_COMPTABILISE);
            dsMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            dsMgr.find();
            System.out.println("nb a traiter : " + dsMgr.size());

            for (int i = 0; i < dsMgr.size(); i++) {
                nbTreat++;
                if (nbTreat % 100 == 0) {
                    System.out.println("Traités : " + nbTreat);
                }
                DSDeclarationViewBean decl = (DSDeclarationViewBean) dsMgr.get(i);
                int nbSalaries = decl.nombreInscriptions();
                // Si 0, on ne met pas à jour pour ne pas écraser la reprise des
                // données, seuls les entêtes DS ont été reprises à la FER
                if (JadeStringUtil.isBlankOrZero(decl.getNbPersonnel()) && (nbSalaries > 0)) {
                    AFCotisationManager cotisations = new AFCotisationManager();
                    cotisations.setSession((BSession) getSessionNaos(getSession()));
                    cotisations.setForAffiliationId(decl.getAffiliationId());
                    cotisations.setForNotMotifFin(globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION);
                    cotisations.setForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                    cotisations.setForAnneeActive(decl.getAnnee());
                    cotisations.find(getTransaction());
                    for (int j = 0; j < cotisations.size(); j++) {
                        AFCotisation coti = (AFCotisation) cotisations.getEntity(j);
                        // Si le comptage existe déjà pour l'année et l'affilié,
                        // on met à jour, sinon on ajoute
                        AFNombreAssuresManager mgrNbr = new AFNombreAssuresManager();
                        mgrNbr.setSession((BSession) getSessionNaos(getSession()));
                        mgrNbr.setForAffiliationId(decl.getAffiliation().getAffiliationId());
                        mgrNbr.setForAnnee(decl.getAnnee());
                        mgrNbr.setForAssuranceId(coti.getAssuranceId());
                        mgrNbr.find(getTransaction());
                        AFNombreAssures nbAssure = null;
                        String nbPersonnel = decl.getNbPersonnel();
                        if (JadeStringUtil.isBlankOrZero(nbPersonnel)) {
                            nbPersonnel = String.valueOf(DSDeclarationServices.countNbrAssuresByCantonForDeclararion(
                                    getSession(), coti.getAssurance().getAssuranceCanton(), decl.getIdDeclaration()));
                        }
                        if (!JadeStringUtil.isBlankOrZero(nbPersonnel)) {
                            if (mgrNbr.size() > 0) {
                                nbAssure = (AFNombreAssures) mgrNbr.getFirstEntity();
                                nbAssure.setNbrAssures(nbPersonnel);
                                nbAssure.update(getTransaction());
                            } else {
                                nbAssure = new AFNombreAssures();
                                nbAssure.setSession((BSession) getSessionNaos(getSession()));
                                nbAssure.setAssuranceId(coti.getAssuranceId());
                                nbAssure.setAnnee(decl.getAnnee());
                                nbAssure.setAffiliationId(decl.getAffiliationId());
                                nbAssure.setNbrAssures(nbPersonnel);
                                nbAssure.add(getTransaction());
                            }
                        }
                        if (!getTransaction().hasErrors()) {
                            getTransaction().commit();
                        } else {
                            getTransaction().rollback();
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        System.out.println(nbTreat);
        return true;
    }

    public String getAnnee() {
        return annee;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    public BISession getSessionNaos(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionNaos");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("NAOS").newSession(local);
            local.setAttribute("sessionNaos", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

}

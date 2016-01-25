package globaz.corvus.process;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REConcordanceCentrale;
import globaz.corvus.db.rentesaccordees.REConcordanceCentraleManager;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonceLight;
import globaz.hermes.application.HEApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author SCR
 * 
 */

public class REConcordanceCentraleProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List annoncesAEnvoyer = new ArrayList();
    private Map attributs = null;
    String emailObject = "";
    private String moisAnnee_MMxAAAA = "";

    IHEInputAnnonceLight remoteEcritureAnnonce = null;

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        BISession remoteSession = PRSession.connectSession(getSession(), HEApplication.DEFAULT_APPLICATION_HERMES);
        // création de l'API
        remoteEcritureAnnonce = (IHEInputAnnonceLight) remoteSession.getAPIFor(IHEInputAnnonceLight.class);

        BITransaction tr2 = getSession().newTransaction();
        boolean succes = true;

        int iTot = 0;
        int LIMIT = 500;

        try {
            if (!tr2.isOpened()) {
                tr2.openTransaction();
            }

            // TODO Vider la table des annonces avant envoi ???? voir avec jmc

            REConcordanceCentraleManager mgr = new REConcordanceCentraleManager();
            mgr.setSession(getSession());
            mgr.setForDate(getMoisAnnee_MMxAAAA());
            BStatement statement = mgr.cursorOpen(getTransaction());
            REConcordanceCentrale cc = null;

            int i = 0;
            int nbrTot = 0;

            while ((cc = (REConcordanceCentrale) mgr.cursorReadNext(statement)) != null) {
                i++;
                iTot++;
                System.out.println(i);

                // Droit 10ème révision
                if ("10".equals(cc.getDroitApplique())) {
                    doTraitementARC46(cc);
                }
                // Droit 9ème révision
                else if ("09".equals(cc.getDroitApplique()) || "9".equals(cc.getDroitApplique())) {
                    doTraitementARC43(cc);
                } else {
                    throw new Exception("Concordance avec la centrale : Droit appliqué inconnu, idBC/idRA = "
                            + cc.getIdBaseCalcul() + "/" + cc.getIdRenteAccordee());
                }
                // On commit tous les 5000 annonces
                if (i >= LIMIT) {
                    nbrTot += envoieAnnonces(tr2);
                    i = 0;
                    annoncesAEnvoyer.clear();
                    tr2.commit();
                }
            }

            nbrTot += envoieAnnonces(tr2);
            i = 0;
            tr2.commit();
            mgr.cursorClose(statement);
            getMemoryLog().logMessage(
                    getSession().getLabel("CONCORDANCE_CENTRALE_NBR_ANNONCE_ENVOYE") + " : " + String.valueOf(nbrTot),
                    FWMessage.INFORMATION, this.getClass().toString());
            return true;
        }

        // En cas d'erreur, tous ne sera pas rollbacké. (Rollback du dernier lot
        // de 5000 uniquement.)
        catch (Exception e) {
            succes = false;
            String msg = "";

            // Pas de gestion de rollback, en cas d'erreur on avertit simplement
            // que tous n'as pas été rollbacké.
            //
            if (iTot > LIMIT) {
                msg = "!!! Rollback partiel des " + LIMIT + " derniers enregistrements uniquement. !!!";
            }
            getMemoryLog().logMessage(e.getMessage() + msg, FWMessage.ERREUR, this.getClass().toString());
            if (tr2.hasErrors()) {
                getMemoryLog().logMessage(tr2.getErrors().toString(), FWMessage.ERREUR, this.getClass().toString());
            }
            if (getSession().hasErrors()) {
                getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                        this.getClass().toString());
            }
            try {
                tr2.rollback();
            } catch (Exception e1) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().toString());
            }
            return false;
        } finally {
            if (succes) {
                emailObject = getSession().getLabel("CONCORDANCE_CENTRALE_OK") + " (" + getMoisAnnee_MMxAAAA() + ")";
            } else {
                emailObject = getSession().getLabel("CONCORDANCE_CENTRALE_KO") + " (" + getMoisAnnee_MMxAAAA() + ")";
            }
            try {
                if (tr2 != null) {
                    tr2.closeTransaction();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (JadeStringUtil.isBlankOrZero(getMoisAnnee_MMxAAAA())) {
            throw new Exception("La date est obligatoire, format : [mm.aaaa]");
        }
        if (JadeStringUtil.isBlankOrZero(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getMoisAnnee_MMxAAAA()))) {
            throw new Exception("La date est obligatoire, format : [mm.aaaa]");
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    private void doTraitementARC43(REConcordanceCentrale cc) throws Exception {

        attributs = new HashMap();
        envoieChamp(IHEAnnoncesViewBean.CODE_APPLICATION, "43");
        envoieChamp(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");

        String nc = formatXPosAppendWithZero(3, true,
                CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()));
        String na = formatXPosAppendWithZero(3, true,
                CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()));
        envoieChamp(IHEAnnoncesViewBean.NUMERO_CAISSE, formatXPosAppendWithBlank(3, true, nc));
        envoieChamp(IHEAnnoncesViewBean.NUMERO_AGENCE, formatXPosAppendWithBlank(3, true, na));

        // no de l'annonce ????

        envoieChamp(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                formatXPosAppendWithBlank(20, false, "RENTES CONCORDANCE"));

        String nss = cc.getNss();
        nss = JadeStringUtil.change(nss, ".", "");
        envoieChamp(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT, formatXPosAppendWithBlank(11, false, nss));
        envoieChamp(IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION,
                formatXPosAppendWithBlank(2, true, cc.getCodePrestation()));

        FWCurrency c = new FWCurrency(cc.getMontant());
        String s = String.valueOf(c.intValue());
        envoieChamp(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS, formatXPosAppendWithZero(5, true, s));
        envoieChamp(
                IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT,
                formatXPosAppendWithBlank(4, false, PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                        .convertDate_MMxAAAA_to_AAAAMM(getMoisAnnee_MMxAAAA()))));
        envoieChamp(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION, "99");

        annoncesAEnvoyer.add(attributs);

    }

    private void doTraitementARC46(REConcordanceCentrale cc) throws Exception {
        attributs = new HashMap();
        envoieChamp(IHEAnnoncesViewBean.CODE_APPLICATION, "46");
        envoieChamp(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");

        String nc = formatXPosAppendWithZero(3, true,
                CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()));
        String na = formatXPosAppendWithZero(3, true,
                CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()));
        envoieChamp(IHEAnnoncesViewBean.NUMERO_CAISSE, formatXPosAppendWithBlank(3, true, nc));
        envoieChamp(IHEAnnoncesViewBean.NUMERO_AGENCE, formatXPosAppendWithBlank(3, true, na));

        // no de l'annonce ????

        envoieChamp(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                formatXPosAppendWithBlank(20, false, "RENTES CONCORDANCE"));
        String nss = cc.getNss();
        nss = JadeStringUtil.change(nss, ".", "");
        envoieChamp(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT, formatXPosAppendWithBlank(11, false, nss));
        envoieChamp(IHEAnnoncesViewBean.CS_GENRE_DE_SERVICE_PRESTATION,
                formatXPosAppendWithBlank(2, true, cc.getCodePrestation()));
        FWCurrency c = new FWCurrency(cc.getMontant());
        String s = String.valueOf(c.intValue());
        envoieChamp(IHEAnnoncesViewBean.CS_MENSUALITE_PRESTATION_FRANCS, formatXPosAppendWithZero(5, true, s));
        envoieChamp(
                IHEAnnoncesViewBean.CS_MOIS_DU_RAPPORT,
                formatXPosAppendWithBlank(4, false, PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                        .convertDate_MMxAAAA_to_AAAAMM(getMoisAnnee_MMxAAAA()))));
        envoieChamp(IHEAnnoncesViewBean.CS_CODE_DE_MUTATION, "99");
        annoncesAEnvoyer.add(attributs);
    }

    private int envoieAnnonces(BITransaction tr) throws Exception {

        Iterator iter = annoncesAEnvoyer.iterator();
        int counter = 0;
        while (iter.hasNext()) {
            remoteEcritureAnnonce.clear();
            remoteEcritureAnnonce.setIdProgramme(REApplication.DEFAULT_APPLICATION_CORVUS);
            remoteEcritureAnnonce.setUtilisateur(getSession().getUserId());
            remoteEcritureAnnonce.setStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);
            Map element = (Map) iter.next();
            remoteEcritureAnnonce.putAll(element);
            remoteEcritureAnnonce.add(tr);
            System.out.println("idArc = " + remoteEcritureAnnonce.getIdAnnonce());
            counter++;
        }

        return counter;
    }

    private void envoieChamp(String clefAttribut, String valeurAttribut) {

        if (!JadeStringUtil.isEmpty(valeurAttribut)) {
            attributs.put(clefAttribut, valeurAttribut);
        }

    }

    private String formatXPosAppendWithBlank(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append(" ");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

    private String formatXPosAppendWithZero(int nombrePos, boolean isAppendLeft, String value) {

        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append("0");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
            }
        }
        return result.toString();
    }

    @Override
    protected String getEMailObject() {
        return emailObject;
    }

    public String getMoisAnnee_MMxAAAA() {
        return moisAnnee_MMxAAAA;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setMoisAnnee_MMxAAAA(String moisAnnee_MMxAAAA) {
        this.moisAnnee_MMxAAAA = moisAnnee_MMxAAAA;
    }

}

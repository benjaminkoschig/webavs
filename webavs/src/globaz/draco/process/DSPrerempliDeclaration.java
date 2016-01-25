/*
 * Créé le 6 mars 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.process;

import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.draco.db.preimpression.DSPreImpressionDeclarationListViewBean;
import globaz.draco.db.preimpression.DSPreImpressionViewBean;
import globaz.draco.util.DSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;

public class DSPrerempliDeclaration extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DSInscriptionsIndividuellesListeViewBean declaration = null;

    private String idDeclaration = "";

    public DSPrerempliDeclaration() {
        super();
    }

    /**
     * @param parent
     */
    public DSPrerempliDeclaration(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public DSPrerempliDeclaration(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // On retrouve la déclaration
        declaration = new DSInscriptionsIndividuellesListeViewBean();
        declaration.setSession(getSession());
        declaration.setIdDeclaration(getIdDeclaration());
        declaration.retrieve(getTransaction());
        if (declaration.isNew()) {
            getMemoryLog().logMessage(getSession().getLabel("DECL_NON_EXISTANTE"), FWViewBeanInterface.ERROR, "");
            abort();
            return false;
        }
        if (!DSDeclarationViewBean.CS_OUVERT.equals(declaration.getEtat())) {
            getMemoryLog().logMessage(getSession().getLabel("DECL_ETAT_NON_OUVERT"), FWViewBeanInterface.ERROR, "");
            abort();
            return false;
        }
        boolean isDateNNssProd = DSUtil.isNNSSActif(getSession(), JACalendar.todayJJsMMsAAAA());

        // On importe déjà les provisoires
        importeInscriptionsProvisoires();

        // On préRempli la déclaration sur la base l'année précédente
        preReplirDSSelonAnneePrecedente(isDateNNssProd);

        if (!isOnError()) {
            setSendCompletionMail(false);
        }
        return !isOnError();
    }

    private void preReplirDSSelonAnneePrecedente(boolean isDateNNssProd) throws Exception {
        DSPreImpressionDeclarationListViewBean mgrPreRempli = new DSPreImpressionDeclarationListViewBean();
        mgrPreRempli.setSession(getSession());
        int anneeEnCours = Integer.parseInt(declaration.getAnnee());
        mgrPreRempli.setAnneeDeclaration(anneeEnCours - 1);
        // L'année est année -1, pour aller rechercher les insc. ci de l'année
        // d'avant
        mgrPreRempli.setAnnee(declaration.getAnnee());
        mgrPreRempli.setForAffiliesNumero(declaration.getAffiliationId());
        mgrPreRempli.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgrPreRempli.find(getTransaction());
        // maintenant, on ajoute les inscriptions
        for (int i = 0; i < mgrPreRempli.size(); i++) {
            DSPreImpressionViewBean lignePre = (DSPreImpressionViewBean) mgrPreRempli.get(i);
            ajoutInscriptionDSIndividuelle(isDateNNssProd, lignePre);
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
        }
    }

    private void ajoutInscriptionDSIndividuelle(boolean isDateNNssProd, DSPreImpressionViewBean lignePre)
            throws Exception {
        DSInscriptionsIndividuelles inscription = new DSInscriptionsIndividuelles();
        inscription.setDeclaration(declaration);
        inscription.setSession(getSession());
        if (!JadeStringUtil.isIntegerEmpty(lignePre.getCompteIndividuelId())
                && !JadeStringUtil.isIntegerEmpty(lignePre.getNumeroAvs())) {
            String CIid = returnCIId(lignePre.getNumeroAvs());
            if ((CIid != null) && isDateNNssProd && (13 != lignePre.getNumeroAvs().length())) {
                inscription.setCompteIndividuelId(CIid);
                inscription.setNumeroAvsNNSS("true");
                inscription.setIsNNNSS(new Boolean(true));
            } else {
                if (13 == NSUtil.unFormatAVS(lignePre.getNumeroAvs()).length()) {
                    inscription.setNumeroAvsNNSS("true");
                    inscription.setIsNNNSS(new Boolean(true));
                }
                inscription.setCompteIndividuelId(lignePre.getCompteIndividuelId());
            }
            // inscription.setCompteIndividuelId(lignePre.getCompteIndividuelId());
            inscription.setIdDeclaration(declaration.getIdDeclaration());
            // On fait un simple add, car on a l'id du CI et on ne veut pas
            // calculer l'AC
            inscription.setCasSpecial(new Boolean(false));
            inscription.setNotWantCI(true);
            inscription.wantCallValidate(false);
            inscription.wantCallMethodAfter(false);
            inscription.add(getTransaction());
        }
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError()) {
            return "Le préremblissage de la déclaration " + declaration.getAffiliation().getAffilieNumero() + " / "
                    + declaration.getAnnee() + " s'est effectué avec succès";
        } else {
            return "Le préremplissage" + declaration.getAffiliation().getAffilieNumero() + " / "
                    + declaration.getAnnee() + " de la déclaration a échoué";
        }

    }

    /**
     * @return
     */
    public String getIdDeclaration() {
        return idDeclaration;
    }

    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Méthode qui insère les écritures provisoires de l'année dans la déclaration
     * 
     * @throws Exception
     */
    private void importeInscriptionsProvisoires() throws Exception {
        if (JadeStringUtil.isBlankOrZero(declaration.getAffiliationId())) {
            return;
        }
        CIEcritureManager mgrEcrCI = new CIEcritureManager();
        mgrEcrCI.setSession((BSession) getSessionCI(getSession()));
        mgrEcrCI.setForAffilie(declaration.getNumeroAffilie());
        mgrEcrCI.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgrEcrCI.setForAnnee(declaration.getAnnee());
        mgrEcrCI.setForCode(CIEcriture.CS_CODE_PROVISOIRE);
        mgrEcrCI.find(getTransaction());

        for (int i = 0; i < mgrEcrCI.size(); i++) {
            CIEcriture ecrCI = (CIEcriture) mgrEcrCI.getEntity(i);
            DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
            insc.setSession(getSession());
            insc.setIdEcrtirueCI(ecrCI.getEcritureId());
            insc.setCompteIndividuelId(ecrCI.getCompteIndividuelId());
            insc.setMontant(ecrCI.getMontant());
            insc.setIdDeclaration(declaration.getIdDeclaration());
            insc.setMontantAf(ecrCI.getMontantSigne());
            // insc.setNotWantCI(true);
            insc.setDeclaration(declaration);
            insc.setNumeroAvs(ecrCI.getAvs());
            insc.setCategoriePerso(ecrCI.getCategoriePersonnel());
            insc.setAnneeInsc(ecrCI.getAnnee());
            insc.setProvisoire(true);
            try {
                insc.add(getTransaction());
                getMemoryLog().logMessage(
                        "Le numéro : " + JAStringFormatter.formatAVS(ecrCI.getAvs())
                                + " a été réaffecté à la déclaration", FWMessage.INFORMATION,
                        "Pré-remplissage de la déclaration");
                if (!getTransaction().hasErrors()) {
                    declaration.retrieve(getTransaction());
                    declaration.setIdJournal(insc.donneIdJournal(getTransaction()));
                    declaration.wantCallMethodAfter(false);
                    declaration.wantCallMethodBefore(false);
                    declaration.update(getTransaction());
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
        }

    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public String returnCIId(String NAVS) {
        try {
            String NNSS = NSUtil.returnNNSS(getSession(), NAVS);
            CICompteIndividuel nsschek = CICompteIndividuel.loadCI(NNSS, getTransaction());
            return nsschek.getCompteIndividuelId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param string
     */
    public void setIdDeclaration(String string) {
        idDeclaration = string;
    }

}

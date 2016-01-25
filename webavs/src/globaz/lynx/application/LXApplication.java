package globaz.lynx.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BApplication;
import globaz.jade.log.JadeLogger;
import globaz.lynx.format.facture.ILXFactureFormat;
import globaz.lynx.format.fournisseur.ILXFournisseurFormat;
import globaz.lynx.format.societe.ILXSocieteDebitriceFormat;
import java.io.Serializable;

public class LXApplication extends BApplication implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_APPLICATION_LYNX = "LYNX";
    public static final String DEFAULT_LYNX_ROOT = "lynxRoot";

    public static final int DEFAULT_MAX_ROWS = 19;
    public static final int DEFAULT_SHOW_ROWS = 3;
    public static final String KEY_COPIER_NUMERO_FACTURE_INTERNE = "copierNumeroFactureInterne";

    public static final String KEY_FORMAT_NUMERO_FACTURE_INTERNE = "formatNumeroFactureInterne";
    public static final String KEY_FORMAT_NUMERO_FOURNISSEUR = "formatNumeroFournisseur";

    public static final String KEY_FORMAT_NUMERO_SOCIETE_DEBITRICE = "formatNumeroSocieteDebitrice";

    public LXApplication() throws Exception {
        super(LXApplication.DEFAULT_APPLICATION_LYNX);
    }

    /**
     * @see BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        // Nothing yet
    }

    /**
     * @see BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        try {
            FWMenuCache.getInstance().addFile("LYNXMenu.xml");
        } catch (Exception e) {
            JadeLogger.error(this, "LYNXMenu.xml non résolu : " + e.toString());
        }

    }

    /**
     * @see BApplication#_initializeCustomActions()
     */
    @Override
    protected void _initializeCustomActions() {
        FWAction.registerActionCustom("lynx.impression.impression.imprimerBalance", FWSecureConstants.READ);
        FWAction.registerActionCustom("lynx.impression.impression.imprimerBalanceAgee", FWSecureConstants.READ);
        FWAction.registerActionCustom("lynx.impression.impression.imprimerGrandLivre", FWSecureConstants.READ);

        FWAction.registerActionCustom("lynx.journal.journal.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("lynx.journal.journal.comptabiliser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("lynx.journal.journal.annuler", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("lynx.notedecredit.noteDeCredit.encaisser", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("lynx.notedecredit.noteDeCredit.extourner", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("lynx.ordregroupe.ordreGroupe.imprimer", FWSecureConstants.READ);
        FWAction.registerActionCustom("lynx.ordregroupe.ordreGroupe.preparation", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("lynx.ordregroupe.ordreGroupe.execution", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("lynx.ordregroupe.ordreGroupe.annuler", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("lynx.recherche.rechercheDetail.chercherDetail", FWSecureConstants.READ);

        FWAction.registerActionCustom("naos.affiliation.affiliation.gedafficherdossier", FWSecureConstants.READ);

        FWAction.registerActionCustom("lynx.canevas.canevas.utiliser", FWSecureConstants.UPDATE);
    }

    /**
     * Return le formateur du numéro de facture interne.
     * 
     * @return
     * @throws Exception
     */
    public ILXFactureFormat getNumeroFactureFormatter() throws Exception {
        return (ILXFactureFormat) Class.forName(this.getProperty(LXApplication.KEY_FORMAT_NUMERO_FACTURE_INTERNE))
                .newInstance();
    }

    /**
     * Return le formateur du numéro de fournisseur
     * 
     * @return
     * @throws Exception
     */
    public ILXFournisseurFormat getNumeroFournisseurFormatter() throws Exception {
        return (ILXFournisseurFormat) Class.forName(this.getProperty(LXApplication.KEY_FORMAT_NUMERO_FOURNISSEUR))
                .newInstance();
    }

    /**
     * Return le formateur du numéro de société débitrice
     * 
     * @return
     * @throws Exception
     */
    public ILXSocieteDebitriceFormat getNumeroSocieteDebitriceFormatter() throws Exception {
        return (ILXSocieteDebitriceFormat) Class.forName(
                this.getProperty(LXApplication.KEY_FORMAT_NUMERO_SOCIETE_DEBITRICE)).newInstance();
    }

    public boolean isCopierNumeroFactureInterne() {
        return Boolean.valueOf(this.getProperty(LXApplication.KEY_COPIER_NUMERO_FACTURE_INTERNE, "false"));
    }
}

package globaz.aquila.print;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.process.interetmanuel.visualcomponent.CAInteretManuelVisualComponent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * <p>
 * Formule 44 ou Reclamer frais et interets
 * </p>
 * 
 * @author Alexandre Cuva, 19-aug-2004
 */
public class CO04ReceptionPaiement extends CODocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String CENT_DEFAUT = "XX";
    public static final String MONTANT_DEFAUT = "XXXX";
    public static final String NUMERO_REFERENCE_INFOROM = "0040GCO";
    public static final String OCRB_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static final String REFERENCE_NON_FACTURABLE_DEFAUT = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final long serialVersionUID = -1361271953197021651L;
    private static final String TEMPLATE_NAME = "CO_04_RECEPTION_PAIEMENT_AF_BVR";

    private List<CAInteretManuelVisualComponent> interetCalcule = null;

    /**
     * Crée une nouvelle instance de la classe CO04ReceptionPaiement.
     * 
     * @throws Exception
     */
    public CO04ReceptionPaiement() throws Exception {
    }

    /**
     * Initialise le document.
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CO04ReceptionPaiement(BSession parent) throws FWIException {
        super(parent);
    }

    /**
     * Ajoute les IM facturé à cette étape au dataSource
     * 
     * @param dataSource
     * @return montant total des IM
     */
    private FWCurrency addMontantIM(List<Map<String, String>> dataSource) {
        FWCurrency totalIM = new FWCurrency("0");
        for (CAInteretManuelVisualComponent im : getInteretCalcule()) {
            Map<String, String> f = new HashMap<String, String>();
            f.put(COParameter.F1, im.getInteretMoratoire().getRubrique().getDescription(getLangue()));
            f.put(COParameter.F2, formatMontant(im.montantInteretTotalCalcule()));
            f.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));

            dataSource.add(f);
            totalIM.add(new FWCurrency(im.montantInteretTotalCalcule()));
        }
        return totalIM;
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        getDocumentInfo().setDocumentProperty("annee", getAnneeFromContentieux());
    }

    /**
     * @throws FWIException
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();
        setTemplateFile(CO04ReceptionPaiement.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("AQUILA_RECEPTION_PAIEMENT"));
        setNumeroReferenceInforom(CO04ReceptionPaiement.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            // destinataire est l'affilie
            destinataireDocument = curContentieux.getCompteAnnexe().getTiers();
            _setLangueFromTiers(destinataireDocument);

            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(curContentieux, true, false, true, getAdressePrincipale(destinataireDocument));
            // -- titre du doc
            // ------------------------------------------------------------------------------
            // rechercher toutes les lignes du titre du document
            StringBuffer body = new StringBuffer();

            getCatalogueTextesUtil().dumpNiveau(getParent(), 1, body, "\n");

            // formater le titre, les conventions de remplacement pour les lignes du titre sont:
            // {0} = numéro de poursuite
            // {1} = période
            // {2} = date de la section
            this.setParametres(
                    COParameter.T1,
                    formatMessage(body,
                            new Object[] { curContentieux.getNumPoursuite(), curContentieux.getPeriodeSection(),
                                    curContentieux.getSection().getDescription(getLangue()),
                                    formatDate(curContentieux.getSection().getDateSection()) }));

            // -- corps du doc
            // ------------------------------------------------------------------------------
            // rechercher tous les paragraphes du corps du document
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 2, body, "\n\n");

            // formater le corps, les conventions de remplacement pour les paragraphes du corps sont:
            // {0} = formule de politesse (ex: Madame, Monsieur)
            // {1} = date de la de la section
            this.setParametres(
                    COParameter.T2,
                    formatMessage(body, new Object[] { getFormulePolitesse(destinataireDocument),
                            formatDate(curContentieux.getSection().getDateSection()) }));

            // -- lignes détail
            // ------------------------------------------------------------------------------
            String[] infoSection = CORequisitionPoursuiteUtil.getSoldeSectionInitial(getSession(),
                    curContentieux.getIdSection());

            this.setParametres(COParameter.P_BASE, formatMontant(infoSection[0]));
            this.setParametres(COParameter.P_DEVISE, getCatalogueTextesUtil().texte(getParent(), 3, 2));

            List<Map<String, String>> dataSource = new ArrayList<Map<String, String>>();

            dataSource.add(montantFactureInitiale(infoSection));

            List<Map<String, String>> situation = createSituationCompteDS(infoSection[2], COParameter.F1,
                    COParameter.F2, COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2),
                    ICOEtape.CS_FRAIS_ET_INTERETS_RECLAMES, infoSection[1]);
            dataSource.addAll(situation);

            FWCurrency totalIM = addMontantIM(dataSource);

            // -- lignes pour nouveaux frais
            FWCurrency totalNouvellesTaxes = addTaxesToDS(dataSource, COParameter.F1, COParameter.F2, COParameter.F3,
                    getCatalogueTextesUtil().texte(getParent(), 3, 2));

            this.setDataSource(dataSource);
            FWCurrency bvr;

            // -- pied après détail
            // ---------------------------------------------------------------------
            this.setParametres(COParameter.T7, getCatalogueTextesUtil().texte(getParent(), 3, 8));
            this.setParametres(COParameter.T6, getCatalogueTextesUtil().texte(getParent(), 3, 2));

            if (totalNouvellesTaxes != null) {
                totalNouvellesTaxes.add(curContentieux.getSolde());
                totalNouvellesTaxes.add(totalIM);
                this.setParametres(COParameter.M6, formatMontant(totalNouvellesTaxes.toString()));
                bvr = totalNouvellesTaxes;
            } else {
                totalIM.add(curContentieux.getSolde());
                this.setParametres(COParameter.M6, formatMontant(totalIM.toString()));
                bvr = totalIM;
            }

            // rechercher tous les paragraphes du pied après détail
            body.setLength(0);
            getCatalogueTextesUtil().dumpNiveau(getParent(), 4, body, "\n\n");

            initBVR(bvr);

            // formater le pied après détail, les conventions de remplacement pour les paragraphes sont:
            // {0} = délai
            // {1} = CCP de la caisse
            // {2} = formule de politesse (ex: Madame, Monsieur,)
            this.setParametres(
                    COParameter.T9,
                    formatMessage(body, new Object[] { getTransition().getDuree(), getNumeroCCP(),
                            getFormulePolitesse(destinataireDocument) }));
        } catch (Exception e) {
            this.log("exception: " + e.getMessage());
        }
    }

    /**
     * @return the interetCalcule
     */
    public List<CAInteretManuelVisualComponent> getInteretCalcule() {
        return interetCalcule;
    }

    /**
     * Initialise la partie du bulletin de versement du document
     * 
     * @param montantTotal
     * @throws Exception
     */
    private void initBVR(FWCurrency montantTotal) throws Exception {
        // -- BVR
        FWCurrency cMontant = new FWCurrency(0.00);
        cMontant.add(montantTotal);

        // Recherche les informations pour remplir le bordereau
        String montantSansCentime = JAUtil.createBigDecimal(cMontant.toString()).toBigInteger().toString();
        BigDecimal montantSansCentimeBigDecimal = JAUtil.createBigDecimal(montantSansCentime);
        BigDecimal montantAvecCentimeBigDecimal = JAUtil.createBigDecimal(cMontant.toString());
        String centimes = montantAvecCentimeBigDecimal.subtract(montantSansCentimeBigDecimal).toString()
                .substring(2, 4);
        montantSansCentime = globaz.globall.util.JANumberFormatter.formatNoRound(montantSansCentime);

        // commencer à écrire les paramètres
        String adresseDebiteur = "";
        try {
            adresseDebiteur = getAdressePrincipale(curContentieux.getCompteAnnexe().getTiers());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Informations pour la référence et l'OCRB
        CAReferenceBVR bvr = new CAReferenceBVR();
        bvr.setSession(getSession());
        bvr.setBVR(curContentieux.getSection(), cMontant.toString());
        try {
            super.setParametres(FWIImportParametre.PARAM_REFERENCE + "_X",
                    CO04ReceptionPaiement.REFERENCE_NON_FACTURABLE_DEFAUT);
            super.setParametres(COParameter.P_OCR + "_X", CO04ReceptionPaiement.OCRB_DEFAUT);
            super.setParametres(COParameter.P_FRANC + "_X", CO04ReceptionPaiement.MONTANT_DEFAUT);
            super.setParametres(COParameter.P_CENTIME + "_X", CO04ReceptionPaiement.CENT_DEFAUT);
            super.setParametres(COParameter.P_ADRESSE, bvr.getAdresseBVR());
            super.setParametres(COParameter.P_ADRESSECOPY, bvr.getAdresseBVR());
            super.setParametres(COParameter.P_COMPTE, bvr.getNumeroCC());// numéro
            // CC
            super.setParametres(COParameter.P_VERSE, bvr.getLigneReference() + "\n" + adresseDebiteur);
            super.setParametres(COParameter.P_PAR, adresseDebiteur);
            super.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());
            super.setParametres(COParameter.P_OCR, bvr.getOcrb());
            super.setParametres(COParameter.P_FRANC, JANumberFormatter.deQuote(montantSansCentime));
            super.setParametres(COParameter.P_CENTIME, centimes);
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr de la sommation : " + e1.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }
    }

    /**
     * @param infoSection
     * @return
     */
    private HashMap<String, String> montantFactureInitiale(String[] infoSection) {
        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put(COParameter.F1, curContentieux.getSection().getDescription(getLangue()));
        fields.put(COParameter.F2, formatMontant(infoSection[0]));
        fields.put(COParameter.F3, getCatalogueTextesUtil().texte(getParent(), 3, 2));
        return fields;
    }

    /**
     * @param interetCalcule
     *            the interetCalcule to set
     */
    public void setInteretCalcule(List<CAInteretManuelVisualComponent> interetCalcule) {
        this.interetCalcule = interetCalcule;
    }

}

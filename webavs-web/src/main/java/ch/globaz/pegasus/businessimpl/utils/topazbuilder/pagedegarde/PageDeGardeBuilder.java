package ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde;

import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.tiers.TITiers;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.TextMerger;
import ch.globaz.common.document.babel.BabelTextDefinition;
import ch.globaz.common.document.babel.TextMergerBabelTopaz;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.pegasus.businessimpl.utils.PegasusDateUtil;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.SingleTransfertPCAbstractBuilder;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Cette classe permet de créer une page de garde PC et de la peupler à l'aide des données fournies et du catalogue de
 * texte.
 * 
 * @author jwe
 * 
 */
public class PageDeGardeBuilder extends AbstractPegasusBuilder {
    private IPageDeGardeDefinition pageDeGardeText = null;
    private TITiers tiers = null;
    private TextGiver<BabelTextDefinition> textGiver = null;
    private TextMerger<BabelTextDefinition> textMerger;
    private JadeUser gestionnaire = null;
    private JadeUser preparateurDecision = null;
    private String NSSReference = "";
    private String dateDecision = null;
    private DocumentData data = new DocumentData();

    /**
     * Il s'agit de la méthode principale permettant de construire une page de garde
     * 
     * @param babelPageDeGardeDoc
     * @param pageDeGardeText
     * @param tiers
     * @param gestionnaire
     * @param NSSReference
     * @param dateDecision
     */
    public PageDeGardeBuilder(ICTDocument babelPageDeGardeDoc, IPageDeGardeDefinition pageDeGardeText, TITiers tiers,
            JadeUser gestionnaire, JadeUser preparateurDecision, String NSSReference, String dateDecision) {
        Checkers.checkNotNull(babelPageDeGardeDoc, "babelPageDeGardeDoc");
        Checkers.checkNotNull(pageDeGardeText, "pageDeGardeText");
        Checkers.checkNotNull(tiers, "tiers");
        Checkers.checkNotNull(gestionnaire, "gestionnaire");
        Checkers.checkNotNull(dateDecision, "dateDecision");
        // on ne renseigne le préparateur de la décision que lorsque l'on se trouve dans le cas d'une décision après
        // calcul
        if (pageDeGardeText instanceof PageDeGardeDefinitionForDAC) {
            Checkers.checkNotNull(preparateurDecision, "preparateurDecision");
            this.preparateurDecision = preparateurDecision;
        }

        this.pageDeGardeText = pageDeGardeText;
        this.tiers = tiers;
        this.gestionnaire = gestionnaire;
        this.NSSReference = NSSReference;
        this.dateDecision = dateDecision;

        textGiver = pageDeGardeText.getTextGiver();
        textGiver.setLangue(tiers.getLangueIso());
        data = new DocumentData();

        textMerger = new TextMergerBabelTopaz(textGiver, data);

    }

    /**
     * Permet de constituer la page de garde avec les données fournies
     * 
     * La page de garde sera construite en fonction de l'implémentation de IPageDeGardeDefinition qui lui sera fournie
     * 
     * @param data
     * @param babelPageDeGardeDoc
     * @param tiers
     * @return
     * @throws Exception
     */
    public DocumentData buildPageDeGarde() throws Exception {
        buildHeaderForPageGarde();
        String formulePolitesse = resolveFormulePolitesse();
        textMerger.addTextToDocument(pageDeGardeText.getPolitesse(), formulePolitesse);
        // Ligne intro
        textMerger.addTextToDocument(pageDeGardeText.getIntro());
        // Salutations
        String langueIso = tiers.getLangueIso();
        textMerger.addTextToDocument(pageDeGardeText.getSalutations(formulePolitesse, langueIso));
        // signature
        textMerger.addTextToDocument(pageDeGardeText.getSignature("STANDARD"));
        // Ajout d'un carriage return si description caisse sur deux lignes
        textMerger.addTextToDocument(pageDeGardeText.getSignatureNomCaisse(), PRStringUtils.replaceString(
                textGiver.resolveText(pageDeGardeText.getSignatureNomCaisse()), AbstractPegasusBuilder.CR_FROM_BABEL,
                AbstractPegasusBuilder.CR));

        textMerger.addTextToDocument(pageDeGardeText.getSignataire());
        textMerger.addTextToDocument(pageDeGardeText.getSignatureNomService());

        textMerger.addTextToDocument(pageDeGardeText.getSignatureGestionnaire(getUserNomFormatte()));

        textMerger.addTextToDocument(pageDeGardeText.getAnnexes());
        textMerger.addTextToDocument(pageDeGardeText.getAnnexesMent());

        return data;

    }

    /**
     * Permet de retourner la formule de politesse du tiers en fonction de sa langue
     * 
     * @param babelPageDeGardeDoc
     * @param tiers
     * @return la formule de politesse associée au tiers
     */
    private String resolveFormulePolitesse() {
        String formulePolitesse = "";
        try {
            formulePolitesse = tiers.getFormulePolitesse(LanguageResolver.resolveCodeSystemFromLanguage(tiers
                    .getLangue()));
        } catch (Exception e) {
            throw new CommonTechnicalException(
                    "An error happened while trying to get the title of the following tiers  [id :" + tiers.getId()
                            + "]");
        }

        if (formulePolitesse == null || JadeStringUtil.isBlankOrZero(formulePolitesse)) {
            // si on ne retrouve pas de formule de politesse par défaut pour le tiers, on retourne la formule de
            // politesse du catalogue de texte
            formulePolitesse = textGiver.resolveText(pageDeGardeText.getPolitesse());
        } else {
            formulePolitesse += ",";
        }

        return formulePolitesse;
    }

    /**
     * Permet de reconstituer les informations d'entêtes pour le document
     * 
     * @return
     * @throws Exception
     */
    private DocumentData buildHeaderForPageGarde() throws Exception {
        textMerger.addTextToDocument(pageDeGardeText.getHeader("STANDARD"));
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        textMerger.addTextToDocument(pageDeGardeText.getPersonneDeReference());
        textMerger.addTextToDocument(pageDeGardeText.getNomCollaborateur(nomCollabo));
        textMerger.addTextToDocument(pageDeGardeText.getTelCollaborateur(gestionnaire.getPhone()));
        textMerger.addTextToDocument(pageDeGardeText.getNomCollabo(nomCollabo));
        textMerger.addTextToDocument(pageDeGardeText.getTelCollabo(gestionnaire.getPhone()));

        textMerger.addTextToDocument(pageDeGardeText.getTelGestionnaire(gestionnaire.getPhone()));

        // Les informations suivantes ne sont renseignées que lorsque l'on créer une page de garde pour une décision
        // après calcul
        String nRef = gestionnaire.getVisa();

        if (pageDeGardeText instanceof PageDeGardeDefinitionForDAC) {
            String nomPrenomPreparateurDecision = preparateurDecision.getFirstname() + " "
                    + preparateurDecision.getLastname();
            textMerger.addTextToDocument(pageDeGardeText.getGestionnaire(nomPrenomPreparateurDecision));
            textMerger.addTextToDocument(pageDeGardeText.getIdUser(preparateurDecision.getIdUser()));
            nRef = "/" + nRef;
            if (Boolean.parseBoolean(getSession().getApplication().getProperty("pegasus.pegasus.decision.ref_user"))) {
                textMerger.addTextToDocument(pageDeGardeText.getNReference(nRef));
            }
        } else {
            textMerger.addTextToDocument(pageDeGardeText.getNReference(nRef));
        }

        textMerger.addTextToDocument(pageDeGardeText.getServiceCollaborateur(gestionnaire.getDepartment()));
        textMerger.addTextToDocument(pageDeGardeText.getEmailCollaborateur(gestionnaire.getEmail()));
        textMerger.addTextToDocument(pageDeGardeText.getVReference(NSSReference));
        textMerger.addTextToDocument(pageDeGardeText.getNSSBeneficiare(NSSReference));

        AdresseTiersDetail adresseTiersDetail = PegasusUtil.getAdresseCascadeByType(tiers.getIdTiers(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                SingleTransfertPCAbstractBuilder.listOrderAdresseTiers);

        textMerger.addTextToDocument(pageDeGardeText.getAdresse(adresseTiersDetail.getAdresseFormate()));

        // Date et lieu
        String dateForPG = PegasusDateUtil.getLitteralDateByTiersLanguage(dateDecision, tiers.getLangue());
        textMerger.addTextToDocument(pageDeGardeText.getDateEtLieu(dateForPG));

        return data;
    }
}

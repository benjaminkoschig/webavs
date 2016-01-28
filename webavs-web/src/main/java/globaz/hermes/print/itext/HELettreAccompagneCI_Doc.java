package globaz.hermes.print.itext;

import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.hermes.babel.HECTConstantes;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;

public class HELettreAccompagneCI_Doc extends HEDocumentManager {
    private static final long serialVersionUID = -6002454129227445548L;
    private static final String CHAMP_CORPS = "P_CORPS";
    private static final String CHAMP_POLITESSE = "P_POLITESSE";
    private static final String CHAMP_SIGNATURE = "P_SIGN_LETTRE";
    // les champs du documents
    private static final String CHAMP_TITRE = "P_TITRE";
    private static final String CHAMPS_ANNEXES = "P_ANNEXES";
    private static final String CHAMPS_SERVICE = "P_SERVICE";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "HERMES_LETTRE_ACCOMPAGNE_CI";
    private String csLangue;
    //
    private String numeroAVS;
    private String service;
    private String titre;
    private String formulePolitesseSpecifique = "";

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public HELettreAccompagneCI_Doc(BSession parent) throws FWIException {
        super(parent, HELettreAccompagneCI_Doc.TEMPLATE_NAME);
    }

    @Override
    protected String _getAdresseString(HEInfos adresse) {
        return getLibelleTitre() + "\n" + super._getAdresseString(adresse);
    }

    @Override
    public void createDataSource() throws Exception {

        // Set du numéro de document INFOROM
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber("0008CCI");
        }

        // Récupération des données
        HEInfos adresseAssure = (HEInfos) currentEntity();
        String reference = JadeStringUtil.isEmpty(annonceAssure.getIdAnnonce11()) ? annonceAssure.getReferenceUnique()
                : annonceAssure.getIdAnnonce11();
        HEInfos langueAssure = getInfoAssure(reference, HEInfos.CS_LANGUE_CORRESPONDANCE);
        titre = getInfoAssure(reference, HEInfos.CS_TITRE_ASSURE) == null ? "" : (getInfoAssure(reference,
                HEInfos.CS_TITRE_ASSURE)).getLibInfo();

        HEInfos infoPolitesse = getInfoAssure(reference, HEInfos.CS_FORMULE_POLITESSE);
        if (infoPolitesse != null) {
            formulePolitesseSpecifique = infoPolitesse.getLibInfo();
        }

        HEOutputAnnonceViewBean annonce11 = new HEOutputAnnonceViewBean();
        annonce11.setSession(getSession());
        annonce11.setIdAnnonce(reference);
        annonce11.retrieve();
        numeroAVS = annonce11.getNumeroAVS();
        //

        if (JadeStringUtil.isEmpty(numeroAVS)) {
            throw new Exception("Impossible de récupérer le numéro AVS de l'assuré");
        }

        if (adresseAssure.isNew() || langueAssure.isNew()) {
            throw new Exception("Adresse ou langue de l'assuré introuvable");
        }

        if (JadeStringUtil.isEmpty(getLangueFromEcran())) {
            _setLangueFromAssure(langueAssure);
            setCsLangue(langueAssure != null ? langueAssure.getLibInfo() : null);
        } else {
            _setLangueWithLangueFromEcran(getLangueFromEcran());
            if ("DE".equalsIgnoreCase(getLangueFromEcran())) {
                setCsLangue(IConstantes.CS_TIERS_LANGUE_ALLEMAND);
            } else if ("IT".equalsIgnoreCase(getLangueFromEcran())) {
                setCsLangue(IConstantes.CS_TIERS_LANGUE_ITALIEN);
            } else {
                setCsLangue(IConstantes.CS_TIERS_LANGUE_FRANCAIS);
            }
        }

        // Gestion du modèle et du titre
        setTemplateFile(HELettreAccompagneCI_Doc.TEMPLATE_NAME);
        setDocumentTitle(getSession().getLabel("HERMES_LETTRE_97") + " " + annonceAssure.getNomPrenom());

        // Renseigne les paramètres du document
        loadCatTexte(_getLangue(), HECTConstantes.CS_TYPE_LETTRE_ACCOMPAGEMENT, "");
        String nAvsFormate = TIApplication.pyxisApp().getAvsFormater().format(numeroAVS);
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, nAvsFormate);
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE, numeroAVS);
        // Gestion de l'en-tête
        _handleHeaders(adresseAssure, "", true);

    }

    public String getCsLangue() {
        return csLangue;
    }

    private String getDefaultFormePolitesse() {
        if (getCsLangue() == null) {
            csLangue = IConstantes.CS_TIERS_LANGUE_FRANCAIS; // fr par def.
        }
        String defaultPolitesse = "Madame, Monsieur"; // fr
        if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(csLangue)) {
            defaultPolitesse = "Sehr geehrte Damen und Herren"; // de
        } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(csLangue)) {
            defaultPolitesse = "Egregi signori"; // it
        }
        return defaultPolitesse;
    }

    public String getFormulePolitesse() {
        try {
            if (!JadeStringUtil.isBlank(formulePolitesseSpecifique)) {
                return formulePolitesseSpecifique;
            } else if (IConstantes.CS_TIERS_TITRE_MONSIEUR.equals(getTitre())) {
                return getSession().getApplication().getLabel("HERMES_10037", _getLangue());
            } else if (IConstantes.CS_TIERS_TITRE_MADAME.equals(getTitre())) {
                return getSession().getApplication().getLabel("HERMES_10036", _getLangue());
            } else {
                return getDefaultFormePolitesse();
            }
        } catch (Exception e) {
            return getDefaultFormePolitesse();
        }
    }

    private String getLangueNonIso() {
        if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(csLangue)) {
            return "I";
        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(csLangue)) {
            return "D";
        } else {
            return "F";
        }
    }

    private String getLibelleTitre() {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(getSession());
        cs.getCode(getTitre());
        return cs.getCodeUtilisateur(getLangueNonIso()).getLibelle();
    }

    @Override
    public int getNbLevel() {
        return HECTConstantes.NB_LEBEL_LETTRE_ACCOMPAGEMENT;
    }

    private String[] getParams() {
        String[] s = new String[3];
        s[0] = NSUtil.formatAVSUnknown(numeroAVS);
        s[1] = getFormulePolitesse();
        s[2] = getFormulePolitesse();
        return s;
    }

    public String getService() {
        return service;
    }

    public String getTitre() {
        return titre;
    }

    public void setCsLangue(String string) {
        csLangue = string;
    }

    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        if (JadeStringUtil.isEmpty(value)) {
            value = " ";
        }
        switch (i) {
            case 1:
                this.setParametres(HELettreAccompagneCI_Doc.CHAMP_TITRE, format(value, getParams()));
                break;
            case 2:
                this.setParametres(HELettreAccompagneCI_Doc.CHAMP_POLITESSE, format(value, getParams()));
                break;
            case 3:
                this.setParametres(HELettreAccompagneCI_Doc.CHAMP_CORPS, format(value, getParams()));
                break;
            case 4:
                this.setParametres(HELettreAccompagneCI_Doc.CHAMP_SIGNATURE, value);
                break;
            case 5:
                this.setParametres(HELettreAccompagneCI_Doc.CHAMPS_ANNEXES, value);
                break;
        }

        if (!JadeStringUtil.isBlank(getService())) {
            this.setParametres(HELettreAccompagneCI_Doc.CHAMPS_SERVICE, getService());
        }
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setTitre(String string) {
        titre = string;
    }
}
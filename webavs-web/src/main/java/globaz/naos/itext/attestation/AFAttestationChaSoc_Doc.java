package globaz.naos.itext.attestation;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.ICommonConstantes;
import java.text.MessageFormat;

public class AFAttestationChaSoc_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String CAT_DOMAINE = "835001";
    private final static String CAT_TYPE = "836035";
    private final static String CS_EXPLOITATION = "508021";
    private final static String MODEL_NAME = "NAOS_ATTESTATION_CHARGE_SOCIALE";
    private final static String NUM_INFOROM = "0275CAF";

    private AFAffiliation affiliation;

    private String affiliationId;
    private ICTDocument catalogue;
    private String dateAttestation;
    private String dateValidite;
    private int iter = 0;
    private String nombreExemplaire;
    private Boolean paiementRegulier;
    private TITiersViewBean tiersAffiliation;
    private String titre;

    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }
        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

    private void _setHeader(CaisseHeaderReportBean bean, TITiersViewBean tiers) throws Exception {
        bean.setAdresse(tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA()));
        bean.setDate(JACalendar.format(getDateAttestation(), tiers.getLangueIso()));
        bean.setNoAffilie(affiliation.getAffilieNumero());

        // Renseignement du numéro ide
        AFIDEUtil.addNumeroIDEInDoc(bean, affiliation.getNumeroIDE(), affiliation.getIdeStatut());

        bean.setConfidentiel(false);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            super.setTemplateFile(AFAttestationChaSoc_Doc.MODEL_NAME);
        } catch (Exception e) {
            throw new FWIException("unable to setTemplateFile:" + e.getMessage());
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        try {
            affiliation = AFAffiliationUtil.getAffiliation(affiliationId, getSession());
        } catch (Exception e) {
            this.abort("affiliation is null", FWMessage.ERREUR);
        }
        tiersAffiliation = affiliation.getTiers();
        if (tiersAffiliation == null) {
            this.abort("tiersAffiliation is null", FWMessage.ERREUR);
        }
        setFileTitle(getSession().getLabel("TITLE_ATTESTATION_CHA_SOC"));
    }

    protected void catalogue() throws FWIException {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            helper.setCsDomaine(AFAttestationChaSoc_Doc.CAT_DOMAINE);
            helper.setCsTypeDocument(AFAttestationChaSoc_Doc.CAT_TYPE);
            helper.setCodeIsoLangue(tiersAffiliation.getLangueIso());
            helper.setActif(Boolean.TRUE);
            helper.setDefault(Boolean.TRUE);

            // charger le catalogue de texte
            ICTDocument[] candidats = helper.load();
            if ((candidats != null) && (candidats.length > 0)) {
                catalogue = candidats[0];
            }
        } catch (Exception e) {
            catalogue = null;
        }
        if (catalogue == null) {
            this.abort(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR);
            throw new FWIException(getSession().getLabel("CATALOGUE_INTROUVABLE"));
        }
    }

    @Override
    public void createDataSource() throws Exception {
        fillDocInfo();
        setDocumentTitle(getSession().getLabel("TITLE_ATTESTATION_CHA_SOC") + " " + affiliation.getAffilieNumero());

        // récupération du catalogue de texte
        catalogue();

        // récupération des informations à afficher
        // nomination du tiers
        TIAdresseDataSource adresseDataSouceTiers;
        // Adresse d'exploitation si disponible
        adresseDataSouceTiers = tiersAffiliation.getAdresseAsDataSource(AFAttestationChaSoc_Doc.CS_EXPLOITATION,
                IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), false);

        // sinon adresse de domicile
        if (adresseDataSouceTiers == null) {
            adresseDataSouceTiers = tiersAffiliation.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.todayJJsMMsAAAA(), false);
        }

        // sinon adresse de courrier
        if (adresseDataSouceTiers == null) {
            adresseDataSouceTiers = tiersAffiliation.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), false);
        }

        // si l'adresse est toujours null on utilise la cascade d'adresse par héritage
        if (adresseDataSouceTiers == null) {
            adresseDataSouceTiers = tiersAffiliation.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(), true);
        }

        String nominationTiers = affiliation.getRaisonSociale();
        nominationTiers += ", " + adresseDataSouceTiers.rue + " " + adresseDataSouceTiers.numeroRue;
        nominationTiers += ", " + adresseDataSouceTiers.localiteNpa + " " + adresseDataSouceTiers.localiteNom;

        // supprime les "&" si contenu (pose problème avec les catalogues stylés)
        if (nominationTiers.contains("&")) {
            String nominationTierCopie = nominationTiers;
            nominationTiers = nominationTierCopie.replaceAll("&",
                    getSession().getApplication().getLabel("TEXTE_ET", tiersAffiliation.getLangueIso()));
        }

        // afficher les assurances actives de son plan
        String concerneTexte = "";
        String cotisationsTexte = "";
        boolean cotiAvsAi = false;
        boolean cotiAf = false;
        boolean cotiAc = false;

        AFPlanAffiliationManager planAffiliationManager = new AFPlanAffiliationManager();
        planAffiliationManager.setSession(getSession());
        planAffiliationManager.setForAffiliationId(affiliation.getAffiliationId());
        planAffiliationManager.setForPlanActif(true);
        planAffiliationManager.find();
        if (planAffiliationManager.size() > 0) {
            for (int i = 0; i < planAffiliationManager.size(); i++) {
                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) planAffiliationManager.getFirstEntity();
                AFCotisationManager cotisationsManager = new AFCotisationManager();
                cotisationsManager.setSession(getSession());
                cotisationsManager.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                cotisationsManager.setForDate(dateAttestation);
                cotisationsManager.find();
                if (cotisationsManager.size() > 0) {
                    for (int j = 0; j < cotisationsManager.size(); j++) {
                        AFCotisation cotisation = (AFCotisation) cotisationsManager.get(j);
                        if (cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AVS_AI)) {
                            cotiAvsAi = true;
                        } else if (cotisation.getAssurance().getTypeAssurance()
                                .equals(CodeSystem.TYPE_ASS_COTISATION_AC)) {
                            cotiAc = true;
                        } else if (cotisation.getAssurance().getTypeAssurance()
                                .equals(CodeSystem.TYPE_ASS_COTISATION_AF)) {
                            cotiAf = true;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }

        if (cotiAvsAi) {
            cotisationsTexte += getSession().getApplication().getLabel("TEXTE_AVSAIAPG",
                    tiersAffiliation.getLangueIso());
        }
        if (cotiAc) {
            if (!JadeStringUtil.isEmpty(cotisationsTexte)) {
                if (cotiAf) {
                    cotisationsTexte += ", ";
                } else {
                    cotisationsTexte += " "
                            + getSession().getApplication().getLabel("TEXTE_ET", tiersAffiliation.getLangueIso()) + " ";
                }
            }
            cotisationsTexte += getSession().getApplication().getLabel("TEXTE_AC", tiersAffiliation.getLangueIso());
        }
        if (cotiAf) {
            if (!JadeStringUtil.isEmpty(cotisationsTexte)) {
                cotisationsTexte += " "
                        + getSession().getApplication().getLabel("TEXTE_ET", tiersAffiliation.getLangueIso()) + " ";
            }
            cotisationsTexte += getSession().getApplication().getLabel("TEXTE_AF", tiersAffiliation.getLangueIso());
        }

        // afficher un texte si il paie régulièrement
        String paiementRegulierTexte = "";
        if (paiementRegulier) {
            paiementRegulierTexte = getSession().getApplication().getLabel("TEXTE_PAIE_REGULIEREMENT",
                    tiersAffiliation.getLangueIso());
            paiementRegulierTexte += ".";
            concerneTexte = getTexte(1, null);
        } else {
            cotisationsTexte += ".";
            concerneTexte = getSession().getApplication().getLabel("TEXTE_CONCERNE_PAS_REGULIER",
                    tiersAffiliation.getLangueIso());
        }

        // envoi des paramètres
        String dateValiditeText = JACalendar.format(dateValidite, tiersAffiliation.getLangueIso());
        String dateDebutAffiliationText = JACalendar
                .format(affiliation.getDateDebut(), tiersAffiliation.getLangueIso());
        this.setParametres("P_CONCERNE", concerneTexte);
        this.setParametres("P_VALIDITE", getTexte(2, new Object[] { dateValiditeText }));
        this.setParametres(
                "P_CORPS",
                getTexte(3, new Object[] { titre, nominationTiers, dateDebutAffiliationText, cotisationsTexte,
                        paiementRegulierTexte }));

        // mise en place du header
        setTemplateFile(AFAttestationChaSoc_Doc.MODEL_NAME);
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), affiliation.getTiers().getLangueIso());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean, tiersAffiliation);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        caisseReportHelper.addSignatureParameters(this);
        getImporter().getParametre().put(
                ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), "header.filename"));

    }

    private void fillDocInfo() throws JAException {
        getDocumentInfo().setDocumentTypeNumber(AFAttestationChaSoc_Doc.NUM_INFOROM);
        getDocumentInfo().setDocumentProperty("annee",
                Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affiliation.getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliation.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affiliation.getAffilieNumero());
        }
        try {
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    getDocumentInfo().getDocumentProperty("numero.affilie.formatte"), getDocumentInfo()
                            .getDocumentProperty("numero.affilie.non.formatte"));
        } catch (Exception e) {
            JadeCodingUtil.catchException(this, "createDataSource()", e);
        }
    }

    protected String formatMessage(StringBuffer message, Object[] args) {
        return AFAttestationChaSoc_Doc.formatMessage(message.toString(), args);
    }

    public AFAffiliation getAffiliation() {
        return affiliation;
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateAttestation() {
        return dateAttestation;
    }

    public String getDateValidite() {
        return dateValidite;
    }

    public String getNombreExemplaire() {
        return nombreExemplaire;
    }

    public Boolean getPaiementRegulier() {
        return paiementRegulier;
    }

    protected String getTexte(int niveau, Object[] args) throws FWIException {
        String resString = "";
        ICTTexte texte = null;
        try {

            ICTListeTextes listTexte = catalogue.getTextes(niveau);

            if (listTexte != null) {
                for (int i = 0; i < listTexte.size(); i++) {
                    texte = listTexte.getTexte(i + 1);
                    if ((i + 1) < listTexte.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }

            if (args != null) {
                resString = AFAttestationChaSoc_Doc.formatMessage(resString, args);
            }

            return resString;

        } catch (Exception e) {
            return "";
        }
    }

    public String getTitre() {
        return titre;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (!isAborted() && (iter < Integer.parseInt(nombreExemplaire))) {
            iter++;
            return true;
        } else {
            return false;
        }
    }

    public void setAffiliation(AFAffiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setDateAttestation(String dateAttestation) {
        this.dateAttestation = dateAttestation;
    }

    public void setDateValidite(String dateValidite) {
        this.dateValidite = dateValidite;
    }

    public void setNombreExemplaire(String nombreExemplaire) {
        this.nombreExemplaire = nombreExemplaire;
    }

    public void setPaiementRegulier(Boolean paiementRegulier) {
        this.paiementRegulier = paiementRegulier;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}

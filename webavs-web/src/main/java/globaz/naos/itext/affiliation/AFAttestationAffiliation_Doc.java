package globaz.naos.itext.affiliation;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationJAssuranceManager;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.constantes.IConstantes;
import globaz.webavs.common.ICommonConstantes;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;

public class AFAttestationAffiliation_Doc extends FWIDocumentManager {

    private static final String DOC_NO = "0002CAF";

    private static final String P_TEXTE = "P_TEXTE";

    private static final String P_TITRE = "P_TITRE";
    private static final long serialVersionUID = -8208520768193032755L;
    private static final String TEMPLATE_FILE_NAME = "NAOS_ATTESTATION_AFFILIATION";
    private AFAffiliation affiliation;
    private String affiliationId;
    private String brancheEconomique;
    private String dateDebutParitaire = "";

    private String dateDebutPersonelle = "";
    private String dateFinParitaire = "";

    private String dateFinPersonnelle = "";
    private boolean documentIndEmpl = false;
    private boolean hasNext = true;

    public AFAttestationAffiliation_Doc() {
    }

    public AFAttestationAffiliation_Doc(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public AFAttestationAffiliation_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            loadAffiliation();
            fillDocInfo();

            // remplir l'en-tête et la signature
            ICaisseReportHelper crh = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), getLangueDestinataire());
            CaisseHeaderReportBean hb = new CaisseHeaderReportBean();

            hb.setAdresse(loadAffiliation().getTiers().getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    ICommonConstantes.CS_APPLICATION_COTISATION, JACalendar.todayJJsMMsAAAA(),
                    loadAffiliation() != null ? loadAffiliation().getAffilieNumero() : null));
            hb.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), loadAffiliation().getTiers().getLangueIso()));
            hb.setEmailCollaborateur(getSession().getUserEMail());
            hb.setNoAffilie(loadAffiliation().getAffilieNumero());

            // Renseignement du numéro ide
            AFIDEUtil.addNumeroIDEInDoc(hb, loadAffiliation().getNumeroIDE(), loadAffiliation().getIdeStatut());

            hb.setNoAvs(loadAffiliation().getTiers().getNumAvsActuel());
            hb.setNomCollaborateur(getSession().getUserFullName());
            hb.setTelCollaborateur(getSession().getUserInfo().getPhone());
            hb.setUser(getSession().getUserInfo());
            crh.addHeaderParameters(this, hb);
            crh.addSignatureParameters(this);

            // renseigner le texte
            ICTDocument document = loadCatalogue();
            StringBuffer buffer = new StringBuffer();

            // -- LE TITRE
            // -----------------------------------------------------------
            for (Iterator titresIter = document.getTextes(1).iterator(); titresIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }

                buffer.append(((ICTTexte) titresIter.next()).getDescription());
            }

            this.setParametres(AFAttestationAffiliation_Doc.P_TITRE, buffer.toString());

            // -- LE TEXTE
            // -------------------------------------------------------------
            buffer.setLength(0);

            for (Iterator textesIter = document.getTextes(2).iterator(); textesIter.hasNext();) {
                if (buffer.length() > 0) {
                    buffer.append("\n\n");
                }

                buffer.append(((ICTTexte) textesIter.next()).getDescription());
            }

            /*
             * remplacement des textes, les conventions sont:
             * 
             * {0} = titre prénom nom {1} = numéro AVS {2} = date début affiliation {3} = branche économique {4} = nom
             * caisse {5} = type d'affiliation
             */
            AFAffiliation aff = loadAffiliation();
            if (documentIndEmpl) {
                String periodeParitaire = "";
                String periodePersonelle = "";
                if (JadeStringUtil.isBlankOrZero(dateFinParitaire)) {
                    periodeParitaire = getSession().getApplication().getLabel("NAOS_COLONNE_DESLE",
                            aff.getTiers().getLangueIso().toUpperCase());
                    periodeParitaire += dateDebutParitaire;
                } else {
                    periodeParitaire = getSession().getApplication().getLabel("NAOS_COLONNE_DU",
                            aff.getTiers().getLangueIso().toUpperCase());
                    periodeParitaire += dateDebutParitaire;
                    periodeParitaire += " "
                            + getSession().getApplication().getLabel("NAOS_COLONNE_AU",
                                    aff.getTiers().getLangueIso().toUpperCase());
                    periodeParitaire += dateFinParitaire;
                }
                if (JadeStringUtil.isBlankOrZero(dateFinPersonnelle)) {
                    periodePersonelle = getSession().getApplication().getLabel("NAOS_COLONNE_DESLE",
                            aff.getTiers().getLangueIso().toUpperCase());
                    periodePersonelle += dateDebutPersonelle;
                } else {
                    periodePersonelle = getSession().getApplication().getLabel("NAOS_COLONNE_DU",
                            aff.getTiers().getLangueIso().toUpperCase());
                    periodePersonelle += dateDebutPersonelle;
                    periodePersonelle += " "
                            + getSession().getApplication().getLabel("NAOS_COLONNE_AU",
                                    aff.getTiers().getLangueIso().toUpperCase());
                    periodePersonelle += dateFinPersonnelle;
                }
                this.setParametres(
                        AFAttestationAffiliation_Doc.P_TEXTE,
                        formatMessage(
                                buffer,
                                new Object[] {
                                        getTitrePrenomNom(),
                                        aff.getTiers().getNumAvsActuel(),
                                        periodeParitaire,
                                        getBrancheEconomique(),
                                        getProp(ACaisseReportHelper.JASP_PROP_NOM_CAISSE),
                                        CodeSystem.getLibelleIso(getSession(), aff.getTypeAffiliation(), aff.getTiers()
                                                .getLangueIso().toUpperCase()), periodePersonelle }));
            } else {
                this.setParametres(
                        AFAttestationAffiliation_Doc.P_TEXTE,
                        formatMessage(
                                buffer,
                                new Object[] {
                                        getTitrePrenomNom(),
                                        aff.getTiers().getNumAvsActuel(),
                                        aff.getDateDebut(),
                                        getBrancheEconomique(),
                                        getProp(ACaisseReportHelper.JASP_PROP_NOM_CAISSE),
                                        CodeSystem.getLibelleIso(getSession(), aff.getTypeAffiliation(), aff.getTiers()
                                                .getLangueIso().toUpperCase()) }));
            }
        } catch (Exception e) {
            abort();
            throw new FWIException("Erreur: " + e.getMessage(), e);
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFAttestationAffiliation_Doc.TEMPLATE_FILE_NAME);
    }

    @Override
    public void createDataSource() throws Exception {
    }

    private void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber(AFAttestationAffiliation_Doc.DOC_NO);

        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affiliation.getAffilieNumero());

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliation.getAffilieNumero()));
            String role = AFAffiliationUtil.getRoleParInd(affiliation);
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), role,
                    affiliation.getAffilieNumero(), affilieFormater.unformat(affiliation.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affiliation.getAffilieNumero());
        }
        getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        getDocumentInfo().setArchiveDocument(true);
    }

    private String formatMessage(StringBuffer message, Object[] args) {
        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar) == '\'')
                    && ((idChar == (message.length() - 1)) || (message.charAt(idChar + 1) != '\''))) {
                message.insert(idChar, '\'');
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
        return MessageFormat.format(message.toString(), args);
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * retourne la langue de l'affilie (doit être appellé ap.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private String getLangueDestinataire() throws Exception {
        String retValue = loadAffiliation().getTiers().getLangueIso().toLowerCase();

        if (Locale.FRENCH.getLanguage().equals(retValue) || Locale.GERMAN.getLanguage().equals(retValue)
                || Locale.ITALIAN.getLanguage().equals(retValue)) {
            return retValue;
        } else {
            return Locale.FRENCH.getLanguage();
        }
    }

    private String getProp(String propName) throws Exception {
        return getTemplateProperty(getDocumentInfo(), propName + getLangueDestinataire().toUpperCase());
    }

    private String getTitrePrenomNom() throws Exception {
        // String titre = this.getSession().getCodeLibelle(this.loadAffiliation().getTiers().getTitreTiers());
        String titre = CodeSystem.getLibelleIso(getSession(), loadAffiliation().getTiers().getTitreTiers(),
                loadAffiliation().getTiers().getLangueIso().toUpperCase());
        if (!JadeStringUtil.isEmpty(titre)) {
            return titre + " " + loadAffiliation().getTiers().getPrenomNom();
        } else {
            return loadAffiliation().getTiers().getPrenomNom();
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private AFAffiliation loadAffiliation() throws Exception {
        if (affiliation == null) {
            affiliation = new AFAffiliation();
            affiliation.setAffiliationId(affiliationId);
            affiliation.setSession(getSession());
            affiliation.retrieve();
        }

        return affiliation;
    }

    private ICTDocument loadCatalogue() throws Exception {
        // préparer le chargement
        ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

        loader.setActif(Boolean.TRUE);
        loader.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF);
        loader.setCodeIsoLangue(getLangueDestinataire());

        if (CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(loadAffiliation().getTypeAffiliation())) {
            loader.setCsTypeDocument(CodeSystem.TYPE_CAT_ATTESTATION_IND_EMPL);
            ICTDocument[] candidats = loader.load();
            if (candidats != null) {
                // Test si il y a 2 adhésions
                AFAdhesionManager adMng = new AFAdhesionManager();
                adMng.setForAffiliationId(loadAffiliation().getAffiliationId());
                adMng.setSession(getSession());
                adMng.find();
                for (int i = 0; i < adMng.getSize(); i++) {
                    AFAdhesion adh = (AFAdhesion) adMng.getEntity(i);
                    if (!adh.getDateDebut().equalsIgnoreCase(adh.getDateFin())) { // Ne pas prendre les adhésions
                                                                                  // annulées
                        AFCotisationJAssuranceManager coMng = new AFCotisationJAssuranceManager();
                        coMng.setForAdhesionId(adh.getAdhesionId());
                        coMng.setForAffiliationId(affiliationId);
                        coMng.setSession(getSession());
                        coMng.find();
                        if (coMng.getSize() > 0) {
                            AFCotisation coti = (AFCotisation) coMng.getFirstEntity();
                            if (CodeSystem.GENRE_ASS_PARITAIRE
                                    .equalsIgnoreCase(coti.getAssurance().getAssuranceGenre())) {
                                // Sauvegarde des dates des plus récentes en cas de plusieurs adhésions (radiation puis
                                // nouvelle adhéesion)
                                if (JadeStringUtil.isBlankOrZero(dateDebutParitaire)
                                        || JadeStringUtil.isEmpty(adh.getDateFin())
                                        || (!JadeStringUtil.isEmpty(dateFinParitaire) && (BSessionUtil
                                                .compareDateFirstGreater(getSession(), adh.getDateFin(),
                                                        dateFinParitaire)))) {
                                    dateDebutParitaire = adh.getDateDebut();
                                    dateFinParitaire = adh.getDateFin();
                                }
                            } else {
                                if (JadeStringUtil.isBlankOrZero(dateDebutPersonelle)
                                        || JadeStringUtil.isEmpty(adh.getDateFin())
                                        || (!JadeStringUtil.isEmpty(dateFinPersonnelle) && (BSessionUtil
                                                .compareDateFirstGreater(getSession(), adh.getDateFin(),
                                                        dateFinPersonnelle)))) {
                                    dateDebutPersonelle = adh.getDateDebut();
                                    dateFinPersonnelle = adh.getDateFin();
                                }
                            }
                        }
                    }
                }
                // Si adhésions personnelles et paritaires de renseignées.
                if (!JadeStringUtil.isBlankOrZero(dateDebutParitaire)
                        && !JadeStringUtil.isBlankOrZero(dateDebutPersonelle)) {
                    documentIndEmpl = true;
                    return candidats[0];
                }
            }
        }
        loader.setDefault(Boolean.TRUE);
        loader.setCsTypeDocument(CodeSystem.TYPE_CAT_ATTESTATION_AFFILIATION);
        // trouver le catalogue
        ICTDocument[] candidats = loader.load();
        if ((candidats == null) || (candidats.length == 0)) {
            throw new Exception("Impossible de trouver le catalogue de texte");
        }

        return candidats[0];
    }

    @Override
    public boolean next() throws FWIException {
        boolean retValue = hasNext;

        if (hasNext) {
            hasNext = !hasNext;
        }

        return retValue;
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }
}

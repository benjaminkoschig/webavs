/*
 * Créé le 12 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.masse;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.itext.AFAdresseDestination;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.api.ITIRole;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public abstract class AFAbstractSalaires_Doc extends FWIDocumentManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAdresseDestination adresse;
    private AFAffiliation affiliation;
    protected String affiliationId;
    private ICTDocument catalogue;

    protected String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    protected String dateRetour;
    protected boolean hasNext = true;
    private AFPlanAffiliation planAffiliation;
    protected String planAffiliationId;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFConfirmationSalaires_Doc.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public AFAbstractSalaires_Doc() throws Exception {
        this(new BSession(AFApplication.DEFAULT_APPLICATION_NAOS));
    }

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalaires_Doc.
     * 
     * @param session
     * 
     * @throws Exception
     */
    public AFAbstractSalaires_Doc(BSession session) throws Exception {
        super(session, AFApplication.DEFAULT_APPLICATION_NAOS_REP, session
                .getLabel(AFSalaires_Param.NOMDOC_CONFIRMATION_SALAIRES));
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param message
     *            DOCUMENT ME!
     * @param type
     *            DOCUMENT ME!
     */
    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected AFAffiliation affiliation() throws Exception {
        if (affiliation == null) {
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(affiliationId);
            affiliation.retrieve();
        }

        return affiliation;
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    protected void beforeBuildReport(String intro, String outro, String date, String signature, Boolean dessineCadre)
            throws FWIException {
        // creer l'entete du doc
        // ---------------------------------------------------
        try {
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), getIsoLangueDestinataire());

            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

            headerBean.setAdresse(getAdresse().getAdresseDestinataire(getIdTiers(), affiliation().getAffilieNumero(),
                    planAffiliation().getDomaineCourrier()));
            headerBean.setDate(JACalendar.format(dateEnvoi, affiliation().getTiers().getLangueIso()));
            headerBean.setNoAffilie(affiliation().getAffilieNumero());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            headerBean.setUser(getSession().getUserInfo());

            headerBean.setNoAvs(" ");

            AFIDEUtil.addNumeroIDEInDoc(headerBean, affiliation().getNumeroIDE(), affiliation().getIdeStatut());

            caisseReportHelper.addHeaderParameters(this, headerBean);
            caisseReportHelper.addSignatureParameters(this);
            // le texte d'intro
            // -----------------------------------------------------
            this.setParametres(AFSalaires_Param.P_INTRO, format(intro, new String[] { getFormulePolitesse() }));

        } catch (Exception e) {
            this.abort("impossible de créer l'en-tête", FWMessage.ERREUR);

            return;
        }

        // les libelles des colonnes du détail
        // ---------------------------------------
        this.setParametres(AFSalaires_Param.P_LIBELLE_COL1, texte(2, 1));
        this.setParametres(AFSalaires_Param.P_LIBELLE_COL2, texte(2, 2));
        try {
            // le bas de page
            // --------------------------------------------------------------
            this.setParametres(AFSalaires_Param.P_OUTRO, format(outro, new String[] { getFormulePolitesse() }));
            if (date != null) {
                this.setParametres(AFSalaires_Param.P_DATE_EMP, date);
            }
            if (signature != null) {
                this.setParametres(AFSalaires_Param.P_SIGNATURE_EMP, signature);
            }
        } catch (Exception e) {
            this.abort("impossible de créer le pied de page", FWMessage.ERREUR);

            return;
        }
        // ne pas dessiner le cadre
        // ------------------------------------------------------
        this.setParametres(AFSalaires_Param.P_DESSINE_CADRE, dessineCadre);
    }

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(AFSalaires_Param.TEMPLATE_SALAIRES);
    }

    /**
     * @return l'instance du calendrier de l'application
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected JACalendar calendar() throws Exception {
        return getSession().getApplication().getCalendar();
    }

    /**
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    protected ICTDocument catalogue() throws FWIException {
        if (catalogue == null) {
            try {
                // chercher les catalogues...
                ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

                helper.setCsDomaine(CodeSystem.DOMAINE_CAT_AFF); // du domaine
                // AF
                helper.setCsTypeDocument(getTypeCatalogue()); // pour le type de
                // catalogue
                helper.setCodeIsoLangue(getIsoLangueDestinataire()); // dans la
                // langue
                // donnée
                helper.setActif(Boolean.TRUE); // actif
                helper.setDefault(Boolean.TRUE); // et par défaut

                // charger le catalogue de texte
                ICTDocument[] candidats = helper.load();

                if ((candidats != null) && (candidats.length > 0)) {
                    catalogue = candidats[0];
                }
            } catch (Exception e) {
                catalogue = null;
            }
        }

        if (catalogue == null) {
            this.abort("impossible de trouver le catalogue de texte", FWMessage.ERREUR);
            throw new FWIException("impossible de trouver le catalogue de texte");
        }

        return catalogue;
    }

    /**
     * Après l'impression d'un document
     */
    public void fillDocInfo(String anneeDocument) {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affiliation.getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affiliation.getAffilieNumero()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), affiliation.getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                    affiliation.getAffilieNumero(), affilieFormater.unformat(affiliation.getAffilieNumero()));
            getDocumentInfo().setDocumentTitle(affiliation().getAffilieNumero());

        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affiliation.getAffilieNumero());
        }

        if (JadeStringUtil.isBlankOrZero(anneeDocument)) {
            getDocumentInfo().setDocumentProperty("annee", String.valueOf(JACalendar.today().getYear()));
        } else {
            getDocumentInfo().setDocumentProperty("annee", anneeDocument);
        }
        getDocumentInfo().setDocumentTypeNumber(getNoDocument());
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param message
     *            DOCUMENT ME!
     * @param args
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected String format(String message, String[] args) {

        if ((message == null) || (message.length() == 0)) {
            return "";
        }

        StringBuffer msgBuf = new StringBuffer();

        msgBuf.append(message.charAt(0));

        for (int idChar = 1; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar - 1) == '\'') && (message.charAt(idChar) != '\'')) {
                msgBuf.append('\'');
            }

            msgBuf.append(message.charAt(idChar));
        }

        return MessageFormat.format(msgBuf.toString(), args).toString();
    }

    /**
     * getter pour l'attribut adresse.
     * 
     * @return la valeur courante de l'attribut adresse
     */
    protected AFAdresseDestination getAdresse() {
        if (adresse == null) {
            adresse = new AFAdresseDestination(getSession());
        }

        return adresse;
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    /**
     * getter pour l'attribut date envoi.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public String getFormulePolitesse() throws Exception {
        return affiliation().getTiers().getFormulePolitesse(null);
    }

    /**
     * getter pour l'attribut id tiers.
     * 
     * @return la valeur courante de l'attribut id tiers
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected String getIdTiers() throws Exception {
        return affiliation().getIdTiers();
    }

    /**
     * getter pour l'attribut iso langue destinataire.
     * 
     * @return la valeur courante de l'attribut iso langue destinataire
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    protected String getIsoLangueDestinataire() throws FWIException {
        try {
            return getAdresse().getLangueDestinataire(getIdTiers());
        } catch (Exception e) {
            throw new FWIException("Impossible de trouver la langue du destinataire", e);
        }
    }

    protected abstract String getNoDocument();

    /**
     * getter pour l'attribut plan affiliation id.
     * 
     * @return la valeur courante de l'attribut plan affiliation id
     */
    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    /**
     * getter pour l'attribut type catalogue.
     * 
     * @return la valeur courante de l'attribut type catalogue
     */
    protected abstract String getTypeCatalogue();

    public boolean isAleardyPresent(LinkedList lignes, AFCotisation cotisation) {
        boolean found = false;
        Iterator it = lignes.iterator();
        while (it.hasNext() && !found) {
            AFCotisation coti = (AFCotisation) it.next();
            if (cotisation.getAssuranceId().equals(coti.getAssuranceId())
                    && cotisation.getPlanAffiliationId().equals(coti.getPlanAffiliationId())) {
                found = true;
            }
        }
        return found;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param niveau
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    protected String loadNiveau(int niveau) throws FWIException {
        StringBuffer tampon = new StringBuffer();
        ICTDocument catalogue = catalogue();

        // concatener toutes les lignes du catalogue pour l'intro.
        try {
            for (Iterator textes = catalogue.getTextes(niveau).iterator(); textes.hasNext();) {
                ICTTexte texte = (ICTTexte) textes.next();

                if (tampon.length() > 0) {
                    tampon.append("\n\n"); // ajouter une fin de paragraphe si
                    // necesaire
                }

                tampon.append(texte.getDescription());
            }
        } catch (RuntimeException e) {
            throw new FWIException("Il n'y a pas de texte au niveau: " + niveau, e);
        }

        return tampon.toString();
    }

    protected AFPlanAffiliation planAffiliation() throws Exception {
        if (planAffiliation == null) {
            planAffiliation = new AFPlanAffiliation();
            planAffiliation.setSession(getSession());
            planAffiliation.setPlanAffiliationId(planAffiliationId);
            planAffiliation.retrieve();
        }

        return planAffiliation;
    }

    /**
     */
    protected void reset() {
        adresse = null;
        affiliation = null;
        affiliationId = null;
        planAffiliationId = null;
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param affiliationId
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    /**
     * setter pour l'attribut date envoi.
     * 
     * @param dateEnvoi
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    /**
     * setter pour l'attribut plan affiliation id.
     * 
     * @param planAffiliationId
     *            une nouvelle valeur pour cet attribut
     */
    public void setPlanAffiliationId(String planAffiliationId) {
        this.planAffiliationId = planAffiliationId;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param niveau
     *            DOCUMENT ME!
     * @param position
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    protected String texte(int niveau, int position) throws FWIException {
        try {
            return catalogue().getTextes(niveau).getTexte(position).getDescription();
        } catch (FWIException e) {
            throw e;
        } catch (Exception e) {
            // throw new FWIException("impossible de trouver le texte");
            return "";
        }
    }

}

/*
 * Créé le 5 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.application.DSApplication;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.itext.AFAdresseDestination;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class DSPreImpressionContentieux_Doc extends DSAbstractDocument {
    private static final long serialVersionUID = 272867803449763662L;

    AFAdresseDestination adresse;
    ICTDocument[] document = null;
    private String documentDate;
    int nbNiveaux = 0;

    /**
     * @throws Exception
     */
    public DSPreImpressionContentieux_Doc() throws Exception {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    /**
     * @param session
     * @param nomDocument
     * @throws Exception
     */
    public DSPreImpressionContentieux_Doc(BSession session) throws Exception {
        super(session, session.getLabel(DSPreImpressionContentieux_Param.L_NOMDOCCONTENTIEUX));
    }

    /**
     * @param session
     * @param nomDocument
     * @throws Exception
     */
    public DSPreImpressionContentieux_Doc(BSession session, String nomDocument) throws Exception {
        super(session, nomDocument);
        // TODO Raccord de constructeur auto-généré
    }

    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        TITiers tiers = getTiers(getIdTiers());
        // Modif JMC 5.3 prendre l'adresse domaine DS pour les rappels
        bean.setAdresse(tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA(), getNumAff()));
        documentDate = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA() : getDateImpression();
        bean.setDate(JACalendar.format(documentDate, getTiers(getIdTiers()).getLangueIso().toLowerCase()));
        bean.setNoAffilie(getNumAff());
        bean.setNoAvs(" ");
        bean.setEmailCollaborateur(" ");
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    @Override
    public void afterPrintDocument() {
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", getNumAff());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(getNumAff()));
            TIDocumentInfoHelper.fill(getDocumentInfo(), getIdTiers(), getSession(), ITIRole.CS_AFFILIE, getNumAff(),
                    affilieFormater.unformat(getNumAff()));
            getDocumentInfo().setPublishDocument(isPublishDocument());
            getDocumentInfo().setDocumentDate(documentDate);
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getNumAff());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {

        super.createDataSource();

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getSession().getIdLangueISO());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        _setHeader(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        ArrayList documents = new ArrayList();
        Map nomDoc = new HashMap();
        nomDoc.put(DSPreImpressionContentieux_Param.F_NOMDOCATTACHE,
                getSession().getApplication().getLabel("DRACO_PREIMPRESSION_CONTENTIEUX", getIsoLangueDestinataire()));
        documents.add(nomDoc);
        this.setDataSource(documents);
    }

    private String format(String paragraphe) throws Exception {
        String amende = String.valueOf((int) JadeStringUtil.parseDouble(
                FWFindParameter.findParameter(getTransaction(), "123002", "MONTANT", "0", "0", 2), 0.0));
        String res = "";
        for (int i = 0; i < paragraphe.length(); i++) {
            if (paragraphe.charAt(i) != '{') {
                res += paragraphe.charAt(i);
            } else if (paragraphe.charAt(i + 1) == '0') {
                res += getPeriode();
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '1') {
                res += amende;
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '2') {
                res += Integer.parseInt(getPeriode()) + 1;
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '3') {
                TITiers tiers = new TITiers();
                tiers.setSession(getSession());
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve();
                // String titre=TIAdresseDataSource.getTitre(getSession(),
                // tiers.getTitreTiers(), tiers.getLangue());
                String titre = tiers.getFormulePolitesse(tiers.getLangue());

                if (JadeStringUtil.isEmpty(titre)) {
                    titre = getSession().getLabel("MADAME_MONSIEUR");
                }/*
                  * else { titre = titre.substring(0,3); if (titre.startsWith("Mon") || titre.startsWith("Mad") ||
                  * titre.startsWith("Mes") || titre.startsWith("Maî") || titre.startsWith("Doc")){ titre =
                  * TIAdresseDataSource.getTitre(getSession(), tiers.getTitreTiers(), tiers.getLangue()); } else { titre
                  * = getSession().getLabel("MADAME_MONSIEUR"); } }
                  */
                res += titre;
                i = i + 2;
            } else if (paragraphe.charAt(i + 1) == '4') {
                AFAffiliationManager affMgr = new AFAffiliationManager();
                affMgr.setSession(getSession());
                affMgr.setForAffilieNumero(getNumAff());
                affMgr.setForTypesAffParitaires();
                affMgr.find();
                if (affMgr.size() > 0) {
                    AFAffiliation affEnCours = (AFAffiliation) affMgr.getFirstEntity();
                    String agenceComm = affEnCours.getAgenceCom(affEnCours.getAffiliationId(),
                            JACalendar.todayJJsMMsAAAA());
                    if (JadeStringUtil.isBlankOrZero(agenceComm)) {
                        return "";
                    }

                    res += agenceComm;
                    i = i + 2;
                }
            }
        }
        return res;
    }

    /**
     * @return
     */
    public AFAdresseDestination getAdresse() {
        if (adresse == null) {
            adresse = new AFAdresseDestination(getSession());
        }
        return adresse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        StringBuffer buffer = new StringBuffer();
        if (isOnError()) {
            buffer.append(getSession().getLabel("MSG_IMPRESSION_ECHEC"));
        } else {
            buffer.append(getSession().getLabel("MSG_IMPRESSION_SUCCES") + getNumAff());
        }
        return buffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#initDocument(java.lang.String )
     */
    private ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "Error while api for document");
        }
        document.setISession(getSession());
        document.setCsDomaine(Doc2_preImpressionDeclaration.CS_DOMAINE);
        if (documentDataSource.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE) == null) {
            getDocumentInfo().setDocumentTypeNumber("0125CDS");
            document.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_PLAINTEPENALE);
        } else {
            AFAffiliationManager affMgr = new AFAffiliationManager();
            affMgr.setSession(getSession());
            affMgr.setForAffilieNumero(getNumAff());
            affMgr.setForTypesAffParitaires();
            try {
                affMgr.find();
            } catch (Exception e) {
                getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "L'affilié n'a pu être identifié");
            }
            if (documentDataSource.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE).equals(
                    ILEConstantes.CS_DEF_FORMULE_SOMMATION_DS)
                    || documentDataSource.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE).equals(
                            ILEConstantes.CS_DEF_FORMULE_SOMMATION_DS_LTN)) {
                if (affMgr.size() > 0) {
                    AFAffiliation affEnCours = (AFAffiliation) affMgr.getFirstEntity();

                    if (affEnCours.getTypeAffiliation().equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN)) {
                        getDocumentInfo().setDocumentTypeNumber("0203CDS");
                        document.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_RAPPEL_LTN);
                    } else {
                        getDocumentInfo().setDocumentTypeNumber("0091CDS");
                        document.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_RAPPEL);
                    }
                }
            } else {
                if (affMgr.size() > 0) {
                    AFAffiliation affEnCours = (AFAffiliation) affMgr.getFirstEntity();
                    if (affEnCours.getTypeAffiliation().equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN)) {
                        getDocumentInfo().setDocumentTypeNumber("0200CDS");
                        document.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_SOMMATION_LTN);
                    } else {
                        getDocumentInfo().setDocumentTypeNumber("0092CDS");
                        document.setCsTypeDocument(Doc2_preImpressionDeclaration.CS_SOMMATION);
                    }
                }
            }
        }

        getDocumentInfo().setDocumentProperty("annee", getPeriode());
        getDocumentInfo().setDocumentProperty("document.date", documentDate);

        document.setCsDestinataire(ICTDocument.CS_EMPLOYEUR);
        document.setDefault(new Boolean(true));
        document.setActif(new Boolean(true));
        try {
            document.setCodeIsoLangue(getIsoLangueDestinataire());
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }

        return res;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#getIsoLangueDestinataire()
     */
    @Override
    public String getIsoLangueDestinataire() throws Exception {
        return getAdresse().getLangueDestinataire(getIdTiers());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#getNomDestinataire()
     */
    @Override
    protected String getNomDestinataire() throws Exception {
        return getAdresse().getNomDestinataire(getIdTiers());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#getNomDoc()
     */
    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(DSPreImpressionContentieux_Param.L_NOMDOCCONTENTIEUX);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#getTemplate()
     */
    @Override
    protected String getTemplate() {
        return DSPreImpressionContentieux_Param.TEMPLATE_CONTENTIEUX;

    }

    private String getTexte(int niveau) throws Exception {

        String resString = "";
        ICTTexte texte = null;

        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                // getMemoryLog().logMessage(e3.toString(),FWMessage.ERREUR,getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
            if (listeTextes == null) {
                // getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE"),FWMessage.ERREUR,"");
            } else {
                for (int i = 0; i < listeTextes.size(); i++) {
                    texte = listeTextes.getTexte(i + 1);
                    if ((i + 1) < listeTextes.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }

        return format(resString);
    }

    private TITiers getTiers(String idTiers) {
        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(getIdTiers());
        try {
            tiers.retrieve();
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERREUR_GETTING_TIERS"));
        }
        return tiers;
    }

    @Override
    protected void initDocument(String isoLangue) throws Exception {
        document = getICTDocument();
        try {
            nbNiveaux = document[0].size();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("DOC_VIDE"));
        }

        this.setParametres(DSPreImpressionContentieux_Param.P_CONCERNE, getTexte(1));
        this.setParametres(DSPreImpressionContentieux_Param.P_CORPS, getTexte(2));
        this.setParametres(DSPreImpressionContentieux_Param.P_SIGNATURE, getTexte(3));
        if (!JadeStringUtil.isBlankOrZero(getTexte(4))) {
            this.setParametres(DSPreImpressionContentieux_Param.P_AGENCE_COM, getTexte(4));
        }

        /*
         * setParametres(DSPreImpressionContentieux_Param.P_FACTURATION, getTexte(2, 2));
         * setParametres(DSPreImpressionContentieux_Param.P_CONCLUSTION, getTexte(2, 3));
         */
    }

    /**
     * @param destination
     */
    public void setAdresse(AFAdresseDestination destination) {
        adresse = destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.draco.print.itext.DSAbstractDocument#setHeader(globaz.caisse.report .helper.CaisseHeaderReportBean)
     */
    @Override
    public void setHeader(CaisseHeaderReportBean headerBean) throws Exception {
        // TODO Raccord de méthode auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setNomDoc(java.lang.String)
     */
    @Override
    public void setNomDoc(String nomDoc) {
        // TODO Raccord de méthode auto-généré
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.leo.process.generation.ILEGeneration#setSessionModule(globaz.globall .db.BSession)
     */
    @Override
    public void setSessionModule(BSession session) throws Exception {
        // TODO Raccord de méthode auto-généré

    }

}

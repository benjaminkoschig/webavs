/*
 * Créé le 25 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.itext.suiviLPP;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.envoi.LEChampUtilisateurListViewBean;
import globaz.leo.db.envoi.LEChampUtilisateurViewBean;
import globaz.leo.process.handler.LEEnvoiHandler;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.itext.AFAbstractTiersDocument;
import globaz.naos.itext.AFAdresseDestination;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class AFDenonciationLPP_Doc extends AFAbstractTiersDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String PARAM_RADIE_DES = "radie.des.";
    AFAdresseDestination adresse;
    private AFAffiliation affiliation;
    private String dateDebut = null;
    private String dateFin = null;
    private String langage = null;
    private ArrayList listeChoix = new ArrayList();
    private String[] params;

    public AFDenonciationLPP_Doc() throws Exception {
        super();
    }

    /**
     * @param session
     * @throws Exception
     */
    public AFDenonciationLPP_Doc(BSession session) throws Exception {
        super(session, session.getLabel(AFDenonciationLPP_Param.L_NOMDOCDENONCIATION));
    }

    @Override
    public void beforeBuildReport() {
        ICaisseReportHelper caisseReportHelper;
        try {
            caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), super.getIsoLangueDestinataire());
            CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
            signBean.setService2("");
            signBean.setSignataire2("");
            signBean.setService(getSession().getLabel("MSG_SERVICE_NOM"));
            signBean.setSignataire(getSession().getUserFullName());
            caisseReportHelper.addSignatureParameters(this, signBean);
            super.beforeBuildReport(caisseReportHelper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createDataSource() throws Exception {
        super.createDataSource();
        fillDocInfo();
        // on recherche si il y a des params pour ce document editable
        LEChampUtilisateurListViewBean managerChamp = LEEnvoiHandler.getChampUt(getSession(), getIdEnvoiParent());
        for (int i = 0; i < managerChamp.getSize(); i++) {
            LEChampUtilisateurViewBean Champ = (LEChampUtilisateurViewBean) managerChamp.getEntity(i);
            if (ILEConstantes.CS_GROUPE_CHOIX.equals(Champ.getCsGroupe())) {
                listeChoix.add(Champ.getValeur());
            } else if (ILEConstantes.CS_GROUPE_DATE.equals(Champ.getCsGroupe())) {
                if (ILEConstantes.CS_DATE_DEBUT.equals(Champ.getCsChamp())) {
                    dateDebut = Champ.getValeur();
                }
                if (ILEConstantes.CS_DATE_FIN.equals(Champ.getCsChamp())) {
                    dateFin = Champ.getValeur();
                }
            }
        }
    }

    @Override
    protected void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber("0111CAF");
        super.fillDocInfo();
    }

    /**
     * @return
     */
    @Override
    public AFAdresseDestination getAdresse() {
        if (adresse == null) {
            adresse = new AFAdresseDestination(getSession());
        }
        return adresse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getCategorie()
     */
    @Override
    public String getCategorie() {
        return CodeSystem.TYPE_CAT_ANNONCE_LPP;
    }

    private String getDebutAffiliation(String idAffiliation) throws Exception {
        if (affiliation == null) {
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(idAffiliation);
            affiliation.retrieve();
        }
        return affiliation.getDateDebut();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getDomaine()
     */
    @Override
    public String getDomaine() {
        return CodeSystem.DOMAINE_CAT_LPP;
    }

    private String getFinAffiliation(String idAffiliation) throws Exception {
        if (affiliation == null) {
            affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(idAffiliation);
            affiliation.retrieve();
        }
        return affiliation.getDateFin();
    }

    /**
     * @return
     */
    private String getlangage() {
        if (langage == null) {
            try {
                langage = getAdresse().getLangueDestinataire(getIdDestinataire());
            } catch (Exception e) {
                langage = JACalendar.LANGUAGE_FR;
            }
        }
        return langage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#getNbLevel()
     */
    @Override
    public int getNbLevel() {
        return AFDenonciationLPP_Param.NB_LEVEL;
    }

    @Override
    protected String getNomDestinataire() throws Exception {
        return getAdresse().getNomDestinataire(getIdTiers());
    }

    @Override
    public String getNomDoc() throws Exception {
        return getSession().getLabel(AFDenonciationLPP_Param.L_NOMDOCDENONCIATION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractTiersDocument#getParams(java.lang.String, globaz.globall.db.BSession)
     */
    protected String[] getParams() throws Exception {
        if (params == null) {
            params = new String[6];
            params[0] = getFormulePolitesse();
            params[1] = JACalendar.format(getDebutAffiliation(getIdAffiliation()), getlangage());
            params[2] = JACalendar.format(dateDebut, getlangage());
            params[3] = JACalendar.format(dateFin, getlangage());
            params[4] = params[0];
            params[5] = JadeStringUtil.isEmpty(getFinAffiliation(getIdAffiliation())) ? "" : getTemplateProperty(
                    getDocumentInfo(), AFDenonciationLPP_Doc.PARAM_RADIE_DES + getlangage())
                    + " "
                    + JACalendar.format(getFinAffiliation(getIdAffiliation()), getlangage());
        }
        return params;
    }

    @Override
    protected String getTemplate() {
        return AFDenonciationLPP_Param.TEMPLATE_DENONCIATION_LPP;
    }

    @Override
    protected void initDocument(String isoLangueTiers) throws Exception {
        // recherche des infos d'envoi pour compléter la lettre
        StringBuffer coordEmpl = new StringBuffer(getTemplateProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_AFFILIE + isoLangueTiers));

        // Recherche IDE
        CaisseHeaderReportBean headerbean = new CaisseHeaderReportBean();
        AFIDEUtil.addNumeroIDEInDocForNumAffilie(getSession(), headerbean, getNumAff());
        if (!JadeStringUtil.isEmpty(headerbean.getNumeroIDE())) {
            coordEmpl = AFIDEUtil.removeEndingSpacesAndDoublePoint(coordEmpl);
            coordEmpl.append("/");
            coordEmpl.append(getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_NO_IDE
                    + isoLangueTiers));
            coordEmpl.append("  ");
            coordEmpl.append(getNumAff());
            coordEmpl.append("/");
            coordEmpl.append(headerbean.getNumeroIDE());
        } else {
            coordEmpl.append(getNumAff());
        }

        coordEmpl.append("\n");
        coordEmpl.append(getAdresse().getAdresseDestinataire(getIdTiers(), getNumAff()));
        this.setParametres(AFDenonciationLPP_Param.P_ADRESSE_EMPLOYEUR, coordEmpl.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#loadCatTexte(java.lang.String)
     */
    @Override
    protected void loadCatTexte(String isoLangueTiers) throws Exception {
        StringBuffer buffer;
        // passe les paramètres nécessaires
        ICTDocument listeTxt = getICTDocument(isoLangueTiers)[0];
        if (listeTxt != null) {
            ICTListeTextes crtListe;
            for (int i = 1; i < getNbLevel() + 1; i++) {
                buffer = new StringBuffer();
                try {
                    crtListe = listeTxt.getTextes(i);
                } catch (Exception e) {
                    crtListe = null;
                }
                if (crtListe != null) {
                    if (i != AFDenonciationLPP_Param.LEVEL_CHOIX) {
                        for (Iterator titresIter = crtListe.iterator(); titresIter.hasNext();) {
                            if (buffer.length() > 0) {
                                buffer.append("\n\n");
                            }
                            buffer.append(((ICTTexte) titresIter.next()).getDescription());
                        }
                    } else {
                        for (Iterator titresIter = crtListe.iterator(); titresIter.hasNext();) {
                            ICTTexte crt = (ICTTexte) titresIter.next();
                            if (listeChoix.contains(crt.getPosition())) {
                                if (buffer.length() > 0) {
                                    buffer.append("\n");
                                }
                                buffer.append(crt.getDescription());
                            }
                        }
                    }
                    setFieldToCatTexte(i, buffer.toString());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.naos.itext.AFAbstractDocument#setFieldToCatTexte(int, java.lang.String)
     */
    @Override
    public void setFieldToCatTexte(int i, String value) throws Exception {
        switch (i) {
            case 1:
                this.setParametres(AFDenonciationLPP_Param.P_TITLE, value);
                break;
            case 2:
                this.setParametres(AFDenonciationLPP_Param.P_CORPS, format(value, this.getParams()));
                break;
            case 3:
                this.setParametres(AFDenonciationLPP_Param.P_CORPS2, value);
                break;
            case 4:
                this.setParametres(AFDenonciationLPP_Param.P_DOCUMENT, value);
                break;
            case 5:
                this.setParametres(AFDenonciationLPP_Param.P_CORPS3, format(value, this.getParams()));
                break;
            case 6:
                this.setParametres(AFDenonciationLPP_Param.P_ANNEXES, value);
                break;
            case 7:
                this.setParametres(AFDenonciationLPP_Param.P_ANNEXE, value);
                break;
        }
    }

    @Override
    public void setHeader(CaisseHeaderReportBean headerBean, String isoLangueTiers) {
        // sécurité, être sûr que le tiers est différent du tiers destinataire
        if (getIdDestinataire().equals(getIdTiers())) {
            headerBean.setAdresse("");
        } else {
            try {
                headerBean.setAdresse(getAdresse().getAdresseDestinataire(getIdDestinataire()));
            } catch (Exception e) {
                headerBean.setAdresse("");
            }
        }
        documentDate = JadeStringUtil.isEmpty(getDateImpression()) ? JACalendar.todayJJsMMsAAAA() : getDateImpression();
        headerBean.setDate(JACalendar.format(documentDate, isoLangueTiers));
        headerBean.setNoAffilie(" ");
        headerBean.setNoAvs(" ");
        headerBean.setNomCollaborateur(getSession().getUserFullName());
        headerBean.setUser(getSession().getUserInfo());
        headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());

    }

}

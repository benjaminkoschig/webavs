package globaz.phenix.documentsItext;

import globaz.aquila.service.tiers.COTiersService;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.naos.util.AFUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.external.IntTiers;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationAdresse;
import globaz.pyxis.db.tiers.TIAdministrationAdresseManager;
import globaz.pyxis.util.TIAdresseResolver;
import globaz.pyxis.util.TITiersService;
import java.util.Collection;
import java.util.Iterator;

/**
 * Date de cr�ation : (02.05.2003 10:26:03)
 * 
 * @author: Administrator
 */
public class CPVerso_DOC extends CPIDecision_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Le nom du mod�le */
    private static final String TEMPLATE_NAME = "PHENIX_VERSO";
    protected ICTDocument document = null;

    /**
     * Date de cr�ation : (07.08.2007 10:00:00)
     */
    public CPVerso_DOC() throws Exception {
        this(new BSession(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    /**
     * Date de cr�ation : (26.03.2003 09:38:26)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CPVerso_DOC(BProcess parent) throws Exception {
        super(parent);
        super.setProcessAppelant(processAppelant);
    }

    /**
     * Date de cr�ation : (26.03.2003 09:38:26)
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CPVerso_DOC(BSession session) throws Exception {
        super(session);
        super.setProcessAppelant(processAppelant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport ()
     */
    @Override
    public void afterBuildReport() {
    }

    /**
     * R�cup�re les informations du d�compte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
    }

    /**
     * Date de cr�ation : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {
        // nom du template
        super.setTemplateFile(CPVerso_DOC.TEMPLATE_NAME);
        // On r�cup�re les documents du catalogue de textes n�cessaires
        langueDoc = AFUtil.toLangueIso(decision.getLangue());
        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), "" + langueDoc);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        caisseReportHelper.addHeaderParameters(this, headerBean);
        // renseigner le texte
        StringBuffer buffer = new StringBuffer();
        // -- LE TITRE
        // -----------------------------------------------------------
        for (Iterator<?> titresIter = getDocument().getTextes(1).iterator(); titresIter.hasNext();) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }

            buffer.append(((ICTTexte) titresIter.next()).getDescription());
        }
        this.setParametres(CPIListeDecisionParam.PARAM_TITRE, format(buffer, "").toString());

        // -- LE TEXTE
        // -------------------------------------------------------------
        buffer.setLength(0);

        for (Iterator<?> textesIter = getDocument().getTextes(2).iterator(); textesIter.hasNext();) {
            // if (buffer.length() > 0) {
            // buffer.append("\n");
            // }

            buffer.append(((ICTTexte) textesIter.next()).getDescription());
        }
        // Si verso opposition => recherche de l'adresse du tribunal
        if ((CodeSystem.CS_VERSO_GEN_OPP.equalsIgnoreCase(getDocument().getIdDocument())
                || CodeSystem.CS_VERSO_OPP_IND.equalsIgnoreCase(getDocument().getIdDocument()) || CodeSystem.CS_VERSO_OPP_NAC
                    .equalsIgnoreCase(getDocument().getIdDocument())) && (buffer.indexOf("{") != -1)) {
            // Recherche de l'adresse du tribunal (selon idtiers)

            String adresseTribunal = "";
            try {
                TIAdministrationAdresse adm = null;
                TITiersService service = new TITiersService();
                service.setSession(getSession());
                CACompteAnnexe compte = new CACompteAnnexe();
                compte.setIdTiers(getDecision().getIdTiers());
                compte.setSession(getSession());
                IntTiers tribunal = new COTiersService().getTribunal(getSession(), compte.getTiers(), getAffiliation()
                        .getAffilieNumero());
                if (tribunal != null) {
                    TIAdministrationAdresseManager adminAdresseMgr = new TIAdministrationAdresseManager();
                    adminAdresseMgr.setSession(getSession());
                    adminAdresseMgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
                    adminAdresseMgr.setForIdTiers(tribunal.getIdTiers());
                    adminAdresseMgr.find();
                    Collection<?> col = TIAdresseResolver.resolve(adminAdresseMgr,
                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, IConstantes.CS_APPLICATION_DEFAUT, "");
                    if (col.size() == 0) {
                        adm = null;
                    } else {
                        adm = (TIAdministrationAdresse) col.iterator().next();

                    }
                }
                if (adm == null) {
                    System.out.println("Aucun correspondance trouv�e");
                    this.setParametres(CPIListeDecisionParam.PARAM_CORPS, format(buffer, "").toString());
                } else { // Adresse format�e :
                    TIAdresseFormater formater = new TIAdresseFormater();
                    TIAdresseDataSource source = new TIAdresseDataSource();
                    source.setSession(getSession());
                    source.load(adm, JACalendar.todayJJsMMsAAAA());
                    adresseTribunal = formater.format(source);
                }
            } catch (Exception e) {
                this._addError(getTransaction(), e.getMessage());
            }
            this.setParametres(CPIListeDecisionParam.PARAM_CORPS, format(buffer, adresseTribunal).toString());
        } else {
            this.setParametres(CPIListeDecisionParam.PARAM_CORPS, format(buffer, "").toString());
        }
    }

    public ICTDocument getDocument() {
        return document;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        return "";
    }

    public void setDocument(ICTDocument document) {
        this.document = document;
    }
}

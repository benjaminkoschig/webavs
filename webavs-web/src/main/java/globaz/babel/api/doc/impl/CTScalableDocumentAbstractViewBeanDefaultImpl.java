/*
 * Créé le 17 nov. 06
 */
package globaz.babel.api.doc.impl;

import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class CTScalableDocumentAbstractViewBeanDefaultImpl implements ICTScalableDocument, FWViewBeanInterface,
        BIPersistentObject, BIContainer {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Object[] METHODES_SEL_EMPLOYEUR = new Object[] { new String[] { "idTiersIntervenant",
            "idTiers" } };
    private static final String URL_ECRAN_EDIT_PARAGRAPH = "/babel?userAction=babel.editableDocument.editParagraphes.afficher";
    private static final String URL_ECRAN_SELECT_ANNEXE_COPIE = "/babel?userAction=babel.editableDocument.choixAnnexesCopies.afficher";
    private static final String URL_ECRAN_SELECT_PARAGRAPH = "/babel?userAction=babel.editableDocument.choixParagraphes.chercher";
    private String action = "";

    private String csDestinataire = "";
    private String csDomaine = "";
    private String csType = "";
    private ICTScalableDocumentProperties documentProperties = null;
    private List documentsPreview = null;
    private int ecranPrecedant = ICTScalableDocument.FROM_ECRAN_INIT;
    private String eMailAddress = "";
    private String generatorImplClassName = "";
    private String id = "";
    private String idDemande = "";
    private String idTiersIntervenant = "";
    private boolean isPreviousAction = false;
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
    private String noAVSTiersPrincipal = "";
    private String nomPrenomTiersPrincipal = "";
    private String returnUrl = "";
    private int selectedNiveau = 0;

    private BISession session;

    private String urlFirstPage = "";
    private boolean wantEditParagraph = true;
    private boolean wantSelectAnnexeCopie = true;
    private boolean wantSelectParagraph = true;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param errorLabel
     *            DOCUMENT ME!
     */
    public void _addError(String errorLabel) {
        BSession bSession = (BSession) session;

        bSession.addError(bSession.getLabel(errorLabel));
        setMsgType(FWViewBeanInterface.ERROR);
        message += bSession.getErrors().toString() + "\n\n";
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /**
     * @param errorMessage
     *            DOCUMENT ME!
     */
    public void addErrorAvecMessagePret(String errorMessage) {
        BSession bSession = (BSession) session;

        bSession.addError(errorMessage);
        setMsgType(FWViewBeanInterface.ERROR);
        message += bSession.getErrors().toString() + "\n\n";
    }

    /**
     * Method canDoNext. Cette méthode retourne false, elle est uniquement là pour qu'il n'y ait pas d'erreur dans la
     * page JSP à cause des templates de pagination
     * 
     * @return boolean
     */
    public boolean canDoNext() {
        return false;
    }

    /**
     * Method canDoPrev. Cette méthode retourne false, elle est uniquement là pour qu'il n'y ait pas d'erreur dans la
     * page JSP à cause des templates de pagination
     * 
     * @return boolean
     */
    public boolean canDoPrev() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#clear()
     */
    @Override
    public void clear() {
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#find()
     */
    @Override
    public void find() throws Exception {
    }

    /**
     * getter pour l'attribut action
     * 
     * @return la valeur courante de l'attribut action
     */
    public String getAction() {
        return action;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#getCount()
     */
    @Override
    public int getCount() throws Exception {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#getDocumentCsDestinataire ()
     */
    @Override
    public String getDocumentCsDestinataire() {
        return csDestinataire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#getDocumentCsDomaine()
     */
    @Override
    public String getDocumentCsDomaine() {
        return csDomaine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#getDocumentCsType()
     */
    @Override
    public String getDocumentCsType() {
        return csType;
    }

    @Override
    public List getDocumentsPreview() {
        return documentsPreview;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#getEMailAddress()
     */
    @Override
    public String getEMailAddress() {
        return eMailAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#getEntity(int)
     */
    @Override
    public BIEntity getEntity(int idx) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#getGeneratorImplCalssName ()
     */
    @Override
    public String getGeneratorImplCalssName() {
        return generatorImplClassName;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getIdDemande()
     */
    @Override
    public String getIdDemande() {
        return idDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getIdTiersIntervenant()
     */
    @Override
    public String getIdTiersIntervenant() {
        return idTiersIntervenant;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getMethodesSelecteurIntervenant ()
     */
    @Override
    public Object[] getMethodesSelecteurIntervenant() {
        return CTScalableDocumentAbstractViewBeanDefaultImpl.METHODES_SEL_EMPLOYEUR;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getNextUrl()
     */
    @Override
    public String getNextUrl() {

        if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_INIT) {

            if (wantSelectParagraph) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_SELECT_PARAGRAPH;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_SELECT_PARAGRAPH;
            }

            if (wantEditParagraph) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_EDIT_PARAGRAPH;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_EDIT_PARAGRAPH;
            }

            if (wantSelectAnnexeCopie) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_SELECT_ANNEXE_COPIE;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_SELECT_ANNEXE_COPIE;
            }

        } else if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_SELECT_PARAGRAPH) {

            if (wantEditParagraph) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_EDIT_PARAGRAPH;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_EDIT_PARAGRAPH;
            }

            if (wantSelectAnnexeCopie) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_SELECT_ANNEXE_COPIE;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_SELECT_ANNEXE_COPIE;
            }

        } else if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_EDIT_PARAGRAPH) {

            if (wantSelectAnnexeCopie) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_SELECT_ANNEXE_COPIE;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_SELECT_ANNEXE_COPIE;
            }

        } else if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_SELECT_ANNEXE_COPIE) {

            // on es dans le dernier ecran

        }

        return getReturnUrl();

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getNoAVSTiersPrincipal()
     */
    @Override
    public String getNoAVSTiersPrincipal() {
        return noAVSTiersPrincipal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getNomPrenomTiersPrincipal()
     */
    @Override
    public String getNomPrenomTiersPrincipal() {
        return nomPrenomTiersPrincipal;
    }

    /**
     * Method getOffset. Cette méthode retourne 0, elle est uniquement là pour qu'il n'y ait pas d'erreur dans la page
     * JSP à cause des templates de pagination
     * 
     * @return int
     */
    public int getOffset() {
        return 0;
    }

    @Override
    public String getPreviousUrl() {

        setPreviousAction(true);

        if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_INIT) {

            // on est dansd le premier ecran

        } else if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_SELECT_PARAGRAPH) {

            if (!JadeStringUtil.isEmpty(getUrlFirstPage())) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_INIT;
                return getUrlFirstPage();
            }

        } else if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_EDIT_PARAGRAPH) {

            if (wantSelectParagraph) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_SELECT_PARAGRAPH;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_SELECT_PARAGRAPH;
            }

            if (!JadeStringUtil.isEmpty(getUrlFirstPage())) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_INIT;
                return getUrlFirstPage();
            }

        } else if (ecranPrecedant == ICTScalableDocument.FROM_ECRAN_SELECT_ANNEXE_COPIE) {

            if (wantEditParagraph) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_EDIT_PARAGRAPH;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_EDIT_PARAGRAPH;
            }

            if (wantSelectParagraph) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_SELECT_PARAGRAPH;
                return CTScalableDocumentAbstractViewBeanDefaultImpl.URL_ECRAN_SELECT_PARAGRAPH;
            }

            if (!JadeStringUtil.isEmpty(getUrlFirstPage())) {
                ecranPrecedant = ICTScalableDocument.FROM_ECRAN_INIT;
                return getUrlFirstPage();
            }
        }

        return getReturnUrl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getReturnUrl()
     */
    @Override
    public String getReturnUrl() {
        return returnUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getScalableDocumentDataAsXml()
     */
    @Override
    public String getScalableDocumentDataAsXml() {
        char tout[];
        try {
            File sourceF = new File("D:\\Websphere\\webAVS_workspace\\properties\\babel\\babel2ex.xml");
            long grandComment = sourceF.length();
            tout = new char[(int) grandComment];
            FileReader f = new FileReader(sourceF);
            int dejaLu = 0;
            while (dejaLu < grandComment) {
                dejaLu = dejaLu + f.read(tout, dejaLu, (int) grandComment - dejaLu);
            }
        } catch (IOException e) {
            System.err.println(" Cela ne marche pas. Voila." + e.toString());
            return "";
        }
        return new String(tout);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.babel.api.doc.ICTScalableDocumentViewBean# getScalableDocumentProperties()
     */
    @Override
    public ICTScalableDocumentProperties getScalableDocumentProperties() {
        return documentProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#getSelectedNiveau()
     */
    @Override
    public int getSelectedNiveau() {
        return selectedNiveau;
    }

    /**
     * getter pour l'attribut session
     * 
     * @return Un objet de type BSession. Peut être null si la BISession du viewBean ne peut pas être converti en
     *         BSession
     */
    public BSession getSession() {
        try {
            BSession bSession = (BSession) session;

            return bSession;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * @return
     */
    public BSpy getSpy() {
        return null;
    }

    /**
     * Donne l'url de la premiere page du processus d'impression
     * 
     * @return
     */
    @Override
    public String getUrlFirstPage() {
        return urlFirstPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#wantEditParagraph()
     */
    @Override
    public boolean getWantEditParagraph() {
        return wantEditParagraph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#wantSelectAnnexeCopie()
     */
    @Override
    public boolean getWantSelectAnnexeCopie() {
        return wantSelectAnnexeCopie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#wantSelectParagraph()
     */
    @Override
    public boolean getWantSelectParagraph() {
        return wantSelectParagraph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#hasRightRead()
     */
    @Override
    public boolean hasRightRead() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isPreviousAction() {
        return isPreviousAction;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /**
     * setter pour l'attribut action
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAction(String string) {
        action = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#setDocumentCsDestinataire (java.lang.String)
     */
    @Override
    public void setDocumentCsDestinataire(String csDestinataire) {
        this.csDestinataire = csDestinataire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#setDocumentCsDomaine (java.lang.String)
     */
    @Override
    public void setDocumentCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#setDocumentCsType(java .lang.String)
     */
    @Override
    public void setDocumentCsType(String csType) {
        this.csType = csType;
    }

    @Override
    public void setDocumentsPreview(List docList) {
        documentsPreview = docList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#setEMailAddress(java .lang.String)
     */
    @Override
    public void setEMailAddress(String eMailAdress) {
        eMailAddress = eMailAdress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocumentViewBean#setGeneratorImplClassName (java.lang.String)
     */
    @Override
    public void setGeneratorImplClassName(String className) {
        generatorImplClassName = className;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setIdDemande(java.lang.String)
     */
    @Override
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setIdTiersIntervenant(java.lang .String)
     */
    @Override
    public void setIdTiersIntervenant(String idTiers) {
        idTiersIntervenant = idTiers;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setNoAVSTiersPrincipal(java. lang.String)
     */
    @Override
    public void setNoAVSTiersPrincipal(String noAVS) {
        noAVSTiersPrincipal = noAVS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setNomPrenomTiersPrincipal(java .lang.String)
     */
    @Override
    public void setNomPrenomTiersPrincipal(String nomPrenom) {
        nomPrenomTiersPrincipal = nomPrenom;
    }

    @Override
    public void setPreviousAction(boolean isPreviousAction) {
        this.isPreviousAction = isPreviousAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setReturnUrl(java.lang.String)
     */
    @Override
    public void setReturnUrl(String url) {
        returnUrl = url;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.babel.api.doc.ICTScalableDocumentViewBean# setScalableDocumentProperties
     * (globaz.babel.api.doc.ICTScalableDocumentProperties)
     */
    @Override
    public void setScalableDocumentProperties(ICTScalableDocumentProperties documentProperties) {
        this.documentProperties = documentProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setSelectedNiveau(java.lang. String)
     */
    @Override
    public void setSelectedNiveau(int selectedNiveau) {
        this.selectedNiveau = selectedNiveau;
    }

    /**
     * 
     * @param urlFirstPage
     */
    @Override
    public void setUrlFirstPage(String urlFirstPage) {
        this.urlFirstPage = urlFirstPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setWantEditParagraph(boolean)
     */
    @Override
    public void setWantEditParagraph(boolean wantEditParagraph) {
        this.wantEditParagraph = wantEditParagraph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setWantSelectAnnexeCopie(boolean )
     */
    @Override
    public void setWantSelectAnnexeCopie(boolean wantSelectAnnexeCopie) {
        this.wantSelectAnnexeCopie = wantSelectAnnexeCopie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.babel.api.doc.ICTScalableDocument#setWantSelectParagraph(boolean)
     */
    @Override
    public void setWantSelectParagraph(boolean wantSelectParagraph) {
        this.wantSelectParagraph = wantSelectParagraph;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIContainer#size()
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /**
     * Valide les données de ce viewBean.
     * 
     * @return vrai si les données de ce viewBean sont valides.
     */
    public boolean validate() {
        return true;
    }

}

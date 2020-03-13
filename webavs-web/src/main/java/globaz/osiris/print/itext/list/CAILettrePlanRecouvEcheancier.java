package globaz.osiris.print.itext.list;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.access.recouvrement.CACouvertureSection;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntTiers;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexandre Cuva, 13-mai-2005
 */
public class CAILettrePlanRecouvEcheancier extends CADocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String NUMERO_REFERENCE_INFOROM = "0042GCA";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "CAIEcheancierListEcheances";
    private double cumulSolde = 0;
    private int docPageNumber = 0;
    private String joindreBVR = "";
    private BigDecimal montantTotal = new BigDecimal(0);

    /** Données du formulaire */
    private CAPlanRecouvrement plan = new CAPlanRecouvrement();

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvEcheancier(BProcess parent) throws FWIException {
        super(parent, CAILettrePlanRecouvEcheancier.TEMPLATE_NAME);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvEcheancier(BSession parent) throws FWIException {
        super(parent, CAILettrePlanRecouvEcheancier.TEMPLATE_NAME);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();

        getDocumentInfo().setTemplateName(CAILettrePlanRecouvEcheancier.TEMPLATE_NAME);

        if ((getPlanRecouvrement() != null) && (getPlanRecouvrement() instanceof CAPlanRecouvrement)) {
            if (!JadeStringUtil.isBlank((getPlanRecouvrement()).getCompteAnnexe().getId())) {
                String numAff = (getPlanRecouvrement()).getCompteAnnexe().getIdExterneRole();
                getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
                try {
                    IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                            TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                            affilieFormater.unformat(numAff));
                    TIDocumentInfoHelper.fill(getDocumentInfo(),
                            (getPlanRecouvrement()).getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                            numAff, affilieFormater.unformat(numAff));
                } catch (Exception e) {
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
                }
            }
        }
        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        super.beforeExecuteReport();

        if (getParent() != null) {
            ++docPageNumber;
        }
        try {
            imprimerPlansRecouvEcheancier();
        } catch (JAException e) {
            JadeLogger.error(this, e);
        }

        setNumeroReferenceInforom(CAILettrePlanRecouvEcheancier.NUMERO_REFERENCE_INFOROM);
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        double soldeResiduel = 0;
        String compteCADesc = "";

        // On récupère les documents du catalogue de textes nécessaires
        setTypeDocument(CACodeSystem.CS_TYPE_SURSIS_ECHEANCIER);

        // Récupération des données
        ArrayList echeances = (ArrayList) currentEntity();
        IntTiers affilie = getPlanRecouvrement().getCompteAnnexe().getTiers();

        if (!JadeStringUtil.isBlank(getPlanRecouvrement().getId())) {
            soldeResiduel = Double.parseDouble(getPlanRecouvrement().getCumulSoldeSections());
            if (!JadeStringUtil.isBlank(getPlanRecouvrement().getCompteAnnexe().getId())) {
                compteCADesc = getPlanRecouvrement().getCompteAnnexe().getDescription();
            }
        }

        // Sette la langue selon le tier.
        _setLangueFromTiers(affilie);

        // Gestion du modèle et du titre
        setTemplateFile(CAILettrePlanRecouvEcheancier.TEMPLATE_NAME);
        setDocumentTitle(getSession().getApplication().getLabel("OSIRIS_LETTRE_PLAN_RECOUV_DECISION", _getLangue())
                + " " + compteCADesc);

        // Gestion de l'en-tête/pied de page/signature
        this._handleHeaders(null, false, false, false);
        // Renseigne les paramètres du document
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_DATE_SECTION,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_DATE_SECTION", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_SECTION,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_SECTION", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_TITRE,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_TITRE", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_DATE_ECHEANCE,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_DATE_ECHEANCE", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_DATE_PAIEMENT,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_DATE_PAIEMENT", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_MNT_ECHEANCE,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_MONTANT", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_MNT_PAIEMENT,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_PAIEMENT", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.LIBELLE_SOLDE,
                getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_SOLDE", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.PAGE,
                getSession().getApplication().getLabel("ECHEANCIER_PAGE", _getLangue()));
        // setParametres(CAILettrePlanRecouvParam.LIBELLE_SOLDE_RESIDUEL,
        // getSession().getApplication().getLabel("ECHEANCIER_LIBELLE_SOLDE_RESIDUEL", _getLangue()));
        this.setParametres(CAILettrePlanRecouvParam.COMPANYNAME,
                _getProperty(CADocumentManager.JASP_PROP_BODY_COMPANY_NAME, ""));
        this.setParametres(CAILettrePlanRecouvParam.AFFILIE, getPlanRecouvrement().getCompteAnnexe().getIdExterneRole()
                + " - " + affilie.getNom());

        // Détail des sections
        // Pour chaque section couverte du plan
        List sectionsCouvertes = plan.fetchSectionsCouvertes().getContainer();
        String dates = "";
        String noSection = "";
        for (Iterator sectionIter = sectionsCouvertes.iterator(); sectionIter.hasNext();) {
            CASection section = ((CACouvertureSection) sectionIter.next()).getSection();
            dates += JACalendar.format(section.getDateSection(), _getLangue()) + "\n";
            noSection += section.getIdExterne() + " " + section.getDescription(_getLangue()) + "\n";
        }
        this.setParametres(CAILettrePlanRecouvParam.DATE, dates);
        this.setParametres(CAILettrePlanRecouvParam.SECTION, noSection);

        setCumulSolde(soldeResiduel);
        // Renseigne la zone avant le détail des sections. Modifiable dans le catalogue de texte
        this.setParametres(CAILettrePlanRecouvParam.ENTETE, format(getTexte(1, 1)));
        // Renseigne la zone après le détail des sections. Modifiable dans le catalogue de texte
        this.setParametres(CAILettrePlanRecouvParam.P_REMARQUE, format(getTexte(2, 1)));

        // Renseigne les lignes dans le tableau du document
        ArrayList liste = new ArrayList();
        for (Iterator iter = echeances.iterator(); iter.hasNext();) {
            CAEcheancePlan echeance = (CAEcheancePlan) iter.next();
            // Ajoute la ligne dans le document
            HashMap map = new HashMap();
            map.put(CAILettrePlanRecouvParam.COL1,
                    JACalendar.format(echeance.getDateExigibilite(), JACalendar.FORMAT_DDsMMsYYYY));
            map.put(CAILettrePlanRecouvParam.COL2, new BigDecimal(echeance.getMontant()));
            map.put(CAILettrePlanRecouvParam.COL5, new BigDecimal(echeance.getMontantPaye()));
            soldeResiduel = soldeResiduel - Double.parseDouble(echeance.getMontant());
            montantTotal = montantTotal.add(new BigDecimal(echeance.getMontantPaye()));
            /*
             * Pour permettre réimpression avec échéance déjà cloturée if (soldeResiduel < 0) {
             * super._addError(getSession().getLabel("OSIRIS_ERR_SOLDE_RESIDUEL_INF_ZERO")); throw new
             * Exception(getSession().getLabel("OSIRIS_ERR_SOLDE_RESIDUEL_INF_ZERO")); }
             */
            // String displayedSoldeResiduel = JANumberFormatter.format(montantTotal.toString());

            map.put(CAILettrePlanRecouvParam.COL3,
                    new BigDecimal(echeance.getMontant()).add(new BigDecimal(echeance.getMontantPaye())));
            map.put(CAILettrePlanRecouvParam.COL4, echeance.getDateEffective()); // Date de paiement
            liste.add(map);
        }
        if (liste.size() > 0) {
            this.setDataSource(liste);
        }
    }

    /**
     * Formate le texte, remplace les {x}
     * 
     * @author: sel Créé le : 22 déc. 06
     * @param paragraphe
     * @return
     */
    private String format(StringBuilder paragraphe) {
        StringBuilder res = new StringBuilder("");
        IntTiers affilie = getPlanRecouvrement().getCompteAnnexe().getTiers();
        try {
            for (int i = 0; i < paragraphe.length(); i++) {
                if (paragraphe.charAt(i) != '{') {
                    res.append(paragraphe.charAt(i));
                } else if (paragraphe.charAt(i + 1) == '1') {
                    // Montant total à verser
                    res.append(JANumberFormatter.format(montantTotal));
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '2') {
                    // Nom de l'affilie
                    res.append(affilie.getNom());
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '3') {
                    // Id de l'affilie
                    res.append(getPlanRecouvrement().getCompteAnnexe().getIdExterneRole());
                    i = i + 2;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return res.toString();
    }

    /**
     * @return La valeur courante de la propriété
     */
    public double getCumulSolde() {
        return cumulSolde;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getJoindreBVR() {
        return joindreBVR;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public CAPlanRecouvrement getPlanRecouvrement() {
        return plan;
    }

    /**
     * Imprimer la liste des échéances du plan de recouvrement
     */
    private void imprimerPlansRecouvEcheancier() throws JAException {
        CAEcheancePlanManager echeancePlanManager = new CAEcheancePlanManager();
        ArrayList listEcheances = new ArrayList();
        try {
            if (!JadeStringUtil.isBlank(getPlanRecouvrement().getId())) {
                // Recherche les écheances pour le plan de recouvrement
                echeancePlanManager.setSession(getSession());
                echeancePlanManager.setForIdPlanRecouvrement(getPlanRecouvrement().getId());
                // que les échéances ouvertes
                echeancePlanManager.orderByDateExigibiliteASC();
                echeancePlanManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                for (int i = 0; i < echeancePlanManager.size(); i++) {
                    CAEcheancePlan echeance = (CAEcheancePlan) echeancePlanManager.getEntity(i);
                    listEcheances.add(echeance);
                    montantTotal = montantTotal.add(new BigDecimal(echeance.getMontant()));
                }
                if (listEcheances.size() > 0) {
                    addEntity(listEcheances);
                }
            }
        } catch (Exception e) {
            super._addError(getSession().getLabel("OSIRIS_IMPRESSION_ECHEANCIER") + " : " + e.getMessage());
            super.setMsgType(FWViewBeanInterface.WARNING);
            super.setMessage(getSession().getLabel("OSIRIS_IMPRESSION_ECHEANCIER") + " : " + e.getMessage());
            throw new JAException(getSession().getLabel("OSIRIS_IMPRESSION_ECHEANCIER") + " : " + e.getMessage());
        } finally {
            echeancePlanManager = null;
        }
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setCumulSolde(double d) {
        cumulSolde = d;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setJoindreBVR(String string) {
        joindreBVR = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setLangueDocument(String langueDoc) {
        _setLangueDocument(langueDoc);
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setPlanRecouvrement(CAPlanRecouvrement planRecouvrement) {
        plan = planRecouvrement;
    }
}

package globaz.hercule.itext.controleEmployeur;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.CEAttributionPtsService;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.service.CEDeclarationSalaireService;
import globaz.hercule.service.CETiersService;
import globaz.hercule.service.declarationSalaire.CEDecSalMassesReprises;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.util.AFUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;

/**
 * Class qui génère le compte rendu d'un contrôle employeur
 * 
 * @author Sullivann Corneille
 * @since 2 nov. 2010
 */
public class CECompteRendu_Doc extends CEDSLettre {

    private static final long serialVersionUID = -4002659839564295332L;
    private static final String DOCUMENT_COMPTE_RENDU = "DOCUMENT_COMPTE_RENDU";
    public static final String NUM_INFOROM = "0256CCE";

    private static final String TITRE_COMPTE_RENDU = "TITRE_COMPTE_RENDU";

    private String idControle;
    private boolean nextUneSeuleFois = true;

    /**
     * Constructeur de CECompteRendu_Doc
     */
    public CECompteRendu_Doc() throws Exception {
        super(new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE));
    }

    /**
     * Constructeur de CECompteRendu_Doc
     */
    public CECompteRendu_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, CEApplication.APPLICATION_HERCULE_ROOT, CECompteRendu_Doc.DOCUMENT_COMPTE_RENDU);
        super.setDocumentTitle(getSession().getLabel(CECompteRendu_Doc.TITRE_COMPTE_RENDU));
        setParentWithCopy(parent);
    }

    /**
     * Constructeur de CECompteRendu_Doc
     */
    public CECompteRendu_Doc(BSession session) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CECompteRendu_Doc.DOCUMENT_COMPTE_RENDU);
        super.setDocumentTitle(getSession().getLabel(CECompteRendu_Doc.TITRE_COMPTE_RENDU));
    }

    /**
     * Constructeur de CECompteRendu_Doc
     */
    public CECompteRendu_Doc(BSession session, String idControle) throws java.lang.Exception {
        super(session, CEApplication.APPLICATION_HERCULE_ROOT, CECompteRendu_Doc.DOCUMENT_COMPTE_RENDU);
        super.setDocumentTitle(getSession().getLabel(CECompteRendu_Doc.TITRE_COMPTE_RENDU));
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setDocumentTitle(getSession().getLabel(CECompteRendu_Doc.TITRE_COMPTE_RENDU));
        setFileTitle(getSession().getLabel(CECompteRendu_Doc.TITRE_COMPTE_RENDU));
    }

    @Override
    public void createDataSource() throws Exception {

        getDocumentInfo().setDocumentTypeNumber(CECompteRendu_Doc.NUM_INFOROM);
        getDocumentInfo().setArchiveDocument(true);
        getDocumentInfo().setPublishDocument(isPublishDocument());
        getDocumentInfo().setDocumentDate(getDateEnvoi());

        // récupération du requerant en cours et du tiers correspondant
        TITiersViewBean tiers = CETiersService.retrieveTiersViewBean(getSession(), getIdTiers());

        // On set la langue du requerant
        setLangueIsoRequerant(AFUtil.toLangueIso(tiers.getLangue()));

        // Récupération du motif du controle
        String motif = CEControleEmployeurService.findLibelleMotifControle(getSession(), getIdControle(),
                AFUtil.toLangueSimple(tiers.getLangue()));

        // Creation du document
        setCorpsDocument(getNumAffilie() + " - " + tiers.getNom(), getDateDebutControle() + " - "
                + getDateFinControle(), motif, tiers.getLangueIso());

        // Remplissage des données avec le résultat de l'attribution des points
        setDonneesAttributionPoints();

        // Récupération du document ITEXT
        setTemplateFile(CEDSLettre.MODEL_NAME_COMPTE_RENDU);

        // Numero affilié passé au docinfo
        fillDocInfo();

        // Mise en place du header et du footer
        setHeader(tiers, _getDateImpression(tiers));
        nextUneSeuleFois = false;
    }

    @Override
    public boolean next() throws FWIException {
        return nextUneSeuleFois;
    }

    /**
     * Rempli le document suivant la langue !
     * 
     * @param nomAffilie
     * @param periodeControle
     * @param motifControle
     * @throws Exception
     */
    private void setCorpsDocument(String nomAffilie, String periodeControle, String motifControle, String langueIso)
            throws Exception {

        // Cartouche d'entete
        this.setParametres("P_TITRE", getSession().getApplication().getLabel("P_TITRE", langueIso));
        this.setParametres("P_EMPLOYEUR", getSession().getApplication().getLabel("P_EMPLOYEUR", langueIso));
        this.setParametres("P_EMPLOYEUR_VALUE", nomAffilie);
        this.setParametres("P_PERIODE_CONTROLE", getSession().getApplication()
                .getLabel("P_PERIODE_CONTROLE", langueIso));
        this.setParametres("P_PERIODE_CONTROLE_VALUE", periodeControle);
        this.setParametres("P_REPRISE", getSession().getApplication().getLabel("P_REPRISE", langueIso));
        this.setParametres("P_AVS", getSession().getApplication().getLabel("P_AVS", langueIso));
        this.setParametres("P_AF", getSession().getApplication().getLabel("P_AF", langueIso));
        this.setParametres("P_AC", getSession().getApplication().getLabel("P_AC", langueIso));
        this.setParametres("P_AUTRE", getSession().getApplication().getLabel("P_AUTRE", langueIso));
        this.setParametres("P_NB_ECRITURE", getSession().getApplication().getLabel("P_NB_ECRITURE", langueIso));
        this.setParametres("P_MOTIF", getSession().getApplication().getLabel("P_MOTIF", langueIso));
        this.setParametres("P_MOTIF_VALUE", motifControle);

        this.setParametres("P_OBSERVATIONS_GENERALES",
                getSession().getApplication().getLabel("P_OBSERVATIONS_GENERALES", langueIso));

        // Critere evaluation
        this.setParametres("P_CRITERE_LABEL", getSession().getApplication().getLabel("P_CRITERE_LABEL", langueIso));
        this.setParametres("P_CRITERE_0", getSession().getApplication().getLabel("P_CRITERE_0", langueIso));
        this.setParametres("P_CRITERE_3", getSession().getApplication().getLabel("P_CRITERE_3", langueIso));
        this.setParametres("P_CRITERE_9", getSession().getApplication().getLabel("P_CRITERE_9", langueIso));
        this.setParametres("P_RESULTAT_CONTROLE",
                getSession().getApplication().getLabel("P_RESULTAT_CONTROLE", langueIso));

        // Qualite ressource humaine
        this.setParametres("P_QUALITE_RH", getSession().getApplication().getLabel("P_QUALITE_RH", langueIso));
        this.setParametres("P_QRH_DESC", getSession().getApplication().getLabel("P_QRH_DESC", langueIso));

        this.setParametres("P_QUALITE_0", getSession().getApplication().getLabel("P_QUALITE_0", langueIso));
        this.setParametres("P_QUALITE_1", getSession().getApplication().getLabel("P_QUALITE_1", langueIso));
        this.setParametres("P_QUALITE_2", getSession().getApplication().getLabel("P_QUALITE_2", langueIso));
        this.setParametres("P_QUALITE_3", getSession().getApplication().getLabel("P_QUALITE_3", langueIso));

        // Criteres spécifiques entreprises
        this.setParametres("P_COLLABORATION", getSession().getApplication().getLabel("P_COLLABORATION", langueIso));
        this.setParametres("P_CSE_LABEL", getSession().getApplication().getLabel("P_CSE_LABEL", langueIso));
        this.setParametres("P_CSE_0", getSession().getApplication().getLabel("P_CSE_0", langueIso));
        this.setParametres("P_CSE_1", getSession().getApplication().getLabel("P_CSE_1", langueIso));
        this.setParametres("P_CSE_2", getSession().getApplication().getLabel("P_CSE_2", langueIso));
        this.setParametres("P_CSE_3", getSession().getApplication().getLabel("P_CSE_3", langueIso));

        this.setParametres("P_CSE_DESCRIPTION", getSession().getApplication().getLabel("P_CSE_DESCRIPTION", langueIso));

        this.setParametres("P_TOTAL", getSession().getApplication().getLabel("P_TOTAL", langueIso));

    }

    /**
     * Methode qui rempli les données concerant les points du controle.
     * 
     * @throws HerculeException
     */
    private void setDonneesAttributionPoints() throws HerculeException {

        // Recherche des points pour le controle
        CEAttributionPts attributionPoints = CEAttributionPtsService.retrieveAttributionPtsActif(getSession(),
                getTransaction(), null, getNumAffilie(), getDateDebutControle(), getDateFinControle());

        CEDecSalMassesReprises massesReprises = CEDeclarationSalaireService.retrieveInfosMassesReprises(getSession(),
                getTransaction(), getIdControle());

        // Si on a une notation, on rempli les champs
        if (attributionPoints != null) {

            // Les masses reprises
            if (massesReprises != null) {
                this.setParametres("P_AVS_V", "0.00");
                this.setParametres("P_AF_V", "0.00");
                this.setParametres("P_AC_V", "0.00");

                if (massesReprises.getMasseAvs() != null) {
                    this.setParametres("P_AVS_V", JANumberFormatter.format(massesReprises.getMasseAvs()));
                }
                if (massesReprises.getMasseAF() != null) {
                    this.setParametres("P_AF_V", JANumberFormatter.format(massesReprises.getMasseAF()));
                }
                // Les masses AC1 et AC2 sont additionnées
                if (massesReprises.getMasseAC1() != null && massesReprises.getMasseAC2() != null) {
                    BigDecimal masseAC = massesReprises.getMasseAC1().add(massesReprises.getMasseAC2());
                    this.setParametres("P_AC_V", JANumberFormatter.format(masseAC));
                } else if (massesReprises.getMasseAC1() != null) {
                    this.setParametres("P_AC_V", JANumberFormatter.format(massesReprises.getMasseAC1()));
                } else if (massesReprises.getMasseAC2() != null) {
                    this.setParametres("P_AC_V", JANumberFormatter.format(massesReprises.getMasseAC2()));
                }

                // La masse autres ne peut pas être récupérée
                this.setParametres("P_AUTRE_V", " ");
                // Le nombre d'écritures reprises
                if (massesReprises.getNbReprise() != null) {
                    this.setParametres("P_NB_ECRITURE_V", massesReprises.getNbReprise().toString());
                }
            }

            // Observations générales
            if (!JadeStringUtil.isBlankOrZero(attributionPoints.getObservations())) {
                this.setParametres("P_COM_OBSERVATIONS_GENERALES", attributionPoints.getObservations());
            }

            // Notes du résultat du controle
            if (ICEControleEmployeur.DERNIERE_REVISION_PAS_DIFFERENCES.equals(attributionPoints.getDerniereRevision())) {
                this.setParametres("P_RESULTAT_CONTROLE_0", "X");
            }
            if (ICEControleEmployeur.DERNIERE_REVISION_PROBLEME_MINIME.equals(attributionPoints.getDerniereRevision())) {
                this.setParametres("P_RESULTAT_CONTROLE_3", "X");
            }
            if (ICEControleEmployeur.DERNIERE_REVISION_PROBLEME_IMPORTANT.equals(attributionPoints
                    .getDerniereRevision())) {
                this.setParametres("P_RESULTAT_CONTROLE_9", "X");
            }

            this.setParametres("P_RESULTAT_CONTROLE_R",
                    "" + new FWCurrency(getSession().getCode(attributionPoints.getDerniereRevision())).intValue());

            // Notes des ressources humaines
            if (ICEControleEmployeur.QUALITE_RH_TB.equals(attributionPoints.getQualiteRH())) {
                this.setParametres("P_QUALITE_RH_0", "X");
            }
            if (ICEControleEmployeur.QUALITE_RH_AB.equals(attributionPoints.getQualiteRH())) {
                this.setParametres("P_QUALITE_RH_1", "X");
            }
            if (ICEControleEmployeur.QUALITE_RH_M.equals(attributionPoints.getQualiteRH())) {
                this.setParametres("P_QUALITE_RH_2", "X");
            }
            if (ICEControleEmployeur.QUALITE_RH_TM.equals(attributionPoints.getQualiteRH())) {
                this.setParametres("P_QUALITE_RH_3", "X");
            }

            this.setParametres("P_QUALITE_RH_R",
                    "" + new FWCurrency(getSession().getCode(attributionPoints.getQualiteRH())).intValue());

            // Remarque ressources humaines
            if (!JadeStringUtil.isBlankOrZero(attributionPoints.getQualiteRHCom())) {
                this.setParametres("P_COM_QUALITE_RH", attributionPoints.getQualiteRHCom());
            }

            // Notes de collaboration
            if (ICEControleEmployeur.COLLABORATION_B.equals(attributionPoints.getCollaboration())) {
                this.setParametres("P_COLLABORATION_0", "X");
            }
            if (ICEControleEmployeur.COLLABORATION_C.equals(attributionPoints.getCollaboration())) {
                this.setParametres("P_COLLABORATION_1", "X");
            }
            if (ICEControleEmployeur.COLLABORATION_M.equals(attributionPoints.getCollaboration())) {
                this.setParametres("P_COLLABORATION_2", "X");
            }
            if (ICEControleEmployeur.COLLABORATION_TM.equals(attributionPoints.getCollaboration())) {
                this.setParametres("P_COLLABORATION_3", "X");
            }

            this.setParametres("P_COLLABORATION_R",
                    "" + new FWCurrency(getSession().getCode(attributionPoints.getCollaboration())).intValue());

            // Notes de critères spécifiques entreprise
            if (ICEControleEmployeur.CRITERES_ENTREPRISE_PP.equals(attributionPoints.getCriteresEntreprise())) {
                this.setParametres("P_CSE_X_0", "X");
            }
            if (ICEControleEmployeur.CRITERES_ENTREPRISE_P.equals(attributionPoints.getCriteresEntreprise())) {
                this.setParametres("P_CSE_X_1", "X");
            }
            if (ICEControleEmployeur.CRITERES_ENTREPRISE_AP.equals(attributionPoints.getCriteresEntreprise())) {
                this.setParametres("P_CSE_X_2", "X");
            }
            if (ICEControleEmployeur.CRITERES_ENTREPRISE_TP.equals(attributionPoints.getCriteresEntreprise())) {
                this.setParametres("P_CSE_X_3", "X");
            }

            this.setParametres("P_CSE_R",
                    "" + new FWCurrency(getSession().getCode(attributionPoints.getCriteresEntreprise())).intValue());

            // Remarques critère spécifiques
            if (!JadeStringUtil.isBlankOrZero(attributionPoints.getCriteresEntrepriseCom())) {
                this.setParametres("P_COM_CSE", attributionPoints.getCriteresEntrepriseCom());
            }

            // Affichage du nombre de points
            this.setParametres("P_TOTAL_PTS", attributionPoints.getNbrePoints());
        }
    }

    /**
     * Setter de idControle
     * 
     * @param idControle the idControle to set
     */
    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    /**
     * Getter de idControle
     * 
     * @return the idControle
     */
    public String getIdControle() {
        return idControle;
    }
}

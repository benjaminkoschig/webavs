package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeListViewBean;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class REListeDemandesAttente extends FWIAbstractManagerDocumentList {

    /**
     * 
     * @author lga
     * 
     */
    private class Pojo extends REDemandeRenteJointDemandeViewBean implements Comparable<Pojo> {

        private static final long serialVersionUID = 1L;
        private REDemandeRenteJointDemandeViewBean entity;
        private String communePolitique;

        public Pojo(REDemandeRenteJointDemandeViewBean entity) {
            this.entity = entity;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getIdTiersRequerant()
         */
        @Override
        public String getIdTiersRequerant() {
            return entity.getIdTiersRequerant();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getCsEtatDemande()
         */
        @Override
        public String getCsEtatDemande() {
            return entity.getCsEtatDemande();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getIdGestionnaire()
         */
        @Override
        public String getIdGestionnaire() {
            return entity.getIdGestionnaire();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getCsTypeDemande()
         */
        @Override
        public String getCsTypeDemande() {
            return entity.getCsTypeDemande();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getDateDebut()
         */
        @Override
        public String getDateDebut() {
            return entity.getDateDebut();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getDateDepot()
         */
        @Override
        public String getDateDepot() {
            return entity.getDateDepot();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getDateFin()
         */
        @Override
        public String getDateFin() {
            return entity.getDateFin();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getDateNaissance()
         */
        @Override
        public String getDateNaissance() {
            return entity.getDateNaissance();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getIdDemandeRente()
         */
        @Override
        public String getIdDemandeRente() {
            return entity.getIdDemandeRente();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getNoAVS()
         */
        @Override
        public String getNoAVS() {
            return entity.getNoAVS();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getNom()
         */
        @Override
        public String getNom() {
            return entity.getNom();
        }

        /**
         * @return
         * @see globaz.corvus.db.demandes.REDemandeRenteJointDemande#getPrenom()
         */
        @Override
        public String getPrenom() {
            return entity.getPrenom();
        }

        @Override
        public int compareTo(Pojo o) {
            if (getAjouterCommunePolitique()) {
                int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result1 != 0) {
                    return result1;
                }
            }

            int result2 = getNom().compareTo(o.getNom());
            if (result2 != 0) {
                return result2;
            }

            return getPrenom().compareTo(o.getPrenom());
        }
    }

    private static final long serialVersionUID = 1L;
    private boolean ajouterCommunePolitique;

    public REListeDemandesAttente() {
        super(null, null, "", "Liste des demandes en attente", new REDemandeRenteJointDemandeListViewBean(),
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    public REListeDemandesAttente(BSession session) {
        super(session, null, "", session.getLabel("LISTE_DEMANDES_ATTENTE_OBJET_MAIL"),
                new REDemandeRenteJointDemandeListViewBean(), REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    @Override
    public void _beforeExecuteReport() {
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_DEMANDES_EN_ATTENTE);

        String date = "";
        try {
            date = " (" + PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()) + ")";
        } catch (JAException e1) {
            JadeLogger.error(this, e1);
        }
        _setDocumentTitle(getSession().getLabel("LISTE_DEMANDES_ATTENTE_OBJET_MAIL") + date);

        try {
            // Création du manager
            REDemandeRenteJointDemandeListViewBean manager = (REDemandeRenteJointDemandeListViewBean) _getManager();
            manager.setSession(getSession());
            manager.setForCsEtatDemandeIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL + ", "
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE + ", "
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE + ", "
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);

            manager.setOrderBy(REDemandeRenteJointDemande.FIELDNAME_NOM_FOR_SEARCH + ","
                    + REDemandeRenteJointDemande.FIELDNAME_PRENOM_FOR_SEARCH);

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                abort();
                getMemoryLog().logMessage(getSession().getLabel("LISTE_DEMANDES_ATTENTE_AUCUNE"), FWServlet.ERROR,
                        getSession().getLabel("LISTE_DEMANDES_ATTENTE_AUCUNE"));
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    @Override
    protected void bindPageHeader() throws Exception {
        super.bindPageHeader();
        if (getAjouterCommunePolitique()) {
            String utilisateur = getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey())
                    + " : " + getSession().getUserId();
            this._addHeaderLine(getFontDate(), utilisateur, null, null, null, null);
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * Contenu des cellules
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        Pojo demandeRente = (Pojo) entity;

        if (getAjouterCommunePolitique()) {
            _addCell(demandeRente.getCommunePolitique());
        }
        String detailAssure = demandeRente.getNoAVS() + " / " + demandeRente.getNom() + " " + demandeRente.getPrenom()
                + " / " + demandeRente.getDateNaissance();

        _addCell(detailAssure);
        _addCell(demandeRente.getDateDepot());
        _addCell(getSession().getCodeLibelle(demandeRente.getCsTypeDemande()));
        _addCell(demandeRente.getDateDebut());
        _addCell(demandeRente.getDateFin());

        FWCurrency montantRABloquee = new FWCurrency("0");

        RERenteAccJoinTblTiersJoinDemRenteManager raJointDemandeMgr = new RERenteAccJoinTblTiersJoinDemRenteManager();
        raJointDemandeMgr.setSession(getSession());
        raJointDemandeMgr.setForNoDemandeRente(demandeRente.getIdDemandeRente());
        try {
            raJointDemandeMgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new FWIException(e.getMessage());
        }

        for (int i = 0; i < raJointDemandeMgr.size(); i++) {
            RERenteAccJoinTblTiersJoinDemandeRente raJointDemande = (RERenteAccJoinTblTiersJoinDemandeRente) raJointDemandeMgr
                    .get(i);

            if (!JadeStringUtil.isBlankOrZero(raJointDemande.getIdEnteteBlocage())) {
                REEnteteBlocage entete = new REEnteteBlocage();
                entete.setSession(getSession());
                entete.setIdEnteteBlocage(raJointDemande.getIdEnteteBlocage());

                try {
                    entete.retrieve(getTransaction());
                    montantRABloquee.add(entete.getMontantBloque());
                    montantRABloquee.sub(entete.getMontantDebloque());
                } catch (Exception e) {
                    ;
                }
            }
        }

        _addCell(montantRABloquee.toStringFormat());
        _addCell(getSession().getCodeLibelle(demandeRente.getCsEtatDemande()));
        _addCell(demandeRente.getIdGestionnaire());
        _addCell(demandeRente.getIdDemandeRente());
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_DEMANDES_ATTENTE_OBJET_MAIL");
    }

    /**
     * Initialisation des colonnes et des groupes
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void initializeTable() {
        // Ajout des colonnes
        if (getAjouterCommunePolitique()) {
            this._addColumnCenter(getSession()
                    .getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 8);
        }
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_ASSURE"), 60);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_DATE_DEPOT"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_TYPE_DEMANDE"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_DATE_DEBUT"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_DATE_FIN"), 15);
        this._addColumnRight(getSession().getLabel("LISTE_DEMANDES_ATTENTE_MONTANTS_BLOQUES"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_ETAT"), 15);
        this._addColumnLeft(getSession().getLabel("LISTE_DEMANDES_ATTENTE_GESTIONNAIRE"), 15);
        this._addColumnRight(getSession().getLabel("LISTE_DEMANDES_ATTENTE_NO"), 5);

        // Management du contenu du manager
        JAVector container = getManager().getContainer();
        Set<String> setIdTiers = new HashSet<String>();
        JAVector pojos = new JAVector();
        Pojo pojo = null;
        for (Object object : container) {
            pojo = new Pojo((REDemandeRenteJointDemandeViewBean) object);
            pojos.add(pojo);
            setIdTiers.add(pojo.getIdTiersRequerant());
        }

        // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
        if (getAjouterCommunePolitique()) {
            Date date = new Date(); // on prend la date du jour
            Map<String, String> mapCommuneParIdTiers = PRTiersHelper
                    .getCommunePolitique(setIdTiers, date, getSession());

            for (Object p : pojos) {
                ((Pojo) p).setCommunePolitique(mapCommuneParIdTiers.get(((Pojo) p).getIdTiersRequerant()));
            }
        }

        Collections.sort(pojos);
        getManager().setContainer(pojos);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public final boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public final void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

}

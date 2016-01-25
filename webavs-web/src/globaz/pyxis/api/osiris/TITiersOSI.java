package globaz.pyxis.api.osiris;

import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntAdresseCourrier;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressecourrier.TIAvoirGroupeLocalite;
import globaz.pyxis.db.adressecourrier.TIAvoirGroupeLocaliteManager;
import globaz.pyxis.db.adressecourrier.TILocaliteLieeAGroupe;
import globaz.pyxis.db.adressecourrier.TILocaliteLieeAGroupeManager;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiementManager;
import globaz.pyxis.db.tiers.TIAdministrationAdresse;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.TIAdressePmtResolver;
import globaz.pyxis.util.TIAdresseResolver;
import globaz.pyxis.util.TITiersService;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author sch Date de création : (14.01.2003 11:32:52) date : 8 mars 06
 */
public class TITiersOSI implements IntTiers, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DOMAINE_RECOUVREMENT = "519012";
    public static final String DOMAINE_REMBOURSEMENT = "519010";
    public static final String DOMAINE_STANDARD = "519004";
    public static final String GENRE_TRIBUNAL = "509035";
    private static final String OFFICE_DES_POURSUITES = "521003";

    private static final String TRIBUNAL = "521005";
    public static final String TYPE_LIEN_OFFICE_POURSUITE = "507010";
    public static final String TYPE_LIEN_TRIBUNAL = "507011";

    private TITiersOSI op = null;
    // Entité mère
    private TITiers tiers = new TITiers();

    private TITiersOSI tribunal = null;

    /**
     * Commentaire relatif au constructeur TITiersOSI.
     */
    public TITiersOSI() {
        super();
    }

    /**
     * @see globaz.osiris.external.IntTiers#getAdresseAsString(String, String, String)
     */
    @Override
    public String getAdresseAsString(JadePublishDocumentInfo docInfo, String idType, String idApplication,
            String idExterne, String date) throws Exception {
        return tiers.getAdresseAsString(docInfo, idType, idApplication, idExterne, date, new TIAdresseFormater(), true,
                null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getAdresseAsString(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, boolean)
     */
    @Override
    public String getAdresseAsString(JadePublishDocumentInfo docInfo, String idType, String idApplication,
            String idExterne, String date, boolean herite) throws Exception {
        return tiers.getAdresseAsString(docInfo, idType, idApplication, idExterne, date, new TIAdresseFormater(),
                herite, null);
    }

    /**
     * Date de création : (28.11.2001 07:45:46)
     * 
     * @return globaz.interfaceext.tiers.IntAdresseCourrier
     * @param typeAdresse
     *            int
     */
    @Override
    public IntAdresseCourrier getAdresseCourrier(String typeAdresse) {
        try {
            // Récupérer l'adresse par défaut pour l'application
            // todo: selon type d'adresse
            String sId = this.getIdAdresseCourrier(typeAdresse);

            // Si id fournie
            if (!JadeStringUtil.isBlank(sId)) {
                // Lecture du tiers
                TIAdresseCourrierOSI adr = new TIAdresseCourrierOSI();
                adr.setISession(getISession());

                adr.retrieve(sId);

                return adr;
                // Id non fournie
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("TITiersOSI.getAdresseCourrier(): exception raised : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date de création : (28.11.2001 07:45:46)
     * 
     * @return globaz.interfaceext.tiers.IntAdresseCourrier
     * @param typeAdresse
     *            int
     */
    @Override
    public IntAdresseCourrier getAdresseCourrier(String typeAdresse, String domaine) {
        try {
            // Récupérer l'adresse par défaut pour l'application
            // todo: selon type d'adresse
            String sId = this.getIdAdresseCourrier(typeAdresse, domaine);

            // Si id fournie
            if (!JadeStringUtil.isBlank(sId)) {
                // Lecture du tiers
                TIAdresseCourrierOSI adr = new TIAdresseCourrierOSI();
                adr.setISession(getISession());

                adr.retrieve(sId);

                return adr;
                // Id non fournie
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("TITiersOSI.getAdresseCourrier(): exception raised : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getAdresseLienAsString(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public String getAdresseLienAsString(String typeLien, String idType, String idApplication, String date,
            String langue) throws Exception {
        return tiers.getAdresseLienAsString(typeLien, idType, idApplication, date, langue);
    }

    /**
     * @see globaz.osiris.external.Intoers#getComplementNom()
     */
    @Override
    public String[] getComplementNom() {
        String[] _adr = new String[2];
        _adr[0] = tiers.getDesignation3();
        _adr[1] = tiers.getDesignation4();
        return _adr;
    }

    @Override
    public TIAdresseDataSource getDataSourceAdresseCourrier() {
        try {
            if ((tiers != null) && !JadeStringUtil.isEmpty(getIdTiers())) {
                return getTIAdresseDataSource(getIdTiers());

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    public TIAdresseDataSource getDataSourceAdresseCourrierForIdTiers(String idTiers) {
        return getTIAdresseDataSource(idTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getDesignation1()
     */
    @Override
    public String getDesignation1() {
        return tiers.getDesignation1();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getDesignation2()
     */
    @Override
    public String getDesignation2() {
        return tiers.getDesignation2();
    }

    /**
     * Renvoie l'id unique de l'entité
     * 
     * @return l'id unique de l'entité
     */
    @Override
    public String getId() {
        return tiers.getId();
    }

    @Override
    public String getIdAdresseCourrier(String typeAdresse) {
        String domaine = IntTiers.PYXIS_DOMAINE_DEFAUT;

        if (IntAdresseCourrier.POURSUITE.equals(typeAdresse)) {
            domaine = IntTiers.PYXIS_DOMAINE_CONTENTIEUX;
        }
        try {
            // // Récupérer l'adresse par défaut pour l'application
            // // TODO: selon type d'adresse
            // TIAvoirAdresse adr = new TIAvoirAdresse();
            // adr.setISession(getISession());
            // // TODO sch Si on a pas d'adresse de contentieux et qu'on est sur une poursuite prendre l'adresse de
            // domicile et non l'adresse de courrier
            // return adr.getIdAdresseCourrier(tiers.getSession(), getIdTiers(), domaine);
            //
            String today = JACalendar.today().toStr(".");
            TIAvoirAdresse avoirAdresse = TITiers.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaine,
                    today, getIdTiers(), tiers.getSession());

            if (avoirAdresse == null) {
                return "";
            }
            return avoirAdresse.getIdAdresse();
        } catch (Exception e) {
            System.out.println("TITiersOSI.getIdAdresseCourrier(): exception raised : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date de création : (28.11.2001 07:47:27)
     * 
     * @return java.lang.String
     * @param typeAdresse
     *            int <br>
     *            Peut avoir comme valeur : <br>
     *            <ul>
     *            <li>IntAdresseCourrier.PRINCIPALE</li>
     *            <li>IntAdresseCourrier.POURSUITE</li>
     *            <li>IntAdresseCourrier.CORRESPONDANCE</li>
     *            <li>IntAdresseCourrier.AUTRE</li>
     *            </ul>
     * @param domaine
     */
    @Override
    public String getIdAdresseCourrier(String typeAdresse, String domaine) {
        if (JadeStringUtil.isBlankOrZero(domaine)) {
            return this.getIdAdresseCourrier(typeAdresse);
        } else {
            try {
                String today = JACalendar.today().toStr(".");
                TIAdresseDataSource datasource = tiers.getAdresseAsDataSource(typeAdresse, domaine, today, true);
                if (datasource == null) {
                    return "";
                }
                return datasource.id_adresse;
            } catch (Exception e) {
                System.out.println("TITiersOSI.getIdAdresseCourrier(): exception raised : " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Date de création : (28.11.2001 07:55:38)
     * 
     * @return java.lang.String
     * @param domaine
     *            int
     */
    @Override
    public String getIdAdressePaiement(String domaine) {
        return this.getIdAdressePaiement(domaine, null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getIdAdressePaiement(java.lang.String, java.lang.String)
     */
    @Override
    public String getIdAdressePaiement(String domaine, String idExterneRole, String date) {
        TIAvoirPaiement adr;
        try {
            if (domaine.equals(TITiersOSI.DOMAINE_RECOUVREMENT)) {
                adr = tiers.findAvoirPaiement(TITiersOSI.DOMAINE_RECOUVREMENT, idExterneRole, date);
            } else if (domaine.equals(TITiersOSI.DOMAINE_REMBOURSEMENT)) {
                adr = tiers.findAvoirPaiement(TITiersOSI.DOMAINE_REMBOURSEMENT, idExterneRole, date);
            } else {
                String today = JACalendar.today().toStr(".");
                TIAdressePaiementDataSource datasource = TIAdressePmtResolver.dataSourceAdrPmt(
                        (BSession) getISession(), tiers.getIdTiers(), domaine, idExterneRole, today, true, null);
                if (datasource == null) {
                    return "";
                }
                return datasource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE);
            }
        } catch (Exception e) {
            return null;
        }
        if (adr == null) {
            return "";
        } else {
            return adr.getIdAdrPmtIntUnique();
        }
    }

    /**
     * Date de création : (27.11.2001 15:49:20)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTiers() {
        return tiers.getIdTiers();
    }

    /**
     * Renvoie la session en cours
     * 
     * @return la session en cours
     */
    @Override
    public BISession getISession() {
        return tiers.getISession();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.11.2001 15:54:21)
     * 
     * @return java.lang.String
     */
    @Override
    public String getLangueISO() {
        if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
            return "FR";
        } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
            return "DE";
        } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ITALIEN)) {
            return "IT";
        } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ANGLAIS)) {
            return "EN";
        } else {
            return "FR";
        }
    }

    /**
     * Renvoie la date de dernière modification de l'objet (format DD.MM.YYYY).
     * 
     * @return la date de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedDate() {
        if (tiers.getSpy() == null) {
            return null;
        }
        return tiers.getSpy().getDate();
    }

    /**
     * Renvoie l'heure de dernière modification de l'objet (format HH:MM:SS).
     * 
     * @return l'heure de dernière modification de l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedTime() {
        if (tiers.getSpy() == null) {
            return null;
        }
        return tiers.getSpy().getTime();
    }

    /**
     * Renvoie l'id du dernier utilisateur qui a modifié l'objet.
     * 
     * @return l'id du dernier utilisateur qui a modifié l'objet, null si pas disponible
     */
    @Override
    public final String getLastModifiedUser() {
        if (tiers.getSpy() == null) {
            return null;
        }
        return tiers.getSpy().getUser();
    }

    /**
     * Date de création : (27.11.2001 15:49:56)
     * 
     * @return java.lang.String
     */
    @Override
    public String getLieu() {
        return tiers.getLocaliteLong();
    }

    /**
     * Date de création : (28.11.2001 07:50:23)
     * 
     * @return globaz.interfaceext.tiers.IntAdresseCourrier[]
     */
    @Override
    public IntAdresseCourrier[] getListeAdressesCourrier() {
        TIAvoirAdresseManager mgr = new TIAvoirAdresseManager();
        mgr.setSession(tiers.getSession());
        mgr.setForIdTiers(tiers.getIdTiers());
        try {
            mgr.find();
            // Manager vide
            if (mgr.size() == 0) {
                return null;
            } else {
                // Construire un tableau d'adresses;
                IntAdresseCourrier adr[] = new TIAdresseCourrierOSI[mgr.size()];
                // Parcourir le manager et stocker les adresse dans le tableau
                for (int i = 0; i < mgr.size(); i++) {
                    TIAvoirAdresse avad = (TIAvoirAdresse) mgr.getEntity(i);
                    TIAdresseCourrierOSI adresse = new TIAdresseCourrierOSI();
                    adresse.setISession(mgr.getSession());
                    adresse.retrieve(avad.getIdAdresse());
                    adr[i] = adresse;
                }
                return adr;
            }
        } catch (Exception e) {
            System.out.println("Exception occured in TITiersOSI.getListeAdresseCourrier()" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date de création : (28.11.2001 08:00:53)
     * 
     * @return globaz.interfaceext.tiers.IntAdressePaiement[]
     */
    @Override
    public IntAdressePaiement[] getListeAdressesPaiement() {
        TIAvoirPaiementManager mgr = new TIAvoirPaiementManager();
        mgr.setSession(tiers.getSession());
        mgr.setForIdTiers(tiers.getIdTiers());
        try {
            mgr.find();
            // Manager vide
            if (mgr.size() == 0) {
                return null;
            } else {
                // Construire un tableau d'adresses;
                IntAdressePaiement adr[] = new TIAdressePaiementOSI[mgr.size()];
                // Parcourir le manager et stocker les adresse dans le tableau
                for (int i = 0; i < mgr.size(); i++) {
                    TIAvoirPaiement avad = (TIAvoirPaiement) mgr.getEntity(i);
                    TIAdressePaiementOSI adresse = new TIAdressePaiementOSI();
                    adresse.setISession(mgr.getSession());
                    adresse.retrieve(avad.getIdAdrPmtIntUnique());
                    adr[i] = adresse;
                }
                return adr;
            }
        } catch (Exception e) {
            System.out.println("Exception occured in TITiersOSI.getListeAdressePaiement(): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date de création : (28.11.2001 07:52:17)
     * 
     * @return globaz.interfaceext.tiers.IntRole[]
     */
    @Override
    public IntRole[] getListeRoles() {
        IntRole[] role = new IntRole[2];
        role[0] = new TIRoleOSI();
        role[1] = new TIRoleOSI();
        try {
            role[0].setISession(getISession());
            role[0].retrieve("517001", getIdTiers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            role[1].setISession(getISession());
            role[1].retrieve("517002", getIdTiers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    /**
     * Ne fait rien. A supprimer ! Date de création : (27.11.2001 15:51:08)
     * 
     * @return java.lang.String ""
     * @param typeMoyen
     *            int
     * @deprecated
     */
    @Override
    @Deprecated
    public String getMoyenCommunication(String typeMoyen) {
        // TODO à supprimer
        /*
         * TIMoyenCommunication moyen = new TIMoyenCommunication(); moyen.setSession(tiers.getSession());
         * moyen.setIdTiers(getIdTiers()); if (typeMoyen.equals(OSIRIS_TELEPHONE)) {
         * moyen.setTypeCommunication(TIMoyenCommunication.CS_PRIVE); } else if (typeMoyen.equals(OSIRIS_FAX)) {
         * moyen.setTypeCommunication(TIMoyenCommunication.CS_FAX); } else if (typeMoyen.equals(OSIRIS_EMAIL)) {
         * moyen.setTypeCommunication(TIMoyenCommunication.CS_EMAIL); } else if (typeMoyen.equals(OSIRIS_PORTABLE)) {
         * moyen.setTypeCommunication(TIMoyenCommunication.CS_PORTABLE); } else {
         * moyen.setTypeCommunication(TIMoyenCommunication.CS_PRIVE); } try { moyen.retrieve(); if (moyen.isNew())
         * return ""; else return moyen.getContact(); } catch (Exception e) { e.printStackTrace(); return ""; }
         */
        return "";
    }

    /**
     * Date de création : (27.11.2001 15:49:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNom() {
        return tiers.getNom();
    }

    /**
     * @return String
     * @param idTiers
     *            String
     */
    @Override
    public String getNomPrenom() {
        TITiers t = new TITiers();
        t.setISession(getISession());
        t.setIdTiers(getIdTiers());
        try {
            t.retrieve();
            return t.getNomPrenom();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getNumAvsActuel() {
        TITiersViewBean ti = new TITiersViewBean();
        ti.setIdTiers(getIdTiers());
        ti.setSession((BSession) getISession());
        try {
            ti.retrieve();
            if (!ti.isNew()) {
                return ti.getNumAvsActuel();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getOfficePoursuites()
     */
    @Override
    public IntTiers getOfficePoursuites() {
        return getOfficePoursuitesSelonLienTiers();
    }

    /**
     * @see globaz.osiris.external.IntTiers#getOfficePoursuitesSelonLien(java.lang.String)
     */
    @Override
    public IntTiers getOfficePoursuitesSelonLien(String idLocalite) {
        if (op != null) {
            return op;
        }

        // rechercher d'abord d'après un lien direct avec le tiers
        op = getOfficePoursuitesSelonLienTiers();

        if (op == null) {
            // sinon, utiliser le lien par défaut avec l'adresse
            op = getOfficePoursuitesSelonLienGrpLocalite(idLocalite);
        }

        return op;
    }

    /**
     * @param idLocalite
     * @return
     */
    private TITiersOSI getOfficePoursuitesSelonLienGrpLocalite(String idLocalite) {
        try {
            // recherche le groupe qui contient la localité
            TILocaliteLieeAGroupeManager localiteeLieeMgr = new TILocaliteLieeAGroupeManager();

            localiteeLieeMgr.setISession(getISession());
            localiteeLieeMgr.setForIdLinkedEntity(idLocalite);
            localiteeLieeMgr.find(BManager.SIZE_NOLIMIT);

            // Charge toutes les agences avant la boucle pour ne faire qu'une seul requête.
            TIAvoirGroupeLocaliteManager avGrMgr = new TIAvoirGroupeLocaliteManager();

            avGrMgr.setISession(getISession());
            avGrMgr.setForCsLinkType(TITiersOSI.OFFICE_DES_POURSUITES); // OFFICE DES POURSUITES
            avGrMgr.find(BManager.SIZE_NOLIMIT);

            for (Iterator it = localiteeLieeMgr.iterator(); it.hasNext();) {
                TILocaliteLieeAGroupe locGr = (TILocaliteLieeAGroupe) it.next();

                // Est-ce que ce groupe a un avoirGroupe avec un type OFFICE DES POURSUITES
                for (Iterator itGr = avGrMgr.iterator(); itGr.hasNext();) {
                    TIAvoirGroupeLocalite avGr = (TIAvoirGroupeLocalite) itGr.next();

                    if (locGr.getIdGroupeLocation().equals(avGr.getIdGroupeLocation())) {
                        op = new TITiersOSI();

                        op.setISession(getISession());
                        op.retrieve(avGr.getIdTiers());

                        if (op.isNew()) {
                            op = null;
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            op = null;
        }

        // Aucun OP trouvé
        return op;
    }

    /**
     * @return
     */
    private TITiersOSI getOfficePoursuitesSelonLienTiers() {
        TICompositionTiersManager compositionTiers = new TICompositionTiersManager();

        compositionTiers.setISession(getISession());
        compositionTiers.setForTypeLien(TITiersOSI.TYPE_LIEN_OFFICE_POURSUITE);
        compositionTiers.setForIdTiersParent(getIdTiers());

        try {
            compositionTiers.find();

            if (!compositionTiers.isEmpty()) {
                TICompositionTiers tiers = (TICompositionTiers) compositionTiers.getFirstEntity();

                op = new TITiersOSI();
                op.setISession(getISession());
                op.retrieve(tiers.getIdTiersEnfant());

                if (op.isNew()) {
                    op = null;
                }
            }
        } catch (Exception e) {
            op = null;
        }

        return op;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getPolitesse()
     */
    @Override
    public String getPolitesse() {
        String language = tiers.getLangue();
        try {
            return tiers.getFormulePolitesse(language);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Cette méthode retourne le prénom et le nom du tiers
     * 
     * @return String Prénom et nom du tiers
     */
    @Override
    public String getPrenomNom() {
        return tiers.getPrenomNom();
    }

    /**
     * @param tiers
     * @param section
     * @return un objet de type TIAdministrationAdresse à partir du tiers.
     */
    public TIAdministrationAdresse getTIAdministrationAdresse(IntTiers tiers, CASection section) {
        // récupérer sesssion
        BSession session = section.getSession();
        // instancier administration adresse
        TIAdministrationAdresse administration = new TIAdministrationAdresse();
        try {
            administration.setIdTiers(tiers.getOfficePoursuites().getId());
            administration.setSession(session);
            administration.retrieve();

            return administration;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public TIAdresseDataSource getTIAdresseDataSource(String idTiers) {
        try {
            return TIAdresseResolver.dataSourceAdr((BSession) getISession(), idTiers,
                    IConstantes.CS_APPLICATION_DEFAUT, IConstantes.CS_AVOIR_ADRESSE_COURRIER, "",
                    JACalendar.todayJJsMMsAAAA(), true, null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @see globaz.osiris.external.IntTiers#getTitre()
     */
    @Override
    public String getTitre() {
        if (JadeStringUtil.isIntegerEmpty(tiers.getTitreTiers())) {
            return "";
        } else {
            FWParametersUserCode code = new FWParametersUserCode();
            code.setSession(tiers.getSession());
            code.setIdCodeSysteme(tiers.getTitreTiers());
            if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_FRANCAIS)) {
                code.setIdLangue("F");
            } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ALLEMAND)) {
                code.setIdLangue("D");
            } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ANGLAIS)) {
                code.setIdLangue("E");
            } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ITALIEN)) {
                code.setIdLangue("I");
            } else if (tiers.getLangue().equals(IConstantes.CS_TIERS_LANGUE_ROMANCHE)) {
                code.setIdLangue("R");
            } else {
                code.setIdLangue("F");
            }

            try {
                code.retrieve();
                if (code.isNew()) {
                    return "";
                } else {
                    return code.getLibelle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * Retourne le titre du tiers en fonction de la langue. Langue formatté sous forme FR, DE ou IT
     */
    @Override
    public String getTitre(String language) {
        if (JadeStringUtil.isIntegerEmpty(tiers.getTitreTiers())) {
            return "";
        } else {
            FWParametersUserCode code = new FWParametersUserCode();
            code.setSession(tiers.getSession());
            code.setIdCodeSysteme(tiers.getTitreTiers());
            code.setIdLangue(language.substring(0, 1));

            try {
                code.retrieve();
                if (code.isNew()) {
                    return "";
                } else {
                    return code.getLibelle();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getTitreTiers()
     */
    @Override
    public String getTitreTiers() {
        return tiers.getTitreTiers();
    }

    /**
     * Date de création : (24.01.2002 16:23:53)
     * 
     * @return java.lang.String
     */
    @Override
    public String getTitulaireNomLieu() {
        // return this.tiers.getNom() + " " + this.tiers.getLocaliteLong();
        String _nomLieu = "";
        try {
            TIAdresseDataSource data = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT,
                    JACalendar.format(JACalendar.today().toString(), JACalendar.FORMAT_DDsMMsYYYY), false);
            Hashtable table = data.getData();
            // seb _nomLieu = (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1) + "\n";
            _nomLieu = (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1) + " ";
            _nomLieu = _nomLieu + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2) + "\n";
            _nomLieu = _nomLieu + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                    + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO) + "\n";
            _nomLieu = _nomLieu + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                    + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        } catch (Exception e) {
            // e.printStackTrace();
            return "";
        }
        return _nomLieu;
    }

    /**
     * Meme que getTitulaireNomLieu sauf qu'il retourne le nom de l'affilié et uniquement la localité
     * 
     * @return
     */
    @Override
    public String getTitulaireNomLocalite() {
        // return this.tiers.getNom() + " " + this.tiers.getLocaliteLong();
        String _nomLieu = "";
        try {
            TIAdresseDataSource data = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT,
                    JACalendar.format(JACalendar.today().toString(), JACalendar.FORMAT_DDsMMsYYYY), false);
            Hashtable table = data.getData();
            // seb _nomLieu = (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1) + "\n";
            _nomLieu = (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D1) + " ";
            _nomLieu = _nomLieu + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_D2) + "\n";
            _nomLieu = _nomLieu + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                    + (String) table.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
        } catch (Exception e) {
            // e.printStackTrace();
            return "";
        }
        return _nomLieu;
    }

    /**
     * Renvoie le tribunal des assurances sociales du canton. <br>
     * Attention, l'administration n'est pas lié directement au tiers... <br>
     * On procède comme suit : <br>
     * On commence par rechercher le canton du tiers a l'aide de l'idTiers. <br>
     * On va ensuite chercher si on trouve un tribunal pour le canton <br>
     * Si on trouve 2 administration, on regarnde encore laquelle prendre en fonction de la langue du tiers <br>
     * et de la langue de l'administration. <br>
     * 
     * @see globaz.osiris.external.IntTiers#getTribunalCanton(java.lang.String)
     * @return le tribunal des assurances sociales du canton
     */
    @Override
    public IntTiers getTribunalCanton() {
        TITiersOSI tribunal = null;
        try {
            TITiersService service = new TITiersService();
            service.setSession(tiers.getSession());
            TIAdministrationAdresse adm = service.findAdministrationAdresseFor(tiers.getIdTiers(), null, null,
                    TITiersOSI.GENRE_TRIBUNAL, "");
            if (adm == null) {
                System.out.println("Aucun correspondance trouvée");
            } else {
                tribunal = new TITiersOSI();
                tribunal.setISession(getISession());
                tribunal.retrieve(adm.getIdTiersAdministration());

                if (tribunal.isNew()) {
                    tribunal = null;
                }
            }
        } catch (Exception e) {
            tribunal = null;
        }

        return tribunal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getTribunalSelonLien(java.lang.String)
     */
    @Override
    public IntTiers getTribunalSelonLien(String idLocalite) {
        if (tribunal != null) {
            return tribunal;
        }

        // rechercher d'abord d'après un lien direct avec le tiers
        tribunal = getTribunalSelonLienTiers();

        if (tribunal == null) {
            // sinon, utiliser le lien par défaut avec l'adresse
            tribunal = getTribunalSelonLienGrpLocalite(idLocalite);
        }

        return tribunal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntTiers#getTribunalSelonLien(java.lang.String, java.lang.String)
     */
    @Override
    public IntTiers getTribunalSelonLien(String idType, String idApplication) {
        if (tribunal != null) {
            return tribunal;
        }

        // rechercher d'abord d'après un lien direct avec le tiers
        tribunal = getTribunalSelonLienTiers();

        if (tribunal == null) {
            // rechercher la localité du tiers
            try {
                TIAvoirAdresse avoirAdresse = TITiers.findAvoirAdresse(idType, idApplication, null, JACalendar.today()
                        .toStr("."), tiers.getIdTiers(), (BSession) getISession());

                if (avoirAdresse != null) {
                    // Sinon, utiliser le lien par défaut avec l'adresse
                    tribunal = getTribunalSelonLienGrpLocalite(avoirAdresse.getIdLocalite());
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return tribunal;
    }

    /**
     * @param idLocalite
     * @return
     */
    private TITiersOSI getTribunalSelonLienGrpLocalite(String idLocalite) {
        try {
            // recherche le groupe qui contient la localité
            TILocaliteLieeAGroupeManager localiteeLieeMgr = new TILocaliteLieeAGroupeManager();
            localiteeLieeMgr.setISession(getISession());
            localiteeLieeMgr.setForIdLinkedEntity(idLocalite);
            localiteeLieeMgr.find(BManager.SIZE_NOLIMIT);

            // Charge toutes les agences avant la boucle pour ne faire qu'une seul requête.
            TIAvoirGroupeLocaliteManager avGrMgr = new TIAvoirGroupeLocaliteManager();
            avGrMgr.setISession(getISession());
            avGrMgr.setForCsLinkType(TITiersOSI.TRIBUNAL); // TRIBUNAL
            avGrMgr.find(BManager.SIZE_NOLIMIT);

            for (Iterator it = localiteeLieeMgr.iterator(); it.hasNext();) {
                TILocaliteLieeAGroupe locGr = (TILocaliteLieeAGroupe) it.next();

                // Est-ce que ce groupe a un avoirGroupe avec un type TRIBUNAL
                for (Iterator itGr = avGrMgr.iterator(); itGr.hasNext();) {
                    TIAvoirGroupeLocalite avGr = (TIAvoirGroupeLocalite) itGr.next();

                    if (locGr.getIdGroupeLocation().equals(avGr.getIdGroupeLocation())) {
                        tribunal = new TITiersOSI();

                        tribunal.setISession(getISession());
                        tribunal.retrieve(avGr.getIdTiers());

                        if (tribunal.isNew()) {
                            tribunal = null;
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tribunal = null;
        }

        // Aucun OP trouvé
        return tribunal;
    }

    /**
     * @return
     */
    private TITiersOSI getTribunalSelonLienTiers() {
        TICompositionTiersManager compositionTiers = new TICompositionTiersManager();

        compositionTiers.setISession(getISession());
        compositionTiers.setForTypeLien(TITiersOSI.TYPE_LIEN_TRIBUNAL);
        compositionTiers.setForIdTiersParent(getIdTiers());

        try {
            compositionTiers.find();

            if (!compositionTiers.isEmpty()) {
                TICompositionTiers tiers = (TICompositionTiers) compositionTiers.getFirstEntity();

                tribunal = new TITiersOSI();
                tribunal.setISession(getISession());
                tribunal.retrieve(tiers.getIdTiersEnfant());

                if (tribunal.isNew()) {
                    tribunal = null;
                }
            }
        } catch (Exception e) {
            tribunal = null;
        }

        return tribunal;
    }

    /**
     * Date de création : (27.11.2001 15:50:19)
     * 
     * @return int
     */
    @Override
    public String getTypeTiers() {
        if (tiers.getPersonnePhysique().booleanValue()) {
            return IntTiers.OSIRIS_PERSONNE_PHYSIQUE;
        } else {
            return IntTiers.OSIRIS_PERSONNE_MORALE;
        }
    }

    /**
     * Indique si l'entité est nouvelle (i.e. n'existe pas dans la BD)
     * 
     * @return true si l'entité n'existe pas dans la BD; false sinon
     */
    @Override
    public boolean isNew() {
        return tiers.isNew();
    }

    /**
     * Date de création : (06.11.2002 16:37:03)
     * 
     * @return boolean
     */
    @Override
    public boolean isOnError() {
        return tiers.hasErrors();
    }

    /**
     * Date de création : (27.11.2001 15:08:22)
     */
    @Override
    public void retrieve(globaz.globall.api.BITransaction transaction, String idTiers) throws Exception {
        tiers.setIdTiers(idTiers);
        tiers.retrieve(transaction);
    }

    /**
     * Date de création : (27.11.2001 15:08:22)
     */
    @Override
    public void retrieve(String idTiers) throws Exception {
        this.retrieve(null, idTiers);
    }

    /**
     * Définit l'id unique de l'entité
     * 
     * @param newId
     *            le nouvel id unique de l'entité
     */
    public void setId(java.lang.String newId) {
        tiers.setId(newId);
    }

    /**
     * Modifie la session en cours
     * 
     * @param newISession
     *            la nouvelle session
     */
    @Override
    public void setISession(globaz.globall.api.BISession newSession) {
        tiers.setISession(newSession);
    }
}

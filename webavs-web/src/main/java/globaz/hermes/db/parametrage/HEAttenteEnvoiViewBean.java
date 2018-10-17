package globaz.hermes.db.parametrage;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.access.HEInfosManager;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.HECSMotif;
import globaz.hermes.utils.HENNSSUtils;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.util.CIAffilie;
import globaz.pavo.util.CIAffilieManager;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class HEAttenteEnvoiViewBean extends BEntity implements FWViewBeanInterface {
    private static final long serialVersionUID = -4355046795181465477L;
    private String adresseAssure = "";
    private String adresseRentier = "";
    private String caisse = "";
    private String categorie = "";

    protected HEAttenteEnvoiChampsListViewBean champs = new HEAttenteEnvoiChampsListViewBean();
    private String creator = "";
    protected String critere = "";
    private String dateCreation = "";
    private String dateEngagement = "";

    protected String debut = "";
    private String enregistrement = "";
    private final String HEANNOP_ARCHIVE = "HEANNOR";
    private final String HEANNOP_EN_COURS = "HEANNOP";
    public static final String CODE_ARC_11 = "11";
    public static final String CODE_ARC_31 = "31";
    public static final String CODE_ARC_61 = "61";

    protected String idAnnonce = ""; // IDANNONCE
    protected String idChamp = ""; // RDTCHA
    //
    protected String idLot = "";
    protected boolean isArchivage = false;
    private boolean isConfirmed = false;

    private String langueCorrespondance = "";
    protected String libelleChamp = ""; // PCOLUT
    protected String longueur = "";
    protected String messageFormatted = "";
    protected String motif = "";
    private String motifArc = "";
    /** modif NNSS */
    private Boolean nnss = new Boolean(false);
    protected String nom = "";
    private String nouveauNumAVS = "";
    private String numavs = "";
    /***/
    private String numeroAffilie = "";
    private String numeroAvsNNSS = "";
    private String numeroEmploye = "";
    private String numeroSuccursale = "";
    protected String paramAnnonce = ""; // REIPAE
    private String refUnique = "";
    private String statut = "";

    protected String titreAssure = "";
    private String titreRentier = "";
    private String formulePolitesse = "";
    protected HashMap tmpRequest;
    //
    protected String typeEnvoi = "";
    protected String valeur = ""; // VALEUR

    private HEInputAnnonceViewBean annonce61ACreer;

    /**
     * Champ permettant de savoir si un arc 61 a été créé lors de la création manuelle d'un arc 11 ou 31.
     * Si la case était cochée, alors un arc 61 est créé
     */
    private Boolean isArc61Cree = new Boolean(false);
    private Boolean isArc61CreeTmp = new Boolean(false);

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // virer tout pour cette référence dans la table des annonces
        HEOutputAnnonceListViewBean annonces = new HEOutputAnnonceListViewBean();
        annonces.setSession(getSession());
        annonces.wantCallMethodAfter(false);
        annonces.wantCallMethodAfterFind(false);
        annonces.wantCallMethodBefore(false);
        annonces.wantCallMethodBeforeFind(false);
        annonces.setForRefUnique(getRefUnique());
        annonces.find(transaction);
        for (int i = 0; i < annonces.size(); i++) {
            HEOutputAnnonceViewBean entity = (HEOutputAnnonceViewBean) annonces.getEntity(i);
            entity.wantCallMethodAfter(false);
            entity.wantCallMethodBefore(false);
            entity.wantCallValidate(false);
            entity.delete(transaction);
        }
        // idem pour les retours
        HEAttenteRetourListViewBean retours = new HEAttenteRetourListViewBean();
        retours.setSession(getSession());
        retours.wantCallMethodAfter(false);
        retours.wantCallMethodAfterFind(false);
        retours.wantCallMethodBefore(false);
        retours.wantCallMethodBeforeFind(false);
        retours.setForReferenceUnique(getRefUnique());
        retours.find(transaction);
        for (int i = 0; i < retours.size(); i++) {
            HEAttenteRetourViewBean entity = (HEAttenteRetourViewBean) retours.getEntity(i);
            entity.wantCallMethodAfter(false);
            entity.wantCallMethodBefore(false);
            entity.wantCallValidate(false);
            entity.delete(transaction);
        }
        // de plus, je supprime les compléments informations
        if (("97".equals(getMotifArc()) && "true".equals(getSession().getApplication().getProperty("adresse.input")))
                || (HEAnnoncesViewBean.isMotifCA(getMotifArc())
                        && "true".equals(getSession().getApplication().getProperty("affilie.input")))) {
            HEInfos infos;
            // Recherche l'addresse de l'assuré
            HEInfosManager infosManager = new HEInfosManager();
            infosManager.setSession(getSession());
            // on supprime toutes les infos en correspondances avec l'arc
            infosManager.setForIdArc(getIdAnnonce());
            infosManager.find(transaction);
            for (int i = 0; i < infosManager.size(); i++) {
                infos = ((HEInfos) infosManager.getEntity(i));
                infos.wantCallMethodAfter(false);
                infos.wantCallMethodBefore(false);
                infos.wantCallValidate(false);
                infos.delete(transaction);
            }
        }
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // Recherche de l'addresse de l'assuré
        if ("97".equals(getMotifArc()) && !isLoadedFromManager()) {
            HEInfosManager infosManager = new HEInfosManager();
            infosManager.setSession(getSession());
            infosManager.setForIdArc(getIdAnnonce());
            infosManager.find(transaction);
            if (infosManager.size() > 0) {
                for (int i = 0; i < infosManager.size(); i++) {
                    HEInfos info = new HEInfos();
                    info = (HEInfos) infosManager.getEntity(i);
                    if (HEInfos.CS_ADRESSE_ASSURE.equals(info.getTypeInfo())) {
                        adresseAssure = info.getLibInfo();
                    }
                    if (HEInfos.CS_LANGUE_CORRESPONDANCE.equals(info.getTypeInfo())) {
                        langueCorrespondance = info.getLibInfo();
                    }
                    if (HEInfos.CS_TITRE_ASSURE.equals(info.getTypeInfo())) {
                        titreAssure = info.getLibInfo();
                    }
                    if (HEInfos.CS_LANGUE_CORRESPONDANCE.equals(info.getTypeInfo())) {
                        langueCorrespondance = info.getLibInfo();
                    }
                    if (HEInfos.CS_FORMULE_POLITESSE.equals(info.getTypeInfo())) {
                        formulePolitesse = info.getLibInfo();
                    }
                }
            }

        }
        if ((HEAnnoncesViewBean.isMotifCA(getMotifArc()) || HEAnnoncesViewBean.isMotifForDeclSalaire(getMotifArc())
                || globaz.hermes.utils.HEUtil.isMotifCert(getSession(), getMotifArc())) && !isLoadedFromManager()) {
            HEInfosManager infosManager = new HEInfosManager();
            infosManager.setSession(getSession());
            infosManager.setForIdArc(getIdAnnonce());
            infosManager.find(transaction);
            if (infosManager.size() > 0) {
                for (int i = 0; i < infosManager.size(); i++) {
                    HEInfos info = new HEInfos();
                    info = (HEInfos) infosManager.getEntity(i);
                    if (HEInfos.CS_NUMERO_AFFILIE.equals(info.getTypeInfo())) {
                        numeroAffilie = info.getLibInfo();
                    }
                    if (HEInfos.CS_NUMERO_SUCCURSALE.equals(info.getTypeInfo())) {
                        numeroSuccursale = info.getLibInfo();
                    }
                    if (HEInfos.CS_NUMERO_EMPLOYE.equals(info.getTypeInfo())) {
                        numeroEmploye = info.getLibInfo();
                    }
                    if (HEInfos.CS_DATE_ENGAGEMENT.equals(info.getTypeInfo())) {
                        dateEngagement = info.getLibInfo();
                    }
                    if (HEInfos.CS_CATEGORIE.equals(info.getTypeInfo())) {
                        categorie = info.getLibInfo();
                    }
                    if (HEInfos.CS_ADRESSE_ASSURE.equals(info.getTypeInfo())) {
                        adresseRentier = info.getLibInfo();
                    }
                    if (HEInfos.CS_TITRE_ASSURE.equals(info.getTypeInfo())) {
                        titreRentier = info.getLibInfo();
                    }
                    if (HEInfos.CS_FORMULE_POLITESSE.equals(info.getTypeInfo())) {
                        formulePolitesse = info.getLibInfo();
                    }
                    if (HEInfos.CS_LANGUE_CORRESPONDANCE.equals(info.getTypeInfo())) {
                        langueCorrespondance = info.getLibInfo();
                    }
                }
            }
        }
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // lorsque que l'on met a jour un arc, il faut mettre aussi les attentes
        // retour a jour...
        if (!JadeStringUtil.isEmpty(getNouveauNumAVS())) {
            HEAttenteRetourListViewBean attentes = new HEAttenteRetourListViewBean();
            attentes.setSession(transaction.getSession());
            attentes.setForIdAnnonce(getIdAnnonce());
            attentes.find(transaction);
            for (int i = 0; i < attentes.size(); i++) {
                HEAttenteRetourViewBean crt = (HEAttenteRetourViewBean) attentes.getEntity(i);
                crt.wantCallMethodAfter(false);
                crt.wantCallMethodBefore(false);
                crt.setNumeroAvs(getNumavs());
                crt.setNumeroAvsNNSS(getNumeroAvsNNSS());
                crt.update(transaction);
            }
        }
        addARC61(transaction);
    }

    /**
     * Création d'un arc 61 avec les mêmes données que l'arc sur lequel on se trouve
     *
     * @param transaction
     */
    private void addARC61(BTransaction transaction) {
        try {
            if (is61Possible() && annonce61ACreer != null) {
                // Le critère 32 est celui utilisé lorsqu'on saisi un motif 61
                annonce61ACreer.computeNeededFields(HECSMotif.CS_AVEC_CI_CA_PRESENTE, "32");
                annonce61ACreer.setMotif(CODE_ARC_61);
                annonce61ACreer.setIsArc61Cree(false);
                annonce61ACreer.getInputTable().put(IHEAnnoncesViewBean.MOTIF_ANNONCE, CODE_ARC_61);
                annonce61ACreer.getInputTable().put(IHEAnnoncesViewBean.ETAT_NOMINATIF, "");
                annonce61ACreer.getInputTable().put(IHEAnnoncesViewBean.DATE_NAISSANCE_JJMMAAAA, "");
                annonce61ACreer.getInputTable().put(IHEAnnoncesViewBean.ETAT_ORIGINE, "");
                annonce61ACreer.getInputTable().put(IHEAnnoncesViewBean.SEXE, "");
                annonce61ACreer.add(transaction);
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    /**
     * Test si un arc 61 peut être créé en fonction du motif de l'arc sur lequel on se trouve, la case à cocher, la
     * valeur de celle-ci en DB et du numéro AVS
     *
     * @return true si les conditions permettent de créer un arc 61
     */
    private boolean is61Possible() {
        return (CODE_ARC_11.equals(getMotifArc()) || CODE_ARC_31.equals(getMotifArc())) && getIsArc61Cree()
                && !JadeStringUtil.isBlankOrZero(getNumavs()) && !isArc61CreeTmp;
    }

    /**
     * Effectue des traitements avant une mise à jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la mise à jour de l'entité dans la BD
     * <p>
     * L'exécution de la mise à jour n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     *
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws java.lang.Exception {
        // ICI modifier les annonces !
        HEOutputAnnonceListViewBean lVbean = new HEOutputAnnonceListViewBean();
        lVbean.setSession(getSession());
        lVbean.setForRefUnique(getRefUnique());
        lVbean.setForIdLot(idLot);
        lVbean.find(transaction);

        Hashtable newValues = new Hashtable();
        Set s = tmpRequest.keySet();
        Iterator itTmp = s.iterator();
        while (itTmp.hasNext()) {
            String param = (String) itTmp.next();

            if (param.startsWith("TOSTR")) {
                param = param.substring(5, param.length());
                newValues.put(param, tmpRequest.get("TOSTR" + param));
            } else {
                newValues.put(param, tmpRequest.get(param));
            }
        }

        for (int i = 0; i < lVbean.size(); i++) {
            HEOutputAnnonceViewBean annonceTrouvee = (HEOutputAnnonceViewBean) lVbean.getEntity(i);
            HEInputAnnonceViewBean annonceDeBase = new HEInputAnnonceViewBean();
            annonceDeBase.setSession(lVbean.getSession());
            annonceDeBase.setIdAnnonce(annonceTrouvee.getIdAnnonce());
            annonceDeBase.retrieve(transaction);
            // on charge les anciennes valeurs
            annonceDeBase.put(IHEAnnoncesViewBean.CODE_APPLICATION,
                    annonceTrouvee.getField(IHEAnnoncesViewBean.CODE_APPLICATION));
            annonceDeBase.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                    annonceTrouvee.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT));
            annonceDeBase.setRefUnique(annonceTrouvee.getRefUnique());
            // s'assurer que le numéro de l'anonce reste identique...
            if (!JadeStringUtil.isEmpty(getRefUnique())) {
                if (getIdAnnonce().length() > 6) {
                    annonceDeBase.put(IHEAnnoncesViewBean.NUMERO_ANNONCE,
                            getRefUnique().substring(getRefUnique().length() - 6, getRefUnique().length()));
                } else {
                    annonceDeBase.put(IHEAnnoncesViewBean.NUMERO_ANNONCE, getRefUnique());
                }
            }
            annonceDeBase.setMotif(annonceTrouvee.getMotif());
            Iterator it = annonceTrouvee.getInputTable().keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                String keyValue = String.valueOf(key);
                if (!JadeStringUtil.isBlank(String.valueOf(annonceTrouvee.get(keyValue)))) {
                    annonceDeBase.put(String.valueOf(key), annonceTrouvee.get(keyValue));
                }
            }
            // balaye les nouvelles valeurs
            it = newValues.keySet().iterator();
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                if (!JadeStringUtil.isBlank(key)) {
                    annonceDeBase.put(key, newValues.get(key));
                }
                if (HEAnnoncesViewBean.isNumeroAVS(key)) {
                    setNouveauNumAVS(String.valueOf(newValues.get(key)));
                    setNumeroAvsNNSS(String.valueOf(newValues.get(key + HENNSSUtils.PARAM_NNSS)));
                }
            }
            annonceDeBase.wantCallMethodAfter(false);
            if (HEAnnoncesViewBean.isMotifCA(getMotifArc())
                    || HEAnnoncesViewBean.isMotifForDeclSalaire(getMotifArc())) {
                annonceDeBase.setNumeroAffilie(getNumeroAffilie());
            }
            if (HEAnnoncesViewBean.isMotifForDeclSalaire(getMotifArc())) {
                annonceDeBase.setDateEngagement(getDateEngagement());
            }
            if ("97".equals(getMotifArc())) {
                annonceDeBase.setTitreAssure(getTitreAssure());
                annonceDeBase.setAdresseAssure(getAdresseAssure());
                annonceDeBase.setLangueCorrespondance(getLangueCorrespondance());
                annonceDeBase.setFormulePolitesse(getFormulePolitesse());
            }
            annonceDeBase.initChampEnregistrementFromAttr();
            FWParametersSystemCodeManager motifs = HEUtil.getCSMotif(getMotifArc(), getSession());
            if (motifs.size() == 1) {
                String csmotif = ((FWParametersSystemCode) motifs.getFirstEntity()).getIdCode();
                annonceDeBase.computeNeededFields(csmotif, true);
                if (annonceDeBase.getHEMotifcodeapplication(0) != null) {
                    annonceDeBase.computeNeededFields(csmotif,
                            annonceDeBase.getHEMotifcodeapplication(0).getIdCritereMotif());
                } else {
                    _addError(transaction,
                            "Erreur dans la validation de cet arc,cause: impossible de charger le critère du motif "
                                    + getMotifArc());
                }
            } else {
                _addError(transaction, "Erreur dans la validation de cet arc,cause: impossible de traduire le motif "
                        + getMotifArc() + "en code système");
            }

            if (HEUtil.isNNSSActif(getSession())
                    && globaz.hermes.utils.HEUtil.isMotifCert(getSession(), getMotifArc())) {
                annonceDeBase.setCategorie(getCategorie());
                if (getCategorie().equals(IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER)) {
                    annonceDeBase.setTitreRentier(getTitreRentier());
                    annonceDeBase.setAdresseRentier(getAdresseRentier());
                    annonceDeBase.setFormulePolitesse(getFormulePolitesse());
                }
            }
            annonceDeBase.setIsArc61Cree(getIsArc61Cree());
            // L'arc 61 ne peut être créé que sous certaines conditions
            if (is61Possible()) {
                annonce61ACreer = annonceDeBase.cloneViewBean();
                annonce61ACreer.setLangueCorrespondance(getLangueCorrespondance());
                annonce61ACreer.wantCallMethodAfter(true);
            }
            annonceDeBase.update(transaction);

            // on met à jour le numAvs de l'attente
            setNumavs(annonceDeBase.getNumeroAVS());
        }
        if ("97".equals(getMotifArc()) && "true".equals(getSession().getApplication().getProperty("adresse.input"))) {
            // mise à jour de l'adresse de l'assuré
            HEInfos infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_ADRESSE_ASSURE);
            infos.retrieve(transaction);
            infos.setLibInfo(getAdresseAssure());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
            // mis à jour de la langue de l'asssuré
            infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_LANGUE_CORRESPONDANCE);
            infos.retrieve(transaction);
            infos.setLibInfo(getLangueCorrespondance());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
            // mis à jour du titre
            infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_TITRE_ASSURE);
            infos.retrieve(transaction);
            infos.setLibInfo(getTitreAssure());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
        }
        if ((HEAnnoncesViewBean.isMotifCA(getMotifArc()) || HEAnnoncesViewBean.isMotifForDeclSalaire(getMotifArc()))
                && "true".equals(getSession().getApplication().getProperty("affilie.input"))) {
            HEInfos infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_NUMERO_AFFILIE);
            infos.retrieve(transaction);
            if (JadeStringUtil.isEmpty(getNumeroAffilie())) {
                if (!infos.isNew()) {
                    infos.delete(transaction);
                }
            } else {
                infos.setLibInfo(getNumeroAffilie());
                if (infos.isNew()) {
                    infos.add(transaction);
                } else {
                    infos.update(transaction);
                }
            }
            // mis à jour du numéro succursale
            infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_NUMERO_SUCCURSALE);
            infos.retrieve(transaction);
            infos.setLibInfo(getNumeroSuccursale());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
            // mis à jour du numéro d'employé
            infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_NUMERO_EMPLOYE);
            infos.retrieve(transaction);
            infos.setLibInfo(getNumeroEmploye());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
        }
        if (HEAnnoncesViewBean.isMotifForDeclSalaire(getMotifArc())
                && "true".equals(getSession().getApplication().getProperty("affilie.input"))) {
            HEInfos infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_DATE_ENGAGEMENT);
            infos.retrieve(transaction);
            if (JadeStringUtil.isEmpty(getNumeroAffilie()) && !infos.isNew()) {
                if (!infos.isNew()) {
                    infos.delete(transaction);
                }
            } else {
                infos.setLibInfo(getDateEngagement());
                if (infos.isNew()) {
                    infos.add(transaction);
                } else {
                    infos.update(transaction);
                }
            }
        }

        if (HEUtil.isNNSSActif(getSession()) && globaz.hermes.utils.HEUtil.isMotifCert(getSession(), getMotifArc())) {
            HEInfos infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_CATEGORIE);
            infos.retrieve(transaction);
            infos.setLibInfo(getCategorie());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
            // mis à jour de l'adresse du rentier
            infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_ADRESSE_ASSURE);
            infos.retrieve(transaction);
            infos.setLibInfo(getAdresseRentier());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }
            // mis à jour du titre du rentier
            infos = new HEInfos();
            infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infos.setIdArc(getIdAnnonce());
            infos.setTypeInfo(HEInfos.CS_TITRE_ASSURE);
            infos.retrieve(transaction);
            infos.setLibInfo(getTitreRentier());
            if (infos.isNew()) {
                infos.add(transaction);
            } else {
                infos.update(transaction);
            }

        }

        // Gestion de la formule de politesse
        HEInfos infos = new HEInfos();
        infos.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
        infos.setIdArc(getIdAnnonce());
        infos.setTypeInfo(HEInfos.CS_FORMULE_POLITESSE);
        infos.retrieve(transaction);
        infos.setLibInfo(getFormulePolitesse());
        if (infos.isNew()) {
            if (!JadeStringUtil.isBlank(getFormulePolitesse())) {
                infos.add(transaction);
            }
        } else {
            infos.update(transaction);
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        if (isArchivage()) {
            return HEANNOP_ARCHIVE;
        } else {
            return HEANNOP_EN_COURS;
        }
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     *
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAnnonce = statement.dbReadNumeric("RNIANN");

        dateCreation = statement.dbReadDateAMJ("RNDDAN");
        enregistrement = statement.dbReadString("RNLENR");
        enregistrement = JAUtil.padString(enregistrement, 120);
        String retour = statement.dbReadNumeric("HEA_RNIANN");
        statut = statement.dbReadString("RNTSTA");
        String uti = statement.dbReadString("RNLUTI");
        refUnique = statement.dbReadString("RNREFU");
        isConfirmed = statement.dbReadBoolean("RMBQUI").booleanValue();
        idLot = statement.dbReadNumeric("RMILOT");
        // creator = statement.dbReadString("RNTPRO");
        if (uti.trim().length() != 0) {
            creator = uti;
        }
        if (!JadeStringUtil.isIntegerEmpty(retour)) {
            isConfirmed = true;
        }
        motifArc = statement.dbReadString("RNMOT");
        caisse = statement.dbReadString("RNCAIS");

        // Modification NNSS
        nnss = statement.dbReadBoolean("RNBNNS");
        if (nnss.booleanValue()) {
            numavs = statement.dbReadString("RNAVS");
            numeroAvsNNSS = "true";
        } else {
            numavs = JAUtil.formatAvs(statement.dbReadString("RNAVS"));
            numeroAvsNNSS = "false";
        }
        setIsArc61Cree(statement.dbReadBoolean("RNBARC"));
        isArc61CreeTmp = statement.dbReadBoolean("RNBARC");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if ("97".equals(getMotifArc()) && "true".equals(getSession().getApplication().getProperty("adresse.input"))) {
            _propertyMandatory(statement.getTransaction(), getAdresseAssure(),
                    getSession().getLabel("HERMES_ADRESSE_OBLIGATOIRE"));
            _propertyMandatory(statement.getTransaction(), getLangueCorrespondance(),
                    getSession().getLabel("HERMES_LANGUE_OBLIGATOIRE"));
        }
        if ("true".equals(getSession().getApplication().getProperty("affilie.input"))) {
            if (HEAnnoncesViewBean.isMotifCA(getMotifArc())) {
                // le numéro d'affilié est-il présent ?
                if (!JadeStringUtil.isEmpty(getNumeroAffilie())) {
                    try {
                        ((HEApplication) getSession().getApplication()).checkAffilie(getNumeroAffilie());
                    } catch (Exception e1) {
                        _addError(statement.getTransaction(), getSession().getLabel("HERMES_NUM_AFF_FAUX"));
                    }
                }
            }

            if (HEAnnoncesViewBean.isMotifForDeclSalaire(getMotifArc())) {
                if (!JadeStringUtil.isEmpty(getNumeroAffilie())) {
                    try {
                        ((HEApplication) getSession().getApplication()).checkDateEngagement(getDateEngagement(),
                                getNumeroAffilie(), statement.getTransaction());
                    } catch (Exception e) {
                        _addError(statement.getTransaction(), e.getMessage());
                    }
                }
            }
        }
        // Remise à jour du code langue
        if (!JadeStringUtil.isEmpty(getLangueCorrespondance())) {
            HEInfos infoslan = new HEInfos();
            infoslan = new HEInfos();
            infoslan.setIdArc(getIdAnnonce());
            infoslan.setTypeInfo(HEInfos.CS_LANGUE_CORRESPONDANCE);
            infoslan.setAlternateKey(HEInfos.ALTERNATE_KEY_IDARC_TYPEINFO);
            infoslan.retrieve(statement.getTransaction());
            infoslan.setLibInfo(getLangueCorrespondance());
            if (!infoslan.isNew()) {
                infoslan.update(statement.getTransaction());
            } else {
                infoslan.add(statement.getTransaction());
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RNIANN", this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAdresseAssure() {
        return adresseAssure;
    }

    public String getAdresseRentier() {
        return adresseRentier;
    }

    public String getAffilieNom() {
        try {
            if (JadeStringUtil.isIntegerEmpty(getNumeroAffilie())) {
                return "";
            }
            CIAffilieManager mgr = new CIAffilieManager();
            mgr.setSession(getSession());
            mgr.setLikeAffilieNumero(getNumeroAffilie());
            mgr.find(1);
            if (mgr.size() > 0) {
                CIAffilie af = (CIAffilie) mgr.getEntity(0);
                TITiersViewBean ti = new TITiersViewBean();
                ti.setSession(getSession());
                ti.setIdTiers(af.getTiersId());
                ti.retrieve();
                return ti.getNom();
            }
            return "";
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            return "";
        }
    }

    public String getArcEtatNominatif() {
        return this.getArcEtatNominatif(20);
    }

    public String getArcEtatNominatif(int limit) {
        return this.getArcEtatNominatif(47, limit);
    }

    public String getArcEtatNominatif(int begin, int limit) {
        String name = enregistrement.substring(begin, begin + 39).trim();
        if (name.length() > limit) {
            return name.substring(0, limit - 1) + "...";
        }
        return name;
    }

    public String getArcEtatNominatif(String ca, int limit) {
        int code = Integer.parseInt(ca);
        switch (code) {
            case 11:
                return this.getArcEtatNominatif(47, 20);
            case 20:
                return this.getArcEtatNominatif(47, 20);
            case 21:
                return this.getArcEtatNominatif(32, 20);
            case 23:
                return this.getArcEtatNominatif(32, 20);
            case 25:
                return this.getArcEtatNominatif(32, 20);
            case 22:
                return this.getArcEtatNominatif(32, 20);
            case 29:
                return this.getArcEtatNominatif(32, 20);
            case 24:
                return this.getArcEtatNominatif(32, 20);
            default:
                return "?";
        }
    }

    /**
     * Returns the caisse.
     *
     * @return String
     */
    public String getCaisse() {
        return caisse;
    }

    public String getCategorie() {
        return categorie;
    }

    /**
     * Returns the champs.
     *
     * @return HEAttenteEnvoiChampsListViewBean
     */
    public HEAttenteEnvoiChampsListViewBean getChamps() {
        return champs;
    }

    public String getChampsAsCodeSystemDefaut(String keyChamp) {
        HEAttenteEnvoiChampsViewBean entity;
        for (int i = 0; i < champs.size(); i++) {
            entity = (HEAttenteEnvoiChampsViewBean) champs.getEntity(i);
            if (entity.getIdChamp().equals(keyChamp)) {
                return entity.getValeur();
            }
        }
        return "";
    }

    public HEAttenteEnvoiChampsViewBean getChampsEnvoiAt(int index) {
        return (HEAttenteEnvoiChampsViewBean) champs.getEntity(index);
    }

    public int getChampsSize() {
        return champs.size();
    }

    public Vector getCountries(String keyChamp) {
        return new HEUtil().getCountries(keyChamp, getSession());
    }

    /**
     * Returns the creator.
     *
     * @return String
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @return
     */
    public String getCritere() {
        return critere;
    }

    /**
     * Returns the dateCreation.
     *
     * @return String
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * @return
     */
    public String getDateEngagement() {
        return dateEngagement;
    }

    /**
     * Returns the debut.
     *
     * @return String
     */
    public String getDebut() {
        return debut;
    }

    /**
     * Returns the enregistrement.
     *
     * @return String
     */
    public String getEnregistrement() {
        return enregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 13:08:20)
     *
     * @return java.lang.String
     */
    public java.lang.String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * Returns the idChamp.
     *
     * @return String
     */
    public String getIdChamp() {
        return idChamp;
    }

    /**
     * Returns the idLot.
     *
     * @return String
     */
    public String getIdLot() {
        return idLot;
    }

    public String getLangueCorrespondance() {
        return langueCorrespondance;
    }

    private String getLibelle(String csCode) throws Exception {
        return ((HEApplication) getSession().getApplication()).getCsCodeApplicationListe(getSession())
                .getCodeSysteme(csCode).getCurrentCodeUtilisateur().getLibelle();
    }

    /**
     * Returns the libelleChamp.
     *
     * @return String
     */
    public String getLibelleChamp() {
        return libelleChamp;
    }

    public String getLibelleMotif() {
        try {
            FWParametersSystemCodeManager csMotifsListe = new FWParametersSystemCodeManager();
            csMotifsListe.setForIdGroupe("HEMOTIFS");
            csMotifsListe.setForIdTypeCode("11100002");
            csMotifsListe.setForActif(new Boolean(true));
            csMotifsListe.setSession(getSession());
            csMotifsListe.setForCodeUtilisateur(getMotifArc());
            csMotifsListe.find(1);
            if (csMotifsListe.size() > 0) {
                return ((FWParametersSystemCode) csMotifsListe.getFirstEntity()).getCurrentCodeUtilisateur()
                        .getLibelle();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getLibelleOrigine(String csEtat) {
        try {
            FWParametersSystemCodeManager csMotifsListe = new FWParametersSystemCodeManager();
            csMotifsListe.setForIdGroupe("CIPAYORI");
            csMotifsListe.setForIdTypeCode("10300015");
            csMotifsListe.setForActif(Boolean.TRUE);
            csMotifsListe.setSession(getSession());
            csMotifsListe.setForCodeUtilisateur(csEtat);
            csMotifsListe.find(1);
            if (csMotifsListe.size() > 0) {
                return ((FWParametersSystemCode) csMotifsListe.getFirstEntity()).getCurrentCodeUtilisateur()
                        .getLibelle();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public Vector getListeCat() {
        return new HEUtil().getListeCat(getSession());
    }

    public Vector getListeTitre() {
        return new HEUtil().getListeTitre(getSession());
    }

    /**
     * Returns the longueur.
     *
     * @return String
     */
    public String getLongueur() {
        return longueur;
    }

    /**
     * Returns the messageFormatted.
     *
     * @return String
     */
    public String getMessageFormatted() {
        return messageFormatted;
    }

    /**
     * @return
     */
    public String getMotif() {
        return motif;
    }

    public String getMotifArc() {
        return motifArc;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.04.2003 17:11:33)
     *
     * @return java.lang.String
     */
    public java.lang.String getNom() {
        return nom;
    }

    /**
     * Returns the nouveauNumAVS.
     *
     * @return String
     */
    public String getNouveauNumAVS() {
        if (JadeStringUtil.isEmpty(nouveauNumAVS)) {
            return getNumavs();
        } else {
            return nouveauNumAVS;
        }
    }

    /**
     * Returns the numavs.
     *
     * @return String
     */
    public String getNumavs() {
        return numavs;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getNumeroAffilieFormatte() {
        try {
            return ((HEApplication) getSession().getApplication()).formatAffilie(getNumeroAffilie());
        } catch (Exception e) {
            return getNumeroAffilie();
        }
    }

    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    public String getNumeroEmploye() {
        return numeroEmploye;
    }

    public String getNumeroSuccursale() {
        return numeroSuccursale;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.05.2003 10:09:50)
     *
     * @return java.lang.String
     */
    public java.lang.String getParamAnnonce() {
        return paramAnnonce;
    }

    /**
     * Returns the refUnique.
     *
     * @return String
     */
    public String getRefUnique() {
        return refUnique;
    }

    /**
     * Returns the statut.
     *
     * @return String
     */
    public String getStatut() {
        return statut;
    }

    public String getStatutLibelle() {
        try {
            return ((HEApplication) getSession().getApplication()).getCsStatutListe(getSession())
                    .getCodeSysteme(getStatut()).getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            return getStatut();
        }
    }

    public String getTitreAssure() {
        return titreAssure;
    }

    public String getTitreRentier() {
        return titreRentier;
    }

    /**
     * Returns the typeEnvoi.
     *
     * @return String
     */
    public String getTypeEnvoi() {
        try {
            if (enregistrement.startsWith("11")) {
                return getLibelle(IHEAnnoncesViewBean.CS_11_ANNONCE_ARC);
            } else if (enregistrement.startsWith("3")) {
                StringBuffer s = new StringBuffer(getLibelle(IHEAnnoncesViewBean.CS_39_EXTRAIT_CI_CONTROLE));
                s.append("(");
                s.append(getCaisse());
                s.append(")");
                return s.toString();
            } else if (enregistrement.startsWith("72")) {
                return getLibelle(IHEAnnoncesViewBean.CS_72_DEC_COMM_MES_INDIV);
            } else if (enregistrement.startsWith("73")) {
                return getLibelle(IHEAnnoncesViewBean.CS_73_DEMANDES);
            } else if (enregistrement.startsWith("74")) {
                return getLibelle(IHEAnnoncesViewBean.CS_74_PRON_RENTES_OU_ALLOC);
            } else if (enregistrement.startsWith("75")) {
                return getLibelle(IHEAnnoncesViewBean.CS_75_REFUS);
            } else if (enregistrement.startsWith("81")) {
                return getLibelle(IHEAnnoncesViewBean.CS_81_APG_ANCIEN_DROIT);
            } else if (enregistrement.startsWith("8F")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8F_APG_NOUVEAU_DROIT);
            } else if (enregistrement.startsWith("85")) {
                return getLibelle(IHEAnnoncesViewBean.CS_85_IJAI_ANCIEN);
            } else if (enregistrement.startsWith("8G")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8G_IJAI_NOUVEAU);
            } else if (enregistrement.startsWith("8A")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8A_DONNEES_COMPTABLES_A_CENTRACOMPTE_EXPLOITATION);
            } else if (enregistrement.startsWith("8B")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8B_BILAN_ET_COMPTE_ADMINISTRATION);
            } else if (enregistrement.startsWith("8C")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8C_RECAPITULATION_MENSUELCOMPTABILITE_AFFILIES);
            } else if (enregistrement.startsWith("8D")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8D_RECAPITULATION_MENSUELRENTES);
            } else if (enregistrement.startsWith("8E")) {
                return getLibelle(IHEAnnoncesViewBean.CS_8E_BALANCE_MOUVEMENTS_ANUELLES);
            } else {
                return "?";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "XXX";
        }
    }

    /**
     * Returns the valeur.
     *
     * @return String
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * Returns the isArchivage.
     *
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    /**
     * Returns the isConfirmed.
     *
     * @return boolean
     */
    public boolean isConfirmed() {
        return isConfirmed;
    }

    public boolean isRevenuCache() {
        try {
            return ((HEApplication) getSession().getApplication()).isRevenuCache(getSession().getUserId(), getSession(),
                    StringUtils.removeDots(getNumavs()));
        } catch (Exception e) {
            setMessage(e.getMessage());
            JadeLogger.error(this, e);
        }
        return true;
    }

    public boolean isUserPermit() {
        if (CIUtil.isSpecialist(getSession()) || getSession().getUserId().equalsIgnoreCase(getCreator())) {
            return true;
        } else {
            return false;
        }
    }

    public void loadChamps() {
        champs.setSession(getSession());
        champs.setForRefUnique(getRefUnique());
        champs.setIsArchivage(isArchivage());
        try {
            champs.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        // ajouter en variable de classe une hashtable
    }

    public void putFieldsToUpdate(HashMap req) {
        tmpRequest = req;
    }

    public void setAdresseAssure(String string) {
        adresseAssure = string;
    }

    public void setAdresseRentier(String adresseRentier) {
        this.adresseRentier = adresseRentier;
    }

    /**
     * Sets the caisse.
     *
     * @param caisse
     *            The caisse to set
     */
    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Sets the champs.
     *
     * @param champs
     *            The champs to set
     */
    public void setChamps(HEAttenteEnvoiChampsListViewBean champs) {
        this.champs = champs;
    }

    /**
     * Sets the creator.
     *
     * @param creator
     *            The creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @param string
     */
    public void setCritere(String string) {
        critere = string;
    }

    /**
     * Sets the dateCreation.
     *
     * @param dateCreation
     *            The dateCreation to set
     */
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * @param string
     */
    public void setDateEngagement(String string) {
        dateEngagement = string;
    }

    /**
     * Sets the debut.
     *
     * @param debut
     *            The debut to set
     */
    public void setDebut(String debut) {
        this.debut = debut;
    }

    /**
     * Sets the enregistrement.
     *
     * @param enregistrement
     *            The enregistrement to set
     */
    public void setEnregistrement(String enregistrement) {
        this.enregistrement = enregistrement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.03.2003 13:08:20)
     *
     * @param newIdAnnonce
     *            java.lang.String
     */
    public void setIdAnnonce(java.lang.String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    /**
     * Sets the idChamp.
     *
     * @param idChamp
     *            The idChamp to set
     */
    public void setIdChamp(String idChamp) {
        this.idChamp = idChamp;
    }

    /**
     * Sets the idLot.
     *
     * @param idLot
     *            The idLot to set
     */
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * Sets the isArchivage.
     *
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(boolean isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Sets the isConfirmed.
     *
     * @param isConfirmed
     *            The isConfirmed to set
     */
    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public void setLangueCorrespondance(String string) {
        langueCorrespondance = string;
    }

    /**
     * Sets the libelleChamp.
     *
     * @param libelleChamp
     *            The libelleChamp to set
     */
    public void setLibelleChamp(String libelleChamp) {
        this.libelleChamp = libelleChamp;
    }

    /**
     * Sets the longueur.
     *
     * @param longueur
     *            The longueur to set
     */
    public void setLongueur(String longueur) {
        this.longueur = longueur;
    }

    /**
     * Sets the messageFormatted.
     *
     * @param messageFormatted
     *            The messageFormatted to set
     */
    public void setMessageFormatted(String messageFormatted) {
        this.messageFormatted = messageFormatted;
    }

    /**
     * @param string
     */
    public void setMotif(String string) {
        motif = string;
    }

    /**
     * @return
     */
    /**
     * @param string
     */
    public void setMotifArc(String string) {
        motifArc = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.04.2003 17:11:33)
     *
     * @param newNom
     *            java.lang.String
     */
    public void setNom(java.lang.String newNom) {
        nom = newNom;
    }

    /**
     * Sets the nouveauNumAVS.
     *
     * @param nouveauNumAVS
     *            The nouveauNumAVS to set
     */
    public void setNouveauNumAVS(String nouveauNumAVS) {
        this.nouveauNumAVS = nouveauNumAVS;
    }

    /**
     * Sets the numavs.
     *
     * @param numavs
     *            The numavs to set
     */
    public void setNumavs(String numavs) {
        this.numavs = numavs;
    }

    /**
     * @param string
     */
    public void setNumeroAffilie(String string) {
        numeroAffilie = string;
    }

    public void setNumeroAvsNNSS(String numeroAvsNNSS) {
        this.numeroAvsNNSS = numeroAvsNNSS;
    }

    public void setNumeroEmploye(String numeroEmploye) {
        this.numeroEmploye = numeroEmploye;
    }

    public void setNumeroSuccursale(String numeroSuccursale) {
        this.numeroSuccursale = numeroSuccursale;
    }

    /**
     * Sets the paramAnnonce.
     *
     * @param paramAnnonce
     *            The paramAnnonce to set
     */
    public void setParamAnnonce(String paramAnnonce) {
        this.paramAnnonce = paramAnnonce;
    }

    /**
     * Sets the refUnique.
     *
     * @param refUnique
     *            The refUnique to set
     */
    public void setRefUnique(String refUnique) {
        this.refUnique = refUnique;
    }

    /**
     * Sets the statut.
     *
     * @param statut
     *            The statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @param string
     */
    public void setTitreAssure(String string) {
        titreAssure = string;
    }

    public void setTitreRentier(String titreRentier) {
        this.titreRentier = titreRentier;
    }

    /**
     * Sets the valeur.
     *
     * @param valeur
     *            The valeur to set
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    /**
     * Getter de formulePolitesse
     *
     * @return the formulePolitesse
     */
    public String getFormulePolitesse() {
        return formulePolitesse;
    }

    /**
     * Setter de formulePolitesse
     *
     * @param formulePolitesse the formulePolitesse to set
     */
    public void setFormulePolitesse(String formulePolitesse) {
        this.formulePolitesse = formulePolitesse;
    }

    public Boolean getIsArc61Cree() {
        return isArc61Cree;
    }

    public void setIsArc61Cree(Boolean isArc61Cree) {
        this.isArc61Cree = isArc61Cree;
    }

}

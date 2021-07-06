package globaz.helios.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBException;

import globaz.jade.properties.JadePropertiesService;
import org.xml.sax.SAXException;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.helios.application.CGApplication;
import globaz.helios.application.CGProperties;
import globaz.helios.db.avs.CGExtendedCompteOfas;
import globaz.helios.db.avs.CGExtendedCompteOfasManager;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAff;
import globaz.helios.db.avs.CGExtendedContrePartieCpteAffManager;
import globaz.helios.db.bouclement.CGBouclement;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.process.annonces.CGPeriodeComptableEnvoiAnnoncesXMLService;
import globaz.helios.process.annonces.CGPeriodeComptableXMLService;
import globaz.helios.process.helper.CGHelperReleveAVS;
import globaz.helios.process.helper.CGMetaAnnonceAttributs;
import globaz.helios.process.helper.CGMontantHelper;
import globaz.helios.translation.CodeSystem;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonceLight;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeException;
import globaz.jade.log.JadeLogger;

/**
 * Insérez la description du type ici. Date de création : (20.03.2003 14:48:16)
 *
 * @author: Administrator
 */
public class CGPeriodeComptableEnvoiAnnonces extends BProcess {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final static String labelPrefix = "ANNONCE_";
    private CGApplication application = null;
    private CGBouclement bouclement = null;
    private CGExerciceComptable exercice = null;
    private String idAnnonce = null;
    private String nomFichier = "";
    private String idComptabilite = "";
    private String idPeriodeComptable = "";
    private Boolean isComptaDefinitive = new Boolean(true);
    private String isPTRA = null;
    private Boolean isEnvoyerAnnoncesOfas = new Boolean(false);

    private List<CGMetaAnnonceAttributs> listAnnonces = new ArrayList<CGMetaAnnonceAttributs>();
    private CGMandat mandat = null;

    private CGPeriodeComptable periode = null;

    private BISession remoteSession = null;
    private BITransaction remoteTransaction = null;
    private CGHelperReleveAVS traitementReleveAvsHelper = new CGHelperReleveAVS();

    private boolean isAnnonceXML;
    private CGPeriodeComptableEnvoiAnnoncesXMLService xmlService;
    PoolMeldungZurZAS.Lot lotAnnonces;

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     */
    public CGPeriodeComptableEnvoiAnnonces() {
        super();
        traitementReleveAvsHelper = new CGHelperReleveAVS();
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     *
     * @param parent BProcess
     */
    public CGPeriodeComptableEnvoiAnnonces(BProcess parent) {
        super(parent);
        traitementReleveAvsHelper = new CGHelperReleveAVS();
    }

    /**
     * Commentaire relatif au constructeur CGJournalComptabiliserProcess.
     *
     * @param session BSession
     */
    public CGPeriodeComptableEnvoiAnnonces(BSession session) {
        super(session);
        traitementReleveAvsHelper = new CGHelperReleveAVS();
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        BTransaction transaction = getTransaction();

        try {
            try {
                application = (CGApplication) GlobazServer.getCurrentSystem().getApplication(
                        CGApplication.DEFAULT_APPLICATION_HELIOS);

                remoteSession = application.getSessionAnnonce(getSession());
                remoteTransaction = ((BSession) remoteSession).newTransaction();
                remoteTransaction.openTransaction();
                isPTRA = JadePropertiesService.getInstance().getProperty("helios.prestation.transitoire");

            } catch (Exception e) {
                e.printStackTrace();
                this._addError(e.getMessage());
                return false;
            }

            try {
                if (!init()) {
                    return false;
                }

                if (!testAnnoncesBalanceAnnuelle()) {
                    return false;
                }
            } catch (Exception e1) {
                return false;
            }

            if (transaction.hasErrors()) {
                this._addError(label("TRANSACTION_ERREUR"));
                return false;
            }
            int nbErrors = 0;

            this.info("DEBUT");

            // Annonces OFAS
            // On créé un fichier attaché au mail contenant les annonces OFAS
            if (isEnvoyerAnnoncesOfas.booleanValue()) {
                // Ajout des annonces OFAS dans le mail, si boulcement annuel
                if (nbErrors == 0) {
                    try {
                        registerAnnoncesOfasToMail();
                    } catch (Exception e) {
                        this.warn("OFAS_ERROR", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // Envoi des annonces à la centrale
            else {
                try {
                    if (isAnnonceXML) {
                        lotAnnonces = CGPeriodeComptableXMLService.getInstance().initPoolMeldungZurZASLot(CGProperties.CENTRALE_TEST.getBooleanValue(), CommonProperties.KEY_NO_CAISSE.getValue());
                        xmlService = new CGPeriodeComptableEnvoiAnnoncesXMLService(getSession(), getTransaction(), periode, exercice, lotAnnonces.getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr(), application);
                        xmlService.preparerAnnoncesComptesExploitation(traitementReleveAvsHelper, isComptaDefinitive);
                    } else {
                        preparerAnnoncesComptesExploitation();
                    }
                    this.info("COMPTE_EXPLOITATION_OK");
                } catch (Exception e) {
                    this.error("COMPTE_EXPLOITATION_ERREUR", e.getMessage());
                    nbErrors++;
                    transaction.addErrors(e.getMessage());
                    e.printStackTrace();
                    // fermeture remotetransaction !!!
                    rollbackRemoteTransaction(e.getMessage());
                }

                if (nbErrors == 0) {
                    try {
                        preparerAnnoncesZAS8B();
                        this.info("COMPTE_8B_OK");

                    } catch (Exception e) {
                        this.error("COMPTE_8B_ERREUR", e.getMessage());
                        nbErrors++;
                        rollbackRemoteTransaction(e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (nbErrors == 0) {
                    try {
                        if (bouclement.isBouclementMensuelAVS().booleanValue()) {
                            if (isAnnonceXML) {
                                xmlService.preparerAnnoncesComptesAffilie();
                            } else {
                                preparerAnnoncesComptesAffilie();
                            }
                            this.info("COMPTE_AFFILIE_OK");
                        }
                    } catch (Exception e) {
                        this.error("COMPTE_AFFILIE_ERREUR", e.getMessage());
                        nbErrors++;
                        rollbackRemoteTransaction(e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (nbErrors == 0) {
                    try {
                        if (bouclement.isBouclementAnnuelAVS().booleanValue()) {
                            if (isAnnonceXML) {
                                xmlService.preparerAnnoncesBalanceAnnuelleMvt(initAnnoncesBalanceAnnuelleManager(), traitementReleveAvsHelper, isComptaDefinitive);
                            } else {
                                preparerAnnoncesBalanceAnnuelleMvt();
                            }
                            this.info("COMPTE_MVTANU_OK");
                        }

                    } catch (Exception e) {
                        this.error("COMPTE_MVTANU_ERREUR", e.getMessage());
                        nbErrors++;
                        rollbackRemoteTransaction(e.getMessage());
                        e.printStackTrace();
                    }
                }

                // Envoi des annonces
                if (nbErrors == 0) {
                    try {
                        if (isAnnonceXML) {
                            nomFichier = annoncerXML();
                        } else {
                            idAnnonce = annoncer(remoteTransaction, remoteSession, listAnnonces, application);
                        }
                    } catch (Exception e) {
                        nbErrors++;
                        rollbackRemoteTransaction(e.getMessage());
                        this.error("UPDATE_IDANNONCE_PERIODE_ERROR", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            if (nbErrors == 0) {
                try {
                    // commit des annonces crées
                    remoteTransaction.commit();
                    remoteTransaction.closeTransaction();
                    remoteTransaction = null;

                    if (!periode.isNew()) {
                        if (isAnnonceXML) {
                            periode.setIdAnnonce("0");
                            periode.setNomFichier(nomFichier);
                            periode.update();
                        } else {
                            // Setter id Annonce à la période:
                            periode.setIdAnnonce(idAnnonce);
                            periode.setNomFichier("");
                            periode.update();
                        }
                    }

                } catch (Exception e) {
                    rollbackRemoteTransaction(e.getMessage());
                    this.error("UPDATE_IDANNONCE_PERIODE_ERROR", e.getMessage());
                    e.printStackTrace();
                }
            }

            this.info("FIN");
        } finally {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                remoteTransaction = null;

            }
        }
        return true;
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {

        if ((getIdPeriodeComptable() == null) || getIdPeriodeComptable().equals("")
                || getIdPeriodeComptable().equals("0")) {
            this._addError(global("PERIODE_INVALIDE"));
            return;
        }

        if (!init()) {
            return;
        }

        if (getParent() != null) {
            setSendCompletionMail(false);
            setSendMailOnError(false);
            setControleTransaction(false);
        } else {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    public String annoncerXML() throws PropertiesException, IOException, SAXException, JAXBException, JadeException {
        String fileName = CGPeriodeComptableXMLService.getInstance().genereFichier(lotAnnonces);
        CGPeriodeComptableXMLService.getInstance().envoiFichier(fileName);
        return new File(fileName).getName();
    }


    /**
     * Ajouter les annonces en BD à l'aide du module Hermes.
     *
     * @param remoteTransaction
     * @param remoteSession
     * @param listMetaAttributs
     * @param application
     * @return
     * @throws Exception
     */
    public String annoncer(BITransaction remoteTransaction, BISession remoteSession,
                           List<CGMetaAnnonceAttributs> listMetaAttributs, CGApplication application) throws Exception {
        boolean hasOpenedTransaction = false;
        try {
            String idAnnonce = null;
            if ((remoteTransaction != null) && !remoteTransaction.hasErrors()) {

                if (!remoteTransaction.isOpened()) {
                    remoteTransaction.openTransaction();
                    hasOpenedTransaction = true;
                }

                BISession local = remoteTransaction.getISession();

                // création de l'API
                IHEInputAnnonceLight remoteEcritureAnnonce = (IHEInputAnnonceLight) remoteSession
                        .getAPIFor(IHEInputAnnonceLight.class);
                // attributs standard

                Iterator iter = listMetaAttributs.iterator();
                while (iter.hasNext()) {
                    remoteEcritureAnnonce.clear();
                    remoteEcritureAnnonce.setIdProgramme(CGApplication.DEFAULT_APPLICATION_HELIOS);
                    remoteEcritureAnnonce.setUtilisateur(local.getUserId());

                    if (!JadeStringUtil.isIntegerEmpty(getPeriode().getExerciceComptable().getMandat().getNoCaisse())) {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE, getPeriode()
                                .getExerciceComptable().getMandat().getNoCaisse());
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE, getPeriode()
                                .getExerciceComptable().getMandat().getNoAgence());
                    } else {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE, CaisseHelperFactory.getInstance()
                                .getNoCaisse(application));
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE, CaisseHelperFactory.getInstance()
                                .getNoAgence(application));
                    }

                    remoteEcritureAnnonce.setStatut(IHEAnnoncesViewBean.CS_TERMINE);

                    CGMetaAnnonceAttributs element = (CGMetaAnnonceAttributs) iter.next();
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, element.getCodeAnnonce());
                    // ajout de attributs spécifique à l'annonce
                    remoteEcritureAnnonce.putAll(element.getAttributs());
                    remoteEcritureAnnonce.add(remoteTransaction);

                    if (!remoteTransaction.hasErrors() && (idAnnonce == null)) {
                        idAnnonce = remoteEcritureAnnonce.getRefUnique();
                    }
                }
            }
            return idAnnonce;
        } finally {
            if (hasOpenedTransaction) {
                try {
                    remoteTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        remoteTransaction.closeTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void bindData(String idPeriodeComptable) {
        setIdPeriodeComptable(idPeriodeComptable);

        try {
            BITransaction remoteTransaction = getSession().newTransaction();
            remoteTransaction.openTransaction();
            setTransaction(remoteTransaction);

            _validate();

            if (getSession().hasErrors()) {
                JadeLogger.error(this, getSession().getErrors().toString());
            }

            if (getTransaction().hasErrors()) {
                JadeLogger.error(this, getTransaction().getErrors().toString());
            }

            _executeProcess();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (remoteTransaction != null) {
                try {
                    remoteTransaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                remoteTransaction = null;
            }
        }

    }

    /**
     * @param count
     * @param nbrComptes
     * @param listResultAnnonces8B
     */
    private void construireAnnonces8B(int count, int nbrComptes, ArrayList<CGMontantHelper> listResultAnnonces8B) {
        HashMap<String, String> attributs = null;

        for (Iterator iterator = listResultAnnonces8B.iterator(); iterator.hasNext(); ) {
            CGMontantHelper result = (CGMontantHelper) iterator.next();

            nbrComptes++;

            // 5 comptes par annonces
            switch (nbrComptes) {
                case 1:
                    attributs = new HashMap<String, String>();
                    // remplissage de l'annonce
                    int codeEnreg = (count % 999) + 1;
                    String codeEnregistrement = String.valueOf(codeEnreg);
                    count++;
                    attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, codeEnregistrement);
                    attributs.put(IHEAnnoncesViewBean.PERIODE_COMPTABLE, periode.getCode());
                    attributs
                            .put(IHEAnnoncesViewBean.EXERCICE_COMPTABLE_AAAA, exercice.getDateDebut().substring(6, 10));
                    attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_1, result.idExterne.substring(0, 3)
                            + result.idExterne.substring(4, 8));
                    if (result.actif != null) {

                        if (result.actif.isPositive() || result.actif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "1");
                        }

                        if (result.actif != null) {
                            result.actif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_1, getFormattedNumer12Pos(result.actif));
                    } else if (result.passif != null) {
                        if (result.passif.isPositive() || result.passif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "1");
                        }
                        if (result.passif != null) {
                            result.passif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_1, getFormattedNumer12Pos(result.passif));

                    } else {
                        attributs.put(IHEAnnoncesViewBean.MONTANT_1, getFormattedNumer12Pos(null));
                        attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "0");
                    }

                    break;
                case 2:
                    attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_2, result.idExterne.substring(0, 3)
                            + result.idExterne.substring(4, 8));
                    if (result.actif != null) {

                        if (result.actif.isPositive() || result.actif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "1");
                        }

                        if (result.actif != null) {
                            result.actif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_2, getFormattedNumer12Pos(result.actif));
                    } else if (result.passif != null) {
                        if (result.passif.isPositive() || result.passif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "1");
                        }

                        if (result.passif != null) {
                            result.passif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_2, getFormattedNumer12Pos(result.passif));

                    } else {
                        attributs.put(IHEAnnoncesViewBean.MONTANT_2, getFormattedNumer12Pos(null));
                        attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "0");
                    }

                    break;
                case 3:
                    attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_3, result.idExterne.substring(0, 3)
                            + result.idExterne.substring(4, 8));
                    if (result.actif != null) {

                        if (result.actif.isPositive() || result.actif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "1");
                        }

                        if (result.actif != null) {
                            result.actif.abs();
                        }

                        attributs.put(IHEAnnoncesViewBean.MONTANT_3, getFormattedNumer12Pos(result.actif));
                    } else if (result.passif != null) {
                        if (result.passif.isPositive() || result.passif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "1");
                        }

                        if (result.passif != null) {
                            result.passif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_3, getFormattedNumer12Pos(result.passif));

                    } else {
                        attributs.put(IHEAnnoncesViewBean.MONTANT_3, getFormattedNumer12Pos(null));
                        attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "0");
                    }

                    break;
                case 4:
                    attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_4, result.idExterne.substring(0, 3)
                            + result.idExterne.substring(4, 8));
                    if (result.actif != null) {

                        if (result.actif.isPositive() || result.actif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "1");
                        }

                        if (result.actif != null) {
                            result.actif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_4, getFormattedNumer12Pos(result.actif));
                    } else if (result.passif != null) {
                        if (result.passif.isPositive() || result.passif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "1");
                        }

                        if (result.passif != null) {
                            result.passif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_4, getFormattedNumer12Pos(result.passif));

                    } else {
                        attributs.put(IHEAnnoncesViewBean.MONTANT_4, getFormattedNumer12Pos(null));
                        attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "0");
                    }

                    break;
                case 5:
                    attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_5, result.idExterne.substring(0, 3)
                            + result.idExterne.substring(4, 8));
                    if (result.actif != null) {

                        if (result.actif.isPositive() || result.actif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "1");
                        }

                        if (result.actif != null) {
                            result.actif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_5, getFormattedNumer12Pos(result.actif));
                    } else if (result.passif != null) {
                        if (result.passif.isPositive() || result.passif.isZero()) {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "0");
                        } else {
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "1");
                        }

                        if (result.passif != null) {
                            result.passif.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_5, getFormattedNumer12Pos(result.passif));

                    } else {
                        attributs.put(IHEAnnoncesViewBean.MONTANT_5, getFormattedNumer12Pos(null));
                        attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "0");
                    }

                    // preparer l'annonces

                    CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
                    metaAttr.setCodeAnnonce("8B");
                    metaAttr.setAttributs(attributs);
                    listAnnonces.add(metaAttr);

                    // reset du compteur
                    nbrComptes = 0;
                    // ////////////////////
                    break;
            }

        }
        // Annonces avec compte 1 uniquement doit être envoyée
        if ((nbrComptes > 0) && (nbrComptes < 5)) {

            CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
            metaAttr.setCodeAnnonce("8B");
            metaAttr.setAttributs(attributs);
            listAnnonces.add(metaAttr);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void error(String msg) {
        this.error(msg, "");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void error(String codeLabel, String msg) {
        getMemoryLog().logMessage(getSession().getLabel(CGPeriodeComptableEnvoiAnnonces.labelPrefix + codeLabel) + msg,
                FWViewBeanInterface.ERROR, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void fatal(String codeLabel) {
        this.fatal(codeLabel, "");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void fatal(String codeLabel, String msg) {
        getMemoryLog().logMessage(getSession().getLabel(CGPeriodeComptableEnvoiAnnonces.labelPrefix + codeLabel) + msg,
                FWMessage.FATAL, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

    /**
     * Method getAnnoncesOFAS. Idem que les annonces de la balance des mvts annuelles, en ne prenant que les comptes de
     * bilan et administration du secteur 9. Les annonces à envoyer sont des 8B, mais générée comme des 8E.
     *
     * @param remoteTransaction
     * @param remoteSession
     * @param listMetaAttributs
     * @return List
     * @throws Exception
     */
    public List getAnnoncesOFAS(BITransaction remoteTransaction, BISession remoteSession, List listMetaAttributs,
                                CGApplication application) throws Exception {
        List result = new ArrayList();
        boolean hasOpenedTransaction = false;
        try {
            if ((remoteTransaction != null) && !remoteTransaction.hasErrors()) {

                if (!remoteTransaction.isOpened()) {
                    remoteTransaction.openTransaction();
                    hasOpenedTransaction = true;
                }

                BISession local = remoteTransaction.getISession();

                // création de l'API
                IHEInputAnnonceLight remoteEcritureAnnonce = (IHEInputAnnonceLight) remoteSession
                        .getAPIFor(IHEInputAnnonceLight.class);
                // attributs standard

                Iterator iter = listMetaAttributs.iterator();
                while (iter.hasNext()) {
                    remoteEcritureAnnonce.clear();
                    remoteEcritureAnnonce.setIdProgramme(CGApplication.DEFAULT_APPLICATION_HELIOS);
                    remoteEcritureAnnonce.setUtilisateur(local.getUserId());

                    if (!JadeStringUtil.isIntegerEmpty(getPeriode().getExerciceComptable().getMandat().getNoCaisse())) {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE, getPeriode()
                                .getExerciceComptable().getMandat().getNoCaisse());
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE, getPeriode()
                                .getExerciceComptable().getMandat().getNoAgence());
                    } else {
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE, CaisseHelperFactory.getInstance()
                                .getNoCaisse(application));
                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE, CaisseHelperFactory.getInstance()
                                .getNoAgence(application));
                    }

                    remoteEcritureAnnonce.setStatut(IHEAnnoncesViewBean.CS_TERMINE);

                    CGMetaAnnonceAttributs element = (CGMetaAnnonceAttributs) iter.next();
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION, element.getCodeAnnonce());
                    // ajout de attributs spécifique à l'annonce
                    remoteEcritureAnnonce.putAll(element.getAttributs());
                    String annonce = remoteEcritureAnnonce.getChampEnregistrementFromAttr();

                    result.add(annonce);
                }
            }
            return result;
        } finally {
            if (hasOpenedTransaction) {
                try {
                    remoteTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        remoteTransaction.closeTransaction();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected String getEMailObject() {
        if (FWMessage.AVERTISSEMENT.equals(getMemoryLog().getErrorLevel())) {
            if (isEnvoyerAnnoncesOfas.booleanValue()) {
                return label("OFAS_OBJET_MAIL_AVERTISSEMENT") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            } else {
                return label("OBJET_MAIL_AVERTISSEMENT") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            }
        } else if (FWMessage.ERREUR.equals(getMemoryLog().getErrorLevel())) {
            if (isEnvoyerAnnoncesOfas.booleanValue()) {
                return label("OFAS_OBJET_MAIL_ERREUR") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            } else {
                return label("OBJET_MAIL_ERREUR") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            }
        } else {
            if (isEnvoyerAnnoncesOfas.booleanValue()) {
                return label("OFAS_OBJET_MAIL_OK") + " " + getPeriode().getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            } else {
                return label("OBJET_MAIL_OK") + " " + periode.getFullDescription() + ", ID "
                        + periode.getIdPeriodeComptable();
            }
        }
    }

    /**
     * Retourne le numéro number formatté : FFFFFFFFFFCC ex. number = 123.456 -> retourne 000000012345
     */

    private String getFormattedNumer12Pos(FWCurrency number) {

        int LENGTH = 12;
        BigDecimal value = new BigDecimal(0);
        if (number != null) {
            value = new BigDecimal(number.toString());
        }
        BigDecimal r1 = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal rr1 = r1.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
        StringBuffer s1 = new StringBuffer(rr1.toString());

        int missingChar = LENGTH - s1.length();
        for (int i = 0; i < missingChar; i++) {
            s1.insert(0, '0');
        }
        return s1.toString();
    }

    /**
     * Retourne le numéro number formatté : FFFFFFFFFFCC ex. number = 123.456 -> retourne 00000000012345
     */

    private String getFormattedNumer14Pos(FWCurrency number) {

        int LENGTH = 14;
        BigDecimal value = new BigDecimal(0);
        if (number != null) {
            value = new BigDecimal(number.toString());
        }
        BigDecimal r1 = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal rr1 = r1.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
        StringBuffer s1 = new StringBuffer(rr1.toString());

        int missingChar = LENGTH - s1.length();
        for (int i = 0; i < missingChar; i++) {
            s1.insert(0, '0');
        }
        return s1.toString();
    }

    /**
     * Returns the reqComptabilite.
     *
     * @return String
     */
    public String getIdComptabilite() {
        return idComptabilite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.03.2003 14:53:42)
     *
     * @return String
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * Returns the isEnvoyerAnnoncesOfas.
     *
     * @return Boolean
     */
    public Boolean getIsEnvoyerAnnoncesOfas() {
        return isEnvoyerAnnoncesOfas;
    }

    /**
     * @param session
     * @param nbRecords
     * @return
     * @throws Exception
     * @deprecated HELotUtils
     */
    @Deprecated
    private String getLotFooter(BSession session, int nbRecords) throws Exception {
        String footer = "9901";

        if (!JadeStringUtil.isIntegerEmpty(getPeriode().getExerciceComptable().getMandat().getNoCaisse())) {
            footer += JadeStringUtil
                    .rightJustify(getPeriode().getExerciceComptable().getMandat().getNoCaisse(), 3, '0')
                    + JadeStringUtil
                    .rightJustify(getPeriode().getExerciceComptable().getMandat().getNoAgence(), 3, '0');
        } else {
            footer += CaisseHelperFactory.getInstance().getNoCaisse(application)
                    + CaisseHelperFactory.getInstance().getNoAgence(application);
        }

        footer = JadeStringUtil.leftJustify(footer, 24);
        footer += "T0" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYY);
        footer += JadeStringUtil.rightJustify(String.valueOf(nbRecords), 6, '0');
        if ("true".equals(session.getApplication().getProperty("ftp.test"))) {
            footer += "TEST";
        } else {
            footer += "    ";
        }
        footer = JadeStringUtil.leftJustify(footer, 120);
        return footer;
    }

    /**
     * @param session
     * @return
     * @throws Exception
     * @deprecated HELotUtils
     */
    @Deprecated
    private String getLotHeader(BSession session) throws Exception {
        String header = "0101";

        if (!JadeStringUtil.isIntegerEmpty(getPeriode().getExerciceComptable().getMandat().getNoCaisse())) {
            header += JadeStringUtil
                    .rightJustify(getPeriode().getExerciceComptable().getMandat().getNoCaisse(), 3, '0')
                    + JadeStringUtil
                    .rightJustify(getPeriode().getExerciceComptable().getMandat().getNoAgence(), 3, '0');
        } else {
            header += CaisseHelperFactory.getInstance().getNoCaisse(application)
                    + CaisseHelperFactory.getInstance().getNoAgence(application);
        }

        header = JadeStringUtil.leftJustify(header, 24);
        header += "T0" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYY);
        header = JadeStringUtil.leftJustify(header, 38);
        if ("true".equals(session.getApplication().getProperty("ftp.test"))) {
            header += "TEST";
        } else {
            header += "    ";
        }
        header = JadeStringUtil.leftJustify(header, 120);
        return header;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 14:37:25)
     *
     * @return CGPeriodeComptable
     */
    public CGPeriodeComptable getPeriode() {
        if (periode == null) {
            try {
                periode = new CGPeriodeComptable();
                periode.setSession(getSession());
                periode.setIdPeriodeComptable(getIdPeriodeComptable());
                periode.retrieve(getTransaction());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return periode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 18:36:40)
     *
     * @param codeLabel String
     * @return String
     */
    private String global(String codeLabel) {
        return getSession().getLabel("GLOBAL_" + codeLabel);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void info(String msg) {
        this.info(msg, "");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void info(String codeLabel, String msg) {
        getMemoryLog().logMessage(getSession().getLabel(CGPeriodeComptableEnvoiAnnonces.labelPrefix + codeLabel) + msg,
                FWMessage.INFORMATION, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

    private boolean init() throws Exception {
        isAnnonceXML = CGProperties.ACTIVER_ANNONCES_XML.getBooleanValue();
        periode = new CGPeriodeComptable();
        periode.setSession(getSession());
        periode.setIdPeriodeComptable(getIdPeriodeComptable());
        periode.retrieve(getTransaction());
        if (periode.isNew()) {
            this._addError(global("PERIODE_INEXISTANT"));
            return false;
        }

        if (!periode.isEstCloture().booleanValue()) {
            this._addError(label("PERIODE_A_BOUCLER"));
            return false;
        }

        if (periode.getCode().equals(CGPeriodeComptable.CS_CODE_CLOTURE)) {
            this._addError(global("PERIODE_INVALIDE"));
            return false;
        }

        exercice = periode.getExerciceComptable();
        if ((exercice == null) || exercice.isNew()) {
            this._addError(global("EXERCICE_INEXISTANT"));
            return false;
        }

        mandat = exercice.getMandat();
        if ((mandat == null) || mandat.isNew()) {
            this._addError(global("MANDAT_INEXISTANT"));
            return false;
        }

        bouclement = new CGBouclement();
        bouclement.setSession(getSession());
        bouclement.setIdBouclement(periode.getIdBouclement());
        bouclement.retrieve(getTransaction());
        if (bouclement.isNew()) {
            this._addError(global("BOUCLEMENT_INEXISTANT"));
            return false;
        }

        return true;
    }

    /**
     * Initialise le manager des annonces de balance annuelle (8E).
     *
     * @return
     */
    private CGExtendedCompteOfasManager initAnnoncesBalanceAnnuelleManager() {
        CGExtendedCompteOfasManager ds = new CGExtendedCompteOfasManager();
        ds.setSession(getSession());
        ds.setForIdMandat(exercice.getIdMandat());

        ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_BALANCE_ANNUELLE);
        return ds;
    }

    /**
     * Returns the isComptaDefinitive.
     *
     * @return Boolean
     */
    public Boolean isComptaDefinitive() {
        return isComptaDefinitive;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        if ((bouclement != null) && bouclement.isBouclementAnnuelAVS().booleanValue()) {
            return GlobazJobQueue.UPDATE_LONG;
        } else {
            return GlobazJobQueue.UPDATE_SHORT;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 18:36:40)
     *
     * @param codeLabel String
     * @return String
     */
    private String label(String codeLabel) {
        return getSession().getLabel(CGPeriodeComptableEnvoiAnnonces.labelPrefix + codeLabel);
    }

    protected void preparerAnnoncesBalanceAnnuelleMvt() throws Exception {

        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;
        CGExtendedCompteOfas extendedCompteOfas = null;
        CGExtendedCompteOfasManager ds = initAnnoncesBalanceAnnuelleManager();

        int count = 0;
        int nbrComptes = 0;
        HashMap attributs = null;

        statement = ds.cursorOpen(getTransaction());
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantMvtAnnuel(getTransaction(), getSession(),
                    extendedCompteOfas, exercice.getIdMandat(), exercice.getIdExerciceComptable(),
                    periode.getIdPeriodeComptable(), !isComptaDefinitive().booleanValue());

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if (((result.passif != null) && !result.passif.isNegative())
                    || ((result.actif != null) && !result.actif.isNegative())) {
                nbrComptes++;

                // 2 comptes par annonces
                switch (nbrComptes) {
                    case 1:
                        attributs = new HashMap();
                        // remplissage de l'annonce
                        int codeEnreg = (count % 999) + 1;
                        String codeEnregistrement = String.valueOf(codeEnreg);
                        count++;
                        attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, codeEnregistrement);
                        attributs.put(IHEAnnoncesViewBean.EXERCICE_COMPTABLE_AAAA,
                                exercice.getDateDebut().substring(6, 10));

                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_1, extendedCompteOfas.getIdExterne()
                                .substring(0, 3) + extendedCompteOfas.getIdExterne().substring(4, 8));

                        attributs.put(IHEAnnoncesViewBean.MONTANT_DEBITEUR_1, getFormattedNumer14Pos(result.actif));
                        attributs.put(IHEAnnoncesViewBean.MONTANT_CREANCIER_1, getFormattedNumer14Pos(result.passif));

                        break;
                    case 2:

                        // remplissage de l'annonce
                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_2, extendedCompteOfas.getIdExterne()
                                .substring(0, 3) + extendedCompteOfas.getIdExterne().substring(4, 8));
                        attributs.put(IHEAnnoncesViewBean.MONTANT_DEBITEUR_2, getFormattedNumer14Pos(result.actif));
                        attributs.put(IHEAnnoncesViewBean.MONTANT_CREANCIER_2, getFormattedNumer14Pos(result.passif));

                        CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
                        metaAttr.setCodeAnnonce("8E");
                        metaAttr.setAttributs(attributs);
                        listAnnonces.add(metaAttr);

                        // reset du compteur
                        nbrComptes = 0;
                        // ////////////////////
                        break;
                }
            }
        }

        ds.cursorClose(statement);

        // Annonces avec compte 1 uniquement doit être envoyée
        if (nbrComptes == 1) {

            CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
            metaAttr.setCodeAnnonce("8E");
            // Les champs du 2èmes comptes sont égal à 'blank'
            attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_2, "       ");
            attributs.put(IHEAnnoncesViewBean.MONTANT_DEBITEUR_2, "              ");
            attributs.put(IHEAnnoncesViewBean.MONTANT_CREANCIER_2, "              ");
            metaAttr.setAttributs(attributs);
            listAnnonces.add(metaAttr);
        }
    }

    protected void preparerAnnoncesComptesAffilie() throws Exception {

        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;

        CGExtendedContrePartieCpteAffManager contrePartieMgr = new CGExtendedContrePartieCpteAffManager();
        contrePartieMgr.setSession(getSession());
        contrePartieMgr.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        contrePartieMgr.setForIdPeriodeComptable(periode.getIdPeriodeComptable());
        contrePartieMgr.setForIdMandat(exercice.getIdMandat());
        contrePartieMgr.setForIsActive(new Boolean(true));
        contrePartieMgr.setForIsProvisoire(new Boolean(false));

        int count = 0;
        int nbrComptes = 0;
        HashMap attributs = null;

        CGExtendedContrePartieCpteAff extContePart = null;
        statement = contrePartieMgr.cursorOpen(getTransaction());
        while ((extContePart = (CGExtendedContrePartieCpteAff) contrePartieMgr.cursorReadNext(statement)) != null) {

            String sMontant = extContePart.getMontant();
            FWCurrency montant = new FWCurrency(sMontant);
            String numeroCompteOfas = extContePart.getNumeroCompteOfas();

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if (!montant.isZero()) {
                nbrComptes++;

                // 2 comptes par annonces
                switch (nbrComptes) {
                    case 1:
                        attributs = new HashMap();
                        // remplissage de l'annonce
                        int codeEnreg = (count % 999) + 1;
                        String codeEnregistrement = String.valueOf(codeEnreg);
                        count++;
                        attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, codeEnregistrement);
                        attributs.put(IHEAnnoncesViewBean.PERIODE_COMPTABLE, periode.getCode());
                        attributs.put(IHEAnnoncesViewBean.EXERCICE_COMPTABLE_AAAA,
                                exercice.getDateDebut().substring(6, 10));

                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_1, numeroCompteOfas.substring(0, 3)
                                + numeroCompteOfas.substring(4, 8));

                        if (montant.isZero() || montant.isPositive()) {
                            attributs.put(IHEAnnoncesViewBean.MONTANT_1, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "0");
                        } else {
                            if (montant != null) {
                                montant.abs();
                            }
                            attributs.put(IHEAnnoncesViewBean.MONTANT_1, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_9, "1");
                        }
                        break;
                    case 2:
                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_2, numeroCompteOfas.substring(0, 3)
                                + numeroCompteOfas.substring(4, 8));

                        if (montant.isZero() || montant.isPositive()) {
                            attributs.put(IHEAnnoncesViewBean.MONTANT_2, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "0");
                        } else {
                            if (montant != null) {
                                montant.abs();
                            }
                            attributs.put(IHEAnnoncesViewBean.MONTANT_2, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_12, "1");
                        }
                        break;

                    case 3:
                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_3, numeroCompteOfas.substring(0, 3)
                                + numeroCompteOfas.substring(4, 8));

                        if (montant.isZero() || montant.isPositive()) {
                            attributs.put(IHEAnnoncesViewBean.MONTANT_3, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "0");
                        } else {
                            if (montant != null) {
                                montant.abs();
                            }
                            attributs.put(IHEAnnoncesViewBean.MONTANT_3, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_15, "1");
                        }
                        break;
                    case 4:
                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_4, numeroCompteOfas.substring(0, 3)
                                + numeroCompteOfas.substring(4, 8));

                        if (montant.isZero() || montant.isPositive()) {
                            attributs.put(IHEAnnoncesViewBean.MONTANT_4, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "0");
                        } else {
                            if (montant != null) {
                                montant.abs();
                            }
                            attributs.put(IHEAnnoncesViewBean.MONTANT_4, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_18, "1");
                        }
                        break;
                    case 5:
                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_5, numeroCompteOfas.substring(0, 3)
                                + numeroCompteOfas.substring(4, 8));

                        if (montant.isZero() || montant.isPositive()) {
                            attributs.put(IHEAnnoncesViewBean.MONTANT_5, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "0");
                        } else {
                            if (montant != null) {
                                montant.abs();
                            }
                            attributs.put(IHEAnnoncesViewBean.MONTANT_5, getFormattedNumer12Pos(montant));
                            attributs.put(IHEAnnoncesViewBean.CODE_VALEUR_DU_CHAMP_21, "1");
                        }

                        CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
                        metaAttr.setCodeAnnonce("8C");
                        metaAttr.setAttributs(attributs);
                        listAnnonces.add(metaAttr);

                        // reset du compteur
                        nbrComptes = 0;
                        // ////////////////////
                        break;
                }
            }
        }

        contrePartieMgr.cursorClose(statement);
        // Annonces avec compte 1à4 uniquement doit être envoyée
        if ((nbrComptes > 0) && (nbrComptes < 5)) {

            CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
            metaAttr.setCodeAnnonce("8C");
            metaAttr.setAttributs(attributs);
            listAnnonces.add(metaAttr);

        }
    }

    /**
     * Envoi des annoonces pour les comptes d'exploitations Date de création : (27.05.2003 11:48:31)
     */
    protected void preparerAnnoncesComptesExploitation() throws Exception {

        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;
        CGExtendedCompteOfas extendedCompteOfas = null;
        CGExtendedCompteOfasManager ds = new CGExtendedCompteOfasManager();
        ds.setSession(getSession());
        ds.setForIdMandat(exercice.getIdMandat());
        ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_EXPLOITATION);
        if (isPTRA != null && isPTRA.equals("true")) {
            ds.setPtra8Aor8B("8A");
        }

        int count = 0;
        int nbrComptes = 0;
        HashMap attributs = null;

        statement = ds.cursorOpen(getTransaction());
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantComptesExploitation(getTransaction(),
                    getSession(), extendedCompteOfas, exercice.getIdMandat(), exercice.getIdExerciceComptable(),
                    periode.getIdPeriodeComptable(), !isComptaDefinitive().booleanValue());

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if (((result.passif != null) && !result.passif.isNegative())
                    || ((result.actif != null) && !result.actif.isNegative())
                    || ((result.soldeCumule != null) && (!result.soldeCumule.isZero()))) {
                nbrComptes++;

                // 2 comptes par annonces
                switch (nbrComptes) {
                    case 1:
                        attributs = new HashMap();
                        // remplissage de l'annonce
                        int codeEnreg = (count % 999) + 1;
                        String codeEnregistrement = String.valueOf(codeEnreg);
                        count++;
                        attributs.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, codeEnregistrement);
                        attributs.put(IHEAnnoncesViewBean.PERIODE_COMPTABLE, periode.getCode());
                        attributs.put(IHEAnnoncesViewBean.EXERCICE_COMPTABLE_AAAA,
                                exercice.getDateDebut().substring(6, 10));

                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_1, extendedCompteOfas.getIdExterne()
                                .substring(0, 3) + extendedCompteOfas.getIdExterne().substring(4, 8));

                        attributs.put(IHEAnnoncesViewBean.MONTANT_AVOIR_1, getFormattedNumer12Pos(result.passif));
                        attributs.put(IHEAnnoncesViewBean.MONTANT_DOIT_1, getFormattedNumer12Pos(result.actif));
                        if (result.soldeCumule != null) {
                            result.soldeCumule.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_SOLDE_1, getFormattedNumer12Pos(result.soldeCumule));

                        break;
                    case 2:

                        // remplissage de l'annonce
                        attributs.put(IHEAnnoncesViewBean.NUMERO_DE_COMPTE_2, extendedCompteOfas.getIdExterne()
                                .substring(0, 3) + extendedCompteOfas.getIdExterne().substring(4, 8));
                        attributs.put(IHEAnnoncesViewBean.MONTANT_AVOIR_2, getFormattedNumer12Pos(result.passif));
                        attributs.put(IHEAnnoncesViewBean.MONTANT_DOIT_2, getFormattedNumer12Pos(result.actif));
                        if (result.soldeCumule != null) {
                            result.soldeCumule.abs();
                        }
                        attributs.put(IHEAnnoncesViewBean.MONTANT_SOLDE_2, getFormattedNumer12Pos(result.soldeCumule));

                        CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
                        metaAttr.setCodeAnnonce("8A");
                        metaAttr.setAttributs(attributs);
                        listAnnonces.add(metaAttr);

                        if (remoteTransaction.hasErrors()) {
                            throw new Exception(label("MSG_ANNONCE_INPUT_INVALID"));
                        }
                        // reset du compteur
                        nbrComptes = 0;
                        // ////////////////////
                        break;
                }
            }
        }

        ds.cursorClose(statement);

        // Annonces avec compte 1 uniquement doit être envoyée
        if (nbrComptes == 1) {

            CGMetaAnnonceAttributs metaAttr = new CGMetaAnnonceAttributs();
            metaAttr.setCodeAnnonce("8A");
            metaAttr.setAttributs(attributs);
            listAnnonces.add(metaAttr);
        }
    }

    /**
     * Method preparerAnnonces8B. Prépare les annonces des comptes Bilan et comptes Administration
     *
     * @throws Exception
     */
    protected void preparerAnnoncesZAS8B() throws Exception {

        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;
        CGExtendedCompteOfas extendedCompteOfas = null;
        CGExtendedCompteOfasManager ds = new CGExtendedCompteOfasManager();
        ds.setSession(getSession());
        ds.setForIdMandat(exercice.getIdMandat());
        ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_BILAN);
        if (isPTRA != null && isPTRA.equals("true")) {
            ds.setPtra8Aor8B("8B");
        }
        int count = 0;
        int nbrComptes = 0;
        ArrayList<CGMontantHelper> listResultAnnonces8B = new ArrayList<CGMontantHelper>();

        statement = ds.cursorOpen(getTransaction());
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantComptesBilan(getTransaction(), getSession(),
                    extendedCompteOfas, exercice.getIdExerciceComptable(), periode.getIdPeriodeComptable(),
                    !isComptaDefinitive().booleanValue());

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if ((result.passif != null) || (result.actif != null)) {
                result.idExterne = extendedCompteOfas.getIdExterne();
                listResultAnnonces8B.add(result);
            }

        }

        ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_ADMINISTRATION);

        statement = ds.cursorOpen(getTransaction());
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantComptesAdministration(getTransaction(),
                    getSession(), extendedCompteOfas, exercice.getIdExerciceComptable(),
                    periode.getIdPeriodeComptable(), !isComptaDefinitive().booleanValue());
            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if ((result.passif != null) || (result.actif != null)) {
                result.idExterne = extendedCompteOfas.getIdExterne();
                listResultAnnonces8B.add(result);
            }

        }
        ds.cursorClose(statement);

        if (isAnnonceXML) {
            xmlService.construireAnnonces8B(listResultAnnonces8B);
        } else {
            construireAnnonces8B(count, nbrComptes, listResultAnnonces8B);
        }
    }

    /**
     * Method preparerEnvoiAnnoncesOFAS. Idem que les annonces de la balance des mvts annuelles, en ne prenant que les
     * comptes de bilan et administration du secteur 9. Les annonces à envoyer sont des 8B, mais générée comme des 8E,
     * cf. CGApplication.getAnnonceOfas
     *
     * @return List
     * @throws Exception
     */
    protected List<CGMetaAnnonceAttributs> preparerEnvoiAnnoncesOFAS() throws Exception {
        if (exercice.getMandat().isEstMandatConsolidation().booleanValue()) {
            return preparerEnvoiAnnoncesOFASForConsolidation();
        } else {
            return preparerEnvoiAnnoncesOFASForAVS();
        }
    }

    private List<CGMetaAnnonceAttributs> preparerEnvoiAnnoncesOFASForAVS() throws Exception {
        // Parse la liste des comptes OFAS pour le mandat donné
        BStatement statement = null;
        CGExtendedCompteOfas extendedCompteOfas = null;
        CGExtendedCompteOfasManager ds = new CGExtendedCompteOfasManager();
        ds.setSession(getSession());
        ds.setForIdMandat(exercice.getIdMandat());
        ds.setTypeRapport(CGExtendedCompteOfasManager.RAPPORT_ANNONCES_OFAS);

        int count = 0;
        int nbrComptes = 0;
        ArrayList<CGMontantHelper> listResultAnnonces8B = new ArrayList<CGMontantHelper>();

        statement = ds.cursorOpen(getTransaction());
        while ((extendedCompteOfas = (CGExtendedCompteOfas) ds.cursorReadNext(statement)) != null) {

            CGMontantHelper result = traitementReleveAvsHelper.getMontantComptesBilan(getTransaction(), getSession(),
                    extendedCompteOfas, exercice.getIdExerciceComptable(), periode.getIdPeriodeComptable(),
                    !isComptaDefinitive().booleanValue());

            // Traitement des annonces
            // Seul les annonces avec des montants !=0 sont à envoyer à la CDC
            if ((result.passif != null) || (result.actif != null)) {
                result.idExterne = extendedCompteOfas.getIdExterne();
                listResultAnnonces8B.add(result);
            }

        }

        construireAnnonces8B(count, nbrComptes, listResultAnnonces8B);

        return listAnnonces;
    }

    protected List<CGMetaAnnonceAttributs> preparerEnvoiAnnoncesOFASForConsolidation() throws Exception {
        ArrayList<CGMontantHelper> listResultAnnonces8B = new ArrayList<CGMontantHelper>();

        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession(getSession());
        manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());
        manager.setForAnnoncesOfas(true);
        manager.setOrderBy(CGPlanComptableViewBean.FIELD_IDEXTERNE);

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        int count = 0;
        int nbrComptes = 0;

        for (int i = 0; i < manager.size(); i++) {
            CGPlanComptableViewBean entity = (CGPlanComptableViewBean) manager.get(i);

            // On ne prend pas en compte les comptes avec un solde à zéro.
            if (!JadeStringUtil.isDecimalEmpty(entity.getSolde())) {
                CGMontantHelper result = new CGMontantHelper();

                if (entity.getIdGenre().equals(CGCompte.CS_GENRE_ACTIF)
                        || entity.getIdGenre().equals(CGCompte.CS_GENRE_CHARGE)) {
                    result.actif = new FWCurrency(entity.getSolde());
                } else if (entity.getIdGenre().equals(CGCompte.CS_GENRE_PASSIF)
                        || entity.getIdGenre().equals(CGCompte.CS_GENRE_PRODUIT)) {
                    result.passif = new FWCurrency(entity.getSolde());
                } else {
                    if (new FWCurrency(entity.getSolde()).compareTo(new FWCurrency(0)) == 1) {
                        result.actif = new FWCurrency(entity.getSolde());
                    } else if (new FWCurrency(entity.getSolde()).compareTo(new FWCurrency(0)) == -1) {
                        result.passif = new FWCurrency(entity.getSolde());
                    }
                }

                // Traitement des annonces
                // Seul les annonces avec des montants !=0 sont à envoyer à la
                // CDC
                if ((result.passif != null) || (result.actif != null)) {
                    result.idExterne = entity.getIdExterne();
                    listResultAnnonces8B.add(result);
                }
            }
        }

        construireAnnonces8B(count, nbrComptes, listResultAnnonces8B);

        return listAnnonces;
    }

    /**
     * Method registerAnnoncesOfasToMail. Ajoute un fichier attaché au mail, contenant les annonces OFAS à envoyer à la
     * centrale.
     *
     * @throws Exception
     */
    private void registerAnnoncesOfasToMail() throws Exception {

        BufferedWriter bw = null;
        FileOutputStream fo = null;

        try {
            if (bouclement.isBouclementAnnuelAVS().booleanValue()) {
                List metaAnnoncesOFAS = preparerEnvoiAnnoncesOFAS();
                List annoncesOFAS = getAnnoncesOFAS(remoteTransaction, remoteSession, metaAnnoncesOFAS, application);
                Iterator iter = annoncesOFAS.iterator();

                File file = File.createTempFile("DATA", "");
                file.deleteOnExit();
                fo = new FileOutputStream(file);
                bw = new BufferedWriter(new OutputStreamWriter(fo));

                bw.write(getLotHeader((BSession) remoteSession));

                int count = 0;
                while (iter.hasNext()) {
                    String element = (String) iter.next();
                    bw.write(element);
                    count++;
                }

                bw.write(getLotFooter((BSession) remoteSession, count));

                this.registerAttachedDocument(file.getAbsolutePath());
            } else {
                this.error("BOUCLEMENT_NON_ANNUEL");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                bw.close();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void rollbackRemoteTransaction(String message) {
        // Logger l'erreur
        getMemoryLog().logMessage(message, FWMessage.ERREUR, this.getClass().getName());
        // Logger les messages d'erreur de la transaction
        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
        try {
            remoteTransaction.rollback();
            remoteTransaction.closeTransaction();
            remoteTransaction = null;

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            e.printStackTrace();
        }
    }

    /**
     * Sets the reqComptabilite.
     *
     * @param reqComptabilite The reqComptabilite to set
     */
    public void setIdComptabilite(String idComptabilite) {
        this.idComptabilite = idComptabilite;
        if (CodeSystem.CS_PROVISOIRE.equals(idComptabilite)) {
            setIsComptaDefinitive(new Boolean(false));
        } else {
            setIsComptaDefinitive(new Boolean(true));
        }
    }

    public void setIdPeriodeComptable(String newidPeriodeComptable) {
        idPeriodeComptable = newidPeriodeComptable;
    }

    /**
     * Sets the isComptaDefinitive.
     *
     * @param isComptaDefinitive The isComptaDefinitive to set
     */
    public void setIsComptaDefinitive(Boolean isComptaDefinitive) {
        this.isComptaDefinitive = isComptaDefinitive;
    }

    /**
     * Sets the isEnvoyerAnnoncesOfas.
     *
     * @param isEnvoyerAnnoncesOfas The isEnvoyerAnnoncesOfas to set
     */
    public void setIsEnvoyerAnnoncesOfas(Boolean isEnvoyerAnnoncesOfas) {
        this.isEnvoyerAnnoncesOfas = isEnvoyerAnnoncesOfas;
    }

    /**
     * Si les annonces annuelles contiennent des montants négatifs, l'envois ne pourra pas être effectué.
     *
     * @throws Exception
     */
    private boolean testAnnoncesBalanceAnnuelle() throws Exception {
        if (bouclement.isBouclementAnnuelAVS().booleanValue()) {
            CGExtendedCompteOfasManager manager = initAnnoncesBalanceAnnuelleManager();
            // Bug 6296
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                CGExtendedCompteOfas extendedCompteOfas = (CGExtendedCompteOfas) manager.get(i);

                CGMontantHelper result = traitementReleveAvsHelper.getMontantMvtAnnuel(getTransaction(), getSession(),
                        extendedCompteOfas, exercice.getIdMandat(), exercice.getIdExerciceComptable(),
                        periode.getIdPeriodeComptable(), !isComptaDefinitive().booleanValue());

                if (((result.passif != null) && result.passif.isNegative())
                        || ((result.actif != null) && result.actif.isNegative())) {
                    this._addError(getTransaction(), getSession().getLabel("ANNONCE_BOUCLEMENT_ANNUELLE_IMPOSSIBLE"));
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void warn(String msg) {
        this.warn(msg, "");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 14:35:53)
     *
     * @param msg String
     */
    protected void warn(String codeLabel, String msg) {
        getMemoryLog().logMessage(getSession().getLabel(CGPeriodeComptableEnvoiAnnonces.labelPrefix + codeLabel) + msg,
                FWMessage.AVERTISSEMENT, getSession().getLabel("GLOBAL_PERIODE") + " N°" + getIdPeriodeComptable());
    }

}

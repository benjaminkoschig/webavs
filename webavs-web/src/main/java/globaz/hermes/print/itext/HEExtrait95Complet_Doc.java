package globaz.hermes.print.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JAException;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEOutputAnnonceException;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.pavo.application.CIApplication;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author Alain Dominé To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class HEExtrait95Complet_Doc extends HEExtraitAnnonce_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        HEExtrait95Complet_Doc process = null;
        // String user = "globazd";
        // String pwd = "ssiiadm";
        boolean deleteOnExit = true;
        /*
         * boolean isNotBatch = true;
         */
        try {
            if (args.length != 3) {
                throw new Exception(
                        "Usage : java globaz.hermes.print.itext.HEExtrait95Complet_Doc <refUnique> <uid> <pwd>");
            }
            BSession session = new BSession("HERMES");
            Jade.getInstance().setHomeDir(((HEApplication) session.getApplication()).getProperty("zas.home.dir"));
            System.out.println("Home dir " + Jade.getInstance().getHomeDir());
            session.connect(args[1], args[2]);
            /** FIN JADE * */
            process = new HEExtrait95Complet_Doc(session);
            // process.setReferenceUnique(args[0]);
            process.setDeleteOnExit(deleteOnExit);
            process.setEMailAddress("ald@globaz.ch");
            process.executeProcess();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            System.exit(0);
        }
    }

    public String reference95 = "";

    public HEExtrait95Complet_Doc() throws FWIException {
        super();
    }

    /**
     * Constructor for HEExtrait95Complet_Doc.
     * 
     * @param session
     * @throws FWIException
     */
    public HEExtrait95Complet_Doc(BSession session) throws FWIException {
        super(session);
    }

    /**
     * Constructor for HEExtrait95Complet_Doc.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public HEExtrait95Complet_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * Constructor for HEExtrait95Complet_Doc.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public HEExtrait95Complet_Doc(FWProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public boolean addCiAdd(HEOutputAnnonceViewBean entity, String userId, String reference, String motif) {
        try {
            String key = getKey(entity, reference, motif);
            String numeroAssure = entity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
            // String annee =
            // entity.getField(HEAnnoncesViewBean.ANNEES_COTISATIONS_AAAA);
            String codeApplication = _getCodeApplication(entity);
            String codeEnregistrement = _getCodeEnregistrement(entity);
            String code = _getCode1ou2(entity);
            // String caisse = entity.getNumeroCaisse();
            // String motif = entity.getMotif();
            if ((m_container = (HEExtraitAnnonceAssureBean) m_mainContainer.get(key)) == null) {
                if (isSpecialMotif(entity)) {
                    m_container = (HEExtraitAnnonceAssureBean) m_mainContainer.get(reference + "_" + numeroAssure
                            + "_00");
                    if (m_container != null) {
                        m_container = (HEExtraitAnnonceAssureBean) m_container.clone();
                        m_container.clearExtrait();
                    }
                }
                if (m_container == null) {
                    m_container = new HEExtraitAnnonceAssureBean(); // Cette
                    // clef
                    // n'existe
                    // pas
                }
            }
            if (codeApplication.equals("38")) {
                if (code.equals("1")) { // Code type 1
                    _currExtrait = new HEExtraitAnnonceBean(entity, userId);
                } else { // Code type 2
                    _currExtrait.setTextRevenu(entity.getField(IHEAnnoncesViewBean.PARTIE_INFORMATION));
                }
            } else if (codeApplication.equals("39")) {
                m_container.setReferenceInterne(entity
                        .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
                m_container.setNumeroCaisseCI(StringUtils.formatCaisseAgence(
                        entity.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI),
                        entity.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI)));
                if (codeEnregistrement.equals("001")) {
                    if (JadeStringUtil.toIntMIN(entity.getField(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI)) < 1) {
                        _currExtrait = new HEExtraitAnnonceBean();
                        _currExtrait.setNumeroCaisse(entity.getNumeroCaisse());
                    }
                } else {
                    m_container.setNomPrenom(entity.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
                    // MODIF NNSS
                    m_container.setNumeroAvs(numeroAssure, entity.getNumeroAvsNNSS());
                    m_container.setMotif(entity.getMotif());
                    m_container.setUtilisateur(getUtilisateur());
                }
            } else {
                m_container.setHeader(entity);
                _currExtrait = null;
            }
            // Sauvegarde des modifications
            if (_currExtrait != null) {
                m_container.setExtrait(_currExtrait);
            }
            if (m_container != null) {
                m_mainContainer.put(key, m_container);
            }
            m_lastCodeApplication = getSignature(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() {
        try {
            // si la liste des références est données sous une forme de vector
            // dans le cas ou PAVO désire imprimer des 95
            setDocumentRoot(CIApplication.APPLICATION_PAVO_REP);
            Vector docList = new Vector();
            for (int i = 0; i < getReferenceUniqueVector().size(); i++) {
                imprimerReference(String.valueOf(referenceUniqueVectorList.get(i)), getUserId());
                imprimerCIAdditionnel(String.valueOf(referenceUniqueVectorList.get(i)), getUserId());
                docList.addAll((new TreeMap(m_mainContainer)).values());
            }
            // Make ready for the first document
            _docIterator = docList.iterator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method getKey.
     * 
     * @param entity
     * @param reference
     * @param motif
     * @return String
     */
    private String getKey(HEOutputAnnonceViewBean entity, String reference, String motif)
            throws HEOutputAnnonceException {
        String numeroAVS = entity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
        String caisse = entity.getNumeroCaisse();
        String theKey = reference + "_" + numeroAVS + "_";
        if (isSpecialMotif(motif)) {
            return theKey + motif + "_" + caisse;
        } else {
            return theKey + "00";
        }
    }

    // /**
    // * Returns the reference95.
    // * @return String
    // */
    // public String getReference95() {
    // return reference95;
    // }
    // /**
    // * Sets the reference95.
    // * @param reference95 The reference95 to set
    // */
    // public void setReference95(String reference95) {
    // this.reference95 = reference95;
    // }
    /**
     * @see globaz.hermes.print.itext.HEExtraitAnnonce_Doc#getRefContainer(HEOutputAnnonceViewBean)
     */
    protected String getRefContainer(HEOutputAnnonceViewBean entity) throws Exception {
        return entity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE) + "_00";
    }

    /**
     * Method imprimerCIAdditionnel.
     * 
     * @param string
     * @param string1
     */
    private void imprimerCIAdditionnel(String reference, String userId) throws Exception {
        // TO DO : ALD
        HEOutputAnnonceListViewBean manager = null;
        // m_mainContainer = new Hashtable();
        // m_container = null;
        // _currExtrait = null;
        try {
            manager = new HEOutputAnnonceListViewBean();
            manager.setSession(getSession());
            manager.setForRefUnique(reference);
            // Information de base
            manager.setOrder("RNIANN");
            // recherche la date de l'ordre du 95
            manager.setLikeEnregistrement("1101");
            manager.find(getTransaction());
            if (manager.size() > 0) {
                HEOutputAnnonceViewBean arcBase = (HEOutputAnnonceViewBean) manager.getFirstEntity();
                String dateOrdre = arcBase.getDateAnnonceJMA();
                String motif = arcBase.getMotif();
                // On recherche d'abord les informations de base -> 25
                // ainsi on aura les numéros avs pour lesquelles on peut
                // recevoir un ci additionnel
                // et on aura la date à laquelle le 95 a été réalisé
                manager.setLikeEnregistrement("25");
                // on spécificie si les données sont dans l'archivage
                manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                HEOutputAnnonceViewBean entity = (HEOutputAnnonceViewBean) manager.getFirstEntity();
                Vector serie3839 = new Vector();
                Vector numAvsTraite = new Vector();
                // on a trouve une arc 25
                for (int i = 0; i < manager.size(); i++) {
                    entity = (HEOutputAnnonceViewBean) manager.getEntity(i);
                    if (!numAvsTraite.contains(entity.getNumeroAVS())) {
                        numAvsTraite.add(entity.getNumeroAVS());
                        manager.setLikeEnregistrement("3");
                        manager.setForRefUnique("");
                        manager.setFromDate(dateOrdre);
                        manager.setForNumeroAVS(entity.getNumeroAVS());
                        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                        HEOutputAnnonceViewBean crt = null;
                        boolean isAnAdditionnal = false;
                        for (int j = 0; j < manager.size(); j++) {
                            // on doit trouver une liste de série de 38-39
                            // les 38 - 39 sont toujours dans l'ordre
                            crt = (HEOutputAnnonceViewBean) manager.getEntity(j);
                            if (crt.getChampEnregistrement().startsWith("38001")) {
                                if (!crt.getField(IHEAnnoncesViewBean.CHIFFRE_CLE_GENRE_COTISATIONS).equals("8")) {
                                    serie3839.clear();
                                    isAnAdditionnal = false;
                                    break;
                                }
                            }
                            if (crt.getChampEnregistrement().startsWith("39001")) {
                                if (crt.getField(IHEAnnoncesViewBean.CI_ADDITIONNEL).equals("1")) {
                                    isAnAdditionnal = true;
                                }
                            }
                            serie3839.add(crt);
                            if (crt.getChampEnregistrement().startsWith("39002")) {
                                // voilà on a un ci complet, est-ce un
                                // additionnel ?
                                if (isAnAdditionnal) {
                                    // oui donc on ajoute les écritures
                                    for (int k = 0; k < serie3839.size(); k++) {
                                        addCiAdd((HEOutputAnnonceViewBean) serie3839.get(k), userId, reference, motif);
                                    }
                                }
                                serie3839.clear();
                                isAnAdditionnal = false;
                            }
                        }
                    }
                    // test si le ci est un additionnel --> si oui on l'ajoute
                    // si non on vide le vecteur
                }
            }
        } catch (Exception e) {
            super._addError("Le document avec la reference '" + reference + "' n'a pas pu être créer : "
                    + e.getMessage());
            super.setMsgType(super.ERROR);
            super.setMessage("Le document avec la reference '" + reference + "' n'a pas pu être créer : "
                    + e.getMessage());
            throw new JAException("Le document avec la reference '" + reference + "' n'a pas pu être créer : "
                    + e.getMessage());
        }
    }

    /**
     * Method isSpecialMotif.
     * 
     * @param motif
     * @return boolean
     */
    private boolean isSpecialMotif(String motif) {
        return (Arrays.asList(motifs).contains(motif));
    }
}

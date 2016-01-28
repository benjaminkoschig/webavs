package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (14.11.2002 08:13:13)
 * 
 * @author: ado
 */
public class HELienannonceViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_MESSAGE_GROUPE = "HEMSG";
    /** Codes message */
    public static final String CS_MESSAGE_TYPECODE = "11100005";
    //
    private FWParametersSystemCode csIdMessage = null;
    // code systeme
    private FWParametersSystemCode csIdMotif = null;
    private FWParametersSystemCodeManager csIdMotifManager = null;
    /** Fichier HELIANP */
    /** (RIILIA) */
    private String idLienAnnonce = new String();
    /** (RITMES) */
    private String idMessage = new String();
    /** (RHIMET) */
    private String idMethode = new String();
    /** (RITMOT) */
    private String idMotif = new String();
    /** (REIPAE) */
    private String idParametrageAnnonce = new String();
    /** méthode entity */
    private HEMethodeViewBean methode;
    //
    /** les méthodes */
    private HEMethodeListViewBean methodeManager;
    /** (HEP_REIPAE) */
    private String par_idParametrageAnnonce = new String();
    /** parametrage annonce */
    private HEParametrageannonce paramAnnonce;
    /** parametrage annonce aller */
    private HEParametrageannonce paramAnnonceAller;
    //
    private HEParametrageannonceManager paramAnnonceManager;

    // code systeme
    /**
     * Commentaire relatif au constructeur HELienannonceViewBean
     */
    public HELienannonceViewBean() {
        super();
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
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        // chargement du CS
        getCsIdMotif().setIdTypeCode(HEMotifsViewBean.TYPE_CODE);
        getCsIdMotif().setGroupe(HEMotifsViewBean.GROUPE);
        getCsIdMotif().setSession(getSession());
        getCsIdMotif().getCode(getIdMotif());
        //
        // chargement du CS
        getCsIdMessage().setIdTypeCode(HELienannonceViewBean.CS_MESSAGE_TYPECODE);
        getCsIdMessage().setGroupe(HELienannonceViewBean.CS_MESSAGE_GROUPE);
        getCsIdMessage().setSession(getSession());
        getCsIdMessage().getCode(getIdMessage());
        //
        // chargement de tout les motifs
        getCsIdMotifManager().setForIdTypeCode(HEMotifsViewBean.TYPE_CODE);
        getCsIdMotifManager().setForIdGroupe(HEMotifsViewBean.GROUPE);
        getCsIdMotifManager().setSession(getSession());
        getCsIdMotifManager().find();
        // chargement de l'annonce retour
        paramAnnonce = new HEParametrageannonce();
        paramAnnonce.setSession(getSession());
        paramAnnonce.setIdParametrageAnnonce(getPar_idParametrageAnnonce());
        paramAnnonce.retrieve();
        // chargement de toutes les annonces
        getParamAnnonceManager().setSession(getSession());
        // getParamAnnonceManager().setForIdParametrageAnnonce(getPar_idParametrageAnnonce());
        getParamAnnonceManager().find();
        //
        // y'a une méthode ou non ?
        methode = new HEMethodeViewBean();
        methode.setSession(getSession());
        methode.setIdMethode(getIdMethode());
        methode.retrieve(transaction);
        //
        methodeManager = new HEMethodeListViewBean();
        methodeManager.setSession(getSession());
        // methodeManager.setForIdMethode(getIdMethode());
        methodeManager.find(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HELIANP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLienAnnonce = statement.dbReadNumeric("RIILIA", 0);
        idMethode = statement.dbReadNumeric("RHIMET", 0);
        idParametrageAnnonce = statement.dbReadNumeric("REIPAE", 0);
        par_idParametrageAnnonce = statement.dbReadNumeric("HEP_REIPAE", 0);
        idMotif = statement.dbReadNumeric("RITMOT", 0);
        idMessage = statement.dbReadNumeric("RITMES", 0);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("RIILIA", _dbWriteNumeric(statement.getTransaction(), getIdLienAnnonce(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement
                .writeField("RIILIA", _dbWriteNumeric(statement.getTransaction(), getIdLienAnnonce(), "idLienAnnonce"));
        statement.writeField("RHIMET", _dbWriteNumeric(statement.getTransaction(), getIdMethode(), "idMethode"));
        statement.writeField("REIPAE",
                _dbWriteNumeric(statement.getTransaction(), getIdParametrageAnnonce(), "idParametrageAnnonce"));
        statement.writeField("HEP_REIPAE",
                _dbWriteNumeric(statement.getTransaction(), getPar_idParametrageAnnonce(), "par_idParametrageAnnonce"));
        statement.writeField("RITMOT", _dbWriteNumeric(statement.getTransaction(), getIdMotif(), "idMotif"));
        statement.writeField("RITMES", _dbWriteNumeric(statement.getTransaction(), getIdMessage(), "idMessage"));
    }

    //
    public String getAnnonceAller(String id) throws Exception {
        // concaténation Code Application + Code enregistrement début + Code
        // Enregistrement fin + Libellé parametrageAnnonce
        String annonceAller = "";
        paramAnnonceAller = new HEParametrageannonce();
        paramAnnonceAller.setSession(getSession());
        paramAnnonceAller.setIdParametrageAnnonce(id);
        paramAnnonceAller.retrieve();
        // Le code application
        annonceAller += paramAnnonceAller.getCsCodeApplication().getCurrentCodeUtilisateur().getCodeUtilisateur() + " ";
        // Code enregistrement début
        annonceAller += paramAnnonceAller.getCodeEnregistrementDebut() + " ";
        // Code Enregistrement fin
        annonceAller += paramAnnonceAller.getCodeEnregistrementFin() + " ";
        // Libellé
        annonceAller += "- " + paramAnnonceAller.getCsCodeApplication().getCurrentCodeUtilisateur().getLibelle();
        return annonceAller;
    }

    public String getAnnonceRetour() throws Exception {
        // concaténation Code Application + Code enregistrement début + Code
        // Enregistrement fin + Libellé parametrageAnnonce
        String annonceRetour = "";
        // chargé dans le _afterRetrive
        // Le code application
        annonceRetour += paramAnnonce.getCsCodeApplication().getCurrentCodeUtilisateur().getCodeUtilisateur() + " ";
        // Code enregistrement début
        annonceRetour += paramAnnonce.getCodeEnregistrementDebut() + " ";
        // Code Enregistrement fin
        annonceRetour += paramAnnonce.getCodeEnregistrementFin() + " ";
        // Libellé
        annonceRetour += "- " + paramAnnonce.getCsCodeApplication().getCurrentCodeUtilisateur().getLibelle();
        return annonceRetour;
    }

    public String getAnnonceRetour(HEParametrageannonce paramEntity) throws Exception {
        // concaténation Code Application + Code enregistrement début + Code
        // Enregistrement fin + Libellé parametrageAnnonce
        String annonceRetour = "";
        // chargé dans le _afterRetrive
        // Le code application
        annonceRetour += paramEntity.getCsCodeApplication().getCurrentCodeUtilisateur().getCodeUtilisateur() + " ";
        // Code enregistrement début
        annonceRetour += paramEntity.getCodeEnregistrementDebut() + " ";
        // Code Enregistrement fin
        annonceRetour += paramEntity.getCodeEnregistrementFin() + " ";
        // Libellé
        annonceRetour += "- " + paramEntity.getCsCodeApplication().getCurrentCodeUtilisateur().getLibelle();
        return annonceRetour;
    }

    public Vector getAnnonceRetourListe() {
        Vector vList = new Vector();
        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);
        try {
            for (int i = 0; i < paramAnnonceManager.size(); i++) {
                list = new String[2];
                HEParametrageannonce entity = (HEParametrageannonce) paramAnnonceManager.getEntity(i);
                list[0] = entity.getIdParametrageAnnonce();
                list[1] = this.getAnnonceRetour(entity);
                vList.add(list);
            }
        } catch (Exception e) {
            // si probleme, retourne list vide.
            e.printStackTrace();
        }
        return vList;
    }

    public FWParametersSystemCode getCsIdMessage() {
        if (csIdMessage == null) {
            // liste pas encore chargee, on la charge
            csIdMessage = new FWParametersSystemCode();
            csIdMessage.getCode(getIdMessage());
        }
        return csIdMessage;
    }

    public FWParametersSystemCode getCsIdMotif() {
        if (csIdMotif == null) {
            // liste pas encore chargee, on la charge
            csIdMotif = new FWParametersSystemCode();
        }
        return csIdMotif;
    }

    public String getCsIdMotifCodeUtiLibelle() {
        return getCsIdMotif().getCurrentCodeUtilisateur().getCodeUtiLib();
    }

    public Vector getCsIdMotifCUandLibList() {
        Vector vList = new Vector();
        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);
        try {
            for (int i = 0; i < csIdMotifManager.size(); i++) {
                list = new String[2];
                FWParametersSystemCode entity = (FWParametersSystemCode) csIdMotifManager.getEntity(i);
                list[0] = entity.getId();
                list[1] = entity.getCurrentCodeUtilisateur().getCodeUtiLib();
                vList.add(list);
            }
        } catch (Exception e) {
            // si probleme, retourne list vide.
            e.printStackTrace();
        }
        return vList;
    }

    public String getCsIdMotifLibelle() {
        return getCsIdMotif().getCurrentCodeUtilisateur().getLibelle();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 16:57:12)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public globaz.globall.parameters.FWParametersSystemCodeManager getCsIdMotifManager() {
        if (csIdMotifManager == null) {
            csIdMotifManager = new FWParametersSystemCodeManager();
        }
        return csIdMotifManager;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdLienAnnonce() {
        return idLienAnnonce;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public String getIdMethode() {
        return idMethode;
    }

    public String getIdMotif() {
        return idMotif;
    }

    public String getIdParametrageAnnonce() {
        return idParametrageAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.11.2002 08:59:13)
     * 
     * @return globaz.hermes.db.parametrage.HEMethodeViewBean
     */
    public HEMethodeListViewBean getMethode() {
        return methodeManager;
    }

    public String getMethodeALancer() {
        // si l'enregistrement a été lu
        if (methode.isNew()) { // enregistrement non lu
            return "";
        }
        // enregistrement lu
        // TODO : implémenter les codes système oui et non
        return "Oui";
    }

    public Vector getMethodeListe() {
        Vector vList = new Vector();
        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);
        try {
            for (int i = 0; i < methodeManager.size(); i++) {
                list = new String[2];
                list[0] = ((HEMethodeViewBean) methodeManager.getEntity(i)).getIdMethode();
                list[1] = ((HEMethodeViewBean) methodeManager.getEntity(i)).getCULibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            // si probleme, retourne list vide.
            e.printStackTrace();
        }
        return vList;
    }

    public String getPar_idParametrageAnnonce() {
        return par_idParametrageAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 11:38:30)
     * 
     * @return globaz.hermes.db.parametrage.HEParametrageannonceManager
     */
    public HEParametrageannonceManager getParamAnnonceManager() {
        if (paramAnnonceManager == null) {
            paramAnnonceManager = new HEParametrageannonceManager();
        }
        return paramAnnonceManager;
    }

    public String isMessageAAfficher() {
        if (csIdMessage.isNew()) {
            return "";
        } else {
            return "Oui";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 16:57:12)
     * 
     * @param newCsIdMotifManager
     *            globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public void setCsIdMotifManager(globaz.globall.parameters.FWParametersSystemCodeManager newCsIdMotifManager) {
        csIdMotifManager = newCsIdMotifManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdLienAnnonce(String newIdLienAnnonce) {
        idLienAnnonce = newIdLienAnnonce;
    }

    public void setIdMessage(String newIdMessage) {
        idMessage = newIdMessage;
    }

    public void setIdMethode(String newIdMethode) {
        idMethode = newIdMethode;
    }

    public void setIdMotif(String newIdMotif) {
        idMotif = newIdMotif;
    }

    public void setIdParametrageAnnonce(String newIdParametrageAnnonce) {
        idParametrageAnnonce = newIdParametrageAnnonce;
    }

    public void setPar_idParametrageAnnonce(String newPar_idParametrageAnnonce) {
        par_idParametrageAnnonce = newPar_idParametrageAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.11.2002 11:38:30)
     * 
     * @param newParamAnnonceManager
     *            globaz.hermes.db.parametrage.HEParametrageannonceManager
     */
    public void setParamAnnonceManager(HEParametrageannonceManager newParamAnnonceManager) {
        paramAnnonceManager = newParamAnnonceManager;
    }
}

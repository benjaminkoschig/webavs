package globaz.hermes.db.gestion;

//
import globaz.commons.nss.NSUtil;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.db.parametrage.HEChampannonceViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationListViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonce;
import globaz.hermes.db.parametrage.HEParametrageannonceManager;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HENNSSUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Insérez la description du type ici. Date de création : (02.12.2002 15:47:24)
 * 
 * @author: ado
 */
public class HEOutputAnnonceViewBean extends HEAnnoncesViewBean implements IHEOutputAnnonce {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Lance l'application.
     * 
     * @param args
     *            un tableau d'arguments de ligne de commande
     */
    public static void main(java.lang.String[] args) {
        try {
            BSession session = new BSession("HERMES");
            session.setIdLangueISO("FR");
            session.connect("ssii", "ssiiadm");
            HEOutputAnnonceViewBean annonce = new HEOutputAnnonceViewBean();
            annonce.setSession(session);
            annonce.setIdAnnonce("9319");
            annonce.retrieve();
            System.out.println(annonce.getInputTable());
            // //////////////////////
            annonce.setIdAnnonce("9320");
            annonce.retrieve();
            System.out.println(annonce.getInputTable());
            // //////////////////////
            // //////////////////////
        } catch (HEOutputAnnonceException iae) {
            System.err.println(iae);
            iae.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    private String codeAppLibelle = "";

    /**
     * Commentaire relatif au constructeur HEOutputAnnonce.
     */
    public HEOutputAnnonceViewBean() {
        super();
    }

    public HEOutputAnnonceViewBean(boolean isArchivage) {
        super();
        this.setArchivage(isArchivage);
    }

    /**
     * Commentaire relatif au constructeur HEOutputAnnonce.
     */
    public HEOutputAnnonceViewBean(BSession session) {
        super();
        setSession(session);
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
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception, HEOutputAnnonceException {
        // on chope le type d'annonce
        StringBuffer annonce = new StringBuffer(getChampEnregistrement());
        // force la completion avec des ' ' pour avoir un enregistrement de 120
        char[] fillCar = new char[120 - annonce.length()];
        java.util.Arrays.fill(fillCar, ' ');
        //
        annonce.append(fillCar);
        //
        String codeApplication = annonce.substring(0, 2);
        // TODO : Cas où le CODE ENREGISTREMENT est sur 3 chiffres
        // put(CODE_ENREGISTREMENT, annonce.substring(2, 4));
        // on a le code application et le code enregistrement, on doit trouver
        // le reste...
        // Je chope le code application
        HECodeapplicationListViewBean caM = new HECodeapplicationListViewBean();
        caM.setForCodeUtilisateur(codeApplication);
        caM.setSession(getSession());
        caM.find(transaction);
        if (caM.size() == 0) {
            throw new HEInputAnnonceException(
                    "Ce code application n'existe pas ou n'est pas parametré (Code application = " + codeApplication
                            + ")");
        }
        HECodeapplicationViewBean codeApp = (HECodeapplicationViewBean) caM.getFirstEntity();
        // cas particulier pour les 38 qui sont identifiés différemment
        if ((caM.size() == 2) && getChampEnregistrement().startsWith("38")) {
            String code = getChampEnregistrement().substring(71, 72);
            if (code.equals("1") && codeApp.getIdCode().equals("111040")) { // pas
                // bon
                //
                // codeApp = (HECodeapplicationViewBean) caM.getEntity(1);
                /*
                 * codeApp.setIdCode("111011"); codeApp.retrieve(transaction);
                 */
                for (int c = 0; c < caM.size(); c++) {
                    codeApp = (HECodeapplicationViewBean) caM.getEntity(c);
                    if (codeApp.getIdCode().equals("111011")) {
                        break;
                    }
                }
            } else if (code.equals("2") && codeApp.getIdCode().equals("111011")) {
                //
                // codeApp = (HECodeapplicationViewBean) caM.getFirstEntity();
                /*
                 * codeApp.setIdCode("111040"); codeApp.retrieve(transaction);
                 */
                for (int c = 0; c < caM.size(); c++) {
                    codeApp = (HECodeapplicationViewBean) caM.getEntity(c);
                    if (codeApp.getIdCode().equals("111040")) {
                        break;
                    }
                }
            }
        }
        codeAppLibelle = codeApp.getCurrentCodeUtilisateur().getCodeUtiLib();
        // je cherche le code enregistrement pour avoir la taille de celui-ci (2
        // ou 3 caractères)
        // - NB : pour un code application, tout les codes enregsitrement sont
        // soit sur 2 soit sur 3
        // mais on mélange pas.
        //
        // Je regarde s'il existe un parametrage pour ce code application/code
        // enregistrement (HEPAREP)
        HEParametrageannonceManager paramManager = new HEParametrageannonceManager();
        paramManager.setSession(getSession());
        // je chope n'importe quel parametrage pour ce code application
        // - récup du code application ID
        paramManager.setForIdCSCodeApplication(codeApp.getIdCode());
        paramManager.find(transaction);
        // au cas où
        if (paramManager.size() == 0) {
            throw new HEInputAnnonceException(
                    "Ce code application n'existe pas ou n'est pas parametré (Code application = " + codeApplication
                            + ")");
        }
        paramAnnonce = (HEParametrageannonce) paramManager.getEntity(0);
        // je récupère l'idParametrageAnnonce et je cherche un champ
        champAnnonceList.setSession(getSession());
        // l'idParametrageAnnonce quelconque
        champAnnonceList.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
        // le champ code enregistrement
        champAnnonceList.setForIdChamp("" + IHEAnnoncesViewBean.CODE_ENREGISTREMENT);
        champAnnonceList.find(transaction);
        if (champAnnonceList.size() == 0) {
            throw new HEInputAnnonceException(
                    "Ce code champ annonce n'existe pas ou n'est pas parametré (Code idParametrageAnnonce = "
                            + paramAnnonce.getIdParametrageAnnonce() + " et idChamp = "
                            + IHEAnnoncesViewBean.CODE_ENREGISTREMENT + ")");
        }
        // je récupère la longeur
        HEChampannonceViewBean champAnnonce;
        champAnnonce = (HEChampannonceViewBean) champAnnonceList.getEntity(0);
        int debut = Integer.parseInt(champAnnonce.getDebut()) - 1;
        int fin = debut + Integer.parseInt(champAnnonce.getLongueur());
        String codeEnregistrement = annonce.substring(debut, fin);
        this.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, codeEnregistrement);
        // code enregistrement
        paramManager.setForAfterCodeEnregistrementDebut(codeEnregistrement);
        paramManager.setForBeforeCodeEnregistrementFin(codeEnregistrement);
        paramManager.find(transaction);
        //
        if (paramManager.size() == 0) {
            throw new HEInputAnnonceException(
                    "Ce code enregistrement n'existe pas ou n'est pas parametré (Code enregistrement = "
                            + codeEnregistrement + ")");
        }
        //
        paramAnnonce = (HEParametrageannonce) paramManager.getEntity(0);
        // je chope la liste des chapms pour cette annonce là
        champAnnonceList.setForIdChamp(""); // je veux tout les champs
        champAnnonceList.setForIdParametrageAnnonce(paramAnnonce.getIdParametrageAnnonce());
        champAnnonceList.find(transaction);
        String tmpRefUnique = getRefUnique(); // car la ref unique est scratchée
        // dans le put
        // System.out.println("les champs pour ce type d'annonce sont : ");
        for (int i = 0; i < champAnnonceList.size(); i++) {
            champAnnonce = (HEChampannonceViewBean) champAnnonceList.getEntity(i);
            debut = Integer.parseInt(champAnnonce.getDebut()) - 1;
            fin = debut + Integer.parseInt(champAnnonce.getLongueur());
            // pour le cas où il s'agit d'un numéro AVS, on remplit la table
            // avec
            // sans le signe -, donc soit un numéro avs, soit un NNSS
            if (HEAnnoncesViewBean.isNumeroAVS(champAnnonce.getIdChamp())) {
                String num = annonce.substring(debut, fin);
                if (HENNSSUtils.isNNSSNegatif(num)) {
                    this.put(champAnnonce.getIdChamp(), HENNSSUtils.convertNegatifToNNSS(num));
                } else {
                    // Il s'agit d'un numéro AVS, donc on le met tel quel dans
                    // la map
                    this.put(champAnnonce.getIdChamp(), annonce.substring(debut, fin));
                }
            } else {
                this.put(champAnnonce.getIdChamp(), annonce.substring(debut, fin));
            }
            // je remplis la champsTable
            champsTable.put(champAnnonce.getIdChamp(), ((HEApplication) GlobazSystem.getApplication("HERMES"))
                    .getCsChampsListe(getSession()).getCodeSysteme(champAnnonce.getIdChamp())
                    .getCurrentCodeUtilisateur().getLibelle(), Integer.parseInt(champAnnonce.getLongueur()));
        }
        setRefUnique(tmpRefUnique);
        chargerDonneeNNSS();
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        statement.writeKey("RNIANN", this._dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), ""));
        statement.writeKey("RNREFU",
                this._dbWriteString(statement.getTransaction(), getRefUnique(), "référence unique"));
    }

    private void chargerDonneeNNSS() throws Exception {
        if ("21".equals(getCodeApplication())) {
            HEOutputAnnonceListViewBean list = new HEOutputAnnonceListViewBean();
            list.setSession(getSession());
            list.setForCodeApplication("20");
            // list.setForCodeEnr01Or001(true);
            list.setForRefUnique(getRefUnique());
            list.find();
            for (int i = 0; i < list.size(); i++) {
                HEOutputAnnonceViewBean v = (HEOutputAnnonceViewBean) list.getEntity(i);
                if ("01".equals(v.getCodeEnregistrement())) {
                    if (!JadeStringUtil.isEmpty(v.getField(IHEAnnoncesViewBean.SEXE))) {
                        this.put(IHEAnnoncesViewBean.SEXE, v.getField(IHEAnnoncesViewBean.SEXE));
                    }
                    if (!JadeStringUtil.isEmpty(v.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA))) {
                        this.put(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA,
                                v.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA));
                    }
                }
                if ("02".equals(v.getCodeEnregistrement())) {
                    if (!JadeStringUtil.isEmpty(v.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS))) {
                        this.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS,
                                v.getField(IHEAnnoncesViewBean.CS_NUMERO_ASSURE_13_POSITIONS));
                    }
                }

            }
        }
    }

    @Override
    public void clear() {
    }

    @Override
    public Boolean getAllAnnonceRetour() {
        try {
            HEAttenteRetourListViewBean retourListe = new HEAttenteRetourListViewBean();
            retourListe.setSession(getSession());
            retourListe.setForReferenceUnique(getRefUnique());
            retourListe.wantCallMethodAfter(false);
            retourListe.wantCallMethodAfterFind(false);
            retourListe.find();
            if (retourListe.size() == 0) {
                return new Boolean(false);
            }
            retourListe.setForIdAnnonceRetour("0");
            retourListe.find();
            if (retourListe.size() >= 1) {
                return new Boolean(false);
            } else {
                return new Boolean(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Boolean(false);
        }
    }

    @Override
    public String getChampEnregistrementFromAttr() throws Exception {
        throw new Exception("Méthode non implémentée");
    }

    public int getChampsSize() {
        return champsTable.size();
    }

    public String getCodeAppLibelle() {
        return codeAppLibelle;
    }

    /**
     * Method getCodeApplication. Renvoit le code application en fonction de l'enregistrement, ne fonctionne qu'après un
     * retrieve
     * 
     * @return String le code application sur 2 chiffres (11,20,38...), vide si pas d'enregistrement
     */
    public String getCodeApplication() {
        if (!JAUtil.isStringEmpty(getChampEnregistrement())) {
            return getChampEnregistrement().substring(0, 2);
        } else {
            return "";
        }
    }

    /**
     * Method getCodeEnregistrement. Renvoit le code enregistrement en fonction de l'enregistrement, ne fonctionne
     * qu'après un retrieve
     * 
     * @return String le code enregistrement sur 2 ou 3 chiffres (01,02,003...), vide si pas d'enregistrement
     * @return String
     */
    public String getCodeEnregistrement() {
        if (!JAUtil.isStringEmpty(getChampEnregistrement())) {
            if (!"38".equals(getCodeApplication()) && !"39".equals(getCodeApplication())) {
                return getChampEnregistrement().substring(2, 4);
            } else {
                return getChampEnregistrement().substring(2, 5);
            }
        } else {
            return "";
        }
    }

    @Override
    public Boolean getConfirmed() {
        HEAttenteRetourListViewBean retourListe = new HEAttenteRetourListViewBean();
        retourListe.setSession(getSession());
        retourListe.setForReferenceUnique(getRefUnique());
        retourListe.setForIdAnnonceRetourAttendue("6");
        try {
            retourListe.find();
        } catch (Exception e) {
            e.printStackTrace();
            return new Boolean(false);
        }
        if (retourListe.size() < 1) {
            return new Boolean(false);
        }
        HEAttenteRetourViewBean retour = (HEAttenteRetourViewBean) retourListe.getEntity(0);
        return new Boolean((!retour.getIdAnnonceRetour().equals("0")));
    }

    public String getDateAnnonceJMA() {
        try {
            return DateUtils.convertDate(getDateAnnonce(), DateUtils.AAAAMMJJ, DateUtils.JJMMAAAA_DOTS);
        } catch (Exception e) {
            e.printStackTrace();
            return getDateAnnonce();
        }
    }

    public String getFieldId(int i) {
        return champsTable.keyAt(i);
    }

    public String getFieldLength(int i) throws HEOutputAnnonceException {
        return this.getFieldLength(i, 0);
    }

    public String getFieldLength(int i, int len) throws HEOutputAnnonceException {
        return "" + (this.getFieldValue(i).length() + len);
    }

    public String getFieldLibelle(int i) {
        return champsTable.valueAt(i);
    }

    public String getFieldValue(int i) throws HEOutputAnnonceException {
        if (HEAnnoncesViewBean.isCurrencyField(getFieldId(i)) && this.isRevenuCache(getSession().getUserId())) {
            return getSession().getLabel("HERMES_10001");
        }
        if (HEAnnoncesViewBean.isInformationField(getFieldId(i)) && this.isRevenuCache(getSession().getUserId())) {
            return getSession().getLabel("HERMES_10001");
        } else {
            return this.getField(champsTable.keyAt(i));
        }
    }

    public String getFieldValue(int i, int pos) {
        String key = i + "";
        String valeur = ((String) inputTable.get(key));
        if (HEAnnoncesViewBean.isCustomField(key)) {
            if (valeur.trim().length() == 0) {
                return "";
            }
            valeur = globaz.hermes.utils.StringUtils.padAfterString(valeur, " ", 9);
            switch (pos) {
                case 0: {
                    return valeur.substring(0, 4);
                }
                case 1: {
                    return valeur.substring(4, 8);
                }
                default: {
                    return "";
                }
            }
        } else {
            return valeur;
        }
    }

    public String getNumeroAvsFormatted() {
        try {
            return NSUtil.formatAVSUnknown((this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE)));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return this.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
            } catch (Exception e2) {
                e2.printStackTrace();
                return "";
            }
        }
    }

    public String[] getNumeroConjointAyantDroit(String refunique) {
        String[] res = new String[2];
        try {
            HEOutputAnnonceListViewBean listViewBean = new HEOutputAnnonceListViewBean();
            listViewBean.setSession(getSession());
            listViewBean.setForRefUnique(refunique);
            listViewBean.setForCodeApplication("11");
            listViewBean.setIsArchivage(getArchivage());
            listViewBean.find();
            HEOutputAnnonceViewBean arc;
            for (int i = 0; i < listViewBean.size(); i++) {
                arc = (HEOutputAnnonceViewBean) listViewBean.getEntity(i);
                if (arc.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("01")) {
                    // modif NNSS
                    // res[0] = arc.getField(HEAnnoncesViewBean.NUMERO_ASSURE);
                    res[0] = globaz.commons.nss.NSUtil.formatAVSNew(arc.getField(IHEAnnoncesViewBean.NUMERO_ASSURE),
                            arc.getNumeroAvsNNSS().equals("true"));
                } else if (arc.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("04")) {
                    res[1] = arc.getField(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA);
                    // modif NNSS
                    // res[0] =
                    // arc.getField(HEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT);
                    res[0] = globaz.commons.nss.NSUtil.formatAVSNew(
                            arc.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT),
                            arc.getNumeroAvsNNSS().equals("true"));

                    if (JAUtil.isStringEmpty(res[0]) || JAUtil.isIntegerEmpty(res[0])) {
                        // modif NNSS
                        // res[0] = arc.getNumeroAVS();
                        res[0] = globaz.commons.nss.NSUtil.formatAVSNew(arc.getNumeroAVS(), arc.getNumeroAvsNNSS()
                                .equals("true"));

                    }

                } else if (arc.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT).equals("05")) {

                    res[0] = arc.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE);

                    if (JAUtil.isStringEmpty(res[0]) || JAUtil.isIntegerEmpty(res[0])) {
                        // modif NNSS
                        // res[0] = arc.getNumeroAVS();
                        res[0] = globaz.commons.nss.NSUtil.formatAVSNew(arc.getNumeroAVS(), arc.getNumeroAvsNNSS()
                                .equals("true"));

                    }
                }
            }
            return res;
        } catch (Exception e) {
            return res;
        }
    }

    /**
     * Method getRefUnique38 : retourne la référence unique de l'annonce 38 associé au 39 en cours.
     * 
     * @return String
     */
    public String getRefUnique38() {
        try { // test si l'on a affaire à une arc 39, si non on retourne une
              // référence vide
            if (getChampEnregistrement().startsWith("39")) {
                // on retrouve un 38 en lien avec le 39 en cours et on retourne
                // sa référence unique
                HEOutputAnnonceViewBean annonce38 = new HEOutputAnnonceViewBean();
                annonce38.setSession(getSession());
                // l'annonce 38 doit se trouver juste avant le 39 (d'où le -1)
                annonce38.setIdAnnonce(String.valueOf(Integer.parseInt(getIdAnnonce()) - 1));
                annonce38.retrieve();
                // recherche de l'annonce précédent le 39 en cours
                while (annonce38.isNew()) {
                    if (annonce38.getIdAnnonce().equals("0")) {
                        return "";
                    } else {
                        annonce38.setIdAnnonce(String.valueOf(Integer.parseInt(annonce38.getIdAnnonce()) - 1));
                        annonce38.retrieve();
                    }
                } // on a une annonce mais est-ce que c'est une annonce 38 ??
                if (annonce38.getChampEnregistrement().startsWith("38")) {
                    return annonce38.getRefUnique();
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getStatutLibelle() {
        try {
            return ((HEApplication) GlobazSystem.getApplication("HERMES")).getCsStatutListe(getSession())
                    .getCodeSysteme(getStatut()).getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String getTypeAnnonceLibelle() {
        String lib = getCodeAppLibelle();
        try {
            return lib.substring(lib.lastIndexOf('-') + 1, lib.length());
        } catch (Exception e) {
            e.printStackTrace();
            return lib;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public boolean isHidden(int i) {
        return (Arrays.asList(HEAnnoncesViewBean.hiddenFields).contains(getFieldId(i)));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.01.2003 08:43:33)
     * 
     * @return java.lang.String
     */
    public boolean isReadOnly(int i) {
        return (Arrays.asList(HEAnnoncesViewBean.forbiddenFields).contains(getFieldId(i)));
    }

    @Override
    public void put(String idField, String value) {
        super.put(idField, value);
    }

    @Override
    public void setArchivage(Boolean bool) {
        super.setArchivage(bool.booleanValue());
    }

    @Override
    public void setCategorie(String cat) {

    }

    @Override
    public void setLangueCorrespondance(String langueCorrespondance) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPriorite(String pty) {
        super.setPrioriteLot(pty);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toMyString() {
        return getChampEnregistrement();
    }

    @Override
    public void wantCheckCiOuvert(String valeur) {
        super.wantCheckCiOuvert = valeur;

    }

    @Override
    public void wantCheckNumAffilie(String valeur) {

    }
}

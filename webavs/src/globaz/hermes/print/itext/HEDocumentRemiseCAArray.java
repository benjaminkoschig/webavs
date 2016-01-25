package globaz.hermes.print.itext;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JACalendar;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.application.HEProperties;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.gestion.HEOutputAnnonceException;
import globaz.hermes.db.gestion.HEOutputAnnonceJointHEInfos;
import globaz.hermes.db.gestion.HEOutputAnnonceJointHEInfosManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.orion.utils.AclComparatorSuccursale;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.util.TreeSet;

public class HEDocumentRemiseCAArray {

    public final static String SEXE_FEMME = "2";

    public final static String SEXE_HOMME = "1";
    public String codeExclus = null;
    private BProcess crtProcess = null;
    private String idlot = "";
    private boolean isAttest = false;
    private boolean isCertif = false;
    private boolean isNotMotifPavo = true;
    private boolean isPrintCSV = false;
    private String noEmploye = "";
    private String noSuccursale = "";
    private String service = new String();
    private BSession session;
    public TreeSet<HEDocumentRemiseCAStruct> tabCA = new TreeSet<HEDocumentRemiseCAStruct>(
            new AclComparatorSuccursale());
    private BTransaction transaction;

    /**
     * Collecte les données des employeurs/employes pour l'affichage PDF/CSV des concordances.
     */
    public HEDocumentRemiseCAArray() {

    }

    public HEDocumentRemiseCAArray(BSession session) {
        this.session = session;
    }

    public HEDocumentRemiseCAArray(BTransaction Transaction, BSession Session) {
        transaction = Transaction;
        session = Session;
    }

    public void fillAffilie(HEDocumentRemiseCAStruct affilie, HEOutputAnnonceJointHEInfos crtLigne) throws Exception {
        affilie.setNnss(globaz.commons.nss.NSUtil.formatAVSNewNum(crtLigne.getNumeroAVS()));

        if (JadeStringUtil.isEmpty(affilie.getReference())) {
            affilie.setReference(crtLigne.getRefUnique());
        }
        if (JadeStringUtil.isEmpty(affilie.getUser())) {
            affilie.setUser(crtLigne.getUtilisateur());
        }

        if (!JadeStringUtil.isEmpty(crtLigne.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF))) {
            String[] nom = crtLigne.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF).split(",");

            if (nom.length > 1) {
                affilie.setNom(JadeStringUtil.stripBlanks(nom[0]));
                affilie.setPrenom(JadeStringUtil.stripBlanks(nom[1]));
            }
            if (!JadeStringUtil.isEmpty(crtLigne.getMotif())) {
                affilie.setMotif(crtLigne.getMotif());
            }
            if (!JadeStringUtil.isEmpty(crtLigne.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA))) {
                affilie.setDateNaiss(crtLigne.getField(IHEAnnoncesViewBean.DATE_NAISSANCE_1_JJMMAA));
            }
        }
        if (!JadeStringUtil.isEmpty(crtLigne.getField(IHEAnnoncesViewBean.SEXE))) {
            affilie.setSexe(crtLigne.getField(IHEAnnoncesViewBean.SEXE));
        }

        if (HEInfos.CS_ADRESSE_ASSURE.equals(crtLigne.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(affilie.getAdresse())) {
                affilie.setAdresse(crtLigne.getLibInfo());
            }
        }
        if (HEInfos.CS_DATE_ENGAGEMENT.equals(crtLigne.getTypeInfo())) {
            if ((JadeStringUtil.isEmpty(crtLigne.getLibInfo()))) {
                affilie.setAnneeCot(String.valueOf(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
            } else {
                affilie.setAnneeCot(crtLigne.getLibInfo());
                affilie.setDateEnregistrement(crtLigne.getLibInfo());
            }
        }
        // Modif 1-5-8, si date engagement vide => date du jour
        if (JadeStringUtil.isBlank(affilie.getDateEnregistrement())) {
            affilie.setDateEnregistrement(JACalendar.format(crtLigne.getDateAnnonce(), JACalendar.FORMAT_DDsMMsYYYY));
        }

        if (HEInfos.CS_TITRE_ASSURE.equals(crtLigne.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(crtLigne.getLibInfo())) {
                affilie.setPolitesse("");
            } else {
                affilie.setPolitesse(crtLigne.getLibInfo());
            }
        }
        if (HEInfos.CS_FORMULE_POLITESSE.equals(crtLigne.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(crtLigne.getLibInfo())) {
                affilie.setFormulePolitesseSpecifique("");
            } else {
                affilie.setFormulePolitesseSpecifique(crtLigne.getLibInfo());
            }
        }
        if (HEInfos.CS_NUMERO_SUCCURSALE.equals(crtLigne.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(crtLigne.getLibInfo())) {
                affilie.setNumeroSuccursale(noSuccursale);
            } else {
                affilie.setNumeroSuccursale(crtLigne.getLibInfo());
            }
        }
        if (HEInfos.CS_NUMERO_EMPLOYE.equals(crtLigne.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(crtLigne.getLibInfo())) {
                affilie.setNumeroEmploye(noEmploye);
            } else {
                affilie.setNumeroEmploye(crtLigne.getLibInfo());
            }
        }
        if (HEInfos.CS_LANGUE_CORRESPONDANCE.equals(crtLigne.getTypeInfo())) {
            affilie.setLangue(TITiers.toLangueIso(crtLigne.getLibInfo()));
        }

        if (HEInfos.CS_CATEGORIE.equals(crtLigne.getTypeInfo())) {
            if (IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR.equals(crtLigne.getLibInfo())) {
                affilie.setEmployeur(true);
            } else if (IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT.equals(crtLigne.getLibInfo())) {
                affilie.setInde(true);
            } else if (IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER.equals(crtLigne.getLibInfo())) {
                affilie.setRentier(true);
            }
        }

        if (HEInfos.CS_NUMERO_AFFILIE.equals(crtLigne.getTypeInfo())) {
            if (!JadeStringUtil.isEmpty(crtLigne.getLibInfo())) {
                affilie.setNAffilie(crtLigne.getLibInfo());
                AFAffiliationManager AffManager = new AFAffiliationManager();
                AFAffiliation crtAff;
                AffManager.setSession(getSession());
                AffManager.setForAffilieNumero(affilie.getNAffilie());
                AffManager.changeManagerSize(1);
                AffManager.find();

                if ((AffManager.size() == 0) || (AffManager.size() > 1)) {
                    String erreur = AffManager.size() + " trouvée(s) pour l'affiliation :" + affilie.getNAffilie();
                    if (getCrtProcess() != null) {
                        getCrtProcess().getMemoryLog().logMessage(erreur, FWMessage.ERREUR, "HEDocumentRemiseCAArray");
                    } else {
                        System.err.println(erreur);
                    }
                }

                if ((crtAff = (AFAffiliation) AffManager.getFirstEntity()) != null) {
                    affilie.setAdresse(crtAff.getTiers().getAdresseAsString(null,
                            IConstantes.CS_AVOIR_ADRESSE_COURRIER, HEApplication.CS_DOMAINE_ADRESSE_CI_ARC,
                            JACalendar.todayJJsMMsAAAA(), crtAff.getAffilieNumero()));
                    if (JadeStringUtil.isEmpty(affilie.getLangue())) { // Inforom 444
                        affilie.setLangue(toLangueIso(crtAff.getTiers().getLangue()));
                    }
                    HEApplication app = (HEApplication) getSession().getApplication();
                    if (HEDocumentRemiseCAArray.SEXE_HOMME.equals(affilie.getSexe())) {
                        affilie.setPolitesse(app.getLabel("MSG_MONSIEUR", affilie.getLangue()));
                    } else {
                        affilie.setPolitesse(app.getLabel("MSG_MADAME", affilie.getLangue()));
                    }
                    affilie.setAffiliation(crtAff);
                }
            }
        }

    }

    public void fillComplement(HEDocumentRemiseCAStruct affilie, HEInfos cpl) throws Exception {
        if (HEInfos.CS_ADRESSE_ASSURE.equals(cpl.getTypeInfo())) {
            affilie.setAdresse(cpl.getLibInfo());
        }
        if (HEInfos.CS_DATE_ENGAGEMENT.equals(cpl.getTypeInfo())) {
            if ((JadeStringUtil.isEmpty(cpl.getLibInfo()))) {
                affilie.setAnneeCot(String.valueOf(JACalendar.getYear(JACalendar.todayJJsMMsAAAA())));
            } else {
                affilie.setAnneeCot(cpl.getLibInfo());
            }
        }

        if (HEInfos.CS_TITRE_ASSURE.equals(cpl.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(cpl.getLibInfo())) {
                affilie.setPolitesse("");
            } else {
                affilie.setPolitesse(cpl.getLibInfo());
            }
        }
        if (HEInfos.CS_FORMULE_POLITESSE.equals(cpl.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(cpl.getLibInfo())) {
                affilie.setFormulePolitesseSpecifique("");
            } else {
                affilie.setFormulePolitesseSpecifique(cpl.getLibInfo());
            }
        }
        if (HEInfos.CS_LANGUE_CORRESPONDANCE.equals(cpl.getTypeInfo())) {
            if (JadeStringUtil.isEmpty(cpl.getLibInfo())) {
                affilie.setLangue("FR");
            } else {
                affilie.setLangue(TITiers.toLangueIso(cpl.getLibInfo()));
            }
        }

        if (HEInfos.CS_CATEGORIE.equals(cpl.getTypeInfo())) {
            if (IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR.equals(cpl.getLibInfo())) {
                affilie.setEmployeur(true);
            } else if (IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT.equals(cpl.getLibInfo())) {
                affilie.setInde(true);
            } else if (IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER.equals(cpl.getLibInfo())) {
                affilie.setRentier(true);
            }
        }
    }

    public void fillList() throws Exception {
        try {
            String motif = getMotif();

            HEDocumentRemiseCAStruct affilie = null;
            HEOutputAnnonceJointHEInfosManager listeAcc = new HEOutputAnnonceJointHEInfosManager();
            listeAcc.setSession(getSession());

            listeAcc.setForMotifLettreCA(motif);
            listeAcc.setNNSS(true);
            listeAcc.setNotMotifPavo(isNotMotifPavo());
            listeAcc.setPrintCSV(isPrintCSV());
            listeAcc.setForService(getService());

            if (isAttest()) {
                listeAcc.setLikeEnregistrement("2101");

                // ajouter substring pour récup le code de référence
            } else {
                listeAcc.setLikeEnregistrement("2001");

            }

            if (isCertif()) {
                listeAcc.setCodeTraitement(true);
            }

            listeAcc.setForIdLot(getIdlot());

            BStatement Statement = listeAcc.cursorOpen(getTransaction());

            try {
                HEOutputAnnonceJointHEInfos crtLigne;
                String lastRef = "";
                while ((crtLigne = (HEOutputAnnonceJointHEInfos) listeAcc.cursorReadNext(Statement)) != null) {

                    if (!isLineExclude(crtLigne)) {

                        if (!crtLigne.getRefUnique().equals(lastRef)) {
                            if (affilie != null) {
                                tabCA.add(affilie);
                            }
                            affilie = new HEDocumentRemiseCAStruct();
                            lastRef = crtLigne.getRefUnique();
                        }

                        fillAffilie(affilie, crtLigne);
                    }

                }
                // traiter le dernier !
                if (affilie != null) {
                    tabCA.add(affilie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                listeAcc.cursorClose(Statement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BProcess getCrtProcess() {
        return crtProcess;
    }

    public String getIdlot() {
        return idlot;
    }

    public String getMotif() {

        String key = "";
        // cherche les motifs dans FWPARP
        if (isCertif()) {
            key = "MOTCERT";
        } else if (isAttest()) {
            key = "MOTATT";
        }

        if (isCertif() || isAttest()) {
            try {
                FWFindParameterManager param = new FWFindParameterManager();
                FWFindParameter parametre;
                param.setSession(session);
                param.setIdApplParametre(HEApplication.DEFAULT_APPLICATION_HERMES);
                param.setIdCleDiffere(key);
                param.find(transaction);
                parametre = (FWFindParameter) param.getFirstEntity();
                return parametre.getValeurAlphaParametre();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public String getNoEmploye() {
        return noEmploye;
    }

    public String getNoSuccursale() {
        return noSuccursale;
    }

    public String getService() {
        return service;
    }

    public BSession getSession() {
        return session;
    }

    public BTransaction getTransaction() {
        return transaction;
    }

    public boolean isAttest() {
        return isAttest;
    }

    public boolean isCertif() {
        return isCertif;
    }

    private boolean isLineExclude(HEOutputAnnonceJointHEInfos crtLigne) throws HEOutputAnnonceException {
        if (codeExclus == null) {
            codeExclus = HEProperties.getParameter(HEProperties.PROP_CODE_ARC_EXCLUS_ATT_CA, getSession(),
                    getTransaction());
        }

        if (!JadeStringUtil.isEmpty(codeExclus)) {
            String refInterne = crtLigne.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE);

            if (!JadeStringUtil.isEmpty(refInterne) && refInterne.startsWith(codeExclus)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isNotMotifPavo() {
        return isNotMotifPavo;
    }

    public boolean isPrintCSV() {
        return isPrintCSV;
    }

    public void setAttest(boolean isAttest) {
        this.isAttest = isAttest;
    }

    public void setCertif(boolean isCertif) {
        this.isCertif = isCertif;
    }

    public void setCrtProcess(BProcess crtProcess) {
        this.crtProcess = crtProcess;
    }

    public void setIdlot(String idlot) {
        this.idlot = idlot;
    }

    public void setNoEmploye(String noEmploye) {
        this.noEmploye = noEmploye;
    }

    public void setNoSuccursale(String noSuccursale) {
        this.noSuccursale = noSuccursale;
    }

    public void setNotMotifPavo(boolean isNotMotifPavo) {
        this.isNotMotifPavo = isNotMotifPavo;
    }

    public void setPrintCSV(boolean isPrintCSV) {
        this.isPrintCSV = isPrintCSV;
    }

    public void setService(String newService) {
        service = newService;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BTransaction transaction) {
        this.transaction = transaction;
    }

    public String toLangueIso(String langue) {
        if (ITITiers.CS_FRANCAIS.equals(langue)) {
            return "fr";
        }
        if (ITITiers.CS_ALLEMAND.equals(langue)) {
            return "de";
        }
        if (ITITiers.CS_ITALIEN.equals(langue)) {
            return "it";
        }
        return "";
    }

}

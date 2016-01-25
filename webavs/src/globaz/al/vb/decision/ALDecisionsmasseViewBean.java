package globaz.al.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;

public class ALDecisionsmasseViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(ALDecisionsmasseViewBean.class);

    /**
     * Collection contenant les tarifs de dossier à prendre en compte
     */
    private HashSet<String> inTarifFilter = new HashSet<String>();
    /**
     * Collection contenant les droits de dossier à prendre en compte
     */
    private HashSet<String> inDroitFilter = new HashSet<String>();
    /**
     * Collection contenant les affiliés de dossier à prendre en compte
     */
    private HashSet<String> inAffilieFilter = new HashSet<String>();
    /**
     * Collection contenant les activités de dossier à prendre en compte
     */
    private HashSet<String> inActiviteFilter = new HashSet<String>();
    /**
     * Collection contenant les statuts de dossier à prendre en compte
     */
    private HashSet<String> inStatutFilter = new HashSet<String>();

    private DossierComplexSearchModel searchModel = null;
    public static final String ETATACTIF = DossierComplexSearchModel.ETATACTIF;
    public static final String ETATRADIE = DossierComplexSearchModel.ETATRADIE;

    private String etatFilter = DossierComplexSearchModel.ETATACTIF;

    private String dateImpression = "";
    private String email = "";
    private String fileName = "";
    private String originalFileName = "";

    private String dateValiditeGREAT = "";
    private String dateValiditeLESS = "";
    private String dateFinValiditeGREAT = "";
    private String dateFinValiditeLESS = "";

    private String dateDebutValidite = "";
    private boolean gestionTexteLibre = false;
    private boolean insertionGED = true;
    private boolean gestionCopie = true;
    private String triImpression = "";
    private String texteLibre = "";

    public ALDecisionsmasseViewBean() {
        super();
        setEmail(JadeThread.currentUserEmail());
        setDateImpression(JadeDateUtil.getGlobazFormattedDate(new Date()));
        setSearchModel(new DossierComplexSearchModel());
    }

    public void setFilterTarif(String tarifs) {
        String[] tarifsTabs = tarifs.split(",");
        for (int i = 0; i < tarifsTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(tarifsTabs[i])) {
                inTarifFilter.add(tarifsTabs[i]);
            }

        }
    }

    public void setFilterDroit(String droits) {
        String[] droitsTabs = droits.split(",");
        for (int i = 0; i < droitsTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(droitsTabs[i])) {
                inDroitFilter.add(droitsTabs[i]);
            }

        }
    }

    public void setFilterActivite(String activites) {
        String[] activitesTabs = activites.split(",");
        for (int i = 0; i < activitesTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(activitesTabs[i])) {
                inActiviteFilter.add(activitesTabs[i]);
            }
        }
    }

    public void setFilterAffilie(String filterAffilie) {
        String[] affilieTabs = filterAffilie.split(",");
        for (int i = 0; i < affilieTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(affilieTabs[i])) {
                inAffilieFilter.add(affilieTabs[i]);
            }
        }
    }

    public void setFilterEtat(String etats) {
        etatFilter = etats;
    }

    public void setFilterStatut(String statuts) {

        String[] statutsTabs = statuts.split(",");
        for (int i = 0; i < statutsTabs.length; i++) {
            if (!JadeStringUtil.isEmpty(statutsTabs[i])) {
                inStatutFilter.add(statutsTabs[i]);
            }
        }

    }

    public String selectedTarif(String code) {
        return (inTarifFilter.isEmpty() || inTarifFilter.contains(code)) ? "selected" : "";
    }

    public String selectedStatut(String code) {
        return (inStatutFilter.isEmpty() || inStatutFilter.contains(code)) ? "selected" : "";
    }

    public String selectedActivite(String code) {
        return (inActiviteFilter.isEmpty() || inActiviteFilter.contains(code)) ? "selected" : "";
    }

    public String selectedDroit(String code) {
        return (inDroitFilter.isEmpty() || inDroitFilter.contains(code)) ? "selected" : "";
    }

    public String selectedTri(String code) {
        return getTriImpression().equalsIgnoreCase(code) ? "selected" : "";
    }

    public String selectedEtat(String code) {
        return getEtatFilter().equalsIgnoreCase(code) ? "selected" : "";
    }

    public String getEtatFilter() {
        return etatFilter;
    }

    @Override
    public void add() throws Exception {
        // NOT IMPLEMENTED
    }

    @Override
    public void delete() throws Exception {
        // NOT IMPLEMENTED
    }

    public boolean getGestionCopie() {
        return gestionCopie;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getEmail() {
        return email;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public boolean getGestionTexteLibre() {
        return gestionTexteLibre;
    }

    public Collection<String> getInStatut() {
        return inStatutFilter;
    }

    public Collection<String> getInAffilie() {
        return inAffilieFilter;
    }

    public Collection<String> getInActivites() {
        return inActiviteFilter;
    }

    public Collection<String> getInTarif() {
        return inTarifFilter;
    }

    public Collection<String> getInTypeDroit() {
        return inDroitFilter;
    }

    @Override
    public String getId() {
        // NOT IMPLEMENTED
        return null;
    }

    public boolean getInsertionGED() {
        return insertionGED;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        // NOT IMPLEMENTED
        return null;
    }

    public String getTexteLibre() {
        return texteLibre;
    }

    public String getDateDebutValidite() {
        return dateDebutValidite;
    }

    public void setDateDebutValidite(String dateDebutValidite) {
        this.dateDebutValidite = dateDebutValidite;
    }

    public String getDateValiditeGREAT() {
        return dateValiditeGREAT;
    }

    public void setDateValiditeGREAT(String dateValiditeGREAT) {
        this.dateValiditeGREAT = dateValiditeGREAT;
    }

    public String getDateValiditeLESS() {
        return dateValiditeLESS;
    }

    public void setDateValiditeLESS(String dateValiditeLESS) {
        this.dateValiditeLESS = dateValiditeLESS;
    }

    public String getDateFinValiditeGREAT() {
        return dateFinValiditeGREAT;
    }

    public void setDateFinValiditeGREAT(String dateFinValiditeGREAT) {
        this.dateFinValiditeGREAT = dateFinValiditeGREAT;
    }

    public String getDateFinValiditeLESS() {
        return dateFinValiditeLESS;
    }

    public void setDateFinValiditeLESS(String dateFinValiditeLESS) {
        this.dateFinValiditeLESS = dateFinValiditeLESS;
    }

    @Override
    public void retrieve() throws Exception {
        // NOT IMPLEMENTED
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setGestionTexteLibre(boolean gestionTexteLibre) {
        this.gestionTexteLibre = gestionTexteLibre;
    }

    public void setGestionCopie(boolean gestionCopie) {
        this.gestionCopie = gestionCopie;
    }

    @Override
    public void setId(String newId) {
        // NOT IMPLEMENTED
    }

    public void setInsertionGED(boolean insertionGED) {
        this.insertionGED = insertionGED;
    }

    public void setTexteLibre(String texteLibre) {
        this.texteLibre = texteLibre;
    }

    @Override
    public void update() throws Exception {
        // NOT IMPLEMENTED
    }

    public String getTriImpression() {
        return triImpression;
    }

    public void setTriImpression(String triImpression) {
        this.triImpression = triImpression;
    }

    public DossierComplexSearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(DossierComplexSearchModel searchModel) {
        this.searchModel = searchModel;
    }

    // placed to avoid FW doing much more introspection
    public void setSelectedId(String ignore) {
    }

    public void setForNumAffilieWidget(String ignore) {
    }

    public void setUserAction(String ignore) {
    }

    /**
     * @deprecated not used, only to avoid framework to do multiple introspection
     * @return null
     */
    @Deprecated
    public String getFilterAffilie() {
        return null;
    }

    /**
     * @deprecated not used, only to avoid framework to do multiple introspection
     * @return null
     */
    @Deprecated
    public String getFilterActivite() {
        return null;
    }

    /**
     * @deprecated not used, only to avoid framework to do multiple introspection
     * @return null
     */
    @Deprecated
    public String getFilterStatut() {
        return null;
    }

    /**
     * @deprecated not used, only to avoid framework to do multiple introspection
     * @return null
     */
    @Deprecated
    public String getFilterTarif() {
        return null;
    }

    /**
     * @deprecated not used, only to avoid framework to do multiple introspection
     * @return null
     */
    @Deprecated
    public String getFilterDroit() {
        return null;
    }

    /**
     * @deprecated not used, only to avoid framework to do multiple introspection
     * @return null
     */
    @Deprecated
    public String forNumAffilieWidget() {
        return null;
    }
    // ENDOF placed to avoid FW doing much more introspection

}

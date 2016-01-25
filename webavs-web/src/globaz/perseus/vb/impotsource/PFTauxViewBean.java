package globaz.perseus.vb.impotsource;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import ch.globaz.perseus.business.constantes.CSTypeBareme;
import ch.globaz.perseus.business.models.impotsource.SimpleBareme;
import ch.globaz.perseus.business.models.impotsource.SimpleBaremeSearchModel;
import ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaire;
import ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaireSearchModel;
import ch.globaz.perseus.business.models.impotsource.Taux;
import ch.globaz.perseus.business.models.impotsource.TauxSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class PFTauxViewBean extends BJadePersistentObjectViewBean {

    private String annee = null;
    private String csTypebareme = null;
    private Vector<String[]> listeAnnee = new Vector<String[]>();
    private ArrayList<SimpleBareme> listeBareme = new ArrayList<SimpleBareme>();
    private ArrayList<Taux> listetaux = new ArrayList<Taux>();
    private ArrayList<SimpleTrancheSalaire> listeTrancheSalaire = new ArrayList<SimpleTrancheSalaire>();

    private Taux taux = null;

    private Map<String, Taux> tauxImposition = null;

    private String valeurTaux = null;

    public PFTauxViewBean() {
        super();
        taux = new Taux();
        listeTrancheSalaire = new ArrayList<SimpleTrancheSalaire>();
        setListeBareme(new ArrayList<SimpleBareme>());
        tauxImposition = new HashMap<String, Taux>();
        listeAnnee = new Vector<String[]>();
    }

    public PFTauxViewBean(Taux taux) {
        super();
        this.taux = taux;
    }

    @Override
    public void add() throws Exception {
        taux.getSimpleTaux().setAnneeTaux(annee);
        taux.getSimpleTaux().setValeurTaux(valeurTaux);
        PerseusServiceLocator.getTauxService().create(taux);

    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getTauxService().delete(taux);

    }

    public String getAnnee() {
        return annee;
    }

    public String getCsTypebareme() {
        return csTypebareme;
    }

    @Override
    public String getId() {
        return taux.getId();
    }

    public Vector<String[]> getListeAnnee() {
        return listeAnnee;
    }

    public ArrayList<SimpleBareme> getListeBareme() {
        return listeBareme;
    }

    public ArrayList<Taux> getListetaux() {
        return listetaux;
    }

    public ArrayList<SimpleTrancheSalaire> getListeTrancheSalaire() {
        return listeTrancheSalaire;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(taux.getSpy());
    }

    public Taux getTaux() {
        return taux;
    }

    public Map<String, Taux> getTauxImposition() {
        return tauxImposition;
    }

    public String getTauxValue(String idTrancheSalaire, String idNbPersonnes) {
        String key = idTrancheSalaire + "," + idNbPersonnes;
        if (tauxImposition.containsKey(key)) {
            return tauxImposition.get(key).getSimpleTaux().getValeurTaux();
        }
        return "";
    }

    public String getValeurTaux() {
        return valeurTaux;
    }

    public void init() throws Exception {
        // Chargement de l'année en cours, si aucune année sélectionnée
        if (annee == null) {
            annee = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now())).substring(6);
        }

        if (csTypebareme == null) {
            csTypebareme = CSTypeBareme.BAREME_B.getCodeSystem();
        }

        // Chargement de la liste de tranche de salaire
        SimpleTrancheSalaireSearchModel trancheSalaireSearch = new SimpleTrancheSalaireSearchModel();
        trancheSalaireSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        trancheSalaireSearch.setForAnnee(annee);
        trancheSalaireSearch = PerseusImplServiceLocator.getSimpleTrancheSalaireService().search(trancheSalaireSearch);
        for (JadeAbstractModel abstractModel : trancheSalaireSearch.getSearchResults()) {
            SimpleTrancheSalaire trancheSalaire = (SimpleTrancheSalaire) abstractModel;
            listeTrancheSalaire.add(trancheSalaire);
        }

        SimpleBaremeSearchModel simpleBaremeSearch = new SimpleBaremeSearchModel();
        simpleBaremeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simpleBaremeSearch.setForCsTypeBareme(csTypebareme);
        simpleBaremeSearch.setForAnnee(annee);
        simpleBaremeSearch = PerseusImplServiceLocator.getSimpleBaremeService().search(simpleBaremeSearch);
        for (JadeAbstractModel abstractModel : simpleBaremeSearch.getSearchResults()) {
            SimpleBareme simpleBareme = (SimpleBareme) abstractModel;
            listeBareme.add(simpleBareme);
        }

        TauxSearchModel tauxSearch = new TauxSearchModel();
        tauxSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        tauxSearch.setForAnnee(annee);
        tauxSearch.setForCsTypeBareme(csTypebareme);
        tauxSearch = PerseusServiceLocator.getTauxService().search(tauxSearch);
        for (JadeAbstractModel tauxModel : tauxSearch.getSearchResults()) {
            Taux taux = (Taux) tauxModel;
            if (!JadeStringUtil.isBlankOrZero(taux.getSimpleTaux().getValeurTaux())) {
                tauxImposition.put(taux.getTrancheSalaire().getId() + "," + taux.getSimpleBareme().getIdBareme(), taux);
            }
        }

        TauxSearchModel tauxSearchAnnee = new TauxSearchModel();
        tauxSearchAnnee.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        tauxSearchAnnee = PerseusServiceLocator.getTauxService().search(tauxSearchAnnee);

        // Récupération des années présentent dans la table taux, de manière unique et ordré
        Set<String> annees = new TreeSet<String>();
        for (JadeAbstractModel tauxModel : tauxSearchAnnee.getSearchResults()) {
            Taux taux = (Taux) tauxModel;
            annees.add(JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now())).substring(6));
            annees.add(annee);
            annees.add(taux.getSimpleTaux().getAnneeTaux());
            // Insertion d'une année suplémentaire pour pouvoir insérer des taux l'année suivante.
            annees.add(String.valueOf(Integer.parseInt(JadeDateUtil
                    .getGlobazFormattedDate(new Date(JadeDateUtil.now())).substring(6)) + 1));
        }

        // Affectation de la liste des années dans le tableau listeAnnee
        for (String annee : annees) {
            listeAnnee.add(new String[] { annee, annee });
        }
    }

    @Override
    public void retrieve() throws Exception {
        init();

    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCsTypebareme(String csTypebareme) {
        this.csTypebareme = csTypebareme;
    }

    @Override
    public void setId(String newId) {
        taux.setId(newId);

    }

    public void setListeAnnee(Vector<String[]> listeAnnee) {
        this.listeAnnee = listeAnnee;
    }

    public void setListeBareme(ArrayList<SimpleBareme> listeBareme) {
        this.listeBareme = listeBareme;
    }

    public void setListetaux(ArrayList<Taux> listetaux) {
        this.listetaux = listetaux;
    }

    public void setListeTrancheSalaire(ArrayList<SimpleTrancheSalaire> listeTrancheSalaire) {
        this.listeTrancheSalaire = listeTrancheSalaire;
    }

    public void setTaux(Taux taux) {
        this.taux = taux;
    }

    public void setTauxImposition(Map<String, Taux> tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    public void setValeurTaux(String valeurTaux) {
        this.valeurTaux = valeurTaux;
    }

    @Override
    public void update() throws Exception {
        TauxSearchModel tauxSearch = new TauxSearchModel();
        tauxSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        tauxSearch.setForAnnee(annee);
        tauxSearch.setForCsTypeBareme(csTypebareme);
        tauxSearch = PerseusServiceLocator.getTauxService().search(tauxSearch);
        for (JadeAbstractModel tauxModel : tauxSearch.getSearchResults()) {
            Taux taux = (Taux) tauxModel;
            PerseusServiceLocator.getTauxService().delete(taux);
        }

        for (Taux taux : tauxImposition.values()) {
            if (Float.parseFloat(taux.getSimpleTaux().getValeurTaux()) > 0) {
                taux.getSimpleTaux().setAnneeTaux(annee);
                PerseusServiceLocator.getTauxService().create(taux);
            }
        }
    }
}

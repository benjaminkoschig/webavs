package globaz.perseus.vb.qd;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.exceptions.models.qd.QDException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.qd.CSTypeQD;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.QDAnnuelle;
import ch.globaz.perseus.business.models.qd.QDAnnuelleSearchModel;
import ch.globaz.perseus.business.models.qd.QDSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.MembreFamille;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * Viewbean permettant l'affichage des détails d'une facture.
 * 
 * @author JSI
 * 
 */
public class PFOuvertureQdViewBean extends BJadePersistentObjectViewBean {

    private Demande demande = null;
    private Set<String> listIdMembresFamillesAvecQD = null;
    private List<MembreFamille> listMembresFamilles = null;
    private String qdToOpen = null;

    public PFOuvertureQdViewBean() {
        super();
        demande = new Demande();
        listMembresFamilles = new ArrayList<MembreFamille>();
        listIdMembresFamillesAvecQD = new HashSet<String>();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the demande
     */
    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        return getDemande().getId();
    }

    /**
     * @return the listIdMembresFamillesAvecQD
     */
    public Set<String> getListIdMembresFamillesAvecQD() {
        return listIdMembresFamillesAvecQD;
    }

    /**
     * @return the listMembresFamilles
     */
    public List<MembreFamille> getListMembresFamilles() {
        return listMembresFamilles;
    }

    /**
     * @return the qdToOpen
     */
    public String getQdToOpen() {
        return qdToOpen;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(getDemande().getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        demande = PerseusServiceLocator.getDemandeService().read(demande.getId());

        // Ajout des membres familles dans la liste
        listMembresFamilles.add(demande.getSituationFamiliale().getRequerant().getMembreFamille());
        if (!JadeStringUtil.isEmpty(demande.getSituationFamiliale().getConjoint().getMembreFamille().getId())) {
            listMembresFamilles.add(demande.getSituationFamiliale().getConjoint().getMembreFamille());
        }
        List<Enfant> enfants = PerseusServiceLocator.getDemandeService().getListEnfants(demande);
        for (Enfant enfant : enfants) {
            listMembresFamilles.add(enfant.getMembreFamille());
        }

        // Recherche des QD
        QDSearchModel searchModel = new QDSearchModel();
        searchModel.setForAnnee(demande.getSimpleDemande().getDateDebut().substring(6));
        searchModel.setForIdDossier(demande.getSimpleDemande().getIdDossier());
        searchModel = PerseusServiceLocator.getQDService().search(searchModel);

        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            QD qd = (QD) model;
            listIdMembresFamillesAvecQD.add(qd.getMembreFamille().getId());
        }
    }

    /**
     * @param demande
     *            the demande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String newId) {
        getDemande().setId(newId);
    }

    /**
     * @param listIdMembresFamillesAvecQD
     *            the listIdMembresFamillesAvecQD to set
     */
    public void setListIdMembresFamillesAvecQD(Set<String> listIdMembresFamillesAvecQD) {
        this.listIdMembresFamillesAvecQD = listIdMembresFamillesAvecQD;
    }

    /**
     * @param listMembresFamilles
     *            the listMembresFamilles to set
     */
    public void setListMembresFamilles(List<MembreFamille> listMembresFamilles) {
        this.listMembresFamilles = listMembresFamilles;
    }

    /**
     * @param qdToOpen
     *            the qdToOpen to set
     */
    public void setQdToOpen(String qdToOpen) {
        this.qdToOpen = qdToOpen;
    }

    @Override
    public void update() throws Exception {
        QDAnnuelleSearchModel searchModel = new QDAnnuelleSearchModel();
        searchModel.setForAnnee(demande.getSimpleDemande().getDateDebut().substring(6));
        searchModel.setForIdDossier(demande.getDossier().getId());
        searchModel = PerseusServiceLocator.getQDAnnuelleService().search(searchModel);
        if (searchModel.getSize() == 0) {
            throw new QDException("Erreur : Il n'y a pas de QD annuelle");
        }
        QDAnnuelle qdAnnuelle = (QDAnnuelle) searchModel.getSearchResults()[0];

        // Chargement des variables métiers pour les montants des QD
        VariableMetierSearch variableMetierSearch = new VariableMetierSearch();
        variableMetierSearch.setForDateValable("01." + qdAnnuelle.getSimpleQDAnnuelle().getAnnee());
        variableMetierSearch = PerseusServiceLocator.getVariableMetierService().search(variableMetierSearch);
        Hashtable<CSVariableMetier, VariableMetier> variablesMetier = new Hashtable<CSVariableMetier, VariableMetier>();
        for (JadeAbstractModel model : variableMetierSearch.getSearchResults()) {
            VariableMetier variableMetier = (VariableMetier) model;
            variablesMetier.put(CSVariableMetier.getEnumFromCodeSystem(variableMetier.getSimpleVariableMetier()
                    .getCsTypeVariableMetier()), variableMetier);
        }

        for (String idMF : qdToOpen.split(",")) {
            MembreFamille mf = PerseusServiceLocator.getMembreFamilleService().read(idMF);

            PerseusServiceLocator.getQDService().create(qdAnnuelle, mf, CSTypeQD.FRAIS_MALADIE, variablesMetier, true);
        }
    }
}

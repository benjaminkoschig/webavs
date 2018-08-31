package ch.globaz.vulpecula.process.syndicats;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.syndicat.ListeTravailleursSansSyndicat;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class ListeTravailleursSansSyndicatProcess extends BProcessWithContext {
    private static final long serialVersionUID = -3840278440960810742L;
    // TODO tenir compte de l'année
    private Annee annee;
    protected String idCaisseMetier;
    protected String idTravailleur;
    private Map<Administration, List<Travailleur>> travailleurByCaisseMetier;
    private Map<String, List<ListeTravailleursSansSyndicat>> travailleursByCaisseMetier;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        print();
        return true;
    }

    private void retrieve() {
        travailleurByCaisseMetier = new HashMap<Administration, List<Travailleur>>();
        travailleursByCaisseMetier = new HashMap<String, List<ListeTravailleursSansSyndicat>>();
        // Récupération des ids des postes de travails qui possèdent des cotisations CPR et qui n'ont pas de syndicat
        // pour une année
        List<ListeTravailleursSansSyndicat> listeTravailleursSansSyndicat = VulpeculaRepositoryLocator
                .getPosteTravailRepository()
                .findPostesTravailWithCPRCotiNotInAffiliationSyndicat(annee, idCaisseMetier);

        for (ListeTravailleursSansSyndicat travailleurSansSyndicat : listeTravailleursSansSyndicat) {
            // Map la liste des travailleurs par caisse métier
            if (!travailleursByCaisseMetier.containsKey(travailleurSansSyndicat.getIdCaisseMetier())) {
                travailleursByCaisseMetier.put(travailleurSansSyndicat.getIdCaisseMetier(),
                        new ArrayList<ListeTravailleursSansSyndicat>());
            }
            List<ListeTravailleursSansSyndicat> travailleurs = travailleursByCaisseMetier.get(travailleurSansSyndicat
                    .getIdCaisseMetier());
            travailleurs.add(travailleurSansSyndicat);
            // Tri de la liste sur le nom du travailleur
            Collections.sort(travailleurs, new Comparator<ListeTravailleursSansSyndicat>() {
                @Override
                public int compare(ListeTravailleursSansSyndicat a, ListeTravailleursSansSyndicat b) {
                    return a.getNomTravailleur().compareTo(b.getNomTravailleur());
                }
            });
        }
        // Charge la sous requete
        // Set<String> listTravailleurNotIn = VulpeculaRepositoryLocator.getAffiliationSyndicatRepository()
        // .findAllIdTravailleursByAnnee(annee);
        //
        // // Charge les postes de travail
        // PosteTravailSearchComplexModel searchModel = new PosteTravailSearchComplexModel();
        // searchModel.setForIdTravailleursNotIn(listTravailleurNotIn);
        // searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        // try {
        // JadePersistenceManager.search(searchModel);
        // } catch (JadePersistenceException e) {
        // JadeLogger.warn(this, "ERROR in process " + this.getClass().getName() + " (" + e.toString() + ")");
        // JadeLogger.warn(this, e);
        // }
        //
        // Map<String, Administration> mappingEmployeursCaisseMetier = new HashMap<String, Administration>();
        // for (JadeAbstractModel abstractModel : searchModel.getSearchResults()) {
        // PosteTravailComplexModel model = (PosteTravailComplexModel) abstractModel;
        //
        // // Mise en cache des caisses métiers
        // String idEmployeur = model.getPosteTravailSimpleModel().getIdEmployeur();
        // if (!mappingEmployeursCaisseMetier.containsKey(idEmployeur)) {
        // Administration caisseMetier = VulpeculaRepositoryLocator.getAdhesionRepository()
        // .findCaisseMetier(idEmployeur).getAdministrationPlanCaisse();
        // mappingEmployeursCaisseMetier.put(idEmployeur, caisseMetier);
        // }
        // Administration caisseMetier = mappingEmployeursCaisseMetier.get(idEmployeur);
        //
        // if (idCaisseMetier != null && idCaisseMetier.length() > 0 && !"0".equals(idCaisseMetier.trim())) {
        // if (!idCaisseMetier.equals(caisseMetier.getId())) {
        // continue;
        // }
        // }
        //
        // Travailleur trav = TravailleurConverter.getInstance().convertToDomain(model.getTravailleurComplexModel());
        // if (!travailleurByCaisseMetier.containsKey(caisseMetier)) {
        // travailleurByCaisseMetier.put(caisseMetier, new ArrayList<Travailleur>());
        // }
        // List<Travailleur> listTravailleur = travailleurByCaisseMetier.get(caisseMetier);
        // listTravailleur.add(trav);
        // Collections.sort(listTravailleur, new Comparator<Travailleur>() {
        // @Override
        // public int compare(Travailleur a, Travailleur b) {
        // return a.getNomPrenomTravailleur().compareTo(b.getNomPrenomTravailleur());
        // }
        // });
        // }

    }

    private void print() throws IOException {
        ListeTravailleursSansSyndicatExcel listeTravailleursSansSyndicatExcel = new ListeTravailleursSansSyndicatExcel(
                getSession(), DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_DOC_NAME,
                DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_NAME);
        listeTravailleursSansSyndicatExcel.setTravailleursByCaisseMetier(travailleursByCaisseMetier);
        listeTravailleursSansSyndicatExcel.setIdCaisseMetier(idCaisseMetier);
        listeTravailleursSansSyndicatExcel.setAnnee(annee);
        listeTravailleursSansSyndicatExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listeTravailleursSansSyndicatExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    /**
     * @return the idCaisseMetier
     */
    public String getIdCaisseMetier() {
        return idCaisseMetier;
    }

    /**
     * @param idCaisseMetier the idCaisseMetier to set
     */
    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }
}

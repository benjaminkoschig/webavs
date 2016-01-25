package ch.globaz.al.business.adapters.prestation;

import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.models.droit.TarifAggregator;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;

/***
 * Contient une liste de {@link EnteteAndPrestationAdapter}. Celle-ci est généré grâce au searchModel passé dans le
 * constructeur
 * 
 * @author age
 * 
 */
public final class PrestationHolder {
    /**
     * Classe interne définissant une représentation sous forme de composition de la classe
     * {@link EnteteAndDetailPrestationComplexModel}, permettant ainsi d'éviter des traitements au sein de la page JSP
     * et pouvoir potentiellement utiliser les EL.
     * 
     * @author age
     * 
     */
    public final class EnteteAndPrestationAdapter {
        private final List<DetailPrestationModel> detailsPrestations = new ArrayList<DetailPrestationModel>();
        private final EntetePrestationModel entetePrestation;
        private final RecapitulatifEntrepriseModel recap;

        /***
         * La construction d'un modèle de ce type passe obligatoirement par la création d'un
         * {@link EntetePrestationModel} et {@link RecapitulatifEntrepriseModel}
         * 
         * @param recapModel
         * @param entetePrestationModel
         */
        public EnteteAndPrestationAdapter(RecapitulatifEntrepriseModel recapModel,
                EntetePrestationModel entetePrestationModel) {
            entetePrestation = entetePrestationModel;
            recap = recapModel;
        }

        public void add(DetailPrestationModel detailPrestation) {
            detailsPrestations.add(detailPrestation);
        }

        /***
         * Redéfinition afin que la méthode {@link List#contains(Object)} puisse retrouver si une entete est déjà
         * présente afin de pouvoir la récupérer et lui ajouter un {@link DetailPrestationModel}.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof EnteteAndPrestationAdapter) {
                EnteteAndPrestationAdapter prestation = (EnteteAndPrestationAdapter) obj;
                return entetePrestation.getId().equals(prestation.entetePrestation.getId());
            }
            return false;
        }

        /***
         * Recherche du tarif associé à la prestation, en se basant sur les informations contenus dans les
         * {@link DetailPrestationModel}
         * 
         * @return un String représentant le code système du tarif, "0" lorsque le tarif n'est pas mentionné en base de
         *         données, "*" (étoile) lorsque que l'on retrouve plusieurs tarifs appliqué à une prestation
         */
        public String getCategorieTarif() {
            return TarifAggregator.calculerTarif(getCategorieTarifs());
        }

        /***
         * Retourne une liste de String contenant les tarifs contenus dans la prestation
         */
        private List<String> getCategorieTarifs() {
            List<String> tarifs = new ArrayList<String>();
            for (DetailPrestationModel detailPrestation : detailsPrestations) {
                tarifs.add(detailPrestation.getCategorieTarif());
            }
            return tarifs;
        }

        public List<DetailPrestationModel> getDetailsPrestations() {
            return detailsPrestations;
        }

        public EntetePrestationModel getEntetePrestation() {
            return entetePrestation;
        }

        public RecapitulatifEntrepriseModel getRecap() {
            return recap;
        }
    }

    private List<EnteteAndPrestationAdapter> prestationsAdapter = new ArrayList<EnteteAndPrestationAdapter>();

    public PrestationHolder() {
        this(null);
    }

    public PrestationHolder(EnteteAndDetailPrestationComplexSearchModel searchModel) {
        if (searchModel != null) {
            prestationsAdapter = parseSearchModel(searchModel);
        } else {
            prestationsAdapter = new ArrayList<PrestationHolder.EnteteAndPrestationAdapter>();
        }
    }

    public List<EnteteAndPrestationAdapter> getPrestationsAdapter() {
        return prestationsAdapter;
    }

    public int getSize() {
        return prestationsAdapter.size();
    }

    /***
     * Génération d'un modèle sous forme de liste de {@link PrestationHolder} à partir du searchModel
     * 
     * @param searchModel
     * @return
     */
    private List<EnteteAndPrestationAdapter> parseSearchModel(EnteteAndDetailPrestationComplexSearchModel searchModel) {
        List<EnteteAndPrestationAdapter> prestationsAdapter = new ArrayList<EnteteAndPrestationAdapter>();

        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            EnteteAndDetailPrestationComplexModel complexModel = (EnteteAndDetailPrestationComplexModel) model;
            EntetePrestationModel entetePrestation = complexModel.getEntetePrestationModel();
            DetailPrestationModel prestation = complexModel.getDetailPrestationModel();
            RecapitulatifEntrepriseModel recap = complexModel.getRecapModel();

            EnteteAndPrestationAdapter prestationAdapter = new EnteteAndPrestationAdapter(recap, entetePrestation);
            // Si l'entrée est déjà présente dans la liste, on reprend l'entrée
            if (prestationsAdapter.contains(prestationAdapter)) {
                EnteteAndPrestationAdapter retrievePrestation = prestationsAdapter.get(prestationsAdapter
                        .indexOf(prestationAdapter));
                retrievePrestation.add(prestation);
            } else {
                prestationAdapter.add(prestation);
                prestationsAdapter.add(prestationAdapter);
            }
        }
        return prestationsAdapter;
    }
}

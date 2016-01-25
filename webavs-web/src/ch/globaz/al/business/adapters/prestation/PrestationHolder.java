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
 * Contient une liste de {@link EnteteAndPrestationAdapter}. Celle-ci est g�n�r� gr�ce au searchModel pass� dans le
 * constructeur
 * 
 * @author age
 * 
 */
public final class PrestationHolder {
    /**
     * Classe interne d�finissant une repr�sentation sous forme de composition de la classe
     * {@link EnteteAndDetailPrestationComplexModel}, permettant ainsi d'�viter des traitements au sein de la page JSP
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
         * La construction d'un mod�le de ce type passe obligatoirement par la cr�ation d'un
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
         * Red�finition afin que la m�thode {@link List#contains(Object)} puisse retrouver si une entete est d�j�
         * pr�sente afin de pouvoir la r�cup�rer et lui ajouter un {@link DetailPrestationModel}.
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
         * Recherche du tarif associ� � la prestation, en se basant sur les informations contenus dans les
         * {@link DetailPrestationModel}
         * 
         * @return un String repr�sentant le code syst�me du tarif, "0" lorsque le tarif n'est pas mentionn� en base de
         *         donn�es, "*" (�toile) lorsque que l'on retrouve plusieurs tarifs appliqu� � une prestation
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
     * G�n�ration d'un mod�le sous forme de liste de {@link PrestationHolder} � partir du searchModel
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
            // Si l'entr�e est d�j� pr�sente dans la liste, on reprend l'entr�e
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

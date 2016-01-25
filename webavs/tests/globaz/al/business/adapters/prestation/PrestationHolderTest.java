package globaz.al.business.adapters.prestation;

import globaz.jade.persistence.model.JadeAbstractModel;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.al.business.adapters.prestation.PrestationHolder;
import ch.globaz.al.business.models.droit.TarifAggregator;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.EnteteAndDetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;

public class PrestationHolderTest {

    public class PrestationHolderBuilder {
        private String[] categorieTarif;
        private String[] idDetail;
        private String[] idEntete;
        private int size;

        public PrestationHolderBuilder() {
            size = 0;
        }

        public PrestationHolder build() {
            JadeAbstractModel[] results = new JadeAbstractModel[size];

            for (int i = 0; i < size; i++) {
                EnteteAndDetailPrestationComplexModel enteteAndDetailPrestation = new EnteteAndDetailPrestationComplexModel();
                EntetePrestationModel entetePrestationModel = new EntetePrestationModel();
                DetailPrestationModel detailPrestationModel = new DetailPrestationModel();

                if ((idDetail != null) && (idDetail[i] != null)) {
                    detailPrestationModel.setId(idDetail[i]);
                }
                if ((idEntete != null) && (idEntete[i] != null)) {
                    entetePrestationModel.setId(idEntete[i]);
                }
                if ((categorieTarif != null) && (categorieTarif[i] != null)) {
                    detailPrestationModel.setCategorieTarif(categorieTarif[i]);
                }

                enteteAndDetailPrestation.setEntetePrestationModel(entetePrestationModel);
                enteteAndDetailPrestation.setDetailPrestationModel(detailPrestationModel);
                results[i] = enteteAndDetailPrestation;
            }

            EnteteAndDetailPrestationComplexSearchModel searchModel = new EnteteAndDetailPrestationComplexSearchModel();
            searchModel.setSearchResults(results);
            PrestationHolder prestationHolder = new PrestationHolder(searchModel);

            return prestationHolder;

        }

        public PrestationHolderBuilder withCategorieTarifCanton(String... categorieTarifCanton) {
            categorieTarif = categorieTarifCanton;
            return this;
        }

        public PrestationHolderBuilder withIdDetail(String... idDetail) {
            this.idDetail = idDetail;
            return this;
        }

        public PrestationHolderBuilder withidEntete(String... idEntete) {
            this.idEntete = idEntete;
            return this;
        }

        public PrestationHolderBuilder withSize(int size) {
            this.size = size;
            return this;
        }
    }

    @Test
    public void givenNothingWhenSizeShouldReturn0() {
        Assert.assertEquals(0, new PrestationHolderBuilder().build().getSize());
    }

    @Test
    public void givenOneElementInSearchModelWhenSizeShouldReturn1() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(1);
        builder.withidEntete(new String[] { "10" });
        builder.withIdDetail(new String[] { "10" });

        Assert.assertEquals(1, builder.build().getSize());
    }

    @Test
    public void givenTwoElementsWithDifferentsEntetesInSearchModelWhenSizeShouldReturn2() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(2);
        builder.withidEntete(new String[] { "10", "11" });
        builder.withIdDetail(new String[] { "10", "10" });

        Assert.assertEquals(2, builder.build().getSize());
    }

    @Test
    public void givenTwoElementsWithSameEntetesInSearchModelWhenGetTarifShouldReturnEmpty() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(2);
        builder.withidEntete(new String[] { "10", "10" });
        builder.withIdDetail(new String[] { "10", "11" });
        builder.withCategorieTarifCanton(new String[] { "600000", "600001" });

        Assert.assertEquals(TarifAggregator.TARIF_MULTIPLE, builder.build().getPrestationsAdapter().get(0)
                .getCategorieTarif());
    }

    @Test
    public void givenTwoElementsWithSameEntetesInSearchModelWhenGetTarifShouldReturnJU() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(2);
        builder.withidEntete(new String[] { "10", "10" });
        builder.withIdDetail(new String[] { "10", "11" });
        builder.withCategorieTarifCanton(new String[] { "600000", "600000" });

        Assert.assertEquals("600000", builder.build().getPrestationsAdapter().get(0).getCategorieTarif());
    }

    @Test
    public void givenTwoElementsWithSameEntetesInSearchModelWhenSizeShouldReturn1() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(2);
        builder.withidEntete(new String[] { "10", "10" });
        builder.withIdDetail(new String[] { "10", "11" });
        builder.withCategorieTarifCanton(new String[] { "600000", "600000" });

        Assert.assertEquals(1, builder.build().getSize());
        Assert.assertEquals(2, builder.build().getPrestationsAdapter().get(0).getDetailsPrestations().size());
    }

    @Test
    public void givenTwoElementsWithSameEntetesWithUndefinedCantonInSearchModelWhenGetTarifShouldReturnUndefined() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(2);
        builder.withidEntete(new String[] { "10", "10" });
        builder.withIdDetail(new String[] { "10", "11" });
        builder.withCategorieTarifCanton(new String[] { "0", "0" });

        Assert.assertEquals(TarifAggregator.TARIF_UNDEFINED, builder.build().getPrestationsAdapter().get(0)
                .getCategorieTarif());
    }

    @Test
    public void givenTwoElementsWithSameEntetesWithUnTarifForceAndUnTarifIndefiniInSearchModelWhenGetTarifShouldReturnUndefined() {
        PrestationHolderBuilder builder = new PrestationHolderBuilder();
        builder.withSize(2);
        builder.withidEntete(new String[] { "10", "10" });
        builder.withIdDetail(new String[] { "10", "11" });
        builder.withCategorieTarifCanton(new String[] { "10", "0" });

        Assert.assertEquals(TarifAggregator.TARIF_MULTIPLE, builder.build().getPrestationsAdapter().get(0)
                .getCategorieTarif());
    }
}

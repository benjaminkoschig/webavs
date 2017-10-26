package ch.globaz.pegasus.rpc.plausi.core;

import static org.fest.assertions.api.Assertions.*;
import java.util.List;
import org.junit.Test;

public class PlausisResultsTest {

    @Test
    public void testIsAllPlausiOkEmpty() throws Exception {
        PlausisResults plausisResults = new PlausisResults();
        assertThat(plausisResults.isEmpty()).isTrue();
    }

    @Test
    public void testIsAllPlausiOkTrue() throws Exception {
        PlausisResults plausisResults = new PlausisResults();
        plausisResults.add(buildPlausi(true));
        assertThat(plausisResults.isAllPlausiOk()).isTrue();
    }

    @Test
    public void testIsAllPlausiOkFalse() throws Exception {
        PlausisResults plausisResults = new PlausisResults();
        plausisResults.add(buildPlausi(false));
        assertThat(plausisResults.isAllPlausiOk()).isFalse();
    }

    @Test
    public void testFiltrePlausiKo() throws Exception {
        PlausisResults plausisResults = new PlausisResults();
        plausisResults.add(buildPlausi(false));
        plausisResults.add(buildPlausi(true));
        plausisResults.add(buildPlausi(false));
        plausisResults.add(buildPlausi(false));
        plausisResults.add(buildPlausi(true));
        assertThat(plausisResults.filtrePlausiKo().getPlausis()).hasSize(3);
    }

    private PlausiResult buildPlausi(final boolean isValide) {
        return new PlausiResult() {

            @Override
            public boolean isValide() {
                return isValide;
            }

            @Override
            public RpcPlausi getPlausi() {
                return new RpcPlausi() {

                    @Override
                    public RpcPlausiType getType() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public String getID() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public String getReferance() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public RpcPlausiCategory getCategory() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public List getApplyTo() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                };
            }
        };
    }

}

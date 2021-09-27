package ch.globaz.common.ws;


import acor.xsd.standard.error.StandardError;
import globaz.prestation.acor.web.ws.AcorStandardErrorUtil;
import ch.globaz.common.ws.configuration.WSExceptionMapper;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.prestation.acor.PRACORException;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;


public class WSExceptionMapperTest {

    @Test
    public void testReponseExceptionPRACOR() {
        try {
            WSExceptionMapper mapper = new WSExceptionMapper();
            PRACORException exception = new PRACORException("test");
            Response resultat = mapper.toResponse(exception);
            final StandardError entity = (StandardError) resultat.getEntity();
            assertThat(entity.getLevel()).isEqualTo(2);
            assertThat(entity.getLabelId()).isEqualTo("test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReponseExceptionGenerique() {
        try {
            WSExceptionMapper mapper = new WSExceptionMapper();
            RETechnicalException exception = new RETechnicalException("test");
            Response resultat = mapper.toResponse(exception);
            final StandardError entity = (StandardError) resultat.getEntity();
            assertThat(entity.getLevel()).isEqualTo(1);
            assertThat(entity.getLabelId()).isEqualTo(AcorStandardErrorUtil.ERROR_ACOR_EXPORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

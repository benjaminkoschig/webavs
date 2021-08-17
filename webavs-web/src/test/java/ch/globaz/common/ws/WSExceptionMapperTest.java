package ch.globaz.common.ws;


import acor.rentes.xsd.standard.error.StandardError;
import ch.globaz.common.acor.Acor2020StandardErrorUtil;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.prestation.acor.PRACORException;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class WSExceptionMapperTest {

    @Test
    public void testReponseExceptionPRACOR(){
        WSExceptionMapper mapper = new WSExceptionMapper();
        PRACORException exception = new PRACORException("test");
        Response resultat = mapper.toResponse(exception);
        final StandardError entity = (StandardError) resultat.getEntity();
        assertThat(entity.getLevel()).isEqualTo(2);
        assertThat(entity.getLabelId()).isEqualTo("test");
    }

    @Test
    public void testReponseExceptionGenerique(){
        WSExceptionMapper mapper = new WSExceptionMapper();
        RETechnicalException exception = new RETechnicalException("test");
        Response resultat = mapper.toResponse(exception);
        final StandardError entity = (StandardError) resultat.getEntity();
        assertThat(entity.getLevel()).isEqualTo(1);
        assertThat(entity.getLabelId()).isEqualTo(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_IMPORT_UNKOWN);
    }

}
package globaz.prestation.acor.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import acor.rentes.xsd.standard.error.StandardError;
import ch.globaz.common.ws.FilterMapper;
import ch.globaz.common.ws.configuration.JacksonJsonProvider;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

//@Provider
//@PreMatching
@Slf4j
public abstract class AcorTokenFilterAbstract implements FilterMapper{

    public abstract Acor2020TokenService<Acor2020Token> getInstanceTokenConverter();

    public void doFilter(final ServletRequest request, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tokenJson = httpRequest.getHeader("authorization");
        Acor2020Token token = getInstanceTokenConverter().convertToken(tokenJson);

        if (Objects.isNull(token)) {
            LOG.error(Acor2020StandardErrorUtil.TOKEN_INVALIDE);

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType(MediaType.APPLICATION_JSON_TYPE.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            PrintWriter out = response.getWriter();
            StandardError standardError = Acor2020StandardErrorUtil.getStandardError(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN);
            out.print(JacksonJsonProvider.getInstance().writeValueAsString(standardError));
            out.flush();
        }

        if (token != null) {
            initSessionAndDoAction(request, servletResponse, chain, token.getUserId());
        }
    }
}

package globaz.corvus.acor2020.ws;

import acor.rentes.xsd.standard.error.OriginType;
import acor.rentes.xsd.standard.error.StandardError;
import ch.globaz.common.acor.Acor2020StandardErrorUtil;
import ch.globaz.common.ws.FilterMapper;
import ch.globaz.common.ws.configuration.JacksonJsonProvider;
import globaz.corvus.acor2020.ws.token.REAcor2020Token;
import globaz.corvus.acor2020.ws.token.REAcor2020TokenService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

//@Provider
//@PreMatching
@Slf4j
public class REAcorTokenFilter implements FilterMapper {

    @Override
    public boolean isFilterable(final HttpServletRequest request) {
        return request.getPathInfo().contains("/acor2020/");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean doChain = true;
        if (!Objects.equals(HttpMethod.OPTIONS, httpRequest.getMethod())) {
            String token = httpRequest.getHeader("authorization");
            REAcor2020Token acor2020Token = REAcor2020TokenService.getInstance().getToken(token);
            if (Objects.isNull(acor2020Token)) {
                doChain = false;
                LOG.error(Acor2020StandardErrorUtil.TOKEN_INVALIDE);

                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setContentType(MediaType.APPLICATION_JSON_TYPE.toString());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                PrintWriter out = response.getWriter();
                StandardError standardError = Acor2020StandardErrorUtil.getStandardError(Acor2020StandardErrorUtil.ERROR_ACOR_EXTERN_TOKEN_INVALID, null, 2, OriginType.TECHNICAL_TOKEN);
                out.print(JacksonJsonProvider.getInstance().writeValueAsString(standardError));
                out.flush();
            }
        }
        if (doChain) {
            chain.doFilter(request, servletResponse);
        }
    }
}

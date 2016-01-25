package globaz.aquila.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public abstract class COActionRecouvrementAbstract extends FWDefaultServletAction {

    public COActionRecouvrementAbstract(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Parse les param�tres de la requ�te en permettant l'utilisation de param�tres donn�s sous la forme d'un tableau �
     * deux dimensions
     * Les param�tres dont le nom correspond � l'expression <code>parametersPattern</code> sont enregistr� dans une Map
     * 
     * Exemple :
     * 
     * expression : (postes\\[)([0-9]+)(\\]\\[)([a-zA-Z]+)(\\])
     * 
     * Param�tres :
     * 
     * postes[0][param1]=valeur1
     * postes[0][param2]=valeur2
     * postes[1][param1]=valeur3
     * postes[1][param2]=valeur4
     * 
     * R�sultat :
     * Map {
     * &nbsp;&nbsp;&nbsp;&nbsp;0={
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;param1= valeur1,
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;param2= valeur2,
     * &nbsp;&nbsp;&nbsp;&nbsp;},
     * &nbsp;&nbsp;&nbsp;&nbsp;1={
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;param1= valeur3,
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;param2= valeur4,
     * &nbsp;&nbsp;&nbsp;&nbsp;},
     * }
     * 
     * @param request la requ�te � traiter
     * @param parametersPattern expression r�guli�re
     * @return Map contenant les param�tres pars�s
     * 
     * @throws ServletException
     * @throws IOException
     */
    protected Map<String, Map<String, String>> getParametersMap(HttpServletRequest request, String parametersPattern)
            throws ServletException, IOException {

        Pattern pattern = Pattern.compile(parametersPattern);
        Map<String, Map<String, String>> params = new LinkedHashMap<String, Map<String, String>>();

        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements();) {

            String paramName = names.nextElement();
            String paramValue = request.getParameter(paramName);

            Matcher matcher = pattern.matcher(paramName);
            if (matcher.find()) {

                String key = matcher.group(2);
                String name = matcher.group(4);

                Map<String, String> param = params.get(key);
                if (param == null) {
                    param = new HashMap<String, String>();
                }

                param.put(name, paramValue);
                params.put(key, param);
            }
        }

        return params;
    }

    /**
     * Initialise un BigDecimal pour une valeur. Si <code>value</code> est vide, la valeur 0.0 est initialis�e
     * 
     * @param value la valeur pour laquelle g�n�rer un BigDecimal
     * @return BigDecimal correspondant � <code>value</code>
     */
    protected BigDecimal getBigDecimal(String value) {

        if (JadeStringUtil.isBlank(value)) {
            return new BigDecimal("0.0");
        } else {
            BigDecimal bigDecimal = new BigDecimal(JANumberFormatter.deQuote(value).trim());
            return bigDecimal;
        }
    }
}

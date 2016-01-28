<%@page import="globaz.fx.vb.process.FXJadeProcessAjaxViewBean"%>
<%@page import="ch.globaz.jade.process.business.models.SimpleExecutionProcess"%>
<%@page import="ch.globaz.jade.process.business.IJadeProcess"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
FXJadeProcessAjaxViewBean viewBean=(FXJadeProcessAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
%>
    <liste> 
        <%
        String idGroup=null; 
        for(JadeAbstractModel model: viewBean.getSearchModel().getSearchResults()){
            SimpleExecutionProcess line=((SimpleExecutionProcess)model);
        %>
            <tr idEntity="<%=line.getId()%>">
                <td><%=line.getDate()%></td>
                <td><%=line.getCsEtat().toLabel()%></td>
                <td><%=line.getIdCurrentStep()%></td>
            </tr>
        <%}%>
    </liste>
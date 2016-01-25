<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	actionNew  = servletContext + mainServletPath + "?userAction=pyxis.adressecourrier.adresseutilisee.afficher";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressecourrier.adresse.customFind";
</SCRIPT>
<%
	globaz.pyxis.db.adressecourrier.TIAdresseUtiliseeViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIAdresseUtiliseeViewBean)session.getAttribute ("viewBean");
%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Cronistoria di un indirizzo di corrispondenza
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

	<TR>
            <TD nowrap width="130">Indirizzo</TD>
            <td><INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getRue()+" "+viewBean.getNumeroRue()%>" class="libelleLongDisabled" readonly></td>


		</TR>
		<TR>
		<TD nowrap width="130"></TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
		<INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>' >
		<input type="hidden" name="reqCritere" value="">
		<input type="hidden" name="reqLibelle" value="<%=request.getParameter("selectedId")%>">
		<input type="hidden" name="inclureHistorique" value="on">
	

	     </TD>
          </TR>

	 
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
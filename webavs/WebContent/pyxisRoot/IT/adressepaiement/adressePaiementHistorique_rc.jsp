<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	actionNew =  servletContext + mainServletPath + "?userAction=userAction=pyxis.adressecourrier.adresseutilisee.afficher";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressepaiement.adressePaiement.customFind";
</SCRIPT>
<%
	globaz.pyxis.db.adressepaiement.TIAdressePaiementUtiliseeViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAdressePaiementUtiliseeViewBean )session.getAttribute ("viewBean");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Historique d'une adresse de paiement<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

		<TR>
            
			 <TD >
				<textarea rows="3" cols="100" style="overflow : hidden;background-color : #b3c4db;border: 0" readonly><%=viewBean.getDescription()%></textarea>
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
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
idEcran ="GTI0026";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressepaiement.adressePaiementUtilisee.lister";
bFind = true;

</SCRIPT>
<%
	globaz.pyxis.db.adressepaiement.TIAdressePaiementUtiliseeViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAdressePaiementUtiliseeViewBean)session.getAttribute ("viewBean");
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Adresse de paiement utilisée par<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<TR>
            <TD >
								<textarea rows="3" cols="100" style="overflow : hidden;background-color : #b3c4db;border: 0" readonly><%=viewBean.getDescription()%></textarea>
								<INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>' >
								<INPUT type="hidden" name="forIdAdressePaiement" value='<%=viewBean.getIdAdressePaiement()%>' >
								

								
		
							</TD>
          </TR>
		 			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>
<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
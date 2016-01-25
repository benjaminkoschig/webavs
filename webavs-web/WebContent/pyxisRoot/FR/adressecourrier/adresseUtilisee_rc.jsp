<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
idEcran ="GTI0022";
bButtonNew = false;
bButtonFind = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressecourrier.adresseUtilisee.lister";
bFind = true;
</SCRIPT>
<%
	globaz.pyxis.db.adressecourrier.TIAdresseViewBean viewBean = (globaz.pyxis.db.adressecourrier.TIAdresseViewBean)session.getAttribute ("viewBean");
%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Adresse de courrier utilisée par
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

	<TR>
            <TD nowrap width="130">Adresse</TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getRue()+" "+viewBean.getNumeroRue()%>" class="libelleLongDisabled" readonly>
	     </TD>
	    		</TR>
		<TR>
		<TD nowrap width="130"></TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
		<INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>' >
		<INPUT type="hidden" name="forIdAdresse" value='<%=viewBean.getIdAdresse()%>' >
		<!--
		<INPUT type="hidden" name="forDefautAdresse" value='null' >
		-->	

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
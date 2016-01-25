<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP1039";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection"); 
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Cotisation - Paramétrage Sedex par canton"
usrAction = "phenix.divers.parametreCanton.lister";
servlet = "phenix";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Parametrierung Kanton/Parameter<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
	  <TR> 
            <TD nowrap width="100"></TD>
            <TD nowrap colspan="5"></TD>
          </TR>
	  <TR>
            <TD nowrap width="120">Kanton</TD>
           <TD nowrap>
			<%
				java.util.HashSet except = new java.util.HashSet();
				except.add(IConstantes.CS_LOCALITE_ETRANGER);
			%>
			<ct:FWCodeSelectTag name="forCanton"
		        defaut=""
				wantBlank="<%=true%>"
		        codeType="PYCANTON"
				libelle="codeLibelle"
				except="<%=except%>"
			/>
			</TD>
			<TD nowrap width="100"></TD>
	      <TD width="120">Funktionalität</TD>
	      <TD nowrap>
			<ct:FWCodeSelectTag name="forTypeParametre"
		        defaut=""
				wantBlank="<%=true%>"
		        codeType="CPPARMCANT"
				libelle="libelle"
			/>
			</TD>
        </TR>
         
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
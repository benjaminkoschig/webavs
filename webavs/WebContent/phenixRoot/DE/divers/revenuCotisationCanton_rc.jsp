<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@page import="globaz.phenix.db.communications.CPReceptionReaderViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP4017";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection"); 
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Cotisation - Paramétrage revenu avec cotisation par canton"
usrAction = "phenix.divers.revenuCotisationCanton.lister";
servlet = "phenix";
bFind = false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Einrichtungsgebühr Einkommen mit nach Kanton<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
	  <TR>
            <TD nowrap width="160">Kanton</TD>
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
	      <TD width="50"></TD>
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
<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CLA0005";
	LAInsertionFichierViewBean viewBean = (LAInsertionFichierViewBean) request.getAttribute("viewBean");
	actionNew = "lacerta?userAction=lacerta.fichier.suiviCaisse.afficherSuivi";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="db.LAInsertionFichierViewBean"%>

<SCRIPT language="JavaScript">
	usrAction = "lacerta.fichier.suiviCaisse.listerSuivi";
	bFind = true;
	
	function onClickNew(){
	}
	
	function onClickFind(){}
	detailLink = "lacerta?userAction=lacerta.fichier.suiviCaisse.afficherSuivi&selectedId=";
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suivi des caisses<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<tr> 
        <td><b>Tiers</b></td> 
        <td><%=viewBean.getNom() + " "+ viewBean.getPrenom() %></td>
        <td></td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
   </tr>
   <tr> 
            <td>N° AVS</td>
            <td>
             	<%="   "+viewBean.getNumAvs()%>
            </td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
   </tr>
   <tr>
        <td><b>Courrier</b></td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
        <td><%=viewBean.getAdresseCRue() + " "+viewBean.getAdresseCNumero() %></td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
        <td><%=viewBean.getLocaliteCCode()+" "+viewBean.getLocaliteC() %></td>
   </tr>
   <tr>
        <td><b>Domicile</b></td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
        <td><%=viewBean.getAdresseDRue() + " "+viewBean.getAdresseDNumero() %></td>
   </tr>
   <tr>
   		<td>&nbsp;</td>
         <td><%=viewBean.getLocaliteDCode()+" "+viewBean.getLocaliteD() %></td>
   </tr>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>
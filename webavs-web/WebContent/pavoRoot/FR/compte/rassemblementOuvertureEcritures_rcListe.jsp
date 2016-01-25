<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
   globaz.pavo.db.compte.CIRassemblementOuvertureEcrituresListViewBean viewBean = (globaz.pavo.db.compte.CIRassemblementOuvertureEcrituresListViewBean)request.getAttribute ("viewBean");
    //CICompteIndividuelViewBean viewBeanFK = (CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <Th width="">Affilié ou partenaire</Th>
    <Th width="">Genre</Th>
    <Th width="">Période</Th>
    <Th width="">Montant</Th>
    <Th width="">Code</Th>
    <Th width="">Date inscr./clôture</Th>
    
    <%
    	size = viewBean.getSize();
    %>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
 <%
 globaz.pavo.db.compte.CIRassemblementOuvertureEcritures line = (globaz.pavo.db.compte.CIRassemblementOuvertureEcritures)viewBean.getEntity(i);
 %>
 
	 <td class="mtd" >
	 	<%=line.getEcriture().getNoNomEmployeur()%>
	 </td>
	 <td class="mtd" >
	 	<%=line.getEcriture().getGreFormat()%>
	 </td>
	 <td class="mtd" align="right">
	 	<%=line.getEcriture().getMoisDebutPad()+"-"+line.getEcriture().getMoisFinPad()+"."+line.getEcriture().getAnnee()%>
	 </td>
	<td class="mtd" align="right">
	 	<%=line.getEcriture().getMontantSigne()%>
	 </td>
	 <td class="mtd" >
	 	<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(line.getEcriture().getCode(),session)%>
	 </td>
	 <td class="mtd" >
	 	<%=line.getEcriture().getDateInscription()%>
	 </td>
	 
	 
 
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%	
	globaz.hermes.db.gestion.HERassemblementListViewBean viewBean = (globaz.hermes.db.gestion.HERassemblementListViewBean)request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "hermes?userAction=hermes.parametrage.attenteRetourCI.chercher&selectedId=";
  	menuName = "rassemblement-detail";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH width="16">&nbsp;</TH>
	<TH>NSS</TH>
    <TH>Caisse</TH>
    <TH>Date Réception</TH>	
	<TH>Date Rappel</TH>
	<TH width="8">Reçu</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
	
    <%
		globaz.hermes.db.gestion.HERassemblementViewBean line = (globaz.hermes.db.gestion.HERassemblementViewBean)viewBean.getEntity(i);
		String optionString = "'"+line.getIdAnnonceRetour();
		String img;
		
	if(line.isAnnonceRecue()){
		actionDetail = targetLocation  + "='" + detailLink +line.getIdAnnonceRetour() +"&refUnique="+line.getReferenceUnique()+"&caisse="+line.getNumCaisse() + "&isArchivage="+line.isArchivage()+ "'";
		if(!line.isAnnonceAttendue()){
			actionDetail = targetLocation  + "='" + detailLink +line.getIdAnnonceDepart() +"&refUnique="+line.getReferenceUnique()+"&caisse="+line.getNumCaisse() + "&isArchivage="+line.isArchivage()+ "'";
			img = "information.gif";%>
			<TD class="mtd" width="16">&nbsp;</TD>
		<%}else{
			img = "ok.gif";
		%>
			<TD class="mtd" width="">
    			<ct:menuPopup menu="HE-rassemblementDetail" label="<%=optionsPopupLabel%>" target="top.fr_main">
					<ct:menuParam key="selectedId" value="<%=line.getIdAnnonceDepart()%>"/>
					<ct:menuParam key="refUnique" value="<%=line.getReferenceUnique()%>"/>
					<ct:menuParam key="isArchivage" value="<%=String.valueOf(line.isArchivage())%>"/>
					<ct:menuParam key="caisse" value="<%=line.getNumCaisse()%>"/>		
     			</ct:menuPopup>   
    		</TD>
		<%}
	}else{
		actionDetail = targetLocation +"='hermes?userAction=hermes.gestion.rassemblement.afficher&selectedId="+line.getIdAttenteRetour()+ "&isArchivage="+line.isArchivage()+ "'";
		img = "erreur.gif";
	%>
		<TD class="mtd" width="16">&nbsp;</TD>	
	<%
	}
	
	%>
	<%-- <td class="mtd" onClick="<%=actionDetail%>"><%=globaz.hermes.utils.AVSUtils.formatAVS8Or9(line.getNumAVS())%></td>--%>
	<td class="mtd" onClick="<%=actionDetail%>"><%=(line.getNumAVS().equals(""))?"&nbsp;": line.getNumAvsWithDots()%></td>		
    <td class="mtd" onClick="<%=actionDetail%>"><%=line.getNumCaisse()%></td>			
    <td class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getDateReceptionCI())?"&nbsp":line.getDateReceptionCI()%></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=line.getDateDernierRappel().equals("0")?"&nbsp;":line.getDateDernierRappel()%></td>	
    <td class="mtd" onClick="<%=actionDetail%>"><%= "<img src=\""+request.getContextPath()+"/images/"+img+"\" border=\"0\">"%></td>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
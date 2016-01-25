 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
detailLink = "hermes?userAction=hermes.gestion.lot.afficher&selectedId=";
globaz.hermes.db.gestion.HELotListViewBean viewBean = (globaz.hermes.db.gestion.HELotListViewBean)request.getAttribute("viewBean");
size = viewBean.size();
//menuName = "lot-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th width="16">&nbsp;</Th>
    <th>Numéro</th>
    <th>Date centrale</th>
    <th>Date traitement</th>
    <th>Heure traitement</th>
    <th>Type</th>
    <th>Priorité</th>
    <th>Nb. ARC</th>
    <th>Etat</th>
    <th>Quittance</th>	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		globaz.hermes.db.gestion.HELotViewBean line = (globaz.hermes.db.gestion.HELotViewBean)viewBean.getEntity(i);
		//actionDetail = targetLocation  + "='" + detailLink + line.getIdLot() + "&nbAnnonces="+line.getNbAnnonces()+"'";
		actionDetail = targetLocation  + "='" + "hermes?userAction=hermes.gestion.lot.chooseType&selectedId=" + line.getIdLot() + "&dateLot="+line.getDateTraitement()+"&nbAnnonces="+line.getNbAnnonces()+"&isArchivage="+line.isArchivage()+"'";
		//String optionString = "'"+line.getIdLot()+"&nbAnnonces="+line.getNbAnnonces()+"&="+line.isArchivage();
	%>
    <TD class="mtd" width="">
    	<ct:menuPopup menu="HE-lotDetail" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdLot()%>"/>
			<ct:menuParam key="nbAnnonces" value="<%=String.valueOf(line.getNbAnnonces())%>"/>
			<ct:menuParam key="isArchivage" value="<%=String.valueOf(line.isArchivage())%>"/>
			<ct:menuParam key="dateLot" value="<%=line.getDateCentrale()%>"/>
     	</ct:menuPopup>   
    </TD>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getIdLot().equals(""))?"&nbsp;":line.getIdLot() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getDateCentraleLibelle().equals(""))?"&nbsp;":line.getDateCentraleLibelle() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getDateTraitementLibelle().equals(""))?"&nbsp;":line.getDateTraitementLibelle() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getHeureTraitement().equals(""))?"&nbsp;":line.getHeureTraitement() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getTypeLibelle().equals(""))?"&nbsp;":line.getTypeLibelle() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getPrioriteLibelle().equals(""))?"&nbsp;":line.getPrioriteLibelle() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= line.getNbAnnonces() %></td>
    <td class="mtd" onClick="<%=actionDetail%>"><%= (line.getEtatLibelle().equals(""))?"&nbsp;":line.getEtatLibelle() %></td>
	<td class="mtd" onClick="<%=actionDetail%>"><%= line.getQuittance().equals("1")?"<img src=\""+request.getContextPath()+"/images/ok.gif\" border=\"0\">":"<img src=\""+request.getContextPath()+"/images/verrou.gif\" border=\"0\">"%></td>	
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
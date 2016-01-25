
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
	detailLink = "hermes?userAction=hermes.gestion.rappel.afficher&selectedId=";	
	globaz.hermes.db.gestion.HERappelListViewBean viewBean = (globaz.hermes.db.gestion.HERappelListViewBean)request.getAttribute("viewBean");
	size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th>&nbsp;</Th>
	<Th>MZR-Nr.</Th>    
	<Th>Datum</Th>
    <Th>SVN</Th>
	<Th>SZ</Th>
	<Th>Kassen-Nr.</Th>	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		globaz.hermes.db.gestion.HERappelViewBean line = (globaz.hermes.db.gestion.HERappelViewBean)viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdAttenteRetour() + "'";
	%>
    <TD class="mtd" width="">
    	<ct:menuPopup menu="HE-rappelDetail" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdAttenteRetour()%>"/>
     	</ct:menuPopup>   
    </TD>
    <td class="mtd" <ct:ifhasright element="hermes.gestion.rappel.afficher" crud="u">onClick="<%=actionDetail%>"</ct:ifhasright>><%=line.getReferenceUnique()%></td>	
    <td class="mtd" <ct:ifhasright element="hermes.gestion.rappel.afficher" crud="u">onClick="<%=actionDetail%>"</ct:ifhasright>><%=line.getDate()%></td>
	<td class="mtd" <ct:ifhasright element="hermes.gestion.rappel.afficher" crud="u">onClick="<%=actionDetail%>"</ct:ifhasright>><%=(line.getNumeroAvs().equals(""))?"&nbsp;": line.getNumeroAvs()%></td>
    <td class="mtd" <ct:ifhasright element="hermes.gestion.rappel.afficher" crud="u">onClick="<%=actionDetail%>"</ct:ifhasright>><%=line.getMotif()%></td>
    <td class="mtd" <ct:ifhasright element="hermes.gestion.rappel.afficher" crud="u">onClick="<%=actionDetail%>"</ct:ifhasright>><%=line.getNumeroCaisse()%></td>			
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
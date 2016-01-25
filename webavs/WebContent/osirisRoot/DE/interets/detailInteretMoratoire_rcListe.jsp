<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%
CADetailInteretMoratoireListViewBean viewBean = (CADetailInteretMoratoireListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.getSize();
detailLink = "osiris?userAction=osiris.interets.detailInteretMoratoire.afficher&selectedId=";
target="parent.fr_detail";
targetLocation = target + ".location.href";
String parentLocation = "parent.fr_detail";

//menuName = "CA-InteretsMoratoires";

globaz.osiris.db.interets.CADetailInteretMoratoire _line = null; 
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<th>Jahr</th>
    <th>Von</th>
    <th>Bis</th>
    <th>Anzahl Tage</th>
    <th>Betrag</th>
    <th>Zinssatz</th>
    <th>Berechneter Verzugszins</th>
    <th>Verzugszins</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%	
    	_line = (globaz.osiris.db.interets.CADetailInteretMoratoire) viewBean.getEntity(i);
    	actionDetail = parentLocation+".location.href='"+detailLink+_line.getIdDetailInteretMoratoire()+"&domaine="+_line.getDomaine()+"'";
    	String styleError = "";

    	//vérification des montants
    	if(!_line.getInteretCalcule().equalsIgnoreCase(_line.getMontantInteret()))
    		styleError = "color:red; font-weight:bold";
    %>
    <td style="vertical-align: middle; text-align: center;" class="mtd" onclick="<%=actionDetail%>"><%= "0".equals(_line.getAnneeCotisation()) ? " " : _line.getAnneeCotisation()%></td>
	<td style="vertical-align: middle; text-align: center;" class="mtd" onclick="<%=actionDetail%>"><%=_line.getDateDebut()%></td>
	<td style="vertical-align: middle; text-align: center;" class="mtd" onclick="<%=actionDetail%>"><%=_line.getDateFin()%></td>
	<td style="vertical-align: middle; text-align: center;" class="mtd" onclick="<%=actionDetail%>"><%=_line.getNbJours()%></td>
	<td style="vertical-align: middle; text-align: right;" class="mtd" onclick="<%=actionDetail%>"><%=_line.getMontantSoumis()%></td>
	<td style="vertical-align: middle; text-align: right;" class="mtd" onclick="<%=actionDetail%>"><%=_line.getTaux()%></td>		
	<td style="vertical-align: middle; text-align: right; <%=styleError%>" class="mtd" onclick="<%=actionDetail%>"><%=_line.getInteretCalcule()%></td>
	<td style="vertical-align: middle; text-align: right; <%=styleError%>" class="mtd" onclick="<%=actionDetail%>"><%=_line.getMontantInteret()%></td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
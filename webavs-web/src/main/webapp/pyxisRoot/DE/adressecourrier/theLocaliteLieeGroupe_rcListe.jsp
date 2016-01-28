 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.vb.adressecourrier.TITheLocaliteLieeGroupeListViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheLocaliteLieeGroupeListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();
	detailLink="pyxis?userAction=pyxis.adressecourrier.theLocaliteLieeGroupe.afficher&selectedId=";
   	session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
    <!--<TH width="40"> &nbsp;</TH> -->

    <Th width="16">&nbsp;</Th>
    <th>PLZ</th>
    <th>Ortschaft</th>
    <th>Land</th>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
<%	globaz.pyxis.db.adressecourrier.TILocaliteLieeAGroupe line =  (globaz.pyxis.db.adressecourrier.TILocaliteLieeAGroupe) viewBean.getEntity(i);
	actionDetail = targetLocation + "='" + detailLink + line.getIdLocationLieeAGroupe() + "'";%>
	<TD class="mtd" width="16" >
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressecourrier.theLocaliteLieeGroupe.afficher&selectedId="+line.getIdLocationLieeAGroupe();%>
	<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />
	</TD>
	<td onClick="<%=actionDetail%>" ><%=line.getLocalite().getNumPostal()%></td>
	<td onClick="<%=actionDetail%>" ><%=line.getLocalite().getLocalite()%></td>
	<td onClick="<%=actionDetail%>" ><%=line.getPays()%></td>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
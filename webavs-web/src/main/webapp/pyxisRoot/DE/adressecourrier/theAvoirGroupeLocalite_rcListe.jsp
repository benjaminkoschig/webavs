 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.vb.adressecourrier.TITheAvoirGroupeLocaliteListViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheAvoirGroupeLocaliteListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();
	detailLink="pyxis?userAction=pyxis.adressecourrier.theAvoirGroupeLocalite.afficher&selectedId=";
//	menuName="groupeLocalite";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
    <!--<TH width="40"> &nbsp;</TH> -->

    <Th width="16">&nbsp;</Th>
      <TH>Verbindungstyp</TH>
      <TH>Gruppentyp</TH>
      <TH>Gruppenname</TH>
      <TH>Beginndatum</TH>
      <TH>Enddatum</TH>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
<%	globaz.pyxis.db.adressecourrier.TIAvoirGroupeLocalite line =  (globaz.pyxis.db.adressecourrier.TIAvoirGroupeLocalite) viewBean.getEntity(i);
	actionDetail = targetLocation + "='" + detailLink + line.getIdAvoirGroupeLocation() + "'";
	String nom = "de".equals(objSession.getIdLangueISO())
					? line.getGroupeLocalite().getNomDe()
					: ("fr".equals(objSession.getIdLangueISO())
						? line.getGroupeLocalite().getNomFr()
						: line.getGroupeLocalite().getNomIt());%>
	<TD class="mtd" width="16" >
		<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressecourrier.theAvoirGroupeLocalite.afficher&selectedId="+line.getIdAvoirGroupeLocation();%>
		<ct:menuPopup menu="TIMenuVide" detailLabelId="Detail" detailLink="<%=url%>" />
	</TD>
	<td onClick="<%=actionDetail%>"><%="".equals(line.getCsLinkType())?"":objSession.getCodeLibelle(line.getCsLinkType())%></td>
	<td onClick="<%=actionDetail%>"><%="".equals(line.getGroupeLocalite().getCsType())?"":objSession.getCodeLibelle(line.getGroupeLocalite().getCsType())%></td>
	<td onClick="<%=actionDetail%>"><%=nom%></td>
	<td onClick="<%=actionDetail%>"><%=line.getStartDate()%></td>
	<td onClick="<%=actionDetail%>"><%=line.getEndDate()%></td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
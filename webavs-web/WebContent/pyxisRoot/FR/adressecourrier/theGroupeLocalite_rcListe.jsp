 
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
	globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteListViewBean viewBean = (globaz.pyxis.vb.adressecourrier.TITheGroupeLocaliteListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();
	detailLink="pyxis?userAction=pyxis.adressecourrier.theGroupeLocalite.afficher&selectedId=";
	menuName="groupeLocalite";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders"  --%> 
    <!--<TH width="40"> &nbsp;</TH> -->

    <Th width="16">&nbsp;</Th>
      <TH>Type</TH>
      <TH>Nom</TH>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList"  --%> 
<%	globaz.pyxis.db.adressecourrier.TIGroupeLocalite line =  (globaz.pyxis.db.adressecourrier.TIGroupeLocalite) viewBean.getEntity(i);
	actionDetail = targetLocation + "='" + detailLink + line.getIdGroupeLocation() + "'";%>
	<TD class="mtd" width="16" >
	
	
	<%String url = request.getContextPath()+"/pyxis?userAction=pyxis.adressecourrier.theGroupeLocalite.afficher&selectedId="+line.getIdGroupeLocation();%>
	<ct:menuPopup menu="groupeLocalite" detailLabelId="Detail" detailLink="<%=url%>" >
		<ct:menuParam key="selectedId" value="<%=line.getIdGroupeLocation()%>"/>
	</ct:menuPopup> 
	
	
	</TD>
	<td onClick="<%=actionDetail%>" ><%="".equals(line.getCsType())?"":objSession.getCodeLibelle(line.getCsType())%></td>
	<td onClick="<%=actionDetail%>" ><%="de".equals(objSession.getIdLangueISO())?line.getNomDe():"fr".equals(objSession.getIdLangueISO())?line.getNomFr():line.getNomIt()%></td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter"  --%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
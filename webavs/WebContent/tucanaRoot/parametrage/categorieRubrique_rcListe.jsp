<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.tucana.db.parametrage.TUCategorieRubriqueListViewBean viewBean = (globaz.tucana.db.parametrage.TUCategorieRubriqueListViewBean) request.getAttribute("viewBean");
    size = viewBean.getSize();
	detailLink = "tucana?userAction=tucana.parametrage.categorieRubrique.afficher&idGroupeCategorie="+request.getParameter("idGroupeCategorie")+"&idCategorieRubrique=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="RUBRIQUE" /></TH>
		<TH><ct:FWLabel key="OPERATION" /></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			globaz.tucana.db.parametrage.TUCategorieRubriqueViewBean line = (globaz.tucana.db.parametrage.TUCategorieRubriqueViewBean) viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdCategorieRubrique() +"'";	
			
			%>
			<TD class="mtd" width="">
				<%
					String detLink = detailLink + line.getIdCategorieRubrique();
				%>
				<ct:menuPopup menu="OVTUCategorieRubrique"
					target="top.fr_main" detailLink="<%=detLink%>" detailLabelId="MNU_DETAIL" labelId="MNU_OPTIONS">
					<ct:menuParam key="idGroupeCategorie" value="<%=line.getIdGroupeCategorie()%>"/>
				</ct:menuPopup>
			</TD>	
			<TD align="left" class="mtd" onclick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsRubrique())%></TD>
			<TD align="left" class="mtd" onclick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsOperation())%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>

	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
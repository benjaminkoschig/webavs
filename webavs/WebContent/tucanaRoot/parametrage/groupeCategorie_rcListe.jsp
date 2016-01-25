<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.tucana.db.parametrage.TUGroupeCategorieListViewBean viewBean = (globaz.tucana.db.parametrage.TUGroupeCategorieListViewBean) request.getAttribute("viewBean");
	//viewBean.setOrder(globaz.tucana.db.bouclement.access.TUBouclementManager.TRI_ANNEE_MOIS);
    size = viewBean.getSize();

	detailLink = "tucana?userAction=tucana.parametrage.groupeCategorie.afficher&idGroupeCategorie=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="GROUPE_RUBRIQUE" /></TH>
		<TH><ct:FWLabel key="CATEGORIE" /></TH>
		<TH><ct:FWLabel key="TYPE" /></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
			<%
			globaz.tucana.db.parametrage.TUGroupeCategorieViewBean line = (globaz.tucana.db.parametrage.TUGroupeCategorieViewBean) viewBean.getEntity(i);
			actionDetail = targetLocation  + "='" + detailLink + line.getIdGroupeCategorie() +"'";	
			
			%>
			<TD class="mtd" width="">
				<%
					String detLink = detailLink + line.getIdGroupeCategorie();
				%>
				<ct:menuPopup menu="OVTUGroupeCategorie"
					target="top.fr_main" detailLink="<%=detLink%>" detailLabelId="MNU_DETAIL" labelId="MNU_OPTIONS">
					<ct:menuParam key="idGroupeCategorie" value="<%=line.getIdGroupeCategorie()%>"/>
				</ct:menuPopup>
			</TD>	
			<TD align="left" class="mtd" onclick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsGroupeRubrique())%></TD>
			<TD align="left" class="mtd" onclick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsCategorie())%></TD>
			<TD align="left" class="mtd" onclick="<%=actionDetail%>"><%=line.getSession().getCodeLibelle(line.getCsType())%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>

	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
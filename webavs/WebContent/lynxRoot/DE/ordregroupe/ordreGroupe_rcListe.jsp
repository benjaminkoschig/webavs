<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.lynx.db.ordregroupe.LXOrdreGroupeListViewBean viewBean = (globaz.lynx.db.ordregroupe.LXOrdreGroupeListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

globaz.lynx.db.ordregroupe.LXOrdreGroupeViewBean ordreGroupe = null;

detailLink ="lynx?userAction=lynx.ordregroupe.ordreGroupe.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH width="30">&nbsp;</TH>
	<TH width="30">Nummer</TH>
	<TH width="150">Fälligkeitsdatum</TH>
	<TH width="400">Bezeichnung</TH>
	<TH width="200">Schuldnerfirma</TH>
	<TH width="200">Ausführungsorgan</TH>
	<TH width="100">Benutzer</TH>
	<TH width="100">Status</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %> 
		<%-- tpl:put name="zoneList" --%>
    <%
    ordreGroupe = (globaz.lynx.db.ordregroupe.LXOrdreGroupeViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='"+detailLink+ordreGroupe.getIdOrdreGroupe()+"'";
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-OrdreGroupe" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=ordreGroupe.getIdOrdreGroupe()%>"/>
			<ct:menuParam key="idSociete" value="<%=ordreGroupe.getIdSociete()%>"/>
			<ct:menuParam key="idOrdreGroupe" value="<%=ordreGroupe.getIdOrdreGroupe()%>"/>
			<ct:menuParam key="idOrganeExecution" value="<%=ordreGroupe.getIdOrganeExecution()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=ordreGroupe.getIdOrdreGroupe()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=ordreGroupe.getDateEcheance()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=ordreGroupe.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=ordreGroupe.getNomSociete()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=ordreGroupe.getNomOrganeExecution()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=ordreGroupe.getProprietaire()%>&nbsp;</td>
    <%
	String image = "";
	if (!ordreGroupe.isOuvert()) {
		if (ordreGroupe.isGenere()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/envoye.gif\"";
		} else if (ordreGroupe.isTraitement()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement.gif\"";
		} else if (ordreGroupe.isPrepare()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/prepare.gif\"";
		} else if (ordreGroupe.isAnnule()) {
			image = "<img style=\"float:right\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\"";
		}
		
		if (!image.equals("")) {
			image += "\\>";
		}
	}
%>
    
    
    <td class="mtd" onClick="<%=actionDetail%>"><%=image%><%=ordreGroupe.getUcEtat().getLibelle()%></td>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
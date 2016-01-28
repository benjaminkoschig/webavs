<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.interets.*" %>
  <%@ page import="globaz.osiris.translation.CACodeSystem" %>
  <%

CAApercuInteretMoratoireListViewBean viewBean = (CAApercuInteretMoratoireListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.getSize();
detailLink = "osiris?userAction=osiris.interets.interetMoratoire.afficher&domaine=" + viewBean.getForDomaine() + "&selectedId=";
globaz.osiris.db.interets.CAApercuInteretMoratoire _line = null;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <th width="20">&nbsp;</th>
    <TH align="center" >Datum</TH>
    <TH align="center"><%=viewBean.isDomaineCA()?"Abrechnungskonto":"Debitor"%></TH>
    <TH align="center"><%=viewBean.isDomaineCA()?"Sektion":"Abrechnung"%></TH>
    <TH align="center">Zinsenart</TH>
    <TH align="center">Zinsen</TH>
    <TH align="center">Status</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    	_line = (globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean) viewBean.getEntity(i);
    	_line.setDomaine(viewBean.getForDomaine());
    	actionDetail = "parent.location.href='"+detailLink+_line.getIdInteretMoratoire()+("&domaine=" + viewBean.getForDomaine()+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + viewBean.getIdTiers(i)+"'");
    %>
    <TD class="mtd">
   	<% String tmp = (detailLink+_line.getIdInteretMoratoire()+("&domaine=" + viewBean.getForDomaine())); %>
    <%
	if (viewBean.isDomaineCA()) {
	%>
	<ct:menuPopup menu="CA-CalculInteretsMoratoires" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>">
		<ct:menuParam key="id" value="<%=_line.getIdInteretMoratoire()%>"/>
		<ct:menuParam key="selectedId" value="<%=_line.getIdInteretMoratoire()%>"/>
		<ct:menuParam key="idInteretMoratoire" value="<%=_line.getIdInteretMoratoire()%>"/>
		<ct:menuParam key="domaine" value="CA"/>
		<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=viewBean.getIdTiers(i)%>"/>

		<%
			if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(_line.getIdJournalFacturation())) {
		%>
			<ct:menuExcludeNode nodeId="imprimerDecisionInteret"/>
		<%
			}
		%>
	</ct:menuPopup>
	<%
		} else {
	%>
	<ct:menuPopup menu="CA-CalculInteretsMoratoires" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>">
		<ct:menuParam key="id" value="<%=_line.getIdInteretMoratoire()%>"/>
		<ct:menuParam key="selectedId" value="<%=_line.getIdInteretMoratoire()%>"/>
		<ct:menuParam key="idInteretMoratoire" value="<%=_line.getIdInteretMoratoire()%>"/>
		<ct:menuParam key="domaine" value="FA"/>
		<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=viewBean.getIdTiers(i)%>"/>

		<%
			if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(_line.getIdJournalFacturation())) {
		%>
			<ct:menuExcludeNode nodeId="imprimerDecisionInteret"/>
		<%
			}
		%>
	</ct:menuPopup>
	<%
		}
	%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="70" valign="top"><%=_line.getDateCalcul()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>">
	    <%=viewBean.isDomaineCA()?_line.getCompteAnnexe().getCARole().getDescription():CACodeSystem.getLibelle(session,_line.getEnteteFacture().getIdRole())%>
	    <%=_line.getIdExterneRole()%><br>
	    <%=_line.getDescription()%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top"><%=viewBean.isDomaineCA()?_line.getIdExterne() + _line.getLibelleDescription():_line.getEnteteFacture().getDescriptionDecompte()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top">
    	<%=globaz.osiris.translation.CACodeSystem.getLibelle(session, _line.getIdGenreInteret())%>
    </TD>
    <TD class="mtd" onClick="<%=actionDetail%>" valign="top" align="right"><%=_line.getTotalMontantInteret()%></TD>

    <TD class="mtd" onClick="<%=actionDetail%>" valign="top"> <%=globaz.osiris.translation.CACodeSystem.getLibelle(session, _line.getStatus())%>
    </TD>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
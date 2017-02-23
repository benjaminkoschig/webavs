
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.ordres.CAOrdreGroupe" %>
<%@ page import="globaz.osiris.api.ordre.APIOrdreGroupe"%>
<%@ page import="globaz.osiris.utils.CAUtil" %>
<%
globaz.osiris.db.ordres.CAOrdreGroupeManagerListViewBean viewBean =
  (globaz.osiris.db.ordres.CAOrdreGroupeManagerListViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.ordres.CAOrdreGroupe _ordre = null ;
size = viewBean.size();
detailLink ="osiris?userAction=osiris.ordres.ordresGroupes.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.osiris.utils.CAUtil"%>
<%@page import="globaz.osiris.db.ordres.CAOrdreGroupe"%><TH colspan="2" align="left">Num&eacute;ro</TH>
    <TH width="122" align="left">Date d'&eacute;ch&eacute;ance</TH>
    <TH nowrap width="333">description</TH>
    <TH width="236" align="left">Montant</TH>
    <TH width="86" align="left">Trans.</TH>
    <TH width="120" align="left">Type</TH>
    <TH align="left">Etat</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    	_ordre = (globaz.osiris.db.ordres.CAOrdreGroupe) viewBean.getEntity(i);
    	actionDetail = "parent.location.href='"+detailLink+_ordre.getIdOrdreGroupe()+"'";
    %>

	<%
	StringBuffer image = new StringBuffer();
	image.append("<div style=\"float:right\">");
	if (_ordre.getOrganeExecution().getCSTypeTraitementOG().equals(_ordre.getOrganeExecution().OG_ISO_20022)) {
		if (_ordre.getIsoCsTransmissionStatutExec().equals(globaz.osiris.db.ordres.CAOrdreGroupe.ISO_TRANSAC_STATUS_PARTIEL)) {
			image.append("<img title=\"" + _ordre.getSession().getCodeLibelle(_ordre.getIsoCsTransmissionStatutExec()) + "\" src=\"" + request.getContextPath()+"/images/contrast-16-orange.gif\">&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if (_ordre.getIsoCsTransmissionStatutExec().equals(globaz.osiris.db.ordres.CAOrdreGroupe.ISO_TRANSAC_STATUS_REJETE)) {
			image.append("<img title=\"" + _ordre.getSession().getCodeLibelle(_ordre.getIsoCsTransmissionStatutExec()) + "\" src=\"" + request.getContextPath()+"/images/circle-16-red.gif\">&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if (_ordre.getIsoCsTransmissionStatutExec().equals(globaz.osiris.db.ordres.CAOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET)) {
			image.append("<img title=\"" + _ordre.getSession().getCodeLibelle(_ordre.getIsoCsTransmissionStatutExec()) + "\" src=\"" + request.getContextPath()+"/images/circle-16-green.gif\">&nbsp;&nbsp;&nbsp;&nbsp;");
		}
	}
	
	if (!_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.OUVERT)) {
	 	if (_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.TRANSMIS)) {
			image.append("<img title=\"" + _ordre.getUcEtat().getLibelle() + "\" src=\"" + request.getContextPath()+"/images/envoye.gif\">");
		} else if (_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.GENERE)) {
			image.append("<img title=\"" + _ordre.getUcEtat().getLibelle() + "\" src=\"" + request.getContextPath()+"/images/envoye.gif\">");
		} else if (_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.TRAITEMENT)) {
			image.append("<img title=\"" + _ordre.getUcEtat().getLibelle() + "\" src=\"" + request.getContextPath()+"/images/avertissement.gif\">");
		} else if (_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.ANNULE)) {
			image.append("<img title=\"" + _ordre.getUcEtat().getLibelle() + "\" src=\"" + request.getContextPath()+"/images/avertissement2.gif\">");
		} else if (_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.ERREUR)) {
			image.append("<img title=\"" + _ordre.getUcEtat().getLibelle() + "\" src=\"" + request.getContextPath()+"/images/erreur.gif\">");
		} else if (_ordre.getEtat().equals(globaz.osiris.db.ordres.CAOrdreGroupe.ERREUR_PREPARATION)) {
			image.append("<img title=\"" + _ordre.getUcEtat().getLibelle() + "\" src=\"" + request.getContextPath()+"/images/erreur.gif\">");
		}
	}
	image.append("</div>");
	%>

	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OrdresGroupes" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="id" value="<%=_ordre.getIdOrdreGroupe()%>"/>
		<ct:menuParam key="selectedId" value="<%=_ordre.getIdOrdreGroupe()%>"/>
		<ct:menuParam key="refresh" value="true"/>
		<% if (!"0".equals(_ordre.getEtat())) { %>
			<ct:menuExcludeNode nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION %>" />
		<% } %>	
		
		<% if (CAOrdreGroupe.ANNULE.equals(_ordre.getEtat())) { %>
			<ct:menuExcludeNode nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION %>" />
			<ct:menuExcludeNode nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER %>" />
		<% } %>	
		
		<% if(APIOrdreGroupe.ISO_TRANSAC_STATUS_COMPLET.equals(_ordre.getIsoCsTransmissionStatutExec()) || !_ordre.hasOrdreRejetes()){ %>
			<ct:menuExcludeNode nodeId="<%=CAUtil.ID_MENU_NODE_CA_ORDRES_REJETER_IMPRIMER %>"/>
		<% } %>
    </ct:menuPopup>
	</TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="74"><%=_ordre.getIdOrdreGroupe()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="122"><%=_ordre.getDateEcheance()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="333"><%=_ordre.getMotif()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="236" align="right"><%=_ordre.getTotalToCurrency().toStringFormat()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="86" align="right"><%=_ordre.getNombreTransactions()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="120"><%=_ordre.getUcTypeOrdreGroupe().getLibelle()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%=image.toString()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
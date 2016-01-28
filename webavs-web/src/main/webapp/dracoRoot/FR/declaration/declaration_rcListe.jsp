<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.draco.db.declaration.DSDeclarationListViewBean" %>
<%@page import="globaz.draco.translation.CodeSystem"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.draco.db.declaration.DSDeclarationViewBean"%>

<%
	detailLink ="draco?userAction=draco.declaration.declaration.afficher&selectedId=";
	menuName = "declaration";
	DSDeclarationListViewBean viewBean = (DSDeclarationListViewBean)request.getAttribute("viewBean");
	size = viewBean.getSize ();%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	<Th width="15">&nbsp;</Th>
	<TH nowrap width="30" align="center"><ct:FWLabel key="CDS0002_NUMERO"/></TH>
	<TH width="150" align="left"><ct:FWLabel key="CDS0002_AFFILIE"/></TH>
	<TH width="50"><ct:FWLabel key="CDS0002_ANNEE"/></TH>
	<TH width="50" align="left"><ct:FWLabel key="CDS0002_DECOMPTE"/></TH>
	<TH width="150" align="left"><ct:FWLabel key="CDS0002_TYPE"/></TH>
	<TH width="150" align="left"><ct:FWLabel key="CDS0002_ETAT"/></TH>
	<TH width="150" align="right"><ct:FWLabel key="CDS0002_MASSE_EFFECTIVE"/></TH>
	<TH width="150" align="left"><ct:FWLabel key="CDS0002_CONTENTIEUX"/></TH>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<% 
		DSDeclarationViewBean entity = (DSDeclarationViewBean)viewBean.getEntity(i);
        actionDetail = "parent.location.href='"+detailLink+entity.getIdDeclaration()
        +"&"+VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE +"=" + entity.getIdTiers() + "'";
	%>

	<TD class="mtd" width="15">
		<ct:menuPopup menu="DS-OptionsDeclaration" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=entity.getIdDeclaration()%>"/>  
			<ct:menuParam key="idDeclaration" value="<%=entity.getIdDeclaration()%>"/>
			<ct:menuParam key="typeDeclaration" value="<%=entity.getTypeDeclaration()%>"/>
			<ct:menuParam key="numAffilie" value="<%=entity.getAffiliation().getAffilieNumero()%>"/>
			<ct:menuParam key="anneeDS" value="<%=entity.getAnnee()%>"/>
			<ct:menuParam key="idTiersVueGlobale" value="<%=entity.getIdTiers()%>"/>
		</ct:menuPopup>
     </TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="30" align="right"><%=entity.getNumeroAffilie()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="150" align="left"><%=!(JadeStringUtil.isBlank(entity.getDesignation1()))?entity.getDesignation1():""%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="50" align="center" ><%=entity.getAnnee()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="50" align="left"><%=entity.getNoDecompte()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="150" align="left"><%=CodeSystem.getLibelle(entity.getSession(), entity.getTypeDeclaration())%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="150" align="left"><%=CodeSystem.getLibelle(entity.getSession(), entity.getEtat())%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="150" align="right"><%=entity.getMasseSalTotal()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="150" align="left">&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>
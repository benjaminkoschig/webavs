<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.hera.vb.famille.SFApercuRelationConjointListViewBean viewBean = (globaz.hera.vb.famille.SFApercuRelationConjointListViewBean) request.getAttribute("viewBean");
globaz.hera.helpers.famille.SFRequerantHelper rh = new globaz.hera.helpers.famille.SFRequerantHelper();
globaz.hera.db.famille.SFRequerantDTO requerantDTO = rh.getRequerantDTO(session);

String idTiersParent = "";
if (requerantDTO != null) {
	idTiersParent = requerantDTO.getIdTiers();
}

size = viewBean.getSize ();
String idMembre = viewBean.getForIdConjoint();

menuDetailLabel = "Detail";
String listDetailLink = baseLink + ".afficher&idRelationConjoint=";

detailLink = servletContext + mainServletPath + "?userAction=hera.famille.apercuRelationFamilialeRequerant.afficher&selectedId=";
menuName="sf-optionconjoint";

globaz.framework.controller.FWController ctrl = (globaz.framework.controller.FWController) session.getAttribute("objController");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <%@page import="globaz.jade.client.util.JadeStringUtil"%>
<TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DETAIL_CONJOINT"/></TH>
	<TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_RELATION"/></TH>
	<TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DATED"/></TH>
	<TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_DATEF"/></TH>
	<TH><ct:FWLabel key="JSP_MEMBRE_FAMILLE_ENFANTS"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.hera.vb.famille.SFApercuRelationConjointViewBean line = (globaz.hera.vb.famille.SFApercuRelationConjointViewBean) viewBean.get(i);
String idRelationConjoint = line.getIdRelationConjoint();
String idConjoint = line.getIdConjoint(idMembre); // IdMembre du conjoint du membre duquel la situation est affichée

String detail = targetLocation  + "='" + baseLink +"afficher"+"&idRelationConjoint="+idRelationConjoint +"&idMembreFamille=" + idConjoint + "'";

String detailMenu = baseLink +"afficher"+"&idRelationConjoint="+idRelationConjoint +"&idMembreFamille=" + idConjoint;
%>
<TD class="mtd" width="16" >
	<ct:menuPopup menu="sf-optionconjoint" detailLabelId="MENU_DETAIL"  detailLink="<%=detailMenu%>">
		<ct:menuParam key="selectedId" value="<%=idConjoint%>"/>
		<ct:menuParam key="idRelationConjoint" value="<%=line.getIdRelationConjoint()%>"/>
		<ct:menuParam key="idConjoint" value="<%=idConjoint%>"/>

		<ct:menuParam key="idMembreFamilleDepuisRelFam" value="<%=idConjoint%>"/>
		<ct:menuParam key="idMembreFamille" value="<%=idConjoint%>"/>
		<ct:menuParam key="idTiersEnfant" value="<%=line.getIdTiersMembre(idMembre)%>"/>
		<ct:menuParam key="idTiers" value="<%=idTiersParent%>"/>		
		<ct:menuParam key="csDomaine" value="<%=line.getCsDomaine()%>"/>
	</ct:menuPopup>
</TD>


<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(line.getDateDecesConjoint(idMembre))) {%>
	<%if(!JadeStringUtil.isBlankOrZero(line.getIdTiersConjoint(idMembre))){%>
		<TD class="mtd" nowrap="nowrap">
			<table width="100%" style="borderStyle=solid;border=0">
				<tr>
					<TD class="mtd" nowrap onclick="<%=detail%>" style="borderStyle=solid;border=0"><%=line.getDetailRequerantSpecial(idMembre)%>
					<td align="right" style="borderStyle=solid;border=0"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=line.getIdTiersConjoint(idMembre)%>" class="external_link" target="_parent">Tiers</A></td>
				</tr>
			</table>
		</TD>
	<%}else{%>
		<TD class="mtd" nowrap onclick="<%=detail%>"><%=line.getDetailRequerantSpecial(idMembre)%>
	<%}%>
<%} else {%>
	<%if(!JadeStringUtil.isBlankOrZero(line.getIdTiersConjoint(idMembre))){%>
		<TD class="mtd" nowrap="nowrap">
			<table width="100%" height="100%" style="borderStyle=solid;border=0">
				<tr>
					<TD class="mtd" nowrap onclick="<%=detail%>" style="borderStyle=solid;border=0"><%=line.getDetailRequerantNormal(idMembre)%>
					<td align="right" style="borderStyle=solid;border=0"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=line.getIdTiersConjoint(idMembre)%>" class="external_link" target="_parent">Tiers</A></td>
				</tr>
			</table>
		</TD>
	<%}else{%>
		<TD class="mtd" nowrap onclick="<%=detail%>"><%=line.getDetailRequerantNormal(idMembre)%>
	<%}%>
<%}%>

<TD class="mtd" onclick="<%=detail%>" nowrap><%=ctrl.getSession().getCodeLibelle(line.getTypeRelation())%></TD>
<TD class="mtd" onclick="<%=detail%>" nowrap><%=line.getDateDebut()%></TD>
<TD class="mtd" onclick="<%=detail%>" nowrap><%=line.getDateFin()%>&nbsp;</TD>
<TD class="mtd" nowrap><%=line.displayEnfants(request.getContextPath(), detail)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
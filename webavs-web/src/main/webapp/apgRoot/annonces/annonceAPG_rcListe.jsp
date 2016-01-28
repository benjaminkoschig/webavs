<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.apg.db.annonces.APAnnonceAPG"%>
<%@page import="globaz.apg.api.annonces.IAPAnnonce"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.apg.vb.annonces.APAnnonceAPGListViewBean viewBean = (globaz.apg.vb.annonces.APAnnonceAPGListViewBean) request.getAttribute("viewBean");

BSession bSession = viewBean.getSession();
	
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
	size = viewBean.size();
	detailLink = "apg?userAction=apg.annonces.annonceAPG.afficher&selectedId=";
	globaz.prestation.tools.PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();
%>
<SCRIPT language="JavaScript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_ANNONCE"/></TH>
	<TH>businessProcessId</TH>
	<TH><ct:FWLabel key="JSP_SERVICE"/></TH>
    <TH><ct:FWLabel key="JSP_MOIS_ANNEE_COMPTABLE"/></TH>
    <TH><ct:FWLabel key="JSP_NO_ASSURE"/></TH>
    <TH><ct:FWLabel key="JSP_PERIODE_DU"/></TH>
    <TH><ct:FWLabel key="JSP_AU"/></TH>
    <TH><ct:FWLabel key="JSP_TOTAL_ALLOCATION"/></TH>
    <TH><ct:FWLabel key="JSP_ETAT"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
<%   
		globaz.apg.vb.annonces.APAnnonceAPGViewBean line = null;
		try {
			line = (globaz.apg.vb.annonces.APAnnonceAPGViewBean) iterH.next();
		} catch (Exception e) {
			break;
		}

		//on transmet dans la requête le type d'annonce pour savoir sur quel ecran aller ensuite
		actionDetail = targetLocation  + "='" + detailLink + line.getIdAnnonce()+"&typeAnnonce="+line.getTypeAnnonce()+"'";

	String detailMenu = detailLink + line.getIdAnnonce()+"&typeAnnonce="+line.getTypeAnnonce();
	
if (iterH.isPositionPlusPetite()) {
%> </TBODY><%
} else if (iterH.isPositionPlusGrande()) {
	%><TBODY id="groupe_<%=line.getIdParent()%>" style="display: none;"><%
} %>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<TD class="mtd">
<% for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) { %>
	&nbsp;&nbsp;
<% } %>

		<ct:menuPopup menu="ap-optionannonce" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>">
			<ct:menuParam key="typeAnnonce" value="<%=line.getTypeAnnonce() %>"/>
			<ct:menuParam key="selectedId" value="<%=line.getIdAnnonce() %>"/>
			<ct:menuParam key="forIdDroit" value="<%=line.getIdDroit() %>"/>

	<% if (IAPAnnonce.CS_APGSEDEX.equals(line.getTypeAnnonce()) && !IAPAnnonce.CS_ENVOYE.equals(line.getEtat())) { %>
				<ct:menuExcludeNode nodeId="APG_CORRIGER_NSS"/>
				<ct:menuExcludeNode nodeId="APG_CORRIGER_ANNONCE"/>
			<% } else if(!viewBean.getSession().hasRight(IAPActions.ACTION_ANNONCESEDEX + ".modifier", FWSecureConstants.UPDATE)){ %>
				<ct:menuExcludeNode nodeId="APG_CORRIGER_NSS"/>
				<ct:menuExcludeNode nodeId="APG_CORRIGER_ANNONCE"/>
			<% } %>
		</ct:menuPopup>
		
<% if (iterH.isPere()) { %>
	<INPUT id="bouton_<%=line.getIdAnnonce()%>" type="button" value="+" onclick="afficherCacher(<%=line.getIdAnnonce()%>)">
<% } %>
</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdAnnonce()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getBusinessProcessId()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getGenre()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getMoisAnneeComptable()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getNumeroAssure())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriodeDe()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriodeA()%>&nbsp;</TD>
	<TD align="right" class="mtd" onClick="<%=actionDetail%>" width="" nowrap><%=globaz.globall.util.JANumberFormatter.fmt(line.getTotalAPG(),true,true,true,2)%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=viewBean.getSession().getCodeLibelle(line.getEtat())%>&nbsp;</TD>
						
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
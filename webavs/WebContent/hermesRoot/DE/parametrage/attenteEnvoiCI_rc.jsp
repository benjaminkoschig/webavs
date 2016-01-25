<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%bButtonNew = false;
globaz.hermes.db.gestion.HEOutputAnnonceViewBean rcBean = (globaz.hermes.db.gestion.HEOutputAnnonceViewBean)session.getAttribute("attenteEnvoiCI-rcBean");
actionNew =  "javascript:document.forms[0].submit();";
if(globaz.pavo.util.CIUtil.isSpecialist(session) && 
				IHEAnnoncesViewBean.CS_EN_ATTENTE.equals(rcBean.getStatut()) && !rcBean.getArchivage()){ 
	bButtonNew = true; 
	btnNewLabel = "Supprimer";
}
else{
	bButtonNew = false;
}
idEcran="GAZ0007";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<SCRIPT language="JavaScript">
usrAction = 'hermes.parametrage.attenteEnvoiCI.lister';
top.document.title = "MZR - Detail des gesendeten IK-Auszug";
bFind = true;
function onClickNew() {
	if(window.confirm("Möchten sie wirklich diesen IK-Auszug löschen ?")){
		setUserAction("hermes.parametrage.supprimerCI.executer");
		document.forms[0].target = "fr_main";
	}
}
</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-OnlyDetail">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Detail des gesendeten IK-Auszug<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
  <tr> 
    <td width="5%" nowrap>SVN&nbsp;</td>
    <td width="16%"> 
      <input type="text" name="numavs" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(rcBean.getField(IHEAnnoncesViewBean.NUMERO_ASSURE))%>">

      
      
    </td>
    <td width="16%">&nbsp;</td>
    <td width="11%" nowrap>Kasse&nbsp;</td>
    <td width="52%"> 
              <input type="text" name="caisseDisplay" class="disabled" readonly value="<%=globaz.hermes.utils.StringUtils.formatCaisseAgence(rcBean.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE),rcBean.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE))%>">
              <input type="hidden" name="caisse" value="<%=rcBean.getNumeroCaisse()%>">
    </td>
  </tr>
  <tr> 
    <td width="5%" nowrap>SZ&nbsp;</td>
    <td width="16%"> 
              <input type="text" name="forMotif" class="disabled" readonly value="<%=rcBean.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)%>">
    </td>
    <td width="16%">&nbsp;</td>
    <td width="11%" nowrap>Datum&nbsp;</td>
    <td width="52%"> 
            <input type="text" name="date" class="disabled" readonly value="<%=rcBean.getDateAnnonce()%>">
    		<input type="hidden" name="forDate" value="<%=rcBean.getDateAnnonce()%>">
    </td>
  </tr>
					<input type="hidden" name="forRefUnique" value="<%=rcBean.getRefUnique()%>">
					<input type="hidden" name="andForRefUnique2" value="<%=rcBean.getRefUnique38()%>">
					<input type="hidden" name="isArchivage" value="<%=request.getParameter("isArchivage")%>">		  
		  
		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
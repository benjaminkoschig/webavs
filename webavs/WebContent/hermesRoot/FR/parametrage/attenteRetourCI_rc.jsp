<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="java.util.*, java.lang.*, globaz.globall.util.*, globaz.hermes.db.gestion.*, globaz.hermes.utils.*" %> 
<%
globaz.hermes.db.gestion.HEOutputAnnonceViewBean rcBean = (globaz.hermes.db.gestion.HEOutputAnnonceViewBean)session.getAttribute("attenteEnvoiCI-rcBean");
actionNew =  "javascript:document.forms[0].submit();";
if(globaz.pavo.util.CIUtil.isSpecialist(session) && 
	(IHEAnnoncesViewBean.CS_A_TRAITER.equals(rcBean.getStatut()) || IHEAnnoncesViewBean.CS_TERMINE.equals(rcBean.getStatut())) &&
	!rcBean.getArchivage()){ 
	bButtonNew = objSession.hasRight(userActionNew, globaz.framework.secure.FWSecureConstants.UPDATE);
	btnNewLabel = " Libérer ";
}
else{
	bButtonNew = false;
}
idEcran="GAZ0011";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<SCRIPT language="JavaScript">
usrAction = 'hermes.parametrage.attenteRetourCI.lister';
top.document.title = "ARC - Détail d'un extrait de CI reçu";
bFind = true;
// redéfinition de la méthode onClickNew()
function onClickNew() {
	if(window.confirm("Désirez-vous vraiment libérer cet extrait ?")){
		setUserAction("hermes.parametrage.libererCI.executer");
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
				<%-- tpl:put name="zoneTitle" --%>Détail d'un extrait de CI reçu<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
  <tr> 
    <td width="5%" nowrap>NSS :&nbsp;</td>
    <td width="16%"> 	
	<%
	String nss = rcBean.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);	
	nss = globaz.commons.nss.NSUtil.formatAVSUnknown(nss);
	%>	
	
	<input type="text" name="numavs" class="disabled" readonly value="<%=nss%>">
	
	
    </td>
    <td width="16%">&nbsp;</td>
            <td width="11%" nowrap>Caisse tenant le CI :&nbsp;</td>
    <td width="52%"> 
              <input type="text" name="caisse" class="disabled" readonly value="<%=StringUtils.formatCaisseAgence(rcBean.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI),rcBean.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI))%>">
    </td>
  </tr>
  <tr> 
    <td width="5%" nowrap>Motif :&nbsp;</td>
    <td width="16%"> 
              <input type="text" name="forMotif" class="disabled" readonly value="<%=rcBean.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE)%>">
    </td>
    <td width="16%">&nbsp;</td>
    <td width="11%" nowrap>Date :&nbsp;</td>
    <td width="52%"> 
       <input type="text" name="date" class="disabled" readonly value="<%=rcBean.getDateAnnonce()%>">
    </td>
  </tr>
<input type="hidden" name="forRefUnique" value="<%=request.getParameter("refUnique")%>">		  
<input type="hidden" name="isArchivage" value="<%=request.getParameter("isArchivage")%>">		  
		  
		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
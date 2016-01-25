 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
  globaz.hermes.db.parametrage.HEAttenteRetourOptimizedListViewBean viewBean = (globaz.hermes.db.parametrage.HEAttenteRetourOptimizedListViewBean)request.getAttribute ("viewBean");
  size = viewBean.getSize();
  
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <th>NSS</th>
    <th>NSS attendu</th>	
    <th>Motif</th>
    <th>Date</th>
    <th>Statut</th>
    <th>Utilisateur</th>
    <th>Type d'ARC</th>
    <th>Reçu</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		globaz.hermes.db.parametrage.HEAttenteRetourOptimizedViewBean line = (globaz.hermes.db.parametrage.HEAttenteRetourOptimizedViewBean)viewBean.getEntity(i);
		if(!line.isConfirmed()){
			//String selectedIdValue = line.getIdAttenteRetour();
			//detailLink = "hermes?userAction=hermes.parametrage.attenteRetourOptimized.afficher&selectedId=";
			actionDetail = "";//targetLocation  + "='" + detailLink + selectedIdValue + "&motif=" + line.getTypeAnnonce() +"&isArchivage="+line.isArchivage()+ "'";
		}else{
			if("8".equals(line.getIdTypeAnnonceAttendue()) || "9".equals(line.getIdTypeAnnonceAttendue())){
				detailLink = "hermes?userAction=hermes.parametrage.attenteRetourCI.chercher&selectedId=";
				actionDetail = targetLocation  + "='" + detailLink + line.getIdRetour() + "&refUnique="+line.getRefUnique()+"&caisse="+line.getCaisseCI()+"&isArchivage="+line.isArchivage()+"'";
			} else {
				String selectedIdValue = line.getIdRetour();
				detailLink = "hermes?userAction=hermes.parametrage.attenteReception.afficher&selectedId=";
				actionDetail = targetLocation  + "='" + detailLink + selectedIdValue + "&motif=" + line.getTypeAnnonce() +"&isArchivage="+line.isArchivage()+ "'";
			}
		}
	%>
	
	<%-- modif NNSS --%> 
	<%-- <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumavs().equals("") || line.getNumavs().equals("00000000000"))?line.getArcEtatNominatif(20):globaz.globall.util.JAUtil.formatAvs(line.getNumavs())%>&nbsp;</TD> --%> 
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumavs().equals("") || line.getNumavs().equals("00000000000"))?line.getArcEtatNominatif(20): globaz.commons.nss.NSUtil.formatAVSNew(line.getNumavs(),line.getNumeroAvsNNSS().equals("true")) %>&nbsp;</TD>
    
	<%--<TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumeroAvsAttenduFormatted().trim().equals(""))?line.getArcEtatNominatif(20):line.getNumeroAvsAttenduFormatted()%>&nbsp;</TD>--%> 
	<TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumeroAvsAttenduFormatted().trim().equals(""))?line.getArcEtatNominatif(20):globaz.commons.nss.NSUtil.formatAVSNew(line.getNumeroAvsAttendu(),line.getNumeroAvsNNSS_attendu().equals("true"))%>&nbsp;</TD>
	
	
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getTypeAnnonce().equals(""))?"&nbsp;":line.getTypeAnnonce()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getDateCreation().equals(""))?"&nbsp;":line.getDateCreation()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getStatut().equals(""))?"&nbsp;":line.getStatut()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getCreator().equals(""))?"&nbsp;":line.getCreator()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getTypeAnnonceAttendue().equals(""))?"&nbsp;":line.getTypeAnnonceAttendue()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= !line.isConfirmed()?"<img src=\"images/erreur.gif\" border=\"0\">":"<img src=\"images/ok.gif\" border=\"0\">"%>&nbsp;</TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
  globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean viewBean = (globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean)request.getAttribute ("viewBean");
  size = viewBean.getSize();
  detailLink = "hermes?userAction=hermes.parametrage.attenteEnvoi.afficher&selectedId=";
  menuName = "attenteEnvoi-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <th widht="16">&nbsp;</th>
    <th>SVN</th>
    <th>SZ</th>
    <th>Datum</th>
    <th>MZR-Nr.</th>    
    <th>Stand</th>
    <th>Benutzer</th>
    <!--
    <th>Motif</th>
    -->
    <th>Grund der MZR</th>    
    <th>Gesendet</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean line = (globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean)viewBean.getEntity(i);
		String isARC39 = "false";
		if(line.getEnregistrement().startsWith("39")){
			String detailLinkCI = "hermes?userAction=hermes.parametrage.attenteEnvoiCI.chercher&selectedId=";
			actionDetail = targetLocation  + "='" + detailLinkCI + line.getIdAnnonce()+"&refUnique="+line.getRefUnique() + "&isArchivage="+line.isArchivage()+"'";
			isARC39 = "true";
		} else {		
			actionDetail = targetLocation  + "='" + detailLink + line.getIdAnnonce()+"&refUnique="+line.getRefUnique() + "&isArchivage="+line.isArchivage()+"'";
		}
	%>
    <TD class="mtd" width="">
    	<ct:menuPopup menu="HE-attenteEnvoiDetail" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdAnnonce()%>"/>
			<ct:menuParam key="referenceUnique" value="<%=line.getRefUnique()%>"/>
			<ct:menuParam key="idAnnonce" value="<%=line.getIdAnnonce()%>"/>
			<ct:menuParam key="isARC39" value="<%=isARC39%>"/>
			<ct:menuParam key="isArchivage" value="<%=String.valueOf(line.isArchivage())%>"/>
     	</ct:menuPopup>   
    </TD>
    <%-- <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumavs().equals("000.00.000.000"))?line.getArcEtatNominatif(20):globaz.hermes.utils.AVSUtils.formatAVS8Or9(line.getNumavs())%>&nbsp;</TD> --%>
    
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getNumavs().equals("000.00.000.000"))?line.getArcEtatNominatif(20):globaz.commons.nss.NSUtil.formatAVSNew(line.getNumavs(),line.getNumeroAvsNNSS().equals("true"))%>&nbsp;</TD>
    
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getMotifArc().equals(""))?"&nbsp;":line.getMotifArc()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getDateCreation().equals(""))?"&nbsp;":line.getDateCreation()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getIdAnnonce().equals(""))?"&nbsp;":line.getIdAnnonce()%>&nbsp;</TD>    
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getStatutLibelle().equals(""))?"&nbsp;":line.getStatutLibelle()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getCreator().equals(""))?"&nbsp;":line.getCreator().toUpperCase()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (line.getTypeEnvoi().equals(""))?"&nbsp;":line.getTypeEnvoi()%>&nbsp;</TD>
    <TD class="mtd" onClick="<%=actionDetail%>"><%= (!line.getStatut().equals("117001"))?"<img src=\""+request.getContextPath()+"/images/ok.gif"+"\" border=0>":"<img src=\""+request.getContextPath()+"/images/verrou.gif"+"\" border=0>" %>&nbsp;</TD>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
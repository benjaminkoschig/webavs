<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*,globaz.globall.util.*"%>
<%
    globaz.pavo.db.splitting.CIDossierSplittingListViewBean viewBean = (globaz.pavo.db.splitting.CIDossierSplittingListViewBean)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="pavo?userAction=pavo.splitting.dossierSplitting.afficher&selectedId=";
    menuName = "dossierSplNoRight-detail";
    //if(objSession.hasRight(userActionUpd, "UPDATE")) {
	//	menuName = "dossierSplitting-detail";
	//} else {
	//	menuName = "dossierSplNoRight-detail";
	//}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 

<Th width="16">&nbsp;</Th>
<Th width="">No</Th>
<Th width="">Date ouverture</Th>
<Th width="">Assuré</Th>
<Th width="">Conjoint</Th>
<Th width="">Divorce</Th>
<Th width="">Etat</Th>    

    
    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
     <% CIDossierSplitting line = (CIDossierSplitting)viewBean.getEntity(i); %>
     <TD class="mtd" width="">
        <ct:menuPopup menu="dossierSplitting-detail" label="<%=optionsPopupLabel%>" target="top.fr_main"
        detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getIdDossierSplitting()%>">
			<ct:menuParam key="idDossierSplitting" value="<%=line.getIdDossierSplitting()%>"/>
			<ct:menuParam key="selectedId" value="<%=line.getIdDossierSplitting()%>"/>
     	</ct:menuPopup>
     </TD>
     <% actionDetail = targetLocation+"='"+detailLink+line.getIdDossierSplitting()+"'"; %>
	 <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getIdDossierInterne()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateOuvertureDossier()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getIdTiersAssure())%>
     <br><%=line.getTiersAssureNomComplet()+ " " + line.getEtatFormateAssure()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width="" nowrap><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getIdTiersConjoint())%>
     <br><%=line.getTiersConjointNomComplet()+" " + line.getEtatFormateConjoint()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.getDateDivorce()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdEtat(),session)%></TD>
  
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
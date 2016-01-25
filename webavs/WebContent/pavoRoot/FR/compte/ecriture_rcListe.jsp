 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*,globaz.pavo.translation.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%	
	detailLink ="pavo?userAction=pavo.compte.ecriture.afficher&selectedId=";
    CIEcritureListViewBean viewBean = (CIEcritureListViewBean)request.getAttribute ("viewBean");
    CICompteIndividuelViewBean viewBeanFK = (CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
    String userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
    if(objSession.hasRight(userActionNew, "ADD")) {
    	if(globaz.pavo.util.CIUtil.isSpecialist(session)){
	    	menuName = "ecriture-detail";
	    }else{
	    	menuName = "ecriture-detailNoSpez";
	    }
    }
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th width="16">&nbsp;</Th>
    <Th width="">Affilié ou partenaire</Th>
    <Th width="">Genre</Th>
    <Th width="">Période</Th>
    <Th width="">Montant</Th>
    <Th width="">Code</Th>
    <!--Th width="">BTA</Th>
<Th width="">Spécial</Th-->
    <Th width="">Date inscr./clôture</Th>
    <Th width="">Rem.</Th>
    <% if(viewBeanFK.hasUserShowRight(null)) {
		size = viewBean.getSize();
	} else {
		size = 0; %>
		<script>errorObj.text="<%=viewBeanFK.getMessageNoRight()%>";</script>
	<% } %>
	<Th width="">Affiliation</Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <% CIEcriture line = (CIEcriture)viewBean.getEntity(i); 
		String lineStyle;
		 if(JAUtil.isIntegerEmpty(line.getDateCiAdditionnel())) {
			lineStyle = "class='mtd'";
		} else {
			lineStyle = "class='giText'";
		}
		if(line.CS_TEMPORAIRE.equals(line.getIdTypeCompte())){
			lineStyle += " style=color:#999999 ";
		}
		if(line.CS_CI_SUSPENS.equals(line.getIdTypeCompte())){
			lineStyle += " style=color:#d57a01 ";
		}
		if(line.CS_TEMPORAIRE_SUSPENS.equals(line.getIdTypeCompte())){
			lineStyle+=" style=font-style:italic; style=color:#D3D3D3 ; ";
		}
		if(line.CS_CI_SUSPENS_SUPPRIMES.equals(line.getIdTypeCompte())){
			lineStyle+=" style=text-decoration:line-through; "; 
		}
		if(line.CS_CORRECTION.equals(line.getIdTypeCompte())){
			lineStyle += " style=color:#0000FF ";
		}

		
	%>
	<%if(CICompteIndividuel.CS_REGISTRE_GENRES_6.equals(viewBeanFK.getRegistre())){%>
    	<TD class="<%=lineStyle%>" width="">
	    <ct:menuPopup menu="ecriture-detailGre6" label="<%=optionsPopupLabel%>" target="top.fr_main"
	    detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getEcritureId()%>">
			<ct:menuParam key="selectedId" value="<%=line.getEcritureId()%>"/>
     	</ct:menuPopup>
	</TD>
	<%}else{ %>
	<TD class="<%=lineStyle%>" width="">
	    <ct:menuPopup menu="ecriture-detailNoSpez" label="<%=optionsPopupLabel%>" target="top.fr_main"
	    detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + line.getEcritureId()%>">
			<ct:menuParam key="selectedId" value="<%=line.getEcritureId()%>"/>
     	</ct:menuPopup>
	</TD>
	
	<%} %>
    <% actionDetail = targetLocation+"='"+detailLink+line.getEcritureId()+"'"; %>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="" ><%=line.getNoNomEmployeur()%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width=""><%=line.getGreFormat()%>&nbsp;</TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="" nowrap align="right"><%=line.getMoisDebutPad()+"-"+line.getMoisFinPad()+"."+line.getAnnee()%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="" align="right" nowrap><%=line.getMontantSigne()%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="" align="center"><%=CodeSystem.getCodeUtilisateur(line.getCode(),session)%></TD>
    
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="" align="center"><%=line.getDateInscription()%></TD>
    <TD <%=lineStyle%> onClick="<%=actionDetail%>" width="" align="center"> 
      <%if(!JAUtil.isIntegerEmpty(line.getIdRemarque())) { %>
      <img src="<%=request.getContextPath()%>/images/attach.png" width="16" height="16"> 
      <% } %>
    </TD>
     <%if(!JadeStringUtil.isBlankOrZero(line.getEmployeur())) {%>
    <%String actionNaos = targetLocation+"='"+"naos?userAction=naos.affiliation.affiliation.chercher&idTiers="+line.getIdTiers()+"'"; %>
      <TD <%=lineStyle%> onClick="<%=actionNaos%>" width="" align="center">Affiliation</TD>
    <%}%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
  <% String total = viewBean.getSumOfCI();
   if(total!=null) {
     rowStyle = "row"; %>
  <TR class="<%=rowStyle%>"> 
    <td colspan="9"> 
      <hr size="1">
    </td>
  </tr>
  <TR class="<%=rowStyle%>"> 
    <TD></TD>
    <TD colspan="3"><b>Total des incriptions</b></TD>
    <TD align="left"><b><%=total%></b>&nbsp;</TD>
    <TD colspan=3><b><%=viewBean.getSumOfCISansCts().substring(0,viewBean.getSumOfCISansCts().indexOf("."))%></b></TD>
  </TR>
  <% } %>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
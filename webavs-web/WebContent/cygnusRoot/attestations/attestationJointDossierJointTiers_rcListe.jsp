<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_RF_??"
	RFAttestationJointDossierJointTiersListViewBean viewBean = (RFAttestationJointDossierJointTiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "cygnus?userAction="+IRFActions.ACTION_ATTESTATION_JOINT_TIERS+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>

<%@page import="globaz.cygnus.vb.attestations.RFAttestationJointDossierJointTiersListViewBean"%>
<%@page import="globaz.cygnus.vb.attestations.RFAttestationJointDossierJointTiersViewBean"%>
<script language="JavaScript">

</script>
   		<TH colspan="2"><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_DETAIL_REQUERANT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_NUMERO"/></TH>
		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_TYPE_DOCUMENT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_DATE_CREATION"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_PERIODES"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_TYPE_SOIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_SOUS_TYPE_SOIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_ATTESTATION_LIST_NUMERO_DOSSIER"/></TH>
   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%										
		RFAttestationJointDossierJointTiersViewBean courant = (RFAttestationJointDossierJointTiersViewBean) viewBean.get(i);

		String urlForMenuPopUp = detailLink + courant.getIdAttestation() +
		   "&idDossier=" +	courant.getIdDossier()+ 
		   "&dateDebut=" + courant.getDateDebut() +
		   "&dateFin=" + courant.getDateFin() +
		   "&idGestionnaire=" + courant.getIdGestionnaire() +
		   "&idAttestation=" + courant.getIdAttestation() +
		   "&idAttestationDossier=" + courant.getIdAttestationDossier() +
		   "&csNiveauAvertissement=" + courant.getForCsNiveauAvertissement() +
		   "&csTypeAttestation=" + courant.getForCsTypeAttestation() +
		   "&creationDate=" + courant.getDateCreation() +
		   "&codeTypeDeSoin=" + courant.getTypeSoin() +
		   "&codeSousTypeDeSoin=" + courant.getSousTypeSoin() +
		   "&idSousTypeSoin=" + courant.getIdSousTypeSoin() +
		   "&nss=" + courant.getNss() + 
		   "&nom=" + courant.getNom() + 
		   "&csNationalite=" + courant.getCsNationalite() +
		   "&csSexe=" + courant.getCsSexe() +
		   "&dateNaissance=" + courant.getDateNaissance() +
		   "&dateDeces=" + courant.getDateDeces() +
		   "&prenom= " + courant.getPrenom();
		
		String detailUrl = "parent.location.href='" + urlForMenuPopUp +"'";
		%>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>">
	   		<ct:menuPopup menu="cygnus-optionsattestations" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>" >			
			</ct:menuPopup>
		</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailAssure()%></TD>				
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdAttestation()%></TD>  
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getForCsTypeAttestation()) %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=JadeStringUtil.isEmpty(courant.getDateCreation())?"&nbsp;":courant.getDateCreation() %></TD>     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateDebut()+ " - " + courant.getDateFin()%></TD>     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=viewBean.getSession().getCodeLibelle(courant.getIdTypeSoin())%>' ><%=courant.getTypeSoin().length()==1?("0"+courant.getTypeSoin()):courant.getTypeSoin() %></a></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=viewBean.getSession().getCodeLibelle(courant.getIdSousTypeSoin())%>' ><%=courant.getSousTypeSoin().length()==1?("0"+courant.getSousTypeSoin()):courant.getSousTypeSoin() %></a></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdDossier() %></TD>

		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
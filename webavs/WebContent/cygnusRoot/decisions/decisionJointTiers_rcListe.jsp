<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.cygnus.db.decisions.RFDecisionJointTiersRcListFormatter"%>
<%@page import="globaz.cygnus.api.decisions.IRFDecisions"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_RF_??"
	RFDecisionJointTiersListViewBean viewBean = (RFDecisionJointTiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	int condInt=0;
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_DECISION_JOINT_TIERS+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
	
	
	RFDecisionJointTiersRcListFormatter formatter = null;
	if(viewBean.iterator()!=null){
		formatter = new RFDecisionJointTiersRcListFormatter(viewBean.iterator());
		formatter.setSize(size);
	}
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>


<%@page import="globaz.cygnus.vb.decisions.RFDecisionJointTiersListViewBean"%>
<%@page import="globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean"%>
<%@page import="globaz.prestation.tools.nnss.PRNSSUtil"%>
<script language="JavaScript">

</script>

   		<TH colspan="2"><ct:FWLabel key="JSP_RF_DECISION_DETAIL_REQUERANT"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DOS_L_GED"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DECISION_ETAT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DECISION_ANNEE_QD"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DECISION_MONTANT_TOTAL"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DECISION_PREPARE_PAR"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DECISION_DATE_PREPARATION"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DECISION_VALIDE_PAR"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DECISION_DATE_VALIDATION"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DECISION_DATE_SUR_DOCUMENT"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DECISION_NUMERO"/></TH>		
		<TH><ct:FWLabel key="JSP_RF_DECISION_SOUS_GEST"/></TH>
		   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		
		<%	
		 if(formatter != null) {
			 RFDecisionJointTiersViewBean lineVb = formatter.getNextElement();
			 //i=formatter.getI();
			 
			 
				if(lineVb != null) {
					condInt++;
			    	//actionDetail = targetLocation  + "='" + detailLink + lineVb.getIdPrestation()+"'";
			
					String urlForMenuPopUp =  detailLink + lineVb.getIdDecision() +
									 "&nss=" + lineVb.getNss()+
									 "&nom=" + lineVb.getNom().replace("'","") + 
									 "&prenom=" + lineVb.getPrenom().replace("'","") +
									 "&dateNaissance=" + lineVb.getDateNaissance() +
									 "&csSexe=" + lineVb.getCsSexe() +
									 "&idDecision=" + lineVb.getIdDecision() +
									 "&numeroDecision=" + lineVb.getNumeroDecision() +
									 "&idTiers=" + lineVb.getIdTiers() +
									 "&idPrestation=" + lineVb.getIdPrestation() +
									 "&montantPrestation=" + lineVb.getMontantPrestation() +
									 "&csNationalite=" + lineVb.getCsNationalite(); 
			
					String detailUrl = "parent.location.href='" + urlForMenuPopUp +"'";

					condition = (condInt % 2 == 0);
					
					if (condition) {
						rowStyle = "row";
					} else {
						rowStyle = "rowOdd";
					}
		%>
		<tr class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
		<% if (isSelection) { %>
			<td class="mtd" width="15px"><ct:FWChooserTag index="<%=i%>"/></td>
		<% } %>
				
<%--	<%
			String urlGED = servletContext + "/cygnus?" 
					+ "userAction=" + IRFActions.ACTION_DECISION_JOINT_TIERS + ".actionAfficherDossierGed" 
					+ "&noAVSId=" + lineVb.getNss() 
					+ "&idTiersExtraFolder=" + null 
					+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
		%>
--%>		
		
		
	   	<TD class="mtd" nowrap>
	   		<ct:menuPopup menu="cygnus-optionsdecisions" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>" >	
	   			<ct:menuParam key="idTierRequerant" value="<%=lineVb.getIdTiers()%>"/>
	     		<ct:menuParam key="idDecision" value="<%=lineVb.getIdDecision()%>"/>
	     		<ct:menuParam key="numeroDecision" value="<%=lineVb.getNumeroDecision()%>"/>	     		
	     		<ct:menuParam key="idPrestation" value="<%=lineVb.getIdPrestation()%>"/>
	     		<ct:menuParam key="montantPrestation" value="<%=lineVb.getMontantPrestation()%>"/>
	     		<ct:menuParam key="selectedId" value="<%=lineVb.getIdDecision()%>"/>
	     		
	     		<% if (!lineVb.getEtatDecision().equals(IRFDecisions.VALIDE)) { %> 
     				<ct:menuExcludeNode nodeId="GENERER_PDF_SELECT"/>
     			<% }%>
     								
			</ct:menuPopup>
		</TD>				
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getDetailAssureGroupBy() %></TD>
     		<%
		     	// Iteration sur les nss de chaque membre famille regroupé dans une même décision, pour créer les liens GED respectifs. 
		     		StringBuffer liensGedParTiers = new StringBuffer();
		     			for(String nss : formatter.getListNssForLinkGedGroupBy()){ 
		     				if(!JadeStringUtil.isEmpty(nss)){
		     			    	String urlGedParNss = servletContext + "/cygnus?" 
					     			+ "userAction=" + IRFActions.ACTION_DECISION_JOINT_TIERS + ".actionAfficherDossierGed" 
					     			+ "&noAVSId=" + nss 
					     			+ "&idTiersExtraFolder=" + null 
					     			+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
		     			     	
		     			     		liensGedParTiers.append("<a href='#' onclick=\"window.open('"+urlGedParNss+"','GED_CONSULT')\">"+viewBean.getSession().getLabel("JSP_LIEN_GED")+"</a></br></br>");
		     			    }
		     			} 
			%>
		     	
		     	
		     	
	    <td align="center" valign="middle" class="mtd" nowrap ><%=liensGedParTiers.toString() %></td> 
	    
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(lineVb.getEtatDecision()) %></TD>    	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getAnneeQD() %></TD>
     	<TD align="right" class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getMontantDecisionList() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getIdPreparePar() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getDatePreparation() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getIdValidePar() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getDateValidation() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getDateSurDocument() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getNumeroDecision() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getIdGestionnaire() %></TD>				

<%}}%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
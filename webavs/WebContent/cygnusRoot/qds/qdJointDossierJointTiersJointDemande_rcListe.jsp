<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.qds.RFQdJointDossierJointTiersJointDemandeListViewBean"%>
<%@page import="globaz.cygnus.vb.qds.RFQdJointDossierJointTiersJointDemandeViewBean"%>
<%@page import="globaz.cygnus.db.qds.RFQdJointDossierJointTiersJointDemandeRCListFormatter"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_L"
	RFQdJointDossierJointTiersJointDemandeListViewBean viewBean = (RFQdJointDossierJointTiersJointDemandeListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	int condInt=0;
	
	RFQdJointDossierJointTiersJointDemandeRCListFormatter formatter = null;
	if(viewBean.iterator()!=null){
		formatter = new RFQdJointDossierJointTiersJointDemandeRCListFormatter(viewBean.iterator());
		formatter.setSize(size);
	}
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH colspan=2><ct:FWLabel key="JSP_RF_QD_L_DETAIL_ASSURE"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DOS_L_GED"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DOS_L_GEST"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_LIMITE_ANUELLE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_MNT_RESIDUEL"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_ANNEE"/></TH>
   		 <%--<TH><ct:FWLabel key="JSP_RF_QD_L_PERIODE"/></TH>--%>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_GENRE_PC"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_ETAT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_GENRE_QD"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_TYPE_SOIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_SOUS_TYPE_SOIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_ID_QD"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_QD_L_PERIODE"/></TH>
   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
		<%-- tpl:put name="zoneList" --%>
		<%	
		 if(formatter != null) {
			 RFQdJointDossierJointTiersJointDemandeViewBean lineVb = formatter.getNextElement();
			 //i=formatter.getI();
			 
			 
				if(lineVb != null) {
					condInt++;
			    	//actionDetail = targetLocation  + "='" + detailLink + lineVb.getIdPrestation()+"'";
			
					String urlForMenuPopUp =  detailLink + lineVb.getIdQd()+
									 "&csGenreQd=" + lineVb.getCsGenreQd()+
									 "&codeTypeDeSoinList=" + lineVb.getQdAssureCodeTypeDeSoin() + 
									 "&codeSousTypeDeSoinList="+ lineVb.getQdAssureCodeSousTypeDeSoin()+
									 "&csNationalite="+lineVb.getCsNationalite()+
									 "&idGestionnaire"+lineVb.getIdGestionnaire();
			
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
		
			<TD valign="top" class="mtd" nowrap width="20px">
			     	<ct:menuPopup menu="cygnus-optionsqds" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>">
			     			<ct:menuParam key="idDossier" value="<%=lineVb.getIdDossier()%>"/>  
				 	</ct:menuPopup>
		    </TD>
		     	
		    <TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getDetailAssureGroupBy()%></TD>
		     	<%
		     	// Iteration sur les nss de chaque membre famille regroupé dans une QD, pour créer les liens GED respectifs. 
		     		StringBuffer liensGedParTiers = new StringBuffer();
		     			for(String nss : formatter.getListNssForLinkGedGroupBy()){ 
		     				if(!JadeStringUtil.isEmpty(nss)){
		     			    	String urlGedParNss = servletContext + "/cygnus?" 
					     			+ "userAction=" + IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE + ".actionAfficherDossierGed" 
					     			+ "&noAVSId=" + nss 
					     			+ "&idTiersExtraFolder=" + null 
					     			+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
		     			     	
		     			     		liensGedParTiers.append("<a href='#' onclick=\"window.open('"+urlGedParNss+"','GED_CONSULT')\">"+viewBean.getSession().getLabel("JSP_LIEN_GED")+"</a></br></br>");
		     			    }
		     			} 
		     	%>
		     	
	     	<td align="center" valign="middle" class="mtd" nowrap ><%=liensGedParTiers.toString() %></td> 
		    	
	     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getGestionnaire()%></TD>
	     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=JadeStringUtil.isBlankOrZero(lineVb.getLimiteAnnuelle())?
		     													   " - ":new FWCurrency(lineVb.getLimiteAnnuelle()).toStringFormat()%></TD>
		     	<%if (lineVb.getIsPlafonnee()){%>
			     	<TD align="right" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(lineVb.getMntResiduelGroupBy()).toStringFormat()%></TD>
		     	<%}else{%>
		     		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%="-"%></TD>
		     	<%}%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getAnneeQd()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=JadeStringUtil.isBlankOrZero(lineVb.getCsGenrePCAccordee())?"-":viewBean.getSession().getCodeLibelle(lineVb.getCsGenrePCAccordee())%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(lineVb.getCsEtat())%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(lineVb.getCsGenreQd())%></TD>
				<%  String titleTypeDeSoin="-";
			        String titleSousTypeDeSoin="-";
			        String codeTypeDeSoin="-";
			        String codeSousTypeDeSoin="-";
					if (!JadeStringUtil.isBlank(lineVb.getQdAssureIdTypeDeSoin()) && !JadeStringUtil.isBlank(lineVb.getQdAssureIdSousTypeDeSoin())){
						titleTypeDeSoin = viewBean.getSession().getCodeLibelle(lineVb.getQdAssureIdTypeDeSoin());
						codeTypeDeSoin = lineVb.getQdAssureCodeTypeDeSoin();
					    titleSousTypeDeSoin = viewBean.getSession().getCodeLibelle(lineVb.getQdAssureIdSousTypeDeSoin());
						codeSousTypeDeSoin = lineVb.getQdAssureCodeSousTypeDeSoin();
					}
				%>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=titleTypeDeSoin%>' ><%=codeTypeDeSoin%></a></TD>
     	    <TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=titleSousTypeDeSoin%>' ><%=codeSousTypeDeSoin%></a></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getIdQd()%></TD>
			<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=lineVb.getPeriodesGrandeQdGroupBy()%></TD>
	<%}}%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
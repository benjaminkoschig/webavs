<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_COMPTE_BANCAIRE_CCP"
	idEcran="PPC0023";

	PCCompteBancaireCCPViewBean viewBean = (PCCompteBancaireCCPViewBean) session.getAttribute("viewBean");
	
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	
	if(viewBeanIsNew){
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel=false;
		bButtonUpdate=false;
		bButtonValidate=false;
	}
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCCompteBancaireCCPViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX_COMPTE_BANCAIRE_CCP="<%=IPCActions.ACTION_DROIT_COMPTE_BANCAIRE_CCP_AJAX%>";

	
	if($('.montantInteret').val()=="" || $('.montantInteret').val()=="0.00") 
	{ 				
		$('.montantFraisTd').hide();
		$('.montantFrais').hide();									 
	}	
	else{ 				
		$('.montantFraisTd').show();		 
		$('.montantFrais').hide();
	}	
				
	
$(function(){
	$('.typePropriete').change(function() {
		var value=($(this).attr("value"));		
		 
		if(value=="64009004") 
		{ 				
			$('.part').attr("readonly",true);
			$('.part').css("color","red");
			$('.part').val("1/1");										 
		}	
		else{ 				
			$('.part').attr("readonly",false);
			$('.part').css("color","black");		 
		}	
				
	});	

});
	
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/CompteBancaireCCP_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/CompteBancaireCCP_de.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<div class="areaAssure">
							<%=viewBean.getRequerantDetail(objSession) %>
						</div>
					
						<hr/>
						
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_FORTUNE_USUELLE,request,servletContext + mainServletPath)%>
						
						<div class="conteneurMembres">
						
							<% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>
						
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>">
								<div class="areaTitre">
									<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<table class="areaDFDataTable">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon" >&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_PROPRIETE"/></th>
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_PART"/></th>
										<!--<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_COMPTE"/></th> -->
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_IBAN"/></th>
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_MONTANT"/></th>											
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_INTERET"/></th>
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_MONTANT_INTERETS"/></th>
											<th><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_MONTANT_FRAIS"/></th>											
											<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_DESSAISISSEMENT_FORTUNE"/></th>
											<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_DESSAISISSEMENT_REVENU"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										FortuneUsuelle donneeComplexe=(FortuneUsuelle)itDonnee.next();
										
										SimpleCompteBancaireCCP donnee=(SimpleCompteBancaireCCP)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=objSession.getCode(donnee.getCsTypePropriete()) %></td>
											<td><%=donnee.getPartProprieteNumerateur() %> / <%=donnee.getPartProprieteDenominateur() %></td>
											<td><%=viewBean.formatIbanForTD(donnee.getIban())%></td>
											<td style="text-align:right;"><%=new FWCurrency(donnee.getMontant()).toStringFormat() %></td>
											<td><% if(donnee.getIsSansInteret().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%>
											</td>
											<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantInteret()).toStringFormat() %></td>
											<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantFraisBancaire()).toStringFormat() %></td>											
											<td><% if(dfHeader.getIsDessaisissementFortune().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
											<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
											<td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
										</tr>
							<%
										idGroup=dfHeader.getIdEntityGroup();
									}
										
							%>
									</tbody>
								</table>
								<div class="areaDFDetail">
									<table>
										<tr>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_L_PROPRIETE"/></td>
											<td><ct:select styleClass="typePropriete"  name="champTypeDePropriete" notation="data-g-select=mandatory:true" >
													<ct:optionsCodesSystems csFamille="PCTYPPROP">
														<ct:excludeCode code="64009003"/>
													</ct:optionsCodesSystems>
												</ct:select>
											 </td>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_PART"/></td>
											<td><input type="text" class="part" value="1/1" data-g-string="mandatory:true"/></td>
										</tr>
										<tr>
											<!--  <td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_COMPTE"/></td>
											<td><ct:select styleClass="typeCompte"  name="champTypeCompte">
													<ct:optionsCodesSystems csFamille="PCTYPCOMP" />
												</ct:select></td>-->
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_IBAN"/></td>
											<td colspan="3"><input type="text" class="iban libelleLong" data-g-iban="mandatory:true"/></td>																				
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_MONTANT"/></td>
											<td><input type="text" class="montant" data-g-amount='mandatory:true ,periodicity:Y'></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_INTERET"/></td>
											<td><input type="checkbox" class="interet"/></td>	
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_MONTANT_INTERETS"/></td>
											<td><input type="text" class="montantInteret" data-g-amount='periodicity:Y'
													   data-g-commutator="context:($(this).parents('.areaDFDetail')),
													   					  condition:($(this).val() =='0.00'||$.trim($(this).val()) ==''),
													   					  actionTrue: hide(context.find('.montantFraisTd')),
													   					  actionFalse:show(context.find('.montantFraisTd'))"
											    />
											</td>	
											<td class="montantFraisTd"><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_MONTANT_FRAIS"/></td>
											<td class="montantFraisTd"><input type="text" class="montantFrais" data-g-amount='mandatory:true, periodicity:Y'/></td>																														
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_DESSAISISSEMENT_FORTUNE"/></td>
											<td><input type="checkbox" class="dessaisissementFortune" /></td>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_DESSAISISSEMENT_REVENU"/></td>
											<td><input type="checkbox" class="dessaisissementRevenu" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_COMPTE_BANCAIRE_CCP_D_DATE_FIN"/></td>
											<td><input name="dateFin" value="" data-g-calendar="type:month" /></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_COMPTE_BANCAIRE_CCP_AJAX%>" crud="cud">
										<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
									</ct:ifhasright>
								</div>
							</div>
							<%
								}
							%>
						</div>
					</div>
				</TD>
			</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>
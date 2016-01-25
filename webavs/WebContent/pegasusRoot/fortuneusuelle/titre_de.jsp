<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
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
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCTitreViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_TITRE"
	idEcran="PPC0023";

	PCTitreViewBean viewBean = (PCTitreViewBean) session.getAttribute("viewBean");
	
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


<%@page import="globaz.pegasus.utils.PCCommonHandler"%>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var CS_GENRE_TITRE_AUTRES  =<%=IPCDroits.CS_GENRE_TITRE_AUTRES%>;
	var ACTION_AJAX_TITRE="<%=IPCActions.ACTION_DROIT_TITRE_AJAX%>";

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

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/Titres_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/Titres_de.js"/></script>

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
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_TITRE_L_PROPRIETE"/></th>
											<th><ct:FWLabel key="JSP_PC_TITRE_L_PART"/></th>
											<th><ct:FWLabel key="JSP_PC_TITRE_L_TITRES"/></th>											
											<th><ct:FWLabel key="JSP_PC_TITRE_L_DESIGNATION"/></th>											
											<th><ct:FWLabel key="JSP_PC_TITRE_L_VALEUR"/></th>
											<th><ct:FWLabel key="JSP_PC_TITRE_L_MONTANT"/></th>
											<th><ct:FWLabel key="JSP_PC_TITRE_L_SANS_RENDEMENT"/></th>											
											<th><ct:FWLabel key="JSP_PC_TITRE_L_RENDEMENT"/></th>
											<th><ct:FWLabel key="JSP_PC_TITRE_L_DROIT_DE_GARDE"/></th>
											<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_TITRE_L_DF"/></th>
											<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_TITRE_L_DR"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_TITRE_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										FortuneUsuelle donneeComplexe=(FortuneUsuelle)itDonnee.next();
										
										SimpleTitre donnee=(SimpleTitre)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=objSession.getCode(donnee.getCsTypePropriete()) %></td>
											<td><%=donnee.getPartProprieteNumerateur() %> / <%=donnee.getPartProprieteDenominateur() %></td>
											<td><%=objSession.getCodeLibelle(donnee.getCsGenreTitre()) %></td>
											<td><%=donnee.getDesignationTitre() %></td>
											<td><%=donnee.getNumeroValeur() %></td>
											<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantTitre()).toStringFormat() %></td>
											<td><% if(donnee.getIsSansRendement().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%>
											</td>
											<td style="text-align:right;"><%=new FWCurrency(donnee.getRendementTitre()).toStringFormat() %></td>
											<td style="text-align:right;"><%=new FWCurrency(donnee.getDroitDeGarde()).toStringFormat() %></td>											
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
											<td><ct:FWLabel key="JSP_PC_TITRE_D_PROPRIETE"/></td>
											<td><ct:select styleClass="typePropriete"  name="champTypeDePropriete" notation="data-g-select='mandatory:true'">
													<ct:optionsCodesSystems csFamille="PCTYPPROP">
														<ct:excludeCode code="64009003"/>
														<ct:excludeCode code="64009005"/>
													</ct:optionsCodesSystems>
												</ct:select>
											 </td>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_PART"/></td>
											<td><input type="text" class="part" value="1/1" data-g-string="mandatory:true"/></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_TITRES"/></td>
											<td><ct:select styleClass="genre"  name="genre" notation="data-g-select='mandatory:true'" >
													<ct:optionsCodesSystems csFamille="PCGENTITR" />
												</ct:select>
											</td>
											<td class="cacherAutres" ><ct:FWLabel key="JSP_PC_TITRE_D_AUTRES"/></td>
											<td class="cacherAutres">
											  <input type="text" class="autres"
											  data-g-commutator="context:$(this).parents('.areaDFDetail'),
											  		 			 master:context.find('.genre'),
											  		 			 condition:context.find('.genre').val()==CS_GENRE_TITRE_AUTRES,
											  		 			 actionTrue:¦show(context.find('.cacherAutres')),mandatory()¦,
											  		 			 actionFalse:¦hide(context.find('.cacherAutres')),notMandatory()¦" 
												/></td>																				
											<td><ct:FWLabel key="JSP_PC_TITRE_L_DESIGNATION"/></td>
											<td><input type="text" class="designation" ></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_VALEUR"/></td>
											<td><input type="text" class="valeur"/></td>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_MONTANT"/></td>
											<td><input type="text" class="montant" data-g-amount="periodicity:Y" /></td>		
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_SANS_RENDEMENT"/></td>										
											<td><input type="checkbox" class="sansRendement" /></td>									
											<td><ct:FWLabel key="JSP_PC_TITRE_D_RENDEMENT"/></td>
											<td><input type="text" class="rendement" data-g-amount="periodicity:Y" /></td>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_DROIT_DE_GARDE"/></td>
											<td><input type="text" class="droitGarde" data-g-amount="periodicity:Y" /></td>
										</tr>
										<tr>																											
											<td><ct:FWLabel key="JSP_PC_TITRE_D_DESSAISISSEMENT_FORTUNE"/></td>
											<td><input type="checkbox" class="dessaisissementFortune" /></td>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_DESSAISISSEMENT_REVENU"/></td>
											<td><input type="checkbox" class="dessaisissementRevenu" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_TITRE_D_DATE_FIN"/></td>
											<td><input type="text" name="dateFin" value="" data-g-calendar="type:month" /></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_TITRE_AJAX%>" crud="cud">
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
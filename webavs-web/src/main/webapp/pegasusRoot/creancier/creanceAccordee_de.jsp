<%@page import="ch.globaz.pegasus.navigation.NavigatorInterface"%>
<%@page import="ch.globaz.pegasus.navigation.creancier.PCCreanceAccordeeLink"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.PCAccordee"%>
<%@page import="globaz.pegasus.vb.creancier.PCCreanceAccordeeViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.creancier.Creancier"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.pegasus.utils.PCCreancierHandler"%>
<%@page import="ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee"%>

<%-- tpl:insert attribute="zoneInit" --%>
 
<%// Les labels de cette page commence par la préfix "JSP_PC_CRANCIER_D"
	idEcran="PPC0051";
	PCCreanceAccordeeViewBean viewBean = (PCCreanceAccordeeViewBean)session.getAttribute("viewBean");
	String idDecision = viewBean.getIdDecision();
	
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	boolean viewBeanIsNew = true;
	bButtonCancel = true; 
	bButtonValidate = true;
	bButtonDelete = false;
	String idDemande = request.getParameter("idDemandePc");
	selectedIdValue = viewBean.getId();
	String rootPath = servletContext+(mainServletPath+"Root");
	int nbCreancier = viewBean.getListCreancier().size();
	bButtonUpdate = (IPCDroits.CS_CALCULE.equals(viewBean.getCurrentVersionedDroit().getCsEtatDroit()));
	//bButtonNew = "add".equals(request.getParameter("_method"));
	%>
	
<%@ include file="/theme/detail/javascripts.jspf" %>
<%
    NavigatorInterface linkNavBar;
	
	if(idDecision == null || idDecision.isEmpty()){
	    linkNavBar = new PCCreanceAccordeeLink(idDemande);
	}else{
	    linkNavBar = new PCCreanceAccordeeLink(idDemande,idDecision);
	}
	 %>
<%@ include file="/pegasusRoot/scripts/navBar.jspf" %>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/droit.css"/>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionscreanciers">
	<%if (idDecision == null || idDecision.isEmpty()) {%>
		<ct:menuActivateNode active="no" nodeId="DETAIL_DECISION"/>
		<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="DETAIL_DECISION"/>
		<ct:menuSetAllParams key="idDecision" value="<%= idDecision %>"/>
		<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getCurrentVersionedDroit().getIdVersionDroit()%>"/>
	<%}%> 
</ct:menuChange>


<script type="text/javascript">
  var url = "<%=IPCActions.ACTION_CREANCIER%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
  var s_repartionNonCorrect =  "<ct:FWLabel key='JSP_PC_CRANCIER_D_REPARTITION_NON_CORRECT'/>";
  var $saveBtn = null;
  
  //ON ready
  $(function () {
	 $saveBtn = $('#btnVal') ;
  });
  
  //Binding event à la fin chargement moteur
  $('html').on(eventConstant.NOTATION_MANAGER_DONE,function() {
	
	var $maxDispoCell = $('#montantDispoPlafond');
	var s_maxDispo = $maxDispoCell.text();
	creanceAccordee.clearColoneDispo();

	//Bind change sur total ligne pca
	$('.totalSommeRepartis').on('change',function (event,data) {
		//element td
		var $element = $(this);
		$element.html("&nbsp;");
		//montantRetro pour pca
		var $mntRetroCell = $element.closest("tr").find('.montantRetroactif');
		var s_mntRetro = $mntRetroCell.text();
		//montant repartis pour pca
		var n_mntSolde = data.somme;
		//erreur dépassement
		var isError = false;
		
		//Gestion des erreurs si erreur deal retro, ok, sinon check error dispo
		if(!creanceAccordee.dealRetroError($mntRetroCell,n_mntSolde,$element)){
			isError = creanceAccordee.dealDispoError($maxDispoCell,n_mntSolde,s_mntRetro,s_maxDispo,$element);
		}else {
			isError = true;
		}
		
		//Si on est pas en erreur, check différences
		if(!isError){
			isError = creanceAccordee.dealDiffError();
		}
		//gestion du bouton valider
		$saveBtn.prop('disabled',isError);
		
	});
  });
</script>

<%-- /tpl:insert --%>
<link type="text/css" href="<%=rootPath %>/css/creancier/creanceAccordee.css" rel="stylesheet" />
<script type="text/javascript" src="<%=rootPath %>/scripts/creancier/creanceAccordeeForm.js"></script>
<script type="text/javascript" src="<%=rootPath %>/scripts/creancier/creanceAccordee_js.js"></script>
<%@ include file="/theme/detail/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
	<ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_REPARTITION_CREANCES" /> 
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %> 

<tr>
	<td colspan="3"><%=viewBean.getRequerantDetail()%> <input type="hidden" id="idDemandePc" name="idDemandePc" value="<%=idDemande%>"></td>		
</tr>
<%-- 
<tr class="hauteur">
	<td class="strong"> <ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_DROIT_ET_VERSION" /></td>
	<td class="strong" > <%=viewBean.getIdDroit()+" / "+viewBean.getNoVersionDroit()%> </td>
	<td></td>
</tr>  --%>

<tr><td class="separation" colspan="<%=nbCreancier+3%>">&nbsp;</td></tr>

 <!--  **************** Zone détail creanciers et creances **************** -->
<tr>
	<td colspan="<%=nbCreancier+3%>">
		<table width="100%" class="creanceACC">
			<tr>
				<td class='libellTabCreance'></td>
				<td class='tailleMontant'></td>
				<%for(Creancier creancier:viewBean.getListCreancier()){%>
					<td class='tailleMontant' data-p-cellsum="vertical:none" >
					 <strong>
					 	<%=viewBean.getDescriptionCreancier(creancier,objSession)%>
					 </strong>
					</td>
				<% } %>
				<td class=''></td>
			</tr>
			
			<tr>
				<td class='libellTabCreance strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_MONTANT_REVENDIQUE" /></td>
				<td class='tailleMontant'></td>
				<%for(Creancier creancier:viewBean.getListCreancier()){%>
					<td data-g-amountformatter=" " class='tailleMontant' 
					    id="creancierMontant_<%=creancier.getSimpleCreancier().getId() %>"><%=creancier.getSimpleCreancier().getMontant()%></td>
				<% } %>
				<td class=''></td>
			</tr> 
			
			<tr>
				<td class='libellTabCreance strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_MONTANT_DEAJA_REMBOURSE" /></td>
				<td class='tailleMontant'></td>
				<%for(Creancier creancier:viewBean.getListCreancier()){%>
					<td class='tailleMontant' id="montantRembourse_<%=creancier.getSimpleCreancier().getId()%>" 
					 data-p-cellsum="vertical:minus"
					 data-g-amountformatter=" "><%=viewBean.getMontantDejaRembourseByIdCreancier(creancier.getId())%></td>
				<% } %>
				<td class=''></td>
			</tr>
			<tr>
				<td class='libellTabCreance strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_MONTANT_RESTE_REMBOURSE" /></td>
				<td class='tailleMontant'></td>
				<%for(Creancier creancier:viewBean.getListCreancier()){%>
					<td class='tailleMontant somme' data-g-cellsum=" " data-g-amountformatter=" "></td>
				<% } %>
				<td class=''></td>
			</tr>
		</table>
	</td>
</tr>

<tr><td class="separation" colspan="<%=nbCreancier+3%>">&nbsp;</td></tr>

<!--  **************** Zone de répartitions des créances **************** -->
<tr>
	<td colspan="<%=nbCreancier+3%>" class="strong"><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_REPARTITIONS" /> </td>
</tr>
<tr>
	<td colspan="<%=nbCreancier+3%>" >
		<table id="repartition" width="100%" class="creanceACC" >
		<tr>
			<td class='libelle strong'></td>
			<td class='tailleMontant strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_MONTANT_RETROACTIF" /></td>
			<td colspan="<%=nbCreancier%>" ></td>
			<td class=''></td>
		</tr>

		<%for(PCAccordee pcAccordee:viewBean.getListPcAccordee()){%>
			<tr>
				<td class='libelle' data-p-cellsum="horizontal:none"><%=viewBean.formateDescriptionPcAccordee(pcAccordee,objSession)%></td>
				<td class='tailleMontant montantRetroactif' data-g-amountformatter=" " data-p-cellsum="horizontal:plus"><%=viewBean.getMontantRetroBrutByIdPCA(pcAccordee.getId())%></td>
			<%for(Creancier creancier:viewBean.getListCreancier()){
				SimpleCreanceAccordee simpleCreanceAccordee = viewBean.getCreanceAccordeeByIdCreancierAndIdPcAccordee(creancier.getId(),pcAccordee.getId());
			    String montant = (simpleCreanceAccordee.getId()==null)?"":simpleCreanceAccordee.getMontant();
			    String id = (simpleCreanceAccordee.getId()==null)?"":simpleCreanceAccordee.getId();
			    %>
				<td class='tailleMontant' data-p-cellsum="horizontal:minus">
					<input id='<%="creanceAcc_"+creancier.getId()%>' 
					       class = "inputMontant"
					       name='<%="creanceAcc_"+creancier.getId()+"_"+pcAccordee.getId()+"_"+id%>' data-g-amount="unsigned:true" 
				           value='<%=montant%>'/>
				</td>
			<% } %>
				<td id="<%=pcAccordee.getSimplePCAccordee().getIdPCAccordee() %>" class="totalSommeRepartis" data-g-cellsum="horizontal:true,redifnegative:true">0</td>
			 </tr>
		 <% } %>
			<!--  soustraction déjé versé -->
			<% 
				String montantVerse = viewBean.getTotalDejaVerse();
				if(!JadeStringUtil.isBlank(montantVerse)){
				%>
				<tr>
					<td class='libelle strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_DEJA_VERSE" /></td>
					<td class='tailleMontant strong'  data-g-amountformatter=" "><%= montantVerse%></td>
				</tr>
				<% 
				} 
			%>
			
		<tr>
			<td class='libelle strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_MONTANT_TOTAL_DETTE" /></td> 
			<td  class='strong tailleMontant'  data-g-amountformatter=" ">-<%=viewBean.getMontantDette() %></td>
		</tr>
			
			<!--  soustraction à disposition -->
			<tr>
				<td class='libelle strong'><ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_MONTANT_DISPOSITION" /></td> 
				<td id="montantDispoPlafond" class='tailleMontant somme'  data-g-amountformatter=" "><%=viewBean.getMontantADispo() %></td>
				
			</tr>
			<!--  somme des créances -->
			<tr>
			<td class='libelle'> </td> 
			<td></td>
				<%for(Creancier creancier:viewBean.getListCreancier()){%>
					<td class='tailleMontant somme total' id="total_<%=creancier.getId()%>" data-g-amountformatter=" " data-g-cellsum=" ">0</td>
				<% } %>
				<td></td>
			</tr>
		 </table>
	 </td>
 </tr>
 
 <tr><td class="separation" colspan="<%=nbCreancier+3%>">&nbsp;</td></tr>
 
 <!--  **************** Zone de calcul différences ******************* -->
 <tr>
 	<td colspan="<%=nbCreancier+3%>" class="strong"> <ct:FWLabel key="JSP_PC_CREANCIE_ACCORDEE_D_DIFFERENCES" /> </td>
 </tr>
 
 <tr>
 	<td colspan="<%=nbCreancier+3%>" class="creanceACC">
 		<table width="100%">
	 		<tr id="difference">
			  <td class='libelleTabDifference'>&nbsp;</td>
			  <td class='tailleMontant'>&nbsp;</td>
				<%for(Creancier creancier:viewBean.getListCreancier()){%>
					<td class="tailleMontant strong diff" id="diff_<%=creancier.getId()%>"  data-g-amountformatter=" ">0</td>
				<% } %>
			  <td class="">&nbsp; </td>
		  	</tr>
		</table>
 	</td>
 </tr>
 <INPUT type="hidden" name="idDecision" value="<%=idDecision%>">
 <input type="hidden" name="idVersionDroit" value="<%=viewBean.getCurrentVersionedDroit().getIdVersionDroit()%>"/>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>

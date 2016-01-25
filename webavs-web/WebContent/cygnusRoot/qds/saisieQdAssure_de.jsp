<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.qds.RFQdAbstractViewBean"%>
<%@page import="globaz.cygnus.api.qds.IRFQd"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.cygnus.vb.qds.RFSaisieQdViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_QD_S"
	idEcran="PRF0017";

	RFSaisieQdViewBean viewBean = (RFSaisieQdViewBean) session.getAttribute("viewBean");	
	
	//autoShowErrorPopup = true;
	
	String showModalDialogURLDetailSoldCharges = servletContext + "/cygnusRoot/qds/majSoldeCharge_dialog.jsp?";
	String showModalDialogURLDetailAugmentation = servletContext + "/cygnusRoot/qds/majAugmentation_dialog.jsp?";
	//System.out.println(userActionUpd);
	if (viewBean.isAfficherDetail()) {
		bButtonDelete = true;
		//bButtonUpdate = true;
		bButtonValidate = true;
		bButtonCancel = true;
		bButtonNew = false;
	}else{
		bButtonDelete 	= false;
		bButtonUpdate 	= false;
		bButtonValidate = true;
		bButtonCancel 	= true;
		bButtonNew = false;
	}
	
	String blank = "";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsqds">
</ct:menuChange>

<script language="JavaScript">

<%@ include file="../utils/rfUtilsQd.js" %>

function readOnly(flag) {
	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
  for(i=0; i < document.forms[0].length; i++) {
      if (!document.forms[0].elements[i].readOnly &&
	       document.forms[0].elements[i].type != 'hidden') {
          
          document.forms[0].elements[i].disabled = flag;
      }
  }
}

function add(){
	
}

function cancel() {
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE%>.chercher";
}  

function onChangeGenreQd() {
	
	if(document.getElementsByName("csGenreQd")[0].value != ""){
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD%>"+".afficherBasDePage";
		document.location.href="cygnus?userAction="+"<%=IRFActions.ACTION_SAISIE_QD%>"+".afficherBasDePage";
		document.forms[0].submit();
	}
}

function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.ajouter";
    }else{
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.modifier";
    }
    return state;
}

function upd(){
	document.getElementById("modifierDetailSolde").style.display = "block";
	document.getElementById("modifierDetailAugmentation").style.display = "block";
}

function typeSousTypeDeSoinListInit(){

	document.getElementsByName("codeTypeDeSoinList")[0].options[parseInt("<%=viewBean.getCodeTypeDeSoinList()%>",10)].selected = true;
	document.getElementsByName("codeTypeDeSoin")[0].value="<%=viewBean.getCodeTypeDeSoinList()%>";
	document.getElementsByName("imgCodeTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;
	document.getElementsByName("codeSousTypeDeSoin")[0].value="<%=viewBean.getCodeSousTypeDeSoinList()%>";
	document.getElementsByName("imgCodeSousTypeDeSoin")[0].src=<%="'"+request.getContextPath()+viewBean.getImageSuccess()+"'"%>;


	currentSousTypeDeSoinTab=sousTypeDeSoinTab[parseInt(document.getElementsByName("codeTypeDeSoin")[0].value,10)-1];
	currentCodeSousTypeList=currentSousTypeDeSoinTab[0];
	currentLibelleSousTypeList=currentSousTypeDeSoinTab[1];
	codeSousTypeDeSoinList=document.getElementsByName("codeSousTypeDeSoinList")[0];
	codeSousTypeDeSoinList.options.length=0;
	codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length]=new Option("","");
			
	for (i=0; i<currentCodeSousTypeList.length; i++){
		codeSousTypeDeSoinList.options[codeSousTypeDeSoinList.options.length]=new Option(currentLibelleSousTypeList[i],currentCodeSousTypeList[i]);
	}

	codeSousTypeDeSoinList.options[parseInt("<%=viewBean.getCodeSousTypeDeSoinList()%>",10)].selected = true;
}

function init(){	
	onClickIsPlafonnee();
	typeSousTypeDeSoinListInit();
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors();
	errorObj.text="";
	<%}%>
	
}

function postInit(){
	<%if (!viewBean.isAfficherDetail()) {%>
			action('add');

			$("[name=dateDebut]").change(function () {
				$("[name=idPotSousTypeDeSoin]").val("");
				$("[name=isLimiteAnnuelleImg]").attr("src","<%=request.getContextPath()+viewBean.getImageError()%>");
				$("[name=isFamilleOk]").val("false");
			});
		
			//$("[name=dateFin]").change(function () {
			//	$("[name=idPotSousTypeDeSoin]").val("");
			//	$("[name=isLimiteAnnuelleImg]").attr("src","request.getContextPath()+viewBean.getImageError()");
			//});
			
	<%}else{%>
			
			action('read');
			
			document.getElementById("soldeCharge").disabled=true;
			document.getElementById("soldeCharge").readOnly=true;

			document.getElementById("augmentationQd").disabled=true;
			document.getElementById("augmentationQd").readOnly=true;

			document.getElementById("modifierDetailSolde").style.display = "none";
			document.getElementById("modifierDetailAugmentation").style.display = "none";

			document.getElementById("dateCreation").disabled=true;
			document.getElementById("dateCreation").readOnly=true;

			//document.getElementById("anchor_dateCreation").disabled=true;
			//document.getElementById("anchor_dateCreation").readOnly=true;

			document.getElementById("dateDebut").disabled=true;
			document.getElementById("dateDebut").readOnly=true;

			document.getElementById("idGestionnaire").disabled=true;
			document.getElementById("idGestionnaire").readOnly=true;

			//document.getElementById("anchor_dateDebut").disabled=true;
			//document.getElementById("anchor_dateDebut").readOnly=true;

			document.getElementById("isPlafonnee").disabled=true;
			document.getElementById("isPlafonnee").readOnly=true;
			
	<%}%>

	document.getElementById("limiteAnnuelle").disabled=true;
	document.getElementById("limiteAnnuelle").readOnly=true;

	document.getElementById("csGenreQd").disabled=true;
	document.getElementById("csGenreQd").readOnly=true;

	document.getElementById("codeTypeDeSoinList").disabled=true;
	document.getElementById("codeTypeDeSoinList").readOnly=true;
	
	document.getElementById("codeSousTypeDeSoinList").disabled=true;
	document.getElementById("codeSousTypeDeSoinList").readOnly=true;
			
	document.getElementById("codeTypeDeSoin").disabled=true;
	document.getElementById("codeTypeDeSoin").readOnly=true;

	document.getElementById("codeSousTypeDeSoin").disabled=true;
	document.getElementById("codeSousTypeDeSoin").readOnly=true;

	document.getElementsByName("csSource")[0].readOnly=true;
	document.getElementsByName("csSource")[0].disabled=true;

	$("[name=csEtat]").change(function() {		
		// si vaut cloturé
		if($("[name=csEtat]").val()=="<%=IRFQd.CS_ETAT_QD_CLOTURE%>"){
			//alors on met la date de fin à la date du jour			
			$("#dateFin").val("<%=JACalendar.today().toStr(".")%>");
		}
	});
}

function afficherHistoriqueAugmentation(){
	var url=	"<%=showModalDialogURLDetailAugmentation%>idQd="+document.getElementsByName("idQd")[0].value;
	window.open(url,null,"dialogHeight:200px;dialogWidth:800px;help:no;status:no;resizable:no;scroll:yes");
}

function afficherHistoriqueSolde(){
	var url=	"<%=showModalDialogURLDetailSoldCharges%>idQd="+document.getElementsByName("idQd")[0].value;
	window.open(url,null,"dialogHeight:200px;dialogWidth:800px;help:no;status:no;resizable:no;scroll:yes");
}

function modifierDetailSolde(){
	
	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_SOLDE_CHARGE%>.chercher";
	document.forms[0].method = "get";
	document.forms[0].submit();
	
}

function modifierDetailAugmentation(){

	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_AUGMENTATION%>.chercher";
	document.forms[0].method = "get";
	document.forms[0].submit();
}

function onClickIsPlafonnee(){
	if (document.getElementsByName("isPlafonnee")[0].checked){
		document.getElementById("tBodyMntResiduelId").style.display = "block";
		document.getElementById("tBodyAugmentationQdId").style.display = "block";
		document.getElementById("tBodyLimiteAnnuelleId").style.display = "block";
		document.getElementById("soldeChargeCharId").style.visibility='visible';
		<%if (viewBean.isAfficherDetail()){%>
			document.getElementById("soldeChargeCharId").innerHTML='-';
		<%}else {%>
			//$("#btnMajLimiteAnnuelle").show();
		<%}%>
	}else{
		document.getElementById("tBodyMntResiduelId").style.display = "none";
		document.getElementById("tBodyAugmentationQdId").style.display = "none";
		document.getElementById("tBodyLimiteAnnuelleId").style.display = "none";
		document.getElementById("soldeChargeCharId").style.visibility='hidden';
		 <%if (viewBean.isAfficherDetail()){%>
			document.getElementById("soldeChargeCharId").innerHTML='+';
		<%}else {%>
			//$("#btnMajLimiteAnnuelle").hide();
		<%}%>
	}

	
}

function majLimiteAnnuelle(){
	
	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.majLimiteAnnuelleQdAssure";
	document.forms[0].method = "get";
	document.forms[0].submit();
}

function del(){
	if (window.confirm("<ct:FWLabel key='WARNING_RF_QD_S_JSP_DELETE_MESSAGE_INFO'/>")){
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_PIED_DE_PAGE%>.supprimer";
		document.forms[0].submit();
	}
	
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_QD_S_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
				<%if (viewBean.isAfficherDetail()){%>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_NO_QD"/></TD>
                	<TD colspan="5">
                		<B><%=viewBean.getIdQdAssure()%></B>
                	</TD>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <%}%>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_GESTIONNAIRE"/></TD>
					<TD colspan="5">
						<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
                        																																viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
						<INPUT type="hidden" name="nss" value="<%=viewBean.getNss()%>"/>
						<INPUT type="hidden" name="provenance" value="<%=viewBean.getProvenance()%>"/>
						<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>
						<INPUT type="hidden" name="idQd" value="<%=viewBean.getIdQdAssure()%>"/>
						<INPUT type="hidden" name="idQdPrincipale" value="<%=viewBean.getIdQdPrincipale()%>"/>
						<INPUT type="hidden" name="csGenreQdHistorique" value="<%=viewBean.getCsGenreQd()%>"/>
						<INPUT type="hidden" name="idPotSousTypeDeSoin" value="<%=viewBean.getIdPotSousTypeDeSoin()%>"/>
						<INPUT type="hidden" name="anneeQd" value="<%=viewBean.getAnneeQd()%>"/>
						<INPUT type="hidden" name="isLimiteAnnuelleOk" value="<%=viewBean.getIsLimiteAnnuelleOk()%>"/>
						<INPUT type="hidden" name="isFamilleOk" value="<%=viewBean.getIsFamilleOk()%>"/>
						<INPUT type="hidden" name="_codeTypeDeSoinList" value="<%=viewBean.getCodeTypeDeSoinList()%>"/>
						<INPUT type="hidden" name="_codeSousTypeDeSoinList" value="<%=viewBean.getCodeSousTypeDeSoinList()%>"/>
					</TD>
				</TR>
				<TR><TD colspan="6">&nbsp;</TD></TR>
				<TR>
					<TD><ct:FWLabel key="JSP_RF_QD_S_TIERS"/></TD>
					<TD colspan="5">
						<%=viewBean.getDetailAssure()%>
					</TD>
				</TR>
				
				<%if (!JadeStringUtil.isEmpty(viewBean.getDetailFamille())){%>
					<TR><TD colspan="6">&nbsp;</TD></TR>
	                <TR><TD colspan="6"><HR></HR></TD></TR>
	                <TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
						<TD valign="top" width="200px"><ct:FWLabel key="JSP_RF_QD_S_FAMILLE"/></TD>
						<TD colspan="5">
							<TABLE>				
								<%=viewBean.getDetailFamille()%>
							</TABLE>			
						</TD>					
					</TR>
				<%}%>
					
				<TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6"><HR></HR></TD></TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_GENRE_QD"/></TD>
	                <TD colspan="5">
	                	<SELECT name="csGenreQd" onchange="onChangeGenreQd()">
	                        <%Vector genreQdData = viewBean.getGenresQdData();
	                          for (int i=0;i<genreQdData.size();i++){%>
	                        	<OPTION value="<%=((String[]) genreQdData.get(i))[0]%>" <%if (((String[]) genreQdData.get(i))[0].equals(viewBean.getCsGenreQd())){%> selected="selected" <%}%>><%=((String[]) genreQdData.get(i))[1]%></OPTION>
	                        <%}%>
	                     </SELECT>
	                </TD>
                </TR>
                
                
               	<TR><TD colspan="6">&nbsp;</TD></TR>
               	<TR><TD colspan="6"><HR></HR></TD></TR>
               	<TR><TD colspan="6">&nbsp;</TD></TR>
               	
	            <%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
                <TR><TD><INPUT type="hidden" name="isSaisieDemande" value="false"/></TD></TR>
                <TR><TD><INPUT type="hidden" name="isEditSoins" value="false"/></TD></TR>
                <TR><TD><INPUT type="hidden" name="isSaisieQd" value="true"/></TD></TR>
                
                <TR><TD>&nbsp;</TD></TR>
                <TR><TD>&nbsp;</TD></TR>
                
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_CREATION"/></TD>
                	<TD colspan="5"><input data-g-calendar=" "  name="dateCreation" value="<%=JadeStringUtil.isBlankOrZero(viewBean.getDateCreation())?
                																	JACalendar.todayJJsMMsAAAA():viewBean.getDateCreation()%>"/></TD>
                </TR>
                <TR>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_DEBUT"/></TD>
                	<TD><input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></TD>
                	<TD><ct:FWLabel key="JSP_RF_QD_S_DATE_FIN"/></TD>
                	<TD><input data-g-calendar=" "  name="dateFin" value="<%=JadeStringUtil.isBlankOrZero(
                												   viewBean.getDateFin())?blank:viewBean.getDateFin()%>"/></TD>
                	<%if (!viewBean.isAfficherDetail()){%>
	                	<TD>
	                		<INPUT type="button" id="btnMajLimiteAnnuelle" value="<ct:FWLabel key="JSP_RF_QD_S_MAJ_LIMITE_ANNUELLE"/>" onclick="majLimiteAnnuelle();">
	                	</TD>
                	<%}%>
                </TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR><TD colspan="6">&nbsp;</TD></TR>
                <TR>
                	<TD colspan="6">
                	<TABLE>
		                <TR>
		                	<TD width="233px"><ct:FWLabel key="JSP_RF_QD_S_IS_PLAFONNEE"/></TD>
		                	<TD><INPUT type="checkbox" name="isPlafonnee" onclick="onClickIsPlafonnee();" <%=viewBean.getIsPlafonnee().booleanValue()?"CHECKED":""%>/></TD>
		                </TR>
		                <TBODY id="tBodyLimiteAnnuelleId">
			                <TR>
			                	<TD><ct:FWLabel key="JSP_RF_QD_S_LIMITE_ANNUELLE"/></TD>
			                	<TD><INPUT type="text" name="limiteAnnuelle" value="<%=viewBean.getLimiteAnnuelle()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/></TD>
			                	<%if (!viewBean.isAfficherDetail()){%>
			                		<TD colspan="2"><IMG src="<%=request.getContextPath()+viewBean.getImageLimiteAnnuelle()%>" alt="" name="isLimiteAnnuelleImg"></TD>
			                	<%}%>
			                </TR>
		                </TBODY>
		                <TR>
		                	<TD><ct:FWLabel key="JSP_RF_QD_S_SOLDE_CHARGE_RFM"/></TD>
		                	<TD>
								<INPUT type="text" name="soldeCharge" value="<%=viewBean.getSoldeCharge()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/>
							</TD>
							<TD>
								<B style="color:red;" id="soldeChargeCharId">-</B>
							</TD>
			                <%if (viewBean.isAfficherDetail()){%>
			                	<TD colspan="4" align="left">
									<TABLE>
			               				<TR>
											<TD>
												<a id="afficherHistoriqueSolde"  href="#" onclick="afficherHistoriqueSolde();">
													<ct:FWLabel key="JSP_RF_QD_S_HISTORIQUE"/>
												</a>
											</TD>
											<TD colspan="3" align="left">
												<a id="modifierDetailSolde" href="#" onclick="modifierDetailSolde();">
													<ct:FWLabel key="JSP_RF_QD_S_MODIFER"/>
												</a>
											</TD>
										</TR>
									</TABLE>
								</TD>
			                <%}else{%>
			                	<TD></TD>
			                <%}%>	                
		                </TR>
		                <TBODY id="tBodyAugmentationQdId">
		                <TR>
		                	<TD><ct:FWLabel key="JSP_RF_QD_S_AUGMENTATION_QD"/></TD>
		                	<TD>
		                		<INPUT type="text" name="augmentationQd" value="<%=viewBean.getAugmentationQd()%>" class="montant"  onkeypress="return filterCharForFloat(window.event);"/>
		                	</TD>
		                	<TD>
		                		<b style="color:blue;">+</b>
		                	</TD>
		                	<%if (viewBean.isAfficherDetail()){%>
		               			<TD align="left">
		               				<TABLE>
			               				<TR>
											<TD>
												<a id="afficherHistoriqueAugmentation"  href="#" onclick="afficherHistoriqueAugmentation();">
													<ct:FWLabel key="JSP_RF_QD_S_HISTORIQUE"/>
												</a>
											</TD>
											<TD colspan="3" align="left">
												<a id="modifierDetailAugmentation" href="#" onclick="modifierDetailAugmentation();">
													<ct:FWLabel key="JSP_RF_QD_S_MODIFER"/>
												</a>
											</TD>
										</TR>
									</TABLE>
								</TD>
		                	 <%}else{%>
			                	<TD></TD>
			                <%}%>                	
		                </TR>
		                </TBODY>
		                <%if (viewBean.isAfficherDetail()){%>
				                <TR>
				                	<TD><ct:FWLabel key="JSP_RF_QD_S_CHARGE_RFM"/></TD>
				                	<TD align="right">
				                		<div><b><%=new FWCurrency(viewBean.getMontantChargeRfm()).toStringFormat()%></b></div>
				                	</TD>
				                	<TD>
				                		<b style="color:red;">-</b>			                	
				                	</TD>
				                </TR>
			            <%}%>
		                <TBODY id="tBodyMntResiduelId">
			                <TR><TD></TD><TD><HR></HR></TD></TR>
			                <TR>
			                	<TD><ct:FWLabel key="JSP_RF_QD_S_QD_RESIDUELLE"/></TD>
			                	<TD align="right"><div id="idMntResiduelAffiche"></div></TD>
			                	<TD><b style="color:black;">=</b></TD>         	
			                </TR>
		                </TBODY>
		                <TR><TD colspan="6">&nbsp;</TD></TR>
		                <TR><TD colspan="6">&nbsp;<INPUT type="hidden" name="mntCharge" value="<%=viewBean.getMontantChargeRfm()%>"/></TD></TR>
					</TABLE>
					</TD>
				</TR>
				<TR>
					<TD width="233px"><ct:FWLabel key="JSP_RF_QD_S_ETAT_QD"/></TD>
					<TD>
						<ct:FWListSelectTag name="csEtat" data="<%=viewBean.getEtatsQdData(false)%>" defaut="<%=viewBean.getCsEtat()%>"/>
					</TD>
					<TD colspan="2"></TD>
					<TD><ct:FWLabel key="JSP_RF_QD_S_SOURCE_QD"/></TD>
					<TD>
						<ct:FWListSelectTag name="csSource" data="<%=viewBean.getSourcesQdData()%>" defaut="<%=viewBean.isAfficherDetail()?
																											   viewBean.getCsSource():IRFQd.CS_SOURCE_QD_GESTIONNAIRE%>"/>
					</TD>
				</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
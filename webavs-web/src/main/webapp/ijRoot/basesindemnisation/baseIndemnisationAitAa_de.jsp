<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/ijRoot/ijtaglib.tld" prefix="ij" %>
<%

	idEcran="PIJ0035";
	globaz.ij.vb.basesindemnisation.IJBaseIndemnisationAitAaViewBean viewBean = (globaz.ij.vb.basesindemnisation.IJBaseIndemnisationAitAaViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdBaseIndemisation();	
	String likeNSS=request.getParameter("likeNSS");
	if (likeNSS==null){
		likeNSS = "";
	}
	
	bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
	bButtonValidate = bButtonValidate && viewBean.isModifierPermis();
	bButtonDelete = bButtonDelete && viewBean.isSupprimerPermis();
	bButtonCancel = false;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<%@page import="globaz.ij.application.IJApplication"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal"/>
<ct:menuChange displayId="options" menuId="ij-basesindemnisations" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdBaseIndemisation()%>"/>
	<ct:menuSetAllParams key="forNoBaseIndemnisation" value="<%=viewBean.getIdBaseIndemisation()%>"/>
	
		<%if(IIJPrononce.CS_PETITE_IJ.equals(viewBean.getCsTypeIJ())||
			 	IIJPrononce.CS_GRANDE_IJ.equals(viewBean.getCsTypeIJ())||
				IIJPrononce.CS_FPI.equals(viewBean.getCsTypeIJ())){ %>
			 <ct:menuActivateNode active="no" nodeId="calculerait"/>
			 <ct:menuActivateNode active="no" nodeId="calculeraa"/>
		<%}else if(IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(viewBean.getCsTypeIJ())){ %>
			 <ct:menuActivateNode active="no" nodeId="calculergp"/>
			 <ct:menuActivateNode active="no" nodeId="calculeraa"/>
		<%}else if(IIJPrononce.CS_ALLOC_ASSIST.equals(viewBean.getCsTypeIJ())){ %>
			 <ct:menuActivateNode active="no" nodeId="calculergp"/>
			 <ct:menuActivateNode active="no" nodeId="calculerait"/>
		<%}%>
		
		<%if(viewBean.getCsEtat().equals(globaz.ij.api.basseindemnisation.IIJBaseIndemnisation.CS_ANNULE)){%>
			<ct:menuActivateNode active="no" nodeId="corrigerbi"/>
		<%} else {%>
			<ct:menuActivateNode active="yes" nodeId="corrigerbi"/>
		<%}%>
		
</ct:menuChange>

<SCRIPT language="javascript"> 

	function add() {
	    document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA%>.ajouter";
	}
	
	function upd() {
	}
	
	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA%>.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA%>.modifier";
	    }
	    
	    return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
			document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA%>.chercher";
		}	  
	}
	
	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA%>.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
	
	function readOnly(flag) {
		// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
	  for(i=0; i < document.forms[0].length; i++) {
	      if (!document.forms[0].elements[i].readOnly && 
	      	document.forms[0].elements[i].className != 'forceDisable' &&
	      	document.forms[0].elements[i].type != 'hidden') {
	          document.forms[0].elements[i].disabled = flag;
	      }
	  }
	}
	
	function nbJoursChange(){
		document.forms[0].elements('wantValidateNbJoursAit').value="<%=true%>";
	}
  
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdBaseIndemisation()%>" tableSource="<%=IJApplication.KEY_POSTIT_BASES_INDEMNISATION%>"/></span>
			<ct:FWLabel key="JSP_BASE_INDEMNISATION"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
					<TR>
						<td width="20%">&nbsp;</td>							
						<td width="50%" colspan="2">&nbsp;</td>
						<td width="30%">&nbsp;</td>
					</TR>
					<TR>
						<TD valign="top" align="left"><LABEL for="idPrononce"><ct:FWLabel key="JSP_BASE_IND_NO_PRONONCE"/></LABEL></TD>
						<TD colspan="3" align="left"><span><%=viewBean.getFullDescriptionPrononce()[0]%><br>
								  <%=viewBean.getDetailRequerant()%></span>
							<INPUT type="hidden" name="idPrononce" value="<%=viewBean.getIdPrononce()%>">
							<INPUT type="hidden" name="forIdPrononce" value="<%=viewBean.getIdPrononce()%>">														
							<INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
							<INPUT type="hidden" name="descr0" value="<%=viewBean.getFullDescriptionPrononce()[0]%>">
							<INPUT type="hidden" name="descr1" value="<%=viewBean.getFullDescriptionPrononce()[1]%>">
							<INPUT type="hidden" name="isPeriodeEtendue" value="true">
							<INPUT type="hidden" name="wantValidateNbJoursAit" value="<%=viewBean.wantValidateNbJoursAit()%>">
						</TD>
					</TR>
					
					<TR><td colspan="4"><br/></td></TR>				

					<TR>
						<TD><ct:FWLabel key="JSP_TYPE_IJ"/></TD>
						<TD colspan="3"><input type="text" value="<%=viewBean.getCsTypeIJLibelle()%>" disabled="disabled" class="forceDisable"></TD>
					</TR>


					<TR><td colspan="4"><hr/></td></TR>

					<TR>																		
						<td colspan="4">
							<TABLE cellspacing="5" align="left">
							<TR>
								<TD><ct:FWLabel key="JSP_DU"/></TD>
								<TD><ct:FWCalendarTag name="dateDebutPeriodeEtendue" value="<%=viewBean.getDateDebutPeriodeEtendue()%>" />
								<script language="javascript">
								 	  	element = document.getElementById("dateDebutPeriodeEtendue");
								 	  	element.onblur=function() {fieldFormat(this,'CALENDAR');}
								 	</script>
								</TD>
								<TD><ct:FWLabel key="JSP_AU"/></TD>
								<TD><ct:FWCalendarTag name="dateFinPeriodeEtendue" value="<%=viewBean.getDateFinPeriodeEtendue()%>" />
									<script language="javascript">
								 	  	element = document.getElementById("dateFinPeriodeEtendue");
								 	  	element.onblur=function() {fieldFormat(this,'CALENDAR');}
								 	</script>
								
								</TD>								 	
								<TD align="right"></TD>
							</TR>
							</TABLE>
						</td>
					</TR>
					
					<TR><td colspan="4"><hr/></td></TR>

					<TR>
						<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS"/></TD>
						<TD colspan="3"><INPUT type="text" name="nombreJoursInterne" value="<%=JadeStringUtil.isIntegerEmpty(viewBean.getNombreJoursInterne())?"":viewBean.getNombreJoursInterne()%>" onchange="nbJoursChange();"></TD>
					</TR>

					<TR>
						<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS_INTERRUPT"/></TD>
						<TD><INPUT type="text" name="nombreJoursInterruption" value="<%=viewBean.getNombreJoursInterruption()%>"></TD>

						<TD><ct:FWLabel key="JSP_BASE_IND_NBR_JRS_MOTIF_INT"/></TD>
						<TD>
							<ct:select name="csMotifInterruption" defaultValue="<%=viewBean.getCsMotifInterruption()%>">
							<ct:optionsCodesSystems csFamille="IJMOTIFINT"/>
							</ct:select>								
						</TD>
					</TR>							
					
					<TR><td colspan="4">&nbsp;</td></TR>
					
					<TR>
						<TD><ct:FWLabel key="JSP_BASE_IND_REMARQUE"/></TD>
						<TD><textarea name="remarque" cols="40" rows="5"><%=viewBean.getRemarque()%></textarea></TD>
						<TD><ct:FWLabel key="JSP_BASE_IND_ETAT"/></TD>
						<TD>
							<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>" disabled="true" styleClass="forceDisable">
							<ct:optionsCodesSystems csFamille="IJETABASIN"/>
							</ct:select>																
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
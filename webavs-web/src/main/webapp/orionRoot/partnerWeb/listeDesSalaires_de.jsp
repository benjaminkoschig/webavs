<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.orion.vb.partnerWeb.EBListeDesSalairesViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "GEB3004";
EBListeDesSalairesViewBean viewBean = (EBListeDesSalairesViewBean) session.getAttribute("viewBean");
userActionValue = "orion.partnerWeb.listeDesSalaires.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<SCRIPT language="javascript"> 

var showerHider = {
		 $trigerElement: null,
		 $elementsToShowOrHide : null, 
		 
		 init: function(triger, elementToShowOrHide){
			 this.$trigerElement = $(triger);
			 this.$elementsToShowOrHide = $(elementToShowOrHide);
			 var that = this;
			 if(this.$trigerElement.prop("checked")){
				 this.$elementsToShowOrHide.show();
			 } else {
				 this.$elementsToShowOrHide.hide()
			 }
			 this.$trigerElement.click(function (){
				 if(this.checked){
					 that.$elementsToShowOrHide.show();
				 } else {
					 that.$elementsToShowOrHide.hide();
				 }
			 })
		 }
	 }
	 
$(function () {
	 showerHider.init("#genererEtapesRappel",".etapeRappel");
})

 </SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<ct:FWLabel key="TITRE_LISTE_DES_SALAIRES"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="2"></TD>
                    	</TR>
			         	<TR> 
  				          	<TD><ct:FWLabel key="EMAIL"/></TD>
 				          	<TD> 
					           <ct:inputText name="email" />          
						    </TD>
          				</TR>
          				<ct:ifhasright element="leo.process.etapesSuivantes.afficherEtape" crud="crud">
          				<%if(viewBean.getDisplayGenererEtapesRappel()){ %>
	          				<TR> 
	  				          	<TD><ct:FWLabel key="GENERER_ETAPES_RAPPEL"/></TD>
	 				          	<TD> 
									<INPUT id="genererEtapesRappel" name="genererEtapesRappel" type="checkbox" <%=(viewBean.getGenererEtapesRappel())?"checked='checked'":"" %> >   
									<ct:FWLabel key="GENERER_ETAPES_RAPPEL_ADMINISTRATEUR"/>                     
							    </TD>
	          				</TR>
	          			<%} %>
          				</ct:ifhasright>
          				<TR class="etapeRappel">
							<TD><ct:FWLabel key="DATE_DE_REFERENCE"/></TD>
							<TD><ct:inputText name="forDateReference" notation="data-g-calendar=' '" /></TD>
						</TR>
						<TR class="etapeRappel">
							<TD><ct:FWLabel key="DATE_IMPRESSION"/></TD>
							<TD><ct:inputText name="forDateImpression"  notation="data-g-calendar=' '"  /></TD>
						</TR>
						<TR class="etapeRappel">
							<TD><ct:FWLabel key="MODE_SIMULATION"/></TD>
							
							<TD> 
							
							 <INPUT name="forIsSimulation" type="checkbox" <%=(viewBean.getForIsSimulation())?"checked='checked'":"" %> >
				
							</TD>
						</TR>
          				<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="EBMenuPrincipal"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
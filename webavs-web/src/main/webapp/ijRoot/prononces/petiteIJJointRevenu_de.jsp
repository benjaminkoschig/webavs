<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0003";

globaz.ij.vb.prononces.IJPetiteIJJointRevenuViewBean viewBean = (globaz.ij.vb.prononces.IJPetiteIJJointRevenuViewBean)(session.getAttribute("viewBean"));
String noAVS = request.getParameter("noAVS");
String detailRequerant = viewBean.getDetailRequerant();
bButtonValidate = false;
bButtonCancel = false;
bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<script language="JavaScript">

  function add() {
  	//pas de add ici
  }
  function upd() {
  	document.forms[0].elements('userAction').value="ij.prononces.petiteIJJointRevenu.modifier";
  	document.forms[0].elements('modifie').value="true";
  }

  function validate() {
    state = true;
    document.forms[0].elements('userAction').value="ij.prononces.petiteIJJointRevenu.modifier";
  
    return state;
  }

  function cancel() {
  	//pas de cancel ici
  }

  function del() {
    //pas de delete possible ici
  }
  
  	function init(){
  	
  	}
  	
  	function arret() {
		document.forms[0].elements('userAction').value = "ij.prononces.petiteIJJointRevenu.arreterEtape5";
  		document.forms[0].submit();
 	}
 	

	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PETITE_IJ"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<INPUT type="hidden" name="modifie" value="<%=viewBean.isModifie()%>">
								<B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B>
							</TD>
							<TD colspan="5"><INPUT type="text" value="<%=detailRequerant%>" size="100" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_SITUATION_ASSURE"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MODE_CALCUL"/></TD>
							<TD><ct:select name="csSituationAssure" wantBlank="true" defaultValue="<%=viewBean.getCsSituationAssure()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prestations.IIJPetiteIJCalculee.CS_GROUPE_MODE_CALCUL%>">										
									</ct:optionsCodesSystems>
								</ct:select></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_DERNIER_REVENU_DURANT_BLABLABLA_MANQUE_A_GAGNER"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT"/></TD>
							<TD><INPUT type="text" name="revenu" value="<%=viewBean.getRevenu()%>"></TD>
							<TD><ct:FWLabel key="JSP_PERIODICITE"/></TD>
							<TD><ct:select name="csPeriodiciteRevenu" defaultValue="<%=viewBean.getCsPeriodiciteRevenu()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE%>">										
									</ct:optionsCodesSystems>
								</ct:select></TD>
							<TD><ct:FWLabel key="JSP_HEURES_SEMAINE"/></TD>
							<TD><INPUT type="text" name="nbHeuresSemaine" value="<%=viewBean.getNbHeuresSemaine()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNEE_NIVEAU"/></TD>
							<TD><INPUT type="text" name="annee" value="<%=viewBean.getAnnee()%>"></TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:ifhasright element="ij.prononces.petiteIJJointRevenu.arreterEtape5" crud="u">
					<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PIJ_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PIJ_ARRET"/>">
				</ct:ifhasright>
				<ct:ifhasright element="ij.prononces.petiteIJJointRevenu.modifier" crud="u">
					<INPUT type="button" value="<ct:FWLabel key="JSP_CALCULER"/> (alt+<ct:FWLabel key="AK_PIJ_CALCULER"/>)" onclick="if(validate()) action(COMMIT);" accesskey="<ct:FWLabel key="AK_PIJ_CALCULER"/>">
				</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
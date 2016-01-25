<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.pavo.db.bta.CIDossierBtaViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
	CIDossierBtaViewBean viewBean = (CIDossierBtaViewBean) session.getAttribute("viewBean");
	if(!JadeStringUtil.isBlank(viewBean.getIdDossierBta())){
		selectedIdValue = viewBean.getIdDossierBta();
	}
	/*else{
		selectedIdValue = request.getParameter(("idDossierBta"));
		viewBean.setIdDossierBta(selectedIdValue);
	}*/
	
	//recharge le viewBean pour l'affichage
	if(JadeStringUtil.isBlank(viewBean.getIdTiersImpotent())){
		viewBean.recharge();
	}
	
	userActionValue = "pavo.bta.dossierBta.modifier";
	idEcran = "CCI0038";
	boolean rightAjout = objSession.hasRight("pavo.bta.requerantBta.afficher","ADD");
	
	/*out.print("viewBean : "+viewBean);
	out.print("<br>id dossier : "+viewBean.getIdDossierBta());
	out.print("<br>id impotent : "+viewBean.getIdTiersImpotent());
	out.print("<br>reception dossier : "+viewBean.getDateReceptionDossier());*/
%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	
	String fieldDisable = "class='disabled' readonly tabindex='-1'";
	//bButtonValidate = false;
	//bButtonUpdate = false;
	//bButtonCancel = false;
	
%>


<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pavo.db.bta.CIDossierBta"%><script>
	function del() {
	    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="pavo.bta.dossierBta.supprimer";
	        document.forms[0].submit();
	    }
	}
	function init(){
	}
	function postInit(){	
		<%if(rightAjout){%>
			document.forms[0].elements('btNewRequerant').disabled = false;
			document.forms[0].elements('btNewRequerant').readonly = false;
		<%}%>
	}
	
	function upd() {
	}
	
	function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.bta.dossierBta.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.bta.dossierBta.modifier";
    
    return state;

	}
	function cancel() {
	if (document.forms[0].elements('_method').value == "add")
  		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="pavo.bta.dossierBta.afficher";
	}

	function add() {
	}
</script>

<ct:menuChange displayId="options" menuId="dossierBta-detail" showTab="options">
	<ct:menuSetAllParams key="idDossierBta" value="<%=viewBean.getIdDossierBta()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossierBta()%>"/>

</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines BGS Dossiers<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						

			<%
			Object[] tiersMethodsName = new Object[]
			{
				new String[]{"setIdTiersImpotent","getIdTiers"}
			};
			Object[] tiersParams = new Object[]{
						new String[]{"selection","_pos"}
					};
			%>
			<!--
			-->
			<tr>
				<td>
					<font size="2"><b>Détail der Person, die die Pflege benötig</b></font>
				</td>
			</tr>
			<tr>
				<td>
					Auswahl der Person, die die Pflege benötig
				</td>
				<td>
            		<ct:FWSelectorTag 
					name="tiersSelector" 		
					methods="<%=tiersMethodsName%>"
					providerPrefix="TI"			
					providerApplication ="pyxis"			
					providerAction ="pyxis.tiers.tiers.chercher"			
					providerActionParams ="<%=tiersParams%>"
					target="fr_main"
					/>
					<%if(!JadeStringUtil.isBlank(viewBean.getIdTiersImpotent())){ %>
					<ct:ifhasright element="pyxis.tiers.tiers.afficher" crud="r">
						<a href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%= viewBean.getIdTiersImpotent() %>">Partner</a>
					</ct:ifhasright>	
					<%} %>
				</td>
			</tr>
						<tr>
							<td>
								SVN
							</td>
							<td>
								<input type="text" size="17" value="<%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getNumAvsActuel():new String("")%>" <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>
								Name und Vorname
							</td>
							<td>
								<input type="text" size="70" value="<%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getDesignation1()+" "+viewBean.getTiersViewBeanForDisplay().getDesignation2():new String("")%>" <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>
								Geburtsdatum
							</td>
							<td>
								<input type="text" size="10" value="<%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getDateNaissance():new String("")%>" <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>
								Adresse
							</td>
							<td>
								<TEXTAREA id="adresseTiers" cols="35" class="libelleLongDisabled" tabindex="-1" readonly="readonly" rows="5"><%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getAdresseAsString():new String("")%></TEXTAREA>
							</td>
						</tr>
						<tr>
                            <td nowrap colspan="7" height="11">
                               <hr size="2">
                            </td>
                        </tr>
						<tr>
							<td>
							<font size="2"><b>Detail über dem Dossier</b></font>
						</td>
						</tr>
						<tr>
							<td>
								Empfangsdatum des Dossiers
							</td>
							<td>
								<ct:FWCalendarTag name="dateReceptionDossier" value="<%=viewBean.getDateReceptionDossier()%>"/>
							</td>
							<td>
								Status des Dossiers
							</td>
							<td>
								<%--<ct:FWCodeSelectTag name="etatDossier" defaut="<%=!JadeStringUtil.isEmpty(viewBean.getEtatDossier())?viewBean.getEtatDossier():new String("vide")%>" codeType="CIETATBTA" wantBlank="false"/>--%>
								 <ct:FWListSelectTag name="etatDossier" defaut="<%=viewBean.getEtatDossier()%>" data="<%=viewBean.getListCodeSystemEtatDossier()%>"/>
							</td>
						</tr>
						<tr>
							<td>
								Abschlussdatum des Dossiers
							</td>
							<td>
								<ct:FWCalendarTag name="dateFinDossier" value="<%=viewBean.getDateFinDossier()%>"/>
							</td>
							<td>
								Abschlussgrund
							</td>
							<td>
								<ct:FWCodeSelectTag name="motifFin" defaut='<%=(!JadeStringUtil.isEmpty(viewBean.getMotifFin()))?viewBean.getMotifFin():""%>' codeType="CIFINBTA" wantBlank="true"/>
								<%--<input type="text" name="motifFin" size="17" value="<%=viewBean.getMotifFin()%>">--%>
							</td>
						</tr>
						<tr>
							<td>
								<br>
							</td>
						</tr>
						<%if(!JadeStringUtil.isBlank(viewBean.getIdDossierBta())){ %>
						<tr>
							<td>Liste der Antragsteller</td>
						</tr>
						<tr>
							<td><br></td>
						</tr>
						
						<tr><%if(JadeStringUtil.isBlank(selectedIdValue)){selectedIdValue="-1";}%>
							<td colspan=4>
								<IFRAME src="<%=request.getContextPath()%>/pavo?userAction=pavo.bta.requerantBta.lister&forIdDossierBta=<%=selectedIdValue %>" name="fr_list" scrolling="YES"  style="border : solid 1px black; width:100%;" height="200px"></IFRAME>
							</td>
						</tr>
						<tr>
							<td>
								<%String actionNewRequerant = request.getContextPath()+"/pavo?userAction=pavo.bta.requerantBta.afficher&_method=add&idDossierBta="+viewBean.getIdDossierBta(); %>
								<%if(rightAjout){%>
									<input name="btNewRequerant" class="btnCtrl" type="button"  value="Antragsteller erstellen" onclick="document.location.href='<%=actionNewRequerant%>'">
								<%}%>
							</td>
						</tr>
						<%} %>						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
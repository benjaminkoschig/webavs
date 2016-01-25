<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%
	idEcran = "GEN0002";
	globaz.leo.db.envoi.LEEnvoiViewBean viewBean = (globaz.leo.db.envoi.LEEnvoiViewBean)session.getAttribute("viewBean");
	boolean rightUpdateDate = objSession.hasRight("leo.envoi.envoi.updateDate","UPDATE");
%>

	

	<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<SCRIPT language="JavaScript">
function init(){};

function postInit(){
	<%if(!rightUpdateDate){%>
		document.getElementById('btnVal').style.visibility='hidden';
		document.getElementById('btnVal').style.display = 'none';
	<%}%>
}

function validate() {
	var exit = true;
	<%if((request.getParameter("action")!=null)&&(request.getParameter("action").equals(globaz.leo.constantes.ILEConstantes.ACTION_RECU))){%>
		document.forms[0].elements('userAction').value="leo.envoi.envoi.reception";
	<%}else{%>
		document.forms[0].elements('userAction').value="leo.envoi.envoi.updateDate";
	<%}%>
	return (exit);

}

function cancel() {
	document.forms[0].elements('userAction').value="leo.envoi.envoi.chercher";
}
function add(){}
</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%if((request.getParameter("action")!=null)&&(request.getParameter("action").equals(globaz.leo.constantes.ILEConstantes.ACTION_RECU))){%>
													Réception envoi<%
												}else{%>
													Détail de l'envoi<%
												}%>
												<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD colspan="3"><B>Info sur l'envoi</B></TD>
						</TR>
						<TR>
						<TD></TD>
						<TD>Type / Libelle</TD>
						<TD><INPUT type="text" name="typeQuestionnaire" value="<%=viewBean.getLibelleAffichage()%>" size="<%=viewBean.getLibelleAffichage().length()+3%>" class="disabled" readonly></TD>
						</TR>
						<TR>
						<TD></TD>
						<TD>Date de création</TD>
						<TD><INPUT type="text" name="dateEnvoi" value="<%=viewBean.getDate()%>" size="<%=viewBean.getDate().length()+3%>" class="disabled" readonly></TD>
						</TR>
						<%if((request.getParameter("action")!=null)&&(request.getParameter("action").equals(globaz.leo.constantes.ILEConstantes.ACTION_RECU))){%>
						<TR>
						<TD></TD>
						<TD>Date de Rappel</TD>
						<TD><INPUT type="text" name="dateRappel" value="<%=viewBean.getDateRappel()%>" size="<%=viewBean.getDateRappel().length()+3%>" class="disabled" readonly></TD>
						</TR>
						<TR>
						<TR>
						<TD></TD>
						<TD>Date de Réception</TD>
						<TD><ct:FWCalendarTag name="dateReception" value="<%=viewBean.getDateReception()%>" doClientValidation="CALENDAR" /></TD>
						</TR>
						<%}else{%>
						<TR>
						<TD></TD>
						<TD>Date de Réception</TD>
						<%if(viewBean.getDateReception().length()>0){%>
						<TD><ct:FWCalendarTag name="dateReception" value="<%=viewBean.getDateReception()%>" doClientValidation="CALENDAR" /></TD>
						<%}else{%>
						<TD><INPUT type="text" name="dateReception" value="<%=viewBean.getDateReception()%>" size="20" class="disabled" readonly></TD>
						<%}%>
						</TR>
						<TR>
						<TD></TD>
						<TD>Date de Rappel</TD>
						<%if(viewBean.getDateReception().length()>0){%>
						<TD><INPUT type="text" name="dateRappel" value="<%=viewBean.getDateRappel()%>" size="20" class="disabled" readonly></TD>
						<%}else{%>
						<TD><ct:FWCalendarTag name="dateRappel" value="<%=viewBean.getDateRappel()%>" doClientValidation="CALENDAR" /></TD>
						<%}%>
						</TR>
						<TR>
						<%}%>
						<TR>
							<TD colspan="3"><B>Liste provenance</B></TD>
						</TR>
						<%
							for(int i=0;i<viewBean.getRefProvList().size();i++){
								globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean prov = (globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean)viewBean.getRefProvList().getEntity(i);
						%>
							<TR>
							<TD></TD>
							<TD><%=viewBean.getSession().getCodeLibelle(prov.getTypeReferenceProvenance())%>
							<TD>
								<INPUT type="text" name="date" value="<%=prov.getIdCleReferenceProvenance()%>" size="<%=prov.getIdCleReferenceProvenance().length()+3%>" class="disabled" readonly>
							</TD>
						</TR>
						<%}%>
						<TR>
							<TD colspan="3"><B>Liste complément</B></TD>
						</TR>
						<%
							for(int i=0;i<viewBean.getCplJourn().size();i++){
								globaz.lupus.db.journalisation.LUComplementJournalViewBean cpl = (globaz.lupus.db.journalisation.LUComplementJournalViewBean)viewBean.getCplJourn().getEntity(i);
						%>
							<TR>
							<TD></TD>
							<TD><%=viewBean.getSession().getCodeLibelle(cpl.getCsTypeCodeSysteme())%></TD>
							<TD>
								<INPUT type="text" name="date" value="<%=viewBean.getSession().getCodeLibelle(cpl.getValeurCodeSysteme())%>" size="<%=viewBean.getSession().getCodeLibelle(cpl.getValeurCodeSysteme()).length()+3%>" class="disabled" readonly>
							</TD>
						</TR>
						<%}%>


						<TR>
							<TD colspan="3"><B>Tiers</B></TD>
						</TR>
						<TR>
						<TD></TD>
						<TD>Nom, Prénom</TD>
						<TD><INPUT type="text" name="nom" value="<%=viewBean.getNomTiers()%>" size="<%=viewBean.getNomTiers().length()+3%>" class="disabled" readonly></TD>
						</TR>
						<TR>
						<TD></TD>
						<TD>Rue</TD>
						<TD><INPUT type="text" name="rue" value="<%=(viewBean.getAdresseTiers()!=null)?viewBean.getAdresseTiers():""%>" size="<%=viewBean.getAdresseTiers().length()+3%>" class="disabled" readonly></TD>
						</TR>
						<TR>
						<TD></TD>
						<TD>Localité</TD>
						<TD><INPUT type="text" name="localite" value="<%=viewBean.getLocaliteTiers()%>" size="<%=viewBean.getLocaliteTiers().length()+3%>" class="disabled" readonly></TD>
						</TR>
						<TR>
						<TD><INPUT type="hidden" name="journalId" value="<%=viewBean.getIdJournalisation()%>"></TD>
						</TR>
						<TR>
						<%if(request.getParameter("forDate")!=null){%>
								<TD><INPUT type=hidden name="forDate" value="<%=request.getParameter("forDate")%>"></TD>
						<%}%>
						<%if(request.getParameter("forDateRappel")!=null){%>
								<TD><INPUT type=hidden name="forDateRappel" value="<%=request.getParameter("forDateRappel")%>"></TD>
						<%}%>
						<%if(request.getParameter("forDateReception")!=null){%>
								<TD><INPUT type=hidden name="forDateReception" value="<%=request.getParameter("forDateReception")%>"></TD>
						<%}%>
						<%if(request.getParameter("forLibelle")!=null){%>
								<TD><INPUT type=hidden name="forLibelle" value="<%=request.getParameter("forLibelle")%>"></TD>
						<%}%>
						<%if(request.getParameter("forUserName")!=null){%>
								<TD><INPUT type=hidden name="forUserName" value="<%=request.getParameter("forUserName")%>"></TD>
						<%}%>
						<%if(request.getParameter("forTypeDocument")!=null){%>
								<TD><INPUT type=hidden name="forTypeDocument" value="<%=request.getParameter("forTypeDocument")%>"></TD>
						<%}%>
						<%for(int i=0;i<globaz.leo.db.envoi.LEEnvoiListViewBean.NBRE_MAX_CRITERE_ENTREE;i++){%>
							<%if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(i+1)) != null){%>
							<TD>
								<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE+(i+1)))%>">
							</TD>
							<%}%>
							<%if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(i+1)) != null){%>
							<TD>
								<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VALEUR+(i+1)))%>">
							</TD>
							<%}%>
							<%if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(i+1)) != null){%>
							<TD>
								<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_VAL_INTER+(i+1)))%>">
							</TD>
							<%}%>
							<%if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(i+1)) != null){%>
							<TD>
								<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(i+1))%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.PARAM_RACINE_TYPE_INTER+(i+1)))%>">
							</TD>
							<%}%>
						<%}%>
						<%if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK) != null){%>
							<TD>
								<INPUT type="hidden" name="<%=java.net.URLDecoder.decode(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK)%>" value="<%=java.net.URLDecoder.decode(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK))%>">
							</TD>
							<%}%>
						</TR>
							<%--			<%
						  globaz.leo.db.envoi.LEChampUtilisateurListViewBean champsUtManager = new globaz.leo.db.envoi.LEChampUtilisateurListViewBean();
						  champsUtManager.setSession(viewBean.getSession());
						  champsUtManager.setForIdJournalisation(viewBean.getIdJournalisation());
						  champsUtManager.find();
						  if(champsUtManager.size()>0){
								 globaz.leo.db.envoi.LEChampUtilisateurViewBean champsUt = null;%>
								<TR>
									<TD colspan="3"><B>Saisie Annonce</B></TD>
								</TR>
								<%
								for(int i=0;i<champsUtManager.getSize();i++){
								%><TR><TD></TD><%
									champsUt = (globaz.leo.db.envoi.LEChampUtilisateurViewBean) champsUtManager.getEntity(i);
									if(champsUt.getCsGroupe().equals(globaz.leo.constantes.ILEConstantes.CS_GROUPE_CHOIX)){%>
										<TD></TD><TD><input type ="text" value="<%=viewBean.getSession().getLabel(champsUt.getValeur())%>" size="<%=viewBean.getSession().getLabel(champsUt.getValeur()).length()+3%>"  class="disabled" readonly></TD>
									<%}else if(champsUt.getCsGroupe().equals(globaz.leo.constantes.ILEConstantes.CS_GROUPE_DATE)){%>
										<TD><%=viewBean.getSession().getCodeLibelle(champsUt.getCsChamp())%></TD>
										<TD><input type ="text" value="<%=champsUt.getValeur()%>" size="<%=champsUt.getValeur().length()%>"  class="disabled" readonly></TD>
									<%}
								}%></TR><%
							}%>
							--%>



						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
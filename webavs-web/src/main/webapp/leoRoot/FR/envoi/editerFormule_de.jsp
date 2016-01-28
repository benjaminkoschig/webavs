<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.leo.db.envoi.LEEditerFormuleViewBean viewBean =(globaz.leo.db.envoi.LEEditerFormuleViewBean) session.getAttribute("viewBean");
	selectedIdValue=request.getParameter("selectedId");
	idEcran = "GEN3002";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
function init(){}
function add(){}
function cancel() {
	document.forms[0].elements('userAction').value="leo.envoi.envoi.chercher";
}
function validate() {
	var exit = true;
		document.forms[0].elements('userAction').value="leo.envoi.envoi.editerFormuleOk";	
	return (exit);
	
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Edition Formule<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="5">&nbsp;<TD>
							<TD colspan="3" align="left"><BR><B>Détail sur l'envoi</B><BR>&nbsp;</TD>
						</TR>
					
						<TR>
							<TD width="5">&nbsp;<TD>						
							<TD>Numéro</TD>
							<TD><INPUT type="text" value="<%=viewBean.getLibelle()%>" disabled="disabled" readonly="readonly" size="<%=viewBean.getLibelle().length()+3%>"></TD>	
						</TR>
						<TR>
							<TD width="5">&nbsp;<TD>							
							<TD>Type de formule</TD>
							<TD><INPUT type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsTypeJournal())%>" disabled="disabled" readonly="readonly" size="<%=viewBean.getSession().getCodeLibelle(viewBean.getCsTypeJournal()).length()+3%>"></TD>
						</TR>
						<TR>
							<TD width="5">&nbsp;<TD>
							<TD>Date de Rappel</TD>
							<TD><INPUT type="text" value="<%=viewBean.getDateRappel()%>" disabled="disabled" readonly="readonly" size="<%=viewBean.getDateRappel().length()+3%>"></TD>
						</TR>
						<TR>
							<TD width="5">&nbsp;<TD>							
							<TD>Destinataire</TD>
							<TD>
							<INPUT type="hidden" name="idChoixDestinataire" value="<%=viewBean.getIdChoixDestinataire()%>">
<%--							<INPUT type="text" value="<%=viewBean.getDestinataire()%>" disabled="disabled" readonly="readonly" size="<%=viewBean.getDestinataire().length()+3%>">--%>
							<TEXTAREA name="" rows="4" cols="40" disabled="disabled" readonly="readonly" ><%=viewBean.getDestinataire(viewBean.getLibelle())%></TEXTAREA>
							
							<%
								Object[] caisseMethods= new Object[]{
									new String[]{"setIdChoixDestinataire","getIdTiers"}
								};%>
								<ct:FWSelectorTag 
									name="caisseSelector" 
									
									methods="<%=caisseMethods%>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.administration.chercher"/> 
							</TD>	
						</TR>	
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<%	String dateName  = globaz.leo.constantes.ILEConstantes.VALUE+globaz.leo.process.handler.LEEditerFormuleHandler.PART_SEPARATOR+globaz.leo.constantes.ILEConstantes.CS_GROUPE_DATE+globaz.leo.process.handler.LEEditerFormuleHandler.PART_SEPARATOR;						
							//String checkName = globaz.leo.constantes.ILEConstantes.CS_GROUPE_CHOIX//globaz.leo.constantes.ILEConstantes.VALUE+"_"+globaz.leo.constantes.ILEConstantes.CS_GROUPE_CHOIX+"_"+globaz.leo.constantes.ILEConstantes.LABEL;
							globaz.leo.db.parametrage.LEGroupesListViewBean g =viewBean.getListeGroupes();
							for(int j=0;j<g.getSize();j++){
									globaz.leo.db.parametrage.LEGroupeViewBean groupe =(globaz.leo.db.parametrage.LEGroupeViewBean) g.getEntity(j);
									globaz.leo.db.parametrage.LEChampListViewBean l = viewBean.getChamps(groupe.getCsGroupe());
									%>
									<TR>
										<TD width="5">&nbsp;<TD>
										<TD colspan="2">
											<B><%=viewBean.getSession().getCodeLibelle(groupe.getCsGroupe())%></B>
											<BR>&nbsp;
										</TD>
										<TD>&nbsp;<TD>
									</TR>
									<%
									for(int i=0;i<l.size();i++){
										globaz.leo.db.parametrage.LEChampViewBean c = (globaz.leo.db.parametrage.LEChampViewBean) l.getEntity(i);
										if(c.getCsTypeChamp().equals(globaz.leo.constantes.ILEConstantes.CS_GROUPE_TYPE_CHOIX)) {
											String checkName = globaz.leo.constantes.ILEConstantes.VALUE+globaz.leo.process.handler.LEEditerFormuleHandler.PART_SEPARATOR+globaz.leo.constantes.ILEConstantes.CS_GROUPE_CHOIX+globaz.leo.process.handler.LEEditerFormuleHandler.PART_SEPARATOR+viewBean.getDomaine()+globaz.leo.process.handler.LEEditerFormuleHandler.PART_SEPARATOR+viewBean.getCategorie()+globaz.leo.process.handler.LEEditerFormuleHandler.PART_SEPARATOR+c.getCsChamp();
											if(c.getCsLabelAComplement().equals(globaz.leo.constantes.ILEConstantes.CS_OUI)){
												globaz.babel.api.ICTListeTextes listeChoix = viewBean.getListePosition(c.getCsChamp());
												if(listeChoix!=null){
													for (java.util.Iterator titresIter = listeChoix.iterator(); titresIter.hasNext();) {
														globaz.babel.api.ICTTexte crt = (globaz.babel.api.ICTTexte) titresIter.next();
														%>
													 	<TR>
													 		<TD width="5">&nbsp;<TD>
															<TD colspan="2">
																<INPUT type="checkbox" name="<%=checkName%>" value="<%=crt.getPosition()%>">
																<INPUT type="text" value="<%=crt.getDescription()%>" size="90" disabled="disabled" readonly="readonly">
															</TD>
													 		<TD>&nbsp;<TD>
													 	</TR>	 		
														<%
													}
												}else{
													%>
													 	<TR>
													 		<TD width="5">&nbsp;<TD>
													 		<TD colspan="2">Impossible de retrouver les textes dans babel</TD>
													 	</TR>	 		
													<%
												}
											}
											%>
										<%}else if(c.getCsTypeChamp().equals(globaz.leo.constantes.ILEConstantes.CS_GROUPE_TYPE_DATE)){%>
											<TR>
									 			<TD width="5">&nbsp;<TD>
												<TD><%=viewBean.getSession().getCodeLibelle(c.getCsChamp())%></TD>					
									 			<TD><ct:FWCalendarTag name="<%=dateName+c.getCsChamp()%>" value="" doClientValidation="CALENDAR"/></TD>	
												<TD>&nbsp;<TD>
											</TR>
										<%}%>
									<%}%>
								<TR>
								<TD colspan="4">&nbsp;</TD>
								</TR>
							<%}%>
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
							<%if(request.getParameter("typeProv"+(i+1)) != null){%>
							<TD>				
								<INPUT type="hidden" name="<%="typeProv"+(i+1)%>" value="<%=request.getParameter("typeProv"+(i+1))%>">
							</TD>
							<%}%>
							<%if(request.getParameter("valProv"+(i+1)) != null){%>
							<TD>				
								<INPUT type="hidden" name="<%="valProv"+(i+1)%>" value="<%=request.getParameter("valProv"+(i+1))%>">
							</TD>
							<%}%>
							<%if(request.getParameter("valInterProv"+(i+1)) != null){%>
							<TD>				
								<INPUT type="hidden" name="<%="valInterProv"+(i+1)%>" value="<%=request.getParameter("valInterProv"+(i+1))%>">
							</TD>
							<%}%>
							<%if(request.getParameter("typeInterProv"+(i+1)) != null){%>
							<TD>				
								<INPUT type="hidden" name="<%="typeInterProv"+(i+1)%>" value="<%=request.getParameter("typeInterProv"+(i+1))%>">
							</TD>
							<%}%>
						<%}%>	
						<%if(request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK) != null){%>
							<TD>				
								<INPUT type="hidden" name="<%=globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK%>" value="<%=request.getParameter(globaz.leo.db.envoi.LEEnvoiViewBean.SRC_GO_BACK)%>">
							</TD>
						<%}%>
								
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
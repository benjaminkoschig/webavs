<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
idEcran ="GTI0046";
%>
<%
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	actionNew  += "&idTiers=" + ((request.getParameter("idTiers")!=null)?request.getParameter("idTiers"):"") ;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
usrAction = "pyxis.adressepaiement.avoirPaiement.lister";

</SCRIPT>
<%
	globaz.pyxis.db.tiers.TITiersViewBean viewBean = (globaz.pyxis.db.tiers.TITiersViewBean  )session.getAttribute ("viewBean");							
%>



<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><span style="font-family:wingdings;font-weight:normal">*</span><span style="font-family:webdings;font-weight:normal">�</span> - Indirizzi di pagamento di un terzo<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<TR>
								 <TD nowrap width="128"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.afficher&idTiers=<%=viewBean.getIdTiers()%>">Terzi</A></TD>
								 <TD nowrap colspan="2">
										 <INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
								 </TD>
								<TD width="50"></TD>
								 <TD nowrap width="100" align="left">Affiliato</TD>
								 <TD nowrap >
										 <INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilieActuel()%>" class="libelleLongDisabled" readonly>
								 </TD>
						</TR>
						<TR>
								<TD nowrap width="128"></TD>
								<TD nowrap colspan="2">
										<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocaliteLong()%>" readonly>
							 <TD width="50"></TD>
							 <TD nowrap width="100" align="left">NSS</TD>
							 <TD nowrap>
										<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvsActuel()%>" class="libelleLongDisabled" readonly>
							</TD>
							<TD>
									 <INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>' >
									 <INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>' >

							</TD>
							</tr>
							<tr>
							<TD colspan="2"><input type="checkbox" name="inclureHistorique">inclure l'historique des modifications dans la recherche
							</TD>
							</tr>


	 				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>

	<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
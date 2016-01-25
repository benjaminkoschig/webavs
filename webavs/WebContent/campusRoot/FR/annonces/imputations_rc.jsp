<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CGE0007";
	actionNew  += "&idLot=" + request.getParameter("idLot")
	+ "&idAnnonceParent=" + request.getParameter("idAnnonceParent");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT>
	usrAction = "campus.annonces.imputations.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Recherche des imputations
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	     				<TR>
							<TD width="100">N° Lot</TD>
							<%if (JadeStringUtil.isBlankOrZero(request.getParameter("idLot"))) {%>
            				<TD width="200">
								<INPUT type="text" name="forIdLot" value="" size="17">
	     					</TD>
	     					<%} else { %>
            				<TD width="200">
								<INPUT type="text" name="forIdLot" readonly="readonly"  
								value="<%=request.getParameter("idLot")%>" size="17">
	     					</TD>
	     					<%}%>
            				<TD width="200">N° Immatriculation transmis</TD>
            				<%if (JadeStringUtil.isBlank(request.getParameter("numImmatriculationTransmis"))) {%>
	            				<TD width="200">
									<INPUT type="text" name="forNumImmatriculationTransmis" value="" size="15">
		     					</TD>
		     				<%} else { %>
		     				<TD width="200">
								<INPUT type="text" name="forNumImmatriculationTransmis" readonly="readonly"  
								value="<%=request.getParameter("numImmatriculationTransmis")%>" size="15">
	     					</TD>
		     				<%}%>
	     					<TD width="80">Nom</TD>
	     					<%if (JadeStringUtil.isBlank(request.getParameter("nom"))) {%>
            				<TD width="200">
								<INPUT type="text" name="forNomLike" value="" size="21">
	     					</TD>
	     					<%} else { %>
	     					<TD width="200">
								<INPUT type="text" name="forNomLike" readonly="readonly"
								value="<%=request.getParameter("nom")%>" size="21">
	     					</TD>
	     					<%}%>
          				</TR>
          				<TR>
	          				<TD>Etat annonce</TD>
	            			<TD>
								<ct:FWCodeSelectTag name="forCsEtatAnnonce"
								defaut="" codeType="GEETAT_ANN" wantBlank="true"/>
							</TD>
	            			<TD>NSS</TD>
	            			<%if (JadeStringUtil.isBlank(request.getParameter("numAvs"))) {%>
	            			<TD>
	            				<nss:nssPopup name="forNumAvs" cssclass="libelle"
		            			avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" />
		            		</TD>
		            		<%} else { %>
		            		<TD>
	            				<INPUT type="text" name="forNumAvs" readonly="readonly"  
								value="<%=request.getParameter("numAvs")%>" size="15">
		            		</TD>
		            		<%}%>
		            		<TD>Prénom</TD>
		            		<%if (JadeStringUtil.isBlank(request.getParameter("prenom"))) {%>
	           				<TD>
								<INPUT type="text" name="forPrenomLike" value="" size="21">
	     					</TD>
	     					<%} else { %>
	     					<TD>
								<INPUT type="text" name="forPrenomLike" readonly="readonly"
								value="<%=request.getParameter("prenom")%>" size="21">
	     					</TD>
	     					<%}%>
	     					<TD>
	     						<%if (!JadeStringUtil.isBlank(request.getParameter("idAnnonceParent"))) {%>
								<INPUT type="hidden" name="forIdAnnonceParent"
								value="<%=request.getParameter("idAnnonceParent")%>">
	     						<%}%>
	     						<INPUT type="hidden" name="forImputationsUniquement"
								value="<%=new Boolean (true)%>">
	     					</TD>
          				</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
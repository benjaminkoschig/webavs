<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_D"

	idEcran="PRE0039";
	String idCopie = "";	
	
globaz.corvus.vb.documents.RECopiesViewBean viewBean = (globaz.corvus.vb.documents.RECopiesViewBean)request.getAttribute("viewBean");
if (JadeStringUtil.isBlankOrZero(viewBean.getIdCopie())) {
	selectedIdValue = "";	
}
else {
	selectedIdValue = viewBean.getIdCopie();
	idCopie = viewBean.getIdCopie();
}

String dd="";
String df="";
String tiersReqInfo = "";

if (!JadeStringUtil.isBlankOrZero(viewBean.getIdTiersRequerant())) {
	tiersReqInfo = viewBean.getTiersRequerantInfo((globaz.globall.db.BSession)controller.getSession());
}

if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutCopie())) {
	dd = viewBean.getDateDebutCopie();
}

if (!JadeStringUtil.isBlankOrZero(viewBean.getDateFinCopie())) {
	df = viewBean.getDateFinCopie();
}




boolean hasRight = viewBean.getSession().hasRight("corvus.documents.copies.modifier", FWSecureConstants.UPDATE);
bButtonUpdate = hasRight;
bButtonDelete = hasRight;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="options"/>


<SCRIPT language="javascript">

	function add(){
	    document.forms[0].elements('userAction').value="corvus.documents.copies.ajouter";
	}

	function upd(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="corvus.documents.copies.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="corvus.documents.copies.modifier";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="corvus.documents.copies.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="corvus.documents.copies.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_COPIES_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>							
							<TD colspan="4">							
							<re:PRDisplayRequerantInfoTag
								session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
								idTiers="<%=viewBean.getIdTiersRequerant()%>"
								style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_ASSURE%>"/>
							</TD>
																				
						</TR>
						<TR><TD colspan="4"><br/><br/></TD></TR>
						
						<TR>							
							<TD><ct:FWLabel key="JSP_COPIES_NO"/></TD>
							<TD><INPUT type="text" class="disabled" name="idCopie" value="<%=idCopie%>"></TD>
							<TD></TD>
							<TD></TD>
							
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_COPIES_DATE_DEBUT"/></TD>							
							<TD>
								<input	id="dateDebutCopie"
										name="dateDebutCopie"
										data-g-calendar="type:default"
										value="<%=dd%>" />
							</TD>
							
							<TD><ct:FWLabel key="JSP_COPIES_DATE_FIN"/></TD>							
							<TD>
								<input	id="dateFinCopie"
										name="dateFinCopie"
										data-g-calendar="type:default"
										value="<%=df%>" />
							</TD>
						</TR>
						
						<tr>
							<td colspan="4">
								<br/>
							</td>
						</tr>	
						<tr>
							<td>
								<ct:FWLabel key="JSP_COPIES_REFERENCE"/>
							</td>
							<td colspan="3">
								<input	id="reference"
										name="reference"
										type="text"
										data-g-string="sizeMax:40,autoFit:true"
								<%	if(viewBean.getReference() != null) { %>
										value="<%=viewBean.getReference()%>"
								<%	} else {%>
										value=""
								<%	} %>
										 />
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<br/>
							</td>
						</tr>	
							
							
							<TR>							
									<TD><LABEL><ct:FWLabel key="JSP_ADMINISTRATION"/></LABEL></TD>
									<TD>
												<ct:FWSelectorTag
														name="selecteurAdminCopiesA"
														
														methods="<%=viewBean.getMethodesSelectionCopiesA()%>"
														providerApplication="pyxis"
														providerPrefix="TI"
														providerAction="pyxis.tiers.administration.chercher"
														target="fr_main"
														redirectUrl="<%=mainServletPath%>"/>
										</TD>
												<TD></TD>
									<TD></TD>				
							</TR>
							<TR>		
										<TD><LABEL><ct:FWLabel key="JSP_TIERS"/></LABEL></TD>										
										<TD><ct:FWSelectorTag
													name="selecteurTiersCopiesA"
													
													methods="<%=viewBean.getMethodesSelectionCopiesA()%>"
													providerApplication="pyxis"
													providerPrefix="TI"
													providerAction="pyxis.tiers.tiers.chercher"
													target="fr_main"
													redirectUrl="<%=mainServletPath%>"/>	
										</TD>
										<TD></TD>
										<TD></TD>
																																										
							</TR>

							<TR>
								<TD colspan="4"><br/><br/><br/></TD>
							</TR>						
						
							<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdTiersCopieA()))  {%>
							<TR>
								<TD><ct:FWLabel key="JSP_COPIES_A"/></TD>							
								<TD colspan="3">
									<span><strong>
										<%=viewBean.getTiersCopiesAInfo(objSession)%>
										</strong>
									</span>								
								</TD>
							</TR>		
							
							<TR>
								<TD colspan="4"><br/></TD>
							</TR>
							
							<TR>
								<TD></TD>							
								<TD colspan="3">
									<span>
										<%=viewBean.getTiersCopiesAAdresseAsStringHtml(objSession)%>										
									</span>								
								</TD>
							</TR>		
							
								
							<%} %>
										
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
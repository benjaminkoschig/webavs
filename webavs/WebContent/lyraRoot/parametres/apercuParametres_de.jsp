<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
idEcran="GLY0004";

globaz.lyra.vb.parametres.LYApercuParametresViewBean viewBean = (globaz.lyra.vb.parametres.LYApercuParametresViewBean) session.getAttribute("viewBean");

	bButtonCancel = false;	
	selectedIdValue = viewBean.getIdParametres();
	
	String[] mois = viewBean.getMoisList();
	String[] annees = viewBean.getAnneesList();
	String moisCourant = viewBean.getMoisCourant();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.ajouter"
  }

  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
  	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
  }
  
  function changerType() {
  		document.forms[0].elements("userAction").value = "<%=globaz.lyra.servlet.ILYActions.ACTION_PARAMETRES%>.afficher";
		document.forms[0].submit();
 }
 
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PARAMETRES_PARAMETRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
								<TD><INPUT type="hidden" name="idParametres" value="<%=viewBean.getIdParametres()%>">
									<INPUT type="hidden" name="idEcheances" value="<%=viewBean.getIdEcheances()%>">
								
								<ct:FWLabel key="JSP_TYPEPARAMETRE"/>
								</TD>
								<TD>&nbsp;</TD>
								<TD>
									<ct:select name="typeParametre" defaultValue="<%=viewBean.getTypeParametre()%>" wantBlank="true" onchange="changerType();">
										<ct:optionsCodesSystems csFamille="<%=globaz.lyra.api.ILYEcheances.CS_TYPE_PARAMETRE%>"/>
									</ct:select>
								</TD>
							</TR>
							<TR><TD colspan="3">&nbsp;<HR>&nbsp;</TD></TR>
						
						
						<%if(globaz.lyra.api.ILYEcheances.CS_TEXTE.equalsIgnoreCase(viewBean.getTypeParametre())){%>
						
						<TBODY id="isTypeText">
							<TR>
								<TD><LABEL for="nomParametre"><ct:FWLabel key="JSP_NOMPARAMETRE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomParametre" value="<%=viewBean.getNomParametre()%>" class="LYlibelleLongDisabled" readonly></TD>
							</TR>
							
							<TR>
								<TD><INPUT type="hidden" name="nomGroupeParametre" value="<%=viewBean.getNomGroupeParametre()%>">
								
								<LABEL for="defaultValue"><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="defaultValue" value="<%=viewBean.getDefaultValue()%>" class="libelleLong"></TD>	
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreDE"><ct:FWLabel key="JSP_LIBELLE_DE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreDE" value="<%=viewBean.getLibelleParametreDE()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreFR"><ct:FWLabel key="JSP_LIBELLE_FR"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreFR" value="<%=viewBean.getLibelleParametreFR()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreIT"><ct:FWLabel key="JSP_LIBELLE_IT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreIT" value="<%=viewBean.getLibelleParametreIT()%>" class="libelleLong"></TD>
							</TR>
							
						</TBODY>
						
						<%} else if(globaz.lyra.api.ILYEcheances.CS_EMAIL.equalsIgnoreCase(viewBean.getTypeParametre())){%>
						
						<TBODY id="isTypeText">
							<TR>
								<TD><LABEL for="nomParametre"><ct:FWLabel key="JSP_NOMPARAMETRE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomParametre" value="<%=viewBean.getNomParametre()%>" class="LYlibelleLongDisabled" readonly></TD>
							</TR>
							
							<TR>
								<TD><INPUT type="hidden" name="nomGroupeParametre" value="<%=viewBean.getNomGroupeParametre()%>">
								
								<LABEL for="defaultValue"><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="defaultValue" value="<%=viewBean.getDefaultValue()%>" class="libelleLong"></TD>	
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreDE"><ct:FWLabel key="JSP_LIBELLE_DE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreDE" value="<%=viewBean.getLibelleParametreDE()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreFR"><ct:FWLabel key="JSP_LIBELLE_FR"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreFR" value="<%=viewBean.getLibelleParametreFR()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreIT"><ct:FWLabel key="JSP_LIBELLE_IT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreIT" value="<%=viewBean.getLibelleParametreIT()%>" class="libelleLong"></TD>
							</TR>
							
						</TBODY>
						
						<%} else if(globaz.lyra.api.ILYEcheances.CS_LISTE.equalsIgnoreCase(viewBean.getTypeParametre())){%>
						
						<TBODY id="isTypeList">	
							<TR>
								<TD><LABEL for="nomParametre"><ct:FWLabel key="JSP_NOMPARAMETRE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomParametre" value="<%=viewBean.getNomParametre()%>" class="LYlibelleLongDisabled" readonly></TD>
							</TR>
							<TR>
								<TD><LABEL for="nomGroupeParametre"><ct:FWLabel key="JSP_NOMGROUPEPARAMETRES"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomGroupeParametre" value="<%=viewBean.getNomGroupeParametre()%>" class="libelleLong" onchange="changerType();"></TD>
							</TR>
							<TR>
								<TD><LABEL for="defaultValue"><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD>
									<%if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getNomGroupeParametre())){ %>
									<ct:select name="defaultValue" defaultValue="<%=viewBean.getDefaultValue()%>" wantBlank="true">
										<ct:optionsCodesSystems csFamille="<%=viewBean.getNomGroupeParametre()%>"/>
									</ct:select>
									<% } else {%>
									<ct:select name="defaultValue" defaultValue="<%=viewBean.getDefaultValue()%>" wantBlank="true">
										<ct:optionsCodesSystems csFamille="aucun"/>
									</ct:select>

									<% } %>
								</TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreDE"><ct:FWLabel key="JSP_LIBELLE_DE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreDE" value="<%=viewBean.getLibelleParametreDE()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreFR"><ct:FWLabel key="JSP_LIBELLE_FR"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreFR" value="<%=viewBean.getLibelleParametreFR()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreIT"><ct:FWLabel key="JSP_LIBELLE_IT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreIT" value="<%=viewBean.getLibelleParametreIT()%>" class="libelleLong"></TD>
							</TR>
							
						</TBODY>
						
						<%} else if(globaz.lyra.api.ILYEcheances.CS_CASEACOCHER.equalsIgnoreCase(viewBean.getTypeParametre())){%>
						
						<TBODY id="isCaseACocher">
							<TR>
								<TD><LABEL for="nomParametre"><ct:FWLabel key="JSP_NOMPARAMETRE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomParametre" value="<%=viewBean.getNomParametre()%>" class="LYlibelleLongDisabled" readonly></TD>
							</TR>
							<TR>
								<TD><INPUT type="hidden" name="nomGroupeParametre" value="<%=viewBean.getNomGroupeParametre()%>">
								
								<LABEL for="defaultValue"><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD>
									<INPUT type="checkbox" name="isCheckBoxChecked" <%=viewBean.getIsCheckBoxChecked().booleanValue()?"CHECKED":""%>>
								</TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreDE"><ct:FWLabel key="JSP_LIBELLE_DE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreDE" value="<%=viewBean.getLibelleParametreDE()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreFR"><ct:FWLabel key="JSP_LIBELLE_FR"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreFR" value="<%=viewBean.getLibelleParametreFR()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreIT"><ct:FWLabel key="JSP_LIBELLE_IT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreIT" value="<%=viewBean.getLibelleParametreIT()%>" class="libelleLong"></TD>
							</TR>
						</TBODY>
						<%} else if(globaz.lyra.api.ILYEcheances.CS_ANNEE.equalsIgnoreCase(viewBean.getTypeParametre())){%>
						
						<TBODY id="isTypeAnnee">
							<TR>
								<TD><LABEL for="nomParametre"><ct:FWLabel key="JSP_NOMPARAMETRE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomParametre" value="<%=viewBean.getNomParametre()%>" class="LYlibelleLongDisabled" readonly></TD>
							</TR>
							<TR>
								<TD><LABEL for="defaultValue"><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD>
									<ct:select name="defaultValue" defaultValue="<%=viewBean.getDefaultValue()%>" wantBlank="true">
										<%for (int i=0; i<annees.length; i=i+2){%>
										<OPTION value="<%=annees[i]%>" <%=annees[i].equalsIgnoreCase(viewBean.getDefaultValue())?"selected":""%>><%=annees[i+1]%></OPTION>
										<%}%>
									</ct:select>
								</TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreDE"><ct:FWLabel key="JSP_LIBELLE_DE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreDE" value="<%=viewBean.getLibelleParametreDE()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreFR"><ct:FWLabel key="JSP_LIBELLE_FR"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreFR" value="<%=viewBean.getLibelleParametreFR()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreIT"><ct:FWLabel key="JSP_LIBELLE_IT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreIT" value="<%=viewBean.getLibelleParametreIT()%>" class="libelleLong"></TD>
							</TR>
						</TBODY>
						
						<%} else if(globaz.lyra.api.ILYEcheances.CS_MOIS.equalsIgnoreCase(viewBean.getTypeParametre())){%>
						
						<TBODY id="isTypeMois">
							<TR>
								<TD><LABEL for="nomParametre"><ct:FWLabel key="JSP_NOMPARAMETRE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="nomParametre" value="<%=viewBean.getNomParametre()%>" class="LYlibelleLongDisabled" readonly></TD>
							</TR>
							<TR>
								<TD><LABEL for="defaultValue"><ct:FWLabel key="JSP_VALEURPARDEFAUT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD>
									<ct:select name="defaultValue" defaultValue="<%=viewBean.getDefaultValue()%>" wantBlank="true">
										<%for (int i=0; i<mois.length; i=i+2){%>
										<OPTION value="<%=mois[i]%>" <%=mois[i].equalsIgnoreCase(viewBean.getDefaultValue())?"selected":""%>><%=mois[i+1]%></OPTION>
										<%}%>
									</ct:select>
								</TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreDE"><ct:FWLabel key="JSP_LIBELLE_DE"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreDE" value="<%=viewBean.getLibelleParametreDE()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreFR"><ct:FWLabel key="JSP_LIBELLE_FR"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreFR" value="<%=viewBean.getLibelleParametreFR()%>" class="libelleLong"></TD>
							</TR>
							<TR>
								<TD><LABEL for="libelleParametreIT"><ct:FWLabel key="JSP_LIBELLE_IT"/></LABEL></TD>
								<TD>&nbsp;</TD>
								<TD><INPUT type="text" name="libelleParametreIT" value="<%=viewBean.getLibelleParametreIT()%>" class="libelleLong"></TD>
							</TR>
						</TBODY>
						
						<%}%>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
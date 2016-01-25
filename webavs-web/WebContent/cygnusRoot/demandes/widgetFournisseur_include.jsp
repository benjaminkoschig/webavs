<%@ page isELIgnored ="false" %>
						<div data-g-multiwidgets="languages:¦'A','T'¦,mandatory:true" class="multiWidgets" >
	                    	<input id="widget4-multiwidget"
	                    		   class="widgetAdmin" 
                    		   	   value="${param.descFournisseur}" 
                    			   data-g-autocomplete="manager:¦globaz.cygnus.db.decisions.RFAdministrationsManager¦,
                                                method:¦find¦,
                                                criterias:¦{
                                                    forCodeAdministrationLike: 'Code administration',
                                                    forDesignation1Like: 'Nom',
                                                    forDesignation2Like: 'Nom suite 1'
                                                }¦,
                                                lineFormatter:¦<b>#{designation1} #{designation2}</b> #{codeAdministration} #{cs(canton)}¦,
                                                modelReturnVariables:¦designation1,designation2,idTiers¦,
                                                functionReturn:¦
                                                    function(element){
                                                    $('#idFournisseurDemande').val($(element).attr('idTiers'));
                                                        this.value=$(element).attr('designation1')+' '+$(element).attr('designation2');
                                                    }
                                                ¦" type="text">
                       		<input id="widget5-multiwidget" 
                       			   class="widgetTiers" 
                       		   	   value="${param.descFournisseur}" 
                    			   data-g-autocomplete="manager:¦globaz.cygnus.db.decisions.RFTiersManager¦,
                                                method:¦find¦,
                                                criterias:¦{
                                                    likeNom: 'Nom',
                                                    likePrenom: 'Prénom',
                                                    likeNumeroNSS: 'Numéro AVS',
                                                    forDateNaissance: 'Date de naissance',
                                                    likeAlias: 'Alias'
                                                }¦,
                                                lineFormatter:¦<b>#{nom} #{prenom}</b> #{datenaissance} #{numero_nss} #{alias}¦,
                                                modelReturnVariables:¦nom,prenom,numero_nss,idTiers¦,
                                                functionReturn:¦
                                                    function(element){
                                                    $('#idFournisseurDemande').val($(element).attr('idTiers'));
                                                        this.value=$(element).attr('nom')+' '+$(element).attr('prenom');
                                                    }
                                                ¦" type="text">
                    	</div>
                    	<INPUT tabindex="1" type="hidden" name="descFournisseur" class="libelleExtraLongDisabled" value="descFournisseur"/>
                    	<ct:inputHidden name="designation1" id="designation1" />
						<ct:inputHidden name="designation2" id="designation2" />
						<ct:inputHidden name="codeAdministration" id="codeAdministration" />
						<ct:inputHidden name="canton" id="canton" />
						<ct:inputHidden name="codeAdministration" id="codeAdministration" />
						<ct:inputHidden name="canton" id="canton" />	
						<ct:inputHidden name="nom" id="nom" />
						<ct:inputHidden name="prenom" id="prenom" />
						<ct:inputHidden name="datenaissance" id="datenaissance" />
						<ct:inputHidden name="numero_nss" id="numero_nss" />
						<ct:inputHidden name="alias" id="alias" />
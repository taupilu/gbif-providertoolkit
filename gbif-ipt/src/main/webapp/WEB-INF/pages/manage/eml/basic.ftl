<#escape x as x?html>
<#include "/WEB-INF/pages/inc/header.ftl">
<title><@s.text name='manage.metadata.basic.title'/></title>
<script type="text/javascript">
	$(document).ready(function(){
		initHelp();			
		 	function getList(list){
				var arr=  list.split(",");
				var newlistaOccurrence={};
				for(index in arr ){
					var val=arr[index].replace(/{|}/g,'');
					var arr2=val.split('=');
					newlistaOccurrence[(arr2[0].trim())]=(arr2[1].trim());
				}
				return newlistaOccurrence;
			}
			
			var resourceType = "${resource.coreType!}";	
			if(resourceType==""){
				$("#type").attr('selectedIndex', '0');
				var subtype="${resource.subtype!}";
				if(subtype!=""){
					var opcion=getKey(getList("${occurrenceSubtypes}"),subtype);
					if(opcion!=""){
						$("#type").attr('selectedIndex', '2');
					}else{
						opcion=getKey(getList("${checklistSubtypes}"),subtype);
						if(opcion!=""){
							$("#type").attr('selectedIndex', '1');
						}else{
							$("#type").attr('selectedIndex', '3');
						}
					}
				}
			}else{
				if(resourceType.indexOf("Occurrence")!=-1){					
					$("#type").attr('disabled','disabled');	
				}else{					
					if (resourceType.indexOf("Taxon")!=-1){						
						$("#type").attr('disabled','disabled');	
					}
				}
			}	
			
			function getKey(map, variable){
				 var val="";
				$.each(map, function(key, value) {
					  if(variable==key.trim()){
						  val = value;
					  }
				});
				return val;
			}
			
			$("#type").change(function(){
				var optionType=$("#type").val();
				$("#resource\\.subtype").attr('selectedIndex', '0');
				switch(optionType)
		        {
		            case 'Occurrence':
		            	$('#resource\\.subtype >option').remove();
		            	var list=getList("${occurrenceSubtypes}");
		            	$.each(list, function(key, value) {
		    				$('#resource\\.subtype').append( new Option(value,key) );
		    			});          	
		            break;
		            case 'Checklist':
		            	$('#resource\\.subtype >option').remove();	
		            	var list=getList("${checklistSubtypes}");
		            	$.each(list, function(key, value) {
		    				$('#resource\\.subtype').append( new Option(value,key) );
		    			});
		            break;
		            default:
		            	$('#resource\\.subtype >option').remove();	            	
		            break;
		        }
			});
			
		$("#copyDetails").click(function(event) {
			event.preventDefault();
			$("#eml\\.resourceCreator\\.firstName").attr("value", $("#eml\\.contact\\.firstName").attr("value"));
			$("#eml\\.resourceCreator\\.lastName").attr("value", $("#eml\\.contact\\.lastName").attr("value"));
			$("#eml\\.resourceCreator\\.position").attr("value", $("#eml\\.contact\\.position").attr("value"));
			$("#eml\\.resourceCreator\\.organisation").attr("value", $("#eml\\.contact\\.organisation").attr("value"));
			$("#eml\\.resourceCreator\\.address\\.address").attr("value", $("#eml\\.contact\\.address\\.address").attr("value"));
			$("#eml\\.resourceCreator\\.address\\.city").attr("value", $("#eml\\.contact\\.address\\.city").attr("value"));
			$("#eml\\.resourceCreator\\.address\\.province").attr("value", $("#eml\\.contact\\.address\\.province").attr("value"));
			$("#eml\\.resourceCreator\\.address\\.postalCode").attr("value", $("#eml\\.contact\\.address\\.postalCode").attr("value"));
			$("#eml\\.resourceCreator\\.address\\.country").attr("value", $("#eml\\.contact\\.address\\.country").attr("value"));
			$("#eml\\.resourceCreator\\.phone").attr("value", $("#eml\\.contact\\.phone").attr("value"));
			$("#eml\\.resourceCreator\\.email").attr("value", $("#eml\\.contact\\.email").attr("value"));
			$("#eml\\.resourceCreator\\.homepage").attr("value", $("#eml\\.contact\\.homepage").attr("value"));		
		});
		$("#copyDetails2").click(function(event) {
			event.preventDefault();
			$("#eml\\.metadataProvider\\.firstName").attr("value", $("#eml\\.contact\\.firstName").attr("value"));
			$("#eml\\.metadataProvider\\.lastName").attr("value", $("#eml\\.contact\\.lastName").attr("value"));
			$("#eml\\.metadataProvider\\.position").attr("value", $("#eml\\.contact\\.position").attr("value"));
			$("#eml\\.metadataProvider\\.organisation").attr("value", $("#eml\\.contact\\.organisation").attr("value"));
			$("#eml\\.metadataProvider\\.address\\.address").attr("value", $("#eml\\.contact\\.address\\.address").attr("value"));
			$("#eml\\.metadataProvider\\.address\\.city").attr("value", $("#eml\\.contact\\.address\\.city").attr("value"));
			$("#eml\\.metadataProvider\\.address\\.province").attr("value", $("#eml\\.contact\\.address\\.province").attr("value"));
			$("#eml\\.metadataProvider\\.address\\.postalCode").attr("value", $("#eml\\.contact\\.address\\.postalCode").attr("value"));
			$("#eml\\.metadataProvider\\.address\\.country").attr("value", $("#eml\\.contact\\.address\\.country").attr("value"));
			$("#eml\\.metadataProvider\\.phone").attr("value", $("#eml\\.contact\\.phone").attr("value"));
			$("#eml\\.metadataProvider\\.email").attr("value", $("#eml\\.contact\\.email").attr("value"));
			$("#eml\\.metadataProvider\\.homepage").attr("value", $("#eml\\.contact\\.homepage").attr("value"));		
		});
	});
	
</script>
<#assign sideMenuEml=true />
 <#assign currentMenu="manage"/>
<#include "/WEB-INF/pages/inc/menu.ftl">
<h1><@s.text name='manage.metadata.basic.title'/>: <a href="resource.do?r=${resource.shortname}"><em>${resource.title!resource.shortname}</em></a> </h1>
<p><@s.text name='manage.metadata.basic.intro'/></p>
<#include "/WEB-INF/pages/macros/forms.ftl"/>
<form class="topForm" action="metadata-${section}.do" method="post">
	<b><@s.text name="manage.metadata.basic.required.message" /></b>
  	<@input name="eml.title" />
  	<@text name="eml.description"/>
  	<div class="halfcolumn">
	  	<@select name="eml.metadataLanguage" help="i18n" options=languages value="${metadataLanguageIso3!'eng'}" />
	</div>
	<div class="halfcolumn">
  	<@select name="eml.language" help="i18n" options=languages value="${languageIso3!'eng'}" />
	</div>

	<#assign coreType="${resource.coreType!}" />
	<div class="halfcolumn">
	<label for="type"><@s.text name="resource.type"/></label>	
	<div class="infos">	  
	<img class="infoImg" src="${baseURL}/images/info.gif" />
	<div class="info">		
	<span class="idSuffix">
		<@s.text name='resource.type.help'/>            	
	</span>         		
	</div>		
	
	<select name="type" id="type">
		<#list types?keys as type>
		<#if coreType?contains("Taxon") && ("${types[type]}")=="Checklist"> 
			<option value="${types[type]}" selected="selected">${type}</option>
		<#elseif coreType?contains("Occurrence") && ("${types[type]}")=="Occurrence">
			<option value="${types[type]}" selected="selected">${type}</option>
		<#else>
			<option value="${types[type]}">${type}</option>
		</#if>
		</#list>
	</select>
	</div>	
	</div>	
	<div class="halfcolumn" id="selectSubtypeDiv">	
	<#if coreType?contains("Taxon") >
		<@select name="resource.subtype" help="i18n" options=checklistSubtypes value="${resource.subtype!''}" />
	<#elseif coreType?contains("Occurrence")>	
		<@select name="resource.subtype" help="i18n" options=occurrenceSubtypes value="${resource.subtype!''}" />
	<#else>
		<#assign subtype="${resource.subtype!}" />
		<#assign listChecklist=checklistSubtypes?keys />
		<#assign listOccurrence=occurrenceSubtypes?keys />
		<#if subtype?has_content >	
			<#if listOccurrence?seq_contains(subtype)>
				<@select name="resource.subtype" help="i18n" options=occurrenceSubtypes value="${resource.subtype!''}" />	
			<#elseif listChecklist?seq_contains(subtype)>
				<@select name="resource.subtype" help="i18n" options=checklistSubtypes value="${resource.subtype!''}" />
			<#else>
				<@select name="resource.subtype" help="i18n" options={} />
			</#if>
		<#else>
			<@select name="resource.subtype" help="i18n" options={} />
		</#if>
	</#if>		
  	</div>  
	
  	<div class="newline"></div>
  	<div class="horizontal_dotted_line_large_foo" id="separator"></div>
  	
  	<!-- Resource Contact -->   	
  	<h2><@s.text name="eml.contact"/></h2>
	<div class="halfcolumn">
	  	<@input name="eml.contact.firstName" />
	</div>
  	<div class="halfcolumn">	  	
	  	<@input name="eml.contact.lastName" />
  	</div>
  	<div class="newline" ></div>
  	<div class="halfcolumn">
  		<@input name="eml.contact.position" />
	</div>
  	<div class="halfcolumn">			
  		<@input name="eml.contact.organisation" />
  	</div>
  	<div class="newline" ></div>
  	<div class="halfcolumn">
  		<@input name="eml.contact.address.address" />
	</div>
  	<div class="halfcolumn">			  		
  		<@input name="eml.contact.address.city" />
  	</div>
  	<div class="newline" ></div>
  	<div class="halfcolumn">
  		<@input name="eml.contact.address.province" />
	</div>
  	<div class="halfcolumn">		  		
  		<@select name="eml.contact.address.country" help="i18n" options=countries value="${eml.contact.address.country!}"/>
   	</div>
  	<div class="newline" ></div>   	
  	<div class="halfcolumn">
  		<@input name="eml.contact.address.postalCode" />
	</div>
  	<div class="halfcolumn">	  		
  		<@input name="eml.contact.phone" />
  	</div>
	<div class="halfcolumn">
	  	<@input name="eml.contact.email" />
	</div>
  	<div class="halfcolumn">		  	
	  	<@input name="eml.contact.homepage" />
  	</div> 	
	<div class="newline"></div>
  	<div class="horizontal_dotted_line_large_foo" id="separator"></div>  	
	<div class="newline"></div>
	<div class="newline"></div>
	
  	<!-- Resource Creator -->
  	<div class="right">
  		<a id="copyDetails" href="">[ <@s.text name="eml.resourceCreator.copyLink" />  ]</a>
  	</div>
  	<h2><@s.text name="eml.resourceCreator"/></h2>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.firstName" />
	</div>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.lastName" />
	</div>
	<div class="newline"></div>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.position" />
	</div>
  	<div class="halfcolumn">		
		<@input name="eml.resourceCreator.organisation" />
	</div>
	<div class="newline"></div>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.address.address" />
	</div>
  	<div class="halfcolumn">		
		<@input name="eml.resourceCreator.address.city" />
	</div>
	<div class="newline"></div>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.address.province" />
	</div>
  	<div class="halfcolumn">		
		<@select name="eml.resourceCreator.address.country" help="i18n" options=countries value="${eml.getResourceCreator().address.country!}"/>
	</div>		
	<div class="newline"></div>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.address.postalCode" />
	</div>
  	<div class="halfcolumn">		
		<@input name="eml.resourceCreator.phone" />
	</div>
	<div class="newline"></div>
  	<div class="halfcolumn">
		<@input name="eml.resourceCreator.email" />
	</div>
  	<div class="halfcolumn">		
		<@input name="eml.resourceCreator.homepage" />
	</div>
	<div class="newline"></div>	
  	<div class="horizontal_dotted_line_large_foo" id="separator"></div>  	
  	<div class="newline"></div>
	<div class="newline"></div>
	
  	<!-- Metadata Provider -->
  	<div class="right">
  		<a id="copyDetails2" href="">[ <@s.text name="eml.resourceCreator.copyLink" />  ]</a>
  	</div>
	<h2><@s.text name="eml.metadataProvider"/></h2>
	
  	<div class="halfcolumn">
		<@input name="eml.metadataProvider.firstName" />
	</div>
  	<div class="halfcolumn">				
		<@input name="eml.metadataProvider.lastName" />
	</div>
	<div class="newline"></div>	
	<div class="halfcolumn">
		<@input name="eml.metadataProvider.position" />
	</div>
  	<div class="halfcolumn">				
		<@input name="eml.metadataProvider.organisation" />
	</div>
	<div class="newline"></div>	
	<div class="halfcolumn">
		<@input name="eml.metadataProvider.address.address" />
	</div>
  	<div class="halfcolumn">				
		<@input name="eml.metadataProvider.address.city" />
	</div>
	<div class="newline"></div>	
	<div class="halfcolumn">
		<@input name="eml.metadataProvider.address.province" />
	</div>
  	<div class="halfcolumn">			
		<@select name="eml.metadataProvider.address.country" help="i18n" options=countries value="${eml.metadataProvider.address.country!}"/>
	</div>		
	<div class="newline"></div>	
	<div class="halfcolumn">
		<@input name="eml.metadataProvider.address.postalCode" />
	</div>
  	<div class="halfcolumn">				
		<@input name="eml.metadataProvider.phone" />
	</div>
	<div class="newline"></div>	
	<div class="halfcolumn">
		<@input name="eml.metadataProvider.email" />
	</div>
  	<div class="halfcolumn">				
		<@input name="eml.metadataProvider.homepage" />
	</div>
	<div class="newline"></div>	
	<div class="buttons">
 		<@s.submit cssClass="button" name="save" key="button.save"/>
 		<@s.submit cssClass="button" name="cancel" key="button.cancel"/>
	</div>
	<!-- internal parameter -->
	<input name="r" type="hidden" value="${resource.shortname}" />  
</form>
<#include "/WEB-INF/pages/inc/footer.ftl">
</#escape>
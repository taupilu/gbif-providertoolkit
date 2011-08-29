<#include "/WEB-INF/pages/inc/header.ftl">
	<title><@s.text name="manage.overview.title"/>: ${resource.title!resource.shortname}</title>
	<script type="text/javascript" src="${baseURL}/js/jconfirmaction.jquery.js"></script>
	<style>
	img.info{
		position: relative;
		top: 4px;
	}
	.actions select, .actions input[type="file"]{
		width: 200px;
	}
	div.definition div.title{
		width: 30%;
	}
	div.title input[type="submit"], div.title button{
		float: right;
		margin-right: 5px;
	}
	div.definition div.body{
		width: 68%;
	}
	span.rightHead{
		float:right;
		margin-right: 10px;
		font-style: italic;
	}
	.report{
		background-color: #f3f3f3;
		color: #111;
		padding: 10px;
	
	}
	</style>
<script type="text/javascript">
$(document).ready(function(){
	$('.confirm').jConfirmAction({question : "<@s.text name="basic.confirm"/>", yesAnswer : "<@s.text name="basic.yes"/>", cancelAnswer : "<@s.text name="basic.no"/>"});
	var showReport=false;
	$("#toggleReport").click(function() {
		if(showReport){
			showReport=false;
			$("#toggleReport").text("Show Report");
			$('#dwcaReport').hide();
		}else{
			showReport=true;
			$("#toggleReport").text("Hide Report");
			$('#dwcaReport').show();
		}
	});
	//Hack needed for Internet Explorer X.*x
	$('.edit').each(function() {
		$(this).click(function() {
			window.location = $(this).parent('a').attr('href');
		});
	});	
});
</script>

 <#assign currentMenu = "manage"/>
<#include "/WEB-INF/pages/inc/menu.ftl">
<#include "/WEB-INF/pages/macros/forms.ftl"/>

<h1>${resource.title!resource.shortname}</h1>
<p>
<@s.text name="manage.overview.intro"><@s.param>${resource.shortname}</@s.param></@s.text>
</p>


<div class="definition" id="metadata">	
  <div class="title">
  	<div class="head">
        <@s.text name='manage.overview.metadata'/>
  	</div>
  	<div class="actions">
	  <form action='metadata-basic.do' method='get'>
	    <input name="r" type="hidden" value="${resource.shortname}" />
       	<@s.submit name="edit" key="button.edit"/>
  	  </form>
  	</div>
  	<#if missingMetadata>
  	<div class="warn">
		<@s.text name='manage.overview.missing.metadata'/>
  	</div>
  	</#if>
  </div>
  <div class="body">
      	<div>
      		<#assign no_description><@s.text name='manage.overview.no.description'/></#assign>
      		${resource.description!no_description}
      	</div>
      	<div class="details">
      		<table>
          		<tr><th><@s.text name='manage.overview.keywords'/></th><td>${resource.eml.subject!}</td></tr>
          		<tr><th><@s.text name='manage.overview.taxcoverage'/></th><td><#list resource.eml.taxonomicCoverages as tc><#list tc.taxonKeywords as k>${k.scientificName!k.commonName!}<#if !k_has_next>; </#if></#list></#list></td></tr>
          		<tr><th><@s.text name='manage.overview.geocoverage'/></th><td><#list resource.eml.geospatialCoverages as geo><#list geo.keywords as k>${k!}<#if !k_has_next>;</#if> </#list></#list></td></tr>
      		</table>
      	</div>
  </div>
</div>

<div class="definition" id="sources">	
  <div class="title">
  	<div class="head">
        <@s.text name='manage.overview.source.data'/>
  	</div>
  	<div class="actions">
	  <form action='addsource.do' method='post' enctype="multipart/form-data">
	    <input name="r" type="hidden" value="${resource.shortname}" />
	    <input name="validate" type="hidden" value="false" />
	    <@s.file name="file" key="manage.resource.create.file" />
	    <br/>
       	<@s.submit name="add" key="button.add"/>
  	  </form>
  	</div>
  </div>
  <div class="body">
      	<div>
      		<@s.text name='manage.overview.source.description1'/> <@s.text name='manage.overview.source.description2'/> <@s.text name="button.add"/><@s.text name='manage.overview.source.description3'/> <@s.text name="button.add"/>. 
      	</div>
      	<div class="details">
      		<table>
      		  <#list resource.sources as src>
      		  	<tr><th>
      			<#if src.rows?exists>
          		 ${src.name} <@s.text name='manage.overview.source.file'/></th><td>${src.fileSizeFormatted}, ${src.rows} <@s.text name='manage.overview.source.rows'/>, ${src.columns} <@s.text name='manage.overview.source.columns'/>. ${(src.lastModified?datetime?string)!}
          		<#else>
          		 ${src.name} <@s.text name='manage.overview.source.sql'/></th><td>db=${src.database!"..."}, ${src.columns} <@s.text name='manage.overview.source.columns'/>. 
          		</#if>
          		<#if !src.readable><img src="${baseURL}/images/warning.gif" /></#if> 
          		<a href="source.do?r=${resource.shortname}&id=${src.name}"><button class="edit"><@s.text name='button.edit'/></button></a>
          		</td></tr>
          	  </#list>
      		</table>
      	</div>
  </div>
</div>

<div class="definition" id="mappings">	
  <div class="title">
  	<div class="head">
        <@s.text name='manage.overview.DwC.Mappings'/>
  	</div>
  	<div class="actions">
  	  <#if (potentialExtensions?size>0)>
	  <form action='mapping.do' method='post'>
	    <input name="r" type="hidden" value="${resource.shortname}" />
	    <select name="id" id="rowType" size="1">
	    <#list potentialExtensions as e>
	      <option value="${e.rowType}">${e.title}</option>
	    </#list>
		</select>
		<input type='submit' name='add' value='Add' />
  	  </form>
  	  <#else>
  	  	<#if (resource.sources?size>0) && !resource.hasCore()>
		  	<div class="warn">
				<@s.text name='manage.overview.no.DwC.extensions'/>
		  	</div>
  	  	</#if>
  	  </#if>
  	</div>
  </div>
  <div class="body">
      	<div>
      		<@s.text name='manage.overview.DwC.Mappings.description'/>
      	</div>
      	<div class="details">
      		<table>
	      		  <#list resource.coreMappings as m>
	          		<tr><th><#if m_index==0>${m.extension.title}</#if></th><td>${m.fields?size} <@s.text name='manage.overview.DwC.Mappings.terms'/> ${(m.source.name)!} 
	          		<a href="mapping.do?r=${resource.shortname}&id=${m.extension.rowType}&mid=${m_index}"><button class="edit"><@s.text name='button.edit'/></button></a>
	          		</td></tr>
	          	  </#list>
      		  <#list resource.getMappedExtensions() as ext>
      		  	<#if !ext.isCore()>
	      		  <#list resource.getMappings(ext.rowType) as m>
	          		<tr><th><#if m_index==0>${ext.title}</#if></th><td>${m.fields?size} <@s.text name='manage.overview.DwC.Mappings.terms'/> ${(m.source.name)!} 
	          		<a href="mapping.do?r=${resource.shortname}&id=${ext.rowType}&mid=${m_index}"><button class="edit"><@s.text name='button.edit'/></button></a>
	          		</td></tr>
	          	  </#list>
	          	</#if>
          	  </#list>
      		</table>
      	</div>
  </div>
</div>

<div class="definition" id="visibility">	
  <div class="title">
  	<div class="head">
        <@s.text name='manage.overview.visibility'/>
        <em class="<#if resource.status=="PRIVATE">RED<#else>green</#if>"><@s.text name="resource.status.${resource.status?lower_case}"/></em>
  	</div>
  	<div class="actions">
	  <form action='resource-visibility.do' method='post'>
	    <input name="r" type="hidden" value="${resource.shortname}" />
	    <#if resource.status=="PUBLIC">
	    	<#if currentUser.hasRegistrationRights() && (organisations?size>0)>
		    <select name="id" id="org" size="1">
		    <#list organisations as o>
		      <option value="${o.key}">${o.alias!o.name}</option>
		    </#list>
			</select>
	       	<@s.submit cssClass="confirm" name="publish" key="button.register" disabled="${missingRegistrationMetadata?string}"/>
	       	<#if missingRegistrationMetadata>
	       		<div class="warn"><@s.text name="manage.overview.visibility.missing.metadata"><@s.param>${baseURL}/manage/metadata-basic.do?r=${resource.shortname}</@s.param></@s.text></div>
	       	</#if>
	       	</#if>
	       	<@s.submit cssClass="confirm" name="unpublish" key="button.private" />
		<#else>
		    <#if resource.status=="PRIVATE">
	       	<@s.submit name="publish" key="button.public"/>
			</#if>
		    <#if resource.status=="REGISTERED">
	       	<@s.submit name="update" key="button.update"/>
			</#if>			
		</#if>
  	  </form>
  	</div>
  </div>
  <div class="body">
      	<div>
			<@s.text name="manage.resource.status.intro.${resource.status?lower_case}"/> 
		    <#if resource.status=="PUBLIC">
			  <#if currentUser.hasRegistrationRights()>
				<@s.text name="manage.resource.status.registration.intro"/>
			  <#else>
			    <div>
		          	<img class="info" src="${baseURL}/images/info.gif" /> 
					<em><@s.text name="manage.resource.status.registration.forbidden"/></em>
	      		</div>
			  </#if>
		      <#if missingRegistrationMetadata>
			    <div>
		          	<img class="info" src="${baseURL}/images/info.gif" /> 
					<em><@s.text name="manage.overview.visibility.missing.registration.metadata"/></em>
	      		</div>
		      </#if>
		      <#if organisations?size==0>
			    <div>
		          	<img class="info" src="${baseURL}/images/info.gif" /> 
					<em><@s.text name="manage.overview.visibility.no.organisations"/></em>
	      		</div>
			  </#if>
		    </#if>
      	</div>
      	<div class="details">
      		<table>
		      	<#if resource.status=="REGISTERED" && resource.key??>
	          		<tr><th><@s.text name="manage.overview.visibility.resource.key"/></th><td>${resource.key} <a href="${cfg.registryUrl}/browse/agent?uuid=${resource.key}">GBRDS</a></td></tr>
	          		<#if resource.organisation?exists>
	          		<tr><th><@s.text name="manage.overview.visibility.organisation"/></th><td>${resource.organisation.name!}</td></tr>
	          		<tr><th><@s.text name="manage.overview.visibility.organisation.contact"/></th><td>${resource.organisation.primaryContactName!}, ${resource.organisation.primaryContactEmail!}</td></tr>
	          		<tr><th><@s.text name="manage.overview.visibility.organisation.key"/></th><td>${resource.organisation.key!}</td></tr>
	          		<tr><th><@s.text name="manage.overview.visibility.endorsing.node"/></th><td>${resource.organisation.nodeName!}</td></tr>
	          		</#if>
          		</#if>
      		</table>
      	</div>
  </div>
</div>

<div class="definition" id="publish">	
  <div class="title">
  	<div class="head">
        <@s.text name="manage.overview.published"/>
  	</div>
   	<div class="actions">
   	  <#if !missingMetadata>
	  <form action='publish.do' method='post'>
	    <input name="r" type="hidden" value="${resource.shortname}" />
	    <@s.submit name="publish" key="button.publish" />
  	  </form>
  	  </#if>
  	</div>
  </div>
  <div class="body">
      	<div>
      		<@s.text name="manage.overview.published.description"/>
      	</div>
   	  <#if missingMetadata>
      	<div>
          	<img class="info"src="${baseURL}/images/info.gif" /> 
			<em><@s.text name="manage.overview.published.missing.metadata"/></em>
      	</div>
  	  </#if>
      	<div class="details">
      		<table>
			  	<#if resource.lastPublished??>
          		 <tr><th><@s.text name="manage.overview.published.last.publication"/></th>
          		 	<td><@s.text name="manage.overview.published.version"/> ${resource.eml.emlVersion} <@s.text name="manage.overview.published.from"/> ${resource.lastPublished?date?string.medium}
				  	<#if report??><a id="toggleReport" href="#">See report</a></#if>
          		    </td>
          		 </tr>
			  	 <#if report??>
          		 <tr id="dwcaReport" style="display: none;"><td colspan="2">
<div class="report">
  <ul class="simple">
   <#list report.messages as msg>
   <li>${msg.message} <span class="small">${msg.date?time?string}</span></li>
   </#list>
  </ul>
  <#if report.hasException()>
  <br/>
  <ul class="simple">
   <li><strong>Exception</strong> ${report.exceptionMessage!}</li>
   <#list report.exceptionStacktrace as msg>
   <li>${msg}</li>
   </#list>
  </ul>
  </#if>
</div>
          		 </td></tr>
				 </#if>          		 
			  	 <#if (resource.recordsPublished>0)>
          		  <tr><th><@s.text name="manage.overview.published.archive"/></th><td><a href="${baseURL}/archive.do?r=${resource.shortname}"><@s.text name="manage.overview.published.download"/></a>, ${resource.recordsPublished} <@s.text name="manage.overview.published.records"/> </td></tr>
			  	 </#if>
          		 <tr><th><@s.text name="manage.overview.published.eml"/></th><td><a href="${baseURL}/eml.do?r=${resource.shortname}"><@s.text name="manage.overview.published.download"/></a> <a href="${baseURL}/resource.do?r=${resource.shortname}"><@s.text name="manage.overview.published.view"/></a></td></tr>
			  	</#if>
      		</table>
      	</div>
  </div>
</div>

<div class="definition" id="managers">	
  <div class="title">
  	<div class="head">
        <@s.text name="manage.overview.resource.managers"/>
  	</div>
  	<div class="actions">
  	  <#if (potentialManagers?size>0)>
	  <form action='resource-addmanager.do' method='post'>
	    <input name="r" type="hidden" value="${resource.shortname}" />
	    <select name="id" id="manager" size="1">
	      <option value=""></option>
	    <#list potentialManagers as u>
	      <option value="${u.email}">${u.name}</option>
	    </#list>
		</select>
		<input type='submit' name='add' value='Add' />
  	  </form>
  	  </#if>
  	</div>
  </div>
  <div class="body">
      	<div>
      		<@s.text name="manage.overview.resource.managers.description"><@s.param>${resource.shortname}</@s.param></@s.text>
		</div>
      	<div class="details">
      		<table>
          		<tr><th><@s.text name="manage.overview.resource.managers.creator"/></th><td>${resource.creator.name}, ${resource.creator.email}</td></tr>
          		<#list resource.managers as u>
          		${u}
          		<tr><th><@s.text name="manage.overview.resource.managers.manager"/></th><td>${u.name}, ${u.email} <a class="confirm" href="resource-delmanager.do?r=${resource.shortname}&id=${u.email}"><button><@s.text name="button.delete"/></button></a></td></tr>
	    		</#list>
      		</table>
      	</div>
  </div>
</div>

<div>
  <form action='resource-delete.do' method='post'>
    <input name="r" type="hidden" value="${resource.shortname}" />
   	<@s.submit cssClass="confirm" name="delete" key="button.delete"/>
  </form>
</div>

<#include "/WEB-INF/pages/inc/footer.ftl">
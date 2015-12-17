<#-- ********************************************* -->
<#-- Displays the topic folder image by its status -->
<#-- ********************************************* -->
<#macro folderImage topic>
	<#if topic.read>
		<#if topic.status == STATUS_UNLOCKED>
			
			<#assign datesChecked = false/>
			<#if (topic.accessDates?? && topic.accessDates.openDate??) && (topic.accessDates.openDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open.gif" width="19" height="18" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/-->
				<#if (topic.accessDates.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open.png" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/>
				</#if>
				<#assign datesChecked = true/>
			<#elseif (topic.accessDates?? && topic.accessDates.allowUntilDate??)>
				<#if (topic.accessDates.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (topic.accessDates?? && topic.accessDates.dueDate??) && (topic.accessDates.dueDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce.gif" width="19" height="18" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce.png" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_sticky.gif" width="19" height="18" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky.png" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_task.gif" width="19" height="18" alt="task topic icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="re-use topic icon" title="${I18n.getMessage("reuse-topic-read-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="priority topic icon" title="${I18n.getMessage("reuse-topic-priority-read-title")}"/>
					</#if>
				<#else>
					<#--if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_hot.gif" width="19" height="18" alt="topic hot icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder.gif" width="19" height="18" alt="topic icon" title="${I18n.getMessage("message-title")}">
					</#if-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal.png" alt="topic icon" title="${I18n.getMessage("message-title")}"/>
				</#if>
			</#if>
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock.png" alt="topic locked icon" title="${I18n.getMessage("locked-forum-topic-read-title")}"/>
		</#if>
	<#else>
		<#if topic.status == STATUS_UNLOCKED>
			
			<#assign datesChecked = false/>
			<#if (topic.accessDates?? && topic.accessDates.openDate??) && (topic.accessDates.openDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open_new.gif" width="19" height="18" alt="not yet open new icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/-->
				<#if (topic.accessDates.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open_new.png" alt="not yet open - unread message icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/>
				</#if>				
				<#assign datesChecked = true/>
			<#elseif (topic.accessDates?? && topic.accessDates.allowUntilDate??)>
				<#if (topic.accessDates.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (topic.accessDates?? && topic.accessDates.dueDate??) && (topic.accessDates.dueDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce_new.gif" width="19" height="18" alt="announce topic new icon" title="${I18n.getMessage("announce-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce_new.png" alt="announce topic unread message icon" title="${I18n.getMessage("announce-topic-unread-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_sticky_new.gif" width="19" height="18" alt="sticky topic new icon" title="${I18n.getMessage("sticky-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky_new.png" alt="sticky topic unread message icon" title="${I18n.getMessage("sticky-topic-unread-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_new_task.gif" width="19" height="18" alt="task topic new icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="re-use topic unread message icon" title="${I18n.getMessage("reuse-topic-unread-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="priority topic unread message icon" title="${I18n.getMessage("reuse-topic-priority-unread-title")}"/>
					</#if>
				<#else>
					<#--if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_new_hot.gif" width="19" height="18" alt="topic hot new icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder_new.gif" width="19" height="18" alt="topic new icon" title="${I18n.getMessage("unread-message-title")}">
					</#if-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal_new.png" alt="topic unread message icon" title="${I18n.getMessage("unread-message-title")}"/>
				</#if>
			</#if>			
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock_new.png" alt="topic locked unread message icon" title="${I18n.getMessage("locked-forum-topic-unread-title")}"/>
		</#if>
	</#if>
</#macro>

<#macro folderImageSpecialAccess topic, specialAccess>
	<#if topic.read>
		<#if topic.status == STATUS_UNLOCKED>
		
			<#assign datesChecked = false/>
			<#if (specialAccess.accessDates?? && specialAccess.accessDates.openDate??) && (specialAccess.accessDates.openDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open.gif" width="19" height="18" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/-->
				<#if (specialAccess.accessDates.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open.png" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/>
				</#if>
				<#assign datesChecked = true/>
			<#elseif (specialAccess.accessDates?? && specialAccess.accessDates.allowUntilDate??)>
				<#if (specialAccess.accessDates.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (specialAccess.accessDates?? && specialAccess.accessDates.dueDate??) && (specialAccess.accessDates.dueDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce.gif" width="19" height="18" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce.png" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<!--img src="${contextPath}/templates/${templateName}/images/folder_sticky.gif" width="19" height="18" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky.png" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_task.gif" width="19" height="18" alt="task topic icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="re-use topic icon" title="${I18n.getMessage("reuse-topic-read-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="priority topic icon" title="${I18n.getMessage("reuse-topic-priority-read-title")}"/>
					</#if>
				<#else>
					<#--
					<#if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_hot.gif" width="19" height="18" alt="topic hot icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder.gif" width="19" height="18" alt="topic icon" title="${I18n.getMessage("message-title")}">
					</#if>
					-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal.png" alt="topic icon" title="${I18n.getMessage("message-title")}"/>
				</#if>
			</#if>
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock.png" alt="topic locked icon" title="${I18n.getMessage("locked-forum-topic-read-title")}"/>
		</#if>
	<#else>
		<#if topic.status == STATUS_UNLOCKED>
			<#assign datesChecked = false/>
			<#if (specialAccess.accessDates?? && specialAccess.accessDates.openDate??) && (specialAccess.accessDates.openDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open_new.gif" width="19" height="18" alt="not yet open new icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/-->
				<#if (topic.accessDates.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open_new.png" alt="not yet open - unread message icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/>
				</#if>
				<#assign datesChecked = true/>
			<#elseif (specialAccess.accessDates?? && specialAccess.accessDates.allowUntilDate??)>
				<#if (specialAccess.accessDates.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (specialAccess.accessDates?? && specialAccess.accessDates.dueDate??) && (specialAccess.accessDates.dueDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce_new.gif" width="19" height="18" alt="announce topic new icon" title="${I18n.getMessage("announce-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce_new.png" alt="announce topic unread message icon" title="${I18n.getMessage("announce-topic-unread-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_sticky_new.gif" width="19" height="18" alt="sticky topic new icon" title="${I18n.getMessage("sticky-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky_new.png" alt="sticky topic unread message icon" title="${I18n.getMessage("sticky-topic-unread-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_new_task.gif" width="19" height="18" alt="task topic new icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="re-use topic unread message icon" title="${I18n.getMessage("reuse-topic-unread-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="priority topic unread message icon" title="${I18n.getMessage("reuse-topic-priority-unread-title")}"/>
					</#if>
				<#else>
					<#--
					<#if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_new_hot.gif" width="19" height="18" alt="topic hot new icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder_new.gif" width="19" height="18" alt="topic new icon" title="${I18n.getMessage("unread-message-title")}">
					</#if>
					-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal_new.png" alt="topic unread message icon" title="${I18n.getMessage("unread-message-title")}"/>
				</#if>
			</#if>			
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock_new.png" alt="topic locked unread message icon" title="${I18n.getMessage("locked-forum-topic-unread-title")}"/>
		</#if>
	</#if>
</#macro>

<#macro row1Class topic>
	<#if topic.type == TOPIC_ANNOUNCE>
		row1Announce
	<#elseif topic.type == TOPIC_STICKY>
		row1Sticky
	<#else>
		row1
	</#if>
</#macro>

<#macro row2Class topic>
	<#if topic.type == TOPIC_ANNOUNCE>
		row2Announce
	<#elseif topic.type == TOPIC_STICKY>
		row2Sticky
	<#else>
		row2
	</#if>
</#macro>

<#macro row3Class topic>
	<#if topic.type == TOPIC_ANNOUNCE>
		row3Announce
	<#elseif topic.type == TOPIC_STICKY>
		row3Sticky
	<#else>
		row3
	</#if>
</#macro>

<#-- ****************** -->
<#-- Moderation buttons -->
<#-- ****************** -->
<#macro moderationButtons>
	 <#if moderator  && openModeration?default(false)>
		<#if can_remove_posts?default(false)>
			<#--input type="submit" name="topicRemove" value="&nbsp;&nbsp;${I18n.getMessage("Delete")}&nbsp;&nbsp;" class="liteoption" accesskey="${I18n.getMessage("delete-access")}" title="${I18n.getMessage("delete-access-description")}" onclick="return validateModerationDelete();"-->
			<span class="gen">
		  		<a class="gen" href="#" onclick="topicDelete();return false;" accesskey="${I18n.getMessage("delete-access")}" rel="nofollow" ><img class="imgicon" src="${contextPath}/templates/${templateName}/images/delete.png" alt="${I18n.getMessage("Delete")}" title="${I18n.getMessage("delete-access-description")}"/>${I18n.getMessage("Delete")}</a>
		  	</span>
		</#if>
		<#if can_move_topics?default(false)>
			<#--input type="submit" name="topicMove" value="&nbsp;&nbsp;${I18n.getMessage("move")}&nbsp;&nbsp;" class="liteoption" onclick="return verifyModerationCheckedTopics();"-->
			<span class="gen">
		  		&nbsp;<a class="gen" href="#" onclick="topicModeration('topicMove');return false;" rel="nofollow" ><img class="imgicon" src="${contextPath}/templates/${templateName}/images/move.png" alt="${I18n.getMessage("move")}" title="${I18n.getMessage("move")}"/>${I18n.getMessage("move")}</a>
		  	</span>
		</#if>
		<#if can_lockUnlock_topics?default(false)>
			<#--input type="submit" name="topicLock" value="&nbsp;&nbsp;${I18n.getMessage("Lock")}&nbsp;&nbsp;" class="liteoption" onclick="return verifyModerationCheckedTopics();"-->
			<span class="gen">
		  		&nbsp;<a class="gen" href="#" onclick="topicModeration('topicLock');return false;" rel="nofollow" ><img class="imgicon" src="${contextPath}/templates/${templateName}/images/lock.png" alt="${I18n.getMessage("Lock")}" title="${I18n.getMessage("Lock")}"/>${I18n.getMessage("Lock")}</a>
		  	</span>
			<#--input type="submit" name="topicUnlock" value="&nbsp;&nbsp;${I18n.getMessage("Unlock")}&nbsp;&nbsp;" class="liteoption" onclick="return verifyModerationCheckedTopics();"-->
			<span class="gen">
		  		&nbsp;<a class="gen" href="#" onclick="topicModeration('topicUnlock');return false;" rel="nofollow" ><img class="imgicon" src="${contextPath}/templates/${templateName}/images/lock_open.png" alt="${I18n.getMessage("Unlock")}" title="${I18n.getMessage("Unlock")}"/>${I18n.getMessage("Unlock")}</a>
		  	</span>
		</#if>
		
		<#--	
		<span class="gensmall"><b><a class="gensmall" href="javascript:select_switch(true);">${I18n.getMessage("PrivateMessage.checkAll")}</a> :: 
									<a class="gensmall" href="javascript:select_switch(false);">${I18n.getMessage("PrivateMessage.uncheckAll")}</a></b></span>
		-->
	  <#else>
	  	<#--
	  	<span class="gensmall"><b><a class="gensmall" href="javascript:select_switch(true);">${I18n.getMessage("PrivateMessage.checkAll")}</a> :: 
									<a class="gensmall" href="javascript:select_switch(false);">${I18n.getMessage("PrivateMessage.uncheckAll")}</a></b></span>
		-->	 
	  </#if>
</#macro>

<#-- ********************** -->
<#-- Forum navigation combo -->
<#-- ********************** -->
<#macro forumsComboTable>
	<table cellspacing="0" cellpadding="0" width="100%" border="0">
		<tr>
			<td align="right">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>			  
						<td nowrap="nowrap">
							<form name="f" id="f" accept-charset="${encoding}">
								<span class="gensmall">${I18n.getMessage("ForumIndex.goTo")}:&nbsp;
								<select onchange="if(this.options[this.selectedIndex].value != -1){ document.location = '${contextPath}/forums/show/'+ this.options[this.selectedIndex].value +'${extension}'; }" name="select">
									<option value="-1" selected="selected">${I18n.getMessage("ForumIndex.goToSelectAForum")}</option>				
									
									<#list allCategories as category>
										<option value="-1">&nbsp;</option>
										<option value="-1">${category.title}</option>
										<option value="-1">-------------</option>
										
										<#list category.getForums() as forum>
											<option value="${forum.id}">${forum.name}</option>
										</#list>
									</#list>
								</select>
								&nbsp;
								<input class="liteoption" type="button" value="${I18n.getMessage("ForumIndex.goToGo")}" onClick="if(document.f.select.options[document.f.select.selectedIndex].value != -1){ document.location = '${contextPath}/forums/show/'+ document.f.select.options[document.f.select.selectedIndex].value +'${extension}'; }" />
							</form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</#macro>




<#macro searchFolderImage topic>
	<#if topic.read>
		<#if topic.status == STATUS_UNLOCKED>
			
			<#assign datesChecked = false/>
			<#if (topic.startDate??) && (topic.startDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open.gif" width="19" height="18" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/-->
				<#if (topic.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open.png" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/>
				</#if>
				<#assign datesChecked = true/>
			<#elseif (topic.allowUntilDate??)>
				<#if (topic.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (topic.endDate??) && (topic.endDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce.gif" width="19" height="18" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce.png" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_sticky.gif" width="19" height="18" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky.png" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_task.gif" width="19" height="18" alt="task topic icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="re-use topic icon" title="${I18n.getMessage("reuse-topic-read-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="priority topic icon" title="${I18n.getMessage("reuse-topic-priority-read-title")}"/>
					</#if>
				<#else>
					<#--if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_hot.gif" width="19" height="18" alt="topic hot icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder.gif" width="19" height="18" alt="topic icon" title="${I18n.getMessage("message-title")}">
					</#if-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal.png" alt="topic icon" title="${I18n.getMessage("message-title")}"/>
				</#if>
			</#if>
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock.png" alt="topic locked icon" title="${I18n.getMessage("locked-forum-topic-read-title")}"/>
		</#if>
	<#else>
		<#if topic.status == STATUS_UNLOCKED>
			
			<#assign datesChecked = false/>
			<#if (topic.startDate??) && (topic.startDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open_new.gif" width="19" height="18" alt="not yet open new icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/-->
				<#if (topic.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open_new.png" alt="not yet open - unread message icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/>
				</#if>				
				<#assign datesChecked = true/>
			<#elseif (topic.allowUntilDate??)>
				<#if (topic.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (topic.endDate??) && (topic.endDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce_new.gif" width="19" height="18" alt="announce topic new icon" title="${I18n.getMessage("announce-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce_new.png" alt="announce topic unread message icon" title="${I18n.getMessage("announce-topic-unread-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_sticky_new.gif" width="19" height="18" alt="sticky topic new icon" title="${I18n.getMessage("sticky-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky_new.png" alt="sticky topic unread message icon" title="${I18n.getMessage("sticky-topic-unread-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_new_task.gif" width="19" height="18" alt="task topic new icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="re-use topic unread message icon" title="${I18n.getMessage("reuse-topic-unread-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="priority topic unread message icon" title="${I18n.getMessage("reuse-topic-priority-unread-title")}"/>
					</#if>
				<#else>
					<#--if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_new_hot.gif" width="19" height="18" alt="topic hot new icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder_new.gif" width="19" height="18" alt="topic new icon" title="${I18n.getMessage("unread-message-title")}">
					</#if-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal_new.png" alt="topic unread message icon" title="${I18n.getMessage("unread-message-title")}"/>
				</#if>
			</#if>			
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock_new.png" alt="topic locked unread message icon" title="${I18n.getMessage("locked-forum-topic-unread-title")}"/>
		</#if>
	</#if>
</#macro>

<#macro searchFolderImageSpecialAccess topic, specialAccess>
	<#if topic.read>
		<#if topic.status == STATUS_UNLOCKED>
		
			<#assign datesChecked = false/>
			<#if (specialAccess.startDate??) && (specialAccess.startDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open.gif" width="19" height="18" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/-->
				<#if (specialAccess.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open.png" alt="not yet open icon" title="${I18n.getMessage("not-yet-open-read-title")}"/>
				</#if>
				<#assign datesChecked = true/>
			<#elseif (specialAccess.allowUntilDate??)>
				<#if (specialAccess.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (specialAccess.endDate??) && (specialAccess.endDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock.png" alt="read only icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce.gif" width="19" height="18" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce.png" alt="announce topic icon" title="${I18n.getMessage("announce-topic-read-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<!--img src="${contextPath}/templates/${templateName}/images/folder_sticky.gif" width="19" height="18" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky.png" alt="sticky topic icon" title="${I18n.getMessage("sticky-topic-read-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_task.gif" width="19" height="18" alt="task topic icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="re-use topic icon" title="${I18n.getMessage("reuse-topic-read-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable.png" alt="priority topic icon" title="${I18n.getMessage("reuse-topic-priority-read-title")}"/>
					</#if>
				<#else>
					<#--
					<#if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_hot.gif" width="19" height="18" alt="topic hot icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder.gif" width="19" height="18" alt="topic icon" title="${I18n.getMessage("message-title")}">
					</#if>
					-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal.png" alt="topic icon" title="${I18n.getMessage("message-title")}"/>
				</#if>
			</#if>
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock.gif" width="19" height="18" alt="topic locked icon" title="${I18n.getMessage("locked-topic-read-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock.png" alt="topic locked icon" title="${I18n.getMessage("locked-forum-topic-read-title")}"/>
		</#if>
	<#else>
		<#if topic.status == STATUS_UNLOCKED>
			<#assign datesChecked = false/>
			<#if (specialAccess.startDate??) && (specialAccess.startDate?datetime gt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/not_yet_open_new.gif" width="19" height="18" alt="not yet open new icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/-->
				<#if (topic.isHideUntilOpen())>
					<img src="${contextPath}/templates/${templateName}/images/item_invisible.png" alt="not yet open - invisible icon" title="${I18n.getMessage("not-yet-open-invisible-title")}"/>
				<#else>
					<img src="${contextPath}/templates/${templateName}/images/item_not_yet_open_new.png" alt="not yet open - unread message icon" title="${I18n.getMessage("not-yet-open-unread-title")}"/>
				</#if>
				<#assign datesChecked = true/>
			<#elseif (specialAccess.allowUntilDate??)>
				<#if (specialAccess.allowUntilDate?datetime lt nowDate?datetime)>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
					<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
					<#assign datesChecked = true/>
				</#if>
			<#elseif (specialAccess.endDate??) && (specialAccess.endDate?datetime lt nowDate?datetime)>
				<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"/-->
				<img src="${contextPath}/templates/${templateName}/images/read_only_lock_new.png" alt="read only - unread message icon" title="${I18n.getMessage("locked-topic-read-title")}"/>
				<#assign datesChecked = true/>
			</#if>
			
			<#if !datesChecked>
				<#if topic.type == TOPIC_ANNOUNCE>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_announce_new.gif" width="19" height="18" alt="announce topic new icon" title="${I18n.getMessage("announce-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_announce_new.png" alt="announce topic unread message icon" title="${I18n.getMessage("announce-topic-unread-title")}"/>
				<#elseif topic.type == TOPIC_STICKY>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_sticky_new.gif" width="19" height="18" alt="sticky topic new icon" title="${I18n.getMessage("sticky-topic-unread-title")}"-->
					<img src="${contextPath}/templates/${templateName}/images/topic_sticky_new.png" alt="sticky topic unread message icon" title="${I18n.getMessage("sticky-topic-unread-title")}"/>
				<#elseif topic.isExportTopic()>
					<#--img src="${contextPath}/templates/${templateName}/images/folder_new_task.gif" width="19" height="18" alt="task topic new icon"-->
					<#if facilitator>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="re-use topic unread message icon" title="${I18n.getMessage("reuse-topic-unread-title")}"/>
					<#else>
					<img src="${contextPath}/templates/${templateName}/images/topic_reusable_new.png" alt="priority topic unread message icon" title="${I18n.getMessage("reuse-topic-priority-unread-title")}"/>
					</#if>
				<#else>
					<#--
					<#if topic.isHot()>
						<img src="${contextPath}/templates/${templateName}/images/folder_new_hot.gif" width="19" height="18" alt="topic hot new icon">
					<#else>
						<img src="${contextPath}/templates/${templateName}/images/folder_new.gif" width="19" height="18" alt="topic new icon" title="${I18n.getMessage("unread-message-title")}">
					</#if>
					-->
					<img src="${contextPath}/templates/${templateName}/images/topic_normal_new.png" alt="topic unread message icon" title="${I18n.getMessage("unread-message-title")}"/>
				</#if>
			</#if>			
		<#else>
			<#--img src="${contextPath}/templates/${templateName}/images/folder_lock_new.gif" width="19" height="18" alt="topic locked new icon" title="${I18n.getMessage("locked-topic-unread-title")}"-->
			<img src="${contextPath}/templates/${templateName}/images/topic_lock_new.png" alt="topic locked unread message icon" title="${I18n.getMessage("locked-forum-topic-unread-title")}"/>
		</#if>
	</#if>
</#macro>
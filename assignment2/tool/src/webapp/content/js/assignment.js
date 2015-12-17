var inIframe = true;
if (typeof iframeId == 'undefined') {
    iframeId = "container";
    inIframe = false;
}

function a2SetMainFrameHeight() {
    if (iframeId != "") {
        if (arguments[0] != null) {
            height = arguments[0];
        } else {
            height = jQuery(document).height(); // + 10;
        }
        jQuery("#" + iframeId, parent.document).height(height);
    }
}

groups_toggle = function() {
    el = jQuery("input[type='radio'][value='false'][name='page-replace\:\:access_select-selection']").get(0);
    if (el && el.checked) {
        jQuery('div#groups_table_li').hide();
    } else {
        jQuery('div#groups_table_li').show();
    }
};

function toggle_group_checkboxes(check_all_box) {
    if (check_all_box.checked) {
        jQuery('table#groupTable :checkbox').attr('checked', 'checked');
    } else {
        jQuery('table#groupTable :checkbox').removeAttr('checked');
    }
}

function update_resubmit_until() {
    el = jQuery("input:checkbox[@name='page-replace\:\:resubmit_until']").get(0);
    if (el) {
        if (el.checked) {
            jQuery(".resubmit_until_toggle").show();
        } else {
            jQuery(".resubmit_until_toggle").hide();
        }
    }
}

function enableDisableGradebookPoints() {
	var gradebook_points_label = jQuery("label[id='page-replace\:\:gradebook_points_label']");
	var gradebook_points = jQuery("input[name='page-replace\:\:gradebook_points']");
	
	var isGradable = false;
	
	if (jQuery("input[type='radio'][id='page-replace\:\:select_ungraded']").get(0).checked ||
	    jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0) == 0 ||

	    ! asnn2.isPointsGradable()) {
		
		gradebook_points.val("");
	    
	    gradebook_points.attr("disabled", true);
	    gradebook_points_label.hide();
	    gradebook_points.hide();
	} else {
        gradebook_points.removeAttr("disabled");
        gradebook_points_label.show();
	    gradebook_points.show();
	}
}

jQuery(document).ready(function() {
    update_resubmit_until();
    asnn2.initializeSorting();
    
    if (jQuery("input[name='page-replace\:\:gradebook_points']").size() > 0) {
        enableDisableGradebookPoints();
    }
});

function slide_submission(img) {
    jQuery(img).parent('h4').next('div').toggle();
    asnn2.flip_image(img);
}
function slideFieldset(img) {
    jQuery(img).parent('legend').next('ol').toggle();
    asnn2.flip_image(img);
    a2SetMainFrameHeight();
}
function slideDiv(img) {
    jQuery(img).parent('legend').next('div').toggle();
    asnn2.flip_image(img);
    a2SetMainFrameHeight();
}


var asnn2 = asnn2 || {};

(function(jQuery, asnn2) {

    asnn2.closeThickboxHelper = function() {
        tb_remove();
        // This really only happens in IE8/CompatMode on Windows Vista
        if (jQuery.browser.msie) {
            jQuery("textarea").each(function(){
                if (FCKeditorAPI != null){
                    editor = FCKeditorAPI.GetInstance(this.id);
                    if (editor != null) {
                        editor.Focus();
                    }
                }
            });
        }
    };


    asnn2.updateAttachments = function(imgsrc, filename, link, ref, filesize) {
        newRow = jQuery('#attachment-list-demo').clone(true); //.appendTo("#attachmentsFieldset ol:first").get(0);
        jQuery(newRow).removeClass("skip");
        jQuery(newRow).removeAttr("id");
        jQuery("img", newRow).attr("src", imgsrc);
        jQuery("a:first", newRow).attr("href", link);
        jQuery("a:first", newRow).html(filename);
        jQuery("input", newRow).attr("value", ref);
        jQuery("input", newRow).attr("name", asnn2.attachmentItemBinding);
        jQuery("span:first", newRow).html(filesize);
    
        // if there are multiple "Add Attachments" sections on the page,
        // we need a way to uniquely identify which section this new attachment
        // will be appended to. We will use the global var asnn2.attachmentItemListSelector
        // to uniquely identify the parent list 
        var attachList = jQuery(asnn2.attachmentItemListSelector);
        jQuery(newRow).appendTo(attachList);
        
        // we may need to hide the "No attachments" message
        attachList.prev(".no_attachments_yet").hide();
        
    }
    
    asnn2.removeAttachment = function(attach) {
        var attachment = jQuery(attach).parent('span').parent('li');
        var parentList = attachment.parent('ol');
        //remove the attachment
        attachment.remove();
        
        // we may need to display the "no attachments" message, so let's find out
        // if there are any attachments left
        if (parentList.children().length === 0) {
            // display the "no attach" message
            parentList.prev(".no_attachments_yet").show();
        }
    }
    
    /**
     * sets the asnn2.attachmentItemBinding variable with the value of the element
     * with the given attachBindingSelector. This variable is used to set the appropriate
     * binding when adding an attachment if we have multiple "Add attachments" sections
     * on the screen.  Also sets the attachmentItemListSelector. This variable is used
     * to identify which attachment section should be updated when we come back from the helper
     */
    asnn2.updateAttachmentVariables = function(attachBindingSelector, attachmentItemListSelector) {
       var attachBinding = jQuery(attachBindingSelector);
       asnn2.attachmentItemBinding = attachBinding.text();
       asnn2.attachmentItemListSelector = attachmentItemListSelector;
    }
    
    /**
     * toggle the first sibling of the toggleHeader with the given classSelector
     * @param sectionSelector jQuery selector of the sibling element you want to toggle
     * @param toggleHeader the header that does the toggling. we will use it to
     * flip the img, if applicable
     */
    asnn2.toggleSubsection = function(sectionSelector, toggleHeader) {
        var toggle = jQuery(toggleHeader);
        
        var subsection = toggle.next(sectionSelector);
        subsection.toggle();
        
        // now see if the expand/collapse img was used and needs to be toggled, as well
        var images = toggle.children('img');
        if (images.length > 0) {
            img = images.get(0);
            asnn2.flip_image(img);
        }    
    }
    
    asnn2.flip_image = function(img) {
        if (img) {
            if (img.src.match('collapse')) {
                img.src = img.src.replace(/collapse/, 'expand');
            } else {
                img.src = img.src.replace(/expand/, 'collapse');
            }
        }
    }
    
})(jQuery, asnn2);

/*
 * Some functions and utilities which may be useful outside of Assignments2
 */
var asnn2util = asnn2util || {};

(function(jQuery, asnn2util) {

    /**
     * A transform function of the kind to be used in fluid.transform. It's
     * purpose is to return the 'data' portion from an EntityBroker Entity
     * serialization.
     */
    asnn2util.dataFromEntity = function (obj, index) {
        return obj.data;
    };

    /**
     * Turns on the 2.x portals background overlay
     */
    asnn2util.turnOnPortalOverlay = function() {
        jQuery("body", parent.document).append('<div id="portalMask" style="position:fixed;width:100%;height:100%;"></div>');
        jQuery("#" + iframeId, parent.document).css("z-index", "9001").css("position", "relative").css("background", "#fff");
    };

    /**
     * Turns off the 2.x portal background overlay
     */
    asnn2util.turnOffPortalOverlay = function() {
        jQuery("#portalMask", parent.document).trigger("unload").unbind().remove();
        jQuery("#" + iframeId, parent.document).css("z-index", "0");
    };

    /**
     * This opens the jQuery object, hopefully representing a hidden element
     * somewhere on the page, as a dialog.  In addition to opening as a modal
     * dialog, it has support for blanking out the background portion of the
     * Sakai 2.x Series Portal.
     *
     * @param dialogObj  Should be a jQuery object for the hidden element to
     * be used as the dialog.
     */
    asnn2util.openDialog = function(dialogObj) {

         // http://bytes.com/groups/javascript/90412-distance-between-element-top-page
         getPageCoords = function (element) {
           var coords = { x: 0, y: 0};
           while (element) {
             coords.x += element.offsetLeft;
             coords.y += element.offsetTop;
             element = element.offsetParent;
           }
           return coords;
        }

        // The following assumes we are in an iframe.
        // It actually works out ok, but should do more testing when we're opened in our
        // own window.
        var viewableWindowHeight =  parent.document.documentElement.clientHeight;
        var scrollTop = parent.document.documentElement.scrollTop;
        var totalDistToDialog = scrollTop + ( viewableWindowHeight / 2 );

        var iframeOffsetTop =  getPageCoords(jQuery("#" + iframeId,parent.document)[0]).y;

        var dialogCenterX = totalDistToDialog - iframeOffsetTop;

        var dialogWidth = 520;
        var dialogHeight = 400;

        var iframeWidth = document.documentElement.clientHeight;

        var dialogXOption = ( iframeWidth / 2 ) - ( dialogWidth / 2 );

        var dialogYOption = dialogCenterX - 50; //( dialogHeight / 2 );

/*
        alert("viewableWindowHeight: " + viewableWindowHeight + "\n"
+ "\n scrollTop: " + scrollTop 
+ "\n totalDistToDialog: " + totalDistToDialog 
+ "\n iframeOffsetTop: " + iframeOffsetTop 
+ "\n dialogCenterX: " + dialogCenterX
+ "\n dialogWidth: " + dialogWidth
+ "\n dialogHeight: " + dialogHeight
+ "\n iframeWidth: " + iframeWidth
+ "\n dialogXOption: " + dialogXOption
+ "\n dialogYOption: " + dialogYOption);
*/

        // Sometimes the iframes don't report the correct width until some DOM manipulation 
        // occurs and this always needs to be positive.
        if (dialogXOption < 0) {
            dialogXOption = 0;
        }
 
        if (dialogYOption < 0) {
            dialogYOption = 0;
        }

        dialogOptions = {
	    resizable: false,
	    width: dialogWidth,
	    modal: true,
	    //            // ASNN-712 Browsers aren't giving the same widths across
	    //            // scenerios, setting to a constant padding.
	    position: [20,dialogYOption],
	    overlay: {
		opacity: 0.5,
		background: "#eee"
	    }
        };
	 // in inline view, the default position is fine
	if (!inIframe) 
	    dialogOptions = {
		resizable: false,
		width: dialogWidth,
		modal: true,
		overlay: {
		    opacity: 0.5,
		    background: "#eee"
		}
	    };
	if (inIframe)
	     asnn2util.turnOnPortalOverlay();
        dialogObj.dialog(dialogOptions).show();
    };

    /**
     * This will close dialog that was opened with asnn2util.openDialog.
     *
     * @param dialogObj The jQuery object representing the element being used
     * as the modal dialog.
     */
    asnn2util.closeDialog = function(dialogObj) {
        dialogObj.dialog('destroy');
        // Remove our event handlers so they are created fresh each time.
        asnn2util.turnOffPortalOverlay();
    };

})(jQuery, asnn2util);

var asnn2 = asnn2 || {};

(function(jQuery, asnn2) {

    // for reordering of assignments
    asnn2.saveOrdering = function(serializedString) {
        serializedString = serializedString.replace(/\[\]/g, "").replace(/row\ /g, "");
        if (serializedString) {
            serializedString = serializedString.replace(/&/g, "");
            var orderedAssignIds = serializedString.split("sortable=");
            var queries = new Array();
            queries.push(RSF.renderBinding("ReorderAssignmentsAction.orderedAssignIds", orderedAssignIds));
            queries.push(RSF.renderActionBinding("ReorderAssignmentsAction.execute"));
            var body = queries.join("&");
            jQuery.post(document.URL, body);
        }
    };

    gbItemName = "";
    gbDueTime = "";

    asnn2.finishedGBItemHelper = function(newGbItemName, newGbDueTime) {
        gbItemName = newGbItemName;
        gbDueTime = newGbDueTime;
    };

    /**
     * pushes the due date from the "add gradebook item" helper to the due date field on the "Add Assignment" screen
     */
    asnn2.populateDueDateWithGBItemDueDate = function() {
        // will be empty if no due date required
        if (gbDueTime != "") {
            var require_due_date = jQuery("input[name='page-replace\:\:require_due_date']").get(0);
            var curr_req_due_date = require_due_date.checked;

            // if it doesn't currently req due date, we will replace it with gb item due date
            if (!curr_req_due_date) {
                // set the text box holding the date
                var dueDate = new Date();
                dueDate.setTime(gbDueTime);
                var dateInput = jQuery("input[name='page-replace\:\:due_date\:1\:date-field']");
                dateInput.val(dueDate.formatDueDate());
                dateInput.change(); // need a change event to update the calendar widget
                // enable the due date section
                require_due_date.checked = true;
                var due_date_container = jQuery('#page-replace\\:\\:require_due_date_container').get(0);
                asnn2.showHideByCheckbox(require_due_date, due_date_container);
            }
        }
    };

    /**
     * Since Windows and Mac display different values for date.toLocaleDateString(),
     * we need make sure the display is consistently xx/yy/zz format instead
     * of January 1, 2008 format.  This method will use the PUC_DATE_FORMAT
     * used by the date picker to define the locale-aware date format we
     * should use for populating the due date
     */
    Date.prototype.formatDueDate = function() {
        // uses the PUC_DATE_FORMAT variable used by the date widget
        // as the default format
        var date = this;
        var month = (date.getMonth() + 1).toString(); // month is 0-based
        var day = date.getDate().toString();
        var fullYear = date.getFullYear().toString();
        var twoDigitYear = fullYear.substr(2);

        var dateFormat = PUC_DATE_FORMAT;
        if (!dateFormat) {
            dateFormat = "M/d/yy";
        }

        var formattedDueDate = dateFormat;
        // check for use of MM, DD, and YYYY before we replace
        if (dateFormat.indexOf("MM") != -1) {
            formattedDueDate = formattedDueDate.replace("MM", month);
        } else {
            formattedDueDate = formattedDueDate.replace("M", month);
        }

        if (dateFormat.indexOf("dd") != -1) {
            formattedDueDate = formattedDueDate.replace("dd", day);
        } else {
            formattedDueDate = formattedDueDate.replace("d", day);
        }

        if (dateFormat.indexOf("yyyy") != -1) {
            formattedDueDate = formattedDueDate.replace("yyyy", fullYear);
        } else {
            formattedDueDate = formattedDueDate.replace("yy", twoDigitYear);
        }

        return formattedDueDate;
    };

    /**
     * automatically select the newly created gb item created via the helper
     */
    asnn2.populateTitleWithGbItemName = function() {
        var curr_title = jQuery("input[name='page-replace\:\:title']").get(0).value;
        if (!curr_title || curr_title == "") {
            // get the currently selected gb item
            var gbSelect = jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0);
            var gbSelIndex = gbSelect.selectedIndex;
            if (gbSelIndex != 0) {
                var selectedItem = gbSelect.options[gbSelIndex].text;
                // replace the empty title field with the new_title
                jQuery("input[name='page-replace\:\:title']").val(selectedItem);
            }
        }
    };

    asnn2.populateGradebookPoints = function() {
        // get the currently selected gb item
        var gbSelect = jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0).value;
        var gbRadio = jQuery("input[type='radio'][id='page-replace\:\:select_graded']").get(0).checked;
        var points = "";

        if (gbSelect != "0" && gbRadio) {
            jQuery.ajax({
                type: "GET",
                async: false,
                url: "/direct/assignment2/getAssignmentPointsinGradebook",
                data: { 
                    contextId: asnn2.contextId,
                    gradebookItemId: gbSelect
                },
                error: function (jqXHR, textStatus, errorThrown) {
                },
                success: function (data) {
            	    points = data;
                }
            });
        }
        
	    jQuery("input[name='page-replace\:\:gradebook_points']").val(points);
        
    };
   
 // ONC-3367
    asnn2.isPointsGradable = function () {
        // get the currently selected gb item
        var gbSelect = jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0).value;
        var gbRadio = jQuery("input[type='radio'][id='page-replace\:\:select_graded']").get(0).checked;
        var answer = false;

        if (gbSelect != "0" && gbRadio) {
            jQuery.ajax({
                type: "GET",
                async: false,
                url: "/direct/assignment2/isPointsGradable",
                data: { 
                    contextId: asnn2.contextId,
                    gradebookItemId: gbSelect
                },
                error: function (jqXHR, textStatus, errorThrown) {
                },
                success: function (data) {
                	if (data != null && data.toLowerCase() == "true") {
                		answer = true;
                	}
                }
            });
        }
            
            return answer;
    };
    
    /**
     * Select the graded/ungraded radio button depending on whether a gb item
     * has been selected in the drop down
     */
    asnn2.selectGraded = function() {
        el = jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0);
        if (el.selectedIndex != 0) {
            jQuery("input[type='radio'][id='page-replace\:\:select_graded']").get(0).checked = true;

        } else {
            jQuery("input[type='radio'][id='page-replace\:\:select_ungraded']").get(0).checked = true;
        }
        
        enableDisableGradebookPoints();
    };

    /**
     * change the selected gb item based upon the gbItemName variable
     */
    asnn2.changeValue = function() {
        el = jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0);
        if (el) {
            for (i = 0; i < el.length; i++) {
                if (el.options[i].text == gbItemName) {
                    el.selectedIndex = i;
                }
            }
        }

        asnn2.selectGraded();
        asnn2.populateTitleWithGbItemName();
        asnn2.populateDueDateWithGBItemDueDate();
        asnn2.showHideGradeSettingError();
        asnn2.populateGradebookPoints();

    };

    /**
     * if user has entered an assignment title before clicking the
     * "Add Gradebook Item" helper link, this method will add that name
     * as a parameter so that the helper is auto-populated with the
     * assignment title
     */
    asnn2.update_new_gb_item_helper_url = function() {
        var gbUrlWithoutName = jQuery("a[id='page-replace\:\:gradebook_url_without_name']").attr("href");
        var new_title = jQuery("input[name='page-replace\:\:title']").get(0).value;

        // encode unsafe characters that may be in the assignment title
        var escaped_title = "";
        if (new_title) {
            escaped_title = escape(new_title);
        }
        
        // we also want to add the due date, if populated
        var require_due_date = jQuery("input[name='page-replace\:\:require_due_date']").get(0);
        var curr_req_due_date = require_due_date.checked;
        if (curr_req_due_date) {
        	var dateInput = jQuery("input[name='page-replace\:\:due_date\:1\:date-field']");
        	var dateVal = dateInput.val();
        	var time = getInputDateAsTimeString(dateVal);
        }

        var modifiedUrl = gbUrlWithoutName + "&name=" + escaped_title + "&dueDateTime=" + time;

        jQuery("a[id='page-replace\:\:gradebook_item_new_helper']").attr("href", modifiedUrl);
        
    }
    
    /**
     * This method will take in a date as string (most likely from the date picker input)
     * that is in the PUC_DATE_FORMAT, parse it into pieces and figure out the
     * equivalent Time value (# milliseconds since yadayada). This format is used
     * to pass the due date from the "add assignment" screen to the gb helper
     */
    function getInputDateAsTimeString(dateString) {
    	var time = "";
    	if (dateString) {
    		// let's see what format is expected and then parse it. i know it's gross...
    		var dateFormat = PUC_DATE_FORMAT;
            if (!dateFormat) {
                dateFormat = "M/d/yy";
            }
            
            // now, let's split this date on '/'
            var datePieces = dateString.split("/");
            var dateFormatPieces = dateFormat.split("/");
            var month;
            var day;
            var year;
            if (datePieces && dateFormatPieces) {
            	if (datePieces.length === dateFormatPieces.length) {
            		for (index = 0; index < datePieces.length; index++) {
            			var formatPiece = dateFormatPieces[index].toLowerCase();
            			var datePiece = datePieces[index];
            			if (formatPiece === "m" || formatPiece === "mm") {
            				month = datePiece;
            			} else if (formatPiece === "d" || formatPiece === "dd") {
            				day = datePiece;
            			} else if (formatPiece === "yy" || formatPiece === "yyyy") {
            				if (datePiece.length === 4) {
            					year = datePiece;
            				} else {
            					// I know this is bad, but I'm not sure how else to do this...
            					year = "20" + datePiece;
            				}
            			}
            		}
            	}
            }
            
            if (month && day && year) {
            	var newDate = new Date();
            	newDate.setDate(day);
            	newDate.setMonth(month-1); // month is 0-based
            	newDate.setFullYear(year);
            	
            	time = newDate.getTime();
            }
    	}
    	
    	return time;
    }

    /**
     * If due date changes and no accept until date has been set, populate
     * the accept until text field with the new due date. This does not
     * select the accept until date as "required," it just sets the default
     * value if someone then goes on to require it
     */
    asnn2.populate_accept_until_with_due_date = function() {
        var require_accept_until = jQuery("input[name='page-replace\:\:require_accept_until']").get(0);
        if (!require_accept_until.checked) {
            // get the due date
            var dueDate = jQuery("input[name='page-replace\:\:due_date\:1\:date-field']").val();
            if (dueDate) {
                var acceptUntilDate = jQuery("input[name='page-replace\:\:accept_until\:1\:date-field']");
                acceptUntilDate.val(dueDate);
                acceptUntilDate.change();
            }
        }
    }

    /**
     * If due time changes and no accept until has been set, populate the accept until time
     * with the due time. This does not select the accept until date as "required," it
     * just sets the default value if someone then goes on to require it.
     */
    asnn2.populate_accept_until_time_with_due_time = function() {
        var require_accept_until = jQuery("input[name='page-replace\:\:require_accept_until']").get(0);
        if (!require_accept_until.checked) {
            // get the due time
            var dueTime = jQuery("input[name='page-replace\:\:due_date\:1\:time-field']").val();
            if (dueTime) {
                var acceptUntilTime = jQuery("input[name='page-replace\:\:accept_until\:1\:time-field']");
                acceptUntilTime.val(dueTime);
            }
        }
    }

    //Sorting functions
    var sortBy;
    var sortDir;
    var pStart = 0;
    var pLength = 5;

    function sortPageRows(b, d) {
        pLength = jQuery("select[name='page-replace\:\:pagerDiv:1:pager_select_box-selection']").val();
        sortBy = b;
        sortDir = d;
        var trs = jQuery.makeArray(jQuery("table#sortable tr:gt(0)"));
        trs.sort(function(a, b) {
            return (jQuery("." + sortBy, a).get(0).innerHTML.toLowerCase() < jQuery("." + sortBy, b).get(0).innerHTML.toLowerCase() ? -1 : (jQuery("." + sortBy, a).get(0).innerHTML.toLowerCase() > jQuery("." + sortBy, b).get(0).innerHTML.toLowerCase() ? 1 : 0));
        });
        if (sortDir == 'desc') {
            trs.reverse();
        }
        jQuery(trs).appendTo(jQuery("table#sortable tbody"));
        jQuery("a img", "table#sortable tr:first").remove();
        imgSrc = "<img src=\"/sakai-assignment2-tool/content/images/bullet_arrow_" + (d == 'asc' ? 'up': 'down') + ".png\" />";
        jQuery("a." + b, "table#sortable tr:first").append(imgSrc);
        //Now paging
        jQuery("table#sortable tr:gt(0)").hide();
        jQuery("table#sortable tr:gt(" + pStart + "):lt(" + pLength + ")").show();
        trsLength = jQuery("table#sortable tr:gt(0)").size();
        if (pStart == 0) {
            jQuery("div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_first_page'], div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_prev_page']").attr('disabled', 'disabled');
        } else {
            jQuery("div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_first_page'], div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_prev_page']").removeAttr('disabled');
        }
        if (Number(pStart) + Number(pLength) >= trsLength) {
            jQuery("div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_next_page'], div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_last_page']").attr('disabled', 'disabled');
        } else {
            jQuery("div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_next_page'], div.pagerDiv input[name='page-replace\:\:pagerDiv\:1\:pager_last_page']").removeAttr('disabled');
        }

        //now parse the date
        format = jQuery("div.pagerDiv div.pager_viewing_format").get(0).innerHTML;
        format = format.replace(/\{0\}/, Number(pStart) + 1);
        last = Number(pStart) + Number(pLength) > trsLength ? trsLength: Number(pStart) + Number(pLength);
        format = format.replace(/\{1\}/, last);
        format = format.replace(/\{2\}/, jQuery("table#sortable tr:gt(0)").size());
        jQuery("div.pagerDiv div.pagerInstruction").html(format);

    };

    asnn2.initializeSorting = function() {
        if (jQuery("table#sortable").get(0)) {
            sortPageRows(defaultSortBy, 'asc');
            pStart = 0;
        }
    };

    asnn2.changeSort = function(newBy) {
        sortPageRows(newBy, (newBy != sortBy ? 'asc': (sortDir == 'asc' ? 'desc': 'asc')));
    };

    asnn2.changePage = function(newPStart) {
        total = jQuery("table#sortable tr:gt(0)").size();
        if ("first" == newPStart) {
            pStart = 0;
        } else if ("prev" == newPStart) {
            pStart = pStart - pLength;
            if (pStart < 0) pStart = 0;
        } else if ("next" == newPStart) {
            pStart = Number(pStart) + Number(pLength);
            if (pStart > total) {
                pStart = total - (total % pLength);
            }
        } else if ("last" == newPStart) {
            if (total > pLength) {
                pStart = total - (total % pLength);
            } else {
                pStart = 0;
            }
        }
        sortPageRows(sortBy, sortDir);
    };

})(jQuery, asnn2);

/* New Asnn2 functions that are namespaced. Will need to go back
 * and namespace others eventually.
 */

(function(jQuery, asnn2) {
    var EXPAND_IMAGE = "/sakai-assignment2-tool/content/images/expand.png";
    var COLLAPSE_IMAGE = "/sakai-assignment2-tool/content/images/collapse.png";
    var NEW_FEEDBACK_IMAGE = "/library/image/silk/email.png";
    var READ_FEEDBACK_IMAGE = "/library/image/silk/email_open.png";

    asnn2.toggle_hideshow_by_id = function(arrowImgId, toggledId) {
        toggle_hideshow(jQuery('#' + arrowImgId.replace(/:/g, "\\:")), jQuery('#' + toggledId.replace(/:/g, "\\:")));
    }

    function toggle_hideshow(arrowImg, toggled) {
        if (arrowImg.attr('src') == EXPAND_IMAGE) {
            arrowImg.attr('src', COLLAPSE_IMAGE);
            toggled.show();
        } else {
            arrowImg.attr('src', EXPAND_IMAGE);
            toggled.hide();
        }
    };

    function mark_feedback(submissionId, versionId) {
        var queries = new Array();
        queries.push(RSF.renderBinding("MarkFeedbackAsReadAction.asnnSubId", submissionId));
        queries.push(RSF.renderBinding("MarkFeedbackAsReadAction.asnnSubVersionId", versionId));
        queries.push(RSF.renderActionBinding("MarkFeedbackAsReadAction.execute"));
        var body = queries.join("&");
        jQuery.post(document.URL, body);
    };

    /**
     * Custom javascript for the Assignment Add/Edit page. When the "Require
     * Submissions" box is checked or unchecked, the Submission Details below it
     * need to show/hide. It does this by hiding/removing all siblings at the
     * same level (ie. the rest of the <li/> in the current list). it also
     * needs to show/hide the notifications block, as well
     *
     * @param element The jQuery element whose downstream siblings will be
     * shown or hidden
     * @param show Whether or not to show them.
     */
    asnn2.showHideSiblings = function(element, show) {
        if (show == true) {
            jQuery(element).nextAll().show();
            jQuery("#notifications").show();
            jQuery("#acceptuntil").show();
            jQuery("#grading").show();
        } else {
            jQuery(element).nextAll().hide();
            jQuery("#notifications").hide();
            jQuery("#acceptuntil").hide();
            jQuery("#grading").hide();
        }
    };

    /**
     * Given a checkbox element, hide or show the area element whenever checkbox
     * is clicked.  Checking the box shows the area, unchecking the box hides
     * the area.
     */
    asnn2.showHideByCheckbox = function(checkboxElem, areaElem) {
        if (checkboxElem && areaElem) {
            var area = jQuery(areaElem);
            if (checkboxElem.checked) {
                area.show();
            } else {
                area.hide();
            }
        }
        
        a2SetMainFrameHeight();
    };
    
    /**
     * @param selectElem
     * @param selectOptions array of values such that, if selectElem is set to one of these values, 
     * we show the given areaElem. if the value of the selectElem is not included in this array, we hide the areaElem.
     * @param areaElem
     */
    asnn2.showHideBySelect = function(selectElem, selectOptions, areaElem) {
      var selectObj = jQuery(selectElem);
      var areaObj = jQuery(areaElem);

      var displayArea = false;
      
      var selectValue = selectObj.val();
      if (selectOptions) {
        for (index in selectOptions) {
          if (selectValue === selectOptions[index]) {
            displayArea = true;
            break;
          }
        }
      }
      
      if (displayArea) {
        areaObj.show();
      } else {
        areaObj.hide();
      }
    }
    
    /**
     * Display a special warning to indicate that the assignment is marked as graded but no item selected
     */
    asnn2.showHideGradeSettingError = function() {
        var graded = jQuery("input[type='radio'][id='page-replace\:\:select_graded']").get(0).checked;
        var gradingSection = jQuery("#grading_section");
        var gradingWarningText = jQuery("#page-replace\\:\\:grading_warning");
        var gradingOptions = jQuery("#grading_options");
        if (graded) {
            // we need to see if a gradebook item was selected
            var gbItemSelect = jQuery("select[name='page-replace\:\:gradebook_item-selection']");
            var selectedItem = gbItemSelect.val();
            
            if (!selectedItem || selectedItem === "0") {
                // display the error indicator
                gradingSection.addClass("messageConfirmation");
                gradingWarningText.show();
                gradingOptions.addClass("messageContentPadding");
            } else {
                gradingSection.removeClass("messageConfirmation");
                gradingWarningText.hide();
                gradingOptions.removeClass("messageContentPadding");
            }
        } else {
            gradingSection.removeClass("messageConfirmation");
            gradingWarningText.hide();
            gradingOptions.removeClass("messageContentPadding");
        }
    };
    
    asnn2.modelAnswerIntegrity = function(checkTrigger) {
        var require_submissions = jQuery("input[name='page-replace\:\:require_submissions']").get(0).checked;
        var due_date = jQuery("input[name='page-replace\:\:require_due_date']").get(0).checked;
        var accept_date = jQuery("input[name='page-replace\:\:require_accept_until']").get(0).checked;
        var modelAnswerEnabled = jQuery("input[name='page-replace\:\:modelAnswerEnabled']").get(0).checked;
        var madr = jQuery("select[name='page-replace\:\:modelAnswerDisplayRule-selection']");

        if (!asnn2.modelDispSubOpts) {                          
            asnn2.modelDispSubOpts = {}                         
            for (var i = 0; i < 6; i++) {                   
                asnn2.modelDispSubOpts[i] = madr.find("option[value='"+i+"']").clone();
            }
        }

        // grab the current value of the dropdown before we remove the options
        var madrVal = madr.val();
        
        // remove all options from the dropdown before we repopulate it
        madr.children().remove();                               

        var addMadrOptions = function() {                       
            for (var i = 0; i < arguments.length; i++) {        
                var newopt = asnn2.modelDispSubOpts[arguments[i]].clone();
                madr.append(newopt);                    
            }
        }

        // See the design spec to make sense of these combinations
        if (!require_submissions && !due_date && !accept_date) {
            //alert("Null set");
            addMadrOptions(0,1);
        }
        else if (require_submissions && !due_date && !accept_date) {
            //alert("R");
            addMadrOptions(0,1,2,3);
        }
        else if (!require_submissions && due_date && !accept_date) {
            //alert("D"); 
            addMadrOptions(0,1,4);            
        }
        else if (!require_submissions && !due_date && accept_date) {
            //alert("Null A"); same as Null set because of the way you get here.
            addMadrOptions(0,1);
        }       
        else if (require_submissions && due_date && !accept_date) {
            //alert("RD");
            addMadrOptions(0,1,2,3,4);
        }
        else if (require_submissions && !due_date && accept_date) {
            //alert("RA"); 
            addMadrOptions(0,1,2,3,5);
        }       
        else if (!require_submissions && due_date && accept_date) {
            //alert("NULL DA"); same as D
            addMadrOptions(0,1,4);
        }
        else if (require_submissions && due_date && accept_date) {
            //alert("RDA");
            addMadrOptions(0,1,2,3,4,5);
        }
        // set the dropdown to the previous value after repopulating the dropdown
        madr.val(madrVal);
        // display warning if the previous option is no longer valid
        var modelAlert = jQuery("#model_alert");
        var modelWarningSubmissionText = jQuery("#page-replace\\:\\:model_warning_submission");
        var modelWarningDueDateText = jQuery("#page-replace\\:\\:model_warning_due_date");
        var modelWarningAcceptUntilText = jQuery("#page-replace\\:\\:model_warning_accept_until");
        if (modelAnswerEnabled && madr.val()!==madrVal)
        {
            if (checkTrigger==="init")
            {
                modelAlert.removeClass("messageConfirmation");
                modelWarningSubmissionText.hide();
                modelWarningDueDateText.hide();
                modelWarningAcceptUntilText.hide();
                modelAlert.removeClass("messageContentPadding");
            }
            else if (checkTrigger==="require_submissions")
            {
                modelAlert.addClass("messageConfirmation");
                modelWarningSubmissionText.show();
                modelWarningDueDateText.hide();
                modelWarningAcceptUntilText.hide();
                modelAlert.addClass("messageContentPadding");
                // Set to 'Immediately'
                madr.val('1');
            }
            else if (checkTrigger==="due_date")
            {
                modelAlert.addClass("messageConfirmation");
                modelWarningSubmissionText.hide();
                modelWarningDueDateText.show();
                modelWarningAcceptUntilText.hide();
                modelAlert.addClass("messageContentPadding");
                // Set to 'Never'
                madr.val('0');
            }
            else if (checkTrigger==="accept_until")
            {
                modelAlert.addClass("messageConfirmation");
                modelWarningSubmissionText.hide();
                modelWarningDueDateText.hide();
                modelWarningAcceptUntilText.show();
                modelAlert.addClass("messageContentPadding");
                // Set to 'Never'
                madr.val('0');
            }
        }
        else
        {
            modelAlert.removeClass("messageConfirmation");
            modelWarningSubmissionText.hide();
            modelWarningDueDateText.hide();
            modelWarningAcceptUntilText.hide();
            modelAlert.removeClass("messageContentPadding");
        }
    }

    /**
     * Setup the element for a Assignment Submission Version. This includes
     * hooking up the (un)collapse actions, as well as the Ajax used to mark
     * feedback as read when the div is expanded.
     *
     * If the markup changes, this will need to change as well as it depends
     * on the structure.
     */
    asnn2.assnSubVersionDiv = function(elementId, feedbackRead, submissionId, versionId, readFBAltText) {
        var escElemId = elementId.replace(/:/g, "\\:");
        var versionHeader = jQuery('#' + escElemId + ' h3');
        var arrow = versionHeader.find("img:first");
        var toggled = jQuery('#' + escElemId + ' div');
        var envelope = versionHeader.find("img:last");
        versionHeader.click(function() {
            toggle_hideshow(arrow, toggled);
            if (envelope.attr('src') == NEW_FEEDBACK_IMAGE) {
                envelope.attr('src', READ_FEEDBACK_IMAGE);
                envelope.attr('alt', readFBAltText);
                envelope.attr('title', readFBAltText);
                mark_feedback(submissionId, versionId);
            }
        });
    };
    
    /**
     * @param toggleElement the toggle element that contains the image to swap
     * @param submissionId the submissionId to mark read
     * @param versoinId the versionId to mark read
     * @param readFBAltText the alt text that will replace the original text
     * Will swap the unread feedback image to the read image and mark the
     * feedback as read via an ajax call
     */
    asnn2.readFeedback = function(toggleElement, submissionId, versionId, readFBAltText) {
        var toggle = jQuery(toggleElement);
        // try to find the unread fb img
        toggle.children('img').each(function() {
            if (this.src.match(NEW_FEEDBACK_IMAGE)) {
                var fbImg = jQuery(this);
                fbImg.attr('src', READ_FEEDBACK_IMAGE);
                fbImg.attr('alt', readFBAltText);
                fbImg.attr('title', readFBAltText);
                mark_feedback(submissionId, versionId);
            }
        });
    };
    

    /**
     * Used to generate the confirmation dialog for different choices on the
     * student submission screen (ie submitting or cancelling submission)
     * @param the form button that was clicked
     * @param dialogSelector jQuery selector for identifying the correct dialog to show
     * @param requireHonorPledge true if the honor pledge needs to be check before proceeding
     * 
     */
    asnn2.studentActionConfirm = function(buttonform, dialogSelector, requireHonorPledge) {
        // first, let's make sure the user has checked the honor pledge, if needed.
        if (requireHonorPledge) {
            // look for the honor pledge checkbox
            var honor_pledge = jQuery('#page-replace\\:\\:portletBody\\:1\\:assignment-edit-submission\\:\\:honor_pledge').get(0);
            if (honor_pledge) {
                if (!honor_pledge.checked) {
                    //display the error and return
                    jQuery('#page-replace\\:\\:portletBody\\:1\\:assignment-edit-submission\\:\\:honor_pledge_error').show();
                    return false;
                }
            }
        }

        // display the confirmation dialog
        confirmDialog = jQuery(dialogSelector);

        var submitButton = buttonform;
        jQuery('input.submission-confirm-button').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
            submitButton.onclick = function(event) {
                // hide the buttons at the bottom of the page to prevent
                // user from hitting them again while the button click request is processed
                jQuery('#page-replace\\:\\:portletBody\\:1\\:assignment-edit-submission\\:\\:submit_section').hide();
                return true;
            };
            submitButton.click();
        });

        jQuery('input.submission-cancel-button').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
            // clear the onclick event on the confirm button
            jQuery('input.submission-confirm-button').unbind('click');
        });

        asnn2util.openDialog(confirmDialog);
        return false;
    };
    
    /**
     * Used to generate the confirmation dialog for when a user removes an attachment
     */
    asnn2.removeAttachmentConfirm = function(thisAttachment) {

        // display the confirmation dialog
        confirmDialog = jQuery('#remove-attachment-confirm-dialog');

        jQuery('input.att-delete-confirm-button').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
            asnn2.removeAttachment(thisAttachment);
        });

        jQuery('input.att-delete-cancel-button').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
            // clear the onclick event on the confirm button or else removing
            // a different attachment will take this one too
            jQuery('input.att-delete-confirm-button').unbind('click');
        });

        asnn2util.openDialog(confirmDialog);
        return false;
    };

    /**
     * Used to generate a confirmation dialog if a user attempts to edit an
     * assignment that already has submissions
     */
    asnn2.editAssignmentConfirm = function(buttonform) {

        // validate the form before we pop up the confirmation
       var valid = asnn2editpage.validate();
        if (!valid) {
          return false;
        }
      
        // display the confirmation dialog
        confirmDialog = jQuery('#edit-assign-confirm-dialog');

        var submitButton = buttonform;
        jQuery('#page-replace\\:\\:submission-confirm-button').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
            submitButton.onclick = function(event) {
                return true
            };
            submitButton.click();
        });

        jQuery('#page-replace\\:\\:submission-cancel-button').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
        });

        asnn2util.openDialog(confirmDialog);
        return false;
    };

    /**
     * Release/Retract all feedback confirmation dialog
     *
     * This function uses the asnn2 dialog utility. The same function
     * is used for both releasing and retracting feedback, distinguished by the
     * "release" boolean parameter. We just use string substitution to
     * differentiate the release and retract dialogs. Be careful if you
     * change the naming conventions!
     *
     * @param submitButtonId - the id of the html element that actually is submitted
     * @param release - true if you want the "release" dialog; false for the "retract" dialog
     */
    asnn2.releaseFeedbackDialog = function(submitButtonId, release) {
        var releaseText;
        if (release) {
            releaseText = 'release';
        } else {
            releaseText = 'retract';
        }

        var confirmDialog = jQuery('#' + releaseText + '-feedback-dialog');
        var submitButton = jQuery('input[id=\'' + submitButtonId + '\']');
        var confirmButton = jQuery('#page-replace\\:\\:' + releaseText + '-feedback-confirm');
        confirmButton.click(function(event) {
            asnn2util.closeDialog(confirmDialog);
            submitButton.onclick = function(event) {
                return true
            };
            submitButton.click();
        });

        var cancelButton = jQuery('#page-replace\\:\\:' + releaseText + '-feedback-cancel').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
        });

        asnn2util.openDialog(confirmDialog);
        return false;
    };

    /**
     * Release/Retract all grades
     *
     * This function uses the asnn2 dialog utility.  The same function is used for
     * both releasing and retracting grades. This is because the two cases
     * are handled by different translations being used in the java code. The
     * two cases use the same submit button for the form.
     *
     * @param submitButtonId the id of the html element that actually is submitted
     * @param contextId the contextId for the submission
     * @param gradebookItemId the gradebookItemId associated w/ the assignment to release grades
     * @param release true if you want to release, false if you want to retract grades
     */
    asnn2.releaseGradesDialog = function(submitButtonId, contextId, gradebookItemId, release) {
        var confirmDialog = jQuery('#release-grades-dialog');
        var submitButton = jQuery('input[id=\'' + submitButtonId + '\']');
        var confirmButton = jQuery('#page-replace\\:\\:release-grades-confirm');
        confirmButton.click(function(event) {
            var confirmCheckbox = jQuery("#confirm-checkbox").get(0);
            if (confirmCheckbox && !confirmCheckbox.checked) {
                jQuery("#page-replace\\:\\:checkbox-error").show();
            } else {
                // include the value of the "include in course grade" option
                var includeInCourseGrade = false;
                var includeInGradeEl = jQuery("#release-and-count").get(0);
                if (includeInGradeEl) {
                    includeInCourseGrade = includeInGradeEl.checked;
                }
                var queries = new Array();
                queries.push(RSF.renderBinding("ReleaseGradesAction.gradebookItemId", gradebookItemId));
                queries.push(RSF.renderBinding("ReleaseGradesAction.curContext", contextId));
                queries.push(RSF.renderBinding("ReleaseGradesAction.releaseGrades", release));
                queries.push(RSF.renderBinding("ReleaseGradesAction.includeInCourseGrade", includeInCourseGrade));

                queries.push(RSF.renderActionBinding("ReleaseGradesAction.execute"));
                var body = queries.join("&");

                jQuery.ajax({
                  type: "POST",
                  url: document.URL,
                  data: body,
                  cache: false,
                  success: function () {
                    asnn2util.closeDialog(confirmDialog);
                    submitButton.onclick = function(event) {
                      return true;
                    };
                    submitButton.click();
                },
                  failure: function() {
                  // TODO We need to handle this
                  } 
                });
            }
        });

        var cancelButton = jQuery('#page-replace\\:\\:release-grades-cancel').click(function(event) {
            asnn2util.closeDialog(confirmDialog);
        });

        asnn2util.openDialog(confirmDialog);
        return false;
    };
    
})(jQuery, asnn2);

// This namespace is for the Assignment Authoring and Editing Screen
var asnn2editpage = asnn2editpage || {};

(function(jQuery, asnn2editpage) {
    asnn2editpage.validate = function() {
        titleMsg = jQuery("#page-replace\\:\\:assignment_title_empty");
        nogbMsg = jQuery("#page-replace\\:\\:assignment_graded_no_gb_item");
        dueBeforeOpenMsg = jQuery("#page-replace\\:\\:assignment_due_before_open");
        acceptBeforeOpenMsg = jQuery("#page-replace\\:\\:assignment_accept_before_open");
        acceptBeforeDueMsg = jQuery("#page-replace\\:\\:assignment_accept_before_due");
        checkAgainstMsg = jQuery("#page-replace\\:\\:assignment_check_against");
        checkAgainstMsg2 = jQuery("#page-replace\\:\\:assignment_check_against2");
        
        /*titleMsg.hide();
            nogbMsg.hide();
            dueBeforeOpenMsg.hide();
            acceptBeforeOpenMsg.hide();
            acceptBeforeDueMsg.hide();*/

        // hide all error messages. some may come from the date widget
        // built-in validator
        jQuery("li.alertMessageInline").hide();

        var valid = true;
        // Reference: You can see these in  Assignment2Validator.java
        // check for empty title
        var title = jQuery("input[name='page-replace\:\:title']").get(0);
        if (title.value == '') {
            titleMsg.show();
            valid = false;
        }

        //check for graded but no gradebookItemId
        var usegb = jQuery("input[id='page-replace\:\:select_graded']").get(0);
        var gbitem = jQuery("select[name='page-replace\:\:gradebook_item-selection']").get(0);
        if (usegb.checked && gbitem.value == '0') {
            nogbMsg.show();
            valid = false;
        }

        var openDateStr = jQuery("#page-replace\\:\\:open_date\\:1\\:true-date").get(0).value;
        var acceptDateStr = jQuery("#page-replace\\:\\:accept_until\\:1\\:true-date").get(0).value;
        var dueDateStr = jQuery("#page-replace\\:\\:due_date\\:1\\:true-date").get(0).value;

        // if the user requires a due date, we need to validate it against the
        // open date
        var require_due_date = jQuery("input[name='page-replace\:\:require_due_date']").get(0).checked;
        if (require_due_date) {
            // check for due date after open date
            // if the due date is null, we'll let the date widget take care of
            // that validation
            if (dueDateStr != '' && dueDateStr <= openDateStr) {
                dueBeforeOpenMsg.show();
                valid = false;
            }
        }

        // if the user requires an accept until date, we need to validate it
        // against the open and due dates
        var require_accept_until = jQuery("input[name='page-replace\:\:require_accept_until']").get(0).checked;
        if (require_accept_until) {
            // we'll let the date widget take care of the null and formatting checks
            if (require_due_date) {
                // check for accept until before due date
                if (acceptDateStr != '' && acceptDateStr < dueDateStr) {
                    acceptBeforeDueMsg.show();
                    valid = false;
                }
            } else {
                // check for accept until date before open date
                if (acceptDateStr != '' && acceptDateStr <= openDateStr) {
                    acceptBeforeOpenMsg.show();
                    valid = false;
                }
            }
        }
        
        // validate the turnitin options ASNN-516
        var useTiiOption = jQuery("input[name='page-replace\:\:use_tii']").get(0);
        var contentReviewName = jQuery(".contentReviewName").html();
        if (useTiiOption && useTiiOption.checked && "TurnItIn" === contentReviewName) {
          // see if at least one checkbox was checked for the "check against" option
          if (jQuery("input[name='page-replace\:\:check_against_student_repo_checkbox']").is(':checked') ||
              jQuery("input[name='page-replace\:\:check_against_internet_repo_checkbox']").is(':checked') ||
              jQuery("input[name='page-replace\:\:check_against_journal_repo_checkbox']").is(':checked') ||
              jQuery("input[name='page-replace\:\:check_against_institution_repo_checkbox']").is(':checked')) {
            // we want at least one to be checked
          } else {
            valid = false;
            checkAgainstMsg.show();
            checkAgainstMsg2.show();
          }
        }

        if (!valid) {
            window.parent.scrollTo(0, 0);
            return false;
        }

        return true;
    };
    
    asnn2editpage.tii_dueDateOptions = function() {
      var tii_content = jQuery('#page-replace\\:\\:tii_content_review_area').get(0);
      if (tii_content) {
        // check to see if user has set a due date
        var require_due_date = jQuery("input[name='page-replace\:\:require_due_date']").get(0);
        var no_due_date_warning = jQuery("#page-replace\\:\\:gen_reports_no_due_date");
        var gen_report_on_due_date = jQuery("input[type='radio'][id='page-replace\:\:gen_report_on_due_date']");
        
        if (require_due_date && require_due_date.checked) {
          // there is a due date, so we enable the due date radio and hide the warning
          no_due_date_warning.hide();
          gen_report_on_due_date.removeAttr("disabled");
        } else {
          // otherwise, hide the warning, disable the due date option, and select the immediate option
          no_due_date_warning.show();
          gen_report_on_due_date.attr("disabled","disabled");
          var gen_report_immediately = jQuery("input[type='radio'][id='page-replace\:\:gen_report_immediately']");
          gen_report_immediately.attr("checked", "checked");
        }
        
      }
    };
    
    asnn2editpage.tii_attachWarning = function() {
        var tii_content = jQuery('#page-replace\\:\\:tii_content_review_area').get(0);
        if (tii_content) {
            // check to see if tii is enabled
            var useTiiOption = jQuery("input[name='page-replace\:\:use_tii']").get(0);
            var attach_warning = jQuery("#page-replace\\:\\:tii_attach_warning");
            if (useTiiOption && useTiiOption.checked) {
                // check to see if assignment is set to accept text & attachments
                var submission_method = jQuery("select[name='page-replace\:\:submission_type-selection']");   
                if (submission_method.val() === '2') {
                    attach_warning.show();
                } else {
                    attach_warning.hide();
                } 
            } else {
                attach_warning.hide();
            }
        }
    };

    asnn2editpage.tii_eraterWarning = function() {
        var tii_content = jQuery('#page-replace\\:\\:tii_content_review_area').get(0);
        if (tii_content) {
            // check to see if tii is enabled
            var useTiiOption = jQuery("input[name='page-replace\:\:use_tii']").get(0);

            var eraterCheckbox = jQuery("input[name='page-replace\:\:erater_checkbox']").get(0);

            var eraterWarning = jQuery("#page-replace\\:\\:tii_erater_warning");
            if (useTiiOption && useTiiOption.checked) {
                if (eraterCheckbox && eraterCheckbox.checked) {
                    eraterWarning.show();
                } else {
                    eraterWarning.hide();
                }
            }
        }
    };

})(jQuery, asnn2editpage);

var asnn2listpage = asnn2listpage || {};

(function(jQuery, asnn2listpage) {
    // Variable to track the current thing being removed so we can fade it out.
    var currRemoveAsnnFadeoutElement;

    function tableVersionSetup() {
        jQuery(".assignmentEdits").hide();
        jQuery("tr.movable").hover(function() {
            jQuery(".assignmentEdits", this).show();
        },
        function() {
            jQuery(".assignmentEdits", this).hide();
        });
    }

    function listVersionSetup() {
        jQuery("ul#assignmentList li.row").css('cursor', 'move');
        jQuery("tr.movable").css('cursor', 'move');
    }

    /**
     *  This is the Fluid event that will be called whenever an Asnn row is
     *  dropped to a new location.
     */
    function onFluidReorder(item, requestedPosition, movables) {
        alert("I've been refactored");
    }

    /**
     *  This is the initialization code for setting up the Fluid Reorderer on
     *  the Asnns List Instructor Landing page.
     */
    function setupFluidReorderer() {
        var opts = {
            listeners: {
                afterMove: onFluidReorder
            },
            selectors: {
                movables: ".movable"
            }
        }
        //fluid.reorderList("#asnn-table-body", opts);
        fluid.reorderList("#assignmentList", opts)
    }

    function setupJQuerySortable() {
        jQuery("#assignmentList").sortable({
            items: ">li.row",
            axis: "y",
            containment: "parent",
            update: saveSortables
        });
    }

    function setupRemoveDialog() {
        var removeDialog = jQuery('#remove-asnn-dialog');

        jQuery('#page-replace\\:\\:remove-asnn-button').click(function(event) {
            var queries = new Array();
            queries.push(RSF.renderBinding("RemoveAssignmentCommand.assignmentId", jQuery("#asnn-to-delete-id").html()));
            queries.push(RSF.renderActionBinding("RemoveAssignmentCommand.execute"));
            var body = queries.join("&");
            jQuery.post(document.URL, body);

            // Close the dialog
            asnn2util.closeDialog(removeDialog);

            jQuery("#removed-asnn-msg").clone().show().appendTo(currRemoveAsnnFadeoutElement);
            setTimeout(function() {
                jQuery(currRemoveAsnnFadeoutElement).fadeOut();
            },
            500);

        });

        jQuery('#page-replace\\:\\:cancel-remove-asnn-button').click(function(event) {
            asnn2util.closeDialog(removeDialog);
            // Setting this to empty to be sure the value doesn't get cached and
            // accidentally used for deleting in the future. ASNN-427
            jQuery("#asnn-to-delete-id").html('');
            jQuery("#asnn-to-delete-title").html('');
            jQuery("#asnn-to-delete-due").html('');
            jQuery("#asnn-to-delete-numsubmissions").html('');
        });
    }

    /**
     * This is used from the Instructor Landing page list.html to put up a
     * prompt dialog when the assignment delete link (trashcan) is clicked.
     */
    asnn2listpage.removeAsnnDialog = function(asnnId, fadeOutElement) {
        currRemoveAsnnFadeoutElement = fadeOutElement;

        var removeDialog = jQuery('#remove-asnn-dialog');

        // This Regexp will handle the following cases:
        // http://149.166.143.211:10080/portal/tool/a5a78a8d-9098-4f01-a634-dc93c791a04e/list
        // http://149.166.143.211:10080/portal/tool/a5a78a8d-9098-4f01-a634-dc93c791a04e?panel=Main
        var toolurlPat = /\/portal\/tool\/[^?/] * /;
	if (window.location.pathname.indexOf('/portal/site/') == 0) 
	    toolurlPat = /\/portal\/site\/[^?/]*\/tool\/[^?/] * /;

        var urlprefix = document.location.toString().match(toolurlPat);

        // TODO FIXME This URL is not guaranteed to have the same prefix.Route
        // this through the entity broker
        jQuery.getJSON(urlprefix + '/assignmentinfo/' + asnnId,
        function(data) {
            jQuery("#asnn-to-delete-id").html(asnnId);
            jQuery("#asnn-to-delete-title").html(data['title']);
            jQuery("#asnn-to-delete-due").html(data['due']);
            jQuery("#asnn-to-delete-numsubmissions").html(data['numsubmissions'].toString());
            asnn2util.openDialog(removeDialog);
        });

        return false;
    };

    asnn2listpage.setupAsnnList = function() {
        listVersionSetup();
        setupRemoveDialog();
        //setupFluidReorderer();
        //setupJQuerySortable();
    };
})(jQuery, asnn2listpage);

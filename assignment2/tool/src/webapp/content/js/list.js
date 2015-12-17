var asnn2 = asnn2 || {};

/**
 * Returns a list of Assignment Objects that can be viewed.
 */

/**
 * This copies the properties from the JSON Entity feed into our render tree
 * Assignment that we want to keep available and unchanged.
 */
asnn2.copyDefaultAsnnObjProperties = function(togo, obj) {
  var ditto = ['id','title', 'sortIndex', 'openDate', 'dueDate',
               'requiresSubmission', 'numSubmissions'];
  
  for (var i = 0; i < ditto.length; i++) {
    togo[ditto[i]] = obj[ditto[i]];
  }
};

/**
 * This adds common items to the togo render leaf from the data obj, so that
 * they can be shared between the Assignment List view, and the Reorder Student
 * View. This includes the things like Open, Due, Restricted, etc.
 */
asnn2.addCommonAsnnListReadOnlyRenderObjects = function (togo, obj) {
  if (obj.draft === true) {
    togo.draft = true;
  }
  if (obj.openDateFormatted) {
    togo.opendatelabel = true;
    togo.opentext = obj.openDateFormatted;
  }
  if (obj.dueDateFormatted) {
    togo.duedatelabel = true;
    togo.duetext = obj.dueDateFormatted;
  }
  if (obj.attachments.length > 0) {
    togo.hasAttachments = true;
  }
  if (obj.groups && obj.groups.length > 0) {
    var groupnames = fluid.transform(obj.groups, function(grp,idx) {
      return " "+grp.title;
    });
    togo.groupslabel = true;
    togo.grouptext = groupnames.toString();
  }
  if (obj.gbItemMissing || obj.groupMissing) {
    togo.needsAttention = true;
  }
  if (obj.reviewEnabled) {
    togo.reviewEnabled = true;
  }
};

var pathprefix = '/portal/tool/'+sakai.curPlacement;
if (window.location.pathname.indexOf('/portal/site/') == 0) 
  pathprefix = '/portal/site/'+sakai.curContext+'/tool/'+sakai.curPlacement;

/**
 * This function fits a transform, and expects to take the raw filtered data
 * from the Assignment Entity feed and builds a component tree for the 
 * Assignment List view to be fed into the Fluid Renderer.
 */
asnn2.buildListRenderTreeFromData = function (obj, index) {
  var togo = {};
  
  asnn2.copyDefaultAsnnObjProperties(togo, obj);
  
  asnn2.addCommonAsnnListReadOnlyRenderObjects(togo, obj);
  
  var canEdit = obj.canEdit && obj.canEdit === true;
  var canAdd = obj.canAdd && obj.canAdd === true;
  var canDelete = obj.canDelete && obj.canDelete === true;
  var canEditMatrix = obj.canMatrixLink && obj.canMatrixLink === true;
  var canGrade = obj.canGrade && obj.canGrade === true;
  
  if (asnn2.pageState.canManageSubmissions) {
      if (obj.requiresSubmission === true && canGrade) {
        togo.inAndNewLink = {
	  target: pathprefix+'/viewSubmissions/'+obj.id,
          linktext: obj.inAndNew
        };
      }
      else {
        togo.inAndNew = obj.inAndNew;
      }
  } 
  
  jQuery.ajax({
      type: "GET",
      async: false,
      url: "/direct/assignment2/getMessageBundleText",
      data: { 
          key: "assignment2.list.view_details"
      },
      error: function (jqXHR, textStatus, errorThrown) {
      },
      success: function (data) {
          viewDetailsLink = data;
      }
  });
  
  // render the details link
  var canDetails = true; // for now this is always true
  if (canDetails) {
    togo.detailslink = {
      target: pathprefix+'/view-assignment/'+obj.id+'?fromView=list',
      linktext: viewDetailsLink    
    };
  }
  
  jQuery.ajax({
      type: "GET",
      async: false,
      url: "/direct/assignment2/getMessageBundleText",
      data: { 
          key: "assignment2.list.edit"
      },
      error: function (jqXHR, textStatus, errorThrown) {
      },
      success: function (data) {
          editLink = data;
      }
  });
  
  if (canEdit) {
    togo.editlink = {
      target: pathprefix+'/assignment/'+obj.id,
      linktext: editLink
    };
    
    if (canDetails) {
        togo.sep0 = true;
    }
  }
  
  jQuery.ajax({
      type: "GET",
      async: false,
      url: "/direct/assignment2/getMessageBundleText",
      data: { 
          key: "assignment2.list.duplicate"
      },
      error: function (jqXHR, textStatus, errorThrown) {
      },
      success: function (data) {
          duplicateLink = data;
      }
  });
  
  if (canAdd) {
    togo.duplink = {
      target: pathprefix+'/assignment?duplicatedAssignmentId='+obj.id,
      linktext: duplicateLink
    };
    if (canEdit || canDetails) {
        togo.sep1 = true;
    }
  }
  
  jQuery.ajax({
      type: "GET",
      async: false,
      url: "/direct/assignment2/getMessageBundleText",
      data: { 
          key: "assignment2.list.matrix_links"
      },
      error: function (jqXHR, textStatus, errorThrown) {
      },
      success: function (data) {
          matrixLink = data;
      }
  });
  
  if (canEditMatrix) {
    togo.matrixlink = {
      //target: '/portal/tool/'+sakai.curPlacement+'/TaggableHelperProducer?values=%2Fassignment%2Fa%2Fusedtools%2F2a4f82db-0b4b-4be6-b7cf-fe9c3debcf6a&helperId=osp.matrix.link&keys=activityRef',
      target: pathprefix+'/TaggableHelperProducer?helperId=osp.matrix.link&keys=activityRef&values='+obj.ref,
      linktext: matrixLink
    };
    
    if (canDetails || canEdit || canAdd) {
        togo.sep2 = true;
    }
    
    // this is the editable title
    togo.titleedit = obj.title;
  }
  
  if (obj.graded === true && canGrade) {
      
      jQuery.ajax({
          type: "GET",
          async: false,
          url: "/direct/assignment2/getMessageBundleText",
          data: { 
              key: "assignment2.list.grade"
          },
          error: function (jqXHR, textStatus, errorThrown) {
          },
          success: function (data) {
              gradeLink = data;
          }
      });
      
      togo.gradelink = {
          target: pathprefix+'/viewSubmissions/'+obj.id,
          linktext: gradeLink
      };
      if (canDetails || canEdit || canAdd || canEditMatrix) {
        togo.sep3 = true;
      }
  }
  else if (obj.requiresSubmission === true && canGrade) {
      jQuery.ajax({
          type: "GET",
          async: false,
          url: "/direct/assignment2/getMessageBundleText",
          data: { 
              key: "assignment2.list.provide_feedback"
          },
          error: function (jqXHR, textStatus, errorThrown) {
          },
          success: function (data) {
              feedbackLink = data;
          }
      });
      
      togo.gradelink = {
          target: pathprefix+'/viewSubmissions/'+obj.id,
          linktext: feedbackLink
      };
      if (canDetails || canEdit || canAdd || canEditMatrix) {
          togo.sep3 = true;
      }
  }
  
  if (canDelete) {
      togo.asnncheck = {
         value: false
      };
  }
  
  return togo;
};
  
asnn2.getRawJSONSiteList = function () {
  var togo = {}
  var xmlhttp = jQuery.ajax({
    type: "GET",
    url: "/direct/assignment2/sitelist.json",
    data: {
      'siteid': sakai.curContext,
      'no-cache': true
    },
    async: false,
    success: function (payload) {
      togo = JSON.parse(payload);
    }
  });
  
  /*
   * This doesn't really belong here, maybe we should return this as well as the
   * data feed.
   */
  if (asnn2.pageState && xmlhttp.getResponseHeader('x-asnn2-canDelete') === 'true') {
    // Set up global permissions for rendering remove widget
    asnn2.pageState.canDelete = true;
  }
  
  if (asnn2.pageState && xmlhttp.getResponseHeader('x-asnn2-canManageSubmissions') === 'true') {
      // Set up global permissions for rendering In/New column
      asnn2.pageState.canManageSubmissions = true;
  }
    
  
  return togo;
};

asnn2.getAsnnCompData = function () {
  return fluid.transform(asnn2.getRawJSONSiteList().assignment2_collection, 
      asnn2util.dataFromEntity, asnn2.buildListRenderTreeFromData);
};

asnn2.selectorMap = [
  { selector: ".row", id: "row:" },
  { selector: ".asnnid", id: "id" },
  { selector: ".asnntitle", id: "title" },
  { selector: ".titleedit", id: "title" },
  { selector: ".gradelink", id: "gradelink"},
  { selector: ".editlink", id: "editlink" },
  { selector: ".detailslink", id: "detailslink" },
  { selector: ".matrixlink", id: "matrixlink" },
  { selector: ".duplink", id: "duplink" },
  { selector: ".opendate", id: "opentext" },
  { selector: ".duedate", id: "duetext" },
  { selector: ".groups", id: "grouptext" },
  { selector: ".inAndNew", id: "inAndNew" },
  { selector: ".inAndNewLink", id: "inAndNewLink" },
  { selector: ".attachments", id: "hasAttachments" },
  { selector: ".needsAttention", id: "needsAttention"},
  { selector: ".draft", id: "draft"},
  { selector: ".sep0", id: "sep0"},
  { selector: ".sep1", id: "sep1"},
  { selector: ".sep2", id: "sep2"},
  { selector: ".sep3", id: "sep3"},
  { selector: ".opendatelabel", id: "opendatelabel" },
  { selector: ".duedatelabel", id: "duedatelabel" },
  { selector: ".groupslabel", id: "groupslabel" },
  { selector: ".addlink", id: "addlink" },
  { selector: ".addimage", id: "addimage" },
  { selector: ".asnncheck", id: "asnncheck" },
  { selector: ".reviewEnabled", id: "reviewEnabled" }
];

asnn2.sortMap = [
  { selector: ".titlesort", property: "title" },
  { selector: ".opendatesort", property: "openDate" },
  { selector: ".duedatesort", property: "dueDate" },
  { selector: ".instsort", property: "sortIndex" }
];

/*
 * This tracks the current page state, such as what we're sorting by and in what
 * order, the cached fluid render template, and the current comp data model array.
 */
asnn2.pageState = {
  listTemplate: Object(),
  sortby: "sortIndex",
  sortDir: -1,
  dataArray: [],
  pageModel: {},
  canDelete: false,
  canManageSubmissions: false,
  minPageSize: 5  // This needs to be in sync with the html template currently.�
};

/*
 * Initializes the top sorting links.
 *
 * For sorting -1 is ascending, and 1 is descending.
 */
asnn2.setupSortLinks = function() {
  for (var i in asnn2.sortMap) {
    var item = asnn2.sortMap[i];
    $(item.selector).bind("click", function(sortby) {
      return function (e) {
        /*
         * If we are sorting by a different term, we want to switch the sort direction back
         * to ascending, otherwise we'll swap it from the current value.
         */
        if (asnn2.pageState.sortby === sortby) {
          asnn2.pageState.sortDir = asnn2.pageState.sortDir * -1;
        }
        else {
          asnn2.pageState.sortby = sortby;
          asnn2.pageState.sortDir = -1;
        }

        var newdata = asnn2.pageState.dataArray;
        newdata.sort(function (arec,brec) {
          var a = arec[sortby];
          var b = brec[sortby];
          // make the title sort case insensitive
          if (sortby === 'title') {
              a = a.toLowerCase();
              b = b.toLowerCase();
          }

          return a === b? 0 : ( a > b? -asnn2.pageState.sortDir : asnn2.pageState.sortDir);
        });

        var newsortclass = jQuery(this).attr('class');
        jQuery("."+newsortclass).each(function () {
          jQuery("img", this.parentNode.parentNode).remove();

          if (asnn2.pageState.sortDir < 0) {
            jQuery(this).after('<img src="/library/image/sakai/sortascending.gif" />');
          }
          else {
            jQuery(this).after('<img src="/library/image/sakai/sortdescending.gif" />');
          }
        });

        asnn2.renderAsnnListPage();
      };
    }(item.property));
  }
};

/*
 * The setup functions below each perform some action on the rendered Assignment list,
 * typically for setting up the events necessary for inline editing, reordering, etc.
 * These need to be called each time the assignment list is repainted.
 */

asnn2.setupRemoveCheckboxes = function () {
  $("#checkall").bind("click", function(e) {
    $(".asnncheck").each(function (i) {
      this.checked = e.currentTarget.checked;
    });
  });
};

asnn2.getAsnnObj = function(val, prop) {
  var p = prop || "id";
  for (var i = 0; i < asnn2.pageState.dataArray.length; i++) {
    var next = asnn2.pageState.dataArray[i];
    if (next[p] == val) { // Yes, double equal. Looking at usage in onFinishEdit still for the inline edit
      return asnn2.pageState.dataArray[i];
    }
  }
  return undefined;
};

/*
 *  Set up inline edits
 */
asnn2.setupInlineEdits = function () {

  // if you only have the inline edit on some of the titles
  // but not all, fluid will blow up. to get around this, we are
  // including the read-only title and editable title elements. go
  // through and figure out which one the user can edit and display
  // the editable one instead of the read-only
  jQuery(".asnntitle").each(function (i) {
    var asnnid = $(".asnnid", this.parentNode.parentNode).text();
    var obj = asnn2.getAsnnObj(asnnid);
    if (obj.editlink) {
        // we want to hide the text-only title display and display the editable one
      jQuery(this).hide();
      var editableTitle = jQuery(this).next(".titleedit");
      editableTitle.show();
    }
  });
  
  jQuery.ajax({
      type: "GET",
      async: false,
      url: "/direct/assignment2/getMessageBundleText",
      data: { 
          key: "assignment2.list.rename.tool_tip"
      },
      error: function (jqXHR, textStatus, errorThrown) {
      },
      success: function (data) {
          renameToolTip = data;
      }
  });


var editFields = jQuery("#asnn-list .asnn-title-cell");
  
  jQuery.each(editFields, function(index, value) {
      var that = fluid.inlineEdit(value, {
          selectors : {
          text: ".titleedit"
          },
	      useTooltip: true,
	      tooltipDelay : 500,
	      tooltipText : renameToolTip,
	      listeners: {
              onFinishEdit: function (newValue, oldValue, editNode, viewNode) {
	              var asnnid = $(".asnnid", viewNode.parentNode).text();
	              var existsInGradebook = "false";

		      if (oldValue != newValue) {
                  jQuery.ajax({
                      type: "GET",
                      async: false,
                      url: "/direct/assignment2/"+asnnid+"/isLinkedAssignmentNameInGradebook",
                      data: { 
                          id: asnnid,
                          title: newValue
                      },
                      error: function (jqXHR, textStatus, errorThrown) {
                      },
                      success: function (data) {
                          existsInGradebook = data;
                      }
                  });
	          
                  if (existsInGradebook == "false") {        
                      jQuery.ajax({
                          type: "POST",
                          url: "/direct/assignment2/"+asnnid+"/edit",
                          data: {
                              id: asnnid,
			      csrftoken: $(".csrf").text(),
                              title: newValue
                          },
                          error: function (jqXHR, textStatus, errorThrown) {
                          }
                      }); 
                      
                      asnn2.getAsnnObj(new Number(asnnid))['title'] = newValue;
                  } else {
                      var displayDialog = jQuery("#gradebooknameconflict-asnn-dialog");
                      var displayDialogText = "";
                      var displayDialogButtonText = "";
                      var freeAssignmentName = "";


                      jQuery.ajax({
                          type: "GET",
                          async: false,
                          url: "/direct/assignment2/"+asnnid+"/getFreeAssignmentNameinGradebook",
                          data: { 
                              id: asnnid,
                              title: newValue
                          },
                          error: function (jqXHR, textStatus, errorThrown) {
                          },
                          success: function (data) {
                              freeAssignmentName = data;
                          }
                      });

                      jQuery.ajax({
                          type: "GET",
                          async: false,
                          url: "/direct/assignment2/getMessageBundleText",
                          data: { 
                              key: "assignment2.assignment_rename.duplicate_gradebook_name_error",
                              mbarg0: freeAssignmentName
                          },
                          error: function (jqXHR, textStatus, errorThrown) {
                          },
                          success: function (data) {
                              displayDialogText = data;
                          }
                      });

                      jQuery.ajax({
                          type: "GET",
                          async: false,
                          url: "/direct/assignment2/getMessageBundleText",
                          data: { 
                              key: "assignment2.close"
                          },
                          error: function (jqXHR, textStatus, errorThrown) {
                          },
                          success: function (data) {
                              displayDialogButtonText = data;
                          }
                      });

                      displayDialog.find(".alertMessageInline").html(displayDialogText); 
                      
                      jQuery("#gradebooknameconfict-asnn-dialog-close-button").attr("value", displayDialogButtonText);

                      asnn2util.openDialog(displayDialog);

                      that.updateModelValue(freeAssignmentName);

                      that.finish();
                      return false;
                  } // end else
		      }
              } // end onFinishEdit 
          } // end listeners 

      });    // end fluid.inlineedit()

  }); // end jQuery each	  

}; // edit assn2.setupInlineEdits
	  
	  

/**
 * Refresh all the actions and listeners on the asnn list table that need to be
 * setup each time it is rendered.
 */
asnn2.setupAsnnList = function () {
  asnn2.setupRemoveCheckboxes();
  asnn2.setupInlineEdits();
};

/** End Asnn List Setup Functions **/

/**
 * Performs the actual rendering of the list area using the Fluid Renderer.
 *
 * @param {Array|null} The list of assignments to render, in renderer model form.
 * If not passed in, will use the stored state data.
 */
asnn2.renderAsnnList = function(asnndata) {
  var data = asnndata || asnn2.pageState.dataArray;
  var totalNumAssignments = asnn2.pageState.dataArray.length;
  
  var showSorting = true;
  if (totalNumAssignments <= 1) {
    showSorting = false;
  }
  
  var showPaging = true;
  if (totalNumAssignments <= asnn2.pageState.minPageSize) {
    showPaging = false;
  }
  
  // if the number of assignments displayed on this page is less than the minimum
  // for the pager, hide the top remove button
  var showTopRemoveButton = true;
  if (data.length <= asnn2.pageState.minPageSize) {
      showTopRemoveButton = false;
  }
  
  // we don't render the table or remove buttons and do render some informational
  // text if no assignments exist yet
  var asnnExist = totalNumAssignments > 0;
  asnn2.toggleNoAssignments(asnnExist);
  
  // check to see if we should show the paging arrows and page info. we only
  // display this if there are more assignments than the max per page
  var showPagerArrows = false;
  var showPageNum = false;
  var maxPerPage = asnn2.pageState.pageModel.pageSize;
  if (maxPerPage) {
      showPagerArrows = maxPerPage < totalNumAssignments;
      showPageNum = maxPerPage < totalNumAssignments;
  }
  
  // make sure you call toggleNoAssignments first or it will override
  // your showTopRemoveButton logic
  asnn2.toggleTableControls(showPaging,showSorting,showTopRemoveButton,showPagerArrows,showPageNum);
  
  var dopple = $.extend(true, [], data);

  var treedata = {
    "row:": dopple
  };

  if (asnn2.asnnListTemplate) {
    fluid.reRender(asnn2.asnnListTemplate, jQuery("#asnn-list"), treedata, {cutpoints: asnn2.selectorMap});
  }
  else {
    asnn2.asnnListTemplate = fluid.selfRender(jQuery("#asnn-list"), treedata, {cutpoints: asnn2.selectorMap});
  }
};

/**
 * This will change the display state of the header and footer sorting/paging
 * controls. This is necessary sometimes we want to change whether one of them
 * is displayed based on the number of current assignments.
 * 
 * These parameters should all be boolean values indicating whether the 
 * particular portions should be shown or hidden.
 */
asnn2.toggleTableControls = function(showPager,showSorting,showTopRemove,showPagerArrows,showPageNum) {
  if (showPager === true) {
    jQuery("#top-pager-area").show();
    jQuery("#bottom-pager-area").show();
    // remove the class from the action bar that allows the actions to take up the entire toolbar
    jQuery("#actionBar").removeClass("actionBarNoPager");
  }
  else {
    jQuery("#top-pager-area").hide();
    jQuery("#bottom-pager-area").hide();
    // allow the action bar to take up the entire tool bar
    jQuery("#actionBar").addClass("actionBarNoPager");
  }
  
  if (showSorting === true) {
    jQuery("#top-sort-area").show();
  }
  else {
    jQuery("#top-sort-area").hide();
    jQuery("#bottom-sort-area").hide();
  }
  
  if (showPager === false && showSorting === false) {
    jQuery(".pager-sort-area").hide();
  }
  else {
    jQuery(".pager-sort-area").show();
  }
  
  if (showTopRemove === true) {
      jQuery("#top-remove-button").show();
  } else {
      jQuery("#top-remove-button").hide();
  }
  
  if (showPageNum === true) {
      jQuery('#page-list').show();
      jQuery('#page-list-bottom').show();
  } else {
      jQuery('#page-list').hide();
      jQuery('#page-list-bottom').hide();
  }
}

/**
 * If there are no assignments, we don't want to display the assignments table
 * or the remove buttons. We do display an alternate section that includes
 * an informational message about how to add assignments.
 * @param assignmentsExist
 */
asnn2.toggleNoAssignments = function(assignmentsExist) {
    if (assignmentsExist) {
        jQuery(".removeAsnn").show();
        jQuery("#asnn-list-table").show();
        jQuery("#noAsnn").hide();
    } else {
        jQuery(".removeAsnn").hide();
        jQuery("#asnn-list-table").hide();
        jQuery("#noAsnn").show();
    }
}

/**
 * Used to render the Asnn List using a model from the Fluid Pager. This is designed to be
 * call from the pager listener and use the pages state to rerender the Asnn List.
 * @param {pageModel} A Fluid Page Model
 */
asnn2.renderAsnnListPage = function(newPageModel) {
  var pageModel = newPageModel || asnn2.pageState.pageModel;
  var bounds = asnn2.findPageSlice(pageModel);
  // TODO: Does Javascript array.slice just copy the references or really make new objects?
  var torender = [];
  for (var i = bounds[0]; i < bounds[1]+1; i++) {
    torender.push(asnn2.pageState.dataArray[i]);
  }
  jQuery("#asnn-list").hide();
  asnn2.renderAsnnList(torender);
  asnn2.setupAsnnList();
  
  if (asnn2.pageState.dataArray.length > 0) {
      jQuery("#asnn-list").show();
  }
};

/**
 * Determine the slice to render based off a pageModel.
 * @param {pageModel} Page model from the Fluid Pager. This is the object model you get whenever it
 * changes.
 * @return {Array} An array consisting of the start and end to use. ex. [10,14]
 */
asnn2.findPageSlice = function(pageModel) {
  var start = pageModel.pageIndex * pageModel.pageSize;
  var end = start + Number(pageModel.pageSize) - 1; // This was getting coerced to String addition
  if (end > (pageModel.totalRange-1)) {
    end = pageModel.totalRange-1;
  }
  return [start,end];
};

asnn2.setupRemoveDialog = function() {
  /*
   * Bind the remove button at the bottom of the screen.
   * TODO: Put the confirmation dialog back in.
   */

  jQuery(".removebutton").show();

  var removeDialog = jQuery('#remove-asnn-dialog');

  jQuery(".removebutton").bind("click", function(e) {
    var togo = "";
    jQuery(".asnncheck").each(function (i) {
      if (this.checked) {
        var asnnid = $(".asnnid", this.parentNode.parentNode).text();
        var obj = asnn2.getAsnnObj(asnnid);
        if (obj.duetext) {
          var duedate = obj.duetext;
        }
        else {
          duedate = "";
        }
        var subs = "";
        if (obj.numSubmissions) {
          subs = obj.numSubmissions;
        }

        togo = togo + "<tr><td>"+obj.title.escapeHTML()+"</td><td>"+duedate+"</td><td>"+subs+"</td></tr>";
      }
    });
    jQuery("#asnn-to-delete").html(togo);
    asnn2util.openDialog(removeDialog);
  });

  // The remove dialog
  jQuery('#remove-asnn-button').click( function (event)  {
    var toremove = [];
    jQuery(".asnncheck").each(function (i) {
      if (this.checked) {
        var asnnid = $(".asnnid", this.parentNode.parentNode).text();
        toremove.push(asnnid);
      }
    });
    jQuery.ajax({
      type: "POST",
      url: "/direct/assignment2/deleteAssignments",
      data: {
	"csrftoken" : $(".csrf").text(),
        "delete-ids" : toremove.toString()
      },
      success: function (data) {
        //TODO Properly refire the pager with an updated model rather than just
        // lazily reload the page.
        asnn2util.closeDialog(removeDialog);
        window.location.reload();
      }
      // TODO Handle Failures with a message
    });

  });

  jQuery('#cancel-remove-asnn-button').click( function (event) {
    asnn2util.closeDialog(removeDialog);
    jQuery("#asnn-to-delete").html('');
  });
};

/**
 * An extension of the String class that will escape html characters since
 * we frequently render html instead of just text and can't trust the text
 */
String.prototype.escapeHTML = function () {
    return(
            this.replace(/&/g,'&amp;').
            replace(/>/g,'&gt;').
            replace(/</g,'&lt;').
            replace(/"/g,'&quot;')
    );
};


/**
 * The master init function to be called at the bottom of the HTML page.
 */
asnn2.initAsnnList = function () {
  asnn2.pageState.dataArray = asnn2.getAsnnCompData();

  // I would like to remove this, but am getting a duplicate attribute error currently
  // when I first render it in the pager listener.
  asnn2.renderAsnnList();

  asnn2.setupSortLinks();

  if (asnn2.pageState.canDelete === true) {
    asnn2.setupRemoveDialog();
    jQuery("#checkall").show();
  }
  
  if (asnn2.pageState.canManageSubmissions === false) {
      jQuery(".inNewColumn").hide();
  }

  /*
   * Set up the pagers
   */
  // I'm getting a too much recursion error when using my component tree, using a simple array for now.
  var fakedata = [];
  for (var i = 0; i < asnn2.pageState.dataArray.length; i++) {
    fakedata.push(i);
  }

  var pager = fluid.pager("body", {
    listeners: {
      onModelChange: function (newModel, oldModel) {
        // We need to store the pageModel so that the Sorting links can use it when they need
        // to refresh the list
        asnn2.pageState.pageModel = newModel;
        asnn2.renderAsnnListPage();
      }
    },
    model: {
      pageIndex: undefined,
      pageSize: 200,
      totalRange: undefined
    },
    dataModel: fakedata,
    pagerBar: {type: "fluid.pager.pagerBar"}
  });

  // All these pager buttons are temporary while I'm ripping the page apart 
  // to implement this next set of UI changes.
  
  // First and Last buttons
  jQuery(".fl-pager-firstpage").click(function() {
    pager.events.initiatePageChange.fire({pageIndex: 0});
  });
  
  jQuery(".fl-pager-lastpage").click(function() {
    var model = pager.model;
    // SWG The below is cut n pasted. Should put in a jira request to have it be
    // reusable in the fluid pager and not private since it's a useful utility.
    var last = Math.min(model.totalRange, (model.pageIndex + 1) * model.pageSize);
    pager.events.initiatePageChange.fire({pageIndex: last});
  });
  
  jQuery("#previous-bottom").click(function() {
    pager.events.initiatePageChange.fire({relativePage: -1});
  });
  
  jQuery("#next-bottom").click(function() {
    pager.events.initiatePageChange.fire({relativePage: +1});
  });
    
};

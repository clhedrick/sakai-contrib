package org.sakaiproject.assignment2.tool.handlerhooks;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.assignment2.tool.producers.ListProducer;
import org.sakaiproject.tool.api.ActiveTool;
import org.sakaiproject.tool.api.ActiveToolManager;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolException;

import org.sakaiproject.rsf.helper.HelperViewParameters;
import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.mapping.ShellInfo;
import uk.org.ponder.rsf.components.ELReference;
import uk.org.ponder.rsf.components.ParameterList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIParameter;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterpreter;
import uk.org.ponder.rsf.flow.support.ARI2Processor;
import uk.org.ponder.rsf.preservation.StatePreservationManager;
import uk.org.ponder.rsf.state.TokenStateHolder;
import uk.org.ponder.rsf.view.View;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.view.ViewResolver;
import uk.org.ponder.rsf.viewstate.BaseURLProvider;
import uk.org.ponder.rsf.viewstate.ViewParamUtil;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.stringutil.URLUtil;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * 
 * @author Andrew Thornton
 */
public class HelperHandlerHookBean {

  private static final String TOKEN_STATE_PREFIX = "HelperHandlerHook";
  private static final String POST_HELPER_BINDING_PARAMLIST = HelperViewParameters.POST_HELPER_BINDING + "PARAMLIST";
  private static final String POST_HELPER_ARI2_VIEWPARAMS = HelperViewParameters.POST_HELPER_BINDING + "ARI2RETPARAMS";
  private static final String HELPER_FINISHED_PATH = "/done";
  private static final String IN_HELPER_PATH = "/tool";
  
  /* This is currently being used to record the fact that we are in the Helper, for the sole sake
   * of being able to decide whether or not to disable the MultipartResolver. It's horrible and needs
   * to be replaced with something more robust. Actually, even the current setup has problems. If you go
   * to another part of the tool with a URL, and go back to somewhere that launches the helper, the information
   * is obviously still there.
   * Actually, we could just camp out in the HelperHandlerHook and remove this anytime we return false
   * from handled.
   */
  public static final String IN_HELPER_INDICATOR = TOKEN_STATE_PREFIX + "in-helper"; // Hack

  private HttpServletResponse response;
  private HttpServletRequest request;
  private ViewParameters viewParameters;
  private ViewResolver viewResolver;
  private StatePreservationManager statePreservationManager;
  private TokenStateHolder tsh;
  private ViewStateHandler vsh;
  private BeanModelAlterer bma;
  private BeanLocator beanLocator;
  private ActionResultInterpreter ari;
  private ARI2Processor ariprocessor;
  private ActiveToolManager activeToolManager;
  private BaseURLProvider bup;
  private String[] pathInfo;

  public boolean handle() {
    String viewID = viewParameters.viewID;
    Logger.log.info("Handling view: " + viewID);

    String pathBeyondViewID = "";
    if (pathInfo.length > 1) {
      pathBeyondViewID = URLUtil.toPathInfo(
          (String[]) ArrayUtil.subArray(pathInfo, 1, pathInfo.length));
    }
    if (Logger.log.isInfoEnabled()) {
      Logger.log.info("pathInfo: " + pathInfo + " pathBeyondViewID: "
          + pathBeyondViewID);
    }
    
    if (pathInfo[0].equals("osp.matrix.link.helper")) {
        return handleHelperHelper(pathBeyondViewID);
    }
    
    if (pathBeyondViewID.startsWith(HELPER_FINISHED_PATH)) {
      return handleHelperDone();
    }
    /*
    if (pathBeyondViewID.startsWith(IN_HELPER_PATH)) {
      return handleHelperHelper(pathBeyondViewID);
    }
    */

    return handleHelperStart();
  }

  private boolean handleHelperDone() {
    Logger.log.info("Done handling helper in view: " + viewParameters.viewID);
    
    // Removing hack
    tsh.clearTokenState(IN_HELPER_INDICATOR);

    ELReference elref = (ELReference) tsh.getTokenState(TOKEN_STATE_PREFIX
        + viewParameters.viewID + HelperViewParameters.POST_HELPER_BINDING);
    ParameterList paramlist = (ParameterList) tsh.getTokenState(TOKEN_STATE_PREFIX
        + viewParameters.viewID + POST_HELPER_BINDING_PARAMLIST);
    statePreservationManager.scopeRestore();
    if (paramlist != null) {
      for (int i = 0; i < paramlist.size(); i++) {
        UIParameter param = (UIParameter) paramlist.get(i);
        if (param instanceof UIELBinding) {
          bma.setBeanValue(((UIELBinding)param).valuebinding.value, beanLocator, 
              ((UIELBinding)param).rvalue, null, false);
        }
      }
    }
    String methodBinding = elref == null ? null : elref.value;
    Object beanReturn = null;
    if (methodBinding != null) {
      ShellInfo shells = bma.fetchShells(methodBinding, beanLocator, true);
      beanReturn = bma.invokeBeanMethod(shells, null);
    }
    
    ViewParameters originParams = (ViewParameters) tsh.getTokenState(TOKEN_STATE_PREFIX
        + viewParameters.viewID + POST_HELPER_ARI2_VIEWPARAMS);
        

    ARIResult ariresult = ari.interpretActionResult(viewParameters, beanReturn);
    ariprocessor.interceptActionResult(ariresult, originParams, ariresult);
    String urlToRedirectTo = ViewParamUtil.getAnyFullURL(ariresult.resultingView, vsh);
    
    // ASNN-521 Hardcoding for now
    urlToRedirectTo = ViewParamUtil.getAnyFullURL(new SimpleViewParameters(ListProducer.VIEW_ID), vsh);
    
    try {
      response.sendRedirect(urlToRedirectTo);
    }
    catch (IOException e) {
      throw UniversalRuntimeException.accumulate(e,
          "Error redirecting to url: " + urlToRedirectTo);
    }
    return true;
  }

  private boolean handleHelperHelper(final String pathBeyondViewID) {
    Logger.log.info("Handling helper in view: " + viewParameters.viewID
        + " pathBeyondViewID: " + pathBeyondViewID);

    //String helperId = (String) tsh.getTokenState(TOKEN_STATE_PREFIX
    //    + viewParameters.viewID + DebugHelperViewParameters.HELPER_ID);
    String helperId = "osp.matrix.link";
    
    ActiveTool helperTool = activeToolManager.getActiveTool(helperId);

    String baseUrl = bup.getBaseURL();
    int hostStart = baseUrl.indexOf("://") + 3;
    int hostEnd = baseUrl.indexOf('/', hostStart);

    String contextPath = baseUrl.substring(hostEnd);
    //contextPath += viewParameters.viewID;
    //contextPath += IN_HELPER_PATH;
    contextPath += "osp.matrix.link.helper";
    //String helperPathInfo = pathInfo[0] + "/" + pathInfo[1];
    String helperPathInfo = "";
   
    //System.out.println("NOT REMOVING NATIVE URL!");
    request.removeAttribute(Tool.NATIVE_URL);

    
    // this is the forward call
    try {
      //helperTool.help(request, response, contextPath, helperPathInfo);
      // Test based off debug tracing
       
      if (pathInfo.length == 1) {
          helperTool.help(request, response, contextPath, null);
      }
      else {
          StringBuilder sb = new StringBuilder();
          for (int i = 1; i < pathInfo.length; i++) {
              sb.append("/");
              sb.append(pathInfo[i]);
          }
          helperTool.help(request, response, contextPath, sb.toString());
      } 
    }
    catch (ToolException e) {
      throw UniversalRuntimeException.accumulate(e,
          "ToolException when trying to call help. HelperId: " + helperId
              + " contextPath: " + contextPath + " pathInfo: " + pathInfo);
    }

    return true;
  }

  private boolean handleHelperStart() {
    Logger.log.info("Handling helper start in view: " + viewParameters.viewID);
    View view = new View();
    List producersList = viewResolver.getProducers(viewParameters.viewID);
    if (producersList.size() != 1) {
      throw new IllegalArgumentException(
          "There is not exactly one view producer for the view: "
              + viewParameters.viewID);
    }
    ViewComponentProducer vp = (ViewComponentProducer) producersList.get(0);

    statePreservationManager.scopeRestore();
    vp.fillComponents(view.viewroot, viewParameters, null);
    statePreservationManager.scopePreserve();
    UIOutput helperId = (UIOutput) view.viewroot
        .getComponent(HelperViewParameters.HELPER_ID);
    UICommand helperBinding = (UICommand) view.viewroot
        .getComponent(HelperViewParameters.POST_HELPER_BINDING);
    tsh.putTokenState(TOKEN_STATE_PREFIX + viewParameters.viewID
        + HelperViewParameters.HELPER_ID, helperId.getValue());
    if (helperBinding != null) {
      tsh.putTokenState(TOKEN_STATE_PREFIX + viewParameters.viewID
          + HelperViewParameters.POST_HELPER_BINDING,
          helperBinding.methodbinding);
      //Support for a ParameterList on the UICommand
      if (helperBinding.parameters != null) {
        tsh.putTokenState(TOKEN_STATE_PREFIX + viewParameters.viewID 
            + POST_HELPER_BINDING_PARAMLIST,
            helperBinding.parameters);
      }
    }
    // We need to save these ViewParameters for ActionResultInterceptors
    tsh.putTokenState(TOKEN_STATE_PREFIX + viewParameters.viewID 
        + POST_HELPER_ARI2_VIEWPARAMS,
        viewParameters);

    // Hack to know if we're in the helper
    tsh.putTokenState(IN_HELPER_INDICATOR, IN_HELPER_INDICATOR);
    
    String helperToolPath = bup.getBaseURL() + "osp.matrix.link.helper"; // + viewParameters.viewID
        // SWG + IN_HELPER_PATH;
    tsh.putTokenState(helperId.getValue() + Tool.HELPER_DONE_URL, bup
        .getBaseURL()
        + viewParameters.viewID + HELPER_FINISHED_PATH);

    try {
      response.sendRedirect(helperToolPath);
    }
    catch (IOException e) {
      throw UniversalRuntimeException.accumulate(e,
          "IOException when trying to redirect to helper tool");
    }

    return true;
  }

  public void setActiveToolManager(ActiveToolManager activeToolManager) {
    this.activeToolManager = activeToolManager;
  }

  public void setActionResultInterpreter(ActionResultInterpreter ari) {
    this.ari = ari;
  }

  public void setBeanLocator(BeanLocator beanLocator) {
    this.beanLocator = beanLocator;
  }

  public void setBeanModelAlterer(BeanModelAlterer bma) {
    this.bma = bma;
  }

  public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
    this.request = httpServletRequest;
  }
  
  public HttpServletRequest getHttpServletRequest() {
      return this.request;
    }

  public void setHttpServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  public void setStatePreservationManager(
      StatePreservationManager statePreservationManager) {
    this.statePreservationManager = statePreservationManager;
  }

  public void setTokenStateHolder(TokenStateHolder tsh) {
    this.tsh = tsh;
  }

  public void setViewParameters(ViewParameters viewParameters) {
    this.viewParameters = viewParameters;
  }

  public void setViewResolver(ViewResolver viewResolver) {
    this.viewResolver = viewResolver;
  }

  public void setViewStateHandler(ViewStateHandler vsh) {
    this.vsh = vsh;
  }

  public void setBaseURLProvider(BaseURLProvider bup) {
    this.bup = bup;
  }

  public void setPathInfo(String[] pathInfo) {
    this.pathInfo = pathInfo;
  }
  
  public String[] getPathInfo() {
    return pathInfo;
}
  
  public void setAriProcessor(ARI2Processor ari2p) {
    this.ariprocessor = ari2p;
  }
}

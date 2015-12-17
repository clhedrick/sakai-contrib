package org.sakaiproject.assignment2.tool.handlerhooks;

import org.sakaiproject.assignment2.tool.params.TaggableHelperViewParams;

import org.sakaiproject.rsf.helper.HelperViewParameters;
import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * @author Andrew Thornton
 * 
 */

public class HelperHandlerHook implements HandlerHook {
  private ViewParameters viewParametersProxy;
  private HelperHandlerHookBean hhhb;

  public boolean handle() {
    // ASNN-521
    String[] pathInfo = hhhb.getPathInfo();
    if (pathInfo.length > 0 && pathInfo[0].equals("osp.matrix.link.helper")) {
      return hhhb.handle();
    }
    else if (viewParametersProxy.get() instanceof TaggableHelperViewParams) {
      return hhhb.handle();
    }
    else return false;
  }

  public void setViewParametersProxy(ViewParameters viewParameters) {
    this.viewParametersProxy = viewParameters;
  }

  public void setHelperHandlerHookBean(HelperHandlerHookBean hhhb) {
    this.hhhb = hhhb;
  }

}

package org.sakaiproject.assignment2.tool.handlerhooks;

import javax.servlet.http.HttpServletRequestWrapper;

import org.sakaiproject.rsf.servlet.SakaiHttpServletFactory;

/**
 * This servlet factory is a temporary work around for our Asnn2/Matrix 
 * integration. The issue is that, in the SakaiHttpServletFactory we wrap
 * the ActiveComponent.MyActiveTool.WrappedRequest with our own to perform 
 * some logic.
 * 
 * However, having that ActiveTool WrappedRequest one level lower in the
 * decorated request trips up the odd Sakai Helper system when being used
 * with JSF helpers and doesn't forward properly.
 * 
 * For now, we are just overriding the init method which creates the
 * Request Wrapper. The original init method also messes a bit with the
 * path info under certain circumstances, but Assignments 2 still seems
 * to work so we can prototype with this for now.
 * 
 * @author sgithens
 *
 */
public class Asnn2HttpServletFactory extends SakaiHttpServletFactory {
	public void init() {
	
	}
}

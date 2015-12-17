/*
 * source: Igor Kupczyński via stackoverflow.com community blog:
 * http://stackoverflow.com/questions/738757/how-can-i-tell-what-events-fire-from-gxt
 *
 * 'Maybe someone will find this useful, 
 * I've created utility class for seeing what kind of events 
 * are risen. The idea of course was proposed in accepted answer.'
 *
 * I did :) - JPG
 *
 *
 */

package org.sakaiproject.gradebook.gwt.client.util;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;

/**
 * Class for debugging purposes. Sometimes it is hard to tell what type of event
 * is invoked and when. During debug process you can just do:
 * 
 * EventUtils.attachDebugListeners(c);
 * EventUtils.attachDebugListeners(c, "NAME");
 * 
 * You'll then get information about events as they are invoked.
 * 
 * List of events copied from {@link Events} class.
 * 
 */
public class EventUtils {

    public static void attachDebugListeners(final Component c) {
        attachDebugListeners(c, null);
    }
 
    public static void attachDebugListeners(final Component c, final String msg) {
        for (final EventType type : eventTypeNames.keySet()) {
            c.addListener(type, new Listener<BaseEvent>() {
            	
                public void handleEvent(BaseEvent be) {
                    String typeName = eventTypeNames.get(type);
                    if (msg != null)
                        System.out.print(msg + " -> ");
                    System.out.println(typeName);
                }
            });
        }
    }

    final static Map<EventType, String> eventTypeNames = new HashMap<EventType, String>();
    static {
        eventTypeNames.put(Events.Activate, "Events.Activate");
        eventTypeNames.put(Events.Add, "Events.Add");
        eventTypeNames.put(Events.Adopt, "Events.Adopt");
        eventTypeNames.put(Events.AfterEdit, "Events.AfterEdit");
        eventTypeNames.put(Events.AfterLayout, "Events.AfterLayout");
        eventTypeNames.put(Events.ArrowClick, "Events.ArrowClick");
        eventTypeNames.put(Events.Attach, "Events.Attach");
        eventTypeNames.put(Events.AutoHide, "Events.AutoHide");
        eventTypeNames.put(Events.BeforeAdd, "Events.BeforeAdd");
        eventTypeNames.put(Events.BeforeAdopt, "Events.BeforeAdopt");
        eventTypeNames.put(Events.BeforeBind, "Events.BeforeBind");
        eventTypeNames.put(Events.BeforeCancelEdit, "Events.BeforeCancelEdit");
        eventTypeNames.put(Events.BeforeChange, "Events.BeforeChange");
        eventTypeNames
                .put(Events.BeforeCheckChange, "Events.BeforeCheckChange");
        eventTypeNames.put(Events.BeforeClose, "Events.BeforeClose");
        eventTypeNames.put(Events.BeforeCollapse, "Events.BeforeCollapse");
        eventTypeNames.put(Events.BeforeComplete, "Events.BeforeComplete");
        eventTypeNames.put(Events.BeforeEdit, "Events.BeforeEdit");
        eventTypeNames.put(Events.BeforeExpand, "Events.BeforeExpand");
        eventTypeNames.put(Events.BeforeHide, "Events.BeforeHide");
        eventTypeNames.put(Events.BeforeLayout, "Events.BeforeLayout");
        eventTypeNames.put(Events.BeforeOpen, "Events.BeforeOpen");
        eventTypeNames.put(Events.BeforeOrphan, "Events.BeforeOrphan");
        eventTypeNames.put(Events.BeforeQuery, "Events.BeforeQuery");
        eventTypeNames.put(Events.BeforeRemove, "Events.BeforeRemove");
        eventTypeNames.put(Events.BeforeRender, "Events.BeforeRender");
        eventTypeNames.put(Events.BeforeSelect, "Events.BeforeSelect");
        eventTypeNames.put(Events.BeforeShow, "Events.BeforeShow");
        eventTypeNames.put(Events.BeforeStartEdit, "Events.BeforeStartEdit");
        eventTypeNames.put(Events.BeforeStateRestore,
                "Events.BeforeStateRestore");
        eventTypeNames.put(Events.BeforeStateSave, "Events.BeforeStateSave");
        eventTypeNames.put(Events.BeforeSubmit, "Events.BeforeSubmit");
        eventTypeNames.put(Events.Bind, "Events.Bind");
        eventTypeNames.put(Events.Blur, "Events.Blur");
        eventTypeNames.put(Events.BodyScroll, "Events.BodyScroll");
        eventTypeNames.put(Events.BrowserEvent, "Events.BrowserEvent");
        eventTypeNames.put(Events.CancelEdit, "Events.CancelEdit");
        eventTypeNames.put(Events.CellClick, "Events.CellClick");
        eventTypeNames.put(Events.CellDoubleClick, "Events.CellDoubleClick");
        eventTypeNames.put(Events.CellMouseDown, "Events.CellMouseDown");
        eventTypeNames.put(Events.CellMouseUp, "Events.CellMouseUp");
        eventTypeNames.put(Events.Change, "Events.Change");
        eventTypeNames.put(Events.CheckChange, "Events.CheckChange");
        eventTypeNames.put(Events.CheckChanged, "Events.CheckChanged");
        eventTypeNames.put(Events.Clear, "Events.Clear");
        eventTypeNames.put(Events.Close, "Events.Close");
        eventTypeNames.put(Events.Collapse, "Events.Collapse");
        eventTypeNames.put(Events.ColumnClick, "Events.ColumnClick");
        eventTypeNames.put(Events.ColumnResize, "Events.ColumnResize");
        eventTypeNames.put(Events.Complete, "Events.Complete");
        eventTypeNames.put(Events.ContextMenu, "Events.ContextMenu");
        eventTypeNames.put(Events.Deactivate, "Events.Deactivate");
        eventTypeNames.put(Events.Detach, "Events.Detach");
        eventTypeNames.put(Events.Disable, "Events.Disable");
        eventTypeNames.put(Events.DoubleClick, "Events.DoubleClick");
        eventTypeNames.put(Events.DragCancel, "Events.DragCancel");
        eventTypeNames.put(Events.DragEnd, "Events.DragEnd");
        eventTypeNames.put(Events.DragEnter, "Events.DragEnter");
        eventTypeNames.put(Events.DragFail, "Events.DragFail");
        eventTypeNames.put(Events.DragLeave, "Events.DragLeave");
        eventTypeNames.put(Events.DragMove, "Events.DragMove");
        eventTypeNames.put(Events.DragStart, "Events.DragStart");
        eventTypeNames.put(Events.Drop, "Events.Drop");
        eventTypeNames.put(Events.EffectCancel, "Events.EffectCancel");
        eventTypeNames.put(Events.EffectComplete, "Events.EffectComplete");
        eventTypeNames.put(Events.EffectStart, "Events.EffectStart");
        eventTypeNames.put(Events.Enable, "Events.Enable");
        eventTypeNames.put(Events.Exception, "Events.Exception");
        eventTypeNames.put(Events.Expand, "Events.Expand");
        eventTypeNames.put(Events.Focus, "Events.Focus");
        eventTypeNames.put(Events.HeaderChange, "Events.HeaderChange");
        eventTypeNames.put(Events.HeaderClick, "Events.HeaderClick");
        eventTypeNames
                .put(Events.HeaderContextMenu, "Events.HeaderContextMenu");
        eventTypeNames
                .put(Events.HeaderDoubleClick, "Events.HeaderDoubleClick");
        eventTypeNames.put(Events.HeaderMouseDown, "Events.HeaderMouseDown");
        eventTypeNames.put(Events.HiddenChange, "Events.HiddenChange");
        eventTypeNames.put(Events.Hide, "Events.Hide");
        eventTypeNames.put(Events.Invalid, "Events.Invalid");
        eventTypeNames.put(Events.KeyDown, "Events.KeyDown");
        eventTypeNames.put(Events.KeyPress, "Events.KeyPress");
        eventTypeNames.put(Events.KeyUp, "Events.KeyUp");
        eventTypeNames.put(Events.LiveGridViewUpdate,
                "Events.LiveGridViewUpdate");
        eventTypeNames.put(Events.Maximize, "Events.Maximize");
        eventTypeNames.put(Events.MenuHide, "Events.MenuHide");
        eventTypeNames.put(Events.MenuShow, "Events.MenuShow");
        eventTypeNames.put(Events.Minimize, "Events.Minimize");
        eventTypeNames.put(Events.Move, "Events.Move");
        eventTypeNames.put(Events.OnBlur, "Events.OnBlur");
        eventTypeNames.put(Events.OnChange, "Events.OnChange");
        eventTypeNames.put(Events.OnClick, "Events.OnClick");
        eventTypeNames.put(Events.OnContextMenu, "Events.OnContextMenu");
        eventTypeNames.put(Events.OnDoubleClick, "Events.OnDoubleClick");
        eventTypeNames.put(Events.OnError, "Events.OnError");
        eventTypeNames.put(Events.OnFocus, "Events.OnFocus");
        eventTypeNames.put(Events.OnKeyDown, "Events.OnKeyDown");
        eventTypeNames.put(Events.OnKeyPress, "Events.OnKeyPress");
        eventTypeNames.put(Events.OnKeyUp, "Events.OnKeyUp");
        eventTypeNames.put(Events.OnLoad, "Events.OnLoad");
        eventTypeNames.put(Events.OnLoseCapture, "Events.OnLoseCapture");
        eventTypeNames.put(Events.OnMouseDown, "Events.OnMouseDown");
        eventTypeNames.put(Events.OnMouseMove, "Events.OnMouseMove");
        eventTypeNames.put(Events.OnMouseOut, "Events.OnMouseOut");
        eventTypeNames.put(Events.OnMouseOver, "Events.OnMouseOver");
        eventTypeNames.put(Events.OnMouseUp, "Events.OnMouseUp");
        eventTypeNames.put(Events.OnMouseWheel, "Events.OnMouseWheel");
        eventTypeNames.put(Events.OnScroll, "Events.OnScroll");
        eventTypeNames.put(Events.Open, "Events.Open");
        eventTypeNames.put(Events.Orphan, "Events.Orphan");
        eventTypeNames.put(Events.Ready, "Events.Ready");
        eventTypeNames.put(Events.Refresh, "Events.Refresh");
        eventTypeNames.put(Events.Register, "Events.Register");
        eventTypeNames.put(Events.Remove, "Events.Remove");
        eventTypeNames.put(Events.Render, "Events.Render");
        eventTypeNames.put(Events.Resize, "Events.Resize");
        eventTypeNames.put(Events.ResizeEnd, "Events.ResizeEnd");
        eventTypeNames.put(Events.ResizeStart, "Events.ResizeStart");
        eventTypeNames.put(Events.Restore, "Events.Restore");
        eventTypeNames.put(Events.RowClick, "Events.RowClick");
        eventTypeNames.put(Events.RowDoubleClick, "Events.RowDoubleClick");
        eventTypeNames.put(Events.RowMouseDown, "Events.RowMouseDown");
        eventTypeNames.put(Events.RowMouseUp, "Events.RowMouseUp");
        eventTypeNames.put(Events.RowUpdated, "Events.RowUpdated");
        eventTypeNames.put(Events.Scroll, "Events.Scroll");
        eventTypeNames.put(Events.Select, "Events.Select");
        eventTypeNames.put(Events.SelectionChange, "Events.SelectionChange");
        eventTypeNames.put(Events.Show, "Events.Show");
        eventTypeNames.put(Events.SortChange, "Events.SortChange");
        eventTypeNames.put(Events.SpecialKey, "Events.SpecialKey");
        eventTypeNames.put(Events.StartEdit, "Events.StartEdit");
        eventTypeNames.put(Events.StateChange, "Events.StateChange");
        eventTypeNames.put(Events.StateRestore, "Events.StateRestore");
        eventTypeNames.put(Events.StateSave, "Events.StateSave");
        eventTypeNames.put(Events.Submit, "Events.Submit");
        eventTypeNames.put(Events.Toggle, "Events.Toggle");
        eventTypeNames.put(Events.TriggerClick, "Events.TriggerClick");
        eventTypeNames.put(Events.TwinTriggerClick, "Events.TwinTriggerClick");
        eventTypeNames.put(Events.UnBind, "Events.UnBind");
        eventTypeNames.put(Events.Unregister, "Events.Unregister");
        eventTypeNames.put(Events.Update, "Events.Update");
        eventTypeNames.put(Events.Valid, "Events.Valid");
        eventTypeNames.put(Events.ValidateDrop, "Events.ValidateDrop");
        eventTypeNames.put(Events.ValidateEdit, "Events.ValidateEdit");
        eventTypeNames.put(Events.ViewReady, "Events.ViewReady");
    }
}

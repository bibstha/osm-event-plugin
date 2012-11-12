package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;

@SuppressWarnings("serial")
public class EventListDialog extends ExtendedDialog
{
    private HashMap<String, Component> componentMap;
    protected EventPrimitive eventPrimitive;
    protected List<EventListener> listeners = new ArrayList<EventListener>();
    private static final String[] buttonTexts = new String[] { tr("Edit"), tr("Delete"), tr("New"), tr("Save"),
	    tr("Cancel") };
    protected JPanel panel = new JPanel(new GridBagLayout());
    private String currentAction = null;

    public EventListDialog(EventPrimitive eventPrimitive)
    {
	super(Main.parent, "List of Events", buttonTexts);

	this.eventPrimitive = eventPrimitive;

	loadUI();
	createComponentMap();

	setContent(panel);
	setupDialog();
	// showDialog();
    }

    @Override
    protected void buttonAction(int buttonIndex, ActionEvent evt)
    {
	if (buttonIndex == 0)
	{
	    JList<Integer> eventEntityList = (JList<Integer>) getComponentByName("entitylist");
	    if (eventEntityList.getSelectedValuesList().isEmpty())
	    {
		return;
	    }
	    Integer selectedNumber = eventEntityList.getSelectedValue();
	    final EventEntity eventEntity = eventPrimitive.getEventMap().get(selectedNumber);
	    EventTagDialog dlgEventTag = new EventTagDialog(eventEntity);
	    dlgEventTag.addEventListener(new EventListener()
	    {
		@Override
		public void notify(String eventType)
		{
		    if (eventType.equals("save"))
		    {
			// We consider the passed eventEntity has been
			// updated
			EventListDialog.this.currentAction = "save";
		    }
		    else if (eventType.equals("cancel"))
		    {
			EventListDialog.this.currentAction = "cancel";
		    }
		    else
		    {
			EventListDialog.this.currentAction = null;
		    }
		}
	    });
	    dlgEventTag.showDialog();
	    if (currentAction == "save")
	    {
		// Do nothing, later may be update the display
	    }
	}
	else if (buttonIndex == 1)
	{
	    // Delete
	    deleteAction();
	}
	else if (buttonIndex == 2)
	{
	    // New
	    createAction();
	}
	else if (buttonIndex == 3)
	{
	    // Save
	    for (EventListener listener : this.listeners)
	    {
		listener.notify("save");
	    }
	    setVisible(false);
	}
	else if (buttonIndex == 4)
	{
	    // Cancel
	    setVisible(false);
	}
    }

    /**
     * Renders the main user interface
     */
    protected void loadUI()
    {
	final JCheckBox chkboxIsEvent = new JCheckBox("Is Event");
	chkboxIsEvent.setName("isevent");
	chkboxIsEvent.setSelected(eventPrimitive.isEvent());
	chkboxIsEvent.addChangeListener(new ChangeListener()
	{
	    @Override
	    public void stateChanged(ChangeEvent arg0)
	    {
		eventPrimitive.setIsEvent(chkboxIsEvent.isSelected());
	    }
	});

	DefaultListModel<Integer> listModel = new DefaultListModel<Integer>();
	final JList<Integer> eventEntityList = new JList<Integer>(listModel);
	loadEventEntityList(eventEntityList);
	eventEntityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	eventEntityList.setName("entitylist");

	panel.add(chkboxIsEvent);
	panel.add(eventEntityList);
    }

    protected void updateUI()
    {
	JList<Integer> jlList = (JList<Integer>) getComponentByName("entitylist");
	loadEventEntityList(jlList);
    }

    protected void loadEventEntityList(JList<Integer> list)
    {
	DefaultListModel<Integer> listModel = (DefaultListModel) list.getModel();
	listModel.clear();

	final Map<Integer, EventEntity> eventMap = eventPrimitive.getEventMap();
	if (!eventMap.isEmpty())
	{
	    for (Integer i : eventMap.keySet())
	    {
		listModel.addElement(i);
	    }
	}
    }

    protected EventListDialog addEventListener(EventListener listener)
    {
	this.listeners.add(listener);
	return this;
    }

    protected void createAction()
    {
	final EventEntity eventEntity = new EventEntity();
	EventTagDialog dlgEventTag = new EventTagDialog(eventEntity);
	dlgEventTag.addEventListener(new EventListener()
	{
	    @Override
	    public void notify(String eventType)
	    {
		if (eventType.equals("save"))
		{
		    // We consider the passed eventEntity has been updated
		    EventListDialog.this.currentAction = "save";
		}
		else if (eventType.equals("cancel"))
		{
		    EventListDialog.this.currentAction = "cancel";
		}
		else
		{
		    EventListDialog.this.currentAction = null;
		}
	    }
	});
	dlgEventTag.showDialog();
	if (currentAction.equals("save"))
	{
	    Integer nextEventNumber = eventPrimitive.getNextHighestEventNumber();
	    eventPrimitive.getEventMap().put(nextEventNumber, eventEntity);
	    eventPrimitive.setHighestEventNumber(nextEventNumber);
	    currentAction = null;

	    updateUI();
	}
    }

    private void deleteAction()
    {
	JList<Integer> eventEntityList = (JList<Integer>) getComponentByName("entitylist");
	if (eventEntityList.getSelectedValuesList().isEmpty())
	{
	    return;
	}
	Integer selectedNumber = eventEntityList.getSelectedValue();
	eventPrimitive.getEventMap().remove(selectedNumber);
    }

    private void createComponentMap()
    {
	componentMap = new HashMap<String, Component>();
	Component[] components = panel.getComponents();
	for (int i = 0; i < components.length; i++)
	{
	    componentMap.put(components[i].getName(), components[i]);
	}
    }

    public Component getComponentByName(String name)
    {
	if (componentMap.containsKey(name))
	{
	    return componentMap.get(name);
	}
	else
	    return null;
    }
}

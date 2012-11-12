package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

@SuppressWarnings("serial")
public class EventPresetEditAction extends JosmAction
{
    private String lastAction = null;

    public EventPresetEditAction()
    {
	super(tr("Event Add/Edit"), null, tr("Add or edit events related with the node"), null, true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
	if (!isEnabled())
	    return;

	if (getCurrentDataSet() == null)
	    return;

	Collection<Node> nodes = getCurrentDataSet().getSelectedNodes();
	Collection<Way> ways = getCurrentDataSet().getSelectedWays();
	if (nodes.size() + ways.size() != 1)
	{
	    System.out.println("Multiple primites selected");
	    return;
	}

	OsmPrimitive sel;
	if (nodes.size() == 1)
	{
	    sel = nodes.iterator().next();
	}
	else
	{
	    sel = ways.iterator().next();
	}

	EventPrimitive primitive = Utils.createEventPrimitive(sel);
	EventListDialog dlgEventList = new EventListDialog(primitive);

	EventListener listener = getEventListener();
	dlgEventList.addEventListener(listener);
	dlgEventList.showDialog();

	if (lastAction != null && !lastAction.equals(""))
	{
	    performAction(sel, primitive);
	}
    }

    private EventListener getEventListener()
    {
	return new EventListener()
	{
	    @Override
	    public void notify(String eventType)
	    {
		if (eventType.equals("save"))
		{
		    lastAction = "save";
		}
		else
		{
		    lastAction = null;
		}
	    }
	};
    }

    private void performAction(OsmPrimitive sel, EventPrimitive primitive)
    {
	if (lastAction == "save")
	{
	    if (primitive.isEvent())
	    {
		sel.put("event", "yes");
	    }
	    else
	    {
		sel.put("event", null);
	    }

	    Map<Integer, EventEntity> eventMap = primitive.getEventMap();
	    if (eventMap != null && !eventMap.isEmpty())
	    {
		for (Integer i : eventMap.keySet())
		{
		    String keyPrefix = "event:" + i + ":";
		    sel.put(keyPrefix + "name", eventMap.get(i).getName());
		    sel.put(keyPrefix + "category", eventMap.get(i).getCategory());
		    sel.put(keyPrefix + "subcategory", eventMap.get(i).getSubCategory());
		    sel.put(keyPrefix + "organization", eventMap.get(i).getOrganization());
		    sel.put(keyPrefix + "startdate", eventMap.get(i).getStartDate());
		    sel.put(keyPrefix + "enddate", eventMap.get(i).getEndDate());
		    sel.put(keyPrefix + "url", eventMap.get(i).getUrl());
		    sel.put(keyPrefix + "num_participants", eventMap.get(i).getNumOfParticipants());
		    sel.put(keyPrefix + "howoften", eventMap.get(i).getHowOften());
		    sel.put(keyPrefix + "howoften_other", eventMap.get(i).getHowOftenOther());
		    sel.put(keyPrefix + "comment", eventMap.get(i).getComment());
		}
	    }
	}
    }
}

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
		    saveOsmPrimitive(sel, keyPrefix + "name", eventMap.get(i).getName());
		    saveOsmPrimitive(sel, keyPrefix + "category", eventMap.get(i).getCategory());
		    saveOsmPrimitive(sel, keyPrefix + "subcategory", eventMap.get(i).getSubCategory());
		    saveOsmPrimitive(sel, keyPrefix + "organization", eventMap.get(i).getOrganization());
		    saveOsmPrimitive(sel, keyPrefix + "startdate", eventMap.get(i).getStartDate());
		    saveOsmPrimitive(sel, keyPrefix + "enddate", eventMap.get(i).getEndDate());
		    saveOsmPrimitive(sel, keyPrefix + "url", eventMap.get(i).getUrl());
		    saveOsmPrimitive(sel, keyPrefix + "num_participants", eventMap.get(i).getNumOfParticipants());
		    saveOsmPrimitive(sel, keyPrefix + "howoften", eventMap.get(i).getHowOften());
		    saveOsmPrimitive(sel, keyPrefix + "howoften_other", eventMap.get(i).getHowOftenOther());
		    saveOsmPrimitive(sel, keyPrefix + "comment", eventMap.get(i).getComment());
		}
	    }
	}
    }

    private void saveOsmPrimitive(OsmPrimitive sel, String tag, String value)
    {
	if (value != null && value.equals(""))
	{
	    value = null;
	}
	sel.put(tag, value);
    }
}

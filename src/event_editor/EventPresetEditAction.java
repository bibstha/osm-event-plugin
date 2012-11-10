package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.util.Collection;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

@SuppressWarnings("serial")
public class EventPresetEditAction extends JosmAction
{

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
	// EventListDialog dialog = new EventListDialog(sel.getKeys());
    }

}

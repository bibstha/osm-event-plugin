package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JList;
import javax.swing.JPanel;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;

@SuppressWarnings("serial")
public class EventListDialog extends ExtendedDialog {

    private static String EVENTKEY = "event";

    private static final String[] buttonTexts = new String[] { tr("OK") };
    protected JPanel panel = new JPanel(new GridBagLayout());

    protected Map<String, String> tags;
    protected Map<Integer, Map<String, String>> eventTagsMap = new HashMap<Integer, Map<String, String>>();

    public EventListDialog(final Map<String, String> tags) {
	super(Main.parent, "List of Events", buttonTexts);

	this.tags = tags;

	filterEventTags();
	panel.add(loadList());

	setContent(panel);
	setupDialog();
	showDialog();
    }

    protected JList<Integer> loadList() {
	JList<Integer> list;
	if (eventTagsMap != null && !eventTagsMap.isEmpty()) {
	    list = new JList<Integer>(eventTagsMap.keySet().toArray(
		    new Integer[0]));
	    list.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent evt) {
		    JList list = (JList) evt.getSource();
		    if (evt.getClickCount() == 2) {
			int index = list.locationToIndex(evt.getPoint());
			Map<String, String> tags = eventTagsMap.get(list
				.getSelectedValue());
			EventTagDialog tagDialog = new EventTagDialog(tags);
		    }
		}
	    });
	} else {
	    list = new JList<Integer>();
	}
	return list;
    }

    protected void filterEventTags() {
	Set<String> keys = tags.keySet();
	if (!keys.contains(EVENTKEY)
		|| !tags.get(EVENTKEY).trim().equals("yes")) {
	    return;
	}

	for (String key : keys) {
	    int eventNumber = Utils.findEventNumber(key);
	    if (eventNumber < 0) {
		System.out.println(key + " is not event Tag");
		continue;
	    }

	    Map<String, String> eventTags;
	    if (null == eventTagsMap.get(eventNumber)) {
		eventTags = new HashMap<String, String>();
		eventTagsMap.put(eventNumber, eventTags);
	    } else {
		eventTags = eventTagsMap.get(eventNumber);
	    }

	    if (!eventTags.containsKey(key)) {
		eventTags.put(key, tags.get(key));
	    }

	    System.out.println("Saved tag " + key + "for event number : "
		    + eventNumber);
	}
    }

}

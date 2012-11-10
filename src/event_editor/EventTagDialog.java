package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.JPanel;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;

public class EventTagDialog extends ExtendedDialog
{

    private static final String[] buttonTexts = new String[] { tr("Save"), tr("Cancel") };
    protected JPanel panel = new JPanel(new GridBagLayout());
    private final Map<String, String> eventTags;

    public EventTagDialog(Map<String, String> eventTags)
    {
	super(Main.parent, "Event Tags", buttonTexts);
	this.eventTags = eventTags;

	setContent(panel);
	setupDialog();
	showDialog();
    }

}

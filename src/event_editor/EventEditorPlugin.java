package event_editor;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.MapView.EditLayerChangeListener;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class EventEditorPlugin extends Plugin
{

    public EventEditorPlugin(PluginInformation info)
    {
	super(info);
	Main.main.menu.presetsMenu.addSeparator();
	MainMenu.add(Main.main.menu.presetsMenu, new EventPresetEditAction());

	MapView.addEditLayerChangeListener(new EditLayerChangeListener()
	{
	    @Override
	    public void editLayerChanged(OsmDataLayer oldLayer, OsmDataLayer newLayer)
	    {
		if (newLayer != null)
		{
		    Main.map.addToggleDialog(new EventRelatedSelectionDialog(Main.map));
		}
	    }
	});
    }
}

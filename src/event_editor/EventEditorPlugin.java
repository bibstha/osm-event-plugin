package event_editor;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class EventEditorPlugin extends Plugin
{

    public EventEditorPlugin(PluginInformation info)
    {
	super(info);
	Main.main.menu.presetsMenu.addSeparator();
	MainMenu.add(Main.main.menu.presetsMenu, new EventPresetEditAction());
    }
}

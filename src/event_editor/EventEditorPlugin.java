package event_editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
		if (!Main.pref.getBoolean("trustosm.jarLibsExtracted"))
		{
			Main.pref.put("event_editor.jarLibsExtracted", extractFiles("event_editor", "lib"));
			Main.pref.put("event_editor.jarLibsExtracted", extractFiles("event_editor", "resources"));
		}

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

	public static boolean extractFiles(String pluginname, String extractDir)
	{
		try
		{
			if (extractDir == null)
				extractDir = "lib";
			JarFile jar = new JarFile(Main.pref.getPluginsDirectory().getPath() + "/" + pluginname + ".jar");
			Enumeration<JarEntry> entries = jar.entries();
			InputStream is;
			FileOutputStream fos;
			File file;
			while (entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (name.startsWith(extractDir + "/") && !entry.isDirectory())
				{
					System.out.println(Main.pref.getPluginsDirectory().getPath() + "/" + pluginname + "/" + name);
					file = new File(Main.pref.getPluginsDirectory().getPath() + "/" + pluginname + "/" + name);
					file.getParentFile().mkdirs();
					is = jar.getInputStream(entry);
					fos = new FileOutputStream(file);
					while (is.available() > 0)
					{ // write contents of 'is' to 'fos'
						fos.write(is.read());
					}
					fos.close();
					is.close();
				}
			}
			return true;

		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

	}
}

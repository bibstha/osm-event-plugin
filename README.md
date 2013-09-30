# Event Editor (event_editor.jar)

EventEditor is a plugin that provides a GUI to facilitate entering event database into the OSM through JOSM.

## Installation Instruction

First download *josm* along with all its plugins.

    svn checkout http://svn.openstreetmap.org/applications/editors/josm/
    cd josm/core
    ant dist
    cd ../plugins
    git clone git@bitbucket.org:polous/osm-event-plugin.git event_editor
    cd event_editor
    ant dist

This will create an editor *event_editor.jar* in *josm/dist* folder.

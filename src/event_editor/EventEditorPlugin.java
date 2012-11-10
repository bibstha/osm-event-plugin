package event_editor;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

public class EventEditorPlugin extends Plugin {

    public EventEditorPlugin(PluginInformation info) {
	super(info);
	Main.main.menu.presetsMenu.addSeparator();
	MainMenu.add(Main.main.menu.presetsMenu, new EventPresetEditAction());

	// UploadAction.registerUploadHook(new UploadHook() {
	//
	// @Override
	// public boolean checkUpload(APIDataSet apiDataSet) {
	// List<OsmPrimitive> primitivesToAdd = apiDataSet
	// .getPrimitivesToAdd();
	// for (OsmPrimitive primitive : primitivesToAdd) {
	// System.out.println(primitive.toString());
	// }
	// EventUploadReview reviewer = new EventUploadReview();
	// reviewer.showReview(apiDataSet);
	//
	// System.out.println(apiDataSet.getSize());
	// return false;
	// }
	// });
    }

    // public class EventUploadReview {
    // public void showReview(APIDataSet apiDataSet) {
    // String information = "";
    // List<OsmPrimitive> primitivesToAdd = apiDataSet
    // .getPrimitivesToAdd();
    // information += formatReviewInfoList(primitivesToAdd,
    // "Primitives To Add");
    //
    // List<OsmPrimitive> primitvesToDelete = apiDataSet
    // .getPrimitivesToDelete();
    // information += formatReviewInfoList(primitvesToDelete,
    // "Primitives to Delete");
    //
    // List<OsmPrimitive> primitivesToUpdate = apiDataSet
    // .getPrimitivesToUpdate();
    // information += formatReviewInfoList(primitivesToUpdate,
    // "Primitives to Update");
    //
    // information = "<html>" + information + "</html>";
    //
    // final JDialog dialog = new JDialog();
    // dialog.add(new JLabel(information), BorderLayout.NORTH);
    //
    // JButton btnUpload = new JButton("Upload");
    // btnUpload.addActionListener(new ActionListener() {
    //
    // @Override
    // public void actionPerformed(ActionEvent arg0) {
    //
    // }
    // });
    //
    // JButton btnClose = new JButton("Close");
    // btnClose.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // dialog.setVisible(false);
    // }
    // });
    //
    // dialog.add(btnUpload, BorderLayout.CENTER);
    // dialog.add(btnClose, BorderLayout.LINE_END);
    //
    // dialog.pack();
    // dialog.setVisible(true);
    // }
    //
    // public String formatReviewInfoList(List<OsmPrimitive> primitives,
    // String title) {
    // String ret = title;
    // if (primitives.isEmpty()) {
    // ret += "<ul>Empty</ul>";
    // } else {
    // String innerHtmlList = "";
    // for (OsmPrimitive primitive : primitives) {
    // innerHtmlList += "<li>" + primitive.toString() + "</li>";
    // }
    // ret += "<ol>" + innerHtmlList + "</ol>";
    // }
    // return ret;
    // }
    // }
}

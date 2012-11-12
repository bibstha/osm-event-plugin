package event_editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;

public class EventTagDialog extends ExtendedDialog
{
    protected EventEntity eventEntity;
    protected List<EventListener> eventListeners = new ArrayList<EventListener>();

    private static final String[] buttonTexts = new String[] { tr("Save"), tr("Cancel") };
    protected JPanel panel;

    Map<String, String> catLabel = new HashMap<String, String>();
    Map<String, List<String>> catMap = new HashMap<String, List<String>>();

    private HashMap<String, Component> componentMap;

    public EventTagDialog(EventEntity eventEntity)
    {
	super(Main.parent, "Event Tags", buttonTexts);
	this.eventEntity = eventEntity;

	initCategories();
	loadUI();

	setContent(panel);
	setupDialog();
    }

    @Override
    protected void buttonAction(int buttonIndex, ActionEvent evt)
    {
	if (buttonIndex == 0)
	{
	    // Save Clicked
	    for (EventListener listener : this.eventListeners)
	    {
		updateEventEntity();
		listener.notify("save");
		setVisible(false);
	    }
	}
	else if (buttonIndex == 1)
	{
	    for (EventListener listener : this.eventListeners)
	    {
		listener.notify("cancel");
		setVisible(false);
	    }
	}
    }

    public void addEventListener(EventListener listener)
    {
	this.eventListeners.add(listener);
    }

    /**
     * Reference
     * http://docs.oracle.com/javase/tutorial/uiswing/layout/spring.html
     */
    protected void loadUI()
    {
	panel = loadPanel(this.eventEntity);
	createComponentMap();
    }

    private JPanel loadPanel(EventEntity entity)
    {
	SpringLayout layout = new SpringLayout();
	JPanel panel = new JPanel(layout);
	// panel.setSize(600, 400);

	JLabel jlName = new JLabel(tr("Name"), JLabel.TRAILING);
	final JTextField jtName = new JTextField(30);
	jtName.setText(eventEntity.getName());
	jtName.setName("name");
	jlName.setLabelFor(jtName);
	panel.add(jlName);
	panel.add(jtName);

	JLabel jlCategory = new JLabel(tr("Category"), JLabel.TRAILING);
	final JComboBox<String> jcCategory = new JComboBox<String>();
	jcCategory.setEditable(true);
	jcCategory.setName("category");
	for (String catPrimary : catMap.keySet())
	{
	    jcCategory.addItem(catPrimary);
	}
	jcCategory.setSelectedItem(eventEntity.getCategory());
	jlCategory.setLabelFor(jcCategory);
	panel.add(jlCategory);
	panel.add(jcCategory);

	JLabel jlSubCategory = new JLabel(tr("SubCategory"), JLabel.TRAILING);
	final JComboBox<String> jcSubCategory = new JComboBox<String>();
	jcSubCategory.setName("subcategory");
	jcSubCategory.setEditable(true);
	jcSubCategory.setSelectedItem(eventEntity.getSubCategory());
	panel.add(jlSubCategory);
	panel.add(jcSubCategory);

	linkCateories(jcCategory, jcSubCategory);

	JLabel jlOrg = new JLabel(tr("Organization"), JLabel.TRAILING);
	final JTextField jtOrg = new JTextField();
	jtOrg.setName("org");
	jtOrg.setText(eventEntity.getOrganization());
	jlOrg.setLabelFor(jtOrg);
	panel.add(jlOrg);
	panel.add(jtOrg);

	JLabel jlStartDate = new JLabel(tr("Start Date"), JLabel.TRAILING);
	JTextField jtStartDate = new JTextField();
	jtStartDate.setName("startdate");
	jtStartDate.setText(eventEntity.getStartDate());
	jlStartDate.setLabelFor(jtStartDate);
	panel.add(jlStartDate);
	panel.add(jtStartDate);

	JLabel jlEndDate = new JLabel(tr("End Date"), JLabel.TRAILING);
	JTextField jtEndDate = new JTextField();
	jtEndDate.setName("enddate");
	jtEndDate.setText(eventEntity.getEndDate());
	jlEndDate.setLabelFor(jtEndDate);
	panel.add(jlEndDate);
	panel.add(jtEndDate);

	JLabel jlLink = new JLabel(tr("URL Link"), JLabel.TRAILING);
	final JTextArea jtLink = new JTextArea(3, 0);
	jtLink.setName("link");
	jtLink.setText(eventEntity.getUrl());
	jlLink.setLabelFor(jtLink);
	panel.add(jlLink);
	panel.add(jtLink);

	JLabel jlNumParticipants = new JLabel(tr("Number of Participants"), JLabel.TRAILING);
	final JComboBox<String> jcNumParticipants = new JComboBox<String>();
	String[] optNumParticipants = { "less than 50", "50 to 200", "200 to 1000", "1000 to 10000", "more than 10000" };
	jcNumParticipants.setName("numparticipants");
	// TODO assign value
	for (String optNumParticipant : optNumParticipants)
	{
	    jcNumParticipants.addItem(optNumParticipant);
	}
	jcNumParticipants.setEditable(true);
	jcNumParticipants.setSelectedItem(eventEntity.getNumOfParticipants());
	jlNumParticipants.setLabelFor(jcNumParticipants);
	panel.add(jlNumParticipants);
	panel.add(jcNumParticipants);

	JLabel jlHowOften = new JLabel(tr("How often"), JLabel.TRAILING);
	final JComboBox<String> jcHowOften = new JComboBox<String>();
	String[] howOftenOptions = { "Yearly", "Monthly", "Bimonthly", "Weekly", "Biweekly", "Daily", "Once", "Other" };
	jcHowOften.setName("howoften");
	// TODO selected value
	for (String howOftenOption : howOftenOptions)
	{
	    jcHowOften.addItem(howOftenOption);
	}
	jcHowOften.setEditable(true);
	jcHowOften.setSelectedItem(eventEntity.getHowOften());
	jlHowOften.setLabelFor(jcHowOften);
	panel.add(jlHowOften);
	panel.add(jcHowOften);

	JLabel jlHowOftenChooseOther = new JLabel(tr("If other, please write"), JLabel.TRAILING);
	final JTextField jtHowOftenChooseOther = new JTextField();
	jtHowOftenChooseOther.setName("howoftenchooseother");
	jtHowOftenChooseOther.setText(eventEntity.getHowOftenOther());
	jlHowOftenChooseOther.setLabelFor(jtHowOftenChooseOther);
	panel.add(jlHowOftenChooseOther);
	panel.add(jtHowOftenChooseOther);

	JLabel jlComment = new JLabel(tr("Comment"), JLabel.TRAILING);
	final JTextArea jtComment = new JTextArea(3, 0);
	jtComment.setName("comment");
	jtComment.setText(eventEntity.getComment());
	panel.add(jlComment);
	panel.add(jtComment);

	// Note the second parameter determines how many rows
	SpringUtilities.makeCompactGrid(panel, 11, 2, 6, 6, 6, 6);

	return panel;
    }

    private void initCategories()
    {
	catLabel = new HashMap<String, String>();
	catLabel.put("accident", "Accident");
	catLabel.put("concert", "Concert");

	catMap = new HashMap<String, List<String>>();
	catMap.put("accident", Arrays.asList("Animal Accident", "Animal Bite", "Assault"));
	catMap.put("concert",
	        Arrays.asList("50s / 60s era", "Alternative", "Bluegrass", "Children / Family", "Classical", "Comedy"));

	// Accident, Concert, Conference, Construction, Educational,
	// Exhibition, Natural, Political, Social, Sport, Traffic, Other

    }

    private void linkCateories(final JComboBox<String> pCat, final JComboBox<String> sCat)
    {
	pCat.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		List<String> subItems = catMap.get(pCat.getSelectedItem());
		if (subItems != null && !subItems.isEmpty())
		{
		    sCat.removeAllItems();
		    for (String subItem : subItems)
		    {
			sCat.addItem(subItem);
		    }
		}
	    }
	});
    }

    private void createComponentMap()
    {
	componentMap = new HashMap<String, Component>();
	Component[] components = panel.getComponents();
	for (int i = 0; i < components.length; i++)
	{
	    componentMap.put(components[i].getName(), components[i]);
	}
    }

    public Component getComponentByName(String name)
    {
	if (componentMap.containsKey(name))
	{
	    return componentMap.get(name);
	}
	else
	    return null;
    }

    public void updateEventEntity()
    {
	String[] componentNames = { "name", "category", "subcategory", "org", "startdate", "enddate", "link",
	        "numparticipants", "howoften", "howoftenchooseother", "comment" };
	for (String cmpName : componentNames)
	{
	    Component cmp = getComponentByName(cmpName);
	    String cmpValue = null;
	    if (cmp instanceof JTextField)
	    {
		cmpValue = ((JTextField) cmp).getText();
	    }
	    else if (cmp instanceof JTextArea)
	    {
		cmpValue = ((JTextArea) cmp).getText();
	    }
	    else if (cmp instanceof JComboBox)
	    {
		cmpValue = (String) ((JComboBox<String>) cmp).getSelectedItem();
	    }
	    else
	    {
		System.out.println("Invalid component named " + cmpName);
		return;
	    }

	    if (cmpName == "name")
	    {
		eventEntity.setName(cmpValue);
	    }
	    else if (cmpName == "category")
	    {
		eventEntity.setCategory(cmpValue);
	    }
	    else if (cmpName == "subcategory")
	    {
		eventEntity.setSubCategory(cmpValue);
	    }
	    else if (cmpName == "org")
	    {
		eventEntity.setOrganization(cmpValue);
	    }
	    else if (cmpName == "startdate")
	    {
		eventEntity.setStartDate(cmpValue);
	    }
	    else if (cmpName == "enddate")
	    {
		eventEntity.setEndDate(cmpValue);
	    }
	    else if (cmpName == "link")
	    {
		eventEntity.setUrl(cmpValue);
	    }
	    else if (cmpName == "numparticipants")
	    {
		eventEntity.setNumOfParticipants(cmpValue);
	    }
	    else if (cmpName == "howoften")
	    {
		eventEntity.setHowOften(cmpValue);
	    }
	    else if (cmpName == "howoftenchooseother")
	    {
		eventEntity.setHowOftenOther(cmpValue);
	    }
	    else if (cmpName == "comment")
	    {
		eventEntity.setComment(cmpValue);
	    }
	    else
	    {
		System.out.println("Wrong cmpName : " + cmpName);
	    }
	}
    }
}

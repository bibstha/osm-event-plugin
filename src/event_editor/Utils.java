package event_editor;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;

public class Utils
{
    protected static Pattern EVENT_NUMBER_PATTERN = Pattern.compile("event:([0-9]+):(.*)");

    public static EventPrimitive createEventPrimitive(OsmPrimitive primitive)
    {
	if (primitive == null)
	{
	    return null;
	}

	EventPrimitive selPrimitive = new EventPrimitive();
	Map<Integer, EventEntity> eventMap = selPrimitive.getEventMap();

	Set<String> keys = (Set<String>) primitive.keySet();
	if (keys.contains("event") && primitive.get("event").equals("yes"))
	{
	    selPrimitive.setIsEvent(true);
	}
	else
	{
	    selPrimitive.setIsEvent(false);
	}

	for (String key : keys)
	{
	    Set<Object> eventInfo = Utils.parseEventTag(key);
	    Iterator<Object> itr = eventInfo.iterator();

	    int eventNumber = (Integer) itr.next();
	    String eventSpecificTag = (String) itr.next();
	    String tagValue = primitive.get(key);

	    if (eventNumber < 0)
	    {
		System.out.println(key + " is not event Tag");
		continue;
	    }

	    if (selPrimitive.getNextHighestEventNumber() <= eventNumber)
	    {
		selPrimitive.setHighestEventNumber(eventNumber);
	    }

	    EventEntity eventEntity = eventMap.get(eventNumber);
	    if (eventEntity == null)
	    {
		eventEntity = new EventEntity();
		eventEntity.setEventId(eventNumber);
		eventMap.put(eventNumber, eventEntity);
	    }

	    if (eventSpecificTag.equals("name"))
	    {
		eventEntity.setName(tagValue);
	    }
	    else if (eventSpecificTag.equals("category"))
	    {
		eventEntity.setCategory(tagValue);
	    }
	    else if (eventSpecificTag.equals("subcategory"))
	    {
		eventEntity.setSubCategory(tagValue);
	    }
	    else if (eventSpecificTag.equals("organization"))
	    {
		eventEntity.setOrganization(tagValue);
	    }
	    else if (eventSpecificTag.equals("startdate"))
	    {
		eventEntity.setStartDate(tagValue);
	    }
	    else if (eventSpecificTag.equals("enddate"))
	    {
		eventEntity.setEndDate(tagValue);
	    }
	    else if (eventSpecificTag.equals("url"))
	    {
		eventEntity.setUrl(tagValue);
	    }
	    else if (eventSpecificTag.equals("num_participants"))
	    {
		eventEntity.setNumOfParticipants(tagValue);
	    }
	    else if (eventSpecificTag.equals("howoften"))
	    {
		eventEntity.setHowOften(tagValue);
	    }
	    else if (eventSpecificTag.equals("howoften_other"))
	    {
		eventEntity.setHowOftenOther(tagValue);
	    }
	    else if (eventSpecificTag.equals("comment"))
	    {
		eventEntity.setComment(tagValue);
	    }
	    else if (eventSpecificTag.equals("related_items"))
	    {
		eventEntity.setRelatedItems(tagValue);
	    }
	    else
	    {
		System.out.println("Utils.java:1: Unknown event tag found :" + eventSpecificTag);
	    }

	    System.out.println("Saved tag " + key + ". Num: " + eventNumber + ", Tag: " + eventSpecificTag);
	}
	return selPrimitive;
    }

    protected static Set<Object> parseEventTag(String key)
    {
	Integer eventNumber = -1;
	String eventSpecificTag = "";
	Matcher matcher = EVENT_NUMBER_PATTERN.matcher(key);
	if (matcher.find())
	{
	    eventNumber = Integer.parseInt(matcher.group(1));
	    eventSpecificTag = matcher.group(2);
	}

	Set<Object> ret = new LinkedHashSet<Object>();
	ret.add(eventNumber);
	ret.add(eventSpecificTag);

	return ret;
    }

    public static void saveEventPrimitive(EventPrimitive eventPrimitive, OsmPrimitive osmPrimitive)
    {
	OsmPrimitive selClone = null;
	if (osmPrimitive instanceof Way)
	{
	    selClone = new Way((Way) osmPrimitive);
	}
	else if (osmPrimitive instanceof Node)
	{
	    selClone = new Node((Node) osmPrimitive);
	}

	if (eventPrimitive.isEvent())
	{
	    osmPrimitive.put("event", "yes");
	}
	else
	{
	    osmPrimitive.put("event", null);
	}

	Map<Integer, EventEntity> eventMap = eventPrimitive.getEventMap();
	if (eventMap != null && !eventMap.isEmpty())
	{
	    for (Integer i : eventMap.keySet())
	    {
		String keyPrefix = "event:" + i + ":";
		saveEventPrimitive(osmPrimitive, keyPrefix + "name", eventMap.get(i).getName());
		saveEventPrimitive(osmPrimitive, keyPrefix + "category", eventMap.get(i).getCategory());
		saveEventPrimitive(osmPrimitive, keyPrefix + "subcategory", eventMap.get(i).getSubCategory());
		saveEventPrimitive(osmPrimitive, keyPrefix + "organization", eventMap.get(i).getOrganization());
		saveEventPrimitive(osmPrimitive, keyPrefix + "startdate", eventMap.get(i).getStartDate());
		saveEventPrimitive(osmPrimitive, keyPrefix + "enddate", eventMap.get(i).getEndDate());
		saveEventPrimitive(osmPrimitive, keyPrefix + "url", eventMap.get(i).getUrl());
		saveEventPrimitive(osmPrimitive, keyPrefix + "num_participants", eventMap.get(i).getNumOfParticipants());
		saveEventPrimitive(osmPrimitive, keyPrefix + "howoften", eventMap.get(i).getHowOften());
		saveEventPrimitive(osmPrimitive, keyPrefix + "howoften_other", eventMap.get(i).getHowOftenOther());
		saveEventPrimitive(osmPrimitive, keyPrefix + "comment", eventMap.get(i).getComment());
		saveEventPrimitive(osmPrimitive, keyPrefix + "related_items", eventMap.get(i).getRelatedItems());
	    }
	}
	if (!osmPrimitive.hasSameTags(selClone))
	{
	    osmPrimitive.setModified(true);
	}
    }

    public static void saveEventPrimitive(OsmPrimitive sel, String tag, String value)
    {
	if (value != null && value.equals(""))
	{
	    value = null;
	}
	sel.put(tag, value);
    }
}

package event_editor;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openstreetmap.josm.data.osm.OsmPrimitive;

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

	    if (selPrimitive.getNextHighestEventNumber() < eventNumber)
	    {
		selPrimitive.setHighestEventNumber(eventNumber);
	    }

	    EventEntity eventEntity = eventMap.get(eventNumber);
	    if (eventEntity == null)
	    {
		eventEntity = new EventEntity();
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
}

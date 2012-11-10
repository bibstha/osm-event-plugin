package event_editor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
	selPrimitive.setIsEvent(true);
	List<EventEntity> eventList = selPrimitive.getEventList();

	Set<String> keys = (Set<String>) primitive.keySet();
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

	    EventEntity eventEntity = eventList.get(eventNumber);
	    if (null == eventEntity)
	    {
		eventEntity = new EventEntity();
		eventList.set(eventNumber, eventEntity);
	    }

	    if (eventSpecificTag == "name")
	    {
		eventEntity.setName(tagValue);
	    }
	    else if (eventSpecificTag == "comment")
	    {
		eventEntity.setComment(tagValue);
	    }

	    System.out.println("Saved tag " + key + "for event number : " + eventNumber);
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

	Set<Object> ret = new HashSet<Object>();
	ret.add(eventNumber);
	ret.add(eventSpecificTag);

	return ret;
    }
}

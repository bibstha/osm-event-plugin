package event_editor;

import java.util.HashMap;
import java.util.Map;

public class EventPrimitive
{

    protected Map<Integer, EventEntity> eventMap = new HashMap<Integer, EventEntity>();
    protected boolean isEvent = true;
    protected Integer highestEventNumber = -1;

    public boolean isEvent()
    {
	return isEvent;
    }

    public void setIsEvent(boolean isEvent)
    {
	this.isEvent = isEvent;
    }

    public Map<Integer, EventEntity> getEventMap()
    {
	return this.eventMap;
    }

    @Override
    public String toString()
    {
	return "EventPrimitive isEvent:" + isEvent + " , " + eventMap.toString();
    }

    public Integer getNextHighestEventNumber()
    {
	return highestEventNumber + 1;
    }

    public void setHighestEventNumber(Integer i)
    {
	this.highestEventNumber = i;
    }
}

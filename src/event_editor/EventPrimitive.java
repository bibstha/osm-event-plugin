package event_editor;

import java.util.ArrayList;
import java.util.List;

public class EventPrimitive {

    protected List<EventEntity> eventList = new ArrayList<EventEntity>();
    protected boolean isEvent = true;

    public boolean isEvent() {
	return isEvent;
    }

    public void setIsEvent(boolean isEvent) {
	this.isEvent = isEvent;
    }

    public List<EventEntity> getEventList() {
	return this.eventList;
    }
}

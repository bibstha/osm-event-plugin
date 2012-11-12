package event_editor;

abstract public class EventListener implements java.util.EventListener
{
    abstract public void notify(String eventType);
}

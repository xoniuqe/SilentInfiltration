package de.mih.core.engine.ecs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;

public class EventManager
{
	//HashMap<Class<? extends BaseEvent>, ArrayList<EventListener<? extends BaseEvent>>> registeredHandlers = new HashMap<>();
	
	LinkedList<BaseEvent> eventQueue = new LinkedList<>();
	ArrayList<EventListener> eventListeners = new ArrayList<>();
//	FileHandle logFile;
	
	public EventManager()
	{		
		// logFile = Gdx.files.local("log.txt");
	}

	public void register(EventListener eventListener)
	{
		eventListeners.add(eventListener);
	}
//	public void register(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
//	{
//		if (!registeredHandlers.containsKey(eventType))
//		{
//			registeredHandlers.put(eventType, new ArrayList<EventListener<? extends BaseEvent>>());
//		}
//		registeredHandlers.get(eventType).add(eventListener);
//	}

//	public void unregister(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
//	{
//		eventListeners.remove(eventListener);
//		if (registeredHandlers.containsKey(eventType))
//		{
//			if (registeredHandlers.get(eventType).contains(eventListener))
//			{
//				registeredHandlers.get(eventType).remove(eventListener);
//			}
//		}
//	}

	public void queueEvent(BaseEvent event)
	{
		this.eventQueue.add(event);
	}
	
	public void update()
	{
		
		//TODO: only one event per update could be critical
		if(eventQueue.isEmpty())
			return;
		while(!eventQueue.isEmpty())
		{
			BaseEvent event = this.eventQueue.poll();
			this.fire(event);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fire(BaseEvent event)
	{
		log(event);
		for(EventListener listener : eventListeners)
		{
			listener.handleEvent(event);
		}
//		if (registeredHandlers.containsKey(event.getClass()))
//		{
//			for(EventListener listener : registeredHandlers.get(event.getClass()))
//			{
//				listener.handleEvent(event);
//			}
//		}
//		if (registeredHandlers.containsKey(BaseEvent.class))
//		{
//			for(EventListener listener : registeredHandlers.get(BaseEvent.class))
//			{
//				listener.handleEvent(event);
//			}
//		}
	}
	
	public void log(BaseEvent event)
	{
		Calendar cal  = Calendar.getInstance();
		Date     time = cal.getTime();
		DateFormat formatter = new SimpleDateFormat();
		//logFile.writeString(event.toString() + "\n" , true, "UTF-8");
		System.out.println(formatter.format(time) + ": " + event.toString());
	}
	

}

package ai.privado.demo.accounts.async;

import ai.privado.demo.accounts.apistubs.DataLoggerS;
import ai.privado.demo.accounts.service.dto.EventD;

public class EventJobRun implements Runnable {

	private DataLoggerS datalogger;

	private EventD event;

	@Override
	public void run() {
		datalogger.sendEvent(event);
	}

	public EventJobRun(DataLoggerS datalogger, EventD event) {
		super();
		this.datalogger = datalogger;
		this.event = event;
	}

	public DataLoggerS getDatalogger() {
		return datalogger;
	}

	public void setDatalogger(DataLoggerS datalogger) {
		this.datalogger = datalogger;
	}

	public EventD getEvent() {
		return event;
	}

	public void setEvent(EventD event) {
		this.event = event;
	}

}

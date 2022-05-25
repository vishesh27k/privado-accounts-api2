package ai.privado.demo.accounts.async;

import ai.privado.demo.accounts.apistubs.DataLoggerS;
import ai.privado.demo.accounts.service.dto.EventD;
import lombok.Data;
import lombok.Setter;

@Data
@Setter

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
}

package org.lttng.flightbox.io;

import org.eclipse.linuxtools.lttng.jni.exception.JniException;
import org.lttng.flightbox.cpu.TraceEventHandlerProcess;
import org.lttng.flightbox.model.SystemModel;
import org.lttng.flightbox.stub.StubTraceReader;

public class ModelBuilder {

	public static void buildFromTrace(String tracePath, SystemModel model) throws JniException {
		buildFromTrace(tracePath, model, null);
	}
	
	public static void buildFromStubTrace(String xmlTracePath, SystemModel model) throws JniException {
		// read metadata and statedump
		TraceEventHandlerModelMeta handlerMeta = new TraceEventHandlerModelMeta();
		handlerMeta.setModel(model);
		TraceReader readerMeta = new StubTraceReader(xmlTracePath);
		readerMeta.register(handlerMeta);
		readerMeta.process();
	
		// read all trace events
		TraceEventHandlerModel handler = new TraceEventHandlerModel();
		handler.setModel(model);
		TraceReader readerTrace = new StubTraceReader(xmlTracePath);
		readerTrace.register(handler);
		readerTrace.process();
	}
	
	
	public static void buildFromTrace(String tracePath, SystemModel model, ITraceEventHandler[] handlers) throws JniException {
		// read metadata and statedump
		TraceEventHandlerModelMeta handlerMeta = new TraceEventHandlerModelMeta();
		handlerMeta.setModel(model);
		TraceReader readerMeta = new TraceReader(tracePath);
		readerMeta.register(handlerMeta);
		readerMeta.process();
	
		// read all trace events
		TraceEventHandlerModel handler = new TraceEventHandlerModel();
		handler.setModel(model);
		TraceReader readerTrace = new TraceReader(tracePath);
		readerTrace.register(handler);
		if (handlers != null) {
			for (ITraceEventHandler hand: handlers) {
				readerTrace.register(hand);
			}
		}
		readerTrace.process();
	}
}

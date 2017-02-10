package com.sos.jitl.classes.event;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.sos.jitl.restclient.JobSchedulerRestApiClient;

import javassist.NotFoundException;
import sos.util.SOSString;

public class JobSchedulerEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerEventHandler.class);

	public static final String MASTER_API_PATH = "/jobscheduler/master/api/";

	public static enum EventType {
		FileBasedEvent, FileBasedAdded, FileBasedRemoved, FileBasedReplaced, FileBasedActivated, TaskEvent, TaskStarted, TaskEnded, TaskClosed, OrderEvent, OrderStarted, OrderFinished, OrderStepStarted, OrderStepEnded, OrderSetBack, OrderNodeChanged, OrderSuspended, OrderResumed, JobChainEvent, JobChainStateChanged, JobChainNodeActionChanged, SchedulerClosed
	};

	public static enum EventSeq {
		NonEmpty, Empty, Torn
	};

	public static enum EventPath {
		event, fileBased, task, order, jobChain
	};

	public static enum EventKey {
		TYPE, key, eventId, eventSnapshots, jobPath
	};

	public static enum EventOverview {
		FileBasedOverview, FileBasedDetailed, TaskOverview, OrderOverview, JobChainOverview
	};

	private int httpClientConnectTimeout = 65_000;
	private int httpClientSocketTimeout = 65_000;
	
	private int webserviceTimeout = 60;

	private String identifier;
	private String baseUrl;
	private JobSchedulerRestApiClient client;

	public void createRestApiClient() {
		String method = getMethodName("createRestApiClient");

		LOGGER.debug(String.format("%s: connectTimeout=%s, socketTimeout=%s", method, this.httpClientConnectTimeout, this.httpClientSocketTimeout));
		client = new JobSchedulerRestApiClient();
		client.setAutoCloseHttpClient(false);
		client.setConnectionTimeout(this.httpClientConnectTimeout);
		client.setSocketTimeout(this.httpClientSocketTimeout);
		client.createHttpClient();
	}

	public void closeRestApiClient() {
		String method = getMethodName("closeRestApiClient");

		if (client != null) {
			LOGGER.info(String.format("%s", method));
			client.closeHttpClient();
		} else {
			LOGGER.info(String.format("%s: skip. client is NULL", method));
		}
		client = null;
	}

	public void setBaseUrl(String httpPort) {
		this.baseUrl = String.format("http://localhost:%s", httpPort);
	}

	public JsonObject getOverview(EventPath path) throws Exception {
		return getOverview(path, getEventOverviewByEventPath(path), null);
	}

	public JsonObject getOverview(EventPath path, String bodyParamPath) throws Exception {
		return getOverview(path, getEventOverviewByEventPath(path), bodyParamPath);
	}

	public JsonObject getOverview(EventPath path, EventOverview overview) throws Exception {
		return getOverview(path, overview, null);
	}

	public JsonObject getOverview(EventPath path, EventOverview overview, String bodyParamPath) throws Exception {
		String method = getMethodName("getOverview");

		LOGGER.debug(String.format("%s: eventPath=%s, eventOverview=%s, bodyParamPath=%s", method, path, overview,
				bodyParamPath));
		URIBuilder ub = new URIBuilder(getUri(path));
		ub.addParameter("return", overview.name());
		return executeJsonPost(ub.build(), bodyParamPath);
	}

	public JsonObject getEvents(Long eventId, EventType[] eventTypes) throws Exception {
		return getEvents(eventId, joinEventTypes(eventTypes), null);
	}

	public JsonObject getEvents(Long eventId, EventType[] eventTypes, String bodyParamPath) throws Exception {
		return getEvents(eventId, joinEventTypes(eventTypes), bodyParamPath);
	}

	public JsonObject getEvents(Long eventId, String eventTypes) throws Exception {
		return getEvents(eventId, eventTypes, null);
	}

	public JsonObject getEvents(Long eventId, String eventTypes, String bodyParamPath) throws Exception {
		String method = getMethodName("getEvents");

		LOGGER.debug(String.format("%s: eventId=%s, eventTypes=%s, bodyParamPath=%s", method, eventId, eventTypes,
				bodyParamPath));

		URIBuilder ub = new URIBuilder(getUri(EventPath.event));
		if (!SOSString.isEmpty(eventTypes)) {
			ub.addParameter("return", eventTypes);
		}
		ub.addParameter("timeout", String.valueOf(webserviceTimeout));
		ub.addParameter("after", eventId.toString());
		return executeJsonPost(ub.build(), bodyParamPath);
	}

	public JsonObject executeJsonPost(URI uri) throws Exception {
		return executeJsonPost(uri, null);
	}

	public JsonObject executeJsonPost(URI uri, String bodyParamPath) throws Exception {
		String method = getMethodName("executeJsonPost");

		String headerKeyContentType = "Content-Type";
		String headerValueApplication = "application/json";

		client.addHeader(headerKeyContentType, headerValueApplication);
		client.addHeader("Accept", headerValueApplication);
		String body = null;
		if (!SOSString.isEmpty(bodyParamPath)) {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add("path", bodyParamPath);
			body = builder.build().toString();
		}
		
		LOGGER.debug(String.format("%s: call uri=%s, bodyParamPath=%s", method, uri, bodyParamPath));
		String response = client.postRestService(uri, body);
		LOGGER.debug(String.format("%s: response=%s", method, response));
		int statusCode = client.statusCode();
		String contentType = client.getResponseHeader(headerKeyContentType);
		JsonObject json = null;
		if (contentType.contains(headerValueApplication)) {
			JsonReader jr = Json.createReader(new StringReader(response));
			json = jr.readObject();
		}
		switch (statusCode) {
		case 200:
			if (json != null) {
				return json;
			} else {
				throw new Exception(
						String.format("%s: unexpected content type '%s'. response: %s", method, contentType, response));
			}
		case 400:
			// TO DO check Content-Type
			// for now the exception is plain/text instead of JSON
			// throw message item value
			if (json != null) {
				throw new Exception(json.getString("message"));
			} else {
				throw new Exception(
						String.format("%s: unexpected content type '%s'. response: %s", method, contentType, response));
			}
		case 404:
			throw new NotFoundException(String.format("%s: %s %s, uri=%s", method, statusCode,
					client.getHttpResponse().getStatusLine().getReasonPhrase(), uri.toString()));
		default:
			throw new Exception(String.format("%s: %s %s", method, statusCode,
					client.getHttpResponse().getStatusLine().getReasonPhrase()));
		}
	}

	public Long getEventId(JsonObject json) {
		Long eventId = null;
		if (json != null) {
			JsonNumber r = json.getJsonNumber(EventKey.eventId.name());
			if (r != null) {
				eventId = r.longValue();
			}
		}
		return eventId;
	}

	public String getEventType(JsonObject json) {
		return json == null ? null : json.getString(EventKey.TYPE.name());
	}

	public JsonArray getEventSnapshots(JsonObject json) {
		return json == null ? null : json.getJsonArray(EventKey.eventSnapshots.name());
	}

	public String getEventKey(JsonObject json) {
		String eventKey = null;
		JsonValue key = json.get(EventKey.key.name());
		if (key != null) {
			if (key.getValueType().equals(ValueType.STRING)) {
				eventKey = key.toString();
			} else if (key.getValueType().equals(ValueType.OBJECT)) {
				if (((JsonObject) key).containsKey(EventKey.jobPath.name())) {
					eventKey = ((JsonObject) key).getString(EventKey.jobPath.name());
				}
			}
		}
		return eventKey;
	}

	public String joinEventTypes(EventType[] type) {
		return type == null ? "" : Joiner.on(",").join(type);
	}

	public EventOverview getEventOverviewByEventTypes(EventType[] type) {
		if (type != null && type.length > 0) {
			String first = type[0].name();
			if (first.toLowerCase().startsWith(EventPath.fileBased.name().toLowerCase())) {
				return EventOverview.FileBasedOverview;
			} else if (first.toLowerCase().startsWith(EventPath.order.name().toLowerCase())) {
				return EventOverview.OrderOverview;
			} else if (first.toLowerCase().startsWith(EventPath.task.name().toLowerCase())) {
				return EventOverview.TaskOverview;
			} else if (first.toLowerCase().startsWith(EventPath.jobChain.name().toLowerCase())) {
				return EventOverview.JobChainOverview;
			}
		}
		return null;
	}

	public EventOverview getEventOverviewByEventPath(EventPath path) {
		if (path != null) {
			if (path.equals(EventPath.fileBased)) {
				return EventOverview.FileBasedOverview;
			} else if (path.equals(EventPath.order)) {
				return EventOverview.OrderOverview;
			} else if (path.equals(EventPath.task)) {
				return EventOverview.TaskOverview;
			} else if (path.equals(EventPath.jobChain)) {
				return EventOverview.JobChainOverview;
			}
		}
		return null;
	}

	public EventPath getEventPathByEventOverview(EventOverview overview) {
		if (overview != null) {
			if (overview.name().toLowerCase().startsWith(EventPath.fileBased.name().toLowerCase())) {
				return EventPath.fileBased;
			} else if (overview.name().toLowerCase().startsWith(EventPath.order.name().toLowerCase())) {
				return EventPath.order;
			} else if (overview.name().toLowerCase().startsWith(EventPath.task.name().toLowerCase())) {
				return EventPath.task;
			} else if (overview.name().toLowerCase().startsWith(EventPath.jobChain.name().toLowerCase())) {
				return EventPath.jobChain;
			} else if (overview.name().toLowerCase().startsWith(EventPath.event.name().toLowerCase())) {
				return EventPath.event;
			}
		}
		return null;
	}

	public URI getUri(EventPath path) throws URISyntaxException {
		if (this.baseUrl == null) {
			throw new URISyntaxException("null", "baseUrl is NULL");
		}
		if (path == null) {
			throw new URISyntaxException("null", "path is NULL");
		}
		StringBuilder uri = new StringBuilder();
		uri.append(baseUrl);
		uri.append(MASTER_API_PATH);
		uri.append(path.name());
		return new URI(uri.toString());
	}

	public String getMethodName(String name) {
		String prefix = this.identifier == null ? "" : String.format("[%s] ", this.identifier);
		return String.format("%s%s", prefix, name);
	}

	public void setIdentifier(String val) {
		this.identifier = val;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public JobSchedulerRestApiClient getRestApiClient() {
		return client;
	}

	public int getHttpClientConnectTimeout() {
		return this.httpClientConnectTimeout;
	}

	public void setHttpClientConnectTimeout(int val) {
		this.httpClientConnectTimeout = val;
	}
	
	public int getHttpClientSocketTimeout() {
		return this.httpClientSocketTimeout;
	}

	public void setHttpClientSocketTimeout(int val) {
		this.httpClientSocketTimeout = val;
	}

	public int getWebserviceTimeout() {
		return this.webserviceTimeout;
	}

	public void setWebserviceTimeout(int val) {
		this.webserviceTimeout = val;
	}

}
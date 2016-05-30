package com.doer.moodle.common.sess.session.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.json.JSONObject;
import com.doer.moodle.common.config.ConfigurationCenter;
import com.doer.moodle.common.config.zoo.ConfigurationWatcher;
import com.doer.moodle.common.sess.session.RequestEventObserver;
import com.doer.moodle.common.sess.session.SessionException;

public class SessionManager implements ConfigurationWatcher {
	private Logger log = Logger.getLogger(SessionManager.class);
	private static final String SESSION_ID_PREFIX = "R_JSID_";
	private static final String SESSION_ID_COOKIE = "WOEGO_JSESSIONID";
	private static final String EXPIRATIONUPDATEINTERVAL_KEY = "expirationUpdateInterval";
	private static final String MAXINACTIVEINTERVAL_KEY = "maxInactiveInterval";
	private static final String HOST_KEY = "host";
	private static final String PORT_KEY = "port";
	private static final String TIMEOUT_KEY = "timeOut";
	private static final String MAXACTIVE_KEY = "maxActive";
	private static final String MAXIDLE_KEY = "maxIdle";
	private static final String MAXWAIT_KEY = "maxWait";
	private static final String TESTONBORROW_KEY = "testOnBorrow";
	private static final String TESTONRETURN_KEY = "testOnReturn";
	private static final String DBINDEX_KEY = "dbIndex";
	private static final String TWEMPROXY_KEY = "twemproxy";
	private static final String DOMAIN_KEY = "domain";
	private String host = null;
	private String port = null;
	private String timeOut = null;
	private String maxActive = null;
	private String maxIdle = null;
	private String maxWait = null;
	private String testOnBorrow = null;
	private String testOnReturn = null;
	private RedisCacheClient cacheClient = null;
	private int dbIndex = 0;
	private String twemproxy = null;
	private int expirationUpdateInterval = 300;
	private int maxInactiveInterval = 1800;
	private String domain = "";

	private String confPath = "";
	private ConfigurationCenter confCenter = null;
	//redis宕机后，session存放在本地
	private Map<String,HttpSession> localSessions = new HashMap<String,HttpSession>();
	//redis宕机标识
	private boolean redisDowntime = false;

	public ConfigurationCenter getConfCenter() {
		return confCenter;
	}

	public void setConfCenter(ConfigurationCenter confCenter) {
		this.confCenter = confCenter;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public CacheHttpSession createSession(
			SessionHttpServletRequestWrapper request,
			HttpServletResponse response,
			RequestEventSubject requestEventSubject, boolean create) {
		String sessionId = getRequestedSessionId(request);

		CacheHttpSession session = null;
		if ((StringUtils.isEmpty(sessionId)) && (!create))
			return null;
		if (StringUtils.isNotEmpty(sessionId)) {
			session = loadSession(sessionId);
		}
		if ((session == null) && (create)) {
			session = createEmptySession(request, response);
		}
		if (session != null)
			attachEvent(session, request, response, requestEventSubject);
		return session;
	}

	private String getRequestedSessionId(HttpServletRequestWrapper request) {
		// String cookid=request.getRequestedSessionId();
		// System.out.println(cookid);
		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0))
			return null;
		for (Cookie cookie : cookies) {
			if (SESSION_ID_COOKIE.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}

	private void saveSession(CacheHttpSession session) {
		try {
			if (this.log.isDebugEnabled())
				this.log.debug("CacheHttpSession saveSession [ID=" + session.id
						+ ",isNew=" + session.isNew + ",isDiry="
						+ session.isDirty + ",isExpired=" + session.expired
						+ "]");
			if (session.expired)
				this.removeSessionFromCache(generatorSessionKey(session.id));
			else
				// sessionService.saveSession(generatorSessionKey(session.id),
				// session,session.maxInactiveInterval +
				// this.expirationUpdateInterval);
				this.saveSessionToCache(generatorSessionKey(session.id),
						session, session.maxInactiveInterval);
		} catch (Exception e) {
			throw new SessionException(e);
		}
	}

	private CacheHttpSession createEmptySession(
			SessionHttpServletRequestWrapper request,
			HttpServletResponse response) {
		CacheHttpSession session = new CacheHttpSession();
		session.id = createSessionId();
		session.creationTime = System.currentTimeMillis();
		session.maxInactiveInterval = this.maxInactiveInterval;
		session.isNew = true;
		if (this.log.isDebugEnabled())
			this.log.debug("CacheHttpSession Create [ID=" + session.id + "]");
		saveCookie(session, request, response);
		return session;
	}

	private String createSessionId() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	private void attachEvent(final CacheHttpSession session,
			final HttpServletRequestWrapper request,
			final HttpServletResponse response,
			RequestEventSubject requestEventSubject) {
		session.setListener(new SessionListenerAdaptor() {
			public void onInvalidated(CacheHttpSession session) {
				SessionManager.this.saveCookie(session, request, response);
			}
		});
		requestEventSubject.attach(new RequestEventObserver() {
			public void completed(HttpServletRequest servletRequest,
					HttpServletResponse response) {
				int updateInterval = (int) ((System.currentTimeMillis() - session.lastAccessedTime) / 1000L);
				if (SessionManager.this.log.isDebugEnabled()) {
					SessionManager.this.log
							.debug("CacheHttpSession Request completed [ID="
									+ session.id + ",lastAccessedTime="
									+ session.lastAccessedTime
									+ ",updateInterval=" + updateInterval + "]");
				}
				if ((!session.isNew)
						&& (!session.isDirty)
						&& (updateInterval < SessionManager.this.expirationUpdateInterval))
					return;
				if ((session.isNew) && (session.expired))
					return;
				session.lastAccessedTime = System.currentTimeMillis();
				SessionManager.this.saveSession(session);
			}
		});
	}

	private void addCookie(CacheHttpSession session,
			HttpServletRequestWrapper request, HttpServletResponse response) {
		Cookie cookie = new Cookie(SESSION_ID_COOKIE, null);
		if (!StringUtils.isBlank(domain))
			cookie.setDomain(domain);
		cookie.setPath(StringUtils.isBlank(request.getContextPath())?"/":request.getContextPath());
		if (session.expired)
			cookie.setMaxAge(0);
		else if (session.isNew) {
			cookie.setValue(session.getId());
		}
		response.addCookie(cookie);
	}

	private void saveCookie(CacheHttpSession session,
			HttpServletRequestWrapper request, HttpServletResponse response) {
		if ((!session.isNew) && (!session.expired))
			return;

		Cookie[] cookies = request.getCookies();
		if ((cookies == null) || (cookies.length == 0)) {
			addCookie(session, request, response);
		} else {
			for (Cookie cookie : cookies) {
				if (SESSION_ID_COOKIE.equals(cookie.getName())) {
					if (!StringUtils.isBlank(domain))
						cookie.setDomain(domain);
					cookie.setPath(StringUtils.isBlank(request.getContextPath())?"/":request.getContextPath());
					cookie.setMaxAge(0);
				}
			}
			addCookie(session, request, response);
		}
		if (this.log.isDebugEnabled())
			this.log.debug("CacheHttpSession saveCookie [ID=" + session.id
					+ "]");
	}

	private CacheHttpSession loadSession(String sessionId) {
		CacheHttpSession session;
		try {
			HttpSession data = this
					.getSessionFromCache(generatorSessionKey(sessionId));
			if (data == null) {
				this.log.debug("Session " + sessionId + " not found in Redis");
				session = null;
			} else {
				session = (CacheHttpSession) data;
			}
			if (this.log.isDebugEnabled())
				this.log.debug("CacheHttpSession Load [ID=" + sessionId
						+ ",exist=" + (session != null) + "]");
			if (session != null) {
				session.isNew = false;
				session.isDirty = false;
			}
			return session;
		} catch (Exception e) {
			this.log.warn("exception loadSession [Id=" + sessionId + "]", e);
		}
		return null;
	}

	private String generatorSessionKey(String sessionId) {
		return SESSION_ID_PREFIX.concat(sessionId);
		// return "R_JSID_".concat(sessionId);
	}

	public SessionManager() {
	}

	public void init() {
		try {
			process(confCenter.getConfAndWatch(confPath, this));
		} catch (PaasException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param conf
	 * 
	 * @see com.ai.paas.client.ConfigurationWatcher#process(java.lang.String)
	 */
	@Override
	public void process(String conf) {
		if (log.isInfoEnabled()) {
			log.info("new session configuration is received: " + conf);
		}
		JSONObject json = JSONObject.fromObject(conf);
		boolean changed = false;
		if (json.containsKey(HOST_KEY)
				&& !json.getString(HOST_KEY).equals(host)) {
			changed = true;
			host = json.getString(HOST_KEY);
		}
		if (json.containsKey(PORT_KEY)
				&& !json.getString(PORT_KEY).equals(port)) {
			changed = true;
			port = json.getString(PORT_KEY);
		}
		if (json.containsKey(TIMEOUT_KEY)
				&& !json.getString(TIMEOUT_KEY).equals(timeOut)) {
			changed = true;
			timeOut = json.getString(TIMEOUT_KEY);
		}
		if (json.containsKey(MAXACTIVE_KEY)
				&& !json.getString(MAXACTIVE_KEY).equals(maxActive)) {
			changed = true;
			maxActive = json.getString(MAXACTIVE_KEY);
		}
		if (json.containsKey(MAXIDLE_KEY)
				&& !json.getString(MAXIDLE_KEY).equals(maxIdle)) {
			changed = true;
			maxIdle = json.getString(MAXIDLE_KEY);
		}
		if (json.containsKey(MAXWAIT_KEY)
				&& !json.getString(MAXWAIT_KEY).equals(maxWait)) {
			changed = true;
			maxWait = json.getString(MAXWAIT_KEY);
		}
		if (json.containsKey(TESTONBORROW_KEY)
				&& !json.getString(TESTONBORROW_KEY).equals(testOnBorrow)) {
			changed = true;
			testOnBorrow = json.getString(TESTONBORROW_KEY);
		}
		if (json.containsKey(TESTONRETURN_KEY)
				&& !json.getString(TESTONRETURN_KEY).equals(testOnReturn)) {
			changed = true;
			testOnReturn = json.getString(TESTONRETURN_KEY);
		}
		if (json.containsKey(DBINDEX_KEY)
				&& !json.getString(DBINDEX_KEY).equals(dbIndex)) {
			dbIndex = json.getInt(DBINDEX_KEY);
		}
		if (json.containsKey(TWEMPROXY_KEY)
				&& !json.getString(TWEMPROXY_KEY).equals(twemproxy)) {
			twemproxy = json.getString(TWEMPROXY_KEY);
		}
		if (json.containsKey(EXPIRATIONUPDATEINTERVAL_KEY)
				&& json.getInt(EXPIRATIONUPDATEINTERVAL_KEY) != expirationUpdateInterval) {
			expirationUpdateInterval = json
					.getInt(EXPIRATIONUPDATEINTERVAL_KEY);
		}
		if (json.containsKey(MAXINACTIVEINTERVAL_KEY)
				&& json.getInt(MAXINACTIVEINTERVAL_KEY) != maxInactiveInterval) {
			maxInactiveInterval = json.getInt(MAXINACTIVEINTERVAL_KEY);
		}
		if (JSONValidator.isChanged(json, DOMAIN_KEY, domain)) {
			domain = json.getString(DOMAIN_KEY);
		}
		if (changed) {
			cacheClient = new RedisCacheClient(conf);
			if (log.isInfoEnabled()) {
				log.info("cache server address is changed to " + conf);
			}
		}
	}

	public HttpSession getSessionFromCache(String id) {
		Object obj = null;
		if ("true".equalsIgnoreCase(twemproxy)) {
			obj = cacheClient.getSession(id);
		} else {
			obj = cacheClient.getSession(dbIndex, id);
		}
		if (obj != null && obj instanceof HttpSession) {
			redisDowntime = false;
			return (HttpSession) obj;
		} else if(obj != null && obj instanceof RedisDowntime) {
			redisDowntime = true;
			log.warn("--------------redis宕机-------------");
			return localSessions.get(id);
		}else {
			redisDowntime = false;
			return null;
		}
	}

	public void saveSessionToCache(String id, HttpSession session, int liveTime) {
		if(redisDowntime){
			localSessions.put(id, session);
		}else{
			if ("true".equalsIgnoreCase(twemproxy)) {
				cacheClient.addItem(id, session, liveTime);
			} else {
				cacheClient.addItem(dbIndex, id, session, liveTime);
			}
			localSessions.clear();
		}

	}

	public void removeSessionFromCache(String id) {
		if(redisDowntime){
			localSessions.remove(id);
		}else{
			if ("true".equalsIgnoreCase(twemproxy)) {
				cacheClient.delItem(id);
			} else {
				cacheClient.delItem(dbIndex, id);
			}
		}
		
	}

	/*
	 * private static SessionManager rs1=null; public static SessionManager
	 * getRs(){ if (rs1==null){ rs1=new SessionManager(); rs1.serializer=new
	 * SerializeUtil(); } return rs1; }
	 */

}

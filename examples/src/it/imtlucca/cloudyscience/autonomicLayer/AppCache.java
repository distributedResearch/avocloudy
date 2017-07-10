package it.imtlucca.cloudyscience.autonomicLayer;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;

/**
 * To maintain cache information about app on queue
 * 
 * @author Stefano Sebastio
 *
 */
public class AppCache {

	private AppAgent app;
	private float duration;
	
	public AppCache(AppAgent app, float duration) {
		super();
		this.app = app;
		this.duration = duration;
	}

	public AppAgent getApp() {
		return app;
	}

	public float getDuration() {
		return duration;
	}
	
	
	
	
}

package com.alexgilleran.icesoap.observer;

import java.util.List;

import com.alexgilleran.icesoap.request.ListRequest;
import com.alexgilleran.icesoap.request.Request;

/**
 * 
 * An extension of {@link SOAP11Observer} to deal with {@link ListRequest}s.
 * 
 * @author Alex Gilleran
 * 
 * @param <ReturnType>
 *            The type of the object that will be retrieved from this request.
 * @param <SOAPFaultType>
 *            The type of the class to use for SOAPFaults
 */
public interface SOAPListObserver<ReturnType, SOAPFaultType> extends SOAPObserver<List<ReturnType>, SOAPFaultType> {

	/**
	 * Called (on the UI thread) when a new list item is received and parsed
	 * form a running {@link ListRequest}.
	 * 
	 * @param request
	 *            The request that the item was parsed by
	 * @param item
	 *            The item instance.
	 */
	void onNewItem(Request<List<ReturnType>, SOAPFaultType> request, ReturnType item);

}
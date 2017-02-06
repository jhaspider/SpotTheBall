/*
 * Copyright (c) 2015.
 * Amarjit Jha
 * MindScale Technologies Private Limited
 * http://www.mindscale.co.in
 */

package bamboobush.com.wheresx.utils;

public interface AsyncResponse
{
	/**
	 * This method return the result from the web service response.
	 */
	void processFinish(String type, String output);

}

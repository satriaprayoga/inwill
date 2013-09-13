/**
 * 
 */
package com.inwill.dci;

import java.io.Serializable;

/**
 * @author G.S Prayoga of SWG
 *
 */
public interface Device extends Serializable{

	public String getAddress();
	public String getCommunity();
	public String getVersion();
}

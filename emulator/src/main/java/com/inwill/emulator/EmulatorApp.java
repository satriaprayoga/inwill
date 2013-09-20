/**
 * 
 */
package com.inwill.emulator;

import java.io.IOException;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class EmulatorApp {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		EmulatorAgent agent=new EmulatorAgent("udp:192.168.7.104/2010");
		agent.start();

        Thread.sleep(100*1000);
	}

}

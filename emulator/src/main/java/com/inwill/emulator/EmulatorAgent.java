/**
 * 
 */
package com.inwill.emulator;

import java.io.File;
import java.io.IOException;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

/**
 * @author G.S Prayoga of SWG
 *
 */
public class EmulatorAgent extends BaseAgent{
	
	private String address;

	protected EmulatorAgent(String configURI) {
		 super(new File("conf.agent"), new File("bootCounter.agent"),
	                new CommandProcessor(
	                new OctetString(MPv3.createLocalEngineID())));
		address=configURI;
	}

	@Override
	protected void registerManagedObjects() {
		// TODO Auto-generated method stub
		
	}
	
	 public void registerManagedObject(ManagedObject mo) {
	        try {
	            server.register(mo, null);
	        } catch (DuplicateRegistrationException ex) {
	            throw new RuntimeException(ex);
	        }
	    }

	@Override
	protected void unregisterManagedObjects() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addUsmUser(USM usm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addNotificationTargets(SnmpTargetMIB targetMIB,
			SnmpNotificationMIB notificationMIB) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addViews(VacmMIB vacm) {
		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(
                "cpublic"), new OctetString("v1v2group"),
                StorageType.nonVolatile);

        vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"),
                SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV,
                MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
                new OctetString("fullWriteView"), new OctetString(
                "fullNotifyView"), StorageType.nonVolatile);

        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);
		
	}

	@Override
	protected void addCommunities(SnmpCommunityMIB communityMIB) {
		Variable[] com2sec = new Variable[]{
	            new OctetString("public"), // community name
	            new OctetString("cpublic"), // security name
	            getAgent().getContextEngineID(), // local engine ID
	            new OctetString("public"), // default context name
	            new OctetString(), // transport tag
	            new Integer32(StorageType.nonVolatile), // storage type
	            new Integer32(RowStatus.active) // row status
	        };
	        MOTableRow row = communityMIB.getSnmpCommunityEntry().createRow(
	                new OctetString("public2public").toSubIndex(true), com2sec);
	        communityMIB.getSnmpCommunityEntry().addRow(row);
		
	}
	
	@Override
	protected void initTransportMappings() throws IOException {
		transportMappings = new TransportMapping[1];
        Address addr = GenericAddress.parse(address);
        TransportMapping<?> tm = TransportMappings.getInstance().createTransportMapping(addr);
        transportMappings[0] = tm;
	}
	
	public void start() throws IOException {

        init();
        // This method reads some old config from a file and causes
        // unexpected behavior.
        // loadConfig(ImportModes.REPLACE_CREATE);
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
    }

}

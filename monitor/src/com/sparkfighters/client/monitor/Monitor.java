package com.sparkfighters.client.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import javax.swing.JOptionPane;

import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

public class Monitor 
{
 
	private  boolean supportedOS()
	{
		OS os=new OS();
		if(os.isSupportedOS()==true)
		{
			return true;
		}
		else
		{				    
			String message="This application not support your operating system ("+os.yourOS+"). Please contact with authors of this application.";
			JOptionPane.showMessageDialog(null, message);
			return false;
		}
	}
	
	private  long getAppPID()
	{
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();	 
        String jvmName = runtimeBean.getName();
        //System.out.println("JVM Name = " + jvmName);
        long pid = Long.valueOf(jvmName.split("@")[0]);
        //System.out.println("JVM PID  = " + pid);
        return pid;
	}
	
	private  <T> void terminate(String filename,Class<T> StartClass)
	{
		long appPID=getAppPID();
		try
    	{
	        MonitoredHost host = MonitoredHost.getMonitoredHost(new String("localhost"));
	        int pid;
	        MonitoredVm vm;
	        String vmClass;
	        String vmCmdLine;
	        for(Object activeVMPid : host.activeVms())
	        {
	                pid = (Integer)activeVMPid;
	                if(pid==appPID) continue;
	                vm = host.getMonitoredVm(new VmIdentifier("//" + pid));
	                vmCmdLine = MonitoredVmUtil.commandLine(vm);
	                vmClass = MonitoredVmUtil.mainClass(vm, true);
	                if(vmClass.equals(StartClass.getName()) ||
	                        vmClass.equals(filename) ||
	                        (
	                                vmCmdLine.contains(filename) ||
	                                vmCmdLine.contains(StartClass.getSimpleName())
	                        ))
	                {
	                	//maybe bug
	                	OS os=new OS();
	                	Runtime rt = Runtime.getRuntime();
	                	if(os.isWindows()==true)
	                	{
	                	     rt.exec("taskkill /pid " +pid);
	                	}
	                	
	                	if(os.isUnix()==true)
	                	{
	                	     rt.exec("kill -9 " +pid);
	                	}
	                	
	                	if (os.isMac()==true) 
	                	{
	                	     rt.exec("kill " +pid);
	                	}
	                	                	
	                }
	        }
	        
    	}
    	catch (Exception e)
		{
		     e.printStackTrace();
		}
	}
	
	private  <T> int getInfo(String filename,Class<T> StartClass)
	{
		int counter = 0;
		long appPID=getAppPID();
		//System.out.println("Runtime info of this class - PID: " + appPID);
		
		//System.out.println("Verify if another running instance is active...");
		try
		{
		        //get monitored host for local computer
		        MonitoredHost host = MonitoredHost.getMonitoredHost(new String("localhost"));
		        //System.out.println(host.getHostIdentifier());
		        //System.out.println(host.activeVms());
		        int pid;
		        MonitoredVm vm;
		        String vmClass;
		        String vmCmdLine;
		        //iterate over pids of running applications on this host.
		        //seems every application is considered an instance of the 'activeVM'
		        for(Object activeVMPid : host.activeVms())
		        {
		                pid = (Integer)activeVMPid;
		                if(pid==appPID) continue;
		                //System.out.println("Looking at pid: " + pid);
		                //get specific vm instance for given pid
		                vm = host.getMonitoredVm(new VmIdentifier("//" + pid));
		                //get class of given vm instance's main class
		                vmCmdLine = MonitoredVmUtil.commandLine(vm);
		                vmClass = MonitoredVmUtil.mainClass(vm, true);
		                //is class in vm same as class you're comparing?
		                //System.out.println("class to examine: [" + vmClass + "]");
		                //System.out.println("cmd line to examine: [" + vmCmdLine + "]");
		                if(vmClass.equals(StartClass.getName()) ||
		                        vmClass.equals(filename) ||
		                        (
		                                vmCmdLine.contains(filename) ||
		                                vmCmdLine.contains(StartClass.getSimpleName())
		                        ))
		                {
		                        counter++;
		                        //System.out.println("Match to current class");
		                }
		        }
		       /* System.out.println("Found running instances of this " +
		                "program(don't count self): " + counter);*/
		       /* System.out.println("Runtime info of this class: " +
		                ManagementFactory.getRuntimeMXBean().getName());*/
		}
		catch (Exception e)
		{
		        e.printStackTrace();
		}
		
		return counter;
	}
	
	public  <T> boolean launch(String filename,Class<T> StartClass, String nameApp)
	{
		if(supportedOS()==false) System.exit(0);
		int counter=getInfo(filename,StartClass);
		
		if(counter >= 1)
		{
		        /*System.out.println("Attempting to run more than " +
		                "one instance.");*/
		        
		        String title="WARNING";		    
		        String message="Another instance of the "+nameApp+" is running. Are you want terminate it?";
		        
		        int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		        
		        if (reply == JOptionPane.YES_OPTION)
		        {
		        	//here terminate others instances
		        	terminate(filename,StartClass);
		   	
		        	return true;
		        }
		        else 
		        {
		        	System.exit(0);
		        	// return false;   
		        }
		      
		}
		else
		{
			  // System.out.println("Run application");   
		       return true;
		}

		System.exit(0);
		return false;
	}

}

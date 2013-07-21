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
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	private static boolean isWindows() 
	{	 	
		return (OS.indexOf("win") >= 0);
	}
 
	private static boolean isMac() 
	{
		return (OS.indexOf("mac") >= 0);
	}
 
	private static boolean isUnix() 
	{
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}
 
	private static boolean isSolaris() 
	{
		return (OS.indexOf("sunos") >= 0);
	}
	
	private static long getAppPID()
	{
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();	 
        String jvmName = runtimeBean.getName();
        //System.out.println("JVM Name = " + jvmName);
        long pid = Long.valueOf(jvmName.split("@")[0]);
        //System.out.println("JVM PID  = " + pid);
        return pid;
	}
	
	private static <T> void terminate(String filename,Class<T> StartClass)
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
	                	Runtime rt = Runtime.getRuntime();
	                	if(isWindows()==true)
	                	{
	                	     rt.exec("taskkill /pid " +pid);
	                	}
	                	
	                	if(isUnix()==true)
	                	{
	                	     rt.exec("kill -9 " +pid);
	                	}
	                	
	                	if (isMac()==true) 
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
	
	private static <T> int getInfo(String filename,Class<T> StartClass)
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
	
	public static <T> boolean launch(String filename,Class<T> StartClass, String nameApp)
	{
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
		        	Monitor.terminate(filename,StartClass);
		   	
		        	return true;
		        }
		        else 
		        {
		        	 return false;     
		        }
		      
		}
		else
		{
			  // System.out.println("Run application");   
		       return true;
		}
	}

}

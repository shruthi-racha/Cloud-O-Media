//+--------------------------------------------------------------------------+
//|                                                                          |
//|                             zMoviePlayer                                 |
//|                         DirectX Movie Player                             |
//|                                                                          |
//|                             Version 1.04                                 |
//+--------------------------------------------------------------------------+
//|                                                                          |
//|                         Author Patrice TERRIER                           |
//|                           copyright (c) 2007                             |
//|                                                                          |
//|                        pterrier@zapsolution.com                          |
//|                                                                          |
//|                          www.zapsolution.com                             |
//|                                                                          |
//+--------------------------------------------------------------------------+
//|                  Project started on : 04-20-2007 (MM-DD-YYYY)            |
//|                        Last revised : 05-21-2007 (MM-DD-YYYY)            |
//+--------------------------------------------------------------------------+

using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Diagnostics;
using Microsoft.DirectX.AudioVideoPlayback;
using Microsoft.Win32;
using Win32;

namespace zap
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>

        public const string RegistryKey = @"HKEY_LOCAL_MACHINE\SOFTWARE\zMoviePlayer";
        public const string RegistryPath = "DefaultPathName";
        public const string RegistryAudioVolume = "AudioVolume";
        public const string RegistryStringData = "StringData";

        [STAThread]
        static void Main(string[] Args)
        {
            string Argument = "";
            if (Args.Length != 0) Argument = Args[0];
            if (!IsAlreadyRunning(Argument))
            {
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                Application.Run(new MAIN_Form(Argument));
            }
            else
            {
                // The process is already running
                Application.Exit(); 
            }
        }

        static bool IsAlreadyRunning(string Argument) 
	    {
            bool bRet = false;
            IntPtr hFound;
            // Get the current process 
		    Process currentProcess = Process.GetCurrentProcess();
		    // Check with other process already running 
		    foreach (Process p in Process.GetProcesses()) 
		    { 
			    // Check running process 
                if (p.Id != currentProcess.Id)
                {
                    if (p.ProcessName.Equals(currentProcess.ProcessName) == true)
                    {
                        //MessageBox.Show(null, "Is already running.", currentProcess.ProcessName.ToString());
                        bRet = true;
                        hFound = p.MainWindowHandle;
                        if (Api.IsIconic(hFound)) // If application is in ICONIC mode then
                        {                         // show it in RESTORE mode.
                            Api.ShowWindow(hFound, Api.SW_RESTORE);
                            Api.SetForegroundWindow(hFound);
                        }
                        if (Argument.Length != 0)
                        {
                            // Save StringData in registry (see also Api.WM_STRINGDATA)
                            Registry.SetValue(RegistryKey, RegistryStringData, Argument);
                            Api.PostMessage(hFound, Api.WM_STRINGDATA, 0, 0);
                        }
                        break;
                    }
                }
		    } 
			return bRet; 
	    } 

    }
}
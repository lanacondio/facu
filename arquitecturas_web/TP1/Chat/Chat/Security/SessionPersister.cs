
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Chat.Models;

namespace Chat.Security
{
    public static class SessionPersister
    {
        private static string usernameSessionvar = "username";

        public static AccountViewModel Account
        {
            get 
            {
                if (HttpContext.Current == null)
                    return null;

                var sessionVar = HttpContext.Current.Session[usernameSessionvar];

                if (sessionVar != null)
                    return sessionVar as AccountViewModel;

                return null;
            }
            set 
            {
                HttpContext.Current.Session[usernameSessionvar] = value;
            }
        }

        public static void LogOut()
        {
            HttpContext.Current.Session.Clear();
        }



        public static bool isAuthenticated
        {
            get 
            {
                var sessionVar = HttpContext.Current.Session[usernameSessionvar];
                if (sessionVar != null)
                    return true;

                return false;
            }
        }
    }
}
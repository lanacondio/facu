using Chat.DomainModel;
using Chat.Security;
using System.Collections.Generic;
using System.Web;

namespace Chat.Security
{
    public class CustomAuthorizeAttribute : AuthorizeAttribute
    {   
        
        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            bool authorize = false;

            if (SessionPersister.isAuthenticated)
            {
                authorize =  (allowRoles==SessionPersister.Account.Role) || (allowRoles.Equals(DomainModel.Enum.Roles.All)) ;
            }

            return authorize;
        }
        protected override void HandleUnauthorizedRequest(AuthorizationContext filterContext)
        {
            if (!SessionPersister.isAuthenticated)
            {
                filterContext.Result = new RedirectToRouteResult(new System.Web.Routing.RouteValueDictionary(new { controller = "Account", action = "Login" }));
            }
            else {
                filterContext.Result = new RedirectToRouteResult(new System.Web.Routing.RouteValueDictionary(new { controller = "AccessDenied", action = "Denied" }));
            }
            
        }
    } 
}
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Chat.Models;
using Chat.Services.Contracts;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication;

namespace Chat.Controllers
{
    public class HomeController : Controller
    {
        public virtual IMembershipService MembershipService { get; set; }
        public HomeController(IMembershipService membershipService)
        {
            this.MembershipService = membershipService;
        }
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult About()
        {
            ViewData["Message"] = "Your application description page.";

            return View();
        }

        public IActionResult Contact()
        {
            ViewData["Message"] = "Your contact page.";

            return View();
        }

        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }

        
        public IActionResult Login()
        {
            return Redirect("/Home/Index");
        }


        [HttpPost]
        public IActionResult Login(string nick, string password)
        {
            try
            {
                var user = this.MembershipService.Login(nick, password);
                
                var claims = new List<Claim>
                {
                    new Claim(ClaimTypes.Name, user.Name),
                    new Claim("Token", user.Token),
                    new Claim("Id", user.Id.ToString())
                };

                var claimsIdentity = new ClaimsIdentity(
                    claims, CookieAuthenticationDefaults.AuthenticationScheme);

                var authProperties = new AuthenticationProperties
                {
                };

                HttpContext.SignInAsync(
                    CookieAuthenticationDefaults.AuthenticationScheme,
                    new ClaimsPrincipal(claimsIdentity),
                    authProperties);

                ViewData["user"] = user;
                
                return Redirect("/Chat/Index");
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Incorrect user or password";
                return View("Index");
            }
            
            
        }

        [HttpPost]
        public IActionResult Create(CreateUserViewModel userModel)
        {
            try
            {
                var user = this.MembershipService.Create(userModel.Name, userModel.Password, userModel.Age, userModel.City);
                
                return View("CreateSuccess");
            }
            catch (Exception ex)
            {
                return Error();
            }


        }


        [HttpGet]
        public async Task<IActionResult> LogOutAsync(string token)
        {
            this.MembershipService.LogOut(token);

            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);

            return Redirect("/Home/Index");

        }

    }
}

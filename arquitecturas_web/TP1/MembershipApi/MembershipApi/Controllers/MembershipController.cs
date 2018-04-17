using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MembershipApi.Services.Contracts;
using MembershipApi.Dtos.Request;

namespace MembershipApi.Controllers
{
    [Route("Membership")]
    public class MembershipController : Controller
    {
        public virtual IMembershipService MembershipService { get; set; }

        public MembershipController(IMembershipService membershipService)
        {
            this.MembershipService = membershipService;
        }
        /// <summary>
        /// Permite hacer el login de un usuario con el nickname correspondiente
        /// </summary>
        /// <param name="name">Contiene el nombre con el que se generará el usuario</param>        
        /// <returns></returns>
        [Route("Login")]
        [HttpPost]
        public IActionResult Login(string name)
        {
            try
            {
                var user = this.MembershipService.Login(name);
                return Ok(user.Token);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.Message);                
            }
        }

        /// <summary>
        /// Permite hacer el logout del usuario con el id y token correspondiente
        /// </summary>
        /// <param name="user">Contiene el usuario para el logout</param>        
        /// <returns></returns>
        [Route("Logout")]
        [HttpPost]
        public IActionResult Logout(UserRequest user)
        {
            try
            {
                this.MembershipService.LogOut(user.Id, user.Token);
                return Ok();
            }
            catch(Exception ex)
            {
                return BadRequest(ex.Message);
            }
        }

        
    }
}
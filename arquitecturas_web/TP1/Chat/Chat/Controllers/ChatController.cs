using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Chat.Services.Contracts;
using Chat.Models;
using System.Security.Claims;

namespace Chat.Controllers
{
    [Authorize]
    public class ChatController : Controller
    {
        public IRoomService RoomService { get; set; }
        public IMembershipService MembershipService { get; set; }
        public ChatController(IRoomService roomService, IMembershipService membershipService)
        {
            this.RoomService = roomService;
            this.MembershipService = membershipService;
        }
        public IActionResult Index()
        {

            var room = this.RoomService.GetRoom("General");
            return View(room);
        }


        public IActionResult PostMessage(MessageViewModel model)
        {
            var userName = HttpContext.User.Identity.Name;
            var user = this.MembershipService.GetUserByName(userName);

            this.RoomService.AddMessage(user, model.Content, model.roomName);

            var room = this.RoomService.GetRoom(model.roomName);

            return View("Index",room);

        }

        public JsonResult GetMessagesByRoom(string roomName)
        {
            var room = this.RoomService.GetRoom("General");            
            return Json(room.Messages);
        }


    }
}
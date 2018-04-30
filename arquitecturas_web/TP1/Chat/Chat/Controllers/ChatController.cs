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
            var userName = HttpContext.User.Identity.Name;
            var user = this.MembershipService.GetUserByName(userName);
            var chatViewModel = new ChatViewModel()
            {
                Messages = room.Messages,
                RoomName = room.Name,
                User = user,
                Users = room.Users
            };
            
            return View(chatViewModel);
        }


        public IActionResult PostMessage(MessageViewModel model)
        {
            var userName = HttpContext.User.Identity.Name;
            var user = this.MembershipService.GetUserByName(userName);

            this.RoomService.AddMessage(user, model.Content, model.roomName);

            var room = this.RoomService.GetRoom(model.roomName);

            var chatViewModel = new ChatViewModel()
            {
                Messages = room.Messages,
                RoomName = room.Name,
                User = user,
                Users = room.Users
            };

            return View("Index", chatViewModel);

        }

        public IActionResult CreateRoom(CreateRoomViewModel model)
        {
            var userName = HttpContext.User.Identity.Name;
            var user = this.MembershipService.GetUserByName(userName);

            var nroom = this.RoomService.CreateRoom(model.SenderName, model.ReceiverName);                

            var chatViewModel = new ChatViewModel()
            {
                Messages = nroom.Messages,
                RoomName = nroom.Name,
                User = user,
                Users = nroom.Users
            };

            return View("Index", chatViewModel);

        }


        public JsonResult GetMessagesByRoom(string roomName)
        {
            var room = this.RoomService.GetRoom("General");            
            return Json(room.Messages);
        }

        public JsonResult GetUsersByRoom(string roomName)
        {
            var room = this.RoomService.GetRoom("General");
            return Json(room.Users);
        }

    }
}
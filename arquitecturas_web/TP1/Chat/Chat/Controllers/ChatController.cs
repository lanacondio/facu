﻿using Microsoft.AspNetCore.Mvc;
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
            var rooms = this.RoomService.GetAll();

            var chatViewModel = new ChatViewModel()
            {
                Messages = room.Messages,
                RoomName = room.Name,
                User = user,
                Users = room.Users,
                Rooms = rooms
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

        public IActionResult CreateRoom(string name, string subject)
        {
            var userName = HttpContext.User.Identity.Name;
            var nroom = this.RoomService.CreateRoom(name, subject, userName);
            return Ok();
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

        public JsonResult GetRoom(string name)
        {
            var room = this.RoomService.GetRoom(name);
            return Json(room);
        }

    }
}
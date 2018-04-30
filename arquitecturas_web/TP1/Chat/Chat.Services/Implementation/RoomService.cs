using Chat.Services.Contracts;
using System;
using System.Collections.Generic;
using System.Text;
using Chat.DomainModel;
using Chat.Repository.Contracts;
using System.Linq;

namespace Chat.Services.Implementation
{
    public class RoomService : IRoomService
    {
        public virtual IRoomRepository RoomRepository {get;set;}
        public virtual IMembershipService UserService { get; set; }
        public RoomService(IRoomRepository roomRepository)
        {
            this.RoomRepository = roomRepository;
        }
        public void AddMessage(User user, string content, string roomName)
        {
            var message = new Message()
            {
                Content = content,
                Date = DateTime.Now,
                Sender = user,
                Id = Guid.NewGuid()
            };

            var room = this.GetRoom(roomName);

            if (room.Messages != null)
            {
                room.Messages.Add(message);
            }
            else
            {
                room.Messages = new List<Message>() { message };
            }
            

            this.RoomRepository.Update(room);
        }

        public void DeleteRoom(Guid id)
        {
            this.RoomRepository.Delete(id);
        }

        public Room GetRoom(string name)
        {
            var room = this.RoomRepository.GetAll().Where(x => x.Name == name).FirstOrDefault();
            return room;
        }

        public Room CreateRoom(string srcUser, string dstUser)
        {
            var srcUserObj = this.UserService.GetUserByName(srcUser);
            var dstUserObj = this.UserService.GetUserByName(dstUser);

            var room = new Room()
            {
                Messages = new List<Message>(),
                Name = srcUser+"-"+dstUser,
                Subject = srcUser + "-" + dstUser,
                Users = new List<string>() {srcUser, dstUser }
            };

            room.Id = this.RoomRepository.Insert(room);
            return room;
            
        }
    }
}

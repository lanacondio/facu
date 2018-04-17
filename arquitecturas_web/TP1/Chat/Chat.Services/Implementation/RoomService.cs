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

        
    }
}

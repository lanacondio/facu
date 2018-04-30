using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace Chat.Services.Contracts
{
    public interface IRoomService
    {
        Room GetRoom(string name);

        void AddMessage(User user, string content, string roomName);

        void DeleteRoom(Guid id);

        Room CreateRoom(string srcUser, string destUser);
    }
}

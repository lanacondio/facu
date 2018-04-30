using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Chat.Models
{
    public class ChatViewModel
    {
        public User User { get; set; }
        public IList<string> Users { get; set; }
        public string RoomName { get; set; }
        public IList<Message> Messages { get; set; }
    }
}

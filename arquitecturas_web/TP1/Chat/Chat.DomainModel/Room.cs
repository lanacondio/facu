using System;
using System.Collections;
using System.Collections.Generic;

namespace Chat.DomainModel
{
    public class Room
    {        
        public virtual Guid Id { get; set; }
        public virtual IList<string> Users { get; set; }
        public virtual string Name { get; set; }
        public virtual string Subject { get; set; }
        public virtual IList<Message> Messages { get; set; }
    }
}

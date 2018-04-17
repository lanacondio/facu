using System;

namespace Chat.DomainModel
{
    public class Message
    {
        public virtual User Sender { get; set; }
        public virtual Guid Id { get; set; }    
        public virtual DateTime Date { get; set; }
        public virtual string Content { get; set; }

    }
}

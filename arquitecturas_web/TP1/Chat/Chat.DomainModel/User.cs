using System;

namespace Chat.DomainModel
{
    public class User
    {
        public virtual Guid Id { get; set; }
        public virtual string Token { get; set; }
        public virtual string Name { get; set; }
        public virtual int Age { get; set; }
        public virtual string City { get; set; }
        public virtual string Password { get; set; }
    }
}

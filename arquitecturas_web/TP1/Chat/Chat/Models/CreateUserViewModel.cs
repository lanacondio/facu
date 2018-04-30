using Chat.DomainModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Chat.Models
{
    public class CreateUserViewModel
    {
        public virtual string Name { get; set; }
        public virtual int Age { get; set; }
        public virtual string City { get; set; }
        public virtual string Password { get; set; }
    }
}

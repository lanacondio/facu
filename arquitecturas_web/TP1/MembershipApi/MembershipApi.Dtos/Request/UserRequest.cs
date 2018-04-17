using System;
using System.Collections.Generic;
using System.Text;

namespace MembershipApi.Dtos.Request
{
    public class UserRequest
    {
        public virtual Guid Id { get; set; }
        public virtual string Token { get; set; }
        public virtual string Name { get; set; }
    }
}
